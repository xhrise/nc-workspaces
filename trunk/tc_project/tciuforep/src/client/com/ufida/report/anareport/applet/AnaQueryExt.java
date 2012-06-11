package com.ufida.report.anareport.applet;

import java.awt.Container;
import java.util.Vector;

import javax.swing.JOptionPane;

import nc.pub.iufo.cache.KeyGroupCache;
import nc.pub.iufo.cache.ReportCache;
import nc.pub.iufo.cache.TaskCache;
import nc.pub.iufo.cache.base.UnitCache;
import nc.ui.hbbb.pub.HBBBSysParaUtil;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.iufo.datasource.DataSourceBO_Client;
import nc.vo.iufo.datasource.DataSourceVO;
import nc.vo.iufo.keydef.KeyGroupVO;
import nc.vo.iufo.measure.MeasureVO;
import nc.vo.iufo.measure.UnitExInfoVO;
import nc.vo.iufo.task.TaskVO;
import nc.vo.iufo.unit.UnitInfoVO;
import nc.vo.iuforeport.rep.ReportVO;

import com.ufida.bi.base.BIException;
import com.ufida.dataset.Context;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.report.rep.model.IBIContextKey;
import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.freequery.MultiMeasureRefDlg;
import com.ufsoft.iufo.inputplugin.biz.file.GeneralQueryUtil;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iuforeport.freequery.FreeQueryContextVO;
import com.ufsoft.iuforeport.freequery.FreeQueryTranceObj;
import com.ufsoft.iuforeport.tableinput.applet.DataSourceInfo;
import com.ufsoft.iuforeport.tableinput.applet.IRepDataParam;
import com.ufsoft.iuforeport.tableinput.applet.TableInputContextVO;
import com.ufsoft.iuforeport.tableinput.applet.TableInputTransObj;
import com.ufsoft.report.ContextVO;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellPosition;

public class AnaQueryExt extends AbsActionExt implements IUfoContextKey{

	/**
	 * @i18n miufo00110=���ɱ���
	 */
	public static final String STRING_ID_ANAREPORT = "miufo00110";
	/**
	 * @i18n miufo00111=��ѯֱ���¼�����
	 */
	public static final String STRING_ID_NEXTCORP="miufo00111";
	
	// ��ѯ�¼�����,��ѯ��������,���ɲ�ѯ
	public static final String[] MENU_NAMES = new String[] { "miufofreequery0002", "miufofreequery0003", "miufofreequery0001", "miufofreequery0004",STRING_ID_NEXTCORP};

	private int m_queryType;

	private UfoReport m_report;// ������

	public AnaQueryExt(int queryType, UfoReport report) {
		m_queryType = queryType;
		m_report = report;
	}

	@Override
	public UfoCommand getCommand() {
		return new AnaQueryCmd(m_report);
	}

	/**
	 * @i18n miufo00113=��ѡ��ָ��
	 */
	@Override
	public Object[] getParams(UfoReport container) {
		// 1�������µĻ�����Ϣ
		try{
		FreeQueryContextVO context = createFreeQueryContextVO(container.getContextVo());
		String strReportPK = context.getIufoRepID();
		// 2����ȡ��ǰѡ�������е�ָ��
		MeasureVO[] selMeasures = getMeasureVOByPos(container, strReportPK);
		UnitExInfoVO[] selUnitInfos = null;
		// 3����û��ѡ��ָ�꣬�Ӳ�����ѡ��
		if (selMeasures == null) {
			Object[] obj = getMeasureVOByRef(container, context, null);
			if(obj != null){
				selMeasures = (MeasureVO[])obj[0];
				selUnitInfos = (UnitExInfoVO[])obj[1];
			}
		}
		if(selMeasures==null||selMeasures.length==0){
			return null;
		}else
            return new Object[] { context, selMeasures, selUnitInfos, m_queryType };
		}catch(BIException ex){
			JOptionPane.showMessageDialog(container, ex.getMessage());
		}
		return null;
	}

	/**
	 * @i18n miufo00110=���ɱ���
	 */
	@Override
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
		uiDes.setName(MultiLangInput.getString(MENU_NAMES[m_queryType]));
		uiDes.setPaths(new String[] {StringResource.getStringResource("miufo00110")});
		return new ActionUIDes[] { uiDes };
	}

	/**
	 * @i18n uiuforep00130=��֧�ֶԴ˰汾���ݵ����ɲ�ѯ��
	 */
	protected FreeQueryContextVO createFreeQueryContextVO(Context contextVO) {
		String loginLevelCode=(String)contextVO.getAttribute(IUfoContextKey.LOGIN_UNIT_LEVELCODE);
		String levelCode=(String)contextVO.getAttribute(IUfoContextKey.ORG_PK);;
		String loginUnitId=(String)contextVO.getAttribute(IUfoContextKey.LOGIN_UNIT_ID);
		if(loginUnitId==null){
			loginUnitId=(String)contextVO.getAttribute(IUfoContextKey.CUR_UNIT_ID);
		}
		if(loginLevelCode==null&&loginUnitId!=null){
			UnitCache unitCache=IUFOUICacheManager.getSingleton().getUnitCache();
			UnitInfoVO unitInfo=unitCache.getUnitInfoByPK(loginUnitId);
			if(unitInfo!=null){
				loginLevelCode=unitInfo.getPropValue(levelCode);
			}
			if(loginLevelCode!=null){
				contextVO.setAttribute(IUfoContextKey.LOGIN_UNIT_LEVELCODE, loginLevelCode);	
			}
		}
			FreeQueryContextVO freeContext = new FreeQueryContextVO((ContextVO)contextVO);
           
			return freeContext;
	}

	private MeasureVO[] getMeasureVOByPos(UfoReport report, String strCurrRepPK) {
		CellPosition[] pos = report.getCellsModel().getSelectModel().getSelectedCells();
		Vector<MeasureVO> vec = new Vector<MeasureVO>();
		if (pos != null) {
			DynAreaModel dynModel = DynAreaModel.getInstance(report.getCellsModel());
			for (int i = 0; i < pos.length; i++) {
				CellPosition formatPos = DynAreaCell.getFormatArea(pos[i], report.getCellsModel()).getStart();
 				MeasureVO mVO = dynModel.getMeasureModel().getMeasureVOByPos(formatPos);
				if (mVO != null) {
					mVO.setSelReportPK(strCurrRepPK);
					if(!vec.contains(mVO) && isSameKeyGroup(vec, mVO))
						vec.addElement(mVO);
				}
			}
		}
		if (vec.size() > 0)
			return vec.toArray(new MeasureVO[0]);
		return null;
	}
	private boolean isSameKeyGroup(Vector<MeasureVO> vec, MeasureVO mVO){
		if(vec.size() == 0)
			return true;
		MeasureVO m0 = vec.get(0);
		return m0.getKeyCombPK().equals(mVO.getKeyCombPK());
	}

	private Object[] getMeasureVOByRef(Container parent, FreeQueryContextVO context, MeasureVO[] selMeasures) {
		ReportCache repCache = IUFOUICacheManager.getSingleton().getReportCache();
		ReportVO repVO = repCache.getByPK(context.getIufoRepID());
		if (repVO == null) {// ��������ı����Ա㵯������
			repVO = new ReportVO();
			repVO.setModel(false);
		}
		// �ӱ������л�ȡ����
		TaskCache taskCache = IUFOUICacheManager.getSingleton().getTaskCache();
		TaskVO taskVO = taskCache.getTaskVO(context.getTaskID());

		String strKeyGroupPk = taskVO.getKeyGroupId();
		context.setKeyGroupID(strKeyGroupPk);

		KeyGroupCache keyGroupCache = IUFOUICacheManager.getSingleton().getKeyGroupCache();
		KeyGroupVO keyGroupVO = keyGroupCache.getByPK(context.getKeyGroupID());

		MultiMeasureRefDlg refDialog = new MultiMeasureRefDlg(new UfoDialog(parent), repVO, keyGroupVO, context
				.getCurUserID(), true, true, true, selMeasures);
		refDialog.setModal(true);
		refDialog.show();

		if (refDialog.getResult() == UfoDialog.ID_OK) {
			Object[] result = new Object[2];
			result[0] = refDialog.getSelMeasureVOs();
			result[1] = refDialog.getSelUnitexVOs();
			return result;
		}
		return null;
	}

}
 