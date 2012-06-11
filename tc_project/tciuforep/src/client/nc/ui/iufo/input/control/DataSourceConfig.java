package nc.ui.iufo.input.control;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import nc.ui.iufo.datasource.DataSourceBO_Client;
import nc.vo.iufo.datasource.DataSourceLoginVO;
import nc.vo.iufo.datasource.DataSourceVO;
import nc.vo.iuforeport.applet.IDataSourceParam;

import com.ufida.dataset.IContext;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.console.ActionHandler;
import com.ufida.zior.view.Mainboard;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;

/**
 * ����Դ��Ϣ���ù������
 * @author weixl
 *
 */
public class DataSourceConfig implements Serializable,Cloneable {
	private static final long serialVersionUID = 1939289619682444011L;
	
	private static final String DATA_SOURCE_CONFIG_KEY="###data_source_config_key###";
	
	//��Applet�����д������ĳ�ʼ����Դ������Ϣ
	transient private DataSourceLoginVO m_initLogin=new DataSourceLoginVO();
	
	//�û����õ�����Դ������Ϣ
	transient private Map<String,DataSourceLoginVO> m_hashLogin=new HashMap<String,DataSourceLoginVO>();
	
	public Object clone(){
		return this;
	}
	
	static void initDataSourceConfig(Mainboard mainBoard){
		//���ɵ�ʵ�������õ���ʼ����Դ������Ϣ
		DataSourceConfig config=new DataSourceConfig();
		config.m_initLogin.setDSUnit((String)mainBoard.getContext().getAttribute(IDataSourceParam.DS_UNIT));
		config.m_initLogin.setDSUser((String)mainBoard.getContext().getAttribute(IDataSourceParam.DS_USER));
		
		String strDSID=(String)mainBoard.getContext().getAttribute(IUfoContextKey.DATA_SOURCE_ID);
		String strDSPass=(String)mainBoard.getContext().getAttribute(IDataSourceParam.DS_PASSWORD);
		if (strDSPass!=null && strDSPass.trim().length()>0)
			strDSPass = nc.bs.iufo.toolkit.Encrypt.decode(strDSPass, strDSID);
		config.m_initLogin.setDSPass(strDSPass);
		
		try{
			//�������context�е�����ԴIDת��Ϊ����ԴVO
			DataSourceVO dataSource=DataSourceBO_Client.loadDataSByID(strDSID);
			if (dataSource!=null){
				dataSource.setLoginDate((String)mainBoard.getContext().getAttribute(IUfoContextKey.LOGIN_DATE));
				mainBoard.getContext().setAttribute(IUfoContextKey.DATA_SOURCE, dataSource);
			}
		}catch(Exception e){
			AppDebug.debug(e);
		}
		mainBoard.getContext().setAttribute(DATA_SOURCE_CONFIG_KEY,config);
	}
	
	static void uninitializeDataSourceConfig(Mainboard mainBoard){
		mainBoard.getContext().removeAttribute(DATA_SOURCE_CONFIG_KEY);
	}
	
	private DataSourceConfig(){
	}
	
	public static DataSourceConfig getInstance(Mainboard mainboard){
		return (DataSourceConfig)mainboard.getContext().getAttribute(DATA_SOURCE_CONFIG_KEY);
	}
	
	/**
	 * ����һ����λ������Դ������Ϣ
	 * @param strIUFOUnitPK
	 * @param strDSUnit
	 * @param strDSUser
	 * @param strDSPass
	 */
	public void addOneSourceConfig(String strIUFOUnitPK,String strDSUnit,String strDSUser,String strDSPass){
		m_hashLogin.put(strIUFOUnitPK, new DataSourceLoginVO(strDSUnit,strDSUser,strDSPass));
		
		//��¼���һ�����õ�����Դ������Ϣ
		m_initLogin.setDSUnit(strDSUnit);
		m_initLogin.setDSUser(strDSUser);
		m_initLogin.setDSPass(strDSPass);
	}
	
	/**
	 * ȡ��һ����λ������Դ������Ϣ
	 * @param strIUFOUnitPK
	 * @return
	 */
	public DataSourceLoginVO getOneSourceConfig(String strIUFOUnitPK){
		//����ڹ�ϣ�����ҵ��õ�λ��������Ϣ����ֱ�Ӵӹ�ϣ����ȡ��
		DataSourceLoginVO login=m_hashLogin.get(strIUFOUnitPK);
		if (login!=null)
			return login;

		try{
			//����������Ϣ�������һ�����õ�������Ϣȡ��
			return (DataSourceLoginVO)ActionHandler.exec("nc.ui.iufo.input.RepDataActionHandler", "loadDataSourceLogin",
					new Object[]{m_initLogin,strIUFOUnitPK});			
		}catch(Exception e){
			AppDebug.debug(e);
		}
		return (DataSourceLoginVO)m_initLogin.clone();
	}
	
	public static void removeFromContext(IContext context){
		if (context!=null)
			context.removeAttribute(DATA_SOURCE_CONFIG_KEY);
	}
}
