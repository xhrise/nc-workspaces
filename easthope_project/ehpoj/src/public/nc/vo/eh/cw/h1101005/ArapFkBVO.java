  /***************************************************************\
  *     The skeleton of this class is generated by an automatic *
  * code generator for NC product. It is based on Velocity.     *
  \***************************************************************/
      	package nc.vo.eh.cw.h1101005;
   	
	import java.util.ArrayList;
	import nc.vo.pub.*;
import nc.vo.pub.lang.*;
	
/**
 * <b> 在此处简要描述此类的功能 </b>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 * 创建日期:2008-5-28
 * @author author
 * @version Your Project 1.0
 */
     @SuppressWarnings("serial")
	public class ArapFkBVO extends SuperVO {
           
             public String pk_corp;
             public String pk_invbasdoc;
             public String pk_fk;
             public UFDouble pricemny;
             public String vsourcebilltype;
             public UFDouble rkmount;
             public UFDateTime ts;
             public String vsourcebillrowid;
             public Integer dr;
             public String vsourcebillid;
             public String pk_fk_b;
             public String pk_in;
             public UFDouble price;
             public String rkbillno;
             public String htbillno;
             public UFDouble htamount;
             public UFDouble sbamount;
             public UFDouble kzamount;
             public UFDouble kjprice;
             public UFDouble yfkje;
             public UFDouble wfkje;
             public String memo;
             public UFDouble bcfkje;
             public UFDouble amount;//add by houcq 2011-07-01
                	         
             public static final String  PK_CORP="pk_corp";   
             public static final String  PRICE="price";
             public static final String  PK_IN="pk_in";
             public static final String  PK_INVBASDOC="pk_invbasdoc";   
             public static final String  PK_FK="pk_fk";   
             public static final String  PRICEMNY="pricemny";   
             public static final String  VSOURCEBILLTYPE="vsourcebilltype";   
             public static final String  RKMOUNT="rkmount";   
             public static final String  TS="ts";   
             public static final String  VSOURCEBILLROWID="vsourcebillrowid";   
             public static final String  DR="dr";   
             public static final String  VSOURCEBILLID="vsourcebillid";   
             public static final String  PK_FK_B="pk_fk_b";   
      
    
        public UFDouble getAmount() {
				return amount;
			}

			public void setAmount(UFDouble amount) {
				this.amount = amount;
			}

		/**
	   * 属性pk_corp的Getter方法.
	   *
	   * 创建日期:2008-5-28
	   * @return String
	   */
	 public String getPk_corp() {
		 return pk_corp;
	  }   
	  
     /**
	   * 属性pk_corp的Setter方法.
	   *
	   * 创建日期:2008-5-28
	   * @param newPk_corp String
	   */
	public void setPk_corp(String newPk_corp) {
		
		pk_corp = newPk_corp;
	 } 	  
       
        /**
	   * 属性pk_invbasdoc的Getter方法.
	   *
	   * 创建日期:2008-5-28
	   * @return String
	   */
	 public String getPk_invbasdoc() {
		 return pk_invbasdoc;
	  }   
	  
     /**
	   * 属性pk_invbasdoc的Setter方法.
	   *
	   * 创建日期:2008-5-28
	   * @param newPk_invbasdoc String
	   */
	public void setPk_invbasdoc(String newPk_invbasdoc) {
		
		pk_invbasdoc = newPk_invbasdoc;
	 } 	  
       
        /**
	   * 属性pk_fk的Getter方法.
	   *
	   * 创建日期:2008-5-28
	   * @return String
	   */
	 public String getPk_fk() {
		 return pk_fk;
	  }   
	  
     /**
	   * 属性pk_fk的Setter方法.
	   *
	   * 创建日期:2008-5-28
	   * @param newPk_fk String
	   */
	public void setPk_fk(String newPk_fk) {
		
		pk_fk = newPk_fk;
	 } 	  
       
        /**
	   * 属性pricemny的Getter方法.
	   *
	   * 创建日期:2008-5-28
	   * @return UFDouble
	   */
	 public UFDouble getPricemny() {
		 return pricemny;
	  }   
	  
     /**
	   * 属性pricemny的Setter方法.
	   *
	   * 创建日期:2008-5-28
	   * @param newPricemny UFDouble
	   */
	public void setPricemny(UFDouble newPricemny) {
		
		pricemny = newPricemny;
	 } 	  
       
        /**
	   * 属性vsourcebilltype的Getter方法.
	   *
	   * 创建日期:2008-5-28
	   * @return String
	   */
	 public String getVsourcebilltype() {
		 return vsourcebilltype;
	  }   
	  
     /**
	   * 属性vsourcebilltype的Setter方法.
	   *
	   * 创建日期:2008-5-28
	   * @param newVsourcebilltype String
	   */
	public void setVsourcebilltype(String newVsourcebilltype) {
		
		vsourcebilltype = newVsourcebilltype;
	 } 	  
       
        /**
	   * 属性rkmount的Getter方法.
	   *
	   * 创建日期:2008-5-28
	   * @return UFDouble
	   */
	 public UFDouble getRkmount() {
		 return rkmount;
	  }   
	  
     /**
	   * 属性rkmount的Setter方法.
	   *
	   * 创建日期:2008-5-28
	   * @param newRkmount UFDouble
	   */
	public void setRkmount(UFDouble newRkmount) {
		
		rkmount = newRkmount;
	 } 	  
       
        /**
	   * 属性ts的Getter方法.
	   *
	   * 创建日期:2008-5-28
	   * @return UFDateTime
	   */
	 public UFDateTime getTs() {
		 return ts;
	  }   
	  
     /**
	   * 属性ts的Setter方法.
	   *
	   * 创建日期:2008-5-28
	   * @param newTs UFDateTime
	   */
	public void setTs(UFDateTime newTs) {
		
		ts = newTs;
	 } 	  
       
        /**
	   * 属性vsourcebillrowid的Getter方法.
	   *
	   * 创建日期:2008-5-28
	   * @return String
	   */
	 public String getVsourcebillrowid() {
		 return vsourcebillrowid;
	  }   
	  
     /**
	   * 属性vsourcebillrowid的Setter方法.
	   *
	   * 创建日期:2008-5-28
	   * @param newVsourcebillrowid String
	   */
	public void setVsourcebillrowid(String newVsourcebillrowid) {
		
		vsourcebillrowid = newVsourcebillrowid;
	 } 	  
       
        /**
	   * 属性dr的Getter方法.
	   *
	   * 创建日期:2008-5-28
	   * @return Integer
	   */
	 public Integer getDr() {
		 return dr;
	  }   
	  
     /**
	   * 属性dr的Setter方法.
	   *
	   * 创建日期:2008-5-28
	   * @param newDr Integer
	   */
	public void setDr(Integer newDr) {
		
		dr = newDr;
	 } 	  
       
        /**
	   * 属性vsourcebillid的Getter方法.
	   *
	   * 创建日期:2008-5-28
	   * @return String
	   */
	 public String getVsourcebillid() {
		 return vsourcebillid;
	  }   
	  
     /**
	   * 属性vsourcebillid的Setter方法.
	   *
	   * 创建日期:2008-5-28
	   * @param newVsourcebillid String
	   */
	public void setVsourcebillid(String newVsourcebillid) {
		
		vsourcebillid = newVsourcebillid;
	 } 	  
       
        /**
	   * 属性pk_fk_b的Getter方法.
	   *
	   * 创建日期:2008-5-28
	   * @return String
	   */
	 public String getPk_fk_b() {
		 return pk_fk_b;
	  }   
	  
     /**
	   * 属性pk_fk_b的Setter方法.
	   *
	   * 创建日期:2008-5-28
	   * @param newPk_fk_b String
	   */
	public void setPk_fk_b(String newPk_fk_b) {
		
		pk_fk_b = newPk_fk_b;
	 } 	  
       
       
    /**
	  * 验证对象各属性之间的数据逻辑正确性.
	  *
	  * 创建日期:2008-5-28
	  * @exception nc.vo.pub.ValidationException 如果验证失败,抛出
	  * ValidationException,对错误进行解释.
	 */
	 @SuppressWarnings("unchecked")
	public void validate() throws ValidationException {
	
	 	ArrayList errFields = new ArrayList(); // errFields record those null

                                                      // fields that cannot be null.
       		  // 检查是否为不允许空的字段赋了空值,你可能需要修改下面的提示信息:
	
	   		if (pk_fk_b == null) {
			errFields.add(new String("pk_fk_b"));
			  }	
	   	
	    StringBuffer message = new StringBuffer();
		message.append("下列字段不能为空:");
		if (errFields.size() > 0) {
		String[] temp = (String[]) errFields.toArray(new String[0]);
		message.append(temp[0]);
		for ( int i= 1; i < temp.length; i++ ) {
			message.append(",");
			message.append(temp[i]);
		}
		throw new NullFieldException(message.toString());
		}
	 }
			   
       
   	/**
	  * <p>取得父VO主键字段.
	  * <p>
	  * 创建日期:2008-5-28
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	  	 
	 		return "pk_fk";
	 	
	}   
    
    /**
	  * <p>取得表主键.
	  * <p>
	  * 创建日期:2008-5-28
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	 	  return "pk_fk_b";
	 	}
    
	/**
      * <p>返回表名称.
	  * <p>
	  * 创建日期:2008-5-28
	  * @return java.lang.String
	 */
	public java.lang.String getTableName() {
				
		return "eh_arap_fk_b";
	}    
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:2008-5-28
	  */
	public ArapFkBVO() {
			
			   super();	
	  }    
    
            /**
	 * 使用主键进行初始化的构造子.
	 *
	 * 创建日期:2008-5-28
	 * @param newPk_fk_b 主键值
	 */
	 public ArapFkBVO(String newPk_fk_b) {
		
		// 为主键字段赋值:
		 pk_fk_b = newPk_fk_b;
	
    	}
    
     
     /**
	  * 返回对象标识,用来唯一定位对象.
	  *
	  * 创建日期:2008-5-28
	  * @return String
	  */
	   public String getPrimaryKey() {
				
		 return pk_fk_b;
	   
	   }

     /**
	  * 设置对象标识,用来唯一定位对象.
	  *
	  * 创建日期:2008-5-28
	  * @param newPk_fk_b  String    
	  */
	 public void setPrimaryKey(String newPk_fk_b) {
				
				pk_fk_b = newPk_fk_b; 
				
	 } 
           
	  /**
       * 返回数值对象的显示名称.
	   *
	   * 创建日期:2008-5-28
	   * @return java.lang.String 返回数值对象的显示名称.
	   */
	 public String getEntityName() {
				
	   return "eh_arap_fk_b"; 
				
	 }

	public String getPk_in() {
		return pk_in;
	}

	public void setPk_in(String pk_in) {
		this.pk_in = pk_in;
	}

	public UFDouble getPrice() {
		return price;
	}

	public void setPrice(UFDouble price) {
		this.price = price;
	}

    /**
     * @return the rkbillno
     */
    public String getRkbillno() {
        return rkbillno;
    }

    /**
     * @param rkbillno the rkbillno to set
     */
    public void setRkbillno(String rkbillno) {
        this.rkbillno = rkbillno;
    }

    /**
     * @return the htbillno
     */
    public String getHtbillno() {
        return htbillno;
    }

    /**
     * @param htbillno the htbillno to set
     */
    public void setHtbillno(String htbillno) {
        this.htbillno = htbillno;
    }

    /**
     * @return the kjprice
     */
    public UFDouble getKjprice() {
        return kjprice;
    }

    /**
     * @param kjprice the kjprice to set
     */
    public void setKjprice(UFDouble kjprice) {
        this.kjprice = kjprice;
    }

    /**
     * @return the kzamount
     */
    public UFDouble getKzamount() {
        return kzamount;
    }

    /**
     * @param kzamount the kzamount to set
     */
    public void setKzamount(UFDouble kzamount) {
        this.kzamount = kzamount;
    }

    /**
     * @return the memo
     */
    public String getMemo() {
        return memo;
    }

    /**
     * @param memo the memo to set
     */
    public void setMemo(String memo) {
        this.memo = memo;
    }

    /**
     * @return the sbamount
     */
    public UFDouble getSbamount() {
        return sbamount;
    }

    /**
     * @param sbamount the sbamount to set
     */
    public void setSbamount(UFDouble sbamount) {
        this.sbamount = sbamount;
    }

    /**
     * @return the wfkje
     */
    public UFDouble getWfkje() {
        return wfkje;
    }

    /**
     * @param wfkje the wfkje to set
     */
    public void setWfkje(UFDouble wfkje) {
        this.wfkje = wfkje;
    }

    /**
     * @return the yfkje
     */
    public UFDouble getYfkje() {
        return yfkje;
    }

    /**
     * @param yfkje the yfkje to set
     */
    public void setYfkje(UFDouble yfkje) {
        this.yfkje = yfkje;
    }

    /**
     * @return the htamount
     */
    public UFDouble getHtamount() {
        return htamount;
    }

    /**
     * @param htamount the htamount to set
     */
    public void setHtamount(UFDouble htamount) {
        this.htamount = htamount;
    }

    /**
     * @return the bcfkje
     */
    public UFDouble getBcfkje() {
        return bcfkje;
    }

    /**
     * @param bcfkje the bcfkje to set
     */
    public void setBcfkje(UFDouble bcfkje) {
        this.bcfkje = bcfkje;
    }

    
} 
