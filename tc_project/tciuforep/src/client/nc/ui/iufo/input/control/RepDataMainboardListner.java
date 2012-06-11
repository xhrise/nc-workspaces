package nc.ui.iufo.input.control;

import nc.ui.iufo.cache.IUFOUICacheManager;

import com.ufida.dataset.IContext;
import com.ufida.iufo.pub.AppWorker;
import com.ufida.zior.view.Mainboard;
import com.ufida.zior.view.event.MainboardListener;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.inputplugin.querynavigation.FormulaTraceNavigation;

/**
 * 为了报表数据，而注册的Applet打开关闭的监听器
 * @author weixl
 *
 */
public class RepDataMainboardListner implements MainboardListener, IUfoContextKey{

	public void shutdown(Mainboard mainboard) {
		//将RepDataControler与DataSourceConfig两个实例清除
		RepDataControler.uninitializeRepDataControler(mainboard);
		DataSourceConfig.uninitializeDataSourceConfig(mainboard);
		FormulaTraceNavigation.uninitializeNavigation(mainboard);
	}

	public void startup(Mainboard mainboard) {
		//wangyga +  判断是否当前透视图
		IContext context = mainboard.getContext();
		String strPerspectiveId = context.getAttribute(PERSPECTIVE_ID)+"";
		if(strPerspectiveId == null || strPerspectiveId.trim().length() == 0 ||
				!strPerspectiveId.equalsIgnoreCase(PERS_DATA_INPUT)){
			return;
		}		
		
		//生成RepDataControler与DataSourceConfig的单实例
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
