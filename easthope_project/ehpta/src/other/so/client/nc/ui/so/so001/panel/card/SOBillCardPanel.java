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
 * ���۶�����Ƭģ��
 * 
 * @author zhongwei
 * 
 * �޸����ڣ�2008-06-26 �޸��ˣ��ܳ�ʤ �޸����ݣ�v5.5��������Ŀ�޸�
 */
public class SOBillCardPanel extends BillCardPanel implements BillEditListener,
		BillEditListener2, BillTotalListener, BillSortListener,
		IBillRelaSortListener2, IBillModelSortPrepareListener, ActionListener {

	private SaleBillCardUI uipanel;

	private String pk_corp;
	
	private Object oldValue;
	
	//�������۶���ҵ�����ͺ������<ҵ������id,���ͺ������>
	private HashMap<String,String> verifyRule = new HashMap<String,String>();
	
	//�ݴ��ͷ�����ۿ�
	public Object ndiscountrate=null;

	// ��Ա���ճ�ʼ����
	public String sEmployeeRefCondition = null;

	protected FreeItemRefPane ivjFreeItemRefPane = null;

	protected SORefDelegate soRefDelegate = null;

	// ���β���
	protected nc.ui.ic.pub.lot.LotNumbRefPane ivjLotNumbRefPane = null;

	// �������������arraylist
	public ArrayList alInvs = new ArrayList();

	// ��ͬ��Ϣ����
	public Hashtable hCTTypeVO = null;
	
	//�༭���ʱ�Ƿ����������ϸ�ִ�к�ͬ
	public boolean ifbindct = false;
	//�༭���ʱ������ͬʱ�Ƿ��Ѿ�ִ�й���ͬ�����ж�
	public boolean ifbindct_findPrice = false;

	//�༭����ʱ������༭��keyֵ
	private String firstChangeKey = null;
	
	// ��Ŵ�������֯Ĭ�Ϸ�����˾
	protected HashMap m_hConCal = new HashMap();

	// ������ճ�ʼ����
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

		// ��˾
		setCorp(pk_corp);
		this.pk_corp = pk_corp;

		// ����Ա
		setOperator(operator);

		// ��Ӽ���
		addBillEditListenerHeadTail(this);

		setBodyMenuShow(true);

		// ��Ӻϼ���
		setTatolRowShow(true);		
		
		getBillTable().setSelectionMode(
		    javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		setEnabled(false);

	}

	/**
	 * �༭���¼�����
	 * 
	 * �������ڣ�(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public void afterEdit(BillEditEvent e) {

		getBillModel().setNeedCalculate(false);
		uipanel.showHintMessage("");
		
		this.firstChangeKey = e.getKey();
		
		//�ݴ��ֵ
		this.oldValue = e.getOldValue();
		
		//�Ƿ�༭���һ�д������ʼ��false
		this.binvedit = false;
		
		try {
			if (e.getPos() == BillItem.HEAD) {

				if (e.getKey().equals("vreceiptcode")) {
					uipanel.m_isCodeChanged = true;
				}

				// ����
				if (e.getKey().equals("ccustomerid")) {
					afterCustomerEdit(e);
					freshBodyLargess(1);
				}
				// ����
				else if (e.getKey().equals("cdeptid")) {
					afterDeptEdit(e);
				}
				// ��Ա
				else if (e.getKey().equals("cemployeeid")) {
					afterEmployeeEdit(e);
				}
				// ������֯
				else if (e.getKey().equals("csalecorpid")) {
					if (!getSouceBillType().equals("4H") && !getSouceBillType().equals("42")) {
						// ���ñ����ȱʡ�ķ�����˾�������֯
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
				// �����ۿ���
				else if (e.getKey().equals("ndiscountrate")) {
					afterDiscountrateEdit(e);
				}
				// ����˰��
				else if (e.getKey().equals("ntaxrate")) {
					afterTaxrateBillEdit(e);
				}
				// ����
				else if (e.getKey().equals("dbilldate")) {
					afterHeadBillDateEdit(e);
					freshBodyLargess(2);
				}
				// ����
				else if (e.getKey().equals("ccurrencytypeid")) {
					afterCurrencyEdit(e);
					freshBodyLargess(2);
				}
				// �۱�����
				else if (e.getKey().equals("nexchangeotobrate")) {
					afterChangeotobrateEdit(e);
				}
				// �����֯
				else if (e.getKey().equals("ccalbodyid")) {
					afterCcalbodyidEdit(e);
				}
				// �ֿ�
				else if (e.getKey().equals("cwarehouseid")) {
					afterCwarehouseidEdit(e);
				}
				// �ջ���λ
				else if (e.getKey().equals("creceiptcustomerid")) {
					afterCreceiptcorpEdit(e);
				}
				// �ջ���ַ
				else if (e.getKey().equals("vreceiveaddress")) {
					afterVreceiveaddressEdit(e);
				}
				// ���䷽ʽ
				else if (e.getKey().equals("ctransmodeid")) {
					afterCtransmodeEdit(e);
				}
				// �Զ�����
				else if (e.getKey().startsWith("vdef")) {
					DefSetTool
							.afterEditHead(getBillData(), e.getKey(),
									"pk_defdoc"
											+ e.getKey().substring(
													"vdef".length()));
				}
				// ��˰�ϼ�
				else if ("nheadsummny".equals(e.getKey())) {
					afterHeadsummnyEdit(e);
				}

				findPriceWhenHeadItemChg(e.getKey());

			}
			if (e.getPos() == BillItem.BODY) {

				if (e.getKey().equals(getRowNoItemKey())) {
					afterRownoEdit(e);
				}				
				// ������� �������
				if (e.getKey().equals("cinventorycode")) {
					afterInventoryMutiEdit(e);
				}
				// ����λ
				else if (e.getKey().equals("cpackunitname")
						|| e.getKey().equals("cquoteunit")) {
					afterUnitEdit(e);
				}
				// ��ͬ
				else if (e.getKey().equals("ct_name")) {
					afterCtManageEdit(e);
				}

				// ������
				else if (e.getKey().equals("vfree0")) {
					afterFreeItemEdit(e);
				}
				
				if (SaleorderBVO.isPriceOrMny(e.getKey()))
					// ���۽��仯
					afterPriceMuyEdit(e);
				else if (!"cinventorycode".equals(e.getKey())&&!"cinventoryid".equals(e.getKey()))
					// �����仯
					afterNumberEdit(e);

				// ��Ʒ
				if (e.getKey().equals("blargessflag")) {
					afterLargessFlagEdit(e);
				}
				// ��Ŀ
				else if (e.getKey().equals("cprojectname")) {
					afterProjectEdit(e);
				}
				// ����״̬
				else if (e.getKey().equals("fbatchstatus")) {
					afterBatchEdit(e);
				}
				// ����
				else if (e.getKey().equals("cbatchid")) {
					afterBatchIDEdit(e);
				}
				// ��������֯(������ͷ�����֯�ͱ��巢�������֯)
				else if (e.getKey().equals("cadvisecalbody")) {
					afterCcalbodyidEdit(e);
				}
				// ����ֿ�
				else if (e.getKey().equals("cbodywarehousename")) {
					afterCwarehouseidEdit(e);
				}
				// �����ջ������֯
				else if (e.getKey().equals("creccalbody")) {
					String creccalbody = getBillCardTools().getBodyStringValue(
							e.getRow(), "creccalbody");
					if (creccalbody == null
							|| !creccalbody.equals(e.getOldValue())) {
						setBodyValueAt(null, e.getRow(), "crecwareid");
						setBodyValueAt(null, e.getRow(), "crecwarehouse");
					}
                    //����ֱ�˲�
		            setDerictTransReceWareHouse(e.getRow());
				}
				// �����ջ��ֿ�
				else if (e.getKey().equals("crecwarehouse")) {
					afterCrecwarehouseEdit(e);
				}
				// ȱ��
				else if (e.getKey().equals("boosflag")) {
					afterOOSFlagEdit(e.getRow(), true);
					
					// ���±�ͷ��˰�ϼ�
					// �˴�Ӧ���ͷ��˰�ϼ��㷨ͬ��
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
				// ����
				else if (e.getKey().equals("bsupplyflag")) {
					afterOOSFlagEdit(e.getRow(), false);
				}
				// ��������
				else if (e.getKey().equals("dconsigndate")) {
					afterBodyDateEdit(e.getRow(), true);
				}
				// ��������
				else if (e.getKey().equals("ddeliverdate")) {
					afterBodyDateEdit(e.getRow(), false);
				}
				// �ջ���λ
				else if (e.getKey().equals("creceiptcorpname")) {
					afterBodyCreceiptcorpidEdit(e);
				}
				// �ջ���ַ
				else if (e.getKey().equals("vreceiveaddress")) {
					afterBodyAddressEdit(e);
				}
				// �Ƿ�ֱ��(ֱ�˵���)
				else if (e.getKey().equals("bdericttrans")) {
					//afterBdericttransEdit(e);
				}
				// ���巢����˾
				else if (e.getKey().equals("cconsigncorp")) {
					afterCconsignCorpEdit(e.getRow());
				}
				// �Զ�����
				else if (e.getKey().startsWith("vdef")) {
					DefSetTool.afterEditBody(getBillData().getBillModel(), e
							.getRow(), e.getKey(), "pk_defdoc"
							+ e.getKey().substring("vdef".length()));
				}

				// �ջ�����
				else if (e.getKey().equals("creceiptareaname")) {
					String sDateDeliver = getBodyValueAt(e.getRow(),
							"ddeliverdate") == null ? null : getBodyValueAt(
							e.getRow(), "ddeliverdate").toString().trim();

					if (sDateDeliver == null || sDateDeliver.length() == 0)
						afterBodyDateEdit(e.getRow(), true);
					else
						afterBodyDateEdit(e.getRow(), false);
				}
				// ��������֯
				else if (e.getKey().equals("cadvisecalbody")) {
					String sDateDeliver = getBodyValueAt(e.getRow(),
							"ddeliverdate") == null ? null : getBodyValueAt(
							e.getRow(), "ddeliverdate").toString().trim();

					if (sDateDeliver == null || sDateDeliver.length() == 0)
						afterBodyDateEdit(e.getRow(), true);
					else
						afterBodyDateEdit(e.getRow(), false);
				}
				// �۸�����
				else if (e.getKey().equals("cpricepolicy")) {
					afterPricePolicy(e);
				}
				// ��Ŀ��
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

			// ִ�б�ͷ��ʽ
			if (e.getPos() == BillItem.HEAD) {
				String[] sFormulas = getHeadItem(e.getKey()).getEditFormulas();
				if (sFormulas != null && sFormulas.length > 0)
					execHeadFormulas(sFormulas);
			}

			getBillModel().setNeedCalculate(true);

			// ���ñ�ͷ��˰�ϼ�
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
	 * �����ջ��ֿ�Ϊֱ�˲�
	 * @param row
	 * @throws BusinessException 
	 */
	private void setDerictTransReceWareHouse(int row) {
        // ֱ�˵���
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
	 * ҵ�����͵ĺ������Ϊֱ������ʱ�����÷����ֿ�Ϊֱ�˲�
	 * @param row
	 * @throws BusinessException
	 */
	private void setDerictTransSendWareHouse(int row) {
		// ҵ�����͵ĺ������Ϊֱ������
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
	 * У��ĩ��������֯
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
															 * @res "ֻ��ĩ��������֯��ѡ"
															 */);
			}
		}
	}

	/**
	 * ����к��ڵ���ģ���е�Keyֵ
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
				if ((uipanel.strState.equals("�޶�"))){
					if ((uipanel.SO78.booleanValue()))
						rowlist.add(new Integer(i));
					continue;
				}
				rowlist.add(new Integer(i));
			}
		}

		if (rowlist.size() <= 0)
			return;

		

		//��ѯ�۵���
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
				//��ձ������ϼ۸�������ֶ�,��ͷѯ��ʱ����ƥ�䶨�۲���
				//V5.5 cnf zc
				getBillCardTools().clearBodyValue(getBillCardTools().getPriceItem(),findrows[i]);
			}
			
			//ѯ��
			findPrice(findrows, null, false);

		}

	}

	/**
	 * �Ƿ�Ѱ�ۡ�
	 * 
	 * �������ڣ�(2001-11-20 15:29:14)
	 * 
	 * @return boolean
	 * 
	 */
	private boolean isFindPrice(int i,String key) {
		Integer bindpricetype = (Integer) getBodyValueAt(i, "bindpricetype");
		if (bindpricetype != null
				&& bindpricetype.intValue() == IInvManDocConst.PRICE_TYPE_BINDLE) {
			// ʹ������ۣ�����ѯ��
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
				
				//�༭���۽��ʱ������ִ�к�ͬ����ж�
				if (firstChangeKey!=null 
						&& ( !"cinventorycode".equals(firstChangeKey)&&!"cinventoryid".equals(firstChangeKey)
								&&!"nnumber".equals(firstChangeKey)&!"nquoteunitnum".equals(firstChangeKey)) 
					)
					return uipanel.SA_15.booleanValue();
				
				String ct_manageid = (String) getBodyValueAt(i, "ct_manageid");
				TypeVO voCtType = null;
				if (ct_manageid != null && ct_manageid.length() != 0) {
					// ��ú�ͬ���ͱ�־

					if (hCTTypeVO != null)
						voCtType = (TypeVO) hCTTypeVO.get(ct_manageid);

					if (voCtType == null) {
						try {
							voCtType = SaleOrderBO_Client
									.getContractType(ct_manageid);
						} catch (Throwable ex) {
							SCMEnv.out("��ú�ͬ���ͱ�־����!");
							// ex.printStackTrace();
						}
					}

					if (voCtType != null) {
						int iInvType = voCtType.getNinvctlstyle() == null ? -1
								: voCtType.getNinvctlstyle().intValue();
						int iDataType = voCtType.getNdatactlstyle() == null ? -1
								: voCtType.getNdatactlstyle().intValue();
						// ��ͬ������Ʒ�ʽ��0��� 1�������
						// ��ͬ���ݿ��Ʒ�ʽ��(���ƺ�ͬ��Щ�����¼�롣0���� 1���� 2��� 3����+���� 4����+���
						// 5����+��� 6����+����+���)
						if (iInvType == 0
								&& (iDataType == 0 || iDataType == 3
										|| iDataType == 4 || iDataType == 6)) {
							if (uipanel.SO_17.booleanValue()) {
								//�༭���ʱ�Ƿ�������������ͬ���ϸ�ִ��
								ifbindct = true;//���ڿ��ƺ����߼��Ƿ���
								ifbindct_findPrice = true;
								return false;
							} else {
								//��ռ۸�--û��Ѱ����
								getBillCardTools().clearBodyValue(getBillCardTools().getSaleItems_Price(), i);                 
								//��ս��
								getBillCardTools().clearBodyValue(getBillCardTools().getSaleItems_Mny(), i);
							}
						} else {
							//���ϸ�ִ�к�ͬ
							//��ռ۸�
							getBillCardTools().clearBodyValue(getBillCardTools().getSaleItems_Price(), i);
							//��ս��
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
	 * Ѱ�ۡ� �������ڣ�(2001-6-23 13:42:53)
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

				// �������仯������Ѱ��
				if (isinvchg) {

					rows = (Integer[]) hs.keySet().toArray(
							new Integer[hs.size()]);
					pricerow = 0;

					for (int i = 0, loop = rows.length; i < loop; i++) {
						// ���в�������ѯ��
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

					// �����û��Ѱ������ͬ����
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
							// ���в�������ѯ��
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
					// ���в�������ѯ��
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

						// Ѱ��ʧ�ܣ������صļ۸���
						getBillCardTools().clearBodyValue(
								SOBillCardTools.pricekeys, pricerow);
						
						//ѯ�ۺ����ø��ֶα༭��
						setEditEnabled(false,pricerow);
						
						continue;
					}
					
					//ѯ�ۺ����ø��ֶα༭��
					setEditEnabled(true,pricerow);

					//����ѯ�۵õ��ĵ��ۡ����ۺ��ۿ�
					if (uipanel.SA_02.booleanValue()) {
						//����ԭʼ��Ѱ�۵��ۡ�����
						setBodyValueAt(resultvo.getNum(), pricerow,"nqtorgtaxprc");
						setBodyValueAt(resultvo.getNetNum(), pricerow,"nqtorgtaxnetprc");
						//���ñ��۽��м���
						setBodyValueAt(resultvo.getNum(), pricerow,"norgqttaxprc");
						setBodyValueAt(resultvo.getDiscount(), pricerow,"nitemdiscountrate");
						calculateNumber(rows[i].intValue(), "norgqttaxprc");
					} else {
						//����ԭʼ��Ѱ�۵��ۡ�����
						setBodyValueAt(resultvo.getNum(), pricerow,"nqtorgprc");
						setBodyValueAt(resultvo.getNetNum(), pricerow,"nqtorgnetprc");
						//���ñ��۽��м���
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
	 * �ҵ���Ҫѯ�۵��У� 
	 * 
	 * �޶���ֻ���޸��˴������������Ҫѯ��
	 * 
	 * @return
	 */
	private ArrayList getNeedFindPriceRows(int[] irows) {

		/*//�޶���ѯ��
		if (!uipanel.strState.equals("�޶�")) -=notranslate=-
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
		// ɾ��ԭ����������Ʒ���
		if (iChgtype != 3) {
			int irowcount = getRowCount();
			int[] iallrow = new int[irowcount];
			for (int i = 0; i < irowcount; i++)
				iallrow[i] = i;
			int[] inewdelline = setBlargebindLineWhenDelLine(iallrow,1);
			if (inewdelline != null && inewdelline.length > 0)
				uipanel.onDelLine(inewdelline);
		}
		// �������ñ�������
		String sPk = null;
		for (int i = getRowCount() - 1; i >= 0; i--) {
			sPk = (String) getBodyValueAt(i, "cinventoryid");
			if (sPk != null)
				afterInventoryMutiEdit(i, new String[] { sPk }, false, false,
						null, false, iChgtype);
		}

	}

	/**
	 * type=1 ����,type=2 ����
	 * ��ȡ��ǰ�и�������Ʒ�л�������
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
		
		//��Ʒ�л������б�־
		String flag = (type==1) ? "blargessflag" : "bbindflag" ;
		
		for (int i = 0; i < getRowCount(); i++) {
			// ��Ʒ������������
			sRow = (String) getBodyValueAt(i, "clargessrowno");
			bLargess_bind = new UFBoolean(
					getBodyValueAt(i, flag) == null ? "false"
							: getBodyValueAt(i, flag).toString());
			if (bLargess_bind != null && bLargess_bind.booleanValue() && sRow != null
					&& vt.contains(sRow)) {
				sRow = (String) getBodyValueAt(i, "crowno");
				// �������Ʒ�л����󲻰����ڱ�ɾ����֮��
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
	 * �޸ı�����״̬�� �������ڣ�(2001-11-26 9:30:07)
	 * 
	 * @param row
	 *            int
	 */
	public void setBodyRowState(int row) {
		if (getBillModel().getRowState(row) == BillModel.NORMAL)
			getBillModel().setRowState(row, BillModel.MODIFICATION);
	}

	/**
	 * �༭��ͷ��˰�ϼ�ʱʹ��
	 * 
	 * @param row
	 * @param key
	 * @param iaPrior
	 */
	public void calculateforHeadSummny(int row, String key, Integer iaPrior) {
		/** v5.3���۽���㷨��� */
		// System.out.println("calculateforHeadSummny----RelationsCal.calculate��#"+row+"#"+key+"#-->���ÿ�ʼ��");
		RelationsCal.calculate(row, this.oldValue, this, SaleorderBVO.getCalculatePara(key, iaPrior, uipanel.SA_02,
				uipanel.SO40), key, SaleorderBVO.getKeys(), SaleorderBVO.getField(), SaleorderBVO.class.getName(),
				SaleorderHVO.class.getName(), null);

		// System.out.println("calculateforHeadSummny----RelationsCal.calculate��#"+row+"#"+key+"#-->���ý�����");
		/** v5.3���۽���㷨��� */
	}
	
	/**
	 * ���㡣 �������ڣ�(2001-11-23 16:55:22)
	 */
	public void calculateNumber(int row, String key) {

		// �༭����λ�����ۼ�����λ���Ի�����Ϊ�仯������㷨
		if (key.equals("cpackunitname") || key.equals("cpackunitid"))
			key = "scalefactor";
		else if (key.equals("cquoteunit"))
			key = "nqtscalefactor";

		RelationsCal.calculate(row, this.oldValue, this, SaleorderBVO.getCalculatePara(key, null, uipanel.SA_02,
				uipanel.SO40), key, SaleorderBVO.getKeys(), SaleorderBVO.getField(), SaleorderBVO.class.getName(),
				SaleorderHVO.class.getName(), null);
	}
	
	/**
	 * �����Դ�������͡�
	 * 
	 * �������ڣ�(2001-11-16 13:24:23)
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
	 * �����Դ�������͡�
	 * 
	 * �������ڣ�(2001-11-16 13:24:23)
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
	 * ���̱༭���¼����� �������ڣ�(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	private void afterCustomerEdit(BillEditEvent e) {
		// ����
		String cdeptid_old = getBillCardTools().getHeadStringValue("cdeptid");
		
		// ��������
		UFDouble ndiscountrate_old = getBillCardTools().getHeadUFDoubleValue(
				"ndiscountrate");
		// ���˷�ʽ
		String ctransmodeid_old = getBillCardTools().getHeadStringValue(
				"ctransmodeid");
		// Ĭ�Ͻ��ױ���
		String ccurrencytypeid_old = getBillCardTools().getHeadStringValue(
				"ccurrencytypeid");
		// �ո���Э��
		String ctermprotocolid_old = getBillCardTools().getHeadStringValue(
				"ctermprotocolid");
		// �����֯
		String ccalbodyid_old = getBillCardTools().getHeadStringValue(
				"ccalbodyid");
		// ������֯
		String csalecorpid_old = getBillCardTools().getHeadStringValue(
				"csalecorpid");
		String creceiptcorpid_old = getBillCardTools().getHeadStringValue(
				"creceiptcorpid");
		String cwarehouseid_old = getBillCardTools().getHeadStringValue(
				"cwarehouseid");

		// //////////////////////////////////////////////////////////////////////////
		ArrayList<String> formulas = new ArrayList();
		// ��Ʊ��λ
		formulas
				.add("creceiptcorpid->getColValue(bd_cumandoc,pk_cusmandoc2,pk_cumandoc,ccustomerid)");
		// �ջ���λ
		formulas
				.add("creceiptcustomerid->getColValue(bd_cumandoc,pk_cusmandoc3,pk_cumandoc,ccustomerid)");
		// ����
		formulas
				.add("cdeptid->getColValue(bd_cumandoc,pk_respdept1,pk_cumandoc,ccustomerid)");
		// ҵ��Ա
		formulas
				.add("cemployeeid->getColValue(bd_cumandoc,pk_resppsn1,pk_cumandoc,ccustomerid)");
		// ��������
		formulas
				.add("ndiscountrate->getColValue(bd_cumandoc,discountrate,pk_cumandoc,ccustomerid)");
		// ���˷�ʽ
		formulas
				.add("ctransmodeid->getColValue(bd_cumandoc,pk_sendtype,pk_cumandoc,ccustomerid)");
		// Ĭ�Ͻ��ױ���
		formulas
				.add("ccurrencytypeid->getColValue(bd_cumandoc,pk_currtype1,pk_cumandoc,ccustomerid)");
		// �ո���Э��
		formulas
				.add("ctermprotocolid->getColValue(bd_cumandoc,pk_payterm,pk_cumandoc,ccustomerid)");
		// �����֯
		formulas
				.add("ccalbodyid->getColValue(bd_cumandoc,pk_calbody,pk_cumandoc,ccustomerid)");
		// ������֯
		formulas
				.add("csalecorpid->getColValue(bd_cumandoc,pk_salestru,pk_cumandoc,ccustomerid)");
		
		// �ͻ���������
		formulas
				.add("ccustbasid->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,ccustomerid)");
		// ɢ����־
		formulas
				.add("bfreecustflag->getColValue(bd_cubasdoc,freecustflag,pk_cubasdoc,ccustbasid)");
		// �ͻ���ID
		formulas
				.add("careaclid->getColValue(bd_cubasdoc,pk_areacl,pk_cubasdoc,ccustbasid)");
		// �ͻ������
		formulas
				.add("careaclcode->getColValue(bd_areacl,areaclcode,pk_areacl,careaclid)");
		formulas
				.add("custshortname->getColValue(bd_cubasdoc,custshortname,pk_cubasdoc,ccustbasid)");
		// Ԥ�տ����
		formulas
				.add("npreceiverate->getColValue(bd_cumandoc,prepaidratio,pk_cumandoc,ccustomerid)");
		//�ֿ�
		formulas
		    .add("cwarehouseid->getColValue(bd_cumandoc,pk_stordoc2,pk_cumandoc,ccustomerid)");

		getBillData().execHeadFormulas(formulas.toArray(new String[] {}));

		// ����
		String cdeptid = getBillCardTools().getHeadStringValue("cdeptid");
		if ((cdeptid == null || cdeptid.trim().length() <= 0)
				&& (cdeptid_old != null && cdeptid_old.trim().length() > 0)) {
			cdeptid = cdeptid_old;
			setHeadItem("cdeptid", cdeptid_old);
		}

		// �ۿ�
		String sdiscountrate = getHeadItem("ndiscountrate").getValue();

		// ��������
		// Ĭ��100
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

		// ���˷�ʽ
		String stemp = getBillCardTools().getHeadStringValue("ctransmodeid");
		if (stemp == null || stemp.trim().length() <= 0)
			setHeadItem("ctransmodeid", ctransmodeid_old);

		// Ĭ�Ͻ��ױ���
		String ccurrencytypeid = getBillCardTools().getHeadStringValue(
				"ccurrencytypeid");
		if ((ccurrencytypeid == null || ccurrencytypeid.trim().length() <= 0)
				&& (ccurrencytypeid_old != null && ccurrencytypeid_old.trim()
						.length() > 0)) {
			ccurrencytypeid = ccurrencytypeid_old;
			setHeadItem("ccurrencytypeid", ccurrencytypeid_old);
		}

		// �ո���Э��
		stemp = getBillCardTools().getHeadStringValue("ctermprotocolid");
		if (stemp == null || stemp.trim().length() <= 0)
			setHeadItem("ctermprotocolid", ctermprotocolid_old);

		String ccalbodyid = null;
		// �����֯,�����ԴΪ�������������֯����
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

		// ������֯
		stemp = getBillCardTools().getHeadStringValue("csalecorpid");
		if (stemp == null || stemp.trim().length() <= 0)
			setHeadItem("csalecorpid", csalecorpid_old);

		// ��δ�����ջ���λ����ȡ�ͻ�
		if ((getHeadItem("creceiptcustomerid") != null)
				&& (getHeadItem("creceiptcustomerid").getValue() == null || getHeadItem(
						"creceiptcustomerid").getValue().length() <= 0)) {
			getHeadItem("creceiptcustomerid").setValue(
					getHeadItem("ccustomerid").getValue());
		}
		// ��δ���ÿ�Ʊ��λ����ȡ�ͻ�
		if (getHeadItem("creceiptcorpid").getValue() == null
				|| getHeadItem("creceiptcorpid").getValue().length() <= 0) {
			getHeadItem("creceiptcorpid").setValue(
					getHeadItem("ccustomerid").getValue());
		}

		// �ջ���ַ
		UIRefPane vreceiveaddress = (UIRefPane) getHeadItem("vreceiveaddress")
				.getComponent();
		vreceiveaddress.setAutoCheck(false);

		// �ջ���ַ����
		if (getHeadItem("creceiptcustomerid") != null)
			((CustAddrRefModel) vreceiveaddress.getRefModel())
					.setCustId(getHeadItem("creceiptcustomerid").getValue());
		// // ��������ID
		// String formula =
		// "getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,creceiptcustomerid)";
		// String pk_cubasdoc = (String) execHeadFormula(formula);
		//
		// String strvreceiveaddress = BillTools.getColValue2("bd_custaddr",
		// "pk_custaddr", "defaddrflag", "Y", "pk_cubasdoc", pk_cubasdoc);
		// vreceiveaddress.setPK(strvreceiveaddress);

		// ɢ��
		UFBoolean bfreecustflag = getBillCardTools().getHeadUFBooleanValue(
				"bfreecustflag");
		if (bfreecustflag == null || !bfreecustflag.booleanValue()) {
			getHeadItem("cfreecustid").setEnabled(false);
		} else {
			getHeadItem("cfreecustid").setEnabled(true);
		}
		// ���ɢ����Ϣ
		getHeadItem("cfreecustid").setValue(null);
		// ����Ĭ��ֵ
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

		// ���ű任
		if (cdeptid != null
				&& (cdeptid_old == null || !cdeptid_old.equals(cdeptid))) {
			// ��Ա���պͲ��Ų���ƥ��
			afterDeptEdit(null);
		}

		// �����ۿ�
		if (ndiscountrate != null
				&& (ndiscountrate_old == null || ndiscountrate_old
						.compareTo(ndiscountrate) != 0)) {
			afterDiscountrateEdit(null);
		}

		// �ջ���λ
		String creceiptcorpid = getBillCardTools().getHeadStringValue(
				"creceiptcorpid");
		if ((creceiptcorpid != null && (creceiptcorpid_old == null || !creceiptcorpid_old
				.equals(creceiptcorpid)))
				|| (creceiptcorpid == null && creceiptcorpid_old != null)) {
			afterCreceiptcorpEdit(null);
		}

		afterCurrencyEdit(e);

		if (!getSouceBillType().equals("4H") && !getSouceBillType().equals("42")) {
			// ���ñ����ȱʡ�ķ�����˾�������֯
			getBillCardTools().setSendCalBodyAndWare(0, getRowCount());

			// ���÷�����˾�����¼� 20061127
			for (int i = 0, iLen = getRowCount(); i < iLen; i++) {
				afterCconsignCorpEdit(i);
			}

			if (ccalbodyid != null && (ccalbodyid_old == null || !ccalbodyid_old.equals(ccalbodyid))) {
				// �����֯
				afterCcalbodyidEdit(null);
			}
			String cwarehouseid = getBillCardTools().getHeadStringValue("cwarehouseid");
			if ((cwarehouseid != null && (cwarehouseid_old == null || !cwarehouseid_old.equals(cwarehouseid)))
					|| (cwarehouseid == null && cwarehouseid_old != null)) {
				// �ֿ�
				afterCwarehouseidEdit(null);
			}
		}
		
		

		// �ı�ͻ���ȥ����ͬ�Ĺ�������������Ϣ jindongmei zhongwei
		if (getRowCount() > 0) {
			String creceipttype = (String) getBodyValueAt(0, "creceipttype");

			if (creceipttype != null && "Z4".equals(creceipttype)) {
				//��� ��ͬ�š���ͬID����ͬ���ơ���ͬ����ࡢ��Դ�������͡���Դ����ID����Դ������ID
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
		
        //����ֱ�˲�
		for (int i=0;i<getRowCount();i++)
			setDerictTransSendWareHouse(i);
	}

	/**
	 * ��ʾ���̹�����Ӧ�������Ϣ�� �������ڣ�(2003-12-26 12:52:43)
	 */
	public void showCustManArInfo() {
		try {
			BillItem[] bis = new BillItem[5];
			// ����Ӧ��accawmny
			bis[0] = getTailItem("accawmny");
			// ҵ��Ӧ��busawmny
			bis[1] = getTailItem("busawmny");
			// ����Ӧ��ordawmny
			bis[2] = getTailItem("ordawmny");
			// ���ö��creditmny
			bis[3] = getTailItem("creditmny");
			// ���ñ�֤��creditmoney
			bis[4] = getTailItem("creditmoney");

			boolean isshow = false;
			// �������ý���
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
			//V5.5�н�������Ŀ�����޸� �ܳ�ʤ 2008-07-01
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
			SCMEnv.out("��ʾ���̹�����Ӧ�������Ϣʧ��");
		}
	}

	/**
	 * ���ű༭���¼����� �������ڣ�(2001-6-23 13:42:53)
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

		if ("����".equals(uipanel.strState))/*-=notranslate=-*/
			sRefInitWhere += " and bd_deptdoc.canceled ='N' ";

		cemployeeid.getRefModel().setWherePart(sRefInitWhere);
		
		// ���ŷ����˱仯��Ӧ�����ҵ��Ա�ֶ�
		getHeadItem("cemployeeid").setValue(null);
	}

	/**
	 * �����ۿ��ʱ༭���¼����� ����ͷ�������ۿ۴�������е������ۿ� �������ڣ�(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public void afterDiscountrateEdit(BillEditEvent e) {
		Object oDiscountrate = null;
		//�Ǳ༭��ͷ�����ۿ����
		if (e == null){
			oDiscountrate = getHeadItem("ndiscountrate").getValue();
			for (int i = 0; i < getRowCount(); i++) {
				setBodyValueAt(oDiscountrate, i, "ndiscountrate");
				calculateNumber(i, "ndiscountrate");
				setBodyRowState(i);
			}
		}
		//�༭��ͷ�����ۿ����
		else{
			UFDouble newDiscountrate = e.getValue() == null ? new UFDouble(100.) : new UFDouble(
					e.getValue().toString());//�������ۿ�ֵ
			Object oldDiscount = ndiscountrate != null ? ndiscountrate
					: getBillCardTools().getOldsaleordervo().getParentVO()
							.getAttributeValue("ndiscountrate");
			UFDouble oldDiscountrate = oldDiscount == null ? new UFDouble(100.)
					: new UFDouble(oldDiscount.toString());// ԭ�����ۿ�ֵ
			
			//�༭��ͷ�ۿۺ����������˰�ϼ�(����˰���)
//			UFDouble newHeadmny = null;
			UFDouble oldHeadmny = null;
			String sItemKey = null;
			//��˰����
			if (uipanel.SA_02.booleanValue()){
				sItemKey = "noriginalcursummny";
				oldHeadmny = new UFDouble(getHeadItem("nheadsummny").getValueObject().toString());
			}
			//��˰����
			else{
				sItemKey = "noriginalcurmny";
				oldHeadmny = getTotalValue(sItemKey);
			}
			afterDiscountrateEditTemplet(sItemKey,oldHeadmny,
					oldDiscountrate,newDiscountrate);
			
			ndiscountrate = e.getValue();//������һ�εı�ͷ�����ۿ�ֵ
		}
	}

	/**
	 * �༭��ͷ�����ۿۺ�ͳһ���㷽��
	 */
	private void afterDiscountrateEditTemplet(String sItemKey,UFDouble oldHeadmny,
			UFDouble oldDiscountrate,UFDouble newDiscountrate){
		// ��������˰�ϼ� = ԭ������˰�ϼ� / ԭ�����ۿ� * �������ۿ�
		UFDouble newHeadmny = (oldHeadmny.div(oldDiscountrate.div(100.))).multiply(newDiscountrate.div(100.));
		
		//���м�˰�ϼƱ���
		UFDouble[] summyRate = getRowMnyRate(sItemKey,oldHeadmny);
		
		//������ԭ�м�˰�ϼƱ��ʼ�������¼�˰�ϼ�
		for (int row = 0,len = this.getRowCount(); row < len; row++ ){
			getBillCardTools().setBodyValue(
					newHeadmny.multiply(summyRate[row]), row,
					sItemKey);
			setBodyValueAt(newDiscountrate, row, "ndiscountrate");
			calculateNumber(row, sItemKey);
		}
		
		/////////////////�����һ��β��//////////////////////////////		
		// �ҵ����һ�У�����Ʒ�С���ȱ���У�
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

		/** ���û���ҵ�����Ҫ����о�ֱ���˳�����ͷ�ͱ���ļ�˰�ϼ��ڱ���ʱ����* */
		if (iLastrow < 0)
			return;
		
		// �õ��µĸ��м�����˰�ϼ��ۼ�ֵ
		UFDouble udLast = getTotalValue(sItemKey);
		//β��ֵ
		UFDouble endRowSummny_w = newHeadmny.sub(udLast);
		if (udLast == null || udLast.doubleValue() != newHeadmny.doubleValue()) {
			UFDouble udNow = (UFDouble) getBodyValueAt(iLastrow,sItemKey);
			udNow = (udNow == null ? new UFDouble(0) : udNow).add(endRowSummny_w);
			setBodyValueAt(udNow, iLastrow, sItemKey);
			calculateNumber(iLastrow, sItemKey);
		}
        /////////////////�����һ��β��//////////////////////////////
	}
	
	
	/**
	 * @return ���м�˰�ϼƻ���˰���ռ��ֵ�ı���
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
	 * �����ۿ��ʱ༭���¼�����
	 * 
	 * ������̷���afterDiscountrateEdit(BillEditEvent e)
	 * 
	 * ��ֹ��¼���еļ۸�仯
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
	 * �ջ���λ�༭���¼����� �������ڣ�(2001-6-23 13:42:53)
	 */
	public void afterCreceiptcorpEdit(BillEditEvent e) {
		// �ջ���ַ
		UIRefPane vreceiveaddress = (UIRefPane) getHeadItem("vreceiveaddress")
				.getComponent();
		vreceiveaddress.setAutoCheck(false);
		// vreceiveaddress.setReturnCode(false);
		// �ջ���ַ����
		String creceiptcustomerid = null;
		if (getHeadItem("creceiptcustomerid") != null)
			creceiptcustomerid = getHeadItem("creceiptcustomerid").getValue();

		((CustAddrRefModel) vreceiveaddress.getRefModel())
				.setCustId(creceiptcustomerid);
		// ��������ID
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
		// �����ջ���ַЯ��
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
	 * ���ֱ༭���¼�����
	 * 
	 * �ֶ��ڱ�ͷ������Ĵ��ֶ����޷���ʾ������
	 * 
	 * ���¼������󣬻���б����н��۸�������
	 * 
	 * �������ڣ�(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public void afterCurrencyEdit(BillEditEvent event) {

		// ���ֲ���
		UIRefPane ccurrencytypeid = (UIRefPane) getHeadItem("ccurrencytypeid").getComponent();
		setHeadItem("nexchangeotobrate", null);

		//��ͷ��������//////////////////////////////////////////////////
		int iDecimalDigits = 2;
		try {
			// ����ȡ����С��λ
			setPanelByCurrency(ccurrencytypeid.getRefPK());

			iDecimalDigits = uipanel.getSOCurrencyRateUtil()
					.getExchangeRateDigits(ccurrencytypeid.getRefPK());
			// ���ñ�ͷ���ʾ���
			getHeadItem("nexchangeotobrate").setDecimalDigits(iDecimalDigits);

			// ��������Һ���
			UFDouble[] ult = getBillCardTools().getExchangeRate(
					ccurrencytypeid.getRefPK(),
					getHeadItem("dbilldate").getValue(),
					ClientEnvironment.getInstance().getCorporation()
							.getPk_corp());
			UFDouble dCurr0 = ult[0];
			setHeadItem("nexchangeotobrate", dCurr0);

			// 2001��11��24�� �����޸�
			// ������ֵ��ڱ�λ�������۱����ʲ����޸ģ�����Ӧ�õ���1
			if (CurrParamQuery.getInstance().isLocalCurrType(getCorp(),
					ccurrencytypeid.getRefPK())) {
				getHeadItem("nexchangeotobrate").setEnabled(false);
			} else
				getHeadItem("nexchangeotobrate").setEnabled(true);			
			
			//�����������//////////////////////////////////////////////////
			for (int i = 0; i < getRowCount(); i++) {
				// �������
				setBodyValueAt(ccurrencytypeid.getRefPK(), i, "ccurrencytypeid");

				setBodyValueAt(ccurrencytypeid.getRefName(), i,
						"ccurrencytypename");

				// �������
				setBodyValueAt(getHeadItem("nexchangeotobrate").getValue(), i,
						"nexchangeotobrate");

				//������ʾ���
				//���ñ�����ʾ���
				nc.ui.so.so001.panel.bom.BillTools
						.setBodyValueAtByDigit(getBillModel(), 
								SmartVODataUtils.getUFDouble(getHeadItem("nexchangeotobrate").getValueObject()),
								i, "nexchangeotobrate", iDecimalDigits);
				
				// ����ձ���۸񡢽����ں���Ĵ������ٽ���ѯ�ۣ��������Ϊѯ�ۣ�
				getBillCardTools().clearRowData(i, SOBillCardTools.pricekeys);
				
				// ���������ۿ�Ĭ��ֵ100%����Ϊ����Ҫ����ѯ�ۣ�
				for (int row = 0 ; row < getRowCount(); row++ )
					getBillCardTools().setBodyValue(100, row, "nitemdiscountrate");
				
				// ����״̬Ϊ�޸�״̬
				setBodyRowState(i);
			}
			// �������ñ�������

		} catch (java.lang.Exception e1) {
			SCMEnv.out("��û���ʧ�ܣ�");
		}
		
		
	}

	/**
	 * ���ݱ������û��ʺͽ��С��λ���� �������ڣ�(2002-6-24 11:15:06)
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

			// ������
			int digit = 4;
			try {
				// v30ȡҵ�񾫶�
				digit = currtypevo.getCurrbusidigit() == null ? 4 : currtypevo
						.getCurrbusidigit().intValue(); // currVO.getCurrdigit().intValue();
				SCMEnv.out(digit);
			} catch (Exception ex2) {
				digit = getBodyItem("noriginalcursummny").getDecimalDigits();
			}
			
			// BOM������
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
			SCMEnv.out("���ݱ�������С��λ��ʧ��!");
			// e.printStackTrace();
		}
	}

	/**
	 * ���巢����˾�仯��nc240,�๫˾���� �������ڣ�(2004-2-9 14:58:33)
	 */
	private void afterCconsignCorpEdit(int row) {

		//ֻ������༭������˾��������ͻ�ʱ��������
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

		// ���������˾�����۹�˾(����½��˾)��ͬ����Ϊ����˾���ۣ�
		// �����ջ������֯���ջ��ֿ⣬�Ƿ�ֱ�˲��ɱ༭
		if (cconsigncorp == null || pk_corp.equals(cconsigncorp)) {
			// �������ֶ�
			getBillCardTools().clearBodyValue(
					new String[] { //"cbodywarehousename", "cbodywarehouseid",
							"cadvisecalbodyid", "cadvisecalbody",
							"creccalbody", "creccalbodyid", "crecwarehouse",
							"crecwareid", "bdericttrans" }, row);
			// ���������֯
			getBillCardTools().setBodyValueByHead("ccalbodyid", row);
			// �����ֿ�
			getBillCardTools().setBodyValueByHead("cwarehouseid", row);

		} 
		//���Ǳ���˾������ģ���趨�����ֶ������������һ��Я���Ƿ�ֱ�ˡ��ջ������֯���ջ��ֿ�
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
	 * ���巢����˾�ı�ʱ�����ƽ���ı仯��
	 * 
	 * �������ڣ�(2004-2-9 15:10:14)
	 * 
	 */
	public void ctlUIOnCconsignCorpChg(int row) {
		String pk_corp = getBillCardTools().getHeadStringValue("pk_corp");
		if (pk_corp == null)
			return;
		String cconsigncorp = getBillCardTools().getBodyStringValue(row,
				"cconsigncorpid");

		// ���������˾�����۹�˾(����½��˾)��ͬ����Ϊ����˾���ۣ�
		// �����ջ������֯���ջ��ֿ⣬�Ƿ�ֱ�˲��ɱ༭
		if (cconsigncorp == null || pk_corp.equals(cconsigncorp)) {

            // �������ֶ�
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
			
			//����Ĭ�Ϸ���ֱ�˲�
			setDerictTransSendWareHouse(row);

		// �繫˾����
		} else {
			getBillCardTools().setBodyCellsEdit(
					new String[] { "boosflag", "bsupplyflag" }, row, false);
			getBillCardTools().setBodyCellsEdit(
					new String[] { "creccalbody", "crecwarehouse",
							"bdericttrans" }, row, true);
			
			//ģ���趨�Ƿ�ֱ�ˡ��ջ������֯���ջ��ֿ��ֶ������������һ��Я��
			setPreRowValueForTransBodyWare(row);
			
			//�繫˾����ֱ�˵���
			getBillCardTools().setBodyValue("Y", row, "bdericttrans");

			//�����ջ�ֱ�˲�
			setDerictTransReceWareHouse(row);
		}
	}
	
	/**
	 * ģ���趨�Ƿ�ֱ�ˡ��ջ������֯���ջ��ֿ��ֶ������������һ��Я��
	 * @param row  ��ǰ��
	 */
	private void setPreRowValueForTransBodyWare(int row){
		if (row>0){
			Object preRowValue = null;
            //ֱ�˵����Ƿ�����
			if (getBodyItem("bdericttrans").isLock()){
				//��һ�е�ֵ
				preRowValue = getBillCardTools().getBodyValue(row-1, "bdericttrans");
				//���ñ��е�ֵ
				getBillCardTools().setBodyValue(preRowValue, row, "bdericttrans");
			}
			//�ջ������֯�Ƿ�����(ģ���ϵ�����յ��ֶ�)
			if (getBodyItem("creccalbody").isLock()){
				//��һ�е�ֵ
				preRowValue = getBillCardTools().getBodyValue(row-1, "creccalbody");
				//���ñ��е�ֵ
				getBillCardTools().setBodyValue(preRowValue, row, "creccalbody");					
			}
			//�ջ��ֿ��Ƿ�����(ģ���ϵ�����յ��ֶ�)
			if (getBodyItem("crecwarehouse").isLock()){
				//��һ�е�ֵ
				preRowValue = getBillCardTools().getBodyValue(row-1, "crecwarehouse");
				//���ñ��е�ֵ
				getBillCardTools().setBodyValue(preRowValue, row, "crecwarehouse");					
			}
		}
	}

	/**
	 * ҵ��Ա�༭���¼�����
	 * 
	 * �������ڣ�(2001-11-13 10:57:39)
	 * 
	 * @param e
	 *            nc.ui.pub.bill.BillEditEvent
	 * 
	 */
	public void afterEmployeeEdit(BillEditEvent e) {

		// ����ҵ��Ա�����ò��Ų���
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

					// //У��ֵ
					// //setPK()����ȫ���ѯ��û��ҵ�����ԣ����ԣ����÷�����������
					// //���ǣ�ʹ�ú����ſ�������������ݳ��ִ���
					refpane_dept.getRefModel().setMatchPkWithWherePart(true);
					refpane_dept.setPK(new_pkdeptid);
					refpane_dept.getRefModel().setMatchPkWithWherePart(false);

					String new_cdeptname = refpane_dept.getUITextField()
							.getText();
					// //�ָ�ԭֵ
					if (new_cdeptname == null
							|| new_cdeptname.trim().length() <= 0) {
						refpane_dept.setPK(old_pkdeptid);
					}
				}// end update new pk
			}
		}// end if refpane not null

	}

	/**
	 * ������༭���¼����� �������ڣ�(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	private void afterFreeItemEdit(BillEditEvent e) {
		try {
			FreeVO voFree = getFreeItemRefPane().getFreeVO();
			// ���������������
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
	 * ���� FreeItemRefPane1 ����ֵ��
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
	 * ������༭���¼����� �������ڣ�(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public void afterFreeItemEditBom(BillEditEvent e) {
		try {
			FreeVO voFree = getFreeItemRefPane().getFreeVO();
			// ���������������
			for (int i = 0; i < 5; i++) {
				String fieldname = "vfree" + (i + 1);
				Object o = voFree.getAttributeValue(fieldname);
				uipanel.getBillTreeCardPanel().setBodyValueAt(o, e.getRow(),
						fieldname);
			}
		} catch (Exception e2) {
			SCMEnv.out("������������������!");
			// e2.printStackTrace();
		}
	}

	/**
	 * ��Ŀ�༭���¼����� �������ڣ�(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	private void afterProjectEdit(BillEditEvent e) {
		// �������
		String[] clearCol = { "cprojectphaseid", "cprojectphasename" };
		clearRowData(e.getRow(), clearCol);
	}

	/**
	 * ����˰�ʱ༭���¼����� �������ڣ�(2001-6-23 13:42:53)
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
	 * ������λ�༭���¼����� �������ڣ�(2001-6-23 13:42:53)
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
	 * ������λ�༭���¼�����
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
					// ��װ��λ����
					formulas[0] = "cpackunitname->getColValue(bd_measdoc,measname,pk_measdoc,cpackunitid)";
					// ������
					execBodyFormulas(eRow, formulas);
					setBodyValueAt(new UFDouble(1), eRow, "scalefactor");
					setBodyValueAt(new UFBoolean(true), eRow, "fixedflag");
				} else {
					String[] formulas = new String[3];
					// ��װ��λ����
					formulas[0] = "cpackunitname->getColValue(bd_measdoc,measname,pk_measdoc,cpackunitid)";
					// ������
					formulas[1] = "scalefactor->getColValue2(bd_convert,mainmeasrate,pk_invbasdoc,cinvbasdocid,pk_measdoc,cpackunitid)";
					// �Ƿ�̶�������
					formulas[2] = "fixedflag->getColValue2(bd_convert,fixedflag,pk_invbasdoc,cinvbasdocid,pk_measdoc,cpackunitid)";
					execBodyFormulas(eRow, formulas);
				}
			} else {
				// ����λΪ��
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
			// ���۵�λ
			String cquoteunitid = (String) getBodyValueAt(eRow, "cquoteunitid");
			if (cunitid != null && cquoteunitid != null) {
				if (cunitid.equals(cquoteunitid)) {
					String[] formulas = new String[1];
					// ���۵�λ����
					formulas[0] = "cquoteunit->getColValue(bd_measdoc,measname,pk_measdoc,cquoteunitid)";
					// ������
					execBodyFormulas(eRow, formulas);
					setBodyValueAt(new UFDouble(1), eRow, "nqtscalefactor");
					setBodyValueAt(new UFBoolean(true), eRow, "bqtfixedflag");
				} else {
					// ������������Ʒ����Ķ��չ�ϵ
					// �����Ʒ�к��������֮ǰ�Ķ��չ�ϵ
					if (isBuyLargessLine(eRow)) {
						setBodyValueAt(new UFBoolean(false), eRow,
								"blargessflag");
						setBodyValueAt(null, eRow, "clargessrowno");
					}

					String[] formulas = new String[3];
					// ���۵�λ����
					formulas[0] = "cquoteunit->getColValue(bd_measdoc,measname,pk_measdoc,cquoteunitid)";
					// ���ۻ�����
					formulas[1] = "nqtscalefactor->getColValue2(bd_convert,mainmeasrate,pk_invbasdoc,cinvbasdocid,pk_measdoc,cquoteunitid)";
					// �Ƿ�̶�������
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
			// ����Դ�Ĳ��ܴ�
			Object sSource = getBodyValueAt(eRow, "creceipttype");
			if (sSource == null || sSource.toString().trim().length() == 0) {
				UFBoolean blar = new UFBoolean(getBodyValueAt(eRow,
						"blargessflag") == null ? "false" : getBodyValueAt(
						eRow, "blargessflag").toString());
				// ������
				if (blar == null || !blar.booleanValue()) {
					String sPk = (String) getBodyValueAt(eRow, "cinventoryid");

					// ɾ��ԭ����������Ʒ���
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
	 * ?user> ���ܣ� 2) �����Ϊ�̶�������ʱ���������ϵĻ��������Բ��ܽ��б༭�������Ϊ�ǹ̶�������ʱ������
	 * 
	 * ���ϵĻ��������Կ��Խ��б༭�����ݱ仯�Ļ����ʼ������������
	 * 
	 * ������ ���أ� ���⣺ ���ڣ�(2006-10-12 20:42:57) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param row
	 *            int
	 */
	public void setScaleEditableByRow(int row) {
		
		/**����λ*/
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
		// �ǹ̶�������
		if (!fixedflag.booleanValue()) {
			setCellEditable(row, "scalefactor", true);
			BillItem bitem = getBodyItem("scalefactor");
			if (bitem != null)
				getBillCardTools().resumeBillBodyItemEdit(bitem);
		} else
			setCellEditable(row, "scalefactor", false);

		
		/**���ۼ�����λ*/		
		Object qtotemp = getBodyValueAt(row, "bqtfixedflag");
		UFBoolean qtfixedflag = new UFBoolean(qtotemp == null ? "N" : qtotemp
				.toString());
		// �ǹ̶�������
		if (!qtfixedflag.booleanValue()) {
			setCellEditable(row, "nqtscalefactor", true);
			BillItem bitem = getBodyItem("nqtscalefactor");
			if (bitem != null)
				getBillCardTools().resumeBillBodyItemEdit(bitem);
		} else
			setCellEditable(row, "nqtscalefactor", false);
		
		
	}

	/**
	 * �ջ���ַ�༭���¼����� �������ڣ�(2001-6-23 13:42:53)
	 */
	private void afterVreceiveaddressEdit(BillEditEvent event) {
		for (int i = 0; i < getBillModel().getRowCount(); i++) {
			getBillCardTools().setBodyValueByHead("vreceiveaddress", i);
			if (getBillModel().getRowState(i) == BillModel.NORMAL)
				getBillModel().setRowState(i, BillModel.MODIFICATION);
		}

	}

	/**
	 * �۱����ʡ� �������ڣ�(2001-6-23 13:42:53)
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
	 * �����֯�༭���¼����� �������ڣ�(2001-6-23 13:42:53)
	 */
	public void afterCcalbodyidEdit(BillEditEvent e) {
		if (e == null || e.getPos() == BillItem.HEAD) {
			UIRefPane refCalbody = (UIRefPane) getHeadItem("ccalbodyid")
					.getComponent();

			String sRefInitWhere = getBillCardTools().getHeadRefInitWhere(
					"cwarehouseid");

			if (sRefInitWhere == null || sRefInitWhere.trim().length() <= 0)
				sRefInitWhere = " 1=1 ";

			if ("����".equals(uipanel.strState))/*-=notranslate=-*/
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

				// ���ñ�ͷ�ֿ�Լ��
				UIRefPane wareRef = (UIRefPane) getHeadItem("cwarehouseid")
						.getComponent();
				if (wareRef == null)
					return;

				if ("����".equals(uipanel.strState))/*-=notranslate=-*/
					wareRef.getRefModel().setSealedDataShow(false);
				else
					wareRef.getRefModel().setSealedDataShow(true);

				wareRef.getRefModel().setWherePart(
						sRefInitWhere + " and pk_calbody = '"
								+ refCalbody.getRefPK() + "' ");
				
		        //����ֱ�˲�
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

				// ���ñ�ͷ�ֿ�Լ��
				UIRefPane wareRef = (UIRefPane) getHeadItem("cwarehouseid")
						.getComponent();
				if (wareRef == null)
					return;

				if ("����".equals(uipanel.strState))/*-=notranslate=-*/
					wareRef.getRefModel().setSealedDataShow(false);
				else
					wareRef.getRefModel().setSealedDataShow(true);

				wareRef.getRefModel().setWherePart(sRefInitWhere);

			}
		} else if (e != null && e.getPos() == BillItem.BODY) {
			setBodyValueAt(null, e.getRow(), "cbodywarehouseid");
			setBodyValueAt(null, e.getRow(), "cbodywarehousename");
			setBodyValueAt(null, e.getRow(), "cbatchid");
			
			//����ֱ�˲�
			setDerictTransSendWareHouse(e.getRow());
			
		}
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2002-10-21 13:16:29)
	 * 
	 * @param ev
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	private void afterBodyAddressEdit(BillEditEvent ev) {
		// �ջ���ַ����
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
	 * �ջ���λ�༭���¼����� �������ڣ�(2001-6-23 13:42:53)
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
	 * ������༭���¼����� �������ڣ�(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public void afterBodyDateEdit(int row, boolean isSendDate) {
		UFDate dateResult = getBillCardTools().getTransDate(row, isSendDate);
		//��������
		UFDate dconsigndate = getBodyValueAt(row, "dconsigndate") == null ? null
				: new UFDate(getBodyValueAt(row, "dconsigndate").toString()
						.trim());
		// ��������
		UFDate ddeliverdate = getBodyValueAt(row, "ddeliverdate") == null ? null
				: new UFDate(getBodyValueAt(row, "ddeliverdate").toString()
						.trim());
		
		//��������Ϊ�գ��򵽻�����Ϊ��
		if (dconsigndate==null){
			setBodyValueAt(null, row, "ddeliverdate");
			return;
		}
		
		//�����������Ϊ�ջ��ߵ�������С�ڷ������ڣ��Զ���������������Ϊ��������
		//�������ڸı�
		if (isSendDate) {
			if (dateResult != null && dateResult.toString().length() != 0
					&& dconsigndate != null && dateResult.after(dconsigndate) )
				setBodyValueAt(dateResult.toString(), row, "ddeliverdate");
			else if ( (dateResult==null && dconsigndate != null && ddeliverdate != null && dconsigndate.after(ddeliverdate))
					|| ddeliverdate==null)
				setBodyValueAt(dconsigndate.toString(), row, "ddeliverdate");
		}
		//�������ڸı�
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
	 * ����״̬�༭���¼����� �������ڣ�(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	private void afterBatchEdit(BillEditEvent e) {
		Object fbatchstatus = getBodyItem("fbatchstatus").converType(
				getBodyValueAt(e.getRow(), "fbatchstatus"));

		// ����
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
	 * ����״̬�༭���¼����� �������ڣ�(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	private void afterBatchIDEdit(BillEditEvent e) {
		UFDouble dNumber = getBodyValueAt(e.getRow(), "nnumber") == null ? new UFDouble(
				0)
				: (UFDouble) (getBodyValueAt(e.getRow(), "nnumber"));
		if (dNumber.compareTo(new UFDouble(0)) < 0)
			return;
		
		/** �������κŶ�ѡ���� 2008-04-02 �й��ֿ�һ��Ҫ�� **/
		/////1.��ȡ���κ�//////////////////////////////////
		nc.ui.ic.pub.lot.LotNumbRefPane lotRef = (nc.ui.ic.pub.lot.LotNumbRefPane) getBodyItem(
				"cbatchid").getComponent();
		nc.vo.ic.pub.lot.LotNumbRefVO[] voLot = null;
		try {
			voLot = lotRef.getLotNumbRefVOs();
		} catch (Exception ex) {
			nc.vo.scm.pub.SCMEnv.error(ex);
		}
		
		/////2.�ֹ�����Ļ�,���ݺϷ����˳�
		if (!getLotNumbRefPane().isClicked()) {
			lotRef.checkData();
			return;
		}
		
	    /////3.�������κ����鸴�Ʊ�����//////////////////////
		int currow = e.getRow();//��ǰ��
		if (voLot != null && voLot.length > 1) {
			//�����õ�һ�У���������Ϊ�������
			//setBodyValueAt(voLot[0].getNinnum(), currow, "nnumber");
			//afterNumberEditLogic(currow, "nnumber", false);
			for (int j = 1; j < voLot.length; j++) {
				uipanel.onCopyLine();
				uipanel.onPasteLine();
				currow++;
				setBodyValueAt(voLot[j].getVbatchcode(), currow, "cbatchid");
				//��������Ϊ�������
				//setBodyValueAt(voLot[j].getNinnum(), currow, "nnumber");
				//afterNumberEditLogic(currow, "nnumber", false);
			}
		}
		/*else{
			//ֻѡһ������ʱ������Ҳ����Ϊ�������
			setBodyValueAt(voLot[0].getNinnum(), currow, "nnumber");
			afterNumberEditLogic(currow, "nnumber", false);
		}*/
		/** �������κŶ�ѡ���� 2008-04-02 �й��ֿ�һ��Ҫ�� **/
	}

//	/**
//	 * �Ƿ�ֱ�˱༭���¼����� �������ڣ�(2001-6-23 13:42:53)
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
	 * �ֿ�༭���¼����� �������ڣ�(2001-6-23 13:42:53)
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
			/** ��������κţ��˴����������壬�ɳ��ⵥ���� V502 yangbo zhongwei* */
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
	 * ���˷�ʽ�༭���¼����� �������ڣ�(2001-6-23 13:42:53)
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
	 * ���ֱ༭���¼����� �������ڣ�(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public void afterCurrencyChange() {
		afterCurrencyEdit(null);
	}

	/**
	 * �ı��к�
	 * 
	 * @param e
	 */
	private void afterRownoEdit(BillEditEvent e) {
		// ��֤�к�
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
	 * �ջ���λ�༭���¼����� �������ڣ�(2001-6-23 13:42:53)
	 */
	public void afterCreceiptcorpEdit() {

		// �ջ���ַ
		UIRefPane vreceiveaddress = (UIRefPane) getHeadItem("vreceiveaddress")
				.getComponent();
		vreceiveaddress.setAutoCheck(false);

		// �ջ���ַ����
		String creceiptcustomerid = null;
		if (getHeadItem("creceiptcustomerid") != null)
			creceiptcustomerid = getHeadItem("creceiptcustomerid").getValue();

		((CustAddrRefModel) vreceiveaddress.getRefModel())
				.setCustId(creceiptcustomerid);
		// ��������ID
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

		// �����ջ���ַЯ��
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
	 * �����֯�༭���¼����� �������ڣ�(2001-6-23 13:42:53)
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
	 * ��ͷ��˰�ϼƱ༭���¼� ���Զԡ���˰�ϼơ����б༭�� �༭����ݸ������еĺ�˰����*����*��Ʒ�ۿ۵Ļ���ֵ �����µġ���˰�ϼơ�ֵ�õ������ۿۣ�
	 * �����ۿ�=����ͷ�ġ���˰�ϼơ�/�������еĺ�˰����*����*��Ʒ�ۿ۵Ļ���ֵ�� Ȼ����ݹ�ʽ��
	 * ��˰����=��˰��*�����ۿ�*��Ʒ�ۿۼ���ó���˰���ۣ� ���ݼ�˰�ϼ�=����*��˰���ۼ���õ������еļ�˰�ϼƣ� �Ѹ��еļ�˰�ϼ�ֵ�����ۼƼӣ�
	 * ���롰��˰�ϼơ��Ĳ���ƽ�������۶��������һ���ϣ� ���ݼ��㹫ʽ ����˰�ϼ�=����*��˰���ۼ���õ����һ�еĺ�˰���ۣ� ���ݹ�ʽ��
	 * ��˰����=��˰��*�����ۿ�*��Ʒ�ۿۼ���ó����һ�еĵ�Ʒ�ۿۣ�
	 * 
	 * @param e
	 * 
	 * @comment ��Ӱ����Ʒ�� ��Ӱ��ȱ���� V502
	 * 
	 */
	protected void afterHeadsummnyEdit(BillEditEvent e) {
		// /�õ��༭ǰ����ֵ
		UFDouble udOld = getTotalValue("noriginalcursummny");
		// UFDouble udOld = getNumPriceDisSummny();

		UFDouble udNew = e.getValue() == null ? new UFDouble(0) : new UFDouble(
				e.getValue().toString());

		// �����ۿ�=����ͷ�ġ���˰�ϼơ�/�������еĺ�˰����*����*��Ʒ�ۿ۵Ļ���ֵ
		UFDouble udDiscountRate = (udOld.doubleValue() == 0 ? new UFDouble(1)
				: udNew.div(udOld)).multiply(100.);
		getHeadItem("ndiscountrate").setValue(udDiscountRate);

		// ��ͷ�����ۿ����¼�������,ǿ�ƺ�˰����
		Integer iaPrior = new Integer(RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE);
		for (int i = 0; i < getRowCount(); i++) {
			setBodyValueAt(getHeadItem("ndiscountrate").getValueObject().toString(), i, "ndiscountrate");
			calculateforHeadSummny(i, "ndiscountrate", iaPrior);
			setBodyRowState(i);
		}
		
		getBillModel().setNeedCalculate(true);
		getBillModel().setNeedCalculate(false);

		// �õ����µļ�˰�ϼ�
		UFDouble udLast = getTotalValue("noriginalcursummny");

		// �ҵ����һ�У�����Ʒ�С���ȱ���У�
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

		/** ���û���ҵ�����Ҫ����о�ֱ���˳�����ͷ�ͱ���ļ�˰�ϼ��ڱ���ʱ����* */
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
	 * ��ͷ�������ڱ༭���¼�
	 * 
	 * @param e
	 */
	private void afterHeadBillDateEdit(BillEditEvent e) {
		// ���巢���͵��������޸�Ϊ��������
		String dbilldate = (getHeadItem("dbilldate").getValueObject() == null || getHeadItem(
				"dbilldate").getValueObject().toString().trim().length() == 0) ? null
				: getHeadItem("dbilldate").getValueObject().toString();
		if (dbilldate == null)
			return;

		/** ֻ��Կ�ֵ�����������߼�У��* */
		Object dconsigndate, ddeliverdate;
		for (int i = 0, len = getBillModel().getRowCount(); i < len; i++) {
			// ��������
			dconsigndate = getBodyValueAt(i, "dconsigndate");
			if (dconsigndate == null) {
				setBodyValueAt(dbilldate, i, "dconsigndate");
			}

			// ��������
			ddeliverdate = getBodyValueAt(i, "ddeliverdate");
			if ((ddeliverdate == null)
					&& (dbilldate.compareTo(getBodyValueAt(i, "dconsigndate")
							.toString()) >= 0)) {
				setBodyValueAt(dbilldate, i, "ddeliverdate");
			}

		}// end for
	}

	// /**
	// * �������еĺ�˰����*����*��Ʒ�ۿ۵Ļ���ֵ
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
	 * �õ�����ֵ
	 * 
	 * @param sItemKey
	 * @return
	 */
	public UFDouble getTotalValue(String sItemKey) {
		int iCol = getBillModel().getBodyColByKey(sItemKey);

		// �õ����µļ�˰�ϼ�
		UFDouble udLast = UFDouble.ZERO_DBL;
		UFDouble tmp;
		Object cinventoryid,blargessflag,boosflag;
		for (int i = 0, len = getBillModel().getRowCount(); i < len; i++) {

			cinventoryid = getBodyValueAt(i, "cinventoryid");
			// ȥ���޴��id�е�Ӱ�죨ֻ�Ա�ͷ��˰�ϼ������ã�
			if (cinventoryid == null || cinventoryid.toString().trim().length() == 0)
				continue;
			
			blargessflag = getBodyValueAt(i, "blargessflag");
			// ȥ����Ʒ��Ӱ�죨ֻ�Ա�ͷ��˰�ϼ������ã�
			if (blargessflag != null && (Boolean) blargessflag)
				continue;

			boosflag = getBodyValueAt(i, "boosflag");
			// ȥ��ȱ����Ӱ�죨ֻ�Ա�ͷ��˰�ϼ������ã�
			if (boosflag != null && (Boolean) boosflag)
				continue;
			
			tmp = (UFDouble) getBillModel().getValueAt(i, iCol);
			if (tmp != null)
				udLast = udLast.add((UFDouble) tmp);
		}// end for

		return udLast;

	}

	/**
	 * ����༭���¼����� �������ڣ�(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	private void afterInventoryMutiEdit(BillEditEvent e) {
		
		// �����Ʒ��,�����к��������֮ǰ�Ķ��չ�ϵ
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

		// ɾ��ԭ����������Ʒ���
		int[] inewdelline = setBlargebindLineWhenDelLine(new int[] { e.getRow() },1);
		if (inewdelline != null && inewdelline.length > 0)
			uipanel.onDelLine(inewdelline);
		
		// ɾ��ԭ��������������
		int[] inewdelline_bind = setBlargebindLineWhenDelLine(new int[] { e
				.getRow() },2);
		if (inewdelline_bind != null && inewdelline_bind.length > 0)
			uipanel.onDelLine(inewdelline_bind);

		//1����Ҫ������������ʹ����Ӧ���������֯ͬʱ����,�ɴ�����ͻ��޸Ĵ����� 
		//2��ֻ�����������󣬲������������֯;�ɱ��֡����������ڡ����������޸Ĵ��� 
		//3��ֻ�����������֯������������������������֯�޸Ĵ���
		afterInventoryMutiEdit(e.getRow(), 1);

		// ����������������ʾ
		// ������ȡ�����Ϣ
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
	 * �ж��Ƿ���Ʒ��
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
	 * �ж��Ƿ�������
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
	 * ����༭���¼�����
	 * 
	 * �������ڣ�(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	private void afterInventoryMutiEdit(int irow, int iChg) {

		String[] refPks = ((UIRefPane) getBodyItem("cinventorycode")
				.getComponent()).getRefPKs();

		afterInventoryMutiEdit(irow, refPks, true, true, null, true, iChg);
	}

	/**
	 * ����༭���¼����������������������󣩡�
	 * 
	 * �������ڣ�(2001-6-23 13:42:53)
	 * 
	 * bCopy:true,���Ǹ��������ģ�������ԭ�У������д�row+1
	 * 
	 * ��ʼ bCopy:false,����ѡ�������������ԭ�У������д�row ��ʼ refPks:��ʼ�Ĵ����
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 * 
	 */
	private void afterInventoryMutiEdit(int irow, String[] refPks,
			boolean bMain, boolean bBinds, String sFormulakey,
			boolean bNeedFindPrice, int iChg) {

		/**���������գ��򽫵��۽������ lining zhangcheng v5.3*/
		if (refPks == null){
			//��ձ��еĵ��۽��
			String[] mny = getBillCardTools().getSaleItems_Mny();
			String[] price = getBillCardTools().getSaleItems_Price();
			for (int i = 0; i < price.length; i++) 
				getBillCardTools().setBodyValue(null, irow, price[i]);
            for (int i = 0; i < mny.length; i++) 
            	getBillCardTools().setBodyValue(null, irow, mny[i]);
            
            //�������ñ�ͷ��˰�ϼ�
            if (getHeadItem("nheadsummny") != null)
				getHeadItem("nheadsummny").setValue(getTotalValue("noriginalcursummny"));
			
            return;
		}

		long s = System.currentTimeMillis();
		SCMEnv.out("��ȡ��������Ϣ��ʼ...");
		ArrayList alist = setBodyByinvs(refPks, irow, bMain, bBinds, iChg);
		SCMEnv.out("ѭ�����ñ���[����ʱ" + (System.currentTimeMillis() - s) + "]");
		if (alist == null)
			return;
		
		// ���Թ�����ͬ������Ĭ�Ϻ�ͬ��
		setDefaultCtItem(irow, alist.size());

		if (uipanel.SO_01 != null && uipanel.SO_01.intValue() != 0
				&& getRowCount() - 1 > uipanel.SO_01.intValue()) {
			uipanel.showErrorMessage(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("40060301", "UPP40060301-000171", null,
							new String[] { uipanel.SO_01.intValue() + "" }));
		}

		s = System.currentTimeMillis();
		SCMEnv.out("�������������Ϣ��ʼ...");

		// ִ���û��Զ��幫ʽ
		if (getBillCardTools().getEditInvfomulas_add() != null
				&& getBillCardTools().getEditInvfomulas_add().length > 0) {
			getBillCardTools().execBodyFormulas(
					getBillCardTools().getEditInvfomulas_add(), alist);
		}

		/** ��ǰ���˴� V502* */
		for (int i = 0, len = alist.size(); i < len; i++) {
			afterCconsignCorpEdit((Integer) alist.get(i));
		}
		/** ��ǰ���˴� V502* */

		/** V502 ����Զ�̵��ô�����ȥ���˴��ж��������ϲ���ʽ�Ĵ��� zhongwei* */
		// if (getBillType().equals(SaleBillType.SaleOrder)
		// || getBillType().equals(SaleBillType.SaleInitOrder)) {
		// ����״̬��ʽ
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
		/** V502 ����Զ�̵��ô�����ȥ���˴��ж��������ϲ���ʽ�Ĵ��� zhongwei* */

		if (getBillType().equals(SaleBillType.SaleOrder)) {
			// ����״̬��ʽ

			// ���ο���:�ĵ�������������Ŀ��wholemanaflag ����
			Object temp = getBodyValueAt(irow, "wholemanaflag");
			boolean wholemanaflag = (temp == null ? false : new UFBoolean(temp
					.toString()).booleanValue());
			setCellEditable(irow, "fbatchstatus", wholemanaflag);
			setCellEditable(irow, "cbatchid", wholemanaflag);
			// �������۰�ť����isconfigable
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

		// ˰����ȡ����������ϵ�˰Ŀ��Ӧ��˰�ʣ����Ϊ�գ���ȡ�������������˰Ŀ��Ӧ��˰�ʡ�
		loadTaxtrate(alist);

		// ���
		getBillCardTools().setBodyInventory1(irow, alist.size());

		// ���ñ������������
		getBillCardTools().setBodyCchantypeid(irow, alist.size());

		SCMEnv.out("�������������Ϣ[����ʱ" + (System.currentTimeMillis() - s) + "]");
		s = System.currentTimeMillis();
		SCMEnv.out("���õ�λת����Ϣ��ʼ...");

		afterInventorysEdit(irow, irow + alist.size(), "ntaxrate", bNeedFindPrice);

		SCMEnv.out("���õ�λת����Ϣ[����ʱ" + (System.currentTimeMillis() - s) + "]");

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
	 * �༭�����ȡ����뷢�������֯��ϵ�������������Ϣ
	 * @param htinvcal
	 * @param startrow
	 * @param stoprow
	 */
	protected void setCalByConCalset(HashMap htinvcal, int startrow, int stoprow) {
		
		//ֻ������༭������˾��������ͻ�ʱ��������
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

				// cconsigncorpid ������˾
				setBodyValueAt(invbvo.getCreceiptcorpid(), i, "cconsigncorpid");
				
				// ���巢����˾�����仯�ı༭���¼�
				afterCconsignCorpEdit(i);
				
				// ���������֯
				setBodyValueAt(invbvo.getCcalbodyid(), i, "cadvisecalbodyid");
				// �ֿ�
				setBodyValueAt(invbvo.getCwarehouseid(), i, "cbodywarehouseid");
		   }
		   //ȡ��ͷĬ�Ϸ��������֯
		   else
			   // ���������֯
			   setBodyValueAt(getBillCardTools().getHeadValue("ccalbodyid"), i, "cadvisecalbodyid");
		}
		getBillModel().execLoadFormulasByKey("cconsigncorpid");
		getBillModel().execLoadFormulasByKey("cadvisecalbodyid");
		getBillModel().execLoadFormulasByKey("cbodywarehouseid");
	}

	/**
	 * ���ñ��尴���
	 * 
	 * @param refPks 
	 * @param irow
	 * @param bMain
	 *            �Ƿ������������true:�������������false�����ڸ��ƣ����ڸ�����
	 * @param bBinds
	 *            �Ƿ������������true:����������������ƴ����false�����ڸ�����
	 * @return
	 */
	private ArrayList setBodyByinvs(String[] refPks, int irow, boolean bMain,
			boolean bBinds, int iChg) {

		try {
			long s = System.currentTimeMillis();
			long s0 = s;
			SCMEnv.out("��ȡ����������Ϣ��ʼ...");

			ArrayList al = getLargessAndBindingsByInvs(refPks, irow, iChg);
			SCMEnv.out("��ȡ����������Ϣ��ʱ[" + (System.currentTimeMillis() - s)
					/ 1000.0 + "s]");
			s = System.currentTimeMillis();

			// �����+ԭ��
			String[] sBinds = (String[]) al.get(0);
			// �����
			Hashtable htbinds = (Hashtable) al.get(1);
			// ������
			BuylargessVO[] bvos = (BuylargessVO[]) al.get(2);

			// �ϲ�Ϊһ�������һ���ѯ�����Ϣ
			Hashtable htLargess = new Hashtable();
			Vector vt = new Vector();
			for (int i = 0; i < sBinds.length; i++) {
				vt.add(sBinds[i]);
			}
			// ԭ���
			for (int i = 0; i < refPks.length; i++) {
				vt.add(refPks[i]);
			}
			// BuylargessVO bvotmp = null;
			String larkey = null;
			if (bvos != null && bvos.length > 0) {
				BuylargessBVO[] bodys = null;
				ArrayList allargess = null;
				for (int i = 0; i < bvos.length; i++) {
					// �ͻ�Ҫ�ּ���

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

			// ������ȡ�����Ϣ
			InvVO[] invvos = new InvVO[snewPks.length];
			for (int i = 0; i < invvos.length; i++) {
				invvos[i] = new InvVO();
				invvos[i].setCinventoryid(snewPks[i]);
			}

			SCMEnv.out("������������������ʱ[" + (System.currentTimeMillis() - s)
					/ 1000.0 + "s]");
			s = System.currentTimeMillis();

			InvoInfoBYFormula invvosget = new InvoInfoBYFormula();
			invvos = invvosget.getQuryInvVOs(invvos, false, true, 1);
			// �����ؽ������hash
			Hashtable htresult = new Hashtable();
			for (int k = 0; k < invvos.length; k++) {
				htresult.put(invvos[k].getCinventoryid(), invvos[k]);
			}
			SCMEnv.out("��ѯ�����Ϣ��ʱ[" + (System.currentTimeMillis() - s) / 1000.0
					+ "s]");
			s = System.currentTimeMillis();

			int count = 0;
			// ������ͨ���
			if (bMain) {
				count += setBodyByOrginvs(refPks, htresult, irow);
				count--;
				SCMEnv.out("�����������ʱ[" + (System.currentTimeMillis() - s)
						/ 1000.0 + "s]");
				s = System.currentTimeMillis();

			}

			// ���������
			int startrow = irow;
			int stoprow = irow + count;
			if (bBinds) {
				// ��ʾ
				if (htbinds.size() > 0) {
					if (uipanel.showOkCancelMessage(nc.ui.ml.NCLangRes
							.getInstance().getStrByID("40060301",
									"UPP40060301-000515")) == MessageDialog.ID_OK) {
						s0 -= (System.currentTimeMillis() - s);
						s = System.currentTimeMillis();
						for (int i = stoprow; i >= startrow; i--) {
							count += setBodyByBindinvs(i, htresult, htbinds);
						}
						SCMEnv.out("�����������ʱ["
								+ (System.currentTimeMillis() - s) / 1000.0
								+ "s]");
						s = System.currentTimeMillis();
					}
				}
			}
			stoprow = irow + count;
			// ����������
			for (int i = stoprow; i >= startrow; i--) {
				count += setBodyByLargessinvs(i, htresult, htLargess);
				// ���÷�
			}
			SCMEnv.out("������������ʱ[" + (System.currentTimeMillis() - s) / 1000.0
					+ "s]");
			s = System.currentTimeMillis();

			// ���ô��������֯���չ�ϵ 20060705
			if (al.size() >= 4) {
				m_hConCal = (HashMap) al.get(3);
			}
			else if (m_hConCal != null) {
				m_hConCal.clear();
			}

			ArrayList alist = new ArrayList();
			stoprow = irow + count;
			// ���÷��ص�����
			for (int i = startrow; i <= stoprow; i++) {
				// ������������������������������ԭ��
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
					// �����¿���
					uipanel.addLine();

					binvedit=true;
					
				}
			}
			SCMEnv.out("��ȡ����������������ʱ[" + (System.currentTimeMillis() - s0)
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
	 *            ��ѡ���Pk
	 * @param icurrow
	 *            ��ǰ��
	 * @param iChgInv
	 *            1����Ҫ������������ʹ����Ӧ���������֯ͬʱ����,�ɴ�����ͻ��޸Ĵ�����
	 *            2��ֻ�����������󣬲������������֯; �ɱ��֡����������ڡ����������޸Ĵ��� 
	 *            3��ֻ�����������֯������������������������֯�޸Ĵ���
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
		// ������ɸĴ�������������Ӳ���������֯
		// 5.csalecorpid
		if (getHeadItem("csalecorpid").getValue() != null) {
			alParam.add(getHeadItem("csalecorpid").getValue());
		} else
			alParam.add("");
		
		/**ͨ�����*/
		ArrayList invclcode = getInvclCode(refPks);
		alParam.add(invclcode);
		/**ͨ�����*/

		/** ֻҪ�д���ͻ��ѯ���������󡢿����֯���չ�ϵ���ض�����һ��Զ�̵���* */
		ArrayList al = buyLargess.getLargessAndBindingsByInvs(refPks, alParam);
		if (al.get(2) instanceof BuylargessVO[]) {
			BuylargessVO[] bvos = (BuylargessVO[]) al.get(2);
			if (bvos != null && bvos.length > 0) {
				for (int i = 0; i < bvos.length; i++) {
					//�ܳ�ʤ  2008-11-04  v5.5 ����֧��ʱЧ
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
					//�ܳ�ʤ
					m_htLargess.put(((BuylargessHVO) bvos[i].getParentVO())
							.getPk_invmandoc(), bvos[i]);
					
				}
			}
		}

		return al;

	}
	
	/**
	 * ͨ�����--����ָ���������pk����ô�������������
	 * @param refPks
	 * @return ArrayList [0]--��������list��[1]--<���id,��������>HashMap
	 */
	private ArrayList getInvclCode(String[] refPks){
		HashMap invid_invclcode = new HashMap();//<invid,invclcode>
		ArrayList linvclcode = new ArrayList();//��Ŵ����
		try {
			for (String refPk : refPks){
				//�����������
				Object[] invbasid = (Object[])nc.ui.scm.pub.CacheTool
				.getColumnValue("bd_invmandoc", "pk_invmandoc","pk_invbasdoc",new String[]{refPk});
				
				//�������pk
				Object[] invclpk =  (Object[])nc.ui.scm.pub.CacheTool
				.getColumnValue("bd_invbasdoc", "pk_invbasdoc","pk_invcl",new String[]{invbasid[0].toString()});
				
				//����������
				Object[] invclcode = (Object[])nc.ui.scm.pub.CacheTool
				.getColumnValue("bd_invcl", "pk_invcl","invclasscode",new String[]{invclpk[0].toString()});
				
				invid_invclcode.put(refPk,invclcode[0].toString() );
				linvclcode.add(invclcode[0].toString());
			}
		} catch (Exception e) {
			SCMEnv.out(e);
			uipanel.showErrorMessage("�����������ȡ��������������ʧ�ܣ�");
		}
		ArrayList sinvclcode = new ArrayList();//���ؽ��
		sinvclcode.add(linvclcode);//��������list
		sinvclcode.add(invid_invclcode);//<���id,��������>
		return sinvclcode;
	}

	/**
	 * ����ԭ�����������������������˳�����ϵ��¡���¼countֵ
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

		// �����¿���
		if (irow == getRowCount() - 1) {
			uipanel.addNullLine(irow, count - 1);
		} else {
			uipanel.insertNullLine(irow, count - 1);
		}
		afterInvEditClear(irow);

		// ��¼��Ҫִ�й�ʽ����
		InvVO tmpvo = null;
		setBodyDefaultData(irow, irow + count);
		for (int i = 0; i < count; i++) {

			tmpvo = (InvVO) htresult.get(refPks[i]);
			setBodyValues(tmpvo, irow + i, irow);
		}

		return count;

	}

	/**
	 * v5.5 ���������ۡ���������գ������ϴν��
	 * ����༭���¼�������������� �������ڣ�(2001-6-23 13:42:53)
	 */
	private void afterInvEditClear(int iRowIndex) {
		// �������
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
				"norgqttaxprc", // ���۵�λ��˰����
				"norgqtprc", // ���۵�λ��˰����
				"norgqttaxnetprc", // ���۵�λ��˰����
				"norgqtnetprc", // ���۵�λ��˰����
				*/
				
				"cbatchid", "vfree0", "vfree1", "vfree2", "vfree3", "vfree4",
				"vfree5", "cinvclassid", "cinvclasscode", "cpricepolicyid",
				"cpricepolicy", "cpriceitemid", "cpriceitem",
				"cpriceitemtable", "cpriceitemtablename", "cpricecalproc",
				"cinventoryid1", // ������˾�Ĵ��������
				// "cchantypeid",//��������ID
				// "cchantype",//��������
				"bsafeprice", // �Ƿ����۱�
				"ntaldcnum", // �Ѳ���۱�������
				"nasttaldcnum", // �Ѳ���۱�������
				"ntaldcmny", // �۱����
				"breturnprofit", // �Ƿ���뷵��
				"nretprofnum", // �Ѳ��뷵��������
				"nastretprofnum", // �Ѳ��뷵��������
				"nretprofmny", // �������

				"cpricepolicyid", // �۸�����ID
				"cpricepolicy", // �۸�����
				"cpriceitemid", // �۸���ĿID
				"cpriceitem", // �۸���Ŀ
				"cpriceitemtable", // ��Ŀ��id
				"cpriceitemtablename", // ��Ŀ��
				"cpricecalproc", // �۸�������id
				"cpricecalprocname", // �۸�������

				"cquoteunitid", "cquoteunit", "bindpricetype"

		};

		Object ctinvclassid = null;
		try {
			ctinvclassid = getBodyValueAt(iRowIndex, "ctinvclassid");
		} catch (Exception e) {
			ctinvclassid = null;
		}

		if (ctinvclassid != null && ctinvclassid.toString().length() != 0) {

			// ������ͬ
			if (uipanel.SO_17.booleanValue()) {
				// ִ�к�ͬ�۸�
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
						"cpricecalproc", "cinventoryid1", // ������˾�Ĵ��������
						// "cchantypeid",//��������ID
						// "cchantype",//��������
						"bsafeprice", // �Ƿ����۱�
						"ntaldcnum", // �Ѳ���۱�������
						"nasttaldcnum", // �Ѳ���۱�������
						"ntaldcmny", // �۱����
						"breturnprofit", // �Ƿ���뷵��
						"nretprofnum", // �Ѳ��뷵��������
						"nastretprofnum", // �Ѳ��뷵��������
						"nretprofmny", // �������

						"cquoteunitid", "cquoteunit"

				};
			}

		} else if (getSouceBillType().equals(SaleBillType.SoContract)
				|| getSouceBillType().equals(SaleBillType.SoInitContract)) {

			// �������
			clearCol = new String[] { "scalefactor", "fixedflag",
					"nqtscalefactor", "bqtfixedflag",

					"noriginalcurtaxmny", "noriginalcurmny",
					"noriginalcursummny", "noriginalcurdiscountmny",

					"cbatchid", "vfree0", "vfree1", "vfree2", "vfree3",
					"vfree4", "vfree5", "cinvclassid", "cinvclasscode",
					"cpricepolicyid", "cpricepolicy", "cpriceitemid",
					"cpriceitem", "cpriceitemtable", "cpriceitemtablename",
					"cpricecalproc", "cinventoryid1", // ������˾�Ĵ��������
					// "cchantypeid",//��������ID
					// "cchantype",//��������
					"bsafeprice", // �Ƿ����۱�
					"ntaldcnum", // �Ѳ���۱�������
					"nasttaldcnum", // �Ѳ���۱�������
					"ntaldcmny", // �۱����
					"breturnprofit", // �Ƿ���뷵��
					"nretprofnum", // �Ѳ��뷵��������
					"nastretprofnum", // �Ѳ��뷵��������
					"nretprofmny", // �������

					"cpricepolicyid", // �۸�����ID
					"cpricepolicy", // �۸�����
					"cpriceitemid", // �۸���ĿID
					"cpriceitem", // �۸���Ŀ
					"cpriceitemtable", // ��Ŀ��id
					"cpriceitemtablename", // ��Ŀ��
					"cpricecalproc", // �۸�������id
					"cpricecalprocname", // �۸�������

					"cquoteunitid", "cquoteunit" };

		}
		getBillCardTools().clearRowData(iRowIndex, clearCol);
		if (nc.ui.pub.pf.PfUtilClient.makeFlag) {
			// ��������Ƶ�������е�������
			String[] clearCol2 = { "creceipttype", "csourcebillid",
					"csourcebillbodyid" };
			getBillCardTools().clearRowData(iRowIndex, clearCol2);
		}
	}

	/**
	 * ���������ñ���Ĭ�����ݡ�
	 * 
	 * �������ڣ�(2001-8-27 10:05:59)
	 * 
	 */
	public void setBodyDefaultData(int istartrow, int iendrow) {

		SOToolVO vo = null;
		String crecaddrnode = null;
		String crecaddrnodename = null;

		try {
			// �ջ���ַ
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

		// �����ۿ�
		afterDiscountrateEdit(istartrow, iendrow);

		updateUI();

	}

	private void setBodyValues(InvVO invvo, int iRow, int iCurrow) {

		// ���ô�������Ϣ
		setBodyValueByInvVO(invvo, iRow);
		// ���õ�λ��Ϣ
		setAssistUnit(iRow);

		// ����������
		alInvs.add(iRow, invvo);

		// ����Ǻ�ͬ
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
	 * �Ƿ񸨼����� �������ڣ�(2001-11-30 15:20:14)
	 * 
	 * @param row
	 *            int
	 */
	public boolean setAssistUnit(int row) {
		// �Ƿ񸨼���
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

		// ���۵�λ
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
	 * �õ�������ʱ����Ҫ��յ���
	 * 
	 * wsy 2005-10-10
	 * 
	 */
	public String[] getNeedSetNullSourceItems() {

		return new String[] { "ct_name", "ct_manageid", "ctinvclassid",

				// ��ͬ��
				"ct_code",
				// ��ͬ�����
				"ctinvclass", "creceipttype", "creceipttype",
				"csourcebillbodyid", "csourcebillid",

				"cbomorderid", "cbomordercode"

		};

	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-11-7 10:30:37)
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
				// ������
				setBodyValueAt(new UFDouble(1), row, "scalefactor");
				// �Ƿ�̶�������
				setBodyValueAt("Y", row, "fixedflag");

			} else {
				// ������
				setBodyValueAt(vo.getHsl(), row, "scalefactor");
				// �Ƿ�̶�������
				// getBodyValueAt(0,"fixedflag");
				if (vo.getIsSolidConvRate() != null
						&& vo.getIsSolidConvRate().intValue() == 1)
					setBodyValueAt("Y", row, "fixedflag");
				else
					setBodyValueAt("N", row, "fixedflag");
			}

			// ���ñ��ۼ�����λ����������ͬ
			setBodyValueAt(vo.getPk_measdoc(), row, "cquoteunitid");
			setBodyValueAt(getBodyValueAt(row, "cunitname"), row, "cquoteunit");
			setBodyValueAt(new UFDouble(1), row, "nqtscalefactor");
			setBodyValueAt("Y", row, "bqtfixedflag");

		} else {
			setBodyValueAt(null, row, "cpackunitid");
			setBodyValueAt(null, row, "cpackunitname");

			// ���ñ��ۼ�����λ����������ͬ
			setBodyValueAt(vo.getPk_measdoc(), row, "cquoteunitid");
			setBodyValueAt(getBodyValueAt(row, "cunitname"), row, "cquoteunit");
			setBodyValueAt(new UFDouble(1.0), row, "nqtscalefactor");
			setBodyValueAt("Y", row, "bqtfixedflag");
		}

	}

	/**
	 * ���ô����������Ϣ,��������
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
		// ��ʼ����һ�������
		ArrayList alneeds = new ArrayList();// �������
		ArrayList altmp = new ArrayList();
		altmp.add(sInvPk);
		altmp.add(udnum);
		alneeds.add(altmp);
		alneeds.add(new ArrayList());

		Integer Icount = new Integer(count);
		ArrayList acount = new ArrayList();
		acount.add(Icount);
		long s = System.currentTimeMillis();
		SCMEnv.out("##�����������ʼ");

		// �ݹ��� ,����Ѱ�����������
		UnitBinds(alneeds, htbinds, acount);
		Icount = (Integer) acount.get(0);
		count = Icount.intValue();
		SCMEnv.out("##�����������ʱ[" + (System.currentTimeMillis() - s) / 1000.0
				+ "s]");
		s = System.currentTimeMillis();

		// �����¿���
		if (irow == getRowCount() - 1) {
			uipanel.addNullLine(irow, count);
		} else {
			uipanel.insertNullLine(irow, count);
		}

		ArrayList allcurrow = new ArrayList();
		allcurrow.add(new Integer(irow));
		setBodyDefaultData(irow, irow + count);

		setBinds(alneeds, htresult, allcurrow, irow);
		SCMEnv.out("##�����������ϸ��ʱ[" + (System.currentTimeMillis() - s) / 1000.0
				+ "s]");

		return count;

	}

	/**
	 * @param alneed
	 *            ��Ҫ��ѯ����
	 * @param
	 * @param htBinds
	 *            ����ļ���
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
			// ���������
			// ��¼����
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
	 *            ���д��
	 * @param htLargess
	 *            ��
	 * @param irow
	 *            ��ǰ��
	 * @return
	 */
	private void setBinds(ArrayList alneeds, Hashtable htresult,
			ArrayList airow, int irow) {

		InvVO tmpvo = null;
		// Ӧ�ӵ�ǰ����һ�п�ʼ
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
				// �����
				// ȡȫ���к�
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
	 * ����������
	 * @param imainrow �������
	 * @param invvo
	 * @param irow ������
	 */
	private void setBindingsItems(InvbindleVO invvo, int irow,int imainrow) {
		// ��������
		setBodyValueAt(invvo.getBindlenum(), irow, "nnumber");
		
		// ����Ĭ��˰��
		ArrayList airow = new ArrayList();
		airow.add(irow);
		loadTaxtrate(airow);
		
		 /** v5.3���۽���㷨��� */
		// System.out.println("setBindingsItems----RelationsCal.calculate��#"+irow+"#"+"nnumber"+"#-->���ÿ�ʼ��");
		RelationsCal.calculate(irow, this.oldValue, this, SaleorderBVO.getCalculatePara("nnumber", null, uipanel.SA_02,
				uipanel.SO40), "nnumber", SaleorderBVO.getKeys(), SaleorderBVO.getField(),
				SaleorderBVO.class.getName(), SaleorderHVO.class.getName(), null);
		
		// ���ñ�־
		setBodyValueAt(new UFBoolean(true), irow, "bbindflag");
		// �����������
		setBodyValueAt(getBodyValueAt(imainrow, "crowno"), irow,
				"clargessrowno");
		// ��������
		setBodyValueAt(invvo.getPricetype(), irow, "bindpricetype");
		// ʹ�������
		if (invvo.getPricetype().intValue() == IInvManDocConst.PRICE_TYPE_BINDLE) {
			// ���ü۸�
			setBodyValueAt(invvo.getPrice(), irow, "noriginalcurtaxprice");
			afterNumberEditLogic(irow, "noriginalcurtaxprice", false);
		}
		
		// ����ԭʼ��Ѱ�۵���
		setBodyValueAt(getBillCardTools().getBodyUFDoubleValue(
				irow, "noriginalcurprice"), irow,
				"nqtorgprc");
		setBodyValueAt(getBillCardTools().getBodyUFDoubleValue(
				irow, "noriginalcurtaxprice"), irow,
				"nqtorgtaxprc");
		
	}

	/**
	 * ���ô����������Ϣ,��������
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
		// ���۵�λ����
		UFDouble nnum = (UFDouble) getBodyValueAt(irow, "nquoteunitnum");
		if (nnum == null)
			nnum = new UFDouble(1);

		BuylargessVO vo = null;
		BuylargessHVO bhvo = null;

		BuylargessVO votmp = null;
		BuylargessHVO btmphvo = null;

		// �õ��ͻ������
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
				// ���ͻ�->�ͻ�����->�ϲ�ͻ�����
				bhvo = (BuylargessHVO) vo.getParentVO();
				// ������еĿͻ���Ϊ�գ���ӦΪ��ѡ
				if (btmphvo.getPk_cumandoc() != null
						&& btmphvo.getPk_cumandoc().trim().length() > 0) {
					vo = votmp;
				} else if (btmphvo.getPk_custgroup() != null
						&& btmphvo.getPk_custgroup().trim().length() > 0) {
					// �������Ϊ�ͻ�����
					// 1������Ϊ�ͻ�����,ȡ�¼�
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
					// 2������Ϊ�ͻ�����ͻ���Ϊ��
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
		// �����¿���
		if (irow == getRowCount() - 1) {
			uipanel.addNullLine(irow, count);
		} else {
			uipanel.insertNullLine(irow, count);
		}

		// ֻ�����¼��е�ֵ
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
	 *            ���д��
	 * @param htLargess
	 *            ��
	 * @param irow
	 *            ��ǰ��
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
				// ������
				setLargessItems((BuylargessHVO) bvo.getParentVO(), bodys[j], j
						+ irow + 1, irow);

			}
		}

	}

	/**
	 * @param bvo
	 * @param irow
	 *            ��ǰ��
	 * @param imainrow
	 *            �������
	 */
	private void setLargessItems(BuylargessHVO hvo, BuylargessBVO bvo,
			int irow, int imainrow) {
		// 1�������������
		setBodyValueAt(getBodyValueAt(imainrow, "crowno"), irow,
				"clargessrowno");
		// 2��������Ʒ��־
		setBodyValueAt(new UFBoolean(true), irow, "blargessflag");

		setBodyValueAt(bvo.getCunitid(), irow, "cquoteunitid");

		afterUnitEdit(irow, "cquoteunitid");
		// �������������
		// 1. �������Ϊ�������ޣ�=min(����������е�����*��������, ����ֵ)
		// 2. �������Ϊ������ޣ�(���=�����еļ�˰�ϼƣ�����=��˰����(ԭ��))
		// a) ����*����>������ޣ����=�������,����=���/����
		// �����������
		UFDouble umainnum = (UFDouble) getBodyValueAt(imainrow, "nquoteunitnum");
		if (umainnum == null)
			umainnum = new UFDouble(1);
		// ������
		umainnum = umainnum.div(hvo.getNbuynum());

		if (bvo.getFtoplimittype() != null
				&& bvo.getFtoplimittype().intValue() == 0
				&& bvo.getNnum().multiply(umainnum).multiply(
						bvo.getNprice() == null ? new UFDouble(0) : bvo
								.getNprice()).compareTo(
						bvo.getNtoplimitvalue() == null ? new UFDouble(0) : bvo
								.getNtoplimitvalue()) > 0) {// ���ƽ��
			// ����*����>���
			setBodyValueAt(bvo.getNtoplimitvalue() == null ? new UFDouble(0)
					: bvo.getNtoplimitvalue(), irow, "noriginalcursummny");
			// setBodyValueAt(bvo.getNnum().multiply(umainnum),irow,"nquoteunitnum");
			setBodyValueAt((bvo.getNtoplimitvalue() == null ? new UFDouble(0)
					: bvo.getNtoplimitvalue()).div(bvo.getNprice()), irow,
					"nquoteunitnum");
			
		    /** v5.3���۽���㷨��� */
			// System.out.println("setLargessItems----RelationsCal.calculate��#"+irow+"#"+"nquoteunitnum"+"#-->���ÿ�ʼ��");
			RelationsCal.calculate(irow, this.oldValue, this, SaleorderBVO.getCalculatePara("nquoteunitnum", null,
					uipanel.SA_02, uipanel.SO40), "nquoteunitnum", SaleorderBVO.getKeys(), SaleorderBVO.getField(),
					SaleorderBVO.class.getName(), SaleorderHVO.class.getName(), null);
			
			
			afterNumberEditLogic(irow, "noriginalcursummny", false);

		} else if (bvo.getFtoplimittype() != null
				&& bvo.getFtoplimittype().intValue() == 1
				&& bvo.getNnum().multiply(umainnum).compareTo(
						bvo.ntoplimitvalue) > 0) {// ��������
			setBodyValueAt(bvo.ntoplimitvalue, irow, "nquoteunitnum");
			afterNumberEdit(new int[] { irow }, "nquoteunitnum", null, false,
					true);
			setBodyValueAt(bvo.getNprice() == null ? new UFDouble(0) : bvo
					.getNprice(), irow, "norgqttaxprc");

			 /** v5.3���۽���㷨��� */
			// System.out.println("setLargessItems----RelationsCal.calculate��#"+irow+"#"+"nquoteunitnum"+"#-->���ÿ�ʼ��");
			RelationsCal.calculate(irow, this.oldValue, this, SaleorderBVO.getCalculatePara("nquoteunitnum", null,
					uipanel.SA_02, uipanel.SO40), "nquoteunitnum", SaleorderBVO.getKeys(), SaleorderBVO.getField(),
					SaleorderBVO.class.getName(), SaleorderHVO.class.getName(), null);

			afterNumberEditLogic(irow, "norgqttaxprc", false);

		} else {
			// ֱ����������
			setBodyValueAt(bvo.getNnum().multiply(umainnum), irow,
					"nquoteunitnum");
			setBodyValueAt(bvo.getNnum().multiply(umainnum)
					.multiply(
							bvo.getNprice() == null ? new UFDouble(0) : bvo
									.getNprice()), irow, "noriginalcursummny");
			// 3�����õ��� ������=��˰����(ԭ��)
			setBodyValueAt(bvo.getNprice() == null ? new UFDouble(0) : bvo
					.getNprice(), irow, "norgqttaxprc");
			
			 /** v5.3���۽���㷨��� */
			//System.out.println("setLargessItems----RelationsCal.calculate��#"+irow+"#"+"nquoteunitnum"+"#-->���ÿ�ʼ��");
			
			RelationsCal.calculate(irow, this.oldValue, this, SaleorderBVO.getCalculatePara("nquoteunitnum", null,
					uipanel.SA_02, uipanel.SO40), "nquoteunitnum", SaleorderBVO.getKeys(), SaleorderBVO.getField(),
					SaleorderBVO.class.getName(), SaleorderHVO.class.getName(), null);
			
			afterNumberEditLogic(irow, "norgqttaxprc", false);

		}
		
		// ����ԭʼ��Ѱ�۵���
		setBodyValueAt(getBillCardTools().getBodyUFDoubleValue(
				irow, "noriginalcurprice"), irow,
				"nqtorgprc");
		setBodyValueAt(getBillCardTools().getBodyUFDoubleValue(
				irow, "noriginalcurtaxprice"), irow,
				"nqtorgtaxprc");
		
		
		/** 
		 * ��Ʒ�����ĵ��۽������ڶ�����.���۶����������������ú�,��������Ʒ��Ȼ�����˵��۽��. 
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
		 * ��Ʒ�����ĵ��۽������ڶ�����.���۶����������������ú�,��������Ʒ��Ȼ�����˵��۽��. 
		 * v5.3 jiangzhe zhangcheng 
		 */
		
	}

	/**
	 * ���Թ�����ͬ
	 * ����Ĭ�ϵĺ�ͬ�������Դ�������͡���ͬID����ͬ�š���ͬ�����ȡ� ����ѡ���ʱ���á�
	 * @param int
	 *            irow �к�
	 * 
	 */
	private void setDefaultCtItem(int istartrow, int ilength) {
		try {
			// ���id
			String[] sinvs = null;
			// ����id
			String[] smans = null;
			String sman = getHeadItem("ccustomerid").getValue();
			if (sman == null || sman.trim().length() == 0)
				return;
			// ȡ������
			// �����ԴΪ�յĿ�����������ͬ
			String sSource = null;
			Vector vt = new Vector();//���id
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

			// ��ѯ��ͬ�����ֵ
			/** ֻҪ�д���ͻ��ѯ��ͬ���ض�����һ��Զ�̵���* */
			Hashtable ht = SaleOrderBO_Client.queryForCntAll(ClientEnvironment
					.getInstance().getCorporation().getPk_corp(), sinvs, smans,
					ClientEnvironment.getInstance().getDate());
			if (ht != null && ht.size() > 0) {
				if (uipanel
						.showYesNoMessage(nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("40060301", "UPP40060301-000508")/* ������غ�ͬ���Ƿ����? */) 
								== MessageDialog.ID_NO) {
					for (int i = 0; i < ilength; i++) {
						setCellEditable(istartrow + i, "ct_name", false);
					}
					return;
				}
			}

			// �����߼�
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
					
					/**�����ĺ�ͬ�������ͷ����һ��*/
					//��ͷ���ֲ�Ϊnull
					if (getHeadItem("ccurrencytypeid").getValueObject()!=null){
						if(!rect.getCCurrencyId().equals(
								getHeadItem("ccurrencytypeid").getValueObject().toString())){
						uipanel
								.showWarningMessage(nc.ui.ml.NCLangRes
										.getInstance().getStrByID("40060301",
												"UPT40060301-000549")/* ��֧�ֹ�������ֺ�ͬ��������ѡ������ */);
						setCellEditable(istartrow + i, "ct_name", false);
						getBillData().getBillModel().delLine(new int[]{istartrow + i});
						return;
						}
					}
					//��ͷ����null,��������ָ�����ͷ
					else
						setHeadItem("ccurrencytypeid", rect.getCCurrencyId());
					/**�����ĺ�ͬ�������ͷ����һ��*/
					
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
	 * �O�ú�ͬ�
	 * v5.3 ����������ͬʱ��������ִӺ�ͬȡ
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
		
		//���ñ�����֣��Ӻ�ͬȡ��
		setBodyValueAt(vo.getCCurrencyId(), irow, "ccurrencytypeid");
		getBillModel().execLoadFormulasByKey("ccurrencytypeid");
		
		//���ݱ����б��������۱��۸�����
		UFDouble[] ult = getBillCardTools().getExchangeRate(vo.getCCurrencyId(),
				getHeadItem("dbilldate").getValue(), ClientEnvironment
						.getInstance().getCorporation().getPk_corp());
		setBodyValueAt(ult[0], irow, "nexchangeotobrate");//�۱�����

		
		// ��Ʒ
		if (getBodyValueAt(irow, "blargessflag") != null
				&& ((Boolean) getBodyValueAt(irow, "blargessflag"))
						.booleanValue()) {
			setBodyValueAt(new UFDouble(0), irow, "noriginalcurprice");
			setBodyValueAt(new UFDouble(0), irow, "noriginalcurtaxprice");
		} else {
			setBodyValueAt(vo.getDOrgPrice(), irow, "noriginalcurprice");
			setBodyValueAt(vo.getDOrgTaxPrice(), irow, "noriginalcurtaxprice");
		}

		// ִ�й�ʽ����Ĭ��ֵ,�������۽��
		executeCtFormula(irow);
		setCtItemEditable(irow);
		
		// �Ѽ�˰�ϼƻ���˰���Ϊ�����㣬���м��㣨�������Ը������۵Ĳ������������ߵļ��㣩
		if (uipanel.SO_17.booleanValue()) {
			String key = (uipanel.SA_02.booleanValue() ? "noriginalcursummny" : "noriginalcurmny");
			int[] para = SaleorderBVO.getCalculatePara(key, null, uipanel.SA_02, uipanel.SO40);
			para[1] = RelationsCalVO.ITEMDISCOUNTRATE_PRIOR_TO_PRICE;// ǿ�Ƶ��ۿ�
			
			RelationsCal.calculate(irow, this.oldValue, this, para, key, SaleorderBVO.getKeys(), SaleorderBVO
					.getField(), SaleorderBVO.class.getName(), SaleorderHVO.class.getName(), null);
		}
		
	}

	/**
	 * ��ʼ��������(������)��
	 * 
	 * �������ڣ�(2001-10-9 13:05:04)
	 * 
	 */
	private void initFreeItem(ArrayList<Integer> irows) throws Exception {

		if (getRowCount() <= 0)
			return;

		if (irows.size() == 0)
			return;

		try {
			// ������ȡ�����Ϣ
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
				/** ������ĩ�Ŀ���* */
				for (int i = 0; i < getRowCount() && i < irows.size(); i++) {
					InvVO voInv = (InvVO) alInvs.get(i);
					setBodyFreeValue(irows.get(i), voInv);
					setBodyValueAt(voInv.getFreeItemVO().getWholeFreeItem(),
							irows.get(i), "vfree0");
				}
			}

		} catch (Exception ex) {
			SCMEnv.out("����������ʧ��!");
			throw ex;
		}
	}

	/**
	 * ��������������ֵ �������ڣ�(01-2-26 13:29:17)
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
	 * ˰����ȡ����������ϵ�˰Ŀ��Ӧ��˰�ʣ����Ϊ�գ���ȡ�������������˰Ŀ��Ӧ��˰�ʡ�
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

		// �ȼ�¼ԭ��˰��
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

		/** ��Ʒ��ѯ�ۣ�Ҳ���ټ��������ͽ������ڼ���˰�ʺ�ֱ�Ӽ���* */
		for (int i = 0, curRow, len = rows.size(); i < len; i++) {
			curRow = (Integer) rows.get(i);
			if (getBodyValueAt(curRow, "blargessflag") != null
					&& (Boolean) getBodyValueAt(curRow, "blargessflag")) {
				calculateNumber(curRow, "ntaxrate");
			}
		}
	}

	/**
	 * ����༭���¼����� �������ڣ�(2001-6-23 13:42:53)
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
			// ��Ʒ
			if (getBodyValueAt(irow, "blargessflag") != null
					&& ((Boolean) getBodyValueAt(irow, "blargessflag"))
							.booleanValue()) {
				setBodyValueAt(new UFDouble(0), irow, "noriginalcurprice");
			} else {
				setBodyValueAt(invRef.getRefValue("ct_b.oriprice"), irow,
						"noriginalcurprice");

			}

			// //��ͬ���������
			executeCtFormula(irow);

		}
		setCtItemEditable(irow);

	}

	private void executeCtFormula(int irow) {

		ArrayList<String> bodyFormula = new ArrayList<String>();

		if (uipanel.strState.equals("����")) {
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

		//�۸������
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

		// ����-����ִ���ۼ�����
		UFDouble num = amount.sub(ordnum).compareTo(UFDouble.ZERO_DBL)<=0?UFDouble.ZERO_DBL:amount.sub(ordnum);
		setBodyValueAt(num, irow, "nnumber");
		setBodyValueAt(num, irow, "nquoteunitnum");

		// ������������������
		Object assistunit = getBodyValueAt(irow, "assistunit");
		if (assistunit != null) {
			getBillModel().execFormulas(irow,
					new String[] { "npacknumber->nnumber/scalefactor" });
		}
		
		//��Ʒ�ۿ�=100��������ͬĬ��
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

		/**v5.3 �˴�ֻ���й�ʽ���㣬�����۽�����ȥ��*/
		/*if (uipanel.SO_17.booleanValue()) {
			afterNumberEditLogic(irow, "noriginalcurprice", false);
			// updateUI();
		}*/

	}

	/**
	 * �O�ú�ͬ헵��пɾ�݋��
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
			
			//v5.5 �ϸ�ִ�к�ͬ���۸񡢽��ۿ۶�������༭	&& !uipanel.SA_15.booleanValue()) {
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
	 * ���ۡ����༭���¼�����
	 */
	private void afterPriceMuyEdit(BillEditEvent e) {
		
		if (e==null||e.getRow()==-1||e.getKey()==null||"".equals(e.getKey().trim()))
			return;
		
		//1.���㵥�۽��
		calculateNumber(e.getRow(), e.getKey());
		
		//2.����ɫ�仯
		changeRowColorByEditPrice(e.getRow());
	}

	/**
	 * �����༭���¼����� �������ڣ�(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	private void afterNumberEdit(BillEditEvent e) {
		Object otemp = getBodyValueAt(e.getRow(), e.getKey());
		if ("�˻�".equals(uipanel.strState)) {/*-=notranslate=-*/
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
																	 * "�˻��������ܳ���ԭ�����Ķ�������"
																	 */);
							setBodyValueAt(e.getOldValue(), e.getRow(),
									"nnumber");
							return;
						}
					}

				}
			}
		} //�˻�

		String sPk = (String) getBodyValueAt(e.getRow(), "cinventoryid");
		String corder_bid = (String) getBodyValueAt(e.getRow(), "corder_bid");

		// 20061028 �����޶�����
		if (("nnumber".equals(e.getKey()) || "nquoteunitnum".equals(e.getKey()))
				&& (sPk != null && sPk.trim().length() > 0)
				&& !(corder_bid != null && uipanel.strState.equals("�޶�"))) {
			// ����Դ�Ĳ��ܴ�
			Object sSource = getBodyValueAt(e.getRow(), "creceipttype");

			if (sSource == null || sSource.toString().trim().length() == 0) {
				//����
				UFBoolean blar = new UFBoolean(getBodyValueAt(e.getRow(),
						"blargessflag") == null ? "false" : getBodyValueAt(
						e.getRow(), "blargessflag").toString());
			    //����
				UFBoolean bind = new UFBoolean(getBodyValueAt(e.getRow(),
				"bbindflag") == null ? "false" : getBodyValueAt(
				e.getRow(), "bbindflag").toString());
				
				// ��ǰ�༭�з���������������
				if ( (blar == null || !blar.booleanValue()) 
						&& (bind == null || !bind.booleanValue()) ) {

					// ɾ��ԭ����������Ʒ���
					int[] inewdelline_largess = setBlargebindLineWhenDelLine(new int[] { e
							.getRow() },1);
					if (inewdelline_largess != null && inewdelline_largess.length > 0)
						uipanel.onDelLine(inewdelline_largess);
					
					// ɾ��ԭ��������������
					int[] inewdelline_bind = setBlargebindLineWhenDelLine(new int[] { e
							.getRow() },2);
					if (inewdelline_bind != null && inewdelline_bind.length > 0)
						uipanel.onDelLine(inewdelline_bind);

					afterNumberEdit(new int[] { e.getRow() }, e.getKey(), null,
							false, true);
					
					//TODO �޸�������Ӧ�õ��ô˷��� 
					//���˷�����������������������ʱ��Ҫ����
					afterInventoryMutiEdit(e.getRow(), new String[] { sPk },
							false, true, e.getKey(), true, 2);
					
					return;
				}

				// ��Ʒ

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
	 * �����༭���¼����� �������ڣ�(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	private void afterNumberEditLogic(int row, String key, boolean isFindPrice) {
		
		/** v5.3���۽���㷨��� ͳһ�ڸ÷�����������*/
		/*System.out.println("afterNumberEditLogic----RelationsCal.calculate��#"+row+"#"+key+"#-->���ÿ�ʼ��");
		
		//BillTools.calcUnitNum(row, getBillModel(), key, getBillType());
		
		RelationsCal.calculate(row, this, getCalculatePara(key,null), key, 
				getKeys(), getField(), SaleorderBVO.class.getName(),SaleorderHVO.class.getName(),null);
		
		System.out.println("afterNumberEditLogic----RelationsCal.calculate��#"+row+"#"+key+"#-->���ý�����");*/
		/** v5.3���۽���㷨��� */

		// Ѱ��
		if (isFindPrice) {
			if (!nc.ui.pub.pf.PfUtilClient.makeFlag
					&& (getSouceBillType().equals(SaleBillType.PurchaseOrder)
							|| getSouceBillType().equals(/*-=notranslate=-*/
							SaleBillType.SaleQuotation)/*-=notranslate=-*/

							|| uipanel.strState.equals("�˻�") || (getBodyValueAt(
							row, "discountflag") != null && getBodyValueAt(row,
							"discountflag").equals(/*-=notranslate=-*/
					new Boolean(true))))) {
			} else {
				// ����
				if (getBodyValueAt(row, "nnumber") != null) {

					if (key.equals("nnumber") || key.equals("npacknumber")
							|| key.equals("ccurrencytypename")) {
						findPrice(new int[] { row }, null, false);
					}

				}
			}
		}
		// �����������
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
	 * �˴����뷽��˵���� ��������: �������: ����ֵ: �쳣����: ����:
	 * 
	 * @param eRow
	 *            int
	 */
	private void afterInventorysEdit(int istartrow, int iendrow,
			String sFormulakey, boolean bNeedFindPrice) {

		ArrayList dalist = new ArrayList();
		UFDouble d100 = new UFDouble(100);
		for (int i = istartrow; i < iendrow; i++) {

			// ���ο���:�ĵ�������������Ŀ��wholemanaflag ����
			Object temp = getBodyValueAt(i, "wholemanaflag");
			boolean wholemanaflag = (temp == null ? false : new UFBoolean(temp
					.toString()).booleanValue());
			setCellEditable(i, "fbatchstatus", wholemanaflag);
			setCellEditable(i, "cbatchid", wholemanaflag);

			String cinventoryid = getBillCardTools().getBodyStringValue(i,
					"cinventoryid");
			if (cinventoryid == null || cinventoryid.trim().length() <= 0) {
				// �����������Ͳ��ɱ༭
				setCellEditable(i, "cchantype", false);
			} else {
				setCellEditable(i, "cchantype", true);
			}

			// Ĭ������Ϊ1
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

				// ���������֯
				getBillCardTools().setBodyValueByHead("ccalbodyid", i);
				// �����ֿ�
				getBillCardTools().setBodyValueByHead("cwarehouseid", i);
			}

			// ����˰��
			if (getHeadItem("ntaxrate") != null) {
				String sMainTax = getHeadItem("ntaxrate").getValue();
				if (sMainTax == null
						|| new UFDouble(sMainTax).doubleValue() == 0) {
				} else {
					// ��������˰�ʣ������û��˰�ʿ�Ŀ��Я��˰�ʣ���ָ�Я������˰��
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
		// �ų������ɵ���Ʒ��

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
			
			// �༭�����õ�˰�ʣ�����Ҫ��˰������һ��
			if ("ntaxrate".equals(sFormulakey.trim())){
				for (int i = 0, loop = findrows.length; i < loop; i++)
					calculateNumber(findrows[i], "ntaxrate");
			}
			
			afterNumberEdit(findrows, "nnumber", null, true, bNeedFindPrice);
		}

	}

	/**
	 * ��Ʒ�¼����� �������ڣ�(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	private void afterLargessFlagEdit(BillEditEvent e) {
		// ��Ʒ���μӴ�ӡ�ϼƼ���
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
		// ���ñ༭��
		getBillCardTools().setCellEditableByLargess(
				blargess && !uipanel.SO59.booleanValue(), row);

		// ��Ʒ���μӽ���ϼƼ���
		getBillModel().reCalcurateAll();

	}

	/**
	 * �����༭���¼����� �������ڣ�(2001-6-23 13:42:53)
	 * isinvchg --- ����Ƿ����仯
	 * bNeedFindPrice -- �Ƿ�ѯ��
	 */
	public void afterNumberEdit(int[] rows, String key, String oldinvid,
			boolean isinvchg, boolean bNeedFindPrice) {

		if (rows == null || rows.length <= 0)
			return;

		boolean bisCalculate = getBillModel().isNeedCalculate();
		getBillModel().setNeedCalculate(false);

		//��Ҫѯ�۵���
		ArrayList rowlist = new ArrayList();
		
		String calKey = key;//��������ʹ�õı仯��key
		boolean ifChgPriceWhenChgQtUnit = false;//�޸ı��ۼ�����λ�����¼���ǰ���۵����Ƿ��޸Ĺ�
		for (int i = 0, loop = rows.length; i < loop; i++) {
			
			//�༭����λ�����ۼ�����λ���Ի�����Ϊ�仯������㷨
			if (key.equals("cpackunitname")||key.equals("cpackunitid"))
				calKey = "scalefactor";
			else if (key.equals("cquoteunit")){
				calKey = "nqtscalefactor";
				ifChgPriceWhenChgQtUnit = ifModifyPrice(rows[i]);
			}
			//���ʸı�--�ɺ����afterNumberEditͳһ����
			else if (key.equals("nexchangeotobrate"))
				break;
			
			//if (!ifbindct) {
				RelationsCal.calculate(rows[i], this.oldValue, this, SaleorderBVO.getCalculatePara(calKey, null,
						uipanel.SA_02, uipanel.SO40), calKey, SaleorderBVO.getKeys(), SaleorderBVO.getField(),
						SaleorderBVO.class.getName(), SaleorderHVO.class.getName(), null);
			//}
			
			// Ѱ��
			if ( bNeedFindPrice && isFindPrice(rows[i],key) ) 
				findPriceEntrance(rows[i], key, rowlist,ifChgPriceWhenChgQtUnit);
			
			// �����Ƿ��޸Ĺ��۸�任����ɫ(�޸���������ɫ)
			if ( (key.equals("cquoteunit") && ifChgPriceWhenChgQtUnit) 
				  || (!key.equals("cquoteunit") && !key.equals("nnumber") 
						&& !key.equals("npacknumber") && !key.equals("nquoteunitnum")
						&& !"cinventorycode".equals(key)&&!"cinventoryid".equals(key)
				     )
				)
				changeRowColorByEditPrice(rows[i]);
		}

		//��Ҫ����ѯ��
		if (bNeedFindPrice && (rowlist != null && rowlist.size() > 0) ) {
				int[] findrows = new int[rowlist.size()];

				for (int i = 0, loop = rowlist.size(); i < loop; i++) {
					findrows[i] = ((Integer) rowlist.get(i)).intValue();
				}

				findPrice(findrows, oldinvid, isinvchg);
		}
		//����Ҫ����ѯ�ۣ����ҷǹ�����ͬ���ϸ�ִ�к�ͬʱ��ǵ������ͬ���۸�������¼���
		else {//if (!ifbindct){
			for (int i = 0, loop = rows.length; i < loop; i++) {
				//���Ҿ��۲�Ϊ��
				if ((uipanel.SA_02.booleanValue() && getBodyValueAt(rows[i],
						"noriginalcurtaxnetprice") != null)
						|| (!uipanel.SA_02.booleanValue() && getBodyValueAt(
								rows[i], "noriginalcurnetprice") != null))
					// �����������
				    calculateNumber(rows[i], key);
			}
		}
		
		getBillModel().setNeedCalculate(bisCalculate);
	}
	
	/**
	 * v5.5
	 * panel��ѯ��ͳһ��ڣ�������Ҫѯ�۵���
	 * row -- ��ѯ����
	 * key -- ����ѯ�۵�key
	 * rowlist -- ������Ҫѯ�۵���
	 * ifChgPriceWhenChgQtUnit -- �޸ı��ۼ�����λ�����¼���ǰ���۵����Ƿ��޸Ĺ�
	 */
	public ArrayList<Integer> findPriceEntrance(int row,String key,ArrayList rowlist,
			boolean ifChgPriceWhenChgQtUnit){
		// Ѱ��
		if (!nc.ui.pub.pf.PfUtilClient.makeFlag
				&& (getSouceBillType().equals(
						SaleBillType.PurchaseOrder)
						|| getSouceBillType().equals(
								SaleBillType.SaleQuotation)
					    || (uipanel.strState.equals("�޶�")&&(!uipanel.SO78.booleanValue()))
						|| uipanel.strState.equals("�˻�") || ((getBodyValueAt(
							row, "discountflag") != null && getBodyValueAt(
							row, "discountflag").equals(Boolean.TRUE))))) {
		} else {
			UFBoolean blargessflag = getBillCardTools()
					.getBodyUFBooleanValue(row, "blargessflag");
			// ����
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
						//û���ֹ��޸Ĺ��۸�ſ���ѯ��
						&& ( (key.equals("cquoteunit") && ifChgPriceWhenChgQtUnit) 
							    || (!ifModifyPrice(row))  
							)
						) {					
					//���ѯ�۵Ļ�����Ҫ��ռ۸��ѯ����أ�
					if (key.equals("cchantype")	|| key.equals("cchantypeid")) {
						getBillCardTools().clearBodyValue(getBillCardTools().getPriceItem(),row);
					}
					rowlist.add(new Integer(row));
					// ������Ѱ�ۺ�����������Ѱ�ۺ�ļ���
					if (key.equals("vfree0"))
						key = "nnumber";
				}
			}
		}
		
		return rowlist;
	}
	
	/**
	 * @param row �����Ƿ��޸Ĺ��۸�任����ɫ
	 */
	public void changeRowColorByEditPrice(int row){
		ArrayList rowList  = new ArrayList();
		rowList.add(new Integer(row));
        // ���ֹ��޸ļ۸��б�ɫ
		if (ifModifyPrice(row)) 
			nc.ui.scm.pub.panel.SetColor.setErrorRowColor(this,rowList);
	    else // ��δ�ֹ��޸ļ۸�����ɫΪԭ�б���ɫ
	    	nc.ui.scm.pub.panel.SetColor.resetErrorRowColor(this,rowList);
	}
	
	/**
	 * @param row ����ɫΪԭ�б���ɫ
	 */
	public void resetErrorRowColor(int row){
		ArrayList rowList  = new ArrayList();
		rowList.add(new Integer(row));
        // ��δ�ֹ��޸ļ۸�����ɫΪԭ�б���ɫ
		nc.ui.scm.pub.panel.SetColor.resetErrorRowColor(this,rowList);
	}
	
	/**
	 * �ֹ��޸Ĺ��۸� --- true û���ֹ��޸Ĺ��۸� --- false
	 * 
	 * @return
	 */
	public boolean ifModifyPrice(int row){
        //���ۺ�˰
	    if(uipanel.SA_02.booleanValue()){
	    //���۵�λ��˰����
	    UFDouble norgqttaxprc = (UFDouble)getBodyValueAt(row, "norgqttaxprc");
	    //ѯ��ԭ�Һ�˰����
	    UFDouble nqtorgtaxprc = (UFDouble)getBodyValueAt(row, "nqtorgtaxprc");
	    //���۵�λ��˰����
	    //UFDouble norgqttaxnetprc = (UFDouble)getBodyValueAt(row, "norgqttaxnetprc");
	    //ѯ��ԭ�Һ�˰����
	    //UFDouble nqtorgtaxnetprc = (UFDouble)getBodyValueAt(row, "nqtorgtaxnetprc");
	    //���ۼ�����λ���۷ǿ��Ҳ�����ѯ��ԭ�ҵ���ʱ��˵���޸Ĺ��۸�
	    if( norgqttaxprc != null){
	      if (nqtorgtaxprc == null || norgqttaxprc.compareTo(nqtorgtaxprc) != 0
	            ){
	        return true;
	      }
	    }
	    //������˰
	    }else{
	      //���۵�λ��˰����
	      UFDouble norgqtprc = (UFDouble)getBodyValueAt(row, "norgqtprc");
	      //ѯ��ԭ����˰����
	      UFDouble nqtorgtaxprc = (UFDouble)getBodyValueAt(row, "nqtorgprc");
	      //���۵�λ��˰����
	      //UFDouble norgqtnetprc = (UFDouble)getBodyValueAt(row, "norgqtnetprc");
	      //ѯ��ԭ����˰����
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
	 * ��ʼ��������(������)��
	 * 
	 * �������ڣ�(2001-10-9 13:05:04)
	 * 
	 */
	public void initFreeItem() {

		alInvs = new java.util.ArrayList();
		if (getRowCount() <= 0)
			return;

		try {
			// ������ȡ�����Ϣ
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
			SCMEnv.out("����������ʧ��!");
		}
	}

	/**
	 * �۸����߱༭���¼�����
	 * 
	 * �۸�����޸�֮���Զ������ü۸���Եļ�Ŀ��������ѯ�ۣ���ʹ��Ŀ��û�б仯ҲҪ����ѯ�ۣ�
	 * 
	 */
	private void afterPricePolicy(BillEditEvent e) {
		int[] rows = new int[] { e.getRow() };

		setBodyValueAt(((UIRefPane) getBodyItem("cpricepolicy").getComponent())
				.getRefPK(), e.getRow(), "cpricepolicyid");

		// ��ռ۸���Ŀ����Ŀ��
		setBodyValueAt(null, e.getRow(), "cpriceitemtable");
		setBodyValueAt(null, e.getRow(), "cpriceitemtablename");
		setBodyValueAt(null, e.getRow(), "cpriceitem");
		setBodyValueAt(null, e.getRow(), "cpriceitemid");

		findPrice(rows, null, false);
	}

	/**
	 * ��Ŀ��༭���¼�����
	 * 
	 * ��Ŀ���޸�֮�󣬼۸���Բ���仯������ѯ�ۣ�����ԭ�еļ۸���Ժ��µļ�Ŀ�����ѯ��
	 * 
	 */
	private void afterPriceItemTable(BillEditEvent e) {
		int[] rows = new int[] { e.getRow() };

		setBodyValueAt(((UIRefPane) getBodyItem("cpriceitemtablename")
				.getComponent()).getRefPK(), e.getRow(), "cpriceitemtable");

		// ��ռ۸���Ŀ
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
		// ����״̬
		if (getBillType().equals(SaleBillType.SaleOrder)) {
			try {
				// ����״̬
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
				// wsy ������ʾ
				uipanel.freshOnhandnum(e.getRow());
			} catch (Exception e1) {
				SCMEnv.out(e1);
				// e1.printStackTrace();
			}
		}

		try {
			if (uipanel.getFuncExtend() != null) {
				// ֧�ֹ�����չ
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
	 * ���۶������ƺ����ø��ֶοɱ༭��
	 * @throws BusinessException 
	 */
	public void setEditEnabledForCopy() throws BusinessException{
		//���塢��ͷ�۱�����
        //���ֲ���
		UIRefPane ccurrencytypeid = (UIRefPane) getHeadItem("ccurrencytypeid").getComponent();
		if (CurrParamQuery.getInstance().isLocalCurrType(getCorp(),
				ccurrencytypeid.getRefPK())) 
			getHeadItem("nexchangeotobrate").setEnabled(false);
		else
			getHeadItem("nexchangeotobrate").setEnabled(true);
		//�����۱����ʲ��ɱ༭
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
	 * findPriceSuccess --- ѯ���Ƿ�ɹ�
	 * row ---------------- ѯ����
	 * 
	 * SA_15:�����Ƿ�ѯ��--Ϊ����۸��ۿ��Ƿ�ɱ༭ȡ����ģ��
	 * ��˰�ϼ�,��˰���,��˰����˰����/����---�Ƿ���Ա༭������
	 * SA39--ѯ�������Ƿ������޸ļ۸�
	 * SA40--ѯ�����Ƿ������޸ļ۸�
	 * SA41--�Ƿ������޸��ۿ�
	 * SO40--�Ľ������޸��ۿۻ��ǵ���
	 * @return fieldEditable
	 */
	public void setEditEnabled(boolean findPriceSuccess , int row ){
		// SA_15:�����Ƿ�ѯ��--Ϊ����۸��ۿ��Ƿ�ɱ༭ȡ����ģ��
		if (!uipanel.SA_15.booleanValue())
			return;

		/* 1.���ۿɱ༭�ԣ�ѯ����ȡ����SA40��ѯ������ȡ����SA39 ** */
		// ѯ���۸�
		if (findPriceSuccess) {
			getBillCardTools().setBodyCellsEdit(
					SOBillCardTools.getSaleOrderItems_Price(), row,
					uipanel.SA_40.booleanValue());		
		}
		// û��ѯ���۸�
		else {
			getBillCardTools().setBodyCellsEdit(
					SOBillCardTools.getSaleOrderItems_Price(), row,
					uipanel.SA_39.booleanValue());
		}

		/* 2.�ۿۿɱ༭��:��ȡ����SA41 ********************** */
		getBillCardTools().setBodyCellsEdit(
				new String[] { "nitemdiscountrate" }, row,
				uipanel.SA_41.booleanValue());

		/*
		 * 3.���ۡ���˰�ϼơ���˰���ɱ༭�� SO40--�����ۿۣ�ȡ����SA41
		 * SO40--�������ۣ�ѯ����ȡ����SA40��ѯ������ȡ����SA39
		 */
		if (uipanel.SO40.equals("�����ۿ�"))
			getBillCardTools().setBodyCellsEdit(
					SOBillCardTools.getSaleOrderItems_NetPrice_Mny(), row,
					uipanel.SA_41.booleanValue());
		else if (uipanel.SO40.equals("��������")) {
			// ѯ���۸�
			if (findPriceSuccess) 
				getBillCardTools().setBodyCellsEdit(
						SOBillCardTools.getSaleOrderItems_NetPrice_Mny(), row,
						uipanel.SA_40.booleanValue());
			// û��ѯ���۸�
			else 
				getBillCardTools().setBodyCellsEdit(
						SOBillCardTools.getSaleOrderItems_NetPrice_Mny(), row,
						uipanel.SA_39.booleanValue());
		}
	}

	/**
	 * �༭ǰ�¼����� �������ڣ�(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public boolean beforeEdit(BillEditEvent e) {

		boolean bret = true;

		if (e.getPos() == BillItem.BODY) {	
		
			// ���������֯
		    if (e.getKey().equals("cadvisecalbody")) {
				// ���彨�鷢�������֯�ܿͻ��������������֯��Ȩ�޿���
				UIRefPane ctm = (UIRefPane) getBodyItem(e.getKey())
						.getComponent();
				CalBodySORefModel mol = (CalBodySORefModel) ctm.getRefModel();
				// //�繫˾�ж�
				String cconsigncorp = getBillCardTools().getBodyStringValue(
						e.getRow(), "cconsigncorpid");
				if (cconsigncorp == null) {
					mol.curPkCorp = pk_corp;
				} else {
					mol.curPkCorp = cconsigncorp;
				}

				// //ǿ��ˢ�����ݣ������ò���ʱ���������ݻ�������
				mol.reloadData();

			}
		    else if (e.getKey().equals("bdericttrans")) {
		    	return false;
		    }
			// ��ͬ�������ɴ���ĺ�ͬ����ദ��
			else if (e.getKey().equals("cinventorycode")) {
				beforeInventoryEdit(e);
				// ��Դ�ĵ��ݴ�������޸�
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
						// @ res "���д�����������������ɣ������޸Ĵ����"
						bret = false;
					}
				}

			} // �ջ���ַ
			else if (e.getKey().equals("vreceiveaddress")) {
				beforeBodyAddressEdit(e);
			}
			// ��ͬ
			else if (e.getKey().equals("ct_name")) {
				// �˻��������޸ĺ�ͬ
				if (uipanel.strState.equals("�˻�")) {
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
							System.err.println("��ͬû�����ã�pk_corp=" + pk_corp);
						}
					} catch (Exception e1) {
						SCMEnv.out(e1);
						// e1.printStackTrace();
					}
				}

			}

			// �ۿ۶�
			else if (e.getKey().equals("noriginalcurdiscountmny")) {
				return false;
			}
			// ��Ŀ�׶�
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
			// ���ο���
			else if (e.getKey().equals("cbatchid")) {
				beforeBatchidEdit(e);
			}
			// ������
			else if (e.getKey().equals("vfree0")) {
				// ��ô��VO
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
					SCMEnv.out("����������ʧ��!");
					// ex.printStackTrace();
				}
			}
			// �ջ������֯
			else if ("creccalbody".equals(e.getKey())) {
				// �ǿ繫˾���ɱ༭
				String cconsigncorp = getBillCardTools().getBodyStringValue(
						e.getRow(), "cconsigncorpid");
				if (cconsigncorp == null || pk_corp.equals(cconsigncorp)) {
					bret = false;
				}
			}
			// �ջ��ֿ�
			else if ("crecwarehouse".equals(e.getKey())) {
				// ֱ�����۲ɹ����ɱ༭
				if ("Z".equals(getVerifyRule()))
					bret = false;
			}
		    // ������
			else if ("scalefactor".equals(e.getKey())) {
				UFBoolean assistunit = getBillCardTools().getBodyUFBooleanValue(e.getRow(), "assistunit");
				UFBoolean fixedflag = getBillCardTools().getBodyUFBooleanValue(e.getRow(), "fixedflag");
				if (!assistunit.booleanValue() || fixedflag.booleanValue() )
					bret = false;
			}
		    // ���ۻ�����
			else if ("nqtscalefactor".equals(e.getKey())) {
				String cunitid = getBillCardTools().getBodyStringValue(e.getRow(), "cunitid");
				String cquoteunitid = getBillCardTools().getBodyStringValue(e.getRow(), "cquoteunitid");
				
				UFBoolean bqtfixedflag = getBillCardTools().getBodyUFBooleanValue(e.getRow(), "bqtfixedflag");
				if ( cunitid.equals(cquoteunitid) || bqtfixedflag.booleanValue() )
					bret = false;
			}
			// �Ƿ�̶�������
			else if ("fixedflag".equals(e.getKey())) {
				// ���������޸� V502 qurui
				bret = false;
			}
		    // �Ƿ񱨼۹̶�������
			else if ("bqtfixedflag".equals(e.getKey())) {
				bret = false;
			}
		    // ���� 
			else if ("ccurrencytypeid".equals(e.getKey())||"ccurrencytypename".equals(e.getKey())) {
				bret = false;
			}

			// ��Ʒ
			else if ("blargessflag".equals(e.getKey())) {
				/** �ѳ�����ѿ�Ʊ�Ķ����������޶���Ʒ��� dongwei zhongwei ������������* */
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
	 * ����༭ǰ�¼�����
	 * 
	 * �������ڣ�(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 * 
	 */
	private void beforeInventoryEdit(BillEditEvent e) {

		UIRefPane invRef = (UIRefPane) getBodyItem("cinventorycode")
				.getComponent();

		// Ĭ�ϼ��ص�ǰ����ѡ�Ĵ��
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

		// sp1: ����SO03��ͬһ����ɷ��ж��У������ƴ���Ĳ���
		/** ��Ʒͬ�����ղ�����������ͨ��ճ����ʵ��֧�֣�������У��* */
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
	// // �Ƿ�ʵ�ֽӿڼ��
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
	 * �õ���ǰ����б�
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
	 * ��ͬ�������ɵĴ�������д���ı༭״̬�ʹ�����������޸�
	 * 
	 * �������ڣ�(2002-10-21 13:16:29)
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
				// �д������Ĵ��
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
		// �ջ���ַ����
		UIRefPane vreceiveaddress = (UIRefPane) getBodyItem("vreceiveaddress")
				.getComponent();
		((CustAddrRefModel) vreceiveaddress.getRefModel())
				.setCustId(sCustManID);

	}

	/**
	 * ���β��ձ༭�¼�ǰ�Ĵ��� �������ڣ�(2002-10-21 13:16:29)
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
				// ����ֿ�VO
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

				// ������VO
				InvVO voInv = new InvVO();
				// if (alInvs.size() > iEventRow)
				// voInv = (InvVO) alInvs.get(iEventRow);
				/** ���ܰ�����������ȡ��ʹ�ô��������id��������������ȡֵ��������* */
				for (int i = 0, len = alInvs.size(); i < len; i++) {
					if (((InvVO) alInvs.get(i)).m_cinventoryid
							.equals(getBodyValueAt(iEventRow, "cinventoryid"))) {
						voInv = (InvVO) alInvs.get(i);
						break;
					}
				}
				/** ���ܰ�����������ȡ��ʹ�ô��������id��������������ȡֵ��������* */

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
			SCMEnv.out("���β�ѯʧ�ܣ�");
			// e1.printStackTrace(System.out);
		}
	}

	/**
	 * ���� LotNumbRefPane1 ����ֵ��
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
	 * ÿ�������׳��쳣ʱ������
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	protected void handleException(java.lang.Throwable exception) {

		/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
		SCMEnv.out("--------- δ��׽�����쳣 ---------");
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
	 * �õ�����VO�� �������ڣ�(2001-6-23 9:47:36) ��ճ����ʱ���������������
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
	 * ��ʼ�������ʡ� �������ڣ�(2001-10-9 13:05:04)
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
			SCMEnv.out("��ʼ��������ʧ��!");
		}
	}

	/**
	 * ����ģ���������ơ� �������ڣ�(2001-11-1 15:26:17)
	 */
	protected void setInputLimit() {

		// �������۹�˾Ϊ���ɱ༭��
		getHeadItem("salecorp").setEnabled(false);
		getHeadItem("salecorp").setEdit(false);
		getHeadItem("nreceiptcathmny").setEnabled(false);
		getHeadItem("nreceiptcathmny").setEdit(false);
		// ���ò�Ʒ�߲��ɱ༭
		getBodyItem("cprolinename").setEdit(false);
		getBodyItem("cprolinename").setEnabled(false);

		// �����ۿ�
		((UIRefPane) getHeadItem("ndiscountrate").getComponent())
				.setMinValue(0.0);

		// ��Ʒ�ۿ�
		((UIRefPane) getBodyItem("nitemdiscountrate").getComponent())
				.setMinValue(0.0);

		// ˰��
		((UIRefPane) getBodyItem("ntaxrate").getComponent()).setMinValue(0.0);

		// Ԥ�տ����
		((UIRefPane) getHeadItem("npreceiverate").getComponent())
				.setMinValue(0.0);

		// Ԥ�տ����
		((UIRefPane) getHeadItem("npreceiverate").getComponent())
				.setMaxValue(100.0);

		((UIRefPane) getHeadItem("npreceiverate").getComponent())
				.setMaxLength(20);

		getHeadItem("npreceiverate").setLength(20);

		/**v5.3ȥ�� */
		/*// ����
		((UIRefPane) getHeadItem("naccountperiod").getComponent())
				.setMinValue(0.0);*/

		// ������˰��
		((UIRefPane) getBodyItem("nreturntaxrate").getComponent())
				.setMinValue(0.0);

		// ������˰��
		((UIRefPane) getBodyItem("nreturntaxrate").getComponent())
				.setMaxValue(100.0);

		UIRefPane ref = null;


		if (getBillType().equals(SaleBillType.SaleOrder)) {
			// ���
			UIRefPane refInv = (UIRefPane) getBodyItem("cinventorycode")
					.getComponent();

			if (refInv != null) {

				refInv.getRefModel().setIsDynamicCol(true);
				refInv.getRefModel().setDynamicColClassName(
						"nc.ui.scm.pub.RefDynamic");

				// 03-10-22 yb edit �������������Ƿ������
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

			// ������˾
			ref = (UIRefPane) getBodyItem("cconsigncorp").getComponent();
			ref.getRefModel().setNotLeafSelectedEnabled(true);
		}

		getBillCardTools().setHeadRefLimit(uipanel.strState);

	}

	/**
	 * ����ϼơ� �������ڣ�(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public UFDouble calcurateTotal(String key) {

		UFDouble total = SoVoConst.duf0;

		for (int i = 0; i < getRowCount(); i++) {
			UFBoolean blargessflag = getBillCardTools().getBodyUFBooleanValue(
					i, "blargessflag");
			// 20061026 ����ȱ���ж�
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
	 * ����󴥷��� �������ڣ�(2001-10-26 14:31:14)
	 * 
	 * @param key
	 *            java.lang.String
	 */
	public void afterSort(java.lang.String key) {

		if (uipanel.strState.equals("�޸�") || uipanel.strState.equals("�޶�")
				|| uipanel.strState.equals("����")) {
			initFreeItem();

			// ���������ɫ����
			InvAttrCellRenderer ficr = new InvAttrCellRenderer();
			ficr.setFreeItemRenderer(this, alInvs);
		}
		return;

	}

	/**
	 * ��������
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
	 * ��������֮��,�����ж�Ӧ˳��<�кţ���>
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
	 * ��������ӱ����һ��˵��ĵ���¼�������
	 * <p>
	 * <b>����˵��</b>
	 * @param items �һ��˵���Ŀ
	 * <p>
	 * @author duy
	 * @time 2008-12-19 ����04:02:00
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
	
	// ����ǰ����
	public void addSortPrepareListener() {
		getBillModel("table").setSortPrepareListener(this);
	}

	public boolean binvedit;

	/* 
	 * ����ʱ����������ֵ����������
	 * (non-Javadoc)
	 * @see nc.ui.pub.bill.IBillModelSortPrepareListener#getSortTypeByBillItemKey(java.lang.String)
	 */
	public int getSortTypeByBillItemKey(String key) {
		if (key.equals("crowno"))
			return BillItem.DECIMAL;
		return BillItem.STRING;
	}
	
	public void showHitDlg(String hitMsg){
		MessageDialog.showHintDlg(getSaleBillCardUI(), "��ʾ", hitMsg);
	}

	/**
	 * ��ǰ����
	 * ĳҵ�����͵ĺ������:Z--ֱ����  Y--��ͨҵ����  C--���ת����  S--ί�д�����
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
