package com.ufsoft.table.re;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

import javax.swing.JFrame;
import javax.swing.SwingConstants;
/**
 * 绘制可以折叠显示的JLabel组件。
 * @author zzl 2005-10-8
 */
public class JWrapLabel extends com.ufsoft.table.beans.UFOLabel {
    private static final long serialVersionUID = 5507479685460279596L;
    private String m_strText;
   
    /*
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawWrapText(g);
    }

    /*
     * @see javax.swing.JLabel#setText(java.lang.String)
     */
    public void setText(String text) {
        m_strText = text;
        super.setText("");
        revalidate();
        repaint();
    }

    private void drawWrapText(Graphics g){
        if(m_strText == null || "".equals(m_strText)){
            return;
        }
        Graphics2D g2 = (Graphics2D) g;//getGraphics();
        if(g2 == null){
            return;
        }
        Color oldColor = g2.getColor();
        g2.setColor(getForeground());
        Rectangle rect = getBounds();
        int horAlign = getHorizontalAlignment();
        int verAlign = getVerticalAlignment();
          
        if (rect.height == 0 || rect.width == 0)
            return;
        if (m_strText == null)
            return;

        AttributedString asText = new AttributedString(m_strText);
        asText.addAttribute(TextAttribute.FONT, getFont());
        AttributedCharacterIterator paragraph = asText.getIterator();
        int paragraphStart = paragraph.getBeginIndex();
        int paragraphEnd = paragraph.getEndIndex();
        FontRenderContext frc = g2.getFontRenderContext();
        LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, frc);
        
        //计算总高度。
        int textHeight = 0;
        lineMeasurer.setPosition(paragraphStart);
        while(lineMeasurer.getPosition() < paragraphEnd){
            TextLayout layout = lineMeasurer.nextLayout(getSize().width);
            textHeight += layout.getAscent()+layout.getDescent()+layout.getLeading();
        }
        float drawPosY;
        if(verAlign == SwingConstants.TOP){
            drawPosY = 0;
        }else if(verAlign == SwingConstants.BOTTOM){
            drawPosY = getSize().height - textHeight;
        }else{
            drawPosY = (getSize().height - textHeight)/2;
        }
        //开始绘制。
        lineMeasurer.setPosition(paragraphStart);
        while (lineMeasurer.getPosition() < paragraphEnd) {
            TextLayout layout = lineMeasurer.nextLayout(getSize().width);
            drawPosY += layout.getAscent();
            float drawPosX;
            if(horAlign == SwingConstants.RIGHT){
                drawPosX = (getSize().width - layout.getAdvance());
            }else if(horAlign == SwingConstants.CENTER){
                drawPosX = (getSize().width - layout.getAdvance())/2;
            }else{
                drawPosX = 0;
            }   
            layout.draw(g2, drawPosX, drawPosY);
            drawPosY += layout.getDescent() + layout.getLeading();
       }        
        g2.setColor(oldColor);
    }    
    
    public Dimension getPreferredSize() {
		Rectangle rect = getBounds();
		int width = rect.width;
	    // @edit by wangyga at 2009-11-17,下午04:04:35
        if (rect.height == 0 || rect.width == 0 || m_strText == null || m_strText.trim().length() == 0){
        	super.setText(m_strText);
        	Dimension preSize = super.getPreferredSize();
        	super.setText("");
        	return preSize;
        }
        
        AttributedString asText = new AttributedString(m_strText);
        asText.addAttribute(TextAttribute.FONT, getFont());
        AttributedCharacterIterator paragraph = asText.getIterator();
        int paragraphStart = paragraph.getBeginIndex();
        int paragraphEnd = paragraph.getEndIndex();
        FontRenderContext frc = ((Graphics2D)getGraphics()).getFontRenderContext();
        LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, frc);
        
        //计算总高度。
        int textHeight = 0;
        lineMeasurer.setPosition(paragraphStart);
        while(lineMeasurer.getPosition() < paragraphEnd){
            TextLayout layout = lineMeasurer.nextLayout(getSize().width);
            textHeight += layout.getAscent()+layout.getDescent()+layout.getLeading();
        }
        return new Dimension(width,textHeight);
	}

	public void setFont(Font font) {
		if(font != null){
			super.setFont(font);
		}
	}

	/**
	 * @i18n miufo00109=我是一个兵，来自老百姓！我是一个兵，来自老百姓！我是一个兵，来自老百姓！我是一个兵，来自老百姓！我是一个兵，来自老百姓！我是一个兵，来自老百姓！
	 */
	public static void main(String[] args) {
        JWrapLabel label = new JWrapLabel();
        label.setOpaque(true);
        label.setText("我是一个兵，来自老百姓！我是一个兵，来自老百姓！我是一个兵，来自老百姓！我是一个兵，来自老百姓！我是一个兵，来自老百姓！我是一个兵，来自老百姓！");
        label.setBackground(Color.YELLOW);
        label.setForeground(Color.RED);
//        label.setFont(new Font("宋体",Font.ITALIC,20));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400,800);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(label,BorderLayout.CENTER);
        frame.setVisible(true);
    }    
} 