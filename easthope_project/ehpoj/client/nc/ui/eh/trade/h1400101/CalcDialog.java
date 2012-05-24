package nc.ui.eh.trade.h1400101;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.eh.refpub.PeriodMonthRefModel;
import nc.ui.eh.refpub.StorRefModel;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.vo.eh.kc.h0250210.PeriodVO;
import nc.vo.pub.BusinessException;
/**
 * 说明：原料库存月报表计算界面
 * 类型：ZA48
 * 作者：wb
 * 时间：2008-5-12 9:44:15
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
			UIButtonComm.setText("计算");
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
            this.setTitle("计算库存月报表");
            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setSize(330, 210);  
            IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
            String sql = "select pk_period FROM eh_period WHERE nyear = "+sDate.substring(0, 4)+" and nmonth = "+sDate.substring(5,7)+" and pk_corp = '"+ce.getCorporation().getPk_corp()+"'";
            try {
            	pk_period = iUAPQueryBS.executeQuery(sql, new ColumnProcessor())==null?null:iUAPQueryBS.executeQuery(sql, new ColumnProcessor()).toString();
            }catch(Exception e){
            	e.printStackTrace();
            }
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
			UILabel1.setText("会计期间：");
			UILabel1.setBounds(30, 15, 60, 20);
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
        	PeriodMonthRefModel RefPeriodMonth = new PeriodMonthRefModel();
        	UIRefPeriod.setName("UIRefPeriod");
        	UIRefPeriod.setBounds(100, 15, 150, 20);  
        	UIRefPeriod.setRefModel(RefPeriodMonth);
        	UIRefPeriod.setPK(pk_period);
//        	UIRefPeriod.setEditable(false);
//        	UIRefPeriod.setEnabled(false);
        	
          } catch (java.lang.Throwable ivjExc) {
              handleException(ivjExc);
          }
        }
        return UIRefPeriod;
    }
    /**
     * 功能：点击计算后进入该方法 
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == getUIButtonComm()) {
           pk_period = getUIRefPane1Period().getRefPK();
//           pk_store = getUIRefPaneStore().getRefPK();
           if(pk_period==null){
		    	JOptionPane.showMessageDialog(this, "期间不能为空!", "警告",0);
		        return; 
           }
           IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
           try {
			PeriodVO perVO = (PeriodVO) iUAPQueryBS.retrieveByPK(PeriodVO.class, pk_period);
		    boolean jz_flag =  perVO.getJz_flag()==null?false:perVO.getJz_flag().booleanValue();
		    if(jz_flag){
		    	JOptionPane.showMessageDialog(this, perVO.getNyear()+"年"+perVO.getNmonth()+"月的会计期间已结帐，不允许重新计算", "警告",0);
		        return;
		    }
           }catch (BusinessException e1) {
			  e1.printStackTrace();
		    }
           this.closeOK();
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




