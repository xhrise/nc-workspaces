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
		//û����Ч��ָ��IDֱ�ӷ���
		if(strAloneIDs == null || strAloneIDs.length == 0)
			return null;
	
		boolean bByTask=false;
		try{
			//ȡ��ָ������ID������ָ��
			//�ж��Ƿ��ϱ�ȷ��
			ReportCommitDMO commitDMO = new ReportCommitDMO(DataManageObjectIufo.IUFO_DATASOURCE);
			CheckResultDMO checkResultDMO=new CheckResultDMO(DataManageObjectIufo.IUFO_DATASOURCE);
			
	        //�ж��Ƿ��Ǹ���λ��������Ա
	        boolean bRoleAdmin=false;
	        if (com.ufsoft.iufo.util.userrole.UserRoleUtil.isRoleAdministrator(userInfo))
	            bRoleAdmin=true;
	        
	        //��������Ա�Ƿ����ɾ���Ѿ��ϱ�������
	        SysPropVO propVO = SysPropMng.getSysProp(ISysProp.CAN_MODIFY_DATA);  
	        
	        TaskCache taskCache=IUFOBSCacheManager.getSingleton().getTaskCache();
	        TaskVO task=taskCache.getTaskVO(strTaskID);
	        String[] strTaskRepIDs=taskCache.getReportIdsByTaskId(strTaskID);
	        if (strTaskRepIDs==null || strTaskRepIDs.length<=0)
	        	return null;
	        
	        //������Ǹ���λ����Ա����Ҫ������Ȩ�޿���
	        //ÿ�ű�����޸�Ȩ�޵ĵ�λ��Χ
	        Hashtable<String,RepRightUnitCollection> hashRepRightUnitCollect=new Hashtable<String,RepRightUnitCollection>();
	        //�����������б����޸�Ȩ�޵ĵ�λ��Χ
	        RepRightUnitCollection taskRightUnitCollect=null;
	        
	        //���ظ�������ʾ�õĲ���ɾ���ı������ݣ������֣����ϱ��ġ�û��ɾ��Ȩ�޵�
	        Vector<String> vCommitedID=new Vector<String>();
	        Vector<String> vNoRightID=new Vector<String>();
	
			for(int i=0; i<strAloneIDs.length; i++){
				//ȡ��һ������ID��Ӧ��Ҫ������ݵ�AloneIds
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
	
	            //����ǳ�������Ա���ҿ����޸��ϱ��������ݣ������ж��Ƿ��ϱ����ж�����Ȩ��
	            if (!bRoleAdmin || !propVO.getValue().equals("true")){
	                //�ж��Ƿ��Ѿ��ϱ�ȷ��
	                int[] commitFlag = commitDMO.loadRptCommitFlag(strAloneIDs[i],strRepIDs);
	
	                //�ҵ�����δ�ϱ�ȷ�ϵ�aloneId
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
	
				//��¼ɾ����aloneid�����ݣ�����ɾ���ϱ���¼ 
				Vector<String> vDelRepID=new Vector<String>();
				for (int j=0;j<strRepIDs.length;j++){
					//��������Ȩ���ж�
					boolean bNeedDel=false;
					//����ǳ�������Ա��������û�е�λ�ؼ��֣�����������Ȩ���ж�
					if (bRoleAdmin || pubData.getUnitPK()==null || pubData.getUnitPK().trim().length()<=0)
						bNeedDel=true;
					else {
						//�ж����������б�����޸�Ȩ�޵ĵ�λ��Χ���Ƿ�����õ�λ
						if (taskRightUnitCollect==null)
							taskRightUnitCollect=RepDataRightUtil.loadAllTaskRepRightUnitCollection(userInfo, task, strOrgPK);
						
						bNeedDel=taskRightUnitCollect.isUnitInCollection(pubData.getUnitPK(),RepDataRightVO.RIGHT_TYPE_MODIFY);
						
						//�ٰ����������ж�Ȩ�޵�λ��Χ���Ƿ�����õ�λ
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
	 * ��ǰ�˷��ز�����ȡ��һ�ű����Ӧ��Ҫ��յ����е�aloneId
	 * @param params,����aloneid+@+repid+@+ver�Ĳ���
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
	 * idStr��ȡ�������������ֱ���aloneId, ����ID, �汾��ver��
	 * ��������'@'�ָ���ȡ���� ��������������һ�������з���
	 * @param idStr
	 * @return
	 */
	private String[] extractIDs(String idStr) {
		//�������Ƿ���Ч
		if (idStr == null)
			return null;

		String[] strIDs=idStr.split("@");
		String[] retIDs = new String[3];		
		System.arraycopy(strIDs, 0, retIDs, 0,Math.min(retIDs.length,strIDs.length));
		return retIDs;
	}
	
	/**
	 * ��ǰ�˷��ز�����ȡ�����б���ID
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
