package nc.ui.pf.afterclass;

import nc.ui.ehpta.pub.convert.ConvertFunc;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.ehpta.hq010401.SaleContractVO;
import nc.vo.ehpta.hq010402.AidcustVO;
import nc.vo.ehpta.hq010402.MultiBillVO;
import nc.vo.ehpta.hq010402.SaleContractBVO;
import nc.vo.pf.change.IchangeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.so.so001.SaleorderBVO;

public class SaleContractAfterCHG implements IchangeVO {

	public AggregatedValueObject retChangeBusiVO(AggregatedValueObject preVO,
			AggregatedValueObject nowVO) throws BusinessException {
		
		if(nowVO != null) 
			nowVO.getParentVO().setAttributeValue("version", preVO.getParentVO().getAttributeValue("version") == null ? null : preVO.getParentVO().getAttributeValue("version") + ".0");
		
		// 多表体无法获取表体数据，这里直接进行赋值.
		setMultiBody(preVO , nowVO);
		
		if(nowVO != null && nowVO.getChildrenVO() != null && nowVO.getChildrenVO().length > 0) {
			UFDouble totalNnumber = new UFDouble("0" , 2);
			
			for(CircularlyAccessibleValueObject vo : nowVO.getChildrenVO()) {
				totalNnumber = totalNnumber.add(vo.getAttributeValue("nnumber") == null ? new UFDouble("0") : new UFDouble(vo.getAttributeValue("nnumber").toString()));
			}
			
			String totalcnnumber = ConvertFunc.getChinaNum(totalNnumber.toString());
			
			nowVO.getParentVO().setAttributeValue("totalnnumber", totalNnumber);
			nowVO.getParentVO().setAttributeValue("totalcnnumber", totalcnnumber);
			
		}
		
		return nowVO; 
	}

	public AggregatedValueObject[] retChangeBusiVOs(
			AggregatedValueObject[] preVOs, AggregatedValueObject[] nowVOs)
			throws BusinessException {
		
		AggregatedValueObject[] retVOs = nowVOs;
		if(nowVOs != null && nowVOs.length > 0) {
			for(int i = 0 , j = nowVOs.length ; i < j ; i ++) {
				retVOs[i] = retChangeBusiVO(preVOs[i], nowVOs[i]);
			}
		}
		
		return retVOs;
	}
	
	private final void setMultiBody(AggregatedValueObject preVO, AggregatedValueObject nowVO) throws BusinessException {
		
		// 多表体无法获取表体数据，这里直接进行赋值.
		if(preVO instanceof MultiBillVO && Integer.valueOf(nowVO.getParentVO().getAttributeValue("contracttype").toString()) == 20) {
			if(preVO.getChildrenVO() == null || preVO.getChildrenVO().length == 0) {
				
				AggregatedValueObject aggVO = HYPubBO_Client.queryBillVOByPrimaryKey(new String[] { 
						MultiBillVO.class.getName() , 
						SaleContractVO.class.getName(), 
						SaleContractBVO.class.getName(), 
						AidcustVO.class.getName(), 
//						PrepolicyVO.class.getName() 
				}, preVO.getParentVO().getPrimaryKey());
				
				if(aggVO != null && aggVO.getParentVO() != null) {
					CircularlyAccessibleValueObject[] cavos = ((MultiBillVO)aggVO).getTableVO(((MultiBillVO)aggVO).getDefaultTableCode());
					
					if(cavos != null && cavos.length > 0) {
						SaleorderBVO[] bvos = new SaleorderBVO[1];
						int i = 0;
						for(CircularlyAccessibleValueObject cavo : cavos) {
							SaleorderBVO bvo = new SaleorderBVO();
							bvo.setAttributeValue("creceipttype", "HQ06");
							bvo.setAttributeValue("ndiscountmny", 0);
							bvo.setAttributeValue("ndiscountrate", 100);
							bvo.setAttributeValue("nitemdiscountrate", 100);
							bvo.setAttributeValue("noriginalcurdiscountmny", 0);
							bvo.setAttributeValue("ccurrencytypeid", "00010000000000000001");
							bvo.setAttributeValue("nexchangeotobrate", 1);
							bvo.setAttributeValue("cadvisecalbodyid", "1120A8100000000YSLF2");
							
//							bvo.setAttributeValue("cinvbasdocid", cavo.getAttributeValue("pk_invbasdoc"));
//							bvo.setAttributeValue("cinventoryid", cavo.getAttributeValue("pk_invbasdoc"));
							bvo.setAttributeValue("crowno", cavo.getAttributeValue("def1"));
							
							// 长单合同不到数量
//							bvo.setAttributeValue("nnumber", cavo.getAttributeValue("num"));
//							UFDouble num = (UFDouble) cavo.getAttributeValue("num");
//							Object invspec = cavo.getAttributeValue("invspec");
//							Integer numof = 0;
//							try{ numof = num.div(Double.valueOf(invspec.toString())).intValue(); } catch(Exception e) {}
//							bvo.setAttributeValue("numof", numof);
//							bvo.setAttributeValue("nquoteunitnum", cavo.getAttributeValue("num"));
							
//							bvo.setAttributeValue("ntaxrate", cavo.getAttributeValue("taxrate"));
							bvo.setAttributeValue("pk_corp", cavo.getAttributeValue("pk_corp"));
							bvo.setAttributeValue("csourcebillbodyid", cavo.getAttributeValue("pk_contract_b"));
							bvo.setAttributeValue("csourcebillid", cavo.getAttributeValue("pk_contract"));
							
							bvos[i] = bvo;
//							i ++ ;
							
							// 无论如何选择，只拉一条表体。
							break;
						}
						
						nowVO.setChildrenVO(bvos);
					}
				}
			}
		}
	}
	
	public  final String getChinaNum(String number) {
		
		if(number == null || "".equals(number) || Double.valueOf(number) == 0)
			return "";
		
		String val = "";
		boolean check = false;
		for (int i = 0; i < number.length(); i++) {
			char splitChar = number.charAt(number.length() - i - 1);
			
			if(splitChar != '.') {
				Integer signnum = Integer.valueOf(String.valueOf(splitChar));
				
				if(signnum != 0)
					check = true;
				
				if(signnum != 0 || check)
					val += signnum;
			} else {
				val += splitChar;
				check = true;
			}
		}
		
		String[] numArray = val.split("\\.");
		String bigNum = "";
		
		String xiao = numArray[0];
		String zheng = numArray[1];
		
		for (int i = 0; i < xiao.length(); i++) {
			bigNum = getChinaSignNum(xiao.substring(i, i + 1)) + bigNum;
		}
		
		if(xiao.length() > 0 )
			bigNum = "点" + bigNum + "吨";
		else 
			bigNum = "吨整";
		
		String f = "";
		String x = "";
		for (int i = 1; i < zheng.length() + 1; i++) {
			x = zheng.substring(i - 1, i);
			int w = i % 8;
			if (i == 1) {
				if (x.equals("0") == false) {
					bigNum = getChinaSignNum(x) + bigNum;
				}
			} else {
				if (w == 1)
					f = "";
				if (w == 2)
					f = "拾";
				if (w == 3)
					f = "佰";
				if (w == 4)
					f = "仟";
				if (w == 5)
					f = "万";
				if (w == 6)
					f = "拾";
				if (w == 7)
					f = "佰";
				if (w == 0)
					f = "仟";
				if (w == 5) {
					if (zheng.length() - i > 3 && x.equals("0")
							&& (zheng.substring(i, i + 1)).equals("0")
							&& (zheng.substring(i + 1, i + 2)).equals("0")
							&& (zheng.substring(i + 2, i + 3)).equals("0")) {
					} else if (x.equals("0") == false) {
					} else
						bigNum = "万" + bigNum;
				}
				if (w == 1)
					bigNum = "亿" + bigNum;
				if (x.equals("0")
						&& (zheng.substring(i - 2, i - 1)).equals("0")
						|| x.equals("0") && w == 1 || x.equals("0") && w == 5) {
				} else {
					if (x.equals("0")) {
						bigNum = getChinaSignNum(x) + bigNum;
					} else {
						bigNum = getChinaSignNum(x) + f + bigNum;
					}
				}
			}
		}
		
		return bigNum;
		
	}
	
	public  final String getChinaSignNum(String number) {
		
		if (number.equals("0"))
			return "零";
		
		if (number.equals("1"))
			return "壹";
		
		if (number.equals("2"))
			return "贰";
		
		if (number.equals("3"))
			return "叁";
		
		if (number.equals("4"))
			return "肆";
		
		if (number.equals("5"))
			return "伍";
		
		if (number.equals("6"))
			return "陆";
		
		if (number.equals("7"))
			return "柒";
		
		if (number.equals("8"))
			return "捌";
		
		if (number.equals("9"))
			return "玖";
		
		return "";
		
	}

}
