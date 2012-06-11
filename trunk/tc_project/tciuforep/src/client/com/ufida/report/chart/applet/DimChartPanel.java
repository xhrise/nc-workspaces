package com.ufida.report.chart.applet;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import javax.swing.JPopupMenu;

import nc.bs.logging.Log;
import nc.itf.iufo.freequery.IMember;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.PieSectionEntity;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.report.chart.model.DimChartException;
import com.ufida.report.chart.model.DimChartModel;
import com.ufida.report.chart.model.DimDataFactory;
import com.ufida.report.chart.property.IDataProperty;
import com.ufida.report.multidimension.model.DataDrillSet;
import com.ufida.report.multidimension.model.DimMemberCombinationVO;
import com.ufida.report.multidimension.model.MultiDimDataModel;
/**
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 * @author caijie
 */
public class DimChartPanel extends ChartPanel implements MouseListener {   
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String DrillTypeChanged = "DimChartPanel_DrillTypeChanged";
    public static final String DrillDetail = "DimChartPanel_Drilled";
    
    private Point mouseClickedPoint = null;   
    
//    private transient EventListenerList listenerList  = new EventListenerList(); 
 
    private DimChartModel dimChartModel = null;


    /**
     * @param chart
     */
    public DimChartPanel(DimChartModel model, JFreeChart chart) {
        super(chart,true, true, true, true,true);
        this.dimChartModel = model;
        this.addMouseListener(new MouseAdapter() { 
            public void mousePressed(MouseEvent e) {
                DimChartPanel.this.requestFocus();
                }
            });
//        popup = createPopupMenu(false,false,false,false);
//        setPopupMenu(popup);
    }
    
    /**
     * 菜单操作的钻取
     * @param type
     * @throws DimChartException
     */
    public void drill(int type) throws DimChartException {
        dimChartModel.drill(getDrillMember((int)mouseClickedPoint.getX(), (int)mouseClickedPoint.getY()), type);
    }
    
    public JPopupMenu getPopupMenu() {
        return null;
    }
    
    protected void displayPopupMenu(int x, int y) {
        if(this.dimChartModel == null) return;

        super.displayPopupMenu(x, y);
    }
    /**
     * Creates a popup menu for the panel.
     *
     * @return The popup menu.
     */
    protected JPopupMenu createPopupMenu(boolean properties, boolean save, boolean print,
                                         boolean zoom) {  
        return null;
    }
        

    private IMember getDrillMember(int viewX, int viewY) {      
        MultiDimDataModel model = getMultiDimDataModel();                
        if(model == null) {  return null; }   
        
        ChartEntity chartEntity  =  super.getEntityForPoint(viewX, viewY);
        if(chartEntity == null) {return null;}
       
        if(chartEntity instanceof CategoryItemEntity) {
            CategoryItemEntity entity = (CategoryItemEntity) chartEntity;
            return this.dimChartModel.getDrillMember(getRowKey(entity.getSeries()),getColKey(entity.getCategoryIndex()));           
        }else  if(chartEntity instanceof PieSectionEntity) {
            PieSectionEntity entity = (PieSectionEntity) chartEntity;
            return this.dimChartModel.getDrillMember(getRowKey(entity.getPieIndex()),getColKey(entity.getSectionIndex()));           
        }
        
        return null;
    }
    
    private String getRowKey(int index){
    	try {
			IDataProperty prop = getDimChartModel().getChartPropertry().getDataProperty();
			DimMemberCombinationVO[] vos = DimDataFactory.getUsedRows(getMultiDimDataModel(), prop);
			return vos[index].getName();
		} catch (Exception e) {
			Log.getInstance(DimChartPanel.class).error(e.getMessage());
			AppDebug.debug(e);
		}
    	return null;
    }
    private String getColKey(int index){
    	try {
			IDataProperty prop = getDimChartModel().getChartPropertry().getDataProperty();
			DimMemberCombinationVO[] vos = DimDataFactory.getUsedColumns(getMultiDimDataModel(), prop);
			return vos[index].getName();
		} catch (Exception e) {
			Log.getInstance(DimChartPanel.class).error(e.getMessage());
			AppDebug.debug(e);
		}
    	return null;
    }

    /**
     * Handles a 'mouse pressed' event.
     * <P>
     * This event is the popup trigger on Unix/Linux.  For Windows, the popup
     * trigger is the 'mouse released' event.
     *
     * @param e  The mouse event.
     */
    public void mousePressed(MouseEvent e) {
//        System.out.println("DimChartPanel:mousePressed" + e.getPoint().toString());
        if(this.getChart() == null) {
            return;
        }
        mouseClickedPoint = new Point(e.getPoint().x, e.getPoint().y);
//        System.out.println("mousePressed后mouseClickedPoint的值是:" + mouseClickedPoint.toString());
        super.mousePressed(e);
       }

    /**
     * Handles a 'mouse released' event.
     * <P>
     * On Windows, we need to check if this is a popup trigger, but only if we
     * haven't already been tracking a zoom rectangle.
     *
     * @param e  Information about the event.
     */
    public void mouseReleased(MouseEvent e) {
//        System.out.println("DimChartPanel:mouseReleased" + e.getPoint().toString());
        
        if(this.getChart() == null) {
            return;
        }
        mouseClickedPoint = new Point(e.getPoint().x, e.getPoint().y);
//        System.out.println("mouseReleased后mouseClickedPoint的值是:" + mouseClickedPoint.toString());
        super.mouseReleased(e);
        }

    /**
     * 如果双击在信息其区域，则执行钻取等信息分析
     * 否则显示图的属性对话框。
     *
     * @param event  Information about the mouse event.
     */
    public void mouseClicked(MouseEvent event) {        
//        System.out.println("DimChartPanel:mouseClicked :" + event.getPoint().toString());
        if(this.getChart() == null) {
            return;
        }
        if(event.getClickCount() > 1){
        	//System.out.println("double click: " + super.getEntityForPoint(event.getX(), event.getY()));
        	 try {
//				dimChartModel.drill(getDrillMember(event.getX(), event.getY()), IMultiDimConst.DATA_DRILLNEXT);
        		 dimChartModel.drill(getDrillMember(event.getX(), event.getY()), getDataDrillModel().getDrillType());
        		 
			} catch (DimChartException e) {			
				AppDebug.debug(e);
			}
        }
        //如果没有菜单或者菜单没有打开，则记录点击点，为菜单中钻取和钻取功能定位
        if ((getPopupMenu() != null) && (getPopupMenu().isShowing())) {
            mouseClickedPoint = new Point(event.getPoint().x, event.getPoint().y);
//            System.out.println("mouseClicked后mouseClickedPoint的值是:" + mouseClickedPoint.toString());
        }
        super.mouseClicked(event); 
    }   
    
    /**
     * @return Returns the dimChartModel.
     */
    private DimChartModel getDimChartModel() {
        return dimChartModel;
    }    
    
    private DataDrillSet getDataDrillModel() {
        DataDrillSet model = null;
        try {
            model = getDimChartModel().getMultiDimemsionModel().getDateDrillSet();
        } catch (Exception e1) {               
        }
        return model;
    }         
    private MultiDimDataModel getMultiDimDataModel() {
        MultiDimDataModel model = null;
        try {
            model = getDimChartModel().getMultiDimemsionModel().getDataModel();
        } catch (Exception e1) {                       
        }
        return model;
    }  
    
    public void print(Graphics g, Rectangle2D rect,double scale){
		Graphics2D g2 = (Graphics2D) g;
		AffineTransform transform = g2.getTransform();
		transform.setToScale(scale, scale);
		g2.transform(transform);

		getChart().draw(g2, rect, null, null);
    }
    
    
    
}
