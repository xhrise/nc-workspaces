package com.ufsoft.table.re;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
import javax.swing.text.TextAction;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.report.util.MultiLang;

/**
 * 文本框输入后，要验证是否合法；按钮点击弹出对话框需要返回一个值。
 * @author zzl 2005-6-16
 */
public class RefTextField extends JTextField{
	private static final long serialVersionUID = 1L;
	private JButton button = new UIButton();
	protected IRefComp m_refComp;
	protected JDialog dialog = null;
	private MouseInputAdapter mia = new MouseInputAdapter(){
        public void mouseEntered(MouseEvent e) {
        	if(e.getComponent() == button){
        		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));	
		    }else{
		    	button.setCursor(Cursor.getDefaultCursor());
		    }
        }
		@Override
		public void mouseClicked(MouseEvent e) {
			showDialog();
		}
    };
    
	 public RefTextField() {
		super();
		setLayout(new BorderLayout());
		button.setPreferredSize(new Dimension(22, 20));
    	button.setIcon(ResConst.getImageIcon("reportcore/ref_down.gif"));
    	
		button.addMouseListener(mia);
		button.addMouseMotionListener(mia);
		add(button, BorderLayout.EAST);

        installKeyboardActions();
        

	}
	 
	public void setRightIcon(ImageIcon image){
		button.setIcon(image);	
	}
	/**
	 * 子类需覆盖该方法,可以调用setRefComp()设置ref组件,主要用于页纬度参照,其他的在构造子中就调用setRefComp()
	 * 
	 * @create by guogang at Jan 9, 2009,10:00:47 AM
	 * 
	 */
	protected void initRef(){
		
	}
	
	static Window getWindowForComponent(Component parentComponent) throws HeadlessException {
    	if (parentComponent == null)
    		return JOptionPane.getRootFrame();
    	if (parentComponent instanceof Frame || parentComponent instanceof Dialog)
    		return (Window)parentComponent;
    	return getWindowForComponent(parentComponent.getParent());
    }
   
   public JDialog getDialog() {
		if (dialog == null) {
			Window window = getWindowForComponent(this);
			if (window instanceof Frame) {
				dialog = new UIDialog((Frame) window);
			} else {
				dialog = new UIDialog((Dialog) window);
			}
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.addWindowListener(new WindowAdapter(){
				@Override
				public void windowActivated(WindowEvent e) {
					// TODO Auto-generated method stub
					super.windowActivated(e);
					if (m_refComp != null) {
						Object validateValue = null;
						if (getText() != null
								&& getText().trim().length() > 0) {
							validateValue = m_refComp
									.getValidateValue(getText());
						}
						m_refComp.setDefaultValue(validateValue);
					}
				}
                
				@Override
				public void windowDeactivated(WindowEvent e) {
					super.windowDeactivated(e);
					getDialog().setVisible(false);
				}
				
			});
			// dialog.setUndecorated(true);
		}
		return dialog;
	}
   

    protected void showDialog(){
    	if(m_refComp==null){
			initRef();
		}
        //增加参照弹出窗口的标题功能,2006-12-31,liulp
        if(m_refComp!=null&&m_refComp.getTitleValue() != null){
        	getDialog().setTitle(m_refComp.getTitleValue());
        }
        getDialog().getContentPane().removeAll();
        getDialog().getContentPane().setLayout(new BorderLayout());
        getDialog().getContentPane().add((JComponent)m_refComp,BorderLayout.CENTER);
        Dimension refCompSize = ((JComponent)m_refComp).getPreferredSize();
        
      //modify by 王宇光 2008-5-9 日历参照的大小由日历的数据决定，不设成固定的
        
        if (!(m_refComp instanceof TimeRefComp)) {
//        	getDialog().getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
        	getDialog().setResizable(true);
//        	refCompSize.height += 29;
//			refCompSize.width += 10;
		}
        //end
        getDialog().setSize(refCompSize);//(300,500);
        //确定对话框放的位置。
        Point p = button.getLocation();
        // add by  王宇光 2008-5-13 日历参照弹出时的位置在编辑器的下方，
        if(m_refComp instanceof TimeRefComp){
            p.setLocation(0, this.getHeight());
        }
        //end
        SwingUtilities.convertPointToScreen(p,button.getParent());      
        getDialog().setLocation(p);
        //当下边界超出屏幕时，自动修正。
        Dimension screenDim = getToolkit().getScreenSize();        
        int dx = p.x + getDialog().getSize().width - screenDim.width;
        if(dx > 0){
            p.translate(-dx,0);
        }
        int dy = p.y + getDialog().getSize().height- screenDim.height;
        if(dy > 0){
        	// add by 王宇光 2008-6-3 日历的自动修正，此处理方式不妥
        	if(m_refComp instanceof TimeRefComp){
        		p.setLocation(p.x, p.y - dialog.getHeight() - button.getParent().getHeight());
        	}else{
        		p.translate(0,-dy);
        	}           
        }
        getDialog().setLocation(p);
        getDialog().setFocusCycleRoot(true);
        getDialog().setVisible(true);
       
    }
    
    public void validateRefText(boolean isShow){
    	if(m_refComp==null){
			initRef();
		}
    	if(m_refComp!=null){
    		String oldText=getText();
    		Object validateValue=m_refComp.getValidateValue(oldText);
        	if(validateValue==null){
        		this.setText("");
        		if(isShow){
        			JOptionPane.showMessageDialog(this, MultiLang.getString("report00103"));
        		}
        		return ;
        	}else{
        		if(!validateValue.toString().equals(oldText)){
        			this.setText(validateValue.toString());
        		}
        		
        	}
    	}
    }
    
    public void setText(String strText){
    	super.setText(strText);
    }
      
    /**
     * @param refComp void
     * @param defaultValue
     */
    public void setRefComp(final IRefComp refComp, Object defaultValue) {
    	 m_refComp = refComp;
    	if(refComp instanceof TimeRefComp){
    		getDialog().setUndecorated(true);
    	}
        
        addCloseListener();
        
    }
    /**
     * 可被子类重载，在有虚根的情况下，虚根没有意义不用双击关闭
     * modify by 王宇光 2008-6-13 修改对Button单击关闭参照的支持
     */
    protected void addCloseListener(){
    	if (m_refComp==null)
    		return;
    	
    	((JComponent)m_refComp).addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e) {
            	Object source = e.getSource();
            	if(source instanceof JButton){
            		if(e.getClickCount()==1){
            			closeRef();
            		}
            	}else{
            		 if(e.getClickCount()>=2){
            			 closeRef();
                     }
            	}
               
            }
        });
    	((JComponent)m_refComp).addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER||(e.getModifiers()==KeyEvent.ALT_MASK&&e.getKeyCode()==KeyEvent.VK_Y)){
					 closeRef();	
				}
			}

			public void keyReleased(KeyEvent e) {
				
			}

			public void keyTyped(KeyEvent e) {
				
			}
				
			});
    	
    	((JComponent)m_refComp).registerKeyboardAction(new ActionListener(){
			public void actionPerformed(ActionEvent e) {				
					closeRef();				
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		
    }
    
    public void closeRef(){
    	Object obj = m_refComp.getSelectValue();
        if(obj!=null && obj.toString()!=null && !"".equals(obj.toString().trim())){
            if(obj instanceof IDName){
                if(((IDName)obj).getID() == null || !((IDName)obj).isRefNode()){
                    return;
                }
                setText(((IDName)obj).getID());
            }else{
                setText(obj.toString()); 
            }   
        }
        
        getDialog().dispose();
        dialog = null;
        
        EventQueue.invokeLater(new Runnable(){
			public void run() { 
				RefTextField.this.requestFocus();
			}
        	
        });
       
    }
    
	public JButton getButton() {
		return button;
	}

	/**
	 * 注册键盘动作
	 */
	protected void installKeyboardActions() {
		registerKeyboardAction(new TextAction("Reference") {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				if (e.getSource() instanceof RefTextField) {
					((RefTextField) e.getSource()).showDialog();
				}

			}
		},KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0),JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

	}
	@Override
	public void requestFocus() {
		// TODO Auto-generated method stub
		
		if(!this.hasFocus()&&this.getText()!=null){
			this.selectAll();
		}
		super.requestFocus();
	}
	@Override
	public boolean requestFocus(boolean temporary) {
		if(!this.hasFocus()&&this.getText()!=null){
			this.selectAll();
		}
		return super.requestFocus(temporary);
	}

}

