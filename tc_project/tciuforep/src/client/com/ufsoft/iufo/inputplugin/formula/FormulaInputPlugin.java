package com.ufsoft.iufo.inputplugin.formula;

import com.ufida.zior.plugin.AbstractPlugin;
import com.ufida.zior.plugin.IPluginAction;
/**
 * ����¼��̬�Ĺ�ʽ���
 * @author zhaopq
 * @created at 2009-4-15,����09:19:41
 * @since v5.6
 */
public class FormulaInputPlugin extends AbstractPlugin{

	@Override
	protected IPluginAction[] createActions() {
		return new IPluginAction[] { 
				new FormulaInputToolBarAction() // ��ʽ������
		};
	}
	@Override
	public void shutdown() {
		
	}

	@Override
	public void startup() {
	}

}
