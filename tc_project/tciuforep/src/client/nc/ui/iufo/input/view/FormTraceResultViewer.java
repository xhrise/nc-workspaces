package nc.ui.iufo.input.view;

import java.awt.BorderLayout;


import com.ufida.zior.view.Viewer;
import com.ufsoft.iufo.inputplugin.biz.formulatracenew.UfoFormulaTraceNavPanel;

public class FormTraceResultViewer extends Viewer {
	
private UfoFormulaTraceNavPanel m_tracePanel=null;
	
	protected void startup() {
		removeAll();
		setLayout(new BorderLayout());
		m_tracePanel=new UfoFormulaTraceNavPanel();
		add(m_tracePanel,BorderLayout.CENTER);
	}
	
	public void reInit(){
	}
	
	public UfoFormulaTraceNavPanel getTracePanel(){
		return m_tracePanel;
	}

	@Override
	public String[] createPluginList() {
		return null;
	}

	@Override
	protected void shutdown() {

	}

}
