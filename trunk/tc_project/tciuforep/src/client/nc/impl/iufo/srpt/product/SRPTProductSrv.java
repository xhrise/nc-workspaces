/*
 * 创建日期 2006-2-20
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
	 * 公有构造方法，因为有组件间调用
	 *
	 */
	public SRPTProductSrv(){
		super();
	}

	/* （非 Javadoc）
	 * @see nc.itf.iufo.product.IProductSrv#getUserEditActionName()
	 */
	public String getUserEditActionName() {
		return ProductSrvHelper.getDefaultEditUserActionName();
	}

	/* （非 Javadoc）
	 * @see nc.itf.iufo.product.IProductSrv#getUnitDisName(java.lang.String)
	 */
	public String getUnitDisName(String strUnitInfoPK) {
		return ProductSrvHelper.getDefaultUnitDisName(strUnitInfoPK);
	}

	/* （非 Javadoc）
	 * @see nc.itf.iufo.product.IProductSrv#getUnitRefActFwd(java.lang.String)
	 */
	public ActionForward getUnitRefActFwd(String strUnitInfoPK) {
		return ProductSrvHelper.getDefaulUnitRefActFwd(strUnitInfoPK);
	}

	/* （非 Javadoc）
     * @see nc.itf.iufo.product.IProductSrv#addRefCount(java.lang.String, nc.vo.pub.ValueObject[])
     */
    public void addRefCount(String strExPropTable, ValueObject[] refedVOs) {
        ProductSrvHelper.addRefCount(strExPropTable,refedVOs);        
    }

    /* （非 Javadoc）
     * @see nc.itf.iufo.product.IProductSrv#delRefCount(java.lang.String, nc.vo.pub.ValueObject[])
     */
    public void delRefCount(String strExPropTable, ValueObject[] refedVOs) {
        ProductSrvHelper.delRefCount(strExPropTable,refedVOs);        
    }

    /* （非 Javadoc）
     * @see nc.itf.iufo.product.IProductSrv#getExPropTypeRefActFwd(java.lang.String, java.lang.String)
     */
    public ActionForward getExPropTypeRefActFwd(String strExPropModuleID, String strRefTypeK) {
        return   ProductSrvHelper.getExPropTypeRefActFwd(strExPropModuleID,strRefTypeK);      
    }

    /* （非 Javadoc）
     * @see nc.itf.iufo.product.IProductSrv#getRefContent(java.lang.String, java.lang.String, java.lang.String)
     */
    public String getRefContent(String strModuleID, String strRefTypePK, String strRefInfoPK) {
        return ProductSrvHelper.getRefContent(strModuleID,strRefTypePK,strRefInfoPK);
    }

    /* （非 Javadoc）
     * @see nc.itf.iufo.product.IProductSrv#getExPropRefActFwd(java.lang.String)
     */
    public ActionForward getExPropRefActFwd(String strExPropModuleID) {
        return  ProductSrvHelper.getExPropRefActFwd(strExPropModuleID);      
    }

    /* （非 Javadoc）
     * @see nc.itf.iufo.product.IProductSrv#getRefItemName(java.lang.String,java.lang.String)
     */
    public String getRefItemName(String strModuleID, String strRefTypePK) {
        return  ProductSrvHelper.getRefItemName(strModuleID,strRefTypePK);    
    }

}
