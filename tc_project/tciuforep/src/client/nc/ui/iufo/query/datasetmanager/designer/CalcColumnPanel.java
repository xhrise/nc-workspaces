package nc.ui.iufo.query.datasetmanager.designer;

import javax.swing.JLabel;

import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITextArea;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.dsmanager.BasicWizardStepPanel;
import nc.ui.pub.querytoolize.WizardShareObject;
import nc.vo.pub.dsmanager.DataSetDesignObject;
import com.ufsoft.iufo.resource.StringResource;

public class CalcColumnPanel extends BasicWizardStepPanel {

	private static final long serialVersionUID = 1L;

	private UITextArea textArea;

	private UITextField textField;

	private UITable table;

	/**
	 * Create the panel
	 */
	public CalcColumnPanel(WizardShareObject wso) {
		super(wso);
		setLayout(null);

		table = new UITable();
		table.setBounds(10, 10, 217, 284);
		add(table);

		textField = new UITextField();
		textField.setBounds(233, 40, 287, 22);
		add(textField);

		textArea = new UITextArea();
		textArea.setBounds(236, 94, 284, 200);
		add(textArea);

		JLabel label = new JLabel();
		label.setText("New JLabel");
		label.setBounds(233, 16, 66, 18);
		add(label);

		JLabel label_1 = new JLabel();
		label_1.setText("New JLabel");
		label_1.setBounds(233, 68, 66, 18);
		add(label_1);
	}

	/**
	 * @i18n mbiadhoc00012=º∆À„¡–
	 */
	public String getStepTitle() {
		return StringResource.getStringResource("mbiadhoc00012");
	}

	public DataSetDesignObject getSharedObject() {
		return (DataSetDesignObject) getWizardShareObject();
	}
}
