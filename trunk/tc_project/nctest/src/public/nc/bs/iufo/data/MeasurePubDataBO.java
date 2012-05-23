package nc.bs.iufo.data;

import java.util.Hashtable;
import java.util.Vector;

import nc.bs.iufo.cache.IUFOBSCacheManager;
import nc.itf.iufo.data.IMeasurePubDataSrv;
import nc.pub.iufo.cache.KeyGroupCache;
import nc.pub.iufo.cache.ReportCache;
import nc.pub.iufo.exception.UFOSrvException;
import nc.vo.iufo.data.MeasurePubDataQryVO;
import nc.vo.iufo.data.MeasurePubDataVO;
import nc.vo.iufo.keydef.KeyGroupVO;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.iufo.pub.DataManageObjectIufo;
import nc.vo.iuforeport.rep.ReportVO;

import com.ufida.iufo.pub.tools.AppDebug;


public class MeasurePubDataBO implements IMeasurePubDataSrv {
	
	public String createMeasurePubData(MeasurePubDataVO measurePubData) throws UFOSrvException {
		try {
			MeasurePubDataDMO dmo = new MeasurePubDataDMO(DataManageObjectIufo.IUFO_DATASOURCE);
			return dmo.createMeasurePubData(measurePubData);
		}
		catch (Throwable e){
			AppDebug.debug(e);
			throw new UFOSrvException("MeasurePubDataBO::createMeasurePubData Exception!" + e.getMessage());
		}
	}

	public MeasurePubDataVO[] createMeasurePubDatas(MeasurePubDataVO[] measurePubDatas) throws UFOSrvException {
		try {
			MeasurePubDataDMO dmo = new MeasurePubDataDMO(DataManageObjectIufo.IUFO_DATASOURCE);
			for (int i=0;i<measurePubDatas.length;i++){
				String aloneId = dmo.createMeasurePubData(measurePubDatas[i]);
				measurePubDatas[i].setAloneID(aloneId);
			}
			fillKeyGroupVO(measurePubDatas);
			return measurePubDatas;
		} catch (Throwable e) {
			AppDebug.debug(e);
			throw new UFOSrvException("MeasurePubDataBO::createMeasurePubDatas Exception!" + e.getMessage());
		}
	}
	
	/**
	 * 根据主表的关键字组合，主表的pubData条件，以及子表的关键字组合，创建一个适合于子表查询条件的pubData。
	 * 创建日期：(2003-10-13 13:18:41)
	 * @return nc.vo.iufo.data.MeasurePubDataVO
	 * @param mainPubData nc.vo.iufo.data.MeasurePubDataVO
	 * @param mainKeyCombPK java.lang.String
	 * @param subKeyCombPK java.lang.String
	 */
	private MeasurePubDataVO createSubPubData(MeasurePubDataVO mainPubData, String mainKeyCombPK, String subKeyCombPK) {
		if(mainKeyCombPK.equals(subKeyCombPK)){
			MeasurePubDataVO result = (MeasurePubDataVO)mainPubData.clone();
			result.setKType(mainKeyCombPK);
			return result;
		}
		KeyGroupCache kgc = IUFOBSCacheManager.getSingleton().getKeyGroupCache();
		KeyGroupVO kg1 = kgc.getByPK(mainKeyCombPK);
		KeyGroupVO kg2 = kgc.getByPK(subKeyCombPK);
	
		MeasurePubDataVO main = (MeasurePubDataVO)mainPubData.clone();
		main.setKType(mainKeyCombPK);
		main.setKeyGroup(kg1);
		MeasurePubDataVO result = (MeasurePubDataVO)mainPubData.clone();
		result.setKType(subKeyCombPK);
		result.setKeyGroup(kg2);
		result.setKeywords(new String[10]);
	
		for(int i=1; i<=10; i++){
			KeyVO[] keys = kg1.getKeys();
			String keyValue = main.getKeywordByIndex(i);
			if(keyValue != null && keyValue.trim().length()>0){
	            if(keys[i-1].getTimeKeyIndex() >= 0 && kg2.getTimeKey() != null){
	                result.setKeywordByName(kg2.getTimeKey().getName(), keyValue);
	            }else{
	                result.setKeywordByName(keys[i - 1].getName(), keyValue);
	            }
	
			}
		}
	
		return result;
	}

	/**
	 * 为指标公共数据填充关键字组合定义。
	 * 创建日期：(2003-8-1 14:56:45)
	 * @param pubData nc.vo.iufo.data.MeasurePubDataVO
	 */
	private void fillKeyGroupVO(MeasurePubDataVO[] pubDatas) {
	    if (pubDatas == null) {
	        return;
	    }
	    KeyGroupCache kgCache = IUFOBSCacheManager.getSingleton().getKeyGroupCache();
	    for (int i = 0; i < pubDatas.length; i++) {
		    if (pubDatas[i]==null)
		    	continue;
	        KeyGroupVO kg = kgCache.getByPK(pubDatas[i].getKType());
	        pubDatas[i].setKeyGroup(kg);
	    }
	}
	
	public MeasurePubDataVO findByAloneID(String sAloneID)
		throws UFOSrvException {
		MeasurePubDataVO[] pubDatas=findByAloneIDs(new String[]{sAloneID});
		if (pubDatas!=null && pubDatas.length>0)
			return pubDatas[0];
		return null;
	}

	public MeasurePubDataVO[] findByAloneIDs(String[] sAloneIDs)
		throws UFOSrvException {
		try {
			MeasurePubDataDMO dmo = new MeasurePubDataDMO(DataManageObjectIufo.IUFO_DATASOURCE);
			MeasurePubDataVO[] result = dmo.findByAloneIDs(sAloneIDs);
			fillKeyGroupVO(result);
			return result;
		} catch (Exception e) {
			AppDebug.debug(e);
			throw new UFOSrvException("MeasurePubDataBO::findByAloneID Exception!" + e.getMessage());
		}
	}

	public MeasurePubDataVO[] findByCondtion(MeasurePubDataVO pubData, String strUnitCode,boolean bIgnoreFormuaID,int iVer) throws nc.pub.iufo.exception.UFOSrvException {
	    try {
	        MeasurePubDataDMO dmo = new MeasurePubDataDMO(DataManageObjectIufo.IUFO_DATASOURCE);
	        MeasurePubDataVO[] pubDatas = dmo.findByCondition(pubData, strUnitCode,iVer, null,bIgnoreFormuaID).toArray(new MeasurePubDataVO[0]);
	        fillKeyGroupVO(pubDatas);
	        return pubDatas;
	    } catch (Exception e) {
	        AppDebug.debug(e);
	        throw new nc.pub.iufo.exception.UFOSrvException("MeasurePubDataBO:findCondition Exception");
	    }
	}
	
	public MeasurePubDataVO[] findByCondtionWithRepPK(
	    MeasurePubDataVO pubData,
	    String strUnitCode,
	    String repPK,
	    String strOrgPK)
	    throws nc.pub.iufo.exception.UFOSrvException {
	    try {
	        if (repPK == null) {
	            return findByCondtion(pubData, strUnitCode,false,0);
	        }
	        
	        ReportCache rc = IUFOBSCacheManager.getSingleton().getReportCache();
	        String[] keyCombPKs = rc.getKeyCombs(repPK);
	        ReportVO rep = rc.getByPks(new String[]{repPK})[0];
	        if(rep == null){
		        return new MeasurePubDataVO[0];
	        }
	        String mainPK = rep.getKeyCombPK();
	
	        MeasurePubDataDMO dmo =new MeasurePubDataDMO(DataManageObjectIufo.IUFO_DATASOURCE);
	
	        Vector<MeasurePubDataVO> vec = new Vector<MeasurePubDataVO>();
	        //根据报表的子表的关键字组合，创建不同的搜索条件的pubData，因为主表和子表的同样的关键字　
	        //可能出现在关键字组不同的位置上
			for(int i=0; keyCombPKs != null && i<keyCombPKs.length; i++){
				String subPK = keyCombPKs[i];
				MeasurePubDataVO tmpPubData = createSubPubData(pubData, mainPK, subPK);
				MeasurePubDataVO[] pubDatas=null;
				if(!shouldIncludeSubUnit(mainPK, subPK)){
					pubDatas =
						dmo.findByCondition(tmpPubData, strUnitCode, false, new String[]{mainPK, subPK},strOrgPK).toArray(
							new MeasurePubDataVO[0]);
				}else{
					pubDatas =
						dmo.findByCondition(tmpPubData, strUnitCode, true, new String[]{mainPK, subPK},strOrgPK).toArray(
							new MeasurePubDataVO[0]);
				}
		        if (pubDatas != null) {
		            fillKeyGroupVO(pubDatas);
		            vec.addAll(java.util.Arrays.asList(pubDatas));
		        }
			}
			return vec.toArray(new MeasurePubDataVO[0]);
	    } catch (Exception e) {
	        AppDebug.debug(e);
	        throw new nc.pub.iufo.exception.UFOSrvException("MeasurePubDataBO:findCondition Exception");
	    }
	}

	/**
	 * 如果主表的关键字组合中没有单位关键字，而子表的关键字组合中含有单位关键字，则在加载数据
	 * 时，要求按当前登录单位，过滤出只含有当前单位及其下级单位的子表数据。
	 * @param mainPK
	 * @param subPK
	 */
	private boolean shouldIncludeSubUnit(String mainPK, String subPK){
		KeyGroupCache kgc = IUFOBSCacheManager.getSingleton().getKeyGroupCache();
		KeyGroupVO main = kgc.getByPK(mainPK);
		KeyGroupVO sub = kgc.getByPK(subPK);
		if(main == null || sub == null){
			return false;
		}
		
		KeyVO[] mainKeys = main.getKeys();
		if(mainKeys != null){
			for(int i=0; i<mainKeys.length; i++){
				//发现在单位关键字，则直接返回false
				if(mainKeys[i] != null && KeyVO.CODE_TYPE_CORP.equals(mainKeys[i].getCode())){
					return false;	
				}
			}
		}
		KeyVO[] subKeys = sub.getKeys();
		if(subKeys != null){
			for(int i=0; i<subKeys.length; i++){
				//主表关键字没有单位关键字，而在子表关键字组合中发现了单位关键字，返回true
				if(subKeys[i] != null && KeyVO.CODE_TYPE_CORP.equals(subKeys[i].getCode())){
					return true;
				}
			}
		}
		return false;
	}

	public MeasurePubDataVO[] findByKeywordArray(MeasurePubDataVO[] pubDatas) throws nc.pub.iufo.exception.UFOSrvException{
		try{
			MeasurePubDataDMO dmo=new MeasurePubDataDMO(DataManageObjectIufo.IUFO_DATASOURCE);
			pubDatas=dmo.findByKeywordArray(pubDatas);
			fillKeyGroupVO(pubDatas);
			return pubDatas;
		}catch(Exception e){
			AppDebug.debug(e);
			throw new UFOSrvException("MeasurePubDataBO:findByKeywordArray Exception" + e.getMessage());
		}
	}
	
	public MeasurePubDataVO findByKeywords(MeasurePubDataVO measurePubData)
		throws UFOSrvException {
		try {
			MeasurePubDataDMO dmo = new MeasurePubDataDMO(DataManageObjectIufo.IUFO_DATASOURCE);
			MeasurePubDataVO measurePubDataVO = dmo.findByKeywords(measurePubData);
			fillKeyGroupVO(new MeasurePubDataVO[]{measurePubDataVO});
			return measurePubDataVO;
		} catch (Exception e) {
			AppDebug.debug(e);
			throw new UFOSrvException("MeasurePubDataBO::findByKeywords Exception!" + e.getMessage());
		}
	}
	
	public MeasurePubDataVO[] findByTotalKeyWords(MeasurePubDataVO pubData) throws nc.pub.iufo.exception.UFOSrvException{
		try{
			MeasurePubDataDMO pubDMO=new MeasurePubDataDMO(DataManageObjectIufo.IUFO_DATASOURCE);
			Vector<MeasurePubDataVO> vPubData=pubDMO.findByTotalCondition(pubData);
			return vPubData.toArray(new MeasurePubDataVO[0]);
		}catch(Exception e){
			AppDebug.debug(e);//@devTools e.printStackTrace();
			throw new UFOSrvException("MeasurePubDataBO Exception:findByTotalKeyWords");
		}
	}

	public String getAloneID(MeasurePubDataVO measurePubDataVO)
	    throws nc.pub.iufo.exception.UFOSrvException {
	    try {
	        String sAloneID =null;
	
	        MeasurePubDataVO newMeasurePubDataVO = findByKeywords(measurePubDataVO);
	        if (newMeasurePubDataVO == null) {
	            sAloneID = createMeasurePubData(measurePubDataVO);
	        } else {
	            sAloneID = newMeasurePubDataVO.getAloneID();
	        }
	
	        return sAloneID;
	    } catch (Exception e) {
	        AppDebug.debug(e);
	        throw new UFOSrvException(
	            "MeasurePubDataBO::getAloneID Exception!" + e.getMessage());
	    }
	}

	public MeasurePubDataVO[] loadPubDataByKeyCombPKs(String[] dynKeyCombPKs)
	    throws nc.pub.iufo.exception.UFOSrvException {
	    if (dynKeyCombPKs == null || dynKeyCombPKs.length == 0) {
	        return null;
	    }
	    try {
	        MeasurePubDataDMO pubDataDMO =new MeasurePubDataDMO(DataManageObjectIufo.IUFO_DATASOURCE);
	        Vector<MeasurePubDataVO> vec = pubDataDMO.loadPubDataByKeyCombPKs(dynKeyCombPKs);
	        MeasurePubDataVO[] results =vec.toArray(new MeasurePubDataVO[0]);
	        fillKeyGroupVO(results);
	        return results;
	    } catch (Exception e) {
	AppDebug.debug(e);//@devTools         e.printStackTrace();
	        throw new nc.pub.iufo.exception.UFOSrvException(e.getMessage(), e);
	    }
	}

	public void removeByIds(String[] strAloneIds) throws UFOSrvException {
	    try {
	        if (strAloneIds != null && strAloneIds.length > 0) {
	            MeasurePubDataDMO dmo =new MeasurePubDataDMO(DataManageObjectIufo.IUFO_DATASOURCE);
	            dmo.removeMeasurePubDatas(strAloneIds);
	        }
	    } catch (Exception e) {
	        AppDebug.debug(e);
	        throw new UFOSrvException("MeasurePubDataBO::removeByIds Exception!" + e.getMessage());
	    }
	}

	public Hashtable loadInputRepAloneIDs(MeasurePubDataQryVO qryData,String[] strRepIDs) throws UFOSrvException{
		try{
			MeasurePubDataDMO dmo=new MeasurePubDataDMO(DataManageObjectIufo.IUFO_DATASOURCE);
			return dmo.loadInputRepAloneIDs(qryData,strRepIDs);
		}
		catch(Exception e){
			AppDebug.debug(e);//@devTools e.printStackTrace();
			throw new UFOSrvException("MeasurePubDataBO loadInputRepAloneIDs Exception",e);
		}
	}
}
