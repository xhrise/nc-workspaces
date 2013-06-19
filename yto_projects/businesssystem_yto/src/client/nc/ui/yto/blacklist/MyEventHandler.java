package nc.ui.yto.blacklist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;

/**
 * 
 * ������AbstractMyEventHandler�������ʵ���࣬ ��Ҫ�������˰�ť��ִ�ж������û����Զ���Щ����������Ҫ�����޸�
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}
	
	@Override
	protected void onBoSave() throws Exception { 
		
		saveValidataIsNull(getBillCardPanelWrapper().getBillCardPanel(), getBillCardPanelWrapper().getBillVOFromUI(), ((ClientUICtrl)getUIController()).getBodyTableName());
		
		super.onBoSave();
	}
	
	public final void saveValidataIsNull(BillCardPanel billCardPanel , AggregatedValueObject currAggVO , String[] bodyTableCodes) throws Exception {
		
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
							throw new Exception ("��ͷ ��" + head.getName() + " , ����Ϊ�գ�");
						
						break;
					} else {
						if(head.getValueObject() == null || "".equals(head.getValueObject()))
							throw new Exception ("��ͷ ��" + head.getName() + " , ����Ϊ�գ�");
						
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
										throw new Exception ("������"+(row + 1)+"  ��" + body.getName() + " , ����Ϊ�գ�");
								
									break;
								} else {
									Object obj = billCardPanel.getBodyValueAt(row, body.getKey());
									if(obj == null || "".equals(obj))
										throw new Exception ("������"+(row + 1)+" ��" + body.getName() + " , ����Ϊ�գ�");
								
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

}