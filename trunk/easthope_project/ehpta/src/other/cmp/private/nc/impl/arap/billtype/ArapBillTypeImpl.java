/**
 * @(#)ArapBillTypeImpl.java	V5.0 2005-11-9
 *
 * Copyright 1988-2005 UFIDA, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of UFSoft, Inc.
 * Use is subject to license terms.
 *
 */

package nc.impl.arap.billtype;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.naming.NamingException;

import nc.bs.arap.callouter_ss.FipCallFacade;
import nc.bs.arap.djlx.DjLXBO;
import nc.bs.arap.djlx.DjLXDMO;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.dbcache.intf.ICacheVersionBS;
import nc.bs.fipub.cache.FipubCacheContant;
import nc.bs.framework.cluster.itf.ClusterMessageHeader;
import nc.bs.framework.cluster.itf.ClusterSender;
import nc.bs.framework.cluster.itf.ClusterServiceException;
import nc.bs.framework.cluster.itf.ObjectClusterMessage;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.exception.ComponentException;
import nc.bs.framework.server.util.NewObjectService;
import nc.bs.logging.Log;
import nc.bs.logging.Logger;
import nc.bs.pub.SystemException;
import nc.cmp.pub.cache.FiPubDataCache;
import nc.itf.arap.prv.IArapBillTypePrivate;
import nc.itf.arap.pub.IArapBillTypePublic;
import nc.vo.arap.bdcontrastinfo.BdcontrastinfoVO;
import nc.vo.arap.djlx.BillTypeVO;
import nc.vo.arap.djlx.DjLXVO;
import nc.vo.arap.exception.ExceptionHandler;
import nc.vo.arap.global.ArapCommonTool;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.sxsp.init.CMPOuterfaceVO;
import nc.vo.sxsp.init.IItemUsedQry;

/**
 * <p>
 *   类的主要说明。类设计的目标，完成什么样的功能。
 * </p>
 * <p>
 * <Strong>主要的类使用：</Strong>
 *  <ul>
 * 		<li>如何使用该类</li>
 *      <li>是否线程安全</li>
 * 		<li>并发性要求</li>
 * 		<li>使用约束</li>
 * 		<li>其他</li>
 * </ul>
 * </p>
 * <p>
 * <Strong>已知的BUG：</Strong>
 * 	<ul>
 * 		<li></li>
 *  </ul>
 * </p>
 *
 * <p>
 * <strong>修改历史：</strong>
 * 	<ul>
 * 		<li><ul>
 * 			<li><strong>修改人:</strong>st</li>
 * 			<li><strong>修改日期：</strong>2005-11-9</li>
 * 			<li><strong>修改内容：<strong></li>
 * 			</ul>
 * 		</li>
 * 		<li>
 * 		</li>
 *  </ul>
 * </p>
 *
 * @author st
 * @version V5.0
 * @since V3.1
 */

public class ArapBillTypeImpl implements IArapBillTypePrivate, IArapBillTypePublic {

    /**
     *
     */
    public ArapBillTypeImpl() {
        super();
        //
    }



    /**
     * @see nc.itf.arap.prv.IArapBillTypePrivate#updateBillType(nc.vo.arap.djlx.DjLXVO, nc.vo.arap.djlx.DjlxtempletVO[])
     */
    public BillTypeVO updateBillType(BillTypeVO billtypevo) throws BusinessException {
        //
    	DjLXVO djlx = (DjLXVO)billtypevo.getParentVO();
//    	DjlxtempletVO[] templetvos = (DjlxtempletVO[])billtypevo.getChildrenVO();
    	checkUnique(djlx);

    	BaseDAO dao = new BaseDAO();
    	dao.updateVO(djlx);
    	updatecacherversion(djlx);
    	FiPubDataCache.clearCache(new String[]{djlx.getDwbm()});
    	clearClusterCache(new String[]{djlx.getDwbm()});
//    	dao.updateVOArray(templetvos);
    	return billtypevo;
//        new DjLXBO().update(djlx,templetvos);
    }

    /**
     * @see nc.itf.arap.prv.IArapBillTypePrivate#checkBillTypeUnique(nc.vo.arap.djlx.DjLXVO)
     */
    public boolean checkBillTypeUnique(DjLXVO billtypeVo) throws BusinessException {
        //
        boolean flag= checkUnique(billtypeVo);
        if(!flag){
    		throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("20060101","UPP20060101-000052"));
    	}
        return flag;
    }

    /**
     * @see nc.itf.arap.prv.IArapBillTypePrivate#deleteCorpsBillType(nc.vo.arap.djlx.DjLXVO, java.lang.String[])
     */
    public Hashtable deleteCorpsBillType(BillTypeVO billtypeVo, String[] pk_corps) throws BusinessException {
        //

        Hashtable hash =new DjLXBO().deleteCorps(billtypeVo,pk_corps);
        for(int i=0;i<pk_corps.length;i++){
        	DjLXVO djlx=(DjLXVO) billtypeVo.getParentVO();
        	String temppk_corp = djlx.getDwbm();
        	djlx.setDwbm(pk_corps[i]);
        	updatecacherversion(djlx);
        	djlx.setDwbm(temppk_corp);
        }
        FiPubDataCache.clearCache(pk_corps);
        clearClusterCache(pk_corps);
        return hash;
    }



    /**
     * @see nc.itf.arap.prv.IArapBillTypePrivate#insertBillType2Corps(nc.vo.arap.djlx.DjLXVO, nc.vo.arap.djlx.DjlxtempletVO[], java.lang.String[])
     */
    public Hashtable<String,String> insertBillType2Corps(BillTypeVO billtypevo, String[] pk_corps) throws BusinessException {
    	DjLXVO djlx = (DjLXVO)billtypevo.getParentVO();
//    	DjlxtempletVO[] templetVos = (DjlxtempletVO[]) billtypevo.getChildrenVO();
    	try {
    		boolean flag = true;
    		Hashtable<String,String> corpcode=checkDjLX(djlx,pk_corps);
              for(int i=0;i<pk_corps.length;i++)
              {
                if(corpcode.get(pk_corps[i])!=null)
                  continue;
                djlx.setDwbm(pk_corps[i]);
                djlx.setDjlxoid(null);
                flag = checkUniqueWithoutException(djlx);
                if(flag){
                	insertDjlx(djlx);
                }else{
                	corpcode.put(pk_corps[i],pk_corps[i]);
                }
              }
              FiPubDataCache.clearCache(pk_corps);
              clearClusterCache(pk_corps);
              return corpcode;
    } catch (BusinessException e) {
        Log.getInstance(this.getClass()).error(e.getMessage());
	      throw e;
    }
    }

    /**
     * @see nc.itf.arap.prv.IArapBillTypePrivate#queryBillType(java.lang.String)
     */
    @SuppressWarnings("unchecked")
	public BillTypeVO[] queryBillType(String pk_corp) throws BusinessException {

    	BillTypeVO[] djLXs = null;
        Vector<BillTypeVO> vec = new Vector<BillTypeVO>();
            DjLXVO[] headers = null;
            BaseDAO dao = new BaseDAO();
            Collection<DjLXVO> cl = dao.retrieveByClause(DjLXVO.class,"dwbm='"+pk_corp+"'");
            headers = new DjLXVO[cl.size()];
            headers = (DjLXVO[])cl.toArray(headers);
            if(headers==null||headers.length==0)
                return djLXs;
            translateBilltypes(headers);
            for(int i=0;i<headers.length;i++){
                BillTypeVO atype = new BillTypeVO();
                atype.setParentVO(headers[i]);
                vec.addElement(atype);
    	    }
        if (vec.size() > 0) {
            djLXs = new BillTypeVO[vec.size()];
            vec.copyInto(djLXs);

        }
        return djLXs;
    }


    @SuppressWarnings("unchecked")
	public BillTypeVO[] queryBillTypeByBillTypeCode(String billtypeCode, String pk_corp) throws BusinessException {
    	if(StringUtil.isEmptyWithTrim(pk_corp)){
    		String sWhere ="";
        	if(billtypeCode!=null){
        	    sWhere += "and djlxbm='"+billtypeCode+"' ";
            }
            if (pk_corp != null)
                sWhere += "and dwbm='" + pk_corp + "' ";
        	if(sWhere.length()>1){
        		sWhere =sWhere.substring(3);
        	}
        	BillTypeVO[] djLXs = null;
            Vector<BillTypeVO> vec = new Vector<BillTypeVO>();
                DjLXVO[] headers = null;
                BaseDAO dao = new BaseDAO();
                Collection<DjLXVO> cl = dao.retrieveByClause(DjLXVO.class,sWhere);
                headers = new DjLXVO[cl.size()];
                headers = (DjLXVO[])cl.toArray(headers);
                if(headers==null||headers.length==0)
                    return djLXs;

                for(int i=0;i<headers.length;i++){
                	FiPubDataCache.translateOneBilltype(headers[i]);
                    BillTypeVO atype = new BillTypeVO();
                    atype.setParentVO(headers[i]);
                    vec.addElement(atype);
        	    }
            if (vec.size() > 0) {
                djLXs = new BillTypeVO[vec.size()];
                vec.copyInto(djLXs);

            }
            return djLXs;

    	}
    	DjLXVO head = FiPubDataCache.getBillType(billtypeCode, pk_corp);
    	if(head==null){
    		return null;
    	}
    	FiPubDataCache.translateOneBilltype(head);
    	BillTypeVO vo = new BillTypeVO();
    	vo.setParentVO(head);
        return new BillTypeVO[]{vo};
    }

    public DjLXVO[] queryByWhereStr(String where) throws BusinessException {
    	return translateBilltypes(new DjLXBO().queryByWhereStr(where));
    }



    /**
     * @see nc.itf.arap.pub.IArapBillTypePublic#queryAllBillTypeByCorp(java.lang.String)
     */
    public DjLXVO[] queryAllBillTypeByCorp(String pk_corp) throws BusinessException {
        return translateBilltypes(new DjLXBO().queryAll(pk_corp));
    }

    /**
     * @see nc.itf.arap.pub.IArapBillTypePublic#deleteBillType(nc.vo.arap.djlx.DjLXVO)
     */
    public void deleteBillType(BillTypeVO billtypevo) throws BusinessException {
    	DjLXVO vo =(DjLXVO) billtypevo.getParentVO();
    	if(vo.getIsdefault()!=null && vo.getIsdefault().booleanValue()){
    		throw ExceptionHandler.createException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("businessbill","UPPbusinessbill-000103")/*@res "默认交易类型不能删除!"*/);
    	}else if (isRefered(vo)){

        	String djlxjc=vo.getDjlxmc();
        	/*@res "单据{0}被引用,不能删除。"*/
        	throw new nc.vo.pub.BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20060101","UPP20060101-000000",null,new String[]{djlxjc}));
        } else {
        	BaseDAO dao = new BaseDAO();
        	dao.deleteByPK(DjLXVO.class,vo.getDjlxoid());
            String strBm = vo.getDjlxbm();
//        	Proxy.getIPFBillCopy().deleteBill(strBm);
        	new FipCallFacade().deleteBill(strBm);
        	updatecacherversion(vo);
        	FiPubDataCache.clearCache(new String[]{vo.getDwbm()});
        	clearClusterCache(new String[]{vo.getDwbm()});
        }
    }
    /**
     * *方法说明:
     * *参数:
     * *返回值:
     * ***注意点***
     * *@author：屈淑轩
     * *创建日期：(2001-12-19 13:15:21)
     */
    private  boolean isRefered(DjLXVO vo){
    	BaseDAO dao = new BaseDAO();
    	try {
    		Collection cl = dao.retrieveByClause(DjLXVO.class,"dwbm <>'0001' and djlxbm ='"+vo.getDjlxbm()+"'");
    		if(cl.size()>0){
    			return true;
    		}else{
    			return false;
    		}

    	} catch (DAOException e) {
    		Logger.error(this,e);
    		throw new BusinessRuntimeException(e.getMessage());
    	}
    }

    /**
     * @see nc.itf.arap.pub.IArapBillTypePublic#insertBillType(nc.vo.arap.djlx.DjLXVO, nc.vo.arap.djlx.DjlxtempletVO[])
     */
    public BillTypeVO insertBillType(BillTypeVO billtypevo) throws BusinessException {
    	DjLXVO djlx = (DjLXVO) billtypevo.getParentVO();
    	djlx.setDr(0);
//    	DjlxtempletVO[] templetvos = (DjlxtempletVO[])billtypevo.getChildrenVO();
    	Vector<String> parentID = new Vector<String>();
        Vector<String> childID = new Vector<String>();
        for (int i = 0; i < 9; i++) {
            parentID.addElement("D" + i);
        }
        parentID.addElement("DZ");
        parentID.addElement("DV");
        parentID.addElement("2330");
        parentID.addElement("2340");

        childID.addElement("ys");
        childID.addElement("yf");
        childID.addElement("sk");
        childID.addElement("fk");
        childID.addElement("sj");
        childID.addElement("fj");
        childID.addElement("hj");
        childID.addElement("ws");
        childID.addElement("wf");
        childID.addElement("ss");
        childID.addElement("yt");
        childID.addElement("jk");
        childID.addElement("bx");

        try {
        	checkUnique(djlx);
        	insertDjlx(djlx);

            /**传递*/
            String strDestID = djlx.getDjlxbm();
            String djdl = djlx.getDjdl();
            int index = childID.indexOf(djdl);
            String strSourceID = parentID.elementAt(index).toString();
            String djmc = djlx.getDjlxmc();


            new FipCallFacade().copybill(strDestID, strSourceID, djmc, "",null);

           updatecacherversion(djlx);
           FiPubDataCache.clearCache(new String[]{djlx.getDwbm()});
        } catch(ComponentException e){
        	Logger.debug("没有找到会计平台提供的增加单据类型接口，应该是会计平台的问题，主流程不受影响");
        }catch (BusinessException e) {
            ExceptionHandler.handleException(e);
        }
        return billtypevo;
    }

	private void insertDjlx(DjLXVO djlx) throws DAOException, BusinessException {

		BaseDAO dao = new BaseDAO();
		dao.insertVO(djlx);
		updatecacherversion(djlx);
	}



	private void updatecacherversion(DjLXVO djlx) {
		ICacheVersionBS cv = (ICacheVersionBS) NCLocator.getInstance().lookup(ICacheVersionBS.class);

		cv.updateVersion(FiPubDataCache.STR_BILLTYPE+"_"+djlx.getDwbm());
	}

    /**
     * @see nc.itf.arap.pub.IArapBillTypePublic#getDjlxvoByDjlxbm(java.lang.String, java.lang.String)
     */
    public DjLXVO getDjlxvoByDjlxbm(String billTypeCode, String pk_corp) throws BusinessException {
    	DjLXVO vo = FiPubDataCache.getBillType(billTypeCode, pk_corp);
//            BillTypeVO[] billtypes = queryBillTypeByBillTypeCode(billTypeCode, pk_corp);
            if(vo==null ){
            	throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2006","UPP2006-000390")/*@res "当前公司没有分配该单据类型！"*/);
            }
            FiPubDataCache.translateOneBilltype(vo);
            return vo;
    }

    public DjLXVO[] getBillSsCtlTypes(String pk_corp) throws BusinessException {
    	// TODO Auto-generated method stub
    	List<DjLXVO> ret=new ArrayList<DjLXVO>();
    	Collection<CMPOuterfaceVO> col=new BaseDAO().retrieveByClause(CMPOuterfaceVO.class, " actname='itemcfgused'");
    	for(CMPOuterfaceVO vo:col){
    		IItemUsedQry user=(IItemUsedQry) NewObjectService.newInstance(vo.getSystem(), vo.getClassname());
    		ret.addAll(user.getBillSsCtlTypes(pk_corp));
    	}
    	return ret.toArray(new DjLXVO[]{});
    }

    /**
     * @see nc.itf.arap.prv.IArapBillTypePrivate#getBillTypesByWhere(java.lang.String)
     */
    public DjLXVO[] getBillTypesByWhere(String condition) throws BusinessException {
    	BaseDAO dao = new BaseDAO();
    	if(condition !=null && (condition.startsWith("where"))){
    		condition = condition.substring(5);
    	}
    	Collection cl = dao.retrieveByClause(DjLXVO.class,condition);
    	return translateBilltypes((DjLXVO[])ArapCommonTool.changeCollection2Array(cl,DjLXVO.class));
//        try {
//            //
//            return new DjLXDMO().getBillTypesByWhere(condition);
//        } catch (SystemException e) {
//            Log.getInstance(this.getClass()).error(e.getMessage());
//            throw new BusinessRuntimeException(e.getMessage());
//        } catch (NamingException e) {
//            Log.getInstance(this.getClass()).error(e.getMessage());
//            throw new BusinessRuntimeException(e.getMessage());
//        } catch (SQLException e) {
//            Log.getInstance(this.getClass()).error(e.getMessage());
//            throw new BusinessRuntimeException(e.getMessage());
//        }
    }
    /**
     * 此处插入方法说明。
     * 创建日期：(2001-8-29 10:29:48)
     * @user
     * @return boolean
     * @param vo nc.vo.arap.djlx.DjLXVO
     * @exception java.sql.SQLException 异常说明。
     */
    private boolean checkUnique(DjLXVO vo) throws BusinessException {
    	boolean bool=checkUniqueWithoutException(vo);
    		if(!bool){
    			throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("20060101","UPP20060101-000052"));
    		}
    		return bool;

    }
    /**
     * 此处插入方法说明。
     * 创建日期：(2001-8-29 10:29:48)
     * @user
     * @return boolean
     * @param vo nc.vo.arap.djlx.DjLXVO
     * @exception java.sql.SQLException 异常说明。
     */
    private boolean checkUniqueWithoutException(DjLXVO vo) throws BusinessException {
    	boolean bool= true;
    		DjLXDMO dmo;
			try {
				dmo = new DjLXDMO();

    		bool = dmo.checkUnique(vo);
			} catch (SystemException e) {
				Log.getInstance(this.getClass()).equals(e);
				throw new BusinessRuntimeException(e.getMessage());
			} catch (NamingException e) {
				Log.getInstance(this.getClass()).equals(e);
				throw new BusinessRuntimeException(e.getMessage());
			}
    		return bool;

    }
//  检查要插入的单据类型该公司是否已经存在  2004-8-3 xhb
    private Hashtable<String,String> checkDjLX(DjLXVO djlx,String[] corps) throws BusinessException
    {
      Hashtable<String,String> corpcode=new Hashtable<String, String>();

        BillTypeVO[] vos=queryBillTypeByBillTypeCode(djlx.getDjlxbm(),null);
        Hashtable<String,BillTypeVO> hash = new Hashtable<String, BillTypeVO>();

        if(vos!=null){
    	    for(int i=0;i<vos.length;i++)
    	    {
    	    	hash.put(((DjLXVO)vos[i].getParentVO()).getDwbm(),vos[i]);
    	    }
    	    for(int i=0;i<corps.length;i++)
    	    {
    	    	if(hash.get(corps[i])!=null){
    	    		corpcode.put(corps[i],corps[i]);
    	    	}
    	    }
        }

      return corpcode;
    }



	public Hashtable<String,String> getDjlxbmbyBillPks(String tabname, ArrayList alPks, Hashtable<String,String> hashResult) throws BusinessException {
		// TODO Auto-generated method stub
		DjLXDMO dmo;
			try {
				dmo = new DjLXDMO();
				return dmo.getDjlxbmbyBillPks(tabname, alPks, hashResult);
			} catch ( Exception e) {
				// TODO Auto-generated catch block
				throw ExceptionHandler.handleException(this.getClass(), e);
			}


	}

	public BdcontrastinfoVO[] queryAllBdcontrastinfoVO(String pk_corp) throws BusinessException {
		//
		try {
			return new DjLXDMO().queryAll(pk_corp);
		}  catch ( Exception e) {
			// TODO Auto-generated catch block
			throw ExceptionHandler.handleException(this.getClass(), e);
		}

	}



	public Vector<DjLXVO> getByPrimaryKeys(String[] djlxoids, String pk_corp) throws BusinessException  {
		// TODO Auto-generated method stub
		Vector<DjLXVO> vResult = new Vector<DjLXVO>();
		if(djlxoids!=null){
			for(String djlxoid:djlxoids){
				vResult.add(FiPubDataCache.getBillType(djlxoid, pk_corp));
			}
		}
//		try {
//			return new DjLXDMO().getByPrimaryKeys(djlxoids, pk_corp);
//		} catch ( Exception e) {
//			// TODO Auto-generated catch block
//			throw ExceptionHandler.handleException(e);
//		}
		return vResult;
	}



	public Map<String, String> getDjdlByDjlxbm(String[] djlxbms) throws BusinessException {
		// TODO Auto-generated method stub
		Map<String,String> retMap=new HashMap<String,String>();
		if(djlxbms!=null){
			for(String djlxbm:djlxbms){
				retMap.put(djlxbm, FiPubDataCache.getBillType(djlxbm, "0001").getDjdl());
			}
		}
		return retMap;
//		try {
//			return new DjLXDMO().getDjdlByDjlxbm(djlxbms);
//		} catch ( Exception e) {
//			// TODO Auto-generated catch block
//			throw ExceptionHandler.handleException(e);
//		}
	}
	private void clearClusterCache(String[] pk_corps)throws BusinessException{
		try {
			ObjectClusterMessage message = new ObjectClusterMessage();
			//进行广播的消息类型的信息
			ClusterMessageHeader header = new ClusterMessageHeader(FipubCacheContant.CLUSTER_MSG_HEADER_MESSAGETYPE, FipubCacheContant.MESSAGETYPE);
			message.addHeader(header);

			//操作类型
			ClusterMessageHeader header1 = new ClusterMessageHeader(FipubCacheContant.CLUSTER_MSG_HEADER_OPERATETYPE, FipubCacheContant.CLUSTER_MSG_HEADER_OPERATETYPE_UPDATECACHE);
			message.addHeader(header1);
			message.setObject(pk_corps);
				ClusterSender sender = NCLocator.getInstance().lookup(ClusterSender.class);
				sender.sendMessage(message);
			Logger.debug("*****fipubCache集群消息发送结束*****");

		} catch (ClusterServiceException e) {
			Logger.error(e.getMessage(), e);
		}
	}
private DjLXVO[] translateBilltypes(DjLXVO[] djlxvos){
	if(djlxvos==null || djlxvos.length==0){
		return djlxvos;
	}
	for(DjLXVO vo:djlxvos){
		FiPubDataCache.translateOneBilltype(vo);
	}
	return djlxvos;
}



}

