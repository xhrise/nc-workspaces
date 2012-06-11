/*
 * �������� 2006-2-20
 *
 */
package nc.impl.iufo.srpt.product;

import com.ufida.web.action.ActionForward;

import nc.itf.iufo.product.ISRPTProductSrv;
import nc.util.iufo.product.ProductSrvHelper;
import nc.vo.pub.ValueObject;

/**
 * @author liulp
 *
 */
public class SRPTProductSrv implements ISRPTProductSrv {
	/**
	 * ���й��췽������Ϊ����������
	 *
	 */
	public SRPTProductSrv(){
		super();
	}

	/* ���� Javadoc��
	 * @see nc.itf.iufo.product.IProductSrv#getUserEditActionName()
	 */
	public String getUserEditActionName() {
		return ProductSrvHelper.getDefaultEditUserActionName();
	}

	/* ���� Javadoc��
	 * @see nc.itf.iufo.product.IProductSrv#getUnitDisName(java.lang.String)
	 */
	public String getUnitDisName(String strUnitInfoPK) {
		return ProductSrvHelper.getDefaultUnitDisName(strUnitInfoPK);
	}

	/* ���� Javadoc��
	 * @see nc.itf.iufo.product.IProductSrv#getUnitRefActFwd(java.lang.String)
	 */
	public ActionForward getUnitRefActFwd(String strUnitInfoPK) {
		return ProductSrvHelper.getDefaulUnitRefActFwd(strUnitInfoPK);
	}

	/* ���� Javadoc��
     * @see nc.itf.iufo.product.IProductSrv#addRefCount(java.lang.String, nc.vo.pub.ValueObject[])
     */
    public void addRefCount(String strExPropTable, ValueObject[] refedVOs) {
        ProductSrvHelper.addRefCount(strExPropTable,refedVOs);        
    }

    /* ���� Javadoc��
     * @see nc.itf.iufo.product.IProductSrv#delRefCount(java.lang.String, nc.vo.pub.ValueObject[])
     */
    public void delRefCount(String strExPropTable, ValueObject[] refedVOs) {
        ProductSrvHelper.delRefCount(strExPropTable,refedVOs);        
    }

    /* ���� Javadoc��
     * @see nc.itf.iufo.product.IProductSrv#getExPropTypeRefActFwd(java.lang.String, java.lang.String)
     */
    public ActionForward getExPropTypeRefActFwd(String strExPropModuleID, String strRefTypeK) {
        return   ProductSrvHelper.getExPropTypeRefActFwd(strExPropModuleID,strRefTypeK);      
    }

    /* ���� Javadoc��
     * @see nc.itf.iufo.product.IProductSrv#getRefContent(java.lang.String, java.lang.String, java.lang.String)
     */
    public String getRefContent(String strModuleID, String strRefTypePK, String strRefInfoPK) {
        return ProductSrvHelper.getRefContent(strModuleID,strRefTypePK,strRefInfoPK);
    }

    /* ���� Javadoc��
     * @see nc.itf.iufo.product.IProductSrv#getExPropRefActFwd(java.lang.String)
     */
    public ActionForward getExPropRefActFwd(String strExPropModuleID) {
        return  ProductSrvHelper.getExPropRefActFwd(strExPropModuleID);      
    }

    /* ���� Javadoc��
     * @see nc.itf.iufo.product.IProductSrv#getRefItemName(java.lang.String,java.lang.String)
     */
    public String getRefItemName(String strModuleID, String strRefTypePK) {
        return  ProductSrvHelper.getRefItemName(strModuleID,strRefTypePK);    
    }

}
