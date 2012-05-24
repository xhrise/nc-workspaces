/**
 * @(#)TradeclassRefModel.java	V3.1 2007-8-29
 * 
 * Copyright 1988-2005 UFIDA, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of UFSoft, Inc.
 * Use is subject to license terms.
 *
 */
package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;

public class CustAreaclRefModel extends AbstractRefModel {
	/**
	 * 默认显示字段中的显示字段数----表示显示前几个字段
	 */
	@Override
	public int getDefaultFieldCount() {
		return 2;
	}

    @Override
	public String[] getFieldCode() {
        // TODO Auto-generated method stub
        return new String[] {"custcode", "custname","pk_cumandoc","pk_areacl"};
    }

    @Override
	public String[] getFieldName() {
        // TODO Auto-generated method stub
    	return new String[] {"客商编码", "客商名称","客商主键","片区主键"};
    }

    @Override
	public String getPkFieldCode() {
        // TODO Auto-generated method stub
        return "pk_cumandoc";
    }

    @Override
	public String getRefTitle() {
        // TODO Auto-generated method stub
        return "片区客商档案";
    }

    @Override
	public String getTableName() {
        // TODO Auto-generated method stub
    	StringBuilder sb = new StringBuilder()
    	.append(" (select c.pk_areacl,b.pk_cumandoc,a.custcode,a.custname from bd_cubasdoc a,bd_cumandoc b,eh_areacl c")
    	.append(" where a.pk_cubasdoc=b.pk_cubasdoc and nvl(a.dr,0)=0 and nvl(b.dr,0)=0")
    	.append(" and b.def5 =c.pk_areacl and nvl(c.dr,0)=0 and b.pk_corp=c.pk_corp")
    	.append(" and b.pk_corp='"+getPk_corp()+"')");
        return sb.toString();
    }    
}

