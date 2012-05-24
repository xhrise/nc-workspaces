package nc.ui.eh.report.h090602001;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.eh.refpub.PeriodMonthRefModel;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
/**
 * ˵������˾ԭ�������ܱ�
 * ���ͣ�����
 * ���ߣ���־Զ
 * ʱ�䣺2009-9-27  ����16:06:53
 */
public class CalcDialog extends UIDialog implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	//ִ�а�ť
	 private nc.ui.pub.beans.UIButton UIButtonComm = null;
	 private nc.ui.pub.beans.UIButton UIButtonCanel = null;	
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

   //��ť
	private nc.ui.pub.beans.UIButton getUIButtonComm(){
		if(UIButtonComm==null){
			UIButtonComm = new nc.ui.pub.beans.UIButton();
			UIButtonComm.setName("UIButtonComm");
			UIButtonComm.setText("ȷ��");
			UIButtonComm.setBounds(70, 150, 71, 22);
		}
		return UIButtonComm;
	}
//	��ť
	private nc.ui.pub.beans.UIButton getUIButtonCanel(){
		if(UIButtonCanel==null)
			UIButtonCanel = new nc.ui.pub.beans.UIButton();
			UIButtonCanel.setName("UIButtonCanel");
			UIButtonCanel.setText("ȡ��");
			UIButtonCanel.setBounds(190, 150, 71, 22);
		return UIButtonCanel;
	}
	//��ʼ��
    private void initialize() {
            setName("ApproveDialog");
            this.setTitle("��ѯ����");
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
  
    //���Ӱ�ť�ȹ���
    private javax.swing.JPanel getUIDialogContentPane() {
    	if (ivjUIDialogContentPane == null) {
                ivjUIDialogContentPane = new javax.swing.JPanel();
                ivjUIDialogContentPane.setName("UIDialogContentPane");
                ivjUIDialogContentPane.setLayout(null);              
                getUIDialogContentPane().add(getUIButtonComm(),getUIButtonComm().getName());
                getUIDialogContentPane().add(getUIButtonCanel(),getUIButtonCanel().getName());
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
			UILabel1.setText("��ѯ���ڣ�");
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
        if (e.getSource() == getUIButtonComm()) {
           pk_period = getUIRefPane1Period().getRefPK();
           if(pk_period==null){
		    	JOptionPane.showMessageDialog(this, "�ڼ䲻��Ϊ��!", "����",0);
		        return; 
           }
           this.closeOK();
        }
        if (e.getSource() == getUIButtonCanel()) {
            this.closeOK();
            pk_period = null;
        }
    }
    

    private void handleException(java.lang.Throwable exception) {
        /* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
        System.out.println("--------- δ��׽�����쳣 ---------");
        exception.printStackTrace(System.out);
    }
}




