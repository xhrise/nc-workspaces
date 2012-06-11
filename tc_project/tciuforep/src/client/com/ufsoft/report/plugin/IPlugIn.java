
package com.ufsoft.report.plugin;

import com.ufida.zior.plugin.IPlugin;
import com.ufsoft.report.UfoReport;
import com.ufsoft.table.re.SheetCellEditor;
import com.ufsoft.table.re.SheetCellRenderer;



/**
 * 插件接口。所有报表工具的插件实现该接口。父接口用来描述客户通过鼠标，键盘触发的一些事件，例如：复制，粘贴等。
 * 此外，插件还何以监听表格的一些基本事件来扩展表格的功能。 此类接口标识数据模型修改后执行的操作，所以没有ispermit的方法。 其他可选接口如下：
 * <ol>
 * <li>SelectListener 响应选择类容的变化
 * <li>CellsModelListener 响应单元的数据或者格式的变化
 * <li>HeaderModelListener 响应行列属性的变化
 * <li>TreeModelListener 响应树模型的变化
 * </ol>
 * 
 * @author wupeng 2004-7-30
 */
public interface IPlugIn extends IPlugin {// extends UserActionListner {
	/**
	 * 插件启动。 报表初始化成功后，调用此方法启动插件。
	 */
	public void startup();

	/**
	 * 插件关闭。 报表关闭前调用此方法。
	 */
	public void shutdown();

	/**
	 * 插件信息存储。 报表调用保存方法时，调用此方法。此方法如果执行成功，实现者应该将脏标记清除。
	 */
	public void store();

	/**
	 * 得到插件描述信息。
	 * 
	 * @return com.ufsoft.report.plugin.IPluginDescriptor
	 */
	public IPluginDescriptor getDescriptor();

	/**
	 * 设置报表工具的句柄。该方法将在调用startup()之前执行。
	 * 
	 * @param report
	 */
	public void setReport(UfoReport report);

	public UfoReport getReport();

	/**
	 * 插件中信息是否被修改。
	 * 
	 * @return boolean 如果返回置为true,在UfoReport调用保存的时候会调用store()。
	 */
	public boolean isDirty();

	/**
	 * 得到需要支持新的编辑器和渲染器的数据类型。
	 * 
	 * @return String[]
	 */
//	public String[] getSupportData();
//
//	/**
//	 * 得到渲染器
//	 * 
//	 * @param extFmtName
//	 * @return SheetCellRenderer
//	 */
//	public SheetCellRenderer getDataRender(String extFmtName);
//
//	/**
//	 * 得到编辑器
//	 * 
//	 * @param extFmtName
//	 * @return SheetCellEditor
//	 */
//	public SheetCellEditor getDataEditor(String extFmtName);

	/**
	 * 添加一个监听器
	 * 
	 * @param lis
	 *            IPlugInListener
	 * @throws NullPointerException
	 *             输入参数为空抛出.
	 */
	public void addListener(IPlugInListener lis);

	/**
	 * 删除一个监听器。
	 * 
	 * @param lis
	 *            被删除的监听器。
	 */
	public void removeListener(IPlugInListener lis);

	/**
	 * 通知所有注册了的监听器执行响应。
	 * 
	 * @param e
	 */
	public void notifyListener(PlugEvent e);

}