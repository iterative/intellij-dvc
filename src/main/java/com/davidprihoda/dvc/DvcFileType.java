package com.davidprihoda.dvc;

import com.intellij.openapi.fileTypes.ExactFileNameMatcher;
import com.intellij.openapi.fileTypes.ExtensionFileNameMatcher;
import com.intellij.openapi.fileTypes.FileNameMatcher;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.YAMLLanguage;

import javax.swing.*;

public class DvcFileType extends LanguageFileType {
    public static final DvcFileType INSTANCE = new DvcFileType();
	public static final String DEFAULT_EXTENSION = "dvc";
	public static final FileNameMatcher EXACT_FILENAME_MATCHER = new ExactFileNameMatcher("Dvcfile");
	public static final FileNameMatcher EXTENSION_MATCHER = new ExtensionFileNameMatcher("dvc");

	protected DvcFileType() {
		super(YAMLLanguage.INSTANCE);
	}

	@NotNull
	public String getName() {
		return "DVC";
	}

	@NotNull
	public String getDescription() {
		return "DVC file";
	}

	@NotNull
	public String getDefaultExtension() {
		return DEFAULT_EXTENSION;
	}

	@NotNull
	public Icon getIcon() {
		return DvcIcons.FILETYPE_ICON;
	}
}
