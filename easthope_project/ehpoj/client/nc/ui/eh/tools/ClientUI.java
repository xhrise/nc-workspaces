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
import nc.ui.eh.refpub.DiscountTypeRefModel;
import nc.ui.eh.refpub.PeriodYearRefModel;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIRefPane;


/**
 * ˵�����ۿ��ڼ����
 * ���ߣ�����
 * 2008-3-7 16:00:44
 */
public class ClientUI extends ToftPanel{
	
    nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();
    String sDate = ce.getDate().toString();
    
    private UILabel         lblPeriodYear           = null;     //�������
    private UILabel         lblDistype           = null;        //�ۿ�����
    private UILabel         lblVersion              = null;     //�·�
    
    private UIButton        btnWriteOrder           = null;     //����Order��ϸ��ť
    
    private UIRefPane       ivjRefDiscounttype        = null;     //�ۿ�����
    private UIRefPane       ivjRefPeriodYear          = null;     //���
    private UIRefPane       ivjRefDiscountValue          = null;  //�ۿ��·�
    private UIComboBox      comMonth                  = null;     //�·�
    
    public String			pk_corp = null;		//Corp
    public String			pk_deptdoc = null;	//deptcode
    public String			deptcode = null;	//deptcode
    
    public UILabel          checkLabel = null;  //˵�����浥ѡť������
    
    public String nowdate = ClientEnvironment.getInstance().getDate().toString();
    
	public ClientUI() {
		super();
		initialize();	
		iniVar();
	}
	
   @Override
public String getTitle() {
		// TODO Auto-generated method stub
		return "�ڼ��ۿۼ���";
	}
	
	private void handleException(java.lang.Throwable exception) {
		/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
		System.out.println("--------- δ��׽�����쳣 ---------");
		exception.printStackTrace(System.out);
	}
	
	protected void initialize() {
		try {
	    this.setName("Jqqnet");
		this.setSize(500, 300);
		
		this.setTitleText("�ڼ��ۿۼ���");
		this.setLayout(null);
		this.add(this.getLblPeriodYear(),this.getLblPeriodYear().getName());   //���
		this.add(this.getLblDisType(),this.getLblDisType().getName());         //�ۿ�����
        this.add(this.getLblVersion(),this.getLblVersion().getName());         //���汾Label����panel
        
        this.add(this.getRefPeriodYear(),this.getRefPeriodYear().getName());  
        this.add(this.getRefDistype(),this.getRefDistype().getName());  
        this.add(this.getComMonth(),this.getComMonth().getName());  
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
                lblPeriodYear.setText("��   �ȣ�");
                lblPeriodYear.setBounds(40,60, 100, 20);
                
                lblPeriodYear.setVisible(true);
            } catch (java.lang.Throwable e) {
                handleException(e);
            }
        } 
        return lblPeriodYear;      
    }
    
    private UILabel getLblDisType(){
        if (lblDistype == null){
            try {
            	lblDistype = new UILabel();
            	lblDistype.setName("lblDistype");
            	lblDistype.setText("�ۿ����ͣ�");
            	lblDistype.setBounds(40,90, 100, 20);
            	lblDistype.setVisible(true);
            } catch (java.lang.Throwable e) {
                handleException(e);
            }
        } 
        return lblDistype;      
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
                lblVersion.setText("�ۿ��·ݣ�");
                lblVersion.setBounds(40,120, 100, 20);
                
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
				String nowyear = nowdate.substring(0, 4);
				ivjRefPeriodYear = new UIRefPane();
				PeriodYearRefModel perYear = new PeriodYearRefModel();
				ivjRefPeriodYear.setRefModel(perYear);
				ivjRefPeriodYear.setName("refVersion");
				ivjRefPeriodYear.setPK(nowyear);
				ivjRefPeriodYear.setBounds(110, 60, 200, 20);
			} catch (java.lang.Throwable e) {
				handleException(e);
			}
		}
		return ivjRefPeriodYear;
	}

	
	
    private nc.ui.pub.beans.UIRefPane getRefDistype() {
		if (ivjRefDiscounttype == null) {
			try {
				ivjRefDiscounttype = new UIRefPane();
				DiscountTypeRefModel distype = new DiscountTypeRefModel();
				ivjRefDiscounttype.setRefModel(distype);
				ivjRefDiscounttype.setName("ivjRefDiscounttype");
//				ivjRefDiscounttype.addValueChangedListener(new ValueChangedListener())
				ivjRefDiscounttype.setBounds(110, 90, 200, 20);
			} catch (java.lang.Throwable e) {
				handleException(e);
			}
		}
		return ivjRefDiscounttype;
	}
    
    private nc.ui.pub.beans.UIRefPane getRefDiscountValue() {
		if (ivjRefDiscountValue == null) {
			try {
				ivjRefDiscountValue = new UIRefPane();
//				PeriodValueRefModel discountValue = new PeriodValueRefModel();
//				ivjRefDiscounttype.setRefModel(discountValue);
				ivjRefDiscounttype.setName("ivjRefDiscounttype");
				ivjRefDiscounttype.setBounds(110, 90, 200, 20);
			} catch (java.lang.Throwable e) {
				handleException(e);
			}
		}
		return ivjRefDiscounttype;
	}

	/**
	 * ����˵��:�汾���� ����:honghai ����:2007-3-13 �޸�:honghai
	 */
	private nc.ui.pub.beans.UIComboBox getComMonth() {
		if (comMonth == null) {
			try {
				comMonth = new UIComboBox();
				comMonth.setName("refVersion");
				comMonth.setBounds(110, 120, 200, 20);
				String distype = ivjRefDiscounttype.getRefName();
				String[] dates = null;
				dates = new String[]{"01","02","03","04","05","06","07","08","09","10","11","12"};
//				if(distype.equals("�¶��ۿ�")){
//					dates = new String[]{"01","02","03","04","05","06","07","08","09","10","11","12"};
//				}
//				if(distype.equals("�����ۿ�")){
//					dates = new String[]{"01","02","03","04"};
//				}
//				if(distype.equals("����ۿ�")){
//					dates = new String[]{ivjRefPeriodYear.getRefName()};
//				}
//				
				comMonth.addItems(dates);
				comMonth.setSelectedItem(nowdate.substring(5, 7));
			} catch (java.lang.Throwable e) {
				handleException(e);
			}
		}
		return comMonth;
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
                btnWriteOrder.setText("�ڼ��ۿۼ���");
                btnWriteOrder.setBounds(120, 160, 100, 20);
                btnWriteOrder.setVisible(true);
                btnWriteOrder.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                           String pk_discounttype = ivjRefDiscounttype.getRefPK();        //�ۿ�����
                           if(pk_discounttype==null){
                        	   showErrorMessage("��ѡ���ۿ�����!");
                        	   return;
                           }
                           String pk_year = ivjRefPeriodYear.getRefPK();               //���
                           if(pk_year==null){
                        	   showErrorMessage("��ѡ�����!");
                        	   return;
                           }
                           int year = Integer.parseInt(pk_year);
                           Object selectMonth = comMonth.getSelectdItemValue();          //�·�
                           if(selectMonth==null){
                        	   showErrorMessage("��ѡ���·�!");
                        	   return;
                           }
                           int value = Integer.parseInt(selectMonth.toString());
                           
                           PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
                           pubItf.insertDiscount(pk_discounttype, year, value);
                           showWarningMessage("�ڼ��ۿۼ���ɹ�!");
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
        ClientUI oneUI = new ClientUI();
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
