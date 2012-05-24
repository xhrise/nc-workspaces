  /***************************************************************\
  *     The skeleton of this class is generated by an automatic *
  * code generator for NC product. It is based on Velocity.     *
  \***************************************************************/
      	package nc.vo.eh.iso.z0502005;
   	
	import java.util.ArrayList;

import nc.vo.pub.NullFieldException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
	
/**
 * <b> 在此处简要描述此类的功能 </b>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 * 创建日期:2008-4-14
 * @author author
 * @version Your Project 1.0
 */
     public class StockCheckreportVO extends SuperVO {
           
             public UFBoolean tc_flag;
             public String pk_corp;
             public UFDate dmakedate;
             public String def_3;
             public String def_1;
             public Integer dr;
             public UFDouble def_9;
             public UFDouble def_10;
             public String vapprovenote;
             public String def_2;
             public UFDouble spnum;
             public Integer vbillstatus;
             public String pk_invbasdoc;
             public String def_8;
             public String vsourcebilltype;
             public String coperatorid;
             public UFDateTime ts;
             public String pk_busitype;
             public Integer resulst;
             public String pk_checkreport;
             public String memo;
             public UFDouble def_6;
             public String note;
             public String def_5;
             public UFDouble samplenum;
             public UFDate dapprovedate;
             public String vbilltype;
             public UFDouble passnum;
             public String vapproveid;
             public String checknote;
             public String def_4;
             public String def_7;
             public String vsourcebillid;
             public String billno;
             public String pk_receipt;
             public String pk_sample;
             public String checktype;
             public UFBoolean edit_flag;
             public String rk_flag;
             public String cyperson;
             public UFDouble rkamount;
             public String pk_sbbills;       // 保存来自于多个司磅单的主键 ("","","")
             public String pk_receipt_b;     // 收货通知单子表主键
             public String vsbbilltype;      // 判断是否来自司磅单的单据类型
             public UFBoolean lock_flag;     //关闭状态
             public String ispass;           //是否合格(主要是在审批情况下显示)
             public UFDouble dnum;
             public String pk_procheckapplys;//存放多个成品检测申请的PK 
             public String allpk;            //存放表体检测物料中的物料PK和vsourcebillid
             public UFDouble kz;             //扣重
             public UFDouble kj;             //扣价
             public UFDouble tkj;            //其他扣价
             public String oaspcode;		//OA特采审批单号
             public String rkbillno;
             public String th_flag;
             public String pk_in;
              
             public static final String  CYPERSON="cyperson";
             public static final String  PK_IN="pk_in";
             public static final String  TH_FLAG="th_flag";
             public static final String  RKBILLNO="rkbillno";
             public static final String  DNUM="dnum";
             public static final String  EDIT_FLAG="edit_flag";
             public static final String  TC_FLAG="tc_flag";   
             public static final String  PK_CORP="pk_corp";   
             public static final String  DMAKEDATE="dmakedate";   
             public static final String  DEF_3="def_3";   
             public static final String  DEF_1="def_1";   
             public static final String  DR="dr";   
             public static final String  DEF_9="def_9";   
             public static final String  DEF_10="def_10";   
             public static final String  VAPPROVENOTE="vapprovenote";   
             public static final String  DEF_2="def_2";   
             public static final String  SPNUM="spnum";   
             public static final String  VBILLSTATUS="vbillstatus";   
             public static final String  PK_INVBASDOC="pk_invbasdoc";   
             public static final String  DEF_8="def_8";   
             public static final String  VSOURCEBILLTYPE="vsourcebilltype";   
             public static final String  COPERATORID="coperatorid";   
             public static final String  TS="ts";   
             public static final String  PK_BUSITYPE="pk_busitype";   
             public static final String  RESULST="resulst";   
             public static final String  PK_CHECKREPORT="pk_checkreport";   
             public static final String  MEMO="memo";   
             public static final String  DEF_6="def_6";   
             public static final String  NOTE="note";   
             public static final String  DEF_5="def_5";   
             public static final String  SAMPLENUM="samplenum";   
             public static final String  DAPPROVEDATE="dapprovedate";   
             public static final String  VBILLTYPE="vbilltype";   
             public static final String  PASSNUM="passnum";   
             public static final String  VAPPROVEID="vapproveid";   
             public static final String  CHECKNOTE="checknote";   
             public static final String  DEF_4="def_4";   
             public static final String  DEF_7="def_7";   
             public static final String  VSOURCEBILLID="vsourcebillid";   
             public static final String  BILLNO="billno";   
             public static final String  PK_RECEIPT="pk_receipt";   
             public static final String  PK_SAMPLE="pk_sample";   
             public static final String  CHECKTYPE="checktype";  
             public static final String RK_FLAG="rk_flag";
      
    
             
             
             
             
        /**
             * @return the lock_flag
             */
            public UFBoolean getLock_flag() {
                return lock_flag;
            }

            /**
             * @param lock_flag the lock_flag to set
             */
            public void setLock_flag(UFBoolean lock_flag) {
                this.lock_flag = lock_flag;
            }

        /**
             * @return the cyperson
             */
            public String getCyperson() {
                return cyperson;
            }

            /**
             * @param cyperson the cyperson to set
             */
            public void setCyperson(String cyperson) {
                this.cyperson = cyperson;
            }

        public UFBoolean getEdit_flag() {
				return edit_flag;
			}

			public void setEdit_flag(UFBoolean edit_flag) {
				this.edit_flag = edit_flag;
			}

		/**
	   * 属性tc_flag的Getter方法.
	   *
	   * 创建日期:2008-4-14
	   * @return UFBoolean
	   */
	 public UFBoolean getTc_flag() {
		 return tc_flag;
	  }   
	  
     /**
	   * 属性tc_flag的Setter方法.
	   *
	   * 创建日期:2008-4-14
	   * @param newTc_flag UFBoolean
	   */
	public void setTc_flag(UFBoolean newTc_flag) {
		
		tc_flag = newTc_flag;
	 } 	  
       
        /**
	   * 属性pk_corp的Getter方法.
	   *
	   * 创建日期:2008-4-14
	   * @return String
	   */
	 public String getPk_corp() {
		 return pk_corp;
	  }   
	  
     /**
	   * 属性pk_corp的Setter方法.
	   *
	   * 创建日期:2008-4-14
	   * @param newPk_corp String
	   */
	public void setPk_corp(String newPk_corp) {
		
		pk_corp = newPk_corp;
	 } 	  
       
        /**
	   * 属性dmakedate的Getter方法.
	   *
	   * 创建日期:2008-4-14
	   * @return UFDate
	   */
	 public UFDate getDmakedate() {
		 return dmakedate;
	  }   
	  
     /**
	   * 属性dmakedate的Setter方法.
	   *
	   * 创建日期:2008-4-14
	   * @param newDmakedate UFDate
	   */
	public void setDmakedate(UFDate newDmakedate) {
		
		dmakedate = newDmakedate;
	 } 	  
       
        /**
	   * 属性def_3的Getter方法.
	   *
	   * 创建日期:2008-4-14
	   * @return String
	   */
	 public String getDef_3() {
		 return def_3;
	  }   
	  
     /**
	   * 属性def_3的Setter方法.
	   *
	   * 创建日期:2008-4-14
	   * @param newDef_3 String
	   */
	public void setDef_3(String newDef_3) {
		
		def_3 = newDef_3;
	 } 	  
       
        /**
	   * 属性def_1的Getter方法.
	   *
	   * 创建日期:2008-4-14
	   * @return String
	   */
	 public String getDef_1() {
		 return def_1;
	  }   
	  
     /**
	   * 属性def_1的Setter方法.
	   *
	   * 创建日期:2008-4-14
	   * @param newDef_1 String
	   */
	public void setDef_1(String newDef_1) {
		
		def_1 = newDef_1;
	 } 	  
       
        /**
	   * 属性dr的Getter方法.
	   *
	   * 创建日期:2008-4-14
	   * @return Integer
	   */
	 public Integer getDr() {
		 return dr;
	  }   
	  
     /**
	   * 属性dr的Setter方法.
	   *
	   * 创建日期:2008-4-14
	   * @param newDr Integer
	   */
	public void setDr(Integer newDr) {
		
		dr = newDr;
	 } 	  
       
        /**
	   * 属性def_9的Getter方法.
	   *
	   * 创建日期:2008-4-14
	   * @return UFDouble
	   */
	 public UFDouble getDef_9() {
		 return def_9;
	  }   
	  
     /**
	   * 属性def_9的Setter方法.
	   *
	   * 创建日期:2008-4-14
	   * @param newDef_9 UFDouble
	   */
	public void setDef_9(UFDouble newDef_9) {
		
		def_9 = newDef_9;
	 } 	  
       
        /**
	   * 属性def_10的Getter方法.
	   *
	   * 创建日期:2008-4-14
	   * @return UFDouble
	   */
	 public UFDouble getDef_10() {
		 return def_10;
	  }   
	  
     /**
	   * 属性def_10的Setter方法.
	   *
	   * 创建日期:2008-4-14
	   * @param newDef_10 UFDouble
	   */
	public void setDef_10(UFDouble newDef_10) {
		
		def_10 = newDef_10;
	 } 	  
       
        /**
	   * 属性vapprovenote的Getter方法.
	   *
	   * 创建日期:2008-4-14
	   * @return String
	   */
	 public String getVapprovenote() {
		 return vapprovenote;
	  }   
	  
     /**
	   * 属性vapprovenote的Setter方法.
	   *
	   * 创建日期:2008-4-14
	   * @param newVapprovenote String
	   */
	public void setVapprovenote(String newVapprovenote) {
		
		vapprovenote = newVapprovenote;
	 } 	  
       
        /**
	   * 属性def_2的Getter方法.
	   *
	   * 创建日期:2008-4-14
	   * @return String
	   */
	 public String getDef_2() {
		 return def_2;
	  }   
	  
     /**
	   * 属性def_2的Setter方法.
	   *
	   * 创建日期:2008-4-14
	   * @param newDef_2 String
	   */
	public void setDef_2(String newDef_2) {
		
		def_2 = newDef_2;
	 } 	  
       
        /**
	   * 属性spnum的Getter方法.
	   *
	   * 创建日期:2008-4-14
	   * @return UFDouble
	   */
	 public UFDouble getSpnum() {
		 return spnum;
	  }   
	  
     /**
	   * 属性spnum的Setter方法.
	   *
	   * 创建日期:2008-4-14
	   * @param newSpnum UFDouble
	   */
	public void setSpnum(UFDouble newSpnum) {
		
		spnum = newSpnum;
	 } 	  
       
        /**
	   * 属性vbillstatus的Getter方法.
	   *
	   * 创建日期:2008-4-14
	   * @return Integer
	   */
	 public Integer getVbillstatus() {
		 return vbillstatus;
	  }   
	  
     /**
	   * 属性vbillstatus的Setter方法.
	   *
	   * 创建日期:2008-4-14
	   * @param newVbillstatus Integer
	   */
	public void setVbillstatus(Integer newVbillstatus) {
		
		vbillstatus = newVbillstatus;
	 } 	  
       
        /**
	   * 属性pk_invbasdoc的Getter方法.
	   *
	   * 创建日期:2008-4-14
	   * @return String
	   */
	 public String getPk_invbasdoc() {
		 return pk_invbasdoc;
	  }   
	  
     /**
	   * 属性pk_invbasdoc的Setter方法.
	   *
	   * 创建日期:2008-4-14
	   * @param newPk_invbasdoc String
	   */
	public void setPk_invbasdoc(String newPk_invbasdoc) {
		
		pk_invbasdoc = newPk_invbasdoc;
	 } 	  
       
        /**
	   * 属性def_8的Getter方法.
	   *
	   * 创建日期:2008-4-14
	   * @return UFDouble
	   */
	 public String getDef_8() {
		 return def_8;
	  }   
	  
     /**
	   * 属性def_8的Setter方法.
	   *
	   * 创建日期:2008-4-14
	   * @param newDef_8 UFDouble
	   */
	public void setDef_8(String newDef_8) {
		
		def_8 = newDef_8;
	 } 	  
       
        /**
	   * 属性vsourcebilltype的Getter方法.
	   *
	   * 创建日期:2008-4-14
	   * @return String
	   */
	 public String getVsourcebilltype() {
		 return vsourcebilltype;
	  }   
	  
     /**
	   * 属性vsourcebilltype的Setter方法.
	   *
	   * 创建日期:2008-4-14
	   * @param newVsourcebilltype String
	   */
	public void setVsourcebilltype(String newVsourcebilltype) {
		
		vsourcebilltype = newVsourcebilltype;
	 } 	  
       
        /**
	   * 属性coperatorid的Getter方法.
	   *
	   * 创建日期:2008-4-14
	   * @return String
	   */
	 public String getCoperatorid() {
		 return coperatorid;
	  }   
	  
     /**
	   * 属性coperatorid的Setter方法.
	   *
	   * 创建日期:2008-4-14
	   * @param newCoperatorid String
	   */
	public void setCoperatorid(String newCoperatorid) {
		
		coperatorid = newCoperatorid;
	 } 	  
       
        /**
	   * 属性ts的Getter方法.
	   *
	   * 创建日期:2008-4-14
	   * @return UFDateTime
	   */
	 public UFDateTime getTs() {
		 return ts;
	  }   
	  
     /**
	   * 属性ts的Setter方法.
	   *
	   * 创建日期:2008-4-14
	   * @param newTs UFDateTime
	   */
	public void setTs(UFDateTime newTs) {
		
		ts = newTs;
	 } 	  
       
        /**
	   * 属性pk_busitype的Getter方法.
	   *
	   * 创建日期:2008-4-14
	   * @return String
	   */
	 public String getPk_busitype() {
		 return pk_busitype;
	  }   
	  
     /**
	   * 属性pk_busitype的Setter方法.
	   *
	   * 创建日期:2008-4-14
	   * @param newPk_busitype String
	   */
	public void setPk_busitype(String newPk_busitype) {
		
		pk_busitype = newPk_busitype;
	 } 	  
       
        /**
	   * 属性resulst的Getter方法.
	   *
	   * 创建日期:2008-4-14
	   * @return Integer
	   */
	 public Integer getResulst() {
		 return resulst;
	  }   
	  
     /**
	   * 属性resulst的Setter方法.
	   *
	   * 创建日期:2008-4-14
	   * @param newResulst Integer
	   */
	public void setResulst(Integer newResulst) {
		
		resulst = newResulst;
	 } 	  
       
        /**
	   * 属性pk_checkreport的Getter方法.
	   *
	   * 创建日期:2008-4-14
	   * @return String
	   */
	 public String getPk_checkreport() {
		 return pk_checkreport;
	  }   
	  
     /**
	   * 属性pk_checkreport的Setter方法.
	   *
	   * 创建日期:2008-4-14
	   * @param newPk_checkreport String
	   */
	public void setPk_checkreport(String newPk_checkreport) {
		
		pk_checkreport = newPk_checkreport;
	 } 	  
       
        /**
	   * 属性memo的Getter方法.
	   *
	   * 创建日期:2008-4-14
	   * @return String
	   */
	 public String getMemo() {
		 return memo;
	  }   
	  
     /**
	   * 属性memo的Setter方法.
	   *
	   * 创建日期:2008-4-14
	   * @param newMemo String
	   */
	public void setMemo(String newMemo) {
		
		memo = newMemo;
	 } 	  
       
        /**
	   * 属性def_6的Getter方法.
	   *
	   * 创建日期:2008-4-14
	   * @return UFDouble
	   */
	 public UFDouble getDef_6() {
		 return def_6;
	  }   
	  
     /**
	   * 属性def_6的Setter方法.
	   *
	   * 创建日期:2008-4-14
	   * @param newDef_6 UFDouble
	   */
	public void setDef_6(UFDouble newDef_6) {
		
		def_6 = newDef_6;
	 } 	  
       
        /**
	   * 属性note的Getter方法.
	   *
	   * 创建日期:2008-4-14
	   * @return String
	   */
	 public String getNote() {
		 return note;
	  }   
	  
     /**
	   * 属性note的Setter方法.
	   *
	   * 创建日期:2008-4-14
	   * @param newNote String
	   */
	public void setNote(String newNote) {
		
		note = newNote;
	 } 	  
       
        /**
	   * 属性def_5的Getter方法.
	   *
	   * 创建日期:2008-4-14
	   * @return String
	   */
	 public String getDef_5() {
		 return def_5;
	  }   
	  
     /**
	   * 属性def_5的Setter方法.
	   *
	   * 创建日期:2008-4-14
	   * @param newDef_5 String
	   */
	public void setDef_5(String newDef_5) {
		
		def_5 = newDef_5;
	 } 	  
       
        /**
	   * 属性samplenum的Getter方法.
	   *
	   * 创建日期:2008-4-14
	   * @return UFDouble
	   */
	 public UFDouble getSamplenum() {
		 return samplenum;
	  }   
	  
     /**
	   * 属性samplenum的Setter方法.
	   *
	   * 创建日期:2008-4-14
	   * @param newSamplenum UFDouble
	   */
	public void setSamplenum(UFDouble newSamplenum) {
		
		samplenum = newSamplenum;
	 } 	  
       
        /**
	   * 属性dapprovedate的Getter方法.
	   *
	   * 创建日期:2008-4-14
	   * @return UFDate
	   */
	 public UFDate getDapprovedate() {
		 return dapprovedate;
	  }   
	  
     /**
	   * 属性dapprovedate的Setter方法.
	   *
	   * 创建日期:2008-4-14
	   * @param newDapprovedate UFDate
	   */
	public void setDapprovedate(UFDate newDapprovedate) {
		
		dapprovedate = newDapprovedate;
	 } 	  
       
        /**
	   * 属性vbilltype的Getter方法.
	   *
	   * 创建日期:2008-4-14
	   * @return String
	   */
	 public String getVbilltype() {
		 return vbilltype;
	  }   
	  
     /**
	   * 属性vbilltype的Setter方法.
	   *
	   * 创建日期:2008-4-14
	   * @param newVbilltype String
	   */
	public void setVbilltype(String newVbilltype) {
		
		vbilltype = newVbilltype;
	 } 	  
       
        /**
	   * 属性passnum的Getter方法.
	   *
	   * 创建日期:2008-4-14
	   * @return UFDouble
	   */
	 public UFDouble getPassnum() {
		 return passnum;
	  }   
	  
     /**
	   * 属性passnum的Setter方法.
	   *
	   * 创建日期:2008-4-14
	   * @param newPassnum UFDouble
	   */
	public void setPassnum(UFDouble newPassnum) {
		
		passnum = newPassnum;
	 } 	  
       
        /**
	   * 属性vapproveid的Getter方法.
	   *
	   * 创建日期:2008-4-14
	   * @return String
	   */
	 public String getVapproveid() {
		 return vapproveid;
	  }   
	  
     /**
	   * 属性vapproveid的Setter方法.
	   *
	   * 创建日期:2008-4-14
	   * @param newVapproveid String
	   */
	public void setVapproveid(String newVapproveid) {
		
		vapproveid = newVapproveid;
	 } 	  
       
        /**
	   * 属性checknote的Getter方法.
	   *
	   * 创建日期:2008-4-14
	   * @return String
	   */
	 public String getChecknote() {
		 return checknote;
	  }   
	  
     /**
	   * 属性checknote的Setter方法.
	   *
	   * 创建日期:2008-4-14
	   * @param newChecknote String
	   */
	public void setChecknote(String newChecknote) {
		
		checknote = newChecknote;
	 } 	  
       
        /**
	   * 属性def_4的Getter方法.
	   *
	   * 创建日期:2008-4-14
	   * @return String
	   */
	 public String getDef_4() {
		 return def_4;
	  }   
	  
     /**
	   * 属性def_4的Setter方法.
	   *
	   * 创建日期:2008-4-14
	   * @param newDef_4 String
	   */
	public void setDef_4(String newDef_4) {
		
		def_4 = newDef_4;
	 } 	  
       
        /**
	   * 属性def_7的Getter方法.
	   *
	   * 创建日期:2008-4-14
	   * @return UFDouble
	   */
	 public String getDef_7() {
		 return def_7;
	  }   
	  
     /**
	   * 属性def_7的Setter方法.
	   *
	   * 创建日期:2008-4-14
	   * @param newDef_7 UFDouble
	   */
	public void setDef_7(String newDef_7) {
		
		def_7 = newDef_7;
	 } 	  
       
        /**
	   * 属性vsourcebillid的Getter方法.
	   *
	   * 创建日期:2008-4-14
	   * @return String
	   */
	 public String getVsourcebillid() {
		 return vsourcebillid;
	  }   
	  
     /**
	   * 属性vsourcebillid的Setter方法.
	   *
	   * 创建日期:2008-4-14
	   * @param newVsourcebillid String
	   */
	public void setVsourcebillid(String newVsourcebillid) {
		
		vsourcebillid = newVsourcebillid;
	 } 	  
       
        /**
	   * 属性billno的Getter方法.
	   *
	   * 创建日期:2008-4-14
	   * @return String
	   */
	 public String getBillno() {
		 return billno;
	  }   
	  
     /**
	   * 属性billno的Setter方法.
	   *
	   * 创建日期:2008-4-14
	   * @param newBillno String
	   */
	public void setBillno(String newBillno) {
		
		billno = newBillno;
	 } 	  
       
        /**
	   * 属性pk_receipt的Getter方法.
	   *
	   * 创建日期:2008-4-14
	   * @return String
	   */
	 public String getPk_receipt() {
		 return pk_receipt;
	  }   
	  
     /**
	   * 属性pk_receipt的Setter方法.
	   *
	   * 创建日期:2008-4-14
	   * @param newPk_receipt String
	   */
	public void setPk_receipt(String newPk_receipt) {
		
		pk_receipt = newPk_receipt;
	 } 	  
       
        /**
	   * 属性pk_sample的Getter方法.
	   *
	   * 创建日期:2008-4-14
	   * @return String
	   */
	 public String getPk_sample() {
		 return pk_sample;
	  }   
	  
     /**
	   * 属性pk_sample的Setter方法.
	   *
	   * 创建日期:2008-4-14
	   * @param newPk_sample String
	   */
	public void setPk_sample(String newPk_sample) {
		
		pk_sample = newPk_sample;
	 } 	  
       
        /**
	   * 属性checktype的Getter方法.
	   *
	   * 创建日期:2008-4-14
	   * @return Integer
	   */
	 public String getChecktype() {
		 return checktype;
	  }   
	  
     /**
	   * 属性checktype的Setter方法.
	   *
	   * 创建日期:2008-4-14
	   * @param newChecktype Integer
	   */
	public void setChecktype(String newChecktype) {
		
		checktype = newChecktype;
	 } 	  
       
       
    /**
	  * 验证对象各属性之间的数据逻辑正确性.
	  *
	  * 创建日期:2008-4-14
	  * @exception nc.vo.pub.ValidationException 如果验证失败,抛出
	  * ValidationException,对错误进行解释.
	 */
	 public void validate() throws ValidationException {
	
	 	ArrayList errFields = new ArrayList(); // errFields record those null

                                                      // fields that cannot be null.
       		  // 检查是否为不允许空的字段赋了空值,你可能需要修改下面的提示信息:
	
	   		if (pk_checkreport == null) {
			errFields.add(new String("pk_checkreport"));
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
	  * 创建日期:2008-4-14
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	  	 
	 	    return null;
	 	
	}   
    
    /**
	  * <p>取得表主键.
	  * <p>
	  * 创建日期:2008-4-14
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	 	  return "pk_checkreport";
	 	}
    
	/**
      * <p>返回表名称.
	  * <p>
	  * 创建日期:2008-4-14
	  * @return java.lang.String
	 */
	public java.lang.String getTableName() {
				
		return "eh_stock_checkreport";
	}    
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:2008-4-14
	  */
	public StockCheckreportVO() {
			
			   super();	
	  }    
    
            /**
	 * 使用主键进行初始化的构造子.
	 *
	 * 创建日期:2008-4-14
	 * @param newPk_checkreport 主键值
	 */
	 public StockCheckreportVO(String newPk_checkreport) {
		
		// 为主键字段赋值:
		 pk_checkreport = newPk_checkreport;
	
    	}
    
     
     /**
	  * 返回对象标识,用来唯一定位对象.
	  *
	  * 创建日期:2008-4-14
	  * @return String
	  */
	   public String getPrimaryKey() {
				
		 return pk_checkreport;
	   
	   }

     /**
	  * 设置对象标识,用来唯一定位对象.
	  *
	  * 创建日期:2008-4-14
	  * @param newPk_checkreport  String    
	  */
	 public void setPrimaryKey(String newPk_checkreport) {
				
				pk_checkreport = newPk_checkreport; 
				
	 } 
           
	  /**
       * 返回数值对象的显示名称.
	   *
	   * 创建日期:2008-4-14
	   * @return java.lang.String 返回数值对象的显示名称.
	   */
	 public String getEntityName() {
				
	   return "eh_stock_checkreport"; 
				
	 }

	public String getRk_flag() {
		return rk_flag;
	}

	public void setRk_flag(String newrk_flag) {
		this.rk_flag = newrk_flag;
	}

	public String getPk_sbbills() {
		return pk_sbbills;
	}

	public void setPk_sbbills(String pk_sbbills) {
		this.pk_sbbills = pk_sbbills;
	}

	public UFDouble getRkamount() {
		return rkamount;
	}

	public void setRkamount(UFDouble rkamount) {
		this.rkamount = rkamount;
	}

	public String getPk_receipt_b() {
		return pk_receipt_b;
	}

	public void setPk_receipt_b(String pk_receipt_b) {
		this.pk_receipt_b = pk_receipt_b;
	}

	public String getVsbbilltype() {
		return vsbbilltype;
	}

	public void setVsbbilltype(String vsbbilltype) {
		this.vsbbilltype = vsbbilltype;
	}

    /**
     * @return the ispass
     */
    public String getIspass() {
        return ispass;
    }

    /**
     * @param ispass the ispass to set
     */
    public void setIspass(String ispass) {
        this.ispass = ispass;
    }

	public UFDouble getDnum() {
		return dnum;
	}

	public void setDnum(UFDouble dnum) {
		this.dnum = dnum;
	}

    /**
     * @return the pk_procheckapplys
     */
    public String getPk_procheckapplys() {
        return pk_procheckapplys;
    }

    /**
     * @param pk_procheckapplys the pk_procheckapplys to set
     */
    public void setPk_procheckapplys(String pk_procheckapplys) {
        this.pk_procheckapplys = pk_procheckapplys;
    }

    /**
     * @return the allpk
     */
    public String getAllpk() {
        return allpk;
    }

    /**
     * @param allpk the allpk to set
     */
    public void setAllpk(String allpk) {
        this.allpk = allpk;
    }

	public UFDouble getKj() {
		return kj;
	}

	public void setKj(UFDouble kj) {
		this.kj = kj;
	}

	public UFDouble getKz() {
		return kz;
	}

	public void setKz(UFDouble kz) {
		this.kz = kz;
	}

	public UFDouble getTkj() {
		return tkj;
	}

	public void setTkj(UFDouble tkj) {
		this.tkj = tkj;
	}

	public String getOaspcode() {
		return oaspcode;
	}

	public void setOaspcode(String oaspcode) {
		this.oaspcode = oaspcode;
	}

	public String getRkbillno() {
		return rkbillno;
	}

	public void setRkbillno(String rkbillno) {
		this.rkbillno = rkbillno;
	}

	public String getTh_flag() {
		return th_flag;
	}

	public void setTh_flag(String th_flag) {
		this.th_flag = th_flag;
	}

	public String getPk_in() {
		return pk_in;
	}

	public void setPk_in(String pk_in) {
		this.pk_in = pk_in;
	} 
    
    
    
} 
