package nc.ui.ehpta.hq010202;

import java.util.Hashtable;


/**
 *
 *�����ҵ�������
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
         *�÷������ڻ�ȡ��ѯ�������û���ȱʡʵ���п��ԶԸ÷��������޸ġ�
         *
         */	
       public String getBodyCondition(Class bodyClass,String key){
		
       	 if(bodyClass == nc.vo.ehpta.hq010202.StorfareBVO.class)
	   return "pk_storfare = '" + key + "' and isnull(dr,0)=0 ";
       		
	 return null;
       } 
	
}
