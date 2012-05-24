
package nc.ui.eh.sc.h0470515;

import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.pub.IVOTreeDataByID;
import nc.vo.eh.sc.h0470515.ScSbbasdocVO;
import nc.vo.pub.SuperVO;
/**
 * 功能：设备档案
 * ZB27
 * @author 王兵
 * 2009-1-9 15:28:11
 */
public class ClientManageData implements IVOTreeDataByID {

    public ClientManageData() {
        super();
    }
    
    
    public String getCodeFieldName() {
        return "name";
    }

    public String getCodeRule() {
        return "222";
    }
    
	public String getIDFieldName() {
		return "pk_sb";
	}

	public String getParentIDFieldName() {
		return "pk_father";
	}

	public String getShowFieldName() {
		return "name,code,typename";
	}

	public SuperVO[] getTreeVO() {
        SuperVO[] treeVOs  = null;
        ClientEnvironment ce = ClientEnvironment.getInstance();
        nc.ui.trade.bsdelegate.BusinessDelegator business = new nc.ui.trade.bsdelegate.BDBusinessDelegator();
        try{
             treeVOs  = business.queryByCondition(ScSbbasdocVO.class, " isnull(dr,0)=0 and pk_corp = '"+ce.getCorporation().getPk_corp()+"' order by code ");
        }catch(Exception e){
            e.printStackTrace();
        }
        return treeVOs;
    }


}


