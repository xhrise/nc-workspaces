package nc.vo.hi.hi_401;

import java.util.*;

/**
 * �˴���������˵����
 * �������ڣ�(2002-5-22 21:02:56)
 * @author���ﺣ��
 */
public class ReportBillVO extends nc.vo.pub.ValueObject {
   public String psn_pk;
   public String billtype="";
   public PsnDataVO[] curPsndatas=null;
   public PsnDataVO[] changes=null;
	
	
	
	
	
/**
 * ReportBillVO ������ע�⡣
 */
public ReportBillVO() {
	super();
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
 * �˴����뷽��˵����
 * �������ڣ�(2002-5-22 21:06:52)
 */
public void newMethod() {}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2002-5-22 21:06:52)
 */
public void setBilltype(String newtype) {
	billtype=newtype;
	
	}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2002-5-22 21:06:52)
 */
public void setChg(PsnDataVO[] chgdata) {
	changes=chgdata;
	
	}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2002-5-22 21:06:52)
 */
public void setCurPsndata(PsnDataVO[] curdatas) {
	curPsndatas=curdatas;
	
	}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2002-5-22 21:06:52)
 */
public void setPsnPK(String newpsnpk) {
	psn_pk=newpsnpk;
	
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
