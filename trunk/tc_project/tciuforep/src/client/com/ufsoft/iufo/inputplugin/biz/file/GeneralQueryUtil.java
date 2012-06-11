package com.ufsoft.iufo.inputplugin.biz.file;

import nc.ui.hbbb.pub.HBBBSysParaUtil;
import nc.ui.iufo.unit.UnitMngBO_Client;
import nc.vo.iufo.unit.UnitInfoVO;

import com.ufida.dataset.IContext;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.inputplugin.biz.InputFilePlugIn;
import com.ufsoft.iufo.inputplugin.biz.RepSelectionPlugIn;
import com.ufsoft.iuforeport.tableinput.applet.TableInputContextVO;
import com.ufsoft.iuforeport.tableinput.applet.TableInputTransObj;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.plugin.IExtension;

/**
 * �����๦��˵����
 * Creation date: 2007-10-24
 * @author chxw
 */
public class GeneralQueryUtil implements IUfoContextKey{
	/**
	 * �Ƿ񱨱����ݲ鿴�������򿪷�ʽ��Ϊ�������ݲ鿴���ۺϲ�ѯ
	 * @return
	 */
	public static boolean isGeneralQuery(IContext context){
		boolean isgenralQuery=false;
		
		if (context!=null){
			Object genralQueryObj = context.getAttribute(GENRAL_QUERY);
			isgenralQuery= genralQueryObj == null ? false : Boolean.parseBoolean(genralQueryObj.toString());
		}
	
		return isgenralQuery;
	}
	
	/**
     * ����Ƿ���ʾ������
     * @return
     */
    public static boolean isShowRepTree(UfoReport ufoReport){
    	TableInputContextVO inputContextVO = (TableInputContextVO)ufoReport.getContextVo();
    	Object showRepTreeObj = inputContextVO.getAttribute(SHOW_REP_TREE);
		boolean isShowRepTree = showRepTreeObj == null ? false : Boolean.parseBoolean(showRepTreeObj.toString());
		
    	return isShowRepTree;
    }
    
    /**
     * �򿪱����Ƿ��Ǳ�������
     * @return
     */
    public static boolean isRepData(UfoReport ufoReport){
    	TableInputContextVO inputContextVO = (TableInputContextVO)ufoReport.getContextVo();
    	Object tableInputTransObj = inputContextVO.getAttribute(TABLE_INPUT_TRANS_OBJ);
		TableInputTransObj inputTransObj = tableInputTransObj != null &&(tableInputTransObj instanceof TableInputTransObj) ? (TableInputTransObj)tableInputTransObj : null;
		
    	return TableInputTransObj.isReportData(inputTransObj);
    }
    
    /**
     * �������ݰ汾
     * @param ufoReport
     * @return
     */
    public static Integer getVer(IContext inputContextVO){
    	Object tableInputTransObj = inputContextVO.getAttribute(TABLE_INPUT_TRANS_OBJ);
		TableInputTransObj inputTransObj = tableInputTransObj != null &&(tableInputTransObj instanceof TableInputTransObj) ? (TableInputTransObj)tableInputTransObj : null;
		
    	if(TableInputTransObj.isReportData(inputTransObj)){
    		String strDataVer = inputTransObj.getRepDataParam().getVer();
    		if(strDataVer == null)
    			return null;
    		String[] strDataVers = strDataVer.split("@");
    		int iDataVer = Integer.parseInt(strDataVers[0]);
    		return new Integer(iDataVer); 
    	}
    	return null;
    }
    
    /**
     * �Ƿ�ԭ�����ݰ汾
     * @param ufoReport
     * @return
     */
    public static boolean isGeneralDataVer(UfoReport ufoReport){
    	Integer iVer = getVer(ufoReport.getContextVo());
    	if(iVer != null && iVer.intValue() == HBBBSysParaUtil.VER_SEPARATE){
    		return true;
    	}
    	return false;
    }
    
    /**
     * �Ƿ���λ�汾
     * @param ufoReport
     * @return
     */
    public static boolean isSWDataVer(UfoReport ufoReport){
    	Integer iVer = getVer((TableInputContextVO)ufoReport.getContextVo());
    	if(iVer != null && iVer.intValue() > 999){
    		return true;
    	}
    	return false;
    }
    
    /**
     * �Ƿ���ܰ汾
     * @param ufoReport
     * @return
     */
    public static boolean isTotalDataVer(IContext context){
    	Integer iVer = getVer(context);
    	if(iVer != null && iVer.intValue() == 350){
    		return true;
    	}
    	return false;
    }
    
    /**
     * ���ؼ���¼��ֵ(¼���Ƿ�Ϊ�գ����ؼ����Ƿ�Ϸ�ͨ����̨���)
     * @param keys
     * @return
     */
    public static boolean checkKeyInput(String[] keys){
    	if(keys == null || keys.length == 0){
    		return true;
    	}
    	
    	for(String key:keys){
    		if(key == null || key.trim().length() == 0){
    			return false;
    		}
    	}
    	return true;
    }
    
    /**
     * ���ĵ�λ������
     * @param ufoReport
     * @return ChooseRepPanel
     */
    public static ChooseRepPanel getChooseRepPanel(UfoReport ufoReport){
    	ChooseRepPanel chooseRepPanel = null;
    	RepSelectionPlugIn selectionPlugIn = (RepSelectionPlugIn)
    		ufoReport.getPluginManager().getPlugin(RepSelectionPlugIn.class.getName());
    	
    	if(selectionPlugIn != null){
    		ChooseRepExt2  chooseRepExt2  = (ChooseRepExt2)(selectionPlugIn.getDescriptor().getExtensions()[0]);
    		chooseRepPanel = chooseRepExt2.getChooseRepNavPanel();
    	}
    	return chooseRepPanel;
    }

    /**
     * �����ۺϲ�ѯ�������
     * @param ufoReport
     * @return ChooseCordPanel
     */
    public static ChooseCordPanel getChooseCordPanel(UfoReport ufoReport){
    	ChooseCordPanel chooseCordPanel = null;
    	RepSelectionPlugIn selectionPlugIn = (RepSelectionPlugIn)
    		ufoReport.getPluginManager().getPlugin(RepSelectionPlugIn.class.getName());

    	if(selectionPlugIn != null){
    		ChooseCordExt chooseCordExt = (ChooseCordExt)(selectionPlugIn.getDescriptor().getExtensions()[1]);
    		chooseCordPanel = (ChooseCordPanel)chooseCordExt.getPanel();	
    	}
    	
    	return chooseCordPanel;

    }
    
    /**
     * ���ݵ�λPK���ص�λ��Ϣ
	 * @param strOperUnitPK
	 */
    public static UnitInfoVO findUnitInfoByPK(String strOperUnitPK) {
    	try {
    		UnitInfoVO[] unitInfos = UnitMngBO_Client
    			.loadUnitInfoByIds(new String[] { strOperUnitPK });
    		return unitInfos != null ? unitInfos[0] : null;
    	} catch (Exception e) {
    		AppDebug.debug(e);
    	}
    	return null;
    }
    
    /**
     * ���ݵ�λPK���ص�λ��Ϣ
	 * @param strOperUnitPK
	 */
    public static UnitInfoVO findUnitInfoByCode(String strOperUnitCode) {
    	try {
    		UnitInfoVO unitInfos = UnitMngBO_Client
    			.findUnitByCode(strOperUnitCode);
    		return unitInfos;
    	} catch (Exception e) {
    		AppDebug.debug(e);
    	}
    	return null;
    }
    
    /**
     * �����Ƿ����ϱ�
     * @param ufoReport
     */
    public static boolean isCommit(UfoReport ufoReport) {
    	InputFilePlugIn plugIn = (InputFilePlugIn)ufoReport.getPluginManager().getPlugin(InputFilePlugIn.class.getName());
    	if(plugIn != null){
    		IExtension[] exts = plugIn.getDescriptor().getExtensions();
    		SaveRepDataExt saveRepDataExt = (SaveRepDataExt)exts[0];
    		return !saveRepDataExt.isEnabledSelf();
    	}
    	return false;

    }
    
}
