package nc.ui.ehpta.hq010202;

import java.util.Hashtable;


/**
 *
 *抽象的业务代理类
 *
 * @author author
 * @version tempProject version
 */
public abstract class AbstractMyDelegator extends nc.ui.trade.bsdelegate.BusinessDelegator {

	public Hashtable loadChildDataAry(String[] tableCodes,String key) 
	                                                 throws Exception{
		
	   Hashtable dataHashTable = new Hashtable();
	                         
           nc.vo.ehpta.hq010202.StorfareBVO[] bodyVOs1 =
                       (nc.vo.ehpta.hq010202.StorfareBVO[])queryByCondition(nc.vo.ehpta.hq010202.StorfareBVO.class,
                                                    getBodyCondition(nc.vo.ehpta.hq010202.StorfareBVO.class,key));   
            if(bodyVOs1 != null && bodyVOs1.length > 0){
                          
              dataHashTable.put("ehpta_storfare_b",bodyVOs1);
            } 
	         
	   	   return dataHashTable;
		
	}
	
	
       /**
         *
         *该方法用于获取查询条件，用户在缺省实现中可以对该方法进行修改。
         *
         */	
       public String getBodyCondition(Class bodyClass,String key){
		
       	 if(bodyClass == nc.vo.ehpta.hq010202.StorfareBVO.class)
	   return "pk_storfare = '" + key + "' and isnull(dr,0)=0 ";
       		
	 return null;
       } 
	
}
