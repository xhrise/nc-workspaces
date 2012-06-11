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
 * �����ߵ�λ�����նԻ���
 * �������ڣ�(2004-4-2 10:42:17)
 * @author������Ƽ
 */
public class UnitTreeRefDlg extends AbsRepToolTreeRefDlg implements IUfoContextKey{
	private static final long serialVersionUID = 1238118768470922659L;
	
	private Context m_ufoContext;
	/**
     * UnitTreeRefDlg ������ע�⡣
     */
    private UnitTreeRefDlg(){
        super();
    }

    /**
     * UnitTreeRefDlg ������ע�⡣
     * @param parent java.awt.Container
     */
    public UnitTreeRefDlg(java.awt.Container parent){
        super(parent);
    }
    

    /**
     * UnitTreeRefDlg ������ע�⡣
     * @param parent java.awt.Container
     * @param refVO ValueObject
     */
    public UnitTreeRefDlg(java.awt.Container parent, ValueObject refVO){
        super(parent, refVO);
    }

    /**
     * ���ضԻ������
     * @return String
     */
    public String getTitle(){
        return StringResource.getStringResource("miufo1001436");  //"��λ������"
    }
    /**
     * �õ�������ģ�͡�
     *
     * �������ڣ�(2004-4-2 10:25:02)
     * @author������Ƽ
     * @return javax.swing.tree.DefaultTreeModel
     */
    protected javax.swing.tree.DefaultMutableTreeNode createTreeRefRoot(){
    	//�������Ĳ��ո��ڵ�
        UnitTreeRefNode rootNode=null;
        if(m_ufoContext!=null){
        	UnitInfoVO rootUnit = null;
        if(m_oRefVO != null && m_oRefVO instanceof UnitInfoVO){
            rootUnit = (UnitInfoVO)m_oRefVO;
        } else{
            //ȱʡʹ��ϵͳ����λ
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
     * �õ�����ֵ,��Ҫ����ʵ�֡�
     *
     * �������ڣ�(2004-4-02 10:16:13)
     * @author������Ƽ
     * @return java.lang.String
     * @param nType int
     */
    public String getReturnValue(int nType){
        String strReturnValue = null;
        if(m_oSelRefNode != null){
            UnitTreeRefNode refNode = (UnitTreeRefNode)m_oSelRefNode;

            //��λ����
            if(nType == DataProcessUtil.ITEM_UNIT_CODE){
                strReturnValue = refNode.getUnitCode();
            }
            //��λ����
            else if(nType == DataProcessUtil.ITEM_UNIT_NAME){
                strReturnValue = refNode.getUnitName();
            }
            //��λ���α���
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

