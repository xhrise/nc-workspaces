package com.ufsoft.report.toolbar.dropdown;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.plaf.basic.BasicComboPopup;
/**
 * <pre>
 * </pre>  JComboBox�ĵ�������չ����ӵ��������˵�Ч��
 * @author �����
 * @version 
 * Create on 2008-3-20
 */
public class SwatchPopuMenu extends BasicComboPopup implements ActionListener {
	/**�����������*/
	private JComboBox combo = null;
	/**�����˵�*/
	private JPopupMenu popupMenu = null;

	public SwatchPopuMenu(JComboBox combo) {
		super(combo);
		this.combo = combo;
		initialize();
	}
	  
	/**
	 * �������˵�����Ӽ�����
	 * @param JPopupMenu popupMenu
	 * @return 
	 */
	private void addItemListener(JPopupMenu popupMenu){
		if(popupMenu == null){
			return;
		}
		int itemCount = popupMenu.getComponentCount();
		if(itemCount<1){
			return;
		}
		for(int i=0;i<itemCount;i++){//��ÿ���˵�����Ӽ���
			Component component = popupMenu.getComponent(i);
			if(component instanceof JMenuItem){
				((JMenuItem)component).addActionListener(this);
			}
		}
	}
	
	private void initialize(){
		if (combo instanceof JPopupPanelButton) {
			popupMenu = (JPopupMenu) ((JPopupPanelButton) combo).getPopupCom();
		}				
		String[] itemAry = getItemValues(popupMenu);
		if(itemAry!=null){
			for(String strItem : itemAry)
				combo.addItem(strItem);
		}
		
		
		addItemListener(popupMenu);
	}
	
	/**
	 * ���ص����˵�����ʾλ��
	 * @param
	 * @return Point popupLocation
	 */
    public Point getPopupLocation() {
		Dimension popupSize = combo.getSize();
		Insets insets = getInsets();
		popupSize.setSize(popupSize.width - (insets.right + insets.left),
				combo.getMaximumRowCount());
		Rectangle popupBounds = computePopupBounds(0, combo.getBounds().height,
				popupSize.width, popupSize.height);
		Point popupLocation = popupBounds.getLocation();
		return popupLocation;
	}

    @Override
	/**
	 * ���ظ���
	 */
	protected int getPopupHeightForRowCount(int maxRowCount) {
		// TODO Auto-generated method stub
		return 100;
	}
    
    /**
	 * ���ÿ���˵�����������
	 * @param JPopupMenu menu
	 * @return String[] values �����������
	 */
    public static String[] getItemValues(JPopupMenu menu){
    	if(menu == null){
    		return null;
    	}
    	int itemCount = menu.getComponentCount();
    	if(itemCount<1){
    		return null;
    	}
    	String[] values = new String[itemCount];
		for(int i=0;i<itemCount;i++){//��ÿ���˵�����Ӽ���
			Component component = menu.getComponent(i);
			if(component instanceof JMenuItem){
				JMenuItem item = (JMenuItem)component;
				values[i] = item.getActionCommand();
			}
		}
		return values;
    }
    
    
	public void actionPerformed(ActionEvent e) {
          Object selectItem = e.getActionCommand();
          if(selectItem == null){
        	  return;
          }
          if(combo.isEditable() && combo.getEditor() != null){
        	  combo.configureEditor(combo.getEditor(), selectItem);
          }
          combo.setSelectedItem(selectItem);//��ģ������Ӳ���ֵ          
	}

}
