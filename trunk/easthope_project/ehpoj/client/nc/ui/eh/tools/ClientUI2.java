/*
 * Created on 2006-6-15
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package nc.ui.eh.tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.ui.eh.refpub.BilTypeRefModel;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIRefPane;


/**
 * ˵����������Ŀ��������
 * ���ߣ�����
 * 2008-3-7 16:00:44
 */
public class ClientUI2 extends ToftPanel{
	
    nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();
    String sDate = ce.getDate().toString();
    
    private UILabel         lblVersion              = null;     //�汾Label
    private UILabel         lblPeriodYear           = null;     //�������
    
    private UIButton        btnWriteOrder           = null;     //����Order��ϸ��ť
    
    private UIComboBox       ivjRefRefVersion        = null;     //�汾
    private UIRefPane       ivjRefPeriodYear        = null;     //���������
    
    public String			pk_corp = null;		//Corp
    public String			pk_deptdoc = null;	//deptcode
    public String			deptcode = null;	//deptcode
    
    public UILabel          checkLabel = null;  //˵�����浥ѡť������
    
    public String nowdate = ClientEnvironment.getInstance().getDate().toString();
    
	public ClientUI2() {
		super();
		initialize();	
		iniVar();
	}
	
   @Override
public String getTitle() {
		// TODO Auto-generated method stub
		return "��Ȩƽ����ֵ����";
	}
	
	private void handleException(java.lang.Throwable exception) {
		/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
		System.out.println("--------- δ��׽�����쳣 ---------");
		exception.printStackTrace(System.out);
	}
	
	protected void initialize() {
		try {
	    this.setName("Jqqnet");
		this.setSize(500, 400);
		
		this.setTitleText("��Ȩƽ����ֵ����");
		this.setLayout(null);
        
        this.add(this.getLblVersion(),this.getLblVersion().getName());  //���汾Label����panel
        
        this.add(this.getLblPeriodYear(),this.getLblPeriodYear().getName()); 
        this.add(this.getRefPeriodYear(),this.getRefPeriodYear().getName());  
        this.add(this.getRefVersion(),this.getRefVersion().getName());  
        this.add(this.getBtnWriteOrder(),this.getBtnWriteOrder().getName());
      
		} catch (java.lang.Throwable e) {
			handleException(e);
		}
	}
	
	private void iniVar(){
//		pk_corp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
	}
   
   
  
    /**
     * ����: ʵ���� �������ı�
     * @author chenjian
     * 2007-3-10 ����05:29:09
     */
    private UILabel getLblPeriodYear(){
        if (lblPeriodYear == null){
            try {
                lblPeriodYear = new UILabel();
                lblPeriodYear.setName("LbPeriodYear");
                lblPeriodYear.setText("�������ͣ�");
                lblPeriodYear.setBounds(40,60, 100, 20);
                
                lblPeriodYear.setVisible(true);
            } catch (java.lang.Throwable e) {
                handleException(e);
            }
        } 
        return lblPeriodYear;      
    }
    
    
    /**
     * ����: ʵ���� �汾Label
     * @author chenjian
     * 2007-3-10 ����05:29:09
     */
    private UILabel getLblVersion(){
        if (lblVersion == null){
            try {
                lblVersion = new UILabel();
                lblVersion.setName("lblVersion");
                lblVersion.setText("��ͷ�������Ŀ��");
                lblVersion.setBounds(40,90, 100, 20);
                
                lblVersion.setVisible(true);
            } catch (java.lang.Throwable e) {
                handleException(e);
            }
        } 
        return lblVersion;      
    }
    
    
     
    
//    /**
//     * ����: ʵ���� �汾Text
//     * @author chenjian
//     * 2007-3-10 ����05:39:51
//     */
//    protected UITextField getTxtDept(){
//        if (txtDept == null){
//            try {
//                txtDept = new UITextField();
//                txtDept.setName("txtDept");
//                
//                //��ȡ��¼�˲�����Ϣ:���ű���
//                //˵��:�����ַ�������,0�ǲ���PK,1�ǲ��ű���,2�ǲ�������
//                String deptCode = PubBgBO_Client.getDeptInfo(ce.getUser().getPrimaryKey())[1];
//                txtDept.setText(deptCode);
//                txtDept.setBounds(110, 30, 200, 20);
//                txtDept.setVisible(true);
//                txtDept.setEnabled(false);
//            } catch (java.lang.Throwable e) {
//                handleException(e);
//            }
//        } 
//        return txtDept;      
//    }
    
   
	
//	 /**
//	 * ����: ʵ���� ����Label
//	 * 
//	 * @author honghai 2007-4-3 ����02:29:09
//	 * @return 
//	 */
//	private UILabel getTextTip() {
//		if (tfTip == null) {
//			try {
//				tfTip = new UILabel();
//				tfTip.setName("tfTip");
//				tfTip.setText("**ϵͳ��ʾ**���е�����������ص�C:\\bgtreport\\ �ļ�����");
//				tfTip.setBounds(30, 300, 310, 20);
//				tfTip.setVisible(true);
//			} catch (java.lang.Throwable e) {
//				handleException(e);
//			}
//		}
//		return tfTip;
//	}
	
    private nc.ui.pub.beans.UIRefPane getRefPeriodYear() {
		if (ivjRefPeriodYear == null) {
			try {
				ivjRefPeriodYear = new UIRefPane();
			    BilTypeRefModel billtype = new BilTypeRefModel();
				ivjRefPeriodYear.setName("refVersion");
				ivjRefPeriodYear.setRefModel(billtype);
				ivjRefPeriodYear.setBounds(130, 60, 200, 20);
				ivjRefPeriodYear.setButtonFireEvent(true);
			} catch (java.lang.Throwable e) {
				handleException(e);
			}
		}
		return ivjRefPeriodYear;
	}

	
	
	 
	/**
	 * ����˵��:�汾���� ����:honghai ����:2007-3-13 �޸�:honghai
	 */
	private nc.ui.pub.beans.UIComboBox getRefVersion() {
		if (ivjRefRefVersion == null) {
			try {
				ivjRefRefVersion = new UIComboBox();
				ivjRefRefVersion.setName("refVersion");
			    ivjRefRefVersion.setBounds(130, 90, 200, 20);
				String[] dates = {"��ͷ","����"};
				ivjRefRefVersion.addItems(dates);
			} catch (java.lang.Throwable e) {
				handleException(e);
			}
		}
		return ivjRefRefVersion;
	}

	

	
    /**
     * ����: ����Order��ϸ��ť
     * @author chenjian
     * 2007-4-9 ����10:13:43
     */
    private UIButton getBtnWriteOrder() {
        if (btnWriteOrder == null) {
            try {
                btnWriteOrder = new UIButton();
                btnWriteOrder.setName("btnWriteOrder");
                btnWriteOrder.setText("������Ŀ����");
                btnWriteOrder.setBounds(120, 160, 100, 20);
                btnWriteOrder.setVisible(true);
                btnWriteOrder.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                        	String billtype = ivjRefPeriodYear.getRefCode();                // ��ѡ��ĵ�������
                            String headflagstr = ivjRefRefVersion.getSelectdItemValue().toString(); // ��ѡ��ı�ͷ�����
                            String headflag = null;
                            if(billtype==null){
                         	   showErrorMessage("��ѡ�񵥾�����!");
                         	   return;
                            }
                            if(headflagstr==null){
                         	   showErrorMessage("��ѡ���ͷ�����!");
                         	   return;
                            }else if(headflagstr.equals("��ͷ")){
                            	headflag = "Y";
                            }else if(headflagstr.equals("����")){
                            	headflag = "N";
                            }
                           PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
                           String info = pubItf.makeDefitem(billtype, headflag);
                           if(info!=null&&info.length()>0){
                        	   showWarningMessage(info);
                           }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                });
            } catch (java.lang.Throwable e) {
                handleException(e);
            }
        }
        return btnWriteOrder;
    }
    
   
    
 /**
 * ����: Main���
 * @param arg
 * @author chenjian
 * 2007-3-10 ����06:15:04
 */
   public static void main(String[] arg) {
        ClientUI2 oneUI = new ClientUI2();
        javax.swing.JFrame frame = new javax.swing.JFrame();
        frame.setContentPane(oneUI);
        frame.setSize(oneUI.getSize());
        frame.setTitle("��Ȩƽ����ֵ����");

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
