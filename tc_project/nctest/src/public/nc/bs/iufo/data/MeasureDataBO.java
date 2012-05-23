package nc.bs.iufo.data;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import nc.bs.iufo.cache.IUFOBSCacheManager;
import nc.bs.iufo.measure.MeasureDMO;
import nc.bs.iufo.query.returnquery.ReportCommitDMO;
import nc.itf.iufo.data.IMeasureDataSrv;
import nc.pub.iufo.cache.MeasureCache;
import nc.pub.iufo.cache.ReportCache;
import nc.pub.iufo.cache.TaskCache;
import nc.pub.iufo.cache.base.ICacheObject;
import nc.pub.iufo.cache.base.IPackaged;
import nc.pub.iufo.exception.UFOSrvException;
import nc.ui.iufo.repdataright.RepDataRightUtil;
import nc.ui.iufo.repdataright.RepRightUnitCollection;
import nc.vo.iufo.data.MeasureDataVO;
import nc.vo.iufo.data.MeasurePubDataVO;
import nc.vo.iufo.measure.MeasurePackVO;
import nc.vo.iufo.measure.MeasureVO;
import nc.vo.iufo.pub.DataManageObjectIufo;
import nc.vo.iufo.repdataright.RepDataRightVO;
import nc.vo.iufo.task.TaskVO;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.check.bs.CheckResultDMO;
import com.ufsoft.iufo.sysprop.ui.ISysProp;
import com.ufsoft.iufo.sysprop.ui.SysPropMng;
import com.ufsoft.iufo.sysprop.vo.SysPropVO;

public class MeasureDataBO  implements IMeasureDataSrv {

	public MeasureDataBO() {
		super();
	}
	public void deleteAllRepData(MeasurePubDataVO[] pubDatas)throws nc.pub.iufo.exception.UFOSrvException {
		try{
			MeasureDMO measDMO=new MeasureDMO(DataManageObjectIufo.IUFO_DATASOURCE);
			MeasureDataDMO measDataDMO=new MeasureDataDMO(DataManageObjectIufo.IUFO_DATASOURCE);
			
			MeasureCache measCache=IUFOBSCacheManager.getSingleton().getMeasureCache();
			ReportCache repCache=IUFOBSCacheManager.getSingleton().getReportCache();
			
			if (pubDatas==null || pubDatas.length<=0)
				return;
			
			String[] strPackPKs=measDMO.loadMeasurePackPKByKeyCombPK(pubDatas[0].getKType());
			if (strPackPKs==null || strPackPKs.length<=0)
				return;
			
			IPackaged[] packages=measCache.getPackages(strPackPKs);
			if (packages==null || packages.length<=0)
				return;
			
			List<MeasureVO> vMeasure=new ArrayList<MeasureVO>();
			for (int i=0;i<packages.length;i++){
				MeasurePackVO measPack=(MeasurePackVO)packages[i];
				if (measPack==null || measPack.getAll()==null || measPack.getAll().length<=0)
					continue;
				
				ICacheObject[] measures=measPack.getAll();
				for (int j=0;j<measures.length;j++){
					MeasureVO measure=(MeasureVO)measures[j];
					if (measure.getReportPK()==null || measure.getReportPK().trim().length()<=0)
						continue;
					
					if (repCache.getByPK(measure.getReportPK())==null)
						continue;
					
					vMeasure.add(measure);
				}
			}
			
			if (vMeasure.size()<=0)
				return;
			
			String[] strAloneIDs=new String[pubDatas.length];
			for (int i=0;i<strAloneIDs.length;i++)
				strAloneIDs[i]=pubDatas[i].getAloneID();
			
			measDataDMO.deleteRepData(strAloneIDs, vMeasure.toArray(new MeasureVO[0]));
		}catch(Exception e){
			AppDebug.debug(e);
			throw new UFOSrvException("MeasureDataBO::deleteAllRepData Exception!" + e.getMessage());
		}
	}
		
	public void deleteRepData(String sAloneID, MeasureVO[] measure)
		throws nc.pub.iufo.exception.UFOSrvException {
		try {
			MeasureDataDMO dmo = new MeasureDataDMO(DataManageObjectIufo.IUFO_DATASOURCE);
			dmo.deleteRepData(new String[]{sAloneID},measure);
		} catch (Exception e) {
			AppDebug.debug(e);//@devTools e.printStackTrace();
			throw new UFOSrvException("MeasureDataBO::deleteRepData Exception!" + e.getMessage());
		}
	}

	public void editRepData(String sAloneID, MeasureDataVO[] measureDatas)
		throws nc.pub.iufo.exception.UFOSrvException {
		try {
			if (sAloneID==null || measureDatas==null || measureDatas.length<=0)
				return;
			
			MeasureDataDMO dmo = new MeasureDataDMO(DataManageObjectIufo.IUFO_DATASOURCE);
			dmo.editRepData(sAloneID,measureDatas);
		} catch (Exception e) {
			AppDebug.debug(e);
			throw new UFOSrvException("MeasureDataBO::editRepData Exception!" + e.getMessage());
		}
	}

	public Vector emptyRepDatas(String[] params,nc.vo.iufo.user.UserInfoVO userInfo,String strTaskID,String strOrgPK)
		throws nc.pub.iufo.exception.UFOSrvException{
		if(params == null || params.length ==0)
			return null;
	
		String[] strAloneIDs = extractAloneIds(params);
		//没有有效的指标ID直接返回
		if(strAloneIDs == null || strAloneIDs.length == 0)
			return null;
	
		boolean bByTask=false;
		try{
			//取得指定报表ID的所有指标
			//判断是否上报确认
			ReportCommitDMO commitDMO = new ReportCommitDMO(DataManageObjectIufo.IUFO_DATASOURCE);
			CheckResultDMO checkResultDMO=new CheckResultDMO(DataManageObjectIufo.IUFO_DATASOURCE);
			
	        //判断是否是根单位超级管理员
	        boolean bRoleAdmin=false;
	        if (com.ufsoft.iufo.util.userrole.UserRoleUtil.isRoleAdministrator(userInfo))
	            bRoleAdmin=true;
	        
	        //超级管理员是否可以删除已经上报的数据
	        SysPropVO propVO = SysPropMng.getSysProp(ISysProp.CAN_MODIFY_DATA);  
	        
	        TaskCache taskCache=IUFOBSCacheManager.getSingleton().getTaskCache();
	        TaskVO task=taskCache.getTaskVO(strTaskID);
	        String[] strTaskRepIDs=taskCache.getReportIdsByTaskId(strTaskID);
	        if (strTaskRepIDs==null || strTaskRepIDs.length<=0)
	        	return null;
	        
	        //如果不是根单位管理员，需要走数据权限控制
	        //每张表的有修改权限的单位范围
	        Hashtable<String,RepRightUnitCollection> hashRepRightUnitCollect=new Hashtable<String,RepRightUnitCollection>();
	        //对任务中所有表有修改权限的单位范围
	        RepRightUnitCollection taskRightUnitCollect=null;
	        
	        //返回给界面提示用的不能删除的报表数据，分两种：已上报的、没有删除权限的
	        Vector<String> vCommitedID=new Vector<String>();
	        Vector<String> vNoRightID=new Vector<String>();
	
			for(int i=0; i<strAloneIDs.length; i++){
				//取出一个报表ID对应的要清空数据的AloneIds
				String[] strRepIDs = extractRepIdsOfAloneID(params, strAloneIDs[i]);
				if(strRepIDs == null || strRepIDs.length == 0){
					continue;
				}
				
				if (strRepIDs[0].equals("repid")){
					bByTask=true;
					strRepIDs=checkResultDMO.loadRepIdsByAloneId(strAloneIDs[i]);
					if (strRepIDs==null || strRepIDs.length<=0)
						continue;
					
					Vector<String> vTaskRepID=new Vector<String>(Arrays.asList(strTaskRepIDs));
					Vector<String> vRepID=new Vector<String>(Arrays.asList(strRepIDs));
					for (int j=vTaskRepID.size()-1;j>=0;j--){
						if (!vRepID.contains(vTaskRepID.get(j))){
							vTaskRepID.remove(j);
						}
					}
					strRepIDs=vTaskRepID.toArray(new String[0]);
				}
	
	            //如果是超级管理员，且可以修改上报过的数据，则不用判断是否上报及判断数据权限
	            if (!bRoleAdmin || !propVO.getValue().equals("true")){
	                //判断是否已经上报确认
	                int[] commitFlag = commitDMO.loadRptCommitFlag(strAloneIDs[i],strRepIDs);
	
	                //找到所有未上报确认的aloneId
	                Vector<String> vec=new Vector<String>();
	                for(int j = 0; j < strRepIDs.length; j++){
	                    if(commitFlag[j] == nc.vo.iufo.query.returnquery.ReportCommitVO.NOTCOMMITED)
	                        vec.add(strRepIDs[j]);
	                    else
	                    	vCommitedID.add(strAloneIDs[i]+"@"+strRepIDs[j]);                    
	                }
	
	                strRepIDs=(String[])vec.toArray(new String[0]);
	            }
	            
	            if (strRepIDs.length<=0)
	            	continue;
	
				RepDataBO repDataBO=new RepDataBO();
				CheckResultDMO resultDMO = new CheckResultDMO(DataManageObjectIufo.IUFO_DATASOURCE);
				MeasurePubDataDMO pubDMO=new MeasurePubDataDMO(DataManageObjectIufo.IUFO_DATASOURCE);
				
				MeasurePubDataVO pubData=pubDMO.findByAloneID(strAloneIDs[i]);
				if (pubData==null)
					continue;
	
				//记录删除的aloneid的数据，用于删除上报记录 
				Vector<String> vDelRepID=new Vector<String>();
				for (int j=0;j<strRepIDs.length;j++){
					//进行数据权限判断
					boolean bNeedDel=false;
					//如果是超级管理员且任务中没有单位关键字，则不用走数据权限判断
					if (bRoleAdmin || pubData.getUnitPK()==null || pubData.getUnitPK().trim().length()<=0)
						bNeedDel=true;
					else {
						//判断任务中所有报表的修改权限的单位范围中是否包括该单位
						if (taskRightUnitCollect==null)
							taskRightUnitCollect=RepDataRightUtil.loadAllTaskRepRightUnitCollection(userInfo, task, strOrgPK);
						
						bNeedDel=taskRightUnitCollect.isUnitInCollection(pubData.getUnitPK(),RepDataRightVO.RIGHT_TYPE_MODIFY);
						
						//再按单个报表判断权限单位范围中是否包括该单位
						if (bNeedDel==false){
							RepRightUnitCollection repRightUnitCollect=hashRepRightUnitCollect.get(strRepIDs[j]);
							if (repRightUnitCollect==null){
								repRightUnitCollect=RepDataRightUtil.loadRepRightUnitCollection(userInfo, task, strRepIDs[j], strOrgPK);
								hashRepRightUnitCollect.put(strRepIDs[j],repRightUnitCollect);
							}
							
							bNeedDel=repRightUnitCollect.isUnitInCollection(pubData.getUnitPK(),RepDataRightVO.RIGHT_TYPE_MODIFY);
						}
					}

					if (bNeedDel){
						repDataBO.removeRepData(strRepIDs[j],userInfo.getID(), pubData, true,strOrgPK);
						resultDMO.removeByRepIdAloneId(strRepIDs[j],strAloneIDs[i]);
						vDelRepID.add(strRepIDs[j]);
					}
					else
						vNoRightID.add(strAloneIDs[i]+"@"+strRepIDs[j]);
				}
				if (vDelRepID.size()>0)
					commitDMO.removeCommit(new String[]{strAloneIDs[i]},vDelRepID.toArray(new String[0]));
			}
			
			Vector<Vector<String>> vRetID=new Vector<Vector<String>>();
			
			if (!bByTask){
				vRetID.add(vCommitedID);
				vRetID.add(vNoRightID);
			}else{
				vRetID.add(new Vector<String>());
				vRetID.add(new Vector<String>());
			}
			
			return vRetID;
		}catch(Exception e){
			AppDebug.debug(e);//@devTools e.printStackTrace();
			throw new nc.pub.iufo.exception.UFOSrvException("MeasureDataBO::emptyRepData Exception!" + e.getMessage());
		}
	}
	
	/**
	 * 从前端返回参数中取得一张报表对应的要清空的所有的aloneId
	 * @param params,形如aloneid+@+repid+@+ver的参数
	 * @param repId
	 * @return
	 */
	private String[] extractRepIdsOfAloneID(String[] params, String strAloneID) {
		if(params == null || strAloneID == null)
			return null;

		Set<String> setRepID=new HashSet<String>();
		for(int i=0; i<params.length; i++){
			String[] ids = extractIDs(params[i]);
			if(ids[1] != null && strAloneID.equals(ids[0])){
				setRepID.add(ids[1]);
			}
		}
		
		return setRepID.toArray(new String[0]);
	}
	
	/**
	 * idStr中取出三个参数，分别是aloneId, 报表ID, 版本号ver，
	 * 参数间以'@'分隔。取出后， 这三个参数放在一个数组中返回
	 * @param idStr
	 * @return
	 */
	private String[] extractIDs(String idStr) {
		//检查参数是否有效
		if (idStr == null)
			return null;

		String[] strIDs=idStr.split("@");
		String[] retIDs = new String[3];		
		System.arraycopy(strIDs, 0, retIDs, 0,Math.min(retIDs.length,strIDs.length));
		return retIDs;
	}
	
	/**
	 * 从前端返回参数中取出所有报表ID
	 * @param params
	 * @return
	 */
	private String[] extractAloneIds(String[] params) {
		if(params == null){
			return null;
		}
		Set<String> setAloneID=new HashSet<String>();
		String[] ids = null;
		for(int i=0; i<params.length; i++){
			ids = extractIDs(params[i]);
			if(ids[0] != null ){
				setAloneID.add(ids[0]);
			}
		}
		return setAloneID.toArray(new String[0]);
	}

	public MeasureDataVO[] getMeasureData(MeasureVO measure, MeasurePubDataVO[] vPubData) throws nc.pub.iufo.exception.UFOSrvException{
		try{
			MeasureDataDMO dmo=new MeasureDataDMO(DataManageObjectIufo.IUFO_DATASOURCE);
			return dmo.getMeasureData(measure,vPubData).toArray(new MeasureDataVO[0]);
		}
		catch(Exception e){
			AppDebug.debug(e);//@devTools e.printStackTrace();
			throw new UFOSrvException("MeasureDataBO:getMeasureData Exception" + e.getMessage());
		}
	}

	public MeasureDataVO[] getRepData(String sAloneID, MeasureVO[] measures) throws nc.pub.iufo.exception.UFOSrvException {
		try {
			MeasureDataDMO dmo = new MeasureDataDMO(DataManageObjectIufo.IUFO_DATASOURCE);
			return dmo.getRepData(new String[]{sAloneID},measures);
		} catch (Exception e) {
			AppDebug.debug(e);//@devTools e.printStackTrace();
			throw new UFOSrvException("MeasureDataBO::getRepData Exception!" + e.getMessage());
		}
	}
}
