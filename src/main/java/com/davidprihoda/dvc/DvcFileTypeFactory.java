package com.davidprihoda.dvc;

import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import org.jetbrains.annotations.NotNull;

public class DvcFileTypeFactory extends FileTypeFactory {
	public void createFileTypes(@NotNull FileTypeConsumer consumer) {
		consumer.consume(DvcFileType.INSTANCE, DvcFileType.EXACT_FILENAME_MATCHER, DvcFileType.EXTENSION_MATCHER);
	}
}
