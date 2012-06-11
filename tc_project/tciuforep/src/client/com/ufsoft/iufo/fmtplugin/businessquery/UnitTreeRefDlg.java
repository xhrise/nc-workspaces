package com.ufsoft.iufo.fmtplugin.businessquery;

import javax.swing.tree.DefaultMutableTreeNode;

import nc.pub.iufo.cache.base.UnitCache;
import nc.vo.iufo.unit.UnitInfoVO;
import nc.vo.pub.ValueObject;

import com.ufida.dataset.Context;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.DataProcessUtil;
import com.ufsoft.iufo.fmtplugin.formatcore.CacheProxy;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.resource.StringResource;

/**
 * 报表工具单位树参照对话框。
 * 创建日期：(2004-4-2 10:42:17)
 * @author：刘良萍
 */
public class UnitTreeRefDlg extends AbsRepToolTreeRefDlg implements IUfoContextKey{
	private static final long serialVersionUID = 1238118768470922659L;
	
	private Context m_ufoContext;
	/**
     * UnitTreeRefDlg 构造子注解。
     */
    private UnitTreeRefDlg(){
        super();
    }

    /**
     * UnitTreeRefDlg 构造子注解。
     * @param parent java.awt.Container
     */
    public UnitTreeRefDlg(java.awt.Container parent){
        super(parent);
    }
    

    /**
     * UnitTreeRefDlg 构造子注解。
     * @param parent java.awt.Container
     * @param refVO ValueObject
     */
    public UnitTreeRefDlg(java.awt.Container parent, ValueObject refVO){
        super(parent, refVO);
    }

    /**
     * 返回对话框标题
     * @return String
     */
    public String getTitle(){
        return StringResource.getStringResource("miufo1001436");  //"单位树参照"
    }
    /**
     * 得到参照树模型。
     *
     * 创建日期：(2004-4-2 10:25:02)
     * @author：刘良萍
     * @return javax.swing.tree.DefaultTreeModel
     */
    protected javax.swing.tree.DefaultMutableTreeNode createTreeRefRoot(){
    	//创建树的参照根节点
        UnitTreeRefNode rootNode=null;
        if(m_ufoContext!=null){
        	UnitInfoVO rootUnit = null;
        if(m_oRefVO != null && m_oRefVO instanceof UnitInfoVO){
            rootUnit = (UnitInfoVO)m_oRefVO;
        } else{
            //缺省使用系统根单位
            UnitCache unitCache = CacheProxy.getSingleton().getUnitCache();
            if(unitCache == null){
                return null;
            }
            rootUnit = unitCache.getRootUnitInfo();
        }
            String strOrgPk = m_ufoContext.getAttribute(ORG_PK) == null ? null : (String)m_ufoContext.getAttribute(ORG_PK);
            rootNode= RepToolTreeRefHelper.createUnitTreeRefRoot(rootUnit,strOrgPk);
        }
        	

        return rootNode;
    }
    
    /**
     * 得到返回值,需要子类实现。
     *
     * 创建日期：(2004-4-02 10:16:13)
     * @author：刘良萍
     * @return java.lang.String
     * @param nType int
     */
    public String getReturnValue(int nType){
        String strReturnValue = null;
        if(m_oSelRefNode != null){
            UnitTreeRefNode refNode = (UnitTreeRefNode)m_oSelRefNode;

            //单位编码
            if(nType == DataProcessUtil.ITEM_UNIT_CODE){
                strReturnValue = refNode.getUnitCode();
            }
            //单位名称
            else if(nType == DataProcessUtil.ITEM_UNIT_NAME){
                strReturnValue = refNode.getUnitName();
            }
            //单位级次编码
            else if(nType == DataProcessUtil.ITEM_UNIT_LEVEL_CODE){
                strReturnValue = refNode.getUnitOrgCode();
            }
        }
        return strReturnValue;
    }
    
    public Context getUfoContext() {
    	return m_ufoContext;
    }
    
    public void setUfoContext(Context context) {
    	m_ufoContext = context;
    	rebuildRefTree();
    }    

   public void rebuildRefTree(){
    DefaultMutableTreeNode rootNode = createTreeRefRoot();
    m_oTreeRefModel = createTreeRefModel(rootNode);
    if(m_oTreeRefModel!=null)
    ivjJTreeRef.setModel(m_oTreeRefModel);
   }
}

