package com.ufsoft.iufo.fmtplugin.datapreview;

import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.iufo.input.table.IufoRefData;
import nc.ui.iufo.input.table.TableInputParam;
import nc.vo.iufo.user.UserInfoVO;

import com.ufida.dataset.IContext;
import com.ufsoft.iufo.fmtplugin.datastate.CellsModelOperator;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.inputplugin.inputcore.InputCorePlugin;
import com.ufsoft.iufo.inputplugin.inputcore.RefData;
import com.ufsoft.iufo.inputplugin.key.KeyInputPlugin;
import com.ufsoft.iufo.inputplugin.measure.MeasureFmt;
import com.ufsoft.iufo.inputplugin.measure.MeasureInputPlugin;
import com.ufsoft.iufo.inputplugin.ufobiz.UfoInputFilePlugin;
import com.ufsoft.iufo.inputplugin.ufodynarea.UfoDynAreaInputPlugin;
import com.ufsoft.iuforeport.repdatainput.LoginEnvVO;
import com.ufsoft.iuforeport.tableinput.applet.IRepDataParam;
import com.ufsoft.iuforeport.tableinput.applet.RepDataParam;
import com.ufsoft.iuforeport.tableinput.applet.TableDataInputAuth;
import com.ufsoft.report.ReportContextKey;
import com.ufsoft.report.ReportDesigner;
import com.ufsoft.report.sysplugin.editplugin.RepEditPlugin;
import com.ufsoft.report.sysplugin.findreplace.FindReplacePlugin;
import com.ufsoft.report.sysplugin.headersize.HeaderSizePlugin;
import com.ufsoft.report.sysplugin.help.HelpPlugin;
import com.ufsoft.report.sysplugin.print.PrintPlugin;
import com.ufsoft.report.sysplugin.repheaderlock.RepHeaderLockPlugin;
import com.ufsoft.report.sysplugin.repstyle.RepStylePlugin;
import com.ufsoft.table.CellsModel;
/**
 * 
 * @author zhaopq
 * @created at 2009-3-21,����09:13:58
 *
 */
public class ReportDataPreviewDesigner extends ReportDesigner {

	private static final long serialVersionUID = 4343028281355449701L;

	private static final String FORMATMODEL_IN_SHOWMODEL_WHEN_DATAPROCESS = "formatModel_in_showModel_when_dataProcess";

	@Override
	public String[] createPluginList() {
		return new String[] { /*DSInfoSetPlugin.class.getName(), ����������κ�Ƶ�file�����ȥ��*/
				UfoInputFilePlugin.class.getName(),
				InputCorePlugin.class.getName(),
				KeyInputPlugin.class.getName(),
				MeasureInputPlugin.class.getName(),
				UfoDynAreaInputPlugin.class.getName(),
				PrintPlugin.class.getName(),// ��ӡ��ز˵���ҳ�����ã���ӡԤ������ӡ
				// �༭
				RepEditPlugin.class.getName(),// �༭��ġ����С����ơ�ճ����ɾ���������
				FindReplacePlugin.class.getName(),// ���ң��滻
				// #��ʽ
				RepStylePlugin.class.getName(),//��ʾ���
				HeaderSizePlugin.class.getName(), // �иߣ��п�
				RepHeaderLockPlugin.class.getName(),// ��//���ᴰ�ڣ�ȡ������
//				CellPostilInputPlugin.class.getName(),

				// #����
				HelpPlugin.class.getName(),// ����������
				CalPlugin.class.getName(), ChangeKeyWordPlugin2.class.getName()

		};
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub

	}

	@Override
	public void startup() {
		super.startup();

		// ׼���������¼��Ļ���,
		RefData.setProxy(new IufoRefData());
	}

	protected void initContext() {
		super.initContext();
		getContext().setAttribute(ReportContextKey.OPERATION_STATE, ReportContextKey.OPERATION_INPUT);
		
	}

	public void initCellsModel(CellsModel cm,String strReportPK) {
		CellsModel cellModel = (CellsModel) cm.clone();
		cellModel = CellsModelOperator.convertDataModelToLWModel(cellModel);
		cellModel = CellsModelOperator.convertDynAreaModelToLWModel(cellModel,
				true);
		cellModel.setClientProperty(FORMATMODEL_IN_SHOWMODEL_WHEN_DATAPROCESS,
				cellModel);
//		cellModel.setCellsAuth(new TableInputAuth(getTable()));
		setCellsModel(cellModel);
		getContext().setAttribute(IUfoContextKey.DATA_RIGHT,IUfoContextKey.RIGHT_DATA_WRITE);
		cellModel.setCellsAuth(new TableDataInputAuth(this));
		
		IRepDataParam param=new RepDataParam();
		param.setOperType(TableInputParam.OPERTYPE_REPDATA_INPUT);
		String strUserID=(String)getMainboard().getContext().getAttribute(IUfoContextKey.CUR_USER_ID);
		param.setOperUserPK(strUserID);
		param.setReportPK(strReportPK);
		UserInfoVO userInfo=IUFOUICacheManager.getSingleton().getUserCache().getUserById(strUserID);
		param.setOperUnitPK(userInfo.getUnitId());
		param.setOrgPK((String)getMainboard().getContext().getAttribute(IUfoContextKey.ORG_PK));
		getContext().setAttribute(IUfoContextKey.REP_DATA_PARAM, param);
		
		LoginEnvVO loginEnv=new LoginEnvVO();
		IContext context=getMainboard().getContext();
		loginEnv.setCurLoginDate((String)context.getAttribute(IUfoContextKey.LOGIN_DATE));
		loginEnv.setLangCode((String)context.getAttribute(IUfoContextKey.CURRENT_LANG));
		loginEnv.setLoginUnit(userInfo.getUnitId());
		getContext().setAttribute(IUfoContextKey.LOGIN_ENV,loginEnv);
		
		MeasureFmt.setCanInput(true);
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	protected boolean save() {
		// TODO Auto-generated method stub
		return false;
	}
	
	

}
