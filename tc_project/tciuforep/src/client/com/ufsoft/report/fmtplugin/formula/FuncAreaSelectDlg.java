package com.ufsoft.report.fmtplugin.formula;

import java.awt.AWTEvent;
import java.awt.ActiveEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.MenuComponent;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.RootPaneContainer;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.AreaSelectDlg;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellsModel;

/**
 * 公式区域选择对话框
 * @author wangyga
 *
 */
public class FuncAreaSelectDlg extends AreaSelectDlg implements ActionListener{

	private JButton ivjJBFold = null;
    private JTextField ivjJTFSelArea = null;
    private String strSelectArea = null;
    
	public FuncAreaSelectDlg(Container owner,CellsModel cellsModel){
		super(owner,cellsModel);
		initialize();
	}
	
	/**
	 * @i18n miufo00139=区域选择
	 */
	private void initialize(){
		setSize(405, 58);
		setLayout(null);
		setTitle(MultiLang.getString("miufo00139"));
		getContentPane().add(getJTFSelArea());
		getContentPane().add(getJBFold());
	}
	
	/**
	 * 返回 JTFSelArea 特性值。
	 * @return javax.swing.JTextField
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JTextField getJTFSelArea() {
	    if (ivjJTFSelArea == null) {
	        try {
	            ivjJTFSelArea = new JTextField();
	            ivjJTFSelArea.setName("JTFSelArea");
	            ivjJTFSelArea.setBounds(2, 1, 370, 21);
	            // user code begin {1}
//	            ivjJTFSelArea.addFocusListener(this);
//	            ivjJTFSelArea.addActionListener(this);
	            // user code end
	        } catch (java.lang.Throwable ivjExc) {
	            // user code begin {2}
	            // user code end
	            handleException(ivjExc);
	        }
	    }
	    return ivjJTFSelArea;
	}
	
	
	/**
	 * 返回 JBFold 特性值。
	 * @return javax.swing.JButton
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JButton getJBFold() {
	    if (ivjJBFold == null) {
	        try {
	            ivjJBFold = new nc.ui.pub.beans.UIButton();
	            ivjJBFold.setName("JBFold");
	            ivjJBFold.setText("");
	            ivjJBFold.setBounds(375, 2, 18, 17);
//	            ivjJBFold.addFocusListener(this);
	            ivjJBFold.addActionListener(this);
//	            ivjJBFold.registerKeyboardAction(this,KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,false),JComponent.WHEN_FOCUSED);
	            
	            ivjJBFold.setIcon(ResConst.getImageIcon("reportcore/down.gif")); //UfoPublic.getImageIcon("down.gif"));
	            
	        } catch (java.lang.Throwable ivjExc) {
	            handleException(ivjExc);
	        }
	    }
	    return ivjJBFold;
	}
	
	/**
	 * 每当部件抛出异常时被调用
	 * @param exception java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {
		AppDebug.debug(exception);
	}
	
	private void myDispatchEvent(AWTEvent event) {
        if(event instanceof ActiveEvent) {
            ((ActiveEvent)event).dispatch();
        } else if( event.getSource() instanceof Component) {
            ((Component)event.getSource()).dispatchEvent(event);
        } else if(event.getSource() instanceof MenuComponent) {
            ((MenuComponent)event.getSource()).dispatchEvent(event);
        }
    }
	
	/**
	 * 实现区域参照。
	 * 创建日期：(2001-1-4 15:01:43)
	 */
	public void show()
	{
	    super.show();
	    EventQueue eq = Toolkit.getDefaultToolkit().getSystemEventQueue();

	    while (true)
	    {
	        try
	        {
	            AWTEvent evt = eq.getNextEvent();
	            if (evt.getSource() == this && evt.getID() == WindowEvent.WINDOW_CLOSING)
	            {
	                myDispatchEvent(evt);
	                break;
	            }
	            else if (evt.getSource() instanceof RootPaneContainer && evt.getID() == WindowEvent.WINDOW_CLOSING)
	            {
	                Toolkit.getDefaultToolkit().beep();
	            }
	            else if (evt.getID() == MouseEvent.MOUSE_PRESSED)
	            {
	                MouseEvent mevt = (MouseEvent) evt;
	                //右键
	                if ((mevt.getModifiers() & InputEvent.BUTTON3_MASK) == 0){
	                    Point p = getPointToScreen(mevt); //转换到相对于屏幕的坐标
	                    if (getBounds().contains(p))
	                    {
	                        //如果点击对话框内消息正常处理
	                        myDispatchEvent(mevt);
	                    }
	                    
	                    else if (mevt.getSource() instanceof RootPaneContainer)
	                    {
	                    	myDispatchEvent(mevt);
                            String strAreaName = getViewAreaName(mevt); //得到视图选中区域
                            ivjJTFSelArea.setText(strAreaName);                           
	                    }

	                    else if (mevt.getSource() instanceof JFrame)
	                    {
	                        JFrame jf = (JFrame) mevt.getSource();
	                        if (jf.getTitle().indexOf(StringResource.getStringResource("miufopublic252")) > 0)  //"帮助"
	                        {
	                            myDispatchEvent(mevt);
	                        }
	                        else
	                            Toolkit.getDefaultToolkit().beep();
	                    }
	                    else
	                        Toolkit.getDefaultToolkit().beep();
	                }else
	                    Toolkit.getDefaultToolkit().beep();
	            }
	            else if (evt.getID() == MouseEvent.MOUSE_DRAGGED)
	            {
	                if (evt.getSource() instanceof RootPaneContainer)
	                {
	                    myDispatchEvent(evt);
	                    String strAreaName = getViewAreaName(evt);
                        ivjJTFSelArea.setText(strAreaName);
	                }
	                if (evt.getSource() == this)
	                         myDispatchEvent(evt);
	            }
	            else if (evt.getID() == MouseEvent.MOUSE_RELEASED)
	            {
	                MouseEvent mevt = (MouseEvent) evt;
	                //右键
	                if ((mevt.getModifiers() & InputEvent.BUTTON3_MASK) == 0){
	                    //鼠标抬起，是自动折叠则自动恢复
	                    if (evt.getSource() instanceof Component)
	                    {
	                        myDispatchEvent(evt);
	                    }
	                }
	            }
	            else
	            {
	                try{
	                myDispatchEvent(evt);
	                }catch(NullPointerException e){
	                    //add by zzl 2005.8.17 这里添加例外捕获,是为了解决公式向导中输入中文字符时出现的错误.
	                }
	            }
	        }
	        catch (InterruptedException ie)
	        {
	        }
	    }
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == ivjJBFold){
			setSelectArea(getJTFSelArea().getText());
			setResult(ID_OK);
			close();
			return;
		}
		
	}

	public String getSelectArea() {
		return strSelectArea;
	}

	public void setSelectArea(String strSelectArea) {
		this.strSelectArea = strSelectArea;
	}
}
 