package com.ufsoft.iufo.fmtplugin.dynarea;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.formula.FormulaModel;
import com.ufsoft.iufo.fmtplugin.formula.UfoFmlExecutor;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaVO;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.IArea;
import com.ufsoft.table.format.Format;
import com.ufsoft.table.format.TableConstant;

/**
 * ����Ϊ��̬��
 * @author chxw
 */
public abstract class AbsDynAreaExt extends AbsActionExt implements IUfoContextKey{
	private DynAreaDefPlugIn m_dynAreaPlugIn = null;
	/**
	 * ����ӵĶ�̬�����󻺴�:AreaPosition-DynAreaCell
	 */
	private Hashtable<AreaPosition, DynAreaCell> m_addedDynArea = new Hashtable<AreaPosition, DynAreaCell>();
	/**
	 * �޸�ǰ��̬�����󻺴�:AreaPosition-DynAreaCell
	 */
	private Hashtable<AreaPosition, DynAreaCell> m_updatedDynArea = new Hashtable<AreaPosition, DynAreaCell>();
	/**
	 * ɾ���Ķ�̬�����󻺴�:���ɾ���Ķ�������һ�ε���updateReport()������ӵ�,ֱ��ɾ���������Map��
	 */
	private ArrayList<DynAreaCell> m_removedDynArea = new ArrayList<DynAreaCell>();
	
	// @edit by ll at 2009-5-14,����02:48:10 �޸�ʱ��¼ԭʼVO���Ա�ȡ��ʱ���лָ�
	private Hashtable<String, DynAreaVO> m_UpdateOldVO = new Hashtable<String, DynAreaVO>();
	/**
	 * ����̬����ʵ����
	 */
	private List<DynAreaCell> m_dynAreas = null;
	
	/**
	 * ���캯��
	 * @param dynAreaPlug
	 */
	public AbsDynAreaExt(DynAreaDefPlugIn dynAreaPlug){
		m_dynAreaPlugIn = dynAreaPlug;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ufsoft.report.menu.ICommandExt#getCommand()
	 */
	public UfoCommand getCommand() {
		return null;
	}

	/**
	 * ��̬��������ʵ�֣�
	 * DynAreaMngDlg.ID_ADDΪ�Ҽ���Ӷ�̬����DynAreaMngDlg.ID_UPDATEΪ�޸Ķ�̬����DynAreaMngDlg.ID_DELETEΪɾ����̬����
	 * 
	 * @param report
	 * @i18n uiiufofmt00001=������Ӷ�̬����:
	 * @i18n uiiufofmt00002=����ɾ����̬����:
	 */
	protected void dynAreaMngImpl(UfoReport report){
		// @edit by wangyga at 2009-1-22,����02:24:37 ���Ӷ�̬�����õ�Ĭ�ϱ���
		boolean isAddOrUpdate = true;
		for (;;) {
			//ֱ����ʾ��̬�������Ի���
			DynAreaMngDlg dlgDynAreaMng = new DynAreaMngDlg(getReport().getTable().getCells(), m_dynAreas.toArray(new DynAreaCell[0])); 
			dlgDynAreaMng.show(); 
			int nRet = dlgDynAreaMng.getResult();
			if(nRet == UfoDialog.ID_OK){//������̬��������ӡ�ɾ�����͸������õ���̬����ҵ��ģ����
				
				boolean haschanged = refreshDynArea();
                if(isAddOrUpdate && haschanged)
                	setDynAreaProperty();		
					
				break;
			} else if(nRet == UfoDialog.ID_CANCEL){
				cancelUpdateDynArea();
				break;
			}
			
			switch (nRet) {
				case DynAreaMngDlg.ID_ADD://��Ӷ�̬�������
					addDynAreaImpl(dlgDynAreaMng, null);				
					continue;
				case DynAreaMngDlg.ID_UPDATE://�޸Ķ�̬����  
					updateDynAreaImpl(dlgDynAreaMng);
					continue;
				case DynAreaMngDlg.ID_DELETE://ɾ����̬����
					delDynAreaImpl(dlgDynAreaMng);
					isAddOrUpdate = false;
					continue;
			}
		}     
	}

	/**
	 * �򿪶�̬�������öԻ������һ���µĶ�̬���򵽶�̬�������Ի���
	 * @param dlgDynAreaMng
	 * @param area
	 * @param isContinue true/false �Ƿ����ѭ��
	 */
	protected boolean addDynAreaImpl(DynAreaMngDlg dlgDynAreaMng, IArea area){
		DynAreaSetDlg dynAreaSetDlg = new DynAreaSetDlg(getReport().getTable().getCells(),getReport().getCellsModel());
		dynAreaSetDlg.setModal(false);                    
		
		//��5.1�汾���ӹ��ܣ�����ѡ������ֱ�ӵ�����̬�������öԻ���
		if(area != null){
			dynAreaSetDlg.setArea(AreaPosition.getInstance(area.toString()));
		}
        DynAreaModel dynAreaModel = getDynAreaModel();
		//��Ϊ���еĶ�̬����ֻ����һ����������ȡ��̬�������Ķ�̬������չ����
		if(dlgDynAreaMng == null && dynAreaModel.getDynAreaDirection() != DynAreaVO.DIRECTION_UNDEFINED){
			dynAreaSetDlg.setDirection(dynAreaModel.getDynAreaDirection());
			dynAreaSetDlg.setDirectionRead();
		} else if(dlgDynAreaMng != null && dlgDynAreaMng.getDynAreaDirection() != DynAreaVO.DIRECTION_UNDEFINED){
			dynAreaSetDlg.setDirection(dlgDynAreaMng.getDynAreaDirection());
			dynAreaSetDlg.setDirectionRead();
		}
		dynAreaSetDlg.show();

		//5.1�汾���ӹ��ܣ�����ѡ������ֱ�ӵ�����̬�������öԻ���
		//����û����ȡ����ť����ֱ�ӹرն�̬�������öԻ��򷵻�
		if(area != null && dynAreaSetDlg.getResult() == UfoDialog.ID_CANCEL){
			return false;
		} else if (dynAreaSetDlg.getResult() == UfoDialog.ID_OK) {
			DynAreaVO newDynAreaVO = new DynAreaVO(dynAreaSetDlg.getArea(), dynAreaSetDlg.getDirection()); 
			DynAreaCell newDynCell = new DynAreaCell(newDynAreaVO.getOriArea(), newDynAreaVO);
			String errorStr = dynAreaModel.check(newDynCell);
			if(errorStr == null)
				errorStr = isIntersectWithAddOrUpdate(dynAreaSetDlg.getArea());
			
			if(errorStr == null){
				m_addedDynArea.put(newDynCell.getArea(),newDynCell);//���뵽���Ӷ�̬���򻺴���
				m_dynAreas.add(newDynCell);
			} else{
				UfoPublic.sendErrorMessage(errorStr,getReport(),null);
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * �򿪶�̬�������öԻ����޸��Ѵ��ڵĶ�̬��������
	 * @param dlgDynAreaMng
	 * @param model
	 */
	private void updateDynAreaImpl(DynAreaMngDlg dlgDynAreaMng){
		DynAreaSetDlg dynAreaUpdateDlg = new DynAreaSetDlg(getReport().getTable().getCells(),getReport().getCellsModel());
		dynAreaUpdateDlg.setModal(false);                        
		
		DynAreaCell oldDynAreaCell = dlgDynAreaMng.getCurrentSelectedDynAreaCell();
		dynAreaUpdateDlg.setArea(oldDynAreaCell.getArea());
		dynAreaUpdateDlg.setDirection(oldDynAreaCell.getDynAreaVO().getDirection());
		//if (model.getDynAreaSize() > 1) {//�����ǰ��̬�����Ѿ�����1���������ܸ���
		//�޸Ķ�̬��ʱ�������޸ķ���
		dynAreaUpdateDlg.setDirectionRead();
		//}
		dynAreaUpdateDlg.show();

		if (dynAreaUpdateDlg.getResult() == UfoDialog.ID_OK) { 
			//����ģ����:����޸ĵ�����������������,ֱ�ӷ����¶���Map��,������ڸ��¶���Map��
			if(m_addedDynArea.get(oldDynAreaCell.getArea()) != null){
				m_addedDynArea.remove(oldDynAreaCell.getArea());
				String errorStr = isIntersectWithAddOrUpdate(dynAreaUpdateDlg.getArea());
				if(errorStr == null){
					oldDynAreaCell.setArea(dynAreaUpdateDlg.getArea());
					oldDynAreaCell.getDynAreaVO().setOriArea(dynAreaUpdateDlg.getArea());
					m_addedDynArea.put(dynAreaUpdateDlg.getArea(), oldDynAreaCell);
				} else{
					m_addedDynArea.put(oldDynAreaCell.getArea(), oldDynAreaCell);
					UfoPublic.sendErrorMessage(errorStr,getReport(),null);
				}
			} else{  
				DynAreaModel dynAreaModel = getDynAreaModel();
				String errorStr = dynAreaModel.checkUpdate(oldDynAreaCell, dynAreaUpdateDlg.getArea(), dynAreaUpdateDlg.getDirection());
				if(errorStr == null)
					errorStr = isIntersectWithAddOrUpdate(dynAreaUpdateDlg.getArea());
				if(errorStr == null){
					DynAreaCell newDynAreaCell = (DynAreaCell) oldDynAreaCell.clone();
					// @edit by ll at 2009-5-14,����03:14:04
					newDynAreaCell.setValue(oldDynAreaCell.getDynAreaVO().clone());//DynAreaCell��value�����ƣ��ᵼ�¶�̬������vo
					
					newDynAreaCell.setArea(dynAreaUpdateDlg.getArea());
					newDynAreaCell.getDynAreaVO().setOriArea(dynAreaUpdateDlg.getArea());
					m_updatedDynArea.put(dynAreaUpdateDlg.getArea(), newDynAreaCell);
					String dynPK = newDynAreaCell.getDynAreaPK();
					if(!m_UpdateOldVO.containsKey(dynPK))//ֻ�ڵ�һ�μ�¼��ԭʼ��cell
						m_UpdateOldVO.put(dynPK, oldDynAreaCell.getDynAreaVO());
					int index = m_dynAreas.indexOf(oldDynAreaCell);
					m_dynAreas.remove(oldDynAreaCell);
					m_dynAreas.add(index,newDynAreaCell);
				} else{
					UfoPublic.sendErrorMessage(errorStr,getReport(),null);
				}
			}
		}
	}
	
	/**
	 * �򿪶�̬�������öԻ���ɾ��ѡ���Ķ�̬����
	 * @param dlgDynAreaMng
	 * @param model
	 */
	private void delDynAreaImpl(DynAreaMngDlg dlgDynAreaMng){
		DynAreaSetDlg dynAreaUpdateDlg = new DynAreaSetDlg(getReport().getTable().getCells(),getReport().getCellsModel());
		dynAreaUpdateDlg.setModal(false);
		DynAreaCell deleteDyaAreaCell = dlgDynAreaMng.getCurrentSelectedDynAreaCell();

		//���ɾ�����������ӵ�����,ֱ��ɾ��,���򻺴浽m_removedDynArea�����ڻ�ԭ
		try {
			DynAreaModel dynAreaModel = getDynAreaModel();
			dynAreaModel.checkRemove(deleteDyaAreaCell);
			
			if(m_addedDynArea.get(deleteDyaAreaCell.getArea()) == null){
				m_removedDynArea.add(deleteDyaAreaCell);
			} else{
				m_addedDynArea.remove(deleteDyaAreaCell.getArea());
			}
			m_dynAreas.remove(deleteDyaAreaCell);
		} catch(Exception e) {
			UfoPublic.sendErrorMessage(StringResource.getStringResource("uiiufofmt00002") + e.getMessage(),getReport(),e);
		}               
	}
	
	/**
	 * �����ӵġ�ɾ���ġ��͸��µĶ�̬�������õ���̬����ģ����
	 */
	private boolean refreshDynArea(){
		boolean hasChanged = false;
		DynAreaModel model =  m_dynAreaPlugIn.getDynAreaModel(); 
		CellsModel cellsModel = getReport().getCellsModel();
		FormulaModel formulaModel = FormulaModel.getInstance(cellsModel);
		UfoFmlExecutor ufoFmlExecutor = formulaModel.getUfoFmlExecutor();
		for(Iterator iter = m_removedDynArea.iterator();iter.hasNext();){
			String strDynPK=((DynAreaCell)iter.next()).getDynAreaVO().getDynamicAreaPK();
			String strRepId = getReport().getContextVo().getAttribute(REPORT_PK) == null ? null : (String)getReport().getContextVo().getAttribute(REPORT_PK);
			
			model.removeDynArea(strRepId,strDynPK);
			//ɾ����̬����ʽ��
			
//			FormulaDefPlugin formPlugIn=(FormulaDefPlugin) getReport().getPluginManager().getPlugin(FormulaDefPlugin.class.getName());
//			if(formPlugIn!=null && formPlugIn.getFmlExecutor()!=null){
//				formPlugIn.getFmlExecutor().removeDynamicArea(strDynPK);
//			}
			ufoFmlExecutor.removeDynamicArea(strDynPK);
			m_dynAreaPlugIn.setDirty(true);
			hasChanged = true;
		}
		
		for(Enumeration enumer = m_updatedDynArea.keys(); enumer.hasMoreElements();){
			AreaPosition area = (AreaPosition) enumer.nextElement();
			String strRepId = getReport().getContextVo().getAttribute(REPORT_PK) == null ? null : (String)getReport().getContextVo().getAttribute(REPORT_PK);
			
			DynAreaCell dCell = (DynAreaCell)m_updatedDynArea.get(area);
			if(m_UpdateOldVO.containsKey(dCell.getDynAreaPK())){//��Ҫ����vo��������ã���Ϊupdate�����и��Ƴ����¶���
				DynAreaVO vo = m_UpdateOldVO.get(dCell.getDynAreaPK());
				vo.setOriArea(dCell.getDynAreaVO().getOriArea());
				dCell.setValue(vo);
			}
			model.updateDynArea(strRepId,area,dCell);
			//ɾ����̬����ʽ��
			String strDynPK=((DynAreaCell)m_updatedDynArea.get(area)).getDynAreaPK();
//			FormulaDefPlugin formPlugIn=(FormulaDefPlugin) getReport().getPluginManager().getPlugin(FormulaDefPlugin.class.getName());
//			if(formPlugIn!=null && formPlugIn.getFmlExecutor()!=null){
//				formPlugIn.getFmlExecutor().updateDynamicArea(strDynPK);
//			}
			ufoFmlExecutor.removeDynamicArea(strDynPK);
			m_dynAreaPlugIn.setDirty(true);
			hasChanged = true;
		}
		
		for(Enumeration enumer = m_addedDynArea.keys(); enumer.hasMoreElements();){
			AreaPosition area = (AreaPosition) enumer.nextElement();
			model.addDynArea((DynAreaCell)m_addedDynArea.get(area));
			m_dynAreaPlugIn.setDirty(true);
			hasChanged = true;
		}
		return hasChanged;
	}
	/**
	 * �����µĶ�̬�������ý��лָ�
	 */
	private void cancelUpdateDynArea(){
		if(m_UpdateOldVO.size()==0)
			return;
		for(Enumeration enumer = m_updatedDynArea.keys(); enumer.hasMoreElements();){
			AreaPosition area = (AreaPosition) enumer.nextElement();
			DynAreaCell dCell = (DynAreaCell)m_updatedDynArea.get(area);
			String key = dCell.getDynAreaPK();
			if(m_UpdateOldVO.containsKey(key)){
				dCell.setValue(m_UpdateOldVO.get(key));
				m_UpdateOldVO.remove(key);
			}
		}
	}
	/**
	 * ���ѡ������Ԫ���Ƿ�����ӵĻ��߸��µĶ�̬���������ཻ
	 * @return
	 */
	private String isIntersectWithAddOrUpdate(AreaPosition area){
		if(m_dynAreas == null || m_dynAreas.size() == 0 || area == null){
			return null;
		}
		
		//�Ƿ�����ӵĶ�̬���ཻ
		for(DynAreaCell dynAreaCell : m_addedDynArea.values()){
			if(dynAreaCell.getArea().intersection(area)){
				return StringResource.getStringResource("miufo1001776");
			}
		}
		
		//�Ƿ�����µĶ�̬���ཻ
		for(DynAreaCell dynAreaCell : m_updatedDynArea.values()){
			if(dynAreaCell.getArea().intersection(area)){
				return StringResource.getStringResource("miufo1001776");
			}
		}
		return null;
	}
	
	/**
	 * ���ö�̬����Ĭ�ϵ�Ԫ����
	 * @create by wangyga at 2008-12-26,����01:17:06
	 *
	 * @param area
	 */
	private void setDynAreaProperty(){
		
		CellsModel cellsModel = getReport().getCellsModel();
		IArea area = cellsModel.getSelectModel().getSelectedArea();
		CellPosition[] cellPoses = area.split();
		for(CellPosition pos : cellPoses){
			IufoFormat format =  (IufoFormat)cellsModel.getCellFormatIfNullNew(pos);
			
			format.setLineType(Format.TOPLINE, TableConstant.L_SOLID1);
			format.setLineType(Format.BOTTOMLINE, TableConstant.L_SOLID1);
			format.setLineType(Format.LEFTLINE, TableConstant.L_SOLID1);
			format.setLineType(Format.RIGHTLINE, TableConstant.L_SOLID1);
			format.setLineColor(Format.TOPLINE, Color.BLUE);
			format.setLineColor(Format.BOTTOMLINE, Color.BLUE);
			format.setLineColor(Format.LEFTLINE, Color.BLUE);
			format.setLineColor(Format.RIGHTLINE, Color.BLUE);
		}
	}
	
	/**
	 * ����������
	 */
	private void clear(){
		DynAreaModel dynAreaModel = getDynAreaModel();
		m_dynAreas = new ArrayList<DynAreaCell>(Arrays.asList(dynAreaModel.getDynAreaCells()));
		
		m_addedDynArea.clear();
		m_updatedDynArea.clear();
		m_removedDynArea.clear();
	}
	
	/**
	 * @return ���ر����ߡ�
	 */
	private UfoReport getReport() {
		return this.getDynAreaPlugIn().getReport();
	}
	
	private DynAreaModel getDynAreaModel(){
		return DynAreaModel.getInstance(getReport().getCellsModel());
	}
	
	/**
	 * @return ���ض�̬��������
	 */
	public DynAreaDefPlugIn getDynAreaPlugIn() {
		return m_dynAreaPlugIn;
	}
	
	@Override
	public Object[] getParams(UfoReport report){
		clear();
		excuteImpl(report);
		return null;
	}
	
	@Override
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
        uiDes.setName(getDesName());
        uiDes.setPaths(new String[]{MultiLang.getString("format")});
        uiDes.setGroup(DynAreaDefPlugIn.MENU_GROUP);
        uiDes.setShowDialog(true);
        ActionUIDes uiDes2 = (ActionUIDes)uiDes.clone();
        uiDes2.setPopup(true);
        uiDes2.setPaths(new String[]{});
        return new ActionUIDes[]{uiDes, uiDes2};
	}

	public abstract String getDesName(); 
	public abstract void excuteImpl(UfoReport report);
}
