package nc.ui.so.so001.panel.card;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.itf.ct.ref.IValiSaleCtRefModel;
import nc.itf.scm.so.so103.IBuyLargess;
import nc.itf.uap.pf.IPFMetaModel;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.bd.b21.CurrParamQuery;
import nc.ui.bd.b21.CurrtypeQuery;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.dbcache.DBCacheFacade;
import nc.ui.ic.pub.lot.LotNumbRefPane;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillSortListener;
import nc.ui.pub.bill.BillStatus;
import nc.ui.pub.bill.BillTableCellRenderer;
import nc.ui.pub.bill.BillTotalListener;
import nc.ui.pub.bill.IBillModelSortPrepareListener;
import nc.ui.pub.bill.IBillRelaSortListener2;
import nc.ui.pub.bill.table.BillTableBooleanCellRenderer;
import nc.ui.scm.ic.freeitem.FreeItemRefPane;
import nc.ui.scm.pub.InvoInfoBYFormula;
import nc.ui.scm.pub.billutil.ClientCacheHelper;
import nc.ui.scm.pub.def.DefSetTool;
import nc.ui.scm.pub.panel.ErrRowsBillTableBooleanCellRenderer;
import nc.ui.scm.pub.panel.ErrRowsTableCellRenderer;
import nc.ui.scm.pub.panel.RelationsCal;
import nc.ui.scm.pub.report.BillRowNo;
import nc.ui.scm.ref.prm.CustAddrRefModel;
import nc.ui.scm.so.SaleBillType;
import nc.ui.so.pub.CalBodySORefModel;
import nc.ui.so.pub.InvAttrCellRenderer;
import nc.ui.so.so001.SaleOrderBO_Client;
import nc.ui.so.so001.panel.SoUIMenuItem;
import nc.ui.so.so001.panel.UnitOfMeasureTool;
import nc.ui.so.so001.panel.bom.BillTools;
import nc.vo.bd.b20.CurrtypeVO;
import nc.vo.bd.invdoc.IInvManDocConst;
import nc.vo.bd.invdoc.InvbindleVO;
import nc.vo.bd.ref.IFilterStrategy;
import nc.vo.pf.pub.BusitypeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ProductCode;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.bd.SmartVODataUtils;
import nc.vo.scm.ct.TypeVO;
import nc.vo.scm.ctpo.RetCtToPoQueryVO;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.ic.bill.WhVO;
import nc.vo.scm.pu.RelationsCalVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.so.so001.SOToolVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;
import nc.vo.so.so001.SoVoConst;
import nc.vo.so.so016.SoVoTools;
import nc.vo.so.so102.InvcalbodyVO;
import nc.vo.so.so103.BuylargessBVO;
import nc.vo.so.so103.BuylargessHVO;
import nc.vo.so.so103.BuylargessVO;
import nc.vo.sp.service.PriceAskResultVO;

/**
 * 销售订单卡片模板
 * 
 * @author zhongwei
 * 
 * 修改日期：2008-06-26 修改人：周长胜 修改内容：v5.5降缓存项目修改
 */
public class SOBillCardPanel extends BillCardPanel implements BillEditListener,
		BillEditListener2, BillTotalListener, BillSortListener,
		IBillRelaSortListener2, IBillModelSortPrepareListener, ActionListener {

	private SaleBillCardUI uipanel;

	private String pk_corp;
	
	private Object oldValue;
	
	//缓存销售订单业务类型核算规则<业务类型id,类型核算规则>
	private HashMap<String,String> verifyRule = new HashMap<String,String>();
	
	//暂存表头整单折扣
	public Object ndiscountrate=null;

	// 人员参照初始条件
	public String sEmployeeRefCondition = null;

	protected FreeItemRefPane ivjFreeItemRefPane = null;

	protected SORefDelegate soRefDelegate = null;

	// 批次参照
	protected nc.ui.ic.pub.lot.LotNumbRefPane ivjLotNumbRefPane = null;

	// 订单存货自由项arraylist
	public ArrayList alInvs = new ArrayList();

	// 合同信息缓存
	public Hashtable hCTTypeVO = null;
	
	//编辑存货时是否关联存货、严格执行合同
	public boolean ifbindct = false;
	//编辑存货时关联合同时是否已经执行过合同类型判断
	public boolean ifbindct_findPrice = false;

	//编辑表体时，最初编辑的key值
	private String firstChangeKey = null;
	
	// 存放存货库存组织默认发货公司
	protected HashMap m_hConCal = new HashMap();

	// 存货参照初始条件
	public String sInvRefCondition = null;

	protected UITextField tfieldBatch = new UITextField();

	protected Hashtable m_htLargess = new Hashtable();

	/**
	 * constructor
	 * 
	 * @param parent
	 * @param name
	 * @param billtype
	 * @param pk_corp
	 * @param operator
	 * @throws Exception
	 */
	public SOBillCardPanel(SaleBillCardUI parent, String name, String billtype,
			String pk_corp, String operator) throws Exception {
		
		uipanel = parent;

		setName(name);

    	setBillType(billtype);

		// 公司
		setCorp(pk_corp);
		this.pk_corp = pk_corp;

		// 操作员
		setOperator(operator);

		// 填加监听
		addBillEditListenerHeadTail(this);

		setBodyMenuShow(true);

		// 填加合计行
		setTatolRowShow(true);		
		
		getBillTable().setSelectionMode(
		    javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		setEnabled(false);

	}

	/**
	 * 编辑后事件处理。
	 * 
	 * 创建日期：(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public void afterEdit(BillEditEvent e) {

		getBillModel().setNeedCalculate(false);
		uipanel.showHintMessage("");
		
		this.firstChangeKey = e.getKey();
		
		//暂存旧值
		this.oldValue = e.getOldValue();
		
		//是否编辑最后一行存货，初始化false
		this.binvedit = false;
		
		try {
			if (e.getPos() == BillItem.HEAD) {

				if (e.getKey().equals("vreceiptcode")) {
					uipanel.m_isCodeChanged = true;
				}

				// 客商
				if (e.getKey().equals("ccustomerid")) {
					afterCustomerEdit(e);
					freshBodyLargess(1);
				}
				// 部门
				else if (e.getKey().equals("cdeptid")) {
					afterDeptEdit(e);
				}
				// 人员
				else if (e.getKey().equals("cemployeeid")) {
					afterEmployeeEdit(e);
				}
				// 销售组织
				else if (e.getKey().equals("csalecorpid")) {
					if (!getSouceBillType().equals("4H") && !getSouceBillType().equals("42")) {
						// 设置表体的缺省的发货公司及库存组织
						getBillCardTools().setSendCalBodyAndWare(0, getRowCount());
						freshBodyLargess(3);
					}

					/** V502 jdm zhongwei* */
					try {
						checkSaleCorp();
					} catch (Exception ee) {
						uipanel.showErrorMessage(ee.getMessage());
					}
					/** V502 jdm zhongwei* */

				}
				// 整单折扣率
				else if (e.getKey().equals("ndiscountrate")) {
					afterDiscountrateEdit(e);
				}
				// 整单税率
				else if (e.getKey().equals("ntaxrate")) {
					afterTaxrateBillEdit(e);
				}
				// 日期
				else if (e.getKey().equals("dbilldate")) {
					afterHeadBillDateEdit(e);
					freshBodyLargess(2);
				}
				// 币种
				else if (e.getKey().equals("ccurrencytypeid")) {
					afterCurrencyEdit(e);
					freshBodyLargess(2);
				}
				// 折本汇率
				else if (e.getKey().equals("nexchangeotobrate")) {
					afterChangeotobrateEdit(e);
				}
				// 库存组织
				else if (e.getKey().equals("ccalbodyid")) {
					afterCcalbodyidEdit(e);
				}
				// 仓库
				else if (e.getKey().equals("cwarehouseid")) {
					afterCwarehouseidEdit(e);
				}
				// 收货单位
				else if (e.getKey().equals("creceiptcustomerid")) {
					afterCreceiptcorpEdit(e);
				}
				// 收货地址
				else if (e.getKey().equals("vreceiveaddress")) {
					afterVreceiveaddressEdit(e);
				}
				// 运输方式
				else if (e.getKey().equals("ctransmodeid")) {
					afterCtransmodeEdit(e);
				}
				// 自定义项
				else if (e.getKey().startsWith("vdef")) {
					DefSetTool
							.afterEditHead(getBillData(), e.getKey(),
									"pk_defdoc"
											+ e.getKey().substring(
													"vdef".length()));
				}
				// 价税合计
				else if ("nheadsummny".equals(e.getKey())) {
					afterHeadsummnyEdit(e);
				}

				findPriceWhenHeadItemChg(e.getKey());

			}
			if (e.getPos() == BillItem.BODY) {

				if (e.getKey().equals(getRowNoItemKey())) {
					afterRownoEdit(e);
				}				
				// 存货编码 清除数据
				if (e.getKey().equals("cinventorycode")) {
					afterInventoryMutiEdit(e);
				}
				// 辅单位
				else if (e.getKey().equals("cpackunitname")
						|| e.getKey().equals("cquoteunit")) {
					afterUnitEdit(e);
				}
				// 合同
				else if (e.getKey().equals("ct_name")) {
					afterCtManageEdit(e);
				}

				// 自由项
				else if (e.getKey().equals("vfree0")) {
					afterFreeItemEdit(e);
				}
				
				if (SaleorderBVO.isPriceOrMny(e.getKey()))
					// 单价金额变化
					afterPriceMuyEdit(e);
				else if (!"cinventorycode".equals(e.getKey())&&!"cinventoryid".equals(e.getKey()))
					// 数量变化
					afterNumberEdit(e);

				// 赠品
				if (e.getKey().equals("blargessflag")) {
					afterLargessFlagEdit(e);
				}
				// 项目
				else if (e.getKey().equals("cprojectname")) {
					afterProjectEdit(e);
				}
				// 批次状态
				else if (e.getKey().equals("fbatchstatus")) {
					afterBatchEdit(e);
				}
				// 批次
				else if (e.getKey().equals("cbatchid")) {
					afterBatchIDEdit(e);
				}
				// 表体库存组织(包括表头库存组织和表体发货库存组织)
				else if (e.getKey().equals("cadvisecalbody")) {
					afterCcalbodyidEdit(e);
				}
				// 表体仓库
				else if (e.getKey().equals("cbodywarehousename")) {
					afterCwarehouseidEdit(e);
				}
				// 表体收货库存组织
				else if (e.getKey().equals("creccalbody")) {
					String creccalbody = getBillCardTools().getBodyStringValue(
							e.getRow(), "creccalbody");
					if (creccalbody == null
							|| !creccalbody.equals(e.getOldValue())) {
						setBodyValueAt(null, e.getRow(), "crecwareid");
						setBodyValueAt(null, e.getRow(), "crecwarehouse");
					}
                    //设置直运仓
		            setDerictTransReceWareHouse(e.getRow());
				}
				// 表体收货仓库
				else if (e.getKey().equals("crecwarehouse")) {
					afterCrecwarehouseEdit(e);
				}
				// 缺货
				else if (e.getKey().equals("boosflag")) {
					afterOOSFlagEdit(e.getRow(), true);
					
					// 更新表头价税合计
					// 此处应与表头价税合计算法同步
					UFDouble nheadsummny = getHeadItem("nheadsummny")
							.getValueObject() == null ? new UFDouble(0)
							: new UFDouble(getHeadItem("nheadsummny")
									.getValueObject().toString());
					UFDouble noriginalcursummny = getBodyValueAt(e.getRow(),
							"noriginalcursummny") == null ? new UFDouble(0)
							: (UFDouble) getBodyValueAt(e.getRow(),
									"noriginalcursummny");
					if ((Boolean) e.getValue()) {
						getHeadItem("nheadsummny")
								.setValue(nheadsummny.sub(noriginalcursummny));
					} else {
						getHeadItem("nheadsummny")
								.setValue(nheadsummny.add(noriginalcursummny));
					}
				}
				// 补货
				else if (e.getKey().equals("bsupplyflag")) {
					afterOOSFlagEdit(e.getRow(), false);
				}
				// 发货日期
				else if (e.getKey().equals("dconsigndate")) {
					afterBodyDateEdit(e.getRow(), true);
				}
				// 到货日期
				else if (e.getKey().equals("ddeliverdate")) {
					afterBodyDateEdit(e.getRow(), false);
				}
				// 收货单位
				else if (e.getKey().equals("creceiptcorpname")) {
					afterBodyCreceiptcorpidEdit(e);
				}
				// 收货地址
				else if (e.getKey().equals("vreceiveaddress")) {
					afterBodyAddressEdit(e);
				}
				// 是否直运(直运调拨)
				else if (e.getKey().equals("bdericttrans")) {
					//afterBdericttransEdit(e);
				}
				// 表体发货公司
				else if (e.getKey().equals("cconsigncorp")) {
					afterCconsignCorpEdit(e.getRow());
				}
				// 自定义项
				else if (e.getKey().startsWith("vdef")) {
					DefSetTool.afterEditBody(getBillData().getBillModel(), e
							.getRow(), e.getKey(), "pk_defdoc"
							+ e.getKey().substring("vdef".length()));
				}

				// 收货地区
				else if (e.getKey().equals("creceiptareaname")) {
					String sDateDeliver = getBodyValueAt(e.getRow(),
							"ddeliverdate") == null ? null : getBodyValueAt(
							e.getRow(), "ddeliverdate").toString().trim();

					if (sDateDeliver == null || sDateDeliver.length() == 0)
						afterBodyDateEdit(e.getRow(), true);
					else
						afterBodyDateEdit(e.getRow(), false);
				}
				// 建议库存组织
				else if (e.getKey().equals("cadvisecalbody")) {
					String sDateDeliver = getBodyValueAt(e.getRow(),
							"ddeliverdate") == null ? null : getBodyValueAt(
							e.getRow(), "ddeliverdate").toString().trim();

					if (sDateDeliver == null || sDateDeliver.length() == 0)
						afterBodyDateEdit(e.getRow(), true);
					else
						afterBodyDateEdit(e.getRow(), false);
				}
				// 价格政策
				else if (e.getKey().equals("cpricepolicy")) {
					afterPricePolicy(e);
				}
				// 价目表
				else if (e.getKey().equals("cpriceitemtablename")) {
					afterPriceItemTable(e);
				}

			}

			getBillCardTools().setManualEdit(e.getRow(), true);

		} catch (Exception ee) {
			// runtime exception
			// it should never happen
			SCMEnv.out(ee);
			//ee.printStackTrace();
		} finally {

			// 执行表头公式
			if (e.getPos() == BillItem.HEAD) {
				String[] sFormulas = getHeadItem(e.getKey()).getEditFormulas();
				if (sFormulas != null && sFormulas.length > 0)
					execHeadFormulas(sFormulas);
			}

			getBillModel().setNeedCalculate(true);

			// 设置表头价税合计
			if (!e.getKey().equals("nheadsummny"))
				if (getHeadItem("nheadsummny") != null)
					getHeadItem("nheadsummny").setValue(
							getTotalValue("noriginalcursummny"));

			updateUI();
		}

		this.firstChangeKey = null;
		uipanel.getPluginProxy().afterEdit(e);
	}
	
	/**
	 * 设置收货仓库为直运仓
	 * @param row
	 * @throws BusinessException 
	 */
	private void setDerictTransReceWareHouse(int row) {
        // 直运调拨
		if ( ((getBodyValueAt(row, "creccalbodyid") != null
				&& getBodyValueAt(row, "creccalbodyid").toString()
				.trim().length() > 0)) && (getBodyValueAt(row, "bdericttrans") != null
				&& new UFBoolean(getBodyValueAt(row, "bdericttrans").toString())
						.booleanValue())) {
			String[] sFormula = {
					"crecwareid->getColValue2(bd_stordoc,pk_stordoc,pk_calbody,creccalbodyid,isdirectstore,\"Y\")",
					"crecwarehouse->getColValue(bd_stordoc,storname,pk_stordoc,crecwareid)" };
			execBodyFormulas(row, sFormula);
		}
	}
	
	/**
	 * 业务类型的核算规则为直运类型时，设置发货仓库为直运仓
	 * @param row
	 * @throws BusinessException
	 */
	private void setDerictTransSendWareHouse(int row) {
		// 业务类型的核算规则为直运类型
		if ((getBodyValueAt(row, "cadvisecalbodyid") != null && getBodyValueAt(
				row, "cadvisecalbodyid").toString().trim().length() > 0)
				&& (getBillCardTools().isZVerifyRule(getBillCardTools().getHeadValue("cbiztype").toString()))) {
			String[] sFormula = {
					"cbodywarehouseid->getColValue2(bd_stordoc,pk_stordoc,pk_calbody,cadvisecalbodyid,isdirectstore,\"Y\")",
					"cbodywarehousename->getColValue(bd_stordoc,storname,pk_stordoc,cbodywarehouseid)" };
			execBodyFormulas(row, sFormula);
		}
	}

	/**
	 * 校验末级销售组织
	 * 
	 */
	public void checkSaleCorp() throws BusinessException {
		Object csalestruid = getHeadItem("csalecorpid").getValueObject();
		if (csalestruid != null) {
			ArrayList o = (ArrayList) DBCacheFacade.runQuery(
					"select csalestruid from bd_salestru where pk_fathsalestru = '"
							+ csalestruid + "'", new ArrayListProcessor());
			if (o != null && o.size() > 0) {
				getHeadItem("csalecorpid").setValue(null);
				throw new BusinessException(NCLangRes.getInstance().getStrByID(
						"40060301", "UPP40060301-000543")/*
															 * @res "只有末级销售组织可选"
															 */);
			}
		}
	}

	/**
	 * 获得行号在单据模版中的Key值
	 * 
	 * @version (00-6-6 13:33:25)
	 * @return java.lang.String
	 */
	public String getRowNoItemKey() {
		return "crowno";
	}

	private void findPriceWhenHeadItemChg(String key) {

		if (getBillModel().getRowCount() <= 0)
			return;

		ArrayList rowlist = new ArrayList();
		for (int i = 0, loop = getBillModel().getRowCount(); i < loop; i++) {
			if (isFindPrice(i,key)) {
				if ((uipanel.strState.equals("修订"))){
					if ((uipanel.SO78.booleanValue()))
						rowlist.add(new Integer(i));
					continue;
				}
				rowlist.add(new Integer(i));
			}
		}

		if (rowlist.size() <= 0)
			return;

		

		//可询价得行
		ArrayList needFindPriceRow = new ArrayList();
		for (int i = 0, loop = rowlist.size(); i < loop; i++) {
			if (!ifModifyPrice(((Integer) rowlist.get(i)).intValue()))
				needFindPriceRow.add(((Integer) rowlist.get(i)).intValue());
		}

		if (needFindPriceRow.size()<=0)
			return;
		
		if ("ccustomerid".equals(key) // || "cdeptid".equals(key)
				|| "csalecorpid".equals(key)
				|| "dbilldate".equals(key)
				|| "ccurrencytypeid".equals(key)) {

			int[] findrows = new int[needFindPriceRow.size()];
			for (int i = 0, loop = needFindPriceRow.size(); i < loop; i++) {
				findrows[i]=((Integer) needFindPriceRow.get(i)).intValue();
				//清空表体行上价格项相关字段,表头询价时重新匹配定价策略
				//V5.5 cnf zc
				getBillCardTools().clearBodyValue(getBillCardTools().getPriceItem(),findrows[i]);
			}
			
			//询价
			findPrice(findrows, null, false);

		}

	}

	/**
	 * 是否寻价。
	 * 
	 * 创建日期：(2001-11-20 15:29:14)
	 * 
	 * @return boolean
	 * 
	 */
	private boolean isFindPrice(int i,String key) {
		Integer bindpricetype = (Integer) getBodyValueAt(i, "bindpricetype");
		if (bindpricetype != null
				&& bindpricetype.intValue() == IInvManDocConst.PRICE_TYPE_BINDLE) {
			// 使用捆绑价，不用询价
			return false;
		}
		if (getBillModel().getRowCount() > 0) {
			if (SaleBillType.SaleQuotation.equals(getSouceBillType())
					|| "38".equals(getSouceBillType())
					|| "3B".equals(getSouceBillType())) {
				// if(binitOnNewByOther)
				// return false;

			} else if (getSouceBillType(i).equals(SaleBillType.SoContract)
					|| getSouceBillType(i).equals(SaleBillType.SoInitContract)) {
				
				//编辑单价金额时不进行执行合同情况判断
				if (firstChangeKey!=null 
						&& ( !"cinventorycode".equals(firstChangeKey)&&!"cinventoryid".equals(firstChangeKey)
								&&!"nnumber".equals(firstChangeKey)&!"nquoteunitnum".equals(firstChangeKey)) 
					)
					return uipanel.SA_15.booleanValue();
				
				String ct_manageid = (String) getBodyValueAt(i, "ct_manageid");
				TypeVO voCtType = null;
				if (ct_manageid != null && ct_manageid.length() != 0) {
					// 获得合同类型标志

					if (hCTTypeVO != null)
						voCtType = (TypeVO) hCTTypeVO.get(ct_manageid);

					if (voCtType == null) {
						try {
							voCtType = SaleOrderBO_Client
									.getContractType(ct_manageid);
						} catch (Throwable ex) {
							SCMEnv.out("获得合同类型标志出错!");
							// ex.printStackTrace();
						}
					}

					if (voCtType != null) {
						int iInvType = voCtType.getNinvctlstyle() == null ? -1
								: voCtType.getNinvctlstyle().intValue();
						int iDataType = voCtType.getNdatactlstyle() == null ? -1
								: voCtType.getNdatactlstyle().intValue();
						// 合同存货控制方式：0存货 1存货分类
						// 合同数据控制方式：(控制合同那些项必须录入。0单价 1数量 2金额 3单价+数量 4单价+金额
						// 5数量+金额 6单价+数量+金额)
						if (iInvType == 0
								&& (iDataType == 0 || iDataType == 3
										|| iDataType == 4 || iDataType == 6)) {
							if (uipanel.SO_17.booleanValue()) {
								//编辑存货时是否关联到单价类合同并严格执行
								ifbindct = true;//用于控制后续逻辑是否反算
								ifbindct_findPrice = true;
								return false;
							} else {
								//清空价格--没有寻过价
								getBillCardTools().clearBodyValue(getBillCardTools().getSaleItems_Price(), i);                 
								//清空金额
								getBillCardTools().clearBodyValue(getBillCardTools().getSaleItems_Mny(), i);
							}
						} else {
							//不严格执行合同
							//清空价格
							getBillCardTools().clearBodyValue(getBillCardTools().getSaleItems_Price(), i);
							//清空金额
							getBillCardTools().clearBodyValue(getBillCardTools().getSaleItems_Mny(), i);
						}
					}
					ifbindct_findPrice = true;
				}
			}
		}

		return uipanel.SA_15.booleanValue();
	}

	/**
	 * 寻价。 创建日期：(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public void findPrice(int[] findrows, String oldinvid, boolean isinvchg) {
		String errmsg = "";
		getBillCardTools().SA34 = uipanel.SA34;

		if (uipanel.SA_15.booleanValue()) {
			ArrayList alNeedFind = getNeedFindPriceRows(findrows);

			HashMap hs = getBillCardTools().findPrice(findrows, oldinvid);

			ArrayList rowlist = new ArrayList();
			if (hs != null && hs.size() > 0) {

				Integer[] rows = null;
				PriceAskResultVO resultvo = null;
				int pricerow = 0;

				// 如果存货变化，需再寻价
				if (isinvchg) {

					rows = (Integer[]) hs.keySet().toArray(
							new Integer[hs.size()]);
					pricerow = 0;

					for (int i = 0, loop = rows.length; i < loop; i++) {
						// 本行不需重新询价
						if (alNeedFind != null && !alNeedFind.contains(rows[i]))
							continue;
						pricerow = rows[i].intValue();
						resultvo = (PriceAskResultVO) hs.get(rows[i]);

						if (resultvo.getErrFlag() != null
								&& resultvo.getErrFlag().intValue() != 0) {
							continue;
						}

						String stemp = (String) getBillCardTools()
								.getBodyValue(pricerow, "cpricepolicyid");
						if (stemp == null || stemp.trim().length() <= 0)
							setBodyValueAt(resultvo.getPricePolicyid(),
									pricerow, "cpricepolicyid");
						stemp = (String) getBillCardTools().getBodyValue(
								pricerow, "cpriceitemid");
						if (stemp == null || stemp.trim().length() <= 0)
							setBodyValueAt(resultvo.getPriceTypeid(), pricerow,
									"cpriceitemid");

						setBodyValueAt(resultvo.getFindProcess(), pricerow,
								"cpricecalproc");
						stemp = (String) getBillCardTools().getBodyValue(
								pricerow, "cpriceitemtable");
						if (stemp == null || stemp.trim().length() <= 0)
							setBodyValueAt(resultvo.getPricetariffid(),
									pricerow, "cpriceitemtable");

					}

					// 检查有没有寻价项相同的行
					boolean istowfindprc = false;
					if (findrows != null && findrows.length > 0) {
						String skey = null, skey1 = null;
						for (int i = 0, loop = findrows.length; i < loop; i++) {
							skey = getBillCardTools().getSalePriceVOKey(
									getBillCardTools().getPriceParam(
											findrows[i]));
							if (skey == null || skey.trim().length() <= 0)
								continue;
							for (int m = 0, loopm = getRowCount(); m < loopm; m++) {
								if (findrows[i] == m)
									continue;
								skey1 = getBillCardTools().getSalePriceVOKey(
										getBillCardTools().getPriceParam(m));
								if (skey.equals(skey1)) {
									istowfindprc = true;
									break;
								}
							}
							if (istowfindprc)
								break;
						}
					}

					if (istowfindprc)
						hs = getBillCardTools().findPrice(findrows, oldinvid);

					if (hs == null || hs.size() <= 0) {
						for (int i = 0, loop = rows.length; i < loop; i++) {
							// 本行不需重新询价
							if (alNeedFind != null
									&& !alNeedFind.contains(rows[i]))
								continue;

							pricerow = rows[i].intValue();
							resultvo = (PriceAskResultVO) hs.get(rows[i]);
							if (resultvo == null)
								continue;
							if (resultvo.getErrFlag() != null
									&& resultvo.getErrFlag().intValue() != 0) {
								continue;
							}

							setBodyValueAt(null, pricerow, "cpricepolicyid");
							setBodyValueAt(null, pricerow, "cpricepolicy");
							
							setBodyValueAt(null, pricerow, "cpriceitemid");
							setBodyValueAt(null, pricerow, "cpriceitem");
							
							setBodyValueAt(null, pricerow, "cpricecalproc");
							
							setBodyValueAt(null, pricerow, "cpriceitemtable");
							setBodyValueAt(null, pricerow, "cpriceitemtablename");

						}
						return;
					}

				}

				rows = (Integer[]) hs.keySet().toArray(new Integer[hs.size()]);
				pricerow = 0;

				for (int i = 0, loop = rows.length; i < loop; i++) {
					// 本行不需重新询价
					if (alNeedFind != null && !alNeedFind.contains(rows[i]))
						continue;

					pricerow = rows[i].intValue();
					resultvo = (PriceAskResultVO) hs.get(rows[i]);
					if (resultvo == null)
						continue;

					if (resultvo.getErrFlag() != null
							&& resultvo.getErrFlag().intValue() != 0) {
						errmsg += nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"40060301",
								"UPP40060301-000235",
								null,
								new String[] {
										(String) getBodyValueAt(pricerow,
												"crowno"),
										resultvo.getErrMessage() });
						errmsg += "\n";

						// 寻价失败，清除相关的价格项
						getBillCardTools().clearBodyValue(
								SOBillCardTools.pricekeys, pricerow);
						
						//询价后设置各字段编辑性
						setEditEnabled(false,pricerow);
						
						continue;
					}
					
					//询价后设置各字段编辑性
					setEditEnabled(true,pricerow);

					//设置询价得到的单价、净价和折扣
					if (uipanel.SA_02.booleanValue()) {
						//设置原始的寻价单价、净价
						setBodyValueAt(resultvo.getNum(), pricerow,"nqtorgtaxprc");
						setBodyValueAt(resultvo.getNetNum(), pricerow,"nqtorgtaxnetprc");
						//设置报价进行计算
						setBodyValueAt(resultvo.getNum(), pricerow,"norgqttaxprc");
						setBodyValueAt(resultvo.getDiscount(), pricerow,"nitemdiscountrate");
						calculateNumber(rows[i].intValue(), "norgqttaxprc");
					} else {
						//设置原始的寻价单价、净价
						setBodyValueAt(resultvo.getNum(), pricerow,"nqtorgprc");
						setBodyValueAt(resultvo.getNetNum(), pricerow,"nqtorgnetprc");
						//设置报价进行计算
						setBodyValueAt(resultvo.getNum(), pricerow, "norgqtprc");
						setBodyValueAt(resultvo.getDiscount(), pricerow,	"nitemdiscountrate");
						calculateNumber(rows[i].intValue(), "norgqtprc");
					}

					String stemp = (String) getBillCardTools().getBodyValue(
							pricerow, "cpricepolicyid");
					if (stemp == null || stemp.trim().length() <= 0)
						setBodyValueAt(resultvo.getPricePolicyid(), pricerow,
								"cpricepolicyid");

					stemp = (String) getBillCardTools().getBodyValue(pricerow,
							"cpriceitemid");
					if (stemp == null || stemp.trim().length() <= 0)
						setBodyValueAt(resultvo.getPriceTypeid(), pricerow,
								"cpriceitemid");

					setBodyValueAt(resultvo.getFindProcess(), pricerow,
							"cpricecalproc");

					stemp = (String) getBillCardTools().getBodyValue(pricerow,
							"cpriceitemtable");
					if (stemp == null || stemp.trim().length() <= 0)
						setBodyValueAt(resultvo.getPricetariffid(), pricerow,
								"cpriceitemtable");

					setBodyValueAt(resultvo.getReturnMoney(), pricerow,
							"breturnprofit");

					setBodyValueAt(resultvo.getPriceProtect(), pricerow,
							"bsafeprice");

					rowlist.add(rows[i]);

					if (getBillModel().getRowState(pricerow) == BillModel.NORMAL)
						getBillModel().setRowState(pricerow,
								BillModel.MODIFICATION);
				}
			}
			
			String[] formulas = {
					"cpriceitem->getColValue(prm_pricetype,cpricetypename,cpricetypeid,cpriceitemid)",
					"cpriceitemtablename->getColValue(prm_tariff,cpricetariffname,cpricetariffid,cpriceitemtable)",
					"cpricepolicy->getColValue(prm_pricepolicy,pricepolicyname,pricepolicyid,cpricepolicyid)" };
			getBillCardTools().execBodyFormulas(formulas, rowlist);
		}
		
		if (errmsg != null && errmsg.trim().length() > 0) {
			uipanel.showErrorMessage(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("40060301", "UPP40060301-000237", null,
							new String[] { errmsg }));
		}
	}

	/**
	 * 找到需要询价的行， 
	 * 
	 * 修订：只有修改了存货和新增行需要询价
	 * 
	 * @return
	 */
	private ArrayList getNeedFindPriceRows(int[] irows) {

		/*//修订不询价
		if (!uipanel.strState.equals("修订")) -=notranslate=-
			return null;*/

		ArrayList al = new ArrayList();
        for (int i = 0; i<irows.length; i++){
        	al.add(irows[i]);
        }
		return al;
		
		/*String csaleid = getHeadItem("csaleid").getValue();
		Hashtable ht = new Hashtable();

		if (csaleid != null) {
			SaleOrderVO saleorder = uipanel.vocache.getSaleOrderVO(csaleid);
			if (saleorder != null) {
				SaleorderBVO[] bvos = saleorder.getBodyVOs();
				if (bvos != null && bvos.length > 0) {
					for (int i = 0, iLen = bvos.length; i < iLen; i++) {
						ht.put(bvos[i].getPrimaryKey(), bvos[i]);
					}
				}
			}
		}

		int k = 0;
		String sPk = null;
		String sInvid = null, sOldInvid = null;
		SaleorderBVO bvo = null;

		for (k = 0; k < getRowCount(); k++) {
			if (getBillModel().getRowState(k) == BillModel.ADD)
				al.add(new Integer(k));
			else if (getBillModel().getRowState(k) == BillModel.MODIFICATION) {
				sPk = (String) getBodyValueAt(k, "corder_bid");
				if (sPk == null) {
					sInvid = (String) getBodyValueAt(k, "cinventoryid");
					if (sInvid != null)
						al.add(new Integer(k));
				} else {
					bvo = (SaleorderBVO) ht.get(sPk);
					if (bvo == null) {
						sInvid = (String) getBodyValueAt(k, "cinventoryid");
						if (sInvid != null)
							al.add(new Integer(k));
					} else {
						sInvid = (String) getBodyValueAt(k, "cinventoryid");
						sOldInvid = bvo.getCinventoryid();
						if (sInvid != null && !sInvid.equals(sOldInvid)) {
							al.add(new Integer(k));
						}
					}
				}
			}
		}

		return al;*/
	}

	public void freshBodyLargess(int iChgtype) {
		// 删除原行所属的赠品存货
		if (iChgtype != 3) {
			int irowcount = getRowCount();
			int[] iallrow = new int[irowcount];
			for (int i = 0; i < irowcount; i++)
				iallrow[i] = i;
			int[] inewdelline = setBlargebindLineWhenDelLine(iallrow,1);
			if (inewdelline != null && inewdelline.length > 0)
				uipanel.onDelLine(inewdelline);
		}
		// 重新设置表体买赠
		String sPk = null;
		for (int i = getRowCount() - 1; i >= 0; i--) {
			sPk = (String) getBodyValueAt(i, "cinventoryid");
			if (sPk != null)
				afterInventoryMutiEdit(i, new String[] { sPk }, false, false,
						null, false, iChgtype);
		}

	}

	/**
	 * type=1 买赠,type=2 捆绑
	 * 获取当前行附带的赠品行或捆绑行
	 */
	public int[] setBlargebindLineWhenDelLine(int[] aryRows , int type) {

		if (aryRows == null || aryRows.length == 0)
			return null;

		String sRow = null;
		Vector vt = new Vector();
		for (int i = 0; i < aryRows.length; i++) {
			sRow = (String) getBodyValueAt(aryRows[i], "crowno");
			if (sRow != null && sRow.trim().length() > 0)
				vt.add(sRow);
		}

		UFBoolean bLargess_bind = null;
		Vector vtnew = new Vector();
		
		//赠品行或捆绑行标志
		String flag = (type==1) ? "blargessflag" : "bbindflag" ;
		
		for (int i = 0; i < getRowCount(); i++) {
			// 赠品或捆绑所属行
			sRow = (String) getBodyValueAt(i, "clargessrowno");
			bLargess_bind = new UFBoolean(
					getBodyValueAt(i, flag) == null ? "false"
							: getBodyValueAt(i, flag).toString());
			if (bLargess_bind != null && bLargess_bind.booleanValue() && sRow != null
					&& vt.contains(sRow)) {
				sRow = (String) getBodyValueAt(i, "crowno");
				// 如果此赠品行或捆绑不包含在被删除行之内
				if (sRow != null && !vt.contains(sRow)) {
					vtnew.add(new Integer(i));
				}
			}
		}

		int[] inewdelline = null;
		if (vtnew.size() > 0) {
			inewdelline = new int[vtnew.size()];
			Integer[] iitmp = new Integer[vtnew.size()];
			vtnew.copyInto(iitmp);
			for (int i = 0; i < iitmp.length; i++) {
				inewdelline[i] = iitmp[i].intValue();
			}
		}

		return inewdelline;

	}

	/**
	 * 修改表体行状态。 创建日期：(2001-11-26 9:30:07)
	 * 
	 * @param row
	 *            int
	 */
	public void setBodyRowState(int row) {
		if (getBillModel().getRowState(row) == BillModel.NORMAL)
			getBillModel().setRowState(row, BillModel.MODIFICATION);
	}

	/**
	 * 编辑表头价税合计时使用
	 * 
	 * @param row
	 * @param key
	 * @param iaPrior
	 */
	public void calculateforHeadSummny(int row, String key, Integer iaPrior) {
		/** v5.3单价金额算法变更 */
		// System.out.println("calculateforHeadSummny----RelationsCal.calculate：#"+row+"#"+key+"#-->调用开始！");
		RelationsCal.calculate(row, this.oldValue, this, SaleorderBVO.getCalculatePara(key, iaPrior, uipanel.SA_02,
				uipanel.SO40), key, SaleorderBVO.getKeys(), SaleorderBVO.getField(), SaleorderBVO.class.getName(),
				SaleorderHVO.class.getName(), null);

		// System.out.println("calculateforHeadSummny----RelationsCal.calculate：#"+row+"#"+key+"#-->调用结束！");
		/** v5.3单价金额算法变更 */
	}
	
	/**
	 * 计算。 创建日期：(2001-11-23 16:55:22)
	 */
	public void calculateNumber(int row, String key) {

		// 编辑辅单位，报价计量单位，以换算率为变化点进入算法
		if (key.equals("cpackunitname") || key.equals("cpackunitid"))
			key = "scalefactor";
		else if (key.equals("cquoteunit"))
			key = "nqtscalefactor";

		RelationsCal.calculate(row, this.oldValue, this, SaleorderBVO.getCalculatePara(key, null, uipanel.SA_02,
				uipanel.SO40), key, SaleorderBVO.getKeys(), SaleorderBVO.getField(), SaleorderBVO.class.getName(),
				SaleorderHVO.class.getName(), null);
	}
	
	/**
	 * 获得来源单据类型。
	 * 
	 * 创建日期：(2001-11-16 13:24:23)
	 * 
	 * @return java.lang.String
	 * 
	 */
	private String getSouceBillType() {
		String creceipttype = null;
		if (getRowCount() > 0) {
			creceipttype = (String) getBodyValueAt(0, "creceipttype");
		}

		if (creceipttype == null || creceipttype.trim().equals(""))
			creceipttype = "NO";

		return creceipttype;
	}

	/**
	 * 获得来源单据类型。
	 * 
	 * 创建日期：(2001-11-16 13:24:23)
	 * 
	 * @return java.lang.String
	 * 
	 */
	private String getSouceBillType(int irow) {
		String creceipttype = null;
		if (getRowCount() > irow) {
			creceipttype = (String) getBodyValueAt(irow, "creceipttype");
		}
		if (creceipttype == null || creceipttype.trim().equals(""))
			creceipttype = "NO";
		return creceipttype;
	}

	/**
	 * 客商编辑后事件处理。 创建日期：(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	private void afterCustomerEdit(BillEditEvent e) {
		// 部门
		String cdeptid_old = getBillCardTools().getHeadStringValue("cdeptid");
		
		// 整单扣率
		UFDouble ndiscountrate_old = getBillCardTools().getHeadUFDoubleValue(
				"ndiscountrate");
		// 发运方式
		String ctransmodeid_old = getBillCardTools().getHeadStringValue(
				"ctransmodeid");
		// 默认交易币种
		String ccurrencytypeid_old = getBillCardTools().getHeadStringValue(
				"ccurrencytypeid");
		// 收付款协议
		String ctermprotocolid_old = getBillCardTools().getHeadStringValue(
				"ctermprotocolid");
		// 库存组织
		String ccalbodyid_old = getBillCardTools().getHeadStringValue(
				"ccalbodyid");
		// 销售组织
		String csalecorpid_old = getBillCardTools().getHeadStringValue(
				"csalecorpid");
		String creceiptcorpid_old = getBillCardTools().getHeadStringValue(
				"creceiptcorpid");
		String cwarehouseid_old = getBillCardTools().getHeadStringValue(
				"cwarehouseid");

		// //////////////////////////////////////////////////////////////////////////
		ArrayList<String> formulas = new ArrayList();
		// 开票单位
		formulas
				.add("creceiptcorpid->getColValue(bd_cumandoc,pk_cusmandoc2,pk_cumandoc,ccustomerid)");
		// 收货单位
		formulas
				.add("creceiptcustomerid->getColValue(bd_cumandoc,pk_cusmandoc3,pk_cumandoc,ccustomerid)");
		// 部门
		formulas
				.add("cdeptid->getColValue(bd_cumandoc,pk_respdept1,pk_cumandoc,ccustomerid)");
		// 业务员
		formulas
				.add("cemployeeid->getColValue(bd_cumandoc,pk_resppsn1,pk_cumandoc,ccustomerid)");
		// 整单扣率
		formulas
				.add("ndiscountrate->getColValue(bd_cumandoc,discountrate,pk_cumandoc,ccustomerid)");
		// 发运方式
		formulas
				.add("ctransmodeid->getColValue(bd_cumandoc,pk_sendtype,pk_cumandoc,ccustomerid)");
		// 默认交易币种
		formulas
				.add("ccurrencytypeid->getColValue(bd_cumandoc,pk_currtype1,pk_cumandoc,ccustomerid)");
		// 收付款协议
		formulas
				.add("ctermprotocolid->getColValue(bd_cumandoc,pk_payterm,pk_cumandoc,ccustomerid)");
		// 库存组织
		formulas
				.add("ccalbodyid->getColValue(bd_cumandoc,pk_calbody,pk_cumandoc,ccustomerid)");
		// 销售组织
		formulas
				.add("csalecorpid->getColValue(bd_cumandoc,pk_salestru,pk_cumandoc,ccustomerid)");
		
		// 客户基本档案
		formulas
				.add("ccustbasid->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,ccustomerid)");
		// 散户标志
		formulas
				.add("bfreecustflag->getColValue(bd_cubasdoc,freecustflag,pk_cubasdoc,ccustbasid)");
		// 客户类ID
		formulas
				.add("careaclid->getColValue(bd_cubasdoc,pk_areacl,pk_cubasdoc,ccustbasid)");
		// 客户类编码
		formulas
				.add("careaclcode->getColValue(bd_areacl,areaclcode,pk_areacl,careaclid)");
		formulas
				.add("custshortname->getColValue(bd_cubasdoc,custshortname,pk_cubasdoc,ccustbasid)");
		// 预收款比例
		formulas
				.add("npreceiverate->getColValue(bd_cumandoc,prepaidratio,pk_cumandoc,ccustomerid)");
		//仓库
		formulas
		    .add("cwarehouseid->getColValue(bd_cumandoc,pk_stordoc2,pk_cumandoc,ccustomerid)");

		getBillData().execHeadFormulas(formulas.toArray(new String[] {}));

		// 部门
		String cdeptid = getBillCardTools().getHeadStringValue("cdeptid");
		if ((cdeptid == null || cdeptid.trim().length() <= 0)
				&& (cdeptid_old != null && cdeptid_old.trim().length() > 0)) {
			cdeptid = cdeptid_old;
			setHeadItem("cdeptid", cdeptid_old);
		}

		// 折扣
		String sdiscountrate = getHeadItem("ndiscountrate").getValue();

		// 整单扣率
		// 默认100
		UFDouble ndiscountrate = null;
		if (sdiscountrate == null || sdiscountrate.trim().length() == 0) {
			if (ndiscountrate_old == null) {
				ndiscountrate = new UFDouble(100);
				setHeadItem("ndiscountrate", ndiscountrate);

			} else {
				setHeadItem("ndiscountrate", ndiscountrate_old);
				ndiscountrate = ndiscountrate_old;
			}
		} else {
			ndiscountrate = new UFDouble(sdiscountrate);
		}

		// 发运方式
		String stemp = getBillCardTools().getHeadStringValue("ctransmodeid");
		if (stemp == null || stemp.trim().length() <= 0)
			setHeadItem("ctransmodeid", ctransmodeid_old);

		// 默认交易币种
		String ccurrencytypeid = getBillCardTools().getHeadStringValue(
				"ccurrencytypeid");
		if ((ccurrencytypeid == null || ccurrencytypeid.trim().length() <= 0)
				&& (ccurrencytypeid_old != null && ccurrencytypeid_old.trim()
						.length() > 0)) {
			ccurrencytypeid = ccurrencytypeid_old;
			setHeadItem("ccurrencytypeid", ccurrencytypeid_old);
		}

		// 收付款协议
		stemp = getBillCardTools().getHeadStringValue("ctermprotocolid");
		if (stemp == null || stemp.trim().length() <= 0)
			setHeadItem("ctermprotocolid", ctermprotocolid_old);

		String ccalbodyid = null;
		// 库存组织,如果来源为借出单，则库存组织不变
		if (getSouceBillType().equals("4H") || getSouceBillType().equals("42")) {
			setHeadItem("ccalbodyid", ccalbodyid_old);
			ccalbodyid = getBillCardTools().getHeadStringValue("ccalbodyid");
		} else {
			ccalbodyid = getBillCardTools().getHeadStringValue("ccalbodyid");
			if ((ccalbodyid == null || ccalbodyid.trim().length() <= 0)
					&& (ccalbodyid_old != null && ccalbodyid_old.trim()
							.length() > 0)) {
				ccalbodyid = ccalbodyid_old;
				setHeadItem("ccalbodyid", ccalbodyid_old);
			}
		}

		// 销售组织
		stemp = getBillCardTools().getHeadStringValue("csalecorpid");
		if (stemp == null || stemp.trim().length() <= 0)
			setHeadItem("csalecorpid", csalecorpid_old);

		// 如未设置收货单位，则取客户
		if ((getHeadItem("creceiptcustomerid") != null)
				&& (getHeadItem("creceiptcustomerid").getValue() == null || getHeadItem(
						"creceiptcustomerid").getValue().length() <= 0)) {
			getHeadItem("creceiptcustomerid").setValue(
					getHeadItem("ccustomerid").getValue());
		}
		// 如未设置开票单位，则取客户
		if (getHeadItem("creceiptcorpid").getValue() == null
				|| getHeadItem("creceiptcorpid").getValue().length() <= 0) {
			getHeadItem("creceiptcorpid").setValue(
					getHeadItem("ccustomerid").getValue());
		}

		// 收货地址
		UIRefPane vreceiveaddress = (UIRefPane) getHeadItem("vreceiveaddress")
				.getComponent();
		vreceiveaddress.setAutoCheck(false);

		// 收货地址参照
		if (getHeadItem("creceiptcustomerid") != null)
			((CustAddrRefModel) vreceiveaddress.getRefModel())
					.setCustId(getHeadItem("creceiptcustomerid").getValue());
		// // 基础档案ID
		// String formula =
		// "getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,creceiptcustomerid)";
		// String pk_cubasdoc = (String) execHeadFormula(formula);
		//
		// String strvreceiveaddress = BillTools.getColValue2("bd_custaddr",
		// "pk_custaddr", "defaddrflag", "Y", "pk_cubasdoc", pk_cubasdoc);
		// vreceiveaddress.setPK(strvreceiveaddress);

		// 散户
		UFBoolean bfreecustflag = getBillCardTools().getHeadUFBooleanValue(
				"bfreecustflag");
		if (bfreecustflag == null || !bfreecustflag.booleanValue()) {
			getHeadItem("cfreecustid").setEnabled(false);
		} else {
			getHeadItem("cfreecustid").setEnabled(true);
		}
		// 清除散户信息
		getHeadItem("cfreecustid").setValue(null);
		// 设置默认值
		try {
			UIRefPane refccurrencytypeid = (UIRefPane) getHeadItem(
					"ccurrencytypeid").getComponent();
			if (refccurrencytypeid.getRefPK() == null) {
				refccurrencytypeid.setPK(CurrParamQuery.getInstance().getLocalCurrPK(getCorp()));
				ccurrencytypeid = getBillCardTools().getHeadStringValue(
						"ccurrencytypeid");
			}
		} catch (java.lang.Exception e1) {
			SCMEnv.out(e1.getMessage());
		}

		// 部门变换
		if (cdeptid != null
				&& (cdeptid_old == null || !cdeptid_old.equals(cdeptid))) {
			// 人员参照和部门参照匹配
			afterDeptEdit(null);
		}

		// 整单折扣
		if (ndiscountrate != null
				&& (ndiscountrate_old == null || ndiscountrate_old
						.compareTo(ndiscountrate) != 0)) {
			afterDiscountrateEdit(null);
		}

		// 收货单位
		String creceiptcorpid = getBillCardTools().getHeadStringValue(
				"creceiptcorpid");
		if ((creceiptcorpid != null && (creceiptcorpid_old == null || !creceiptcorpid_old
				.equals(creceiptcorpid)))
				|| (creceiptcorpid == null && creceiptcorpid_old != null)) {
			afterCreceiptcorpEdit(null);
		}

		afterCurrencyEdit(e);

		if (!getSouceBillType().equals("4H") && !getSouceBillType().equals("42")) {
			// 设置表体的缺省的发货公司及库存组织
			getBillCardTools().setSendCalBodyAndWare(0, getRowCount());

			// 设置发货公司后续事件 20061127
			for (int i = 0, iLen = getRowCount(); i < iLen; i++) {
				afterCconsignCorpEdit(i);
			}

			if (ccalbodyid != null && (ccalbodyid_old == null || !ccalbodyid_old.equals(ccalbodyid))) {
				// 库存组织
				afterCcalbodyidEdit(null);
			}
			String cwarehouseid = getBillCardTools().getHeadStringValue("cwarehouseid");
			if ((cwarehouseid != null && (cwarehouseid_old == null || !cwarehouseid_old.equals(cwarehouseid)))
					|| (cwarehouseid == null && cwarehouseid_old != null)) {
				// 仓库
				afterCwarehouseidEdit(null);
			}
		}
		
		

		// 改变客户后，去掉合同的关联，清空相关信息 jindongmei zhongwei
		if (getRowCount() > 0) {
			String creceipttype = (String) getBodyValueAt(0, "creceipttype");

			if (creceipttype != null && "Z4".equals(creceipttype)) {
				//清空 合同号、合同ID、合同名称、合同存货类、来源单据类型、来源单据ID、来源单据行ID
				for (int i = 0, len = getRowCount(); i < len; i++) {
					setBodyValueAt(null, i, "ct_code");
					setBodyValueAt(null, i, "ct_manageid");
					setBodyValueAt(null, i, "ct_name");
					setBodyValueAt(null, i, "ctinvclassid");
					setBodyValueAt(null, i, "creceipttype");
					setBodyValueAt(null, i, "csourcebillbodyid");
					setBodyValueAt(null, i, "csourcebillid");
				}
			}
		}
		
		showCustManArInfo();
		
        //设置直运仓
		for (int i=0;i<getRowCount();i++)
			setDerictTransSendWareHouse(i);
	}

	/**
	 * 显示客商管理档案应收相关信息。 创建日期：(2003-12-26 12:52:43)
	 */
	public void showCustManArInfo() {
		try {
			BillItem[] bis = new BillItem[5];
			// 财务应收accawmny
			bis[0] = getTailItem("accawmny");
			// 业务应收busawmny
			bis[1] = getTailItem("busawmny");
			// 订单应收ordawmny
			bis[2] = getTailItem("ordawmny");
			// 信用额度creditmny
			bis[3] = getTailItem("creditmny");
			// 信用保证金creditmoney
			bis[4] = getTailItem("creditmoney");

			boolean isshow = false;
			// 设置信用金额精度
			int digit = getBillData().getBodyItem("noriginalcursummny")
					.getDecimalDigits();
			for (int i = 0; i < bis.length; i++) {
				if (bis[i] != null) {
					bis[i].setDecimalDigits(digit);
					if (bis[i].isShow()) {
						isshow = true;
					}
				}
			}

			if (!isshow)
				return;

			String ccustomerid = getHeadItem("ccustomerid").getValue();
			if (ccustomerid == null || ccustomerid.trim().length() <= 0) {
				for (int i = 0; i < bis.length; i++) {
					if (bis[i] != null) {
						bis[i].setValue(null);
					}
				}
				return;
			}
			//V5.5中降缓存项目进行修改 周长胜 2008-07-01
			String accawmnySql="select pk_cumandoc,accawmny from bd_cumandoc where pk_cumandoc='"+ccustomerid+"'";
			String busawmnySql="select pk_cumandoc,busawmny from bd_cumandoc where pk_cumandoc='"+ccustomerid+"'";
			String ordawmnySql="select pk_cumandoc,ordawmny from bd_cumandoc where pk_cumandoc='"+ccustomerid+"'";
			String creditmoneySql="select pk_cumandoc,creditmoney from bd_cumandoc where pk_cumandoc='"+ccustomerid+"'";
			setHeadItem("accawmny", SaleOrderBO_Client.queryBDDatas(accawmnySql).get(ccustomerid).toString());
			setHeadItem("busawmny", SaleOrderBO_Client.queryBDDatas(busawmnySql).get(ccustomerid).toString());
			setHeadItem("ordawmny", SaleOrderBO_Client.queryBDDatas(ordawmnySql).get(ccustomerid).toString());
			setHeadItem("creditmoney", SaleOrderBO_Client.queryBDDatas(creditmoneySql).get(ccustomerid).toString());
			String formulas[] = {
					//"accawmny->getColValue(bd_cumandoc,accawmny,pk_cumandoc,ccustomerid)",
					//"busawmny->getColValue(bd_cumandoc,busawmny,pk_cumandoc,ccustomerid)",
					//"ordawmny->getColValue(bd_cumandoc,ordawmny,pk_cumandoc,ccustomerid)",
					//"creditmoney->getColValue(bd_cumandoc,creditmoney,pk_cumandoc,ccustomerid)",
					"creditmny->getColValue(bd_cumandoc,creditmny,pk_cumandoc,ccustomerid)"};
			
			execHeadFormulas(formulas);
			

		} catch (Exception e) {
			// e.printStackTrace();
			SCMEnv.out("显示客商管理档案应收相关信息失败");
		}
	}

	/**
	 * 部门编辑后事件处理。 创建日期：(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	private void afterDeptEdit(BillEditEvent e) {
		UIRefPane cemployeeid = (UIRefPane) getHeadItem("cemployeeid")
				.getComponent();

		String sRefInitWhere = getBillCardTools().getHeadRefInitWhere(
				"cemployeeid");

		if (sRefInitWhere == null || sRefInitWhere.trim().length() <= 0)
			sRefInitWhere = " 1=1 ";

		if ("新增".equals(uipanel.strState))/*-=notranslate=-*/
			sRefInitWhere += " and bd_deptdoc.canceled ='N' ";

		cemployeeid.getRefModel().setWherePart(sRefInitWhere);
		
		// 部门发生了变化，应该清空业务员字段
		getHeadItem("cemployeeid").setValue(null);
	}

	/**
	 * 整单折扣率编辑后事件处理。 将表头的整单折扣带入表体行的整单折扣 创建日期：(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public void afterDiscountrateEdit(BillEditEvent e) {
		Object oDiscountrate = null;
		//非编辑表头整单折扣入口
		if (e == null){
			oDiscountrate = getHeadItem("ndiscountrate").getValue();
			for (int i = 0; i < getRowCount(); i++) {
				setBodyValueAt(oDiscountrate, i, "ndiscountrate");
				calculateNumber(i, "ndiscountrate");
				setBodyRowState(i);
			}
		}
		//编辑表头整单折扣入口
		else{
			UFDouble newDiscountrate = e.getValue() == null ? new UFDouble(100.) : new UFDouble(
					e.getValue().toString());//新整单折扣值
			Object oldDiscount = ndiscountrate != null ? ndiscountrate
					: getBillCardTools().getOldsaleordervo().getParentVO()
							.getAttributeValue("ndiscountrate");
			UFDouble oldDiscountrate = oldDiscount == null ? new UFDouble(100.)
					: new UFDouble(oldDiscount.toString());// 原整单折扣值
			
			//编辑表头折扣后的新整单价税合计(或无税金额)
//			UFDouble newHeadmny = null;
			UFDouble oldHeadmny = null;
			String sItemKey = null;
			//含税优先
			if (uipanel.SA_02.booleanValue()){
				sItemKey = "noriginalcursummny";
				oldHeadmny = new UFDouble(getHeadItem("nheadsummny").getValueObject().toString());
			}
			//无税优先
			else{
				sItemKey = "noriginalcurmny";
				oldHeadmny = getTotalValue(sItemKey);
			}
			afterDiscountrateEditTemplet(sItemKey,oldHeadmny,
					oldDiscountrate,newDiscountrate);
			
			ndiscountrate = e.getValue();//缓存上一次的表头整单折扣值
		}
	}

	/**
	 * 编辑表头整单折扣后，统一计算方法
	 */
	private void afterDiscountrateEditTemplet(String sItemKey,UFDouble oldHeadmny,
			UFDouble oldDiscountrate,UFDouble newDiscountrate){
		// 新整单价税合计 = 原整单价税合计 / 原整单折扣 * 新整单折扣
		UFDouble newHeadmny = (oldHeadmny.div(oldDiscountrate.div(100.))).multiply(newDiscountrate.div(100.));
		
		//各行价税合计比率
		UFDouble[] summyRate = getRowMnyRate(sItemKey,oldHeadmny);
		
		//按各行原有价税合计比率计算各行新价税合计
		for (int row = 0,len = this.getRowCount(); row < len; row++ ){
			getBillCardTools().setBodyValue(
					newHeadmny.multiply(summyRate[row]), row,
					sItemKey);
			setBodyValueAt(newDiscountrate, row, "ndiscountrate");
			calculateNumber(row, sItemKey);
		}
		
		/////////////////挤最后一行尾差//////////////////////////////		
		// 找到最后一行（非赠品行、非缺货行）
		int iLastrow = getRowCount() - 1;
		Object blargessflag, boosflag;
		for (int i = iLastrow; i >= 0; i--) {
			if (getBodyValueAt(i, "cinventoryid") != null
					&& getBodyValueAt(i, "cinventoryid").toString().length() > 0) {
				blargessflag = getBodyValueAt(i, "blargessflag");
				boosflag = getBodyValueAt(i, "boosflag");
				if (blargessflag != null && (Boolean) blargessflag)
					iLastrow--;
				else if (boosflag != null && (Boolean) boosflag)
					iLastrow--;
				else
					break;
			} else
				iLastrow--;
		}

		/** 如果没有找到符合要求的行就直接退出，表头和表体的价税合计在保存时检验* */
		if (iLastrow < 0)
			return;
		
		// 得到新的各行计算后价税合计累加值
		UFDouble udLast = getTotalValue(sItemKey);
		//尾差值
		UFDouble endRowSummny_w = newHeadmny.sub(udLast);
		if (udLast == null || udLast.doubleValue() != newHeadmny.doubleValue()) {
			UFDouble udNow = (UFDouble) getBodyValueAt(iLastrow,sItemKey);
			udNow = (udNow == null ? new UFDouble(0) : udNow).add(endRowSummny_w);
			setBodyValueAt(udNow, iLastrow, sItemKey);
			calculateNumber(iLastrow, sItemKey);
		}
        /////////////////挤最后一行尾差//////////////////////////////
	}
	
	
	/**
	 * @return 各行价税合计或无税金额占总值的比率
	 */
	public UFDouble[] getRowMnyRate(String key,UFDouble oldmny){
		final UFDouble zero = new UFDouble(0.);
		UFDouble[] mnyRate = new UFDouble[this.getRowCount()];
		oldmny = (oldmny==null?zero:oldmny);
		for (int row = 0,len = this.getRowCount(); row < len; row++ ){
			UFDouble mny = getBillCardTools().getBodyUFDoubleValue(row, key);
			mnyRate[row] = (mny==null?zero:mny).div(oldmny);
		}
		return mnyRate;
	}
	
	/**
	 * 整单折扣率编辑后事件处理。
	 * 
	 * 处理过程仿照afterDiscountrateEdit(BillEditEvent e)
	 * 
	 * 防止已录入行的价格变化
	 * 
	 */
	public void afterDiscountrateEdit(int istartrow, int iendrow) {
		Object oDiscountrate = getHeadItem("ndiscountrate").getValue();

		for (int i = istartrow; i < iendrow; i++) {
			setBodyValueAt(oDiscountrate, i, "ndiscountrate");
			calculateNumber(i, "ndiscountrate");
			setBodyRowState(i);

		}
	}

	/**
	 * 收货单位编辑后事件处理。 创建日期：(2001-6-23 13:42:53)
	 */
	public void afterCreceiptcorpEdit(BillEditEvent e) {
		// 收货地址
		UIRefPane vreceiveaddress = (UIRefPane) getHeadItem("vreceiveaddress")
				.getComponent();
		vreceiveaddress.setAutoCheck(false);
		// vreceiveaddress.setReturnCode(false);
		// 收货地址参照
		String creceiptcustomerid = null;
		if (getHeadItem("creceiptcustomerid") != null)
			creceiptcustomerid = getHeadItem("creceiptcustomerid").getValue();

		((CustAddrRefModel) vreceiveaddress.getRefModel())
				.setCustId(creceiptcustomerid);
		// 基础档案ID
		String formula = "getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,creceiptcustomerid)";
		String pk_cubasdoc = (String) execHeadFormula(formula);

		SOToolVO toolsvo = new SOToolVO();

		toolsvo.setAttributeValue("pk_cumandoc", creceiptcustomerid);

		toolsvo.setAttributeValue("pk_cubasdoc", pk_cubasdoc);

		toolsvo.setAttributeValue("pk_custaddr", "");

		toolsvo.setAttributeValue("crecaddrnode", "");

		toolsvo.setAttributeValue("crecaddrnodename", "");

		String pk_custaddr = BillTools.getColValue2("bd_custaddr",
				"pk_custaddr", "pk_cubasdoc", pk_cubasdoc, "defaddrflag", "Y");

		toolsvo.setAttributeValue("pk_custaddr", pk_custaddr);

		String[] formulas = {

				"crecaddrnode->getColValue(bd_custaddr,pk_address,pk_custaddr,pk_custaddr)",

				"crecaddrnodename->getColValue(bd_address,addrname,pk_address,crecaddrnode)"

		};

		getBillCardTools().execFormulas(formulas, new SOToolVO[] { toolsvo });

		vreceiveaddress.setPK(pk_custaddr);
		// 表体收货地址携带
		afterVreceiveaddressEdit(null);
		UIRefPane refCreceiptcorpid = null;
		if (getHeadItem("creceiptcustomerid") != null)
			refCreceiptcorpid = (UIRefPane) getHeadItem("creceiptcustomerid")
					.getComponent();
		if (refCreceiptcorpid != null && refCreceiptcorpid.getRefPK() != null) {
			for (int i = 0; i < getBillModel().getRowCount(); i++) {
				setBodyValueAt(refCreceiptcorpid.getRefPK(), i,
						"creceiptcorpid");
				setBodyValueAt(refCreceiptcorpid.getRefName(), i,
						"creceiptcorpname");

				setBodyValueAt(toolsvo.getAttributeValue("crecaddrnode"), i,
						"crecaddrnode");

				setBodyValueAt(toolsvo.getAttributeValue("crecaddrnodename"),
						i, "crecaddrnodename");

				if (getBillModel().getRowState(i) == BillModel.NORMAL)
					getBillModel().setRowState(i, BillModel.MODIFICATION);

			}
		} else {
			for (int i = 0; i < getBillModel().getRowCount(); i++) {
				setBodyValueAt(null, i, "creceiptcorpid");
				setBodyValueAt(null, i, "creceiptcorpname");

				setBodyValueAt(toolsvo.getAttributeValue("crecaddrnode"), i,
						"crecaddrnode");

				setBodyValueAt(toolsvo.getAttributeValue("crecaddrnodename"),
						i, "crecaddrnodename");

				if (getBillModel().getRowState(i) == BillModel.NORMAL)
					getBillModel().setRowState(i, BillModel.MODIFICATION);
			}
		}

	}

	/**
	 * 币种编辑后事件处理。
	 * 
	 * 字段在表头，表体的此字段现无法显示和配置
	 * 
	 * 故事件触发后，会进行表体行金额、价格项的清空
	 * 
	 * 创建日期：(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public void afterCurrencyEdit(BillEditEvent event) {

		// 币种参照
		UIRefPane ccurrencytypeid = (UIRefPane) getHeadItem("ccurrencytypeid").getComponent();
		setHeadItem("nexchangeotobrate", null);

		//表头汇率设置//////////////////////////////////////////////////
		int iDecimalDigits = 2;
		try {
			// 金额精度取币种小数位
			setPanelByCurrency(ccurrencytypeid.getRefPK());

			iDecimalDigits = uipanel.getSOCurrencyRateUtil()
					.getExchangeRateDigits(ccurrencytypeid.getRefPK());
			// 设置表头汇率精度
			getHeadItem("nexchangeotobrate").setDecimalDigits(iDecimalDigits);

			// 如果单主币核算
			UFDouble[] ult = getBillCardTools().getExchangeRate(
					ccurrencytypeid.getRefPK(),
					getHeadItem("dbilldate").getValue(),
					ClientEnvironment.getInstance().getCorporation()
							.getPk_corp());
			UFDouble dCurr0 = ult[0];
			setHeadItem("nexchangeotobrate", dCurr0);

			// 2001年11月24日 刘杰修改
			// 如果币种等于本位币种则折本汇率不能修改，而且应该等于1
			if (CurrParamQuery.getInstance().isLocalCurrType(getCorp(),
					ccurrencytypeid.getRefPK())) {
				getHeadItem("nexchangeotobrate").setEnabled(false);
			} else
				getHeadItem("nexchangeotobrate").setEnabled(true);			
			
			//表体汇率设置//////////////////////////////////////////////////
			for (int i = 0; i < getRowCount(); i++) {
				// 表体币种
				setBodyValueAt(ccurrencytypeid.getRefPK(), i, "ccurrencytypeid");

				setBodyValueAt(ccurrencytypeid.getRefName(), i,
						"ccurrencytypename");

				// 表体汇率
				setBodyValueAt(getHeadItem("nexchangeotobrate").getValue(), i,
						"nexchangeotobrate");

				//表体汇率精度
				//设置表体汇率精度
				nc.ui.so.so001.panel.bom.BillTools
						.setBodyValueAtByDigit(getBillModel(), 
								SmartVODataUtils.getUFDouble(getHeadItem("nexchangeotobrate").getValueObject()),
								i, "nexchangeotobrate", iDecimalDigits);
				
				// 先清空表体价格、金额项，在后面的处理中再进行询价（如果参数为询价）
				getBillCardTools().clearRowData(i, SOBillCardTools.pricekeys);
				
				// 重新设置折扣默认值100%（因为后面要重新询价）
				for (int row = 0 ; row < getRowCount(); row++ )
					getBillCardTools().setBodyValue(100, row, "nitemdiscountrate");
				
				// 置行状态为修改状态
				setBodyRowState(i);
			}
			// 重新设置表体买赠

		} catch (java.lang.Exception e1) {
			SCMEnv.out("获得汇率失败！");
		}
		
		
	}

	/**
	 * 根据币种设置汇率和金额小数位数。 创建日期：(2002-6-24 11:15:06)
	 */
	public void setPanelByCurrency(String ccurrencytypeid) {
		try {

			SOBillCardTools.setCardPanelByCurrency(this, ccurrencytypeid,
					ClientEnvironment.getInstance().getCorporation()
							.getPk_corp(), uipanel.BD302, 
							"nexchangeotobrate",
					new String[] { "noriginalcurtaxmny", "noriginalcurmny",
							"noriginalcursummny", "noriginalcurdiscountmny" },
					new String[] { "ntaxmny", "nmny", "nsummny",
							"ndiscountmny", "ntalbalancemny" },
					new String[] { "nreceiptcathmny", "npreceivemny",
							"nheadsummny" }, null);

			if (ccurrencytypeid == null || ccurrencytypeid.length() == 0)
				return;

			CurrtypeQuery currquery = CurrtypeQuery.getInstance();
			CurrtypeVO currtypevo = null;
			if (ccurrencytypeid != null)
				currtypevo = currquery.getCurrtypeVO(ccurrencytypeid);

			// 金额项精度
			int digit = 4;
			try {
				// v30取业务精度
				digit = currtypevo.getCurrbusidigit() == null ? 4 : currtypevo
						.getCurrbusidigit().intValue(); // currVO.getCurrdigit().intValue();
				SCMEnv.out(digit);
			} catch (Exception ex2) {
				digit = getBodyItem("noriginalcursummny").getDecimalDigits();
			}
			
			// BOM金额项精度
			if (getBillType().equals(SaleBillType.SaleOrder)) {
				uipanel.getBillTreeCardPanel().getBillData().getHeadItem(
						"nsaleprice").setDecimalDigits(digit);

				uipanel.getBillTreeCardPanel().getBillData().getBodyItem(
						"nprice").setDecimalDigits(digit);
				String name = uipanel.getBillTreeCardPanel().getBodyItem(
						"nprice").getName();
				if (uipanel.getBillTreeCardPanel().getBodyPanel().hasShowCol(
						name))
					uipanel.getBillTreeCardPanel().getBodyPanel()
							.resetTableCellRenderer(name);
			}

		} catch (Exception e) {
			SCMEnv.out("根据币种设置小数位数失败!");
			// e.printStackTrace();
		}
	}

	/**
	 * 表体发货公司变化，nc240,多公司销售 创建日期：(2004-2-9 14:58:33)
	 */
	private void afterCconsignCorpEdit(int row) {

		//只有最初编辑发货公司、存货、客户时才起作用
		if (this.firstChangeKey!=null
				&& !this.firstChangeKey.equals("cconsigncorp")
				&& !this.firstChangeKey.equals("ccustomerid")
				&& !this.firstChangeKey.equals("cinventorycode")
				&& !this.firstChangeKey.equals("cinventoryid") )
			return;
		
		String pk_corp = getBillCardTools().getHeadStringValue("pk_corp");
		if (pk_corp == null)
			return;
		String cconsigncorp = getBillCardTools().getBodyStringValue(row,"cconsigncorpid");

		// 如果发货公司与销售公司(即登陆公司)相同，则为单公司销售，
		// 发货收货库存组织，收货仓库，是否直运不可编辑
		if (cconsigncorp == null || pk_corp.equals(cconsigncorp)) {
			// 清空相关字段
			getBillCardTools().clearBodyValue(
					new String[] { //"cbodywarehousename", "cbodywarehouseid",
							"cadvisecalbodyid", "cadvisecalbody",
							"creccalbody", "creccalbodyid", "crecwarehouse",
							"crecwareid", "bdericttrans" }, row);
			// 发货库存组织
			getBillCardTools().setBodyValueByHead("ccalbodyid", row);
			// 发货仓库
			getBillCardTools().setBodyValueByHead("cwarehouseid", row);

		} 
		//不是本公司，并且模版设定上述字段锁定，则从上一行携带是否直运、收货库存组织、收货仓库
		else {
			getBillCardTools().clearBodyValue(
					new String[] { "cbodywarehousename", "cbodywarehouseid",
							"cadvisecalbodyid", "cadvisecalbody","crecwareid", "crecwarehouse"}, row);
			
			((UIRefPane) getBodyItem("cadvisecalbody").getComponent())
					.getRefModel().setSelectedData(null);

			getBillCardTools().setBodyValue(new UFBoolean(false), row,
					"boosflag");
			getBillCardTools().setBodyValue(new UFBoolean(false), row,
					"bsupplyflag");
		}
		getBillCardTools().setBodyInventory1(row, 1);

		((UIRefPane) getBodyItem("cadvisecalbody").getComponent())
				.getUITextField().setText(null);
		((UIRefPane) getBodyItem("cbodywarehousename").getComponent())
				.getRefModel().setSelectedData(null);

		ctlUIOnCconsignCorpChg(row);
	}

	/**
	 * 表体发货公司改变时，控制界面的变化。
	 * 
	 * 创建日期：(2004-2-9 15:10:14)
	 * 
	 */
	public void ctlUIOnCconsignCorpChg(int row) {
		String pk_corp = getBillCardTools().getHeadStringValue("pk_corp");
		if (pk_corp == null)
			return;
		String cconsigncorp = getBillCardTools().getBodyStringValue(row,
				"cconsigncorpid");

		// 如果发货公司与销售公司(即登陆公司)相同，则为单公司销售，
		// 发货收货库存组织，收货仓库，是否直运不可编辑
		if (cconsigncorp == null || pk_corp.equals(cconsigncorp)) {

            // 清空相关字段
			getBillCardTools().clearBodyValue(
					new String[] { //"cbodywarehousename", "cbodywarehouseid",
							//"cadvisecalbodyid", "cadvisecalbody",
							"creccalbody", "creccalbodyid", "crecwarehouse",
							"crecwareid", "bdericttrans" }, row);
			
			getBillCardTools().setBodyCellsEdit(
					new String[] { "creccalbody", "crecwarehouse",
							"bdericttrans" }, row, false);
			getBillCardTools().setBodyCellsEdit(
					new String[] { "boosflag", "bsupplyflag" }, row, true);
			
			//设置默认发货直运仓
			setDerictTransSendWareHouse(row);

		// 跨公司销售
		} else {
			getBillCardTools().setBodyCellsEdit(
					new String[] { "boosflag", "bsupplyflag" }, row, false);
			getBillCardTools().setBodyCellsEdit(
					new String[] { "creccalbody", "crecwarehouse",
							"bdericttrans" }, row, true);
			
			//模版设定是否直运、收货库存组织、收货仓库字段锁定，则从上一行携带
			setPreRowValueForTransBodyWare(row);
			
			//跨公司必须直运调拨
			getBillCardTools().setBodyValue("Y", row, "bdericttrans");

			//设置收货直运仓
			setDerictTransReceWareHouse(row);
		}
	}
	
	/**
	 * 模版设定是否直运、收货库存组织、收货仓库字段锁定，则从上一行携带
	 * @param row  当前行
	 */
	private void setPreRowValueForTransBodyWare(int row){
		if (row>0){
			Object preRowValue = null;
            //直运调拨是否锁定
			if (getBodyItem("bdericttrans").isLock()){
				//上一行的值
				preRowValue = getBillCardTools().getBodyValue(row-1, "bdericttrans");
				//设置本行的值
				getBillCardTools().setBodyValue(preRowValue, row, "bdericttrans");
			}
			//收货库存组织是否锁定(模板上的配参照的字段)
			if (getBodyItem("creccalbody").isLock()){
				//上一行的值
				preRowValue = getBillCardTools().getBodyValue(row-1, "creccalbody");
				//设置本行的值
				getBillCardTools().setBodyValue(preRowValue, row, "creccalbody");					
			}
			//收货仓库是否锁定(模板上的配参照的字段)
			if (getBodyItem("crecwarehouse").isLock()){
				//上一行的值
				preRowValue = getBillCardTools().getBodyValue(row-1, "crecwarehouse");
				//设置本行的值
				getBillCardTools().setBodyValue(preRowValue, row, "crecwarehouse");					
			}
		}
	}

	/**
	 * 业务员编辑后事件处理。
	 * 
	 * 创建日期：(2001-11-13 10:57:39)
	 * 
	 * @param e
	 *            nc.ui.pub.bill.BillEditEvent
	 * 
	 */
	public void afterEmployeeEdit(BillEditEvent e) {

		// 根据业务员参照置部门参照
		UIRefPane cemployeeid = (UIRefPane) getHeadItem("cemployeeid")
				.getComponent();
		UIRefPane refpane_dept = (UIRefPane) getHeadItem("cdeptid")
				.getComponent();
		if (cemployeeid != null && refpane_dept != null) {
			if (cemployeeid.getRefPK() != null) {
				Object new_pkdeptid = cemployeeid
						.getRefValue("bd_psndoc.pk_deptdoc");

				if (new_pkdeptid != null
						&& new_pkdeptid.toString().trim().length() > 0) {
					String old_pkdeptid = refpane_dept.getRefPK();

					// //校验值
					// //setPK()属于全表查询，没有业务特性；所以，调用方法进行限制
					// //但是，使用后必须放开；以免浏览数据出现错误
					refpane_dept.getRefModel().setMatchPkWithWherePart(true);
					refpane_dept.setPK(new_pkdeptid);
					refpane_dept.getRefModel().setMatchPkWithWherePart(false);

					String new_cdeptname = refpane_dept.getUITextField()
							.getText();
					// //恢复原值
					if (new_cdeptname == null
							|| new_cdeptname.trim().length() <= 0) {
						refpane_dept.setPK(old_pkdeptid);
					}
				}// end update new pk
			}
		}// end if refpane not null

	}

	/**
	 * 自由项编辑后事件处理。 创建日期：(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	private void afterFreeItemEdit(BillEditEvent e) {
		try {
			FreeVO voFree = getFreeItemRefPane().getFreeVO();
			// 将自由项填入表体
			for (int i = 0; i < 5; i++) {
				String fieldname = "vfree" + (i + 1);
				Object o = voFree.getAttributeValue(fieldname);
				setBodyValueAt(o, e.getRow(), fieldname);
			}
		} catch (Exception e2) {
			SCMEnv.out(e2);
			// e2.printStackTrace();
		}
	}

	/**
	 * 返回 FreeItemRefPane1 特性值。
	 * 
	 * @return nc.ui.ic.pub.freeitem.FreeItemRefPane
	 */
	public FreeItemRefPane getFreeItemRefPane() {
		if (ivjFreeItemRefPane == null) {
			try {
				ivjFreeItemRefPane = new FreeItemRefPane();
				ivjFreeItemRefPane.setName("FreeItemRefPane");
				ivjFreeItemRefPane.setLocation(209, 4);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjFreeItemRefPane;
	}

	/**
	 * 自由项编辑后事件处理。 创建日期：(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public void afterFreeItemEditBom(BillEditEvent e) {
		try {
			FreeVO voFree = getFreeItemRefPane().getFreeVO();
			// 将自由项填入表体
			for (int i = 0; i < 5; i++) {
				String fieldname = "vfree" + (i + 1);
				Object o = voFree.getAttributeValue(fieldname);
				uipanel.getBillTreeCardPanel().setBodyValueAt(o, e.getRow(),
						fieldname);
			}
		} catch (Exception e2) {
			SCMEnv.out("将自由项填入表体出错!");
			// e2.printStackTrace();
		}
	}

	/**
	 * 项目编辑后事件处理。 创建日期：(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	private void afterProjectEdit(BillEditEvent e) {
		// 清除数据
		String[] clearCol = { "cprojectphaseid", "cprojectphasename" };
		clearRowData(e.getRow(), clearCol);
	}

	/**
	 * 整单税率编辑后事件处理。 创建日期：(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	private void afterTaxrateBillEdit(BillEditEvent e) {
		for (int i = 0; i < getRowCount(); i++) {
			setBodyValueAt(e.getValue(), i, "ntaxrate");
			setBodyRowState(i);
			calculateNumber(i, "ntaxrate");
		}

	}

	/**
	 * 计量单位编辑后事件处理。 创建日期：(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	private void afterUnitEdit(BillEditEvent e) {
		afterUnitEdit(e.getRow(), e.getKey());
		if (e.getKey().equals("cpackunitname")) {
			setScaleEditableByRow(e.getRow());
		}
	}

	/**
	 * 计量单位编辑后事件处理。
	 * 
	 * @param eRow
	 *            int
	 */
	private void afterUnitEdit(int eRow, String key) {
		String cunitid = (String) getBodyValueAt(eRow, "cunitid");
		if ("cinventorycode".equals(key) || "cpackunitname".equals(key)) {
			String cpackunitid = (String) getBodyValueAt(eRow, "cpackunitid");
			if (cunitid != null && cpackunitid != null) {
				if (cunitid.equals(cpackunitid)) {
					String[] formulas = new String[1];
					// 包装单位名称
					formulas[0] = "cpackunitname->getColValue(bd_measdoc,measname,pk_measdoc,cpackunitid)";
					// 换算率
					execBodyFormulas(eRow, formulas);
					setBodyValueAt(new UFDouble(1), eRow, "scalefactor");
					setBodyValueAt(new UFBoolean(true), eRow, "fixedflag");
				} else {
					String[] formulas = new String[3];
					// 包装单位名称
					formulas[0] = "cpackunitname->getColValue(bd_measdoc,measname,pk_measdoc,cpackunitid)";
					// 换算率
					formulas[1] = "scalefactor->getColValue2(bd_convert,mainmeasrate,pk_invbasdoc,cinvbasdocid,pk_measdoc,cpackunitid)";
					// 是否固定换算率
					formulas[2] = "fixedflag->getColValue2(bd_convert,fixedflag,pk_invbasdoc,cinvbasdocid,pk_measdoc,cpackunitid)";
					execBodyFormulas(eRow, formulas);
				}
			} else {
				// 辅单位为空
				setBodyValueAt(null, eRow, "npacknumber");
				setBodyValueAt(null, eRow, "cpackunitid");
				setBodyValueAt(null, eRow, "cpackunitname");
				setBodyValueAt(null, eRow, "scalefactor");
				setBodyValueAt(null, eRow, "fixedflag");
			}

			InvVO voInv = null;
			if (alInvs != null && alInvs.size() > eRow)
				voInv = (InvVO) alInvs.get(eRow);
			if (voInv != null) {
				voInv.setCastunitid(cpackunitid);
				voInv.setCastunitname((String) getBodyValueAt(eRow,
						"cpackunitname"));
			}
		}
		if ("cinventorycode".equals(key) || "cquoteunit".equals(key)) {
			// 报价单位
			String cquoteunitid = (String) getBodyValueAt(eRow, "cquoteunitid");
			if (cunitid != null && cquoteunitid != null) {
				if (cunitid.equals(cquoteunitid)) {
					String[] formulas = new String[1];
					// 报价单位名称
					formulas[0] = "cquoteunit->getColValue(bd_measdoc,measname,pk_measdoc,cquoteunitid)";
					// 换算率
					execBodyFormulas(eRow, formulas);
					setBodyValueAt(new UFDouble(1), eRow, "nqtscalefactor");
					setBodyValueAt(new UFBoolean(true), eRow, "bqtfixedflag");
				} else {
					// 清除主存货与赠品存货的对照关系
					// 清除赠品行和主存货行之前的对照关系
					if (isBuyLargessLine(eRow)) {
						setBodyValueAt(new UFBoolean(false), eRow,
								"blargessflag");
						setBodyValueAt(null, eRow, "clargessrowno");
					}

					String[] formulas = new String[3];
					// 报价单位名称
					formulas[0] = "cquoteunit->getColValue(bd_measdoc,measname,pk_measdoc,cquoteunitid)";
					// 报价换算率
					formulas[1] = "nqtscalefactor->getColValue2(bd_convert,mainmeasrate,pk_invbasdoc,cinvbasdocid,pk_measdoc,cquoteunitid)";
					// 是否固定换算率
					formulas[2] = "bqtfixedflag->getColValue2(bd_convert,fixedflag,pk_invbasdoc,cinvbasdocid,pk_measdoc,cquoteunitid)";
					execBodyFormulas(eRow, formulas);
				}
			} else if (cunitid != null && cquoteunitid == null) {
				setBodyValueAt(cunitid, eRow, "cquoteunitid");
				setBodyValueAt(getBodyValueAt(eRow, "cunitname"), eRow,
						"cquoteunit");
				setBodyValueAt(new UFDouble(1.0), eRow, "nqtscalefactor");
				setBodyValueAt(new UFBoolean(true), eRow, "bqtfixedflag");
			} else {
				setBodyValueAt(null, eRow, "cquoteunitid");
				setBodyValueAt(null, eRow, "cquoteunit");
				setBodyValueAt(null, eRow, "nqtscalefactor");
				setBodyValueAt(null, eRow, "bqtfixedflag");
			}
			// 有来源的不能带
			Object sSource = getBodyValueAt(eRow, "creceipttype");
			if (sSource == null || sSource.toString().trim().length() == 0) {
				UFBoolean blar = new UFBoolean(getBodyValueAt(eRow,
						"blargessflag") == null ? "false" : getBodyValueAt(
						eRow, "blargessflag").toString());
				// 搜买赠
				if (blar == null || !blar.booleanValue()) {
					String sPk = (String) getBodyValueAt(eRow, "cinventoryid");

					// 删除原行所属的赠品存货
					int[] inewdelline = setBlargebindLineWhenDelLine(new int[] { eRow },1);
					if (inewdelline != null && inewdelline.length > 0)
						uipanel.onDelLine(inewdelline);

					if (sPk!=null)
						afterInventoryMutiEdit(eRow, new String[] { sPk }, false,
							false, null, true, 2);

				}
			}

		}
	}

	/**
	 * ?user> 功能： 2) 当存货为固定换算率时，订单行上的换算率属性不能进行编辑，当存货为非固定换算率时，订单
	 * 
	 * 行上的换算率属性可以进行编辑，根据变化的换算率计算出辅数量。
	 * 
	 * 参数： 返回： 例外： 日期：(2006-10-12 20:42:57) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param row
	 *            int
	 */
	public void setScaleEditableByRow(int row) {
		
		/**辅单位*/
		String cpackunitid = (String) getBodyValueAt(row, "cpackunitid");
		if (cpackunitid == null || cpackunitid.trim().length() == 0) {
			setCellEditable(row, "scalefactor", false);
		}
		String cquoteunitid = (String) getBodyValueAt(row, "cquoteunitid");
		if (cquoteunitid == null || cquoteunitid.trim().length() == 0) {
			setCellEditable(row, "nqtscalefactor", false);
			return;
		}

		Object otemp = getBodyValueAt(row, "fixedflag");
		UFBoolean fixedflag = new UFBoolean(otemp == null ? "N" : otemp
				.toString());
		// 非固定换算率
		if (!fixedflag.booleanValue()) {
			setCellEditable(row, "scalefactor", true);
			BillItem bitem = getBodyItem("scalefactor");
			if (bitem != null)
				getBillCardTools().resumeBillBodyItemEdit(bitem);
		} else
			setCellEditable(row, "scalefactor", false);

		
		/**报价计量单位*/		
		Object qtotemp = getBodyValueAt(row, "bqtfixedflag");
		UFBoolean qtfixedflag = new UFBoolean(qtotemp == null ? "N" : qtotemp
				.toString());
		// 非固定换算率
		if (!qtfixedflag.booleanValue()) {
			setCellEditable(row, "nqtscalefactor", true);
			BillItem bitem = getBodyItem("nqtscalefactor");
			if (bitem != null)
				getBillCardTools().resumeBillBodyItemEdit(bitem);
		} else
			setCellEditable(row, "nqtscalefactor", false);
		
		
	}

	/**
	 * 收货地址编辑后事件处理。 创建日期：(2001-6-23 13:42:53)
	 */
	private void afterVreceiveaddressEdit(BillEditEvent event) {
		for (int i = 0; i < getBillModel().getRowCount(); i++) {
			getBillCardTools().setBodyValueByHead("vreceiveaddress", i);
			if (getBillModel().getRowState(i) == BillModel.NORMAL)
				getBillModel().setRowState(i, BillModel.MODIFICATION);
		}

	}

	/**
	 * 折本汇率。 创建日期：(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public void afterChangeotobrateEdit(BillEditEvent e) {
		for (int i = 0; i < getRowCount(); i++) {
			setBodyValueAt(getHeadItem("nexchangeotobrate").getValue(), i,
					"nexchangeotobrate");
			calculateNumber(i, "nexchangeotobrate");

			setBodyRowState(i);
		}

	}

	/**
	 * 库存组织编辑后事件处理。 创建日期：(2001-6-23 13:42:53)
	 */
	public void afterCcalbodyidEdit(BillEditEvent e) {
		if (e == null || e.getPos() == BillItem.HEAD) {
			UIRefPane refCalbody = (UIRefPane) getHeadItem("ccalbodyid")
					.getComponent();

			String sRefInitWhere = getBillCardTools().getHeadRefInitWhere(
					"cwarehouseid");

			if (sRefInitWhere == null || sRefInitWhere.trim().length() <= 0)
				sRefInitWhere = " 1=1 ";

			if ("新增".equals(uipanel.strState))/*-=notranslate=-*/
				sRefInitWhere += " and (sealflag = 'N' or sealflag is null) ";

			if (refCalbody.getRefPK() != null) {
				for (int i = 0; i < getBillModel().getRowCount(); i++) {

					if (!getBillCardTools().isOtherCorpRow(i)) {// )!isshowbody){

						setBodyValueAt(refCalbody.getRefPK(), i,
								"cadvisecalbodyid");
						setBodyValueAt(refCalbody.getRefName(), i,
								"cadvisecalbody");

						setBodyValueAt(null, i, "cbodywarehouseid");
						setBodyValueAt(null, i, "cbodywarehousename");
						setBodyValueAt(null, i, "cbatchid");
						setBodyRowState(i);

					}
				}

				// 设置表头仓库约束
				UIRefPane wareRef = (UIRefPane) getHeadItem("cwarehouseid")
						.getComponent();
				if (wareRef == null)
					return;

				if ("新增".equals(uipanel.strState))/*-=notranslate=-*/
					wareRef.getRefModel().setSealedDataShow(false);
				else
					wareRef.getRefModel().setSealedDataShow(true);

				wareRef.getRefModel().setWherePart(
						sRefInitWhere + " and pk_calbody = '"
								+ refCalbody.getRefPK() + "' ");
				
		        //设置直运仓
				for (int i=0;i<getRowCount();i++)
					setDerictTransSendWareHouse(i);

			} else {

				for (int i = 0; i < getBillModel().getRowCount(); i++) {

					if (!getBillCardTools().isOtherCorpRow(i)) {

						setBodyValueAt(null, i, "cadvisecalbodyid");
						setBodyValueAt(null, i, "cadvisecalbody");

						setBodyValueAt(null, i, "cbodywarehouseid");
						setBodyValueAt(null, i, "cbodywarehousename");
						setBodyValueAt(null, i, "cbatchid");
						setBodyRowState(i);
					}

				}

				// 设置表头仓库约束
				UIRefPane wareRef = (UIRefPane) getHeadItem("cwarehouseid")
						.getComponent();
				if (wareRef == null)
					return;

				if ("新增".equals(uipanel.strState))/*-=notranslate=-*/
					wareRef.getRefModel().setSealedDataShow(false);
				else
					wareRef.getRefModel().setSealedDataShow(true);

				wareRef.getRefModel().setWherePart(sRefInitWhere);

			}
		} else if (e != null && e.getPos() == BillItem.BODY) {
			setBodyValueAt(null, e.getRow(), "cbodywarehouseid");
			setBodyValueAt(null, e.getRow(), "cbodywarehousename");
			setBodyValueAt(null, e.getRow(), "cbatchid");
			
			//设置直运仓
			setDerictTransSendWareHouse(e.getRow());
			
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-10-21 13:16:29)
	 * 
	 * @param ev
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	private void afterBodyAddressEdit(BillEditEvent ev) {
		// 收货地址参照
		UIRefPane vreceiveaddress = (UIRefPane) getBodyItem("vreceiveaddress")
				.getComponent();

		if (vreceiveaddress.getRefPK() != null) {
			setBodyValueAt(
					vreceiveaddress.getRefValue("bd_custaddr.pk_areacl"), ev
							.getRow(), "creceiptareaid");
			setBodyValueAt(vreceiveaddress.getRefValue("bd_areacl.areaclname"),
					ev.getRow(), "creceiptareaname");
		}
	}

	/**
	 * 收货单位编辑后事件处理。 创建日期：(2001-6-23 13:42:53)
	 */
	private void afterBodyCreceiptcorpidEdit(BillEditEvent e) {
		try {

			String sBodyCreceiptcorpid = (String) (getBodyValueAt(e.getRow(),
					"creceiptcorpid"));

			SOToolVO toolsvo = new SOToolVO();

			toolsvo.setAttributeValue("pk_cumandoc", sBodyCreceiptcorpid);

			toolsvo.setAttributeValue("pk_cubasdoc", "");

			toolsvo.setAttributeValue("pk_custaddr", "");

			toolsvo.setAttributeValue("vreceiveaddress", "");

			toolsvo.setAttributeValue("creceiptareaid", "");

			toolsvo.setAttributeValue("creceiptareaname", "");

			toolsvo.setAttributeValue("crecaddrnode", "");

			toolsvo.setAttributeValue("crecaddrnodename", "");

			toolsvo.setAttributeValue("defaddrflag", "Y");

			getBillCardTools()
					.execFormulas(
							new String[] { "pk_cubasdoc->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,pk_cumandoc)" },
							new SOToolVO[] { toolsvo });

			String pk_custaddr = BillTools.getColValue2("bd_custaddr","pk_custaddr", "pk_cubasdoc", 
					(String) toolsvo.getAttributeValue("pk_cubasdoc"), "defaddrflag","Y");

			toolsvo.setAttributeValue("pk_custaddr", pk_custaddr);

			String[] formulas = {

					"vreceiveaddress->getColValue(bd_custaddr,addrname,pk_custaddr,pk_custaddr)",

					"crecaddrnode->getColValue(bd_custaddr,pk_address,pk_custaddr,pk_custaddr)",

					"creceiptareaid->getColValue(bd_custaddr,pk_areacl,pk_custaddr,pk_custaddr)",

					"crecaddrnodename->getColValue(bd_address,addrname,pk_address,crecaddrnode)",

					"creceiptareaname->getColValue(bd_areacl,areaclname,pk_areacl,creceiptareaid)"

			};

			getBillCardTools().execFormulas(formulas,new SOToolVO[] { toolsvo });

			setBodyValueAt(toolsvo.getAttributeValue("vreceiveaddress"),// aryAddressAndArea[1],
					e.getRow(), "vreceiveaddress");
			setBodyValueAt(toolsvo.getAttributeValue("creceiptareaid"),// aryAddressAndArea[2],
					e.getRow(), "creceiptareaid");
			setBodyValueAt(toolsvo.getAttributeValue("creceiptareaname"),// aryAddressAndArea[3],
					e.getRow(), "creceiptareaname");
			setBodyValueAt(toolsvo.getAttributeValue("crecaddrnode"),// aryAddressAndArea[3],
					e.getRow(), "crecaddrnode");
			setBodyValueAt(toolsvo.getAttributeValue("crecaddrnodename"),// aryAddressAndArea[3],
					e.getRow(), "crecaddrnodename");

		} catch (Throwable ex) {
			handleException(ex);
		}
	}

	/**
	 * 自由项编辑后事件处理。 创建日期：(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public void afterBodyDateEdit(int row, boolean isSendDate) {
		UFDate dateResult = getBillCardTools().getTransDate(row, isSendDate);
		//发货日期
		UFDate dconsigndate = getBodyValueAt(row, "dconsigndate") == null ? null
				: new UFDate(getBodyValueAt(row, "dconsigndate").toString()
						.trim());
		// 到货日期
		UFDate ddeliverdate = getBodyValueAt(row, "ddeliverdate") == null ? null
				: new UFDate(getBodyValueAt(row, "ddeliverdate").toString()
						.trim());
		
		//发货日期为空，则到货日期为空
		if (dconsigndate==null){
			setBodyValueAt(null, row, "ddeliverdate");
			return;
		}
		
		//如果到货日期为空或者到货日期小于发货日期，自动将到货日期设置为发货日期
		//发货日期改变
		if (isSendDate) {
			if (dateResult != null && dateResult.toString().length() != 0
					&& dconsigndate != null && dateResult.after(dconsigndate) )
				setBodyValueAt(dateResult.toString(), row, "ddeliverdate");
			else if ( (dateResult==null && dconsigndate != null && ddeliverdate != null && dconsigndate.after(ddeliverdate))
					|| ddeliverdate==null)
				setBodyValueAt(dconsigndate.toString(), row, "ddeliverdate");
		}
		//到货日期改变
		else {
			if (dateResult != null && dateResult.toString().length() != 0
					&& ddeliverdate != null && !dateResult.after(ddeliverdate) )
				setBodyValueAt(dateResult.toString(), row, "dconsigndate");
			else if ( (ddeliverdate != null && dconsigndate != null
					&& !ddeliverdate.after(dconsigndate)) || dconsigndate==null )
				setBodyValueAt(ddeliverdate.toString(), row, "dconsigndate");
		}
	}

	/**
	 * 批次状态编辑后事件处理。 创建日期：(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	private void afterBatchEdit(BillEditEvent e) {
		Object fbatchstatus = getBodyItem("fbatchstatus").converType(
				getBodyValueAt(e.getRow(), "fbatchstatus"));

		// 批次
		if (SoVoConst.fbatchstatus_batchset.equals(fbatchstatus)) {
			setCellEditable(e.getRow(), "cbatchid", true);
		} else if (SoVoConst.fbatchstatus_batchall.equals(fbatchstatus)) {
			setCellEditable(e.getRow(), "cbatchid", false);
			setBodyValueAt(null, e.getRow(), "cbatchid");
		} else {
			setCellEditable(e.getRow(), "cbatchid", true);
		}
	}

	/**
	 * 批次状态编辑后事件处理。 创建日期：(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	private void afterBatchIDEdit(BillEditEvent e) {
		UFDouble dNumber = getBodyValueAt(e.getRow(), "nnumber") == null ? new UFDouble(
				0)
				: (UFDouble) (getBodyValueAt(e.getRow(), "nnumber"));
		if (dNumber.compareTo(new UFDouble(0)) < 0)
			return;
		
		/** 增加批次号多选处理 2008-04-02 中国乐凯一期要求 **/
		/////1.获取批次号//////////////////////////////////
		nc.ui.ic.pub.lot.LotNumbRefPane lotRef = (nc.ui.ic.pub.lot.LotNumbRefPane) getBodyItem(
				"cbatchid").getComponent();
		nc.vo.ic.pub.lot.LotNumbRefVO[] voLot = null;
		try {
			voLot = lotRef.getLotNumbRefVOs();
		} catch (Exception ex) {
			nc.vo.scm.pub.SCMEnv.error(ex);
		}
		
		/////2.手工输入的话,数据合法则退出
		if (!getLotNumbRefPane().isClicked()) {
			lotRef.checkData();
			return;
		}
		
	    /////3.基于批次号数组复制表体行//////////////////////
		int currow = e.getRow();//当前行
		if (voLot != null && voLot.length > 1) {
			//先设置第一行，数量设置为库存结存量
			//setBodyValueAt(voLot[0].getNinnum(), currow, "nnumber");
			//afterNumberEditLogic(currow, "nnumber", false);
			for (int j = 1; j < voLot.length; j++) {
				uipanel.onCopyLine();
				uipanel.onPasteLine();
				currow++;
				setBodyValueAt(voLot[j].getVbatchcode(), currow, "cbatchid");
				//数量设置为库存结存量
				//setBodyValueAt(voLot[j].getNinnum(), currow, "nnumber");
				//afterNumberEditLogic(currow, "nnumber", false);
			}
		}
		/*else{
			//只选一个批次时，数量也设置为库存结存量
			setBodyValueAt(voLot[0].getNinnum(), currow, "nnumber");
			afterNumberEditLogic(currow, "nnumber", false);
		}*/
		/** 增加批次号多选处理 2008-04-02 中国乐凯一期要求 **/
	}

//	/**
//	 * 是否直运编辑后事件处理。 创建日期：(2001-6-23 13:42:53)
//	 * 
//	 * @return nc.vo.pub.lang.UFDouble
//	 */
//	private void afterBdericttransEdit(BillEditEvent e) {
//		getBillCardTools().clearBodyValue(
//				new String[] { "crecwareid", "crecwarehouse" }, e.getRow());
//		if (new UFBoolean(e.getValue().toString()).booleanValue()
//				&& getBodyValueAt(e.getRow(), "creccalbodyid") != null
//				&& getBodyValueAt(e.getRow(), "creccalbodyid").toString()
//						.trim().length() > 0) {
//			String[] sFormula = {
//					"crecwareid->getColValue2(bd_stordoc,pk_stordoc,pk_calbody,creccalbodyid,isdirectstore,\"Y\")",
//					"crecwarehouse->getColValue(bd_stordoc,storname,pk_stordoc,crecwareid)" };
//			execBodyFormulas(e.getRow(), sFormula);
//		}
//	}

	/**
	 * 仓库编辑后事件处理。 创建日期：(2001-6-23 13:42:53)
	 */
	private void afterCwarehouseidEdit(BillEditEvent e) {
		if (e == null || e.getPos() == BillItem.HEAD) {
			UIRefPane refWare = (UIRefPane) getHeadItem("cwarehouseid")
					.getComponent();
			if (refWare.getRefPK() != null) {
				for (int i = 0; i < getBillModel().getRowCount(); i++) {
					String cconsigncorpid = (String) getBodyValueAt(i,
							"cconsigncorpid");
					if ((cconsigncorpid == null
							|| cconsigncorpid.trim().length() <= 0 || cconsigncorpid
							.equals(pk_corp))
							&& !refWare.getRefPK().equals(
									getBodyValueAt(i, "cbodywarehouseid"))) {

						setBodyValueAt(refWare.getRefPK(), i,
								"cbodywarehouseid");
						setBodyValueAt(refWare.getRefName(), i,
								"cbodywarehousename");
						setBodyValueAt(null, i, "cbatchid");
						setBodyRowState(i);

					}
				}
			}
		} else {
			/** 不清空批次号，此处控制无意义，由出库单处理 V502 yangbo zhongwei* */
			String cbodywarehouseid = getBillCardTools().getBodyStringValue(
					e.getRow(), "cbodywarehouseid");
			if (cbodywarehouseid == null
					|| cbodywarehouseid.trim().length() <= 0) {
				// setBodyValueAt(null, e.getRow(), "cbatchid");
				setBodyValueAt(null, e.getRow(), "cbodywarehouseid");
				setBodyValueAt(null, e.getRow(), "cbodywarehousename");

			} else {
				// setBodyValueAt(null, e.getRow(), "cbatchid");
				String[] formulas = {
						"cadvisecalbodyid->getColValue(bd_stordoc,pk_calbody,pk_stordoc,cbodywarehouseid)",
						"cadvisecalbody->getColValue(bd_calbody,bodyname,pk_calbody,cadvisecalbodyid)" };
				execBodyFormulas(e.getRow(), formulas);
			}

		}
	}

	/**
	 * 发运方式编辑后事件处理。 创建日期：(2001-6-23 13:42:53)
	 */
	private void afterCtransmodeEdit(BillEditEvent event) {
		for (int i = 0; i < getBillModel().getRowCount(); i++) {
			String sDateDeliver = getBodyValueAt(i, "ddeliverdate") == null ? null
					: getBodyValueAt(i, "ddeliverdate").toString().trim();
			if (sDateDeliver != null && sDateDeliver.length() != 0) {
				afterBodyDateEdit(i, false);
			}
		}
	}

	/**
	 * 币种编辑后事件处理。 创建日期：(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public void afterCurrencyChange() {
		afterCurrencyEdit(null);
	}

	/**
	 * 改变行号
	 * 
	 * @param e
	 */
	private void afterRownoEdit(BillEditEvent e) {
		// 验证行号
		BillRowNo.afterEditWhenRowNo(this, e, getBillType());
		String sOldrow = (String) e.getOldValue();
		if (sOldrow == null)
			return;
		String sRow = null;
		UFBoolean bLargess = null;
		for (int i = 0; i < getRowCount(); i++) {
			sRow = (String) getBodyValueAt(i, "clargessrowno");
			bLargess = new UFBoolean(
					getBodyValueAt(i, "blargessflag") == null ? "false"
							: getBodyValueAt(i, "blargessflag").toString());
			if (sRow != null && bLargess.booleanValue() && sRow.equals(sOldrow)) {
				setBodyValueAt(e.getValue(), i, "clargessrowno");
			}
		}

	}

	/**
	 * 收货单位编辑后事件处理。 创建日期：(2001-6-23 13:42:53)
	 */
	public void afterCreceiptcorpEdit() {

		// 收货地址
		UIRefPane vreceiveaddress = (UIRefPane) getHeadItem("vreceiveaddress")
				.getComponent();
		vreceiveaddress.setAutoCheck(false);

		// 收货地址参照
		String creceiptcustomerid = null;
		if (getHeadItem("creceiptcustomerid") != null)
			creceiptcustomerid = getHeadItem("creceiptcustomerid").getValue();

		((CustAddrRefModel) vreceiveaddress.getRefModel())
				.setCustId(creceiptcustomerid);
		// 基础档案ID
		String formula = "getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,creceiptcustomerid)";
		String pk_cubasdoc = (String) execHeadFormula(formula);

		SOToolVO toolsvo = new SOToolVO();

		toolsvo.setAttributeValue("pk_cumandoc", creceiptcustomerid);

		toolsvo.setAttributeValue("pk_cubasdoc", pk_cubasdoc);

		toolsvo.setAttributeValue("pk_custaddr", "");

		toolsvo.setAttributeValue("crecaddrnode", "");

		toolsvo.setAttributeValue("crecaddrnodename", "");

		String pk_custaddr = BillTools.getColValue2("bd_custaddr",
				"pk_custaddr", "pk_cubasdoc", pk_cubasdoc, "defaddrflag", "Y");

		toolsvo.setAttributeValue("pk_custaddr", pk_custaddr);

		String[] formulas = {

				"crecaddrnode->getColValue(bd_custaddr,pk_address,pk_custaddr,pk_custaddr)",

				"crecaddrnodename->getColValue(bd_address,addrname,pk_address,crecaddrnode)"

		};

		getBillCardTools().execFormulas(formulas, new SOToolVO[] { toolsvo });

		if (vreceiveaddress.getUITextField().getText() == null
				|| vreceiveaddress.getUITextField().getText().trim().length() <= 0)
			vreceiveaddress.setPK(pk_custaddr);

		// 表体收货地址携带
		afterVreceiveaddressEdit(null);
		if (getHeadItem("creceiptcustomerid") != null) {
			UIRefPane refCreceiptcorpid = (UIRefPane) getHeadItem(
					"creceiptcustomerid").getComponent();
			if (refCreceiptcorpid.getRefPK() != null) {

				String creceiptcorpid = null;
				String vreceiveaddressbody = null;

				for (int i = 0; i < getBillModel().getRowCount(); i++) {

					creceiptcorpid = (String) getBodyValueAt(i,
							"creceiptcorpid");
					if (creceiptcorpid == null
							|| creceiptcorpid.trim().length() <= 0) {

						setBodyValueAt(refCreceiptcorpid.getRefPK(), i,
								"creceiptcorpid");
						setBodyValueAt(refCreceiptcorpid.getRefName(), i,
								"creceiptcorpname");

						setBodyValueAt(toolsvo
								.getAttributeValue("crecaddrnode"), i,
								"crecaddrnode");

						setBodyValueAt(toolsvo
								.getAttributeValue("crecaddrnodename"), i,
								"crecaddrnodename");

						setBodyRowState(i);
					}

					vreceiveaddressbody = (String) getBodyValueAt(i,
							"vreceiveaddress");
					if (vreceiveaddressbody == null
							|| vreceiveaddressbody.trim().length() <= 0) {
						getBillCardTools().setBodyValueByHead(
								"vreceiveaddress", i);
						setBodyRowState(i);
					}

				}
			}
		}

	}

	/**
	 * 库存组织编辑后事件处理。 创建日期：(2001-6-23 13:42:53)
	 */
	private void afterCrecwarehouseEdit(BillEditEvent e) {
		if (e != null) {
			String crecwareid = getBillCardTools().getBodyStringValue(
					e.getRow(), "crecwareid");

			if (crecwareid == null || crecwareid.trim().length() <= 0) {

			} else {
				String[] formulas = {
						"creccalbodyid->getColValue(bd_stordoc,pk_calbody,pk_stordoc,crecwareid)",
						"creccalbody->getColValue(bd_calbody,bodyname,pk_calbody,creccalbodyid)" };
				execBodyFormulas(e.getRow(), formulas);
			}

		}
	}

	/**
	 * 表头价税合计编辑后事件 可以对“价税合计”进行编辑， 编辑后根据各订单行的含税单价*数量*单品折扣的汇总值 与最新的“价税合计”值得到整单折扣，
	 * 整单折扣=订单头的“价税合计”/各订单行的含税单价*数量*单品折扣的汇总值， 然后根据公式：
	 * 含税净价=含税价*整单折扣*单品折扣计算得出含税净价， 根据价税合计=数量*含税净价计算得到订单行的价税合计， 把各行的价税合计值进行累计加，
	 * 把与“价税合计”的差异平衡在销售订单的最后一行上， 根据计算公式 ：价税合计=数量*含税净价计算得到最后一行的含税净价， 根据公式：
	 * 含税净价=含税价*整单折扣*单品折扣计算得出最后一行的单品折扣；
	 * 
	 * @param e
	 * 
	 * @comment 不影响赠品行 不影响缺货行 V502
	 * 
	 */
	protected void afterHeadsummnyEdit(BillEditEvent e) {
		// /得到编辑前汇总值
		UFDouble udOld = getTotalValue("noriginalcursummny");
		// UFDouble udOld = getNumPriceDisSummny();

		UFDouble udNew = e.getValue() == null ? new UFDouble(0) : new UFDouble(
				e.getValue().toString());

		// 整单折扣=订单头的“价税合计”/各订单行的含税单价*数量*单品折扣的汇总值
		UFDouble udDiscountRate = (udOld.doubleValue() == 0 ? new UFDouble(1)
				: udNew.div(udOld)).multiply(100.);
		getHeadItem("ndiscountrate").setValue(udDiscountRate);

		// 表头整单折扣重新计算数据,强制含税优先
		Integer iaPrior = new Integer(RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE);
		for (int i = 0; i < getRowCount(); i++) {
			setBodyValueAt(getHeadItem("ndiscountrate").getValueObject().toString(), i, "ndiscountrate");
			calculateforHeadSummny(i, "ndiscountrate", iaPrior);
			setBodyRowState(i);
		}
		
		getBillModel().setNeedCalculate(true);
		getBillModel().setNeedCalculate(false);

		// 得到最新的价税合计
		UFDouble udLast = getTotalValue("noriginalcursummny");

		// 找到最后一行（非赠品行、非缺货行）
		int iLastrow = getRowCount() - 1;
		Object blargessflag, boosflag;
		for (int i = iLastrow; i >= 0; i--) {
			if (getBodyValueAt(i, "cinventoryid") != null
					&& getBodyValueAt(i, "cinventoryid").toString().length() > 0) {
				blargessflag = getBodyValueAt(i, "blargessflag");
				boosflag = getBodyValueAt(i, "boosflag");
				if (blargessflag != null && (Boolean) blargessflag)
					iLastrow--;
				else if (boosflag != null && (Boolean) boosflag)
					iLastrow--;
				else
					break;
			} else
				iLastrow--;
		}

		/** 如果没有找到符合要求的行就直接退出，表头和表体的价税合计在保存时检验* */
		if (iLastrow < 0)
			return;

		if (udLast == null || udLast.doubleValue() != udNew.doubleValue()) {

			UFDouble udModify = udNew.sub(udLast);
			UFDouble udNow = (UFDouble) getBodyValueAt(iLastrow,
					"noriginalcursummny");
			udNow = (udNow == null ? new UFDouble(0) : udNow).add(udModify);
			setBodyValueAt(udNow, iLastrow, "noriginalcursummny");
			afterNumberEdit(new int[] { iLastrow }, "noriginalcursummny", null,
					false, true);
		}

	}

	/**
	 * 表头单据日期编辑后事件
	 * 
	 * @param e
	 */
	private void afterHeadBillDateEdit(BillEditEvent e) {
		// 表体发货和到货日期修改为单据日期
		String dbilldate = (getHeadItem("dbilldate").getValueObject() == null || getHeadItem(
				"dbilldate").getValueObject().toString().trim().length() == 0) ? null
				: getHeadItem("dbilldate").getValueObject().toString();
		if (dbilldate == null)
			return;

		/** 只针对空值，满足日期逻辑校验* */
		Object dconsigndate, ddeliverdate;
		for (int i = 0, len = getBillModel().getRowCount(); i < len; i++) {
			// 发货日期
			dconsigndate = getBodyValueAt(i, "dconsigndate");
			if (dconsigndate == null) {
				setBodyValueAt(dbilldate, i, "dconsigndate");
			}

			// 到货日期
			ddeliverdate = getBodyValueAt(i, "ddeliverdate");
			if ((ddeliverdate == null)
					&& (dbilldate.compareTo(getBodyValueAt(i, "dconsigndate")
							.toString()) >= 0)) {
				setBodyValueAt(dbilldate, i, "ddeliverdate");
			}

		}// end for
	}

	// /**
	// * 各订单行的含税单价*数量*单品折扣的汇总值
	// *
	// * @return
	// */
	// private UFDouble getNumPriceDisSummny() {
	// UFDouble ud = new UFDouble(0);
	// UFDouble udt = null;
	// int icout = getRowCount();
	// for (int i = 0; i < icout; i++) {
	// udt = getBodyValueAt(i, "noriginalcurtaxprice") == null ? new UFDouble(
	// 0)
	// : (UFDouble) getBodyValueAt(i, "noriginalcurtaxprice");
	// udt = udt
	// .multiply(getBodyValueAt(i, "nnumber") == null ? new UFDouble(
	// 0)
	// : (UFDouble) getBodyValueAt(i, "nnumber"));
	// udt = udt
	// .multiply(
	// getBodyValueAt(i, "nitemdiscountrate") == null ? new UFDouble(
	// 0)
	// : (UFDouble) getBodyValueAt(i,
	// "nitemdiscountrate")).div(100);
	// ud = ud.add(udt);
	//
	// }
	// return ud;
	// }

	/**
	 * 得到汇总值
	 * 
	 * @param sItemKey
	 * @return
	 */
	public UFDouble getTotalValue(String sItemKey) {
		int iCol = getBillModel().getBodyColByKey(sItemKey);

		// 得到最新的价税合计
		UFDouble udLast = UFDouble.ZERO_DBL;
		UFDouble tmp;
		Object cinventoryid,blargessflag,boosflag;
		for (int i = 0, len = getBillModel().getRowCount(); i < len; i++) {

			cinventoryid = getBodyValueAt(i, "cinventoryid");
			// 去掉无存货id行的影响（只对表头价税合计起作用）
			if (cinventoryid == null || cinventoryid.toString().trim().length() == 0)
				continue;
			
			blargessflag = getBodyValueAt(i, "blargessflag");
			// 去掉赠品的影响（只对表头价税合计起作用）
			if (blargessflag != null && (Boolean) blargessflag)
				continue;

			boosflag = getBodyValueAt(i, "boosflag");
			// 去掉缺货的影响（只对表头价税合计起作用）
			if (boosflag != null && (Boolean) boosflag)
				continue;
			
			tmp = (UFDouble) getBillModel().getValueAt(i, iCol);
			if (tmp != null)
				udLast = udLast.add((UFDouble) tmp);
		}// end for

		return udLast;

	}

	/**
	 * 存货编辑后事件处理。 创建日期：(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	private void afterInventoryMutiEdit(BillEditEvent e) {
		
		// 清除赠品行,捆绑行和主存货行之前的对照关系
		if (e.getOldValue() != null && e.getValue() != null
				&& !e.getOldValue().equals(e.getValue())) {
			if(isBuyLargessLine(e.getRow())){
				setBodyValueAt(new UFBoolean(false), e.getRow(), "blargessflag");
				setBodyValueAt(null, e.getRow(), "clargessrowno");
			}
			else if (isBindLine(e.getRow())){
				setBodyValueAt(new UFBoolean(false), e.getRow(), "bbindflag");
				setBodyValueAt(null, e.getRow(), "clargessrowno");
			}
		}

		// 删除原行所属的赠品存货
		int[] inewdelline = setBlargebindLineWhenDelLine(new int[] { e.getRow() },1);
		if (inewdelline != null && inewdelline.length > 0)
			uipanel.onDelLine(inewdelline);
		
		// 删除原行所属的捆绑存货
		int[] inewdelline_bind = setBlargebindLineWhenDelLine(new int[] { e
				.getRow() },2);
		if (inewdelline_bind != null && inewdelline_bind.length > 0)
			uipanel.onDelLine(inewdelline_bind);

		//1、需要进行买赠捆绑和存货对应发货库存组织同时处理,由存货、客户修改触发； 
		//2、只处理买赠捆绑，不处理发货库存组织;由币种、数量、日期、渠道分组修改触发 
		//3、只处理发货库存组织，不处理买赠捆绑；由销售组织修改触发
		afterInventoryMutiEdit(e.getRow(), 1);

		// 设置自由项输入提示
		// 批量获取存货信息
		InvVO[] invvos = new InvVO[getRowCount()];
		for (int i = 0; i < invvos.length; i++) {
			invvos[i] = new InvVO();
			invvos[i].setCinventoryid((String) getBodyValueAt(i, "cinventoryid"));
		}

		InvoInfoBYFormula invvosget = new InvoInfoBYFormula();
		invvos = invvosget.getQuryInvVOs(invvos, false, true, 1);

		ArrayList al_invs = new ArrayList();
		for (InvVO invvo : invvos) {
			al_invs.add(invvo);
		}

		InvAttrCellRenderer ficr = new InvAttrCellRenderer();
		ficr.setFreeItemRenderer(this, al_invs);

		updateUI();

	}

	/**
	 * 判断是否赠品行
	 * 
	 * @param irow
	 * @return
	 */
	private boolean isBuyLargessLine(int i) {
		String sRow = (String) getBodyValueAt(i, "clargessrowno");
		UFBoolean bLargess = new UFBoolean(
				getBodyValueAt(i, "blargessflag") == null ? "false"
						: getBodyValueAt(i, "blargessflag").toString());
		if (bLargess != null && bLargess.booleanValue() && sRow != null
				&& sRow.trim().length() > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断是否捆绑行
	 * 
	 * @param irow
	 * @return
	 */
	private boolean isBindLine(int i) {
		String sRow = (String) getBodyValueAt(i, "clargessrowno");
		UFBoolean bLargess = new UFBoolean(
				getBodyValueAt(i, "bbindflag") == null ? "false"
						: getBodyValueAt(i, "bbindflag").toString());
		if (bLargess != null && bLargess.booleanValue() && sRow != null
				&& sRow.trim().length() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 存货编辑后事件处理。
	 * 
	 * 创建日期：(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	private void afterInventoryMutiEdit(int irow, int iChg) {

		String[] refPks = ((UIRefPane) getBodyItem("cinventorycode")
				.getComponent()).getRefPKs();

		afterInventoryMutiEdit(irow, refPks, true, true, null, true, iChg);
	}

	/**
	 * 存货编辑后事件处理（包含设置买赠和捆绑）。
	 * 
	 * 创建日期：(2001-6-23 13:42:53)
	 * 
	 * bCopy:true,这是复制新增的，不包含原行，新增行从row+1
	 * 
	 * 开始 bCopy:false,这是选存货新增，包含原行，新增行从row 开始 refPks:初始的存货行
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 * 
	 */
	private void afterInventoryMutiEdit(int irow, String[] refPks,
			boolean bMain, boolean bBinds, String sFormulakey,
			boolean bNeedFindPrice, int iChg) {

		/**若将存货清空，则将单价金额均清空 lining zhangcheng v5.3*/
		if (refPks == null){
			//清空本行的单价金额
			String[] mny = getBillCardTools().getSaleItems_Mny();
			String[] price = getBillCardTools().getSaleItems_Price();
			for (int i = 0; i < price.length; i++) 
				getBillCardTools().setBodyValue(null, irow, price[i]);
            for (int i = 0; i < mny.length; i++) 
            	getBillCardTools().setBodyValue(null, irow, mny[i]);
            
            //重新设置表头价税合计
            if (getHeadItem("nheadsummny") != null)
				getHeadItem("nheadsummny").setValue(getTotalValue("noriginalcursummny"));
			
            return;
		}

		long s = System.currentTimeMillis();
		SCMEnv.out("读取存货相关信息开始...");
		ArrayList alist = setBodyByinvs(refPks, irow, bMain, bBinds, iChg);
		SCMEnv.out("循环设置表体[共用时" + (System.currentTimeMillis() - s) + "]");
		if (alist == null)
			return;
		
		// 尝试关联合同：新增默认合同项
		setDefaultCtItem(irow, alist.size());

		if (uipanel.SO_01 != null && uipanel.SO_01.intValue() != 0
				&& getRowCount() - 1 > uipanel.SO_01.intValue()) {
			uipanel.showErrorMessage(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("40060301", "UPP40060301-000171", null,
							new String[] { uipanel.SO_01.intValue() + "" }));
		}

		s = System.currentTimeMillis();
		SCMEnv.out("设置其他相关信息开始...");

		// 执行用户自定义公式
		if (getBillCardTools().getEditInvfomulas_add() != null
				&& getBillCardTools().getEditInvfomulas_add().length > 0) {
			getBillCardTools().execBodyFormulas(
					getBillCardTools().getEditInvfomulas_add(), alist);
		}

		/** 提前到此处 V502* */
		for (int i = 0, len = alist.size(); i < len; i++) {
			afterCconsignCorpEdit((Integer) alist.get(i));
		}
		/** 提前到此处 V502* */

		/** V502 控制远程调用次数，去掉此处判断条件，合并公式的处理 zhongwei* */
		// if (getBillType().equals(SaleBillType.SaleOrder)
		// || getBillType().equals(SaleBillType.SaleInitOrder)) {
		// 批次状态公式
		String[] appendFormula = {
				"wholemanaflag->getColValue(bd_invmandoc,wholemanaflag,pk_invmandoc,cinventoryid)",
				"isconfigable->getColValue(bd_invmandoc,isconfigable,pk_invmandoc,cinventoryid)",
				"isspecialty->getColValue(bd_invmandoc,isspecialty,pk_invmandoc,cinventoryid)",
				"cprolineid->getColValue(bd_invbasdoc,pk_prodline,pk_invbasdoc,cinvbasdocid)",
				"laborflag->getColValue(bd_invbasdoc,laborflag,pk_invbasdoc,cinvbasdocid)",
				"discountflag->getColValue(bd_invbasdoc,discountflag,pk_invbasdoc,cinvbasdocid)",
				"isappendant->getColValue(bd_invmandoc,isappendant,pk_invmandoc,cinventoryid)",
				"nreturntaxrate->getColValue(bd_invmandoc,expaybacktax,pk_invmandoc,cinventoryid)",
				"cprolinename->getColValue(bd_prodline,prodlinename,pk_prodline,cprolineid)",
				"cconsigncorp->getColValue(bd_corp,unitname,pk_corp,cconsigncorpid)",
				"cadvisecalbody->getColValue(bd_calbody,bodyname,pk_calbody,cadvisecalbodyid)",
				"cbodywarehousename->getColValue(bd_stordoc,storname,pk_stordoc,cbodywarehouseid)" };

		getBillCardTools().execBodyFormulas(appendFormula, alist);
		// }
		/** V502 控制远程调用次数，去掉此处判断条件，合并公式的处理 zhongwei* */

		if (getBillType().equals(SaleBillType.SaleOrder)) {
			// 批次状态公式

			// 批次控制:改到单据摸版内项目：wholemanaflag 控制
			Object temp = getBodyValueAt(irow, "wholemanaflag");
			boolean wholemanaflag = (temp == null ? false : new UFBoolean(temp
					.toString()).booleanValue());
			setCellEditable(irow, "fbatchstatus", wholemanaflag);
			setCellEditable(irow, "cbatchid", wholemanaflag);
			// 配置销售按钮控制isconfigable
			temp = getBodyValueAt(irow, "isconfigable");
			boolean isconfigable = (temp == null ? false : new UFBoolean(temp
					.toString()).booleanValue());
			uipanel.boBom.setEnabled(isconfigable);
		}
		uipanel.updateButton(uipanel.boBom);

		// String[] appendFormula = {
		// "cprolineid->getColValue(bd_invbasdoc,pk_prodline,pk_invbasdoc,cinvbasdocid)",
		// "laborflag->getColValue(bd_invbasdoc,laborflag,pk_invbasdoc,cinvbasdocid)",
		// "discountflag->getColValue(bd_invbasdoc,discountflag,pk_invbasdoc,cinvbasdocid)",
		// "isappendant->getColValue(bd_invmandoc,isappendant,pk_invmandoc,cinventoryid)",
		// "nreturntaxrate->getColValue(bd_invmandoc,expaybacktax,pk_invmandoc,cinventoryid)",
		// "cprolinename->getColValue(bd_prodline,prodlinename,pk_prodline,cprolineid)"
		// };
		//
		// getBillCardTools().execBodyFormulas(appendFormula, alist);

		// 税率先取存货管理档案上的税目对应的税率，如果为空，则取存货基本档案上税目对应的税率。
		loadTaxtrate(alist);

		// 存货
		getBillCardTools().setBodyInventory1(irow, alist.size());

		// 设置表体的渠道类型
		getBillCardTools().setBodyCchantypeid(irow, alist.size());

		SCMEnv.out("设置其他相关信息[共用时" + (System.currentTimeMillis() - s) + "]");
		s = System.currentTimeMillis();
		SCMEnv.out("设置单位转化信息开始...");

		afterInventorysEdit(irow, irow + alist.size(), "ntaxrate", bNeedFindPrice);

		SCMEnv.out("设置单位转化信息[共用时" + (System.currentTimeMillis() - s) + "]");

		setCalByConCalset(m_hConCal, irow, irow + alist.size());

		// getBillCardTools()
		// .execBodyFormulas(
		// new String[] {
		// "cconsigncorp->getColValue(bd_corp,unitname,pk_corp,cconsigncorpid)",
		// "cadvisecalbody->getColValue(bd_calbody,bodyname,pk_calbody,cadvisecalbodyid)",
		// "cbodywarehousename->getColValue(bd_stordoc,storname,pk_stordoc,cbodywarehouseid)"
		// },
		// alist);

		initFreeItem();

		updateUI();
		return;
	}

	/**
	 * 编辑存货读取存货与发货库存组织关系定后，设置相关信息
	 * @param htinvcal
	 * @param startrow
	 * @param stoprow
	 */
	protected void setCalByConCalset(HashMap htinvcal, int startrow, int stoprow) {
		
		//只有最初编辑发货公司、存货、客户时才起作用
		if (this.firstChangeKey!=null
				&& !this.firstChangeKey.equals("cconsigncorp")
				&& !this.firstChangeKey.equals("ccustomerid")
				&& !this.firstChangeKey.equals("cinventorycode")
				&& !this.firstChangeKey.equals("cinventoryid") )
			return;
		
		String sInv = null;
		InvcalbodyVO invbvo = null;
		m_hConCal = htinvcal;
		for (int i = startrow; i <= stoprow; i++) {
			sInv = (String) getBodyValueAt(i, "cinventoryid");
			if (sInv != null && htinvcal.containsKey(sInv)) {

				invbvo = (InvcalbodyVO) htinvcal.get(sInv);

				if (invbvo == null)
					continue;

				// cconsigncorpid 发货公司
				setBodyValueAt(invbvo.getCreceiptcorpid(), i, "cconsigncorpid");
				
				// 表体发货公司发生变化的编辑后事件
				afterCconsignCorpEdit(i);
				
				// 发货库存组织
				setBodyValueAt(invbvo.getCcalbodyid(), i, "cadvisecalbodyid");
				// 仓库
				setBodyValueAt(invbvo.getCwarehouseid(), i, "cbodywarehouseid");
		   }
		   //取表头默认发货库存组织
		   else
			   // 发货库存组织
			   setBodyValueAt(getBillCardTools().getHeadValue("ccalbodyid"), i, "cadvisecalbodyid");
		}
		getBillModel().execLoadFormulasByKey("cconsigncorpid");
		getBillModel().execLoadFormulasByKey("cadvisecalbodyid");
		getBillModel().execLoadFormulasByKey("cbodywarehouseid");
	}

	/**
	 * 设置表体按存货
	 * 
	 * @param refPks 
	 * @param irow
	 * @param bMain
	 *            是否设置主存货，true:用于新增存货，false：用于复制，用于改数量
	 * @param bBinds
	 *            是否设置捆绑件，true:用于新增存货、复制存货，false：用于改数量
	 * @return
	 */
	private ArrayList setBodyByinvs(String[] refPks, int irow, boolean bMain,
			boolean bBinds, int iChg) {

		try {
			long s = System.currentTimeMillis();
			long s0 = s;
			SCMEnv.out("读取捆绑买赠信息开始...");

			ArrayList al = getLargessAndBindingsByInvs(refPks, irow, iChg);
			SCMEnv.out("读取捆绑买赠信息用时[" + (System.currentTimeMillis() - s)
					/ 1000.0 + "s]");
			s = System.currentTimeMillis();

			// 捆绑件+原件
			String[] sBinds = (String[]) al.get(0);
			// 捆绑件
			Hashtable htbinds = (Hashtable) al.get(1);
			// 买赠件
			BuylargessVO[] bvos = (BuylargessVO[]) al.get(2);

			// 合并为一存货数组一起查询存货信息
			Hashtable htLargess = new Hashtable();
			Vector vt = new Vector();
			for (int i = 0; i < sBinds.length; i++) {
				vt.add(sBinds[i]);
			}
			// 原存货
			for (int i = 0; i < refPks.length; i++) {
				vt.add(refPks[i]);
			}
			// BuylargessVO bvotmp = null;
			String larkey = null;
			if (bvos != null && bvos.length > 0) {
				BuylargessBVO[] bodys = null;
				ArrayList allargess = null;
				for (int i = 0; i < bvos.length; i++) {
					// 客户要分级次

					larkey = ((BuylargessHVO) bvos[i].getParentVO())
							.getPk_invmandoc()
							+ ((BuylargessHVO) bvos[i].getParentVO())
									.getCunitid();

					if (htLargess.containsKey(larkey)) {
						allargess = (ArrayList) htLargess.get(larkey);
						allargess.add(bvos[i]);
					} else {
						allargess = new ArrayList();
						allargess.add(bvos[i]);
						htLargess.put(larkey, allargess);
					}

					bodys = (BuylargessBVO[]) bvos[i].getChildrenVO();
					for (int j = 0; j < bodys.length; j++) {
						if (!vt.contains(bodys[j].getPk_invmandoc())) {
							vt.add(bodys[j].getPk_invmandoc());
						}
					}
				}
			}

			String[] snewPks = new String[vt.size()];
			vt.copyInto(snewPks);

			// 批量获取存货信息
			InvVO[] invvos = new InvVO[snewPks.length];
			for (int i = 0; i < invvos.length; i++) {
				invvos[i] = new InvVO();
				invvos[i].setCinventoryid(snewPks[i]);
			}

			SCMEnv.out("分析捆绑买赠数据用时[" + (System.currentTimeMillis() - s)
					/ 1000.0 + "s]");
			s = System.currentTimeMillis();

			InvoInfoBYFormula invvosget = new InvoInfoBYFormula();
			invvos = invvosget.getQuryInvVOs(invvos, false, true, 1);
			// 将返回结果存入hash
			Hashtable htresult = new Hashtable();
			for (int k = 0; k < invvos.length; k++) {
				htresult.put(invvos[k].getCinventoryid(), invvos[k]);
			}
			SCMEnv.out("查询存货信息用时[" + (System.currentTimeMillis() - s) / 1000.0
					+ "s]");
			s = System.currentTimeMillis();

			int count = 0;
			// 设置普通存货
			if (bMain) {
				count += setBodyByOrginvs(refPks, htresult, irow);
				count--;
				SCMEnv.out("设置主存货用时[" + (System.currentTimeMillis() - s)
						/ 1000.0 + "s]");
				s = System.currentTimeMillis();

			}

			// 设置捆绑件
			int startrow = irow;
			int stoprow = irow + count;
			if (bBinds) {
				// 提示
				if (htbinds.size() > 0) {
					if (uipanel.showOkCancelMessage(nc.ui.ml.NCLangRes
							.getInstance().getStrByID("40060301",
									"UPP40060301-000515")) == MessageDialog.ID_OK) {
						s0 -= (System.currentTimeMillis() - s);
						s = System.currentTimeMillis();
						for (int i = stoprow; i >= startrow; i--) {
							count += setBodyByBindinvs(i, htresult, htbinds);
						}
						SCMEnv.out("设置捆绑件用时["
								+ (System.currentTimeMillis() - s) / 1000.0
								+ "s]");
						s = System.currentTimeMillis();
					}
				}
			}
			stoprow = irow + count;
			// 设置买赠件
			for (int i = stoprow; i >= startrow; i--) {
				count += setBodyByLargessinvs(i, htresult, htLargess);
				// 设置返
			}
			SCMEnv.out("设置买赠件用时[" + (System.currentTimeMillis() - s) / 1000.0
					+ "s]");
			s = System.currentTimeMillis();

			// 设置存货与库存组织对照关系 20060705
			if (al.size() >= 4) {
				m_hConCal = (HashMap) al.get(3);
			}
			else if (m_hConCal != null) {
				m_hConCal.clear();
			}

			ArrayList alist = new ArrayList();
			stoprow = irow + count;
			// 设置返回的数组
			for (int i = startrow; i <= stoprow; i++) {
				// 如果不是新增存货，则不用再重新设置原行
				if (!bMain && i == irow)
					continue;

				alist.add(new Integer(i));
			}

			if (bMain
					&& uipanel.boAddLine.isEnabled()
					&& (uipanel.SO_01 == null || uipanel.SO_01.intValue() == 0 || uipanel.SO_01
							.intValue() > getRowCount())) {
				if (irow + alist.size() < getBillModel().getRowCount()) {
					getBillTable().getSelectionModel().setSelectionInterval(
							irow + alist.size(), irow + alist.size());

					getBillTable().getSelectionModel().setSelectionInterval(
							irow, irow);
				} else {
					// 增加新空行
					uipanel.addLine();

					binvedit=true;
					
				}
			}
			SCMEnv.out("读取设置捆绑买赠共用时[" + (System.currentTimeMillis() - s0)
					/ 1000.0 + "s]");
			s = System.currentTimeMillis();

			return alist;

		} catch (Exception e) {
			SCMEnv.out(e);
			// e.printStackTrace();
			if (e instanceof BusinessException)
				uipanel.showWarningMessage(e.getMessage());
			else
				uipanel.showErrorMessage(e.getMessage());
			return null;
		}

	}

	/**
	 * @param refPks
	 *            所选存货Pk
	 * @param icurrow
	 *            当前行
	 * @param iChgInv
	 *            1、需要进行买赠捆绑和存货对应发货库存组织同时处理,由存货、客户修改触发；
	 *            2、只处理买赠捆绑，不处理发货库存组织; 由币种、数量、日期、渠道分组修改触发 
	 *            3、只处理发货库存组织，不处理买赠捆绑；由销售组织修改触发
	 * @return
	 * @throws Exception
	 */
	private ArrayList getLargessAndBindingsByInvs(String[] refPks, int icurrow,
			int iChgInv) throws Exception {
		String dBillDate = null;
		IBuyLargess buyLargess = (IBuyLargess) NCLocator.getInstance().lookup(
				IBuyLargess.class.getName());

		dBillDate = getHeadItem("dbilldate").getValue() == null ? ClientEnvironment
				.getInstance().getBusinessDate().toString()
				: getHeadItem("dbilldate").getValue().toString();
		ArrayList alParam = new ArrayList();
		// 0
		alParam.add(new Integer(iChgInv));
		// 0:pk_corp
		alParam.add(ClientEnvironment.getInstance().getCorporation()
				.getPk_corp());
		// 1:dbilldate
		alParam.add(dBillDate);
		// 2:ccustomerid
		if (getHeadItem("ccustomerid").getValue() != null)
			alParam.add(getHeadItem("ccustomerid").getValue());
		else
			alParam.add("");

		String ccurrencytypeid = null;
		String cchantypeid = null;
		ccurrencytypeid = (String) getBodyValueAt(icurrow, "ccurrencytypeid");
		// 3.ccurrencytypeid
		if (ccurrencytypeid == null)
			alParam.add("");
		else
			alParam.add(ccurrencytypeid);
		cchantypeid = (String) getBodyValueAt(icurrow, "cchantypeid");
		// 4.cchantypeid
		if (cchantypeid == null)
			alParam.add("");
		else
			alParam.add(cchantypeid);
		// 如果是由改存货触发，则增加参数销售组织
		// 5.csalecorpid
		if (getHeadItem("csalecorpid").getValue() != null) {
			alParam.add(getHeadItem("csalecorpid").getValue());
		} else
			alParam.add("");
		
		/**通灵翠钻*/
		ArrayList invclcode = getInvclCode(refPks);
		alParam.add(invclcode);
		/**通灵翠钻*/

		/** 只要有存货就会查询买赠、捆绑、库存组织对照关系，必定会有一次远程调用* */
		ArrayList al = buyLargess.getLargessAndBindingsByInvs(refPks, alParam);
		if (al.get(2) instanceof BuylargessVO[]) {
			BuylargessVO[] bvos = (BuylargessVO[]) al.get(2);
			if (bvos != null && bvos.length > 0) {
				for (int i = 0; i < bvos.length; i++) {
					//周长胜  2008-11-04  v5.5 买赠支持时效
					ArrayList buylargessBVOList=new ArrayList();
					BuylargessBVO[] buyLarBVO=(BuylargessBVO[])bvos[i].getChildrenVO();
					for(int j=0;j<buyLarBVO.length;j++){
						UFDate beginDate = buyLarBVO[j].getTbegdate();
						UFDate endDate = buyLarBVO[j].getTenddate();
						if ((beginDate == null || beginDate.toString().compareTo(dBillDate) <= 0)
								&& (endDate == null || endDate.toString().compareTo(dBillDate) >= 0)
							) {
							buylargessBVOList.add(buyLarBVO[j]);
						}
					}
					HashMap hash=new HashMap();
					ArrayList pk_invmanArry=new ArrayList();
					for(int k=0;k<buylargessBVOList.size();k++){
						String pk_invmandoc=((BuylargessBVO)buylargessBVOList.get(k)).getPk_invmandoc();
						if(hash.containsKey(pk_invmandoc)){
							ArrayList arryKinds=(ArrayList) hash.get(pk_invmandoc);
							arryKinds.add(buylargessBVOList.get(k));
							hash.put(pk_invmandoc, arryKinds);
						}else{
							ArrayList buylargessTemp=new ArrayList();
							buylargessTemp.add(buylargessBVOList.get(k));
							hash.put(pk_invmandoc,buylargessTemp);
							pk_invmanArry.add(pk_invmandoc);
						}
					}
					ArrayList listLast=new ArrayList();
					for(int m=0;m<pk_invmanArry.size();m++){
						String pk_invmandoc=(String) pk_invmanArry.get(m);
						ArrayList list=(ArrayList) hash.get(pk_invmandoc);
						
						UFDate maxBegin=((BuylargessBVO) list.get(0)).getTbegdate();
						ArrayList ListTemp=new ArrayList();
						ListTemp.add(list.get(0));
						for(int n=1;n<list.size();n++){
							UFDate nextNew=((BuylargessBVO) list.get(n)).getTbegdate();
							if(maxBegin.compareTo(nextNew)<0){
								ListTemp.clear();
								ListTemp.add(list.get(n));
							}else if(maxBegin.compareTo(nextNew)==0){
								ListTemp.add(list.get(n));
							}
						}
						if(ListTemp.size()>1){
							UFDate minEnd=((BuylargessBVO) ListTemp.get(0)).getTenddate();
							ArrayList ListDate=new ArrayList();
							ListDate.add(ListTemp.get(0));
							for(int p=1;p<ListTemp.size();p++){
								UFDate nextEndNew=((BuylargessBVO) ListTemp.get(p)).getTenddate();
								if(minEnd.compareTo(nextEndNew)>0){
									ListDate.clear();
									ListDate.add(ListTemp.get(p));
								}else if(minEnd.compareTo(nextEndNew)==0){
									ListDate.add(ListTemp.get(p));
								}
							}
							listLast.add(ListDate.get(0));
						}else{
							listLast.add(ListTemp.get(0));
						}
					}
					BuylargessBVO [] lastBVO=new  BuylargessBVO[listLast.size()];
					if(listLast.size()>0){
						listLast.toArray(lastBVO);
					}
					bvos[i].setChildrenVO(lastBVO);
					//周长胜
					m_htLargess.put(((BuylargessHVO) bvos[i].getParentVO())
							.getPk_invmandoc(), bvos[i]);
					
				}
			}
		}

		return al;

	}
	
	/**
	 * 通灵翠钻--根据指定存货管理pk，获得存货所属分类编码
	 * @param refPks
	 * @return ArrayList [0]--存货类编码list；[1]--<存货id,存货类编码>HashMap
	 */
	private ArrayList getInvclCode(String[] refPks){
		HashMap invid_invclcode = new HashMap();//<invid,invclcode>
		ArrayList linvclcode = new ArrayList();//存放存货类
		try {
			for (String refPk : refPks){
				//存货基本档案
				Object[] invbasid = (Object[])nc.ui.scm.pub.CacheTool
				.getColumnValue("bd_invmandoc", "pk_invmandoc","pk_invbasdoc",new String[]{refPk});
				
				//存货分类pk
				Object[] invclpk =  (Object[])nc.ui.scm.pub.CacheTool
				.getColumnValue("bd_invbasdoc", "pk_invbasdoc","pk_invcl",new String[]{invbasid[0].toString()});
				
				//存货分类编码
				Object[] invclcode = (Object[])nc.ui.scm.pub.CacheTool
				.getColumnValue("bd_invcl", "pk_invcl","invclasscode",new String[]{invclpk[0].toString()});
				
				invid_invclcode.put(refPk,invclcode[0].toString() );
				linvclcode.add(invclcode[0].toString());
			}
		} catch (Exception e) {
			SCMEnv.out(e);
			uipanel.showErrorMessage("存货买赠：获取存货所属分类编码失败！");
		}
		ArrayList sinvclcode = new ArrayList();//返回结果
		sinvclcode.add(linvclcode);//存货类编码list
		sinvclcode.add(invid_invclcode);//<存货id,存货类编码>
		return sinvclcode;
	}

	/**
	 * 设置原来存货，即非买赠、非捆绑，顺序由上到下。记录count值
	 * 
	 * @param refPks
	 * @param irow
	 * @param bCopy
	 * @return
	 */
	private int setBodyByOrginvs(String[] refPks, Hashtable htresult, int irow)
			throws Exception {

		if (refPks == null || refPks.length == 0)
			return 0;

		int count = refPks.length;

		// Invcount += count;

		// 增加新空行
		if (irow == getRowCount() - 1) {
			uipanel.addNullLine(irow, count - 1);
		} else {
			uipanel.insertNullLine(irow, count - 1);
		}
		afterInvEditClear(irow);

		// 纪录需要执行公式的行
		InvVO tmpvo = null;
		setBodyDefaultData(irow, irow + count);
		for (int i = 0; i < count; i++) {

			tmpvo = (InvVO) htresult.get(refPks[i]);
			setBodyValues(tmpvo, irow + i, irow);
		}

		return count;

	}

	/**
	 * v5.5 数量、单价、金额均不清空，保留上次结果
	 * 存货编辑后事件处理。清空数据项 创建日期：(2001-6-23 13:42:53)
	 */
	private void afterInvEditClear(int iRowIndex) {
		// 清除数据
		String[] clearCol = {
				"scalefactor",
				"fixedflag",
				"npacknumber",
				"nqtscalefactor",
				"bqtfixedflag",
				
				/*"nnumber",
				"norgviaprice",
				"norgviapricetax",
				"noriginalcurprice",
				"noriginalcurtaxprice",
				"noriginalcurnetprice",
				"noriginalcurtaxnetprice",
				"noriginalcurtaxmny",
				"noriginalcurmny",
				"noriginalcursummny",
				"noriginalcurdiscountmny",
				"nquoteunitnum",
				"norgqttaxprc", // 报价单位含税单价
				"norgqtprc", // 报价单位无税单价
				"norgqttaxnetprc", // 报价单位含税净价
				"norgqtnetprc", // 报价单位无税净价
				*/
				
				"cbatchid", "vfree0", "vfree1", "vfree2", "vfree3", "vfree4",
				"vfree5", "cinvclassid", "cinvclasscode", "cpricepolicyid",
				"cpricepolicy", "cpriceitemid", "cpriceitem",
				"cpriceitemtable", "cpriceitemtablename", "cpricecalproc",
				"cinventoryid1", // 发货公司的存货管理档案
				// "cchantypeid",//渠道类型ID
				// "cchantype",//渠道类型
				"bsafeprice", // 是否参与价保
				"ntaldcnum", // 已参与价保主数量
				"nasttaldcnum", // 已参与价保辅数量
				"ntaldcmny", // 价保金额
				"breturnprofit", // 是否参与返利
				"nretprofnum", // 已参与返利主数量
				"nastretprofnum", // 已参与返利辅数量
				"nretprofmny", // 返利金额

				"cpricepolicyid", // 价格政策ID
				"cpricepolicy", // 价格政策
				"cpriceitemid", // 价格项目ID
				"cpriceitem", // 价格项目
				"cpriceitemtable", // 价目表id
				"cpriceitemtablename", // 价目表
				"cpricecalproc", // 价格计算过程id
				"cpricecalprocname", // 价格计算过程

				"cquoteunitid", "cquoteunit", "bindpricetype"

		};

		Object ctinvclassid = null;
		try {
			ctinvclassid = getBodyValueAt(iRowIndex, "ctinvclassid");
		} catch (Exception e) {
			ctinvclassid = null;
		}

		if (ctinvclassid != null && ctinvclassid.toString().length() != 0) {

			// 存货类合同
			if (uipanel.SO_17.booleanValue()) {
				// 执行合同价格
				clearCol = new String[] {
						"scalefactor",
						"fixedflag",
						// "nnumber",
						// "npacknumber",
						"noriginalcurtaxmny", "noriginalcurmny",
						"noriginalcursummny", "noriginalcurdiscountmny",
						"cbatchid", "vfree0", "vfree1", "vfree2", "vfree3",
						"vfree4", "vfree5",
						"cinvclassid",
						"cinvclasscode",
						// v30add
						"nqtscalefactor", "bqtfixedflag", "cpricepolicyid",
						"cpricepolicy", "cpriceitemid", "cpriceitem",
						"cpriceitemtable", "cpriceitemtablename",
						"cpricecalproc", "cinventoryid1", // 发货公司的存货管理档案
						// "cchantypeid",//渠道类型ID
						// "cchantype",//渠道类型
						"bsafeprice", // 是否参与价保
						"ntaldcnum", // 已参与价保主数量
						"nasttaldcnum", // 已参与价保辅数量
						"ntaldcmny", // 价保金额
						"breturnprofit", // 是否参与返利
						"nretprofnum", // 已参与返利主数量
						"nastretprofnum", // 已参与返利辅数量
						"nretprofmny", // 返利金额

						"cquoteunitid", "cquoteunit"

				};
			}

		} else if (getSouceBillType().equals(SaleBillType.SoContract)
				|| getSouceBillType().equals(SaleBillType.SoInitContract)) {

			// 清除数据
			clearCol = new String[] { "scalefactor", "fixedflag",
					"nqtscalefactor", "bqtfixedflag",

					"noriginalcurtaxmny", "noriginalcurmny",
					"noriginalcursummny", "noriginalcurdiscountmny",

					"cbatchid", "vfree0", "vfree1", "vfree2", "vfree3",
					"vfree4", "vfree5", "cinvclassid", "cinvclasscode",
					"cpricepolicyid", "cpricepolicy", "cpriceitemid",
					"cpriceitem", "cpriceitemtable", "cpriceitemtablename",
					"cpricecalproc", "cinventoryid1", // 发货公司的存货管理档案
					// "cchantypeid",//渠道类型ID
					// "cchantype",//渠道类型
					"bsafeprice", // 是否参与价保
					"ntaldcnum", // 已参与价保主数量
					"nasttaldcnum", // 已参与价保辅数量
					"ntaldcmny", // 价保金额
					"breturnprofit", // 是否参与返利
					"nretprofnum", // 已参与返利主数量
					"nastretprofnum", // 已参与返利辅数量
					"nretprofmny", // 返利金额

					"cpricepolicyid", // 价格政策ID
					"cpricepolicy", // 价格政策
					"cpriceitemid", // 价格项目ID
					"cpriceitem", // 价格项目
					"cpriceitemtable", // 价目表id
					"cpriceitemtablename", // 价目表
					"cpricecalproc", // 价格计算过程id
					"cpricecalprocname", // 价格计算过程

					"cquoteunitid", "cquoteunit" };

		}
		getBillCardTools().clearRowData(iRowIndex, clearCol);
		if (nc.ui.pub.pf.PfUtilClient.makeFlag) {
			// 如果是自制单据清该行单据类型
			String[] clearCol2 = { "creceipttype", "csourcebillid",
					"csourcebillbodyid" };
			getBillCardTools().clearRowData(iRowIndex, clearCol2);
		}
	}

	/**
	 * 新增行设置表体默认数据。
	 * 
	 * 创建日期：(2001-8-27 10:05:59)
	 * 
	 */
	public void setBodyDefaultData(int istartrow, int iendrow) {

		SOToolVO vo = null;
		String crecaddrnode = null;
		String crecaddrnodename = null;

		try {
			// 收货地址
			UIRefPane refVreceiveaddress = (UIRefPane) getHeadItem(
					"vreceiveaddress").getComponent();
			String vreceiveaddressid = refVreceiveaddress.getRefPK();

			if (vreceiveaddressid != null
					&& vreceiveaddressid.trim().length() > 0) {

				String vreceiveaddress = (String) getBodyValueAt(istartrow,
						"vreceiveaddress");

				setBodyValueAt(vreceiveaddressid, istartrow, "vreceiveaddress");
				String[] fs = {
						"crecaddrnode->getColValue(bd_custaddr,pk_address,pk_custaddr,vreceiveaddress)",
						"crecaddrnodename->getColValue(bd_address,addrname,pk_address,crecaddrnode)" };
				getBillModel().execFormulas(istartrow, fs);
				crecaddrnode = getBillCardTools().getBodyStringValue(istartrow,
						"crecaddrnode");
				crecaddrnodename = getBillCardTools().getBodyStringValue(
						istartrow, "crecaddrnodename");

				setBodyValueAt(vreceiveaddress, istartrow, "vreceiveaddress");

			}

		} catch (Exception e) {
			SCMEnv.out(e);
			// e.printStackTrace();
		}

		if (istartrow > 0) {

			String vreceiveaddress = (String) getBodyValueAt(0,
					"vreceiveaddress");

			String creceiptareaid = (String) getBodyValueAt(0, "creceiptareaid");

			String creceiptareaname = (String) getBodyValueAt(0,
					"creceiptareaname");

			String crecaddrnode_body = (String) getBodyValueAt(0,
					"crecaddrnode");

			String crecaddrnodename_body = (String) getBodyValueAt(0,
					"crecaddrnodename");

			for (int i = istartrow; i < iendrow; i++) {

				vo = getBillCardTools().getBodyDefaultData(i);
				getBillCardTools().setBodyValuesByVO(i, vo);
				setBodyValueAt(crecaddrnode, i, "crecaddrnode");
				setBodyValueAt(crecaddrnodename, i, "crecaddrnodename");

				if (SoVoTools.isEmptyString((String) getBodyValueAt(i,
						"vreceiveaddress"))) {
					setBodyValueAt(vreceiveaddress, i, "vreceiveaddress");
				}

				if (SoVoTools.isEmptyString((String) getBodyValueAt(i,
						"creceiptareaid"))) {
					setBodyValueAt(creceiptareaid, i, "creceiptareaid");
					setBodyValueAt(creceiptareaname, i, "creceiptareaname");
				}

				if (SoVoTools.isEmptyString((String) getBodyValueAt(i,
						"crecaddrnode"))) {
					setBodyValueAt(crecaddrnode_body, i, "crecaddrnode");
					setBodyValueAt(crecaddrnodename_body, i, "crecaddrnodename");
				}
				ctlUIOnCconsignCorpChg(i);

			}

		} else {
			for (int i = istartrow; i < iendrow; i++) {

				vo = getBillCardTools().getBodyDefaultData(i);
				getBillCardTools().setBodyValuesByVO(i, vo);
				setBodyValueAt(crecaddrnode, i, "crecaddrnode");
				setBodyValueAt(crecaddrnodename, i, "crecaddrnodename");
				ctlUIOnCconsignCorpChg(i);

			}
		}

		// 整单折扣
		afterDiscountrateEdit(istartrow, iendrow);

		updateUI();

	}

	private void setBodyValues(InvVO invvo, int iRow, int iCurrow) {

		// 设置存货相关信息
		setBodyValueByInvVO(invvo, iRow);
		// 设置单位信息
		setAssistUnit(iRow);

		// 设置自由项
		alInvs.add(iRow, invvo);

		// 如果是合同
		if (getSouceBillType(iCurrow).equals(SaleBillType.SoContract)
				|| getSouceBillType(iCurrow)
						.equals(SaleBillType.SoInitContract)) {
			String[] sSource = getNeedSetNullSourceItems();
			for (int k = 0; k < sSource.length; k++) {
				setBodyValueAt(getBodyValueAt(iCurrow, sSource[k]), iRow,
						sSource[k]);
			}
		}

	}

	/**
	 * 是否辅计量。 创建日期：(2001-11-30 15:20:14)
	 * 
	 * @param row
	 *            int
	 */
	public boolean setAssistUnit(int row) {
		// 是否辅计量
		UFBoolean assistunit = new UFBoolean(false);
		if (getBodyValueAt(row, "assistunit") != null)
			assistunit = new UFBoolean(getBodyValueAt(row, "assistunit")
					.toString());

		boolean bEdit = true;
		if (!assistunit.booleanValue()) {
			bEdit = false;
		}
		setCellEditable(row, "cpackunitname", bEdit);
		setCellEditable(row, "npacknumber", bEdit);

		setCellEditable(row, "cquoteunit", bEdit);

		String cunitid = (String) getBodyValueAt(row, "cunitid");

		// 报价单位
		String cquoteunitid = getBillCardTools().getBodyStringValue(row,
				"cquoteunitid");

		if (cquoteunitid == null || cquoteunitid.trim().length() <= 0) {

			setBodyValueAt(cunitid, row, "cquoteunitid");
			setBodyValueAt(getBodyValueAt(row, "cunitname"), row, "cquoteunit");
			setBodyValueAt(new UFDouble(1.0), row, "nqtscalefactor");
			setBodyValueAt(new UFBoolean(true), row, "bqtfixedflag");

		}

		return bEdit;
	}

	/**
	 * 得到复制行时，需要清空的项
	 * 
	 * wsy 2005-10-10
	 * 
	 */
	public String[] getNeedSetNullSourceItems() {

		return new String[] { "ct_name", "ct_manageid", "ctinvclassid",

				// 合同号
				"ct_code",
				// 合同存货类
				"ctinvclass", "creceipttype", "creceipttype",
				"csourcebillbodyid", "csourcebillid",

				"cbomorderid", "cbomordercode"

		};

	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-11-7 10:30:37)
	 * 
	 * @param vo
	 *            InvVO
	 * @param row
	 *            int
	 */
	protected void setBodyValueByInvVO(InvVO vo, int row) {
		if (vo == null)
			return;
		if (row < 0)
			return;
		String temp = "";
		setBodyValueAt(vo.getCinventoryid(), row, "cinventoryid");
		setBodyValueAt(vo.getCinvmanid(), row, "cinvbasdocid");
		setBodyValueAt(vo.getCinventorycode(), row, "cinventorycode");
		setBodyValueAt(vo.getInvname(), row, "cinventoryname");
		if (vo.getInvspec() != null)
			temp += vo.getInvspec();
		if (vo.getInvtype() != null)
			temp += vo.getInvtype();
		setBodyValueAt(temp, row, "GGXX");
		setBodyValueAt(vo.getInvspec(), row, "cinvspec");
		setBodyValueAt(vo.getInvtype(), row, "cinvtype");
		setBodyValueAt(vo.getPk_measdoc(), row, "cunitid");
		setBodyValueAt(vo.getMeasdocname(), row, "cunitname");
		setBodyValueAt(vo.getDiscountflag(), row, "discountflag");

		if (vo.getIsAstUOMmgt() != null && vo.getIsAstUOMmgt().intValue() == 1)
			setBodyValueAt("Y", row, "assistunit");
		else
			setBodyValueAt("N", row, "assistunit");
		setBodyValueAt(vo.getCastunitid(), row, "cpackunitid");
		setBodyValueAt(vo.getCastunitname(), row, "cpackunitname");

		if (vo.getLaborflag() != null && vo.getLaborflag().booleanValue())
			setBodyValueAt("Y", row, "laborflag");
		else
			setBodyValueAt("N", row, "laborflag");

		if (vo.getPk_measdoc() != null && vo.getCastunitid() != null) {
			if (vo.getPk_measdoc().equals(vo.getCastunitid())) {
				// 换算率
				setBodyValueAt(new UFDouble(1), row, "scalefactor");
				// 是否固定换算率
				setBodyValueAt("Y", row, "fixedflag");

			} else {
				// 换算率
				setBodyValueAt(vo.getHsl(), row, "scalefactor");
				// 是否固定换算率
				// getBodyValueAt(0,"fixedflag");
				if (vo.getIsSolidConvRate() != null
						&& vo.getIsSolidConvRate().intValue() == 1)
					setBodyValueAt("Y", row, "fixedflag");
				else
					setBodyValueAt("N", row, "fixedflag");
			}

			// 设置报价计量单位与主计量相同
			setBodyValueAt(vo.getPk_measdoc(), row, "cquoteunitid");
			setBodyValueAt(getBodyValueAt(row, "cunitname"), row, "cquoteunit");
			setBodyValueAt(new UFDouble(1), row, "nqtscalefactor");
			setBodyValueAt("Y", row, "bqtfixedflag");

		} else {
			setBodyValueAt(null, row, "cpackunitid");
			setBodyValueAt(null, row, "cpackunitname");

			// 设置报价计量单位与主计量相同
			setBodyValueAt(vo.getPk_measdoc(), row, "cquoteunitid");
			setBodyValueAt(getBodyValueAt(row, "cunitname"), row, "cquoteunit");
			setBodyValueAt(new UFDouble(1.0), row, "nqtscalefactor");
			setBodyValueAt("Y", row, "bqtfixedflag");
		}

	}

	/**
	 * 设置存货的捆绑信息,依次增加
	 * 
	 * @param refPks
	 * @param irow
	 * @param bCopy
	 * @return
	 */
	private int setBodyByBindinvs(int irow, Hashtable htresult,
			Hashtable htbinds) throws Exception {

		String sInvPk = (String) getBodyValueAt(irow, "cinventoryid");
		if (sInvPk == null)
			return 0;

		UFDouble udnum = (UFDouble) getBodyValueAt(irow, "nnumber");
		if (udnum == null)
			udnum = new UFDouble(1);

		if (!htbinds.containsKey(sInvPk))
			return 0;

		int count = 0;
		// 初始化第一层捆绑件
		ArrayList alneeds = new ArrayList();// 存捆绑件
		ArrayList altmp = new ArrayList();
		altmp.add(sInvPk);
		altmp.add(udnum);
		alneeds.add(altmp);
		alneeds.add(new ArrayList());

		Integer Icount = new Integer(count);
		ArrayList acount = new ArrayList();
		acount.add(Icount);
		long s = System.currentTimeMillis();
		SCMEnv.out("##连接捆绑件开始");

		// 递归树 ,继续寻找其它捆绑件
		UnitBinds(alneeds, htbinds, acount);
		Icount = (Integer) acount.get(0);
		count = Icount.intValue();
		SCMEnv.out("##连接捆绑件用时[" + (System.currentTimeMillis() - s) / 1000.0
				+ "s]");
		s = System.currentTimeMillis();

		// 增加新空行
		if (irow == getRowCount() - 1) {
			uipanel.addNullLine(irow, count);
		} else {
			uipanel.insertNullLine(irow, count);
		}

		ArrayList allcurrow = new ArrayList();
		allcurrow.add(new Integer(irow));
		setBodyDefaultData(irow, irow + count);

		setBinds(alneeds, htresult, allcurrow, irow);
		SCMEnv.out("##设置捆绑件明细用时[" + (System.currentTimeMillis() - s) / 1000.0
				+ "s]");

		return count;

	}

	/**
	 * @param alneed
	 *            需要查询捆绑
	 * @param
	 * @param htBinds
	 *            捆绑的集合
	 * @return
	 */
	private void UnitBinds(ArrayList alneed, Hashtable htBinds, ArrayList count) {
		Object o = null;
		String sPk = null;
		InvbindleVO bvo = null;
		Vector vt = null;
		InvbindleVO[] bvos = null;
		ArrayList alold = null;
		ArrayList altmp = null;
		UFDouble udnum = null;

		o = alneed.get(0);
		if (o instanceof ArrayList) {
			sPk = (String) ((ArrayList) o).get(0);

			udnum = (UFDouble) ((ArrayList) o).get(1);
		} else if (o instanceof InvbindleVO) {
			bvo = (InvbindleVO) o;
			sPk = bvo.getPk_bindleinvmandoc();
			udnum = bvo.getBindlenum() == null ? new UFDouble(1) : bvo
					.getBindlenum();
		}
		if (htBinds.containsKey(sPk)) {
			vt = (Vector) htBinds.get(sPk);
			bvos = new InvbindleVO[vt.size()];
			vt.copyInto(bvos);
			// 捆绑的数量
			// 记录数量
			Integer icount = (Integer) count.get(0);
			icount = new Integer(icount.intValue() + vt.size());
			count.clear();
			count.add(icount);

			alold = (ArrayList) alneed.get(1);
			for (int j = 0; j < vt.size(); j++) {
				altmp = new ArrayList();
				bvo = (InvbindleVO) bvos[j].clone();
				bvo.setBindlenum((bvo.getBindlenum() == null ? new UFDouble(1)
						: bvo.getBindlenum()).multiply(udnum));

				altmp.add(bvo);
				altmp.add(new ArrayList());
				alold.add(altmp);
				UnitBinds(altmp, htBinds, count);
			}
		}
	}

	/**
	 * @param alnees
	 *            所有存货
	 * @param htLargess
	 *            赠
	 * @param irow
	 *            当前行
	 * @return
	 */
	private void setBinds(ArrayList alneeds, Hashtable htresult,
			ArrayList airow, int irow) {

		InvVO tmpvo = null;
		// 应从当前行下一行开始
		String sPk = null;
		// ArrayList altmp =null;
		Object o = null;
		InvbindleVO invbvo = null;
		for (int i = 0; i < alneeds.size(); i++) {

			// altmp = (ArrayList)alneeds.get(0);
			o = alneeds.get(i);
			invbvo = null;

			if (o instanceof InvbindleVO) {
				invbvo = (InvbindleVO) o;
				// 捆绑件
				// 取全局行号
				Integer iirow = (Integer) airow.get(0);
				int icurrow = iirow.intValue() + 1;
				airow.clear();
				airow.add(new Integer(icurrow));

				sPk = invbvo.getPk_bindleinvmandoc();
				tmpvo = (InvVO) htresult.get(sPk);
				setBodyValues(tmpvo, icurrow, irow);
				if (invbvo != null) {
					setBindingsItems(invbvo, icurrow,irow);
				}

			} else if (o instanceof ArrayList) {
				setBinds((ArrayList) o, htresult, airow, irow);
			}

		}

	}

	/**
	 * 设置捆绑项
	 * @param imainrow 主存货行
	 * @param invvo
	 * @param irow 捆绑行
	 */
	private void setBindingsItems(InvbindleVO invvo, int irow,int imainrow) {
		// 设置数量
		setBodyValueAt(invvo.getBindlenum(), irow, "nnumber");
		
		// 设置默认税率
		ArrayList airow = new ArrayList();
		airow.add(irow);
		loadTaxtrate(airow);
		
		 /** v5.3单价金额算法变更 */
		// System.out.println("setBindingsItems----RelationsCal.calculate：#"+irow+"#"+"nnumber"+"#-->调用开始！");
		RelationsCal.calculate(irow, this.oldValue, this, SaleorderBVO.getCalculatePara("nnumber", null, uipanel.SA_02,
				uipanel.SO40), "nnumber", SaleorderBVO.getKeys(), SaleorderBVO.getField(),
				SaleorderBVO.class.getName(), SaleorderHVO.class.getName(), null);
		
		// 设置标志
		setBodyValueAt(new UFBoolean(true), irow, "bbindflag");
		// 设置主存货行
		setBodyValueAt(getBodyValueAt(imainrow, "crowno"), irow,
				"clargessrowno");
		// 设置类型
		setBodyValueAt(invvo.getPricetype(), irow, "bindpricetype");
		// 使用捆绑件
		if (invvo.getPricetype().intValue() == IInvManDocConst.PRICE_TYPE_BINDLE) {
			// 设置价格
			setBodyValueAt(invvo.getPrice(), irow, "noriginalcurtaxprice");
			afterNumberEditLogic(irow, "noriginalcurtaxprice", false);
		}
		
		// 设置原始的寻价单价
		setBodyValueAt(getBillCardTools().getBodyUFDoubleValue(
				irow, "noriginalcurprice"), irow,
				"nqtorgprc");
		setBodyValueAt(getBillCardTools().getBodyUFDoubleValue(
				irow, "noriginalcurtaxprice"), irow,
				"nqtorgtaxprc");
		
	}

	/**
	 * 设置存货的捆绑信息,依次增加
	 * 
	 * @param refPks
	 * @param irow
	 * @param bCopy
	 * @return
	 */
	private int setBodyByLargessinvs(int irow, Hashtable htresult,
			Hashtable htLargess) throws Exception {

		String sInvPk = (String) getBodyValueAt(irow, "cinventoryid")
				+ (String) getBodyValueAt(irow, "cquoteunitid");

		if (sInvPk == null)
			return 0;
		if (!htLargess.containsKey(sInvPk))
			return 0;
		//
		ArrayList allargess = (ArrayList) htLargess.get(sInvPk);
		// 报价单位数量
		UFDouble nnum = (UFDouble) getBodyValueAt(irow, "nquoteunitnum");
		if (nnum == null)
			nnum = new UFDouble(1);

		BuylargessVO vo = null;
		BuylargessHVO bhvo = null;

		BuylargessVO votmp = null;
		BuylargessHVO btmphvo = null;

		// 得到客户组编码
		Vector vt = new Vector();
		for (int i = 0, isize = allargess.size(); i < isize; i++) {
			votmp = (BuylargessVO) allargess.get(i);
			btmphvo = (BuylargessHVO) votmp.getParentVO();
			if (btmphvo.getPk_custgroup() != null
					&& btmphvo.getPk_custgroup().trim().length() > 0)
				vt.add(btmphvo.getPk_custgroup());
		}
		HashMap hmp = null;
		if (vt.size() > 0) {
			String[] skeys = new String[vt.size()];
			vt.copyInto(skeys);
			hmp = getCustgroupCodes(skeys);
		} else {
			hmp = new HashMap();
		}

		String scustgroup = null;
		String stmpcustgroup = null;
		for (int i = 0, isize = allargess.size(); i < isize; i++) {
			votmp = (BuylargessVO) allargess.get(i);
			btmphvo = (BuylargessHVO) votmp.getParentVO();

			if (btmphvo.getNbuynum().compareTo(nnum) > 0)
				continue;
			if (vo == null)
				vo = votmp;
			else {
				// 按客户->客户分组->上层客户分组
				bhvo = (BuylargessHVO) vo.getParentVO();
				// 如果新行的客户不为空，则应为首选
				if (btmphvo.getPk_cumandoc() != null
						&& btmphvo.getPk_cumandoc().trim().length() > 0) {
					vo = votmp;
				} else if (btmphvo.getPk_custgroup() != null
						&& btmphvo.getPk_custgroup().trim().length() > 0) {
					// 如果新行为客户分组
					// 1、旧行为客户分组,取下级
					if (bhvo.getPk_custgroup() != null
							&& bhvo.getPk_custgroup().trim().length() > 0) {
						scustgroup = (String) hmp.get(bhvo.getPk_custgroup());
						stmpcustgroup = (String) hmp.get(btmphvo
								.getPk_custgroup());
						if (scustgroup != null
								&& stmpcustgroup != null
								&& scustgroup.trim().length() < stmpcustgroup
										.trim().length())
							vo = votmp;
					}
					// 2、旧行为客户分组客户均为空
					else if ((bhvo.getPk_cumandoc() == null || bhvo
							.getPk_cumandoc().trim().length() == 0)
							&& (bhvo.getPk_custgroup() == null || bhvo
									.getPk_custgroup().trim().length() == 0)) {
						vo = votmp;
					}
				}

			}

		}
		if (vo == null)
			return 0;

		int count = vo.getChildrenVO().length;
		// 增加新空行
		if (irow == getRowCount() - 1) {
			uipanel.addNullLine(irow, count);
		} else {
			uipanel.insertNullLine(irow, count);
		}

		// 只设置新加行的值
		setBodyDefaultData(irow + 1, irow + count);

		setLargess(vo, htresult, irow);

		return count;

	}

	private HashMap getCustgroupCodes(String[] sKeys) {

		HashMap hp = new HashMap();
		if (sKeys == null || sKeys.length == 0)
			return hp;
		String swheres = "pk_defdoc in(";
		for (int i = 0; i < sKeys.length; i++) {
			if (i > 0)
				swheres += ",";

			swheres += "'" + sKeys[i] + "'";
		}
		swheres += ")";
		ArrayList o = (ArrayList) DBCacheFacade.runQuery(
				"select pk_defdoc,doccode from bd_defdoc where " + swheres,
				new ArrayListProcessor());

		Object[] o1 = null;
		if (o == null || o.size() == 0)
			return hp;
		for (int i = 0; i < o.size(); i++) {
			o1 = (Object[]) o.get(i);

			hp.put(o1[0], o1[1]);
		}
		return hp;
	}

	/**
	 * @param alnees
	 *            所有存货
	 * @param htLargess
	 *            赠
	 * @param irow
	 *            当前行
	 * @return
	 */
	private void setLargess(BuylargessVO bvo, Hashtable htresult, int irow) {

		if (bvo == null)
			return;

		BuylargessBVO[] bodys = (BuylargessBVO[]) bvo.getChildrenVO();
		InvVO tmpvo = null;

		if (bodys != null && bodys.length > 0) {
			for (int j = 0; j < bodys.length; j++) {
				tmpvo = (InvVO) htresult.get(bodys[j].getPk_invmandoc());
				setBodyValues(tmpvo, j + irow + 1, irow);
				// 买赠件
				setLargessItems((BuylargessHVO) bvo.getParentVO(), bodys[j], j
						+ irow + 1, irow);

			}
		}

	}

	/**
	 * @param bvo
	 * @param irow
	 *            当前行
	 * @param imainrow
	 *            主存货行
	 */
	private void setLargessItems(BuylargessHVO hvo, BuylargessBVO bvo,
			int irow, int imainrow) {
		// 1、设置主存货行
		setBodyValueAt(getBodyValueAt(imainrow, "crowno"), irow,
				"clargessrowno");
		// 2、设置赠品标志
		setBodyValueAt(new UFBoolean(true), irow, "blargessflag");

		setBodyValueAt(bvo.getCunitid(), irow, "cquoteunitid");

		afterUnitEdit(irow, "cquoteunitid");
		// 设置数量、金额
		// 1. 如果控制为数量上限：=min(捆绑件设置中的数量*主件数量, 上限值)
		// 2. 如果控制为金额上限：(金额=订单中的价税合计；单价=含税单价(原币))
		// a) 数量*单价>金额上限？金额=金额上限,数量=金额/单价
		// 主存货行数量
		UFDouble umainnum = (UFDouble) getBodyValueAt(imainrow, "nquoteunitnum");
		if (umainnum == null)
			umainnum = new UFDouble(1);
		// 除基数
		umainnum = umainnum.div(hvo.getNbuynum());

		if (bvo.getFtoplimittype() != null
				&& bvo.getFtoplimittype().intValue() == 0
				&& bvo.getNnum().multiply(umainnum).multiply(
						bvo.getNprice() == null ? new UFDouble(0) : bvo
								.getNprice()).compareTo(
						bvo.getNtoplimitvalue() == null ? new UFDouble(0) : bvo
								.getNtoplimitvalue()) > 0) {// 控制金额
			// 数量*单价>金额
			setBodyValueAt(bvo.getNtoplimitvalue() == null ? new UFDouble(0)
					: bvo.getNtoplimitvalue(), irow, "noriginalcursummny");
			// setBodyValueAt(bvo.getNnum().multiply(umainnum),irow,"nquoteunitnum");
			setBodyValueAt((bvo.getNtoplimitvalue() == null ? new UFDouble(0)
					: bvo.getNtoplimitvalue()).div(bvo.getNprice()), irow,
					"nquoteunitnum");
			
		    /** v5.3单价金额算法变更 */
			// System.out.println("setLargessItems----RelationsCal.calculate：#"+irow+"#"+"nquoteunitnum"+"#-->调用开始！");
			RelationsCal.calculate(irow, this.oldValue, this, SaleorderBVO.getCalculatePara("nquoteunitnum", null,
					uipanel.SA_02, uipanel.SO40), "nquoteunitnum", SaleorderBVO.getKeys(), SaleorderBVO.getField(),
					SaleorderBVO.class.getName(), SaleorderHVO.class.getName(), null);
			
			
			afterNumberEditLogic(irow, "noriginalcursummny", false);

		} else if (bvo.getFtoplimittype() != null
				&& bvo.getFtoplimittype().intValue() == 1
				&& bvo.getNnum().multiply(umainnum).compareTo(
						bvo.ntoplimitvalue) > 0) {// 控制数量
			setBodyValueAt(bvo.ntoplimitvalue, irow, "nquoteunitnum");
			afterNumberEdit(new int[] { irow }, "nquoteunitnum", null, false,
					true);
			setBodyValueAt(bvo.getNprice() == null ? new UFDouble(0) : bvo
					.getNprice(), irow, "norgqttaxprc");

			 /** v5.3单价金额算法变更 */
			// System.out.println("setLargessItems----RelationsCal.calculate：#"+irow+"#"+"nquoteunitnum"+"#-->调用开始！");
			RelationsCal.calculate(irow, this.oldValue, this, SaleorderBVO.getCalculatePara("nquoteunitnum", null,
					uipanel.SA_02, uipanel.SO40), "nquoteunitnum", SaleorderBVO.getKeys(), SaleorderBVO.getField(),
					SaleorderBVO.class.getName(), SaleorderHVO.class.getName(), null);

			afterNumberEditLogic(irow, "norgqttaxprc", false);

		} else {
			// 直接设置数量
			setBodyValueAt(bvo.getNnum().multiply(umainnum), irow,
					"nquoteunitnum");
			setBodyValueAt(bvo.getNnum().multiply(umainnum)
					.multiply(
							bvo.getNprice() == null ? new UFDouble(0) : bvo
									.getNprice()), irow, "noriginalcursummny");
			// 3、设置单价 ；单价=含税单价(原币)
			setBodyValueAt(bvo.getNprice() == null ? new UFDouble(0) : bvo
					.getNprice(), irow, "norgqttaxprc");
			
			 /** v5.3单价金额算法变更 */
			//System.out.println("setLargessItems----RelationsCal.calculate：#"+irow+"#"+"nquoteunitnum"+"#-->调用开始！");
			
			RelationsCal.calculate(irow, this.oldValue, this, SaleorderBVO.getCalculatePara("nquoteunitnum", null,
					uipanel.SA_02, uipanel.SO40), "nquoteunitnum", SaleorderBVO.getKeys(), SaleorderBVO.getField(),
					SaleorderBVO.class.getName(), SaleorderHVO.class.getName(), null);
			
			afterNumberEditLogic(irow, "norgqttaxprc", false);

		}
		
		// 设置原始的寻价单价
		setBodyValueAt(getBillCardTools().getBodyUFDoubleValue(
				irow, "noriginalcurprice"), irow,
				"nqtorgprc");
		setBodyValueAt(getBillCardTools().getBodyUFDoubleValue(
				irow, "noriginalcurtaxprice"), irow,
				"nqtorgtaxprc");
		
		
		/** 
		 * 赠品参数的单价金额不保留在订单上.销售订单设置了买赠设置后,带出的赠品仍然保留了单价金额. 
		 * v5.3 jiangzhe zhangcheng 
		 */
		UFBoolean largess = (UFBoolean) getBillCardTools().getBodyUFBooleanValue(irow, "blargessflag");
		boolean blargess = largess == null ? false : largess.booleanValue();

		if (blargess && !uipanel.SO59.booleanValue()) {
			setBodyValueAt(SoVoConst.duf0, irow, "noriginalcurprice");
			setBodyValueAt(SoVoConst.duf0, irow, "noriginalcurtaxprice");
			setBodyValueAt(SoVoConst.duf0, irow, "noriginalcurnetprice");
			setBodyValueAt(SoVoConst.duf0, irow, "noriginalcurtaxnetprice");
			setBodyValueAt(SoVoConst.duf0, irow, "norgqttaxprc");
			setBodyValueAt(SoVoConst.duf0, irow, "norgqtprc");
			setBodyValueAt(SoVoConst.duf0, irow, "norgqttaxnetprc");
			setBodyValueAt(SoVoConst.duf0, irow, "norgqtnetprc");
			afterNumberEdit(new int[] { irow }, "nnumber", null, false, false);
		}
		/** 
		 * 赠品参数的单价金额不保留在订单上.销售订单设置了买赠设置后,带出的赠品仍然保留了单价金额. 
		 * v5.3 jiangzhe zhangcheng 
		 */
		
	}

	/**
	 * 尝试关联合同
	 * 设置默认的合同项，包括来源单据类型、合同ID、合同号、合同存货类等。 用于选存货时调用。
	 * @param int
	 *            irow 行号
	 * 
	 */
	private void setDefaultCtItem(int istartrow, int ilength) {
		try {
			// 存货id
			String[] sinvs = null;
			// 客商id
			String[] smans = null;
			String sman = getHeadItem("ccustomerid").getValue();
			if (sman == null || sman.trim().length() == 0)
				return;
			// 取得条件
			// 如果来源为空的可以找其它合同
			String sSource = null;
			Vector vt = new Vector();//存货id
			for (int i = 0; i < ilength; i++) {
				sSource = (String) getBodyValueAt(istartrow + i,
						"csourcebillbodyid");
				if (SoVoTools.isEmptyString(sSource)
						&& getBodyValueAt(istartrow + i, "cinventoryid") != null
						&& getBodyValueAt(istartrow + i, "cinventoryid")
								.toString().trim().length() > 0) {

					vt.add(getBodyValueAt(istartrow + i, "cinventoryid"));
				}
			}
			if (vt.size() > 0) {
				sinvs = new String[vt.size()];
				vt.copyInto(sinvs);
				smans = new String[vt.size()];
				for (int i = 0; i < smans.length; i++) {
					smans[i] = sman;
				}
			} else {
				return;
			}

			// 查询合同：获得值
			/** 只要有存货就会查询合同，必定会有一次远程调用* */
			Hashtable ht = SaleOrderBO_Client.queryForCntAll(ClientEnvironment
					.getInstance().getCorporation().getPk_corp(), sinvs, smans,
					ClientEnvironment.getInstance().getDate());
			if (ht != null && ht.size() > 0) {
				if (uipanel
						.showYesNoMessage(nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("40060301", "UPP40060301-000508")/* 存在相关合同，是否关联? */) 
								== MessageDialog.ID_NO) {
					for (int i = 0; i < ilength; i++) {
						setCellEditable(istartrow + i, "ct_name", false);
					}
					return;
				}
			}

			// 处理逻辑
			RetCtToPoQueryVO rect = null;

			ArrayList<Integer> alrows = new ArrayList<Integer>();
			for (int i = 0; i < ilength; i++) {
				sSource = (String) getBodyValueAt(istartrow + i,
						"csourcebillbodyid");

				if (SoVoTools.isEmptyString(sSource)
						&& getBodyValueAt(istartrow + i, "cinventoryid") != null
						&& getBodyValueAt(istartrow + i, "cinventoryid")
								.toString().trim().length() > 0
						&& ht.containsKey(getBodyValueAt(istartrow + i,
								"cinventoryid"))) {
					rect = (RetCtToPoQueryVO) ht.get(sinvs[i]);
					
					/**关联的合同必须与表头币种一致*/
					//表头币种不为null
					if (getHeadItem("ccurrencytypeid").getValueObject()!=null){
						if(!rect.getCCurrencyId().equals(
								getHeadItem("ccurrencytypeid").getValueObject().toString())){
						uipanel
								.showWarningMessage(nc.ui.ml.NCLangRes
										.getInstance().getStrByID("40060301",
												"UPT40060301-000549")/* 不支持关联多币种合同，请重新选择存货！ */);
						setCellEditable(istartrow + i, "ct_name", false);
						getBillData().getBillModel().delLine(new int[]{istartrow + i});
						return;
						}
					}
					//表头币种null,将表体币种赋给表头
					else
						setHeadItem("ccurrencytypeid", rect.getCCurrencyId());
					/**关联的合同必须与表头币种一致*/
					
					setCtItems(istartrow + i, rect);
					setCellEditable(istartrow + i, "ct_name", true);
					alrows.add(istartrow + i);

				} else {
					setCellEditable(istartrow + i, "ct_name", false);

				}
			}
			initFreeItem(alrows);

		} catch (Exception e) {
			handleException(e);
		}

	}

	/**
	 * 設置合同項
	 * v5.3 订单关联合同时，表体币种从合同取
	 * @param irow
	 * @param vo
	 */
	private void setCtItems(int irow, RetCtToPoQueryVO vo) {

		setBodyValueAt(vo.getCContractCode(), irow, "ct_code");
		setBodyValueAt(vo.getCContractID(), irow, "ct_manageid");
		setBodyValueAt(vo.getCtname(), irow, "ct_name");
		setBodyValueAt(vo.getCInvClass(), irow, "ctinvclassid");
		setBodyValueAt("Z4", irow, "creceipttype");
		setBodyValueAt(vo.getCContractRowId(), irow, "csourcebillbodyid");
		setBodyValueAt(vo.getCContractID(), irow, "csourcebillid");
		
		//设置表体币种（从合同取）
		setBodyValueAt(vo.getCCurrencyId(), irow, "ccurrencytypeid");
		getBillModel().execLoadFormulasByKey("ccurrencytypeid");
		
		//根据表体行币种设置折本折辅汇率
		UFDouble[] ult = getBillCardTools().getExchangeRate(vo.getCCurrencyId(),
				getHeadItem("dbilldate").getValue(), ClientEnvironment
						.getInstance().getCorporation().getPk_corp());
		setBodyValueAt(ult[0], irow, "nexchangeotobrate");//折本汇率

		
		// 赠品
		if (getBodyValueAt(irow, "blargessflag") != null
				&& ((Boolean) getBodyValueAt(irow, "blargessflag"))
						.booleanValue()) {
			setBodyValueAt(new UFDouble(0), irow, "noriginalcurprice");
			setBodyValueAt(new UFDouble(0), irow, "noriginalcurtaxprice");
		} else {
			setBodyValueAt(vo.getDOrgPrice(), irow, "noriginalcurprice");
			setBodyValueAt(vo.getDOrgTaxPrice(), irow, "noriginalcurtaxprice");
		}

		// 执行公式、赋默认值,包括单价金额
		executeCtFormula(irow);
		setCtItemEditable(irow);
		
		// 已价税合计或无税金额为触发点，进行计算（这样可以根据销售的参数进行优先线的计算）
		if (uipanel.SO_17.booleanValue()) {
			String key = (uipanel.SA_02.booleanValue() ? "noriginalcursummny" : "noriginalcurmny");
			int[] para = SaleorderBVO.getCalculatePara(key, null, uipanel.SA_02, uipanel.SO40);
			para[1] = RelationsCalVO.ITEMDISCOUNTRATE_PRIOR_TO_PRICE;// 强制调折扣
			
			RelationsCal.calculate(irow, this.oldValue, this, para, key, SaleorderBVO.getKeys(), SaleorderBVO
					.getField(), SaleorderBVO.class.getName(), SaleorderHVO.class.getName(), null);
		}
		
	}

	/**
	 * 初始化自由项(换算率)。
	 * 
	 * 创建日期：(2001-10-9 13:05:04)
	 * 
	 */
	private void initFreeItem(ArrayList<Integer> irows) throws Exception {

		if (getRowCount() <= 0)
			return;

		if (irows.size() == 0)
			return;

		try {
			// 批量获取存货信息
			InvVO[] invvos = new InvVO[irows.size()];
			for (int i = 0; i < invvos.length; i++) {
				invvos[i] = new InvVO();
				invvos[i].setCinventoryid((String) getBodyValueAt(irows.get(i),
						"cinventoryid"));
			}

			InvoInfoBYFormula invvosget = new InvoInfoBYFormula();
			invvos = invvosget.getQuryInvVOs(invvos, false, true, 1);

			for (int i = 0; i < invvos.length; i++)
				alInvs.add(invvos[i]);

			if (alInvs != null) {
				/** 过滤最末的空行* */
				for (int i = 0; i < getRowCount() && i < irows.size(); i++) {
					InvVO voInv = (InvVO) alInvs.get(i);
					setBodyFreeValue(irows.get(i), voInv);
					setBodyValueAt(voInv.getFreeItemVO().getWholeFreeItem(),
							irows.get(i), "vfree0");
				}
			}

		} catch (Exception ex) {
			SCMEnv.out("自由项设置失败!");
			throw ex;
		}
	}

	/**
	 * 设置下拉自由项值 创建日期：(01-2-26 13:29:17)
	 */
	public void setBodyFreeValue(int row, InvVO voInv) {
		if (voInv != null) {
			voInv.setFreeItemValue("vfree1", (String) getBodyValueAt(row,
					"vfree1"));
			voInv.setFreeItemValue("vfree2", (String) getBodyValueAt(row,
					"vfree2"));
			voInv.setFreeItemValue("vfree3", (String) getBodyValueAt(row,
					"vfree3"));
			voInv.setFreeItemValue("vfree4", (String) getBodyValueAt(row,
					"vfree4"));
			voInv.setFreeItemValue("vfree5", (String) getBodyValueAt(row,
					"vfree5"));
		}
	}

	/**
	 * 税率先取存货管理档案上的税目对应的税率，如果为空，则取存货基本档案上税目对应的税率。
	 * 
	 * @param rows
	 */
	private void loadTaxtrate(ArrayList rows) {
		if (rows == null)
			return;
		String[] targetitemkey = { "ctaxitemid" };
		String tname = "bd_invmandoc";
		String pkname = "pk_invmandoc";
		String[] field = { "mantaxitem" };
		String sourceitemkey = "cinventoryid";

		// 先记录原来税率
		UFDouble nheadtaxrate = getHeadItem("ntaxrate").getValue() == null ? new UFDouble(
				0)
				: new UFDouble(getHeadItem("ntaxrate").getValue());
		ArrayList<UFDouble> altax = new ArrayList<UFDouble>();
		UFDouble udBodytax = null;
		int count = rows.size();
		int irows[] = new int[count];
		for (int i = 0; i < count; i++) {

			if (rows.get(i) != null) {
				irows[i] = ((Integer) rows.get(i)).intValue();
				udBodytax = (UFDouble) getBodyValueAt(irows[i], "ntaxrate");
				altax.add(udBodytax == null ? nheadtaxrate : udBodytax);
			} else {
				irows[i] = -1;
				altax.add(new UFDouble(0));
			}

		}// end for

		ClientCacheHelper.getColValueBatch(this, irows, targetitemkey, tname,
				pkname, field, sourceitemkey);

		Object temp = null;
		ArrayList newRows = new ArrayList();
		for (int i = 0; i < count; i++) {
			if (rows.get(i) != null) {
				temp = getBodyValueAt(((Integer) rows.get(i)).intValue(),
						"ctaxitemid");
				if (temp == null || temp.toString().trim().length() < 20)
					newRows.add(rows.get(i));
			}
		}

		count = newRows.size();
		int[] inewRows = new int[count];
		for (int i = 0; i < count; i++) {
			if (newRows.get(i) != null) {
				inewRows[i] = ((Integer) newRows.get(i)).intValue();
			}
		}
		if (inewRows.length > 0) {
			targetitemkey = new String[] { "ctaxitemid" };
			tname = "bd_invbasdoc";
			pkname = "pk_invbasdoc";
			field = new String[] { "pk_taxitems" };
			sourceitemkey = "cinvbasdocid";

			ClientCacheHelper.getColValueBatch(this, inewRows, targetitemkey,
					tname, pkname, field, sourceitemkey);
		}
		targetitemkey = new String[] { "ntaxrate" };
		tname = "bd_taxitems";
		pkname = "pk_taxitems";
		field = new String[] { "taxratio" };
		sourceitemkey = "ctaxitemid";
		ClientCacheHelper.getColValueBatch(this, irows, targetitemkey, tname,
				pkname, field, sourceitemkey);

		for (int i = 0; i < count; i++) {
			if (irows[i] == -1)
				continue;
			udBodytax = (UFDouble) getBodyValueAt(irows[i], "ntaxrate");
			if (udBodytax == null) {
				setBodyValueAt(altax.get(i), irows[i], "ntaxrate");
			}

		}// end for

		/** 赠品不询价，也不再计算数量和金额，所以在加载税率后直接计算* */
		for (int i = 0, curRow, len = rows.size(); i < len; i++) {
			curRow = (Integer) rows.get(i);
			if (getBodyValueAt(curRow, "blargessflag") != null
					&& (Boolean) getBodyValueAt(curRow, "blargessflag")) {
				calculateNumber(curRow, "ntaxrate");
			}
		}
	}

	/**
	 * 存货编辑后事件处理。 创建日期：(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	private void afterCtManageEdit(BillEditEvent e) {

		int irow = e.getRow();
		UIRefPane invRef = (UIRefPane) getBodyItem("ct_name").getComponent();

		String refPk = invRef.getRefPK();
		String refCode = invRef.getRefCode();
		// String refName = invRef.getRefName();
		if (refPk == null || refPk.trim().length() == 0) {
			setBodyValueAt(null, irow, "ct_code");

			setBodyValueAt(null, irow, "ct_manageid");
			setBodyValueAt(null, irow, "ct_name");
			setBodyValueAt(null, irow, "ctinvclassid");
			setBodyValueAt(null, irow, "creceipttype");
			setBodyValueAt(null, irow, "csourcebillbodyid");
			setBodyValueAt(null, irow, "csourcebillid");
			setBodyValueAt(null, irow, "ctinvclass");

		} else {
			setBodyValueAt(refCode, irow, "ct_code");

			setBodyValueAt(invRef.getRefValue("ct_b.pk_ct_manage"), irow,
					"ct_manageid");
			setBodyValueAt("Z4", irow, "creceipttype");
			setBodyValueAt(refPk, irow, "csourcebillbodyid");
			setBodyValueAt(invRef.getRefValue("ct_b.pk_ct_manage"), irow,
					"csourcebillid");
			// 赠品
			if (getBodyValueAt(irow, "blargessflag") != null
					&& ((Boolean) getBodyValueAt(irow, "blargessflag"))
							.booleanValue()) {
				setBodyValueAt(new UFDouble(0), irow, "noriginalcurprice");
			} else {
				setBodyValueAt(invRef.getRefValue("ct_b.oriprice"), irow,
						"noriginalcurprice");

			}

			// //合同存货类名称
			executeCtFormula(irow);

		}
		setCtItemEditable(irow);

	}

	private void executeCtFormula(int irow) {

		ArrayList<String> bodyFormula = new ArrayList<String>();

		if (uipanel.strState.equals("新增")) {
			bodyFormula
					.add("ctinvclassid->getColValue(ct_manage_b,invclid,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("ctinvclass->getColValue(bd_invcl,invclassname,pk_invcl,ctinvclassid)");
			bodyFormula
					.add("ntaxrate->getColValue(ct_manage_b,taxration,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("bsafeprice->getColValue(ct_manage_b,bsafeprice,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("breturnprofit->getColValue(ct_manage_b,breturnprofit,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("cpriceitemtable->getColValue(ct_manage_b,cpricetableid,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("cpricepolicyid->getColValue(ct_manage_b,sopriceid,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("vdef1->getColValue(ct_manage_b,def1,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("vdef2->getColValue(ct_manage_b,def2,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("vdef3->getColValue(ct_manage_b,def3,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("vdef4->getColValue(ct_manage_b,def4,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("vdef5->getColValue(ct_manage_b,def5,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("vdef6->getColValue(ct_manage_b,def6,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("vdef7->getColValue(ct_manage_b,def7,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("vdef8->getColValue(ct_manage_b,def8,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("vdef9->getColValue(ct_manage_b,def9,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("vdef10->getColValue(ct_manage_b,def10,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("vdef11->getColValue(ct_manage_b,def11,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("vdef12->getColValue(ct_manage_b,def12,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("vdef13->getColValue(ct_manage_b,def13,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("vdef14->getColValue(ct_manage_b,def14,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("vdef15->getColValue(ct_manage_b,def15,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("vdef16->getColValue(ct_manage_b,def16,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("vdef17->getColValue(ct_manage_b,def17,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("vdef18->getColValue(ct_manage_b,def18,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("vdef19->getColValue(ct_manage_b,def19,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("vdef20->getColValue(ct_manage_b,def20,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("nnumber->getColValue(ct_manage_b,amount,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("npacknumber->getColValue(ct_manage_b,ordnum,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("ts->getColValue(ct_manage,ts,pk_ct_manage,csourcebillid)");
		} else {
			bodyFormula
					.add("ctinvclassid->getColValue(ct_manage_b,invclid,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("ctinvclass->getColValue(bd_invcl,invclassname,pk_invcl,ctinvclassid)");
			bodyFormula
					.add("ntaxrate->getColValue(ct_manage_b,taxration,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("bsafeprice->getColValue(ct_manage_b,bsafeprice,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("breturnprofit->getColValue(ct_manage_b,breturnprofit,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("cpriceitemtable->getColValue(ct_manage_b,cpricetableid,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("cpricepolicyid->getColValue(ct_manage_b,sopriceid,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("vdef1->getColValue(ct_manage_b,def1,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("vdef2->getColValue(ct_manage_b,def2,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("vdef3->getColValue(ct_manage_b,def3,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("vdef4->getColValue(ct_manage_b,def4,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("vdef5->getColValue(ct_manage_b,def5,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("vdef6->getColValue(ct_manage_b,def6,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("vdef7->getColValue(ct_manage_b,def7,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("vdef8->getColValue(ct_manage_b,def8,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("vdef9->getColValue(ct_manage_b,def9,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("vdef10->getColValue(ct_manage_b,def10,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("vdef11->getColValue(ct_manage_b,def11,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("vdef12->getColValue(ct_manage_b,def12,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("vdef13->getColValue(ct_manage_b,def13,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("vdef14->getColValue(ct_manage_b,def14,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("vdef15->getColValue(ct_manage_b,def15,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("vdef16->getColValue(ct_manage_b,def16,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("vdef17->getColValue(ct_manage_b,def17,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("vdef18->getColValue(ct_manage_b,def18,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("vdef19->getColValue(ct_manage_b,def19,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("vdef20->getColValue(ct_manage_b,def20,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("nnumber->getColValue(ct_manage_b,amount,pk_ct_manage_b,csourcebillbodyid)");
			bodyFormula
					.add("npacknumber->getColValue(ct_manage_b,ordnum,pk_ct_manage_b,csourcebillbodyid)");

		}

		//价格金额相关
		bodyFormula.add("noriginalcursummny->getColValue(ct_manage_b,oritaxsummny,pk_ct_manage_b,csourcebillbodyid)");
		bodyFormula.add("noriginalcurmny->getColValue(ct_manage_b,orisum,pk_ct_manage_b,csourcebillbodyid)");
		
		String[] formulas = new String[bodyFormula.size()];
		formulas = bodyFormula.toArray(formulas);
		getBillModel().execFormulas(irow, formulas);

		UFDouble amount = (UFDouble) getBodyValueAt(irow, "nnumber");
		UFDouble ordnum = (UFDouble) getBodyValueAt(irow, "npacknumber");
		if (ordnum == null)
			ordnum = new UFDouble(0);
		if (amount == null)
			amount = new UFDouble(0);

		// 数量-订单执行累计数量
		UFDouble num = amount.sub(ordnum).compareTo(UFDouble.ZERO_DBL)<=0?UFDouble.ZERO_DBL:amount.sub(ordnum);
		setBodyValueAt(num, irow, "nnumber");
		setBodyValueAt(num, irow, "nquoteunitnum");

		// 辅数量根据数量换算
		Object assistunit = getBodyValueAt(irow, "assistunit");
		if (assistunit != null) {
			getBillModel().execFormulas(irow,
					new String[] { "npacknumber->nnumber/scalefactor" });
		}
		
		//单品折扣=100，关联合同默认
		UFDouble d100 = new UFDouble(100.);
		setBodyValueAt(d100, irow, "nitemdiscountrate");
		
		// // bodyFormula[0]="nnumber->nnumber-npacknumber";
		// String s_bodyFormula =
		// "npacknumber->getColValue(ct_manage_b,astnum,pk_ct_manage_b,csourcebillbodyid)";
		// getBillModel().execFormulas(irow, new String[] { s_bodyFormula });
		// UFDouble astnum = (UFDouble) getBodyValueAt(irow, "npacknumber");
		// if (amount != null && amount.doubleValue() != 0 && astnum != null)
		// setBodyValueAt((amount.sub(ordnum)).multiply(astnum).div(amount),
		// irow, "npacknumber");

		/**v5.3 此处只进行公式计算，将单价金额计算去掉*/
		/*if (uipanel.SO_17.booleanValue()) {
			afterNumberEditLogic(irow, "noriginalcurprice", false);
			// updateUI();
		}*/

	}

	/**
	 * 設置合同項的行可編輯性
	 * 
	 * @param i
	 */
	public void setCtItemEditable(int i) {
		String ct_manageid = getBodyValueAt(i, "ct_manageid") == null ? null
				: getBodyValueAt(i, "ct_manageid").toString();
		if (ct_manageid != null && ct_manageid.length() != 0) {

			getBodyItem("cinventorycode").setEdit(true);
			setCellEditable(i, "cinventorycode", true);
			setCellEditable(i, "ct_name", true);
			
			//v5.5 严格执行合同：价格、金额、折扣都不允许编辑	&& !uipanel.SA_15.booleanValue()) {
			if (uipanel.SO_17.booleanValue()){
				UFDouble noriginalcurtaxprice = getBodyValueAt(i,
						"noriginalcurtaxprice") == null ? new UFDouble(0)
						: new UFDouble(
								getBodyValueAt(i, "noriginalcurtaxprice")
										.toString());
				UFDouble noriginalcurprice = getBodyValueAt(i,
						"noriginalcurprice") == null ? new UFDouble(0)
						: new UFDouble(getBodyValueAt(i, "noriginalcurprice")
								.toString());
						
				if (noriginalcurtaxprice.doubleValue() != 0) {
					for (String key : getBillCardTools().getSaleItems_Price())
						setCellEditable(i, key, false);
					for (String key : getBillCardTools().getSaleItems_Mny())
						setCellEditable(i, key, false);
					setCellEditable(i, "nitemdiscountrate", false);
					setCellEditable(i, "ndiscountrate", false);
				}				
				
			} else {
				String[] sItems = { "noriginalcurtaxprice",
						"noriginalcurprice", "norgqttaxprc",
						"noriginalcurtaxprice", "norgqtprc" };
				for (int k = 0; k < sItems.length; k++) {
					setCellEditable(i, sItems[k], getBillModel().getItemByKey(sItems[k]).isEdit());
				}
			}
		} else {
			setCellEditable(i, "ct_name", false);

		}

	}
	
	/**
	 * 单价、金额编辑后事件处理
	 */
	private void afterPriceMuyEdit(BillEditEvent e) {
		
		if (e==null||e.getRow()==-1||e.getKey()==null||"".equals(e.getKey().trim()))
			return;
		
		//1.计算单价金额
		calculateNumber(e.getRow(), e.getKey());
		
		//2.行颜色变化
		changeRowColorByEditPrice(e.getRow());
	}

	/**
	 * 数量编辑后事件处理。 创建日期：(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	private void afterNumberEdit(BillEditEvent e) {
		Object otemp = getBodyValueAt(e.getRow(), e.getKey());
		if ("退货".equals(uipanel.strState)) {/*-=notranslate=-*/
			UFDouble nnumber = getBillCardTools().getBodyUFDoubleValue(
					e.getRow(), "nnumber");

			if (nnumber != null) {
				String csourcebillbodyid = getBillCardTools()
						.getBodyStringValue(e.getRow(), e.getKey());
				if (csourcebillbodyid != null
						&& csourcebillbodyid.trim().length() > 0) {
					int pos = nc.vo.so.so016.SoVoTools.find(getBillCardTools()
							.getOldsaleordervo().getBodyVOs(),
							new String[] { "corder_bid" },
							new Object[] { csourcebillbodyid });
					if (pos >= 0) {
						if (getBillCardTools().getOldsaleordervo().getBodyVOs()[pos]
								.getNnumber().doubleValue() < nnumber.abs()
								.doubleValue()) {
							uipanel.showErrorMessage(nc.ui.ml.NCLangRes
									.getInstance().getStrByID("40060301",
											"UPP40060301-000173")/*
																	 * @res
																	 * "退货数量不能超过原订单的订货数量"
																	 */);
							setBodyValueAt(e.getOldValue(), e.getRow(),
									"nnumber");
							return;
						}
					}

				}
			}
		} //退货

		String sPk = (String) getBodyValueAt(e.getRow(), "cinventoryid");
		String corder_bid = (String) getBodyValueAt(e.getRow(), "corder_bid");

		// 20061028 增加修订过滤
		if (("nnumber".equals(e.getKey()) || "nquoteunitnum".equals(e.getKey()))
				&& (sPk != null && sPk.trim().length() > 0)
				&& !(corder_bid != null && uipanel.strState.equals("修订"))) {
			// 有来源的不能带
			Object sSource = getBodyValueAt(e.getRow(), "creceipttype");

			if (sSource == null || sSource.toString().trim().length() == 0) {
				//买赠
				UFBoolean blar = new UFBoolean(getBodyValueAt(e.getRow(),
						"blargessflag") == null ? "false" : getBodyValueAt(
						e.getRow(), "blargessflag").toString());
			    //捆绑
				UFBoolean bind = new UFBoolean(getBodyValueAt(e.getRow(),
				"bbindflag") == null ? "false" : getBodyValueAt(
				e.getRow(), "bbindflag").toString());
				
				// 当前编辑行非买赠或捆绑类存货
				if ( (blar == null || !blar.booleanValue()) 
						&& (bind == null || !bind.booleanValue()) ) {

					// 删除原行所属的赠品存货
					int[] inewdelline_largess = setBlargebindLineWhenDelLine(new int[] { e
							.getRow() },1);
					if (inewdelline_largess != null && inewdelline_largess.length > 0)
						uipanel.onDelLine(inewdelline_largess);
					
					// 删除原行所属的捆绑存货
					int[] inewdelline_bind = setBlargebindLineWhenDelLine(new int[] { e
							.getRow() },2);
					if (inewdelline_bind != null && inewdelline_bind.length > 0)
						uipanel.onDelLine(inewdelline_bind);

					afterNumberEdit(new int[] { e.getRow() }, e.getKey(), null,
							false, true);
					
					//TODO 修改数量后不应该调用此方法 
					//但此方法用于设置买赠和捆绑暂时还要调用
					afterInventoryMutiEdit(e.getRow(), new String[] { sPk },
							false, true, e.getKey(), true, 2);
					
					return;
				}

				// 赠品

			}// end if no source

		}

		if (otemp == null && e.getOldValue() == null)
			return;
		if (otemp != null && otemp.equals(e.getOldValue()))
			return;
		if (e.getOldValue() != null && e.getOldValue().equals(otemp))
			return;
		boolean ischginv = false;
		if ("cinventorycode".equals(e.getKey())
				|| "cinventoryid".equals(e.getKey()))
			ischginv = true;
		afterNumberEdit(new int[]{e.getRow()},e.getKey(),null,ischginv,true);
	}

	/**
	 * 数量编辑后事件处理。 创建日期：(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	private void afterNumberEditLogic(int row, String key, boolean isFindPrice) {
		
		/** v5.3单价金额算法变更 统一在该方法的最后计算*/
		/*System.out.println("afterNumberEditLogic----RelationsCal.calculate：#"+row+"#"+key+"#-->调用开始！");
		
		//BillTools.calcUnitNum(row, getBillModel(), key, getBillType());
		
		RelationsCal.calculate(row, this, getCalculatePara(key,null), key, 
				getKeys(), getField(), SaleorderBVO.class.getName(),SaleorderHVO.class.getName(),null);
		
		System.out.println("afterNumberEditLogic----RelationsCal.calculate：#"+row+"#"+key+"#-->调用结束！");*/
		/** v5.3单价金额算法变更 */

		// 寻价
		if (isFindPrice) {
			if (!nc.ui.pub.pf.PfUtilClient.makeFlag
					&& (getSouceBillType().equals(SaleBillType.PurchaseOrder)
							|| getSouceBillType().equals(/*-=notranslate=-*/
							SaleBillType.SaleQuotation)/*-=notranslate=-*/

							|| uipanel.strState.equals("退货") || (getBodyValueAt(
							row, "discountflag") != null && getBodyValueAt(row,
							"discountflag").equals(/*-=notranslate=-*/
					new Boolean(true))))) {
			} else {
				// 数量
				if (getBodyValueAt(row, "nnumber") != null) {

					if (key.equals("nnumber") || key.equals("npacknumber")
							|| key.equals("ccurrencytypename")) {
						findPrice(new int[] { row }, null, false);
					}

				}
			}
		}
		// 计算数量金额
		calculateNumber(row, key);
	}

	private void afterOOSFlagEdit(int row, boolean isOOS) {
		try {
			if (isOOS)
				setBodyValueAt("N", row, "bsupplyflag");
			else
				setBodyValueAt("N", row, "boosflag");

		} catch (Exception ex) {
			SCMEnv.out(ex);
			// ex.printStackTrace();
		}
	}

	/**
	 * 此处插入方法说明。 功能描述: 输入参数: 返回值: 异常处理: 日期:
	 * 
	 * @param eRow
	 *            int
	 */
	private void afterInventorysEdit(int istartrow, int iendrow,
			String sFormulakey, boolean bNeedFindPrice) {

		ArrayList dalist = new ArrayList();
		UFDouble d100 = new UFDouble(100);
		for (int i = istartrow; i < iendrow; i++) {

			// 批次控制:改到单据摸版内项目：wholemanaflag 控制
			Object temp = getBodyValueAt(i, "wholemanaflag");
			boolean wholemanaflag = (temp == null ? false : new UFBoolean(temp
					.toString()).booleanValue());
			setCellEditable(i, "fbatchstatus", wholemanaflag);
			setCellEditable(i, "cbatchid", wholemanaflag);

			String cinventoryid = getBillCardTools().getBodyStringValue(i,
					"cinventoryid");
			if (cinventoryid == null || cinventoryid.trim().length() <= 0) {
				// 设置渠道类型不可编辑
				setCellEditable(i, "cchantype", false);
			} else {
				setCellEditable(i, "cchantype", true);
			}

			// 默认数量为1
			Object oTemp = getBodyValueAt(i, "discountflag");
			boolean isDiscount = oTemp == null ? false : new UFBoolean(oTemp
					.toString()).booleanValue();
			oTemp = getBodyValueAt(i, "laborflag");
			boolean isLabor = oTemp == null ? false : new UFBoolean(oTemp
					.toString()).booleanValue();
			if (!isDiscount) {
				if (getBillCardTools().getBodyUFDoubleValue(i, "nnumber") == null)
					setBodyValueAt(uipanel.SO34, i, "nnumber");
				dalist.add(new Integer(i));
				// afterNumberEdit(i, "nnumber");

			}

			setBodyValueAt(d100, i, "nitemdiscountrate");

			if (isLabor || isDiscount) {

				getBillCardTools().setBodyCellsEdit(
						new String[] { "cconsigncorp", "creccalbody",
								"crecwarehouse", "bdericttrans", "boosflag",
								"bsupplyflag" }, i, false);

				// 发货库存组织
				getBillCardTools().setBodyValueByHead("ccalbodyid", i);
				// 发货仓库
				getBillCardTools().setBodyValueByHead("cwarehouseid", i);
			}

			// 整单税率
			if (getHeadItem("ntaxrate") != null) {
				String sMainTax = getHeadItem("ntaxrate").getValue();
				if (sMainTax == null
						|| new UFDouble(sMainTax).doubleValue() == 0) {
				} else {
					// 若有整单税率，而存货没有税率科目可携带税率，则恢复携带整单税率
					Object oCurRowTax = getBodyValueAt(i, "ntaxrate");
					if (oCurRowTax == null
							|| new UFDouble(oCurRowTax.toString())
									.doubleValue() == 0) {
						setBodyValueAt(sMainTax, i, "ntaxrate");
					}
				}
			}

			ctlUIOnCconsignCorpChg(i);
			setScaleEditableByRow(i);

		}
		// 排除新生成的赠品行

		if (dalist != null && dalist.size() > 0) {
			ArrayList alnew = new ArrayList();
			int itmp;
			String clargessrowno = null;
			Object blargessflag = null;
			for (int i = 0, loop = dalist.size(); i < loop; i++) {
				itmp = ((Integer) dalist.get(i)).intValue();
				clargessrowno = (String) getBodyValueAt(itmp, "clargessrowno");
				blargessflag = getBodyValueAt(itmp, "blargessflag");
				if (!(clargessrowno != null
						&& clargessrowno.trim().length() > 0
						&& blargessflag != null && (new UFBoolean(blargessflag
						.toString())).booleanValue())) {
					alnew.add(new Integer(itmp));
				}
			}

			int[] findrows = new int[alnew.size()];
			for (int i = 0, loop = alnew.size(); i < loop; i++) {
				findrows[i] = ((Integer) alnew.get(i)).intValue();
			}
			
			// 编辑存货会得到税率，所以要用税率先算一边
			if ("ntaxrate".equals(sFormulakey.trim())){
				for (int i = 0, loop = findrows.length; i < loop; i++)
					calculateNumber(findrows[i], "ntaxrate");
			}
			
			afterNumberEdit(findrows, "nnumber", null, true, bNeedFindPrice);
		}

	}

	/**
	 * 赠品事件处理。 创建日期：(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	private void afterLargessFlagEdit(BillEditEvent e) {
		// 赠品不参加打印合计计算
		int row = e.getRow();
		UFBoolean largess = (UFBoolean) getBillCardTools()
				.getBodyUFBooleanValue(row, "blargessflag");
		boolean blargess = largess == null ? false : largess.booleanValue();

		if (blargess)
			setBodyValueAt("1", row, "is_total");
		else
			setBodyValueAt(null, row, "is_total");

		if (blargess && !uipanel.SO59.booleanValue()) {

			setBodyValueAt(SoVoConst.duf0, row, "noriginalcurprice");
			setBodyValueAt(SoVoConst.duf0, row, "noriginalcurtaxprice");
			setBodyValueAt(SoVoConst.duf0, row, "noriginalcurnetprice");
			setBodyValueAt(SoVoConst.duf0, row, "noriginalcurtaxnetprice");

			setBodyValueAt(SoVoConst.duf0, row, "norgqttaxprc");
			setBodyValueAt(SoVoConst.duf0, row, "norgqtprc");
			setBodyValueAt(SoVoConst.duf0, row, "norgqttaxnetprc");
			setBodyValueAt(SoVoConst.duf0, row, "norgqtnetprc");
			afterNumberEdit(new int[] { row }, "nnumber", null, false, false);

		}
		// 设置编辑性
		getBillCardTools().setCellEditableByLargess(
				blargess && !uipanel.SO59.booleanValue(), row);

		// 赠品不参加界面合计计算
		getBillModel().reCalcurateAll();

	}

	/**
	 * 数量编辑后事件处理。 创建日期：(2001-6-23 13:42:53)
	 * isinvchg --- 存货是否发生变化
	 * bNeedFindPrice -- 是否询价
	 */
	public void afterNumberEdit(int[] rows, String key, String oldinvid,
			boolean isinvchg, boolean bNeedFindPrice) {

		if (rows == null || rows.length <= 0)
			return;

		boolean bisCalculate = getBillModel().isNeedCalculate();
		getBillModel().setNeedCalculate(false);

		//需要询价的行
		ArrayList rowlist = new ArrayList();
		
		String calKey = key;//真正计算使用的变化的key
		boolean ifChgPriceWhenChgQtUnit = false;//修改报价计量单位后，重新计算前报价单价是否修改过
		for (int i = 0, loop = rows.length; i < loop; i++) {
			
			//编辑辅单位，报价计量单位，以换算率为变化点进入算法
			if (key.equals("cpackunitname")||key.equals("cpackunitid"))
				calKey = "scalefactor";
			else if (key.equals("cquoteunit")){
				calKey = "nqtscalefactor";
				ifChgPriceWhenChgQtUnit = ifModifyPrice(rows[i]);
			}
			//汇率改变--由后面的afterNumberEdit统一处理
			else if (key.equals("nexchangeotobrate"))
				break;
			
			//if (!ifbindct) {
				RelationsCal.calculate(rows[i], this.oldValue, this, SaleorderBVO.getCalculatePara(calKey, null,
						uipanel.SA_02, uipanel.SO40), calKey, SaleorderBVO.getKeys(), SaleorderBVO.getField(),
						SaleorderBVO.class.getName(), SaleorderHVO.class.getName(), null);
			//}
			
			// 寻价
			if ( bNeedFindPrice && isFindPrice(rows[i],key) ) 
				findPriceEntrance(rows[i], key, rowlist,ifChgPriceWhenChgQtUnit);
			
			// 根据是否修改过价格变换行颜色(修改数量不变色)
			if ( (key.equals("cquoteunit") && ifChgPriceWhenChgQtUnit) 
				  || (!key.equals("cquoteunit") && !key.equals("nnumber") 
						&& !key.equals("npacknumber") && !key.equals("nquoteunitnum")
						&& !"cinventorycode".equals(key)&&!"cinventoryid".equals(key)
				     )
				)
				changeRowColorByEditPrice(rows[i]);
		}

		//需要进行询价
		if (bNeedFindPrice && (rowlist != null && rowlist.size() > 0) ) {
				int[] findrows = new int[rowlist.size()];

				for (int i = 0, loop = rowlist.size(); i < loop; i++) {
					findrows[i] = ((Integer) rowlist.get(i)).intValue();
				}

				findPrice(findrows, oldinvid, isinvchg);
		}
		//不需要进行询价，并且非关联合同或不严格执行合同时或非单价类合同，价格进行重新计算
		else {//if (!ifbindct){
			for (int i = 0, loop = rows.length; i < loop; i++) {
				//并且净价不为空
				if ((uipanel.SA_02.booleanValue() && getBodyValueAt(rows[i],
						"noriginalcurtaxnetprice") != null)
						|| (!uipanel.SA_02.booleanValue() && getBodyValueAt(
								rows[i], "noriginalcurnetprice") != null))
					// 计算数量金额
				    calculateNumber(rows[i], key);
			}
		}
		
		getBillModel().setNeedCalculate(bisCalculate);
	}
	
	/**
	 * v5.5
	 * panel上询价统一入口：返回需要询价的行
	 * row -- 待询价行
	 * key -- 引发询价的key
	 * rowlist -- 返回需要询价的行
	 * ifChgPriceWhenChgQtUnit -- 修改报价计量单位后，重新计算前报价单价是否修改过
	 */
	public ArrayList<Integer> findPriceEntrance(int row,String key,ArrayList rowlist,
			boolean ifChgPriceWhenChgQtUnit){
		// 寻价
		if (!nc.ui.pub.pf.PfUtilClient.makeFlag
				&& (getSouceBillType().equals(
						SaleBillType.PurchaseOrder)
						|| getSouceBillType().equals(
								SaleBillType.SaleQuotation)
					    || (uipanel.strState.equals("修订")&&(!uipanel.SO78.booleanValue()))
						|| uipanel.strState.equals("退货") || ((getBodyValueAt(
							row, "discountflag") != null && getBodyValueAt(
							row, "discountflag").equals(Boolean.TRUE))))) {
		} else {
			UFBoolean blargessflag = getBillCardTools()
					.getBodyUFBooleanValue(row, "blargessflag");
			// 数量
			if (getBodyValueAt(row, "nnumber") != null
					&& (blargessflag == null || !blargessflag
							.booleanValue())) {

				if ( (key.equals("nnumber")
						|| key.equals("npacknumber")
						|| key.equals("nquoteunitnum")
						|| key.equals("ccurrencytypename")
						|| key.equals("vfree0")
						|| key.equals("cquoteunit")
						|| key.equals("cpackunitname")
						|| key.equals("cpriceitem")
						|| key.equals("cpriceitemid")
						|| key.equals("cpriceitemtablename")
						|| key.equals("cpriceitemtable")
						|| key.equals("cpricepolicy")
						|| key.equals("cpricepolicyid")
						|| key.equals("creceiptareaname")
						|| key.equals("creceiptareaid")
						|| (key.equals("cchantype") || key
								.equals("cchantypeid"))
						|| (key.equals("blargessflag") && isFindPriceAfterlargess(row)))
						//没有手工修改过价格才可以询价
						&& ( (key.equals("cquoteunit") && ifChgPriceWhenChgQtUnit) 
							    || (!ifModifyPrice(row))  
							)
						) {					
					//如果询价的话，需要清空价格项（询价相关）
					if (key.equals("cchantype")	|| key.equals("cchantypeid")) {
						getBillCardTools().clearBodyValue(getBillCardTools().getPriceItem(),row);
					}
					rowlist.add(new Integer(row));
					// 自由项寻价后做类似数量寻价后的计算
					if (key.equals("vfree0"))
						key = "nnumber";
				}
			}
		}
		
		return rowlist;
	}
	
	/**
	 * @param row 根据是否修改过价格变换行颜色
	 */
	public void changeRowColorByEditPrice(int row){
		ArrayList rowList  = new ArrayList();
		rowList.add(new Integer(row));
        // 若手工修改价格，行变色
		if (ifModifyPrice(row)) 
			nc.ui.scm.pub.panel.SetColor.setErrorRowColor(this,rowList);
	    else // 若未手工修改价格，行颜色为原有背景色
	    	nc.ui.scm.pub.panel.SetColor.resetErrorRowColor(this,rowList);
	}
	
	/**
	 * @param row 行颜色为原有背景色
	 */
	public void resetErrorRowColor(int row){
		ArrayList rowList  = new ArrayList();
		rowList.add(new Integer(row));
        // 若未手工修改价格，行颜色为原有背景色
		nc.ui.scm.pub.panel.SetColor.resetErrorRowColor(this,rowList);
	}
	
	/**
	 * 手工修改过价格 --- true 没有手工修改过价格 --- false
	 * 
	 * @return
	 */
	public boolean ifModifyPrice(int row){
        //基价含税
	    if(uipanel.SA_02.booleanValue()){
	    //报价单位含税单价
	    UFDouble norgqttaxprc = (UFDouble)getBodyValueAt(row, "norgqttaxprc");
	    //询价原币含税单价
	    UFDouble nqtorgtaxprc = (UFDouble)getBodyValueAt(row, "nqtorgtaxprc");
	    //报价单位含税净价
	    //UFDouble norgqttaxnetprc = (UFDouble)getBodyValueAt(row, "norgqttaxnetprc");
	    //询价原币含税净价
	    //UFDouble nqtorgtaxnetprc = (UFDouble)getBodyValueAt(row, "nqtorgtaxnetprc");
	    //报价计量单位单价非空且不等于询价原币单价时，说明修改过价格
	    if( norgqttaxprc != null){
	      if (nqtorgtaxprc == null || norgqttaxprc.compareTo(nqtorgtaxprc) != 0
	            ){
	        return true;
	      }
	    }
	    //基价无税
	    }else{
	      //报价单位无税单价
	      UFDouble norgqtprc = (UFDouble)getBodyValueAt(row, "norgqtprc");
	      //询价原币无税单价
	      UFDouble nqtorgtaxprc = (UFDouble)getBodyValueAt(row, "nqtorgprc");
	      //报价单位无税净价
	      //UFDouble norgqtnetprc = (UFDouble)getBodyValueAt(row, "norgqtnetprc");
	      //询价原币无税净价
	      //UFDouble nqtorgnetprc = (UFDouble)getBodyValueAt(row, "nqtorgnetprc");
	      if(norgqtprc != null){
	        if(nqtorgtaxprc == null || nqtorgtaxprc.compareTo(norgqtprc) != 0)
	          return true;
	      }
	    }
	    return false;
	}

	private boolean isFindPriceAfterlargess(int row) {
		UFBoolean largess = (UFBoolean) getBillCardTools()
				.getBodyUFBooleanValue(row, "blargessflag");
		if (largess == null || largess.booleanValue())
			return false;
		UFDouble nprice = (UFDouble) getBodyValueAt(row, "ntaxprice");
		if (nprice != null && nprice.doubleValue() != 0)
			return false;
		return true;
	}

	/**
	 * 初始化自由项(换算率)。
	 * 
	 * 创建日期：(2001-10-9 13:05:04)
	 * 
	 */
	public void initFreeItem() {

		alInvs = new java.util.ArrayList();
		if (getRowCount() <= 0)
			return;

		try {
			// 批量获取存货信息
			InvVO[] invvos = new InvVO[getRowCount()];
			for (int i = 0; i < invvos.length; i++) {
				invvos[i] = new InvVO();
				invvos[i].setCinventoryid((String) getBodyValueAt(i,
						"cinventoryid"));
			}

			InvoInfoBYFormula invvosget = new InvoInfoBYFormula();
			invvos = invvosget.getQuryInvVOs(invvos, false, true, 1);

			for (int i = 0; i < invvos.length; i++)
				alInvs.add(invvos[i]);

			if (alInvs != null) {
				for (int i = 0; i < getRowCount(); i++) {
					InvVO voInv = (InvVO) alInvs.get(i);
					setBodyFreeValue(i, voInv);
					setBodyValueAt(voInv.getFreeItemVO().getWholeFreeItem(), i,
							"vfree0");
				}
			}
		} catch (Exception ex) {
			SCMEnv.out("自由项设置失败!");
		}
	}

	/**
	 * 价格政策编辑后事件处理
	 * 
	 * 价格策略修改之后，自动带出该价格策略的价目表，并重新询价（即使价目表没有变化也要重新询价）
	 * 
	 */
	private void afterPricePolicy(BillEditEvent e) {
		int[] rows = new int[] { e.getRow() };

		setBodyValueAt(((UIRefPane) getBodyItem("cpricepolicy").getComponent())
				.getRefPK(), e.getRow(), "cpricepolicyid");

		// 清空价格项目、价目表
		setBodyValueAt(null, e.getRow(), "cpriceitemtable");
		setBodyValueAt(null, e.getRow(), "cpriceitemtablename");
		setBodyValueAt(null, e.getRow(), "cpriceitem");
		setBodyValueAt(null, e.getRow(), "cpriceitemid");

		findPrice(rows, null, false);
	}

	/**
	 * 价目表编辑后事件处理
	 * 
	 * 价目表修改之后，价格策略不会变化，重新询价，按照原有的价格策略和新的价目表进行询价
	 * 
	 */
	private void afterPriceItemTable(BillEditEvent e) {
		int[] rows = new int[] { e.getRow() };

		setBodyValueAt(((UIRefPane) getBodyItem("cpriceitemtablename")
				.getComponent()).getRefPK(), e.getRow(), "cpriceitemtable");

		// 清空价格项目
		setBodyValueAt(null, e.getRow(), "cpriceitem");
		setBodyValueAt(null, e.getRow(), "cpriceitemid");

		findPrice(rows, null, false);
	}

	public void bodyRowChange(BillEditEvent e) {
		UFBoolean isInvBom = getBillCardTools().getBodyUFBooleanValue(
				e.getRow(), "isconfigable");
		if (isInvBom != null && isInvBom.booleanValue())
			uipanel.boBom.setEnabled(true);
		else
			uipanel.boBom.setEnabled(false);
		uipanel.updateButton(uipanel.boBom);
		// 单据状态
		if (getBillType().equals(SaleBillType.SaleOrder)) {
			try {
				// 单据状态
				int iStatus = Integer.parseInt(getHeadItem("fstatus")
						.getValue() == null ? "0" : getHeadItem("fstatus")
						.getValue());
				if (e.getRow() > -1) {

					Object cfreezeid = getBodyValueAt(e.getRow(), "cfreezeid");

					if (cfreezeid != null
							&& cfreezeid.toString().trim().length() != 0) {

					} else {
						if (iStatus == BillStatus.AUDIT)
							uipanel.boStockLock.setEnabled(true);
						else
							uipanel.boStockLock.setEnabled(false);
					}
					uipanel.updateButton(uipanel.boStockLock);
				}
				// wsy 存量显示
				uipanel.freshOnhandnum(e.getRow());
			} catch (Exception e1) {
				SCMEnv.out(e1);
				// e1.printStackTrace();
			}
		}

		try {
			if (uipanel.getFuncExtend() != null) {
				// 支持功能扩展
				uipanel.getFuncExtend().rowchange(uipanel, this, null,
						nc.ui.scm.extend.IFuncExtend.LIST,
						nc.ui.pub.bill.BillItem.HEAD);
			}
		} catch (Throwable exx) {
			SCMEnv.out(exx);
			// exx.printStackTrace();
		}
		
		uipanel.getPluginProxy().bodyRowChange(e);
	}
	
	/**
	 * 销售订单复制后设置各字段可编辑性
	 * @throws BusinessException 
	 */
	public void setEditEnabledForCopy() throws BusinessException{
		//表体、表头折本汇率
        //币种参照
		UIRefPane ccurrencytypeid = (UIRefPane) getHeadItem("ccurrencytypeid").getComponent();
		if (CurrParamQuery.getInstance().isLocalCurrType(getCorp(),
				ccurrencytypeid.getRefPK())) 
			getHeadItem("nexchangeotobrate").setEnabled(false);
		else
			getHeadItem("nexchangeotobrate").setEnabled(true);
		//表体折本汇率不可编辑
		getBillCardTools().setBodyItemEnable(
				SOBillCardTools.getSaleOrderCurrTypeDigit(), false);
		
		UFBoolean bfreecustflag = getBillCardTools().getHeadUFBooleanValue("bfreecustflag");
		if (bfreecustflag != null && bfreecustflag.booleanValue()) {
			getHeadItem("cfreecustid").setEnabled(true);
		} else {
			getHeadItem("cfreecustid").setEnabled(false);
		}
	}
	
	/**
	 * findPriceSuccess --- 询价是否成功
	 * row ---------------- 询价行
	 * 
	 * SA_15:销售是否询价--为否，则价格、折扣是否可编辑取决于模版
	 * 价税合计,无税金额,含税、无税净价/单价---是否可以编辑看参数
	 * SA39--询不到价是否允许修改价格
	 * SA40--询到价是否允许修改价格
	 * SA41--是否允许修改折扣
	 * SO40--改金额、净价修改折扣还是单价
	 * @return fieldEditable
	 */
	public void setEditEnabled(boolean findPriceSuccess , int row ){
		// SA_15:销售是否询价--为否，则价格、折扣是否可编辑取决于模版
		if (!uipanel.SA_15.booleanValue())
			return;

		/* 1.单价可编辑性：询到价取决于SA40；询不到价取决于SA39 ** */
		// 询到价格
		if (findPriceSuccess) {
			getBillCardTools().setBodyCellsEdit(
					SOBillCardTools.getSaleOrderItems_Price(), row,
					uipanel.SA_40.booleanValue());		
		}
		// 没有询到价格
		else {
			getBillCardTools().setBodyCellsEdit(
					SOBillCardTools.getSaleOrderItems_Price(), row,
					uipanel.SA_39.booleanValue());
		}

		/* 2.折扣可编辑性:仅取决于SA41 ********************** */
		getBillCardTools().setBodyCellsEdit(
				new String[] { "nitemdiscountrate" }, row,
				uipanel.SA_41.booleanValue());

		/*
		 * 3.净价、价税合计、无税金额可编辑性 SO40--调整折扣：取决于SA41
		 * SO40--调整单价：询到价取决于SA40；询不到价取决于SA39
		 */
		if (uipanel.SO40.equals("调整折扣"))
			getBillCardTools().setBodyCellsEdit(
					SOBillCardTools.getSaleOrderItems_NetPrice_Mny(), row,
					uipanel.SA_41.booleanValue());
		else if (uipanel.SO40.equals("调整单价")) {
			// 询到价格
			if (findPriceSuccess) 
				getBillCardTools().setBodyCellsEdit(
						SOBillCardTools.getSaleOrderItems_NetPrice_Mny(), row,
						uipanel.SA_40.booleanValue());
			// 没有询到价格
			else 
				getBillCardTools().setBodyCellsEdit(
						SOBillCardTools.getSaleOrderItems_NetPrice_Mny(), row,
						uipanel.SA_39.booleanValue());
		}
	}

	/**
	 * 编辑前事件处理。 创建日期：(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public boolean beforeEdit(BillEditEvent e) {

		boolean bret = true;

		if (e.getPos() == BillItem.BODY) {	
		
			// 发货库存组织
		    if (e.getKey().equals("cadvisecalbody")) {
				// 表体建议发货库存组织受客户、存货、销售组织的权限控制
				UIRefPane ctm = (UIRefPane) getBodyItem(e.getKey())
						.getComponent();
				CalBodySORefModel mol = (CalBodySORefModel) ctm.getRefModel();
				// //跨公司判断
				String cconsigncorp = getBillCardTools().getBodyStringValue(
						e.getRow(), "cconsigncorpid");
				if (cconsigncorp == null) {
					mol.curPkCorp = pk_corp;
				} else {
					mol.curPkCorp = cconsigncorp;
				}

				// //强制刷新数据，否则共用参照时，会有数据缓存问题
				mol.reloadData();

			}
		    else if (e.getKey().equals("bdericttrans")) {
		    	return false;
		    }
			// 合同参照生成存货的合同存货类处理
			else if (e.getKey().equals("cinventorycode")) {
				beforeInventoryEdit(e);
				// 来源的单据存货不能修改
				String csourcebillbodyid = (String) getBodyValueAt(e.getRow(),
						"csourcebillbodyid");
				String cinventoryid = (String) getBodyValueAt(e.getRow(),
						"cinventoryid");
				if (!SoVoTools.isEmptyString(csourcebillbodyid)
						&& !SoVoTools.isEmptyString(cinventoryid)) {

					String ctinvclassid = (String) getBodyValueAt(e.getRow(),
							"ctinvclassid");
					if (SoVoTools.isEmptyString(ctinvclassid)) {

						uipanel.showHintMessage(nc.ui.ml.NCLangRes
								.getInstance().getStrByID("40060301",
										"UPP40060301-000421"));
						// @ res "该行存货参照其它单据生成，不能修改存货！"
						bret = false;
					}
				}

			} // 收货地址
			else if (e.getKey().equals("vreceiveaddress")) {
				beforeBodyAddressEdit(e);
			}
			// 合同
			else if (e.getKey().equals("ct_name")) {
				// 退货不允许修改合同
				if (uipanel.strState.equals("退货")) {
					return false;
				}

				String invbasid = (String) getBodyValueAt(e.getRow(),
						"cinvbasdocid");
				String sman = getHeadItem("ccustbasid").getValue();
				if (invbasid == null || invbasid.length() == 0) {
					setCellEditable(e.getRow(), "ct_name", false);
				} else {
					UIRefPane ctm = (UIRefPane) getBodyItem("ct_name")
							.getComponent();
					setCellEditable(e.getRow(), "ct_name", true);

					try {
						if (SaleOrderBO_Client.isModuleEnabled(pk_corp,
								ProductCode.PROD_CT)) {
							Class clz = Class
									.forName("nc.ui.ct.ref.ValiSaleCtRefModel");
							if (clz != null) {
								IValiSaleCtRefModel ref = (IValiSaleCtRefModel) clz
										.newInstance();
								ref.setWhereParameter(pk_corp, sman, invbasid,
										ClientEnvironment.getInstance()
												.getDate());
								ctm.setRefModel((AbstractRefModel) ref);
								ctm.setReturnCode(false);
							}
						}
						// it should never happen
						else {
							System.err.println("合同没有启用，pk_corp=" + pk_corp);
						}
					} catch (Exception e1) {
						SCMEnv.out(e1);
						// e1.printStackTrace();
					}
				}

			}

			// 折扣额
			else if (e.getKey().equals("noriginalcurdiscountmny")) {
				return false;
			}
			// 项目阶段
			else if (e.getKey().equals("cprojectphasename")) {
				stopEditing();
				String cprojectid = (getBodyValueAt(e.getRow(), "cprojectid") == null ? null
						: getBodyValueAt(e.getRow(), "cprojectid").toString());
				if (cprojectid == null || cprojectid.equals(""))
					cprojectid = "ABCDEF";
				UIRefPane cprojectphasename = (UIRefPane) getBodyItem(
						"cprojectphasename").getComponent();
				cprojectphasename.setRefModel(new nc.ui.bd.b39.PhaseRefModel(
						cprojectid));
			}
			// 批次控制
			else if (e.getKey().equals("cbatchid")) {
				beforeBatchidEdit(e);
			}
			// 自由项
			else if (e.getKey().equals("vfree0")) {
				// 获得存货VO
				try {
					stopEditing();
					InvVO voInv = null;
					if (alInvs.size() > e.getRow()) {
						int index = rowindex.get(getBodyValueAt(e.getRow(),
								"crowno")) == null ? -1 : rowindex
								.get(getBodyValueAt(e.getRow(), "crowno"));
						if (index > -1)
							voInv = (InvVO) alInvs.get(index);
						else
							voInv = (InvVO) alInvs.get(e.getRow());
					}

					setBodyFreeValue(e.getRow(), voInv);
					getFreeItemRefPane().setFreeItemParam(voInv);
				} catch (Exception ex) {
					SCMEnv.out("自由项设置失败!");
					// ex.printStackTrace();
				}
			}
			// 收货库存组织
			else if ("creccalbody".equals(e.getKey())) {
				// 非跨公司不可编辑
				String cconsigncorp = getBillCardTools().getBodyStringValue(
						e.getRow(), "cconsigncorpid");
				if (cconsigncorp == null || pk_corp.equals(cconsigncorp)) {
					bret = false;
				}
			}
			// 收货仓库
			else if ("crecwarehouse".equals(e.getKey())) {
				// 直运销售采购不可编辑
				if ("Z".equals(getVerifyRule()))
					bret = false;
			}
		    // 换算率
			else if ("scalefactor".equals(e.getKey())) {
				UFBoolean assistunit = getBillCardTools().getBodyUFBooleanValue(e.getRow(), "assistunit");
				UFBoolean fixedflag = getBillCardTools().getBodyUFBooleanValue(e.getRow(), "fixedflag");
				if (!assistunit.booleanValue() || fixedflag.booleanValue() )
					bret = false;
			}
		    // 报价换算率
			else if ("nqtscalefactor".equals(e.getKey())) {
				String cunitid = getBillCardTools().getBodyStringValue(e.getRow(), "cunitid");
				String cquoteunitid = getBillCardTools().getBodyStringValue(e.getRow(), "cquoteunitid");
				
				UFBoolean bqtfixedflag = getBillCardTools().getBodyUFBooleanValue(e.getRow(), "bqtfixedflag");
				if ( cunitid.equals(cquoteunitid) || bqtfixedflag.booleanValue() )
					bret = false;
			}
			// 是否固定换算率
			else if ("fixedflag".equals(e.getKey())) {
				// 不在允许修改 V502 qurui
				bret = false;
			}
		    // 是否报价固定换算率
			else if ("bqtfixedflag".equals(e.getKey())) {
				bret = false;
			}
		    // 币种 
			else if ("ccurrencytypeid".equals(e.getKey())||"ccurrencytypename".equals(e.getKey())) {
				bret = false;
			}

			// 赠品
			else if ("blargessflag".equals(e.getKey())) {
				/** 已出库或已开票的订单不允许修订赠品标记 dongwei zhongwei 参照以往需求* */
				if (getBodyValueAt(e.getRow(), "ntotalinvoicenumber") != null
						&& ((UFDouble) getBodyValueAt(e.getRow(),
								"ntotalinvoicenumber")).doubleValue() > 0)
					return false;
				else if (getBodyValueAt(e.getRow(), "ntotalinventorynumber") != null
						&& ((UFDouble) getBodyValueAt(e.getRow(),
								"ntotalinventorynumber")).doubleValue() > 0)
					return false;
				else
					return true;
			}

		}// end before edit body

		getSORefDelegate().beforeEdit(e);

		uipanel.getPluginProxy().beforeEdit(e);
		
		return bret;
	}

	/**
	 * 存货编辑前事件处理。
	 * 
	 * 创建日期：(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 * 
	 */
	private void beforeInventoryEdit(BillEditEvent e) {

		UIRefPane invRef = (UIRefPane) getBodyItem("cinventorycode")
				.getComponent();

		// 默认加载当前行已选的存货
//		if (invRef != null) {
//			invRef.setPK(getBodyValueAt(e.getRow(), "cinventoryid"));
//		}

		if (getSouceBillType().equals(SaleBillType.SoContract)
				|| getSouceBillType().equals(SaleBillType.SoInitContract)) {
			beforeCtInvEdit(e);
		}

		AbstractRefModel m = invRef.getRefModel();
//		String calid = (String) getBodyValueAt(e.getRow(), "cadvisecalbodyid");
//		if (calid != null && calid.trim().length() > 0) {
//			String[] o = new String[] { pk_corp, calid };
//			m.setUserParameter(o);
//		} else {
//			invRef.setPK(null);
//		}

		// sp1: 参数SO03（同一存货可否列多行）来控制存货的参照
		/** 赠品同样参照不到，但可以通过粘贴行实现支持，保存有校验* */
		if (uipanel.SO_03 != null && !uipanel.SO_03.booleanValue()) {
			m.setFilterPks(getInvPks(),
					IFilterStrategy.REFDATACOLLECT_MINUS_INSECTION);
		}

		// getDynamicColClass(invRef.getRefModel().getDynamicColClassName())
		// .getDynamicInfo(invRef.getRefModel().getUserParameter(),
		// invRef.getRefModel());

	}

	// private IDynamicColumn getDynamicColClass(String className) {
	//
	// // 是否实现接口检查
	// IDynamicColumn newDynamicClass = null;
	// try {
	// newDynamicClass = (IDynamicColumn) Class.forName(className)
	// .newInstance();
	// } catch (Exception e) {
	// e.printStackTrace();
	// return null;
	//
	// }
	//
	// return newDynamicClass;
	// }

	/**
	 * 得到当前存货列表
	 * 
	 * @return
	 */
	private String[] getInvPks() {
		int rowCount = getRowCount();
		ArrayList list = new ArrayList();
		int icurrow = getBillTable().getSelectedRow();
		Object temp = null;
		for (int i = 0; i < rowCount; i++) {
			if (i == icurrow)
				continue;
			temp = getBodyValueAt(i, "cinventoryid");
			if (temp != null && !list.contains(temp)) {
				list.add(temp);
			}
		}
		String[] sResults = new String[list.size()];
		list.toArray(sResults);
		return sResults;
	}

	/**
	 * 合同参照生成的存货，该行存货的编辑状态和存货参照条件修改
	 * 
	 * 创建日期：(2002-10-21 13:16:29)
	 * 
	 * @param ev
	 *            nc.ui.pub.bill.BillEditEvent
	 * 
	 */
	private void beforeCtInvEdit(BillEditEvent ev) {
		UIRefPane refInv = (UIRefPane) getBodyItem("cinventorycode")
				.getComponent();

		if (refInv == null)
			return;

		if (getSouceBillType().equals(SaleBillType.SoContract)
				|| getSouceBillType().equals(SaleBillType.SoInitContract)) {
			String sCtinvclid = (String) getBodyValueAt(ev.getRow(),
					"ctinvclassid");
			if (sCtinvclid == null || sCtinvclid.length() == 0) {

				refInv.getRefModel().setWherePart(sInvRefCondition);
			} else {

				String[] formula = { "ctinvclassid->getColValue(bd_invcl,invclasscode,pk_invcl,ctinvclassid)" };
				execBodyFormulas(ev.getRow(), formula);
				String sCtinvclcode = (String) getBodyValueAt(ev.getRow(),
						"ctinvclassid");
				setBodyValueAt(sCtinvclid, ev.getRow(), "ctinvclassid");
				// 有存货分类的存货
				setCellEditable(ev.getRow(), "cinventorycode", true);
				refInv
						.getRefModel()
						.setWherePart(
								sInvRefCondition
										+ " AND pk_invcl IN (SELECT pk_invcl FROM bd_invcl WHERE invclasscode LIKE '"
										+ sCtinvclcode + "%') ");
			}
		} else {
			setCellEditable(ev.getRow(), "cinventorycode", true);
			refInv.getRefModel().setWherePart(sInvRefCondition);
		}
	}

	private void beforeBodyAddressEdit(BillEditEvent ev) {
		String sCustManID = (String) (getBodyValueAt(ev.getRow(),
				"creceiptcorpid"));
		// 收货地址参照
		UIRefPane vreceiveaddress = (UIRefPane) getBodyItem("vreceiveaddress")
				.getComponent();
		((CustAddrRefModel) vreceiveaddress.getRefModel())
				.setCustId(sCustManID);

	}

	/**
	 * 批次参照编辑事件前的处理 创建日期：(2002-10-21 13:16:29)
	 * 
	 * @param ev
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	private void beforeBatchidEdit(BillEditEvent ev) {

		if (getBillCardTools().isOtherCorpRow(ev.getRow())) {
			setCellEditable(ev.getRow(), "cbatchid", false);
			return;
		}
		stopEditing();
		int iEventRow = ev.getRow();
		try {
			Object tempO = getBodyValueAt(iEventRow, "nnumber");
			UFDouble dNumber = (tempO == null || tempO.toString().trim()
					.length() == 0) ? new UFDouble(0)
					: (UFDouble) (getBodyValueAt(iEventRow, "nnumber"));
			if (dNumber.compareTo(new UFDouble(0)) < 0) {

				tfieldBatch.setMaxLength(30);
				getBodyPanel().setTableCellEditor("cbatchid",
						new nc.ui.pub.bill.BillCellEditor(tfieldBatch));
			} else {
				getBodyPanel().setTableCellEditor("cbatchid",
						new nc.ui.pub.bill.BillCellEditor(getLotNumbRefPane()));
				// 构造仓库VO
				WhVO voWh = null;
				String idCalbody = getHeadItem("ccalbodyid").getValue() == null ? ""
						: getHeadItem("ccalbodyid").getValue();
				Object oTemp = getBodyValueAt(iEventRow, "cbodywarehouseid");
				String idWareHouse = oTemp == null ? "" : oTemp.toString()
						.trim();
				if (idCalbody.length() != 0 || idWareHouse.length() != 0) {
					voWh = new WhVO();
					voWh.setPk_calbody(idCalbody);
					voWh.setPk_corp(pk_corp);
					voWh.setCwarehouseid(idWareHouse);
					voWh.setCwarehousename((String) getBodyValueAt(iEventRow,
							"cbodywarehousename"));
				}

				// 构造存货VO
				InvVO voInv = new InvVO();
				// if (alInvs.size() > iEventRow)
				// voInv = (InvVO) alInvs.get(iEventRow);
				/** 不能按照行索引获取，使用存货管理档案id，解决表体排序后取值错误问题* */
				for (int i = 0, len = alInvs.size(); i < len; i++) {
					if (((InvVO) alInvs.get(i)).m_cinventoryid
							.equals(getBodyValueAt(iEventRow, "cinventoryid"))) {
						voInv = (InvVO) alInvs.get(i);
						break;
					}
				}
				/** 不能按照行索引获取，使用存货管理档案id，解决表体排序后取值错误问题* */

				setBodyFreeValue(iEventRow, voInv);
				Object invID = getBodyValueAt(iEventRow, "cinventoryid");
				if (invID != null) {
					LotNumbRefPane batchref = (LotNumbRefPane) ((UIRefPane) getBodyItem(
							"cbatchid").getComponent());
					batchref.setAutoCheck(false);

					batchref.setParameter(voWh, voInv);
				}
			}
		} catch (Exception e1) {
			SCMEnv.out("批次查询失败！");
			// e1.printStackTrace(System.out);
		}
	}

	/**
	 * 返回 LotNumbRefPane1 特性值。
	 * 
	 * @return nc.ui.ic.pub.lot.LotNumbRefPane
	 */
	protected nc.ui.ic.pub.lot.LotNumbRefPane getLotNumbRefPane() {
		if (ivjLotNumbRefPane == null) {
			try {
				ivjLotNumbRefPane = new nc.ui.ic.pub.lot.LotNumbRefPane();
				ivjLotNumbRefPane.setName("LotNumbRefPane");
				ivjLotNumbRefPane.setLocation(38, 1);
				ivjLotNumbRefPane.setMaxLength(30);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjLotNumbRefPane;
	}

	private SORefDelegate getSORefDelegate() {
		if (soRefDelegate == null)
			soRefDelegate = new SORefDelegate(uipanel);
		return soRefDelegate;
	}

	public void actionPerformed(ActionEvent e) {
		UIMenuItem item = (UIMenuItem) e.getSource();
		
		if (item instanceof SoUIMenuItem) {
			uipanel.onButtonClicked(((SoUIMenuItem) item).getButtonObject());
		}
		else {
			if (item == getInsertLineMenuItem()) {
				uipanel.onInsertLine();
			} else if (item == getAddLineMenuItem()) {
				uipanel.onAddLine();
			} else if (item == getDelLineMenuItem()) {
				uipanel.onDelLine();
			} else if (item == getCopyLineMenuItem()) {
				uipanel.onCopyLine();
			} else if (item == getPasteLineMenuItem()) {
				uipanel.onPasteLine();
			} else if (item == getPasteLineToTailMenuItem()) {
				uipanel.onPasteLineToTail();
			}
	
			uipanel.getPluginProxy().onMenuItemClick(e);
		}
		
		uipanel.setButtonsState();
	}

	public SOBillCardTools getBillCardTools() {
		return uipanel.getBillCardTools();
	}

	public SaleBillCardUI getSaleBillCardUI() {
		return uipanel;
	}
	
	/**
	 * 每当部件抛出异常时被调用
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	protected void handleException(java.lang.Throwable exception) {

		/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
		SCMEnv.out("--------- 未捕捉到的异常 ---------");
		SCMEnv.out(exception);
		//exception.printStackTrace();
	}

	public void cleanNullLine() {
		int rowCount = getRowCount();
		InvVO invvo = null;
		for (int i = rowCount - 1; i >= 0; i--) {
			Object oTemp = getBodyValueAt(i, "cinventoryid");
			if (oTemp == null || oTemp.toString().trim().length() == 0) {
				setBodyValueAt(null, i, "vfree1");
				setBodyValueAt(null, i, "vfree2");
				setBodyValueAt(null, i, "vfree3");
				setBodyValueAt(null, i, "vfree4");
				setBodyValueAt(null, i, "vfree5");
			} else {
				if (alInvs != null && alInvs.size() > i) {
					invvo = (InvVO) alInvs.get(i);
					if (invvo != null && oTemp.equals(invvo.getCinventoryid())) {
						if (invvo.getFreeItemVO() == null
								|| invvo.getFreeItemVO().getVfreeid1() == null
								|| invvo.getFreeItemVO().getVfreeid1().trim()
										.length() <= 0)
							setBodyValueAt(null, i, "vfree1");
						if (invvo.getFreeItemVO() == null
								|| invvo.getFreeItemVO().getVfreeid2() == null
								|| invvo.getFreeItemVO().getVfreeid2().trim()
										.length() <= 0)
							setBodyValueAt(null, i, "vfree2");
						if (invvo.getFreeItemVO() == null
								|| invvo.getFreeItemVO().getVfreeid3() == null
								|| invvo.getFreeItemVO().getVfreeid3().trim()
										.length() <= 0)
							setBodyValueAt(null, i, "vfree3");
						if (invvo.getFreeItemVO() == null
								|| invvo.getFreeItemVO().getVfreeid4() == null
								|| invvo.getFreeItemVO().getVfreeid4().trim()
										.length() <= 0)
							setBodyValueAt(null, i, "vfree4");
						if (invvo.getFreeItemVO() == null
								|| invvo.getFreeItemVO().getVfreeid5() == null
								|| invvo.getFreeItemVO().getVfreeid5().trim()
										.length() <= 0)
							setBodyValueAt(null, i, "vfree5");
					}
				}
			}
		}
		for (int i = rowCount - 1; i >= 0; i--) {
			Object oTemp = getBodyValueAt(i, "cinventoryid");
			if (oTemp == null || oTemp.toString().trim().length() == 0) {
				int[] rowIndex = { i };
				getBillData().getBillModel().delLine(rowIndex);
				if (alInvs != null && alInvs.size() > i) {
					alInvs.remove(i);
				}

				if (uipanel.vRowATPStatus != null
						&& uipanel.vRowATPStatus.size() > i
						&& uipanel.vRowATPStatus.size() > 0) {
					uipanel.vRowATPStatus.remove(i);
				}
			}
		}
	}

	/**
	 * 得到单据VO。 创建日期：(2001-6-23 9:47:36) 当粘贴行时，处理捆绑和买赠
	 * 
	 * @return nc.vo.so.so001.SaleOrderVO
	 */
	public void setBindAndLargeWhenPaste(int startrow, int stoprow) {
		UFBoolean bLargess = null;
		String sPk = null;
		for (int i = stoprow; i >= startrow; i--) {
			bLargess = new UFBoolean(
					getBodyValueAt(i, "blargessflag") == null ? "false"
							: getBodyValueAt(i, "blargessflag").toString());

			sPk = (String) getBodyValueAt(i, "cinventoryid");
			if (bLargess != null && bLargess.booleanValue()) {

			} else if (sPk != null && sPk.trim().length() > 0) {
				setBodyValueAt(null, 0, "clargessrowno");
				afterInventoryMutiEdit(i, new String[] { sPk }, false, true,
						null, true, 2);
			}
		}
	}

	/**
	 * 初始化换算率。 创建日期：(2001-10-9 13:05:04)
	 */
	public void initUnit() {

		if (getRowCount() <= 0)
			return;

		try {
			new UnitOfMeasureTool(getBillModel()).calculateUnitOfMeasure();
			for (int i = 0, count = getRowCount(); i < count; i++) {
				InvVO voInv = null;
				if (alInvs != null && alInvs.size() > i)
					voInv = (InvVO) alInvs.get(i);
				if (voInv != null) {

					if (voInv.getIsAstUOMmgt() != null
							&& voInv.getIsAstUOMmgt().intValue() == 1)
						setBodyValueAt("Y", i, "assistunit");
					else
						setBodyValueAt("N", i, "assistunit");

					voInv.setCastunitid((String) getBodyValueAt(i,
							"cpackunitid"));
					voInv.setCastunitname((String) getBodyValueAt(i,
							"cpackunitname"));
				}

				setAssistUnit(i);
			}

		} catch (Exception ex) {
			SCMEnv.out("初始化换算率失败!");
		}
	}

	/**
	 * 设置模板输入限制。 创建日期：(2001-11-1 15:26:17)
	 */
	protected void setInputLimit() {

		// 设置销售公司为不可编辑项
		getHeadItem("salecorp").setEnabled(false);
		getHeadItem("salecorp").setEdit(false);
		getHeadItem("nreceiptcathmny").setEnabled(false);
		getHeadItem("nreceiptcathmny").setEdit(false);
		// 设置产品线不可编辑
		getBodyItem("cprolinename").setEdit(false);
		getBodyItem("cprolinename").setEnabled(false);

		// 整单折扣
		((UIRefPane) getHeadItem("ndiscountrate").getComponent())
				.setMinValue(0.0);

		// 单品折扣
		((UIRefPane) getBodyItem("nitemdiscountrate").getComponent())
				.setMinValue(0.0);

		// 税率
		((UIRefPane) getBodyItem("ntaxrate").getComponent()).setMinValue(0.0);

		// 预收款比例
		((UIRefPane) getHeadItem("npreceiverate").getComponent())
				.setMinValue(0.0);

		// 预收款比例
		((UIRefPane) getHeadItem("npreceiverate").getComponent())
				.setMaxValue(100.0);

		((UIRefPane) getHeadItem("npreceiverate").getComponent())
				.setMaxLength(20);

		getHeadItem("npreceiverate").setLength(20);

		/**v5.3去掉 */
		/*// 帐期
		((UIRefPane) getHeadItem("naccountperiod").getComponent())
				.setMinValue(0.0);*/

		// 出口退税率
		((UIRefPane) getBodyItem("nreturntaxrate").getComponent())
				.setMinValue(0.0);

		// 出口退税率
		((UIRefPane) getBodyItem("nreturntaxrate").getComponent())
				.setMaxValue(100.0);

		UIRefPane ref = null;


		if (getBillType().equals(SaleBillType.SaleOrder)) {
			// 存货
			UIRefPane refInv = (UIRefPane) getBodyItem("cinventorycode")
					.getComponent();

			if (refInv != null) {

				refInv.getRefModel().setIsDynamicCol(true);
				refInv.getRefModel().setDynamicColClassName(
						"nc.ui.scm.pub.RefDynamic");

				// 03-10-22 yb edit 存货管理档案存货是否可销售
				if (refInv.getRefModel().getWherePart().indexOf(
						"and bd_invmandoc.iscansold ='Y'") < 0) {
					refInv.getRefModel().setWherePart(
							refInv.getRefModel().getWherePart()
									+ " and bd_invmandoc.iscansold ='Y' ");
				}

				if (sInvRefCondition == null) {
					sInvRefCondition = refInv.getRefModel().getWherePart();
				}
			}

			// 发货公司
			ref = (UIRefPane) getBodyItem("cconsigncorp").getComponent();
			ref.getRefModel().setNotLeafSelectedEnabled(true);
		}

		getBillCardTools().setHeadRefLimit(uipanel.strState);

	}

	/**
	 * 计算合计。 创建日期：(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public UFDouble calcurateTotal(String key) {

		UFDouble total = SoVoConst.duf0;

		for (int i = 0; i < getRowCount(); i++) {
			UFBoolean blargessflag = getBillCardTools().getBodyUFBooleanValue(
					i, "blargessflag");
			// 20061026 增加缺货判断
			UFBoolean boosflag = getBillCardTools().getBodyUFBooleanValue(i,
					"boosflag");

			if (SaleorderBVO.isPriceOrMny(key)
					&& ((blargessflag != null && blargessflag.booleanValue()) || (boosflag != null && boosflag
							.booleanValue())))
				continue;

			Object value = getBodyValueAt(i, key);
			String v = (value == null || value.toString().trim().length() <= 0) ? "0"
					: value.toString();
			total = total.add(new UFDouble(v));

		}

		return total;
	}

	/**
	 * 排序后触发。 创建日期：(2001-10-26 14:31:14)
	 * 
	 * @param key
	 *            java.lang.String
	 */
	public void afterSort(java.lang.String key) {

		if (uipanel.strState.equals("修改") || uipanel.strState.equals("修订")
				|| uipanel.strState.equals("新增")) {
			initFreeItem();

			// 加自由项变色龙。
			InvAttrCellRenderer ficr = new InvAttrCellRenderer();
			ficr.setFreeItemRenderer(this, alInvs);
		}
		return;

	}

	/**
	 * 表体排序
	 * 
	 * @return
	 */
	public Object[] getRelaSortObjectArray() {
		AggregatedValueObject avo = uipanel.getVo();

		updateRowIndex();

		return avo.getChildrenVO();
	}

	private HashMap<String, Integer> rowindex = new HashMap<String, Integer>();

	/**
	 * 表体排序之后,更新行对应顺序<行号，行>
	 * 
	 */
	private void updateRowIndex() {
		for (int i = 0, len = getRowCount(); i < len; i++) {
			rowindex.put(getBodyValueAt(i, "crowno").toString(), i);
		}
	}
	
	public void addSortLstn() {
		getBillModel().addSortListener(this);
	}

	public void addSortRelaLstn() {
		getBillModel().addSortRelaObjectListener2(this);
	}

	public void addEditLstn() {
		addEditListener("table", this);
	}

	public void addEditLstn2() {
		addBodyEditListener2("table", this);
	}
	
	/**
	 * 
	 * 描述：添加表体右击菜单的点击事件侦听器
	 * <p>
	 * <b>参数说明</b>
	 * @param items 右击菜单条目
	 * <p>
	 * @author duy
	 * @time 2008-12-19 下午04:02:00
	 */
	public void addMenuListener(UIMenuItem[] items) {
		for (UIMenuItem item : items) {
			item.removeActionListener(this);
			item.addActionListener(this);
		}
	}

	public void addTotalLstn() {
		addBodyTotalListener("table", this);
	}
	
	// 排序前监听
	public void addSortPrepareListener() {
		getBillModel("table").setSortPrepareListener(this);
	}

	public boolean binvedit;

	/* 
	 * 排序时，返回排序值得数据类型
	 * (non-Javadoc)
	 * @see nc.ui.pub.bill.IBillModelSortPrepareListener#getSortTypeByBillItemKey(java.lang.String)
	 */
	public int getSortTypeByBillItemKey(String key) {
		if (key.equals("crowno"))
			return BillItem.DECIMAL;
		return BillItem.STRING;
	}
	
	public void showHitDlg(String hitMsg){
		MessageDialog.showHintDlg(getSaleBillCardUI(), "提示", hitMsg);
	}

	/**
	 * 当前单据
	 * 某业务类型的核算规则:Z--直运类  Y--普通业务类  C--借出转销售  S--委托代销类
	 */
	public String getVerifyRule() {
		String cbiztype = getBillCardTools().getHeadValue("cbiztype")
				.toString();
		String sverifyRule = verifyRule.get(cbiztype);
		if (sverifyRule == null) {
			IPFMetaModel bo = (IPFMetaModel) NCLocator.getInstance().lookup(
					IPFMetaModel.class.getName());
			BusitypeVO busvo;
			try {
				busvo = bo.findBusitypeByPK(cbiztype);
				sverifyRule = busvo.getVerifyrule();
				verifyRule.put(cbiztype, sverifyRule);
			} catch (BusinessException e) {
				handleException(e);
			}
		}
		return sverifyRule;
	}
	
}
