package com.ufsoft.iufo.inputplugin.biz;

import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.inputplugin.biz.file.ChangeKeywordsData;
import com.ufsoft.iufo.inputplugin.biz.file.ChooseCordExt;
import com.ufsoft.iufo.inputplugin.biz.file.ChooseRepExt2;
import com.ufsoft.iufo.inputplugin.biz.file.SwitchTreeView;
import com.ufsoft.iuforeport.tableinput.applet.IRepDataParam;
import com.ufsoft.iuforeport.tableinput.applet.TableInputContextVO;
import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.INavigationExt;
import com.ufsoft.report.plugin.IPluginDescriptor;

/**
 * 报表选择插件(报表数据切换)
 * chxw
 * 2007-09-05
 */
public class RepSelectionPlugIn extends AbstractPlugIn implements IUfoContextKey{

	@Override
	protected IPluginDescriptor createDescriptor() {
		return new AbstractPlugDes(this){
			protected IExtension[] createExtensions() {
				//打开任务是否包含单位关键字，不包含单位关键字则按报表组织
				boolean hasCorpKeyInTask = hasCorpKeyInTask();
				if(!hasCorpKeyInTask){
					TableInputContextVO inputContextVO = (TableInputContextVO)this.getReport().getContextVo();
			    	inputContextVO.setAttribute(SHOW_REP_TREE, true);
				}
				
				INavigationExt chooseReport2 = new ChooseRepExt2(getReport());
				INavigationExt chooseCordExt = new ChooseCordExt(getReport());
				if(hasCorpKeyInTask){
					IExtension switchTreeView = new SwitchTreeView(getReport());
					return new IExtension[]{chooseReport2, chooseCordExt, switchTreeView};
				} else{
					return new IExtension[]{chooseReport2, chooseCordExt};
				}
				
			}
		};
	}

	/**
	 * 检查当前任务是否包含单位关键字
	 * @return
	 */
	private boolean hasCorpKeyInTask(){
		ChangeKeywordsData[] changeKeywordsDatas = ChooseCordExt.doGetKeywordsDatas(getReport());
		if(changeKeywordsDatas == null)
			return false;
		
		for(ChangeKeywordsData changeKeywordsData : changeKeywordsDatas){
			if(changeKeywordsData.isCorpKey()){
				return true;
			}
		}
		return false;
		
	}
	
	public void startup(){
//		IRepDataParam param = InputBizOper.doGetTransObj(getReport()).getRepDataParam();
//		String strAlondID = null;
//		if (param != null){
//			strAlondID = param.getAloneID();
////			int nBizType = ITableInputMenuType.MENU_TYPE_OPEN;
////			InputOpenRepOper inputOpenRepOper = new InputOpenRepOper(getReport());
////			inputOpenRepOper.performBizTask(nBizType);
//		}
////		if(strAlondID == null){
////			ChangeKeywordsExt changeKeywordsExt = new ChangeKeywordsExt(getReport());
////			Object[] paramsPre = changeKeywordsExt.getParams(getReport());
////			if(paramsPre != null){
////				changeKeywordsExt.getCommand().execute(paramsPre);
////			}
////		}
	}

}
