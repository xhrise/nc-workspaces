/*
 * �������� 2006-9-25
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
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
		return "ubirep0016";//�ֲ�����
	}

}
