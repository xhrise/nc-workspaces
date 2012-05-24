/*
 * Created on 2006-6-15
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package nc.ui.wb.impor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UITextField;


/**
 * ˵�������ݵ���  ��SQLSERVER �����ݵ��뵽Oracle��
 * ���ߣ�����
 * 2009-7-23 11:50:32
 */
public class ClientUI extends ToftPanel{
	
    nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();
    String sDate = ce.getDate().toString();
    
    private UILabel         lblTable        = null;     //�����ı�
    private UITextField 	txtTables 		= null;		//������			
    
    private UIButton        btnExport       = null;     //ִ��
    
	public ClientUI() {
		super();
		initialize();	
		iniVar();
	}
	
   @Override
   public String getTitle() {
		// TODO Auto-generated method stub
		return "���ݵ�������(SQLSERVER->ORACLE)";
	}
	
	private void handleException(java.lang.Throwable exception) {
		/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
		System.out.println("--------- δ��׽�����쳣 ---------");
		exception.printStackTrace(System.out);
	}
	
	protected void initialize() {
		try {
	    this.setName("Export");
		this.setSize(500, 300);
		
		this.setTitleText("���ݵ�������(SQLSERVER->ORACLE)");
		this.setLayout(null);
        
        this.add(this.getLblTable(),this.getLblTable().getName());  //���汾Label����panel
        this.add(this.getTxtTables(),this.getTxtTables().getName()); 
        this.add(this.getBtnExport(),this.getBtnExport().getName());
      
		} catch (java.lang.Throwable e) {
			handleException(e);
		}
	}
	
	private void iniVar(){
		
	}
   
    /**
     * ����: �����ı�
     * @author chenjian
     * 2007-3-10 ����05:29:09
     */
    private UILabel getLblTable(){
        if (lblTable == null){
            try {
            	lblTable = new UILabel();
            	lblTable.setName("lblVersion");
            	lblTable.setText("���ݱ�(������� ',' ������Ĭ��Ϊϵͳ��eh��ͷ�ı�)");
            	lblTable.setBounds(50,90, 350, 20);
            	lblTable.setVisible(true);
            } catch (java.lang.Throwable e) {
                handleException(e);
            }
        } 
        return lblTable;      
    }
    
     
    
    /**
     * ����: ������ Text
     * @author wb
     * 2007-3-10 ����05:39:51
     */
    protected UITextField getTxtTables(){
        if (txtTables == null){
            try {
            	txtTables = new UITextField();
            	txtTables.setName("txtTables");
                
            	txtTables.setText("");
            	txtTables.setBounds(50, 120, 350, 20);
            	txtTables.setVisible(true);
            	txtTables.setText("bd_billtype,sm_funcregister");
            	txtTables.setEnabled(true);
            } catch (java.lang.Throwable e) {
                handleException(e);
            }
        } 
        return txtTables;      
    }
    
	
    /**
     * ����: ����
     * @author chenjian
     * 2007-4-9 ����10:13:43
     */
    private UIButton getBtnExport() {
        if (btnExport == null) {
            try {
                btnExport = new UIButton();
                btnExport.setName("btnExport");
                btnExport.setText("ִ��");
                btnExport.setBounds(120, 160, 100, 20);
                btnExport.setVisible(true);
                btnExport.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                           String inputTables = txtTables.getText();
                           PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
           				   String res = pubItf.exportMSdata(inputTables);
           				   JOptionPane.showMessageDialog(null,res,"��ʾ",JOptionPane.INFORMATION_MESSAGE);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                });
            } catch (java.lang.Throwable e) {
                handleException(e);
            }
        }
        return btnExport;
    }
    
   
    
 /**
 * ����: Main���
 * @param arg
 * @author chenjian
 * 2007-3-10 ����06:15:04
 */
   public static void main(String[] arg) {
        ClientUI oneUI = new ClientUI();
        javax.swing.JFrame frame = new javax.swing.JFrame();
        frame.setContentPane(oneUI);
        frame.setSize(oneUI.getSize());
        frame.setTitle("���ݵ���");

        frame.show();
        java.awt.Insets insets = frame.getInsets();
        frame.setSize(frame.getWidth() + insets.left + insets.right, frame
                .getHeight() + insets.top + insets.bottom);
        frame.setVisible(true);
    }

	@Override
	public void onButtonClicked(ButtonObject arg0) {
        // TODO Auto-generated method stub
        
    }

	

}
