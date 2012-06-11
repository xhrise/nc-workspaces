package nc.ui.iufo.install;

import javax.swing.JComponent;
import javax.swing.JPanel;

import nc.ui.sm.accountmanage.advance.IAdvanceSetup;
/**
 * 升级界面交互类，目前暂无需求：汇率数据节点不升级，仅会计类型时间关键字的任务从汇率方案取数
 * @author liulp 2008-06-27
 * 
 *
 */
public class IUFOAdvancedSetup extends JPanel implements IAdvanceSetup{
	private static final long serialVersionUID = 1L;

	public Object getKey() {
		return null;
	}

	public Object getObject() {
		return null;
	}

	public JComponent getSetupUI() {
		return null;
	}

	public String getTitle() {
		return null;
	}

	public void initUI(Object value) {

	}

}
