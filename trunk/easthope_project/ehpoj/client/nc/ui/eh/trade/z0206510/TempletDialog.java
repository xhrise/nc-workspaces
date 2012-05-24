/*
 * $Id: CheckOutNumDialog.java,v 1.3 2008/01/05 01:33:59 maoyl Exp $
 * 功能：TODO
 * 履历：2007-12-26 创建 BY MickeyMao
 */
package nc.ui.eh.trade.z0206510;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillListPanel;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.trade.z0206510.LadingbillBVO;
import nc.vo.eh.trade.z0206510.LadingbillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.to.pub.BillVO;

/**
 * @author  MickeyMao 2007-12-26
 * @类型名  : CheckOutNumDialog
 * @功能 : TODO
 * @版本 : v1.0
 */
public class TempletDialog extends UIDialog implements   ActionListener,BillEditListener{

	
	 private BillListPanel billListPanel =null;
     
	 private BillCardPanel billCardPanel =null;
	    
	 private UIButton  accept=null;
	 private UIButton  cancel=null;
     
     public AggregatedValueObject vos=new BillVO();
    
	 
	 private UIPanel   btnPanel=null;
	 
	 private UIPanel   mainPanel=null;
     
	 private Vector   billTempletData=new Vector();
	 
	 private ClientEnvironment ce=ClientEnvironment.getInstance();// 系统环境变量
	 
	 public String icgenerhid=null;
	 
	 public UIButton getUIButtonComm(){
		 if(accept==null){
			 accept=new UIButton();
			 accept.setText("确定");
		 }
		 return accept;
	 }
     
	 public UIButton getUIButtonCanel(){
	     if(cancel==null){
             cancel=new UIButton();
             cancel.setText("取消");
	     }
	     return cancel;
	 }
	 
	 public UIPanel getBtnPanel(){
		 if(btnPanel==null){
			 btnPanel=new UIPanel();
			 btnPanel.setName("btnPanel");
			 btnPanel.setLayout(new FlowLayout());
			 btnPanel.add(getUIButtonComm(), "Center");
			 btnPanel.add(getUIButtonCanel(), "Center");
		 }
		 return btnPanel;
	 }
	 

     
    public BillCardPanel getoutCardPanel(){
        if(billCardPanel==null)
          {
            billCardPanel=new BillCardPanel();
            billCardPanel.setName("billCardPanel");
            billCardPanel.setPreferredSize(new java.awt.Dimension(774, 419));
            billCardPanel.loadTemplet("ZA13", null,ce.getUser().getPrimaryKey(),
                      ce.getCorporation().getUnitcode());
            billCardPanel.setEnabled(true);
          }
          return billCardPanel;
     }
	 
	 public UIPanel getMainPanel(){
		    if(mainPanel==null){
		    	mainPanel=new UIPanel();
		    	mainPanel.setName("mainPanel");
		    	mainPanel.setLayout(new BorderLayout());
		    	mainPanel.add(getoutCardPanel(), "Center");
		    	mainPanel.add(getBtnPanel(), "South");
		    }
		    return mainPanel;
	 }
	 
	 
 

public TempletDialog(String icgenerhid){
	      super();
	      this.icgenerhid=icgenerhid;
	      initialize();
   }
	/**
	 * @param parent
	 */
	public TempletDialog(Container parent) {
		super(parent);
		// TODO 自动生成构造函数存根
	}

	/**
	 * @param owner
	 */
	public TempletDialog(Frame owner) {
		super(owner);
		// TODO 自动生成构造函数存根
	}

	/**
	 * @param parent
	 * @param title
	 */
	public TempletDialog(Container parent, String title) {
		super(parent, title);
		// TODO 自动生成构造函数存根
	}

	/**
	 * @param owner
	 * @param title
	 */
	public TempletDialog(Frame owner, String title) {
		super(owner, title);
		// TODO 自动生成构造函数存根
	}
	
	public void initialize(){
		  setSize(new Dimension(800, 600));
		  setTitle("临时折扣表");
		getContentPane().add(getMainPanel());
		addListenerEvent();
		setData();
	}
	public void addListenerEvent() {
//		getButton().addActionListener(this);
//		getoutListPanel().addBodyEditListener(this);
//		getoutCardPanel().addBodyEditListener2(this);

        getUIButtonComm().addActionListener(this);
        getUIButtonCanel().addActionListener(this);
	}

//	public void actionPerformed(ActionEvent arg0) {
//		// TODO 自动生成方法存根
//		if(arg0.getSource().equals(getButton())){
//			try {
//				acction();
//			} catch (BusinessException e) {
//				// TODO 自动生成 catch 块
//				Debug.error(e.getMessage());
//			}
//		}
//	}

	public void setData(){
//	    getoutCardPanel().setBillValueVO(getVOs());
//        getoutCardPanel().setBillData(getBillDate());
        
       
	}




	public void bodyRowChange(BillEditEvent e) {
		// TODO 自动生成方法存根
		
	}


    
    /**
     * 取得devVOs的值
     * @return devVOs - DevicenumberVO[]
     */
    public AggregatedValueObject getVOs() {
        return vos;
    }
    /**
     * 设置devVOs的值
     * @param devVOs 要设置的 devVOs - DevicenumberVO[]
     */
    public void setVOs(AggregatedValueObject vos) {
        this.vos = vos;
//        getoutCardPanel().setBillValueVO(getVOs());
//        getoutCardPanel().getBillModel().execLoadFormula();
    }
    
    /**
     * 设置devVOs的值
     * @param devVOs 要设置的 devVOs - DevicenumberVO[]
     */
    public void setBillDate(Vector   billTempletData) {
        this.billTempletData = billTempletData;
    }
    /**
     * 设置devVOs的值
     * @param devVOs 要设置的 devVOs - DevicenumberVO[]
     */
    public Vector getBillDate() {
        return billTempletData;
    }



    public void afterEdit(BillEditEvent arg0) {
        // TODO Auto-generated method stub
        
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == getUIButtonComm()) {
            AggregatedValueObject vo=getoutCardPanel().getBillValueVO(PubBillVO.class.getName(),
                    LadingbillVO.class.getName(),
                    LadingbillBVO.class.getName());
            setVOs(vo);
            this.closeOK();
        }
        if (e.getSource() == getUIButtonCanel()) {
            this.closeOK();
        }
    }
    
    @Override
    public int showModal() {
        // TODO 自动生成方法存根
        getoutCardPanel().setBillValueVO(getVOs());
        getoutCardPanel().getBillModel().execLoadFormula();
        return super.showModal();
    }
    
    
 
}
