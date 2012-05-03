package nc.vo.hi.hi_301;

import java.util.*;
/**
 * ���Ʋ���VO��
 * �������ڣ�(2004-5-9 19:17:07)
 * @author��Administrator
 */

public class CtrlDeptVO implements java.io.Serializable, Cloneable {
	//��������
	private java.lang.String name;
	//��������
	private String pk_dept;
	//���ſ���Ȩ��
	private boolean controlled = true;
	//���ŵ��Ӳ���
	private java.util.Vector children;
	//�����Ĺ�˾����
	private String pk_corp;
	public java.util.Vector deptchildren;
	public java.lang.String code;
	public static final int CORP = 0;
	public static final int DEPT = 1;
	public int nodeType = 0;
	public boolean loadDept = false;	
	//wangkf add
	private boolean hrcanceled = false;
	private boolean canceled = false;
	private String moduleCode = null;
	private String pk_fathercorp = null;
	/** �ڲ����� */
	public String innercode;
	public String getFathercorp() {
		return pk_fathercorp;
	}
	public void setFathercorp(String pk_fathercorp) {
		this.pk_fathercorp = pk_fathercorp;
	}
/**
 * CtrlDept ������ע�⡣
 */
public CtrlDeptVO() {
	super();
	nodeType = DEPT;
}
/**
 * ����Ӳ���
 * �������ڣ�(2004-5-9 20:16:46)
 * @param child nc.vo.hi.hi_301.CtrlDeptVO
 */
public void addChild(CtrlDeptVO child) {
	if(nodeType==child.nodeType||nodeType==-1){
		if(children==null)
			children=new Vector();
		children.addElement(child);
	}else{
		if(nodeType==CORP&&child.nodeType==DEPT){
			if(deptchildren==null)
				deptchildren = new Vector();
			deptchildren.addElement(child);
		}
	}
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-10-19 9:18:25)
 * @param vo nc.vo.hi.hi_301.CtrlDeptVO
 */
public void addDeptChildren(CtrlDeptVO children) {
	if(deptchildren==null)
		deptchildren=new Vector();
	deptchildren.addElement(children);
}
	/**
	 *��¡�Դ˶���Ϊ��������CtrlDeptVO��Ԫ��
	 */
	public Object clone(){
		CtrlDeptVO cloned=null;
		try{
			cloned=(CtrlDeptVO)super.clone();
		}catch(Exception e){
			//�������¡ʧ�ܣ�������ʵ��������ʼ��
			cloned=new CtrlDeptVO();
			cloned.name=name;
			cloned.pk_dept=pk_dept;
			cloned.controlled=controlled;
			cloned.nodeType = nodeType;
			cloned.code = code;
			cloned.pk_corp = pk_corp;
		}
		if(children!=null){//����к��ӽڵ㣬��¡����
			Vector clonedChildren=new Vector();
			for(int i=0;i<children.size();i++){				
				CtrlDeptVO element=(CtrlDeptVO)children.elementAt(i);
				//��ӿ�¡�Ӻ���
				clonedChildren.addElement(element.clone());
			}
			//���ú���
			cloned.setChildren(clonedChildren);
		}
		if(deptchildren!=null){
			Vector cdeptchildren = new Vector();
			for(int i=0;i<deptchildren.size();i++){
				CtrlDeptVO element=(CtrlDeptVO)deptchildren.elementAt(i);
				//��ӿ�¡�Ӻ���
				cdeptchildren.addElement(element.clone());
			}
			//���ú���
			cloned.setChildren(cdeptchildren);
		}
		return cloned;
	}
/**
 * ��������Ϊpk_dept�Ľڵ㡣
 * �������ڣ�(2004-5-9 20:06:06)
 * @return nc.vo.hi.hi_301.CtrlDeptVO
 * @param pk_dept java.lang.String
 */
public CtrlDeptVO find(String pk_dept) {
	if (pk_dept == null) {
		//���ҵ��Ǹ����
		if (this.pk_dept == null)
			//��ǰ�ڵ��Ǹ����
			return this;
		else
			//��ǰ�ڵ㲻�Ǹ����
			return null;
	}
	//���ҵĲ��Ǹ����
	if (pk_dept.equals(this.pk_dept))
		return this;

	if (children != null) {
		//����к���
		for (int i = 0; i < children.size(); i++) {
			CtrlDeptVO child = (CtrlDeptVO) children.elementAt(i);

			if (child != null) {
				CtrlDeptVO dest = child.find(pk_dept);
				if (dest != null) //�ҵ���
					return dest;
			}
		}
	}
	//û���ҵ�
	return null;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-10-20 9:46:50)
 * @return nc.vo.hi.hi_301.CtrlDeptVO
 * @param pk_corp java.lang.String
 */
public CtrlDeptVO findCorpVO(String pk_corp) {
	if (pk_corp == null) {
		//���ҵ��Ǹ����
		if (this.pk_corp == null)
			//��ǰ�ڵ��Ǹ����
			return this;
		else
			//��ǰ�ڵ㲻�Ǹ����
			return null;
	}
	//���ҵĲ��Ǹ����
	if (pk_corp.equals(this.pk_corp))
		return this;

	if (children != null) {
		//����к���
		for (int i = 0; i < children.size(); i++) {
			CtrlDeptVO child = (CtrlDeptVO) children.elementAt(i);

			if (child != null) {
				CtrlDeptVO dest = child.findCorpVO(pk_corp);
				if (dest != null) //�ҵ���
					return dest;
			}
		}
	}
	//û���ҵ�
	return null;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-10-20 9:46:18)
 * @return nc.vo.hi.hi_301.CtrlDeptVO
 * @param pk_dept java.lang.String
 */
public CtrlDeptVO findDeptChild(String pk_dept) {
	if (pk_dept == null) {
		//���ҵ��Ǹ����
		if (this.pk_dept == null)
			//��ǰ�ڵ��Ǹ����
			return this;
		else
			//��ǰ�ڵ㲻�Ǹ����
			return null;
	}
	//���ҵĲ��Ǹ����
	if (pk_dept.equals(this.pk_dept))
		return this;
	if (this.nodeType == CORP) {
	    if(deptchildren!=null){
			//����к���
			for (int i = 0; i < deptchildren.size(); i++) {
				CtrlDeptVO child = (CtrlDeptVO) deptchildren.elementAt(i);

				if (child != null) {
					CtrlDeptVO dest = child.findDeptChild(pk_dept);
					if (dest != null) //�ҵ���
						return dest;
				}
			}
		}
	}
	else {
		if (children != null) {
			//����к���
			for (int i = 0; i < children.size(); i++) {
				CtrlDeptVO child = (CtrlDeptVO) children.elementAt(i);

				if (child != null) {
					CtrlDeptVO dest = child.find(pk_dept);
					if (dest != null) //�ҵ���
						return dest;
				}
			}
		}
	}
	//û���ҵ�
	return null;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-5-9 19:30:53)
 * @return java.util.Vector
 */
public java.util.Vector getChildren() {
	return children;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-10-18 16:55:37)
 * @return java.lang.String
 */
public java.lang.String getCode() {
	return code;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-10-18 16:49:08)
 * @return java.util.Vector
 */
public java.util.Vector getDeptchildren() {
	return deptchildren;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-5-9 19:17:48)
 * @return java.lang.String
 */
public java.lang.String getName() {
	return name;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-10-19 9:13:10)
 * @return int
 */
public int getNodeType() {
	return nodeType;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-10-18 16:11:27)
 * @return java.lang.String
 */
public String getPk_corp() {
	return pk_corp;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-5-9 19:18:33)
 * @return int
 */
public String getPk_dept() {
	return pk_dept;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-5-26 20:28:28)
 * @return int
 */
public int hashCode() {
	if (nodeType == DEPT) {
		if (pk_dept != null)
			return pk_dept.hashCode();
		else
			return super.hashCode();
	}
	else {
		if (pk_corp != null)
			return pk_corp.hashCode();
		else
			return super.hashCode();
	}
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-5-9 19:18:58)
 * @return boolean
 */
public boolean isControlled() {
	return controlled;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-10-20 10:51:47)
 * @return boolean
 */
public boolean isLoadDept() {
	return loadDept;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-10-20 10:14:08)
 */
public void removeAllDeptChild() {
	deptchildren = null;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-5-9 19:30:53)
 * @param newChildren java.util.Vector
 */
public void setChildren(java.util.Vector newChildren) {
	children = newChildren;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-10-18 16:55:37)
 * @param newCode java.lang.String
 */
public void setCode(java.lang.String newCode) {
	code = newCode;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-5-9 19:18:58)
 * @param newControlled boolean
 */
public void setControlled(boolean newControlled) {
	controlled = newControlled;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-10-18 16:49:08)
 * @param newDeptchildren java.util.Vector
 */
public void setDeptchildren(java.util.Vector newDeptchildren) {
	deptchildren = newDeptchildren;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-10-20 10:51:47)
 * @param newLoadDept boolean
 */
public void setLoadDept(boolean newLoadDept) {
	loadDept = newLoadDept;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-5-9 19:17:48)
 * @param newName java.lang.String
 */
public void setName(java.lang.String newName) {
	name = newName;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-10-19 9:12:57)
 * @param nodeType int
 */
public void setNodeType(int nodeType) {
	this.nodeType = nodeType;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-10-18 16:10:38)
 * @param pk_corp java.lang.String
 */
public void setPk_corp(String pk_corp) {
	this.pk_corp = pk_corp;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-5-9 19:18:33)
 * @param newPk_dept int
 */
public void setPk_dept(String newPk_dept) {
	pk_dept = newPk_dept;
}
	public String toString(){
		return code+" "+name;
	}
	/**
	 * @return ���� canceled��
	 */
	public boolean isCanceled() {
		return canceled;
	}
	/**
	 * @param canceled Ҫ���õ� canceled��
	 */
	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}
	/**
	 * @return ���� hrcanceled��
	 */
	public boolean isHrcanceled() {
		return hrcanceled;
	}
	/**
	 * @param hrcanceled Ҫ���õ� hrcanceled��
	 */
	public void setHrcanceled(boolean hrcanceled) {
		this.hrcanceled = hrcanceled;
	}
	/**
	 * @return ���� moduleCode��
	 */
	public String getModuleCode() {
		return moduleCode;
	}
	/**
	 * @param moduleCode Ҫ���õ� moduleCode��
	 */
	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}
	/**
	 * �ڲ�����Getter.
	 * @return
	 */
	public String getInnercode() {
		return innercode;
	}
	/**
	 * �ڲ�����Setter.
	 * @param newInnercode
	 */
	public void setInnercode(String newInnercode) {
		innercode = newInnercode;
	}
}
