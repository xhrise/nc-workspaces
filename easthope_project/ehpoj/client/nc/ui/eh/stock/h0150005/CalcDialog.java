package nc.ui.eh.stock.h0150005;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.eh.refpub.PeriodMonthRefModel;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
/**
 * 说明：供应商评价年月参照
 * 作者：张志远
 * 时间：2009-11-11 9:44:15
 */
public class CalcDialog extends UIDialog implements ActionListener {
	//名称：
	private static final long serialVersionUID = 1L;
	//执行按钮
	 private javax.swing.JPanel ivjUIDialogContentPane = null;	 
	 private  nc.ui.pub.beans.UILabel UILabel1=null;
	 private  UIRefPane UIRefPeriod = null;
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

	//初始话
    private void initialize() {
            setName("ApproveDialog");
            this.setTitle("供应商评价");
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
    }
  
    //增加按钮等工作
    private javax.swing.JPanel getUIDialogContentPane() {
    	if (ivjUIDialogContentPane == null) {
                ivjUIDialogContentPane = new javax.swing.JPanel();
                ivjUIDialogContentPane.setName("UIDialogContentPane");
                ivjUIDialogContentPane.setLayout(null);              
                getUIDialogContentPane().add(getUILabel1(),getUILabel1().getName());
                getUIDialogContentPane().add(getUIRefPane1Period(),getUIRefPane1Period().getName());
                }
    	return ivjUIDialogContentPane;
    }
    
    private Component getUILabel1() {
		if(UILabel1==null)
		{
			UILabel1=new nc.ui.pub.beans.UILabel();
			UILabel1.setName("Label1");
			UILabel1.setText("会计期间：");
			UILabel1.setBounds(30, 15, 60, 20);
		}
		return UILabel1;
	}
    
    public nc.ui.pub.beans.UITextField getUITextField3(){
        if(TextField3==null)
        {
          try {
            TextField3=new nc.ui.pub.beans.UITextField();
            TextField3.setName("TextField3");
            TextField3.setBounds(100, 95, 150, 20); 
          } catch (java.lang.Throwable ivjExc) {
              
              handleException(ivjExc);
          }
        }
        return TextField3;
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
          } catch (java.lang.Throwable ivjExc) {
              handleException(ivjExc);
          }
        }
        return UIRefPeriod;
    }
      
    public void actionPerformed(ActionEvent e) {

    }
    
    private void handleException(java.lang.Throwable exception) {
        /* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
        System.out.println("--------- 未捕捉到的异常 ---------");
        exception.printStackTrace(System.out);
    }
}




