/*
 * Created on 2005-6-29
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.rep.applet;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITextField;

import com.ufida.report.rep.model.FilterConditionItem;
import com.ufida.report.rep.model.FilterRowDescriptor;
import com.ufida.report.rep.model.IColumnData;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
/**
 * @author caijie
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FilterRowDlg extends UfoDialog {

	private static final long serialVersionUID = -1180473186251360367L;

	private javax.swing.JPanel jContentPane = null;

	private JPanel jPanel = null;
//	private JCheckBox filterRowCB = null;
	private JLabel maxLineNumLabel = null;
	private UITextField maxLineNumTF = null;
	private JButton OKBtn = null;
	private JButton cancelBtn = null;
//	private ButtonGroup buttonGroup = null;
	
	private FilterRowDescriptor filterRowDescriptor = null;
//	private IColumnData[] fields = null;
//	private boolean oriFilterCondionIsNull = false;
	/**
	 * This is the default constructor
	 */
	public FilterRowDlg(Container parent, FilterRowDescriptor filterRowDescriptor, IColumnData[] fields) {
		super(parent);
		if(fields == null || fields.length == 0) return ;
//		if(filterRowDescriptor == null) {
//		    oriFilterCondionIsNull = true;
//		}
		this.filterRowDescriptor = filterRowDescriptor;		
//		this.fields = fields;	
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 * @i18n mbirep00009=筛选行数
	 */
	private void initialize() {
		this.setSize(290, 148);
		this.setTitle(StringResource.getStringResource("mbirep00009"));
		this.setContentPane(getJContentPane());
	}
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if(jContentPane == null) {
			jContentPane = new UIPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJPanel(), null);
			jContentPane.add(getOKBtn(), null);
			jContentPane.add(getCancelBtn(), null);
		}
		return jContentPane;
	}
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 * @i18n mbirep00009=筛选行数
	 */    
	private JPanel getJPanel() {
		if (jPanel == null) {			
			jPanel = new UIPanel();
			jPanel.setLayout(null);
			jPanel.setBounds(6, 5, 266, 80);
			jPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, StringResource.getStringResource("mbirep00009"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));	
			
//			jPanel.add(getFilterRowCB(), null);
			jPanel.add(getMaxLineNumLabel(), null);
			jPanel.add(getMaxLineNumTF(), null);
		}
		return jPanel;
	}
	
	/**
	 * @i18n mbirep00010=最大行数:
	 */
	private JLabel getMaxLineNumLabel() {
	    if(maxLineNumLabel == null) {
	        maxLineNumLabel = new nc.ui.pub.beans.UILabel();
	        maxLineNumLabel.setBounds(12, 46, 65, 23);		
			maxLineNumLabel.setText(StringResource.getStringResource("mbirep00010"));
	    }
	    return maxLineNumLabel;
	}
	/**
	 * This method initializes filterRowCB	
	 * 	
	 * @return javax.swing.JCheckBox	
	 * @i18n mbirep00011=启用行数筛选
	 */  
	/*
	public JCheckBox getFilterRowCB() {
		if (filterRowCB == null) {
			filterRowCB = new UICheckBox();
			filterRowCB.setText(StringResource.getStringResource("mbirep00011"));
			filterRowCB.setBounds(12, 21, 109, 19);
			if(oriFilterCondionIsNull) {
			    filterRowCB.setSelected(false);                
            }else {
                filterRowCB.setSelected(true);       
            }		
			setRowAreaStatus(filterRowCB.isSelected());
			filterRowCB.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if(filterRowCB.isSelected()) {
                        if(filterRowDescriptor == null) {
                            filterRowDescriptor = new FilterRowDescriptor();
                        }
                    }
                    setRowAreaStatus(filterRowCB.isSelected());
                }
			    });
		}
		return filterRowCB;
	}
	*/
//	private void setRowAreaStatus(boolean enable) {
//	    if(enable) {	           
//	        this.getFieldsListLabel().setEnabled(true);
//	        this.getFiledsList().setEnabled(true);	
	        
//	        if(this.getFiledsList().getSelectedIndex() == -1) {
//	            setFiledSelectedStatus(false);
//	        }else {
//	            setFiledSelectedStatus(true);
//	        }		        
//	    }else {
//	        this.getFieldsListLabel().setEnabled(false);
//	        this.getFiledsList().setEnabled(false);	    
//	        setFiledSelectedStatus(false);
//	    }  
	   
//	}
	
//	private void setFiledSelectedStatus(boolean enalbe) {
//	    this.getTypeLabel().setEnabled(enalbe);
//	    this.getAscendingRB().setEnabled(enalbe);
//	    this.getDecendingRB().setEnabled(enalbe);
//	    this.getMaxLineNumTF().setEditable(enalbe);
//	    this.getMaxLineNumTF().setEnabled(enalbe);
//	    this.getMaxLineNumLabel().setEnabled(enalbe);
//	}
//	private  ButtonGroup getButtonGroup() {
//        if(buttonGroup == null) {
//            buttonGroup = new ButtonGroup();
//        }
//        return buttonGroup;
//    }   
	/**
	 * This method initializes maxLineNumTF	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private UITextField getMaxLineNumTF() {
		if (maxLineNumTF == null) {
			maxLineNumTF = new UITextField();
			maxLineNumTF.setMinValue(0);
			maxLineNumTF.setBounds(77, 46, 177, 23);
			if( getFilterRowDesc() != null) {
			    setMaxNumbTOTF(getFilterRowDesc().getMaxLineNum());
			}		
				
			maxLineNumTF.addFocusListener(new FocusListener() {
                public void focusGained(FocusEvent e) {
                   
                    
                }

                public void focusLost(FocusEvent e) { 
                    try {
                    } catch (Exception e1) {                        
                    }finally {
//                        setMaxNumbTOTF(getFilterRowDesc().getMaxLineNum());
                    }
                }});
		}
		return maxLineNumTF;
	}
	
	private void setMaxNumbTOTF(int num) {
	    if(num == FilterConditionItem.UNDEFINED) {
	        getMaxLineNumTF().setText(null);
	    }else {
	        getMaxLineNumTF().setText(String.valueOf(num));
	    }
	}
	/**
	 * This method initializes OKBtn	
	 * 	
	 * @return javax.swing.JButton	
	 * @i18n miufo1003314=确定
	 */    
	private JButton getOKBtn() {
		if (OKBtn == null) {
			OKBtn = new nc.ui.pub.beans.UIButton();
			OKBtn.setBounds(45, 90, 75, 22);
			OKBtn.setText(StringResource.getStringResource("miufo1003314"));
			OKBtn.addActionListener(new ActionListener() {
                /**
				 * @i18n uimultical00001=非法的条件
				 */
                public void actionPerformed(ActionEvent e) {
                   	if(maxLineNumTF.getText() != null && maxLineNumTF.getText().length()>0){
                   		try{
                       		getFilterRowDesc().setMaxLineNum(Integer.parseInt(maxLineNumTF.getText()));
                   			getFilterRowDescriptor().checkValid();
                        } catch (Exception e1) {
                            JOptionPane.showMessageDialog(getJContentPane(), StringResource.getStringResource("miufopublic389")//e1.getMessage(),
                            		, StringResource.getStringResource("uimultical00001"), JOptionPane.ERROR_MESSAGE);
			                return;
                        }
                   	}
                   	else{
                        filterRowDescriptor = null;
                   	}

                   setResult(UfoDialog.ID_OK);
                   close();
                }
			    });
		}
		return OKBtn;
	}
	/**
	 * This method initializes cancelBtn	
	 * 	
	 * @return javax.swing.JButton	
	 * @i18n miufo1003315=取消
	 */    
	private JButton getCancelBtn() {
		if (cancelBtn == null) {
			cancelBtn = new nc.ui.pub.beans.UIButton();
			cancelBtn.setBounds(160, 90, 75, 22);
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
	 private FilterRowDescriptor getFilterRowDesc() {
	        if( filterRowDescriptor == null) {
	            filterRowDescriptor = new FilterRowDescriptor();
	        }
	        return filterRowDescriptor;
	    }
	 
	 /***
	  * 返回最终结果
	  * @return
	  */
	 public FilterRowDescriptor getFilterRowDescriptor() {
//	     if(getFilterRowCB().isSelected()) {	        
	         return filterRowDescriptor;
//	     }else {
//	         return null;
//	     }	       
	    }
   public static void main(String[] args) {
//        SelectQueryModelDlg dlg = new SelectQueryModelDlg(null);
//        dlg.setLocationRelativeTo(null);
//        dlg.show();
//        if ((dlg.getResult() == UfoDialog.ID_OK)
//                && (dlg.getQueryModel() != null)) {
//            MetaDataVO[] fileds = QueryModelSrv.getDimFlds(dlg.getQueryModel().getID());
//            if(fileds != null) {
//                FilterRowDlg fd = new FilterRowDlg(null, null, BaseReportUtil.convertToDefalutReportField(fileds));
//             
//                fd.show();
//            }            
//        }       
    }  
}  //  @jve:decl-index=0:visual-constraint="8,7"
 