/*
 * 创建日期 2006-8-25
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.ui.bi.integration.dimension;

import nc.itf.bi.exproperty.IBIExPropConstants;
import nc.itf.iufo.exproperty.IExPropConstants;
import nc.ui.iufo.exproperty.ExPropListAction;
import nc.ui.iufo.exproperty.ExPropMngAction;

import com.ufida.web.action.ActionForm;
import com.ufida.web.action.ActionForward;
import com.ufida.web.util.WebGlobalValue;
import com.ufsoft.iufo.web.MultiFrameAction;

public class DimensionMngAction extends MultiFrameAction {

	public ActionForward   toExPropMng(ActionForm   actionForm){
		//检查是否是引用维度
		ActionForward	fwd = DimUIToolKit.checkDimRefer(this);
		if( fwd == null ){
			String		strDimPKWithAuthFlag = getTableSelectedID();
			fwd = new ActionForward(ExPropMngAction.class.getName(), "execute");
			fwd.addParameter(IExPropConstants.PARAM_EXPROP_MODULEID,
	        		IBIExPropConstants.EXPROP_MODULE_DIMENSION);
			fwd.addParameter(WebGlobalValue.TABLE_SELECTED_ID, strDimPKWithAuthFlag);
			if( DimUIToolKit.hasModifyRight(strDimPKWithAuthFlag, getCurUserInfo()) == false){
				fwd.addParameter(ExPropMngAction.PARAM_READONLY, "true");
			}
			fwd.setRedirect(true);
		}
		return fwd;
	}
	public ActionForward   toExPropList(ActionForm   actionForm){
		//检查是否是引用维度
		ActionForward	fwd = DimUIToolKit.checkDimRefer(this);
		if( fwd == null ){
			fwd = new ActionForward(ExPropListAction.class.getName(),"execute");
			fwd.addParameter(IExPropConstants.PARAM_EXPROP_MODULEID,
        		IBIExPropConstants.EXPROP_MODULE_DIMENSION);
			fwd.addParameter(WebGlobalValue.TABLE_SELECTED_ID, getTableSelectedID());
			fwd.setRedirect(true);
		}
		return fwd;
	}
	

	@Override
	public String getFormName() {
		// TODO 自动生成方法存根
		return null;
	}

}
