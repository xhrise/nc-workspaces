/**
 * 
 */
package com.ufsoft.report;

import nc.ui.pub.beans.UIPanel;

/**
 * @author guogang
 *
 */
public abstract class UIStatePanel extends UIPanel {
  private boolean isClear;
  public abstract void clear();
  public abstract void setData();
  
public boolean isClear() {
	return isClear;
}
public void setClear(boolean isClear) {
	this.isClear = isClear;
}
  
}
