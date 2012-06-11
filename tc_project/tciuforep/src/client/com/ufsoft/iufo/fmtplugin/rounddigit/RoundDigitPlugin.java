package com.ufsoft.iufo.fmtplugin.rounddigit;

import com.ufida.zior.plugin.AbstractPlugin;
import com.ufida.zior.plugin.IPluginAction;
import com.ufsoft.iufo.fmtplugin.rounddigitarea.RoundDigitAreaRender;
import com.ufsoft.table.re.CellRenderAndEditor;
/**
 * ��λ������
 * @created at 2009-4-23,����02:28:32
 * @since v56
 */
public class RoundDigitPlugin extends AbstractPlugin{
	
	public static final String GROUP = "rounddigit";
	
	static{
		CellRenderAndEditor.getInstance().registExtSheetRenderer(new RoundDigitAreaRender());
	}

	@Override
	protected IPluginAction[] createActions() {
		return new IPluginAction[] {
				new RoundDigitSetAction(),//������λ��
				new RoundDigitCancelAction(),//���÷���λ��
				new RoundDigitDisplayAction()//��ʾ����λ��
		};
	}

	@Override
	public void shutdown() {
	}

	@Override
	public void startup() {
	}

}
