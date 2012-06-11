package nc.ui.iufo.input.control;

import nc.ui.iufo.cache.IUFOUICacheManager;

import com.ufida.dataset.IContext;
import com.ufida.iufo.pub.AppWorker;
import com.ufida.zior.view.Mainboard;
import com.ufida.zior.view.event.MainboardListener;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.inputplugin.querynavigation.FormulaTraceNavigation;

/**
 * Ϊ�˱������ݣ���ע���Applet�򿪹رյļ�����
 * @author weixl
 *
 */
public class RepDataMainboardListner implements MainboardListener, IUfoContextKey{

	public void shutdown(Mainboard mainboard) {
		//��RepDataControler��DataSourceConfig����ʵ�����
		RepDataControler.uninitializeRepDataControler(mainboard);
		DataSourceConfig.uninitializeDataSourceConfig(mainboard);
		FormulaTraceNavigation.uninitializeNavigation(mainboard);
	}

	public void startup(Mainboard mainboard) {
		//wangyga +  �ж��Ƿ�ǰ͸��ͼ
		IContext context = mainboard.getContext();
		String strPerspectiveId = context.getAttribute(PERSPECTIVE_ID)+"";
		if(strPerspectiveId == null || strPerspectiveId.trim().length() == 0 ||
				!strPerspectiveId.equalsIgnoreCase(PERS_DATA_INPUT)){
			return;
		}		
		
		//����RepDataControler��DataSourceConfig�ĵ�ʵ��
		DataSourceConfig.initDataSourceConfig(mainboard);
		new AppWorker("IufoCache init..."){
			@Override
			protected Object construct() throws Exception {
				IUFOUICacheManager.getSingleton();
				return null;
			}
			
		}.start(500);
		RepDataControler.initRepDataControler(mainboard);
		
		

	}

}
