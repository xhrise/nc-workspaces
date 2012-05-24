/*
 * $Id: CheckOutNumDialog.java,v 1.3 2008/01/05 01:33:59 maoyl Exp $
 * ���ܣ�TODO
 * ������2007-12-26 ���� BY MickeyMao
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
 * @������  : CheckOutNumDialog
 * @���� : TODO
 * @�汾 : v1.0
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
	 
	 private ClientEnvironment ce=ClientEnvironment.getInstance();// ϵͳ��������
	 
	 public String icgenerhid=null;
	 
	 public UIButton getUIButtonComm(){
		 if(accept==null){
			 accept=new UIButton();
			 accept.setText("ȷ��");
		 }
		 return accept;
	 }
     
	 public UIButton getUIButtonCanel(){
	     if(cancel==null){
             cancel=new UIButton();
             cancel.setText("ȡ��");
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
		// TODO �Զ����ɹ��캯�����
	}

	/**
	 * @param owner
	 */
	public TempletDialog(Frame owner) {
		super(owner);
		// TODO �Զ����ɹ��캯�����
	}

	/**
	 * @param parent
	 * @param title
	 */
	public TempletDialog(Container parent, String title) {
		super(parent, title);
		// TODO �Զ����ɹ��캯�����
	}

	/**
	 * @param owner
	 * @param title
	 */
	public TempletDialog(Frame owner, String title) {
		super(owner, title);
		// TODO �Զ����ɹ��캯�����
	}
	
	public void initialize(){
		  setSize(new Dimension(800, 600));
		  setTitle("��ʱ�ۿ۱�");
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
//		// TODO �Զ����ɷ������
//		if(arg0.getSource().equals(getButton())){
//			try {
//				acction();
//			} catch (BusinessException e) {
//				// TODO �Զ����� catch ��
//				Debug.error(e.getMessage());
//			}
//		}
//	}

	public void setData(){
//	    getoutCardPanel().setBillValueVO(getVOs());
//        getoutCardPanel().setBillData(getBillDate());
        
       
	}




	public void bodyRowChange(BillEditEvent e) {
		// TODO �Զ����ɷ������
		
	}


    
    /**
     * ȡ��devVOs��ֵ
     * @return devVOs - DevicenumberVO[]
     */
    public AggregatedValueObject getVOs() {
        return vos;
    }
    /**
     * ����devVOs��ֵ
     * @param devVOs Ҫ���õ� devVOs - DevicenumberVO[]
     */
    public void setVOs(AggregatedValueObject vos) {
        this.vos = vos;
//        getoutCardPanel().setBillValueVO(getVOs());
//        getoutCardPanel().getBillModel().execLoadFormula();
    }
    
    /**
     * ����devVOs��ֵ
     * @param devVOs Ҫ���õ� devVOs - DevicenumberVO[]
     */
    public void setBillDate(Vector   billTempletData) {
        this.billTempletData = billTempletData;
    }
    /**
     * ����devVOs��ֵ
     * @param devVOs Ҫ���õ� devVOs - DevicenumberVO[]
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
        // TODO �Զ����ɷ������
        getoutCardPanel().setBillValueVO(getVOs());
        getoutCardPanel().getBillModel().execLoadFormula();
        return super.showModal();
    }
    
    
 
}
