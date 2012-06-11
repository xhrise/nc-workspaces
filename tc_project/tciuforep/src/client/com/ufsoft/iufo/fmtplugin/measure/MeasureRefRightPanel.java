package com.ufsoft.iufo.fmtplugin.measure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.JOptionPane;

import nc.pub.iufo.cache.KeyGroupCache;
import nc.pub.iufo.cache.MeasureCache;
import nc.pub.iufo.cache.RepFormatModelCache;
import nc.pub.iufo.cache.ReportCache;
import nc.util.iufo.pub.UFOString;
import nc.vo.iufo.keydef.KeyGroupVO;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.iufo.measure.MeasureVO;
import nc.vo.iuforeport.rep.ReportVO;

import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.iufo.fmtplugin.formatcore.CacheProxy;
import com.ufsoft.iufo.fmtplugin.key.KeywordModel;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaVO;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.table.CellsModel;

public abstract class MeasureRefRightPanel extends nc.ui.pub.beans.UIPanel {
	protected ReportCache reportCache = CacheProxy.getSingleton().getReportCache();
	protected MeasureCache measureCache = CacheProxy.getSingleton().getMeasureCache();
	protected KeyGroupCache keyGroupCache = CacheProxy.getSingleton().getKeyGroupCache();
	protected RepFormatModelCache formatCache = CacheProxy.getSingleton().getRepFormatCache();
	
	private boolean _isContainsCurrentReport;
	//ѡ�еı�������
	private String _repCode = "";
	private MeasureRefDlg _parentDlg;
	private KeyGroupVO m_oCurrentKeyGroupVO;
	private ArrayList _excludeMeasuresList;
	private boolean m_bIncludeRefMeas;
	ReportVO _reportVO = null;


	public void changeReport(ReportVO reportVO){
		
		if (reportVO != null
				&& (_reportVO == null || !reportVO.getReportPK().equals(
						_reportVO.getReportPK()))) {
			changeReportImpl(reportVO, m_bIncludeRefMeas);
		}
		_reportVO = reportVO;
	}
	abstract protected void changeReportImpl(ReportVO reportVO, boolean bIncludeRefMeas);
	protected ReportVO getCurReportVO(){
		return _reportVO;
	}
	abstract protected MeasureVO getSelectedMeasureVO();

	public MeasureRefRightPanel(MeasureRefDlg parentDlg, boolean isContainsCurrentReport, KeyGroupVO currentKeyGroupVO, ArrayList excludeMeasuresList, boolean bIncludeRefMeas) {
		super();
		_isContainsCurrentReport = isContainsCurrentReport;
		_parentDlg = parentDlg;
		m_oCurrentKeyGroupVO = currentKeyGroupVO;
		_excludeMeasuresList = excludeMeasuresList;
		m_bIncludeRefMeas = bIncludeRefMeas;
	}
	protected void closeWithOKResult() {
		if(_parentDlg!=null){
		_parentDlg.setResult(UfoDialog.ID_OK);				
		_parentDlg.close();	
		}
	}

	/**
	 * ָ�궨��ʱ���������������Զ��رա�
	 * ��ʽ����ʱ�����������Զ��رա�
	 * @return
	 */
	protected boolean isAutoClose() {
		return _isContainsCurrentReport;
	}
	protected boolean isContainsCurrentReport(){
		return _isContainsCurrentReport;
	}
	protected void setReportCode(String repCode){
		_repCode = repCode;
	}
	
	
	public KeyGroupVO getCurrentKeyGroupVO() {
		return m_oCurrentKeyGroupVO;
	}
	public void setCurrentKeyGroupVO(KeyGroupVO currentKeyGroupVO) {
		if(m_oCurrentKeyGroupVO!=currentKeyGroupVO){
			this.repaint();
		}
		m_oCurrentKeyGroupVO = currentKeyGroupVO;
	}
	
	public String getStrRefMeasure() {
		MeasureVO selVO = getSelectedMeasureVO();
		if (selVO != null)
			return "'" + _repCode + "->" + selVO.getName() + "'";
		else
			return "";
	}
	protected MeasureVO[] getMatchingMeasureVOs(ReportVO repvo, boolean bIncludeRefMeas) {
		String repKGPk = repvo.getKeyCombPK();
		KeyGroupVO keyGroup = keyGroupCache.getByPK(repKGPk);
		Vector measVec = new Vector();
		MeasureVO[] meas = null;
		//�����ǰ����ؼ������Ϊ�ջ��ѡ��ı�������ؼ�����ͬ����Ҫ��ѡ��ı��������ָ����뵽�����б�
		if ((m_oCurrentKeyGroupVO == null && isContainsCurrentReport())
				|| m_oCurrentKeyGroupVO != null&&keyGroup!=null
				&& keyGroup.canContainsKeyGroup(m_oCurrentKeyGroupVO)) {
			//װ�ر���ָ��
			if(bIncludeRefMeas){//���ַ�ʽ�Ĳ�ѯ�����������ָ��
			   	String[] aryMeasurePKs = reportCache.getMeasurePKs(repvo.getReportPK());
				meas = measureCache.loadMeasuresByCodes(aryMeasurePKs);	
			}
			else{
				meas = measureCache.loadMeasureByReportPK(repvo.getReportPK());
			}
			if (meas != null && meas.length > 0) {
				String measKGPk;				
				for (int j = 0; j < meas.length; j++) {
					// ���ݲ��������Ƿ��������ָ��
					if (bIncludeRefMeas
							|| meas[j].getReportPK()
									.equals(repvo.getReportPK())) {
						measKGPk = meas[j].getKeyCombPK();
						// ����ָ�� ,��������ؼ��������ȫ��ȣ���ֱ�Ӽ��أ����򲻼���
						// ����m_oCurrentKeyGroupVO==null��ʱ�򣬼�Ϊ���幫ʽ��ʱ��
						if (m_oCurrentKeyGroupVO != null
								&& m_oCurrentKeyGroupVO.getKeyGroupPK() != null) {
							if (measKGPk.equals(m_oCurrentKeyGroupVO
									.getKeyGroupPK())
									|| (m_oCurrentKeyGroupVO == null && isContainsCurrentReport())) {
								measVec.add(meas[j]);
							}
						} else {
							keyGroup = keyGroupCache.getByPK(measKGPk);
							if (keyGroup.equals(m_oCurrentKeyGroupVO)
									|| (m_oCurrentKeyGroupVO == null && isContainsCurrentReport())) {
								measVec.add(meas[j]);
							}
						}

					}
				}
			}
		}		
		
		//add by wangyag ����뵱ǰ����ʱ��ؼ�����ͬ�Ķ�̬����Ĺؼ���
		CellsModel cellsModel = getCellsModel(repvo);
		if(cellsModel == null){
			MeasureVO[] vos = new MeasureVO[measVec.size()];
			measVec.copyInto(vos);
			return vos;
		}
		
		MeasureModel measureModel = MeasureModel.getInstance(cellsModel);
		DynAreaVO[] dynAreaVos = getDynVoByTimeKeyWord(cellsModel);
		if(dynAreaVos != null && dynAreaVos.length > 0){
			for(DynAreaVO dynVo : dynAreaVos){
				MeasureVO[] dynMeasureVos = measureModel.getMeasureVOs(dynVo.getDynamicAreaPK());
				if(dynMeasureVos == null || dynMeasureVos.length ==0)
					continue;
				for(MeasureVO measureVo : dynMeasureVos){
					if(!measVec.contains(measureVo))
					    measVec.add(measureVo);			
				}
									
			}			
		}
		
		Collections.sort(measVec, new Comparator(){
	        public int compare(Object a, Object b){
	            if(a != null && b != null){
	                String aName = ((MeasureVO)a).getName();
	                String bName = ((MeasureVO)b).getName();
	                return UFOString.compareHZString(aName, bName);
	            }
	            return -1;
	        }
	    });
		
		if (repvo.getCode() != null)
			setReportCode(repvo.getCode());
		MeasureVO[] vos = new MeasureVO[measVec.size()];
		measVec.copyInto(vos);
		return vos;
	}
	
	/**
	 * ����뵱ǰ����ʱ��ؼ�����ͬ�Ķ�̬��
	 * @return
	 */
	protected DynAreaVO[] getDynVoByTimeKeyWord(CellsModel cellsModel){
		if(m_oCurrentKeyGroupVO == null)
			return null;
		if(!(m_oCurrentKeyGroupVO.isTTimeTypeAcc() || m_oCurrentKeyGroupVO.isTTimeTypeTime()))
			return null;
		KeyVO currentTimeKeyVo = m_oCurrentKeyGroupVO.getTTimeKey();
		
		DynAreaModel dynModel = DynAreaModel.getInstance(cellsModel);
		KeywordModel keyWordModel = dynModel.getKeywordModel();
		
		if(dynModel == null || keyWordModel == null)
			return null;
		DynAreaVO[] dynAreaVos = dynModel.getDynAreaVOs();
		if(dynAreaVos == null || dynAreaVos.length == 0)
			return null;
		Vector<DynAreaVO> dynVosVector = new Vector<DynAreaVO>();
		for(DynAreaVO dynAreaVo : dynAreaVos){
			KeyVO[] dynKeyWordVos = keyWordModel.getKeyVOs(dynAreaVo.getDynamicAreaPK());
			if(dynKeyWordVos == null || dynKeyWordVos.length == 0)
				return null;
			for(KeyVO keyVo : dynKeyWordVos){
				if(keyVo.isTTimeKeyVO()){
					if(keyVo.equals(currentTimeKeyVo))
						dynVosVector.add(dynAreaVo);
				}
			}		
		}
		return dynVosVector.toArray(new DynAreaVO[0]);
	}
	
	protected CellsModel getCellsModel(ReportVO repVo){
		return formatCache.getUfoTableFormatModel(repVo.getReportPK());
	}
	protected boolean isPermitDupRefer() {
		return isContainsCurrentReport();
	}

	protected boolean isReferenced(String code) {
		return _excludeMeasuresList.contains(code);
	}
	protected MeasureVO filterMeasureVO(MeasureVO vo) {
		if(vo != null && isReferenced(vo.getCode()) && !isPermitDupRefer()){
			JOptionPane.showMessageDialog(this,StringResource
					.getStringResource("miufo1001463"),"",JOptionPane.ERROR_MESSAGE);
			return null;
		}
		return vo;
	}
	protected boolean isIncludeRefMeasures(){
		return m_bIncludeRefMeas;
	}

	/**
	 * �����Ѿ�ѡ���ָ����Ϣ
	 * @param selMeasures
	 */
	public abstract void setSelMeasureVOs(MeasureVO[] selMeasures);
	/**
	 * �ӽ����ϻ�ȡѡ���ָ����Ϣ
	 * @return
	 */
	public abstract MeasureVO[] getSelMeasureVOs();
}
