package nc.ui.iufo.input.view;

import java.awt.BorderLayout;

import com.ufida.zior.view.Viewer;
import com.ufsoft.iufo.inputplugin.ufobiz.data.UfoCheckResultPanel;

public class CheckResultViewer extends Viewer {
	private static final long serialVersionUID = 4199459419478966889L;
	private UfoCheckResultPanel m_checkPanel=null;
	
	protected void startup() {
		removeAll();
		setLayout(new BorderLayout());
		m_checkPanel=new UfoCheckResultPanel();
		add(m_checkPanel,BorderLayout.CENTER);
	}
	
	public void changeRepDataEditor(){
		m_checkPanel.changeRepDataEditor();
	}
	
	public String[] createPluginList() {
		return null;
	}

	protected void shutdown() {
	}
}
