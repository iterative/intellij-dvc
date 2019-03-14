// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See License.txt in the project root.

// Original file copied from:
// https://github.com/Microsoft/azure-devops-intellij/blob/master/plugin/src/com/microsoft/alm/plugin/external/ToolRunner.java
// Modified for DVC purposes

package com.davidprihoda.dvc;

import com.google.common.util.concurrent.SettableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * This class is used to run an external command line tool and listen to the output.
 * Use the nested class ArgumentBuilder to build up the list of arguments and the nested interface Listener to
 * get callbacks for processing output, exceptions and completion events.
 */
public class ToolRunner {
    private static final Logger logger = LoggerFactory.getLogger(ToolRunner.class);

    private Process toolProcess;
    private final String workingDirectory;
    private final String[] commandLine;
    private StreamProcessor standardErrorProcessor;
    private StreamProcessor standardOutProcessor;
    private ProcessWaiter processWaiter;
    private ListenerProxy listenerProxy;

    /**
     * Implement this class to get callbacks on events triggered by the ToolRunner.
     */
    public interface Listener {
        void processStandardOutput(final String line);

        void processStandardError(final String line);

        void processException(final Throwable throwable);

        void completed(int returnCode);
    }

    public ToolRunner(final String workingDirectory, String... commandLine) {
        this.workingDirectory = workingDirectory;
        this.commandLine = commandLine;
        this.listenerProxy = new ListenerProxy();
    }

    public String[] getCommandLine() {
        return commandLine;
    }

    public void addListener(final Listener listener) {
        listenerProxy.addListener(listener);
    }

    public Process start() {
        logger.info("ToolRunner.start: toolLocation = " + String.join(" ", commandLine));
        logger.info("ToolRunner.start: workingDirectory = " + workingDirectory);

        try {
            SettableFuture<Boolean> standardOutputFlushed = SettableFuture.create();
            SettableFuture<Boolean> standardErrorFlushed = SettableFuture.create();

            // Create and start the process from the tool location and working directory
            // (it is perfectly okay if working directly is null here. null == not set)
            toolProcess = Runtime.getRuntime().exec(commandLine, null, new File(workingDirectory));
            final InputStream stderr = toolProcess.getErrorStream();
            final InputStream stdout = toolProcess.getInputStream();
            standardErrorProcessor = new StreamProcessor(stderr, true, listenerProxy, standardErrorFlushed);
            standardErrorProcessor.start();
            standardOutProcessor = new StreamProcessor(stdout, false, listenerProxy, standardOutputFlushed);
            standardOutProcessor.start();
            processWaiter = new ProcessWaiter(toolProcess, listenerProxy, standardErrorFlushed, standardOutputFlushed);
            processWaiter.start();
            return toolProcess;
        } catch (final IOException e) {
            logger.warn("Failed to start tool process or redirect output.", e);
            listenerProxy.processException(e);
        }

        return null;
    }

    /**
     * Call the dispose method to make sure all threads are cleaned up and disposed of properly.
     */
    public void dispose() {
        try {
            if (processWaiter != null) {
                processWaiter.cleanUp();
                processWaiter = null;
            }
            if (standardErrorProcessor != null) {
                standardErrorProcessor.cleanUp();
                standardErrorProcessor = null;
            }
            if (standardOutProcessor != null) {
                standardOutProcessor.cleanUp();
                standardOutProcessor = null;
            }
        } catch (final InterruptedException e) {
            logger.warn("Failed to dispose ToolRunner.", e);
        }
    }

    private static class ListenerProxy implements Listener {
        private final List<Listener> listeners = new ArrayList<Listener>(2);

        public ListenerProxy() {
        }

        public void addListener(final Listener listener) {
            listeners.add(listener);
        }

        @Override
        public void processStandardOutput(final String line) {
            for (final Listener l : listeners) {
                l.processStandardOutput(line);
            }
        }

        @Override
        public void processStandardError(final String line) {
            for (final Listener l : listeners) {
                l.processStandardError(line);
            }
        }

        @Override
        public void processException(final Throwable throwable) {
            for (final Listener l : listeners) {
                l.processException(throwable);
            }
        }

        @Override
        public void completed(final int returnCode) {
            for (final Listener l : listeners) {
                l.completed(returnCode);
            }
        }
    }

    /**
     * This internal class is used to manage the thread that waits on the process to finish.
     * It takes in the process to wait on and the listener to issue callbacks to.
     */
    private static class ProcessWaiter extends Thread {
        private boolean processRunning;
        private final Process process;
        private final Listener listener;
        private final SettableFuture<Boolean> errorsFlushed;
        private final SettableFuture<Boolean> outputFlushed;

        public ProcessWaiter(final Process process, final Listener listener, final SettableFuture<Boolean> errorsFlushed, final SettableFuture<Boolean> outputFlushed) {
            this.process = process;
            this.listener = listener;
            this.errorsFlushed = errorsFlushed;
            this.outputFlushed = outputFlushed;
        }

        @Override
        public void run() {
            // Don't let exceptions escape from this top level method
            try {
                processRunning = true;
                // Wait for the process to finish
                process.waitFor();
                // Wait for the output streams to be flushed
                errorsFlushed.get(30, TimeUnit.SECONDS);
                outputFlushed.get(30, TimeUnit.SECONDS);
                // Clear the member variable so we don't try to destroy the process later
                processRunning = false;
                // Call the completed event on the listener with the exit code
                listener.completed(process.exitValue());
            } catch (Throwable e) {
                logger.warn("Failed to wait for process exit.", e);
                listener.processException(e);
            }
        }

        /**
         * This method forces the thread to end by interrupting it and joining with the calling thread.
         *
         * @throws InterruptedException
         */
        public void cleanUp() throws InterruptedException {
            if (processRunning) {
                try {
                    process.destroy();
                } catch (final Throwable t) {
                    logger.warn("Failed to destroy process.", t);
                }
            }
            this.interrupt();
            this.join();
        }
    }

    /**
     * This internal class is used to manage the threads that receive the output from the process.
     * One thread is created to listen for standard output and one is created to listen to standard error.
     * The constructor takes in the stream to listen to, what kind of stream it is, and the listener to
     * issue callbacks to.
     */
    private static class StreamProcessor extends Thread {

        private final InputStream stream;
        private final boolean isStandardError;
        private final Listener listener;
        private final SettableFuture<Boolean> flushed;

        public StreamProcessor(final InputStream stream, final boolean isStandardError, final Listener listener, final SettableFuture<Boolean> flushed) {
            this.stream = stream;
            this.isStandardError = isStandardError;
            this.listener = listener;
            this.flushed = flushed;
        }

        @Override
        public void run() {
            BufferedReader bufferedReader = null;

            // Don't let exceptions escape from this top level method
            try {
                // Create a buffered reader so that we can process the output one line at a time
                bufferedReader = new BufferedReader(new InputStreamReader(stream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    // Call the appropriate event with the line that was read
                    if (isStandardError) {
                        listener.processStandardError(line);
                    } else {
                        listener.processStandardOutput(line);
                    }
                }
            } catch (Throwable e) {
                logger.warn("Failed to process output.", e);
                listener.processException(e);
            } finally {
                // Don't let exceptions escape from this top level method
                try {
                    // Make sure to close the buffered reader
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                    flushed.set(true);
                } catch (Throwable e) {
                    logger.warn("Failed to close buffer.", e);
                    listener.processException(e);
                }
            }
        }

        /**
         * This method forces the thread to end by interrupting it and joining with the calling thread.
         *
         * @throws InterruptedException
         */
        public void cleanUp() throws InterruptedException {
            this.interrupt();
            this.join();
        }
    }
}