package nc.ui.bi.query.freequery.measquery;

import javax.swing.JComponent;
import javax.swing.JTextField;

import com.ufida.dataset.ICustomRefModel;

public class IUFO2NCRefModel implements ICustomRefModel {
	private JTextField iufoRef=null;
	
	public IUFO2NCRefModel(JTextField iufoRef){
		this.iufoRef=iufoRef;
	}

	public JComponent getComponent() {
		return this.iufoRef;
	}

	public Object getValue() {
		if (this.iufoRef != null) {
			return iufoRef.getText();
		} else {
			return null;
		}
	}

	public void setValue(Object value) {
		if(iufoRef!=null&&value!=null){
			iufoRef.setText(value.toString());
		}

	}

}
