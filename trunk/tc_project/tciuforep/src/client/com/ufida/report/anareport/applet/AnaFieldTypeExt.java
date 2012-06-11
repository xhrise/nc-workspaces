package com.ufida.report.anareport.applet;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.beans.util.NCOptionPane;

import com.ufida.dataset.descriptor.DescriptorType;
import com.ufida.dataset.metadata.DataTypeConstant;
import com.ufida.dataset.metadata.Field;
import com.ufida.report.adhoc.model.IFldCountType;
import com.ufida.report.anareport.model.AnaCellCombine;
import com.ufida.report.anareport.model.AnaCrossTableSet;
import com.ufida.report.anareport.model.AnaRepField;
import com.ufida.report.anareport.model.AnaReportModel;
import com.ufida.report.anareport.model.AreaDataModel;
import com.ufida.report.anareport.model.FieldCountDef;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.StateUtil;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.menu.UFPopupMenu;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.IArea;
import com.ufsoft.table.SelectListener;
import com.ufsoft.table.event.SelectEvent;
import com.ufsoft.table.exarea.ExAreaCell;
import com.ufsoft.table.exarea.ExAreaModel;

/**
 * ���������ֶε���������
 * 
 * @author ll
 * 
 */
public class AnaFieldTypeExt extends AbsActionExt {

	/**
	 * @i18n miufo1001596=����
	 */
	private static final String RESID_SETFIELD_GROUP = "miufo1001596";
	/**
	 * @i18n miufo00274=ȡ������
	 */
	private static final String RESID_SETFIELD_DETAIL = "miufo00274";
	/**
	 * @i18n miufo00275=ȡ��ͳ��
	 */
	private static final String RESID_CANCELFIELD_CALC = "miufo00275";
	/**
	 * @i18n miufo00276=ͳ��
	 */
	static final String RESID_SETFIELD_CALC = "miufo00276";
	/**
	 * @i18n miufo00277=����С��
	 */
	static final String RESID_SETFIELD_SUBTOTAL = "miufo00277";
	/**
	 * @i18n miufo00178=�ֶ�����
	 */
	public static final String RESID_SETFIELD = "miufo00178";
	private JComboBox item = null;
	
	private AnaReportPlugin m_plugin = null;
	private int m_fldType = AnaRepField.TYPE_GROUP_FIELD;
	
	private class AnaFieldCommand extends UfoCommand {
		public void execute(Object[] params) {
			if (params == null)
				return;

			UfoReport report = (UfoReport) params[0];
			CellPosition[] fmtcells = m_plugin.getModel().getFormatPoses(report.getCellsModel(),
					report.getCellsModel().getSelectModel().getSelectedAreas());
			changeAnaFieldType(m_plugin.getModel().getFormatModel(), m_fldType, params, fmtcells);
			if(m_fldType == AnaRepField.TYPE_CALC_FIELD && !m_plugin.getModel().isFormatState())
				m_plugin.refreshDataModel(true);			
		}
	}


	public AnaFieldTypeExt(AnaReportPlugin plugin, int fldType) {
		super();
		m_plugin = plugin;
		m_fldType = fldType;
	}

	public UfoCommand getCommand() {
		return new AnaFieldCommand();
	}

	public Object[] getParams(UfoReport container) {

		return doGetParams(container);
	}

	public boolean isEnabled(Component focusComp) {
		if(!m_plugin.getModel().isFormatState() && m_fldType == AnaRepField.Type_CROSS_SUBTOTAL)
			return false;
		AnaReportModel model = m_plugin.getModel();
		CellsModel fmtModel = model.getFormatModel();
		AreaPosition[] areaPos = m_plugin.getFormatSelected();	
		if(areaPos == null || areaPos.length == 0)
			return false;
		if(isHasChart(areaPos[0]))//ͼ�����ܶ���ͳ���ֶΣ����飬����
			return false;
		
		for (AreaPosition area : areaPos) {
			AreaDataModel areaData = m_plugin.getExAreaModel(fmtModel, area);
			if (areaData == null || areaData.getDSTool() == null)
				return false;
			if (!areaData.getDSTool().isSupport(DescriptorType.AggrDescriptor))// ���ڲ�֧�ֻ��ܵ����ݼ����˹��ܲ�����
				return false;
			AnaCrossTableSet crossTableSet = areaData.getCrossSet();
			if (m_fldType == AnaRepField.TYPE_GROUP_FIELD) {// ���������У������ֶβ�����
				CellPosition[] selectedPos=m_plugin.getSelectedPos(fmtModel, area);
				if (areaData.isCross())
					return false;
				else if(AnaReportFieldAction.isSelAnaField(fmtModel,selectedPos))
					return true;
				else
					return false;
			}
			if (m_fldType == AnaRepField.TYPE_CALC_FIELD
					|| m_fldType == AnaRepField.TYPE_CANCEL_CALC_FIELD
					|| m_fldType == AnaRepField.Type_CROSS_SUBTOTAL
					|| m_fldType == AnaRepField.TYPE_CANCEL_CROSS_SUBTOTAL) {// ͳ��
				Cell cell = fmtModel.getCell(area.getStart());
				AnaRepField field = null;
				if (cell != null
						&& cell.getExtFmt(AnaRepField.EXKEY_FIELDINFO) != null) {
					field = (AnaRepField) cell
							.getExtFmt(AnaRepField.EXKEY_FIELDINFO);
				}
                
				if (m_fldType == AnaRepField.Type_CROSS_SUBTOTAL || m_fldType == AnaRepField.TYPE_CANCEL_CROSS_SUBTOTAL) {// ˵���ǽ�����
					if(crossTableSet == null){
						return false;
					}else if (AnaReportFieldAction.isInMeasureArea(crossTableSet,
							area.getStart())) {
						if (m_fldType == AnaRepField.TYPE_CANCEL_CROSS_SUBTOTAL) {// ȡ������С��
							if (field!=null&&field.getFieldCountDef() != null
									&& field.getFieldCountDef().getRangeFld() != null) {
								return true;
							}else{
								return false;
							}
							
						}
						return true;
					}
				} else if (m_fldType == AnaRepField.TYPE_CALC_FIELD
						|| m_fldType == AnaRepField.TYPE_CANCEL_CALC_FIELD) {
					if (crossTableSet != null) {// ������ָ��
						if (AnaReportFieldAction.isInMeasureArea(crossTableSet,
								area.getStart())) {
							if (m_fldType == AnaRepField.TYPE_CALC_FIELD) {// ͳ��
								if (field!=null&&field.getFieldCountDef() != null
										&& field.getFieldCountDef()
												.getRangeFld() == null) {
									return true;
								}
							}
							return false;
						} else {
							return false;
						}
					} else {
						if(m_fldType == AnaRepField.TYPE_CANCEL_CALC_FIELD){
							if (field!=null&&field.getFieldCountDef() != null){
								return true;
							}
							return false;
						}
                         return true;
					}
				}
				return false;
			}
		}
		return StateUtil.isAreaSel(m_plugin.getReport(), focusComp); 		 
	}

	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes3 = new ActionUIDes();
		uiDes3.setToolBar(true);
		uiDes3.setGroup(StringResource.getStringResource(RESID_SETFIELD));
		uiDes3.setPaths(new String[] {});
		if (m_fldType == AnaRepField.TYPE_DETAIL_FIELD) {
			uiDes3.setName(StringResource.getStringResource(RESID_SETFIELD_DETAIL));
			uiDes3.setTooltip(StringResource.getStringResource(RESID_SETFIELD_DETAIL));
			uiDes3.setImageFile("reportcore/remove_group.gif");
		} else if (m_fldType == AnaRepField.TYPE_GROUP_FIELD) {
			uiDes3.setName(StringResource.getStringResource(RESID_SETFIELD_GROUP));
			uiDes3.setTooltip(StringResource.getStringResource(RESID_SETFIELD_GROUP));
			uiDes3.setImageFile("reportcore/set_group.gif");
		} else if (m_fldType == AnaRepField.TYPE_CALC_FIELD) {
			uiDes3.setComboType(2);//�����˵�
			uiDes3.setListCombo(true);
			uiDes3.setComboComponent(createComboxMenu()); 
			uiDes3.setName(StringResource.getStringResource(RESID_SETFIELD_CALC));
			uiDes3.setTooltip(StringResource.getStringResource(RESID_SETFIELD_CALC));
			uiDes3.setImageFile("reportcore/calc_field.gif");
		} else if(m_fldType==AnaRepField.TYPE_CANCEL_CALC_FIELD){
        	setFldType(AnaRepField.TYPE_CANCEL_CALC_FIELD);
        	uiDes3.setName(StringResource.getStringResource(RESID_CANCELFIELD_CALC));
        	uiDes3.setTooltip(StringResource.getStringResource(RESID_CANCELFIELD_CALC));
        	uiDes3.setImageFile("reportcore/calc_no.gif");
		}else if (m_fldType == AnaRepField.Type_CROSS_SUBTOTAL) {
			uiDes3.setName(StringResource.getStringResource(RESID_SETFIELD_SUBTOTAL));
			uiDes3.setTooltip(StringResource.getStringResource(RESID_SETFIELD_SUBTOTAL));
			uiDes3.setImageFile("reportcore/cross_subtotal.gif");
		}else if (m_fldType == AnaRepField.TYPE_CANCEL_CROSS_SUBTOTAL) {
			uiDes3.setName(StringResource.getStringResource(RESID_CANCELFIELD_CALC));
			uiDes3.setTooltip(StringResource.getStringResource("miufo00278"));
			uiDes3.setImageFile("reportcore/remove_group.gif");
		}
		return new ActionUIDes[] { uiDes3 };
	}

	/**
	 * override
	 * @see AbsActionExt.initListenerByComp();
	 */
	public void initListenerByComp(Component stateChangeComp) {	
		
		if(stateChangeComp instanceof JComboBox){
			setItem((JComboBox) stateChangeComp);
		}
		
		if(!(stateChangeComp instanceof JButton))
			return;
		final JButton toolButton = (JButton)stateChangeComp;
		m_plugin.getModel().getCellsModel().getSelectModel().addSelectModelListener(
				new SelectListener(){
					public void selectedChanged(SelectEvent e) {
						if(e.getProperty() != SelectEvent.ANCHOR_CHANGED)
							return;
						AnaReportModel model = m_plugin.getModel();
						CellPosition cellPos = model.getCellsModel().getSelectModel().getAnchorCell();
						Cell cell = model.getFormatCell(model.getCellsModel(), cellPos);
						if (cell == null)
							return;
						AnaRepField fld = (AnaRepField) cell.getExtFmt(AnaRepField.EXKEY_FIELDINFO);								
						if (fld == null){
							if(m_fldType==AnaRepField.TYPE_DETAIL_FIELD&&toolButton.getName().equals(StringResource.getStringResource(RESID_SETFIELD_DETAIL))){
								switchButtonProperty(toolButton,AnaRepField.TYPE_GROUP_FIELD);
							}
							return;
						}
							
						
						if(m_fldType == AnaRepField.TYPE_GROUP_FIELD && fld.getFieldType() == AnaRepField.TYPE_GROUP_FIELD){
							switchButtonProperty(toolButton,AnaRepField.TYPE_DETAIL_FIELD);
						} else if(m_fldType == AnaRepField.TYPE_DETAIL_FIELD && fld.getFieldType() == AnaRepField.TYPE_DETAIL_FIELD){
							switchButtonProperty(toolButton,AnaRepField.TYPE_GROUP_FIELD);
						}
						
//						if(m_fldType == AnaRepField.TYPE_CANCEL_CALC_FIELD && m_fldType != AnaRepField.Type_CROSS_SUBTOTAL){//ȡ��ͳ�ƺ�ȡ��������ͬһ����ť
//							if(fld.getFieldType() == AnaRepField.TYPE_GROUP_FIELD){
//								switchButtonProperty(toolButton,AnaRepField.TYPE_DETAIL_FIELD);
//							} else if(fld.getFieldType() == AnaRepField.TYPE_DETAIL_FIELD){
//								switchButtonProperty(toolButton,AnaRepField.TYPE_GROUP_FIELD);
//							}							
//						}
							
//						if(m_fldType != AnaRepField.TYPE_CANCEL_CALC_FIELD && fld.getFieldCountDef() != null && m_fldType != AnaRepField.Type_CROSS_SUBTOTAL){//ȡ��ͳ�ƺ�ȡ��������ͬһ����ť
//							switchButtonProperty(toolButton,AnaRepField.TYPE_CANCEL_CALC_FIELD);
//						}	
						
//						if(m_fldType == AnaRepField.Type_CROSS_SUBTOTAL && fld.getFieldType() == AnaRepField.Type_CROSS_SUBTOTAL){
//							switchButtonProperty(toolButton,AnaRepField.TYPE_CANCEL_CROSS_SUBTOTAL);
//						}else if(m_fldType == AnaRepField.TYPE_CANCEL_CROSS_SUBTOTAL&&fld.getFieldCountDef()!=null&&fld.getFieldCountDef().getRangeFld()==null){
//							switchButtonProperty(toolButton,AnaRepField.Type_CROSS_SUBTOTAL);
//						}
						
					}
					
				});
	
			toolButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					if (m_fldType == AnaRepField.TYPE_DETAIL_FIELD) {
						switchButtonProperty(toolButton,AnaRepField.TYPE_GROUP_FIELD);
					} else if (m_fldType == AnaRepField.TYPE_GROUP_FIELD) {
						switchButtonProperty(toolButton,AnaRepField.TYPE_DETAIL_FIELD);
					} 
//						else if(m_fldType == AnaRepField.TYPE_CANCEL_CALC_FIELD){
//						switchButtonProperty(toolButton,AnaRepField.TYPE_GROUP_FIELD);
//					} else if(m_fldType == AnaRepField.TYPE_CANCEL_CROSS_SUBTOTAL){
//						switchButtonProperty(toolButton,AnaRepField.TYPE_CANCEL_CALC_FIELD);
//					}
					
					if(!m_plugin.getModel().isFormatState())
						m_plugin.refreshDataModel(true);
				}
				
			});		
	}
	
    /**
	 * @i18n miufo00278=ȡ������С��
	 */
    private void switchButtonProperty(JButton toolButton,int iFldType){
    	switch (iFldType) {
		case AnaRepField.TYPE_DETAIL_FIELD:
			setFldType(AnaRepField.TYPE_DETAIL_FIELD);
			toolButton.setName(StringResource.getStringResource(RESID_SETFIELD_DETAIL));
			toolButton.setToolTipText(StringResource.getStringResource(RESID_SETFIELD_DETAIL));
			toolButton.setIcon(ResConst.getImageIcon("reportcore/remove_group.gif"));
			break;
		case AnaRepField.TYPE_GROUP_FIELD:
			setFldType(AnaRepField.TYPE_GROUP_FIELD);
			toolButton.setName(StringResource.getStringResource(RESID_SETFIELD_GROUP));
			toolButton.setToolTipText(StringResource.getStringResource(RESID_SETFIELD_GROUP));
			toolButton.setIcon(ResConst.getImageIcon("reportcore/set_group.gif"));
		    break;
        case AnaRepField.Type_CROSS_SUBTOTAL:
        	setFldType(AnaRepField.Type_CROSS_SUBTOTAL);
			toolButton.setName(StringResource.getStringResource(RESID_SETFIELD_SUBTOTAL));
			toolButton.setToolTipText(StringResource.getStringResource(RESID_SETFIELD_SUBTOTAL));
			toolButton.setIcon(ResConst.getImageIcon("reportcore/cross_subtotal.gif"));
		    break; 
        case AnaRepField.TYPE_CANCEL_CROSS_SUBTOTAL:
        	setFldType(AnaRepField.TYPE_CANCEL_CROSS_SUBTOTAL);
			toolButton.setName(StringResource.getStringResource(RESID_CANCELFIELD_CALC));
			toolButton.setToolTipText(StringResource.getStringResource("miufo00278"));
			toolButton.setIcon(ResConst.getImageIcon("reportcore/remove_group.gif"));
		default:
			break;
		}
    	toolButton.setEnabled(isEnabled(m_plugin.getReport().getTable().getCells()));
    }
    

	// ִ�������ֶε���������
	private void changeAnaFieldType(CellsModel fmtModel, int fldType, Object[] param, CellPosition[] formatPoses) {
		if (formatPoses == null || formatPoses.length == 0)
			return;
		ArrayList<CellPosition> al_flds = new ArrayList<CellPosition>();// �������ֶα仯�ĵ�Ԫλ��
		for (CellPosition pos : formatPoses) {

			Cell cell = fmtModel.getCell(pos);
			if (cell == null && (fldType == AnaRepField.TYPE_CALC_FIELD|| fldType == AnaRepField.Type_CROSS_SUBTOTAL))// ����ͳ���ֶκͽ���С�ƣ���Ҫ��Ԫ������
				cell = fmtModel.getCellIfNullNew(pos.getRow(), pos.getColumn());

			if (cell != null) {
				
				AnaRepField fld = (AnaRepField) cell.getExtFmt(AnaRepField.EXKEY_FIELDINFO);
				Field[] allFlds = null;
				Field[] countFlds = null;
				
				
				if (fldType == AnaRepField.TYPE_CALC_FIELD || fldType == AnaRepField.Type_CROSS_SUBTOTAL) {
					
					FieldCountDef def = (FieldCountDef) param[1];
					countFlds = (Field[]) param[2];
					allFlds = (Field[]) param[3];
					String dsPK = (String) param[4];

					Field mainFld = null;//ԭʼ���ݼ��ֶ�
					String fldName=null;
					for (Field f : countFlds) {
						if(f instanceof FieldCountDef){
							fldName=((FieldCountDef)f).getMainFldName();
						}else{
							fldName=f.getFldname();
						}
						if (fldName.equals(def.getMainFldName())) {
							mainFld = f;
							break;
						}
					}
					if(formatPoses.length == 1){//��������ͳ������ʱ������Ҫ�����µ��ֶ�
						if (fld == null || (fld.getFieldID() != null && !fld.getFieldID().equals(mainFld.getFldname()))) {
							fld = new AnaRepField(mainFld, fldType, dsPK);							
						}
					}
                    
                    boolean isBefore=def.isBefore();
                    boolean isAfter=def.isAfter();
                    //??new
					def = new FieldCountDef(fld.getField(), def.getCountType(), def.getRangeFld(),def.getRangeName(),
								null);
					def.setBefore(isBefore);
					def.setAfter(isAfter);
					fld.setCountFieldDef(def);
					fld.setOrderType(AnaRepField.ORDERTYPE_NONE);
				}
				if(fld == null)
                	continue;
				if(fldType == AnaRepField.TYPE_GROUP_FIELD)//�����ֶΣ�û����������ʱ���Զ�Ĭ��Ϊ����
					if(fld.isNoOrder())
						fld.setOrderType(AnaRepField.ORDERTYPE_ASCENDING);	
				if(fldType == AnaRepField.TYPE_DETAIL_FIELD){
//					fld.setOrderType(AnaRepField.ORDERTYPE_NONE);
					fmtModel.fireExtPropChanged(pos);
				}
				if(fld.getFieldType() == AnaRepField.Type_CROSS_SUBTOTAL && fldType == AnaRepField.TYPE_CANCEL_CROSS_SUBTOTAL){//ȡ������С��
					fld.setFieldType(AnaRepField.TYPE_CALC_FIELD);
					FieldCountDef countDef = new FieldCountDef(fld.getField(),IFldCountType.TYPE_SUM,null,null,null);
					fld.setCountFieldDef(countDef);
				}
				
				if (fld != null) {
					fld.setFieldType(fldType);
					AnaReportFieldAction.addFlds(fmtModel, pos, fld);

					al_flds.add(pos);
				}
				
				if(fldType == AnaRepField.TYPE_CANCEL_CALC_FIELD){//ȡ��ͳ��
					removeCountDefFld(fld, cell);
				}
			}		
		}
		if (al_flds.size() > 0 && m_plugin.getOperationState() == UfoReport.OPERATION_INPUT) {// ����̬������Ҫ�Զ�����С��
			if (m_fldType == AnaRepField.TYPE_GROUP_FIELD) {// ���÷����ֶ�
				for (CellPosition pos : al_flds) {
					// �����Զ��ϲ�����
					fmtModel.getCell(pos).addExtFmt(AnaCellCombine.KEY, new AnaCellCombine());
					// ���Զ�׷��С��
					// TODO
				}
			} else if (m_fldType == AnaRepField.TYPE_DETAIL_FIELD) {// ����ϸ���ֶ�
				// ɾ���Զ��ϲ�����
				for (CellPosition pos : al_flds) {
					fmtModel.getCell(pos).removeExtFmt(AnaCellCombine.KEY);
				}
				// ɾ��С���ֶ�
				// TODO
			}
		}
		return;
	}

	/**
	 * @i18n iufobi00064=��������û��ָ���γ��,�޷����н�������
	 */
	private Object[] doGetParams(UfoReport container) {
		CellsModel cells = m_plugin.getModel().getFormatModel();
		AreaPosition[] areaPos = m_plugin.getFormatSelected();
		if (areaPos == null || areaPos.length <= 0)
			return null;
		AreaPosition selectedArea=areaPos[0];
		CellPosition[] selectedPos=m_plugin.getSelectedPos(cells, selectedArea);
        Cell cell = null;
        AnaRepField fld = null;
		
		
		if (m_fldType != AnaRepField.TYPE_CALC_FIELD && m_fldType != AnaRepField.Type_CROSS_SUBTOTAL) {// ��Ҫѡ���ֶβſ���
			if (!AnaReportFieldAction.isSelAnaField(cells,selectedPos))
				return null;
			if(m_fldType != AnaRepField.TYPE_CANCEL_CALC_FIELD && m_fldType != AnaRepField.TYPE_CANCEL_CROSS_SUBTOTAL){
				for(CellPosition pos : selectedPos){
					cell=cells.getCell(pos);
					if(cell != null)
						fld = (AnaRepField) cell.getExtFmt(AnaRepField.EXKEY_FIELDINFO);
					if(fld!=null&&!checkField(fld))
						return null;
				}
				
			}			
			return new Object[] { container };
		} else {// ͳ���ֶ����ã���Ҫ�������ݵ���չ������
			cell=cells.getCell(selectedPos[0]);
			if(cell != null)
				fld = (AnaRepField) cell.getExtFmt(AnaRepField.EXKEY_FIELDINFO);	
			AreaDataModel areaModel = m_plugin.getExAreaModel(cells, selectedArea);
			if (areaModel == null || areaModel.getDSPK() == null)
				return null;
			Field[] allFlds = areaModel.getDSInfo().getDataSetDef().getMetaData().getFields(true);
			// ����ͳ���ֶε����ã���Ҫ��ȡ���ݼ����ֶ���Ϣ
			
			Field[] countFlds = null;
			Field[] aggrFlds = null;
			boolean bSubTotal = m_fldType == AnaRepField.Type_CROSS_SUBTOTAL;
			if (bSubTotal) {// ������С��
				if (!areaModel.isCross())// ��չ����ģ����û�н�������
					return null;
				
				AnaCrossTableSet cross = areaModel.getCrossSet();
				if (cross != null) {
					Field[] meas = cross.getCrossFlds(AnaRepField.Type_CROSS_MEASURE);
					if (meas != null && meas.length > 0)
						countFlds = meas;
					Field[] rows=cross.getCrossFlds(AnaRepField.Type_CROSS_ROW);
					Field[] cols=cross.getCrossFlds(AnaRepField.Type_CROSS_COLUMN);
					if(rows!=null||cols!=null){
						int rowLen=rows==null?0:rows.length;
						int colLen=cols==null?0:cols.length;
						aggrFlds=new Field[rowLen+colLen];
						for(int i=0;i<rowLen;i++){
							aggrFlds[i]=rows[i];
						}
						for(int j=0;j<colLen;j++){
							aggrFlds[j+rowLen]=cols[j];
						}
						
					}
				}
				//modify by guogang 2009-1-14
				if(countFlds==null||aggrFlds==null){
					UfoPublic.sendWarningMessage(StringResource.getStringResource("iufobi00064"), null);
					return null;
				}
			}else{//modify by guogang 2009-1-14
				if (countFlds == null)
					countFlds = allFlds;
				 if(aggrFlds==null){
					aggrFlds=allFlds;
				  }
			}
			
			
			
			if(fld != null && getSelectItem() != null){	//modify by wangyga ֱ�Ӹı��ֶε�ͳ������
				return getCountTypeParams(fld,countFlds,allFlds,areaModel.getDSPK());								
			}
			
			boolean isShowCountList = !(AnaReportFieldAction.isInMeasureArea(areaModel.getCrossSet(),selectedPos[0]) && m_fldType == AnaRepField.TYPE_CALC_FIELD);
			CountFieldDefDlg dlg = new CountFieldDefDlg(container, countFlds, aggrFlds, bSubTotal,isShowCountList);
			if (cell != null) {				
				if (fld != null) {
					FieldCountDef def = null;
					if (fld.getFieldType() == AnaRepField.TYPE_CALC_FIELD)
						def = fld.getFieldCountDef();
					else {
						if (fld.getField() instanceof FieldCountDef)
							def = (FieldCountDef) fld.getField();
						else
							def = new FieldCountDef(fld.getField(), getSelectCountType(), null,null,
									null);
					}
					if (def != null)						
						dlg.setClacFieldInfo(def);
				} else{//�ֶ�ΪNULLʱ��Ҳ��ʼ��ѡ�в˵���ͳ������
					FieldCountDef countDef = new FieldCountDef();
					countDef.setCountType(getSelectCountType());
					dlg.setClacFieldInfo(countDef);
				}
			}	
			
			if (dlg.showModal() == UfoDialog.ID_OK) {
				getItem().getModel().setSelectedItem(null);
				return new Object[] { container, dlg.getCalcInfo(), countFlds, allFlds, areaModel.getDSPK() };
			}			
			return null;
		}

	}

	private void resetCrossFld(AnaRepField fld, CellPosition pos, AreaDataModel areaData) {
		if (areaData == null || fld == null)
			return;
		AnaCrossTableSet crossSet = areaData.getCrossSet();
		if (crossSet == null )
			return;
		if(crossSet.getCrossArea().contain(pos)){
		   crossSet.setDirty(true);
		}
	}

	private boolean isHasChart(IArea area){
		AnaReportModel model = m_plugin.getModel();
		ExAreaModel exAreaModel = ExAreaModel.getInstance(model.getFormatModel());
		ExAreaCell exAreaCell = exAreaModel.getExArea(area);
		if(exAreaCell == null)
			return false;
		if(exAreaCell.getExAreaType() == ExAreaCell.EX_TYPE_CHART)
			return true;
		return false;
	}
	
	private Object[] getCountTypeParams(AnaRepField fld,Field[] countFlds,Field[] allFlds,String strDsPk){
		String msg = checkCountType(fld);
		if (msg != null){
			UfoPublic.sendWarningMessage(msg, m_plugin.getReport());
			getItem().getModel().setSelectedItem(null);
			return null;
		}					
		FieldCountDef countDef = fld.getFieldCountDef();
		if(countDef == null){
			countDef = new FieldCountDef(fld.getField(),getSelectCountType(),null,null,null);
		}
		countDef.setCountType(getSelectCountType());
		getItem().getModel().setSelectedItem(null);
		return new Object[] { m_plugin.getReport(), countDef, countFlds, allFlds, strDsPk };	
	}
	
	private void removeCountDefFld(AnaRepField fld,Cell cell){
		FieldCountDef countDef = fld.getFieldCountDef();
		if(countDef == null)
			return ;
		Field dsDield = m_plugin.getModel().getDataSetTool(fld.getDSPK()).getField(countDef.getMainFldName());
		AnaRepField newAnaField = new AnaRepField(dsDield,AnaRepField.TYPE_DETAIL_FIELD,fld.getDSPK());
		cell.setValue(newAnaField.getFieldLabel());
		cell.setExtFmt(AnaRepField.EXKEY_FIELDINFO, newAnaField);
	}
	
	private int getSelectCountType(){
		return getItem().getSelectedIndex()< 0 ? 0 : getItem().getSelectedIndex();
	}
	
	private Object getSelectItem(){
		return getItem().getSelectedItem();
	}
	
	
	
	/**
	 * create JPopupMenu
	 * @return
	 */
	private JPopupMenu createComboxMenu(){
		JPopupMenu popuMenu = new UFPopupMenu();

		String[] countTypeNamesAry = (new FieldCountDef()).getCountTypeNames();
		for (int i = 0;i<countTypeNamesAry.length;i++) {
			JMenuItem item = new UIMenuItem(countTypeNamesAry[i]);
			item.setActionCommand(String.valueOf(i));
			popuMenu.add(item);
		}				
		return popuMenu;
	}

	/**
	 * @i18n miufo00279=ֻ�������ֵ���͵��ֶμ��� 
	 */
	private String checkCountType(AnaRepField anaField) {		
		if(anaField == null)
			return null;
		Field field = anaField.getField();
		String strFieldName = field.getFldname();
		if(field instanceof FieldCountDef){
			strFieldName = ((FieldCountDef)field).getMainFldName();
		}
		Field oldField = m_plugin.getModel().getDataSetTool(anaField.getDSPK()).getField(strFieldName);
		if(oldField == null)
			return null;
		int iType = getSelectCountType();
		int iFldType = oldField.getDataType();
		if (!(iType == IFldCountType.TYPE_COUNT || iType == IFldCountType.TYPE_MAX || iType == IFldCountType.TYPE_MIN)) {// ���˼���,�����С֮�⣬����ͳ�����Ͷ�Ҫ���ֶ�����ֵ����
			if (!DataTypeConstant.isNumberType(iFldType)) {
				return StringResource.getStringResource("miufo00279");
			}
		}
		return null;
	}
	
	/**
	 * @i18n miufo00280=�Բ���ͳ���ֶ��ϲ��������������!
	 */
	private boolean checkField(AnaRepField anaField){
		if(anaField == null)
			return false;
		FieldCountDef countDef = anaField.getFieldCountDef();
		if(countDef != null){
			showErrorMessage(StringResource.getStringResource("miufo00280"));
			return false;
		}
		return true;
	}
	
	void showErrorMessage(String error) {
		NCOptionPane.showMessageDialog(m_plugin.getReport(), error, null, NCOptionPane.ERROR_MESSAGE);
	}
	
	public JComboBox getItem() {
		if(item == null)
			item = new UIComboBox();
		return item;
	}
	
	public void setItem(JComboBox item) {
		this.item = item;
	}

	private void setFldType(int type) {
		this.m_fldType = type;
	}
}
  