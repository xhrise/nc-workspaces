package com.ufsoft.iufo.inputplugin.biz.data;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
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

import nc.ui.pub.beans.UIPanel;

import com.ufsoft.iufo.check.vo.CheckDetailVO;
import com.ufsoft.iufo.check.vo.CheckNoteVO;
import com.ufsoft.iufo.check.vo.CheckResultVO;
import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.inputplugin.biz.formulatrace.FormulaTraceBizUtil;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.report.UICloseableTabbedPane;
import com.ufsoft.report.UfoReport;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.IArea;
import com.ufsoft.table.TableStyle;

public class CheckResultPanel extends UIPanel implements IUfoContextKey{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JScrollPane jScrollPane = null;
	private JList jListCheck = null;

	private UfoReport m_ufoReport = null;
	private CheckDetailVO[] m_details=null;
	/**
	 * 审核信息所属报表id,对于表间审核为null
	 */
	private String m_strRepId=null;
	
	private CheckDetailVO m_curDetail=null;
	private List<CellPosition> m_checkCells=null;
	private Color m_checkColor=null;
	private String panelName;
	/**
	 * This method initializes 
	 * 
	 */
	public CheckResultPanel(UfoReport ufoReport,String name) {
		super();
		m_ufoReport=ufoReport;
		panelName=name;
		initialize();
	}
	public UfoReport getReport(){
		return m_ufoReport;
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
			    	
			    	area.setOpaque(true);
			    	return area;
				}
			});
			jListCheck.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jListCheck.addListSelectionListener(new ListSelectionListener(){

				public void valueChanged(ListSelectionEvent e) {
					if(e.getSource()==jListCheck){
						CheckDetailVO detail=(CheckDetailVO) jListCheck.getSelectedValue();
						setCurDetail(detail);

					}
					
				}
				
			});
		}
	
		return jListCheck;
	}
	private void setCurDetail(CheckDetailVO detail){
		boolean bChanged=false;
		if(detail==null && m_curDetail!=null){
			bChanged=true;
		}else if(detail!=null && detail.equals(m_curDetail)==false)
			bChanged=true;
		
		if(bChanged){
			m_curDetail=detail;
			setCheckCells();
			setColor();
			refreshUfoTable();
		}
			
	}
	/**
	 * 改变当前报表
	 * @param strNewRepPK
	 */
	public void changeReport(String strNewRepPK,boolean isFresh){
		if(isFresh||(strNewRepPK!=null && m_strRepId!=null &&
				strNewRepPK.equals(m_strRepId)==false)){
			reset();
			getJListCheck().setModel(new DefaultListModel());
			if(getParent()!=null&&getParent() instanceof UICloseableTabbedPane){
				UICloseableTabbedPane parent=(UICloseableTabbedPane)getParent();
				if (panelName!=null&&parent.indexOfTab(panelName) >= 0) {
					parent.removeTabAt(panelName);

				}
			}
		}
		else
			getJListCheck().clearSelection();
	}
	
	public void init(CheckResultVO checkResult){
		reset();
		
		m_details=checkResult==null?null:checkResult.getDetailVO();
		m_strRepId=checkResult==null?null:checkResult.getRepId();
		m_details = getCheckDetails(checkResult);
		
		if(m_details!=null)
			getJListCheck().setListData(m_details);
		else
			getJListCheck().setModel(new DefaultListModel());

	}
	
	/**
	 * 根据审核结果返回正确的审核明细信息
	 * @param checkResult
	 * @return
	 */
	private CheckDetailVO[] getCheckDetails(CheckResultVO checkResult) {
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
	    		detail.setCheckMsg(note.getNote());
	    		vDetail.add(detail);
	    	}
	    	vDetailDatas.addAll(vDetail);
	    }
	    
	    return vDetailDatas.toArray(new CheckDetailVO[0]);
	}
	
	private void reset(){
		m_curDetail=null;
		m_checkCells=null;
		m_checkColor=null;
		m_details=null;
		m_strRepId=null;
	}


	
	private void setCheckCells() {

		m_checkCells=null;
		String strRepId = m_ufoReport.getContextVo().getAttribute(REPORT_PK) == null ? null : (String)m_ufoReport.getContextVo().getAttribute(REPORT_PK);
		if (strRepId != null && m_curDetail != null) {
			
			ArrayList<CellPosition> cells=new ArrayList<CellPosition>();
			Map<String, List<IArea>> mapAreas = m_curDetail.getRepArea();
			if (mapAreas != null) {
				List<IArea> listAreas=mapAreas.get(strRepId);
				if(listAreas!=null){
					for(IArea area:listAreas){
						List<CellPosition> listTemp=m_ufoReport.getTable().getCellsModel().getSeperateCellPos(area);
						if(listTemp!=null)
							cells.addAll(listTemp);
					}
					
				}
			}
			Map<String, List<String>> mapMeas =m_curDetail.getRepMeas();
			if(mapMeas!=null){
				List<String> listMeas=mapMeas.get(strRepId);
				if(listMeas!=null){
					DynAreaModel util=DynAreaModel.getInstance(m_ufoReport.getTable().getCellsModel());
					for(String strMeas:listMeas){
						CellPosition cellMeas=util.getMeasureModel().getMeasurePosByPK(strMeas);
						if(cellMeas!=null){
							IArea realArea = DynAreaCell.getRealArea(cellMeas, m_ufoReport.getCellsModel());
							List<CellPosition> listTemp=m_ufoReport.getTable().getCellsModel().getSeperateCellPos(realArea);
							if(listTemp!=null)
								cells.addAll(listTemp);
						}
					}
				}
			}
			if(cells.size()>0)
				m_checkCells=cells;
		}
	}
	private void refreshUfoTable(){		
		m_ufoReport.getTable().getCells().repaint();
		if(m_checkCells != null && m_checkCells.size() > 0){
			IArea firstPos = (IArea)m_checkCells.get(0);
			FormulaTraceBizUtil.setView2HighlightArea(m_ufoReport.getTable(),firstPos,true);				
		}		
	}
	private void setColor(){
		if(m_curDetail==null)
			m_checkColor=null;
		else
			m_checkColor=getBgColor(m_curDetail.getCheckMsg());
	}

	public List<CellPosition> getCheckCells(){
		return m_checkCells;
	}
	public Color getCheckColor(){
		return m_checkColor;
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
	

}  //  @jve:decl-index=0:visual-constraint="10,10"
