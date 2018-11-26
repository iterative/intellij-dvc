package com.davidprihoda.dvc;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import org.jetbrains.annotations.NotNull;

public class DvcFileTypeFactory extends FileTypeFactory {
	private static final Logger LOG = Logger.getInstance(DvcReferenceContributor.class);

	public void createFileTypes(@NotNull FileTypeConsumer consumer) {
    	LOG.info("Adding DVC type: "+DvcFileType.INSTANCE);
		consumer.consume(DvcFileType.INSTANCE, DvcFileType.EXACT_FILENAME_MATCHER, DvcFileType.EXTENSION_MATCHER);
	}
}
