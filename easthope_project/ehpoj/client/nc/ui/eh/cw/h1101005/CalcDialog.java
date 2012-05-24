package nc.ui.eh.cw.h1101005;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import nc.ui.pub.beans.UIDialog;
/**
 * ˵�����տ����۷�Ʊ�ĺ���
 * ʱ�䣺2008-5-12 9:44:15
 */
public class CalcDialog extends UIDialog implements ActionListener {
	//ִ�а�ť
	 private nc.ui.pub.beans.UIButton UIButtonComm = null;
	 private nc.ui.pub.beans.UIButton UIButtonCanel = null;	
	 private javax.swing.JPanel ivjUIDialogContentPane = null;	 
	 private  nc.ui.pub.beans.UILabel UILabel1=null;
	 
	 private nc.ui.pub.beans.UITextField TextField3=null;
     
     public static String billno = null;
     
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
			UIButtonComm.setBounds(50, 70, 60, 25);
			
		}
		return UIButtonComm;
	}
//	��ť
	private nc.ui.pub.beans.UIButton getUIButtonCanel(){
		if(UIButtonCanel==null)
			UIButtonCanel = new nc.ui.pub.beans.UIButton();
			UIButtonCanel.setName("UIButtonCanel");
			UIButtonCanel.setText("ȡ��");
			UIButtonCanel.setBounds(160, 70, 60, 25);
		return UIButtonCanel;
	}
	//��ʼ��
    private void initialize() {
            setName("ApproveDialog");
            this.setTitle("����");
            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setSize(250, 180);
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
                getUIDialogContentPane().add(getUITextField3(),getUITextField3().getName());
                }
    	return ivjUIDialogContentPane;
    }
    
    private Component getUILabel1() {
		if(UILabel1==null)
		{
			UILabel1=new nc.ui.pub.beans.UILabel();
			UILabel1.setName("Label1");
			UILabel1.setText("��Ʊ�ţ�");
			UILabel1.setBounds(20, 25, 60, 20);
		}
		return UILabel1;
	}
   
    
  public nc.ui.pub.beans.UITextField getUITextField3(){
        if(TextField3==null)
        {
          try {
            TextField3=new nc.ui.pub.beans.UITextField();
            TextField3.setName("TextField3");
            TextField3.setBounds(80, 25, 150, 20); 
          } catch (java.lang.Throwable ivjExc) {
              
              handleException(ivjExc);
          }
        }
        return TextField3;
    } 
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == getUIButtonComm()) {
        	if(getUITextField3().getText()!=null && getUITextField3().getText().length()>0){
        		billno=getUITextField3().getText();
        	}
        }
        this.closeOK();
        if (e.getSource() == getUIButtonCanel()) {
            this.closeOK();
            billno = null;
        }
    }
    

    private void handleException(java.lang.Throwable exception) {
        /* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
        System.out.println("--------- δ��׽�����쳣 ---------");
        exception.printStackTrace(System.out);
    }
}




