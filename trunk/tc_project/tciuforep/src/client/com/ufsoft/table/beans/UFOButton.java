package com.ufsoft.table.beans;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;

import nc.ui.pub.beans.TooltipUtil;

/**
 * ����:UIButton,���Ӳ�������Ԥ��ֵ
 * ע��:�ڿ��ӻ�״̬Ӧ����������IButtonType��Text,Ȼ����������������.
	 		ȷ�����ɴ���ʱ��ִ��setIButtonType��setText
 * @author chxiaowei
 */
public class UFOButton extends javax.swing.JButton  {
	/**Ĭ�ϸ߶ȿ��*/
	private int fieldIButtonType = 1; /**ȱʡ*/
	private boolean fieldTranslate = true;
	private static final int JAVAX_DEFAULT_TYPE = 0; /**ϵͳĬ��ֵ����*/

	/**
	 * UIButton ������ע��.
	 */
	public UFOButton() {
		super();
		initialize();
	}
	/**
	 * �����ƶ���־�İ�ť
	 * @param text java.lang.String
	 */
	public UFOButton(String text) {
		super(text);
		setText(text);
		initialize();
	}
	/**
	 * �������б����ͼ��İ�ť
	 * @param text java.lang.String
	 * @param icon javax.swing.Icon
	 */
	public UFOButton(String text, javax.swing.Icon icon) {
		super(text, icon);
		setText(text);
		initialize();
	}
	/**
	 * ��������ͼ��İ�ť
	 * @param icon com.sun.java.swing.Icon
	 */
	public UFOButton(javax.swing.Icon icon) {
		super(icon);
		initialize();
	}
	/**
	 * 
	 * ��������:(2001-2-16 14:29:48)
	 * @return java.awt.Rectangle
	 */
	public java.awt.Rectangle getBounds() {
		java.awt.Rectangle rBounds = super.getBounds();
		return rBounds;
	}
	/**
	 * 
	 * ��������:(2001-2-16 15:36:36)
	 * @return int
	 */
	public int getHeight() {
		return super.getHeight();
	}
	/**
	 * ��ȡ iButtonType ���� (int) ֵ.
	 * @return iButtonType ������ֵ.
	 * @see #setIButtonType
	 */
	public int getIButtonType() {
		return fieldIButtonType;
	}
	/**
	 * 
	 * ��������:(2001-3-2 12:41:37)
	 * @return java.lang.String
	 */
	public java.lang.String getToolTipText() {
		return TooltipUtil.getTip4Button(this,super.getToolTipText());
	}
	/**
	 * 
	 * ��������:(2001-4-27 19:13:52)
	 * @return java.lang.String
	 */
	public String getToolTipText(MouseEvent e) {
		return getToolTipText();
	}
	/**
	 * 
	 * ��������:(2002-3-21 10:30:55)
	 * @return java.lang.String
	 */
	public String getUntranslatedText() {
		return super.getText();
	}
	/**
	 * ϵͳԤ�貿������,���ҿ��޸�
	 * ��������:(2001-2-21 16:39:33)
	 */
	public void initialize() {

		setSize(new Dimension(75, 20));
		setPreferredSize(new Dimension(75,20));
		javax.swing.ToolTipManager toolTipManager = javax.swing.ToolTipManager.sharedInstance();
		toolTipManager.registerComponent(this);
		setInitLook();
	}
	/**
	 * ��ȡ translate ���� (boolean) ֵ.
	 * @return translate ����ֵ.
	 * @see #setTranslate
	 */
	public boolean isTranslate() {
		return fieldTranslate;
	}
	/**
	 * 
	 * ��������:(2001-3-20 9:59:10)
	 */
	protected void processKeyEvent(java.awt.event.KeyEvent e) {
		super.processKeyEvent(e);
	}

	/**
	 * ���� iButtonType ���� (int) ��ֵ.
	 * @param iButtonType ���Ե���ֵ.
	 * @see #getIButtonType
	 */
	public void setIButtonType(int iButtonType) {
		int oldValue = fieldIButtonType;
		fieldIButtonType = iButtonType;
		if (fieldIButtonType == JAVAX_DEFAULT_TYPE)
			initialize();
		firePropertyChange("iButtonType", new Integer(oldValue), new Integer(iButtonType));
	}
	//���ó�ʼ���:����ɫ��border
	private void setInitLook(){
		Color clrBack = new Color(0XC4C4C4);
		setBackground(clrBack);
		setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED,Color.white,clrBack,new Color(0X5F5F5F),clrBack));
	}
	/**
	 * 
	 * ��������:(2001-3-2 12:41:37)
	 * @param newText java.lang.String
	 */
	public void setText(java.lang.String newText) {
		String oldValue = super.getText();
		super.setText(newText);
		if (fieldIButtonType != JAVAX_DEFAULT_TYPE)
			initialize();
		firePropertyChange("text", oldValue, newText);
	}
	/**
	 * ���� translate ���� (boolean) ֵ.
	 * @param translate �µ�����ֵ.
	 * @see #getTranslate
	 */
	public void setTranslate(boolean translate) {
//		boolean oldValue = fieldTranslate;
//		fieldTranslate = translate;
//		firePropertyChange("translate", new Boolean(oldValue), new Boolean(translate));
	}

//	public static void main(String[] args){
//	JFrame frame = new JFrame("testframe");
//	frame.setSize(200, 300);
//	UIManager.put("ButtonUI", UIButtonUI.class.getName());
//	frame.addWindowListener(new WindowAdapter(){
//	public void windowClosing(WindowEvent e) {System.exit(0);}
//	});
//	frame.getContentPane().setLayout(null);
//	UIButton btn = new UIButton("testbutn");
//	btn.setUI(new UIButtonUI());
////	btn.setEnabled(false);
//	UIButton btn1 = new UIButton("testbutn1");
////	btn.setBackground(Color.red);
//	btn.setIcon(new ImageIcon(btn.getClass().getClassLoader().getResource("images/maindesktop/down.gif")));
//	btn.setLocation(20,30);
//	btn1.setLocation(20,60);
//	frame.getContentPane().add(btn);
//	frame.getContentPane().add(btn1);
//	frame.setVisible(true);
//	}
}
