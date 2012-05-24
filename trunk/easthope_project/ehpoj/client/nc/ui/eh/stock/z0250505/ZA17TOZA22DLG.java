package nc.ui.eh.stock.z0250505;

import java.awt.Container;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JOptionPane;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.pub.ICombobox;
import nc.ui.eh.uibase.AbstractBTOBBillQueryDLG;
import nc.ui.eh.uibase.AbstractBTOBDLG;
import nc.ui.pub.bill.BillItem;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.eh.iso.z0502005.StockCheckBillVO;
import nc.vo.eh.iso.z0502005.StockCheckreportBVO;
import nc.vo.eh.iso.z0502005.StockCheckreportCVO;
import nc.vo.eh.iso.z0502005.StockCheckreportVO;
import nc.vo.eh.pub.Toolkits;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;

/**
 * 说明:上下游单据界面(检测报告到入库单)
 * @author 王明 
 * 2008-4-18 下午04:02:28
 */
public class ZA17TOZA22DLG extends AbstractBTOBDLG {

	public ZA17TOZA22DLG(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			String nodeKey, Object userObj, Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType,
				businessType, templateId, currentBillType, nodeKey, userObj,
				parent);

		BillItem billItem = getbillListPanel().getBillListData().getHeadItem(
				"vbillstatus");
		initComboBox(billItem, IBillStatus.strStateRemark, true);
        BillItem result = getbillListPanel().getBillListData().getHeadItem("resulst");
        initComboBox(result, ICombobox.STR_RESULE,true);
        BillItem result2 = getbillListPanel().getBillListData().getBodyItem("result");
        initComboBox(result2, ICombobox.STR_PASS_FLAG,true);
        
	}

	public String[] getBillVos() {
		return new String[] {
                StockCheckBillVO.class.getName(),
				StockCheckreportVO.class.getName(),
				StockCheckreportBVO.class.getName(),
                StockCheckreportCVO.class.getName()
				};
	}


	protected AbstractBTOBBillQueryDLG getQueryDlg() {
		if (queryCondition == null) {
			queryCondition = new AbstractBTOBBillQueryDLG(this, getPkCorp(),
					getOperator(), getFunNode(), getBusinessType(),
					getCurrentBillType(), getBillType(), null, "H0502005","检测报告");
			queryCondition.hideNormal();
		}
		return queryCondition;
	}

	public String getHeadCondition() {	
		StringBuffer strWherePart = new StringBuffer();
		// 根据业务类型，公司编码，单据状态查询
		strWherePart.append(" pk_corp = '");
		strWherePart.append(getPkCorp());
		strWherePart.append("'");
		strWherePart.append(" and resulst<>1 ");
		strWherePart.append(" and vsourcebilltype='"+IBillType.eh_z0501505+"' and (isnull(lock_flag,'N')='N' or lock_flag='') and  (isnull(rk_flag,'N')='N' or rk_flag='') " +
                " and th_flag='S' and vbillstatus="+IBillStatus.CHECKPASS+"  ");
		String strwp = strWherePart.toString();
		return strwp;
	}

	@Override
	public void loadBodyData2(String tmpWhere) throws Exception{
		//回去参照单据中表体VO
        String BillVos2 = getBillVos()[3];			//检测物料
        //返回根据条件查询的表体
        SuperVO[] childvo = (SuperVO[]) HYPubBO_Client.queryByCondition(Class.forName(BillVos2), tmpWhere);
        getbillListPanel().getBodyBillModel("eh_stock_checkreport_c").setBodyDataVO(childvo);
        getbillListPanel().getBodyBillModel("eh_stock_checkreport_c").execLoadFormula();
	}
	/**
	 * 功能:判断上游多行明细是否来自与同一个客户
	 * 
	 * @author 王明 2007-11-1 下午03:14:05
	 * @throws BusinessException 
	 */
	public void onOk(){
		if (getbillListPanel().getHeadBillModel().getRowCount() > 0) {
			getbillListPanel().setBodyModelDataCopy(getbillListPanel().getHeadBillModel().convertIntoModelRow(
			getbillListPanel().getHeadTable().getSelectedRow()));
			retBillVo = getbillListPanel().getSelectedVO(m_billVo,m_billHeadVo, m_billBodyVo);
			retBillVos = getbillListPanel().getMultiSelectedVOs(m_billVo,m_billHeadVo, m_billBodyVo);
			StockCheckreportVO stvo = (StockCheckreportVO) retBillVo.getParentVO();
			IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			//判断是否选择
			if (retBillVos.length <= 0) {
				JOptionPane.showMessageDialog(null, "请至少选择一行数据!","提示",JOptionPane.INFORMATION_MESSAGE);
				return;
			}	
			
			if (retBillVos.length >1) {
				JOptionPane.showMessageDialog(null, "不能多张检测报告单同时入库!", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			String pk_receipt= (((StockCheckreportVO) retBillVos[0].getParentVO()).getPk_receipt())==null?""
					:(((StockCheckreportVO) retBillVos[0].getParentVO()).getPk_receipt()).toString();  //收货通知单的主表主键
			String vsbbilltype= (((StockCheckreportVO) retBillVos[0].getParentVO()).getVsbbilltype())==null?""
					:(((StockCheckreportVO) retBillVos[0].getParentVO()).getVsbbilltype()).toString();  //来源单据是司榜还是收货通知单的的单据类型
			
			String[] pk_receipt_bs=new String[retBillVos.length];
			String [] pk_checkreport=new String[retBillVos.length];
			for (int i = 0; i < retBillVos.length; i++) {
				StockCheckreportVO retBillvo = (StockCheckreportVO) retBillVos[i].getParentVO();
				String pk_receipts = retBillvo.getPk_receipt()==null?"":retBillvo.getPk_receipt().toString(); //收货通知单的主表主键
				String vsbbilltypes=retBillvo.getVsbbilltype()==null?"":retBillvo.getVsbbilltype().toString();//来源单据是司榜还是收货通知单的的单据类型
				pk_receipt_bs[i]=retBillvo.getPk_receipt_b()==null?"":retBillvo.getPk_receipt_b().toString();//收货通知单的子表
				pk_checkreport[i]=retBillvo.getPk_checkreport()==null?"":retBillvo.getPk_checkreport().toString();
				
				if(!pk_receipt.equals(pk_receipts)){
					JOptionPane.showMessageDialog(null,
							"你的表头数据选择有误（没有来自同一张收货通知单）", "提示",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				if(!vsbbilltype.equals(vsbbilltypes)){
					JOptionPane.showMessageDialog(null,
							"你的表头数据选择有误（不是同个司榜和收货通知单）", "提示",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
			}
			
			//***************************************kz,kj,tkj带入
			HashMap hmpk_invbasdoc=new HashMap();//以物料为主键存放kz,kj,tkj的值
			for (int i = 0; i < retBillVos.length; i++) {
				ArrayList alkzkj=new ArrayList();
				StockCheckreportVO retBillvo = (StockCheckreportVO) retBillVos[i].getParentVO();
				String pk_invbasdoc=retBillvo.getPk_invbasdoc()==null?"":retBillvo.getPk_invbasdoc().toString();
				UFDouble kz=new UFDouble(retBillvo.getKz()==null?"0":retBillvo.getKz().toString());
				UFDouble kj=new UFDouble(retBillvo.getKj()==null?"0":retBillvo.getKj().toString());
				UFDouble tkj=new UFDouble(retBillvo.getTkj()==null?"0":retBillvo.getTkj().toString());
				alkzkj.add(kz);
				alkzkj.add(kj);
				alkzkj.add(tkj);
				hmpk_invbasdoc.put(pk_invbasdoc, alkzkj);
			}
			//***************************************end
			
			
			
			
			String billid = Toolkits.combinArrayToString(pk_checkreport);//收货通知单的子表PK（‘’，‘’，）
			//表头数据
			//司榜单自制的时候
			if(pk_receipt.equals("")){
				StringBuffer sbr=new StringBuffer();
				for(int i=0;i<retBillVos.length;i++){
					StockCheckreportVO retBillvo = (StockCheckreportVO) retBillVos[i].getParentVO();
					String sbill=retBillvo.getPk_sbbills()==null?"":retBillvo.getPk_sbbills().toString();
					if(i==retBillVos.length-1){
						sbr.append(sbill);
					}else{
						sbr.append(sbill);
						sbr.append(",");
					}
				}
				String sbills=sbr.toString();
				String sql="select pk_cubasdoc,shname,carnumber from eh_sbbill where pk_sbbill in("+sbills+")";
				ArrayList al=null;
				try {
					al=(ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
				} catch (BusinessException e) {
					e.printStackTrace();
				}
				if(al==null||al.size()<=0){
					JOptionPane.showMessageDialog(null,
							"司磅单在检测报告的有问题", "提示",
							JOptionPane.INFORMATION_MESSAGE);
					return;					
				}
				for(int i=0;i<al.size();i++){
					HashMap hm=(HashMap) al.get(0);
					String pk_cuabsdoc=hm.get("pk_cubasdoc")==null?"":hm.get("pk_cubasdoc").toString();
					String shname=hm.get("shname")==null?"":hm.get("shname").toString();
					String carnumber=hm.get("carnumber")==null?"":hm.get("carnumber").toString();
					StockCheckreportVO vos=(StockCheckreportVO) retBillVo.getParentVO();
					vos.setDef_1(shname);
					vos.setDef_2(carnumber);
					vos.setDef_3(pk_cuabsdoc);
					retBillVo.setParentVO(vos);
				}
			}else{//司榜单不是自制的时候
				String sql="select pk_cubasdoc,pk_deptdoc,pk_psndoc,vsourcebillid,retailinfo,carnumber from " +
						"eh_stock_receipt where  pk_receipt='"+pk_receipt+"'";
				
				StringBuffer sbr=new StringBuffer();
				for(int i=0;i<retBillVos.length;i++){
					StockCheckreportVO retBillvo = (StockCheckreportVO) retBillVos[i].getParentVO();
					String sbill=retBillvo.getPk_sbbills()==null?"":retBillvo.getPk_sbbills().toString();
					if(i==retBillVos.length-1){
						sbr.append(sbill);
					}else{
						sbr.append(sbill);
						sbr.append(",");
					}
				}
				String sbills=sbr.toString();
				ArrayList al2=null;
				if(!sbills.equals("")){
					String sql2="select shname,carnumber from eh_sbbill where pk_sbbill in("+sbills+")";
					try {
						al2=(ArrayList) iUAPQueryBS.executeQuery(sql2, new MapListProcessor());
					} catch (BusinessException e) {
						e.printStackTrace();
					}
				}
				ArrayList al=null;
				try {
					al=(ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for(int i=0;i<al.size();i++){
					HashMap hm=(HashMap) al.get(0);
					
					String pk_cubasdoc=hm.get("pk_cubasdoc")==null?"":hm.get("pk_cubasdoc").toString();
					String pk_deptdoc=hm.get("pk_deptdoc")==null?"":hm.get("pk_deptdoc").toString();
					String pk_psndoc=hm.get("pk_psndoc")==null?"":hm.get("pk_psndoc").toString();
					String vsourcebillid=hm.get("vsourcebillid")==null?"":hm.get("vsourcebillid").toString();					

					if(!sbills.equals("")){
						HashMap hm2=(HashMap) al2.get(0);
						String retailinfo=hm2.get("shname")==null?"":hm2.get("shname").toString();
						String carnumber=hm2.get("carnumber")==null?"":hm2.get("carnumber").toString();
						stvo.setDef_1(retailinfo);
						stvo.setDef_2(carnumber);
					}else{
						String retailinfo=hm.get("retailinfo")==null?"":hm.get("retailinfo").toString();
						String carnumber=hm.get("carnumber")==null?"":hm.get("carnumber").toString();
						stvo.setDef_1(retailinfo);
						stvo.setDef_2(carnumber);
					}

					stvo.setDef_3(pk_cubasdoc);
					stvo.setDef_5(pk_psndoc);
					stvo.setNote(vsourcebillid);
				}
				stvo.setVsourcebillid(pk_receipt);//收货通知单的主键PK（表中的数据是通过界面公式全部带出来）
			}
			 
			if(vsbbilltype.equals(IBillType.eh_z0151001)){  //"20"
				// 表体数据					
				String sql2="select a.pk_invbasdoc,c.rkamount,a.packagweight,a.pk_unit,a.taxinprice,a.pk_receipt_b" +
						" from eh_stock_receipt_b a,eh_stock_checkreport c where  " +
						"c.pk_checkreport in "+billid+" and a.pk_receipt_b=c.pk_receipt_b and  isnull(a.dr,0)=0  and isnull(c.dr,0)=0 ";
				
				try {
					Vector ves = (Vector) iUAPQueryBS.executeQuery(sql2,new VectorProcessor());		
					if(ves==null||ves.size()==0){
						JOptionPane.showMessageDialog(null,
								"收货通知单此物料不存在", "提",
								JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					StockCheckreportBVO[] pstv= new StockCheckreportBVO[ves.size()];
					for(int i=0;i<ves.size();i++){
						Vector ve = (Vector) ves.get(i);
						StockCheckreportBVO bVO = new StockCheckreportBVO();
						String pk_invbasdoc = ve.get(0) == null ? "" : ve.get(0).toString();// 物料主键
						UFDouble inamount = ve.get(1) == null ? new UFDouble(): new UFDouble(ve.get(1).toString());// 数量
						String pk_unit = ve.get(3) == null ? "" : ve.get(3).toString();// 单位PK
						UFDouble packagweight = ve.get(2) == null ? new UFDouble(): new UFDouble(ve.get(2).toString()).div(new UFDouble(1000));// 数量
						UFDouble taxinprice = ve.get(4) == null ? new UFDouble(): new UFDouble(ve.get(4).toString());// 牌价
						String pk_receipt_b=ve.get(5)==null?"":ve.get(5).toString();
						UFDouble sub=inamount;
						UFDouble mendye=taxinprice.multiply(sub);
//						bVO.setDef_2(taxinprice.toString()); 扣重扣价的时修改
						bVO.setDef_3(taxinprice.toString());
						bVO.setDef_8(taxinprice);
						bVO.setDef_6(packagweight);
						bVO.setDef_4(pk_unit);
//						bVO.setDef_7(sub);扣重扣价的时修改
						bVO.setDef_5(pk_invbasdoc);
//						bVO.setDef_10(mendye);扣重扣价的时修改
						bVO.setPk_project(pk_receipt_b);
						//*******************自动扣重扣价
						ArrayList alkzkj=(ArrayList) hmpk_invbasdoc.get(pk_invbasdoc);
						bVO.setDef_11(new UFDouble(alkzkj.get(0).toString()));
						bVO.setDef_12(new UFDouble(alkzkj.get(1).toString()));
						bVO.setDef_13(new UFDouble(alkzkj.get(2).toString()));
						bVO.setDef_2((taxinprice.sub(new UFDouble(alkzkj.get(1).toString()))).toString());
						bVO.setDef_7(sub.sub(new UFDouble(alkzkj.get(0).toString())));
						bVO.setDef_10((taxinprice.sub(new UFDouble(alkzkj.get(1).toString()))).multiply(sub.sub(new UFDouble(alkzkj.get(0).toString()))));
						//******************
						
						pstv[i]=bVO;	
						
					}
					retBillVo.setChildrenVO(pstv);	
					StockCheckreportVO vo=(StockCheckreportVO) retBillVo.getParentVO();
					vo.setPk_checkreport(billid);
					retBillVo.setParentVO(vo);
				} catch (BusinessException e) {
					e.printStackTrace();
				}
				
			}
			if(vsbbilltype.equals(IBillType.eh_z06005)){//"18"
				//先要判断是否是由净重的
				StringBuffer br=new StringBuffer();
				if(retBillVos.length>1){
					for(int i=0;i<retBillVos.length;i++){
						StockCheckreportVO retBillvo = (StockCheckreportVO) retBillVos[i].getParentVO();
						String pk_sbills=retBillvo.getPk_sbbills()==null?"":retBillvo.getPk_sbbills().toString();
						br.append(pk_sbills);
						br.append(",");
					}
					br.append("' '");
				}else{
					StockCheckreportVO retBillvo = (StockCheckreportVO) retBillVos[0].getParentVO();
					String pk_sbills=retBillvo.getPk_sbbills()==null?"":retBillvo.getPk_sbbills().toString();
					br.append(pk_sbills);
				}
				//判断空车的重量有没有 a=number b=number  净重 和PK 显示 ，物料,司榜重量，包装物重
				String sql3=null;
				if(pk_receipt.equals("")){
					StringBuffer sql = new StringBuffer("")
					.append(" select c.pk_invbasdoc, ") 
					.append("        sum(c.bzkz) packagweight, ") 
					.append("        a.pk_measdoc pk_unit, ") 
					.append("        sum(c.suttle) suttle, ") 
					.append("        sum(c.emptyload) emptyload, ") 
					.append("        sum(c.wrapperweight) wrapperweight ") 
					.append("   from eh_sbbill c, bd_invbasdoc a, bd_invmandoc aa ") 
					.append("  where c.pk_sbbill in ("+br.toString()+") ") 
					.append("    and aa.pk_invmandoc = c.pk_invbasdoc ") 
					.append("    and a.pk_invbasdoc = aa.pk_invbasdoc ") 
					.append("    and nvl(c.dr, 0) = 0 ") 
					.append("  group by c.pk_invbasdoc, a.pk_measdoc ") ;
					sql3 = sql.toString();

					
				}else{
					sql3="select c.pk_invbasdoc,sum(a.inamount) inamount,sum(c.bzkz) packagweight,a.pk_unit,a.taxinprice,a.pk_receipt_b," +
					"sum(c.suttle) suttle, sum(c.emptyload) emptyload ,sum(c.wrapperweight) wrapperweight from eh_stock_receipt_b a," +
					"eh_sbbill c where  c.vsourcebillrowid=a.pk_receipt_b  and " +
					" c.pk_sbbill in ("+br.toString()+") and " +
					"isnull(a.dr,0)=0 and  isnull(c.dr,0)=0 group by c.pk_invbasdoc,a.pk_unit,a.taxinprice,a.pk_receipt_b";
				}
				ArrayList al=null;
				try {
					al = (ArrayList) iUAPQueryBS.executeQuery(sql3, new MapListProcessor());
				} catch (BusinessException e) {
					e.printStackTrace();
				}
				if(al==null||al.size()==0){
					JOptionPane.showMessageDialog(null,
							"司磅单没有走上下游", "提示",
							JOptionPane.INFORMATION_MESSAGE);
					return;					
				}
				StockCheckreportBVO[] pstv= new StockCheckreportBVO[al.size()];
				for(int i=0;i<al.size();i++){
					HashMap hm=(HashMap) al.get(i);
					StockCheckreportBVO bVO = new StockCheckreportBVO();
					String pk_invbasdoc = hm.get("pk_invbasdoc") == null ? "" : hm.get("pk_invbasdoc").toString();// 物料主键
					UFDouble inamount =hm.get("inamount") == null ? new UFDouble(): new UFDouble(hm.get("inamount").toString());// 数量
					UFDouble taxinprice = hm.get("taxinprice") == null ? new UFDouble(): new UFDouble(hm.get("taxinprice").toString());// 牌价
					UFDouble suttle = hm.get("suttle") == null ? new UFDouble(): new UFDouble(hm.get("suttle").toString()).div(new UFDouble(1000));// 司榜重量
					UFDouble packagweight=new UFDouble(hm.get("packagweight")==null?"":hm.get("packagweight").toString()).div(new UFDouble(1000),3);
					String pk_receipt_b=hm.get("pk_receipt_b") == null ? "" : hm.get("pk_receipt_b").toString();
					String pk_unit = hm.get("pk_unit") == null ? "" : hm.get("pk_unit").toString();// 单位PK
					String emptyload=hm.get("emptyload")==null?"":hm.get("emptyload").toString();
					if(emptyload.equals("")){
						JOptionPane.showMessageDialog(null,
								"还有空车没有测重！！", "提示",
								JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					
					UFDouble sub=suttle.sub(packagweight);
					UFDouble mendye=taxinprice.multiply(sub);
					bVO.setDef_9(sub);
					bVO.setDef_8(taxinprice);
//					bVO.setDef_2(taxinprice.toString());扣重扣价的时修改
					bVO.setDef_3(taxinprice.toString());
					bVO.setDef_4(pk_unit);
//					bVO.setDef_7(sub);扣重扣价的时修改
					bVO.setDef_6(packagweight);
					bVO.setDef_5(pk_invbasdoc);
//					bVO.setDef_10(mendye);扣重扣价的时修改
					bVO.setPk_project(pk_receipt_b);
					pstv[i]=bVO;
					//*******************自动扣重扣价
					ArrayList alkzkj=(ArrayList) hmpk_invbasdoc.get(pk_invbasdoc);
					bVO.setDef_11(new UFDouble(alkzkj.get(0).toString()));
					bVO.setDef_12(new UFDouble(alkzkj.get(1).toString()));
					bVO.setDef_13(new UFDouble(alkzkj.get(2).toString()));
					bVO.setDef_2((taxinprice.sub(new UFDouble(alkzkj.get(1).toString()))).toString());
					bVO.setDef_7(sub.sub(new UFDouble(alkzkj.get(0).toString())));
					bVO.setDef_10((taxinprice.sub(new UFDouble(alkzkj.get(1).toString()))).multiply(sub.sub(new UFDouble(alkzkj.get(0).toString()))));
					//******************
					
				}
					retBillVo.setChildrenVO(pstv);		
					StockCheckreportVO vo=(StockCheckreportVO) retBillVo.getParentVO();
					//add by houcq  begin 2010-10-11
					String sbbillno=vo.getDef_7();
					String[] sbstrs= sbbillno.split(",");
					for (int i=0;i<sbstrs.length;i++)
		        	{        		
		        		String sbsql = " select * from eh_sbbill where NVL(dr,0)=0 and vbillstatus<>1 and pk_corp='"+getPkCorp()+"' and billno ='"+sbstrs[i]+"'";
		        		ArrayList sblist;
						try {
							sblist = (ArrayList) iUAPQueryBS.executeQuery(sbsql, new MapListProcessor());
							String message="司磅单"+sbstrs[i]+"还未审批，请先审批!";
							if (sblist.size()>0)
			            	{
			            		JOptionPane.showMessageDialog(null,
			            				message, "提示",
										JOptionPane.INFORMATION_MESSAGE);
								return;
			            	}
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            	
		        	} 
					//add by houcq end
					vo.setPk_checkreport(billid);
					retBillVo.setParentVO(vo);
				
			}
			this.getAlignmentX();
			this.closeOK();

		}
	}

	
}