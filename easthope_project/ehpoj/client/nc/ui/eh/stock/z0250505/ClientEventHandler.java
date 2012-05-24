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
 * ����˵������ⵥ
 * 
 * @author ���� 2008-03-24 ����04:03:18
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

	// add by houcq 2010-09-27f���Ƿ����� ���´�ӡ����
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
				getBillUI().showErrorMessage("def_5�ֶε�ֵ����ת��Ϊ����");
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
		// �Էǿ���֤
		getBillCardPanelWrapper().getBillCardPanel().getBillData()
				.dataNotNullValidate();

		// ǰ̨У��
		BillModel bm = getBillCardPanelWrapper().getBillCardPanel()
				.getBillModel();
		PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(
				PubItf.class.getName());
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		int res = new PubTools().uniqueCheck(bm, new String[] { "pk_invbasdoc",
				"vsourcebillid" });
		if (res == 1) {
			getBillUI().showErrorMessage("����ͬһ���ջ�֪ͨ��,��ɾ��һ�����������ظ�����");
			return;
		}

		StockInVO VO = (StockInVO) getBillCardPanelWrapper().getBillVOFromUI()
				.getParentVO();
		StockInBVO[] BVO = (StockInBVO[]) getBillCardPanelWrapper()
				.getBillVOFromUI().getChildrenVO();
		String billno =VO.getBillno();//add by houcq 2011-03-03
		// ��ͷ�����ۼ�
		UFDouble summoney = new UFDouble(0);
		for (int i = 0; i < BVO.length; i++) {
			StockInBVO vo = BVO[i];
			//add by houcq 2011-09-01 begin
			if (vo.getInamount().doubleValue()==0 || vo.getInprice().doubleValue()==0)
			{
				getBillUI().showErrorMessage("������������۸���Ϊ��!");
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
		// ���ܣ����������ʱ���Իػ��ϼ���Ӧ�̵��жϣ������лػ���ʱ����Ӧ��Ϊ���������룩��ʱ�䣺2010-01-08���ߣ���־Զ
		if (vosurcebilltype.equals("")) {
			int rows = this.getBillCardPanelWrapper().getBillCardPanel()
					.getBillModel().getRowCount();
			//add by houcq 2011-06-21 begin
			//�����Ϲ����������isvirtual���ֶ�ΪYʱ��������������ⵥʱ�������ͷ��Ӧ��Ϊ�գ���Ϊ�ջ���ΪNʱ���������ͷ��Ӧ��Ϊ��
			/*modify by houcq 2011-10-10
			 * ȡ��ǰ������򹴺󣬸����Ͽ�����ⵥ��ͷ�޵�λ����������Ĵ��롣��ֹ������ⵥ��ͷ�޵�λ���������
			����ʱ������ӱ��е������Ƿ����ڴ����������ά���ֿ⣬���ޣ�����ʾ�������������δά���ֿ⣬��ȷ�ϸ������Ƿ�����ʹ�ã���
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
				getBillUI().showErrorMessage("��"+temp.substring(0,temp.length()-1)+"�д��������δά���ֿ⣬��ȷ�ϸ������Ƿ�����ʹ��!");
				return;
			}
		
			//add by houcq end
		}		
		/*********** EastHope Editor shaoyt 2011-03-01 Start ***********/
		//��������ֹͬʱ����������
		try {
			
			if(!Toolkits.isEmpty(VO.getVsourcebillid())){
				String sourcebillid = VO.getVsourcebillid().toString();
				boolean bLockSuccess = LockBO_Client.lockPK(sourcebillid, _getOperator(),null);
				if (!bLockSuccess) {
					/*
					 * @res "�������û��ڲ��������Ժ����ԡ�"
					 */
					this.getBillUI().showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("201218", "UPP201218-000056"));
					return;
				}
			}
			super.onBoSave();
			if (vosurcebilltype.equals("ZA20")) { // �ջ�֪ͨ��
				/*
				 * 2.�ڴ��ϱ����� 3.�ڲ�ѯ�жϻ�д
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
		            		getBillUI().showErrorMessage("��"+(i+1)+"�������Ѿ����,��ȡ��������!");
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
				HashMap hm1 = new HashMap();// �ŵ�����ǵĸ�
				HashMap hm2 = new HashMap();// �ŵ�һ���ĸ���
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
	        		getBillUI().showErrorMessage("��Դ����ID����Ϊ��!");
	                return;
	        	}  	        	
	        	if (billno==null)
	        	{
	        		String jcsql = " select ycy_flag from eh_sbbill where ycy_flag='Y' and NVL(dr,0)=0 and pk_sbbill='"+pk_sbbill+"'";
		    		ArrayList sbdlist=(ArrayList) iUAPQueryBS.executeQuery(jcsql, new MapListProcessor());
		        	if (sbdlist!=null &&sbdlist.size()>0)
		        	{
		        		getBillUI().showErrorMessage("��˾�����Ѿ��������,�����ظ����!");
		                return;
		        	}
		    		//add by houcq end
					String sql = "update eh_sbbill set ycy_flag = 'Y' where pk_sbbill ='"
							+ pk_sbbill + "' and NVL(dr,0)=0";
					pubitf.updateSQL(sql);
	        	}
	    		
			}

			// ���ε��Ǽ�ⱨ��(���ؿۼ�)2008��10��30��10:57:53 wm
			if (vosurcebilltype.equals("ZA30")) {
				// �жϼ�ⱨ�浥�е�˾�����Ƿ�����ͨ��
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
								"˾����" + sbstrs[i] + "��δ��������������");
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
				            		getBillUI().showErrorMessage("�ü�ⱨ�浥�Ѿ��������,�����ظ����!");
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
			        		getBillUI().showErrorMessage("��Դ����ID����Ϊ��!");
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
								.getEditFormulas();// ��ȡ�༭��ʽ
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
		int res = onBoDeleteN(); // 1Ϊɾ�� 0Ϊȡ��ɾ��
		if (res == 0) {
			return;
		}
		StockInVO vo = (StockInVO) getBillCardPanelWrapper().getBillVOFromUI()
				.getParentVO();
		String vsourcebilltype = vo.getVsourcebilltype() == null ? "" : vo
				.getVsourcebilltype().toString();
		String ypk_in = vo.getYpk_in() == null ? "" : vo.getYpk_in().toString();
		if (vsourcebilltype.equals("ZA30")) { // ��ⱨ��
			String pk_checkreport = vo.getVsourcebillid() == null ? "" : vo
					.getVsourcebillid().toString();
			String sql = "update eh_stock_checkreport set rk_flag='N' where pk_checkreport in  "
					+ pk_checkreport;
			PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(
					PubItf.class.getName());
			pubitf.updateSQL(sql);
		}
		if (!ypk_in.equals("")) { // �˻����
			String pk_back = vo.getVsourcebillid() == null ? "" : vo
					.getVsourcebillid().toString();
			String sql = "update eh_stock_back set rk_flag='N' where pk_back =  '"
					+ pk_back + "'";
			PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(
					PubItf.class.getName());
			pubitf.updateSQL(sql);
		}
		if (vsourcebilltype.equals("ZA20")) { // �ջ�֪ͨ��
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
		if (vsourcebilltype.equals("ZA18")) { // ˾��
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
		//�������Ƶ����⣬���ϲ������޸�
//		if (!"".equals(str))
//		{
//			getBillCardPanelWrapper().getBillCardPanel().getBodyItem("vinvcode").setEnabled(false);
//		}
		if (str.equals("��ⱨ��")) {
			String def_1 = getBillCardPanelWrapper().getBillCardPanel()
					.getHeadItem("pk_cgpsn").getValueObject() == null ? ""
					: getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
							"pk_cgpsn").getValueObject().toString();
			if (def_1.equals("")) {
				getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
						"pk_cgpsn").setEnabled(true);
			}
			// ���ؿۼ۵Ķ��ջ�۸���޸�
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
					.getVsourcebillid().toString();// ��ⱨ��������PK
			HashMap hm = null;
			try {
				if (pk_checkreport != null && pk_checkreport.length() > 0) {
					hm = KZKJ(pk_checkreport);
					// ���ݼ�ⱨ��PKȡ��˾�����š�
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
						.getPk_invbasdoc().toString(); // ����
				if (!pk_invbasdoc.equals("")) {
					ArrayList al = (ArrayList) hm.get(pk_invbasdoc); // ��Ӧ���ϵļ��Ľ��
					toHeadvalue(al, i); // �Լ������������
				}
			}
			for (int i = 0; i < BVO.length; i++) {
				// �ջ�����
				UFDouble taxinprice = new UFDouble(
						BVO[i].getTaxinprice() == null ? "0" : BVO[i]
								.getTaxinprice().toString());
				// ˾������
				UFDouble poundamount = new UFDouble(
						BVO[i].getPoundamount() == null ? "0" : BVO[i]
								.getPoundamount().toString());
				// ��������
				UFDouble deduamount = new UFDouble(
						getBillCardPanelWrapper().getBillCardPanel()
								.getBodyValueAt(i, "deduamount") == null ? "0"
								: getBillCardPanelWrapper().getBillCardPanel()
										.getBodyValueAt(i, "deduamount")
										.toString());
				// �ۼ�
				UFDouble deduprice = new UFDouble(
						getBillCardPanelWrapper().getBillCardPanel()
								.getBodyValueAt(i, "deduprice") == null ? "0"
								: getBillCardPanelWrapper().getBillCardPanel()
										.getBodyValueAt(i, "deduprice")
										.toString());
				// �������
				UFDouble inamount = new UFDouble(
						getBillCardPanelWrapper().getBillCardPanel()
								.getBodyValueAt(i, "inamount") == null ? "0"
								: getBillCardPanelWrapper().getBillCardPanel()
										.getBodyValueAt(i, "inamount")
										.toString());
				// ���۸�
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
			// <�޸�>���������������ݺ�����Ӹ����͵ĵ�����Ҫ�����Ϊ�ɱ༭״̬��ʱ�䣺2009-09-07
			// this.getEnabled();
		}
		if (str.equals("�˻�֪ͨ��")) {
			try {
				// ȡ��˾�����š�ʱ��2010-01-22���ߣ���־Զ
				StockInVO VO = (StockInVO) getBillCardPanelWrapper()
						.getBillVOFromUI().getParentVO();
				String pk_thsbbill = VO.getVsourcebillid() == null ? null : VO
						.getVsourcebillid().toString();// �˻�֪ͨ��PK
				if (pk_thsbbill != null && pk_thsbbill.length() > 0) {
					// �����˻�֪ͨ��PKȡ��˾�����š�
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

				// <�޸�>������Ŀ��غͿۼ�����Ϊ�����Ա༭״̬.ʱ��:2009-09-04
				// this.getDisabled();//modify by houcq 2011-02-10
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (str.equals("˾����")) {
			try {
				// ȡ��˾�����š�ʱ��2010-01-22���ߣ���־Զ
				StockInVO VO = (StockInVO) getBillCardPanelWrapper()
						.getBillVOFromUI().getParentVO();
				String pk_sbbill = VO.getVsourcebillid() == null ? null : VO
						.getVsourcebillid().toString();// �˻�֪ͨ��PK
				if (pk_sbbill != null && pk_sbbill.length() > 0) {
					// ����˾����PKȡ��˾�����š�
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
				// <�޸�>������Ŀ��غͿۼ�����Ϊ�����Ա༭״̬.ʱ��:2009-09-04
				// this.getDisabled();//modify by houcq 2011-02-10
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (str.equals("�ջ�֪ͨ��")) {
			// <�޸�>������Ŀ��غͿۼ�����Ϊ�����Ա༭״̬.ʱ��:2009-09-04
			// this.getDisabled();//modify by houcq 2011-02-10
		}
		String vsourcebilltype = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("vsourcebilltype").getValueObject() == null ? ""
				: getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
						"vsourcebilltype").getValueObject().toString();
		if (vsourcebilltype.equals(IBillType.eh_z0151001)) {// �ջ�֪ͨ��
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
	 * ��ⱨ�����ϵĿ��ؿۼ۽��
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
					"kzkj").toString()); // ���ص�û�м۸��ϵ��
			String groupitem = hm.get("groupitem") == null ? "" : hm.get(
					"groupitem").toString(); // �������
			UFDouble invprice = new UFDouble(hm.get("invprice") == null ? "-1"
					: hm.get("invprice").toString()); // ���ϼ۸�
			String iskzkj = hm.get("iskzkj") == null ? "" : hm.get("iskzkj")
					.toString(); // �Ƿ���ؿۼ�
			String ishigh = hm.get("ishigh") == null ? "0" : hm.get("ishigh")
					.toString(); // �Ƿ��������

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
	 * ��������
	 * 
	 * @param alrows
	 *            ��Ӧ��ⱨ���е���Ŀ����
	 */
	@SuppressWarnings("unchecked")
	public void toHeadvalue(ArrayList alrows, int row) {
		// ÿ���������Ŀ��ؿۼۺͼ۸�
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
		// û��������п���
		UFDouble nendkz = new UFDouble(0);
		// û�����û�е��۵����пۼ�
		UFDouble nendkjx = new UFDouble(0);
		// û������е��۵����еĿۼ�
		UFDouble nendkj = new UFDouble(0);
		// �������еĿ���
		UFDouble endkz = new UFDouble(0);
		// ����û�е��۵����еĿۼ�
		@SuppressWarnings("unused")
		UFDouble endkjx = new UFDouble(0);
		// �����е��۵����еĿۼ�
		UFDouble endkj = new UFDouble(0);
		for (int i = 0; i < alrows.size(); i++) {
			// �Ӳ౨��ûһ�е�����
			ArrayList alOne = (ArrayList) alrows.get(i);
			// ��Ŀ����
			Integer groupitem = null;
			if (alOne.get(1) == null || alOne.get(1).equals("")) {
				groupitem = new Integer(9);
			} else {
				groupitem = new Integer(alOne.get(1) == null ? "9" : alOne.get(
						1).toString());
			}

			// ���ؿۼ�
			UFDouble kzkj = (UFDouble) alOne.get(0);
			// �Ƿ��ǿ��ؿۼ�
			String iskzkj = (String) alOne.get(3);
			// �Ƿ��������
			Integer ishigh = new Integer(alOne.get(4) == null ? "2" : alOne
					.get(4).toString());
			// �۸�
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
			// û�з���
			case 9:
				UFDouble[] doub = getKZKJS(iskzkj, kzkj, invprice, nendkz,
						nendkjx, nendkj);
				nendkz = doub[0];
				nendkjx = doub[1];
				nendkj = doub[2];
				break;
			// ��һ��
			case 0:
				UFDouble[] doub0 = getKZKJ(iskzkj, kzkj, invprice, endkz0,
						endkj0, endjg0, ishigh);
				endkz0 = doub0[0];
				endkj0 = doub0[1];
				endjg0 = doub0[2];
				break;
			// �ڶ���
			case 1:
				UFDouble[] doub1 = getKZKJ(iskzkj, kzkj, invprice, endkz1,
						endkj1, endjg1, ishigh);
				endkz1 = doub1[0];
				endkj1 = doub1[1];
				endjg1 = doub1[2];
				break;
			// ������
			case 2:
				UFDouble[] doub2 = getKZKJ(iskzkj, kzkj, invprice, endkz2,
						endkj2, endjg2, ishigh);
				endkz2 = doub2[0];
				endkj2 = doub2[1];
				endjg2 = doub2[2];
				break;
			// ������
			case 3:
				UFDouble[] doub3 = getKZKJ(iskzkj, kzkj, invprice, endkz3,
						endkj3, endjg3, ishigh);
				endkz3 = doub3[0];
				endkj3 = doub3[1];
				endjg3 = doub3[2];
				break;
			// ������
			case 4:
				UFDouble[] doub4 = getKZKJ(iskzkj, kzkj, invprice, endkz4,
						endkj4, endjg4, ishigh);
				endkz4 = doub4[0];
				endkj4 = doub4[1];
				endjg4 = doub4[2];
				break;
			// ������
			case 5:
				UFDouble[] doub5 = getKZKJ(iskzkj, kzkj, invprice, endkz5,
						endkj5, endjg5, ishigh);
				endkz5 = doub5[0];
				endkj5 = doub5[1];
				endjg5 = doub5[2];
				break;
			// ������
			case 6:
				UFDouble[] doub6 = getKZKJ(iskzkj, kzkj, invprice, endkz6,
						endkj6, endjg6, ishigh);
				endkz6 = doub6[0];
				endkj6 = doub6[1];
				endjg6 = doub6[2];
				break;
			// �ڰ���
			case 7:
				UFDouble[] doub7 = getKZKJ(iskzkj, kzkj, invprice, endkz7,
						endkz7, endjg7, ishigh);
				endkz7 = doub7[0];
				endkj7 = doub7[1];
				endjg7 = doub7[2];
				break;
			// �ھ���
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
	 * ������ؿۼ۵����ֵ�ļ���
	 * 
	 * @param iskzkj
	 *            �Ƿ��ǿ��ؿۼ�
	 * @param endkz
	 *            ���յĿ���
	 * @param kzkj
	 *            ���ؿۼ�
	 * @param endkj
	 *            ���յĿۼ�
	 * @param endkj
	 *            ���յĿۼ�
	 * @param endjg
	 *            ���յĵ���
	 * @param invprice
	 *            ����
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
	 * û�з���ļ�����ؿۼ�
	 * 
	 * @param iskzkj
	 *            �Ƿ��ǿ��ؿۼ�
	 * @param endkz
	 *            ���յĿ���
	 * @param kzkj
	 *            ���ؿۼ�
	 * @param endkjx
	 *            ���յĿۼۣ��������ۣ�
	 * @param endkj
	 *            ���յĿۼۣ������ۣ�
	 * @param invprice
	 *            ����
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

	// <�޸�>������Ŀ��غͿۼ�����Ϊ�����Ա༭��ʱ��2009-09-04
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

	// <�޸�>������Ŀ��غͿۼ�����Ϊ���Ա༭��ʱ��2009-09-07
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
	 * ���ܣ�ȡ����ⵥ��Ӧ��˾�����š�ʱ�䣺2010-01-21.���ߣ���־Զ
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getsbbillno(String pk_tork, String pk_corp, int kind) {
		String sbbillnos = null;
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		if (kind == 1) {
			// ��ⱨ���Ӧ��˾����
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
		                 getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "vsourcebillid").toString();//��ⵥ�ӱ�PK 
		            
					String Sql = " update eh_stock_receipt_b set rk_flag='N' where pk_receipt_b='"+vsourcebillrowid+"'";
		            pubitf.updateSQL(Sql);
				}
         }
         super.onBoLineDel();
    }
}
