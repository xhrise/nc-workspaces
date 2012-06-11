/*
 * Created on 2005-5-10
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.chart.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;

import com.ufsoft.report.constant.DefaultSetting;
import com.ufsoft.table.format.DefaultFormatValue;
import com.ufsoft.table.format.TableConstant;
import com.ufsoft.iufo.resource.StringResource;

/**
 * 字体选择对话框
 * @author caijie
 *
 */
public class FontChooserDialog {
    public class FontChooserPanel extends nc.ui.pub.beans.UIPanel{       

        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/** The list of fonts. */
        private JList fontlist;

        /** The list of sizes. */
        private JList sizelist;

        /** The checkbox that indicates whether the font is bold. */
        private JCheckBox bold;

        /** The checkbox that indicates whether or not the font is italic. */
        private JCheckBox italic;        

        /**
         * Standard constructor - builds a FontChooserPanel initialised with the specified font.
         *
         * @param font  the initial font to display.
         * @i18n miufo1000846=字体
         * @i18n uibichart00049=大小
         * @i18n miufopublic361=粗体
         * @i18n miufopublic362=斜体
         * @i18n miufo1001244=属性
         */
        public FontChooserPanel(final Font font) {

            final String[] fonts = DefaultSetting.fontNames;

            setLayout(new BorderLayout());
            final JPanel right = new UIPanel(new BorderLayout());

            final JPanel fontPanel = new UIPanel(new BorderLayout());
            fontPanel.setBorder(BorderFactory.createTitledBorder(
                                BorderFactory.createEtchedBorder(), 
                                StringResource
										.getStringResource("miufo1000846")));
            this.fontlist = new UIList(fonts);
            final JScrollPane fontpane = new UIScrollPane(this.fontlist);
            fontpane.setBorder(BorderFactory.createEtchedBorder());
            fontPanel.add(fontpane);
            add(fontPanel);

            final JPanel sizePanel = new UIPanel(new BorderLayout());
            sizePanel.setBorder(BorderFactory.createTitledBorder(
                                BorderFactory.createEtchedBorder(), 
                                StringResource
										.getStringResource("uibichart00049")));
            this.sizelist = new UIList(DefaultSetting.fontSizes);
            final JScrollPane sizepane = new UIScrollPane(this.sizelist);
            sizepane.setBorder(BorderFactory.createEtchedBorder());
            sizePanel.add(sizepane);

            final JPanel attributes = new UIPanel(new GridLayout(1, 2));
            this.bold = new UICheckBox(StringResource.getStringResource("miufopublic361"));
            this.italic = new UICheckBox(StringResource.getStringResource("miufopublic362"));
            attributes.add(this.bold);
            attributes.add(this.italic);
            attributes.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                                 StringResource
										.getStringResource("miufo1001244")));

            right.add(sizePanel, BorderLayout.CENTER);
            right.add(attributes, BorderLayout.SOUTH);

            add(right, BorderLayout.EAST);

            setSelectedFont(font);
        }

        /**
         * Returns a Font object representing the selection in the panel.
         *
         * @return the font.
         */
        public Font getSelectedFont() {
            return new Font(getSelectedName(), getSelectedStyle(), getSelectedSize());
        }

        /**
         * Returns the selected name.
         *
         * @return the name.
         */
        public String getSelectedName() {
            return (String) this.fontlist.getSelectedValue();
        }

        /**
         * Returns the selected style.
         *
         * @return the style.
         */
        public int getSelectedStyle() {
            if (this.bold.isSelected() && this.italic.isSelected()) {
                return Font.BOLD + Font.ITALIC;
            }
            if (this.bold.isSelected()) {
                return Font.BOLD;
            }
            if (this.italic.isSelected()) {
                return Font.ITALIC;
            }
            else {
                return Font.PLAIN;
            }
        }

        /**
         * Returns the selected size.
         *
         * @return the size.
         */
        public int getSelectedSize() {
            final String selected = (String) this.sizelist.getSelectedValue();
            if (selected != null) {
                return Integer.parseInt(selected);
            }
            else {
                return 10;
            }
        }

        /**
         * Initializes the contents of the dialog from the given font
         * object.
         *
         * @param font the font from which to read the properties.
         */
        public void setSelectedFont (final Font font) {
            if (font == null) {
                throw new NullPointerException();
            }
            this.bold.setSelected(font.isBold());
            this.italic.setSelected(font.isItalic());

            final String fontName = font.getName();
            ListModel model = this.fontlist.getModel();
            this.fontlist.clearSelection();
            for (int i = 0; i < model.getSize(); i++) {
                if (fontName.equals(model.getElementAt(i))) {
                    this.fontlist.setSelectedIndex(i);
                    break;
                }
            }

            final String fontSize = String.valueOf(font.getSize());
            model = this.sizelist.getModel();
            this.sizelist.clearSelection();
            for (int i = 0; i < model.getSize(); i++) {
                if (fontSize.equals(model.getElementAt(i))) {
                    this.sizelist.setSelectedIndex(i);
                    break;
                }
            }
        }

    }
    
    FontChooserPanel m_fontChooserPanel = null;
    Component m_parentComponent = null;
    
    int m_result = JOptionPane.CANCEL_OPTION;
    
    public FontChooserDialog(Component parentComponent , Font font) {
        m_fontChooserPanel = new FontChooserPanel(font);
        m_parentComponent = parentComponent;
    }
   /**
    * 文字说明字符串
    */
    public static String getFontString(Font font) {
	    if(font == null) return null;
	    String result;
	    result = font.getName() + ", " + font.getSize();
	    return result;
	}
    
   /**
 * @i18n uibichart00050=字体选择
 */
public void show() {
       m_result =  JOptionPane.showConfirmDialog(m_parentComponent, m_fontChooserPanel,
               StringResource.getStringResource("uibichart00050"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
   }
   
   public int getResult() {
       return m_result;
   }
	public Font getSelectedFont() {
	    if (m_result == JOptionPane.OK_OPTION) {
	        return m_fontChooserPanel.getSelectedFont();
	    }
	    return null;
	}  
	
	public static void main(String[] args) {
	    FontChooserDialog dlg = new FontChooserDialog(null, new Font(DefaultFormatValue.FONTNAME, TableConstant.FS_NORMAL, 14));	        
	    dlg.show();
	      
	   }   
}
