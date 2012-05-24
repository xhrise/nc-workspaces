package nc.ui.eh.tools;

import nc.ui.pub.beans.ValueChangedEvent;
import nc.ui.pub.beans.ValueChangedListener;

public class ZKActionListener  implements ValueChangedListener{

	public void valueChanged(ValueChangedEvent e) {
		String distype = e.getNewValue().toString();
		
	}

}
