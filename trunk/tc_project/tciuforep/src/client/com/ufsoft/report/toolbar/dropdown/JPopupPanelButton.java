package com.ufsoft.report.toolbar.dropdown;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.plaf.ComboBoxUI;

import com.ufsoft.report.plugin.IActionExt;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.report.toolbar.dropdown.PopupPanelButtonUI.PopupArrowButton;


public class JPopupPanelButton extends JComboBox implements Serializable {

	private static final long serialVersionUID = 696111427093037473L;
	
	IActionExt m_ext;
	/** 左按钮*/
	private JButton bttLeft;
	/** 右按钮*/
    private JButton bttRight;
    /** 合成按钮左按钮的事件*/
    private int actionIndex = -1;
    /** 按钮图标*/
    private Icon leftIcon;
    /** 弹出菜单的面板 modify by 王宇光 2008-3-20 使其支持通用弹出组件*/
//    private SwatchPanel swathcPanel;
    /** add by 王宇光 2008-3-20 弹出组件*/
    private JComponent popupCom = null;
    /** 下拉框的数据模型*/
    private DefaultComboBoxModel model ;

	public JPopupPanelButton(IActionExt m_ext,String name,SwatchPanel panel) {
        this(m_ext,name, null,panel);
        this.m_ext = m_ext;
    }
	
	
	//add by 王宇光 2008-3-20 支持通用组件
    public JPopupPanelButton(IActionExt m_ext,String text, Icon icon,JComponent c) {
    	this.m_ext = m_ext;
    	setName(text);
        leftIcon=icon;
        setPreferredSize(new Dimension(32, 23));
        setSize(new Dimension(32, 23));
        setMaximumSize(new Dimension(32, 23));
        setPopupCom(c);
        setComBoxModel(c);     
        refreshUI();
        setRequestFocusEnabled(false);
    }
    
	/**
     * add by 王宇光 2008-3-20 设置样品面板并更新数据模型
     * @param swathcPanel
     */
	private void setComBoxModel(JComponent c){
		if(c == null){
			return;
		}
		if(c instanceof SwatchPanel){
			SwatchPanel swatchPanel = (SwatchPanel)c;
			model = new DefaultComboBoxModel(swatchPanel.getSwatchValues());			
		}
		if(c instanceof JPopupMenu){
			String[] aryValues = SwatchPopuMenu.getItemValues((JPopupMenu)c);
			if(aryValues==null){
				model = new DefaultComboBoxModel();	
			}else{
				model = new DefaultComboBoxModel(aryValues);	
			}
			
		}
		setModel(model); 
	}
	
	/**
     * 先重构组件，再更新UI
     */
    private void refreshUI(){
    	initComponents();
        ComboBoxUI cui = this.getUI();
        cui = new PopupPanelButtonUI(bttLeft,bttRight);
        setUI(cui);
        
    }
    
    /**
	 * 初始化组件 add by 王宇光 2008-5-22 点击左键的监听
	 */
    private ActionListener createLeftBtListener(){
    	return new ActionListener(){
    		public void actionPerformed(ActionEvent e){
    			fireActionEvent();
    			}
                
    	};
    }
    
    /**
	 * 初始化组件
	 */
	private void initComponents() {
		
		if (bttLeft == null) {
			bttLeft = new JButton("",leftIcon);
			bttLeft.setPreferredSize(new Dimension(21, 23));
			bttLeft.setToolTipText(this.getName());
			bttLeft.setMargin(new Insets(0, 0, 0, 0));
			bttLeft.addActionListener(createLeftBtListener());
		}
		if (bttRight == null) {
			bttRight = new PopupArrowButton();
			bttRight.setPreferredSize(new Dimension(11, 1));
		}
	}
    /**
     * 设置显示图标
     * @param icon Icon
     */
    public void setIcon(Icon icon) {
     if(bttLeft!=null){
    	 bttLeft.setIcon(icon);
     }
    }
    
    public void adjustEnabled(Component focusComp){
        if(m_ext != null){
        	boolean isEnabled = m_ext.isEnabled(focusComp);
            setEnabled(isEnabled);
            bttLeft.setEnabled(isEnabled);
        }
    }
       
    @Override
	public void setEnabled(boolean b) {
    	if(super.isEnabled() != b){
//    		super.setEditable(b);
    		super.setEnabled(b);
    	}
    	if(bttLeft.isEnabled() != b){
    		bttLeft.setEnabled(b);
    	}
    	if(bttRight.isEnabled() != b){
    		bttRight.setEnabled(b);
    	}
    	
	}

	@Override
	protected void fireActionEvent() {
		// TODO Auto-generated method stub
		super.fireActionEvent();
	}
    
    protected JComponent getPopupCom() {
		return popupCom;
	}
	protected void setPopupCom(JComponent popupCom) {
		this.popupCom = popupCom;
	}
	
	/**
     * 获得左按钮 add by 王宇光 2008-5-22
     * @param 
     * @preturn JButton
     */
	public JButton getBttLeft() {
		return bttLeft;
	}
	
    public static void main(String[] args){
    	JFrame frame = new JFrame("test");
    	frame.setSize(200, 50);
    	JPopupPanelButton btt1=new JPopupPanelButton(null,"",ResConst.getImageIcon("reportcore/ufheart.gif"),new ColorPanel());
    	 btt1.setPreferredSize(new Dimension(60, 25));
         btt1.setSize(new Dimension(60, 25));
         btt1.setMaximumSize(new Dimension(60, 25));
         JToolBar bar = new JToolBar("Toolbar");
         bar.setRollover(true);
         bar.add(btt1);
         frame.getContentPane().add(bar, BorderLayout.NORTH);
         frame.setVisible(true);
    }
	
	
}
