package com.ufsoft.iufo.fmtplugin;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.ui.iufo.datasource.DataSourceBO_Client;
import nc.ui.pub.ClientEnvironment;
import nc.vo.bd.CorpVO;
import nc.vo.iufo.datasource.DataSourceVO;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.pub.dsmanager.IQEContextKey;
import nc.vo.pub.querymodel.ModelUtil;
import nc.vo.sm.UserVO;
import nc.vo.sm.config.Account;

import com.ufida.dataset.Context;
import com.ufida.dataset.IContext;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.formatcore.FreeReportContextKey;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.key.KeywordModel;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iuforeport.freequery.FreeQueryContextVO;
import com.ufsoft.iuforeport.tableinput.applet.DataSourceInfo;
import com.ufsoft.report.UfoReport;
import com.ufsoft.script.spreadsheet.UfoCalcEnv;



/**
 * ΪǨ��������
 * 
 * �ṩ�˹�����
 * @author liuyy
 *
 */
public class ContextFactory implements IUfoContextKey,IQEContextKey {

	private ContextFactory(){
		
	}
	
	public Context createContext(UfoCalcEnv env){
		Context context = new Context();
		//TODO
		return context;
	}

	/**
	 * @i18n miufohbbb00132=NC����Դ��Ч,����Ĭ��NC����Դ:
	 */
	public static void initNCContext(Context context){
		if(context == null)
			return;
		ClientEnvironment ce = ClientEnvironment.getInstance();
		DataSourceVO dsVO = null;
		if(context.getAttribute(IUfoContextKey.DATA_SOURCE)!=null &&
				(context.getAttribute(IUfoContextKey.DATA_SOURCE) instanceof DataSourceVO)){
			dsVO = (DataSourceVO)context.getAttribute(IUfoContextKey.DATA_SOURCE);
		}
		String dsn = ModelUtil.getDefaultDsname();
		String unitId = "";
		String loginName = "";
		try {
			if(dsVO == null || dsVO.getType()!=DataSourceVO.TYPENC2){
				AppDebug.debug(StringResource.getStringResource("miufohbbb00132")+(dsn==null?"":dsn));
			}else{
				dsn = dsVO.getAddr();
				unitId = dsVO.getUnitId();
				loginName = dsVO.getLoginName();
				InvocationInfoProxy.getInstance().setDefaultDataSource(dsn);
			}
			
			// ������Ϣ 
			ce.setConfigAccount(new Account());
			ce.getConfigAccount().setAccountCode("");
			ce.getConfigAccount().setDataSourceName(dsn);
			// ��˾��Ϣ
			ce.setCorporation(new CorpVO());
			ce.getCorporation().setPrimaryKey(unitId);
			// �û���Ϣ
			ce.setUser(new UserVO());
			ce.getUser().setUserCode(loginName);
		} catch (Exception e) {
			AppDebug.debug(e.getMessage());
		}
	}
	
	public static Context createContext(UfoReport report){
		Context context = null;
		
		if (report.getContextVo() instanceof Context) {
			context = new Context(report.getContextVo());
			KeywordModel kModel = KeywordModel.getInstance(report.getCellsModel());
			KeyVO[] mKeys = kModel.getMainKeyVOs();
			if (mKeys != null) {
				context.setAttribute(IUfoContextKey.MAIN_REP_KEY_VOS, mKeys);
			}
		}
		
		if (report.getContextVo() instanceof FreeQueryContextVO) {
			FreeQueryContextVO ucvo = (FreeQueryContextVO) report.getContextVo();
			context = new Context(ucvo);
			Object datasourceObj =ucvo.getAttribute(FreeReportContextKey.DATA_SOURCEINFO);
			if (datasourceObj instanceof DataSourceInfo) {//��ǰ����Դ
				DataSourceInfo dsInfo = (DataSourceInfo)datasourceObj;
				try {
					String dataSourceID = dsInfo.getDSID();
					DataSourceVO dataSourceVO = DataSourceBO_Client.loadDataSByID(dataSourceID);
					if (dataSourceVO != null) {
						dataSourceVO.setLoginUnit(dsInfo.getDSUnitPK());
						dataSourceVO.setLoginName(dsInfo.getDSUserPK());
						dataSourceVO.setUnitId(dsInfo.getDSUnitPK());
						String dsPassword = nc.bs.iufo.toolkit.Encrypt.decode(dsInfo.getDSPwd(), dataSourceID);
						dataSourceVO.setLoginPassw(dsPassword);
						dataSourceVO.setLoginDate(dsInfo.getDSDate());
						context.setAttribute(DATA_SOURCE, dataSourceVO);
					}
				} catch (Exception e) {
					AppDebug.debug(e);
				}
			}
		}
		
		//@add by:yza :-> ����NC�������Ϣ
		addReplaceParam2Context(context);
		return context;
	}
	
	/**��Context�м���NC�����
	 * @author yaoza
	 */
	public static void addReplaceParam2Context(IContext context){
		if(context == null || context.getAttribute(DATA_SOURCE) == null)
			return;
		try{
			DataSourceVO dsVO = (DataSourceVO)context.getAttribute(DATA_SOURCE);
			context.setAttribute(LOGIN_ACCOUNT_ENV,dsVO.getAccount()); //��¼����
			context.setAttribute(LOGIN_CORP_ENV, dsVO.getLoginUnit());		//��¼��˾
			context.setAttribute(LOGIN_USER_ENV, dsVO.getLoginName());	//��¼�û�
			context.setAttribute(LOGIN_DATE_ENV, dsVO.getLoginDate());		//��¼����
			//���ڴ���
			if (dsVO.getLoginDate() != null) {
				String date = dsVO.getLoginDate();
				context.setAttribute(LOGIN_YEARMONTH_ENV,date.substring(0,date.lastIndexOf('-'))); //��¼����
				context.setAttribute(LOGIN_YEAR_ENV, date.substring(0,4)); //��¼��
				context.setAttribute(LOGIN_MONTH_ENV, date.substring(date.indexOf('-')+1,date.lastIndexOf('-'))); //��¼��
			} else {
				context.setAttribute(LOGIN_YEARMONTH_ENV, "");
				context.setAttribute(LOGIN_YEAR_ENV, "");
				context.setAttribute(LOGIN_MONTH_ENV, "");	
			}
		}catch(Exception ex){
			AppDebug.debug(ex);
		}
	}
}
