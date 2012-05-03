package nc.vo.hi.hi_301;

/**
 * 类的功能:用于存放全局变量
 * @author ：代群义
 * @version (2002-2-28 9:28:55)
 * @see 
 * @since 
 * 
 */
public class Global301VarVO extends nc.vo.pub.ValueObject{



/**
 *用户的公司主键
 */
 private String pk_corp =null;
 /**
 *用户的员工主键
 */
 private String pk_psndoc=null;
 /**
  *员工部门主键
  */
 private String pk_deptdoc=null;


           
             

             
            

             
           
  public String getPk_corp(){
	  return pk_corp;
 }              
 public void setPk_corp(String pk_corp){
	this.pk_corp=pk_corp;
 }             

  public String getPk_psndoc(){
	  return pk_psndoc;
 }              
 public void setPk_psndoc(String pk_psndoc){
	this.pk_psndoc=pk_psndoc;
 }         
             
      

   public String getPk_deptdoc(){
	  return pk_deptdoc;
 }              
 public void setPk_deptdoc(String pk_deptdoc){
	this.pk_deptdoc=pk_deptdoc;
 }              

             
             
 

/**
 * 返回数值对象的显示名称。
 * 
 * 创建日期：(2001-2-15 14:18:08)
 * @return java.lang.String 返回数值对象的显示名称。
 */
public java.lang.String getEntityName() {
	return null;
}

/**
 * 验证对象各属性之间的数据逻辑正确性。
 * 
 * 创建日期：(2001-2-15 11:47:35)
 * @exception nc.vo.pub.ValidationException 如果验证失败，抛出
 *     ValidationException，对错误进行解释。
 */
public void validate() throws nc.vo.pub.ValidationException {}


 //排序SQL语句
 private String sql = "";
/**
 *登录用户的ID
 */
String userID=null;

  public String getSql(){
	  return sql;
 } 

public String  getUserID(){
	return  this.userID;
}

 public void setSql(String newsql){
	sql = newsql;
 } 

public void setUserID(String userID){
	this.userID=userID;
}
}