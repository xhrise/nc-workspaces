package com.ufsoft.report.util;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import nc.ui.pub.beans.util.NCOptionPane;

import com.ufsoft.report.component.ProcessController;
import com.ufsoft.report.dialog.ErrorDialog;
import com.ufsoft.report.exception.MessageException;
/**
 * ��������ʹ�õĹ����ࡣ
 * @author wupeng
 * 2004-8-11
 */
public class UfoPublic {
	private static final String m_strError = MultiLang.getString("miufo00082");
	private static final String m_strWarning = MultiLang.getString("miufo1000925");
	private static final String m_strMessage = MultiLang.getString("miufopublic384");

	/**
	 * Public ������ע�⡣
	 */
	private UfoPublic() {
	}	
	
	static public void sendStatusHintMessage(String strMessage){
		
	}
	
	static public void setStatusProcessDisplay(ProcessController controller){
		controller.showProcessBar(null);
	}
	
	/**
	 * ��ʾ��ͨ��Ϣ��
	 * @param strMessage
	 * @param parent
	 */
	static public void sendMessage(String strMessage, Component parent) {
	    NCOptionPane.showMessageDialog(parent, strMessage, m_strMessage,JOptionPane.INFORMATION_MESSAGE);
	}
	
	static public void sendMessage(com.ufida.zior.exception.MessageException e, Component parent) {
	    NCOptionPane.showMessageDialog(parent, e.getMessage(), "",e.getType());
	} 
	
	static public void sendMessage(Throwable e, Component parent) {
		if(e instanceof MessageException || e instanceof com.ufida.zior.exception.MessageException){
			sendMessage((com.ufida.zior.exception.MessageException) e, parent);
			return;
		}
	    sendErrorMessage(e.getMessage(), parent, e);
	} 
	
	/**
	 * ִ�й��������û���ʾ������Ϣ��Ҫ���û���Ԥ��
	 * @param strMessage
	 * @param parent
	 */
	static public void sendWarningMessage(String strMessage, Component parent) {
	    NCOptionPane.showMessageDialog(parent, strMessage,m_strWarning,NCOptionPane.WARNING_MESSAGE);
	}

	
	/**
	 * ��ʾ������Ϣ
	 * @param strMessage ������Ϣ����
	 * @param parent ������
	 * @param t �쳣��Ϣ������ӡ���쳣�Ķ�ջ��Ϣ��
	 * @i18n miufo00082=������ʾ
	 */
	static public void sendErrorMessage(String strMessage, Component parent,Throwable t) {
		if(t == null){
			NCOptionPane.showMessageDialog(parent, strMessage, m_strError, NCOptionPane.ERROR_MESSAGE);
			return;
		}
		ErrorDialog errorDia = new ErrorDialog(parent);
		errorDia.setErrorMessage(strMessage);
		errorDia.setError(t);
		errorDia.setLocationRelativeTo(parent);
		errorDia.pack();
		errorDia.setVisible(true);
		Dimension dim = errorDia.getPreferredSize();
		if(dim.getWidth() < 320){
			dim.setSize(320, dim.getHeight());
		}
		errorDia.setPreferredSize(dim);
		
	}
	/**
	 * ���ö��ŵĸ�ʽ 
	 * @param strData
	 * @return String
	 */
	static public String setCommaFormat(String strData) {
		if (strData == null)
			return null;

		//���ö��ŵĸ�ʽ

		int pointPos = strData.indexOf('.');
		StringBuffer sb = new StringBuffer();
		char cs[] = strData.toCharArray();
		int curpos = cs.length - 1;
		int commapos = 0;
		while (curpos >= 0) {
			sb.insert(0, cs[curpos]);
			if (curpos < pointPos) {
				commapos++;
				if (commapos == 3 && curpos > 0) {
					if (!(curpos == 1 && (cs[0] == '-' || cs[0] == '+'))) {
						sb.insert(0, ',');
					}
					commapos = 0;
				}
			}
			curpos--;
		}
		return sb.toString();
	}
	
	
	/**
	 * ����ֵ�ο�JOptionPane.showConfirmDialog(parentComponent, message)��
	 * @param parentComponent
	 * @param message
	 * @return int
	 */
	public static int showConfirmDialog(Component parentComponent,Object message){
		return NCOptionPane.showConfirmDialog(parentComponent, message);
	}
	/**
	 * ����ֵ�ο�JOptionPane.showConfirmDialog(parentComponent,message, title, optionType)
	 * @param parentComponent
	 * @param message
	 * @param title
	 * @param optionType
	 * @return int
	 */
	public static int showConfirmDialog(Component parentComponent,
	        Object message, String title, int optionType){
	    return NCOptionPane.showConfirmDialog(parentComponent,message, title, optionType);
	}
	/**
	 * ����������Ϣ��
	 * @param parentComponent
	 * @param message
	 * @param title
	 * @return
	 */
	public static void showErrorDialog(Component parentComponent,Object message, String title){
		if(title==null||title.equals("")){
			title=m_strError;
		}
		NCOptionPane.showMessageDialog(parentComponent,message, title, JOptionPane.ERROR_MESSAGE);
	}
	/**
	 * ��Vectorת��ΪArrayList
	 * ���VectorΪnull,�򷵻�null;
	 * caijie  2004-12-29
	 * @param vec
	 * @return
	 */
	public static ArrayList vectorToList(Vector vec){
	    if(vec == null) return null;
	    ArrayList result = new ArrayList(vec.size());
	    int size =  vec.size();
	    for(int i = 0; i <size; i++){
	        result.add(i, vec.get(i));
	    }
	    return result;
	}
	/**
	 * ��ArrayListת��ΪVector
	 * ���ArrayListΪnull,�򷵻�null;
	 * caijie  2004-12-29
	 * @param list
	 * @return
	 */
	public static Vector listToVector(ArrayList list){
	    if(list == null) return null;
	    Vector result = new Vector(list.size());
	    int size =  list.size();
	    for(int i = 0; i <size; i++){
	        result.add(i, list.get(i));
	    }
	    return result;
	}
//	/**
//	 * �˴����뷽��˵����
//	 * �������ڣ�(2001-4-4 15:12:43)
//	 */
////	���ý������ٷ���
//	static public void setPercentBar(int nPercent)
//	{
//		//FIXME
////		IUfoContainer mainFrame = UfoPublic.getUfoContainer();
////		if(mainFrame != null)
////			mainFrame.getStatusPanel().setPercent(nPercent);
//	}
//	/**
//	 * �˴����뷽��˵����
//	 * �������ڣ�(2001-4-4 15:12:43)
//	 */
////	���ý������ı����ַ�
//	static public void setPercentBarText(String strText)
//	{
//		//FIXME
////		IUfoContainer mainFrame = UfoPublic.getUfoContainer();
////		if(mainFrame != null)
////			mainFrame.getStatusPanel().setPercentText(strText);
//	}
} 