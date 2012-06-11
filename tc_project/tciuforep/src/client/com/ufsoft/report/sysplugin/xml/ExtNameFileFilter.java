package com.ufsoft.report.sysplugin.xml;

import java.io.File;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileView;

public class ExtNameFileFilter extends FileFilter{
	private String _extendName;

	public ExtNameFileFilter(String extendName) {
		super();
		_extendName = extendName;
	}

	/**
	 * Whether the given file is accepted by this filter.
	 */
	public boolean accept(java.io.File f) {
		if (f.isDirectory())
			return true;

		String extension = null;
		String name = f.getName();
		int pos = name.lastIndexOf('.');
		if (pos > 0 && pos < name.length() - 1)
			extension = name.substring(pos + 1);

		if (extension != null && extension.equalsIgnoreCase(_extendName))
			return true;

		return false;
	}

	/**
	 * The description of this filter.
	 * @see FileView#getName
	 */
	public String getDescription() {
		return _extendName+" Files";
	}
	public File getModifiedFile(File file){
		String pathName = file.getPath();
		int pos = pathName.lastIndexOf('.');			
		if (pos > 0) {
			String extName = pathName.substring(pos + 1);
			if(extName.equalsIgnoreCase(_extendName)){
				return file;
			}
		} 
		pathName += ".";
		pathName += _extendName;
		return new File(pathName);
	}
}
