package nc.vo.hr.alert;

import nc.bs.pub.pa.html.IAlertMessage;

/**
 * ȱʡ�ĸ�ʽ����Ϣ�࣬��ʵ�� IAlertMessage �ӿڣ�������ҵ����󷵻ظ�ʽ������֮��... �������ڣ�(01-5-31 21:24:35)
 * 
 * @author: ������
 */
public class FormatMessage implements IAlertMessage {

	/** ҵ����������Ϣ�ı��� */
	private java.lang.String title;

	/** ҵ����������Ϣ������ */
	private java.lang.String[] bottoms;

	/** ҵ����������Ϣ������ */
	private java.lang.String[] tops;

	/** ҵ����������Ϣ���ֶ����� */
	private String[] fields;

	/** ҵ����������Ϣ���ֶ����� */
	private float[] widths;

	/** ҵ����������Ϣ�Ķ�Ӧ�ֶε����� */
	private java.lang.String[][] values;

	/**
	 * DefaultFormatMessage ������ע�⡣
	 */
	public FormatMessage() {
		super();
	}

	/**
	 * FormatMessage ������ע�⡣
	 */
	public FormatMessage(String title, String[] top, String[] fields,
			String[][] value, float[] widths, String[] bottom) {
		super();
		setTitle(title);
		setTop(top);
		setBodyFields(fields);
		setBodyValue(value);
		setBodyWidths(widths);
		setBottom(bottom);
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-9-20 11:22:42)
	 * 
	 * @return java.lang.String[]
	 */
	public String[] getBodyFields() {
		return fields;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-9-20 11:25:56)
	 * 
	 * @return java.lang.Object[][]
	 */
	public Object[][] getBodyValue() {
		return values;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-9-20 11:26:33)
	 * 
	 * @return float[]
	 */
	public float[] getBodyWidths() {
		return widths;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-9-20 11:34:32)
	 * 
	 * @return java.lang.String[]
	 */
	public String[] getBottom() {
		return bottoms;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-9-20 11:28:01)
	 * 
	 * @return java.lang.String
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-9-20 11:27:16)
	 * 
	 * @return java.lang.String[]
	 */
	public String[] getTop() {
		return tops;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-9-20 11:22:42)
	 * 
	 * @return java.lang.String[]
	 */
	public void setBodyFields(String[] fields) {
		this.fields = fields;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-9-20 11:25:56)
	 * 
	 * @return java.lang.Object[][]
	 */
	public void setBodyValue(String[][] values) {
		this.values = values;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-9-20 11:26:33)
	 * 
	 * @return float[]
	 */
	public void setBodyWidths(float[] widths) {
		this.widths = widths;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-9-20 11:34:32)
	 * 
	 * @return java.lang.String[]
	 */
	public void setBottom(String[] bottom) {
		bottoms = bottom;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-9-20 11:28:01)
	 * 
	 * @return java.lang.String
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-9-20 11:27:16)
	 * 
	 * @return java.lang.String[]
	 */
	public void setTop(String[] top) {
		tops = top;
	}
}
