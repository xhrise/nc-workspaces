package com.ufsoft.report.sysplugin.insertdelete;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRadioButton;

import com.ufsoft.report.dialog.BaseDialog;
import com.ufsoft.report.util.IUFOLogger;
import com.ufsoft.report.util.MultiLang;

/**
 * ɾ����Ԫ���С��жԻ���
 * @author caijie
 * @since 3.1
 */

public class DeleteInsertDialog extends BaseDialog {
      
	private static final long serialVersionUID = -2778899219986288372L;
	
	//ERROR ʹ�ñ��ؼ��еĳ���
    public final static int UNDEFINED = 0;
    public final static int CELL_MOVE_LEFT = 1;
    public final static int CELL_MOVE_UP   = 2;
    public final static int DELETE_ROW     = 3;
    public final static int DELTE_COLUMN   = 4;
    public final static int CELL_MOVE_RIGHT = 5;
    public final static int CELL_MOVE_DOWN   = 6;
    public final static int INSERT_ROW     = 7;
    public final static int INSERT_COLUMN   = 8;
    
    private boolean isAdd;
    int iSelected = UNDEFINED;
    /**
     * @param parentComponent
     */
    public DeleteInsertDialog(Component parentComponent,boolean isAdd) {
        super(parentComponent,isAdd?MultiLang.getString("uiuforep0000877"):MultiLang.getString("uiuforep0000710"), true);//"ɾ��"
        this.isAdd=isAdd;
        if(isAdd){
        	iSelected=CELL_MOVE_RIGHT;
        }else{
        	iSelected=CELL_MOVE_LEFT;
        }
        intialRadioButton(getDialogArea());
      }
    
    protected JPanel createDialogPane() {        
        JPanel panel = new UIPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(200,150));
        return  panel;
      }
    
    /**
     * ��ʼ����ѡť
     * @i18n report00078=�Ҳ൥Ԫ������
     * @i18n report00079=�·���Ԫ������
     */
    private void intialRadioButton(JPanel panel){        
        JRadioButton m_rbMoveLeft = new UIRadioButton();
        JRadioButton m_rbRow = new UIRadioButton();
        JRadioButton m_rbMoveUp = new UIRadioButton();
        JRadioButton m_rbColumn = new UIRadioButton();
        
        panel.add(m_rbMoveUp);
        panel.add(m_rbMoveLeft);
        panel.add(m_rbColumn);
        panel.add(m_rbRow);
        
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(m_rbMoveLeft);
        buttonGroup.add(m_rbRow);
        buttonGroup.add(m_rbMoveUp);
        buttonGroup.add(m_rbColumn);
        
        
        int radioWidth = 150;
        int radioHeight = 25;
        int radioX = 30;
        int radioY = 10;
        int radioInter = 25;
        if(this.isAdd){
        	 m_rbMoveLeft.setText(MultiLang.getString("miufo1001030"));
        }else{
        	 m_rbMoveLeft.setText(MultiLang.getString("report00078"));
        }
       
        m_rbMoveLeft.setBounds(new Rectangle(radioX, radioY, radioWidth, radioHeight));
        m_rbMoveLeft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setOperatorMethod(isAdd?CELL_MOVE_RIGHT:CELL_MOVE_LEFT);
            }
          });
        if(this.isAdd){
        	m_rbMoveUp.setText(MultiLang.getString("miufo1001031"));
        }else{
        	 m_rbMoveUp.setText(MultiLang.getString("report00079"));
        }      
        m_rbMoveUp.setBounds(new Rectangle(radioX, radioY + radioInter, radioWidth, radioHeight));
        m_rbMoveUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	setOperatorMethod(isAdd?CELL_MOVE_DOWN:CELL_MOVE_UP);
            }
          });
        
        m_rbRow.setText(MultiLang.getString("uiuforep0000707"));//����
        m_rbRow.setBounds(new Rectangle(radioX, radioY + 2*radioInter, radioWidth, radioHeight));
        m_rbRow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	setOperatorMethod(isAdd?INSERT_ROW:DELETE_ROW);
            }
          });        
        
        m_rbColumn.setText(MultiLang.getString("uiuforep0000708"));//����;
        m_rbColumn.setBounds(new Rectangle(radioX, radioY + 3*radioInter, radioWidth, radioHeight));
        m_rbColumn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	setOperatorMethod(isAdd?INSERT_COLUMN:DELTE_COLUMN);
            }
          });
       
        m_rbMoveLeft.setSelected(true);
    }  
    /**
     * @return ����ɾ����ʽ��
     */
    public int getOperatorMethod() {
        return iSelected;
    }
    /**
     * ����ɾ����ʽ
     * @param value Ҫ���õ�ɾ����ʽ��
     */
    public void setOperatorMethod(int value) {
        if(value>=this.UNDEFINED&&value<=INSERT_COLUMN) {           
        	iSelected = value;
            return;
        }
        
        IUFOLogger.getLogger(this).fatal(MultiLang.getString("uiuforep0000709"));//"�����ɾ������."
        throw new IllegalArgumentException(MultiLang.getString("uiuforep0000709"));
    }
    
    public void show() { 
        
       
        this.setResizable(false);
        pack();
        setLocationRelativeTo(null);
        super.show();
      }
}
  