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
 * 说明：单据项目管理生成
 * 作者：王兵
 * 2008-3-7 16:00:44
 */
public class ClientUI2 extends ToftPanel{
	
    nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();
    String sDate = ce.getDate().toString();
    
    private UILabel         lblVersion              = null;     //版本Label
    private UILabel         lblPeriodYear           = null;     //财务年度
    
    private UIButton        btnWriteOrder           = null;     //导出Order明细按钮
    
    private UIComboBox       ivjRefRefVersion        = null;     //版本
    private UIRefPane       ivjRefPeriodYear        = null;     //财务会计年度
    
    public String			pk_corp = null;		//Corp
    public String			pk_deptdoc = null;	//deptcode
    public String			deptcode = null;	//deptcode
    
    public UILabel          checkLabel = null;  //说明下面单选钮的作用
    
    public String nowdate = ClientEnvironment.getInstance().getDate().toString();
    
	public ClientUI2() {
		super();
		initialize();	
		iniVar();
	}
	
   @Override
public String getTitle() {
		// TODO Auto-generated method stub
		return "加权平均热值计算";
	}
	
	private void handleException(java.lang.Throwable exception) {
		/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
		System.out.println("--------- 未捕捉到的异常 ---------");
		exception.printStackTrace(System.out);
	}
	
	protected void initialize() {
		try {
	    this.setName("Jqqnet");
		this.setSize(500, 400);
		
		this.setTitleText("加权平均热值计算");
		this.setLayout(null);
        
        this.add(this.getLblVersion(),this.getLblVersion().getName());  //将版本Label加入panel
        
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
     * 功能: 实例化 会计年度文本
     * @author chenjian
     * 2007-3-10 下午05:29:09
     */
    private UILabel getLblPeriodYear(){
        if (lblPeriodYear == null){
            try {
                lblPeriodYear = new UILabel();
                lblPeriodYear.setName("LbPeriodYear");
                lblPeriodYear.setText("单据类型：");
                lblPeriodYear.setBounds(40,60, 100, 20);
                
                lblPeriodYear.setVisible(true);
            } catch (java.lang.Throwable e) {
                handleException(e);
            }
        } 
        return lblPeriodYear;      
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
                lblVersion.setText("表头或表体项目：");
                lblVersion.setBounds(40,90, 100, 20);
                
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
	 * 功能说明:版本参照 作者:honghai 日期:2007-3-13 修改:honghai
	 */
	private nc.ui.pub.beans.UIComboBox getRefVersion() {
		if (ivjRefRefVersion == null) {
			try {
				ivjRefRefVersion = new UIComboBox();
				ivjRefRefVersion.setName("refVersion");
			    ivjRefRefVersion.setBounds(130, 90, 200, 20);
				String[] dates = {"表头","表体"};
				ivjRefRefVersion.addItems(dates);
			} catch (java.lang.Throwable e) {
				handleException(e);
			}
		}
		return ivjRefRefVersion;
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
                btnWriteOrder.setText("单据项目生成");
                btnWriteOrder.setBounds(120, 160, 100, 20);
                btnWriteOrder.setVisible(true);
                btnWriteOrder.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                        	String billtype = ivjRefPeriodYear.getRefCode();                // 所选择的单据类型
                            String headflagstr = ivjRefRefVersion.getSelectdItemValue().toString(); // 所选择的表头或表体
                            String headflag = null;
                            if(billtype==null){
                         	   showErrorMessage("请选择单据类型!");
                         	   return;
                            }
                            if(headflagstr==null){
                         	   showErrorMessage("请选择表头或表体!");
                         	   return;
                            }else if(headflagstr.equals("表头")){
                            	headflag = "Y";
                            }else if(headflagstr.equals("表体")){
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
 * 功能: Main入口
 * @param arg
 * @author chenjian
 * 2007-3-10 下午06:15:04
 */
   public static void main(String[] arg) {
        ClientUI2 oneUI = new ClientUI2();
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
