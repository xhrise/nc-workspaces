
package nc.ui.eh.iso.z0502005;

import java.util.Hashtable;

import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.vo.eh.iso.z0502005.StockCheckreportBVO;
import nc.vo.eh.iso.z0502005.StockCheckreportCVO;
import nc.vo.pub.SuperVO;

public class ClientBaseBD extends BusinessDelegator {
   

	public ClientBaseBD() {
		super();
	}
    
	@Override
	public Hashtable loadChildDataAry(String[] tableCodes, String key)
		throws java.lang.Exception {
		Hashtable ht=new Hashtable();
		SuperVO[] vos=null;
		int len=tableCodes.length;
		if (len==0)
			return null;	
        String whereSql = "pk_checkreport='"+key+"'";
		for(int i=0;i<len;i++){
			if ("eh_stock_checkreport".equals(tableCodes[i]))
				vos=queryByCondition(StockCheckreportBVO.class, whereSql);
			else if ("eh_stock_checkreport_c".equals(tableCodes[i]))
				vos=queryByCondition(StockCheckreportCVO.class, whereSql);
			if (vos!=null)
			    ht.put(tableCodes[i],vos);
		}
		return ht;
	}
}
