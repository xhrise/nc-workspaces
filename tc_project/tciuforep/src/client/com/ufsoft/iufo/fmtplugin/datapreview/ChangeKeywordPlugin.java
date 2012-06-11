package com.ufsoft.iufo.fmtplugin.datapreview;

import java.awt.event.ActionEvent;
import java.util.Arrays;

import nc.vo.iufo.keydef.KeyVO;

import com.ufida.web.util.WebGlobalValue;
import com.ufida.zior.plugin.AbstractPlugin;
import com.ufida.zior.plugin.IPluginAction;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.key.KeywordModel;
import com.ufsoft.iufo.inputplugin.biz.file.ChangeKeywordsData;
import com.ufsoft.iufo.inputplugin.biz.file.ChangeKeywordsDlg;
import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.iuforeport.tableinput.TableInputOperUtil;
import com.ufsoft.iuforeport.tableinput.applet.IRepDataParam;
import com.ufsoft.iuforeport.tableinput.applet.TableInputTransObj;
import com.ufsoft.report.AbstractRepPluginAction;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.MultiLang;

public class ChangeKeywordPlugin extends AbstractPlugin {

	private static final String GROUP = "keyword";

	

	@Override
	protected IPluginAction[] createActions() {
		return new IPluginAction[] { new ChangeKeyWordAction() };
	}

	@Override
	public void shutdown() {
	}

	@Override
	public void startup() {
	}

	private class ChangeKeyWordAction extends AbstractRepPluginAction {

		@Override
		public void execute(ActionEvent e) {
			// 得到切换关键字数据对象
			KeyVO[] keyVOs = KeywordModel.getInstance(getCellsModel())
					.getMainKeyVOs();
			Arrays.sort(keyVOs);
			String strOrgPK = (String) getContext().getAttribute(
					IUfoContextKey.ORG_PK);

			ChangeKeywordsData[] changeKeywordsDatas = (ChangeKeywordsData[]) TableInputOperUtil
					.geneChangeKeywordsDatas(keyVOs, null, false, strOrgPK);
			IRepDataParam repDataParam = ((TableInputTransObj) getContext()
					.getAttribute(IUfoContextKey.TABLE_INPUT_TRANS_OBJ))
					.getRepDataParam();
			repDataParam.setOperType(WebGlobalValue.MenuType_RELEASE_PREVIEW);

			// 打开选择报表的窗口
			ChangeKeywordsDlg dlg = new ChangeKeywordsDlg(getCellsPane(),
					changeKeywordsDatas);
			dlg.setVisible(true);
			if (dlg.getResult() == UfoDialog.ID_OK) {
				String[] keyValues = dlg.getInputKeyValues();

				// 报表预览状态下切换关键字时，加入报表关键字定义
				Object[] otherParams = null;
				if (keyValues == null || keyValues.length == 0) {
					otherParams = new Object[1];
					otherParams[0] = keyVOs;
				} else {
					otherParams = new Object[keyValues.length + 1];
					System
							.arraycopy(keyValues, 0, otherParams, 0,
									keyValues.length);
					otherParams[keyValues.length] = keyVOs;
				}

//				IInputBizOper inputMenuOper = new InputChangeKeywordsOper(
//						container, otherParams);
//				inputMenuOper
//						.performBizTask(ITableInputMenuType.BIZ_TYPE_CHANGEKEYWORDDSUBMIT);
			}

			

		}

		@Override
		public IPluginActionDescriptor getPluginActionDescriptor() {
			PluginActionDescriptor descriptor = new PluginActionDescriptor();
			descriptor.setGroupPaths(MultiLang.getString("file"), GROUP);// "数据"
			descriptor.setName(MultiLangInput.getString("uiufotableinput0002"));// "切换关键字";
			descriptor.setExtensionPoints(new XPOINT[] { XPOINT.MENU });
			return descriptor;
		}
	}
}
