package nc.bs.iufo.data;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.naming.NamingException;

import nc.bs.iufo.cache.IUFOBSCacheManager;
import nc.bs.iufo.pub.BSUtil;
import nc.bs.iufo.query.returnquery.ReportCommitDMO;
import nc.bs.pub.SystemException;
import nc.itf.iufo.data.IRepDataSrv;
import nc.pub.iufo.cache.KeyGroupCache;
import nc.pub.iufo.cache.MeasureCache;
import nc.pub.iufo.cache.ReportCache;
import nc.pub.iufo.cache.base.CodeCache;
import nc.pub.iufo.cache.base.UnitCache;
import nc.pub.iufo.cache.base.UserCache;
import nc.pub.iufo.exception.UFOSrvException;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.iufo.data.MeasurePubDataBO_Client;
import nc.ui.iufo.dataexchange.RepDataExport;
import nc.ui.iufo.dataexchange.base.TableDataToExcel;
import nc.ui.iufo.input.CSomeParam;
import nc.vo.iufo.code.CodeVO;
import nc.vo.iufo.data.MeasureDataVO;
import nc.vo.iufo.data.MeasurePubDataVO;
import nc.vo.iufo.data.RepDataVO;
import nc.vo.iufo.data.ReportCommitedException;
import nc.vo.iufo.keydef.KeyGroupVO;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.iufo.measure.MeasureVO;
import nc.vo.iufo.pub.DataManageObjectIufo;
import nc.vo.iufo.pub.date.UFODate;
import nc.vo.iufo.unit.UnitInfoVO;
import nc.vo.iufo.user.UserInfoVO;
import nc.vo.iuforeport.rep.ReportVO;
import nc.zq.iufo.iufotonc;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.iufo.pub.tools.DateUtil;
import com.ufsoft.iufo.check.bs.CheckResultDMO;
import com.ufsoft.iufo.check.ui.CheckResultBO_Client;
import com.ufsoft.iufo.check.vo.CheckResultVO;
import com.ufsoft.iufo.sysprop.bs.SysPropDMO;
import com.ufsoft.iufo.sysprop.ui.ISysProp;
import com.ufsoft.iufo.sysprop.vo.SysPropVO;
import com.ufsoft.report.sysplugin.excel.HSSFFontFactory;

/**
 * @update by xd 2005-05-19
 * �Ż�postMeasureDatas()���������ָ�����ݲ���Ч�ʣ�
 * @end
 * @update by л�� at (2003-12-5 9:18:46)
 * �޸�ɾ����������ʱ����Խ��Ĵ�������ѭ��ʱѭ����������
 * @end
 * @update by л�� at (2003-11-24 17:07:01)
 * �޸�ɾ����������ʱδɾ���ϱ�ȷ�ϼ�¼������
 * @end
 * @update by л�� at (2003-10-18 16:00:50)
 * �޸ļ������ݵķ���,����̬������������Ϊ0��,������.�Ӷ�ʹ�ö�̬�����ݿ���ȥȷʵ��ɾ����
 * @end
 * @update by л�� at (2003-10-18 13:24:45)
 * �޸Ĵ����������ݵķ���,ʹ֮��ɾ��ԭ�������е�����,���õ�ָ������Ϊ���.
 * @end

 * @update 2003-10-17
 * ����loadDataWithConvertCode()������֧�ּ�������ʱ���ؼ��ֺ�ָ������Ϊ�����͵�
 * ����ֵת��Ϊ�������ݡ�
 * @end

 * �˴���������������
 * �������ڣ�(2003-8-1 9:05:55)
 * @author��л��
 */
public class RepDataBO implements IRepDataSrv {

	/**
	 * RepDataBO ������ע�⡣
	 */
	public RepDataBO() {
		super();
	}
	/**
	 * �˴����뷽��������
	 * �������ڣ�(2003-10-17 11:32:08)
	 * @author��л��
	 * @param data nc.vo.iufo.data.MeasureDataVO
	 */
	private void convertCodeToContent(MeasureDataVO data) {
		if (data == null
			|| data.getMeasureVO() == null
			|| data.getMeasureVO().getType() != MeasureVO.TYPE_CODE
			|| data.getStringValue() == null) {
			return;
		}
		try {
			String codeValue = data.getStringValue();
			String codeId = data.getMeasureVO().getRefPK();
			String codeContent = getCodeInfo(codeId, codeValue);
			if (codeContent != null) {
				data.setDataValue(codeContent);
			}
		} catch (Exception e) {
			AppDebug.debug(e);//@devTools e.printStackTrace();
		}
	}
	/**
	 * �����������еĹؼ���ֵ��Ϊ�����ת��Ϊ�������ݡ�
	 * �������ڣ�(2003-10-17 14:26:52)
	 * @author��л��
	 * @param repData nc.vo.iufo.data.RepDataVO
	 */
	private void convertKeyCodeToContent(RepDataVO repData) {
		if (repData == null) {
			return;
		}
		MeasurePubDataVO[] pubDatas = repData.getPubDatas();
		if (pubDatas != null && pubDatas.length > 0) {
			for (int i = 0; i < pubDatas.length; i++) {
				convertPubKeyCodeToContent(pubDatas[i]);
			}
		}
		MeasureDataVO[][] datas = repData.getMeasureDatasAsMatrix(null);
		if (datas != null && datas.length > 0) {
			for (int i = 0; i < datas.length; i++) {
				MeasureDataVO[] oneLineDatas = datas[i];
				if (oneLineDatas != null && oneLineDatas.length > 0) {
					if (oneLineDatas[0] != null && oneLineDatas[0].hasPrvKeyValues()) {
						convertPrivKeyCodeToContent(oneLineDatas[0]);
						String[] privKeyValues = oneLineDatas[0].getPrvtKeyValues();
						for (int k = 1; k < oneLineDatas.length; k++) {
							if (oneLineDatas[k]!=null)
								oneLineDatas[k].setPrvtKeyValues(privKeyValues);
						}
					}
				}
			}
		}
	}
	/**
	 * ���ؼ�������Ϊ�����͵�˽�йؼ��ֵ�ֵת��Ϊ��������
	 * �������ڣ�(2003-10-17 14:33:47)
	 * @author��л��
	 * @param data nc.vo.iufo.data.MeasureDataVO
	 */
	private void convertPrivKeyCodeToContent(MeasureDataVO data) {
		if (data == null) {
			return;
		}
		KeyGroupVO kg = null;
		if (kg == null) {
			KeyGroupCache kgc = IUFOBSCacheManager.getSingleton().getKeyGroupCache();
			kg = kgc.getByPK(data.getPrvtKeyGroupPK());
			if (kg == null || !kg.isContainsPrivatekey()) {
				return;
			}
		}
		KeyVO[] keys = kg.getKeys();
		int pubKeyNum = 0;
		for (int i = 0; i < keys.length; i++) {
			KeyVO key = keys[i];
			String codeValue = null;
			if (!key.isPrivate() || key.getType() != KeyVO.TYPE_REF) {
				pubKeyNum++;
				continue;
			}
			try {
				codeValue = data.getKeyValueByIndex(i + 1 - pubKeyNum);
				if (codeValue == null) {
					continue;
				}
				String codeContent = getCodeInfo(key.getRef(), codeValue);
				if (codeContent != null) {
					data.setKeyValueByIndex(i + 1 - pubKeyNum, codeContent);
				}
			} catch (Exception e) {
			}
		}
	}
	/**
	 * ��ָ�깫�������йؼ���Ϊ�����͵ı���ת��Ϊ�������ݡ�
	 * �������ڣ�(2003-10-17 14:33:27)
	 * @author��л��
	 * @param pubData nc.vo.iufo.data.MeasurePubDataVO
	 */
	private void convertPubKeyCodeToContent(MeasurePubDataVO pubData) {
		if (pubData == null) {
			return;
		}
		KeyGroupVO kg = pubData.getKeyGroup();
		if (kg == null) {
			KeyGroupCache kgc = IUFOBSCacheManager.getSingleton().getKeyGroupCache();
			kg = kgc.getByPK(pubData.getKType());
			if (kg == null) {
				return;
			}
			pubData.setKeyGroup(kg);
		}
		KeyVO[] keys = kg.getKeys();
		for (int i = 0; i < keys.length; i++) {
			KeyVO key = keys[i];
			String codeValue = pubData.getKeywordByIndex(i + 1);
			if (key.getType() != KeyVO.TYPE_REF || codeValue == null) {
				continue;
			}
			try {
				String codeContent = getCodeInfo(key.getRef(), codeValue);
				if (codeContent != null) {
					pubData.setKeywordByIndex(i + 1, codeContent);
				}
			} catch (Exception e) {
			}
		}
	}
	/**
	 * ����һ�ű������ݡ�
	 * �ڱ���������Ҫ�������Ҫ��������������������ʱ���߼����⡣

	 * �����������������⣺
	 * ����̬����Ӧ�������ǹ̶�������ӳ��ʱ���Իᰴָ�깫������ѭ���������ݣ�Ч��������
	 * TODO ����createRepData�����ĵط�����Ҫ��repData���������õ�ǰ�û��ĵ��á�


	 * �������ڣ�(2003-8-1 10:13:48)
	 * @return nc.vo.iufo.data.RepDataVO
	 * @param repData nc.vo.iufo.data.RepDataVO
	 * @exception nc.pub.iufo.exception.UFOSrvException �쳣˵����
	 */
	public RepDataVO createRepData(RepDataVO repData,String strOrgPK,Hashtable hashDynAloneID) throws nc.pub.iufo.exception.UFOSrvException {
		if (repData == null) {
			return null;
		}

		try {
			checkRepCommitStatus(repData.getMainPubData().getAloneID(),repData.getReportPK(),repData.getUserID());

			MeasurePubDataDMO pdataDMO = new MeasurePubDataDMO(DataManageObjectIufo.IUFO_DATASOURCE);
			MeasureDataDMO dataDMO = new MeasureDataDMO(DataManageObjectIufo.IUFO_DATASOURCE);

			//����һ��ָ�깫������
			MeasurePubDataVO[] pubDatas = repData.getPubDatas();
			if (pubDatas == null)
				return null;
			MeasurePubDataVO pubData = null;
			//�õ������е�����ָ��
			ReportCache rc = IUFOBSCacheManager.getSingleton().getReportCache();
			UserCache userCache = IUFOBSCacheManager.getSingleton().getUserCache();

			UserInfoVO user = userCache.getUserById(repData.getUserID());
            String unitId = null;
            if(user != null){
            	unitId = user.getUnitId();
            } else {
            	AppDebug.debug("�û���ϢΪ�գ����ñ�������������λΪ����λ�� ");
            	unitId = IUFOBSCacheManager.getSingleton().getUnitCache().getRootUnitInfo().getPK();
            }
			String[] mpks = rc.getMeasurePKs(repData.getReportPK());
			MeasureCache mc = IUFOBSCacheManager.getSingleton().getMeasureCache();
			MeasureVO[] ms = mc.getMeasures(mpks);

			//�õ����������еĹؼ������PK
			MeasurePubDataBO pubDataBO = new MeasurePubDataBO();
			Object[] objs =
				pubDataBO.findByCondtionWithRepPK(repData.getMainPubData(), unitId, repData.getReportPK(),strOrgPK);
			MeasurePubDataVO[] oldPubDatas = new MeasurePubDataVO[objs.length];
			System.arraycopy(objs, 0, oldPubDatas, 0, objs.length);

			List<String> vEmptyAloneID=new ArrayList<String>();
			for (int i = 0; i < oldPubDatas.length; i++) {
				if (hashDynAloneID==null || hashDynAloneID.get(oldPubDatas[i].getKType())==null)
					vEmptyAloneID.add(oldPubDatas[i].getAloneID());
			}
			
			if (hashDynAloneID!=null && hashDynAloneID.size()>0){
				Enumeration enumKType=hashDynAloneID.keys();
				while (enumKType.hasMoreElements()){
					String strKType=(String)enumKType.nextElement();
					Vector<String> vAloneID=(Vector<String>)hashDynAloneID.get(strKType);
					Vector<String> vOneEmptyAloneID=(Vector<String>)vAloneID.clone();
					for (int i=0;i<pubDatas.length;i++){
						if (pubDatas[i].getKType().equals(strKType)==false)
							continue;
						
						vOneEmptyAloneID.remove(pubDatas[i].getAloneID());
					}
					for (int i=0;i<vOneEmptyAloneID.size();i++){
						if (vEmptyAloneID.contains(vOneEmptyAloneID.get(i))==false)
							vEmptyAloneID.add(vOneEmptyAloneID.get(i));
					}
				}
			}
			
			//ɾ����ԭ����������������,���ԭ����ָ�������������õ�ָ��,�ⲿ������ֻ�����
			dataDMO.deleteRepData(repData.getReportPK(),vEmptyAloneID.toArray(new String[0]), ms);

			//������������
			for (int i = 0; i < pubDatas.length; i++) {
				pubData = null;
				String aloneId = pubDatas[i].getAloneID();
				if (aloneId != null) {
					//���ָ�깫�����ݱ����Ƿ��Ѿ��˼�¼
					pubData = pdataDMO.findByAloneID(aloneId);
					if (pubData == null) {
						//��������ֱ�Ӵ���
						pdataDMO.createMeasurePubData(pubData);
					}
					//ɾ��(�����)�������漰�ı�������
					//dataDMO.deleteRepData(repData.getReportPK(), new String[]{aloneId}, ms);
					MeasureDataVO[] datas = repData.getMeasureDatasByAloneId(aloneId);
					//����ָ������
					dataDMO.editRepData(aloneId, datas);
				}
			}
			//����˽����¼���в���δ��˼�¼
			if (repData.getReportPK() != null) {
                String repPk = repData.getReportPK();
				CheckResultDMO resultDMO = new CheckResultDMO(DataManageObjectIufo.IUFO_DATASOURCE);
//				resultDMO.removeTaskCheckResultsByRepId(repPk,repData.getMainPubData().getAloneID());
				resultDMO.creatInputResult(
					repData.getMainPubData().getAloneID(),
					new String[] { repData.getReportPK()});
            }
			
			
			/////////////////////
			//���ڣ���λ���룬������Ա��������룬byte[] zqian
			String strDate=repData.getMainPubData().getInputDate();
			String strUnitCode=IUFOBSCacheManager.getSingleton().getUnitCache().getUnitInfoByPK(repData.getMainPubData().getUnitPK()).getCode();
			String strUserName=user==null?"":user.getNameWithCode();
			String strReportCode=IUFOBSCacheManager.getSingleton().getReportCache().getByPK(repData.getReportPK()).getCode();
			byte[] bytes=null;
			try{
				RepDataExport export=new RepDataExport();
				export.setOrgPK(strOrgPK);
				export.setLoginDate(DateUtil.getCurDay());
				CSomeParam param=new CSomeParam();
				param.setAloneId(repData.getMainPubData().getAloneID());
				param.setRepId(repData.getReportPK());
				
				ByteArrayOutputStream out=new ByteArrayOutputStream();
			    HSSFWorkbook workBook=new HSSFWorkbook();
			    HSSFFontFactory fontFactory=new HSSFFontFactory(workBook);
			    export.setParam(param);
			    TableDataToExcel.translate(export,workBook,"sheet1",fontFactory);
			    workBook.write(out);
		        out.close();
		        bytes=out.toByteArray();
		        
		        iufotonc.iufotoncbill(bytes, strUnitCode, strUserName, strDate, strReportCode);
		        doCheckRep(strReportCode, strUnitCode, strDate);
			}catch(Exception e){
				e.printStackTrace();
			}
			///
			
			////////////
		} catch (Throwable e) {
			AppDebug.debug(e);
			throw new UFOSrvException(null, e);
		}
		return repData;

	}

	/**
	 * ��鱨���Ƿ��Ѿ��ϱ���������ϱ������׳��쳣��֪ͨUI��
	 * @param repData
	 * @throws NamingException
	 * @throws SystemException
	 * @throws SQLException
	 * @throws ReportCommitedException
	 */
	private void checkRepCommitStatus(String strAloneID,String strReportPK,String strUserPK)
		throws NamingException, SystemException, SQLException, ReportCommitedException {
		ReportCommitDMO commitDMO = new ReportCommitDMO(DataManageObjectIufo.IUFO_DATASOURCE);
		int[] flag =
			commitDMO.loadRptCommitFlag(strAloneID, new String[] { strReportPK});
		if (flag != null && flag.length > 0) {
			if (flag[0] > 0) { //�����Ѿ��ϱ������޷��޸�
				//����Ƿ��ǳ����û�
				if(strUserPK != null){
					UserInfoVO userInfo = IUFOBSCacheManager.getSingleton().getUserCache().getUserById(strUserPK);
					if(userInfo.getRole() == UserInfoVO.SUPER_USER){
						//�ǳ����û�,���ϵͳ�����У������û��Ƿ�����޸��ϱ���������
						SysPropDMO sysPropDMO = new SysPropDMO(DataManageObjectIufo.IUFO_DATASOURCE);
						SysPropVO canModify = sysPropDMO.getSysProp(ISysProp.CAN_MODIFY_DATA);
						//�����û������޸����ϱ���������
						if(canModify != null && "true".equalsIgnoreCase(canModify.getValue())){
							return;
						}
					}
				}
				throw new ReportCommitedException("miufo1001863");  //"�����Ѿ��ϱ�ȷ��,�޷��޸�!"
			}
		}
	}

	/**
	 * �ҵ����зǶ�̬����ָ��
	 * �������ڣ�(2003-9-18 10:27:46)
	 * @return nc.vo.iufo.measure.MeasureVO[]
	 * @param src nc.vo.iufo.measure.MeasureVO[]
	 * @param mainKeyCombPK java.lang.String
	 */
	private MeasureVO[] filterFixedMeasures(MeasureVO[] src, String repPK, String mainKeyCombPK) {
		Vector vec = new Vector();
		MeasureCache mc = IUFOBSCacheManager.getSingleton().getMeasureCache();
		for (int i = 0; i < src.length; i++) {
			String keypk = mc.getKeyCombPk(src[i].getCode());
			if (mainKeyCombPK.equals(keypk))
				vec.add(src[i]);
		}
		MeasureVO[] result = new MeasureVO[vec.size()];
		vec.copyInto(result);
		return result;
	}
	/**
	 * �˴����뷽��������
	 * �������ڣ�(2003-10-17 14:45:08)
	 * @author��л��
	 * @return java.lang.String
	 * @param codeId java.lang.String
	 * @param codeInfoId java.lang.String
	 */
	private String getCodeInfo(String codeId, String codeInfoId) throws Exception {
		CodeCache cc = IUFOBSCacheManager.getSingleton().getCodeCache();
		CodeVO code = new CodeVO();
		code.setId(codeId);
		String codeContent = cc.getAllContentByID(code, codeInfoId);
		return codeContent;
	}
	/**
	 * ��ָ�깫�����ݰ������ؼ���ֵ��Ϸ��顣����֮���ÿһ��ָ�깫�����ݣ�
	 * ������һ��RepDataVO��
	 * �������ڣ�(2003-8-6 10:03:36)
	 * @return java.util.Hashtable
	 * @param String keyGroupPK ��������ؼ������PK
	 * @param pubDatas nc.vo.iufo.data.MeasurePubDataVO[]
	 */
	private ArrayList[] groupPubDatas(String keyGroupPK, MeasurePubDataVO[] pubDatas) {

		if (keyGroupPK == null) {
			return null;
		}
		//�ҳ����ؼ�����϶�Ӧ��ָ�깫������
		Vector vec = new Vector();
		for (int i = 0; i < pubDatas.length; i++) {
			if (keyGroupPK.equals(pubDatas[i].getKType())) {
				vec.add(pubDatas[i]);
			}
		}

		//�ٸ��������ҳ������ؼ�����϶�Ӧ��ָ�깫�����ݷ������е�ָ�깫������
		Hashtable result = new Hashtable();
		for (int k = 0; k < pubDatas.length; k++) {
			for (int i = 0; i < vec.size(); i++) {
				MeasurePubDataVO pubData = (MeasurePubDataVO) vec.elementAt(i);
				if (pubData.canContains(pubDatas[k]) || pubData.equals(pubDatas[k])) {
					ArrayList list = (ArrayList) result.get(pubData.getAloneID());
					if (list == null) {
						list = new ArrayList();
						result.put(pubData.getAloneID(), list);
					}
					list.add(pubDatas[k]);
					break;
				}
			}
		}
		ArrayList[] retValue = new ArrayList[result.size()];

		Enumeration elements = result.elements();
		int iPos = 0;
		while (elements.hasMoreElements())
			retValue[iPos++] = (ArrayList) elements.nextElement();

		return retValue;
	}
	/**
	 * �˴����뷽��������
	 * �������ڣ�(2003-8-6 10:18:05)
	 * @return boolean
	 * @param aloneId java.lang.String
	 * @param pubDatas java.util.ArrayList
	 */
	private boolean isIncludedBy(String aloneId, ArrayList pubDatas) {
		if (aloneId == null)
			return false;
		for (int i = 0; i < pubDatas.size(); i++) {
			MeasurePubDataVO pubData = (MeasurePubDataVO) pubDatas.get(i);
			if (aloneId.equals(pubData.getAloneID())) {
				return true;
			}
		}
		return false;
	}
	/**
	 * �����ݿ��м��ر������ݡ�
	 * Ϊ�õ������ȫ�����ݣ��ڱ�ʾ��ѯ������pubData�У���������ktype(���ؼ�����ϵ�PKֵ)
	 * �����޷��õ���̬�������ݡ�

	 * �������ڣ�(2003-8-1 9:08:17)
	 * @return nc.vo.iufo.data.RepDataVO[]
	 * @param repPK java.lang.String ��������
	 * @param pubData MeasurePubDataVO �˲���ȷ����ѯ����,��ؼ���ֵ���汾�ŵ���Ϣ
	 * @exception nc.pub.iufo.exception.UFOSrvException �쳣˵����
	 */
	public RepDataVO[] loadRepData(String repPK, String curUserID, MeasurePubDataVO pubData,String strOrgPK) throws UFOSrvException {
		return loadRepDataWithConvertCode(repPK, curUserID, pubData, false,strOrgPK);
	}
	/**
	 * �����ݿ��м��ر������ݡ�
	 * Ϊ�õ������ȫ�����ݣ��ڱ�ʾ��ѯ������pubData�У���������ktype(���ؼ�����ϵ�PKֵ)
	 * �����޷��õ���̬�������ݡ�

	 * �������ڣ�(2003-8-1 9:08:17)
	 * @return nc.vo.iufo.data.RepDataVO[]
	 * @param repPK java.lang.String ��������
	 * @param pubData MeasurePubDataVO �˲���ȷ����ѯ����,��ؼ���ֵ���汾�ŵ���Ϣ
	 * @exception nc.pub.iufo.exception.UFOSrvException �쳣˵����
	 */
	public RepDataVO[] loadRepDataWithConvertCode(
		String repPK,
		String curUserID,
		MeasurePubDataVO pubData,
		boolean isConvertCode,
		String strOrgPK)
		throws UFOSrvException {
		if (repPK == null || pubData == null) {
			return null;
		}
		try {
			Object[] retValue = new Object[3];
			String strKType = pubData.getKType();
			pubData.setKType(null);
			if (preProcess(repPK, curUserID, pubData, retValue, strOrgPK) == false) {
				pubData.setKType(strKType);
				return null;
			}
			pubData.setKType(strKType);

			ArrayList[] pubDataGroups = (ArrayList[]) retValue[2];
			MeasureVO[] measures = (MeasureVO[]) retValue[1];
			String mainKeyGroupPK = (String) retValue[0];

			//��ָ�깫�����ݼ�ָ��������֯��RepDataVO
			RepDataVO[] result = new RepDataVO[pubDataGroups.length];
			MeasureDataDMO dataDMO = new MeasureDataDMO(DataManageObjectIufo.IUFO_DATASOURCE);

			for (int i = 0; i < pubDataGroups.length; i++) {
				ArrayList list = pubDataGroups[i];
				String[] aloneIds = new String[list.size()];
				MeasurePubDataVO[] pds = new MeasurePubDataVO[list.size()];
				list.toArray(pds);
				for (int k = 0; k < pds.length; k++) {
					aloneIds[k] = pds[k].getAloneID();
				}
				//����ָ������
				MeasureDataVO[] datas = dataDMO.getRepData(aloneIds, measures);

				//�������ת��Ϊ��������
				if (isConvertCode) {
					for (int k = 0; datas != null && k < datas.length; k++) {
						convertCodeToContent(datas[k]);
					}
				}

				//����һЩ��Ϊ��¼��ؼ�����Ϻ���뵽�����еı��е�ָ��ֵ����Щָ��ֵ��Ϊû�г�ʼ����
				//�����ݿ���ȡ��ʱ����û�����ǵ�ֵ�����ֹ�����������Щֵ��ע��ֻ���������Ӧ��ָ��
				//ֵ����̬���е�ֵ������
				MeasureVO[] fixedMeasures = filterFixedMeasures(measures, repPK, mainKeyGroupPK);
				if (fixedMeasures != null && fixedMeasures.length > 0) {
					//�ҵ������Ӧ��aloneId
					String mainAloneId = null;
					for (int k = 0; k < pds.length; k++) {
						if (pds[k].getKType().equals(mainKeyGroupPK)) {
							mainAloneId = pds[k].getAloneID();
							break;
						}
					}
					datas = postMeasureDatas(datas, fixedMeasures, mainAloneId, mainKeyGroupPK);
				}
				RepDataVO repData = new RepDataVO(repPK, mainKeyGroupPK);
				repData.setDatas(pds, datas);
				repData.setUserID(curUserID);
				//����ؼ��ֱ����͵ģ�ת���ؼ��ֵ�ֵΪ��������
				if (isConvertCode) {
					convertKeyCodeToContent(repData);
				}
				//���˶�̬��������ȫΪ0����
				repData.filterZeroLines();
				result[i] = repData;
			}

			return result;
		} catch (Exception e) {
			AppDebug.debug(e);//@devTools e.printStackTrace();
			throw new UFOSrvException(null, e);
		}
	}
	/**
	 * �˴����뷽��������
	 * �������ڣ�(2003-9-18 10:31:21)
	 * @return nc.vo.iufo.data.MeasureDataVO[]
	 * @param datas nc.vo.iufo.data.MeasureDataVO[]
	 */
	private MeasureDataVO[] postMeasureDatas(
		MeasureDataVO[] datas,
		MeasureVO[] fixedMeasures,
		String aloneId,
		String mainKeyCombPK) {
		Vector vec = new Vector();
		vec.addAll(Arrays.asList(datas));
		HashSet hashMCodes = new HashSet();
		for(int i=0; i<datas.length; i++)
		    hashMCodes.add(datas[i].getMeasureVO().getCode());
		for (int i = 0; i < fixedMeasures.length; i++) {
			boolean isExist = false;
			if (!hashMCodes.contains(fixedMeasures[i].getCode())) {
				MeasureDataVO data = new MeasureDataVO();
				data.setAloneID(aloneId);
				data.setMeasureVO(fixedMeasures[i]);
				data.setRowNo(0);
				data.setPrvtKeyGroupPK(mainKeyCombPK);
				vec.add(data);
			}
		}
		MeasureDataVO[] result = new MeasureDataVO[vec.size()];
		vec.copyInto(result);
		return result;
	}
	/**
	 * ���ط�����ɾ��������Ԥ������̡�
	 * �������ڣ�(2003-8-20 10:23:40)
	 */
	private boolean preProcess(String repPK, String curUserID, MeasurePubDataVO pubData, Object[] retValue,String strOrgPK)
		throws Exception {
		//���ݵ�ǰ�û����õ���ǰ��λ
		String curUnitID = getCurUnitID(curUserID);
//		MeasurePubDataDMO pdataDMO = new MeasurePubDataDMO(DataManageObjectIufo.IUFO_DATASOURCE);
		MeasurePubDataBO pubDataBO = new MeasurePubDataBO();
//		Vector vec = null;
		MeasurePubDataVO[] pubDatas = null;
		if (pubData != null) {
			Object[] objs = pubDataBO.findByCondtionWithRepPK(pubData, curUnitID, repPK,strOrgPK);
			if (objs != null && objs.length > 0) {
				pubDatas = new MeasurePubDataVO[objs.length];
				System.arraycopy(objs, 0, pubDatas, 0, objs.length);
			}
		} else{
			//modify by chxw 2007-6-19 ��������������ϴ�ʱ���ڴ���������⣻
			//ɾ����������ʱ��ֻ���ظñ����MeasurePubDataVO[]
			String[] aloneIDs = getMeasureDataIDsByRepPK(repPK);
			pubDatas = (aloneIDs == null || aloneIDs.length == 0)? null : pubDataBO.findByAloneIDs(aloneIDs);
			//pubDatas = pubDataBO.loadPubDataByRepPK(repPK);
		}
		if (pubDatas == null || pubDatas.length == 0) {
			return false;
		}
		//�õ�������
		ReportCache rc = IUFOBSCacheManager.getSingleton().getReportCache();

		//�õ������е�����ָ��
		MeasureVO[] measures = BSUtil.findMeasuresByRepPKs(new String[] { repPK });
		if (measures == null || measures.length == 0) {
			return false;
		}

		//�����س���ָ�깫�����ݰ������Ӧ�����ؼ�����Ϸ��飬���������ڱ������ָ�깫�������˳�
		String mainKeyGroupPK = rc.getByPks(new String[] { repPK })[0].getKeyCombPK();
		ArrayList[] pubDataGroups = groupPubDatas(mainKeyGroupPK, pubDatas);
		retValue[0] = mainKeyGroupPK;
		retValue[1] = measures;
		retValue[2] = pubDataGroups;
		return true;
	}

	/**
	 * ���ݱ���PK���õ�����ָ�����ݵ�aloneID
	 * @param reportPK
	 * @return
	 * @throws Exception
	 */
	private String[] getMeasureDataIDsByRepPK(String reportPK)
		throws Exception {
		if(reportPK == null)
			return null;
		
		MeasureVO[] measures = BSUtil.findMeasuresByRepPKs(new String[] {reportPK});
		if(measures == null || measures.length == 0){
			return null;
		}
		
		MeasureDataDMO dataDMO = new MeasureDataDMO(DataManageObjectIufo.IUFO_DATASOURCE);
		MeasureDataVO[] measureDatas = dataDMO.getRepData(measures);
		String[] measureDataIDs = new String[measureDatas.length];
		for(int i = 0; i < measureDatas.length; i++){
			measureDataIDs[i] = measureDatas[i].getAloneID();
		}
		return measureDataIDs;
	}
	/**
	 * ���ݵ�ǰ�û���ȡ�õ�ǰ��¼��λID
	 * @param curUserID
	 * @return
	 */
	private String getCurUnitID(String curUserID) {
		String curUnitID = null;
		UserCache uc = IUFOUICacheManager.getSingleton().getUserCache();
		UserInfoVO userInfo = uc.getUserById(curUserID);
		if (userInfo != null) {
			curUnitID = userInfo.getUnitId();
		}
		return curUnitID;
	}
	/**
	 * ɾ������̬�����ݵ�Ԥ������̡�
	 * �������ڣ�(2003-8-20 10:23:40)
	 */
	private boolean preProcess1(String repPK, String dynKeyCombPK, Object[] retValue) throws Exception {
		//�õ�������
		ReportCache rc = IUFOBSCacheManager.getSingleton().getReportCache();
		MeasureCache mc = IUFOBSCacheManager.getSingleton().getMeasureCache();
		//�����س���ָ�깫�����ݰ������Ӧ�����ؼ�����Ϸ��飬���������ڱ������ָ�깫�������˳�
		ReportVO[] reportVOs = rc.getByPks(new String[] { repPK });
		if(reportVOs[0] == null){//zzl 2006.9.21
			return false;
		}
		String mainKeyGroupPK = reportVOs[0].getKeyCombPK();
		KeyGroupCache kc = IUFOBSCacheManager.getSingleton().getKeyGroupCache();
		KeyGroupVO kg = kc.getByPK(dynKeyCombPK);
		//liuyy. 2005-3-3
		if(kg == null){
		    return false;
		}
		//�Ƿ���˽�йؼ��֣������ɾ��˽�йؼ�����϶�Ӧ�Ķ�̬�����ݣ�Ҫͨ������Ĺؼ�������ҵ�
		//���е�aloneId
		KeyVO[] keys = kg.getPrivatekey();
		String[] strKeyCombPKs = null;
		if (keys == null || keys.length == 0) {
			strKeyCombPKs = new String[] { mainKeyGroupPK };
		} else {
			strKeyCombPKs = new String[] { dynKeyCombPK };
		}
		//�ҵ����������Ķ�̬�����ݶ�Ӧ�����������¼
		MeasurePubDataBO pubDataBO = new MeasurePubDataBO();
		MeasurePubDataVO[] pubDatas = pubDataBO.loadPubDataByKeyCombPKs(strKeyCombPKs);
		if (pubDatas == null || pubDatas.length == 0) {
			return false;
		}

		//�õ������е�����ָ��
		MeasureVO[] measures = BSUtil.findMeasuresByRepPKs(new String[] { repPK });
		if (measures == null || measures.length == 0) {
			return false;
		}
		Vector<MeasureVO> vec1 = new Vector<MeasureVO>();
		for (int i = 0; i < measures.length; i++) {
			//����ֻ���ڴ˶�̬����ָ�꣬ע��Ҫ���˵���������ָ��
			if (measures[i].getReportPK().equals(repPK)
				&& dynKeyCombPK.equals(mc.getKeyCombPk(measures[i].getCode())))
				vec1.add(measures[i]);
		}
		MeasureVO[] dynMs = new MeasureVO[vec1.size()];
		vec1.copyInto(dynMs);

		ArrayList[] pubDataGroups =new ArrayList[]{new ArrayList(Arrays.asList(pubDatas))};
		retValue[0] = mainKeyGroupPK;
		retValue[1] = dynMs;
		retValue[2] = pubDataGroups;
		return true;
	}
	/**
	 * ɾ��һ�ű��������
	 * ��������ɾ�������ӱ��е�ָ�����ݼ�˽�йؼ������ݣ���ɾ��ָ�����������е�PubData


	 * �������ڣ�(2003-8-1 9:15:13)
	 * @param repPK java.lang.String ��������
	 * @param pubData nc.vo.iufo.data.MeasurePubDataVO ȷ��ɾ����������ؼ���ֵ���汾�ŵ�
	 * @param deleteRefMeasureDatas �Ƿ�ɾ�����õ�ָ�������
	 * @exception nc.pub.iufo.exception.UFOSrvException �쳣˵����
	 */
	public void removeRepData(String repPK, String curUserID, MeasurePubDataVO pubData, boolean deleteRefMeasureDatas,String strOrgPK)
		throws nc.pub.iufo.exception.UFOSrvException {
		if (repPK == null) {
			return;
		}
		String curUnitID = getCurUnitID(curUserID);
		try {
			if(pubData != null)
				checkRepCommitStatus(pubData.getAloneID(),repPK,curUserID);
			
			Object[] retValue = new Object[3];
			if (preProcess(repPK, curUserID, pubData, retValue,strOrgPK) == false) {
				return;
			}
			ArrayList[] pubDataGroups = (ArrayList[]) retValue[2];
			MeasureVO[] measures = (MeasureVO[]) retValue[1];
			String mainKeyGroupPK = (String) retValue[0];

			if (measures == null || measures.length == 0)
				return;
			
			if(!deleteRefMeasureDatas){
				//ȡ��ֻ���ڱ����ָ��
				measures = extractSelfMeasures(repPK, measures);
			}
			if(measures == null || measures.length == 0){
			    return;
			}

			MeasureDataDMO dataDMO = new MeasureDataDMO(DataManageObjectIufo.IUFO_DATASOURCE);
			CheckResultDMO resultDMO = new CheckResultDMO(DataManageObjectIufo.IUFO_DATASOURCE);

			for (int i = 0; i < pubDataGroups.length; i++) {
				ArrayList list = pubDataGroups[i];
				String[] aloneIds = new String[list.size()];
				MeasurePubDataVO[] pds = new MeasurePubDataVO[list.size()];
				list.toArray(pds);
				String mainAloneID = null;
				for (int k = 0; k < pds.length; k++) {
					aloneIds[k] = pds[k].getAloneID();
					//�ҵ������aloneID
					if (mainKeyGroupPK.equals(pds[k].getKType())) {
						mainAloneID = aloneIds[k];
					}
				}
				dataDMO.deleteRepData(repPK, aloneIds, measures);
				//ɾ���ϱ�ȷ�ϼ�¼
				if (mainAloneID != null) {
					resultDMO.removeTaskCheckResultsByRepId(repPK,mainAloneID);
					resultDMO.removeByRepIdAloneId(repPK, mainAloneID);
				}
			}

		} catch (Exception e) {
			AppDebug.debug(e);//@devTools e.printStackTrace();
			throw new UFOSrvException(null, e);
		}

	}
	/**
     * @param repPK
     * @param measures
     */
    private MeasureVO[] extractSelfMeasures(String repPK, MeasureVO[] measures) {
        Vector vec = new Vector();
        for (int i = 0; i < measures.length; i++) {
        	if (repPK.equals(measures[i].getReportPK())) {
        		vec.add(measures[i]);
        	}
        }
        MeasureVO[] selfMeasures = new MeasureVO[vec.size()];
        vec.copyInto(selfMeasures);
        return selfMeasures;
    }
    /**
	 * ɾ��һ�ű����е�ĳ����̬��������
	 * ��������ɾ�������ӱ��е�ָ�����ݼ�˽�йؼ������ݣ���ɾ��ָ�����������е�PubData

	 * �������ڣ�(2003-8-1 9:15:13)
	 * @param repPK java.lang.String ��������
	 * @exception nc.pub.iufo.exception.UFOSrvException �쳣˵����
	 */
	public void removeSubRepData(String repPK, String dynAreaPK) throws nc.pub.iufo.exception.UFOSrvException {
		if (repPK == null || dynAreaPK == null) {
			return;
		}
		try {
			Object[] retValue = new Object[3];
			if (preProcess1(repPK, dynAreaPK, retValue) == false) {
				return;
			}
			ArrayList[] pubDataGroups = (ArrayList[]) retValue[2];
			MeasureVO[] measures = (MeasureVO[]) retValue[1];
//			String mainKeyGroupPK = (String) retValue[0];

			MeasureDataDMO dataDMO = new MeasureDataDMO(DataManageObjectIufo.IUFO_DATASOURCE);

			for (int i = 0; i < pubDataGroups.length; i++) {
				ArrayList list = pubDataGroups[i];
				String[] aloneIds = new String[list.size()];
				MeasurePubDataVO[] pds = new MeasurePubDataVO[list.size()];
				list.toArray(pds);
				for (int k = 0; k < pds.length; k++) {
					aloneIds[k] = pds[k].getAloneID();
				}
				dataDMO.deleteRepData(repPK, aloneIds, measures);
			}
		} catch (Exception e) {
			AppDebug.debug(e);//@devTools e.printStackTrace();
			throw new UFOSrvException(null, e);
		}

	}
    /**
	 * ������˻�д���������ݱ����ţ���λ���룬���ڽ��б������
	 * �������ڣ�(2009-8-1 9:15:13)
	 * @param 
	 * @exception �쳣˵����
	 */
	public void doCheckRep(String strRepCode,String strUnitCode,String strInputDate) throws Exception{
		UnitCache unitCache=IUFOBSCacheManager.getSingleton().getUnitCache();
		ReportCache repCache=IUFOBSCacheManager.getSingleton().getReportCache();
		KeyGroupCache kgCache=IUFOBSCacheManager.getSingleton().getKeyGroupCache();
		
		UnitInfoVO unitInfo=unitCache.getUnitInfoByCode(strUnitCode);
		ReportVO report=repCache.getByCode(strRepCode, false);
		KeyGroupVO keyGroup=kgCache.getByPK(report.getKeyCombPK());
		
		MeasurePubDataVO pubData=new MeasurePubDataVO();
		pubData.setKType(keyGroup.getKeyGroupPK());
		pubData.setKeyGroup(keyGroup);
		
		strInputDate=new UFODate(strInputDate).getEndDay(keyGroup.getTimeProp()).toString();
		pubData.setInputDate(strInputDate);
		pubData.setKeyword1(unitInfo.getPK());
		pubData.setKeyword2(strInputDate);
		
		String strAloneID=MeasurePubDataBO_Client.getAloneID(pubData);
		
		CheckResultVO result=new CheckResultVO();
		result.setAloneId(strAloneID);
		result.setRepId(report.getReportPK());
		result.setCheckState(CheckResultVO.PASS);
		result.setCheckTime(DateUtil.getCurTime());
		CheckResultBO_Client.creatCheckResult(result);
	}
}
