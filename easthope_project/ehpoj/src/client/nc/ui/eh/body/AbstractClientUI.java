package nc.ui.eh.body;

import nc.ui.bd.pub.AbstractBdBillCardUI;
import nc.ui.trade.card.CardEventHandler;

/**
 * @author Jorli
 * @date 2011-12-18 обнГ8:22:10
 * @type nc.zip.pub.body.AbstractClientUI
 */
@SuppressWarnings("serial")
public abstract class AbstractClientUI extends AbstractBdBillCardUI {

	protected boolean m_multiSelect = false;

	protected boolean getMultiSelect() {
		return this.m_multiSelect;
	}

	protected abstract CardEventHandler createEventHandler();

	public String getRefBillType() {
		return null;
	}

	public void setDefaultData() throws Exception {
	}

}
