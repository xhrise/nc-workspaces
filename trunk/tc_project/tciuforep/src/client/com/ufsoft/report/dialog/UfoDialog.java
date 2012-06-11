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
 * ��������ʹ�õĶԻ���Ļ��ࡣ ���ദ����̵�Esc��Window�رյ��¼������ұ�֤�Ի������ͳһ��
 * ����Ի�����������Ϊģʽ�Ի��򣻹ر�ʱDisPose.
 * @author wupeng 2004-6-15
 */
public class UfoDialog extends JDialog implements KeyListener, WindowListener {
 
	private static final long serialVersionUID = 2312966358727777968L;
	
	private int m_nResult = 2;
	/**
	 * ȷ����ť���
	 */
	public final static int ID_OK = 1;
	/**
	 * ȡ����ť���
	 */
	public final static int ID_CANCEL = 2;	
	
	//ҳ�洴��ʱΪ����Ч���Զ������Χ�� liuyy. 2006-11-21
	private boolean bWrapBorder = false;
	
	/**
	 * UfoDialog ������ע�⡣
	 */
	public UfoDialog() {
		super();
		initDialog();

	}
	/**
	 * �˴����뷽��˵���� 
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
	 * �˴����뷽��˵���� 
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
	 * UfoDialog ������ע�⡣
	 * @param owner java.awt.Dialog
	 * @param title java.lang.String
	 */
	public UfoDialog(java.awt.Dialog owner, String title) {
		super(owner, title);
		initDialog();
	}

	/**
	 * UfoDialog ������ע�⡣
	 * @param owner java.awt.Frame
	 */
	public UfoDialog(java.awt.Frame owner) {
		super(owner);
		initDialog();
	}

//	/**
//	 * ͳһ��ӱ߿��ࡣ
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
        // �¾��ʹ��Ļ��˸
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
	 * �رնԻ���ִ�еĲ�����
	 */
	public void close() {
		this.dispose();
	}
	/**
	 * �õ��û��رնԻ���ǰ��ѡ��
	 * @return int
	 */
	public int getResult() { 
		return m_nResult;
	}
	/**
	 * �����û��رնԻ���ǰ�İ�ťѡ��
	 * @param n int �μ�����
	 */
	public void setResult(int n) {
		m_nResult = n;
	}
	/**
	 * �Զ�������ʾ�Ի���
	 */
	public void view() {
			setVisible(true);
	}
	
	/**
	 * ��ʼ���ࡣ
	 */
	protected void initDialog() {
		// ��Ӱ���.
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
	 * ���� WindowListener �ӿ��¼��ķ�����
	 * @param e java.awt.event.WindowEvent
	 */
	public void windowActivated(java.awt.event.WindowEvent e) {
		requestFocus();
	}
	/**
	 * ���� WindowListener �ӿ��¼��ķ�����
	 * @param e java.awt.event.WindowEvent
	 */
	public void windowClosed(java.awt.event.WindowEvent e) {
	}
	/**
	 * ���� WindowListener �ӿ��¼��ķ�����
	 * @param e  java.awt.event.WindowEvent
	 */
	public void windowClosing(java.awt.event.WindowEvent e) {
//		setResult(ID_CANCEL);
		close();
		return;
	}
	/**
	 * ���� WindowListener �ӿ��¼��ķ�����
	 * @param e java.awt.event.WindowEvent
	 */
	public void windowDeactivated(java.awt.event.WindowEvent e) {
	}
	/**
	 * ���� WindowListener �ӿ��¼��ķ�����
	 * @param e  java.awt.event.WindowEvent
	 */
	public void windowDeiconified(java.awt.event.WindowEvent e) {
	}
	/**
	 * ���� WindowListener �ӿ��¼��ķ�����
	 * @param e java.awt.event.WindowEvent
	 */
	public void windowIconified(java.awt.event.WindowEvent e) {
	}
	/**
	 * ���� WindowListener �ӿ��¼��ķ�����
	 * @param e  java.awt.event.WindowEvent
	 */
	public void windowOpened(java.awt.event.WindowEvent e) {
	}
	
	/**
	 * window���İ���,������F1ʱ,��ʾ�뵱ǰ������صİ���.
	 * ���������Ҫ���Ƶ�ǰ����,��Ҫʵ�ִ˷���,������helpID.
	 * @return helpID
	 */
	protected String getHelpID(){
	    //����̳б�����.
	    return null;
	}
	/**
	 * field���İ���.
	 * ���������ҪΪһ�����,����JField��Ӱ���,���Ե��ô˷���.
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
 * ������������ȫ��ʾ�ı��������tooltip��ʾ��
 * @param c void
 */
public static void addToolTipAuto(JComponent c){
    Rectangle rect = c.getBounds();
    Dimension dimPre = c.getPreferredSize();
    if(dimPre.width>rect.width){//����ߴ���ʣ���ô����ʾ��ʾ��Ϣ��
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
	 * ����Ĭ�ϵġ�ȷ�ϡ���ť������ʹ��ʱ��Ҫ��������bounds��
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
	 * ����Ĭ�ϵġ�ȡ������ť������ʹ��ʱ��Ҫ��������bounds��
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
	 * ����Ĭ�ϵġ��رա���ť������ʹ��ʱ��Ҫ��������bounds��
	 * @return
	 */
	protected JButton createCloseButton() {
		JButton closeButton = createCancleButton();
		closeButton.setText(MultiLang.getString("close"));
		return closeButton;
	}
}