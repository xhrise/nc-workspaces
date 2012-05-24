/**
 * @(#)CubgysRefModel.java	V3.1 2008-6-21
 * 
 * Copyright 1988-2005 UFIDA, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of UFSoft, Inc.
 * Use is subject to license terms.
 *
 */
package nc.ui.eh.businessref;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.eh.pub.PubTools;
import nc.ui.pub.ClientEnvironment;

/**
 * ���ܣ���Ӧ��
 * @author zqy
 * @date 2008-6-21 13:35:24
 */

public class CubMulityRefModel extends AbstractRefModel {
    ClientEnvironment ce = ClientEnvironment.getInstance();
    public CubMulityRefModel() {
        super();
    }
    
    @Override
	public int getDefaultFieldCount() {
            return 3;
    }
        
    
    @Override
	public String[] getFieldCode() {
        return new String[]{
        		"mnecode","custname","custcode","custshortname","taxpayerid","conaddr","phone2","pk_cubasdoc"
        };
    }

    @Override
	public String[] getFieldName() {
        return new String[]{
        		"������","��Ӧ������","��Ӧ�̱���","��Ӧ�̼��","��˰�˵ǼǺ�","ͨ�ŵ�ַ","�绰","����"
            };
    }

    
    @Override
	public String getRefTitle() {
        return "��Ӧ�̵���";
    }

    @Override
	public String getTableName() {
        return "bd_cubasdoc";
    }

    @Override
	public String getPkFieldCode() {
        return "pk_cubasdoc";
    }

    
    @Override
	public String getWherePart() {
        StringBuffer sql = new StringBuffer()
        .append(" isnull(dr,0)=0 and pk_corp='"+ce.getCorporation().getPk_corp()+"' and isnull(lock_flag,'N')='N' ");
        return sql.toString();
    }
    
    /**
	 * �ĵ�����ȡֵ add by wb
	 */
	@Override
	public String getPkValue() {
		String strValue = null;
//		Object objValue = getValue(getPkFieldCode());
		String[] pkvaluses = getPkValues();
//		if (objValue != null) {
//			strValue = objValue.toString();
//		}
	   if (pkvaluses != null&&pkvaluses.length>1){
		  strValue = PubTools.combinArrayToString2(pkvaluses);
       }else if(pkvaluses!=null && pkvaluses.length==1){
           strValue = pkvaluses[0];
       }

		return strValue;
	}

	    /**
	     * ͨ������������װsql add by wb
	     */
		public String getWherePartByFieldsAndValues(String[] fields, String[] values)
		    {
		        String wherePart = "";
		        StringBuffer sb = new StringBuffer();
		        if(fields != null)
		        {
		            int length = fields.length;
		            if(values==null) return null;
		            String pks = values[0];
		            for(int i = 0; i < length; i++)
		            {
		                if(pks.length()==20)
		                    sb.append(fields[i]).append(" = '").append(pks).append("'");
		                else
		                    sb.append(fields[i]).append(" in (").append(pks).append(")");
		                if(i != length - 1)
		                    sb.append(" or ");
		            }

		        } else
		        {
		            return null;
		        }
		        wherePart = sb.toString();
		        return wherePart;
		    }

}

