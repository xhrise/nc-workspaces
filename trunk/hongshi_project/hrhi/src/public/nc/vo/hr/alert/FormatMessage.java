package nc.vo.hr.alert;

import nc.bs.pub.pa.html.IAlertMessage;

/**
 * 缺省的格式化消息类，它实现 IAlertMessage 接口，用来给业务对象返回格式化对象之用... 创建日期：(01-5-31 21:24:35)
 * 
 * @author: 赵世春
 */
public class FormatMessage implements IAlertMessage {

	/** 业务插件返回信息的标题 */
	private java.lang.String title;

	/** 业务插件返回信息的描述 */
	private java.lang.String[] bottoms;

	/** 业务插件返回信息的描述 */
	private java.lang.String[] tops;

	/** 业务插件返回信息的字段数组 */
	private String[] fields;

	/** 业务插件返回信息的字段数组 */
	private float[] widths;

	/** 业务插件返回信息的对应字段的内容 */
	private java.lang.String[][] values;

	/**
	 * DefaultFormatMessage 构造子注解。
	 */
	public FormatMessage() {
		super();
	}

	/**
	 * FormatMessage 构造子注解。
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
	 * 此处插入方法说明。 创建日期：(2001-9-20 11:22:42)
	 * 
	 * @return java.lang.String[]
	 */
	public String[] getBodyFields() {
		return fields;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-20 11:25:56)
	 * 
	 * @return java.lang.Object[][]
	 */
	public Object[][] getBodyValue() {
		return values;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-20 11:26:33)
	 * 
	 * @return float[]
	 */
	public float[] getBodyWidths() {
		return widths;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-20 11:34:32)
	 * 
	 * @return java.lang.String[]
	 */
	public String[] getBottom() {
		return bottoms;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-20 11:28:01)
	 * 
	 * @return java.lang.String
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-20 11:27:16)
	 * 
	 * @return java.lang.String[]
	 */
	public String[] getTop() {
		return tops;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-20 11:22:42)
	 * 
	 * @return java.lang.String[]
	 */
	public void setBodyFields(String[] fields) {
		this.fields = fields;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-20 11:25:56)
	 * 
	 * @return java.lang.Object[][]
	 */
	public void setBodyValue(String[][] values) {
		this.values = values;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-20 11:26:33)
	 * 
	 * @return float[]
	 */
	public void setBodyWidths(float[] widths) {
		this.widths = widths;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-20 11:34:32)
	 * 
	 * @return java.lang.String[]
	 */
	public void setBottom(String[] bottom) {
		bottoms = bottom;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-20 11:28:01)
	 * 
	 * @return java.lang.String
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-20 11:27:16)
	 * 
	 * @return java.lang.String[]
	 */
	public void setTop(String[] top) {
		tops = top;
	}
}
