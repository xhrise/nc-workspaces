package com.ufsoft.iufo.inputplugin.biz.formulatrace;

import com.ufsoft.iuforeport.tableinput.applet.FormulaTraceValueItem;
import com.ufsoft.iuforeport.tableinput.applet.IFormulaParsedDataItem;
import com.ufsoft.iuforeport.tableinput.applet.IFormulaTraceValueItem;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.util.UfoPublic;
/**
 * 需向服务器端发出请求的公式追踪业务接口实现类 's Mock
 * @author liulp
 *
 */
public class FormulaTraceBizLinkMock implements IFormulaTraceBizLink{
	private static IFormulaTraceBizLink s_oFormulaTraceBizLink = new FormulaTraceBizLinkMock();
	private FormulaTraceBizLinkMock(){		
	} 
	public static IFormulaTraceBizLink getInstance(){
		return s_oFormulaTraceBizLink;
	}
	public Object getCalulatedValue(UfoReport ufoReport, IFormulaParsedDataItem formulaParsedDataItem) {
		return "ValueFromLinkMock";
	}
	public IFormulaTraceValueItem[] getTraceMultiValues(UfoReport ufoReport, IFormulaParsedDataItem formulaParsedDataItem) {
		int nSize = 3;
		return getMultiValuesMock(nSize,true);
	}
	protected static IFormulaTraceValueItem[] getMultiValuesMock(int nSize,boolean bFromClient) {
		IFormulaTraceValueItem[] valueItems = new FormulaTraceValueItem[nSize];
		String[] strKeyNames = {"strKeyName0","strKeyName1","strKeyName2"};
		String[] strKeyValues = {"strKeyValue0","strKeyValue1","strKeyValue2"};
		String strPre = "Row";
		if(!bFromClient){
			strPre += "Srv";
		}
		for(int i = 0; i < nSize; i++){
			valueItems[i] = new FormulaTraceValueItem();
			valueItems[i].setKeyNames(new String[]{"Row " +i + strKeyNames[0],strKeyNames[1],strKeyNames[2]});
			valueItems[i].setKeyValues(new String[]{"Row " +i + strKeyValues[0],strKeyValues[1],strKeyValues[2]});
			valueItems[i].setValue("Row " +i + "objValue");
		}
		return valueItems;
	}
	public void doTraceValue(UfoReport ufoReport, IFormulaTraceValueItem formulaTraceValueItem) {
		//追踪到某任务中的目标报表，如果当前用户有数据权限则打开，不过没有则提示无数据权限
		UfoPublic.sendWarningMessage("waitring for trace single values' implementation", ufoReport);
		return;
	}
}
