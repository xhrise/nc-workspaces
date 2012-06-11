/*
 * Created on 2005-4-26
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.chart.common;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITextArea;

import com.ufsoft.iufo.resource.StringResource;
/**
 * @author caijie
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ChartTypePanel extends nc.ui.pub.beans.UIPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2793076096774371196L;
	class NameAndPictureModel extends DefaultListModel{
/**
		 * 
		 */
		private static final long serialVersionUID = 5797311743154135371L;

		//		public NameAndPictureModel(String[] names, ImageIcon[] pics){
//			for(int i = 0; i < names.length; ++i){
//				addElement(new Object[]{ names[i], pics[i]});
//			}
//		}
		public NameAndPictureModel(){
			super();
		}
		
		public void addElement(String name, ImageIcon pic){
			addElement(new Object[]{ name, pic});
		}
		public String getName(Object object){
			Object[] array = (Object[])object;
			return (String)array[0];
		}
		
		public Icon getIcon(Object object){
			Object[] array = (Object[])object;
			return (Icon)array[1];
		}
	}
	
	class NameAndPictureListCellRenderer extends nc.ui.pub.beans.UILabel implements ListCellRenderer{		
		/**
		 * 
		 */
		private static final long serialVersionUID = 2183938572001825814L;
		private javax.swing.border.Border lineBorder = BorderFactory.createLineBorder(Color.red, 2);
		private javax.swing.border.Border emptyBorder = BorderFactory.createEmptyBorder(2,2,2,2);
		
		public NameAndPictureListCellRenderer(){	
			super();
			setOpaque(true);
		}
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			NameAndPictureModel model = (NameAndPictureModel) list.getModel();
			this.setText(model.getName(value));
			this.setIcon(model.getIcon(value));
			if(isSelected){
				this.setForeground(list.getSelectionForeground());
				this.setBackground(list.getSelectionBackground());
			}else{
				this.setForeground(list.getForeground());
				this.setBackground(list.getBackground());
			}
			
			if(cellHasFocus) 	this.setBorder(lineBorder); else this.setBorder(emptyBorder);
			
			return this;
		}		
	}
	
	private JLabel jLabel = null;
	private JList chartTypeList = null;  //  @jve:decl-index=0:visual-constraint="-65,70"
	private JList subChartTypeList = null;
	private JLabel jLabel1 = null;
	private JTextArea typeDescArea = null;
	private JScrollPane jScrollPane = null;
	private JScrollPane jScrollPane1 = null;
	private JLabel jLabel2 = null;
	
	private int[] m_typeChartArr = null;//图形列表对应数组
    private int m_resultChartIndex = ChartPublic.UNDIFINED;//保存要设置的图表
    private int[] m_chartCategoryIndex = ChartPublic.getAllBasicType();
    
	/**
	 * This is the default constructor
	 */
	public ChartTypePanel(int type) {
		super();
		this.setName(StringResource.getStringResource("ubichart00157"));
		this.m_resultChartIndex = type;
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private  void initialize() {
		jLabel1 = new nc.ui.pub.beans.UILabel();
		jLabel = new nc.ui.pub.beans.UILabel();
		this.setLayout(null);
		jLabel2 = new nc.ui.pub.beans.UILabel();
		jLabel.setText(StringResource.getStringResource("ubichart00157"));
		jLabel.setBounds(45, 2, 82, 24);
		jLabel1.setBounds(307, 2, 82, 24);
		jLabel1.setText(StringResource.getStringResource("ubichart00158"));
		jLabel2.setBounds(307, 278, 82, 24);
		jLabel2.setText(StringResource.getStringResource("ubichart00159"));
		this.setBounds(0, 0, 600, 400);
		this.add(jLabel, null);
		this.add(jLabel1, null);
		this.add(getJScrollPane(), null);
		this.add(getJScrollPane1(), null);
		this.add(jLabel2, null);
		this.add(getTypeDescArea(), null);
		initCategoryAndTypeList();
	}
	/**
	 * This method initializes jList	
	 * 	
	 * @return javax.swing.JList	
	 */    
	public JList getChartTypeList() {
		if (chartTypeList == null) {
			chartTypeList = new UIList();
		}
		return chartTypeList;
	}
	/**
	 * This method initializes jList	
	 * 	
	 * @return javax.swing.JList	
	 */    
	public JList getSubChartTypeList() {
		if (subChartTypeList == null) {
			subChartTypeList = new UIList();
			subChartTypeList.setModel(new NameAndPictureModel());
			subChartTypeList.setCellRenderer(new NameAndPictureListCellRenderer());
			subChartTypeList.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
		return subChartTypeList;
	}
	/**
	 * This method initializes jTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */    
	public JTextArea getTypeDescArea() {
		if (typeDescArea == null) {
			typeDescArea = new UITextArea();
			typeDescArea.setLineWrap(true);
			typeDescArea.setEditable(false);
			typeDescArea.setEnabled(true);
			typeDescArea.setBounds(307, 305, 272, 84);
		}
		return typeDescArea;
	}
	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new UIScrollPane();
			jScrollPane.setBounds(23, 30, 255, 361);
			jScrollPane.setViewportView(getChartTypeList());
		}
		return jScrollPane;
	}
	/**
	 * This method initializes jScrollPane1	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new UIScrollPane();
			jScrollPane1.setBounds(307, 31, 272, 243);
			jScrollPane1.setViewportView(getSubChartTypeList());
		}
		return jScrollPane1;
	}
	private void initCategoryAndTypeList() {
        //准备图类       
        String[] items = new String[m_chartCategoryIndex.length];
        for(int i = 0; i < m_chartCategoryIndex.length; i++){
            items[i] = ChartPublic.getChartName(m_chartCategoryIndex[i]);
        }         
     
        //设置图类列表       
        DefaultListModel model = new DefaultListModel();
        for(int i = 0; i < items.length; i++)
            model.addElement(items[i]);       
        getChartTypeList().setModel(model);
        getChartTypeList().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);       
        
//      初始化值
        int initialCatIndex = 0;
        if(m_resultChartIndex != ChartPublic.UNDIFINED){
        	int parent = ChartPublic.getParent(m_resultChartIndex);
        	for(int i = 0; i < m_chartCategoryIndex.length; i++){
        		if(parent == m_chartCategoryIndex[i])initialCatIndex=i;
        	}
        }
        getChartTypeList().setSelectedIndex(initialCatIndex);       
        ArrayList subTypes = ChartPublic.getSubTypes(m_chartCategoryIndex[initialCatIndex]);
        if((subTypes != null) && (!subTypes.isEmpty())){
            m_typeChartArr = new int[subTypes.size()];
            for(int i = 0; i < subTypes.size();i++)
                m_typeChartArr[i] =((Integer)subTypes.get(i)).intValue();           
            NameAndPictureModel md = (NameAndPictureModel)getSubChartTypeList().getModel();
            md.removeAllElements();            
            int subSelectedIndex = 0;
            for(int i = 0; i < m_typeChartArr.length; i++)  {
           	  md.addElement(ChartPublic.getChartName(m_typeChartArr[i]), ChartPublic.getImageIcon(m_typeChartArr[i]));
           	  if(m_typeChartArr[i] == m_resultChartIndex) subSelectedIndex = i;
            }
            getSubChartTypeList().setSelectedIndex(subSelectedIndex);            
        //    m_resultChartIndex = m_typeChartArr[0];           
            getTypeDescArea().setText(ChartPublic.getChartHint(m_resultChartIndex));  
        }        
        
        
        //监听对图形子类列表的值改变     
        getSubChartTypeList().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent e) {               
                int selected = getSubChartTypeList().getSelectedIndex();
                if(selected == -1) {
                    m_resultChartIndex = ChartPublic.UNDIFINED;
                    getTypeDescArea().setText(null);
                    return;  
                }
                m_resultChartIndex = m_typeChartArr[selected];
                getTypeDescArea().setText(ChartPublic.getChartHint(m_resultChartIndex));
            }});
        
       
        
        //监听图形大类的改变,当图大类改变时自动加载选取图的所有子类，并加载图标的提示信息
        getChartTypeList().addListSelectionListener(new ListSelectionListener(){
         public void valueChanged(ListSelectionEvent e) {
             int selected = getChartTypeList().getSelectedIndex();            
             if(selected == -1 
                     || ChartPublic.getSubTypes(m_chartCategoryIndex[selected]) == null 
                     || ChartPublic.getSubTypes(m_chartCategoryIndex[selected]).isEmpty()) {
            	 NameAndPictureModel m1 = (NameAndPictureModel)getSubChartTypeList().getModel();
                 m1.removeAllElements();
                 m_typeChartArr = null;
                 getTypeDescArea().setText(null);
                 m_resultChartIndex = ChartPublic.UNDIFINED;
                 return;
             }
             
             ArrayList list = ChartPublic.getSubTypes(m_chartCategoryIndex[selected]);            
             m_typeChartArr = new int[list.size()];
             for(int i = 0; i < list.size();i++)
                 m_typeChartArr[i] =((Integer)list.get(i)).intValue();                    
             NameAndPictureModel m = (NameAndPictureModel)getSubChartTypeList().getModel();
             m.removeAllElements();
             
             int selectedIndex = 0;
             for(int i = 0; i < m_typeChartArr.length; i++)  {
            	 m.addElement(ChartPublic.getChartName(m_typeChartArr[i]), ChartPublic.getImageIcon(m_typeChartArr[i]));
            	 if(m_resultChartIndex == m_typeChartArr[i])selectedIndex = i;
             }
                 
             getSubChartTypeList().setSelectedIndex(selectedIndex);            
             getSubChartTypeList().firePropertyChange("ChartChanged", 0, 1);
             getTypeDescArea().setText(ChartPublic.getChartHint(m_resultChartIndex)); 
         }});
        
        
    }
    /**
     * @return Returns the m_resultChartIndex.
     */
    public int getSelectedChartIndex() {
        return m_resultChartIndex;
    }
}
