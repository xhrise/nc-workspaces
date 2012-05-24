package nc.ui.eh.stock.z0250505;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.lock.LockBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.pub.Toolkits;
import nc.vo.eh.stock.z0250505.StockInBVO;
import nc.vo.eh.stock.z0250505.StockInVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

/**
 * 功能说明：入库单
 * 
 * @author 王明 2008-03-24 下午04:03:18
 */

@SuppressWarnings("deprecation")
public class ClientEventHandler extends AbstractEventHandler {

	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);

	}

	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
	}

	@Override
	protected void onBoCard() throws Exception {
		// TODO Auto-generated method stub
		super.onBoCard();
	}

	@Override
	protected void onBoCard2() throws Exception {
		// TODO Auto-generated method stub
		super.onBoCard2();
	}

	// add by houcq 2010-09-27f覆盖方法， 更新打印次数
	@Override
	protected void onBoPrint() throws Exception {
		int num = 0;
		String billno = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("billno").getValueObject().toString();
		String old = getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"def_5").getValueObject().toString();
		if (null == old || "".equals(old)) {
			old = "-1";
		} else {
			try {
				num = Integer.valueOf(old).intValue() + 1;
			} catch (Exception e) {
				getBillUI().showErrorMessage("def_5字段的值不能转换为数字");
				return;
			}
		}

		PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(
				PubItf.class.getName());
		String sql = " update eh_stock_in set def_5='" + num
				+ "' where pk_corp='"
				+ ClientEnvironment.getInstance().getCorporation().getPk_corp()
				+ "' and billno='" + billno + "'";
		pubitf.updateSQL(sql);
		onBoRefresh();
		super.onBoPrint();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onBoSave() throws Exception {
		// 对非空验证
		getBillCardPanelWrapper().getBillCardPanel().getBillData()
				.dataNotNullValidate();

		// 前台校验
		BillModel bm = getBillCardPanelWrapper().getBillCardPanel()
				.getBillModel();
		PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(
				PubItf.class.getName());
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		int res = new PubTools().uniqueCheck(bm, new String[] { "pk_invbasdoc",
				"vsourcebillid" });
		if (res == 1) {
			getBillUI().showErrorMessage("来自同一张收货通知单,请删除一条（物料有重复）！");
			return;
		}

		StockInVO VO = (StockInVO) getBillCardPanelWrapper().getBillVOFromUI()
				.getParentVO();
		StockInBVO[] BVO = (StockInBVO[]) getBillCardPanelWrapper()
				.getBillVOFromUI().getChildrenVO();
		String billno =VO.getBillno();//add by houcq 2011-03-03
		// 表头金额的累加
		UFDouble summoney = new UFDouble(0);
		for (int i = 0; i < BVO.length; i++) {
			StockInBVO vo = BVO[i];
			//add by houcq 2011-09-01 begin
			if (vo.getInamount().doubleValue()==0 || vo.getInprice().doubleValue()==0)
			{
				getBillUI().showErrorMessage("入库数量或入库价格不能为零!");
				return;
			}
			//add by houcq 2011-09-01 begin
			UFDouble def_6 = vo.getDef_6() == null ? new UFDouble(0) : vo
					.getDef_6();
			
			summoney = summoney.add(def_6);
		}
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("summoney",
				summoney);
		String vosurcebilltype = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("vsourcebilltype").getValueObject() == null ? ""
				: getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
						"vsourcebilltype").getValueObject().toString();
		@SuppressWarnings("unused")
		String ypk_in = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("ypk_in").getValueObject() == null ? ""
				: getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
						"ypk_in").getValueObject().toString();
		// 功能：当自制入库时，对回机料及供应商的判断（表体有回机料时，供应商为不必须输入）。时间：2010-01-08作者：张志远
		if (vosurcebilltype.equals("")) {
			int rows = this.getBillCardPanelWrapper().getBillCardPanel()
					.getBillModel().getRowCount();
			//add by houcq 2011-06-21 begin
			//当物料管理档案中虚项（isvirtual）字段为Y时，此物料在做入库单时，允许表头供应商为空，当为空或者为N时，不允许表头供应商为空
			/*modify by houcq 2011-10-10
			 * 取消前期虚项打勾后，该物料可在入库单表头无单位情况下做入库的代码。禁止所有入库单表头无单位入库的情况。
			保存时，检查子表中的物料是否有在存货管理档案中维护仓库，如无，则提示：‘存货管理档案未维护仓库，请确认该物料是否允许使用！’
			*/
			StringBuilder  rowcount= new StringBuilder("");
			for (int i = 0; i < rows; i++) {
				String pk_invbasdoc = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "pk_invbasdoc") == null ? 
							"": getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "pk_invbasdoc").toString();
				StringBuilder sb = new StringBuilder()
				.append(" select def1 from bd_invmandoc")
				.append(" where pk_corp = '"+_getCorp().getPk_corp()+"'")
				.append(" and def1 is null")
				.append(" and pk_invmandoc = '"+pk_invbasdoc+"'");
				ArrayList shlist=(ArrayList) iUAPQueryBS.executeQuery(sb.toString(), new MapListProcessor());
	        	if (shlist.size()>0)
	         	{
	         		rowcount.append((i+1)+",");
	        	}
			}
			if (!"".equals(rowcount.toString()))
			{
				String temp =rowcount.toString();
				getBillUI().showErrorMessage("第"+temp.substring(0,temp.length()-1)+"行存货管理档案未维护仓库，请确认该物料是否允许使用!");
				return;
			}
		
			//add by houcq end
		}		
		/*********** EastHope Editor shaoyt 2011-03-01 Start ***********/
		//加锁，防止同时操作并发。
		try {
			
			if(!Toolkits.isEmpty(VO.getVsourcebillid())){
				String sourcebillid = VO.getVsourcebillid().toString();
				boolean bLockSuccess = LockBO_Client.lockPK(sourcebillid, _getOperator(),null);
				if (!bLockSuccess) {
					/*
					 * @res "有其他用户在操作，请稍候再试。"
					 */
					this.getBillUI().showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("201218", "UPP201218-000056"));
					return;
				}
			}
			super.onBoSave();
			if (vosurcebilltype.equals("ZA20")) { // 收货通知单
				/*
				 * 2.在打上表体标记 3.在查询判断回写
				 */
				String[] pk_receipt_b = new String[BVO.length];
				for (int i = 0; i < BVO.length; i++) {
					pk_receipt_b[i] = BVO[i].getVsourcebillid() == null ? ""
							: BVO[i].getVsourcebillid().toString();
					if (billno==null)
					{
						//add by houcq 2011-03-02 begin
		            	String shsql = " select rk_flag from eh_stock_receipt_b where rk_flag='Y' and NVL(dr,0)=0 and pk_receipt_b = '"+pk_receipt_b[i]+"'";
		        		ArrayList shlist=(ArrayList) iUAPQueryBS.executeQuery(shsql, new MapListProcessor());
		            	if (shlist!=null &&shlist.size()>0)
		            	{
		            		getBillUI().showErrorMessage("第"+(i+1)+"行物料已经入库,请取消后重做!");
		                    return;
		            	}
		            	//add by houcq 2011-03-02 end
					}
				}
				String pk_receipt = VO.getVsourcebillid() == null ? "" : VO
						.getVsourcebillid().toString();
				String pk_receipt_bs = PubTools
						.combinArrayToString(pk_receipt_b);
				String sql = "update eh_stock_receipt_b set rk_flag='Y' where pk_receipt_b in "
						+ pk_receipt_bs;
				pubitf.updateSQL(sql);
				String sql2 = "select count(*) amount,'A' flag from eh_stock_receipt_b where NVL(allcheck,'N')='N'"
						+ " and NVL(issb,'N')='N' and pk_receipt ='"
						+ pk_receipt
						+ "' "
						+ " union all "
						+ " select count(*) amount, 'B' flag from eh_stock_receipt_b where NVL(allcheck,'N')='N' "
						+ " and NVL(issb,'N')='N' and pk_receipt ='"
						+ pk_receipt + "' and rk_flag='Y' ";
				ArrayList al = (ArrayList) iUAPQueryBS.executeQuery(sql2,
						new MapListProcessor());
				HashMap hm1 = new HashMap();// 放的做标记的个
				HashMap hm2 = new HashMap();// 放的一共的个数
				for (int i = 0; i < al.size(); i++) {
					HashMap hm = (HashMap) al.get(i);
					UFDouble amountc = new UFDouble(
							hm.get("amount") == null ? "0" : hm.get("amount")
									.toString());
					String flagc = hm.get("flag") == null ? "" : hm.get("flag")
							.toString();
					if (flagc.equals("B")) {
						hm1.put(pk_receipt, amountc);
					}
					if (flagc.equals("A")) {
						hm2.put(pk_receipt, amountc);
					}
				}
				double amountflag = new UFDouble(
						hm1.get(pk_receipt) == null ? "-1000" : hm1.get(
								pk_receipt).toString()).doubleValue();
				double amount = new UFDouble(
						hm2.get(pk_receipt) == null ? "-2000" : hm2.get(
								pk_receipt).toString()).doubleValue();
				if (amountflag == amount) {
					String sql3 = "update eh_stock_receipt set rk2_flag='Y' where pk_receipt='"
							+ pk_receipt + "'";
					pubitf.updateSQL(sql3);
				} else {
					String sql3 = "update eh_stock_receipt set rk2_flag='N' where pk_receipt='"
							+ pk_receipt + "'";
					pubitf.updateSQL(sql3);
				}
			}
			if (vosurcebilltype.equals("ZA18")) {
				String pk_sbbill = VO.getVsourcebillid() == null ? "" : VO
						.getVsourcebillid().toString();
				//add by houcq 2011-03-02 begin
	        	if ("".equals(pk_sbbill))
	        	{
	        		getBillUI().showErrorMessage("来源单据ID不能为空!");
	                return;
	        	}  	        	
	        	if (billno==null)
	        	{
	        		String jcsql = " select ycy_flag from eh_sbbill where ycy_flag='Y' and NVL(dr,0)=0 and pk_sbbill='"+pk_sbbill+"'";
		    		ArrayList sbdlist=(ArrayList) iUAPQueryBS.executeQuery(jcsql, new MapListProcessor());
		        	if (sbdlist!=null &&sbdlist.size()>0)
		        	{
		        		getBillUI().showErrorMessage("该司磅单已经入过库了,不能重复入库!");
		                return;
		        	}
		    		//add by houcq end
					String sql = "update eh_sbbill set ycy_flag = 'Y' where pk_sbbill ='"
							+ pk_sbbill + "' and NVL(dr,0)=0";
					pubitf.updateSQL(sql);
	        	}
	    		
			}

			// 上游的是检测报告(扣重扣价)2008年10月30日10:57:53 wm
			if (vosurcebilltype.equals("ZA30")) {
				// 判断检测报告单中的司磅单是否审批通过
				// add by houcq 2010-09-25 begin
				String sbbillno = VO.getVsbbillno();
				String[] sbstrs = sbbillno.split(",");
				String pk_corp = ClientEnvironment.getInstance()
						.getCorporation().getPk_corp();
				for (int i = 0; i < sbstrs.length; i++) {
					String sbsql = " select * from eh_sbbill where NVL(dr,0)=0 and vbillstatus<>1 and pk_corp='"
							+ pk_corp + "' and billno ='" + sbstrs[i] + "'";
					ArrayList sblist = (ArrayList) iUAPQueryBS.executeQuery(
							sbsql, new MapListProcessor());
					if (sblist.size() > 0) {
						getBillUI().showErrorMessage(
								"司磅单" + sbstrs[i] + "还未审批，请先审批");
						return;
					}
				}
				StockInVO vo = (StockInVO) getBillCardPanelWrapper()
						.getBillVOFromUI().getParentVO();
				String pk_checkreport = vo.getVsourcebillid() == null ? null
						: vo.getVsourcebillid().toString(); 
				
				if(pk_checkreport!=null&&pk_checkreport.length()>0)
			        	{							
							if (billno==null)
							{
								//add by houcq 2011-03-02 begin
				        		String jcsql = " select rk_flag from eh_stock_checkreport where rk_flag='Y' and NVL(dr,0)=0 and pk_checkreport in "+pk_checkreport+"";
				        		ArrayList jclist=(ArrayList) iUAPQueryBS.executeQuery(jcsql, new MapListProcessor());
				            	if (jclist!=null &&jclist.size()>0)
				            	{
				            		getBillUI().showErrorMessage("该检测报告单已经入过库了,不能重复入库!");
				                    return;
				            	}
				        		//add by houcq end
				            	String sql="update eh_stock_checkreport set rk_flag='Y' where pk_checkreport in  "+pk_checkreport;
				        		pubitf.updateSQL(sql);
							}			        	
			        		
			        	}
			        	//add by houcq 2011-03-02 begin
			        	else
			        	{
			        		getBillUI().showErrorMessage("来源单据ID不能为空!");
			                return;
			        	}
			        	//add by houcq 2011-03-02 end
				// add by houcq end
				for (int i = 0; i < BVO.length; i++) {
					UFDouble poundamount = new UFDouble(
							getBillCardPanelWrapper().getBillCardPanel()
									.getBodyValueAt(i, "poundamount") == null ? "0"
									: getBillCardPanelWrapper()
											.getBillCardPanel().getBodyValueAt(
													i, "poundamount")
											.toString());
					if (poundamount.toDouble() > 0) {
						String[] formual = getBillCardPanelWrapper()
								.getBillCardPanel().getBodyItem("tkj")
								.getEditFormulas();// 获取编辑公式
						getBillCardPanelWrapper().getBillCardPanel()
								.execBodyFormulas(i, formual);
					} else {
						String[] formual = new String[] {
								"inprice->taxinprice-deduprice-kj;",
								"def_6->inamount*inprice", };
						getBillCardPanelWrapper().getBillCardPanel()
								.execBodyFormulas(i, formual);
					}
				}
			}
		} finally {
			if(!Toolkits.isEmpty(VO.getVsourcebillid())){
				String sourcebillid = VO.getVsourcebillid().toString();
				LockBO_Client.freePK(sourcebillid, _getOperator(), null);
			}
		}
		/*********** EastHope Editor shaoyt 2011-03-01 End ***********/
	}

	@Override
	protected void onBoDelete() throws Exception {
		int res = onBoDeleteN(); // 1为删除 0为取消删除
		if (res == 0) {
			return;
		}
		StockInVO vo = (StockInVO) getBillCardPanelWrapper().getBillVOFromUI()
				.getParentVO();
		String vsourcebilltype = vo.getVsourcebilltype() == null ? "" : vo
				.getVsourcebilltype().toString();
		String ypk_in = vo.getYpk_in() == null ? "" : vo.getYpk_in().toString();
		if (vsourcebilltype.equals("ZA30")) { // 检测报告
			String pk_checkreport = vo.getVsourcebillid() == null ? "" : vo
					.getVsourcebillid().toString();
			String sql = "update eh_stock_checkreport set rk_flag='N' where pk_checkreport in  "
					+ pk_checkreport;
			PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(
					PubItf.class.getName());
			pubitf.updateSQL(sql);
		}
		if (!ypk_in.equals("")) { // 退货入库
			String pk_back = vo.getVsourcebillid() == null ? "" : vo
					.getVsourcebillid().toString();
			String sql = "update eh_stock_back set rk_flag='N' where pk_back =  '"
					+ pk_back + "'";
			PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(
					PubItf.class.getName());
			pubitf.updateSQL(sql);
		}
		if (vsourcebilltype.equals("ZA20")) { // 收获通知单
			StockInBVO[] BVO = (StockInBVO[]) getBillCardPanelWrapper()
					.getBillVOFromUI().getChildrenVO();
			StockInVO VO = (StockInVO) getBillCardPanelWrapper()
					.getBillVOFromUI().getParentVO();
			PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(
					PubItf.class.getName());
			String[] pk_receipt_b = new String[BVO.length];
			for (int i = 0; i < BVO.length; i++) {
				pk_receipt_b[i] = BVO[i].getVsourcebillid() == null ? ""
						: BVO[i].getVsourcebillid().toString();
			}
			String pk_receipt = VO.getVsourcebillid() == null ? "" : VO
					.getVsourcebillid().toString();
			String pk_receipt_bs = PubTools.combinArrayToString(pk_receipt_b);
			String sql2 = "update eh_stock_receipt_b set rk_flag='N' where pk_receipt_b in "
					+ pk_receipt_bs;
			pubitf.updateSQL(sql2);
			String sql = "update eh_stock_receipt set rk2_flag='N' where pk_receipt =  '"
					+ pk_receipt + "'";
			pubitf.updateSQL(sql);
		}
		if (vsourcebilltype.equals("ZA18")) { // 司磅
			String pk_sbbill = vo.getVsourcebillid() == null ? "" : vo
					.getVsourcebillid().toString();
			String sql = "update eh_sbbill set ycy_flag = 'N' where pk_sbbill ='"
					+ pk_sbbill + "' and NVL(dr,0)=0";
			PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(
					PubItf.class.getName());
			pubitf.updateSQL(sql);
		}
		super.onBoTrueDelete();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onButton_N(ButtonObject bo, BillModel model) {
		super.onButton_N(bo, model);
		String str = bo.getCode();
		//除了自制单据外，物料不允许修改
//		if (!"".equals(str))
//		{
//			getBillCardPanelWrapper().getBillCardPanel().getBodyItem("vinvcode").setEnabled(false);
//		}
		if (str.equals("检测报告")) {
			String def_1 = getBillCardPanelWrapper().getBillCardPanel()
					.getHeadItem("pk_cgpsn").getValueObject() == null ? ""
					: getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
							"pk_cgpsn").getValueObject().toString();
			if (def_1.equals("")) {
				getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
						"pk_cgpsn").setEnabled(true);
			}
			// 扣重扣价的对收获价格的修改
			try {
				StockInBVO[] BVO = (StockInBVO[]) getBillCardPanelWrapper()
						.getBillVOFromUI().getChildrenVO();
				for (int i = 0; i < BVO.length; i++) {
					UFDouble price = new UFDouble(
							getBillCardPanelWrapper().getBillCardPanel()
									.getBodyValueAt(i, "taxinprice") == null ? ""
									: getBillCardPanelWrapper()
											.getBillCardPanel().getBodyValueAt(
													i, "taxinprice").toString());
					if (price.toDouble() == 0) {
						getBillCardPanelWrapper().getBillCardPanel()
								.getBillModel().setCellEditable(i,
										"taxinprice", true);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			StockInBVO[] BVO = null;
			StockInVO VO = null;
			try {
				BVO = (StockInBVO[]) getBillCardPanelWrapper()
						.getBillVOFromUI().getChildrenVO();
				VO = (StockInVO) getBillCardPanelWrapper().getBillVOFromUI()
						.getParentVO();
			} catch (Exception e) {
				e.printStackTrace();
			}
			String pk_checkreport = VO.getVsourcebillid() == null ? null : VO
					.getVsourcebillid().toString();// 检测报告的主表的PK
			HashMap hm = null;
			try {
				if (pk_checkreport != null && pk_checkreport.length() > 0) {
					hm = KZKJ(pk_checkreport);
					// 根据检测报告PK取得司磅单号。
					String billnos = this.getsbbillno(pk_checkreport,
							_getCorp().getPk_corp(), 1);
					this.getBillCardPanelWrapper().getBillCardPanel()
							.setHeadItem("vsbbillno", billnos);
				}
			} catch (BusinessException e) {
				e.printStackTrace();
			}
			for (int i = 0; i < BVO.length; i++) {
				StockInBVO bvo = BVO[i];
				String pk_invbasdoc = bvo.getPk_invbasdoc() == null ? "" : bvo
						.getPk_invbasdoc().toString(); // 物料
				if (!pk_invbasdoc.equals("")) {
					ArrayList al = (ArrayList) hm.get(pk_invbasdoc); // 对应物料的检测的结果
					toHeadvalue(al, i); // 对检测结果的做处理
				}
			}
			for (int i = 0; i < BVO.length; i++) {
				// 收货单价
				UFDouble taxinprice = new UFDouble(
						BVO[i].getTaxinprice() == null ? "0" : BVO[i]
								.getTaxinprice().toString());
				// 司磅重量
				UFDouble poundamount = new UFDouble(
						BVO[i].getPoundamount() == null ? "0" : BVO[i]
								.getPoundamount().toString());
				// 扣重数量
				UFDouble deduamount = new UFDouble(
						getBillCardPanelWrapper().getBillCardPanel()
								.getBodyValueAt(i, "deduamount") == null ? "0"
								: getBillCardPanelWrapper().getBillCardPanel()
										.getBodyValueAt(i, "deduamount")
										.toString());
				// 扣价
				UFDouble deduprice = new UFDouble(
						getBillCardPanelWrapper().getBillCardPanel()
								.getBodyValueAt(i, "deduprice") == null ? "0"
								: getBillCardPanelWrapper().getBillCardPanel()
										.getBodyValueAt(i, "deduprice")
										.toString());
				// 入库数量
				UFDouble inamount = new UFDouble(
						getBillCardPanelWrapper().getBillCardPanel()
								.getBodyValueAt(i, "inamount") == null ? "0"
								: getBillCardPanelWrapper().getBillCardPanel()
										.getBodyValueAt(i, "inamount")
										.toString());
				// 入库价格
				UFDouble inprice = new UFDouble(
						getBillCardPanelWrapper().getBillCardPanel()
								.getBodyValueAt(i, "inprice") == null ? "0"
								: getBillCardPanelWrapper().getBillCardPanel()
										.getBodyValueAt(i, "inprice")
										.toString());

				if (deduamount.toDouble() > 0 && deduprice.toDouble() == 0) {
					getBillCardPanelWrapper().getBillCardPanel()
							.setBodyValueAt(poundamount.sub(deduamount), i,
									"inamount");
					// getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(poundamount.sub(deduamount),
					// i, "poundamount");//add by houcq 2011-02-18
					getBillCardPanelWrapper().getBillCardPanel()
							.setBodyValueAt(poundamount, i, "poundamount");// modify
																			// by
																			// houcq
																			// 2011-02-21
					getBillCardPanelWrapper().getBillCardPanel()
							.setBodyValueAt(
									inprice.multiply((poundamount
											.sub(deduamount))), i, "def_6");
				} else if (deduamount.toDouble() == 0
						&& deduprice.toDouble() > 0) {
					getBillCardPanelWrapper().getBillCardPanel()
							.setBodyValueAt(taxinprice.sub(deduprice), i,
									"inprice");
					getBillCardPanelWrapper().getBillCardPanel()
							.setBodyValueAt(
									(taxinprice.sub(deduprice))
											.multiply(inamount), i, "def_6");
				} else if (deduamount.toDouble() > 0
						&& deduprice.toDouble() > 0) {
					getBillCardPanelWrapper().getBillCardPanel()
							.setBodyValueAt(poundamount.sub(deduamount), i,
									"inamount");
					// getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(poundamount.sub(deduamount),
					// i, "poundamount");//add by houcq 2011-02-18
					getBillCardPanelWrapper().getBillCardPanel()
							.setBodyValueAt(poundamount, i, "poundamount");// modify
																			// by
																			// houcq
																			// 2011-02-21
					getBillCardPanelWrapper().getBillCardPanel()
							.setBodyValueAt(taxinprice.sub(deduprice), i,
									"inprice");
					getBillCardPanelWrapper().getBillCardPanel()
							.setBodyValueAt(
									(poundamount.sub(deduamount))
											.multiply((taxinprice
													.sub(deduprice))), i,
									"def_6");
				}
			}
			// <修改>如果添加完其它单据后再添加该类型的单据需要将其变为可编辑状态。时间：2009-09-07
			// this.getEnabled();
		}
		if (str.equals("退货通知单")) {
			try {
				// 取得司磅单号。时间2010-01-22作者：张志远
				StockInVO VO = (StockInVO) getBillCardPanelWrapper()
						.getBillVOFromUI().getParentVO();
				String pk_thsbbill = VO.getVsourcebillid() == null ? null : VO
						.getVsourcebillid().toString();// 退货通知单PK
				if (pk_thsbbill != null && pk_thsbbill.length() > 0) {
					// 根据退货通知单PK取得司磅单号。
					String billnos = this.getsbbillno(pk_thsbbill, _getCorp()
							.getPk_corp(), 3);
					this.getBillCardPanelWrapper().getBillCardPanel()
							.setHeadItem("vsbbillno", billnos);
				}

				StockInBVO[] BVO = (StockInBVO[]) getBillCardPanelWrapper()
						.getBillVOFromUI().getChildrenVO();
				if (BVO.length > 0) {
					UFDouble inamount = new UFDouble(0).sub(new UFDouble(BVO[0]
							.getInamount() == null ? "0" : BVO[0].getInamount()
							.toString()));
					UFDouble je = new UFDouble(0).sub(new UFDouble(BVO[0]
							.getDef_6() == null ? "0" : BVO[0].getDef_6()
							.toString()));
					getBillCardPanelWrapper().getBillCardPanel()
							.setBodyValueAt(inamount, 0, "inamount");
					getBillCardPanelWrapper().getBillCardPanel()
							.setBodyValueAt(je, 0, "def_6");
				}

				// <修改>将表体的扣重和扣价设置为不可以编辑状态.时间:2009-09-04
				// this.getDisabled();//modify by houcq 2011-02-10
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (str.equals("司磅单")) {
			try {
				// 取得司磅单号。时间2010-01-22作者：张志远
				StockInVO VO = (StockInVO) getBillCardPanelWrapper()
						.getBillVOFromUI().getParentVO();
				String pk_sbbill = VO.getVsourcebillid() == null ? null : VO
						.getVsourcebillid().toString();// 退货通知单PK
				if (pk_sbbill != null && pk_sbbill.length() > 0) {
					// 根据司磅单PK取得司磅单号。
					String billnos = this.getsbbillno(pk_sbbill, _getCorp()
							.getPk_corp(), 2);
					this.getBillCardPanelWrapper().getBillCardPanel()
							.setHeadItem("vsbbillno", billnos);
				}

				StockInBVO[] BVO = (StockInBVO[]) getBillCardPanelWrapper()
						.getBillVOFromUI().getChildrenVO();
				for (int i = 0; i < BVO.length; i++) {
					UFDouble inamount = new UFDouble(
							BVO[i].getInamount() == null ? "0" : BVO[i]
									.getInamount().toString());
					UFDouble inprice = new UFDouble(
							BVO[i].getInprice() == null ? "0" : BVO[i]
									.getInprice().toString());
					getBillCardPanelWrapper().getBillCardPanel()
							.setBodyValueAt(inamount.multiply(inprice), i,
									"def_6");
				}
				// <修改>将表体的扣重和扣价设置为不可以编辑状态.时间:2009-09-04
				// this.getDisabled();//modify by houcq 2011-02-10
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (str.equals("收货通知单")) {
			// <修改>将表体的扣重和扣价设置为不可以编辑状态.时间:2009-09-04
			// this.getDisabled();//modify by houcq 2011-02-10
		}
		String vsourcebilltype = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("vsourcebilltype").getValueObject() == null ? ""
				: getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
						"vsourcebilltype").getValueObject().toString();
		if (vsourcebilltype.equals(IBillType.eh_z0151001)) {// 收货通知单
			getButtonManager().getButton(IBillButton.AddLine).setEnabled(false);
			getButtonManager().getButton(IBillButton.DelLine).setEnabled(false);
			getButtonManager().getButton(IBillButton.InsLine).setEnabled(false);
			getButtonManager().getButton(IBillButton.CopyLine)
					.setEnabled(false);
			getButtonManager().getButton(IBillButton.PasteLine).setEnabled(
					false);
		}
	}

	/**
	 * 检测报告物料的扣重扣价结果
	 * 
	 * @param pk_checkreport
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public HashMap KZKJ(String pk_checkreport) throws BusinessException {
		String sql = "select a.pk_checkreport,a.pk_invbasdoc,b.kzkj,b.groupitem,b.invprice,b.iskzkj,b.ishigh from eh_stock_checkreport a ,eh_stock_checkreport_b b  "
				+ "where a.pk_checkreport in "
				+ pk_checkreport
				+ " and a.pk_checkreport=b.pk_checkreport and NVL(a.dr,0)=0 and NVL(b.dr,0)=0 ";
		HashMap<String, ArrayList> hminv = new HashMap<String, ArrayList>();
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		ArrayList al = (ArrayList) iUAPQueryBS.executeQuery(sql,
				new MapListProcessor());
		for (int i = 0; i < al.size(); i++) {
			HashMap hm = (HashMap) al.get(i);
			String pk_invbasdoc = hm.get("pk_invbasdoc") == null ? "" : hm.get(
					"pk_invbasdoc").toString();
			UFDouble kzkj = new UFDouble(hm.get("kzkj") == null ? "0" : hm.get(
					"kzkj").toString()); // 扣重的没有价格的系数
			String groupitem = hm.get("groupitem") == null ? "" : hm.get(
					"groupitem").toString(); // 分组情况
			UFDouble invprice = new UFDouble(hm.get("invprice") == null ? "-1"
					: hm.get("invprice").toString()); // 物料价格
			String iskzkj = hm.get("iskzkj") == null ? "" : hm.get("iskzkj")
					.toString(); // 是否扣重扣价
			String ishigh = hm.get("ishigh") == null ? "0" : hm.get("ishigh")
					.toString(); // 是否是最高项

			if (hminv.containsKey(pk_invbasdoc)) {
				ArrayList<ArrayList> alinv = (ArrayList<ArrayList>) hminv
						.get(pk_invbasdoc);
				ArrayList<Object> alOne = new ArrayList<Object>();
				alOne.add(kzkj);
				alOne.add(groupitem);
				alOne.add(invprice);
				alOne.add(iskzkj);
				alOne.add(ishigh);
				alinv.add(alOne);
				hminv.put(pk_invbasdoc, alinv);
			} else {
				ArrayList<Object> alOne = new ArrayList<Object>();
				ArrayList<ArrayList> alinv = new ArrayList<ArrayList>();
				alOne.add(kzkj);
				alOne.add(groupitem);
				alOne.add(invprice);
				alOne.add(iskzkj);
				alOne.add(ishigh);
				alinv.add(alOne);
				hminv.put(pk_invbasdoc, alinv);
			}
		}
		return hminv;
	}

	/**
	 * 处理检测结果
	 * 
	 * @param alrows
	 *            对应检测报告中的项目行数
	 */
	@SuppressWarnings("unchecked")
	public void toHeadvalue(ArrayList alrows, int row) {
		// 每个组中最大的扣重扣价和价格
		UFDouble endkz0 = new UFDouble(0);
		UFDouble endkj0 = new UFDouble(0);
		UFDouble endjg0 = new UFDouble(0);
		UFDouble endkz1 = new UFDouble(0);
		UFDouble endkj1 = new UFDouble(0);
		UFDouble endjg1 = new UFDouble(0);
		UFDouble endkz2 = new UFDouble(0);
		UFDouble endkj2 = new UFDouble(0);
		UFDouble endjg2 = new UFDouble(0);
		UFDouble endkz3 = new UFDouble(0);
		UFDouble endkj3 = new UFDouble(0);
		UFDouble endjg3 = new UFDouble(0);
		UFDouble endkz4 = new UFDouble(0);
		UFDouble endkj4 = new UFDouble(0);
		UFDouble endjg4 = new UFDouble(0);
		UFDouble endkz5 = new UFDouble(0);
		UFDouble endkj5 = new UFDouble(0);
		UFDouble endjg5 = new UFDouble(0);
		UFDouble endkz6 = new UFDouble(0);
		UFDouble endkj6 = new UFDouble(0);
		UFDouble endjg6 = new UFDouble(0);
		UFDouble endkz7 = new UFDouble(0);
		UFDouble endkj7 = new UFDouble(0);
		UFDouble endjg7 = new UFDouble(0);
		UFDouble endkz8 = new UFDouble(0);
		UFDouble endkj8 = new UFDouble(0);
		UFDouble endjg8 = new UFDouble(0);
		UFDouble endkz9 = new UFDouble(0);
		UFDouble endkj9 = new UFDouble(0);
		@SuppressWarnings("unused")
		UFDouble endjg9 = new UFDouble(0);
		// 没分组的所有扣重
		UFDouble nendkz = new UFDouble(0);
		// 没分组的没有单价的所有扣价
		UFDouble nendkjx = new UFDouble(0);
		// 没分组的有单价的所有的扣价
		UFDouble nendkj = new UFDouble(0);
		// 分组所有的扣重
		UFDouble endkz = new UFDouble(0);
		// 分组没有单价的所有的扣价
		@SuppressWarnings("unused")
		UFDouble endkjx = new UFDouble(0);
		// 分组有单价的所有的扣价
		UFDouble endkj = new UFDouble(0);
		for (int i = 0; i < alrows.size(); i++) {
			// 接侧报告没一行的数据
			ArrayList alOne = (ArrayList) alrows.get(i);
			// 项目分组
			Integer groupitem = null;
			if (alOne.get(1) == null || alOne.get(1).equals("")) {
				groupitem = new Integer(9);
			} else {
				groupitem = new Integer(alOne.get(1) == null ? "9" : alOne.get(
						1).toString());
			}

			// 扣重扣价
			UFDouble kzkj = (UFDouble) alOne.get(0);
			// 是否是扣重扣价
			String iskzkj = (String) alOne.get(3);
			// 是否是最高项
			Integer ishigh = new Integer(alOne.get(4) == null ? "2" : alOne
					.get(4).toString());
			// 价格
			UFDouble invprice = (UFDouble) alOne.get(2);
			if (invprice.toDouble() == 0) {
				invprice = new UFDouble(
						getBillCardPanelWrapper().getBillCardPanel()
								.getBodyValueAt(row, "taxinprice") == null ? ""
								: getBillCardPanelWrapper().getBillCardPanel()
										.getBodyValueAt(row, "taxinprice")
										.toString());
			}

			switch (groupitem) {
			// 没有分组
			case 9:
				UFDouble[] doub = getKZKJS(iskzkj, kzkj, invprice, nendkz,
						nendkjx, nendkj);
				nendkz = doub[0];
				nendkjx = doub[1];
				nendkj = doub[2];
				break;
			// 第一组
			case 0:
				UFDouble[] doub0 = getKZKJ(iskzkj, kzkj, invprice, endkz0,
						endkj0, endjg0, ishigh);
				endkz0 = doub0[0];
				endkj0 = doub0[1];
				endjg0 = doub0[2];
				break;
			// 第二组
			case 1:
				UFDouble[] doub1 = getKZKJ(iskzkj, kzkj, invprice, endkz1,
						endkj1, endjg1, ishigh);
				endkz1 = doub1[0];
				endkj1 = doub1[1];
				endjg1 = doub1[2];
				break;
			// 第三组
			case 2:
				UFDouble[] doub2 = getKZKJ(iskzkj, kzkj, invprice, endkz2,
						endkj2, endjg2, ishigh);
				endkz2 = doub2[0];
				endkj2 = doub2[1];
				endjg2 = doub2[2];
				break;
			// 第四组
			case 3:
				UFDouble[] doub3 = getKZKJ(iskzkj, kzkj, invprice, endkz3,
						endkj3, endjg3, ishigh);
				endkz3 = doub3[0];
				endkj3 = doub3[1];
				endjg3 = doub3[2];
				break;
			// 第五组
			case 4:
				UFDouble[] doub4 = getKZKJ(iskzkj, kzkj, invprice, endkz4,
						endkj4, endjg4, ishigh);
				endkz4 = doub4[0];
				endkj4 = doub4[1];
				endjg4 = doub4[2];
				break;
			// 第六组
			case 5:
				UFDouble[] doub5 = getKZKJ(iskzkj, kzkj, invprice, endkz5,
						endkj5, endjg5, ishigh);
				endkz5 = doub5[0];
				endkj5 = doub5[1];
				endjg5 = doub5[2];
				break;
			// 第七组
			case 6:
				UFDouble[] doub6 = getKZKJ(iskzkj, kzkj, invprice, endkz6,
						endkj6, endjg6, ishigh);
				endkz6 = doub6[0];
				endkj6 = doub6[1];
				endjg6 = doub6[2];
				break;
			// 第八组
			case 7:
				UFDouble[] doub7 = getKZKJ(iskzkj, kzkj, invprice, endkz7,
						endkz7, endjg7, ishigh);
				endkz7 = doub7[0];
				endkj7 = doub7[1];
				endjg7 = doub7[2];
				break;
			// 第九组
			case 8:
				UFDouble[] doub8 = getKZKJ(iskzkj, kzkj, invprice, endkz8,
						endkj8, endjg8, ishigh);
				endkz8 = doub8[0];
				endkj8 = doub8[1];
				endjg8 = doub8[2];
				break;
			}
		}
		endkj = endkj.add(endkj0);
		endkj = endkj.add(endkj1);
		endkj = endkj.add(endkj2);
		endkj = endkj.add(endkj3);
		endkj = endkj.add(endkj4);
		endkj = endkj.add(endkj5);
		endkj = endkj.add(endkj6);
		endkj = endkj.add(endkj7);
		endkj = endkj.add(endkj8);
		endkj = endkj.add(endkj9);

		endkz = endkz.add(endkz0);
		endkz = endkz.add(endkz1);
		endkz = endkz.add(endkz2);
		endkz = endkz.add(endkz3);
		endkz = endkz.add(endkz4);
		endkz = endkz.add(endkz5);
		endkz = endkz.add(endkz6);
		endkz = endkz.add(endkz7);
		endkz = endkz.add(endkz8);
		endkz = endkz.add(endkz9);

		UFDouble kz = nendkz.add(endkz);
		UFDouble kj = nendkjx.add(endkj);

		UFDouble weight = new UFDouble(
				getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(
						row, "poundamount") == null ? ""
						: getBillCardPanelWrapper().getBillCardPanel()
								.getBodyValueAt(row, "poundamount").toString());
		UFDouble kzsl = kz.multiply(weight);
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(kzsl, row,
				"deduamount");
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(kj, row,
				"deduprice");
	}

	/**
	 * 分组扣重扣价的最大值的计算
	 * 
	 * @param iskzkj
	 *            是否是扣重扣价
	 * @param endkz
	 *            最终的扣重
	 * @param kzkj
	 *            扣重扣价
	 * @param endkj
	 *            最终的扣价
	 * @param endkj
	 *            最终的扣价
	 * @param endjg
	 *            最终的单价
	 * @param invprice
	 *            单价
	 */
	public UFDouble[] getKZKJ(String iskzkj, UFDouble kzkj, UFDouble invprice,
			UFDouble endkz, UFDouble endkj, UFDouble endjg, Integer ishigh) {
		UFDouble[] ufdou = new UFDouble[3];
		if (iskzkj.equals("0")) {
			if (endkz.toDouble() <= kzkj.toDouble()) {
				endkz = kzkj;
			}
		} else if (iskzkj.equals("1")) {
			if (ishigh == 0) {
				if (endkj.toDouble() <= kzkj.multiply(invprice).toDouble()) {
					endkj = kzkj.multiply(invprice);
					endjg = invprice;
				}
			} else if (ishigh == 1) {
				if (endkj.toDouble() >= kzkj.multiply(invprice).toDouble()
						&& endkj.toDouble() != 0) {
					endkj = kzkj.multiply(invprice);
					endjg = invprice;
				} else if (endkj.toDouble() == 0) {
					endkj = kzkj.multiply(invprice);
					endjg = invprice;
				}

			}
		}
		ufdou[0] = endkz;
		ufdou[1] = endkj;
		ufdou[2] = endjg;
		return ufdou;
	}

	/**
	 * 没有分组的计算扣重扣价
	 * 
	 * @param iskzkj
	 *            是否是扣重扣价
	 * @param endkz
	 *            最终的扣重
	 * @param kzkj
	 *            扣重扣价
	 * @param endkjx
	 *            最终的扣价（不含单价）
	 * @param endkj
	 *            最终的扣价（含单价）
	 * @param invprice
	 *            单价
	 */
	public UFDouble[] getKZKJS(String iskzkj, UFDouble kzkj, UFDouble invprice,
			UFDouble endkz, UFDouble endkjx, UFDouble endkj) {
		UFDouble[] ufdou = new UFDouble[3];
		if (iskzkj.equals("0")) {
			endkz = endkz.add(kzkj);
		} else if (iskzkj.equals("1")) {
			endkjx = endkjx.add(invprice.multiply(kzkj));
		}
		ufdou[0] = endkz;
		ufdou[1] = endkjx;
		ufdou[2] = endkj;
		return ufdou;
	}

	public String addCondtion() {
		return " vbilltype = '" + IBillType.eh_z0250505 + "'";
	}

	// <修改>将表体的扣重和扣价设置为不可以编辑。时间2009-09-04
	// public void getDisabled(){
	// try{
	// StockInBVO[] BVO=(StockInBVO[])
	// this.getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
	// for(int i=0;i<BVO.length;i++){
	// this.getBillCardPanelWrapper().getBillCardPanel().getBodyItem("kz").setEnabled(false);
	// this.getBillCardPanelWrapper().getBillCardPanel().getBodyItem("kj").setEnabled(false);
	// }
	// }catch(Exception e){
	// e.printStackTrace();
	// }
	// }

	// <修改>将表体的扣重和扣价设置为可以编辑。时间2009-09-07
	// public void getEnabled(){
	// try{
	// StockInBVO[] BVO=(StockInBVO[])
	// this.getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
	// for(int i=0;i<BVO.length;i++){
	// this.getBillCardPanelWrapper().getBillCardPanel().getBodyItem("kz").setEnabled(true);
	// this.getBillCardPanelWrapper().getBillCardPanel().getBodyItem("kj").setEnabled(true);
	// }
	// }catch(Exception e){
	// e.printStackTrace();
	// }
	// }
	/**
	 * 功能：取得入库单对应的司磅单号。时间：2010-01-21.作者：张志远
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getsbbillno(String pk_tork, String pk_corp, int kind) {
		String sbbillnos = null;
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		if (kind == 1) {
			// 检测报告对应的司磅单
			StringBuffer sql = new StringBuffer()
					.append(
							" select a.def_2 from eh_stock_sample a where a.pk_sample in ( ")
					.append(
							" select b.vsourcebillid from eh_stock_checkreport b ")
					.append(
							" where b.pk_checkreport in " + pk_tork
									+ " and NVL(b.dr,0)=0 and b.pk_corp = '"
									+ pk_corp + "' ").append(
							"  )and NVL(a.dr,0)=0 and a.pk_corp = '" + pk_corp
									+ "' ");
			try {
				ArrayList al = (ArrayList) iUAPQueryBS.executeQuery(sql
						.toString(), new MapListProcessor());
				if (al != null && al.size() > 0) {
					for (int i = 0; i < al.size(); i++) {
						HashMap hm = (HashMap) al.get(i);
						sbbillnos = hm.get("def_2") == null ? "" : hm.get(
								"def_2").toString();
					}
				}
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		} else if (kind == 2) {
			StringBuffer sql = new StringBuffer()
					.append(" select billno from eh_sbbill where pk_sbbill = '"
							+ pk_tork + "' and NVL(dr,0)=0 and pk_corp = '"
							+ pk_corp + "' ");
			try {
				ArrayList al = (ArrayList) iUAPQueryBS.executeQuery(sql
						.toString(), new MapListProcessor());
				if (al != null && al.size() > 0) {
					for (int i = 0; i < al.size(); i++) {
						HashMap hm = (HashMap) al.get(i);
						sbbillnos = hm.get("billno") == null ? "" : hm.get(
								"billno").toString();
					}
				}
			} catch (BusinessException e) {
				e.printStackTrace();
			}

		} else if (kind == 3) {
			StringBuffer sql = new StringBuffer()
					.append(
							" select c.vsbbillno billno from eh_stock_in c where pk_in in( select a.vsourcebillid from eh_stock_sample a ")
					.append(
							" where a.pk_sample in ( select b.vsourcebillid from eh_stock_checkreport b where b.pk_checkreport =  ")
					.append(
							" (select e.vsourcebillid from eh_stock_back e where e.pk_back = '"
									+ pk_tork
									+ "' and NVL(e.dr,0)=0 and e.pk_corp = '"
									+ pk_corp + "') ")
					.append(
							" and NVL(b.dr, 0) = 0 and b.pk_corp = '"
									+ pk_corp
									+ "') and NVL(a.dr, 0) = 0 and a.pk_corp = '"
									+ pk_corp
									+ "') and NVL(c.dr, 0) = 0 and c.pk_corp = '"
									+ pk_corp + "' ");
			try {
				ArrayList al = (ArrayList) iUAPQueryBS.executeQuery(sql
						.toString(), new MapListProcessor());
				if (al != null && al.size() > 0) {
					for (int i = 0; i < al.size(); i++) {
						HashMap hm = (HashMap) al.get(i);
						sbbillnos = hm.get("billno") == null ? "" : hm.get(
								"billno").toString();
					}
				}
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		}
		return sbbillnos;
	}
    //add by houcq 2011-03-03
	@Override
    protected void onBoLineDel() throws Exception{
    	 	 
         int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRowCount();
         PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName()); 
         String vosurcebilltype = getBillCardPanelWrapper().getBillCardPanel()
			.getHeadItem("vsourcebilltype").getValueObject() == null ? ""
			: getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"vsourcebilltype").getValueObject().toString();
         Object billno = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("billno").getValueObject();
         if (vosurcebilltype.equals("ZA20")&&billno!=null) { 
				for (int i = 0; i < row; i++) {					
					String vsourcebillrowid = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "vsourcebillid")==null?"":
		                 getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "vsourcebillid").toString();//入库单子表PK 
		            
					String Sql = " update eh_stock_receipt_b set rk_flag='N' where pk_receipt_b='"+vsourcebillrowid+"'";
		            pubitf.updateSQL(Sql);
				}
         }
         super.onBoLineDel();
    }
}
