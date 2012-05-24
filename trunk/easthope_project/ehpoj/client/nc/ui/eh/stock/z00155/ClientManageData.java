
package nc.ui.eh.stock.z00155;

import nc.ui.trade.pub.IVOTreeDataByCode;
import nc.vo.pub.SuperVO;
/**
 * ����ά��
 * @throws Exception
 * @author ����
 * 2008-05-01 ����04:03:18
 */
public class ClientManageData implements IVOTreeDataByCode {

	public String getIDFieldName() {
		return "pk_contracttype";
	}

	public String getParentIDFieldName() {
		return null;
	}

	public String getShowFieldName() {
		return "workshopcode,workshopname";
	}

	public SuperVO[] getTreeVO() {
        SuperVO[] treeVOs  = null;
        
        nc.ui.trade.bsdelegate.BusinessDelegator business = new nc.ui.trade.bsdelegate.BDBusinessDelegator();
        try{
             treeVOs  = business.queryByCondition(nc.vo.eh.stock.z00155.WorkshopVO.class, "pk_corp='"+ClientUI.pk_corp+"' and isnull(dr,0)=0 order by workshopcode ");
        }catch(Exception e){
            e.printStackTrace();
        }
        return treeVOs;
    }

	public String getCodeFieldName() {
		return "workshopcode";
	}

	public String getCodeRule() {
		return "1.1";
	}

}


