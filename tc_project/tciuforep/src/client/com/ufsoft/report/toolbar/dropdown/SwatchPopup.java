package com.ufsoft.report.toolbar.dropdown;

import java.awt.BorderLayout;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicComboPopup;

import com.ufida.iufo.pub.tools.AppDebug;

public class SwatchPopup extends BasicComboPopup implements ChangeListener {
	SwatchPanel swatchPanel=null;
	JComboBox combo = null;
	public SwatchPopup(JComboBox combo) {
		super(combo);
        this.combo = combo;
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder());

		if (combo instanceof JPopupPanelButton) {
			JComponent com = ((JPopupPanelButton) combo).getPopupCom();
			if (com instanceof SwatchPanel) {
				addPanel((SwatchPanel) com);
			}
		}

	}
    private void addPanel(SwatchPanel swatchPanel){
    	if(this.getComponentCount()>0){
    		this.removeAll();
    	}
    	this.swatchPanel=swatchPanel;
    	swatchPanel.addSwatchChangeListener(this);
    	add(swatchPanel, BorderLayout.CENTER);
    }
    
	@Override
	/**
	 * 必须要重载的方法
	 */
	protected int getPopupHeightForRowCount(int maxRowCount) {
		// TODO Auto-generated method stub
		return 100;
	}
	/**
     * 显示弹出面板
     */
    protected void firePropertyChange(String propertyName,
                                      Object oldValue,
                                      Object newValue){
        if(propertyName.equals("visible")){
            if(oldValue.equals(Boolean.FALSE)
               && newValue.equals(Boolean.TRUE)){ //SHOW
                try{
                    Object selectedItem = comboBox.getSelectedItem();
                    if(swatchPanel!=null){
                      swatchPanel.setSelectedSwatch(selectedItem);
                    }
                } catch(Exception ex){
                    AppDebug.debug(ex);
                }
            } else if(oldValue.equals(Boolean.TRUE)
                      && newValue.equals(Boolean.FALSE)){ //HIDE
            }
        }
        super.firePropertyChange(propertyName, oldValue, newValue);
    }

    public void stateChanged(ChangeEvent e){
    	Object selectedItem =e.getSource();
        if(comboBox.isEditable() && comboBox.getEditor() != null){
            comboBox.configureEditor(comboBox.getEditor(), selectedItem);
        }
        comboBox.setSelectedItem(selectedItem);
        comboBox.setPopupVisible(false);
        changeLeftIcon(selectedItem);//add by 王宇光 根据需要改变leftBtn 2008-5-22
    }
    
    private void changeLeftIcon(Object selectedItem) {
		if (selectedItem == null) {
			return;
		}
		if (swatchPanel instanceof ImagePanel) {
			ImagePanel imagePanel = (ImagePanel) swatchPanel;
			Hashtable imageTable = imagePanel.getImageTable();
			ImageIcon image = null;
			if (imageTable != null && imageTable.size() > 0) {
				image = (ImageIcon) imageTable.get(selectedItem);
			}
			if (combo instanceof JPopupPanelButton) {
				((JPopupPanelButton) combo).getBttLeft().setIcon(image);
			}
		}
	}

}
