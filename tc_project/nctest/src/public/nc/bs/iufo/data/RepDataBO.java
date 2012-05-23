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
 * 优化postMeasureDatas()方法，提高指标数据查找效率；
 * @end
 * @update by 谢东 at (2003-12-5 9:18:46)
 * 修改删除报表数据时数组越界的错误。两重循环时循环变量错误。
 * @end
 * @update by 谢东 at (2003-11-24 17:07:01)
 * 修改删除报表数据时未删除上报确认记录的问题
 * @end
 * @update by 谢东 at (2003-10-18 16:00:50)
 * 修改加载数据的方法,当动态区中整行数据为0行,不加载.从而使得动态区数据看上去确实被删除了
 * @end
 * @update by 谢东 at (2003-10-18 13:24:45)
 * 修改创建报表数据的方法,使之能删除原来报表中的数据,引用的指标数据为清空.
 * @end

 * @update 2003-10-17
 * 增加loadDataWithConvertCode()方法，支持加载数据时将关键字和指标数据为编码型的
 * 编码值转换为编码内容。
 * @end

 * 此处插入类型描述。
 * 创建日期：(2003-8-1 9:05:55)
 * @author：谢东
 */
public class RepDataBO implements IRepDataSrv {

	/**
	 * RepDataBO 构造子注解。
	 */
	public RepDataBO() {
		super();
	}
	/**
	 * 此处插入方法描述。
	 * 创建日期：(2003-10-17 11:32:08)
	 * @author：谢东
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
	 * 将报表数据中的关键字值中为编码的转换为编码内容。
	 * 创建日期：(2003-10-17 14:26:52)
	 * @author：谢东
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
	 * 将关键字类型为编码型的私有关键字的值转换为编码内容
	 * 创建日期：(2003-10-17 14:33:47)
	 * @author：谢东
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
	 * 将指标公共数据中关键字为编码型的编码转换为编码内容。
	 * 创建日期：(2003-10-17 14:33:27)
	 * @author：谢东
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
	 * 创建一张报表数据。
	 * 在本方法中需要处理的主要问题是数据在批量更改时的逻辑问题。

	 * 本方法现有如下问题：
	 * 当动态区对应的数据是固定表数据映射时，仍会按指标公共数据循环增加数据，效率有问题
	 * TODO 引用createRepData方法的地方，需要在repData中增加设置当前用户的调用。


	 * 创建日期：(2003-8-1 10:13:48)
	 * @return nc.vo.iufo.data.RepDataVO
	 * @param repData nc.vo.iufo.data.RepDataVO
	 * @exception nc.pub.iufo.exception.UFOSrvException 异常说明。
	 */
	public RepDataVO createRepData(RepDataVO repData,String strOrgPK,Hashtable hashDynAloneID) throws nc.pub.iufo.exception.UFOSrvException {
		if (repData == null) {
			return null;
		}

		try {
			checkRepCommitStatus(repData.getMainPubData().getAloneID(),repData.getReportPK(),repData.getUserID());

			MeasurePubDataDMO pdataDMO = new MeasurePubDataDMO(DataManageObjectIufo.IUFO_DATASOURCE);
			MeasureDataDMO dataDMO = new MeasureDataDMO(DataManageObjectIufo.IUFO_DATASOURCE);

			//创建一组指标公共数据
			MeasurePubDataVO[] pubDatas = repData.getPubDatas();
			if (pubDatas == null)
				return null;
			MeasurePubDataVO pubData = null;
			//得到报表中的所有指标
			ReportCache rc = IUFOBSCacheManager.getSingleton().getReportCache();
			UserCache userCache = IUFOBSCacheManager.getSingleton().getUserCache();

			UserInfoVO user = userCache.getUserById(repData.getUserID());
            String unitId = null;
            if(user != null){
            	unitId = user.getUnitId();
            } else {
            	AppDebug.debug("用户信息为空，设置报表数据所属单位为根单位。 ");
            	unitId = IUFOBSCacheManager.getSingleton().getUnitCache().getRootUnitInfo().getPK();
            }
			String[] mpks = rc.getMeasurePKs(repData.getReportPK());
			MeasureCache mc = IUFOBSCacheManager.getSingleton().getMeasureCache();
			MeasureVO[] ms = mc.getMeasures(mpks);

			//得到报表中所有的关键字组合PK
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
			
			//删除掉原来报表中所有数据,如果原来的指标数据中有引用的指标,这部分数据只被清空
			dataDMO.deleteRepData(repData.getReportPK(),vEmptyAloneID.toArray(new String[0]), ms);

			//创建报表数据
			for (int i = 0; i < pubDatas.length; i++) {
				pubData = null;
				String aloneId = pubDatas[i].getAloneID();
				if (aloneId != null) {
					//检查指标公共数据表中是否已经此记录
					pubData = pdataDMO.findByAloneID(aloneId);
					if (pubData == null) {
						//不存在则直接创建
						pdataDMO.createMeasurePubData(pubData);
					}
					//删除(或清空)本报表涉及的报表数据
					//dataDMO.deleteRepData(repData.getReportPK(), new String[]{aloneId}, ms);
					MeasureDataVO[] datas = repData.getMeasureDatasByAloneId(aloneId);
					//增加指标数据
					dataDMO.editRepData(aloneId, datas);
				}
			}
			//向审核结果记录表中插入未审核记录
			if (repData.getReportPK() != null) {
                String repPk = repData.getReportPK();
				CheckResultDMO resultDMO = new CheckResultDMO(DataManageObjectIufo.IUFO_DATASOURCE);
//				resultDMO.removeTaskCheckResultsByRepId(repPk,repData.getMainPubData().getAloneID());
				resultDMO.creatInputResult(
					repData.getMainPubData().getAloneID(),
					new String[] { repData.getReportPK()});
            }
			
			
			/////////////////////
			//日期，单位编码，操作人员，报表编码，byte[] zqian
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
	 * 检查报表是否已经上报过，如果上报过，抛出异常以通知UI端
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
			if (flag[0] > 0) { //报表已经上报过，无法修改
				//检查是否是超级用户
				if(strUserPK != null){
					UserInfoVO userInfo = IUFOBSCacheManager.getSingleton().getUserCache().getUserById(strUserPK);
					if(userInfo.getRole() == UserInfoVO.SUPER_USER){
						//是超级用户,检查系统参数中，超级用户是否可以修改上报过的数据
						SysPropDMO sysPropDMO = new SysPropDMO(DataManageObjectIufo.IUFO_DATASOURCE);
						SysPropVO canModify = sysPropDMO.getSysProp(ISysProp.CAN_MODIFY_DATA);
						//超级用户可以修改已上报过的数据
						if(canModify != null && "true".equalsIgnoreCase(canModify.getValue())){
							return;
						}
					}
				}
				throw new ReportCommitedException("miufo1001863");  //"报表已经上报确认,无法修改!"
			}
		}
	}

	/**
	 * 找到所有非动态区的指标
	 * 创建日期：(2003-9-18 10:27:46)
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
	 * 此处插入方法描述。
	 * 创建日期：(2003-10-17 14:45:08)
	 * @author：谢东
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
	 * 将指标公共数据按报表及关键字值组合分组。分组之后的每一组指标公共数据，
	 * 将属于一个RepDataVO。
	 * 创建日期：(2003-8-6 10:03:36)
	 * @return java.util.Hashtable
	 * @param String keyGroupPK 报表的主关键字组合PK
	 * @param pubDatas nc.vo.iufo.data.MeasurePubDataVO[]
	 */
	private ArrayList[] groupPubDatas(String keyGroupPK, MeasurePubDataVO[] pubDatas) {

		if (keyGroupPK == null) {
			return null;
		}
		//找出主关键字组合对应的指标公共数据
		Vector vec = new Vector();
		for (int i = 0; i < pubDatas.length; i++) {
			if (keyGroupPK.equals(pubDatas[i].getKType())) {
				vec.add(pubDatas[i]);
			}
		}

		//再根据上面找出的主关键字组合对应的指标公共数据分组所有的指标公共数据
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
	 * 此处插入方法描述。
	 * 创建日期：(2003-8-6 10:18:05)
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
	 * 从数据库中加载报表数据。
	 * 为得到报表的全部数据，在表示查询条件的pubData中，不能设置ktype(即关键字组合的PK值)
	 * 否则将无法得到动态区的数据。

	 * 创建日期：(2003-8-1 9:08:17)
	 * @return nc.vo.iufo.data.RepDataVO[]
	 * @param repPK java.lang.String 报表主键
	 * @param pubData MeasurePubDataVO 此参数确定查询条件,如关键字值，版本号等信息
	 * @exception nc.pub.iufo.exception.UFOSrvException 异常说明。
	 */
	public RepDataVO[] loadRepData(String repPK, String curUserID, MeasurePubDataVO pubData,String strOrgPK) throws UFOSrvException {
		return loadRepDataWithConvertCode(repPK, curUserID, pubData, false,strOrgPK);
	}
	/**
	 * 从数据库中加载报表数据。
	 * 为得到报表的全部数据，在表示查询条件的pubData中，不能设置ktype(即关键字组合的PK值)
	 * 否则将无法得到动态区的数据。

	 * 创建日期：(2003-8-1 9:08:17)
	 * @return nc.vo.iufo.data.RepDataVO[]
	 * @param repPK java.lang.String 报表主键
	 * @param pubData MeasurePubDataVO 此参数确定查询条件,如关键字值，版本号等信息
	 * @exception nc.pub.iufo.exception.UFOSrvException 异常说明。
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

			//将指标公共数据及指标数据组织成RepDataVO
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
				//加载指标数据
				MeasureDataVO[] datas = dataDMO.getRepData(aloneIds, measures);

				//处理编码转换为编码内容
				if (isConvertCode) {
					for (int k = 0; datas != null && k < datas.length; k++) {
						convertCodeToContent(datas[k]);
					}
				}

				//处理一些因为在录入关键字组合后加入到任务中的表中的指标值，这些指标值因为没有初始化，
				//从数据库中取出时，将没有它们的值。现手工处理，加入这些值。注意只加入主表对应的指标
				//值，动态区中的值不处理
				MeasureVO[] fixedMeasures = filterFixedMeasures(measures, repPK, mainKeyGroupPK);
				if (fixedMeasures != null && fixedMeasures.length > 0) {
					//找到主表对应的aloneId
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
				//如果关键字编码型的，转换关键字的值为编码内容
				if (isConvertCode) {
					convertKeyCodeToContent(repData);
				}
				//过滤动态区数据中全为0的行
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
	 * 此处插入方法描述。
	 * 创建日期：(2003-9-18 10:31:21)
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
	 * 加载方法与删除方法的预处理过程。
	 * 创建日期：(2003-8-20 10:23:40)
	 */
	private boolean preProcess(String repPK, String curUserID, MeasurePubDataVO pubData, Object[] retValue,String strOrgPK)
		throws Exception {
		//根据当前用户，得到当前单位
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
			//modify by chxw 2007-6-19 解决报表数据量较大时，内存溢出的问题；
			//删除报表数据时，只加载该报表的MeasurePubDataVO[]
			String[] aloneIDs = getMeasureDataIDsByRepPK(repPK);
			pubDatas = (aloneIDs == null || aloneIDs.length == 0)? null : pubDataBO.findByAloneIDs(aloneIDs);
			//pubDatas = pubDataBO.loadPubDataByRepPK(repPK);
		}
		if (pubDatas == null || pubDatas.length == 0) {
			return false;
		}
		//得到报表缓存
		ReportCache rc = IUFOBSCacheManager.getSingleton().getReportCache();

		//得到报表中的所有指标
		MeasureVO[] measures = BSUtil.findMeasuresByRepPKs(new String[] { repPK });
		if (measures == null || measures.length == 0) {
			return false;
		}

		//将加载出的指标公共数据按报表对应的主关键字组合分组，并将不属于本报表的指标公共数据滤除
		String mainKeyGroupPK = rc.getByPks(new String[] { repPK })[0].getKeyCombPK();
		ArrayList[] pubDataGroups = groupPubDatas(mainKeyGroupPK, pubDatas);
		retValue[0] = mainKeyGroupPK;
		retValue[1] = measures;
		retValue[2] = pubDataGroups;
		return true;
	}

	/**
	 * 根据报表PK，得到所有指标数据的aloneID
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
	 * 根据当前用户，取得当前登录单位ID
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
	 * 删除报表动态区数据的预处理过程。
	 * 创建日期：(2003-8-20 10:23:40)
	 */
	private boolean preProcess1(String repPK, String dynKeyCombPK, Object[] retValue) throws Exception {
		//得到报表缓存
		ReportCache rc = IUFOBSCacheManager.getSingleton().getReportCache();
		MeasureCache mc = IUFOBSCacheManager.getSingleton().getMeasureCache();
		//将加载出的指标公共数据按报表对应的主关键字组合分组，并将不属于本报表的指标公共数据滤除
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
		//是否有私有关键字，如果是删除私有关键字组合对应的动态区数据，要通过主表的关键字组合找到
		//所有的aloneId
		KeyVO[] keys = kg.getPrivatekey();
		String[] strKeyCombPKs = null;
		if (keys == null || keys.length == 0) {
			strKeyCombPKs = new String[] { mainKeyGroupPK };
		} else {
			strKeyCombPKs = new String[] { dynKeyCombPK };
		}
		//找到符合条件的动态区数据对应的数据主表记录
		MeasurePubDataBO pubDataBO = new MeasurePubDataBO();
		MeasurePubDataVO[] pubDatas = pubDataBO.loadPubDataByKeyCombPKs(strKeyCombPKs);
		if (pubDatas == null || pubDatas.length == 0) {
			return false;
		}

		//得到报表中的所有指标
		MeasureVO[] measures = BSUtil.findMeasuresByRepPKs(new String[] { repPK });
		if (measures == null || measures.length == 0) {
			return false;
		}
		Vector<MeasureVO> vec1 = new Vector<MeasureVO>();
		for (int i = 0; i < measures.length; i++) {
			//过滤只属于此动态区的指标，注意要过滤掉引用来的指标
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
	 * 删除一张报表的数据
	 * 本方法仅删除数据子表中的指标数据及私有关键字数据，不删除指标数据主表中的PubData


	 * 创建日期：(2003-8-1 9:15:13)
	 * @param repPK java.lang.String 报表主键
	 * @param pubData nc.vo.iufo.data.MeasurePubDataVO 确定删除条件，如关键字值，版本号等
	 * @param deleteRefMeasureDatas 是否删除引用的指标的数据
	 * @exception nc.pub.iufo.exception.UFOSrvException 异常说明。
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
				//取到只属于本表的指标
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
					//找到主表的aloneID
					if (mainKeyGroupPK.equals(pds[k].getKType())) {
						mainAloneID = aloneIds[k];
					}
				}
				dataDMO.deleteRepData(repPK, aloneIds, measures);
				//删除上报确认记录
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
	 * 删除一张报表中的某个动态区的数据
	 * 本方法仅删除数据子表中的指标数据及私有关键字数据，不删除指标数据主表中的PubData

	 * 创建日期：(2003-8-1 9:15:13)
	 * @param repPK java.lang.String 报表主键
	 * @exception nc.pub.iufo.exception.UFOSrvException 异常说明。
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
	 * 报表审核回写方法，根据报表编号，单位编码，日期进行报表审核
	 * 创建日期：(2009-8-1 9:15:13)
	 * @param 
	 * @exception 异常说明。
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
