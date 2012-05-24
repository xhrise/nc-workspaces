package nc.ui.eh.stock.z0151001;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.refpub.PeriodMonthRefModel;
import nc.ui.eh.refpub.ReceiptsRefModel;
import nc.ui.eh.refpub.StorRefModel;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.vo.eh.kc.h0250210.PeriodVO;
import nc.vo.pub.BusinessException;
/**
 * 说明：批量关闭收货通知单
 * 作者：wb
 * 时间：2009-11-12 10:46:48
 */
public class CalcDialog extends UIDialog implements ActionListener {
	//执行按钮
	 private nc.ui.pub.beans.UIButton UIButtonComm = null;
	 private nc.ui.pub.beans.UIButton UIButtonCanel = null;	
	 private javax.swing.JPanel ivjUIDialogContentPane = null;	 
	 private  nc.ui.pub.beans.UILabel UILabel1=null;
	 private  nc.ui.pub.beans.UILabel UILabel2=null;
	 private  nc.ui.pub.beans.UILabel UILabel4=null;
	 private  UIRefPane UIRefPeriod = null;
	 private  UIRefPane UIRefStore = null;
	 
	 private nc.ui.pub.beans.UITextField TextField3=null;
     
     public static String pk_store = null;
     public static String pk_period = null;
     nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();
     String sDate = ce.getDate().toString();
     ReceiptsRefModel refModel = null;
	 @SuppressWarnings("deprecation")
	public CalcDialog()
	 {
		super();
		initialize();
	 }


   //按钮
	private nc.ui.pub.beans.UIButton getUIButtonComm(){
		if(UIButtonComm==null){
			UIButtonComm = new nc.ui.pub.beans.UIButton();
			UIButtonComm.setName("UIButtonComm");
			UIButtonComm.setText("确定");
			UIButtonComm.setBounds(70, 150, 71, 22);
			
		}
		return UIButtonComm;
	}
//	按钮
	private nc.ui.pub.beans.UIButton getUIButtonCanel(){
		if(UIButtonCanel==null)
			UIButtonCanel = new nc.ui.pub.beans.UIButton();
			UIButtonCanel.setName("UIButtonCanel");
			UIButtonCanel.setText("取消");
			UIButtonCanel.setBounds(190, 150, 71, 22);
		return UIButtonCanel;
	}
	//初始话
    private void initialize() {
            setName("ApproveDialog");
            this.setTitle("批量关闭");
            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setSize(330, 210);  
            setContentPane(getUIDialogContentPane());
            getUIButtonComm().addActionListener(this);
            getUIButtonCanel().addActionListener(this);
    }
  
    //增加按钮等工作
    private javax.swing.JPanel getUIDialogContentPane() {
    	if (ivjUIDialogContentPane == null) {
            
                ivjUIDialogContentPane = new javax.swing.JPanel();
                ivjUIDialogContentPane.setName("UIDialogContentPane");
                ivjUIDialogContentPane.setLayout(null);              
                getUIDialogContentPane().add(getUIButtonComm(),getUIButtonComm().getName());
                getUIDialogContentPane().add(getUIButtonCanel(),getUIButtonCanel().getName());
                getUIDialogContentPane().add(getUILabel1(),getUILabel1().getName());
//                getUIDialogContentPane().add(getUILabel2(),getUILabel2().getName());
                getUIDialogContentPane().add(getUIRefPane1Period(),getUIRefPane1Period().getName());
//                getUIDialogContentPane().add(getUIRefPaneStore(),getUIRefPaneStore().getName());
               // getUIDialogContentPane().add(getUITextField3(),getUITextField3().getName());
                }
    	return ivjUIDialogContentPane;
    }
    
    private Component getUILabel1() {
		// TODO Auto-generated method stub
		if(UILabel1==null)
		{
			UILabel1=new nc.ui.pub.beans.UILabel();
			UILabel1.setName("Label1");
			UILabel1.setText("收货通知单：");
			UILabel1.setBounds(30, 35, 80, 20);
		}
		return UILabel1;
	}
    
    
    private Component getUILabel2() {
		// TODO Auto-generated method stub
		if(UILabel2==null)
		{
			UILabel2=new nc.ui.pub.beans.UILabel();
			UILabel2.setName("Label2");
			UILabel2.setText("仓  库：");
			UILabel2.setBounds(30, 55, 150, 20);
		}
		return UILabel2;
	}
    
    
    private Component getUILabel4() {
        // TODO Auto-generated method stub
        if(UILabel4==null)
        {
            UILabel4=new nc.ui.pub.beans.UILabel();
            UILabel4.setName("Label4");
            UILabel4.setText("密码错误！");
            UILabel4.setBounds(120, 125, 400, 20);
            UILabel4.setVisible(false);
        }
        return UILabel4;
    }

    
    public nc.ui.pub.beans.UITextField getUITextField3(){
        if(TextField3==null)
        {
          try {
            TextField3=new nc.ui.pub.beans.UITextField();
            TextField3.setName("TextField3");
            TextField3.setBounds(100, 95, 150, 20); 
//            TextField3.setEditable(true);
//            TextField3.setEnabled(true);
          } catch (java.lang.Throwable ivjExc) {
              
              handleException(ivjExc);
          }
        }
        return TextField3;
    }
    
    private nc.ui.pub.beans.UIRefPane getUIRefPaneStore(){
    	if(UIRefStore==null)
    	{
          try {
        	UIRefStore = new UIRefPane();
        	StorRefModel RefStroe = new StorRefModel();
        	UIRefStore.setName("UIRefStore");
        	UIRefStore.setBounds(100, 55, 150, 20);
//        	UIRefStore.setEditable(true);
//        	UIRefStore.setEnabled(true);
        	UIRefStore.setRefModel(RefStroe);
        	
          } catch (java.lang.Throwable ivjExc) {
              
              handleException(ivjExc);
          }
    	}
    	return UIRefStore;
    }
    
    public nc.ui.pub.beans.UIRefPane getUIRefPane1Period(){
        if(UIRefPeriod==null)
        {
          try {
        	UIRefPeriod = new UIRefPane();
        	refModel = new ReceiptsRefModel();
        	UIRefPeriod.setName("UIRefPeriod");
        	UIRefPeriod.setBounds(120, 35, 150, 20);  
        	UIRefPeriod.setRefModel(refModel);
        	UIRefPeriod.setPK(pk_period);
        	UIRefPeriod.setMultiSelectedEnabled(true);
        	UIRefPeriod.setTreeGridNodeMultiSelected(true);
//        	UIRefPeriod.setEditable(false);
//        	UIRefPeriod.setEnabled(false);
        	
          } catch (java.lang.Throwable ivjExc) {
              handleException(ivjExc);
          }
        }
        return UIRefPeriod;
    }
      
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == getUIButtonComm()) {
           String[] pks = getUIRefPane1Period().getRefPKs();
           if(pks==null){
		    	JOptionPane.showMessageDialog(this, "没有选择收货通知单!", "警告",0);
		        return; 
           }
           PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
           try {
			   String pkss = PubTools.combinArrayToString(pks);
			   String sql = " update eh_stock_receipt set lock_flag = 'Y' where pk_receipt in "+pkss+"";
			   pubItf.updateSQL(sql);
			   JOptionPane.showMessageDialog(this, "成功关闭"+(pks.length)+"张单据!", "提示",0);
			   UIRefPeriod.setPK(null);
			   refModel.reloadData();
           }catch (Exception e1) {
			  e1.printStackTrace();
		    }
        }
        if (e.getSource() == getUIButtonCanel()) {
            this.closeOK();
            pk_store = null;
            pk_period = null;
        }
    }
    

    private void handleException(java.lang.Throwable exception) {
        /* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
        System.out.println("--------- 未捕捉到的异常 ---------");
        exception.printStackTrace(System.out);
    }
}




