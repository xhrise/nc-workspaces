package com.ufsoft.report.sysplugin.fill;



import java.awt.Component;

import javax.swing.KeyStroke;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.ICommandExt;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPlugIn;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.SelectModel;

/**
 * <pre>
 * </pre>//ERROR
 * @author zzl
 * @version 5.0
 * Create on 2004-10-25
 */
public class FillDescriptor extends AbstractPlugDes {
	/**
     * @param plugin
     */
    public FillDescriptor(IPlugIn plugin) {
        super(plugin);
    }
	/* @see com.ufsoft.report.plugin.IPluginDescriptor#getName()
	 */
	public String getName() {
		return MultiLang.getString("uiuforep0000500");//"野割";
	}

	/* @see com.ufsoft.report.plugin.IPluginDescriptor#getNote()
	 */
	public String getNote() {
		return MultiLang.getString("uiuforep0000500");//"野割";
	}

	/* @see com.ufsoft.report.plugin.IPluginDescriptor#getPluginPrerequisites()
	 */
	public String[] getPluginPrerequisites() {
		return null;
	}

	/* @see com.ufsoft.report.plugin.IPluginDescriptor#getExtensions()
	 */
	public IExtension[] createExtensions() {
		return new IExtension[]{
				new AbsActionExt(){
					public UfoCommand getCommand() {
						return null;
					}
					@Override
					public Object[] getParams(UfoReport container) {
						return null;
					}
					@Override
					public ActionUIDes[] getUIDesArr() {
						ActionUIDes uiDes = new ActionUIDes();
						uiDes.setName(MultiLang.getString("uiuforep0000500"));
						uiDes.setPaths(new String[]{MultiLang.getString("edit")});
						uiDes.setDirectory(true);
						uiDes.setGroup("insertAndFill");
						return new ActionUIDes[]{uiDes};
					}					
				},
				new FillToDownExt(getReport()),
				new FillToRightExt(getReport()),
				new FillToUpExt(getReport()),
				new FillToLeftExt(getReport())
		};
	}

	/* @see com.ufsoft.report.plugin.IPluginDescriptor#getHelpNode()
	 */
	public String getHelpNode() {
		return null;
	}
}
