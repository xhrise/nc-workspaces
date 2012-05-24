
package nc.ui.eh.trade.z00101;

import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.pub.IVOTreeDataByID;
import nc.vo.pub.SuperVO;
/**
 * 功能：片区管理
 * @author 张起源
 * 日期：2008-3-25
 */
public class ClientManageData implements IVOTreeDataByID {	

    public String getIDFieldName() {
		return "pk_areacl";
	}

	public String getParentIDFieldName() {
		return "fatherpk";
	}

	public String getShowFieldName() {
		return "areacode,areaname";
	}

	public SuperVO[] getTreeVO() {
        SuperVO[] treeVOs  = null;
        ClientEnvironment ce = ClientEnvironment.getInstance();
        nc.ui.trade.bsdelegate.BusinessDelegator business = new nc.ui.trade.bsdelegate.BDBusinessDelegator();
        try{
             treeVOs  = business.queryByCondition(nc.vo.eh.trade.z00101.AreaclVO.class, " pk_corp = '"+ce.getCorporation().getPk_corp()+"' and isnull(dr,0)=0 order by areacode ");
        }catch(Exception e){
            e.printStackTrace();
        }
        return treeVOs;
    }

}


