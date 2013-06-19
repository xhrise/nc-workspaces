/*
 * �������� 2006-1-20
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
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
 * TODO Ҫ���Ĵ����ɵ�����ע�͵�ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
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
						"UC000-0004073"), "��˾"/* @res "���ű���" */});
		list.add(new String[] {
				NCLangResOnserver.getInstance().getStrByID("10080404",
						"UC000-0004069"), "��˾"/* @res "��������" */});
		return list;
	}

	/* ���� Javadoc��
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

	/* ���� Javadoc��
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
		//Ҫɾ���Ĳ����Ƿ����
		
		delDeptVO = getService().findDeptdocVOByPK(deptdocVO.getPrimaryKey());
		if (delDeptVO == null || delDeptVO.getPrimaryKey()==null || delDeptVO.getPrimaryKey().length() <=0){
			throw new BDException(nc.vo.bd.BDMsg.MSG_REFRESH()/*�ѷ���������������ˢ������!*/);
		}

		//���ô���ǰ�ӿ�
		getBdOperate().beforeOperate("10080404",IBDOperate.BDOPERATION_DEL,deptdocVO.getPrimaryKey(),null,delDeptVO);
        
		
		// ɾ�����ŵ���ʱͬ�����м�� add by river for 2011-09-13
		
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
						System.out.println("<<<<<<  ���ŵ���ɾ���߳�ֹͣ�� >>>>>>");
						System.out.println("<<<<<<  �̺߳�: " + this.getId() + " >>>>>>");
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
    	//    	���ô����ӿ�
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
			throw new BDException(nc.vo.bd.BDMsg.MSG_REFRESH()/*�ѷ���������������ˢ������!*/);
		}
		
		   //�Ƿ���
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
																			 * "��������¼�����Ϊ�ϼ�����"
																			 */})); // ���ܽ�������¼�����Ϊ�ϼ�����
		}

		// ����ͳһ����ӿ���
		BDDocSyncInform bdDocSync = getBdDocSyncInform();
		// ���ô���ǰ�ӿ�
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
		
		if(isCancel) {  //���ִ���˷��
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
	/* ���� Javadoc��
	 * @see nc.vo.trade.comcheckunique.IUniqueFieldCheck#isDetail()
	 */
	public boolean isDetail() {

		return false;
	}

	/* ���� Javadoc��
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
		boolean isCancel = false; // �Ƿ���
		if (deptdocVO.getCanceled().booleanValue()) {
			if (!oldVO.getCanceled().booleanValue()) { // ִ�е��ǳ�������
				checkDeptCancelable(deptdocVO);
				isCancel = true;
				// deptdoc.setCancelDate(new
				// nc.vo.pub.lang.UFDate(System.currentTimeMillis())); //���÷��ʱ��
				bdOS.beforeOperate("10080404",
						nc.vo.bd.service.IBDOperate.BDOPERATION_SEAL, deptdocVO
								.getPrimaryKey(), null, null);
			}
		} else {
			// chenw 040702
			if (oldVO.getCanceled().booleanValue()) { // �޸�ǰ�Ѿ�����?
//				if (oldVO.getHrcanceled() != null
//						&& oldVO.getHrcanceled().booleanValue()) {
//					throw new BDException(
//							nc.bs.ml.NCLangResOnserver.getInstance()
//									.getStrByID("10080404",
//											"UPP10080404-000030")/*
//																	 * @res
//																	 * "�����ѱ�����"
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
//																							 * "ȡ�����"
//																							 */})); // �����ѱ�����,����ȡ�����
//				}
				if (deptdocVO.getPk_fathedept() != null) {
					DeptdocVO fVO = getService().findDeptdocVOByPK(deptdocVO
							.getPk_fathedept());
					if (fVO == null
							|| fVO.getPrimaryKey() == null
							|| (fVO != null && fVO.getCanceled() != null && !fVO
									.getCanceled().booleanValue())) { // ����ϼ�������,���ϼ�û�б�����
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
																								 * "���"
																								 */,
														nc.bs.ml.NCLangResOnserver
																.getInstance()
																.getStrByID(
																		"10080404",
																		"UPP10080404-000005") /*
																								 * @res
																								 * "ȡ�����"
																								 */})); // �ϼ��ѷ��,����ȡ������¼�
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

			// ��������VO�ӽ����ȡʱû���ڲ�������Ϣ��������лָ�
			vo.setInnercode(oldvo.getInnercode());
			vo.setMaxinnercode(oldvo.getMaxinnercode());

			// ��������δ�仯��������������
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
	 * �����������Ƿ��ܹ�������
	 * 
	 * @param deptdocVO
	 * @throws BusinessException
	 * @since NC5.0
	 *        <p>
	 *        <strong>����ģ�飺ͨ��</strong>
	 *        <p>
	 *        <strong>����޸��ˣ�liujian</strong>
	 *        <p>
	 *        <strong>����޸����ڣ�2005-12-5</strong>
	 *        <p>
	 */
	private void checkDeptCancelable(DeptdocVO deptdocVO)
			throws BusinessException {
		
		String key = deptdocVO.getPrimaryKey();

		if (key != null && key.length() != 0) {
			if( existChild(key)) {// �����¼�����
				throw new BDException(nc.vo.bd.MultiLangTrans.getTransStr("MC6",
						new String[] {
								nc.bs.ml.NCLangResOnserver.getInstance()
										.getStrByID("10080404",
												"UPP10080404-000002")/*
																		 * @res
																		 * "�¼�����"
																		 */, null })
						+ ","
						+ nc.vo.bd.MultiLangTrans.getTransStr("MP1",
								new String[] { nc.bs.ml.NCLangResOnserver
										.getInstance().getStrByID("10080404",
												"UPP10080404-000003") /*
																		 * @res "���"
																		 */})); // �¼������Ѿ�����,���ܷ��
			} 
		}

	}

	/**
	 * ���ָ�������Ƿ�����¼�
	 * 
	 * @param pkDeptdoc
	 * @return
	 * @since NC5.0
	 *        <p>
	 *        <strong>����ģ�飺ͨ��</strong>
	 *        <p>
	 *        <strong>����޸��ˣ�liujian</strong>
	 *        <p>
	 *        <strong>����޸����ڣ�2005-12-5</strong>
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
	 * ���ָ�������Ƿ������Ա
	 * 
	 * @param pkDeptdoc
	 *            ��������
	 * @return ָ�������Ƿ������Ա. ����Ϊture,��֮Ϊfalse.
	 * @since NC5.0
	 *        <p>
	 *        <strong>����ģ�飺ͨ��</strong>
	 *        <p>
	 *        <strong>����޸��ˣ�liujian</strong>
	 *        <p>
	 *        <strong>����޸����ڣ�2005-12-5</strong>
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
	 * <p><strong>����ģ�飺ͨ��</strong>
	 * <p><strong>����޸��ˣ�liujian</strong>	 
	 * <p><strong>����޸����ڣ�2005-12-5</strong><p>	 
	 */
	private void checkBeforeDelete(String pkDeptdoc) throws BusinessException {
		//�Ƿ����Ӳ���
		DeptdocVO[] deptdocvos = getService().query_child(pkDeptdoc);
		if (deptdocvos != null && deptdocvos.length != 0){
			throw new BDException(nc.vo.bd.MultiLangTrans.getTransStr("MO2",new String[]{nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("10080404","UC001-0000039")/*@res "ɾ��"*/})); //�Ѵ����¼��ڵ�,����ɾ��
			}
		IReferenceCheck ref = (IReferenceCheck) NCLocator.getInstance().lookup(IReferenceCheck.class.getName());
        if(ref.isReferenced("bd_deptdoc", pkDeptdoc)) {
            throw new BDException(nc.vo.bd.BDMsg.MSG_REF_NOT_DELETE()/*�����Ѿ������ã�����ɾ��!*/);
        }
	}

    public ArrayList getFieldBdinfoArray() {
      ArrayList fieldBdinfoList=new ArrayList();
      
      fieldBdinfoList.add(new String[] {IBdinfoContext.BDINFOCONST_NOT_A_DOC,IBdinfoConst.CORP});
      fieldBdinfoList.add(new String[] {IBdinfoContext.BDINFOCONST_NOT_A_DOC,IBdinfoConst.CORP});
        return fieldBdinfoList;
    }
}
