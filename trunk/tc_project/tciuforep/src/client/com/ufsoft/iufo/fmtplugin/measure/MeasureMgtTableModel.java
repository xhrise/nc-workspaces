package com.ufsoft.iufo.fmtplugin.measure;	

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import nc.pub.iufo.cache.ReportCache;
import nc.util.iufo.pub.UFOString;
import nc.vo.iuforeport.rep.ReportVO;

import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.iufo.fmtplugin.formatcore.CacheProxy;
import com.ufsoft.iufo.fmtplugin.key.KeywordModel;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.exception.MessageException;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.TableUtilities;
import com.ufsoft.table.format.TableConstant;
/**
@update
	修改显示信息
@end
 * 此处插入类型描述。
 * 创建日期：(2002-5-13 19:12:56)
 * @author：王海涛
 */
public class MeasureMgtTableModel extends AbstractTableModel {

	public static final int CELL_COLUMN = 0;
	public static final int NAME_COLUMN = 1;	
	public static final int TYPE_COLUMN = 2;
	public static final int REPORT_COLUMN = 3;
	public static final int TABNAME_COLUMN = 4;
	public static final int TABCOLUMN_COLUMN = 5;

    //是否进行过数据处理，如果进行过数据处理，则不允许进行指标迁移
    private boolean m_bIsProcessed = false;
//  指标列表.构建本类时初始化.因为指标管理只有修改位置的作用,所以点OK按钮时更新CellsModel即可.	
	Vector m_vecMeasure = new Vector();
	public static final MeasureColumnModel columnNames[]
		= {new MeasureColumnModel(StringResource.getStringResource("miufo1001529"),60, JLabel.CENTER),     //"单    元"
		   new MeasureColumnModel(StringResource.getStringResource("miufopublic262"),100, JLabel.CENTER),  //"指标名称"
		   new MeasureColumnModel(StringResource.getStringResource("miufo1000773"),100, JLabel.CENTER),    //"指标类型"
		   new MeasureColumnModel(StringResource.getStringResource("miufo1001530"),100, JLabel.CENTER),    //"所属报表"
		   new MeasureColumnModel(StringResource.getStringResource("miufo1004039"),100, JLabel.CENTER),    //"数据表表名"
		   new MeasureColumnModel(StringResource.getStringResource("miufo1004040"),100, JLabel.CENTER)};   //"数据表列名"
	private CellsModel m_cellsModel = null;
/**
 * TargerData 构造子注解。
 */
public MeasureMgtTableModel(CellsModel cModel, Vector measurePosVOVector,boolean isProcessed) {
	super();	
	m_cellsModel = cModel;
	m_vecMeasure = measurePosVOVector;
    this.m_bIsProcessed = isProcessed;    
}


	public void addEmlment(MeasurePosVO vo)
	{
		if(vo != null)
		{
			m_vecMeasure.addElement(vo);
		}
	}
	private CellsModel getCellsModel(){
	    return m_cellsModel;
	}
/**
 * 按照所选的列排序
 * 创建日期：(2002-6-3 16:24:47)
 * @param table javax.swing.JTable
 */
public void addMouseListener(final JTable ta)
{
	ta.getTableHeader().addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent event)
			{
				int col = ta.columnAtPoint(event.getPoint());
				Hashtable htable = new Hashtable();
				MeasurePosVO vo ;

				Vector vec = new Vector();			
				for( int i = 0 ; i < m_vecMeasure.size() ; i++ )
				{
					vo = ( MeasurePosVO )m_vecMeasure.get(i);
					String flag = vo.getActPos()+vo.getMeasureVO().getCode();//作为排序后获取对象的标示
					switch(col)
					{
						case CELL_COLUMN:
							htable.put(flag,vo);
							vec.add( vo.getActPos() +"," +flag);
							break;
						case NAME_COLUMN:
							htable.put(flag,vo);
							vec.add( vo.getMeasureVO().getName() +"," +flag);
							break;
						case TYPE_COLUMN:
							htable.put(flag,vo);
							vec.add(MeasureDefineTableModel.TYPES[vo.getMeasureVO().getType()] +"," + flag);						
							break;
						case REPORT_COLUMN:
							htable.put(flag,vo);
							vec.add(getReportColunmLabel(vo.getMeasureVO().getReportPK()) +"," + flag);	
							break;
						case TABNAME_COLUMN:
							htable.put(flag,vo);
							vec.add(vo.getMeasureVO().getDbtable() +"," +flag);
							break;
						case TABCOLUMN_COLUMN:
							htable.put(flag,vo);
							vec.add(vo.getMeasureVO().getDbcolumn() +"," +flag);
							break;
						default :break;
					}
				}
				Collections.sort(vec, new Comparator(){
	                public int compare(Object a, Object b){
	                    if(a != null && b != null){
	                        String aName = (String)a;
	                        String bName = (String)b;
	                        return UFOString.compareHZString(aName, bName);
	                    }
	                    return -1;
	                }
	            });
				
				m_vecMeasure.clear();
				
				for(int i = 0;i < vec.size();i++){
					String str = (String) vec.get(i);
					String[] tmp = str.split(",");
					String flag = tmp[tmp.length-1];
					m_vecMeasure.addElement((MeasurePosVO)htable.get(flag));
				}
					
				
				fireTableDataChanged();
			}
		});
	}
	

	public Class getColumnClass(int c)
	{
	    return String.class;
	}
	public int getColumnCount()
	{
		return columnNames.length;
	}
	public String getColumnName(int c)
	{
		return columnNames[c].title;
	}
	public int getRowCount()
	{
		return m_vecMeasure.size();
	}
	public Object getValueAt(int r,int c)
	{
		MeasurePosVO vo = ( MeasurePosVO )m_vecMeasure.get(r);
        if(vo == null || vo.getMeasureVO() == null)
            return "";
		
		switch(c)
		{
			case CELL_COLUMN: return vo.getActPos();
			case NAME_COLUMN: return vo.getMeasureVO().getName();
			case TYPE_COLUMN: return MeasureDefineTableModel.TYPES[vo.getMeasureVO().getType()];
			case REPORT_COLUMN: return getReportColunmLabel(vo.getMeasureVO().getReportPK());
			case TABNAME_COLUMN: return vo.getMeasureVO().getDbtable();
			case TABCOLUMN_COLUMN: return vo.getMeasureVO().getDbcolumn();
		}
		return "";
	}
	
	private static String getReportColunmLabel(String repid){
		if(repid!= null && repid.length() > 0)
		{
			ReportCache reportCache = CacheProxy.getSingleton().getReportCache();
			ReportVO repvo = (ReportVO )reportCache.get(repid);
			if(repvo != null)
				return "(" + repvo.getCode() + ")"+repvo.getName();
		}
		return "";
	}
	public boolean isCellEditable(int r,int c)
	{
		if(c == CELL_COLUMN&&(!m_bIsProcessed))
        {
            return true;
        }
        return false;
	}

	/**
	 * 此处插入方法描述。
	 * 创建日期：(2002-5-9 16:37:39)
	 * @param Aera java.lang.String
	 */
	public void setValueAt(Object obj,int r,int c)
	{
        MeasurePosVO vo = ( MeasurePosVO )m_vecMeasure.get(r);
        if(c == CELL_COLUMN)
        {
            String cell = ( (String)obj).trim();
            if(cell.length() == 0){
                UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo1001531"),null);  //"请输入单元位置！"
            } else{
				try {
					CellPosition cellPos = TableUtilities.getCellPosByString(cell);
					if(cell == null){
					    throw new Exception();
					}
					//检查输入的单元是否合已设置的指标位置冲突，是，提示用户错误
					checkCell(cellPos, vo);
					vo.setActPos(cell);
				} catch (MessageException ce) {
					UfoPublic.sendMessage(ce, null);
				} catch (Exception e) {
					UfoPublic.sendWarningMessage(StringResource
							.getStringResource("miufo1001067"),null); //"请输入正确的单元位置！"
				}
			}
        }
	}

   
/**
 * @return Returns the m_vecMeasure.
 */
public Vector getUpdatedMeasures() {
    return m_vecMeasure;
}



//private void checkCell(CellPosition cellPos,MeasurePosVO vo) throws MessageException
//{
//  //指标所在新位置属性
//  Format cp;
//  //指标新的实际位置
////  UfoArea actArea = new UfoArea();
//  //主表内和动态区内的指标位置不能重复
//  //位置没有改变，直接返回
////  UfoArea oldArea = new UfoArea(vo.getActPos());
//  if (vo.getActPos().equals(cellPos.toString())){
//		return;
//	}
////  String areaName = ufoarea.Start.getName();
//  //表样单元不允许定义为指标
//  Cell cell = getCellsModel().getCell(cellPos);
//	cp = cell==null?null:cell.getFormat();
//	if (cp != null) {
//		if (cp.getCellType() == TableConstant.CELLTYPE_SAMPLE) {
//			String strMsg = StringResource.getStringResource("miufo1001532"); //"指标不能定义在表样单元上！"
//			throw new MessageException(strMsg);
//		}
//	}
//
//	if (cell != null) {
//	    CellPosition pos = CellPosition.getInstance(cell.getRow(), cell.getCol());
//	    
//	    MeasurePosVO posVO = null;
//	    Enumeration enum = m_vecMeasure.elements();
//	    for(Enumeration e = m_vecMeasure.elements(); e.hasMoreElements();){
//	        posVO = (MeasurePosVO) e.nextElement();
//	        if(posVO.getActPos().equalsIgnoreCase(cellPos.toString())){
//	            String strMsg = StringResource.getStringResource("miufo1001533"); //"为该指标制定的新的位置已经设置了指标，请重新设置指标位置！"
//			    throw new MessageException(strMsg);
//	        }
//	    }		   
//	}
//      if(cellPos.getRow() > this.getCellsModel().getMaxRow()
//         ||cellPos.getColumn() > this.getCellsModel().getMaxCol())   {
//      	String msg = StringResource.getStringResource("uiuforep000026",new Object[]{cellPos.toString()});
//          throw new MessageException(msg);
//      }
//      for(Iterator iter = m_vecMeasure.iterator();iter.hasNext();){
//		    MeasurePosVO mtvoTmp = (MeasurePosVO)iter.next();
//		    if(mtvoTmp.getActPos().equals(cellPos)){
//				String strMsg = StringResource.getStringResource("miufo1001536"); //"已经在该位置上设置了指标，不允许覆盖设置！"
//				throw new MessageException(strMsg);
//		    }
//		}
//      
//   vo.setActPos(cellPos.toString());
//}
/**
 * 校验用户输入的单元位置是否有冲突，包括：
 * 1。动态区域中定义的位置不能超过动态区的范围
 * 2。主表定义的位置不能在动态区域中
 * 3。主表内和动态区内的指标位置不能重复
 * 4。表样单元不允许定义为指标
 * 5。如果单元属性和指标类型不一致，要改为指标的类型
 * 6. 动态区中的指标不能定义在动态区关键字上
 * @param ufocell UfoArea
 * @param vo MeasureTableVO
 * @return boolean
 * @i18n uiiufofmt00066=指标迁移不能跨主表和各个动态区
 * @i18n uiiufofmt00067=迁移的目标位置有关键字，不允许迁移
 */
private void checkCell(CellPosition newPos,MeasurePosVO vo)
{
    CellPosition oldPos = AreaPosition.getInstance(vo.getActPos()).getStart();
    if(newPos.equals(oldPos))
        return;
    String areaName = newPos.toString();
    //表样单元不允许定义为指标 modify by wangyga 2008-12-22 去掉此处的交验，因为指标添加或者移动时，回去改变单元的类型
//    IufoFormat iufoFormat = (IufoFormat) m_cellsModel.getCellFormat(newPos);
//    if(iufoFormat != null){
//        if(iufoFormat.getCellType() == TableConstant.CELLTYPE_SAMPLE){
//        	String strMsg = StringResource.getStringResource("miufo1001532");  //"指标不能定义在表样单元上！"
//            throw new MessageException(MessageException.TYPE_WARNING,strMsg);
//        }
//    }

    for(int j = 0; j < m_vecMeasure.size() ; j++)
    {
        if(areaName.equals(((MeasurePosVO)m_vecMeasure.get(j)).getActPos()))
        {
        	String strMsg = StringResource.getStringResource("miufo1001533");  //"为该指标制定的新的位置已经设置了指标，请重新设置指标位置！"
            throw new MessageException(MessageException.TYPE_WARNING,strMsg);
        }
    }
    DynAreaCell oldDynCell = getDynAreaModel().getDynAreaCellByPos(oldPos);
    DynAreaCell newDynCell = getDynAreaModel().getDynAreaCellByPos(newPos);
    if(oldDynCell != newDynCell){
    	String msg = StringResource.getStringResource("uiiufofmt00066");
        throw new MessageException(MessageException.TYPE_WARNING,msg);
    }
    if(getKeyModel().getKeyVOByPos(newPos) != null){
    	throw new MessageException(MessageException.TYPE_WARNING,StringResource.getStringResource("uiiufofmt00067"));
    }
}
private DynAreaModel getDynAreaModel(){
	return DynAreaModel.getInstance(m_cellsModel);
}
private KeywordModel getKeyModel(){
	return KeywordModel.getInstance(m_cellsModel);
}
}
 