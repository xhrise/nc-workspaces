package nc.vo.hi.hi_301;

/**
 * �˴���������������
 * @author��Administrator
 * �������ڣ�(2002-12-27 11:18:27)
 * @version	
 * @see		
 * @since	
 * 
 * // �޸��� + �޸�����
 * // �޸�˵��
 * 
 */
public class HrJtMainVO extends nc.vo.pub.ValueObject {
	private HRMainVO mainvo;
	private nc.vo.hi.hi_401.PsnDataVO[] workexpvo;
	private nc.vo.hi.hi_401.PsnDataVO[] articlevo;
	private nc.vo.hi.hi_401.PsnDataVO[] taskvo;
/**
 * HrJtMainVO ������ע�⡣
 */
public HrJtMainVO() {
	super();
}
/**
 * �����Ĺ��ܡ���;�������Եĸ��ģ��Լ�����ִ��ǰ������״̬��
 * @param		����˵��
 * @return		����ֵ
 * @exception �쳣����
 * @see		��Ҫ�μ�����������
 * @since		�������һ���汾���˷�������ӽ���������ѡ��
 * 
 * 
 * 
 * @return nc.vo.hi.hi_401.PsnDataVO
 */
public nc.vo.hi.hi_401.PsnDataVO[] getArticlevo() {
	return articlevo;
}
/**
 * ������ֵ�������ʾ���ơ�
 * 
 * �������ڣ�(2001-2-15 14:18:08)
 * @return java.lang.String ������ֵ�������ʾ���ơ�
 */
public String getEntityName() {
	return null;
}
/**
 * �����Ĺ��ܡ���;�������Եĸ��ģ��Լ�����ִ��ǰ������״̬��
 * @param		����˵��
 * @return		����ֵ
 * @exception �쳣����
 * @see		��Ҫ�μ�����������
 * @since		�������һ���汾���˷�������ӽ���������ѡ��
 * 
 * 
 * 
 * @return nc.vo.hi.hi_301.HRMainVO
 */
public HRMainVO getMainvo() {
	return mainvo;
}
/**
 * �����Ĺ��ܡ���;�������Եĸ��ģ��Լ�����ִ��ǰ������״̬��
 * @param		����˵��
 * @return		����ֵ
 * @exception �쳣����
 * @see		��Ҫ�μ�����������
 * @since		�������һ���汾���˷�������ӽ���������ѡ��
 * 
 * 
 * 
 * @return nc.vo.hi.hi_401.PsnDataVO
 */
public nc.vo.hi.hi_401.PsnDataVO[] getTaskvo() {
	return taskvo;
}
/**
 * �����Ĺ��ܡ���;�������Եĸ��ģ��Լ�����ִ��ǰ������״̬��
 * @param		����˵��
 * @return		����ֵ
 * @exception �쳣����
 * @see		��Ҫ�μ�����������
 * @since		�������һ���汾���˷�������ӽ���������ѡ��
 * 
 * 
 * 
 * @return nc.vo.hi.hi_401.PsnDataVO
 */
public nc.vo.hi.hi_401.PsnDataVO[] getWorkexpvo() {
	return workexpvo;
}
/**
 * �����Ĺ��ܡ���;�������Եĸ��ģ��Լ�����ִ��ǰ������״̬��
 * @param		����˵��
 * @return		����ֵ
 * @exception �쳣����
 * @see		��Ҫ�μ�����������
 * @since		�������һ���汾���˷�������ӽ���������ѡ��
 * 
 * 
 * 
 * @param newArticlevo nc.vo.hi.hi_401.PsnDataVO
 */
public void setArticlevo(nc.vo.hi.hi_401.PsnDataVO[] newArticlevo) {
	articlevo = newArticlevo;
}
/**
 * �����Ĺ��ܡ���;�������Եĸ��ģ��Լ�����ִ��ǰ������״̬��
 * @param		����˵��
 * @return		����ֵ
 * @exception �쳣����
 * @see		��Ҫ�μ�����������
 * @since		�������һ���汾���˷�������ӽ���������ѡ��
 * 
 * 
 * 
 * @param newMainvo nc.vo.hi.hi_301.HRMainVO
 */
public void setMainvo(HRMainVO newMainvo) {
	mainvo = newMainvo;
}
/**
 * �����Ĺ��ܡ���;�������Եĸ��ģ��Լ�����ִ��ǰ������״̬��
 * @param		����˵��
 * @return		����ֵ
 * @exception �쳣����
 * @see		��Ҫ�μ�����������
 * @since		�������һ���汾���˷�������ӽ���������ѡ��
 * 
 * 
 * 
 * @param newTaskvo nc.vo.hi.hi_401.PsnDataVO
 */
public void setTaskvo(nc.vo.hi.hi_401.PsnDataVO[] newTaskvo) {
	taskvo = newTaskvo;
}
/**
 * �����Ĺ��ܡ���;�������Եĸ��ģ��Լ�����ִ��ǰ������״̬��
 * @param		����˵��
 * @return		����ֵ
 * @exception �쳣����
 * @see		��Ҫ�μ�����������
 * @since		�������һ���汾���˷�������ӽ���������ѡ��
 * 
 * 
 * 
 * @param newWorkexp nc.vo.hi.hi_401.PsnDataVO
 */
public void setWorkexpvo(nc.vo.hi.hi_401.PsnDataVO[] newWorkexp) {
	workexpvo = newWorkexp;
}
/**
 * ��֤���������֮��������߼���ȷ�ԡ�
 * 
 * �������ڣ�(2001-2-15 11:47:35)
 * @exception nc.vo.pub.ValidationException �����֤ʧ�ܣ��׳�
 *     ValidationException���Դ�����н��͡�
 */
public void validate() throws nc.vo.pub.ValidationException {}
}
