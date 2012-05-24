
package nc.ui.eh.trade.z00120;

import nc.ui.trade.pub.IVOTreeDataByID;
import nc.vo.eh.trade.z00102.InvclVO;
import nc.vo.pub.SuperVO;
/**
 * 功能：物料档案
 * @author 张起源
 * 日期：2008-3-25
 */
public class ClientManageData implements IVOTreeDataByID {

	public String getIDFieldName() {
		return "pk_invcl";
	}

	public String getParentIDFieldName() {
		return "pk_father";
	}

	public String getShowFieldName() {
		return "invclasscode,invclassname";
	}

	public SuperVO[] getTreeVO() {
        SuperVO[] treeVOs  = null;
        
        nc.ui.trade.bsdelegate.BusinessDelegator business = new nc.ui.trade.bsdelegate.BDBusinessDelegator();
        try{
             treeVOs  = business.queryByCondition(InvclVO.class, " isnull(dr,0)=0 order by invclasscode ");
        }catch(Exception e){
            e.printStackTrace();
        }
        return treeVOs;
    }
}


