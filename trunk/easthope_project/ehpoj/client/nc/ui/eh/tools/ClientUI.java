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
 * 说明：折扣期间计算
 * 作者：王兵
 * 2008-3-7 16:00:44
 */
public class ClientUI extends ToftPanel{
	
    nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();
    String sDate = ce.getDate().toString();
    
    private UILabel         lblPeriodYear           = null;     //财务年度
    private UILabel         lblDistype           = null;        //折扣类型
    private UILabel         lblVersion              = null;     //月份
    
    private UIButton        btnWriteOrder           = null;     //导出Order明细按钮
    
    private UIRefPane       ivjRefDiscounttype        = null;     //折扣类型
    private UIRefPane       ivjRefPeriodYear          = null;     //年度
    private UIRefPane       ivjRefDiscountValue          = null;  //折扣月份
    private UIComboBox      comMonth                  = null;     //月份
    
    public String			pk_corp = null;		//Corp
    public String			pk_deptdoc = null;	//deptcode
    public String			deptcode = null;	//deptcode
    
    public UILabel          checkLabel = null;  //说明下面单选钮的作用
    
    public String nowdate = ClientEnvironment.getInstance().getDate().toString();
    
	public ClientUI() {
		super();
		initialize();	
		iniVar();
	}
	
   @Override
public String getTitle() {
		// TODO Auto-generated method stub
		return "期间折扣计算";
	}
	
	private void handleException(java.lang.Throwable exception) {
		/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
		System.out.println("--------- 未捕捉到的异常 ---------");
		exception.printStackTrace(System.out);
	}
	
	protected void initialize() {
		try {
	    this.setName("Jqqnet");
		this.setSize(500, 300);
		
		this.setTitleText("期间折扣计算");
		this.setLayout(null);
		this.add(this.getLblPeriodYear(),this.getLblPeriodYear().getName());   //年度
		this.add(this.getLblDisType(),this.getLblDisType().getName());         //折扣类型
        this.add(this.getLblVersion(),this.getLblVersion().getName());         //将版本Label加入panel
        
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
     * 功能: 实例化 会计年度文本
     * @author chenjian
     * 2007-3-10 下午05:29:09
     */
    private UILabel getLblPeriodYear(){
        if (lblPeriodYear == null){
            try {
                lblPeriodYear = new UILabel();
                lblPeriodYear.setName("LbPeriodYear");
                lblPeriodYear.setText("年   度：");
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
            	lblDistype.setText("折扣类型：");
            	lblDistype.setBounds(40,90, 100, 20);
            	lblDistype.setVisible(true);
            } catch (java.lang.Throwable e) {
                handleException(e);
            }
        } 
        return lblDistype;      
    }
    /**
     * 功能: 实例化 版本Label
     * @author chenjian
     * 2007-3-10 下午05:29:09
     */
    private UILabel getLblVersion(){
        if (lblVersion == null){
            try {
                lblVersion = new UILabel();
                lblVersion.setName("lblVersion");
                lblVersion.setText("折扣月份：");
                lblVersion.setBounds(40,120, 100, 20);
                
                lblVersion.setVisible(true);
            } catch (java.lang.Throwable e) {
                handleException(e);
            }
        } 
        return lblVersion;      
    }
    
    
     
    
//    /**
//     * 功能: 实例化 版本Text
//     * @author chenjian
//     * 2007-3-10 下午05:39:51
//     */
//    protected UITextField getTxtDept(){
//        if (txtDept == null){
//            try {
//                txtDept = new UITextField();
//                txtDept.setName("txtDept");
//                
//                //获取登录人部门信息:部门编码
//                //说明:返回字符串数组,0是部门PK,1是部门编码,2是部门名称
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
//	 * 功能: 实例化 课室Label
//	 * 
//	 * @author honghai 2007-4-3 下午02:29:09
//	 * @return 
//	 */
//	private UILabel getTextTip() {
//		if (tfTip == null) {
//			try {
//				tfTip = new UILabel();
//				tfTip.setName("tfTip");
//				tfTip.setText("**系统提示**所有导出报表均下载到C:\\bgtreport\\ 文件夹下");
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
	 * 功能说明:版本参照 作者:honghai 日期:2007-3-13 修改:honghai
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
//				if(distype.equals("月度折扣")){
//					dates = new String[]{"01","02","03","04","05","06","07","08","09","10","11","12"};
//				}
//				if(distype.equals("季度折扣")){
//					dates = new String[]{"01","02","03","04"};
//				}
//				if(distype.equals("年度折扣")){
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
     * 功能: 导出Order明细按钮
     * @author chenjian
     * 2007-4-9 上午10:13:43
     */
    private UIButton getBtnWriteOrder() {
        if (btnWriteOrder == null) {
            try {
                btnWriteOrder = new UIButton();
                btnWriteOrder.setName("btnWriteOrder");
                btnWriteOrder.setText("期间折扣计算");
                btnWriteOrder.setBounds(120, 160, 100, 20);
                btnWriteOrder.setVisible(true);
                btnWriteOrder.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                           String pk_discounttype = ivjRefDiscounttype.getRefPK();        //折扣类型
                           if(pk_discounttype==null){
                        	   showErrorMessage("请选择折扣类型!");
                        	   return;
                           }
                           String pk_year = ivjRefPeriodYear.getRefPK();               //年度
                           if(pk_year==null){
                        	   showErrorMessage("请选择年度!");
                        	   return;
                           }
                           int year = Integer.parseInt(pk_year);
                           Object selectMonth = comMonth.getSelectdItemValue();          //月份
                           if(selectMonth==null){
                        	   showErrorMessage("请选择月份!");
                        	   return;
                           }
                           int value = Integer.parseInt(selectMonth.toString());
                           
                           PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
                           pubItf.insertDiscount(pk_discounttype, year, value);
                           showWarningMessage("期间折扣计算成功!");
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
 * 功能: Main入口
 * @param arg
 * @author chenjian
 * 2007-3-10 下午06:15:04
 */
   public static void main(String[] arg) {
        ClientUI oneUI = new ClientUI();
        javax.swing.JFrame frame = new javax.swing.JFrame();
        frame.setContentPane(oneUI);
        frame.setSize(oneUI.getSize());
        frame.setTitle("加权平均热值计算");

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
