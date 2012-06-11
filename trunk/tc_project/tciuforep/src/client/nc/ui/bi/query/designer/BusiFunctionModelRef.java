package nc.ui.bi.query.designer;

import nc.ui.bd.ref.AbstractRefModel;
import com.ufsoft.iufo.resource.StringResource;

/**
 * ҵ�������� �������ڣ�(02-2-27 15:02:28)
 * 
 * @author���쿡��
 */
public class BusiFunctionModelRef extends AbstractRefModel {
	/**
	 * DicTableRef ������ע�⡣
	 */
	public BusiFunctionModelRef() {
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
		return new String[] { "classname", "funcname" };
	}

	/**
	 * ��ʾ�ֶ������� �������ڣ�(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 * @i18n miufo00378=����
	 * @i18n miufo1001051=����
	 */
	public java.lang.String[] getFieldName() {
		return new String[] { StringResource.getStringResource("miufo00378"), StringResource.getStringResource("miufo1001051") };
	}

	/**
	 * �����ֶ���
	 * 
	 * @return java.lang.String
	 */
	public String getPkFieldCode() {
		//return "pk_busifunc";
		return "classname";
	}

	/**
	 * ���ձ��� �������ڣ�(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 * @i18n miufo00379=ҵ��������
	 */
	public String getRefTitle() {
		return StringResource.getStringResource("miufo00379");
	}

	/**
	 * �������ݿ�������ͼ�� �������ڣ�(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 */
	public String getTableName() {
		return "bi_busifunc";
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
 