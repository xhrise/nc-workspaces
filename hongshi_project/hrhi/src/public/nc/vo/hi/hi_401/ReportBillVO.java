package nc.vo.hi.hi_401;

import java.util.*;

/**
 * 此处插入类型说明。
 * 创建日期：(2002-5-22 21:02:56)
 * @author：田海波
 */
public class ReportBillVO extends nc.vo.pub.ValueObject {
   public String psn_pk;
   public String billtype="";
   public PsnDataVO[] curPsndatas=null;
   public PsnDataVO[] changes=null;
	
	
	
	
	
/**
 * ReportBillVO 构造子注解。
 */
public ReportBillVO() {
	super();
}
/**
 * 返回数值对象的显示名称。
 * 
 * 创建日期：(2001-2-15 14:18:08)
 * @return java.lang.String 返回数值对象的显示名称。
 */
public String getEntityName() {
	return null;
}
/**
 * 此处插入方法说明。
 * 创建日期：(2002-5-22 21:06:52)
 */
public void newMethod() {}
/**
 * 此处插入方法说明。
 * 创建日期：(2002-5-22 21:06:52)
 */
public void setBilltype(String newtype) {
	billtype=newtype;
	
	}
/**
 * 此处插入方法说明。
 * 创建日期：(2002-5-22 21:06:52)
 */
public void setChg(PsnDataVO[] chgdata) {
	changes=chgdata;
	
	}
/**
 * 此处插入方法说明。
 * 创建日期：(2002-5-22 21:06:52)
 */
public void setCurPsndata(PsnDataVO[] curdatas) {
	curPsndatas=curdatas;
	
	}
/**
 * 此处插入方法说明。
 * 创建日期：(2002-5-22 21:06:52)
 */
public void setPsnPK(String newpsnpk) {
	psn_pk=newpsnpk;
	
	}
/**
 * 验证对象各属性之间的数据逻辑正确性。
 * 
 * 创建日期：(2001-2-15 11:47:35)
 * @exception nc.vo.pub.ValidationException 如果验证失败，抛出
 *     ValidationException，对错误进行解释。
 */
public void validate() throws nc.vo.pub.ValidationException {}
}
