/*
 * 创建日期 2006-1-18
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.ui.bd.deptdoc;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Enumeration;
import java.util.List;

import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import nc.bs.logging.Logger;
import nc.bs.uap.sf.facility.SFServiceFacility;
import nc.itf.trade.excelimport.IImportableEditor;
import nc.itf.trade.excelimport.ImportableInfo;
import nc.ui.bd.ddreader.DataReaderConstEnumFactoryAdapter;
import nc.ui.bd.ddreader.QzsmIntToIntDataTypeConvert;
import nc.ui.bd.ddreader.QzsmIntToStrDataTypeConvert;
import nc.ui.bd.def.CardDefShowUtil;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITree;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.linkoperate.ILinkMaintainData;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.button.ButtonVOFactory;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.card.CardEventHandler;
import nc.ui.trade.excelimport.InputItem;
import nc.ui.trade.excelimport.InputItemCreator;
import nc.ui.trade.excelimport.inputitem.InputItemImpl;
import nc.ui.trade.pub.IVOTreeData;
import nc.ui.trade.pub.TableTreeNode;
import nc.ui.trade.treecard.BillTreeCardUI;
import nc.vo.bd.deptdoc.DeptdocUserObj;
import nc.vo.bd.deptdoc.importable.processor.DeptdocVOProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ExtendedAggregatedValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.trade.button.ButtonVO;

import org.apache.commons.lang.StringUtils;

/**
 * @author xuchao
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class DeptDocUI extends BillTreeCardUI implements IImportableEditor {

	/**
	 * <code>serialVersionUID</code> 的注释
	 */
	private static final long serialVersionUID = -2379575065574502806L;
	private Object userObj;
	private UFBoolean isHRModuleUsed;
	private DeptDocBusiDelegater busiDelegater;
	private ButtonVO[] fatherBtnVO =null;
	
	
	//新增内容
	private UIPanel showSealDataPanel=null;//是否显示封存数据Panel
	private UICheckBox showSealdataCbx=null;//是否显示封存数据复选框
	private UICheckBox showHrcanceldataCbx=null;//是否显示撤销数据复选框
	DeptDocTreeCardData treeData=null;
	
	public DeptDocUI() {
		super();
		initlize();
	}

	public DeptDocUI(String pk_corp, String pk_billType, String pk_busitype,
			String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
		initlize();
	}
	
	private UICheckBox getShowHrcanceldataCbx() {
		if(showHrcanceldataCbx == null) {
			showHrcanceldataCbx = new UICheckBox();
			showHrcanceldataCbx.setPreferredSize(new Dimension(20,20));
			showHrcanceldataCbx.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent e) {
					doWhenHrcancelFlagChange(e);
				}});
		}
		return showHrcanceldataCbx;
	}
	
	 private UIPanel getSealdataPanel() {
	    	if(showSealDataPanel==null) {
	    		showSealDataPanel=new UIPanel();
	    		showSealDataPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
	    		showSealDataPanel.add(getShowSealdataCbx());
	    		showSealDataPanel.add(new UILabel(NCLangRes.getInstance().getStrByID("10080404", "UPT10080404-000000")/*是否显示封存数据*/));
	    		showSealDataPanel.add(getShowHrcanceldataCbx());
	    		showSealDataPanel.add(new UILabel(NCLangRes.getInstance().getStrByID("10080404", "UPP10080404-000041")/*是否显示撤销部门*/));
	    	}
	    	return showSealDataPanel;
	 }
	 
	 private void doWhenHrcancelFlagChange(ItemEvent e) {
		 try {
				if(e.getStateChange()==ItemEvent.SELECTED) {
					((DeptDocTreeCardData)getCreateTreeData()).setShowHrcancelData(true);
				}else {
					((DeptDocTreeCardData)getCreateTreeData()).setShowHrcancelData(false);
				}
				//初始化缓存
				clearTreeSelect();

				createBillTree(getCreateTreeData());

				afterInit();

				setBillOperate(nc.ui.trade.base.IBillOperate.OP_INIT);//将列表界面的打印按钮设为不可用
				
				//特殊处理
				ButtonObject[] btns=getButtons();
				if(btns!=null && btns.length>0) {
					for(int i=0;i<btns.length;i++) {
						if(btns[i].getCode().equals("打印")) {
							btns[i].setEnabled(true);
						}
					}
				}
				updateButtons();
				
				//卡片界面
				if(getEhd().getPanelState()==0) {
				}
				//列表界面
				else {
					getEhd().getDeptTablePanel().getBillListPanel().setHeaderValueVO(getBufferData().getAllHeadVOsFromBuffer());
				}
				
			}catch(Exception be) {
				Logger.error(be.getMessage(),be);
			}
			
	 }
	 
	 public UICheckBox getShowSealdataCbx() {
		 if(showSealdataCbx==null) {
			 showSealdataCbx=new UICheckBox();
			 showSealdataCbx.setPreferredSize(new Dimension(20,20));
			 showSealdataCbx.addItemListener(new ItemListener() {

					public void itemStateChanged(ItemEvent e) {
						doWhenSealflagChange(e);
					}

				});
		 }
		 return showSealdataCbx;
	 }
	 

	protected IVOTreeData createTableTreeData() {
		// TODO 自动生成方法存根
		return null;
	}

	protected IVOTreeData createTreeData() {
		return new DeptDocTreeCardData();
	}
	
	
//    @Override
//	public IVOTreeData getCreateTreeData() {
//    	if(treeData==null) {
//			treeData=new DeptDocTreeCardData();
//		}
//		return treeData;
//	}

	protected CardEventHandler createEventHandler() {
	// TODO 自动生成方法存根
	return new DeptDocEHD(this,getUIControl(),isHRModuleUsed(),getFaterButtonVO());
}
	protected ICardController createController() {
		// TODO 自动生成方法存根
		return new DeptDocCTL(isHRModuleUsed(),getFaterButtonVO());
	}

	public String getRefBillType() {
		// TODO 自动生成方法存根
		return null;
	}

	protected void initSelfData() {
		getBillCardWrapper().initHeadComboBox("deptattr", new DataReaderConstEnumFactoryAdapter("bd_deptdoc","deptattr",new QzsmIntToStrDataTypeConvert()).getAllConstEnums(), false);
		getBillCardWrapper().initHeadComboBox("depttype", new DataReaderConstEnumFactoryAdapter("bd_deptdoc","depttype",new QzsmIntToIntDataTypeConvert()).getAllConstEnums(), false);
	    dealDefShow();
	    UIRefPane refPane=  (UIRefPane) getBillCardWrapper().getBillCardPanel().getHeadItem("pk_fathedept").getComponent();
	    refPane.getRefModel().setUseDataPower(false);//不启用数据权限
	    refPane.getRefModel().setSealedWherePart("canceled <>'Y' and hrcanceled<>'Y' ");
	    
	    //设置部门负责人可以跨公司选择
	    UIRefPane psndocRef1=(UIRefPane)getBillCardPanel().getHeadItem("pk_psndoc").getComponent();
	    UIRefPane psndocRef2=(UIRefPane)getBillCardPanel().getHeadItem("pk_psndoc2").getComponent();
	    UIRefPane psndocRef3=(UIRefPane)getBillCardPanel().getHeadItem("pk_psndoc3").getComponent();
	    psndocRef1.setMultiCorpRef(true);
	    psndocRef2.setMultiCorpRef(true);
	    psndocRef3.setMultiCorpRef(true);
	    
	}
	public void afterInit() throws Exception {
        super.afterInit();

        // 修改树根节点名字
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) getBillTree().getModel().getRoot();
        node.setUserObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC000-0004064")/*
				  * @res
				  * "部门"
				  */ );
    }
	protected UITree getBillTree() {
        UITree tree = super.getBillTree();
        if( ! (tree.getCellRenderer() instanceof  DeptDocTreeCellRender) ){
            tree.setCellRenderer( new DeptDocTreeCellRender());
        }
        return tree;
    }
	  public Object getUserObject() {
	        if( userObj==null ){
	            userObj = new DeptdocUserObj();
	        }
	        return userObj;
	    }
	  /* （非 Javadoc）
	 * @see nc.ui.pub.bill.BillEditListener#afterEdit(nc.ui.pub.bill.BillEditEvent)
	 */
	@SuppressWarnings("deprecation")
	public void afterEdit(BillEditEvent e) {
		if(e.getKey().equals("canceled"))
		{
			if(getBillCardPanel().getHeadItem("canceled").getValue().equals("false"))
			{
				
				getBillCardPanel().getHeadItem("canceldate").setEnabled(false);
				getBillCardPanel().setHeadItem("canceldate",null);
			}
			else {
				getBillCardPanel().getHeadItem("canceldate").setEnabled(true);
				getBillCardPanel().getHeadItem("canceldate").setValue(getClientEnvironment().getDate());
			}
			
		}
		super.afterEdit(e);
	}
	  protected void initPrivateButton() {

        DeptDocSwitchBtnVO switchBtnVO = new DeptDocSwitchBtnVO();
        addPrivateButton(switchBtnVO.getButtonVO());
        DeptDocCopyBtnVO copyBtnVO = new DeptDocCopyBtnVO();
        addPrivateButton(copyBtnVO.getButtonVO());
        DeptDocExportBtnVO exportBtnVO = new DeptDocExportBtnVO();
        addPrivateButton(exportBtnVO.getButtonVO());
        addHRButtonMenu();
        ButtonVO refreshBtnVO = ButtonVOFactory.getInstance().build(
                IBillButton.Refresh);
        refreshBtnVO.setOperateStatus(new int[] {IBillOperate.OP_INIT, IBillOperate.OP_NOTEDIT });
        addPrivateButton(refreshBtnVO);
        //DeptDocManBtnVO docmanBtnVO=new DeptDocManBtnVO();
        //addPrivateButton(docmanBtnVO.getButtonVO());
    }

	/**
	 * 
	 */
	private void addHRButtonMenu() {
		try {
		   if(getFaterButtonVO()==null || getFaterButtonVO().length == 0)
			   return;
		   for (int i = 0; i < getFaterButtonVO().length; i++) {
			ButtonVO fatherButton = getFaterButtonVO()[i];
			addPrivateButton(fatherButton);
			ButtonVO[] childButtonVOs= getBusiDelegater().getHRBtnMenu(isHRModuleUsed()).getChildButtons()[i];
			if (childButtonVOs != null && childButtonVOs.length != 0) {
				int[] childAry = new int[childButtonVOs.length];
				for (int j = 0; j < childButtonVOs.length; j++) {
					if (childButtonVOs[j] == null)
						continue;
					childAry[j] = childButtonVOs[j].getBtnNo();
					addPrivateButton(childButtonVOs[j]);
				}
				fatherButton.setChildAry(childAry);
			}
		}
			
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			showErrorMessage(e.getMessage());
		}
	}

	/**
	 * @param iDeptButtonMenu
	 * @return
	 */
	public ButtonVO[] getFaterButtonVO() {
		try {
			if(fatherBtnVO==null){
			IDeptButtonMenu  buttonMenu=getBusiDelegater().getHRBtnMenu(isHRModuleUsed());
			 if( buttonMenu==null)return null;
			 fatherBtnVO= buttonMenu.getFatherButton();
			}
			 return fatherBtnVO;
		} catch (BusinessException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
			showErrorMessage(e.getMessage());
		}
		return null;
	}

	  /**
		 * HR模拟是否启用
		 * 
		 * @return
		 */
	private boolean isHRModuleUsed(){
	  	if(isHRModuleUsed == null) {
	  		try{
	  			isHRModuleUsed = new UFBoolean(SFServiceFacility.getCreateCorpQueryService().isEnabled(getPk_corp(),"HR"));
	  		} catch(Exception e){
	  			e.printStackTrace();
	  			isHRModuleUsed = new UFBoolean(false);
	  		}
	  	}
	  	return isHRModuleUsed.booleanValue();
	  }
	  /**
	 * @return
	 */
	private String getPk_corp(){
			
			String	pk_corp = getClientEnvironment().getCorporation().getPrimaryKey();
			
			return pk_corp;
		}
	/**
	 * @return
	 */
	private DeptDocBusiDelegater getBusiDelegater() {
		if(busiDelegater==null) {
			busiDelegater=new DeptDocBusiDelegater();
			
		}
		return busiDelegater;
		
	}
	private void initlize() {
		try {
			//edit by stl remove the Ctrl+C accelerator  from tree InputMap
			InputMap map=getBillTree().getInputMap().getParent();
			if(map!=null){
				map.put(KeyStroke.getKeyStroke('C',Event.CTRL_MASK), null);
			}
			
			//增加是否显示封存数据Panel
			addSealdataPanel();
			
			setBillOperate(nc.ui.trade.base.IBillOperate.OP_INIT);
		} catch (Exception e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
	}
	
	public void addSealdataPanel() {
		add(getSealdataPanel(),BorderLayout.NORTH);
	}
	
	
	 private void dealDefShow()   {
	        
	        String [] strDefObjs=new String[] {"部门档案"};
	        String [] strPrefix=new String[] {"def"};
	        
	        try {
				new CardDefShowUtil(this.getBillCardPanel()).showDefWhenRef(strDefObjs, strPrefix,true);
			} catch (Exception e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
				showErrorMessage(e.getMessage());
			}

	    
	    }

	/* （非 Javadoc）
	 * @see nc.ui.trade.treecard.BillTreeCardUI#setDefaultData()
	 */
	public void setDefaultData() throws Exception {
		getBillCardPanel().getHeadItem("pk_corp").setValue(getClientEnvironment().getCorporation().getPrimaryKey());
		getBillCardPanel().getHeadItem("depttype").setValue(new Integer(0));
		getBillCardPanel().getHeadItem("createdate").setValue(getClientEnvironment().getDate());
	}

	/* （非 Javadoc）
	 * @see nc.ui.pub.ToftPanel#setButtons(nc.ui.pub.ButtonObject[])
	 */
	public void setButtons(ButtonObject[] buttons) {
		// TODO 自动生成方法存根
		super.setButtons(buttons);
	}
	
    public UITree getUITree() {
		return this.getBillTree();
	}
    
    private DeptDocEHD getEhd() {
		return ((DeptDocEHD) getCardEventHandler());
	}
    
    /***********************************
     * 以下方法是实现了IImportableEditor接口
     ***********************************/
    
    /** 
     * 返回档案是否可导入信息
     *
     * @see nc.itf.trade.excelimport.IImportableEditor#getImportableInfo()
     */
    public ImportableInfo getImportableInfo() {
		return new ImportableInfo();
	}
    
	/**
	 * (non-Javadoc)
	 *
	 * @see nc.itf.trade.excelimport.IImportableEditor#addNew()
	 */
	public void addNew() {
		getEhd().addNewForImport();
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see nc.itf.trade.excelimport.IImportableEditor#cancel()
	 */
	public void cancel() {
		try {
			getEhd().onBoCancel();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see nc.itf.trade.excelimport.IImportableEditor#save()
	 */
	public void save() throws Exception {
		getEhd().onBoSave();
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see nc.itf.trade.excelimport.IImportableEditor#setValue(java.lang.Object)
	 */
	public void setValue(Object obj) {
		try {
			setDefaultData();
			ExtendedAggregatedValueObject aggvo = (ExtendedAggregatedValueObject) obj;
			process(aggvo);
			getBillCardPanel().getBillData().setImportBillValueVO(aggvo);
			adjustBillByLogic();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据属性的转换规则处理数据
	 */
	private void process(ExtendedAggregatedValueObject aggvo) {
		if (aggvo != null && aggvo.getParentVO() != null)
			DeptdocVOProcessor.processVO(aggvo.getParentVO());
	}

	/**
	 * 根据具体的业务逻辑调整单据的属性或属性值
	 */
	private void adjustBillByLogic() {
		getBillCardPanel().getHeadItem("canceled").setEdit(false);
		getBillCardPanel().getHeadItem("canceldate").setEdit(false);
	}

	/**
	 * 卡片单表头
	 * 
	 * @see nc.itf.trade.excelimport.IImportableEditor#getInputItems()
	 */
	public List<InputItem> getInputItems() {
		List<InputItem> items = InputItemCreator.getInputItems(
				getBillCardPanel().getBillData(), true);
		processSpecialItems(items);
		return items;
	}

	/**
	 * 处理列表中的特殊属性： 
	 * 将特殊属性"封存"、"封存日期"都设为不可编辑
	 */
	private void processSpecialItems(List<InputItem> items) {
		for (InputItem item : items) {
			// 将"封存"、"封存日期"都设为不可编辑
			if ("canceled".equals(item.getItemKey())
					|| "canceldate".equals(item.getItemKey())) {
				InputItemImpl newItem = InputItemImpl.getEquivalent(item);
				newItem.setEdit(false);
				items.set(items.indexOf(item), newItem);
			}
		}
	}

	private void doWhenSealflagChange(ItemEvent e) {
		try {
			if(e.getStateChange()==ItemEvent.SELECTED) {
				((DeptDocTreeCardData)getCreateTreeData()).setShowSealData(true);
			}else {
				((DeptDocTreeCardData)getCreateTreeData()).setShowSealData(false);
			}
			//初始化缓存
			clearTreeSelect();

			createBillTree(getCreateTreeData());

			afterInit();

			setBillOperate(nc.ui.trade.base.IBillOperate.OP_INIT);//将列表界面的打印按钮设为不可用
			
			//特殊处理
			ButtonObject[] btns=getButtons();
			if(btns!=null && btns.length>0) {
				for(int i=0;i<btns.length;i++) {
					if(btns[i].getCode().equals("打印")) {
						btns[i].setEnabled(true);
					}
				}
			}
			updateButtons();
			
			//卡片界面
			if(getEhd().getPanelState()==0) {
			}
			//列表界面
			else {
				getEhd().getDeptTablePanel().getBillListPanel().setHeaderValueVO(getBufferData().getAllHeadVOsFromBuffer());
//				getEhd().getDeptTablePanel().getBillListPanel().getHeadBillModel().execLoadFormula();
			}
			
		}catch(Exception be) {
			Logger.error(be.getMessage(),be);
		}
		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void doMaintainAction(ILinkMaintainData maintaindata) {
		String pk_bill = maintaindata.getBillID();
		if (StringUtils.isBlank(pk_bill))
			return;
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) getBillTree().getModel().getRoot();
		Enumeration<TableTreeNode> enumeration = root.preorderEnumeration();
		enumeration.nextElement();

		TableTreeNode selectedNode = null;
		while (enumeration.hasMoreElements()) {
			TableTreeNode currentNode = (TableTreeNode) enumeration
					.nextElement();
			String currentPrimaryKey = currentNode.getNodeID().toString();
			if (pk_bill.equals(currentPrimaryKey)) {
				selectedNode = currentNode;
				break;
			}
		}

		if (selectedNode != null) {
			getUITree().setSelectionPath(new TreePath(selectedNode.getPath()));
			getMultiAppManager().getStack().get(0);
		} else {
			MessageDialog.showWarningDlg(this, NCLangRes.getInstance()
					.getStrByID("_bill", "UPP_Bill-000034")/* @res "提示" */,
					"查询数据在当前公司内不存在.");
//					NCLangRes.getInstance().getStrByID("_beans",
//							"UPP_Beans-000079")/* @res "定位" */
//							+ NCLangRes.getInstance().getStrByID("common",
//									"UC001-0000053")/* @res "数据" */
//							+ NCLangRes.getInstance().getStrByID("_bill",
//									"UPP_Bill-000262")/* @res "为空" */ /*
//																		 * @res
//																		 * "定位数据为空"
//																		 */
		}
	}
}