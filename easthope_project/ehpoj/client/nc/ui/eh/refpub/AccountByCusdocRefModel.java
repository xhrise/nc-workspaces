/*
 * Created on 2006-9-4
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package nc.ui.eh.refpub;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.bd.ref.AbstractRefModel;
import nc.vo.pub.BusinessException;

/**
 * 功能：根据客商得到银行账户信息(付款单用)
 * @author 张志远
 * @date 2009-12-25 16:52:00
 */
public class AccountByCusdocRefModel extends AbstractRefModel {
	public static String pk_cubasdoc;
	public static String pk_corp;
	public AccountByCusdocRefModel() {
		super();
	}
	
	public int getDefaultFieldCount() {
	        return 3;
	}
	    
	public String[] getFieldCode() {
		return new String[]{
              "account","accountname","accountcode","remcode","banktypename","unitname", "pk_bankaccbas"
        };
	}

	public String[] getFieldName() {
		return new String[]{
                "账号","账户名称","账户编码","助记码","银行类别","单位名称","主键"
			};
	}

	public String getRefTitle() {
		return "客商银行账户";
	}

	public String getTableName() {
		return "eh_view_account";
	}

	public String getPkFieldCode() {
		return "pk_bankaccbas";
	}
	/**
	 * 功能：根据PK_CORP判断该各户是否为内部客商
	 */
	@SuppressWarnings("unchecked")
	public String getWherePart() {
		StringBuffer sql = new StringBuffer();
		StringBuffer issql = new StringBuffer()
		.append(" select b.pk_corp1 pk_corp from bd_cumandoc a,bd_cubasdoc b,bd_areacl c")
		.append(" where  a.pk_cubasdoc=b.pk_cubasdoc")
		.append(" and a.pk_cumandoc='"+pk_cubasdoc+"'")
		.append(" and b.pk_areacl=c.pk_areacl")
		.append(" and c.areaclcode='099'");
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		try {
			ArrayList shlist=(ArrayList) iUAPQueryBS.executeQuery(issql.toString(), new MapListProcessor());
			if (shlist.size()>0)
			{
				HashMap hs = (HashMap) shlist.get(0);
				String ownercorp = hs.get("pk_corp")==null?"":hs.get("pk_corp").toString();
		    	sql.append(" ownercorp='"+ownercorp+"'");
			}
			else
			{
			   sql.append(" pk_bankaccbas in")
			   .append(" (")
			   .append(" SELECT m.pk_accbank FROM BD_CUSTBANK m,BD_CUMANDOC n")
			   .append(" where m.pk_cubasdoc = n.pk_cubasdoc ")
			   .append(" and n.pk_cumandoc like '"+pk_cubasdoc+"'")
			   .append(" and nvl(m.dr, 0) = 0 and nvl(n.dr, 0) = 0")
			   .append(" )");	    	
			}
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return sql.toString();
    }

}
