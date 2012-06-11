/*
 * Created on 2005-5-8
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.chart.panel;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITextField;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.Legend;

import com.ufida.report.chart.property.LegendProperty;
import com.ufida.report.chart.property.TitleProperty;
import com.ufsoft.iufo.resource.StringResource;
/**
 * @author caijie
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LegendPropPanel extends nc.ui.pub.beans.UIPanel implements ActionListener{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**图例位置选择项*/
    protected class Item{
        private String str;
        private int pos;
        
        public Item(String str, int pos) {
            this.str = str;
            this.pos = pos;
        }
        
        public String toString() {
            return str;
        }
        public int getPos() {
            return pos;
        }
    }
    private TitleProperty m_titleProperty = null;    
    private LegendProperty m_legendProperty = null;
    private JFreeChart m_sample = null;
    
	private JCheckBox showLegend = null;
	private JLabel jLabel = null;
	private JComboBox posotionBox = null;
	private JButton legendFontButton = null;
	private JButton legendColorButton = null;
	private JLabel jLabel1 = null;
	private JTextField title = null;
	private JTextField legendFontString = null;
	private JLabel jLabel2 = null;
	private JLabel jLabel3 = null;
	private JButton titleButton = null;
	private JPanel legendPanel = null;
	private JPanel titltPanel = null;
	private JCheckBox showTitle = null;
	private JLabel jLabel4 = null;
	private JLabel jLabel5 = null;
	private JButton titleFontButton = null;
	private JButton titleColorButton = null;
	private JTextField titleFontString = null;
	
	
	/**
	 * This is the default constructor
	 */
	public LegendPropPanel(final TitleProperty title , final LegendProperty legend, JFreeChart sample) {
		super();
		m_titleProperty = title;
		m_legendProperty = legend;		
		m_sample = sample;
		if(this.m_sample == null || m_titleProperty == null || m_legendProperty == null)
		    throw new IllegalArgumentException();	
		initialize();
		setLegendButtonEnable(getLegendProperty().isShowLegend());	
		setTitleButtonEnable(getTitleProperty().isVisible());
		
		m_titleProperty.applyProperties(sample);
		m_legendProperty.applyProperties(sample);
	}
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 * @i18n uibichart00021=图例
	 * @i18n miufo1001423=名称:
	 * @i18n miufo1000947=颜色：
	 * @i18n uibichart00022=字体:
	 */
	private  void initialize() {		
		jLabel3 = new nc.ui.pub.beans.UILabel();
		jLabel2 = new nc.ui.pub.beans.UILabel();
		jLabel1 = new nc.ui.pub.beans.UILabel();
		this.setLayout(null);
		this.setSize(300, 300);
		this.setName(StringResource.getStringResource("uibichart00021"));		
		this.setPreferredSize(new java.awt.Dimension(300,300));
		jLabel1.setText(StringResource.getStringResource("miufo1001423"));
		jLabel1.setBounds(15, 54, 38, 21);
		jLabel2.setText(StringResource.getStringResource("miufo1000947"));
		jLabel2.setBounds(13, 120, 38, 20);
		jLabel3.setText(StringResource.getStringResource("uibichart00022"));
		jLabel3.setBounds(13, 85, 38, 20);
		this.add(getLegendPanel(), null);
		this.add(getTitltPanel(), null);
	}
	private void setTitleButtonEnable(boolean enable) {	
	    this.getTitle().setEnabled(enable);
	    this.getTitleFontString().setEnabled(enable);
	    this.getTitleButton().setEnabled(enable);
	    this.getTitleFontButton().setEnabled(enable);
	    this.getTitleColorButton().setEnabled(enable);	
	}
	private void setLegendButtonEnable(boolean enable) {	
	    this.getPosotionBox().setEnabled(enable);
	    this.getLegendFontString().setEnabled(enable);	    
	    this.getLegendFontButton().setEnabled(enable);
	    this.getLegendColorButton().setEnabled(enable);	    	    
	}
	/**
	 * @i18n uibichart00023=位置:
	 */
	private JLabel getJLabel() {
		if (jLabel == null) {
		    jLabel = new nc.ui.pub.beans.UILabel();			
			jLabel.setText(StringResource.getStringResource("uibichart00023"));
			jLabel.setBounds(13, 50, 38, 20);
		}
		return jLabel;
	}
	/**
	 * This method initializes jCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 * @i18n uibichart00024=是否显示图例
	 */    
	private JCheckBox getShowLegend() {
		if (showLegend == null) {
			showLegend = new UICheckBox();
			showLegend.setText(StringResource.getStringResource("uibichart00024"));
			showLegend.setActionCommand("showLegend");
			showLegend.setName("showLegend");
			showLegend.setBounds(13, 15, 114, 20);
			
			showLegend.setSelected(getLegendProperty().isShowLegend());			
			showLegend.addActionListener(this);
		}
		return showLegend;
	}
	/**
	 * This method initializes jComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */    
	private JComboBox getPosotionBox() {
		if (posotionBox == null) {
			posotionBox = new UIComboBox(getPositionItems());	
			posotionBox.setBounds(-2147483648, -2147483648, -1, -1);
			int x = (int) this.getLegendFontString().getBounds().getMinX();
			int y = (int) this.getJLabel().getBounds().getMinY();
			
			posotionBox.setBounds(x, y, getTitle().getWidth(), getTitle().getHeight());
			initPosotionBox(posotionBox);			
			
			posotionBox.addActionListener(this);
		}
		return posotionBox;
	}
	
	/**
	 * @i18n uibichart00025=上
	 * @i18n uibichart00026=下
	 * @i18n uibichart00027=左
	 * @i18n uibichart00028=右
	 */
	private Object[] getPositionItems() {
	    Object items[] = new Item[4];
	    items[0] = new Item(StringResource.getStringResource("uibichart00025"), Legend.NORTH);	    
	    items[1] = new Item(StringResource.getStringResource("uibichart00026"), Legend.SOUTH);
	    items[2] = new Item(StringResource.getStringResource("uibichart00027"), Legend.WEST);
	    items[3] = new Item(StringResource.getStringResource("uibichart00028"), Legend.EAST);
	    return items;
	}
	private void initPosotionBox(JComboBox box) {
	    int count = box.getItemCount();
	    Item item = null;
	    for(int i = 0; i < count; i++ ) {
	        item = (Item) box.getItemAt(i);
	        if(item.getPos() == this.getLegendProperty().getAnchor()) {
	            box.setSelectedItem(item);
	            return;
	        }
	    }
	}
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 * @i18n miufo1000589=选择
	 */    
	private JButton getLegendFontButton() {
		if (legendFontButton == null) {
			legendFontButton = new nc.ui.pub.beans.UIButton();
			legendFontButton.setText(StringResource.getStringResource("miufo1000589"));
			legendFontButton.setBounds(198, 85, 75, 22);
			legendFontButton.addActionListener(this);
		}
		return legendFontButton;
	}
	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getLegendColorButton() {
		if (legendColorButton == null) {
			legendColorButton = new nc.ui.pub.beans.UIButton();
			legendColorButton.setBackground((Color)this.getLegendProperty().getPaint());	
			legendColorButton.setBounds(53, 120, 146, 22);
			legendColorButton.addActionListener(this);
		}
		return legendColorButton;
	}
	
//    /**
//     * @param m_sample The m_sample to set.
//     */
//    public void setSampleChart(JFreeChart m_sample) {
//        this.m_sample = m_sample;
//        m_oriLengend = m_sample.getLegend();
//        samplePanel.removeAll();
//        ((ChartPanel)samplePanel).setChart(m_sample);        
//    }
    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    /**
	 * @i18n uibichart00029=图名
	 * @i18n uibichart00029=图名
	 * @i18n uibichart00018=颜色选取
	 * @i18n uibichart00018=颜色选取
	 */
    public void actionPerformed(ActionEvent e) {   
        
        if(e.getSource()== getShowTitle()){             
            this.getTitleProperty().setVisible(this.getShowTitle().isSelected());
            setTitleButtonEnable(this.getShowTitle().isSelected());	
			showSamplePrivew();
		}	
        if(e.getSource()== getTitleButton()) {
            String str = (String) JOptionPane.showInputDialog(this, StringResource.getStringResource("uibichart00029"), StringResource.getStringResource("uibichart00029"), JOptionPane.QUESTION_MESSAGE, null, null,this.getTitle().getText());
            if(str != null) {
                this.getTitleProperty().setText(str);
                this.getTitle().setText(this.getTitleProperty().getText());
                showSamplePrivew(); 
            }                   
         }
        if(e.getSource()== getTitleFontButton()){             
            FontChooserDialog dlg = new FontChooserDialog(this, getLegendProperty().getFont());
            dlg.show();
            if(dlg.getResult() == JOptionPane.OK_OPTION) {
                if(dlg.getSelectedFont() != null) {
                    this.getTitleProperty().setFont(dlg.getSelectedFont());
                    titleFontString.setText(FontChooserDialog.getFontString(this.getTitleProperty().getFont()));                   
                    showSamplePrivew();                    
                }                
            }
		}
        
        if(e.getSource()== getTitleColorButton()) {      
            Color c = JColorChooser.showDialog(this, StringResource.getStringResource("uibichart00018"), (Color) this.getTitleProperty().getPaint());
    		if (c != null) {
    		    this.getTitleProperty().setPaint(c);
    		    getTitleColorButton().setBackground((Color) this.getTitleProperty().getPaint());
    		    showSamplePrivew();    		    
    		}
        }
        if(e.getSource()==showLegend){         
            this.getLegendProperty().setShowLegend(this.getShowLegend().isSelected());
            setLegendButtonEnable(this.getLegendProperty().isShowLegend());	
			showSamplePrivew();		
		}	
        
        
        if(e.getSource()==legendFontButton) {
            FontChooserDialog dlg = new FontChooserDialog(this, getLegendProperty().getFont());
            dlg.show();
            if(dlg.getResult() == JOptionPane.OK_OPTION) {
                if(dlg.getSelectedFont() != null) {
                    this.getLegendProperty().setFont(dlg.getSelectedFont());
                    legendFontString.setText(FontChooserDialog.getFontString(this.getLegendProperty().getFont()));                   
                    showSamplePrivew();                    
                }                
            }
        }
        if(e.getSource()== this.getLegendColorButton()) {
            Color c = JColorChooser.showDialog(this, StringResource.getStringResource("uibichart00018"), (Color) this.getLegendProperty().getPaint());
    		if (c != null) {
    		    this.getLegendProperty().setPaint(c);
    		    getLegendColorButton().setBackground((Color) this.getLegendProperty().getPaint());
    		    showSamplePrivew();    		    
    		}
        }
        
        if(e.getSource()==posotionBox) {
           Item item = (Item) posotionBox.getSelectedItem();
           this.getLegendProperty().setAnchor(item.getPos());
           showSamplePrivew();      
        }
        
    }
    
    /**
     * 显示预览效果
     */
    private void showSamplePrivew() {  
        this.getTitleProperty().applyProperties(this.getSample());
        this.getLegendProperty().applyProperties(this.getSample());
        }
	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getTitle() {
		if (title == null) {
			title = new UITextField();
			title.setText(this.getTitleProperty().getText());
			title.setEditable(false);			
			title.setBounds(58, 54, 136, 21);
		}
		return title;
	}
	/**
	 * This method initializes jTextField1	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getLegendFontString() {
		if (legendFontString == null) {
			legendFontString = new UITextField();
			legendFontString.setEditable(false);
			legendFontString.setText(FontChooserDialog.getFontString(getLegendProperty().getFont()));
			legendFontString.setBounds(53, 85, 139, 20);
		}
		return legendFontString;
	}
	
	
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 * @i18n miufo1001396=修改
	 */    
	private JButton getTitleButton() {
		if (titleButton == null) {
			titleButton = new nc.ui.pub.beans.UIButton();
			titleButton.setText(StringResource.getStringResource("miufo1001396"));
			titleButton.setBounds(200, 54, 75, 22);
			titleButton.addActionListener(this);
		}
		return titleButton;
	}
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 * @i18n uibichart00021=图例
	 */    
	private JPanel getLegendPanel() {
		if (legendPanel == null) {
			legendPanel = new UIPanel();
			legendPanel.setLayout(null);
			legendPanel.setBounds(7, 151, 286, 145);
			legendPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, StringResource.getStringResource("uibichart00021"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
			legendPanel.add(getJLabel(), null);
			legendPanel.add(jLabel3, null);
			legendPanel.add(getLegendFontString(), null);
			legendPanel.add(jLabel2, null);
			legendPanel.add(getLegendColorButton(), null);
			legendPanel.add(getPosotionBox(), null);
			legendPanel.add(getLegendFontButton(), null);
			legendPanel.add(getShowLegend(), null);
		}
		return legendPanel;
	}
	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 * @i18n uibichart00030=图标题
	 * @i18n miufo1000846=字体
	 * @i18n miufo1000860=颜色
	 */    
	private JPanel getTitltPanel() {
		if (titltPanel == null) {
			jLabel5 = new nc.ui.pub.beans.UILabel();
			jLabel4 = new nc.ui.pub.beans.UILabel();
			titltPanel = new UIPanel();
			titltPanel.setLayout(null);
			titltPanel.setBounds(7, 3, 286, 145);
			titltPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, StringResource.getStringResource("uibichart00030"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
			jLabel4.setBounds(15, 87, 38, 21);
			jLabel4.setText(StringResource.getStringResource("miufo1000846"));
			jLabel5.setBounds(15, 120, 38, 21);
			jLabel5.setText(StringResource.getStringResource("miufo1000860"));
			titltPanel.add(jLabel1, null);
			titltPanel.add(getTitle(), null);
			titltPanel.add(getTitleButton(), null);
			titltPanel.add(getShowTitle(), null);
			titltPanel.add(jLabel4, null);
			titltPanel.add(jLabel5, null);
			titltPanel.add(getTitleFontButton(), null);
			titltPanel.add(getTitleColorButton(), null);
			titltPanel.add(getTitleFontString(), null);
		}
		return titltPanel;
	}
	/**
	 * This method initializes jCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 * @i18n uibichart00031=是否显示标题
	 */    
	private JCheckBox getShowTitle() {
		if (showTitle == null) {
			showTitle = new UICheckBox();
			showTitle.setBounds(15, 21, 142, 21);
			showTitle.setText(StringResource.getStringResource("uibichart00031"));			
			showTitle.setSelected(getTitleProperty().isVisible());			
			showTitle.addActionListener(this);
		}
		return showTitle;
	}
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 * @i18n miufo1000589=选择
	 */    
	private JButton getTitleFontButton() {
		if (titleFontButton == null) {
			titleFontButton = new nc.ui.pub.beans.UIButton();
			titleFontButton.setBounds(200, 87, 75, 22);
			titleFontButton.setText(StringResource.getStringResource("miufo1000589"));
			titleFontButton.addActionListener(this);
		}
		return titleFontButton;
	}
	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getTitleColorButton() {
		if (titleColorButton == null) {
			titleColorButton = new nc.ui.pub.beans.UIButton();
			titleColorButton.setBounds(58, 117, 136, 22);			
			titleColorButton.setBackground((Color) this.getLegendProperty().getPaint());			
			titleColorButton.addActionListener(this);
		}
		return titleColorButton;
	}
	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getTitleFontString() {
		if (titleFontString == null) {
			titleFontString = new UITextField();
			titleFontString.setBounds(58, 87, 136, 21);
			titleFontString.setEditable(false);
			titleFontString.setText(FontChooserDialog.getFontString(this.getTitleProperty().getFont()));
		}
		return titleFontString;
	}
    /**
     * @return Returns the m_titleProperty.
     */
    public TitleProperty getTitleProperty() {
        return m_titleProperty;
    }
    /**
     * @return Returns the m_legendProperty.
     */
    public LegendProperty getLegendProperty() {
        return m_legendProperty;
    }
    /**
     * @return Returns the m_sample.
     */
    public JFreeChart getSample() {
        return m_sample;
    }
      }  //  @jve:decl-index=0:visual-constraint="24,19"
