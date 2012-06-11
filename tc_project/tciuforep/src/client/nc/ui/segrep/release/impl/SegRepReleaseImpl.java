/*
 * 创建日期 2006-9-25
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.ui.segrep.release.impl;

import com.ufsoft.iufo.resource.StringResource;

import nc.ui.bi.release.impl.BIRepReleaseImpl;

public class SegRepReleaseImpl extends BIRepReleaseImpl {
	public String getMailName(String[] strRepIDs) throws Exception {
		return StringResource.getStringResource("usrdef0003");
	}

	public String getMailTitles(String[] strRepIDs) throws Exception {
		return StringResource.getStringResource("usrdef0003");
	}
	public String getTypeID() {
		return "ubirep0016";//分部报告
	}

}
