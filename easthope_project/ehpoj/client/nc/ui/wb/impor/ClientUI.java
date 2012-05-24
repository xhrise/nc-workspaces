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
 * 说明：数据导入  将SQLSERVER 中数据导入到Oracle中
 * 作者：王兵
 * 2009-7-23 11:50:32
 */
public class ClientUI extends ToftPanel{
	
    nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();
    String sDate = ce.getDate().toString();
    
    private UILabel         lblTable        = null;     //导出的表
    private UITextField 	txtTables 		= null;		//表内容			
    
    private UIButton        btnExport       = null;     //执行
    
	public ClientUI() {
		super();
		initialize();	
		iniVar();
	}
	
   @Override
   public String getTitle() {
		// TODO Auto-generated method stub
		return "数据导出工具(SQLSERVER->ORACLE)";
	}
	
	private void handleException(java.lang.Throwable exception) {
		/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
		System.out.println("--------- 未捕捉到的异常 ---------");
		exception.printStackTrace(System.out);
	}
	
	protected void initialize() {
		try {
	    this.setName("Export");
		this.setSize(500, 300);
		
		this.setTitleText("数据导出工具(SQLSERVER->ORACLE)");
		this.setLayout(null);
        
        this.add(this.getLblTable(),this.getLblTable().getName());  //将版本Label加入panel
        this.add(this.getTxtTables(),this.getTxtTables().getName()); 
        this.add(this.getBtnExport(),this.getBtnExport().getName());
      
		} catch (java.lang.Throwable e) {
			handleException(e);
		}
	}
	
	private void iniVar(){
		
	}
   
    /**
     * 功能: 导出的表
     * @author chenjian
     * 2007-3-10 下午05:29:09
     */
    private UILabel getLblTable(){
        if (lblTable == null){
            try {
            	lblTable = new UILabel();
            	lblTable.setName("lblVersion");
            	lblTable.setText("数据表(多个表用 ',' 隔开，默认为系统中eh开头的表)");
            	lblTable.setBounds(50,90, 350, 20);
            	lblTable.setVisible(true);
            } catch (java.lang.Throwable e) {
                handleException(e);
            }
        } 
        return lblTable;      
    }
    
     
    
    /**
     * 功能: 表内容 Text
     * @author wb
     * 2007-3-10 下午05:39:51
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
     * 功能: 导出
     * @author chenjian
     * 2007-4-9 上午10:13:43
     */
    private UIButton getBtnExport() {
        if (btnExport == null) {
            try {
                btnExport = new UIButton();
                btnExport.setName("btnExport");
                btnExport.setText("执行");
                btnExport.setBounds(120, 160, 100, 20);
                btnExport.setVisible(true);
                btnExport.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                           String inputTables = txtTables.getText();
                           PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
           				   String res = pubItf.exportMSdata(inputTables);
           				   JOptionPane.showMessageDialog(null,res,"提示",JOptionPane.INFORMATION_MESSAGE);
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
 * 功能: Main入口
 * @param arg
 * @author chenjian
 * 2007-3-10 下午06:15:04
 */
   public static void main(String[] arg) {
        ClientUI oneUI = new ClientUI();
        javax.swing.JFrame frame = new javax.swing.JFrame();
        frame.setContentPane(oneUI);
        frame.setSize(oneUI.getSize());
        frame.setTitle("数据导入");

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
