package nc.vo.hi.hi_306;

import nc.vo.pub.SuperVO;

public class DocPsnCodeVO extends SuperVO {
	
	public String pk_psndoc;
	
	public String psncode;
	
	
	public static final String PK_PSNDOC="pk_psndoc";
	public static final String PSNCODE="psncode";
	
	

	/**
	 * @return the pk_psndoc
	 */
	public String getPk_psndoc() {
		return pk_psndoc;
	}

	/**
	 * @param pk_psndoc the pk_psndoc to set
	 */
	public void setPk_psndoc(String pk_psndoc) {
		this.pk_psndoc = pk_psndoc;
	}

	/**
	 * @return the psncode
	 */
	public String getPsncode() {
		return psncode;
	}

	/**
	 * @param psncode the psncode to set
	 */
	public void setPsncode(String psncode) {
		this.psncode = psncode;
	}

	@Override
	public String getPKFieldName() {
		return "pk_psndoc";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "bd_psndoc";
	}

	
	public DocPsnCodeVO() {
		
		   super();	
	}    

	public DocPsnCodeVO(String newPk_psndoc) {
	
	// 为主键字段赋值:
		pk_psndoc = newPk_psndoc;

	}
	 /**
	  * 返回对象标识,用来唯一定位对象.
	  *
	  * 创建日期:2007-3-7
	  * @return String
	  */
	   public String getPrimaryKey() {
				
		 return pk_psndoc;
	   
	   }

    /**
	  * 设置对象标识,用来唯一定位对象.
	  *
	  * 创建日期:2007-3-7
	  * @param newPk_docapply_h  String    
	  */
	 public void setPrimaryKey(String newPk_psndoc) {
				
		 pk_psndoc = newPk_psndoc; 
				
	 } 
          
	  /**
      * 返回数值对象的显示名称.
	   *
	   * 创建日期:2007-3-7
	   * @return java.lang.String 返回数值对象的显示名称.
	   */
	 public String getEntityName() {
				
	   return "bd_psndoc"; 
				
	 } 
}
