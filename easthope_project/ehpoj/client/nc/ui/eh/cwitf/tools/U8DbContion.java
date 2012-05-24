package nc.ui.eh.cwitf.tools;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;

public class U8DbContion {

	public static String getU8DBname(){
		String bdname = null;
		String sql = "select dataname from eh_itf_datasource where isnull(dr,0)=0";
		IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
    	try {
    		bdname = iUAPQueryBS.executeQuery(sql, new ColumnProcessor())==null?null:iUAPQueryBS.executeQuery(sql, new ColumnProcessor()).toString();
    		bdname = bdname+".dbo.";
    	} catch (Exception e) {
			e.printStackTrace();
		}
		return bdname;
	}
}
