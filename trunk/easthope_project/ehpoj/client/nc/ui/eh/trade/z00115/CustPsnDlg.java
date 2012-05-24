package nc.ui.eh.trade.z00115;

import java.awt.Container;
import javax.swing.JButton;
import javax.swing.JPanel;

import nc.ui.eh.pub.PubTools;
import nc.ui.eh.refpub.CustPsndocRefModel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillItem;
import nc.ui.scm.goldtax.Msg;
import nc.vo.pub.ValidationException;
import nc.vo.pub.bill.BillTempletVO;

/**
 * 传金税公共对话框，通常的使用方法如下：
 */
@SuppressWarnings("restriction")
public class CustPsnDlg extends UIDialog implements 
	BillEditListener {
	
	private static final long serialVersionUID = 1L;

	private JPanel ivjInternalDialogContentPane = null;
	
    private UIPanel ivjUIPanelNorth = null;
    private UIPanel ivjUIPanelEast = null;
    private UIPanel ivjUIPanelWest = null;
    private UIPanel ivjUIPanelButton = null;
    private BillCardPanel ivjBillCardPanel = null;
    
    private UIButton genBtn = null;
	private UIButton cancelBtn = null;
	
	private String[] pks = null;
	
	

	public String[] getPks() {
		return pks;
	}

	public void setPks(String[] pks) {
		this.pks = pks;
	}

	/**
	 * @param owner 父面板
	 */
	public CustPsnDlg(Container parent) {
		super(parent);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setTitle("客商-营销代表批量修改");
		this.setContentPane(getInternalDialogContentPane());
	}

	public BillCardPanel getBillCardPanel(){
		if(ivjBillCardPanel == null){
			ivjBillCardPanel = new BillCardPanel();
			ivjBillCardPanel.setName("BillCardPanel");
			BillTempletVO vo = ivjBillCardPanel.getTempletData("ZB49",null,null,null,null);
			BillData billDataVo = new BillData(vo);
			ivjBillCardPanel.setBillData(billDataVo);
			BillItem areaItem = ivjBillCardPanel.getHeadItem("oldpsnpk");
			UIRefPane areaRef = (UIRefPane)areaItem.getComponent();
			areaRef.setMultiSelectedEnabled(true);
			ivjBillCardPanel.addEditListener(this);
		}
		return ivjBillCardPanel;
	}
	 
	/**
     * 返回 InternalDialogContentPane 特性值.
     * @return javax.swing.JPanel
     */
    private javax.swing.JPanel getInternalDialogContentPane() {
        if (ivjInternalDialogContentPane == null) {
            try {
                ivjInternalDialogContentPane = new javax.swing.JPanel();
                ivjInternalDialogContentPane.setName("InternalDialogContentPane");
                ivjInternalDialogContentPane.setLayout(new java.awt.BorderLayout());
                getInternalDialogContentPane().add(getUIPanelNorth(), "North");
                getInternalDialogContentPane().add(getUIPanelWest(), "West");
                getInternalDialogContentPane().add(getUIPanelEast(), "East");
              getInternalDialogContentPane().add(getBillCardPanel(), "Center");
              getInternalDialogContentPane().add(getUIPanelButton(), "South");
            } catch (java.lang.Throwable ivjExc) {
            }
        }
        return ivjInternalDialogContentPane;
    }
    
    /**
     * 返回 UIPanel 特性值.
     * @return nc.ui.pub.beans.UIPanel
     */
    public nc.ui.pub.beans.UIPanel getUIPanelNorth() {
        if (ivjUIPanelNorth == null) {
            try {
                ivjUIPanelNorth = new nc.ui.pub.beans.UIPanel();
                ivjUIPanelNorth.setName("UIPanelNorth");
                ivjUIPanelNorth.setPreferredSize(new java.awt.Dimension(100, 10));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjUIPanelNorth;
    }
    
    /**
     * 返回 UIPanel 特性值.
     * @return nc.ui.pub.beans.UIPanel
     */
    public nc.ui.pub.beans.UIPanel getUIPanelWest() {
        if (ivjUIPanelWest == null) {
            try {
                ivjUIPanelWest = new nc.ui.pub.beans.UIPanel();
                ivjUIPanelWest.setName("UIPanelWest");
                ivjUIPanelWest.setPreferredSize(new java.awt.Dimension(5, 10));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjUIPanelWest;
    }
    
    /**
     * 返回 UIPanel 特性值.
     * @return nc.ui.pub.beans.UIPanel
     */
    public nc.ui.pub.beans.UIPanel getUIPanelEast() {
        if (ivjUIPanelEast == null) {
            try {
                ivjUIPanelEast = new nc.ui.pub.beans.UIPanel();
                ivjUIPanelEast.setName("UIPanelEast");
                ivjUIPanelEast.setPreferredSize(new java.awt.Dimension(5, 10));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjUIPanelEast;
    }
    
    /**
     * 每当部件抛出异常时被调用
     * @param exception java.lang.Throwable
     */
    public void handleException(java.lang.Throwable exception) {
        exception.printStackTrace(System.out);
    }
    
	/**
	 * This method initializes transferBtn	
	 * @return javax.swing.JButton	
	 */
	private JButton getTransferBtn() {
		if (genBtn == null) {
			genBtn = new UIButton();
			genBtn.setText("批量修改");//"批量修改"
			genBtn.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if(!nullValidate()){
						return;
					};
					closeOK();
				}
			});
		}
		return genBtn;
	}

	/**
	 * This method initializes cancelBtn	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getCancelBtn() {
		if (cancelBtn == null) {
			cancelBtn = new UIButton();
			cancelBtn.setText(Msg.common("UC001-0000008"));//"取消"
			cancelBtn.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					closeCancel();
				}
			});
		}
		return cancelBtn;
	}
    
    /**
     * 返回 UIPanel 特性值.
     * @return nc.ui.pub.beans.UIPanel
     */
    public nc.ui.pub.beans.UIPanel getUIPanelButton() {
        if (ivjUIPanelButton == null) {
            try {
                ivjUIPanelButton = new nc.ui.pub.beans.UIPanel();
                ivjUIPanelButton.setName("UIPanelButton");
                ivjUIPanelButton.setLayout(new java.awt.BorderLayout());
                UIPanel temp2 = new UIPanel(new java.awt.FlowLayout());
                temp2.add(getTransferBtn());
                temp2.add(getCancelBtn());
                ivjUIPanelButton.add(temp2);
                ivjUIPanelButton.setPreferredSize(new java.awt.Dimension(300, 50));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjUIPanelButton;
    }
	/**
	 * 字段的非空校验
	 * 
	 * @return 通过校验返回 true，否则返回 false
	 */
	private boolean nullValidate() {
		try {
			getBillCardPanel().getBillData().dataNotNullValidate();
		} catch (ValidationException e) {
			MessageDialog.showErrorDlg(this, null,e.getMessage());
			return false;
		}
		return true;
	}

	
	public void afterEdit(BillEditEvent e) {
		
		if(e.getKey().equals("oldpsnpk")){
			BillItem oldItem = getBillCardPanel().getHeadItem("oldpsnpk");
	        UIRefPane oldRef = (UIRefPane)oldItem.getComponent();
	        String[] str = oldRef.getRefPKs();
			String pkss = PubTools.combinArrayToString(str);
				BillItem custItem = getBillCardPanel().getHeadItem("pk_cumandoc");
		        UIRefPane custRef = (UIRefPane)custItem.getComponent();
		        CustPsndocRefModel  ref=new CustPsndocRefModel();
		        custRef.setRefModel(ref);
		        custRef.setMultiSelectedEnabled(true);
		        custRef.setWhereString("pk_psndoc in "+pkss);
		}
		if(e.getKey().equals("pk_cumandoc")){
				BillItem custItem = getBillCardPanel().getHeadItem("pk_cumandoc");
		        UIRefPane custRef = (UIRefPane)custItem.getComponent();
		        String[] pks =custRef.getRefPKs();
		        setPks(pks);
		}
		
	}

	public void bodyRowChange(BillEditEvent e) {
		// TODO Auto-generated method stub
	}
}
