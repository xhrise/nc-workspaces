/*
 * 创建日期 2006-6-28
 */
package com.ufida.report.adhoc.applet;

import java.util.ArrayList;

import com.ufida.report.adhoc.calc.AdhocFuncDriver;
import com.ufida.report.adhoc.model.IFunctionFieldElement;
import com.ufida.report.rep.model.BIFuncInfo;
import com.ufida.report.rep.model.ICalcElements;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.util.parser.UfoSimpleObject;
import com.ufsoft.script.function.UfoFuncInfo;

/**
 * 组头、表头公式中的元素描述
 * @author ljhua
 */
public class AdhocFormulaElement implements ICalcElements {
	private static final String[] ALL_OPERATOR={"+","-","*","/","And","Or","Like","=","<","<=",">",">=","<>" };
	

	private IFunctionFieldElement[] m_fields=null;
	/**
	 * 
	 */
	public AdhocFormulaElement(IFunctionFieldElement[] fields) {
		super();
		m_fields=fields;
	}

	/* （非 Javadoc）
	 * @see com.ufida.report.rep.model.ICalcElements#getElements()
	 */
	public String[][] getElements() {
		if(m_fields!=null && m_fields.length>0){
			String[][] strReturn=new String[m_fields.length][2];
			StringBuffer  buf1=null;
			for(int i=0,size=m_fields.length;i<size;i++){
				buf1=new StringBuffer();
				buf1.append("['");
				buf1.append(m_fields[i].getName());
				buf1.append("']");
				strReturn[i][0]=m_fields[i].getName();
				strReturn[i][1]=buf1.toString();
			}
			return strReturn;
		}
		return null;
	}

	/* （非 Javadoc）
	 * @see com.ufida.report.rep.model.ICalcElements#getFuncCatalogs()
	 */
	public UfoSimpleObject[] getFuncCatalogs() {
		String[] strNames=AdhocFuncDriver.CATNAMES;
		
		UfoSimpleObject[] objs=new UfoSimpleObject[strNames.length];
		for(int i=0,size=objs.length;i<size;i++){
			objs[i]=new UfoSimpleObject(i+1,StringResource.getStringResource(strNames[i]));
		}
		return objs;
	}

	/* （非 Javadoc）
	 * @see com.ufida.report.rep.model.ICalcElements#getFuncInfos(int)
	 */
	public BIFuncInfo[] getFuncInfos(int iCatalogid) {
		UfoFuncInfo[] funcInfos=AdhocFuncDriver.FUNCLIST;
		ArrayList<UfoFuncInfo> list =new ArrayList<UfoFuncInfo>();
		
		for(int i=0,size=funcInfos.length;i<size;i++){
			if(funcInfos[i].m_ftype==iCatalogid){
				list.add(funcInfos[i]);
			}
		}
		
		BIFuncInfo[] funcReturn=new BIFuncInfo[list.size()];
		UfoFuncInfo temp=null;
		for(int i=0,size=list.size();i<size;i++){
			temp=(UfoFuncInfo) list.get(i);
			funcReturn[i]=new BIFuncInfo(temp.m_name,
					temp.m_ptypelist==null?0:temp.m_ptypelist.length,
					temp.m_description);
		}
		return funcReturn;
	}

	/* （非 Javadoc）
	 * @see com.ufida.report.rep.model.ICalcElements#getOperators()
	 */
	public String[] getOperators() {
		return ALL_OPERATOR;
	}

}
