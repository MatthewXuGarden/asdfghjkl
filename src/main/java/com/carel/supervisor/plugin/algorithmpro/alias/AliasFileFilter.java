package com.carel.supervisor.plugin.algorithmpro.alias;

import java.io.File;

public class AliasFileFilter implements java.io.FileFilter {

	public static String EXT = "alias";

	public boolean accept(File f) {
		String name = f.getName();
		if (name != null && name.endsWith(EXT))
			return true;
		else
			return false;
	}
}
