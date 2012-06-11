/*
 * 创建日期 2006-3-21
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.ui.bi.query.designer;

import java.util.Hashtable;

import nc.ui.iufo.applet.AbsAppletAction;
import nc.ui.iufo.resmng.common.AuthUIBizHelper;
import nc.util.iufo.resmng.ResMngToolKit;
import nc.vo.bi.query.manager.BIQueryConst;
import nc.vo.iufo.pub.DataManageObjectIufo;

import com.ufsoft.iufo.resource.StringResource;

/**
 * @author zyjun
 *
 * 查询设计Applet启动Action类
 */
public class QueryDesignAppletAction extends AbsAppletAction {

	protected void setCurAppletParam(Hashtable<String, String> hashParams) {
		hashParams.put(BIQueryConst.APPLET_QUERY_ID, ResMngToolKit.getVOIDByTreeObjectID(getTableSelectedID()));
		hashParams.put(BIQueryConst.APPLET_DSNAME_DEF,DataManageObjectIufo.IUFO_DATASOURCE);
	}
	protected String getTitle() {
		return StringResource.getStringResource("ubiquery0126");
	}
	protected String getAppletName() {
		return "nc.ui.bi.query.designer.QueryDesignApplet";
	}
	protected  void checkRight(){
		AuthUIBizHelper.checkRight(getTableSelectedID(), this);
	}
}
