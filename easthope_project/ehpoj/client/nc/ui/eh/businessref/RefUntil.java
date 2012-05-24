package nc.ui.eh.businessref;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.pub.PubTools;
import nc.vo.pub.BusinessException;

public class RefUntil {
	
	/**
	 * 执行列表公式 add by wb
	 * @param defxvalue
	 * @return
	 * @throws BusinessException
	 */
  public static String getRefname(String tablename,String refnamefield,String pk_reffield,String pk_refvalue) throws BusinessException {
		
	   String refname = "";
	   String dnamevalue = null;
	   String[] refnames = null;
	   
	   String sql="select "+refnamefield+" from "+tablename+" where "+pk_reffield+" in ("+pk_refvalue+") and isnull(dr,0)=0";
	   IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	   ArrayList list =  (ArrayList)iUAPQueryBS.executeQuery(sql.toString(),new MapListProcessor());
	    
		if (list == null || list.size() == 0)
			   return null;
		refnames = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			HashMap hm = (HashMap)list.get(i);
			dnamevalue = hm.get(refnamefield)==null?"":hm.get(refnamefield).toString();
			refnames[i] = dnamevalue;
		}
		refname = PubTools.combinArrayToString3(refnames);
		
	return refname;
	
	}

}
