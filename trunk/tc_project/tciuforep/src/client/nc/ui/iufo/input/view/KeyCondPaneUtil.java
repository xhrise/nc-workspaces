package nc.ui.iufo.input.view;

import nc.pub.iufo.cache.KeyGroupCache;
import nc.ui.hbbb.pub.HBBBSysParaUtil;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.iufo.input.control.RepDataControler;
import nc.ui.iufo.input.edit.RepDataEditor;
import nc.vo.iufo.data.MeasurePubDataVO;
import nc.vo.iufo.data.VerItem;
import nc.vo.iufo.keydef.KeyGroupVO;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.iufo.task.TaskDefaultVO;
import nc.vo.iufo.task.TaskVO;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.console.ActionHandler;
import com.ufida.zior.view.Mainboard;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.inputplugin.biz.file.ChangeKeywordsData;
import com.ufsoft.iufo.querycond.ui.QueryConditionUIUtil;
import com.ufsoft.iuforeport.tableinput.TableInputOperUtil;

public class KeyCondPaneUtil  {
	private static final long serialVersionUID = -7193631312951278653L;
	
	public static KeyCondPanel geneKeyCondPane(RepDataEditor editor){	
		MeasurePubDataVO m_pubData=editor.getPubData();
		String m_strTaskPK=editor.getTaskPK();
		String m_strRepPK=editor.getRepPK();
		Mainboard mainBoard=editor.getMainboard();
		
		boolean isGeneQuery=false;
		if("true".equals(editor.getContext().getAttribute(IUfoContextKey.GENRAL_QUERY))){
			isGeneQuery=true;
		}
		KeyVO[] keyVOs =  getKeyVOs(m_strTaskPK);
		//2,得到关键字的初始值
		String[] strKeyInitValues =null;
		try{
			strKeyInitValues=getKeyValues(editor,keyVOs,m_pubData,m_strTaskPK,RepDataControler.getInstance(mainBoard).getLoginEnv(mainBoard).getCurLoginDate());
		}catch(Exception e){
			AppDebug.debug(e);
		}
		//#得到切换关键字的界面显示需要的数据对象
		ChangeKeywordsData[] datas = geneChangeKeywordsDatas(keyVOs,strKeyInitValues,false,(String)editor.getContext().getAttribute(IUfoContextKey.ORG_PK));
		KeyCondPanel m_condPane=new KeyCondPanel(datas,isGeneQuery,m_strTaskPK,m_strRepPK,getVerInfo(m_pubData),getSubVerInfo(m_pubData));
		return m_condPane;
	}
	
	/**
	 * 根据当前任务得到关键字数组
	 * @param strTaskPK
	 * @return
	 */
	private static KeyVO[] getKeyVOs(String strTaskPK){
        TaskVO taskVO = IUFOUICacheManager.getSingleton().getTaskCache().getTaskVO(strTaskPK);
        KeyVO[] keyVOs = null;
        if(taskVO != null){
            String strKeyGroupID = taskVO.getKeyGroupId();
            KeyGroupCache keyGroupCache = IUFOUICacheManager.getSingleton().getKeyGroupCache();
            KeyGroupVO keyGroupVO = keyGroupCache.getByPK(strKeyGroupID);
            if(keyGroupVO != null){
                keyVOs = keyGroupVO.getKeys();
            }
        }
        return keyVOs;
    }
	
	public static VerItem getVerInfo(MeasurePubDataVO pubData){
		VerItem ver=null;
		if(pubData!=null&&pubData.getAloneID()!=null){
			if(TableInputOperUtil.isValidVer(pubData.getVer(),HBBBSysParaUtil.VER_SEPARATE)){
				ver=new VerItem(""+HBBBSysParaUtil.VER_SEPARATE,null);
			}
			if(TableInputOperUtil.isValidVer(pubData.getVer(),HBBBSysParaUtil.VER_HBBB)){
				ver=new VerItem(""+HBBBSysParaUtil.VER_HBBB,null);
			}
			if(TableInputOperUtil.isValidVer(pubData.getVer(),350)){
				ver=new VerItem("350@"+pubData.getFormulaID(),null);
			}
		}
		return ver;
	}
	public static VerItem getSubVerInfo(MeasurePubDataVO pubData){
        VerItem ver=null;
        if(pubData!=null&&pubData.getAloneID()!=null){
        	int iVer=pubData.getVer();
        	if (iVer>=1000)
        		iVer = iVer - 10 * ((int) (iVer / 10))+1;
        	else
        		iVer=0;
        	ver=new VerItem(iVer+"@"+pubData.getAloneID(),null);
        }
		return ver;
	}
	
	public static int getSWVer(String strVerPK) {
		int iVer = 0;
		if (strVerPK != null && strVerPK.length() > 0) {
			try {
				int iPos = strVerPK.indexOf("@");
				String strVer = strVerPK.substring(0, iPos);
				iVer = Integer.parseInt(strVer);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}

		return iVer;
	}
    /**
	 * 根据<code>KeyVO</code>及传入参数得到关键字值数组
	 * 
	 * @param keys
	 * @param tableInputTransObj
	 * @return
	 * @throws Exception
	 */
    private static String[] getKeyValues(RepDataEditor editor,KeyVO[] keys,MeasurePubDataVO pubData,String strTaskPK,String strLoginDate) throws Exception{
        if(keys == null || keys.length <=0){
        	return new String[0];
        }
        
        Mainboard mainBoard=editor.getMainboard();
        RepDataControler controler=RepDataControler.getInstance(mainBoard);
        
        //得到关键字的初始值
        String[] strKeyValues = pubData.getKeywords();
        int nKeyCount = keys.length;
        
        //得到任务的缺省值
        TaskDefaultVO taskDefault = IUFOUICacheManager.getSingleton().getTaskCache().getTaskDefaultVO(strTaskPK);
        for(int i =0;i < nKeyCount;i++){
            String strValue = strKeyValues[i];
            if(strValue == null){
                if(keys[i].getKeywordPK().equals(KeyVO.CORP_PK)){ //单位
                    strValue= controler.getCurUserInfo(mainBoard).getUnitId();
                } else if(keys[i].getKeywordPK().equals(KeyVO.DIC_CORP_PK)){//对方单位                    
                    strValue= controler.getCurUserInfo(mainBoard).getUnitId();
                } else if(keys[i].getType() == KeyVO.TYPE_TIME){//时间              
                    strValue=QueryConditionUIUtil.getCurLoginTTimeValue(strLoginDate,strTaskPK);
                } else if(keys[i].getType() == KeyVO.TYPE_ACC){// add by wangyga 2008-6-20 添加会计期间关键字默认值
                	strValue = QueryConditionUIUtil.getCurLoginTTimeValue(strLoginDate,strTaskPK);
                }
            }
            if (strValue==null && taskDefault!=null){
                strValue=taskDefault.getKeywordValueByIndex(i+1);
            }
            strKeyValues[i] = strValue;
        }
        return strKeyValues;
    }
    
	private static ChangeKeywordsData[] geneChangeKeywordsDatas(KeyVO[] keyVOs, String[] strKeyValues,boolean bHBBBData,String strOrgPK) {
		ChangeKeywordsData[] datas=(ChangeKeywordsData[])ActionHandler.exec("nc.ui.iufo.input.RepDataActionHandler", "geneChangeKeywordsDatas",
				new Object[]{keyVOs, strKeyValues, bHBBBData,strOrgPK});
		return datas;
    }
}
