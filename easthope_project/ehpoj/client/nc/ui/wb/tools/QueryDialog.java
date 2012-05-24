package nc.ui.wb.tools;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import nc.ui.pub.beans.UIDialog;

public class QueryDialog extends UIDialog implements ActionListener {
	//执行按钮
	 private nc.ui.pub.beans.UIButton UIButtonComm = null;
	 private javax.swing.JPanel ivjUIDialogContentPane = null; 
	 private  String value=null;
	 
	 
	 private nc.ui.pub.beans.UITextField chooseField=null;
	 
	 @SuppressWarnings("deprecation")
	public QueryDialog()
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
			UIButtonComm.setBounds(110, 90, 85, 25);
			
		}
		return UIButtonComm;
	}
	//初始话
    private void initialize() {
            setName("QueryDialog");
            this.setTitle("动态密码输入框");
            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setSize(300, 160);    
            setContentPane(getUIDialogContentPane());
            getUIButtonComm().addActionListener(this);
    }
  
    //增加按钮等工作
    private javax.swing.JPanel getUIDialogContentPane() {
    	if (ivjUIDialogContentPane == null) {
            
                ivjUIDialogContentPane = new javax.swing.JPanel();
                ivjUIDialogContentPane.setName("UIDialogContentPane");
                ivjUIDialogContentPane.setLayout(null);              
                getUIDialogContentPane().add(getUIButtonComm(),getUIButtonComm().getName());
                getUIDialogContentPane().add(getUITextField(),getUITextField().getName());
                }
    	return ivjUIDialogContentPane;
    }
    
    private Component getUITextField(){
    	if(chooseField==null)
    	{
    		chooseField=new nc.ui.pub.beans.UITextField();
    		chooseField.setName("chooseField");
    		chooseField.setBounds(80, 40, 160, 100);   		
    	}
    	return chooseField;
    }

      
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == getUIButtonComm()) {
        	value=chooseField.getText();
        	this.closeOK();
        }
    }


	public String getValue() {
		return value;
	}
    
}




