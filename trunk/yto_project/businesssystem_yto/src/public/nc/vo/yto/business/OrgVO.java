package nc.vo.yto.business;

import nc.vo.pub.SuperVO;

public class OrgVO extends SuperVO {
	
	private String pk_organizational;
	private String proc;
	private String unitcode;
	private String unitname;
	private String parentcode;
	private String type;
	private int dr;
	private String ts;
	
	public static final String PK_ORGANIZATIONAL = "pk_organizational";
	public static final String PROC = "proc";
	public static final String UNITCODE = "unitcode";
	public static final String UNITNAME = "unitname";
	public static final String PARENTCODE = "parentcode";
	public static final String TYPE = "type";
	public static final String DR = "dr";
	public static final String TS = "ts";

	@Override
	public String getPKFieldName() {
		return "PK_ORGANIZATIONAL";
	}

	@Override
	public String getParentPKFieldName() {
		return null;
	}

	@Override
	public String getTableName() {
		return "bd_organizational";
	}

	public int getDr() {
		return dr;
	}

	public void setDr(int dr) {
		this.dr = dr;
	}

	public String getParentcode() {
		return parentcode;
	}

	public void setParentcode(String parentcode) {
		this.parentcode = parentcode;
	}

	public String getPk_organizational() {
		return pk_organizational;
	}

	public void setPk_organizational(String pk_organizational) {
		this.pk_organizational = pk_organizational;
	}

	public String getProc() {
		return proc;
	}

	public void setProc(String proc) {
		this.proc = proc;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUnitcode() {
		return unitcode;
	}

	public void setUnitcode(String unitcode) {
		this.unitcode = unitcode;
	}

	public String getUnitname() {
		return unitname;
	}

	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}

}
