package nc.ui.ehpta.pub.valid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.ehpta.pub.UAPQueryBS;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;

public class Validata {
	
	public static final void saveValidataIsNull(BillCardPanel billCardPanel , AggregatedValueObject currAggVO , String[] bodyTableCodes) throws Exception {
		
		BillItem[] headItems = billCardPanel.getHeadItems();
		List<BillItem> bodyItems = new ArrayList<BillItem>();
		
		if(bodyTableCodes != null && bodyTableCodes.length > 0) {
			for(String tableCode : bodyTableCodes) {
				bodyItems.addAll(Arrays.asList(billCardPanel.getBillData().getBodyItemsForTable(tableCode)));
			}
		}
		
		for(BillItem head : headItems) {
			if(head.isNull()) {
				
				for(String attr : currAggVO.getParentVO().getAttributeNames()) {
					if(attr.equals(head.getKey())) {
						Object obj = currAggVO.getParentVO().getAttributeValue(head.getKey());
						
						if(obj == null || "".equals(obj)) 
							throw new Exception ("表头 ：" + head.getName() + " , 不能为空！");
						
						break;
					} else {
						if(head.getValueObject() == null || "".equals(head.getValueObject()))
							throw new Exception ("表头 ：" + head.getName() + " , 不能为空！");
						
						break;
					}
					
				}
				
			}
			
		}
		
		if(bodyItems != null && bodyItems.size() > 0) {
			for(BillItem body : bodyItems) {
				
				if(body.isNull()) {
					SuperVO[] childVOs = (SuperVO[]) currAggVO.getChildrenVO();
					if(childVOs != null && childVOs.length > 0) {
						int row = 0;
						for(SuperVO child : childVOs) {
							for(String attr : child.getAttributeNames()) {
								if(attr.equals(body.getKey())) {
									Object obj = child.getAttributeValue(body.getKey());
									if(obj == null || "".equals(obj))
										throw new Exception ("表体行"+(row + 1)+"  ：" + body.getName() + " , 不能为空！");
								
									break;
								} else {
									Object obj = billCardPanel.getBodyValueAt(row, body.getKey());
									if(obj == null || "".equals(obj))
										throw new Exception ("表体行"+(row + 1)+" ：" + body.getName() + " , 不能为空！");
								
									break;
								
								}
								
							}
							
							row ++;
						}
						
					}
					
				}
			
			} 
		}
		
	}
	
	public static final void cancleAuditValid(AggregatedValueObject currVO) throws Exception {
		if(currVO != null && currVO.getParentVO() != null) {
			Object pk_contract = currVO.getParentVO().getAttributeValue("pk_contract");
			Integer count = (Integer) UAPQueryBS.getInstance().executeQuery("select count(1) from so_sale where pk_contract = '"+pk_contract+"' and nvl(dr,0)=0 ", new ColumnProcessor());
			count += (Integer) UAPQueryBS.getInstance().executeQuery(" select count(1) from arap_djzb where zyx6 = '"+pk_contract+"' and nvl(dr,0)=0 ", new ColumnProcessor());
			
			if(count > 0)
				throw new Exception("当前合同已被引用，当前操作将终止！");
			
			
		}
	}
	
}
