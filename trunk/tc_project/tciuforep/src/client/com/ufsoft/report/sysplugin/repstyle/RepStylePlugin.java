/**
 * 
 */
package com.ufsoft.report.sysplugin.repstyle;

import com.ufida.zior.plugin.AbstractPlugin;
import com.ufida.zior.plugin.IPluginAction;

/**
 * @author wangyga
 * ������ʾ��������¿�ܲ�����������ǰ��
 * @created at 2009-9-3,����11:24:23
 *
 */
public class RepStylePlugin extends AbstractPlugin{

	@Override
	protected IPluginAction[] createActions() {
		return new IPluginAction[]{new RepStyleAction(),
				new RepDisplayPercentAction()};
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startup() {
		// TODO Auto-generated method stub
		
	}

}
