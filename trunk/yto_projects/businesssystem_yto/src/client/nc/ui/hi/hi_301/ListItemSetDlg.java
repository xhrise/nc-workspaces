package nc.ui.hi.hi_301;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.table.TableCellEditor;

import nc.itf.hi.HIDelegator;
import nc.ui.hr.global.Global;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.vo.hi.hi_301.GeneralVO;

/**
 * 项目设置窗口。 创建日期：(2005-12-26 9:57:08)
 * 
 * @author：Administrator
 */
public class ListItemSetDlg extends nc.ui.pub.beans.UIDialog implements
	ActionListener{
	private SortableBillScrollPane ivjListItemSetTable = null;

	private nc.ui.pub.beans.UIButton ivjUIButtonAllCancel = null;

	private nc.ui.pub.beans.UIButton ivjUIButtonAllSelect = null;

	private nc.ui.pub.beans.UIButton ivjUIButtonCancel = null;

	private nc.ui.pub.beans.UIButton ivjUIButtonOK = null;

	private javax.swing.JPanel ivjUIDialogContentPane = null;

	private nc.ui.pub.beans.UIPanel ivjUIPanelSouth = null;
	
	private PsnInfCollectUI parent;
	
	private String userid = ClientEnvironment.getInstance().getUser().getPrimaryKey();

	/**
	 * ListItemSetDlg 构造子注解。
	 */
	public ListItemSetDlg() {
		super();
		initialize();
	}

	/**
	 * ListItemSetDlg 构造子注解。
	 * 
	 * @param parent
	 *            java.awt.Container
	 */
	public ListItemSetDlg(java.awt.Container parent) {
		super(parent);
	}

	/**
	 * ListItemSetDlg 构造子注解。
	 * 
	 * @param parent
	 *            java.awt.Container
	 * @param title
	 *            java.lang.String
	 */
	public ListItemSetDlg(java.awt.Container parent, String title) {
		super(parent, title);
	}

	public ListItemSetDlg(PsnInfCollectUI parent) {
		super(parent);
		this.parent = parent;	
		initialize();
	}
	/**
	 * ListItemSetDlg 构造子注解。
	 * 
	 * @param owner
	 *            java.awt.Frame
	 */
	public ListItemSetDlg(java.awt.Frame owner) {
		super(owner);
	}

	/**
	 * ListItemSetDlg 构造子注解。
	 * 
	 * @param owner
	 *            java.awt.Frame
	 * @param title
	 *            java.lang.String
	 */
	public ListItemSetDlg(java.awt.Frame owner, String title) {
		super(owner, title);
	}

	/**
	 * 返回 ListItemSetTable 特性值。
	 * 
	 * @return nc.ui.hi.hi_301.SortableBillScrollPane
	 */
	/* 警告：此方法将重新生成。 */
	private SortableBillScrollPane getListItemSetTable() {
		if (ivjListItemSetTable == null) {
			try {
				ivjListItemSetTable = new nc.ui.hi.hi_301.SortableBillScrollPane();
				ivjListItemSetTable.setName("ListItemSetTable");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjListItemSetTable;
	}

	/**
	 * 返回 UIButtonAllCancel 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getUIButtonAllCancel() {
		if (ivjUIButtonAllCancel == null) {
			try {
				ivjUIButtonAllCancel = new nc.ui.pub.beans.UIButton();
				ivjUIButtonAllCancel.setName("UIButtonAllCancel");
				ivjUIButtonAllCancel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("600704","UPP600704-000210")/*@res "全消"*/);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjUIButtonAllCancel;
	}

	/**
	 * 返回 UIButtonAllSelect 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getUIButtonAllSelect() {
		if (ivjUIButtonAllSelect == null) {
			try {
				ivjUIButtonAllSelect = new nc.ui.pub.beans.UIButton();
				ivjUIButtonAllSelect.setName("UIButtonAllSelect");
				ivjUIButtonAllSelect.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("600704","UPP600704-000211")/*@res "全选"*/);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjUIButtonAllSelect;
	}

	/**
	 * 返回 UIButtonCancel 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getUIButtonCancel() {
		if (ivjUIButtonCancel == null) {
			try {
				ivjUIButtonCancel = new nc.ui.pub.beans.UIButton();
				ivjUIButtonCancel.setName("UIButtonCancel");
				ivjUIButtonCancel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("600704","upt600704-000155")/*@res "取消"*/);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjUIButtonCancel;
	}

	/**
	 * 返回 UIButtonOK 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getUIButtonOK() {
		if (ivjUIButtonOK == null) {
			try {
				ivjUIButtonOK = new nc.ui.pub.beans.UIButton();
				ivjUIButtonOK.setName("UIButtonOK");
				ivjUIButtonOK.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("600704","UPP600704-000010")/*@res "确定"*/);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjUIButtonOK;
	}

	/**
	 * 返回 UIDialogContentPane 特性值。
	 * 
	 * @return javax.swing.JPanel
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {
			try {
				ivjUIDialogContentPane = new javax.swing.JPanel();
				ivjUIDialogContentPane.setName("UIDialogContentPane");
				ivjUIDialogContentPane.setLayout(new java.awt.BorderLayout());
				getUIDialogContentPane().add(getListItemSetTable(), "Center");
				getUIDialogContentPane().add(getUIPanelSouth(), "South");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjUIDialogContentPane;
	}

	/**
	 * 返回 UIPanelSouth 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIPanel getUIPanelSouth() {
		if (ivjUIPanelSouth == null) {
			try {
				ivjUIPanelSouth = new nc.ui.pub.beans.UIPanel();
				ivjUIPanelSouth.setName("UIPanelSouth");
				getUIPanelSouth().add(getUIButtonAllSelect(),
						getUIButtonAllSelect().getName());
				getUIPanelSouth().add(getUIButtonAllCancel(),
						getUIButtonAllCancel().getName());
				getUIPanelSouth().add(getUIButtonOK(),
						getUIButtonOK().getName());
				getUIPanelSouth().add(getUIButtonCancel(),
						getUIButtonCancel().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjUIPanelSouth;
	}

	/**
	 * 每当部件抛出异常时被调用
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {

		/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
		// System.out.println("--------- 未捕捉到的异常 ---------");
		// exception.printStackTrace(System.out);
	}

	/**
	 * 初始化类。
	 */
	/* 警告：此方法将重新生成。 */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("ListItemSetDlg");
			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			setSize(779, 450);
			setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID("600704","UPP600704-000191")/*@res "员工信息项目设置"*/);
			setContentPane(getUIDialogContentPane());
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		getUIButtonOK().addActionListener(this);
		getUIButtonAllCancel().addActionListener(this);
		getUIButtonAllSelect().addActionListener(this);
		getUIButtonCancel().addActionListener(this);
		
		getListItemSetTable().setTableModel(getTableModel());
		setTableData();
//		getListItemSetTable().getTableModel().setBodyDataVO(
//				parent.getPsnListData());
//		getListItemSetTable().setRowNOShow(true);
		// user code end
	}

	/**
	 * 主入口点 - 当部件作为应用程序运行时，启动这个部件。
	 * 
	 * @param args
	 *            java.lang.String[]
	 */
	public static void main(java.lang.String[] args) {
		try {
			ListItemSetDlg aListItemSetDlg;
			aListItemSetDlg = new ListItemSetDlg();
			aListItemSetDlg.setModal(true);
			aListItemSetDlg
					.addWindowListener(new java.awt.event.WindowAdapter() {
						public void windowClosing(java.awt.event.WindowEvent e) {
							System.exit(0);
						};
					});
			aListItemSetDlg.show();
			java.awt.Insets insets = aListItemSetDlg.getInsets();
			aListItemSetDlg.setSize(aListItemSetDlg.getWidth() + insets.left
					+ insets.right, aListItemSetDlg.getHeight() + insets.top
					+ insets.bottom);
			aListItemSetDlg.setVisible(true);
		} catch (Throwable exception) {
			System.err.println("nc.ui.pub.beans.UIDialog 的 main() 中发生异常");
			exception.printStackTrace(System.out);
		}
	}

	/* （非 Javadoc）
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getUIButtonAllSelect()) {
			setTableColumnValue(e, true,0);
		} else if (e.getSource() == getUIButtonAllCancel()) {
			setTableColumnValue(e, false,0);
		} else if (e.getSource() == getUIButtonOK()) {
			GeneralVO[] selectItemVos = getSelectedVOs();			
			
			int arrylen = (selectItemVos==null?1:selectItemVos.length+1);
			GeneralVO[] insertItemvos = new GeneralVO[arrylen];
			for (int i = 0; i < insertItemvos.length; i++) {
				if(i==0){
					insertItemvos[0] = new GeneralVO();
					insertItemvos[0].setAttributeValue("pk_corp",Global.getCorpPK());
					insertItemvos[0].setAttributeValue("funcode",parent.getModuleName());
					insertItemvos[0].setAttributeValue("userid", userid);
				}else{
					insertItemvos[i] = selectItemVos[i-1];
					insertItemvos[i].setAttributeValue("userid", userid);
				}				
			}
			try {
				//50 fixed
				HIDelegator.getPsnInf().insertTable(insertItemvos, "hi_itemset");
				GeneralVO[] itemSetedVOs = HIDelegator.getPsnInf().queryListItem(Global.getCorpPK(),parent.getModuleName(),"seted");
				//PsnInfBO_Client.insertTable(insertItemvos, "hi_itemset");
				//GeneralVO[] itemSetedVOs = nc.ui.hi.hi_301.PsnInfBO_Client.queryListItem(Global.getCorpPK(),parent.getModuleName(),"seted");				
				//50 end				
				
				GeneralVO item = new  GeneralVO();
				item.setAttributeValue("isDisplay", new Boolean(true));
				item.setAttributeValue("setcode","bd_psnbasdoc");
				item.setAttributeValue("setname", "人员基本信息");
				item.setAttributeValue("fldcode", "belong_pk_corp");
				item.setAttributeValue("fldname", "归属公司");
				item.setAttributeValue("datatype", new Integer(6));
				item.setAttributeValue("fldreftype", "公司目录(集团)S");
				item.setAttributeValue("pk_flddict", "aaaaaaaaaaaaaaaa");
				item.setAttributeValue("pk_setdict", "40000000000000000002");
				item.setAttributeValue("showorder", 0);
				
				GeneralVO[] itemVOsnew = new GeneralVO[itemSetedVOs.length+1];
				itemVOsnew[0] = item;
				for(int i=0;i<itemSetedVOs.length;i++){
					
					itemVOsnew[i+1] = itemSetedVOs[i];
				}

				if (parent != null) {
					parent.setSelectItemVos(itemVOsnew);
					if(itemSetedVOs == null || itemSetedVOs.length == 0){
						parent.setListDefault(true);
					}else{
						parent.setListDefault(false);
					}
				}
			} catch (Exception exception) {
				exception.printStackTrace();
				reportException(exception);
			}
			parent.setItemchange(true);
			closeOK();
		} else if (e.getSource() == getUIButtonCancel()) {
		    
			try {
				//50 fixed
				GeneralVO[] itemSetedVOs = HIDelegator.getPsnInf()
				.queryListItem(Global.getCorpPK(), parent
						.getModuleName(), "seted");
//				GeneralVO[] itemSetedVOs = nc.ui.hi.hi_301.PsnInfBO_Client
//						.queryListItem(Global.getCorpPK(), parent
//								.getModuleName(), "seted");
				//50 end
				if (parent != null) {
					parent.setSelectItemVos(itemSetedVOs);
					if(itemSetedVOs == null || itemSetedVOs.length == 0){
						parent.setListDefault(true);
					}else{
						parent.setListDefault(false);
					}
				}
			} catch (Exception exception) {
				exception.printStackTrace();
				reportException(exception);
			}
			parent.setItemchange(false);
			closeCancel();
		}
		
	}
	
	public GeneralVO[] getSelectedVOs(){
		// 不失去焦点得不到数据
	     TableCellEditor editor = getListItemSetTable().getTable()
				.getCellEditor();
		if (editor != null) {
			editor.stopCellEditing();
		}
		int rowcount = getListItemSetTable().getTable().getRowCount();
		Vector vSelectRow = new Vector();
		if (rowcount > 0) {
			for (int i = 0; i < rowcount; i++) {
				Boolean isSelected = (Boolean) getListItemSetTable()
						.getTable().getValueAt(i, 0);
				if (isSelected != null && isSelected.booleanValue()) {
					GeneralVO vo = (nc.vo.hi.hi_301.GeneralVO)getListItemSetTable().getTableModel().getBodyValueRowVO(i,"nc.vo.hi.hi_301.GeneralVO");
					vSelectRow.addElement(vo);
				}
			}
		}
		GeneralVO[] selectVOs = null;
		if(vSelectRow.size() > 0){
			selectVOs = new GeneralVO[vSelectRow.size()];
			vSelectRow.copyInto(selectVOs);
		}
		return  selectVOs;
	}
	
	/**
	 * 增加参数ActionEvent e，用来判断是否点击“全消”按钮。
	 * amend on 2009-09-17 by fengwei
	 */
	private void setTableColumnValue(ActionEvent e, boolean isSelect,int columnNO) {
		int rowcount = getListItemSetTable().getTable().getRowCount();
		if (rowcount > 0) {
			for (int i = 0; i < rowcount; i++) {
				//---------------------------------------------------------------------------
				if(e.getSource() == getUIButtonAllCancel()){
//					String value = (String) getListItemSetTable().getTable().getValueAt(i, 1);
//					if(value != null && (value.equals("姓名") || value.equals("人员编码"))){
//						isSelect = true;
//					}else {
//						isSelect = false;
//					}
					isSelect = false;
				}
				//---------------------------------------------------------------------------
				
				getListItemSetTable().getTable().setValueAt(
						new Boolean(isSelect), i, columnNO);
			}
		}
	}
	/**
	 * 
	 * @return
	 */
	public nc.ui.pub.bill.BillModel getTableModel() {
        //是否显示 项目名称 显示顺序 信息集
        String[] columnName = {
                nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
                        "UPP600704-000212")/*
                                            * @res "是否显示"
                                            */,
                nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
                        "UPP600704-000119")/*
                                            * @res "项目名称"
                                            */,
                nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
                        "UPP600704-000213")/*
                                            * @res "显示顺序"
                                            */,
                nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
                        "UPP600704-000012")/*
                                            * @res "信息集"
                                            */, "项目主键", "信息集主键", "项目编码" };
        String[] columnCode = { "isDisplay", "fldname", "showorder", "setname",
                "pk_flddict", "pk_setdict", "fldcode" };
        int[] dataType = { BillItem.BOOLEAN, BillItem.STRING, BillItem.INTEGER,
                BillItem.STRING, BillItem.STRING, BillItem.STRING,
                BillItem.STRING };
        BillItem[] biaBody = new BillItem[columnCode.length];
        for (int i = 0; i < columnCode.length; i++) {
            biaBody[i] = new BillItem();
            biaBody[i].setName(columnName[i]);
            biaBody[i].setKey(columnCode[i]);
            biaBody[i].setWidth(100);
            biaBody[i].setEnabled(true);
            //			biaBody[i].setEdit(false);
            biaBody[i].setShow(true);
            biaBody[i].setNull(false);
            biaBody[i].setDataType(dataType[i]);
            if (i == 0) {
                ((UICheckBox) biaBody[i].getComponent())
                        .setHorizontalAlignment(UICheckBox.CENTER);
            } else if (i > 3) {
                biaBody[i].setShow(false);
            }
            if (i == 0 || i == 2) {
                biaBody[i].setEdit(true);
            } else {
                biaBody[i].setEdit(false);
            }
        }

        BillModel billModel = new BillModel();
        billModel.setBodyItems(biaBody);

        return billModel;
    }
	
	private void setTableData(){
		try{
			//String funcode = parent.getModuleName();//debug
			//得到itemVO
			//50 fixed
			GeneralVO[] itemVOs = HIDelegator.getPsnInf().queryListItem(Global.getCorpPK(),parent.getModuleName(),"all");
			//GeneralVO[] itemVOs = nc.ui.hi.hi_301.PsnInfBO_Client.queryListItem(Global.getCorpPK(),parent.getModuleName(),"all");
			//50 end
			//增加“归属公司”，此字段在信息集中不存在，在程序中增加。
			GeneralVO item = new  GeneralVO();
			item.setAttributeValue("isDisplay", new Boolean(true));
			item.setAttributeValue("setcode","bd_psnbasdoc");
			item.setAttributeValue("setname", "人员基本信息");
			item.setAttributeValue("fldcode", "belong_pk_corp");
			item.setAttributeValue("fldname", "归属公司");
			item.setAttributeValue("datatype", new Integer(6));
			item.setAttributeValue("fldreftype", "公司目录(集团)S");
			item.setAttributeValue("pk_flddict", "aaaaaaaaaaaaaaaa");
			item.setAttributeValue("pk_setdict", "40000000000000000002");
			item.setAttributeValue("showorder", 0);
			
			GeneralVO[] itemVOsnew = new GeneralVO[itemVOs.length+1];
			itemVOsnew[0] = item;
			for(int i=0;i<itemVOs.length;i++){
				
				itemVOsnew[i+1] = itemVOs[i];
			}

			getListItemSetTable().getTableModel().setBodyDataVO(itemVOsnew);
			getListItemSetTable().setRowNOShow(true);
		}catch(Exception e){
			e.printStackTrace();
			reportException(e);
		}
	}
}
