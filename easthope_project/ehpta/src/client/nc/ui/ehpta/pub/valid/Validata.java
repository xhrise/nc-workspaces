package nc.ui.ehpta.pub.valid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;

public class Validata {
	
	public static final void saveValidataIsNull(BillCardPanel billCardPanel , AggregatedValueObject currAggVO , String[] bodyTableCodes) throws Exception {
		
		BillItem[] headItems = billCardPanel.getHeadItems();
		List<BillItem> bodyItems = new ArrayList<BillItem>();
		
		for(String tableCode : bodyTableCodes) {
			bodyItems.addAll(Arrays.asList(billCardPanel.getBillData().getBodyItemsForTable(tableCode)));
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
