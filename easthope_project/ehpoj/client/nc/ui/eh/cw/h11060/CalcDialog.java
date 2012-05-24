package nc.ui.eh.cw.h11060;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import nc.ui.eh.refpub.HXCubgysRefModel;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
/**
 * 说明: 成品核销
 * @author 王明
 * 2008年8月26日14:38:55
 */
public class CalcDialog extends UIDialog implements ActionListener {
	//执行按钮
	 private nc.ui.pub.beans.UIButton UIButtonComm = null;
	 private nc.ui.pub.beans.UIButton UIButtonCanel = null;	
	 private javax.swing.JPanel ivjUIDialogContentPane = null;	 
	 private  nc.ui.pub.beans.UILabel UILabel2=null;
	 private  UIRefPane UIRefStore = null;
	 

     public static String pk_cubasdoc = null;
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
            this.setTitle("客户查询");
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
                getUIDialogContentPane().add(getUILabel2(),getUILabel2().getName());
                getUIDialogContentPane().add(getUIRefPaneStore(),getUIRefPaneStore().getName());
                }
    	return ivjUIDialogContentPane;
    }
    
    private Component getUILabel2() {
		if(UILabel2==null)
		{
			UILabel2=new nc.ui.pub.beans.UILabel();
			UILabel2.setName("Label2");
			UILabel2.setText("客  商：");
			UILabel2.setBounds(30, 55, 150, 20);
		}
		return UILabel2;
	}
    
    private nc.ui.pub.beans.UIRefPane getUIRefPaneStore(){
    	if(UIRefStore==null)
    	{
          try {
        	UIRefStore = new UIRefPane();
        	HXCubgysRefModel RefStroe = new HXCubgysRefModel();
        	UIRefStore.setName("UIRefStore");
        	UIRefStore.setBounds(100, 55, 150, 20);
        	UIRefStore.setRefModel(RefStroe);
        	
          } catch (java.lang.Throwable ivjExc) {
              
              handleException(ivjExc);
          }
    	}
    	return UIRefStore;
    }
      
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == getUIButtonComm()) {
        	pk_cubasdoc = getUIRefPaneStore().getRefPK();
           if(pk_cubasdoc==null){
		    	JOptionPane.showMessageDialog(this, "客商不能为空!", "警告",0);
		        return; 
           }
           this.closeOK();
        }
        if (e.getSource() == getUIButtonCanel()) {
            this.closeOK();
            pk_cubasdoc = null;
        }
    }
    

    private void handleException(java.lang.Throwable exception) {
        /* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
        System.out.println("--------- 未捕捉到的异常 ---------");
        exception.printStackTrace(System.out);
    }
}




