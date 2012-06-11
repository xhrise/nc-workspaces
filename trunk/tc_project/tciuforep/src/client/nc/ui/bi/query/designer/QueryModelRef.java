package nc.ui.bi.query.designer;

import nc.ui.bd.ref.AbstractRefModel;
import nc.vo.iufo.pub.IDatabaseNames;
import com.ufsoft.iufo.resource.StringResource;

/**
 * ��ѯģ�Ͳ��� �������ڣ�(02-2-27 15:02:28)
 * 
 * @author���쿡��
 */
public class QueryModelRef extends AbstractRefModel {
	/**
	 * DicTableRef ������ע�⡣
	 */
	public QueryModelRef() {
		super();
	}

	/**
	 * Ĭ����ʾ�ֶ��е���ʾ�ֶ���----��ʾ��ʾǰ�����ֶ�
	 */
	public int getDefaultFieldCount() {
		return 2;
	}

	/**
	 * ��ʾ�ֶ��б� �������ڣ�(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String[] getFieldCode() {
		return new String[] { "pk_querymodel", "queryname" };
	}

	/**
	 * ��ʾ�ֶ������� �������ڣ�(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 * @i18n miufo1001384=����
	 * @i18n miufo1003134=����
	 */
	public java.lang.String[] getFieldName() {
		return new String[] { StringResource.getStringResource("miufo1001384"), StringResource.getStringResource("miufo1003134") };
	}

	/**
	 * �����ֶ���
	 * 
	 * @return java.lang.String
	 */
	public String getPkFieldCode() {
		return "pk_querymodel";
	}

	/**
	 * ���ձ��� �������ڣ�(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 * @i18n miufo00304=��ѯ�������
	 */
	public String getRefTitle() {
		return StringResource.getStringResource("miufo00304");
	}

	/**
	 * �������ݿ�������ͼ�� �������ڣ�(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 */
	public String getTableName() {
		return IDatabaseNames.BI_QUERY;
	}

	/**
	 * ��ѯ���� �������ڣ�(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 */
	public String getWherePart() {
		return "1=1";
	}
}
 