package nc.vo.hi.hi_301;

/**
 * ��Ĺ���:���ڴ��ȫ�ֱ���
 * @author ����Ⱥ��
 * @version (2002-2-28 9:28:55)
 * @see 
 * @since 
 * 
 */
public class Global301VarVO extends nc.vo.pub.ValueObject{



/**
 *�û��Ĺ�˾����
 */
 private String pk_corp =null;
 /**
 *�û���Ա������
 */
 private String pk_psndoc=null;
 /**
  *Ա����������
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
 * ������ֵ�������ʾ���ơ�
 * 
 * �������ڣ�(2001-2-15 14:18:08)
 * @return java.lang.String ������ֵ�������ʾ���ơ�
 */
public java.lang.String getEntityName() {
	return null;
}

/**
 * ��֤���������֮��������߼���ȷ�ԡ�
 * 
 * �������ڣ�(2001-2-15 11:47:35)
 * @exception nc.vo.pub.ValidationException �����֤ʧ�ܣ��׳�
 *     ValidationException���Դ�����н��͡�
 */
public void validate() throws nc.vo.pub.ValidationException {}


 //����SQL���
 private String sql = "";
/**
 *��¼�û���ID
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