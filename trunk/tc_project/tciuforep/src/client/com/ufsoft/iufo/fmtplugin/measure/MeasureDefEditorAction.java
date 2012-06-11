package com.ufsoft.iufo.fmtplugin.measure;

import java.awt.Container;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import nc.vo.iufo.measure.MeasureVO;

import com.ufida.dataset.IContext;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.AreaDataProcess;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.CrossTabDef;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.DataProcessDef;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.GroupLayingDef;
import com.ufsoft.iufo.fmtplugin.datastate.CellsModelOperator;
import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.key.AbsEditorAction;
import com.ufsoft.iufo.fmtplugin.key.KeywordModel;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;

public class MeasureDefEditorAction extends AbsEditorAction implements IUfoContextKey{

	MeasureDefEditorAction(Container owner){
		super(owner);
	}
	
	@Override
	public void execute(Object[] params) {

		Container parent = getParent();
		CellsModel m_cm = getCellsModel();
		CellPosition[] poss = m_cm.getSelectModel().getSelectedCells();
				
		DynAreaModel m_dynAreaModel = DynAreaModel.getInstance(m_cm);
		
		//��̬����������ͽ����������ȡָ��
		for(CellPosition cellPos:poss){
			DynAreaCell dynAreaCell = m_dynAreaModel.getDynAreaCellByPos(cellPos);
			if(dynAreaCell != null){
				AreaDataProcess dataProcess = m_dynAreaModel.getDataProcess(dynAreaCell.getDynAreaPK());
				DataProcessDef dataProcessDef = (dataProcess != null)?dataProcess.getDataProcessDef():null;
				if(dataProcessDef != null && dataProcessDef instanceof CrossTabDef || dataProcessDef instanceof GroupLayingDef){
					UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo1004038"), parent); 
					return;
				}
			}
		}
		
		//�޳�������Ԫ.
		Vector vecPos = new Vector();
		for(int i=0;i<poss.length;i++){
		    CellPosition pos = poss[i];
		    Cell cell = m_cm.getCell(pos);
		    if(cell == null || cell.getValue() == null || cell.getValue().equals("")){
		    	if(getKeyModel().getKeyVOByPos(pos) == null){
		    		vecPos.add(pos);
		    	}
		    }		    
		}
		if(vecPos.size() == 0){
		    UfoPublic.sendWarningMessage(StringResource.getStringResource("uiiufofmt00029"), parent); 
		    return;
		}
		poss = (CellPosition[])vecPos.toArray(new CellPosition[0]);
		
        //�ж��Ƿ����ѡ��.
        
        DynAreaCell curDynAreaCell = null;//�������ѭ��i!=0ʱ,null��������.        
		for (int i = 0; i < poss.length; i++) {
            CellPosition cellPosTmp = poss[i];
            DynAreaCell theDynAreaCell = m_dynAreaModel.getDynAreaCellByPos(cellPosTmp);
            if(i == 0){
                curDynAreaCell = theDynAreaCell;
            }else{
                if(theDynAreaCell == curDynAreaCell){
                    continue;
                }else if(curDynAreaCell == null || theDynAreaCell == null){
                    UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo1001734"),parent);//"ѡ�е������а�����������Ϳɱ����򣬲�������ȡָ�꣡"
                    return;
                }else{
                    UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo1001732"),parent);//"ֻ����ѡ�е�������һ���ɱ����ڣ��������ɱ�����ȡָ�꣡"
                    return;
                }
            }            
        }
		//�жϵ�ǰ�Ƿ����ڶ�̬�����ϵĲ���.
		String dlgTitle;
		if(curDynAreaCell == null){//������
		    dlgTitle = StringResource.getStringResource("miufo1001603"); //"ָ����ȡ";
//		    KeyVO[] htKey = getKeyModel().getMainKeyVOs();		    
//		    if(htKey.length == 0){
//		        UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo1001731"),m_report);//"����δ���ùؼ��֣�������������ָ�꣡"
//		    }
		}else{
		    dlgTitle = StringResource.getStringResource("miufo1001735");//"��̬����ָ����ȡ���ؼ�������";
		}
		
		//��ʼ�����Ի�����в���.
		IContext contextVo = getContextVo();
		String strRepId = contextVo.getAttribute(REPORT_PK) == null ? null : (String)contextVo.getAttribute(REPORT_PK);
		
		boolean isAnaRep = contextVo.getAttribute(ANA_REP) == null ? false : Boolean.parseBoolean(contextVo.getAttribute(ANA_REP).toString());       	
    	
		MeasureDefineTableModel tableModel = new MeasureDefineTableModel(strRepId, m_cm, poss, isAnaRep);
		MeasureDefineDialog dialog = new MeasureDefineDialog(parent, tableModel,getContextVo());
		dialog.setLocationRelativeTo(parent);
		dialog.setTitle(dlgTitle);
		dialog.setVisible(true);
		if(dialog.getResult() == UfoDialog.ID_OK){
			//����
			Vector mtvoVec = dialog.getMeasureDefineTableModel().getVector();
			Boolean sureDelMeasure = null;
			for(int i=0;i<mtvoVec.size();i++){
			    MeasurePosVO mtvo = (MeasurePosVO) mtvoVec.get(i);
			    if(mtvo.getFlag()!=null && mtvo.getFlag().booleanValue()){
			        setMeasureVO(curDynAreaCell,CellPosition.getInstance(mtvo.getActPos()),mtvo.getMeasureVO());
			    }else{
			    	if(sureDelMeasure == null){
			    		int userSel = UfoPublic.showConfirmDialog(parent, StringResource.getStringResource("miufo1000717"),UIManager.getString("OptionPane.titleText"),JOptionPane.YES_NO_OPTION);
			    		sureDelMeasure = Boolean.valueOf(userSel == JOptionPane.OK_OPTION);
			    	}
			    	if(sureDelMeasure.booleanValue()){
			    		setMeasureVO(curDynAreaCell,CellPosition.getInstance(mtvo.getActPos()),null);
			    	}
			    }
            }
			m_cm.setDirty(true);
        }
    
		
	}

	@Override
	public Object[] getParams() {
		
		return null;
	}
	
	
	private void setMeasureVO(DynAreaCell dynAreaCell,CellPosition cellPos,MeasureVO measureVO){
		
		if(dynAreaCell == null){
			getMeasureModel().setMainMeasureVO(cellPos,measureVO);
		}else{
			getMeasureModel().setDynAreaMeasureVO(dynAreaCell.getDynAreaVO().getDynamicAreaPK(),cellPos,measureVO);
		}
	}
	private KeywordModel getKeyModel(){
		return CellsModelOperator.getKeywordModel(getCellsModel());
	}
	private MeasureModel getMeasureModel(){
		return CellsModelOperator.getMeasureModel(getCellsModel());
	}

}
