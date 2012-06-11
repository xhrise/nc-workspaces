package com.ufsoft.report.dialog;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowListener;
import java.lang.reflect.Method;

import javax.help.CSH;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.report.UIUtilities;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.report.util.MultiLang;

/**
 * 报表工具中使用的对话框的基类。 该类处理键盘的Esc和Window关闭的事件，并且保证对话框风格的统一。
 * 这里对话框属性设置为模式对话框；关闭时DisPose.
 * @author wupeng 2004-6-15
 */
public class UfoDialog extends JDialog implements KeyListener, WindowListener {
 
	private static final long serialVersionUID = 2312966358727777968L;
	
	private int m_nResult = 2;
	/**
	 * 确定按钮标记
	 */
	public final static int ID_OK = 1;
	/**
	 * 取消按钮标记
	 */
	public final static int ID_CANCEL = 2;	
	
	//页面创建时为界面效果自动添加外围框。 liuyy. 2006-11-21
	private boolean bWrapBorder = false;
	
	/**
	 * UfoDialog 构造子注解。
	 */
	public UfoDialog() {
		super();
		initDialog();

	}
	/**
	 * 此处插入方法说明。 
	 * @param parent  java.awt.Container
	 */
	public UfoDialog(Container parent) {
		super(javax.swing.JOptionPane.getFrameForComponent(parent));
		initDialog();

	}
	protected UfoDialog(Dialog dialog){
		super(dialog);
		initDialog();
	}
	/**
	 * 此处插入方法说明。 
	 * @param parent java.awt.Container
	 * @param title  java.lang.String
	 */
	public UfoDialog(Container parent, String title) {
		super(javax.swing.JOptionPane.getFrameForComponent(parent), title);
		initDialog();

	}
	
	public UfoDialog(Container parent, String title, boolean model) {
		this(parent, title);
		setModal(model);
	}

	/**
	 * UfoDialog 构造子注解。
	 * @param owner java.awt.Dialog
	 * @param title java.lang.String
	 */
	public UfoDialog(java.awt.Dialog owner, String title) {
		super(owner, title);
		initDialog();
	}

	/**
	 * UfoDialog 构造子注解。
	 * @param owner java.awt.Frame
	 */
	public UfoDialog(java.awt.Frame owner) {
		super(owner);
		initDialog();
	}

//	/**
//	 * 统一添加边框间距。
//	 * liuyy.
//	 */
//   public void setContentPane(Container contentPane) {
//	   
//	   if(contentPane instanceof JPanel){
//		   super.setContentPane(UIUtil.showPanelBorder((JPanel) contentPane, 30));//liuyy.
//		   Dimension size = this.getMinimumSize();
//		   this.setMinimumSize(new Dimension(getWidth() + 30, getHeight() + 30));
//		  		   
//	   } else {
//		   super.setContentPane(contentPane);
//	   }
//	          
//    }
	
    public int showModal() {
        // 下句会使屏幕闪烁
        // if (getTopFrame() == null && getTopParent() != null)
        // getTopParent().setEnabled(false);
        //
        setModal(true);

        if (!isShowing()) {
            show();
        }
        return getResult();
    }
	
	/**
	 * 关闭对话框执行的操作。
	 */
	public void close() {
		this.dispose();
	}
	/**
	 * 得到用户关闭对话框前的选择
	 * @return int
	 */
	public int getResult() { 
		return m_nResult;
	}
	/**
	 * 设置用户关闭对话框前的按钮选择。
	 * @param n int 参见常量
	 */
	public void setResult(int n) {
		m_nResult = n;
	}
	/**
	 * 自动居中显示对话框。
	 */
	public void view() {
			setVisible(true);
	}
	
	/**
	 * 初始化类。
	 */
	protected void initDialog() {
		// 添加帮助.
		if (getHelpID() != null) {
			javax.help.HelpBroker hb = ResConst.getHelpBroker();
			if (hb != null) {
				CSH.setHelpIDString(getRootPane(), getHelpID());
			}
		}
		setModal(true);
		addKeyListener(this);
		addWindowListener(this);
	}
	/**
	 * Invoked when a key has been pressed.
	 * @param e
	 */
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			setResult(ID_CANCEL);
			close();
			return;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_P && e.getModifiers() == InputEvent.CTRL_MASK + InputEvent.SHIFT_MASK){//InputEvent.ALT_MASK + 
			JOptionPane.showMessageDialog(this, this.getClass().getName());
		}
		
	}
	/**
	 * Invoked when a key has been released.
	 * @param e
	 */
	public void keyReleased(KeyEvent e) {
//		if(e.getKeyCode() == KeyEvent.VK_P && e.getModifiers() == InputEvent.CTRL_MASK + InputEvent.SHIFT_MASK){//InputEvent.ALT_MASK + 
//			JOptionPane.showMessageDialog(this, this.getClass().getName());
//		}
	}
	/**
	 * Invoked when a key has been typed. This event occurs when a key press is
	 * followed by a key release.
	 * @param e
	 */
	public void keyTyped(KeyEvent e) {
	}	
	/**
	 * 处理 WindowListener 接口事件的方法。
	 * @param e java.awt.event.WindowEvent
	 */
	public void windowActivated(java.awt.event.WindowEvent e) {
		requestFocus();
	}
	/**
	 * 处理 WindowListener 接口事件的方法。
	 * @param e java.awt.event.WindowEvent
	 */
	public void windowClosed(java.awt.event.WindowEvent e) {
	}
	/**
	 * 处理 WindowListener 接口事件的方法。
	 * @param e  java.awt.event.WindowEvent
	 */
	public void windowClosing(java.awt.event.WindowEvent e) {
//		setResult(ID_CANCEL);
		close();
		return;
	}
	/**
	 * 处理 WindowListener 接口事件的方法。
	 * @param e java.awt.event.WindowEvent
	 */
	public void windowDeactivated(java.awt.event.WindowEvent e) {
	}
	/**
	 * 处理 WindowListener 接口事件的方法。
	 * @param e  java.awt.event.WindowEvent
	 */
	public void windowDeiconified(java.awt.event.WindowEvent e) {
	}
	/**
	 * 处理 WindowListener 接口事件的方法。
	 * @param e java.awt.event.WindowEvent
	 */
	public void windowIconified(java.awt.event.WindowEvent e) {
	}
	/**
	 * 处理 WindowListener 接口事件的方法。
	 * @param e  java.awt.event.WindowEvent
	 */
	public void windowOpened(java.awt.event.WindowEvent e) {
	}
	
	/**
	 * window级的帮助,当按下F1时,显示与当前窗口相关的帮助.
	 * 子类如果需要定制当前帮助,需要实现此方法,并返回helpID.
	 * @return helpID
	 */
	protected String getHelpID(){
	    //子类继承本方法.
	    return null;
	}
	/**
	 * field级的帮助.
	 * 子类如果需要为一个组件,比如JField添加帮助,可以调用此方法.
	 * @param component
	 * @param string
	 */
	protected void setHelpIDString(Component component, String string){
	    if(component == null || string == null){
	        throw new NullPointerException();
	    }
		javax.help.HelpBroker hb = ResConst.getHelpBroker();
		if (hb == null) return;
		hb.enableHelpKey(component, string, null);
	}


    public void show() {
    	setLocationRelativeTo(getParent());
        adjustToolTipDisplay(this);
       // setResizable(true);
        Container contentPane = this.getContentPane();
 	   if(!bWrapBorder && contentPane instanceof JPanel){
 		  Dimension fullsize = Toolkit.getDefaultToolkit().getScreenSize();
 		  int width = getWidth();
 		  int height =  getHeight();
 		  if(fullsize.width > width + 60 && fullsize.height > height + 60){
			   setContentPane(UIUtilities.showPanelBorder((JPanel) contentPane, 20));
			   this.setSize(new Dimension(getWidth() + 20, getHeight() + 20));
 		  }
 		 bWrapBorder = true;
	   }
        
        super.show();
    }
    
    public void setVisible(boolean visible){
    	if(visible){
    		show();
    	}else{
    		super.setVisible(false);
    	}
    }
/**
 *  void
 */
private void adjustToolTipDisplay(Container com) {
    if(com instanceof JButton ||
       com instanceof JRadioButton ||
       com instanceof JCheckBox ||
       com instanceof JLabel
            
    ){
        addToolTipAuto((JComponent)com);
    }else if(com instanceof JDialog ||
            com instanceof JRootPane ||
            com instanceof JLayeredPane ||
             com instanceof JPanel
    ){
        Component[] coms = com.getComponents();
        for(int i=0;i<coms.length && coms[i] instanceof Container;i++){
            adjustToolTipDisplay((Container) coms[i]);
        }
    }
    
}
/**
 * 如果组件不能完全显示文本，则添加tooltip提示。
 * @param c void
 */
public static void addToolTipAuto(JComponent c){
    Rectangle rect = c.getBounds();
    Dimension dimPre = c.getPreferredSize();
    if(dimPre.width>rect.width){//如果尺寸合适，那么不显示提示信息。
        try{           
            Method method = c.getClass().getMethod("getText",new Class[0]);
            String text = (String) method.invoke(c, new Object[0]);
            c.setToolTipText(text);
        }catch(Exception e){
            System.out.println("The component do not support getText method!");
            AppDebug.debug(e);
        }
    }
}
	/**
	 * 创建默认的“确认”按钮，子类使用时需要重新设置bounds。
	 * @return
	 */
	protected JButton createOkButton() {
		JButton okButton = new nc.ui.pub.beans.UIButton();
		okButton.setText(MultiLang.getString("ok"));
		okButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				setResult(ID_OK);
				close();
			}
		});
		return okButton;
	}
	/**
	 * 创建默认的“取消”按钮，子类使用时需要重新设置bounds。
	 * @return
	 */
	protected JButton createCancleButton() {
		JButton cancleButton = new nc.ui.pub.beans.UIButton();
		cancleButton.setText(MultiLang.getString("cancel"));
		cancleButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				setResult(ID_CANCEL);
				close();
			}
		});
		return cancleButton;
	}
	/**
	 * 创建默认的“关闭”按钮，子类使用时需要重新设置bounds。
	 * @return
	 */
	protected JButton createCloseButton() {
		JButton closeButton = createCancleButton();
		closeButton.setText(MultiLang.getString("close"));
		return closeButton;
	}
}