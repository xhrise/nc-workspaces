package com.ufsoft.iufo.fmtplugin.measure;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JComponent;
import javax.swing.JLabel;

import nc.pub.iufo.cache.RepFormatModelCache;
import nc.vo.iufo.keydef.KeyGroupVO;
import nc.vo.iufo.measure.MeasureVO;
import nc.vo.iuforeport.rep.ReportVO;

import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.iufo.fmtplugin.formatcore.CacheProxy;
import com.ufsoft.iufo.fmtplugin.formula.FormulaDefRenderer;
import com.ufsoft.iufo.fmtplugin.key.KeyDefDisPlayRenderer;
import com.ufsoft.iufo.fmtplugin.key.KeyDefRenderer;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaVO;
import com.ufsoft.report.ReportContextKey;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.UFOTable;
import com.ufsoft.table.re.DefaultSheetCellEditor;
import com.ufsoft.table.re.SheetCellRenderer;
import com.ufsoft.iufo.resource.StringResource;

public class MeasureRefRightPanelSample extends MeasureRefRightPanel {

	private static final long serialVersionUID = 3834704046365332194L;

	public MeasureRefRightPanelSample(MeasureRefDlg parentDlg, boolean isContainsCurrentReport, KeyGroupVO currentKeyGroupVO, ArrayList excludeMeasuresList, boolean bIncludeRefMeas) {
		super(parentDlg, isContainsCurrentReport, currentKeyGroupVO, excludeMeasuresList, bIncludeRefMeas);
		setLayout(new BorderLayout());
	}

	/**
	 * @i18n uiiufofmt00013=��ƥ��ָ�꣡
	 */
	protected void changeReportImpl(ReportVO reportVO, boolean bIncludeRefMeas) {
		if(!isHasMatchingMeasure(reportVO,bIncludeRefMeas)){
			removeAll();
			add(new nc.ui.pub.beans.UILabel(StringResource.getStringResource("uiiufofmt00013")),BorderLayout.CENTER);
			((JComponent)getParent()).revalidate();
			return;
		}
		RepFormatModelCache repFormatModelCache = CacheProxy.getSingleton().getRepFormatCache();
		CellsModel formatModel = repFormatModelCache.getUfoTableFormatModel(reportVO.getReportPK());
		formatModel = (CellsModel) formatModel.clone();
		reMoveDynMeasures(formatModel);
		UFOTable table = UFOTable.createTableByCellsModel(formatModel);
		// @edit by ll at 2009-5-14,����10:34:22
		table.setOperationState(ReportContextKey.OPERATION_REF);
		initReportRenderAndEditor(table);
		removeAll();
		add(table,BorderLayout.CENTER);
		((JComponent)getParent()).revalidate();
	}
    
	private boolean isHasMatchingMeasure(ReportVO reportVO, boolean bIncludeRefMeas){
		String repKGPk = reportVO.getKeyCombPK();
		KeyGroupVO keyGroup = keyGroupCache.getByPK(repKGPk);
		MeasureVO[] meas = null;
		//�����ǰ����ؼ������Ϊ�ջ��ѡ��ı�������ؼ�����ͬ����Ҫ��ѡ��ı��������ָ����뵽�����б�
		if ((getCurrentKeyGroupVO() == null && isContainsCurrentReport())
				||getCurrentKeyGroupVO()!= null&&keyGroup!=null
				&& keyGroup.canContainsKeyGroup(getCurrentKeyGroupVO())) {
			//װ�ر���ָ��
			if(bIncludeRefMeas){//���ַ�ʽ�Ĳ�ѯ�����������ָ��
			   	String[] aryMeasurePKs = reportCache.getMeasurePKs(reportVO.getReportPK());
				meas = measureCache.loadMeasuresByCodes(aryMeasurePKs);	
			}
			else{
				meas = measureCache.loadMeasureByReportPK(reportVO.getReportPK());
			}
			if (meas != null && meas.length > 0) {
				String measKGPk;				
				for (int j = 0; j < meas.length; j++) {
					// ���ݲ��������Ƿ��������ָ��
					if (bIncludeRefMeas
							|| meas[j].getReportPK()
									.equals(reportVO.getReportPK())) {
						measKGPk = meas[j].getKeyCombPK();
						// ����ָ�� ,��������ؼ��������ȫ��ȣ���ֱ�Ӽ��أ����򲻼���
						// ����m_oCurrentKeyGroupVO==null��ʱ�򣬼�Ϊ���幫ʽ��ʱ��
						if (getCurrentKeyGroupVO() != null
								&& getCurrentKeyGroupVO().getKeyGroupPK() != null) {
							if (measKGPk.equals(getCurrentKeyGroupVO()
									.getKeyGroupPK())
									|| (getCurrentKeyGroupVO() == null && isContainsCurrentReport())) {
								return true;
							}
						} else {
							keyGroup = keyGroupCache.getByPK(measKGPk);
							if (keyGroup.equals(getCurrentKeyGroupVO())
									|| (getCurrentKeyGroupVO() == null && isContainsCurrentReport())) {
								return true;
							}
						}

					}
				}
			}
		}	
		return false;
	}
	/**
	 * add by wangyga ��getDynVoByTimeKeyWord()���ز��ǿ�,ֻ���������̬����ָ��,������ָ���ģ����ɾ��
	 * @param cellsModel
	 */
	private void reMoveDynMeasures(CellsModel cellsModel){
		DynAreaVO[] dynAreaVos = getDynVoByTimeKeyWord(cellsModel);
		if(dynAreaVos == null || dynAreaVos.length == 0)
			return;
		MeasureModel meausreModel = MeasureModel.getInstance(cellsModel);
		DynAreaModel dynAreaModel = DynAreaModel.getInstance(cellsModel);
		
		meausreModel.removeDynAreaMeasures(DynAreaVO.MAINTABLE_DYNAREAPK);
		
		DynAreaVO[] allDynVos = dynAreaModel.getDynAreaVOs();
		if(allDynVos == null || allDynVos.length == 0)
			return;
		for(DynAreaVO dynAreaVo : allDynVos){
			boolean isContinue = false;
			for(DynAreaVO hasTimeMeasDynVo : dynAreaVos){
				if(hasTimeMeasDynVo.getDynamicAreaPK().equals(dynAreaVo.getDynamicAreaPK()))
					isContinue = true;
			}
			if(isContinue)
				continue;
			meausreModel.removeDynAreaMeasures(dynAreaVo.getDynamicAreaPK());
		}
		
	}
	private void initReportRenderAndEditor(UFOTable table){

		CellsPane view =null;
		if(table.getMainView()!=null){
			view= (CellsPane) table.getMainView().getView();
			if(view!=null){
				initCellsRenderAndEditor(view);
			}
		}
		if(table.getRightView()!=null){
			view= (CellsPane)table.getRightView().getView();
			if(view!=null){
				initCellsRenderAndEditor(view);
			}
		}
		if(table.getRightDownView()!=null){
			view= (CellsPane) table.getRightDownView().getView();
			if(view!=null){
				initCellsRenderAndEditor(view);
			}
		}
		if(table.getDownView()!=null){
			view= (CellsPane) table.getDownView().getView();
			if(view!=null){
				initCellsRenderAndEditor(view);
			}
		}
	
	}
	private void initCellsRenderAndEditor(CellsPane cellsPane) {
		//ָ����ʾΪ"ָ������"��
		cellsPane.registExtSheetRenderer(new KeyDefRenderer());
		cellsPane.registExtSheetRenderer(new KeyDefDisPlayRenderer());
		cellsPane.registExtSheetRenderer(new MeasureDefRender());
		cellsPane.registExtSheetRenderer(new FormulaDefRenderer(true));
		cellsPane.registExtSheetRenderer(new FormulaDefRenderer(false));
		
		cellsPane.registExtSheetRenderer(new SheetCellRenderer(){
			JLabel label = new nc.ui.pub.beans.UILabel();
			public Component getCellRendererComponent(CellsPane cellsPane,Object value, boolean isSelected, boolean hasFocus, int row, int column, Cell cell) {
				CellsModel cellsModel = cellsPane.getDataModel();
				MeasureModel measureModel = MeasureModel.getInstance(cellsModel);
				MeasureVO measureVO = measureModel.getMeasureVOByPos(CellPosition.getInstance(row,column));
				if(measureVO != null){
					String meausreName = measureVO.getName();	
					label.setText("<"+meausreName+">");
					return label;
				}else{
					return null;
				}
				
			}			
		});
		
		//������Ϊ�жϵ�ǰΪ��Чָ��ʱ�����ص�ǰָ�ꡣ
		cellsPane.registExtSheetEditor(new DefaultSheetCellEditor(new nc.ui.pub.beans.UILabel()){
		    public Component getTableCellEditorComponent(CellsPane table, Object value,
		            boolean isSelected, int row, int column) {		    	
		    	if(isAutoClose()){
		    		MeasureVO selVO = getSelectedMeasureVO();
		    		if(selVO != null){
		    			closeWithOKResult();
		    		}
		    	}
		    	return null;
		    }
	        public int getEditorPRI() {
	        	return 10;
	        }
	    	public boolean isEnabled(CellsModel cellsModel, CellPosition cellPos) {
				return true;
			}
		});
		
	}

	protected MeasureVO getSelectedMeasureVO() {
		Component component = getComponent(0);
		if(!(component instanceof UFOTable)){
			return null;
		}
		UFOTable table = (UFOTable) component;
		CellsModel cellsModel = table.getCellsModel();
		MeasureModel measureModel = MeasureModel.getInstance(cellsModel);
		CellPosition anchorPos = cellsModel.getSelectModel().getAnchorCell();
		MeasureVO[] matchingVOs = getMatchingMeasureVOs(getCurReportVO(), isIncludeRefMeasures());
		MeasureVO selVO = measureModel.getMeasureVOByPos(anchorPos);
		if(!Arrays.asList(matchingVOs).contains(selVO)){
			return null;
		}
		return filterMeasureVO(selVO);
	}

	@Override
	public MeasureVO[] getSelMeasureVOs() {
		return new MeasureVO[]{getSelectedMeasureVO()};
	}

	@Override
	public void setSelMeasureVOs(MeasureVO[] selMeasures) {
		// TODO Auto-generated method stub
		
	}

}
 