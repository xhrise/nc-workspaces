
package com.ufsoft.report.plugin;

import com.ufida.zior.plugin.IPlugin;
import com.ufsoft.report.UfoReport;
import com.ufsoft.table.re.SheetCellEditor;
import com.ufsoft.table.re.SheetCellRenderer;



/**
 * ����ӿڡ����б����ߵĲ��ʵ�ָýӿڡ����ӿ����������ͻ�ͨ����꣬���̴�����һЩ�¼������磺���ƣ�ճ���ȡ�
 * ���⣬��������Լ�������һЩ�����¼�����չ���Ĺ��ܡ� ����ӿڱ�ʶ����ģ���޸ĺ�ִ�еĲ���������û��ispermit�ķ����� ������ѡ�ӿ����£�
 * <ol>
 * <li>SelectListener ��Ӧѡ�����ݵı仯
 * <li>CellsModelListener ��Ӧ��Ԫ�����ݻ��߸�ʽ�ı仯
 * <li>HeaderModelListener ��Ӧ�������Եı仯
 * <li>TreeModelListener ��Ӧ��ģ�͵ı仯
 * </ol>
 * 
 * @author wupeng 2004-7-30
 */
public interface IPlugIn extends IPlugin {// extends UserActionListner {
	/**
	 * ��������� �����ʼ���ɹ��󣬵��ô˷������������
	 */
	public void startup();

	/**
	 * ����رա� ����ر�ǰ���ô˷�����
	 */
	public void shutdown();

	/**
	 * �����Ϣ�洢�� ������ñ��淽��ʱ�����ô˷������˷������ִ�гɹ���ʵ����Ӧ�ý����������
	 */
	public void store();

	/**
	 * �õ����������Ϣ��
	 * 
	 * @return com.ufsoft.report.plugin.IPluginDescriptor
	 */
	public IPluginDescriptor getDescriptor();

	/**
	 * ���ñ����ߵľ�����÷������ڵ���startup()֮ǰִ�С�
	 * 
	 * @param report
	 */
	public void setReport(UfoReport report);

	public UfoReport getReport();

	/**
	 * �������Ϣ�Ƿ��޸ġ�
	 * 
	 * @return boolean ���������Ϊtrue,��UfoReport���ñ����ʱ������store()��
	 */
	public boolean isDirty();

	/**
	 * �õ���Ҫ֧���µı༭������Ⱦ�����������͡�
	 * 
	 * @return String[]
	 */
//	public String[] getSupportData();
//
//	/**
//	 * �õ���Ⱦ��
//	 * 
//	 * @param extFmtName
//	 * @return SheetCellRenderer
//	 */
//	public SheetCellRenderer getDataRender(String extFmtName);
//
//	/**
//	 * �õ��༭��
//	 * 
//	 * @param extFmtName
//	 * @return SheetCellEditor
//	 */
//	public SheetCellEditor getDataEditor(String extFmtName);

	/**
	 * ���һ��������
	 * 
	 * @param lis
	 *            IPlugInListener
	 * @throws NullPointerException
	 *             �������Ϊ���׳�.
	 */
	public void addListener(IPlugInListener lis);

	/**
	 * ɾ��һ����������
	 * 
	 * @param lis
	 *            ��ɾ���ļ�������
	 */
	public void removeListener(IPlugInListener lis);

	/**
	 * ֪ͨ����ע���˵ļ�����ִ����Ӧ��
	 * 
	 * @param e
	 */
	public void notifyListener(PlugEvent e);

}