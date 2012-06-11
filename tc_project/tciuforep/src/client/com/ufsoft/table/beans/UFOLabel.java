package com.ufsoft.table.beans;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

import nc.ui.pub.beans.TooltipUtil;

/**
 * ����:UILable,��װ�˲������Ե�Ԥ��ֵ ע��:�ڿ��ӻ�״̬Ӧ����������ILabelType,Ȼ����������������.
 * ȷ�����ɴ���ʱ��ִ��setILabelType
 * @author chxiaowei 2007-4-20
 */
public class UFOLabel extends JLabel {
    public final static Color notNullColor = new Color(52, 102, 153);
    /** ���� */
    public static final int JAVAX_DEFAULT_TYPE = 0;
    /** JAVAXĬ��ֵ���� */
    private int fieldILabelType = 1;
    
    private boolean m_bShrink=false;
    
    public static final int STYLE_DEFAULT = 1;
    public static final int STYLE_TITLE_REPORT = 2;
    public static final int STYLE_TITLE_BILL = 3;
    public static final int STYLE_TITLE_SMALL = 4;
    public static final int STYLE_BLACKBLUE = 5;
    public static final int STYLE_NOTNULL = 6;

    static class LabelStyle {
        Font font;
        Color forground;
        int hAlignment;
        int vAlignment;
    }

    private static LabelStyle createLabelStyle(int type) {
        Font f = new Font("Dialog", Font.PLAIN, 12);
        Color c = Color.black;
        int ha = LEFT;
        int va = CENTER;

        LabelStyle ls = new LabelStyle();
        switch (type) {
        case JAVAX_DEFAULT_TYPE:
            break;
        case STYLE_DEFAULT:
            f = new Font("Dialog", Font.PLAIN, 12);
            c = java.awt.Color.black;
            ha = LEFT;
            va = CENTER;
            break;
        case STYLE_TITLE_REPORT:
            f = new Font("Dialog", Font.PLAIN, 18);
            c = notNullColor;
            ha = CENTER;
            va = CENTER;
            break;
        case STYLE_TITLE_BILL:
            f = new Font("Dialog", Font.PLAIN, 18);
            c = notNullColor;
            ha = CENTER;
            va = CENTER;
            break;
        case STYLE_TITLE_SMALL:
            f = new Font("Dialog", Font.PLAIN, 14);
            c = java.awt.Color.black;
            ha = CENTER;
            va = CENTER;
            break;
        case STYLE_BLACKBLUE:
            f = new Font("Dialog", Font.PLAIN, 12);
            c = notNullColor;
            ha = LEFT;
            va = CENTER;
            break;
        case STYLE_NOTNULL:
            f = new Font("Dialog", Font.PLAIN, 12);
            c = notNullColor;
            ha = LEFT;
            va = CENTER;
            break;
        }
        ls.font = f;
        ls.forground = c;
        ls.hAlignment = ha;
        ls.vAlignment = va;
        return ls;
    }

    public UFOLabel() {
        super();
        initialize();
    }
    public UFOLabel(java.lang.String p0) {
        super(p0);
        initialize();
    }
    public UFOLabel(java.lang.String p0, int p1) {
        super(p0, p1);
        initialize();
    }
    public UFOLabel(java.lang.String p0, javax.swing.Icon p1, int p2) {
        super(p0, p1, p2);
        initialize();
    }
    public UFOLabel(javax.swing.Icon p0) {
        super(p0);
        initialize();
    }
    public UFOLabel(javax.swing.Icon p0, int p1) {
        super(p0, p1);
        initialize();
    }

    public int getILabelType() {
        return fieldILabelType;
    }
    
    public java.lang.String getToolTipText() {
        return TooltipUtil.getTip4Label(this, super.getToolTipText());
    }

    public String getToolTipText(MouseEvent e) {
        return getToolTipText();
    }
    /**
     * 
     * ��������:(2002-3-21 10:30:55)
     * 
     * @return java.lang.String
     */
    public String getUntranslatedText() {
        return super.getText();
    }
    
    /**
     * ϵͳԤ�貿������,���ҿ��޸�
     */
    public void initialize() {
        setILabelType0(fieldILabelType);

        javax.swing.ToolTipManager toolTipManager = javax.swing.ToolTipManager
                .sharedInstance();
        toolTipManager.registerComponent(this);
		setUI(UfoLabelUI.createUI(this));
    }
    
	public UfoLabelUI getUI() {
		return (UfoLabelUI) ui;
	}
    /**
     * @return boolean
     */
    public boolean isFocusTraversable() {
        return false;
    }
    
    /**
     * ��ȡ translate ���� (boolean) ֵ.
     * 
     * @return translate ����ֵ.
     * @see #setTranslate
     */
    public boolean isTranslate() {
        return true;
    }
    
    /**
     * ���� iLabelType ���� (int) ��ֵ.
     * 
     * @param iLabelType
     *            ���Ե���ֵ.
     * @see #getILabelType
     */
    public void setILabelType(int iLabelType) {
        if (fieldILabelType != iLabelType)
            setILabelType0(iLabelType);

    }
    
    private void setILabelType0(int iLabelType) {
        fieldILabelType = iLabelType;

        LabelStyle ls = createLabelStyle(iLabelType);

        setFont(ls.font);
        setForeground(ls.forground);
        setHorizontalAlignment(ls.hAlignment);
        setVerticalAlignment(ls.vAlignment);
    }

    /**
     * ���� translate ���� (boolean) ֵ.
     * 
     * @param translate
     *            �µ�����ֵ.
     * @see #getTranslate
     */
    public void setTranslate(boolean translate) {
    }

	public boolean isShrink() {
		return m_bShrink;
	}

	public void setShrink(boolean shrink) {
		m_bShrink = shrink;
	}
}
