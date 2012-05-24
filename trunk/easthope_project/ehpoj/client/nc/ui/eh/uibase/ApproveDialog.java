package nc.ui.eh.uibase;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.bs.framework.exception.ComponentException;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.pub.beans.UIDialog;
import nc.vo.framework.rsa.Encode;
import nc.vo.pub.BusinessException;
/**打卡审批
 * @author 洪海
 * 2008-04-18 下午04:03:18
 */
public class ApproveDialog extends UIDialog implements ActionListener {
	//执行按钮
	 private nc.ui.pub.beans.UIButton UIButtonComm = null;
	 private nc.ui.pub.beans.UIButton UIButtonCanel = null;	
	 private javax.swing.JPanel ivjUIDialogContentPane = null;	 
	 private  nc.ui.pub.beans.UILabel UILabel1=null;
	 private  nc.ui.pub.beans.UILabel UILabel2=null;
	 private  nc.ui.pub.beans.UILabel UILabel3=null;
	 private  nc.ui.pub.beans.UILabel UILabel4=null;
	 
	 
	 private nc.ui.pub.beans.UITextField TextField1=null;
	 private nc.ui.pub.beans.UITextField TextField2=null;
	 private nc.ui.pub.beans.UITextField TextField3=null;
     
     public static String cuserid=null;
	 
	 @SuppressWarnings("deprecation")
	public ApproveDialog()
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
            this.setTitle("刷卡审批");
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
                getUIDialogContentPane().add(getUILabel2(),getUILabel2().getName());
                getUIDialogContentPane().add(getUILabel3(),getUILabel3().getName());
                getUIDialogContentPane().add(getUILabel4(),getUILabel4().getName());
                getUIDialogContentPane().add(getUITextField1(),getUITextField1().getName());
                getUIDialogContentPane().add(getUITextField2(),getUITextField2().getName());
                getUIDialogContentPane().add(getUITextField3(),getUITextField3().getName());
                }
    	return ivjUIDialogContentPane;
    }
    
    private Component getUILabel1() {
		// TODO Auto-generated method stub
		if(UILabel1==null)
		{
			UILabel1=new nc.ui.pub.beans.UILabel();
			UILabel1.setName("Label1");
			UILabel1.setText("工号：");
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
			UILabel2.setText("姓名：");
			UILabel2.setBounds(30, 55, 150, 20);
		}
		return UILabel2;
	}
    private Component getUILabel3() {
		// TODO Auto-generated method stub
		if(UILabel3==null)
		{
			UILabel3=new nc.ui.pub.beans.UILabel();
			UILabel3.setName("Label3");
			UILabel3.setText("密码：");
			UILabel3.setBounds(30, 95, 400, 20);
		}
		return UILabel3;
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
            TextField3.setEditable(true);
            TextField3.setEnabled(true);
          } catch (java.lang.Throwable ivjExc) {
              
              handleException(ivjExc);
          }
        }
        return TextField3;
    }
    
    private nc.ui.pub.beans.UITextField getUITextField2(){
    	if(TextField2==null)
    	{
          try {
            TextField2=new nc.ui.pub.beans.UITextField();
            TextField2.setName("TextField2");
            TextField2.setBounds(100, 55, 150, 20);
            TextField2.setEditable(false);
            TextField2.setEnabled(false);
          } catch (java.lang.Throwable ivjExc) {
              
              handleException(ivjExc);
          }
    	}
    	return TextField2;
    }
    
    public nc.ui.pub.beans.UITextField getUITextField1(){
        if(TextField1==null)
        {
          try {
            TextField1=new nc.ui.pub.beans.UITextField();
            TextField1.setName("TextField1");
            TextField1.setBounds(100, 15, 150, 20);  
            TextField1.setEditable(true);
            TextField1.setEnabled(true);
          } catch (java.lang.Throwable ivjExc) {
              handleException(ivjExc);
          }
        }
        return TextField1;
    }
      
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == getUIButtonComm()) {
//        	getPriceVo();
            
            try {
                Encode encode=new Encode(); 
                String user_code=getUITextField1().getText().toString();
                String password=getUITextField3().getText().toString();
                IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
                HashMap hm=(HashMap)iUAPQueryBS.executeQuery("select cuserid from sm_user where user_code='"+user_code+"' and user_password='"+encode.encode(password)+"'",
                        new MapProcessor());
//                int count=Integer.parseInt(hm.get("count")==null?"0":hm.get("count").toString());
                if(hm==null){
                    getUILabel4().setVisible(true);
                    return;
                }
                cuserid=hm.get("cuserid").toString();
                int i=0;
            } catch (ComponentException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (NumberFormatException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (BusinessException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            
        	this.closeOK();
        }
        if (e.getSource() == getUIButtonCanel()) {
            this.closeOK();
        }
    }
    
//    public String getPriceVo()
//    {
//    	String values=TextField1.getText();
//    	return values;
//    }
//    public static void main(String[] arg) {
//    	QueryDialog dialog = new QueryDialog();
//		dialog.showModal();
//    }
    
    private void handleException(java.lang.Throwable exception) {
        /* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
        System.out.println("--------- 未捕捉到的异常 ---------");
        exception.printStackTrace(System.out);
    }
}




