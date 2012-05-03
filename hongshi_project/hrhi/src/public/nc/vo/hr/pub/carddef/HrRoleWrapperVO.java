package nc.vo.hr.pub.carddef;

import nc.vo.pub.ValidationException;
import nc.vo.pub.ValueObject;
import nc.vo.uap.rbac.RoleVO;

/**
 * 为了解决角色VO的toString方法问题，加入的VO包裹类
 * @author wangxing
 *
 */
public class HrRoleWrapperVO extends ValueObject {
	private RoleVO roleVO = null;
	

	/**
	 * 
	 *
	 */
	public HrRoleWrapperVO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	public String getEntityName() {
		// TODO Auto-generated method stub
		return HrRoleWrapperVO.class.getName();
	}

	/**
	 * 
	 */
	public void validate() throws ValidationException {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 */
	public String toString() {
		// TODO Auto-generated method stub
		return roleVO==null?null:(roleVO.getRole_code()+" "+roleVO.getRole_name());
	}

	/**
	 * @return the roleVO
	 */
	public RoleVO getRoleVO() {
		return roleVO;
	}

	/**
	 * @param roleVO the roleVO to set
	 */
	public void setRoleVO(RoleVO roleVO) {
		this.roleVO = roleVO;
	}
}
