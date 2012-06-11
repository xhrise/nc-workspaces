/*
 * �������� 2006-3-21
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
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
 * ��ѯ���Applet����Action��
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
