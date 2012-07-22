package nc.vo.yto.business;

import java.io.Serializable;

import nc.ui.hr.global.Global;

public class OperationMsg implements Serializable{

	public String usercode;

	public String username;

	public String unitcode;

	public String optime;

	public String getOptime() {
		return optime;
	}

	public void setOptime(String optime) {
		this.optime = optime;
	}

	public String getUnitcode() {
		return unitcode;
	}

	public void setUnitcode(String unitcode) {
		this.unitcode = unitcode;
	}

	public String getUsercode() {
		return usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
