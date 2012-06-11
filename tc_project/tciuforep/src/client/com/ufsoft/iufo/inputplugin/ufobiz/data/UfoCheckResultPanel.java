package com.ufsoft.iufo.inputplugin.ufobiz.data;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.ui.iufo.input.control.RepDataControler;
import nc.ui.iufo.input.edit.RepDataEditor;
import nc.ui.iufo.input.view.KeyCondPanel;
import nc.ui.pub.beans.UIPanel;

import com.ufida.zior.view.Mainboard;
import com.ufsoft.iufo.check.vo.CheckDetailVO;
import com.ufsoft.iufo.check.vo.CheckNoteVO;
import com.ufsoft.iufo.check.vo.CheckResultVO;
import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.inputplugin.biz.formulatrace.FormulaTraceBizUtil;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.IArea;
import com.ufsoft.table.TableStyle;

public class UfoCheckResultPanel extends UIPanel implements IUfoContextKey{
	private static final long serialVersionUID = 3543104879262733502L;
	
	private JScrollPane jScrollPane = null;
	private JList jListCheck = null;


	/**
	 * This method initializes 
	 * 
	 */
	public UfoCheckResultPanel() {
		super();
		initialize();
	}	
	
	/**
	 * @i18n miufo1000151=表内审核结果
	 * @i18n miufo1000150=表间审核结果
	 */
	public void changeRepDataEditor(){
		RepDataControler controler=RepDataControler.getInstance(KeyCondPanel.getMainboard(this));
		RepDataEditor editor=controler.getLastActiveRepDataEditor();
		if (editor==null){
			getJListCheck().setModel(new DefaultListModel());
			return;
		}
			
		CheckResultVO taskCheckResult=controler.getTaskCheckResult(editor);
		CheckResultVO repCheckResult=editor.getRepCheckResult();
		
		List<Object> vListData=new ArrayList<Object>();
		if (repCheckResult!=null){
			CheckDetailVO[] details=getCheckDetails(repCheckResult,false);
			if (details!=null && details.length>0){
				vListData.add(StringResource.getStringResource("miufo1000151"));
				vListData.addAll(Arrays.asList(details));
			}
		}
		
		if (taskCheckResult!=null){
			CheckDetailVO[] details=getCheckDetails(taskCheckResult,true);
			if (details!=null && details.length>0){
				vListData.add(StringResource.getStringResource("miufo1000150"));
				vListData.addAll(Arrays.asList(details));
			}
		}
		
		if(vListData.size()>0){
			getJListCheck().setListData(vListData.toArray());
			CheckDetailVO detail=controler.getTaskCheckDetail(editor);
			if (detail!=null){
				setCurDetail(detail);
				getJListCheck().setSelectedValue(detail, true);
			}else {
				if (editor.getRepCheckDetail()!=null){
					detail=editor.getRepCheckDetail();
					setCurDetail(detail);
					getJListCheck().setSelectedValue(detail,true);
				}
			}
		}
		else
			getJListCheck().setModel(new DefaultListModel());
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.setLayout(new CardLayout());
        this.setSize(531, 286);
        this.add(getJScrollPane(), getJScrollPane().getName());
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setName("jScrollPane");
			jScrollPane.setViewportView(getJListCheck());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jListCheck	
	 * 	
	 * @return javax.swing.JList	
	 */    
	private JList getJListCheck() {
		if (jListCheck == null) {
			jListCheck = new JList();
			jListCheck.setCellRenderer(new ListCellRenderer(){
			    public Component getListCellRendererComponent(
					          JList list,
					          Object value,
					          int index,
					         boolean isSelected,
					          boolean cellHasFocus){
			    	JTextArea area=new JTextArea();
			    	area.setText(value.toString());
			    	area.setBackground(isSelected ? TableStyle.SELECTION_BACKGROUND : Color.white);
			    	
			    	if (value instanceof String){
			    		area.setForeground(new Color(0,0,255));
			    	}
			    	
			    	area.setOpaque(true);
			    	return area;
				}
			});
			jListCheck.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jListCheck.addListSelectionListener(new ListSelectionListener(){
				public void valueChanged(ListSelectionEvent e) {
					if(e.getSource()==jListCheck){
						Object obj=jListCheck.getSelectedValue();
						if (obj instanceof CheckDetailVO){
							CheckDetailVO detail=(CheckDetailVO) jListCheck.getSelectedValue();
							if (detail!=null && jListCheck.getModel()!=null && jListCheck.getModel().getSize()>0)
								setCurDetail(detail);
						}else{
							RepDataControler controler=RepDataControler.getInstance(KeyCondPanel.getMainboard(UfoCheckResultPanel.this));
							RepDataEditor editor=controler.getLastActiveRepDataEditor();
							CheckDetailVO detail=controler.getTaskCheckDetail(editor);
							if (detail==null)
								detail=editor.getRepCheckDetail();
							
							if (detail!=null){
//								controler.clearTaskCheckDetail(editor);
//								editor.setRepCheckDetail(null);
								jListCheck.setSelectedValue(detail,true);
							}
						}

					}
				}
			});
		}
	
		return jListCheck;
	}
	
	private void setCurDetail(CheckDetailVO detail){
		RepDataControler controler=RepDataControler.getInstance(KeyCondPanel.getMainboard(this));
		RepDataEditor editor=controler.getLastActiveRepDataEditor();
		CheckDetailVO oldDetail=controler.getTaskCheckDetail(editor);
		if (oldDetail==null)
			oldDetail=editor.getRepCheckDetail();
		
		boolean bChanged=false;
		if(detail==null && oldDetail!=null){
			bChanged=true;
		}else if(detail!=null && detail.equals(oldDetail)==false)
			bChanged=true;
		
		if(bChanged){
			if (detail.isTaskCheck()){
				controler.setTaskCheckDetail(editor,detail);
				editor.clearCheckDetail();
				controler.setTaskCheckCell(editor,loadCheckCells(this,detail));
				controler.setTaskCheckColor(editor,loadCheckColor(detail));
			}else{
				editor.setRepCheckDetail(detail);
				controler.clearTaskCheckDetail(editor);
				editor.setRepCheckCell(loadCheckCells(this,detail));
				editor.setRepCheckColor(loadCheckColor(detail));
			}
			refreshUfoTable(this);
		}
	}
	
	/**
	 * 根据审核结果返回正确的审核明细信息
	 * @param checkResult
	 * @return
	 */
	private static CheckDetailVO[] getCheckDetails(CheckResultVO checkResult,boolean bTaskCheck) {
		if (checkResult==null)
			return null;
		
		List<CheckNoteVO> vNote=checkResult.getCheckNote();
		if (vNote==null || vNote.size()<=0){
			return null;
		}
		
		Map<String,List<CheckDetailVO>> hashDetail = new HashMap<String, List<CheckDetailVO>>();
		CheckDetailVO[] details = checkResult.getDetailVO();
		if (details!=null){
			for (int i=0;i<details.length;i++){
				details[i].setTaskCheck(bTaskCheck);
				if (details[i]!=null && details[i].getID()!=null){
					if ((details[i].getCheckMsg()!=null && details[i].getCheckMsg().trim().length()>0)
							||(details[i].getDetail()!=null && details[i].getDetail().trim().length()>0)){
						List<CheckDetailVO> vDetail=hashDetail.get(details[i].getID());
						if (vDetail==null){
							vDetail=new ArrayList<CheckDetailVO>();
							hashDetail.put(details[i].getID(),vDetail);
						}
						vDetail.add(details[i]);
					}
				}
			}
		}
		
	    List<CheckDetailVO> vDetailDatas = new ArrayList<CheckDetailVO>();
	    for (int i=0;i<vNote.size();i++){
	    	CheckNoteVO note=vNote.get(i);
	    	if (note==null || note.getFormulaID()==null)
	    		continue;
	    	
	    	List<CheckDetailVO> vDetail=hashDetail.get(note.getFormulaID());
	    	if (vDetail==null || vDetail.size()<=0){
	    		if (note.getNote()==null || note.getNote().trim().length()<=0)
	    			continue;
	    		
	    		vDetail=new ArrayList<CheckDetailVO>();
	    		CheckDetailVO detail=new CheckDetailVO();
	    		detail.setTaskCheck(bTaskCheck);
	    		detail.setCheckMsg(note.getNote());
	    		vDetail.add(detail);
	    	}
	    	vDetailDatas.addAll(vDetail);
	    }
	    
	    return vDetailDatas.toArray(new CheckDetailVO[0]);
	}

	private static List<CellPosition> loadCheckCells(UfoCheckResultPanel pane,CheckDetailVO curDetail) {
		RepDataControler controler=RepDataControler.getInstance(KeyCondPanel.getMainboard(pane));
		RepDataEditor repDataEditor=controler.getLastActiveRepDataEditor();
		
		List<CellPosition> checkCells=null;
		
		String strRepId = repDataEditor.getRepDataParam().getReportPK();
		if (strRepId != null && curDetail != null){
			ArrayList<CellPosition> cells=new ArrayList<CellPosition>();
			Map<String, List<IArea>> mapAreas = curDetail.getRepArea();
			if (mapAreas != null) {
				List<IArea> listAreas=mapAreas.get(strRepId);
				if(listAreas!=null){
					for(IArea area:listAreas){
						List<CellPosition> listTemp=repDataEditor.getCellsModel().getSeperateCellPos(area);
						if(listTemp!=null)
							cells.addAll(listTemp);
					}
					
				}
			}
			Map<String, List<String>> mapMeas =curDetail.getRepMeas();
			if(mapMeas!=null){
				List<String> listMeas=mapMeas.get(strRepId);
				if(listMeas!=null){
					DynAreaModel util=DynAreaModel.getInstance(repDataEditor.getCellsModel());
					for(String strMeas:listMeas){
						CellPosition cellMeas=util.getMeasureModel().getMeasurePosByPK(strMeas);
						if(cellMeas!=null){
							IArea realArea = DynAreaCell.getRealArea(cellMeas,repDataEditor.getCellsModel());
							List<CellPosition> listTemp=repDataEditor.getCellsModel().getSeperateCellPos(realArea);
							if(listTemp!=null)
								cells.addAll(listTemp);
						}
					}
				}
			}
			if(cells.size()>0)
				checkCells=cells;
		}
		return checkCells;
	}
	
	private static void refreshUfoTable(UfoCheckResultPanel pane){		
		Mainboard mainBoard=KeyCondPanel.getMainboard(pane);
		RepDataControler controler=RepDataControler.getInstance(mainBoard);
		
		RepDataEditor repDataEditor=controler.getLastActiveRepDataEditor();
		if (mainBoard.getView(repDataEditor.getId())!=null)
			mainBoard.openView(RepDataEditor.class.getName(),repDataEditor.getId());
		repDataEditor.getTable().repaint();
		
		List<CellPosition> checkCells=controler.getTaskCheckCells(repDataEditor);
		if (checkCells==null)
			checkCells=repDataEditor.getRepCheckCell();
		if(checkCells != null && checkCells.size() > 0){
			IArea firstPos = (IArea)checkCells.get(0);
			FormulaTraceBizUtil.setView2HighlightArea(repDataEditor.getTable(),firstPos,true);				
		}		
	}
	
	private Color loadCheckColor(CheckDetailVO curDetail){		
		Color checkColor=null;
		if(curDetail!=null)
			checkColor=getBgColor(curDetail.getCheckMsg());
		return checkColor;
	}

	private static Color getBgColor(String strCheckMsg) {
		Color color = null;
		if (strCheckMsg.indexOf(CheckResultVO.ERROR) > -1) {
			color = new Color(255, 0, 0);
		} else if (strCheckMsg.indexOf(CheckResultVO.HINT) > -1) {
			color = new Color(252, 218, 218);
		} else if (strCheckMsg.indexOf(CheckResultVO.WARNING) > -1) {
			color = new Color(247, 238, 27);
		}
		return color;
	}
} 
 