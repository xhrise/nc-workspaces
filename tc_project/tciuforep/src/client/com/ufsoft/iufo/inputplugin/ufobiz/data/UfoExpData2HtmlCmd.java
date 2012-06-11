package com.ufsoft.iufo.inputplugin.ufobiz.data;

import java.awt.Component;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.swing.JFileChooser;

import nc.ui.iufo.input.control.RepDataControler;
import nc.ui.iufo.input.edit.RepDataEditor;
import nc.ui.pub.beans.UIFileChooser;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.console.ActionHandler;
import com.ufsoft.iufo.inputplugin.ufobiz.AbsUfoBizCmd;
import com.ufsoft.report.sysplugin.xml.ExtNameFileFilter;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.iufo.resource.StringResource;

public class UfoExpData2HtmlCmd extends AbsUfoBizCmd {
	public UfoExpData2HtmlCmd(RepDataEditor editor){
		super(editor);
	}
	
	/**
	 * @i18n miufohbbb00119=导出HTML失败
	 */
	protected void executeIUFOBizCmd(RepDataEditor editor, Object[] params) {
		try{
	    	if (!AbsUfoBizCmd.stopCellEditing(editor))
	    		return;
	    	
			String strHTML=(String)ActionHandler.exec("com.ufsoft.iuforeport.repdatainput.TableInputActionHandler", "exportData2Html",
				new Object[]{editor.getRepDataParam(),RepDataControler.getInstance(editor.getMainboard()).getLoginEnv(editor.getMainboard())});
			saveHTML2Local(editor,strHTML,"htm");
		}catch(Exception e){
			AppDebug.debug(e);
			UfoPublic.sendErrorMessage(StringResource.getStringResource("miufohbbb00119")+e.getMessage(),editor,null);
		}
	}
	
	/**
	 * 弹出文件选择界面，保存生成的HTML内容到用户本地文件
	 * @param parent
	 * @param strHTMLContent
	 * @param strFilePostfix
	 */
	public static void saveHTML2Local(Component parent, String strHTMLContent,
			String strFilePostfix) {
		if (parent == null || strHTMLContent == null) {
			return;
		}

		if (strFilePostfix == null) {
			strFilePostfix = "htm";
		}
		JFileChooser chooser = new UIFileChooser();
		ExtNameFileFilter xf = new ExtNameFileFilter(strFilePostfix);
		chooser.setFileFilter(xf);
		chooser.setMultiSelectionEnabled(false);
		int returnVal = chooser.showSaveDialog(parent);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			file = xf.getModifiedFile(file);
			PrintWriter writer = null;
			try {
				writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
				writer.write(strHTMLContent);
			} catch (FileNotFoundException e) {
				AppDebug.debug(e);//@devTools                 AppDebug.debug(e);
			} catch (UnsupportedEncodingException e) {
				AppDebug.debug(e);//@devTools                 e.printStackTrace(System.out);
			} finally {
				if (writer != null) {
					writer.close();
				}
			}
		}
	}
}
 