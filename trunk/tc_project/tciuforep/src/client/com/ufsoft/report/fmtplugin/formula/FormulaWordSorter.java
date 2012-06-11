package com.ufsoft.report.fmtplugin.formula;

import nc.vo.pub.beans.editor.IWordType;
import nc.vo.pub.beans.editor.sorter.JavaWordSorter;

/**
 * ¹«Ê½¹Ø¼ü×Ö
 * 
 * @author wangyga
 * 
 */
public class FormulaWordSorter extends JavaWordSorter {
	private String[] keyWordArray = null;

	public FormulaWordSorter() {
		super();
	}

	public int getWordType(String word) {
		if (word.startsWith("'") && word.endsWith("'")) {
			return IWordType.STRING;
		}
		if (findWord(word) >= 0) {
			return IWordType.KEYWORD;
		}
		return super.getWordType(word);
	}

	protected void initWordSet() {
		super.initWordSet();
		deliminiters.remove("'");
		deliminiters.remove("-");
		deliminiters.remove(">");
	}

	private int findWord(String word) {
		int iLen = getKeyWordArray().length;
		for (int i = 0; i < iLen; i++) {
			if (getKeyWordArray()[i].equalsIgnoreCase(word))
				return i;
		}
		return -1;
	}

	public String[] getKeyWordArray() {
		if (keyWordArray == null) {
			keyWordArray = new String[0];
		}
		return keyWordArray;
	}

	public void setKeyWordArray(String[] keyWordArray) {
		this.keyWordArray = keyWordArray;
	}

}

