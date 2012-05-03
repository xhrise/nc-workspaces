package nc.vo.mbSyn;

import java.util.List;

import nc.vo.pub.SuperVO;

public class DepartmentVO extends SuperVO{
	private String[] deptNames;
	private String[] units;

	public String[] getDeptNames() {
		return deptNames;
	}

	public void setDeptNames(String[] deptNames) {
		this.deptNames = deptNames;
	}

	public String[] getUnits() {
		return units;
	}

	public void setUnits(String[] units) {
		this.units = units;
	}

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}
}
