/*
 * �������� 2006-8-25
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
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
		//����Ƿ�������ά��
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
		//����Ƿ�������ά��
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
		// TODO �Զ����ɷ������
		return null;
	}

}
