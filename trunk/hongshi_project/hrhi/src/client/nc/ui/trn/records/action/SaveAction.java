package nc.ui.trn.records.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JOptionPane;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.hr.utils.ResHelper;
import nc.impl.mbSyn.MbSynImpl;
import nc.impl.mbSyn.ServiceUtilImpl;
import nc.itf.hr.bd.ICorpWorkout;
import nc.itf.hr.bd.ISetdict;
import nc.itf.hr.jf.IJob;
import nc.itf.hr.trn.TRNDelegator;
import nc.itf.mbSyn.IMbSys;
import nc.itf.mbSyn.IQueryList;
import nc.itf.mbSyn.IServiceUtil;
import nc.itf.uap.bd.dept.IDeptdocQry;
import nc.itf.uap.bd.psn.IPsncl;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.hr.frame.FrameUI;
import nc.ui.hr.frame.button.AbstractBtnReg;
import nc.ui.hr.frame.util.BillPanelUtils;
import nc.ui.hr.global.Global;
import nc.ui.hr.utils.Util;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillItem;
import nc.ui.smtm.pub.CommonValue;
import nc.ui.trn.records.StafTransRdsDataModel;
import nc.ui.trn.records.StafTransRdsStateReg;
import nc.vo.bd.b04.DeptdocVO;
import nc.vo.bd.b05.PsnclVO;
import nc.vo.hi.hi_301.BDFormuleVO;
import nc.vo.hi.hi_301.GeneralVO;
import nc.vo.hi.hi_301.HRSubVO;
import nc.vo.hi.hi_401.PsnDataVO;
import nc.vo.hr.bd.setdict.FlddictVO;
import nc.vo.hr.tools.pub.ArrayUtils;
import nc.vo.hr.tools.pub.HRAggVO;
import nc.vo.om.om_005.JobVO;
import nc.vo.om.om_013.WorkoutResultVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.trn.records.PsndocVO;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import com.seeyon.client.PersonServiceStub;
import com.seeyon.client.PersonServiceStub.PersonInfoParam_All;

public class SaveAction extends RdsBaseAction {
	//������Ӽ�����
	private SuperVO saveSubData = null;
	//����λ
	private List<String> jobAbortedls = null;
	//��沿��
	private List<String> deptCanceledls = new ArrayList<String>();
	//��� ������Χ��Ӧ
	private Map<String,Integer> hashPsnclScope = new HashMap<String,Integer>();

//	 �Ƿ�ͬ����������
	private boolean isInPhase = false;

	private boolean isInster = false;

	private int saveType;

	private int currow;

	private static final String[] NEED_SYNC_WORK = new String[]{
		CommonValue.HI_PSNDOC_DEPTCHG,CommonValue.HI_PSNDOC_PART
	};

	public SaveAction(FrameUI frameUI1) {
		super(frameUI1);
	}

	@Override
	public void execute() throws Exception {
		if(saveType == 0)
			saveType = StafTransRdsDataModel.UPDATE;
		
		if (saveType == StafTransRdsDataModel.ADD) {
			// �ӱ����Ӻ󱣴�
			saveAddData();
		} else if (saveType == StafTransRdsDataModel.UPDATE) {
			// �ӱ��޸ĺ󱣴�
			saveUpdateData();

		} else if (saveType == StafTransRdsDataModel.INSERT) {
			// �ӱ����󱣴�
			saveInsertData();

		}
		afterExecute();
	}

	private void afterExecute() throws Exception {
		//�ع�����һ״̬
		getParentUI().setCurrentState(getDataMdl().getState().getLastState());
		//���治�ɱ༭
		getMainPanel().setMainPanelEnabled(false);
		
		// ��Ա���䱣��ʱͬ����A8 add by river for 2011-09-27
		boolean check = false;
		PsndocVO psndocVO =(PsndocVO) hrAggVO.getParentVO();
		PersonServiceStub.PersonInfoParam_All person = null;
		IQueryList queryList = (IQueryList) NCLocator.getInstance().lookup(
		           IQueryList.class.getName());
		
		List<PersonInfoParam_All> personList = queryList.getPerson1("nc56true",
				psndocVO.getAttributeValue("pk_psndoc").toString());
		if (personList.size() == 0) {
			System.out.println("��Ա�б�:NULL");
			//return;
		} else
			person = personList.get(0);
		
		try {
			IServiceUtil util = new ServiceUtilImpl();
			Long accid = util.getAccountId("");
			
			
		} catch (Exception e1) {
			JOptionPane
					.showMessageDialog(
							null,
							e1.getMessage() ,
							"��ʾ", JOptionPane.OK_OPTION);
			
			check = true;
		}

		if (getDataMdl().isQueryPsn()
				&& (CommonValue.HI_PSNDOC_DIMISSION.equals(strTabCode)
				      ||CommonValue.HI_PSNDOC_DEPTCHG.equals(strTabCode))) {
			
			if(CommonValue.HI_PSNDOC_DEPTCHG.equals(strTabCode)){
				if(!check) {
					try {
						Long pk_oauser = null;
						List list = queryList.getPersonbypkpsn("nc56true",
								psndocVO.getAttributeValue("pk_psndoc").toString());
						if (list.size() == 0) {
							System.out.println("��Ա��Ϣ��OA name is NULL");
							return;
						} else
							pk_oauser = (Long) list.get(0);
						IMbSys mbSyn = new MbSynImpl();
						
						try {
							String[] defs = queryList.getPersonDefVal(psndocVO.getAttributeValue("pk_psndoc").toString(), "nc56true");
							queryList.updateDelPerson2(person.getLoginName(), "nc56true");
							queryList.updateNewPerson(psndocVO.getAttributeValue("pk_psndoc").toString(), defs, "nc56true");
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						if(person.getLoginName() != null) {
							
							nc.itf.uap.IUAPQueryBS querybs = (nc.itf.uap.IUAPQueryBS)NCLocator.getInstance().lookup(nc.itf.uap.IUAPQueryBS.class.getName());
							String sql = "select pk_psndoc from bd_psndoc where pk_psnbasdoc = (select pk_psnbasdoc from bd_psndoc where pk_psndoc = '"+psndocVO.getAttributeValue("pk_psndoc").toString()+"') and psnclscope = 0";
							String pk_psndoc = querybs.executeQuery(sql, new ColumnProcessor("pk_psndoc")) == null ? "" : querybs.executeQuery(sql ,new ColumnProcessor("pk_psndoc")).toString();
							
							mbSyn.update(pk_psndoc, psndocVO.getAttributeValue("pk_corp").toString(),
										pk_oauser, "nc56true", true, false);
							
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
			
			// ������ְ��¼��Ӧˢ�½���
			getFrameUI().getBtnManager().getCmdByID(
					AbstractBtnReg.SYSBTN_REFRESH).execute();
			
			return ;
		} 
		
		
		// ������Ա������
		if (CommonValue.HI_PSNDOC_DEPTCHG.equals(strTabCode)) {
			resetDeptchgInf();
		}
		
		
		// ��Ա��ְ��Ϣͬ�� add by river for 2011-10-08
		if(CommonValue.HI_PSNDOC_PART.equals(strTabCode) || CommonValue.HI_PSNDOC_DEPTCHG.equals(strTabCode)) {
			if(!check) {
				try {
					Long pk_oauser = null;
					List list = queryList.getPersonbypkpsn("nc56true",
							psndocVO.getAttributeValue("pk_psndoc").toString());
					if (list.size() == 0) {
						System.out.println("��Ա��Ϣ��OA name is NULL");
						return;
					} else
						pk_oauser = (Long) list.get(0);
					IMbSys mbSyn = new MbSynImpl();
					
					try {
						String[] defs = queryList.getPersonDefVal(psndocVO.getAttributeValue("pk_psndoc").toString(), "nc56true");
						queryList.updateDelPerson2(person.getLoginName(), "nc56true");
						queryList.updateNewPerson(psndocVO.getAttributeValue("pk_psndoc").toString(), defs, "nc56true");
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					if(person.getLoginName() != null) {
						
						nc.itf.uap.IUAPQueryBS querybs = (nc.itf.uap.IUAPQueryBS)NCLocator.getInstance().lookup(nc.itf.uap.IUAPQueryBS.class.getName());
						String sql = "select pk_psndoc from bd_psndoc where pk_psnbasdoc = (select pk_psnbasdoc from bd_psndoc where pk_psndoc = '"+psndocVO.getAttributeValue("pk_psndoc").toString()+"') and psnclscope = 0";
						String pk_psndoc = querybs.executeQuery(sql, new ColumnProcessor("pk_psndoc")) == null ? "" : querybs.executeQuery(sql ,new ColumnProcessor("pk_psndoc")).toString();
						
						
						mbSyn.update(pk_psndoc , psndocVO.getAttributeValue("pk_corp").toString(),
								pk_oauser, "nc56true", true, false);
						
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		
		getCurBillModel().updateValue();
		getDataModel().getBillBufferUtils().clearLoadingBodyCacheVO();
		HRAggVO selectdata = getMainPnl().loadBodyData();
		getMainPnl().setSelectedData(selectdata);
		getMainPnl().refreshLastShowData();

	}

	/**
	 * ��supervoת��Ϊpsndatavo
	 * @param vo
	 * @return
	 */
	private PsnDataVO createPsnData(SuperVO vo){
		PsnDataVO psnDataVO = null;
		psnDataVO = new PsnDataVO();
		psnDataVO.setTablename(strTabCode);

//		 ��������ֶ���
		psnDataVO.setPkname(vo.getParentPKFieldName());
//		 �ӱ������ֶ���
		psnDataVO.setPksubname(vo.getPKFieldName());
//		 ����¼������
		psnDataVO.setPrimaryKey(vo.getPrimaryKey());
//		 ����¼�����
		psnDataVO.setPk_main(parentVO.getPk_psndoc());

		ISetdict setdictServer = (ISetdict) NCLocator.getInstance().lookup(
				ISetdict.class.getName());
		FlddictVO[] flddictVOs = null;
		try {
			flddictVOs = setdictServer.queryAllSonForCode(
					getDataMdl().getPk_corp(), vo.getTableName(), null, true,
					true);
		} catch (Exception e) {
			getParentUI().handleException(e);
		}
		Vector<Integer> typevec = new Vector<Integer>();
		Vector<String> namevec = new Vector<String>();
		Vector<Object> valuevec = new Vector<Object>();

		for(FlddictVO o: flddictVOs){
			if (!o.getFldcode().equals(psnDataVO.getPkname())
					&&!o.getFldcode().equals(psnDataVO.getPksubname())){
				namevec.add(o.getFldcode());
				if (getCurBillModel().getItemByKey(o.getFldcode())!=null) {
					valuevec.add(vo.getAttributeValue(o.getFldcode()));
				}else if(CommonValue.HI_PSNDOC_PART.equals(strTabCode)&&"isreturn".equals(o.getFldcode())){
					valuevec.add("N");
				}else{
					valuevec.add(null);
				}
				typevec.add((o.getDatatype()!=BillItem.UFREF?o.getDatatype():1));
			}
		}
		if (CommonValue.HI_PSNDOC_PART.equals(strTabCode)) {
			Collections.addAll(namevec, new String[]{"bendflag"});
			Collections.addAll(valuevec, new Object[]{(vo.getAttributeValue("bendflag")==null)?"N":vo.getAttributeValue("bendflag")});
			Collections.addAll(typevec, new Integer[]{5});
		}
		psnDataVO.setFieldNames(namevec.toArray(new String[0]));
		psnDataVO.setFieldValues(valuevec.toArray(new Object[0]));
		psnDataVO.setFieldTypes(typevec.toArray(new Integer[0]));
		if (strTabCode.equals(CommonValue.HI_PSNDOC_DEPTCHG)) {
			psnDataVO.setAttributeValue("jobtype", new Integer(0));
		}

		return psnDataVO;
	}
	/**
	 * �����ӱ����Ӻ������ �������ڣ�(2002-4-4 11:15:58)
	 */
	public boolean saveAddData() throws BusinessException {

		nc.vo.hi.hi_401.PsnDataVO vo = createPsnData(saveSubData);
		if (vo == null)
			return false;
		vo.setAttributeValue("recordnum", new Integer(0));
		vo.setAttributeValue("lastflag", new UFBoolean(true));
		vo.setPk_psnbasdoc(parentVO.getPk_psnbasdoc());

		// �õ���Ϣ����������Ա䶯�ȣ�
		int itabletype = (getDataMdl().getItabletypeMap().isEmpty())?0:getDataMdl().getItabletypeMap().get(strTabCode).intValue();

		// �����޹��ɱ���ÿ�ʼʱ�䡢����ʱ���У��
		checkDataForTableType(itabletype, StafTransRdsDataModel.ADD);

		if (getParentUI().getModuleCode().equalsIgnoreCase(
				CommonValue.STAFFING_RECORDS)) {
			if (ArrayUtils.isExistInArray(NEED_SYNC_WORK, strTabCode)) {
				//�Ƿ�ͬ����������
				if (!synWordCheck()) {
					return false;
				}
				// ������
				if (CommonValue.HI_PSNDOC_DEPTCHG.equals(strTabCode)
						&&isExceedWorkoutForAdd()) {
					return false;
				}
			}
		}

		//ֻ����ְ��¼����Ҫͬ������
		isInPhase = isInPhase&& (ArrayUtils.isExistInArray(NEED_SYNC_WORK, strTabCode));
		if (CommonValue.HI_PSNDOC_DIMISSION.equals(strTabCode)
				|| CommonValue.HI_PSNDOC_DEPTCHG.equals(strTabCode)) {
			PsnDataVO vo2 = (PsnDataVO) vo.clone();
//			if (CommonValue.HI_PSNDOC_DIMISSION.equals(strTabCode)) {
//				updateKeyPerson(vo.getPk_main(), new UFDate(vo
//						.getAttributeValueDes("leavedate")),
//						nc.ui.hr.global.Global.getCorpPK());
//			}
			vo = (nc.vo.hi.hi_401.PsnDataVO) saveChild(vo,itabletype, nc.ui.hr.global.Global.getCorpPK(), isInPhase);
//			processTurnover(vo2);
			// end
		} else {
			vo = (nc.vo.hi.hi_401.PsnDataVO) saveChild(vo,itabletype, nc.ui.hr.global.Global.getCorpPK(), isInPhase);
		}
		return true;
	}
	/**
	 * �����ӱ���º������ �������ڣ�(2002-4-4 11:15:58)
	 */
	public boolean saveUpdateData() throws BusinessException {
		nc.vo.hi.hi_401.PsnDataVO vo = createPsnData(saveSubData);

		// �õ���Ϣ����������Ա䶯�ȣ�
		int itabletype = (getDataMdl().getItabletypeMap().isEmpty())?0:getDataMdl().getItabletypeMap().get(strTabCode).intValue();

		// �����޹��ɱ���ÿ�ʼʱ�䡢����ʱ���У��
		checkDataForTableType(itabletype, StafTransRdsDataModel.UPDATE);
		PsnDataVO voname = (PsnDataVO) vo.clone();
		// ���ӱ��и��µ����ݴ������ݿ�
		if (vo.getAttributeValue("pk_jobserial") == null) {
			voname.setAttributeValue("pk_jobserial", null);
		}
		if (vo.getAttributeValue("pk_jobrank") == null) {
			voname.setAttributeValue("pk_jobrank", null);
		}
		if (vo.getAttributeValue("pk_detytype") == null) {
			voname.setAttributeValue("pk_detytype", null);
		}
		if (vo.getAttributeValue("pk_om_duty") == null) {
			voname.setAttributeValue("pk_om_duty", null);
		}

		if (getParentUI().getModuleCode().equalsIgnoreCase(
				CommonValue.STAFFING_RECORDS)) {
			if (ArrayUtils.isExistInArray(NEED_SYNC_WORK, strTabCode)) {
				// �Ƿ�ͬ����������
				if (!synWordCheck()) {
					return false;
				}
				// chexz 20070823�����¼�ڵ��޸���ʷ��ְ��¼ʱ��Ӧ�ó��ֳ�����ʾ
				if (Integer.valueOf(saveSubData.getAttributeValue("recordnum").toString()) == 0
						&& CommonValue.HI_PSNDOC_DEPTCHG.equals(strTabCode)) {
					// ������
					if (isExceedWorkoutForUpdate()) {
						return false;
					}
				}
			}
		}

		//���ӱ��и��µ����ݴ������ݿ�
		//��ְ��¼,��ְ��¼ ��Ҫͬ������
		isInPhase = isInPhase&& (ArrayUtils.isExistInArray(NEED_SYNC_WORK, strTabCode)||CommonValue.HI_PSNDOC_DIMISSION.equals(strTabCode));
		if (CommonValue.HI_PSNDOC_DIMISSION.equals(strTabCode)
				|| CommonValue.HI_PSNDOC_DEPTCHG.equals(strTabCode)) {
			updateChild(vo,  itabletype, voname, isInPhase);
			processTurnover(vo);
		} else {
			updateChild(vo,  itabletype, voname, isInPhase);
		}

		if (CommonValue.HI_PSNDOC_DEPTCHG.equals(strTabCode)
				|| CommonValue.HI_PSNDOC_PART.equals(strTabCode)) {
			Object corp = saveSubData.getAttributeValue("pk_corp");
			String pk_corp = (corp != null ? corp.toString() : corpPK);
			resetRefDuty(pk_corp);
		}
		return true;
	}
	/**
	 * �����ӱ����Ӻ������ �������ڣ�(2002-4-4 11:15:58)
	 */
	public boolean saveInsertData() throws BusinessException {
		nc.vo.hi.hi_401.PsnDataVO vo = createPsnData(saveSubData);

		int n = currow;//��ǰѡ����
		vo.setAttributeValue("recordnum", new Integer(getCurBillModel()
				.getRowCount()
				- n - 1));
		vo.setAttributeValue("lastflag", new UFBoolean(false));
		saveSubData.setAttributeValue("recordnum", (Integer) vo
				.getAttributeValue("recordnum"));
		saveSubData.setAttributeValue("lastflag", (UFBoolean) vo
				.getAttributeValue("lastflag"));


		// pk_psnbasdoc
		vo.setPk_psnbasdoc(parentVO.getPk_psnbasdoc());

		vo.setPk_main(parentVO.getPrimaryKey());

//		BDFormuleVO[] tongbuFields = getBDFormulas(strTabCode);
		// �õ���Ϣ����������Ա䶯�ȣ�
		int itabletype = (getDataMdl().getItabletypeMap().isEmpty())?0:getDataMdl().getItabletypeMap().get(strTabCode).intValue();
		// ������Ҳ������¼�¼�������־lastflagΪ"N"
		if (n != 0 && itabletype != nc.vo.hi.pub.CommonValue.CHG_THIRD) {
			vo.setAttributeValue("lastflag", new UFBoolean(false));
		}

		// �����޹��ɱ���ÿ�ʼʱ�䡢����ʱ���У��
		checkDataForTableType(itabletype, StafTransRdsDataModel.INSERT);

		isInster = true;
		// �������һ����ְ��¼
		if (CommonValue.HI_PSNDOC_DIMISSION.equals(strTabCode)
				|| CommonValue.HI_PSNDOC_DEPTCHG.equals(strTabCode)) {
			PsnDataVO vo2 = (PsnDataVO) vo.clone();
			vo = (nc.vo.hi.hi_401.PsnDataVO) saveChild(vo, itabletype, nc.ui.hr.global.Global.getCorpPK(), false);
			processTurnover(vo2);
		} else {
			vo = (nc.vo.hi.hi_401.PsnDataVO) saveChild(vo, itabletype, nc.ui.hr.global.Global.getCorpPK(), false);
		}

		return true;
	}
	/**
	 * ������ְ�Ӽ���Ϣ����
	 */
	private void resetDeptchgInf() {
		String refitemkey = "pk_psncl" + BillPanelUtils.REF_SHOW_NAME;
		UIRefPane refPane = (UIRefPane) getMainPnl().getBodyBillModel(
				strTabCode).getItemByKey(refitemkey).getComponent();
		if (getDataMdl().getCurrentState() != StafTransRdsStateReg.SUBLIST_EDITING) {
			refPane.setWhereString(" bd_psncl.psnclscope <> "
					+ nc.vo.hi.pub.CommonValue.PSNCLSCOPE_APPLY
					+ " and (bd_psncl.pk_corp='" + getDataMdl().getPk_corp()
					+ "' or bd_psncl.pk_corp='"
					+ nc.vo.hi.pub.CommonValue.GROUPCODE + "')");
		} else {
			refPane.setWhereString("( bd_psncl.psnclscope = "
					+ nc.vo.hi.pub.CommonValue.PSNCLSCOPE_WORK
					+ " or  bd_psncl.psnclscope = "
					+ nc.vo.hi.pub.CommonValue.PSNCLSCOPE_OTHER
					+ ") and (bd_psncl.pk_corp='" + getDataMdl().getPk_corp()
					+ "' or bd_psncl.pk_corp='"
					+ nc.vo.hi.pub.CommonValue.GROUPCODE + "')");
		}
		refPane.getRefModel().reloadData();

	}
	/**
	 * ����ְ�����
	 *
	 * @param pk_corp
	 */
	private void resetRefDuty(String pk_corp) {
		BillItem duty = getCurBillModel().getItemByKey("pk_om_duty"+BillPanelUtils.REF_SHOW_NAME);
		if (duty != null) {
			String wherePart = null;
			if (Util.isDutyDependJobSeries) {
				wherePart = " (duty.pk_corp='" + pk_corp
						+ "' or duty.pk_corp = '"
						+ nc.vo.hi.pub.CommonValue.GROUPCODE
						+ "') and sery.pk_defdoc=duty.series ";
			} else {
				wherePart = " (duty.pk_corp='"
						+ pk_corp
						+ "' or duty.pk_corp = '"
						+ nc.vo.hi.pub.CommonValue.GROUPCODE
						+ "') and sery.pk_defdoc=duty.series and duty.series in(select pk_defdoc from bd_defdoc where pk_defdoclist='HI000000000000000020') ";
			}
			((UIRefPane) duty.getComponent()).getRefModel().setWherePart(
					wherePart);
			((UIRefPane) duty.getComponent()).getRefModel().reloadData();
		}
	}

	@Override
	public boolean validate() throws ValidationException {


		boolean result = super.validate();
		if(!result){
			return result;
		}
		//�༭ҳǩ������У��
		getMainPnl().dataNotNullValidate(strTabCode);

		int index = getMainPnl().getCurrentBodyPageIndex();

		//��ȡ��Ҫ������Ӽ�
		SuperVO[] vos = ((SuperVO[])getCurBillModel().getBodyValueChangeVOs(getDataMdl().getBodyVOClassArray()[index].getName()));

		if(vos==null||vos.length==0){
			throw new ValidationException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("60090713","UPP60090713-000217")/*@res "�����ޱ仯!"*/);
		}
		saveSubData = vos[0];

		if(CommonValue.HI_PSNDOC_PART.equals(strTabCode)){
			validatePart();
		}else if (CommonValue.HI_PSNDOC_PSNCHG.equals(strTabCode)) {
			validatePsnchg();
		}else if(CommonValue.HI_PSNDOC_DEPTCHG.equals(strTabCode)){
			validateDeptchg();
		}else if(CommonValue.HI_PSNDOC_DIMISSION.equals(strTabCode)){
			validateDimission();
		}

		saveType = getDataMdl().getEditType();

		currow = getCurBillModel().getEditRow();





		return super.validate();
	}

	public boolean validateFlowSet(SuperVO vo) throws ValidationException {
		if (vo.getAttributeValue("chgflag") == null
				|| vo.getAttributeValue("chgflag").toString().length() <= 0) {
			throw new ValidationException(ResHelper.getString("60090713",
							"UPP60090713-000093")/* @res "����״̬����Ϊ�գ�" */);
		}
		if (vo.getAttributeValue("chgdate") == null
				|| vo.getAttributeValue("chgdate").toString().length() <= 0) {
			throw new ValidationException(ResHelper.getString("60090713",
							"UPP60090713-000094")/* @res "�������ڲ���Ϊ�գ�" */);
		}
		if (vo.getAttributeValue("chgtype") == null
				|| vo.getAttributeValue("chgtype").toString().length() <= 0) {
			throw new ValidationException(ResHelper.getString("60090713",
							"UPP60090713-000095")/* @res "���������Ϊ�գ�" */);
		}
		if (!checkFlowdate(getMainPnl().getBodySelectedRow(strTabCode))) {
			return false;
		}
		return true;
	}
	/**
	 * У�鿪ʼ���ںϷ���
	 *
	 * �������ڣ�(2002-6-7 12:23:10)
	 *
	 * @return boolean
	 * @param curRow
	 *            int
	 * @param flowDate
	 *            nc.vo.pub.lang.UFDate
	 */
	public boolean checkFlowdate(int curRow) throws ValidationException {
		//��ǰ�е���������
		UFDate flowDate = null;
		if (getCurBillModel().getValueAt(curRow, "chgdate") != null) {
			flowDate = new UFDate(getCurBillModel().getValueAt(curRow,
					"chgdate").toString());
		}
		//��һ����¼����������
		UFDate nextRowFlowdate = null;
		//������
		int iRowCount = getCurBillModel().getRowCount() - 1;
		if (curRow != iRowCount) {
			if (getCurBillModel().getValueAt(curRow + 1, "chgdate") != null) {
				nextRowFlowdate = new UFDate(getCurBillModel().getValueAt(
						curRow + 1, "chgdate").toString());
			}
			if (nextRowFlowdate != null && flowDate != null) {
				if (nextRowFlowdate.before(flowDate)) {
					throw new ValidationException(ResHelper.getString(
									"60090713", "UPP60090713-000027")/*
																	  * @res
																	  * "��ǰ����������Ӧ������һ�е��������ڣ�"
																	  */);
				}
			}
		}
		if (curRow != 0) {
			UFDate preRowFlowdate = null;
			if (getCurBillModel().getValueAt(curRow - 1, "chgdate") != null) {
				preRowFlowdate = new UFDate(getCurBillModel().getValueAt(
						curRow - 1, "chgdate").toString());
			}

			if (preRowFlowdate != null) {
				if (preRowFlowdate.after(flowDate)) {
					throw new ValidationException(ResHelper.getString(
									"60090713", "UPP60090713-000028")/*
																	  * @res
																	  * "��ǰ����������Ӧ������һ���������ڣ�"
																	  */);
				}
			}
		}
		return true;
	}
	/**
	 * �����ӱ����Ӻ������ �������ڣ�(2002-4-4 11:15:58)
	 */

	public nc.vo.hi.hi_401.PsnDataVO saveChild(PsnDataVO vo, int i, String pk_corp, boolean isInPhase)
			throws BusinessException {
		nc.vo.hi.hi_401.PsnDataVO rvo = TRNDelegator.getStapplybH().insertPsnData(vo,i, pk_corp,
				isInPhase);
//		if(!isInster)
//		//������ְ����ְ��¼ʱ�޸�н����Ϣ
//		if (vo.getTablename() != null) {
//			if (vo.getTablename().equalsIgnoreCase("hi_psndoc_deptchg")) {
//				UFDate ufDate =new UFDate((rvo.getAttributeValue("begindate")).toString());
//				updateWa(rvo.getPk_main(), rvo.getPrimaryKey(), ufDate);//null chexz 20070807
//			} else if (vo.getTablename()
//					.equalsIgnoreCase("hi_psndoc_dimission")) {
//				UFDate ufDate =new UFDate((rvo.getAttributeValue("leavedate")).toString());
//				updateWa(rvo.getPk_main(), rvo.getPrimaryKey(),
//						nc.itf.hr.wa.IPsnChanged.DIMISSION, corpPK, ufDate);//null chexz 20070807
//			}
//		}
//		isInster = false;
		return rvo;
	}
	/**
	 * �����ӱ����Ӻ������ �������ڣ�(2002-4-4 11:15:58)
	 */
	private void updateChild(HRSubVO vo, int i,HRSubVO voname, boolean isInPhase) throws BusinessException {

		TRNDelegator.getStapplybH().updatePsnData((nc.vo.hi.hi_401.PsnDataVO) vo,i, (nc.vo.hi.hi_401.PsnDataVO) voname, isInPhase);

		/*boolean isFinisRow = false;
		if(getMainPnl().getBodySelectedRow(strTabCode) == getMainPnl().getBodySelectedPaneRowCount(strTabCode)-1)
			isFinisRow = true;//�ж��Ƿ����һ��

		// �޸���ְ����ְ����ְ��¼ʱ�޸�н����Ϣ
		if (vo.getTablename() != null) {
			if (isFinisRow && vo.getTablename().equalsIgnoreCase("hi_psndoc_deptchg")) {
				updateWa(vo.getPk_main(), vo.getPrimaryKey(), new UFDate(vo.getAttributeValue("begindate").toString()));//null
			} else if (isFinisRow && vo.getTablename()
					.equalsIgnoreCase("hi_psndoc_dimission")) {
				updateWa(vo.getPk_main(), vo.getPrimaryKey(),
						nc.itf.hr.wa.IPsnChanged.DIMISSION, corpPK, new UFDate(vo.getAttributeValue("leavedate").toString()));
			} else if (vo.getTablename().equalsIgnoreCase("hi_psndoc_part")
					&& vo.getAttributeValue("pk_corp") != null) {
				//�п�ʼ��û�н����ļ�ְ��¼
				if(vo.getAttributeValue("enddate")==null || vo.getAttributeValue("enddate").equals("")){
					updateWa(vo.getPk_main(), vo.getPrimaryKey(),
							nc.itf.hr.wa.IPsnChanged.PARTTIME, (String) vo
									.getAttributeValue("pk_corp"), new UFDate(vo.getAttributeValue("begindate").toString()));
				}
				//lyq 20080710  �Ѿ������˼�ְʱ��У��  ����Ѿ���������Ҫ�н�������
				if(vo.getAttributeValue("bendflag")!=null&&"Y".equals(vo.getAttributeValue("bendflag").toString())){
					try {
						if (Util.isModuleInstalled(vo.getAttributeValue("pk_corp").toString(), "TA")) {
							ITBMPsndocForTRN ita = (ITBMPsndocForTRN)NCLocator.getInstance().lookup(ITBMPsndocForTRN.class.getName());
							ita.updateTBMPsndocForEndPart(vo.getAttributeValue("pk_psndoc_sub").toString(), (UFDate)(vo.getAttributeValue("enddate")));
						}

					} catch (Exception e) {
						getParentUI().handleException(e);
					}
				}
			}
		}*/
	}


	private void validateDeptchg() throws ValidationException {
		PsndocVO psndocVO =(PsndocVO) hrAggVO.getParentVO();

		String pk_psncl =(String) saveSubData.getAttributeValue("pk_psncl");
		String pk_deptdoc =(String) saveSubData.getAttributeValue("pk_deptdoc");
		String pk_postdoc = (String) saveSubData.getAttributeValue("pk_postdoc");



		if (StringUtils.isBlank(pk_psncl)) {
			throw new ValidationException(ResHelper.getString("60090713",
							"UPP60090713-000022")/* @res "��Ա�����Ϊ��!" */);
		}
		if (StringUtils.isBlank(pk_deptdoc)) {
			throw new ValidationException(ResHelper.getString("60090713",
							"UPP60090713-000023")/* @res "���Ų���Ϊ��!" */);
		}

		//���һ��ʱУ��
		if (getMainPnl().getBodySelectedRow(strTabCode) == getCurBillModel().getRowCount() - 1) {
			//����ְ��Ա�޸����һ����¼ʱ����У��
			if (saveSubData.getStatus() == VOStatus.UPDATED
					&& (psndocVO.getPsnclscope() == nc.vo.hi.pub.CommonValue.PSNCLSCOPE_DISMISS
							|| psndocVO.getPsnclscope() == nc.vo.hi.pub.CommonValue.PSNCLSCOPE_LEAVE
							|| psndocVO.getPsnclscope() == nc.vo.hi.pub.CommonValue.PSNCLSCOPE_RETIRE)) {
			} else {
				//��Ա���Ϊ��ְʱ���
				Integer psnclscope = (Integer) getHashPsnclScope().get(pk_psncl);
				if ((psnclscope == nc.vo.hi.pub.CommonValue.PSNCLSCOPE_WORK
						|| psnclscope == nc.vo.hi.pub.CommonValue.PSNCLSCOPE_OTHER)) {
					//��鵱ǰ��Ա�Ƿ��Ѿ����������
					String strReturn = null;
					try {
						strReturn = TRNDelegator.getStapplybH()
								.isExistPsndocBad(psndocVO.getPk_psndoc());
					} catch (Exception e) {
						Logger.error(e.getMessage(),e);
					}
					if (strReturn != null) {
						throw new ValidationException(ResHelper.getString("60090713","UPP60090713-000160")/*
																	  * @res
																	  * "����Ա�����ں������У������޸�Ϊ��ְ��������Ա!"
																	  */);
					}

					if (pk_deptdoc != null
							&& getDeptCanceledls().contains(pk_deptdoc)) {
						throw new ValidationException(
								ResHelper.getString("60090713","UPP60090713-000142")/*
																	  * @res
																	  * "�����ѳ�������ѡ����������!"
																	  */);
					}

					if (pk_postdoc != null
							&& getJobAbortedls().contains(pk_postdoc)) {
						throw new ValidationException(
								ResHelper.getString("60090713",
												"UPP60090713-000155")/*
																	  * @res
																	  * "��λ�ѳ�������ѡ��������λ!"
																	  */);
					}
				}
			}
		}



	}

	private void validateDimission() throws ValidationException {
		PsndocVO psndocvo =(PsndocVO) hrAggVO.getParentVO();
		// ��ְ���
		String psnclafter = (String) saveSubData.getAttributeValue("psnclafter");

		if (StringUtils.isBlank(psnclafter)) {
			throw new ValidationException(ResHelper.getString("60090713",
					"UPP60090713-000165")/* @res "��ְ����Ա�����Ϊ��!" */);
		}
		String pk_deptdoc = (String) saveSubData
				.getAttributeValue("pkdeptafter");

		if (StringUtils.isBlank(pk_deptdoc)) {
			throw new ValidationException(ResHelper.getString("60090713",
					"UPP60090713-000164")/* @res "��ְ���Ų���Ϊ��!" */);
		}

		String type = (String) saveSubData.getAttributeValue("type");

		if (StringUtils.isBlank(type)) {
			throw new ValidationException(ResHelper.getString("60090713",
					"UPP60090713-000166")/* @res "��ְ���Ͳ���Ϊ��!" */);
		}


		UFDate begindate = null;

		UFDate leavedate = (UFDate) saveSubData.getAttributeValue("leavedate");
		if (leavedate == null) {
			throw new ValidationException(
					ResHelper.getString("60090713", "UPP60090713-000156")/*
															  * @res
															  * "��ְ���ڲ���Ϊ�գ�"
															  */);
		}

	}

	private void validatePart() throws ValidationException {
		String pk_corp =(String) saveSubData.getAttributeValue("pk_corp");
		String pk_psncl =(String) saveSubData.getAttributeValue("pk_psncl");
		String pk_deptdoc =(String) saveSubData.getAttributeValue("pk_deptdoc");
		String pk_postdoc = (String) saveSubData.getAttributeValue("pk_postdoc");
		if (StringUtils.isBlank(pk_corp)) {
			throw new ValidationException(ResHelper.getString("60090713",
							"UPP60090713-000015")/*
												  * @res "��ְ"
												  */
							+ nc.vo.hr.global.CommonValue.TRADETYPE_CORP
							+ ResHelper.getString("60090713", "UPP60090713-000024")/*
																	  * @res
																	  * "����Ϊ�գ�"
																	  */);
		}

		if (StringUtils.isBlank(pk_psncl)) {
			throw new ValidationException(
					ResHelper.getString("60090713",
							"UPP60090713-000026")/* @res "��Ա�����Ϊ�գ�" */);
		}
		if (saveSubData.getAttributeValue("jobtype") == null) {
			throw new ValidationException(ResHelper.getString("60090713",
							"UPP60090713-000096")/* @res "��ְ���Ͳ���Ϊ�գ�" */);
		}
		//chexz 20070928
		if (StringUtils.isBlank(pk_deptdoc)) {
			throw new ValidationException(ResHelper.getString("60090713",
					"UPP60090713-000203")/* "��ְ���Ų���Ϊ��!" */);
		}
		//lyq 20080710  ��ְ��¼���������������ڲ���Ϊ�ա�
		if(saveSubData.getAttributeValue("bendflag")!=null&&"Y".equals(saveSubData.getAttributeValue("bendflag").toString())){//��ְ������¼
			if (saveSubData.getAttributeValue("enddate") == null) {
				throw new ValidationException(ResHelper.getString("60090713",
						"UPP60090713-000204")/* "�����ļ�ְ��¼�������ڲ���Ϊ�գ�" */);
			}
		}
		//chexz 20071207
		if (saveSubData.getAttributeValue("enddate") != null && saveSubData.getAttributeValue("begindate")!=null
				&& new UFDate(saveSubData.getAttributeValue("enddate").toString())
						.before(new UFDate(saveSubData.getAttributeValue("begindate").toString()))) {
			throw new ValidationException(ResHelper.getString("60090713",
					"UPP60090713-000205")/* "�������ڲ���С�ڿ�ʼ����!" */);
		}
		if (saveSubData.getAttributeValue("begindate") == null
				|| saveSubData.getAttributeValue("begindate").toString()
						.trim().length() == 0) {
			throw new ValidationException(
					ResHelper.getString("60090713","UPP60090713-000071")/*
												  * @res "��ʼ���ڲ���Ϊ�գ�"
												  */);
		}
		if (pk_postdoc != null && getJobAbortedls().contains(pk_postdoc)) {
			throw new ValidationException(
					ResHelper.getString("60090713","UPP60090713-000155")/*
												  * @res "��λ�ѷ�棬��ѡ��������λ!"
												  */);
		}

		//�����Ա���͹�˾�Ƿ�ƥ�䣬�����ƥ����ʾ��Ϣ
		Object corp = null;

		Hashtable<String, String> hashPsncl = new Hashtable<String, String>();

		IPsncl publicPsncl = (IPsncl) NCLocator.getInstance().lookup(IPsncl.class.getName());
		PsnclVO[] psnclVOs;
		try {
			psnclVOs = publicPsncl.queryAllPsnclVOs(pk_corp);
		} catch (BusinessException e) {
			Logger.error(e.getMessage());
			throw new ValidationException("");
		}

		if (psnclVOs != null && psnclVOs.length > 0) {
			for (int i = 0; i < psnclVOs.length; i++) {
				hashPsncl.put(psnclVOs[i].getPk_psncl(), psnclVOs[i]
						.getPk_corp());
			}
		}
		corp = hashPsncl.get(pk_psncl);

		if (corp == null) {
			throw new ValidationException(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("60090713",
							"UPP60090713-000141")/* @res "��Ա���͹�˾��ƥ�䣡" */);
		}
		// chexz 20071224���Ѿ����Ĳ��Ÿ�λ���ݲ��ܰ��Ѿ������ȥ��
		UFBoolean dept = (saveSubData.getAttributeValue("bendflag")!=null)?(UFBoolean)saveSubData.getAttributeValue("bendflag"):new UFBoolean(false);
		if (!dept.booleanValue()
				&& ((getDeptCanceledls().contains(pk_deptdoc))
						||( pk_postdoc != null && getJobAbortedls().contains(pk_postdoc)))) {
			throw new ValidationException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("hr_trn_pub","UPPhr_trn_pub-000057")/*@res "���Ż��λ�Ѿ���治�ܰѼ�ְ��¼�����ȥ��"*/);

		}
	}
	private void validatePsnchg()  throws ValidationException {
		saveSubData.setAttributeValue("pk_corp", getDataMdl().getPk_corp());
		validateFlowSet(saveSubData);
	}
	/**
	 * �õ�Ҫͬ�����������Ϣ�� �������ڣ�(2002-4-8 13:16:17)
	 */
	private BDFormuleVO[] getBDFormulas(String tablename) throws BusinessException {
		ISetdict setdictServer = (ISetdict) NCLocator.getInstance().lookup(
				ISetdict.class.getName());
		FlddictVO[] flddictVOs = setdictServer.queryAllSonForCode(getDataMdl()
				.getPk_corp(), tablename, null, true, true);
		//����ӱ��к�����ͬ�����ֶε�����
		Vector<BDFormuleVO> vec = new Vector<BDFormuleVO>();
		BDFormuleVO vo = new BDFormuleVO();
		for (int i = 0; i < flddictVOs.length; i++) {
			if (flddictVOs[i].getBdfldpk() != null
					&& flddictVOs[i].getBdfldpk().trim().length() > 0) {
				vo = new BDFormuleVO();
				vo.setFieldKey(flddictVOs[i].getFldcode());
				vo.setBdfldpk(flddictVOs[i].getBdfldpk());
				vec.addElement(vo);
			}
		}
		BDFormuleVO[] tongbuFields = new BDFormuleVO[vec.size()];
		if (vec.size() > 0) {
			vec.copyInto(tongbuFields);
		}
		return tongbuFields;
	}

	public List<String> getDeptCanceledls() {
		if(deptCanceledls.size()==0){
			try {
				IDeptdocQry deptdocQryService = (IDeptdocQry) NCLocator
						.getInstance().lookup(IDeptdocQry.class.getName());
				DeptdocVO[] vos = deptdocQryService.queryAllDeptdocVO(getDataMdl().getPk_corp());
				if (vos != null && vos.length > 0) {
					for (int i = 0; i < vos.length; i++) {
						if (vos[i].hrcanceled.booleanValue())
							deptCanceledls.add(vos[i].getPrimaryKey());
					}
				}
			} catch (Exception e) {
				Logger.error(e.getMessage());
			}

		}
		return deptCanceledls;
	}

	public List<String> getJobAbortedls() {
		if(jobAbortedls == null){
			jobAbortedls = new ArrayList<String>();
			try {
				JobVO vo = new JobVO();
				vo.setIsAbort(new UFBoolean(true));
				IJob jobServer = (IJob) NCLocator.getInstance().lookup(
						IJob.class.getName());
				JobVO[] vos = jobServer.queryByVO(vo, Boolean.TRUE);
				if (vos != null && vos.length > 0) {
					for (int i = 0; i < vos.length; i++) {
						if (vos[i].getIsAbort().booleanValue())
							jobAbortedls.add(vos[i].getPrimaryKey());
					}
				}
			} catch (Exception e) {
				Logger.error(e.getMessage());
			}
		}
		return jobAbortedls;
	}


	public Map<String, Integer> getHashPsnclScope() {
		if (hashPsnclScope.size() == 0) {
			try {
				IPsncl publicPsncl = (IPsncl) NCLocator.getInstance().lookup(
						IPsncl.class.getName());
				PsnclVO[] psnclVOs = publicPsncl.queryAllPsnclVOs(corpPK);
				if (psnclVOs != null && psnclVOs.length > 0) {
					for (int i = 0; i < psnclVOs.length; i++) {
						hashPsnclScope.put(psnclVOs[i].getPk_psncl(),
								psnclVOs[i].getPsnclscope());
					}
				}
			} catch (Exception e) {
				getParentUI().handleException(e);
			}
		}

		return hashPsnclScope;
	}

	private void checkDataForTableType(int itabletype,int saveType) throws BusinessException {
		UFDate begindate = null;
		UFDate enddate = null;
		if (itabletype == nc.vo.hi.pub.CommonValue.CHG_FIRST) {
			if (CommonValue.HI_PSNDOC_DIMISSION.equals(strTabCode)) {
				nc.vo.hi.hi_401.PsnDataVO[] psnChildDataVOs= TRNDelegator.getTrnPub().queryCorpPsndata(parentVO.getPk_psndoc(), "hi_psndoc_deptchg",
						getDataMdl().getPk_corp(), 0);
				String psnname = (String) parentVO.getPsnname();
				UFDate leavedate = (UFDate) saveSubData.getAttributeValue("leavedate");
				if (psnChildDataVOs != null) {
					for (int i = 0; i < psnChildDataVOs.length; i++) {
						// ������һ����ְ��¼��Ϣ�Ƿ���Ч
						if (((Integer) psnChildDataVOs[i].getAttributeValue("recordnum")).intValue() == 0) {
							begindate = UFDate.getDate(ObjectUtils.toString(psnChildDataVOs[i].getAttributeValue("begindate"), null));
							enddate = UFDate.getDate(ObjectUtils.toString(psnChildDataVOs[i].getAttributeValue("enddate"), null));
							if ((begindate == null || (begindate!=null&&enddate!=null))
									&& saveType == StafTransRdsDataModel.ADD) {
								throw new BusinessException(ResHelper.getString(
																"60090713",
																"UPP60090713-000062")/*
																						 * @res
																						 * "��Ա '"
																						 */
														+ psnname
														+ ResHelper.getString(
																		"60090713",
																		"UPP60090713-000157")/*
																								 * @res "'
																								 * ��������Ч����ְ��¼��û����Ч��ְ��¼��������ְ��ʼ����Ϊ�գ�����ִ��!"
																								 */);
							}
							break;
						}
					}
					if(saveType == StafTransRdsDataModel.ADD
						&& begindate.after(leavedate)){
						throw new BusinessException(ResHelper.getString(
								"60090713", "UPP60090713-000158")/*
								  * @res
								  * "��ְ���ڲ���С����Ч��ְ��¼�Ŀ�ʼ����!"
								  */);
					}
					if (saveType == StafTransRdsDataModel.UPDATE
						&& leavedate.before(enddate)) {
						throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("60090713","UPP60090713-000218")/*@res "��ְ���ڲ�������������ְ��¼�Ľ�������!"*/);
					}
				}
			} else {
				begindate = (UFDate) saveSubData.getAttributeValue("begindate");
				enddate = (UFDate) saveSubData.getAttributeValue("enddate");

				if (begindate==null) {
					throw new BusinessException(ResHelper.getString(
									"60090713", "UPP60090713-000071")/*
																	  * @res
																	  * "��ʼ���ڲ���Ϊ�գ�"
																	  */);
				}
				if (saveSubData.getAttributeValue("recordnum") != null
						&& ((Integer) saveSubData.getAttributeValue("recordnum")).intValue() != 0
						&& enddate==null) {
					throw new BusinessException(ResHelper.getString(
									"60090713", "UPP60090713-000074")/*
																	  * @res
																	  * "��ֹ���ڲ���Ϊ�գ�"
																	  */);
				}
				if (enddate!=null && begindate.after(enddate)) {
					throw new ValidationException(ResHelper.getString(
							"60090713", "UPP60090713-000066")/*
																 * @res
																 * "��ʼ���ڲ��ܴ�����ֹ���ڣ�"
																 */);
				}
				checkBegindate();
			}
		}
		//�������ڱ���ڼ��У��
		else if (itabletype == nc.vo.hi.pub.CommonValue.CHG_SECOND) {
			if (saveSubData.getAttributeValue("period") == null
					|| saveSubData.getAttributeValue("period").toString().length() <= 0) {
				throw new BusinessException(ResHelper.getString(
								"60090713", "UPP60090713-000072")/*
																  * @res
																  * "�ڼ䲻��Ϊ�գ�"
																  */);
			}
		}

	}
	/**
	 * У�鿪ʼ���ںϷ���
	 *
	 * �������ڣ�(2002-6-7 12:23:10)
	 *
	 * @return boolean
	 * @param curRow
	 *            int
	 * @param beginDate
	 *            nc.vo.pub.lang.UFDate
	 */
	public boolean checkBegindate() throws BusinessException {
		/***********************************************************************
		 * rowindex describe 0 : next 1 : curRow 2 : per . . .
		 **********************************************************************/
		//��ǰ�еĿ�ʼ���ں���ֹ����
		UFDate beginDate =(UFDate) saveSubData.getAttributeValue("begindate");

		UFDate endDate = (UFDate) saveSubData.getAttributeValue("enddate");;
		if (currow != 0) {
			if (getCurBillModel().getValueAt(currow, "enddate") != null) {
				endDate = new UFDate(getCurBillModel().getValueAt(currow,
						"enddate").toString());
			}
		} else {
			if (getCurBillModel().getValueAt(currow, "enddate") != null) {
				if (getCurBillModel().getValueAt(currow, "enddate") != null) {
					endDate = new UFDate(getCurBillModel().getValueAt(currow,
							"enddate").toString());
				}
			}
		}
//		getCurBillModel()
		//ǰһ����¼�Ŀ�ʼ����
		UFDate preRowEnddate = null;
		//��һ����¼�Ŀ�ʼ����
		UFDate nextRowBegindate = null;
		//������
		int iRowCount = getCurBillModel().getRowCount() - 1;
		if (currow != iRowCount) {
			if (getCurBillModel().getValueAt(currow + 1, "begindate") != null) {
				nextRowBegindate = new UFDate(getCurBillModel().getValueAt(
						currow + 1, "begindate").toString());
			}
		}
		if (currow != 0) {
			if (getCurBillModel().getValueAt(currow - 1, "enddate") != null) {

				if (getCurBillModel().getValueAt(currow - 1, "enddate") != null) {
					preRowEnddate = new UFDate(getCurBillModel().getValueAt(
							currow - 1, "enddate").toString());
				}
			} else {

				UFDate preRowBegindate = null;
				if (getCurBillModel().getValueAt(currow - 1, "begindate") != null) {
					preRowBegindate = new UFDate(getCurBillModel().getValueAt(
							currow - 1, "begindate").toString());
				}
				preRowEnddate = beginDate.getDateBefore(1);
				if (preRowBegindate != null) {
					if (preRowBegindate.after(preRowEnddate)) {
						throw new BusinessException(ResHelper.getString(
										"60090713", "UPP60090713-000018")/*
																		  * @res
																		  * "��ʼ���ڲ���С�ڵ�����һ��¼�Ŀ�ʼ���ڣ�"
																		  */);
					}
				} else {
					throw new BusinessException(ResHelper.getString(
									"60090713", "UPP60090713-000019")/*
																	  * @res
																	  * "ǰһ����¼�Ŀ�ʼ���ڲ���Ϊ�գ�"
																	  */);
				}
			}
		}
		if (preRowEnddate != null
				&& (preRowEnddate.compareTo(beginDate) == 0 || preRowEnddate
						.after(beginDate))) {
			throw new BusinessException(ResHelper.getString("60090713",
							"UPP60090713-000020")/*
												  * @res "��ʼ���ڲ���С�ڵ�����һ��¼�Ľ������ڣ�"
												  */);
		}
		if (endDate != null
				&& nextRowBegindate != null
				&& (nextRowBegindate.compareTo(endDate) == 0 || nextRowBegindate
						.before(endDate))) {
			throw new BusinessException(ResHelper.getString("60090713",
							"UPP60090713-000021")/*
												  * @res "�������ڲ��ܴ��ڵ�����һ��¼����ʼ���ڣ�"
												  */);
		}
		//�ù�˾��һ����ְ��¼ ��ְ���ںʹ�Ա����������˾����ְ�������Ƚϣ�У���Ƿ��н���
		if (currow == 0 && CommonValue.HI_PSNDOC_DEPTCHG.equals(strTabCode)) {
			boolean flag = TRNDelegator.getTrnQBS().checkPsnWorkDate(
					Global.getCorpPK(), parentVO.getPk_psndoc(),beginDate, endDate);

			if (flag) {
				throw new BusinessException(ResHelper.getString("60090713",
						"UPP60090713-000209"));// ��ְ���ں���Ա��������˾����ְ�����г�ͻ!
			}
		}
		return true;
	}
	/**
	 * �Ƿ񳬱�_�޸���ְ��¼ʱ���
	 *
	 * @return
	 */
	private boolean isExceedWorkoutForUpdate() throws BusinessException {
		boolean isExceedWorkout = false;

		String msg = "";
		// ��ǰУ����
		GeneralVO[] oldPsnVOs = new GeneralVO[1];
		//
		oldPsnVOs[0] = new GeneralVO();
		oldPsnVOs[0].setAttributeValue("pk_psndoc", parentVO.getPrimaryKey());
		oldPsnVOs[0].setAttributeValue("pk_psncl", parentVO.getAttributeValue("pk_psncl"));
		// ��Ա����
		oldPsnVOs[0].setAttributeValue("pk_deptdoc", parentVO.getAttributeValue("pk_deptdoc"));
		// ��Ա��λ
		Object pk_om_job = parentVO.getAttributeValue("pk_om_job");
		if (pk_om_job != null)
			oldPsnVOs[0].setAttributeValue("pk_om_job", pk_om_job);
		else
			oldPsnVOs[0].setAttributeValue("pk_om_job",ICorpWorkout.GENERAL_NULLVALUE);

		//
		GeneralVO[] newPsnVOs = new GeneralVO[1];
		// ���������
		// ��ǰУ
		newPsnVOs[0] = getWorkoutDataForAdd();
		//����Ƿ񳬱�
		WorkoutResultVO resultVO = ((ICorpWorkout) NCLocator
				.getInstance().lookup(ICorpWorkout.class.getName())).checkWorkout(
				corpPK, Global.getUserID(), false, Global.getLogYear(),oldPsnVOs, newPsnVOs);
		//
		if (!resultVO.isSyncLock()) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("hr_trn_pub","UPPhr_trn_pub-000021")/*@res "���ƹ������������û����������Ժ�����"*/);
		} else {
			if (resultVO.isPassTest()) {
				if (StringUtils.isNotBlank(resultVO.getMessage())) {
					getParentUI().showWarningMessage(resultVO.getMessage());
				}
				// ������ǿ�Ʋ�ͨ��
			} else {
				throw new BusinessException(resultVO.getMessage());
			}
		}		
		return isExceedWorkout;
	}
	/**
	 * �Ƿ񳬱�_������ְ��¼ʱ���
	 *
	 * @return
	 */
	private boolean isExceedWorkoutForAdd() throws BusinessException {
		boolean isExceedWorkout = false;

		String msg = "";

		GeneralVO[] oldPsnVOs = new GeneralVO[1];
		//
		oldPsnVOs[0] = new GeneralVO();
		oldPsnVOs[0].setAttributeValue("pk_psndoc", parentVO.getPrimaryKey());
		oldPsnVOs[0].setAttributeValue("pk_psncl", parentVO.getAttributeValue("pk_psncl"));
		// ��Ա����
		oldPsnVOs[0].setAttributeValue("pk_deptdoc", parentVO.getAttributeValue("pk_deptdoc"));
		// ��Ա��λ
		Object pk_om_job = parentVO.getAttributeValue("pk_om_job");
		if (pk_om_job != null)
			oldPsnVOs[0].setAttributeValue("pk_om_job", pk_om_job);
		else
			oldPsnVOs[0].setAttributeValue("pk_om_job",ICorpWorkout.GENERAL_NULLVALUE);
		//
		GeneralVO[] newPsnVOs = new GeneralVO[1];
		// ���������
		newPsnVOs[0] = getWorkoutDataForAdd();
		//����Ƿ񳬱�
		WorkoutResultVO resultVO = ((ICorpWorkout) NCLocator
				.getInstance().lookup(ICorpWorkout.class.getName())).checkWorkout(
				corpPK, Global.getUserID(), false, Global.getLogYear(),oldPsnVOs, newPsnVOs);
		if (!resultVO.isSyncLock()) {			
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("hr_trn_pub","UPPhr_trn_pub-000021")/*@res "���ƹ������������û����������Ժ�����"*/);
		} else {
			if (resultVO.isPassTest()) {
				if (StringUtils.isNotBlank(resultVO.getMessage())) {
					getParentUI().showWarningMessage(resultVO.getMessage());
				}
				// ������ǿ�Ʋ�ͨ��
			} else {				
				throw new BusinessException(resultVO.getMessage());
			}
		}		
		return isExceedWorkout;
	}
	/**
	 * �õ���Ҫ�����жϵ����� V53 add
	 *
	 * @param row
	 * @return
	 */
	private GeneralVO getWorkoutDataForAdd() {
		GeneralVO newPsnVO = new GeneralVO();
		//��Ա���
		newPsnVO.setAttributeValue("pk_psncl", saveSubData.getAttributeValue("pk_psncl"));
		// ��Ա����
		newPsnVO.setAttributeValue("pk_deptdoc", saveSubData.getAttributeValue("pk_deptdoc"));
		// ��Ա��λ
		Object pk_om_job = saveSubData.getAttributeValue("pk_postdoc");
		if (pk_om_job != null)
			newPsnVO.setAttributeValue("pk_om_job", pk_om_job);
		else
			newPsnVO.setAttributeValue("pk_om_job",ICorpWorkout.GENERAL_NULLVALUE);
		// ��λ����
		Object jobseries = saveSubData.getAttributeValue("pk_jobserial");
		if (jobseries != null)
			newPsnVO.setAttributeValue("jobseries", jobseries);
		else
			newPsnVO.setAttributeValue("jobseries",
					ICorpWorkout.GENERAL_NULLVALUE);
		// ��λ�ȼ�
		Object jobrank = saveSubData.getAttributeValue("pk_jobrank");
		if (jobrank != null)
			newPsnVO.setAttributeValue("jobrank", jobrank);
		else
			newPsnVO.setAttributeValue("jobrank",
					ICorpWorkout.GENERAL_NULLVALUE);
		// ְ��
		Object dutyname = saveSubData.getAttributeValue("pk_om_duty");
		if (dutyname != null)
			newPsnVO.setAttributeValue("dutyname", dutyname);
		else
			newPsnVO.setAttributeValue("dutyname",
					ICorpWorkout.GENERAL_NULLVALUE);
		// ְ���
		Object series = saveSubData.getAttributeValue("pk_detytype");
		if (series != null)
			newPsnVO.setAttributeValue("series", series);
		else
			newPsnVO.setAttributeValue("series", ICorpWorkout.GENERAL_NULLVALUE);
		return newPsnVO;
	}

	/**
	 * ��Ա���仯�����Ϊ��Ա����ѯˢ������ʾ��Ա�仯
	 * @param vo
	 * @param voname
	 * @throws Exception
	 */
	private void processTurnover(nc.vo.hi.hi_401.PsnDataVO vo) {
		//ͬ���������޸���ְ��ְ��¼
		if (vo.getAttributeValue("recordnum").toString().equalsIgnoreCase("0")) {
			//��������¼�¼&&��Ա��Ϊ����ְ������Χ��
			if (CommonValue.HI_PSNDOC_DIMISSION.equals(strTabCode)) {
				Object pk_psncl = vo.getAttributeValue("psnclafter");
				if (pk_psncl != null) {
					Integer psnclscope = (Integer) getHashPsnclScope().get(pk_psncl);
					if (psnclscope != null
							&& psnclscope.intValue() != getDataMdl().getPsnclscope()) {
//						isChangedNotWork = true;
					}
				}
			} else if (CommonValue.HI_PSNDOC_DEPTCHG.equals(strTabCode)) {
				Object pk_psncl = vo.getAttributeValue("pk_psncl");
				if (pk_psncl != null) {
					Integer psnclscope = (Integer) hashPsnclScope.get(pk_psncl);
					if (psnclscope != null
							&& ((psnclscope.intValue() == nc.vo.hi.pub.CommonValue.PSNCLSCOPE_WORK && getDataMdl().getPsnclscope() != nc.vo.hi.pub.CommonValue.PSNCLSCOPE_WORK)
								|| (psnclscope.intValue() == nc.vo.hi.pub.CommonValue.PSNCLSCOPE_OTHER && getDataMdl().getPsnclscope() != nc.vo.hi.pub.CommonValue.PSNCLSCOPE_OTHER))) {
//						isChangedToWork = true;
					}
				}
			}
		}
	}
	private boolean synWordCheck() throws BusinessException {
		String msg = ResHelper.getString("60090713", "UPP60090713-000135")/*
			 * @res
			 * "�Ƿ�ͬ������������"
			 */;
		isInPhase = (getParentUI().showYesNoMessage(msg
				) == UIDialog.ID_YES);
		if (isInPhase
				&&CommonValue.HI_PSNDOC_DEPTCHG.equals(strTabCode)
				&&UFBoolean.valueOf(saveSubData.getAttributeValue("lastflag").toString()).booleanValue()) {
			Map<UFDate,String[]> temp = new HashMap<UFDate,String[]>();
			temp.put((UFDate) saveSubData.getAttributeValue("begindate"), new String[]{parentVO.getPrimaryKey()});
			String strReturn = TRNDelegator.getTrnPub().isValidDateForUpWork(temp,false);
			if (strReturn != null && strReturn.trim().length() > 0) {
				if (getParentUI().showYesNoMessage(
						ResHelper.getString("60090713",
								"UPP60090713-000202"))/*
														 * @res
														 * "��ְ��ʼ����Ӧ������һ�����������Ŀ�ʼ���ڣ��Ƿ������"
														 */
				== UIDialog.ID_NO) {
					return false;
				}
			}
		}
		return true;
	}
}