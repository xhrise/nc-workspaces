
package nc.ui.eh.cw.h13006;

import nc.ui.trade.pub.IVOTreeDataByID;
import nc.vo.eh.cw.h10110.FytypeVO;
import nc.vo.pub.SuperVO;

/*
 * 功能：成本费用分摊
 * 作者：zqy
 * 时间：2008-9-10 10:00:00
 */

public class ClientManageData implements IVOTreeDataByID {

    public String getIDFieldName() {
        return "pk_fytype";
    }

    public String getParentIDFieldName() {
        return null;
    }

    public String getShowFieldName() {
        return "typecode,typename";
    }

    public SuperVO[] getTreeVO() {
        SuperVO[] treeVOs  = null;
        
        nc.ui.trade.bsdelegate.BusinessDelegator business = new nc.ui.trade.bsdelegate.BDBusinessDelegator();
        try{
             treeVOs  = business.queryByCondition(FytypeVO.class, " isnull(dr,0)=0 order by typecode ");
        }catch(Exception e){
            e.printStackTrace();
        }
        return treeVOs;
    }

}


