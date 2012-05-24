
package nc.ui.eh.cwitf.h10106;

import nc.ui.trade.pub.IVOTreeDataByID;
import nc.vo.eh.cwitf.h10105.ItfBilltypeVO;
import nc.vo.pub.SuperVO;

/**
 * ���ܣ�ƾ֤ģ�嶨��
 * @author ����Դ
 * ���ڣ�2008-7-10 15:28:46
 */

public class ClientManageData implements IVOTreeDataByID {

    public String getIDFieldName() {
        return "pk_billtype";
    }

    public String getParentIDFieldName() {
        return null;
    }

    public String getShowFieldName() {
        return "billtypecode,billtypename";
    }

    public SuperVO[] getTreeVO() {
        SuperVO[] treeVOs  = null;
        
        nc.ui.trade.bsdelegate.BusinessDelegator business = new nc.ui.trade.bsdelegate.BDBusinessDelegator();
        try{
             treeVOs  = business.queryByCondition(ItfBilltypeVO.class, " isnull(dr,0)=0 order by billtypecode ");
        }catch(Exception e){
            e.printStackTrace();
        }
        return treeVOs;
    }

}


