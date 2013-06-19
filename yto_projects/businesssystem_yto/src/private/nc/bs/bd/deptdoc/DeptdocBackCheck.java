/*
 * 创建日期 2006-1-20
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.bs.bd.deptdoc;

import java.util.ArrayList;
import java.util.List;

import nc.bs.bd.cache.BDDelLog;
import nc.bs.bd.cache.CacheProxy;
import nc.bs.bd.service.BDOperateServ;
import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.generate.Gener;
import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.bduifactory.busicom.BDDocSyncInform;
import nc.bs.trade.business.IBDBusiCheck;
import nc.bs.trade.comcheckunique.BillIsUnique;
import nc.bs.trade.comcheckunique.IBillIsUnique;
import nc.bs.uap.bd.BDException;
import nc.bs.util.SleepTime;
import nc.bs.util.Uriread;
import nc.bs.yto.client.FilePost;
import nc.itf.uap.IVOPersistence;
import nc.itf.uap.bd.dept.IDeptdocQry;
import nc.itf.uap.bd.innercode.IInnerCodeService;
import nc.itf.uap.bd.refcheck.IReferenceCheck;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.bd.access.IBdinfoConst;
import nc.vo.bd.b04.DeptdocVO;
import nc.vo.bd.b04.InnerDeptCode;
import nc.vo.bd.service.IBDOperate;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.filemanage.BDAssociateFileUtil;
import nc.vo.pub.filemanage.IBDFileManageConst;
import nc.vo.trade.bdinfo.IBdinfoContext;
import nc.vo.trade.comcheckunique.IUniqueFieldCheck2;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IBDACTION;

/**
 * @author xuchao
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class DeptdocBackCheck implements IBDACTION, IBDBusiCheck,
		IUniqueFieldCheck2 {
	private int operateType;

	private final int INSERT = 0;

	private final int UPDATE = 1;

	private BDOperateServ opServ = null;

	private IDeptdocQry deptQryService = null;

	private BDDocSyncInform bdDocSync=null;
	
	private boolean isCancel=false;
	
	private DeptdocVO delDeptVO=null;
	
	private DeptdocVO oldDeptVO=null;
	
	private List<nc.vo.yto.business.DeptdocVO> DeptdocDel = new ArrayList<nc.vo.yto.business.DeptdocVO>();
	private List<nc.vo.yto.business.DeptdocVO> DeptdocDel2 = new ArrayList<nc.vo.yto.business.DeptdocVO>();
	
	public DeptdocBackCheck() {
		super();

	}

	public ArrayList getFieldArray() {
		ArrayList list = new ArrayList();
		list.add(new String[] { "deptcode", "pk_corp" });
		list.add(new String[] { "deptname", "pk_corp" });
		return list;
	}

	public ArrayList getNameArray() {
		ArrayList list = new ArrayList();
		list.add(new String[] {
				NCLangResOnserver.getInstance().getStrByID("10080404",
						"UC000-0004073"), "公司"/* @res "部门编码" */});
		list.add(new String[] {
				NCLangResOnserver.getInstance().getStrByID("10080404",
						"UC000-0004069"), "公司"/* @res "部门名称" */});
		return list;
	}

	/* （非 Javadoc）
	 * @see nc.bs.trade.business.IBDBusiCheck#check(int, nc.vo.pub.AggregatedValueObject, java.lang.Object)
	 */
	public void check(int intBdAction, AggregatedValueObject vo, Object userObj)
			throws Exception {
		if (vo == null || vo.getParentVO() == null)
			return;
		DeptdocVO deptdocVO = (DeptdocVO) vo.getParentVO();
		if (intBdAction == IBDACTION.SAVE) {

			onCheckUnique(vo);

			
			if (deptdocVO.getPrimaryKey() == null
					|| deptdocVO.getPrimaryKey().length() == 0) {
				doBeforeInsert(deptdocVO);
				operateType = INSERT;
			} else {
				doBeforeUpdate(deptdocVO);
				operateType = UPDATE;
			}

		}else if(intBdAction==IBDACTION.DELETE) {
			doBeforeDelete(deptdocVO);
		}

	}

	/* （非 Javadoc）
	 * @see nc.bs.trade.business.IBDBusiCheck#dealAfter(int, nc.vo.pub.AggregatedValueObject, java.lang.Object)
	 */
	public void dealAfter(int intBdAction, AggregatedValueObject billVo,
			Object userObj) throws Exception {
		if (intBdAction == IBDACTION.SAVE) {
			if (operateType == INSERT) {
				doAfterInsert((DeptdocVO) billVo.getParentVO());
                CacheProxy.fireDataInserted("bd_deptdoc",null);
			} else if (operateType == UPDATE) {
				doAfterUpdate((DeptdocVO) billVo.getParentVO());
                CacheProxy.fireDataUpdated("bd_deptdoc",null);
			}

		}else if(intBdAction == IBDACTION.DELETE) {
			doAfterDelete(delDeptVO);
		}

	}

	/**
	 * @param vo
	 * @throws Exception
	 */
	private void onCheckUnique(AggregatedValueObject vo) throws Exception {
		IBillIsUnique uniCheck = new BillIsUnique();
		try {
			uniCheck.checkBDisUnique((HYBillVO) vo, this.getClass().getName());
		} catch (BusinessException e) {
			throw new BusinessException(e.getMessage());
		}
	}
    private void doBeforeDelete(DeptdocVO deptdocVO) throws BusinessException {
    	checkBeforeDelete(deptdocVO.getPrimaryKey());
		//要删除的部门是否存在
		
		delDeptVO = getService().findDeptdocVOByPK(deptdocVO.getPrimaryKey());
		if (delDeptVO == null || delDeptVO.getPrimaryKey()==null || delDeptVO.getPrimaryKey().length() <=0){
			throw new BDException(nc.vo.bd.BDMsg.MSG_REFRESH()/*已发生并发操作，请刷新数据!*/);
		}

		//调用处理前接口
		getBdOperate().beforeOperate("10080404",IBDOperate.BDOPERATION_DEL,deptdocVO.getPrimaryKey(),null,delDeptVO);
        
		
		// 删除部门档案时同步至中间表 add by river for 2011-09-13
		
		nc.vo.yto.business.DeptdocVO deptdocvo = new nc.vo.yto.business.DeptdocVO();
		for(String attr : delDeptVO.getAttributeNames()) {
			deptdocvo.setAttributeValue(attr, delDeptVO.getAttributeValue(attr));
		}
		
		String retStr = new FilePost().postFile(Uriread.uriPath() , 
				new Gener().generateXml3(deptdocvo, "RequestDeptdoc", "dept", "del"));
		
		String[] strs = retStr.split("<success>");
		String retMsg = "";
		if (strs.length > 1)
			retMsg = strs[1].substring(0, strs[1].indexOf("<"));
		
		if (retMsg.equals("false") || strs.length <= 1) {
			DeptdocDel.add(deptdocvo);
			new Thread() {
				public void run() {
					try {
						if(true) {
							this.sleep(SleepTime.Time);
						
							for(nc.vo.yto.business.DeptdocVO dept : DeptdocDel) {
								String retStr = new FilePost().postFile(Uriread.uriPath() , 
										new Gener().generateXml3(dept, "RequestDeptdoc", "dept", "del"));
								
								String[] strs = retStr.split("<success>");
								String retMsg = "";
								if (strs.length > 1)
									retMsg = strs[1].substring(0, strs[1].indexOf("<"));
							
								if(retMsg.equals("false") || strs.length <= 1)
									DeptdocDel2.add(dept);
							}
							
							DeptdocDel = DeptdocDel2;
							
							DeptdocDel2 = new ArrayList<nc.vo.yto.business.DeptdocVO>();
							
//							if(DeptdocDel.size() == 0)
//								break;
							
							
						}
						System.out.println("<<<<<<  部门档案删除线程停止！ >>>>>>");
						System.out.println("<<<<<<  线程号: " + this.getId() + " >>>>>>");
						this.stop();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}.start();
		}
	}
    private void doAfterDelete(DeptdocVO deptdocVO) throws BusinessException {
    	IVOPersistence iVOPersistence = (IVOPersistence) NCLocator.getInstance()
		        .lookup(IVOPersistence.class.getName());
         iVOPersistence.deleteVO(deptdocVO);
        new BDAssociateFileUtil(IBDFileManageConst.DEPT_FILEMANAGE_PATH).deleteAssociateFile(deptdocVO, deptdocVO.getPk_corp());
    	//    	调用处理后接口
    	CacheProxy.fireDataDeleted("bd_deptdoc",deptdocVO.getPrimaryKey());
    	new BDDelLog().delPKs(deptdocVO.getTableName(),new String[] {deptdocVO.getPrimaryKey()});
    	getBdOperate().afterOperate("10080404",IBDOperate.BDOPERATION_DEL,deptdocVO.getPrimaryKey(),null,delDeptVO);

	} 
	/**
	 * @param deptdocVO
	 * @throws BusinessException
	 */
	private void doBeforeInsert(DeptdocVO deptdocVO) throws BusinessException {

		getBdOperate().beforeOperate("10080404", IBDOperate.BDOPERATION_INSERT,
				deptdocVO.getPrimaryKey(), null, deptdocVO);
		
		insertInnerCode(deptdocVO);
	}

	/**
	 * @return
	 */
	private BDOperateServ getBdOperate() {
		if (opServ == null)
			opServ = new BDOperateServ();
		return opServ;
	}

	/**
	 * @param deptdocVO
	 * @throws BusinessException
	 */
	private void doBeforeUpdate(DeptdocVO deptdocVO) throws BusinessException {
		oldDeptVO = getService().findDeptdocVOByPK(
				deptdocVO.getPrimaryKey());
		
		if (oldDeptVO == null || oldDeptVO.getPrimaryKey()==null || oldDeptVO.getPrimaryKey().length() <=0){
			throw new BDException(nc.vo.bd.BDMsg.MSG_REFRESH()/*已发生并发操作，请刷新数据!*/);
		}
		
		   //是否撤销
        isCancel = beforeCancle(getBdOperate(),deptdocVO,oldDeptVO); 
		if (isTreeLoop(deptdocVO)) {
			throw new BDException(
					nc.vo.bd.MultiLangTrans
							.getTransStr(
									"MP1",
									new String[] { nc.bs.ml.NCLangResOnserver
											.getInstance().getStrByID(
													"10080404",
													"UPP10080404-000001") /*
																			 * @res
																			 * "将自身或下级设置为上级部门"
																			 */})); // 不能将自身或下级设置为上级部门
		}

		// 构造统一处理接口类
		BDDocSyncInform bdDocSync = getBdDocSyncInform();
		// 调用处理前接口
		bdDocSync.bdDocSyncUpdateBefore(oldDeptVO, deptdocVO);

		updateInnerCode(deptdocVO);

	}

	/**
	 * @return
	 */
	private BDDocSyncInform getBdDocSyncInform() {
		if(bdDocSync==null)
		bdDocSync = new BDDocSyncInform("10080404",
				"pk_deptdoc", "deptname", "deptcode");
		return bdDocSync;
	}

	private void doAfterInsert(DeptdocVO deptdocVO) throws BusinessException {
		getBdOperate().afterOperate("10080404", IBDOperate.BDOPERATION_INSERT,
				deptdocVO.getPrimaryKey(), null, deptdocVO);
		CacheProxy.fireDataInserted(deptdocVO.getTableName(), deptdocVO.getPrimaryKey());
	}

	/**
	 * @param deptdocVO
	 * @throws BusinessException
	 */
	private void doAfterUpdate(DeptdocVO deptdocVO) throws BusinessException {
		
		getBdDocSyncInform().bdDocSyncUpdateAfter(oldDeptVO,deptdocVO); 
		
		if(isCancel) {  //如果执行了封存
			afterCancel(getBdOperate(),deptdocVO,oldDeptVO);
		}
	}
	  /**
	 * @param bdOS
	 * @param deptdocVO
	 * @param oldVO
	 * @throws BusinessException
	 */
	private void afterCancel(BDOperateServ bdOS,DeptdocVO deptdocVO,DeptdocVO oldVO) throws BusinessException {
	        bdOS.afterOperate("10080404", nc.vo.bd.service.IBDOperate.BDOPERATION_SEAL, deptdocVO.getPrimaryKey(), null, null);
	    }
	/* （非 Javadoc）
	 * @see nc.vo.trade.comcheckunique.IUniqueFieldCheck#isDetail()
	 */
	public boolean isDetail() {

		return false;
	}

	/* （非 Javadoc）
	 * @see nc.vo.trade.comcheckunique.IUniqueFieldCheck#isSingleTable()
	 */
	public boolean isSingleTable() {

		return true;
	}

	/**
	 * @param vo
	 * @throws BusinessException
	 */
	private void insertInnerCode(DeptdocVO vo) throws BusinessException {

		try {
			InnerDeptCode innercode = new InnerDeptCode();
			IInnerCodeService iInnerCodeService = (IInnerCodeService) NCLocator.getInstance().lookup(
					IInnerCodeService.class.getName());
			String[] innercodes = null;

			innercodes = iInnerCodeService.getCodes(innercode, vo.getPk_fathedept(), vo
					.getPk_corp(), 1);
			vo.setInnercode(innercodes[0]);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new BDException(e.getMessage(), e);
		}

	}

	/**
	 * 
	 */
	private IDeptdocQry getService() {
		if (deptQryService == null)
			deptQryService = (IDeptdocQry) NCLocator.getInstance().lookup(
					IDeptdocQry.class.getName());
		return deptQryService;
	}

	/**
	 * @param bdOS
	 * @param deptdocVO
	 * @param oldVO
	 * @return
	 * @throws BusinessException
	 */
	private boolean beforeCancle(BDOperateServ bdOS, DeptdocVO deptdocVO,
			DeptdocVO oldVO) throws BusinessException {
		boolean isCancel = false; // 是否撤销
		if (deptdocVO.getCanceled().booleanValue()) {
			if (!oldVO.getCanceled().booleanValue()) { // 执行的是撤销操作
				checkDeptCancelable(deptdocVO);
				isCancel = true;
				// deptdoc.setCancelDate(new
				// nc.vo.pub.lang.UFDate(System.currentTimeMillis())); //设置封存时间
				bdOS.beforeOperate("10080404",
						nc.vo.bd.service.IBDOperate.BDOPERATION_SEAL, deptdocVO
								.getPrimaryKey(), null, null);
			}
		} else {
			// chenw 040702
			if (oldVO.getCanceled().booleanValue()) { // 修改前已经撤销?
//				if (oldVO.getHrcanceled() != null
//						&& oldVO.getHrcanceled().booleanValue()) {
//					throw new BDException(
//							nc.bs.ml.NCLangResOnserver.getInstance()
//									.getStrByID("10080404",
//											"UPP10080404-000030")/*
//																	 * @res
//																	 * "部门已被撤销"
//																	 */
//									+ ", "
//									+ nc.vo.bd.MultiLangTrans
//											.getTransStr(
//													"MP1",
//													new String[] { nc.bs.ml.NCLangResOnserver
//															.getInstance()
//															.getStrByID(
//																	"10080404",
//																	"UPP10080404-000005") /*
//																							 * @res
//																							 * "取消封存"
//																							 */})); // 部门已被撤销,不能取消封存
//				}
				if (deptdocVO.getPk_fathedept() != null) {
					DeptdocVO fVO = getService().findDeptdocVOByPK(deptdocVO
							.getPk_fathedept());
					if (fVO == null
							|| fVO.getPrimaryKey() == null
							|| (fVO != null && fVO.getCanceled() != null && !fVO
									.getCanceled().booleanValue())) { // 如果上级不存在,或上级没有被撤销
						deptdocVO.setCancelDate(null);
					} else {
						throw new BDException(
								nc.vo.bd.MultiLangTrans
										.getTransStr(
												"MO4",
												new String[] {
														nc.bs.ml.NCLangResOnserver
																.getInstance()
																.getStrByID(
																		"10080404",
																		"UPP10080404-000003")/*
																								 * @res
																								 * "封存"
																								 */,
														nc.bs.ml.NCLangResOnserver
																.getInstance()
																.getStrByID(
																		"10080404",
																		"UPP10080404-000005") /*
																								 * @res
																								 * "取消封存"
																								 */})); // 上级已封存,不能取消封存下级
					}
				}
			} else {
				deptdocVO.setCancelDate(null);
			}
		}
		return isCancel;
	}

	/**
	 * @param vo
	 * @return
	 * @throws BusinessException
	 */
	private boolean isTreeLoop(DeptdocVO vo) throws BusinessException {
		boolean isloop = false;
		String pk = vo.getPrimaryKey();
		String fpk = vo.getPk_fathedept();

		while (fpk != null) {
			if (pk.equals(fpk)) {
				isloop = true;
				break;
			}
			fpk = ((DeptdocVO) getService().findDeptdocVOByPK(fpk))
					.getPk_fathedept();
		}

		return isloop;
	}

	/**
	 * @param vo
	 * @throws BusinessException
	 */
	private void updateInnerCode(DeptdocVO vo) throws BusinessException {
		try {
			InnerDeptCode innercode = new InnerDeptCode();
			IInnerCodeService iInnerCodeService = (IInnerCodeService) NCLocator.getInstance().lookup(
					IInnerCodeService.class.getName());
			String[] innercodes = null;

			DeptdocVO oldvo = (DeptdocVO) new BaseDAO().retrieveByPK(
					DeptdocVO.class, vo.getPrimaryKey());
			String oldFather = oldvo.getPk_fathedept() == null
					|| oldvo.getPk_fathedept().trim().length() == 0 ? null
					: oldvo.getPk_fathedept().trim();
			String newFather = vo.getPk_fathedept() == null
					|| vo.getPk_fathedept().trim().length() == 0 ? null : vo
					.getPk_fathedept().trim();

			// 因待保存的VO从界面读取时没有内部编码信息，这里进行恢复
			vo.setInnercode(oldvo.getInnercode());
			vo.setMaxinnercode(oldvo.getMaxinnercode());

			// 如果父结点未变化，则不做其它处理
			if ((oldFather != null && oldFather.equalsIgnoreCase(newFather))
					|| (oldFather == null && newFather == null))
				return;

			String oldInnercode = oldvo.getInnercode();
			innercodes = iInnerCodeService.getCodes(innercode, vo.getPk_fathedept(), vo
					.getPk_corp(), 1);
			vo.setInnercode(innercodes[0]);
			if (oldInnercode != null
					&& !oldInnercode.equalsIgnoreCase(innercodes[0])) {
				iInnerCodeService.updateAllChildrenCodes(innercode, oldInnercode,
						innercodes[0], vo.getPk_corp());
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new BDException("Error when update inner code");
		}

	}

	/**
	 * 检查给定部门是否能够被撤销
	 * 
	 * @param deptdocVO
	 * @throws BusinessException
	 * @since NC5.0
	 *        <p>
	 *        <strong>调用模块：通用</strong>
	 *        <p>
	 *        <strong>最后修改人：liujian</strong>
	 *        <p>
	 *        <strong>最后修改日期：2005-12-5</strong>
	 *        <p>
	 */
	private void checkDeptCancelable(DeptdocVO deptdocVO)
			throws BusinessException {
		
		String key = deptdocVO.getPrimaryKey();

		if (key != null && key.length() != 0) {
			if( existChild(key)) {// 存在下级部门
				throw new BDException(nc.vo.bd.MultiLangTrans.getTransStr("MC6",
						new String[] {
								nc.bs.ml.NCLangResOnserver.getInstance()
										.getStrByID("10080404",
												"UPP10080404-000002")/*
																		 * @res
																		 * "下级部门"
																		 */, null })
						+ ","
						+ nc.vo.bd.MultiLangTrans.getTransStr("MP1",
								new String[] { nc.bs.ml.NCLangResOnserver
										.getInstance().getStrByID("10080404",
												"UPP10080404-000003") /*
																		 * @res "封存"
																		 */})); // 下级部门已经存在,不能封存
			} 
		}

	}

	/**
	 * 检查指定部门是否存在下级
	 * 
	 * @param pkDeptdoc
	 * @return
	 * @since NC5.0
	 *        <p>
	 *        <strong>调用模块：通用</strong>
	 *        <p>
	 *        <strong>最后修改人：liujian</strong>
	 *        <p>
	 *        <strong>最后修改日期：2005-12-5</strong>
	 *        <p>
	 */
	private boolean existChild(String pkDeptdoc) throws BusinessException {
		String sql = "select count(*) from bd_deptdoc where canceled = 'N' and pk_fathedept = ?";
		SQLParameter para = new SQLParameter();
		para.addParam(pkDeptdoc);
		Integer count = (Integer) new BaseDAO().executeQuery(sql, para,
				new ColumnProcessor());
		return count.intValue() > 0;
	}

	/**
	 * 检查指定部门是否存在人员
	 * 
	 * @param pkDeptdoc
	 *            部门主键
	 * @return 指定部门是否存在人员. 存在为ture,反之为false.
	 * @since NC5.0
	 *        <p>
	 *        <strong>调用模块：通用</strong>
	 *        <p>
	 *        <strong>最后修改人：liujian</strong>
	 *        <p>
	 *        <strong>最后修改日期：2005-12-5</strong>
	 *        <p>
	 */
	private boolean existPsn(String pkDeptdoc) throws BusinessException {

		String sql = "select count(*) from bd_psndoc where "/*psnclscope = 0 and*/
				+ " sealdate is null and pk_deptdoc = ?";
		SQLParameter para = new SQLParameter();
		para.addParam(pkDeptdoc);
		Integer count = (Integer) new BaseDAO().executeQuery(sql, para,
				new ColumnProcessor());
		return count.intValue() > 0;
	}

	/**
	 * @param pkDeptdoc
	 * @throws BusinessException  
	 * @since NC5.0	
	 * <p><strong>调用模块：通用</strong>
	 * <p><strong>最后修改人：liujian</strong>	 
	 * <p><strong>最后修改日期：2005-12-5</strong><p>	 
	 */
	private void checkBeforeDelete(String pkDeptdoc) throws BusinessException {
		//是否有子部门
		DeptdocVO[] deptdocvos = getService().query_child(pkDeptdoc);
		if (deptdocvos != null && deptdocvos.length != 0){
			throw new BDException(nc.vo.bd.MultiLangTrans.getTransStr("MO2",new String[]{nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("10080404","UC001-0000039")/*@res "删除"*/})); //已存在下级节点,不能删除
			}
		IReferenceCheck ref = (IReferenceCheck) NCLocator.getInstance().lookup(IReferenceCheck.class.getName());
        if(ref.isReferenced("bd_deptdoc", pkDeptdoc)) {
            throw new BDException(nc.vo.bd.BDMsg.MSG_REF_NOT_DELETE()/*数据已经被引用，不能删除!*/);
        }
	}

    public ArrayList getFieldBdinfoArray() {
      ArrayList fieldBdinfoList=new ArrayList();
      
      fieldBdinfoList.add(new String[] {IBdinfoContext.BDINFOCONST_NOT_A_DOC,IBdinfoConst.CORP});
      fieldBdinfoList.add(new String[] {IBdinfoContext.BDINFOCONST_NOT_A_DOC,IBdinfoConst.CORP});
        return fieldBdinfoList;
    }
}
