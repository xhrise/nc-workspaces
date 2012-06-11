package com.ufsoft.report;


import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UISeparator;
import nc.ui.pub.beans.UITextField;

import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.component.ProcessController;
import com.ufsoft.report.plugin.IStatusBarExt;

/**
 * ״̬���� ��ʾ״̬��ʾ��Ϣ���������ȡ�
 * @author CaiJie
 */
public class ReportStatusBar extends nc.ui.pub.beans.UIPanel {
	private static final long serialVersionUID = -7679372569216341906L;
	
	private UfoReport m_report = null;
	
	private JPanel leftMessage =  new UIPanel();
	
	private boolean isView=true; //add by guogang 2007-6-14
	
	private String strHintMessage = null;//add by wangyga 2008-11-11
	
	/**
	 * ״̬���Ŀ��
	 */
	private static int statusBarWidth = 60;
	/**
	 * ״̬���ĸ߶�
	 */
	private static int statusBarHeight = 30;
	/**
	 * ״̬����JLabel��ǩ��Ҳ���Ƿ���
	 */
	private Vector<JLabel> status = null;
	/**
	 * ÿһ����Ŀ�Ŀ��
	 */
	private int[] eachWidthes = null;
	/**
     * ��ʾ��Ϣ
     */
    private JTextField m_tfHintMessage = null;
    /**
     * ������
     */
    private JProgressBar m_pbProgress = null;
    
    /**
     * @since 3.0
     */
    public ReportStatusBar(UfoReport rpt) {
        super();
        m_report = rpt;
        leftMessage.setOpaque(false);
        m_tfHintMessage = new UITextField();
        m_tfHintMessage.setEditable(false);        
        m_pbProgress = new JProgressBar(0,100);
        m_pbProgress.setIndeterminate(true);
        m_pbProgress.setStringPainted(true);
        m_pbProgress.setBorderPainted(false);
        status = new Vector<JLabel>();
        eachWidthes = new int[10];
        
        //���ñ߿�
        this.setBorder(BorderFactory.createEtchedBorder(UIManager
                .getColor("InternalFrame.borderHighlight"), UIManager
                .getColor("ToolBar.background")));
        initUI();
        
    }

    /**
     * ������ʾ��Ϣ�� ��ʾ��Ϣ��ʾ��״̬��������ࡣ
     * ��������setProgressValue��������ʾ��Ϣ����״̬��ɾ��
     */
    public void setHintMessage(String hintMessage) {
//        m_tfHintMessage.setText(hintMessage);       
//        m_tfHintMessage.setToolTipText(hintMessage);
//        leftMessage.removeAll();
//        leftMessage.add(m_tfHintMessage);
    	this.strHintMessage = hintMessage;
    	this.repaint();
    }
   
    public void setProcessDisplay(ProcessController controller){
    	leftMessage.removeAll();
    	leftMessage.add(m_pbProgress);
    	m_pbProgress.setSize(leftMessage.getSize());
    	controller.showProcessBar(m_pbProgress);
    }

    
    /**
     * ����״̬����Ŀ����m_arrStratusBar���²���״̬��,���������Ϣ���������������ݲ���ʾ�� ����Ҫ��ʾ��״̬����CAPACITY��״̬��Ϣ����ʾ��״̬���Ҳ�ƫ�ơ�
     * ����ʱ��2004-8-5 14:45:09
     */
    private void initUI() {
        this.removeAll();  
        BoxLayout boxLayout = new BoxLayout(this,BoxLayout.X_AXIS);
        setLayout(boxLayout);
        //�����Ϣ������߽��������
        add(leftMessage);
        //add(Box.createHorizontalGlue());
        //add(createSeparator(),null);
        add(Box.createRigidArea(new Dimension(30,0))); 
        //setBackground(StyleUtil.reportStatusBarColor);
        setBackground(new Color(236,233,216));        
    }
    
    private JSeparator createSeparator(){
    	JSeparator s = new UISeparator(JSeparator.VERTICAL);
    	s.setVisible(false);
    	Dimension d = new Dimension(6,200);
    	s.setMaximumSize(d);
    	return s;
    }
    
	public Component addExtension(IStatusBarExt statusBarExt) {
		final JLabel label = new com.ufsoft.table.beans.UFOLabel();
		final IStatusBarExt ext = statusBarExt;
		label.addMouseListener(new MouseAdapter() {               
            public void mouseClicked(MouseEvent e) {
                if(e == null || ext == null) return;                    
                if(e.getClickCount() >= 2) {                                              
                    Object[] params = ext.getParams(m_report);
                    UfoCommand cmd = ext.getCommand();
                    if (cmd != null) {
                        cmd.run(params,label);
                    }                    
                }                
                }
            }); 
		
        statusBarExt.initListenerByComp(label);        
        addGridLabel(label, statusBarWidth);
        
        return label;
	}
	
	/**
	 * ���뱨��򿪺�һֱ�������Ϣ�����籨�����Ƶȡ�
	 * @param text
	 */
	public void addImmutableInfo(String text){
		JLabel label = new com.ufsoft.table.beans.UFOLabel();
		label.setText(text);
		addGridLabel(label, statusBarWidth);
	}

	/**
	 * ���뱨��򿪺�һֱ�������Ϣ�����籨�����Ƶȡ�
	 * @param text
	 */
	public void addImmutableInfo(String text, int size){
		JLabel label = new com.ufsoft.table.beans.UFOLabel();
		label.setText(text);
		addGridLabel(label, size);
	}
	
	private void addGridLabel(JLabel label, int size) {
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setVerticalAlignment(SwingConstants.CENTER);
		label.setBorder(new LineBorder(new Color(211,211,211), 1, false));
		add(label);
		
		if(status == null){
			status = new Vector<JLabel>();
		}
		status.add(label);
		if(eachWidthes == null){
			eachWidthes = new int[0];
		}
		eachWidthes[status.size()] = size;
		if(status.size() == 0){
			add(Box.createRigidArea(new Dimension(40,0))); 
		} else{
			add(Box.createRigidArea(new Dimension(4,0))); 
		}
		
	}


	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (m_report.getCellsModel().isDirty())
			return;
		
		Graphics2D g2d = (Graphics2D) g;
		FontRenderContext context = g2d.getFontRenderContext();
		Font f = g2d.getFont();
		
		String strMessage = strHintMessage == null ? "" : strHintMessage;
		Rectangle2D bounds = f.getStringBounds(strMessage, context);

		double x = getWidth() - bounds.getWidth();
		double y = getHeight() + bounds.getHeight() / 2;

		g2d.drawString(strMessage, (int) x / 2, (int) y / 2);
	}
	
	/** 
	 * ����������
	 * @param widthes
	 */
//	private void processWidthes(int[] widthes)
//	{
//		if (widthes == null) {
//			eachWidthes = new int[1];
//			eachWidthes[0] = 100;
//			setStatusNumber(1);
//		} else {
//			eachWidthes = widthes;
//			int widthSum = 0;
//			for (int index = 0; index < eachWidthes.length; index++)
//			{
//				if (widthSum >= 100)
//					break;
//				if (eachWidthes[index] < 1 || eachWidthes[index] > 99)
//					eachWidthes[index] = 0;
//				widthSum += eachWidthes[index];
//				if (widthSum > 100)
//					eachWidthes[index] = 100 - (widthSum - eachWidthes[index]);
//			}
//			setStatusNumber(eachWidthes.length);
//		}
//	}
	
	public boolean isView() {
		return isView;
	}

	public void setView(boolean isView) {
		this.isView = isView;
	}
	
}