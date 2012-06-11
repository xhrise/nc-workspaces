/*
 * Created on 2005-6-17
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.rep.applet;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UIScrollPane;

import com.ufsoft.iufo.data.IMetaData;
import com.ufida.report.rep.model.SortVO;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;




/**
 * @author caijie
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SortDlg extends UfoDialog {

	private static final long serialVersionUID = 1L;

	private javax.swing.JPanel jContentPane = null;  //  @jve:decl-index=0:visual-constraint="47,81"

	private JList candidateFieldList = null;  //  @jve:decl-index=0:visual-constraint="410,73"
	private JList selectedFieldList = null;
	private JLabel jLabel = null;
	private JLabel jLabel1 = null;
	private JButton addBtn = null;
	private JButton deleteBtn = null;
	private JRadioButton ascendingRB = null;
	private JRadioButton dscendingRB = null;
	private JButton OKBtn = null;
	private JButton cancelBtn = null;
	private JScrollPane jScrollPane = null;
	private JScrollPane jScrollPane1 = null;
	private ButtonGroup buttonGroup = null;
	
	private SortVO sortVO = null;
	private ArrayList<IMetaData> candidateFields = null;
	private JButton moveUpBtn = null;
	private JButton moveDownBtn = null;
	private IMetaData[] allFields = null;
	/**
	 * This is the default constructor
	 * 
	 */
	public SortDlg(Container parent, final IMetaData[] allFields, SortVO sortVO) {
		super(parent);
		if(allFields == null ) throw new IllegalArgumentException();
		this.allFields = allFields;			
		this.sortVO = sortVO;		
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 * @i18n mbirep00008=设置排序
	 */
	private void initialize() {
		this.setTitle(StringResource.getStringResource("mbirep00008"));
		this.setContentPane(getJContentPane());
		this.setSize(408, 379);	
		
		reComputerCandidateFields();
		initCandidateFieldList();
		initselectedFieldList();
	}
	private void initselectedFieldList() {			
		DefaultListModel selectedFieldModel = (DefaultListModel) this.getSelectedFieldList().getModel();		
		selectedFieldModel.removeAllElements();		
		ArrayList<IMetaData> sortVOList = this.getSortVO().getSortList();
		for(int i = 0; i < sortVOList.size(); i++) {
		    if(sortVOList.get(i) != null) {
		        IMetaData vo = sortVOList.get(i);
		        selectedFieldModel.addElement(vo);
		        getSelectedFieldList().setSelectedIndex(0);
		    }		    
		}
	}
	private void initCandidateFieldList() {
	    DefaultListModel candidateFieldModel = (DefaultListModel) this.getCandidateFieldList().getModel();
	    candidateFieldModel.removeAllElements();
	    for(int i = 0; i < candidateFields.size(); i++) {
		    if(candidateFields.get(i) != null) {
		        IMetaData vo = (IMetaData) candidateFields.get(i);
			    candidateFieldModel.addElement(vo);
			    getCandidateFieldList().setSelectedIndex(0);
		    }		    
		}
	}
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 * @i18n miufo1001438=字段
	 * @i18n mbiadhoc00041=排序字段
	 */
	private javax.swing.JPanel getJContentPane() {
		if(jContentPane == null) {
			jLabel1 = new nc.ui.pub.beans.UILabel();
			jLabel = new nc.ui.pub.beans.UILabel();
			jContentPane = new UIPanel();
			jContentPane.setLayout(null);
			jLabel.setBounds(14, 13, 87, 18);
			jLabel.setText(StringResource.getStringResource("miufo1001438"));
			jLabel1.setBounds(252, 13, 87, 18);
			jLabel1.setText(StringResource.getStringResource("mbiadhoc00041"));
			jContentPane.add(jLabel, null);
			jContentPane.add(jLabel1, null);
			jContentPane.add(getAddBtn(), null);
			jContentPane.add(getDeleteBtn(), null);
			jContentPane.add(getAscendingRB(), null);
			jContentPane.add(getDscendingRB(), null);
			jContentPane.add(getOKBtn(), null);
			jContentPane.add(getCancelBtn(), null);
			jContentPane.add(getJScrollPane(), null);
			jContentPane.add(getJScrollPane1(), null);
			jContentPane.add(getMoveUpBtn(), null);
			jContentPane.add(getMoveDownBtn(), null);
		}
		return jContentPane;
	}
	/**
	 * This method initializes jList	
	 * 	
	 * @return javax.swing.JList	
	 */    
	private JList getCandidateFieldList() {
		if (candidateFieldList == null) {
			candidateFieldList = new UIList(new DefaultListModel());
		}
		return candidateFieldList;
	}
	/**
	 * This method initializes jList1	
	 * 	
	 * @return javax.swing.JList	
	 */    
	private JList getSelectedFieldList() {
		if (selectedFieldList == null) {
			selectedFieldList = new UIList(new DefaultListModel());
			selectedFieldList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			selectedFieldList.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    
                    try {
                        IMetaData value = (IMetaData) selectedFieldList.getModel().getElementAt(selectedFieldList.getSelectedIndex());                        
                        int type = getSortVO().getType(value);
                        if(type == SortVO.SORT_ASCENDING) {
                            getAscendingRB().setSelected(true);
                            getDscendingRB().setSelected(false);
                        }else if(type == SortVO.SORT_DESCENDING) {
                            getAscendingRB().setSelected(false);
                            getDscendingRB().setSelected(true);
                        }else {
                            getAscendingRB().setSelected(false);
                            getDscendingRB().setSelected(false);
                        }
                    } catch (Exception e1) {                       
                    }                    
                }});			
		}
		return selectedFieldList;
	}
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 * @i18n miufo1001141=添加
	 */    
	private JButton getAddBtn() {
		if (addBtn == null) {
			addBtn = new nc.ui.pub.beans.UIButton();
			addBtn.setBounds(168, 51, 75, 22);
			addBtn.setText(StringResource.getStringResource("miufo1001141"));
			addBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                   Object[] objArr = getCandidateFieldList().getSelectedValues();
                   if(objArr == null || objArr.length == 0) return; 
                   IMetaData[] arr = new IMetaData[objArr.length];
                   for(int i = 0; i < objArr.length; i++) { 
                       if(objArr[i] == null) continue;
                       arr[i] = (IMetaData) objArr[i];
                   }
                   addSortFiled(arr);
                }
			    });
		}
		return addBtn;
	}
	
	/**
	 * 添加排序字段
	 * @param arr
	 */
	private void addSortFiled(IMetaData[] arr) {
	    if(arr == null || arr.length == 0) return;       
        for(int i = 0; i < arr.length; i++) {  
            if(arr[i] == null) continue;
            getSortVO().addSort(arr[i], SortVO.SORT_ASCENDING); 
        }   
        reComputerCandidateFields();
        this.initCandidateFieldList();
        this.initselectedFieldList(); 
        setFocusElements(getSelectedFieldList(), arr);
        this.getCandidateFieldList().setSelectedIndex(0);
	}
	
	/**
	 * 添加排序字段
	 * @param arr
	 */
	private void removeSortFiled(IMetaData vo) {
	    if(vo == null ) return;
        getSortVO().removeSort(vo); 
        reComputerCandidateFields();
        this.initCandidateFieldList();
        this.initselectedFieldList(); 
        setFocusElements(getCandidateFieldList(), new IMetaData[] {vo});
        this.getSelectedFieldList().setSelectedIndex(0);
	}
	
	/**将已经排序的元素从候选列表中删除*/
    public void reComputerCandidateFields() {   
   	   ArrayList<IMetaData> result = new ArrayList<IMetaData>();	   
   	     for(int i = 0; i < allFields.length; i++) {	       
   	       if(allFields[i] != null) {
   	         if(getSortVO().getOrder(allFields[i]) == SortVO.UNDIFINED) {
   	           result.add(allFields[i]);
   	         }
   	       }
   	     }
   	  candidateFields = result;
    }
    
//    /**
//     * 上移动级别
//     * @param vo
//     * @param level
//     */
//    private void moveUp(IMetaData vo, int level) {
//        if(vo == null ) return;
//        getSortVO().removeSort(vo); 
//        reComputerCandidateFields();
//        this.initCandidateFieldList();
//        this.initselectedFieldList(); 
//    }
	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 * @i18n ubiquery0110=删除
	 */    
	private JButton getDeleteBtn() {
		if (deleteBtn == null) {
			deleteBtn = new nc.ui.pub.beans.UIButton();
			deleteBtn.setBounds(168, 91, 75, 22);
			deleteBtn.setText(StringResource.getStringResource("ubiquery0110"));
			deleteBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    DefaultListModel model = (DefaultListModel) getSelectedFieldList().getModel();
                    int index = getSelectedFieldList().getSelectedIndex();
                    if(index == -1) return;
                    IMetaData vo = (IMetaData) model.get(index);
                    removeSortFiled(vo);                    
                }
			    });
		}
		return deleteBtn;
	}
	
	
	/**
	 * This method initializes jRadioButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 * @i18n miufo1001305=升序
	 */    
	private JRadioButton getAscendingRB() {
		if (ascendingRB == null) {
			ascendingRB = new UIRadioButton();
			getButtonGroup().add(ascendingRB);
			ascendingRB.setBounds(252, 273, 50, 18);
			ascendingRB.setText(StringResource.getStringResource("miufo1001305"));
			ascendingRB.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    IMetaData vo = (IMetaData) getSelectedFieldList().getModel().getElementAt(getSelectedFieldList().getSelectedIndex());                   
                   if(ascendingRB.isSelected()) {
                       getSortVO().setType(vo, SortVO.SORT_ASCENDING);
                   }else {
                       getSortVO().setType(vo, SortVO.SORT_DESCENDING);
                   }                    
                }
			    });
		}
		return ascendingRB;
	}
	/**
	 * This method initializes jRadioButton1	
	 * 	
	 * @return javax.swing.JRadioButton	
	 * @i18n miufo1001306=降序
	 */    
	private JRadioButton getDscendingRB() {
		if (dscendingRB == null) {
			dscendingRB = new UIRadioButton();
			getButtonGroup().add(dscendingRB);
			dscendingRB.setBounds(310, 273, 50, 18);
			dscendingRB.setText(StringResource.getStringResource("miufo1001306"));
			dscendingRB.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    IMetaData vo = (IMetaData) getSelectedFieldList().getModel().getElementAt(getSelectedFieldList().getSelectedIndex());                   
                    if(dscendingRB.isSelected()) {
                        getSortVO().setType(vo, SortVO.SORT_DESCENDING);
                    }else {
                        getSortVO().setType(vo, SortVO.SORT_ASCENDING);
                    }                
                }
			    });
		}
		return dscendingRB;
	}
	/**
	 * This method initializes jButton2	
	 * 	
	 * @return javax.swing.JButton	
	 * @i18n miufo1003314=确定
	 */    
	private JButton getOKBtn() {
		if (OKBtn == null) {
			OKBtn = new nc.ui.pub.beans.UIButton();
			OKBtn.setBounds(86, 318, 75, 22);
			OKBtn.setText(StringResource.getStringResource("miufo1003314"));
			OKBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                   setResult(UfoDialog.ID_OK);
                   close();
                }
			    });
		}
		return OKBtn;
	}
	/**
	 * This method initializes jButton3	
	 * 	
	 * @return javax.swing.JButton	
	 * @i18n miufo1003315=取消
	 */    
	private JButton getCancelBtn() {
		if (cancelBtn == null) {
			cancelBtn = new nc.ui.pub.beans.UIButton();
			cancelBtn.setBounds(242, 318, 75, 22);
			cancelBtn.setText(StringResource.getStringResource("miufo1003315"));
			cancelBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                   setResult(UfoDialog.ID_CANCEL);
                   close();
                }
			    });
		}
		return cancelBtn;
	}
	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new UIScrollPane();
			jScrollPane.setBounds(14, 35, 146, 226);
			jScrollPane.setViewportView(getCandidateFieldList());
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
			jScrollPane1.setBounds(252, 35, 145, 226);
			jScrollPane1.setViewportView(getSelectedFieldList());
		}
		return jScrollPane1;
	}
	private ButtonGroup getButtonGroup() {
	    if(buttonGroup == null) {
	        buttonGroup = new ButtonGroup();
	    }
	    return buttonGroup;
	}
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 * @i18n miufo1001298=上移
	 */    
	private JButton getMoveUpBtn() {
		if (moveUpBtn == null) {
			moveUpBtn = new nc.ui.pub.beans.UIButton();
			moveUpBtn.setBounds(168, 154, 75, 22);
			moveUpBtn.setText(StringResource.getStringResource("miufo1001298"));
			moveUpBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    DefaultListModel model = (DefaultListModel) getSelectedFieldList().getModel();
                    int index = getSelectedFieldList().getSelectedIndex();
                    if(index == -1) return ;
                    IMetaData vo = (IMetaData) model.get(index);
                    setSelectedOrder(vo, index - 1);
                }
			    });
		}
		return moveUpBtn;
	}
	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 * @i18n miufo1001290=下移
	 */    
	private JButton getMoveDownBtn() {
		if (moveDownBtn == null) {
			moveDownBtn = new nc.ui.pub.beans.UIButton();
			moveDownBtn.setBounds(168, 193, 75, 22);
			moveDownBtn.setText(StringResource.getStringResource("miufo1001290"));
			moveDownBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    DefaultListModel model = (DefaultListModel) getSelectedFieldList().getModel();
                    int index = getSelectedFieldList().getSelectedIndex();
                    if(index == -1) return ;
                    IMetaData vo = (IMetaData) model.get(index);
                    setSelectedOrder(vo, index + 1);
                }
			    });
		}
		return moveDownBtn;
	}
	/** 设置次序	 */
	private void setSelectedOrder(IMetaData vo, int order) {	    
        if(vo == null ) return;                   
        getSortVO().setOrder(vo, order);     
        initselectedFieldList(); 
        setFocusElements(getSelectedFieldList(), new IMetaData[] {vo});
	}
	/**设置列表中处于选择状态的元素*/
	private void setFocusElements(JList list, IMetaData[] voArr) {
	    if(list == null || voArr == null || voArr.length  == 0) return;	    
        int [] index = new int[voArr.length];
        for(int i = 0; i < voArr.length; i++) {
            index[i] = getElementIndex(list, voArr[i]);
        }
        list.setSelectedIndices(index);	   
	}
	
	/**获取列表中元素的位置*/
	private int getElementIndex(JList list, IMetaData vo) {
	    if(list == null || vo == null ) return -1;
	    DefaultListModel model = (DefaultListModel) list.getModel();
	    IMetaData tmp = null;
	    for(int i = 0; i < model.getSize(); i++) {
	        Object obj = model.get(i);
	        if(obj != null) {
	            tmp = (IMetaData) obj;
	            if(tmp.equals(vo)) {
	                return i;
	            }
	        }
	    }
	    return -1;
	}
    	 public static void main(String[] args) {  	     
	       
	    }  
    	 
    
    /**
     * @return Returns the sortVO.
     */
    private SortVO getSortVO() {
        if(sortVO == null) {
            sortVO = new SortVO();
        }
        return sortVO;
    }
    
    public SortVO getResultSort() {       
        return sortVO;
    }
    
    
}  //  @jve:decl-index=0:visual-constraint="10,10"
 