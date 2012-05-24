
package nc.ui.eh.iso.z0500201;


import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.eh.iso.z0500201.ClientUICheckRuleGetter;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.pub.ICombobox;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.HYBillVO;

/**
 * 功能：检测项目单
 * @author 张起源
 * 2008-04-11 
 */
public class ClientUI extends BillCardUI {

    public ClientUI() {
        super();
        initilize();
    }

    public ClientUI(String arg0, String arg1, String arg2, String arg3, String arg4) {
        super(arg0, arg1, arg2, arg3, arg4);
        initilize();
    }

    @Override
	protected ICardController createController() {
        return new ClientCtrl();
    }

    @Override
	protected CardEventHandler createEventHandler() {
        return new ClientEventHandler(this,this.getUIControl());
    }

    @Override
	public String getRefBillType() {
        return null;
    }

    @Override
	protected void initSelfData() {
    	getBillCardWrapper().initBodyComboBox("ishigh",ICombobox.CW_HIGH, true);
	    getBillCardWrapper().initBodyComboBox("groupitem",ICombobox.CW_GROUP, true);
	
    	
    }
    /**
     * @author 张起源
     * 2008-04-11 
     */
    @Override
	public void setDefaultData() throws Exception {
        try {

            Class c = Class.forName(getUIControl().getBillVoName()[1]);
            SuperVO[] vos = getBusiDelegator().queryByCondition(c, getBodyWherePart());
            //需要先清空
            getBufferData().clear();

            if (vos != null) {
                HYBillVO billVO = new HYBillVO();
                //加载数据到单据
                billVO.setChildrenVO(vos);
                //加载数据到缓冲
                if (getBufferData().isVOBufferEmpty()) {
                    getBufferData().addVOToBuffer(billVO);
                } else {
                    getBufferData().setCurrentVO(billVO);
                }

                //设置当前行
                getBufferData().setCurrentRow(0);
            } else {
                getBufferData().setCurrentRow(-1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    /**
     * 功能: 初始化调用setDefaultData()
     * @return:void
     * @author 张起源
     * 2008-04-11
     */
    private void initilize() {
        getBillCardPanel().getBodyPanel().getRendererVO().setShowZeroLikeNull(false);
        try {
            setDefaultData();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /**
     * 功能: 表体查询条件
     * @return
     * @return:String
     * @author 张起源
     * 2008-03-24
     */
    protected String getBodyWherePart() {
        return " pk_corp='" + _getCorp().getPk_corp() + "' order by itemcode ";
    }

	@Override
	public Object getUserObject() {
		return new ClientUICheckRuleGetter();
	}
	
	@Override
	public void afterEdit(BillEditEvent e) {
		String strKey=e.getKey();
		int row=e.getRow();
		
		

        if(e.getPos()==HEAD){
               String[] formual=getBillCardPanel().getHeadItem(strKey).getEditFormulas();//获取编辑公式
               getBillCardPanel().execHeadFormulas(formual);
           }else if (e.getPos()==BODY){
               String[] formual=getBillCardPanel().getBodyItem(strKey).getEditFormulas();//获取编辑公式
               getBillCardPanel().execBodyFormulas(e.getRow(),formual);
           }else{
               getBillCardPanel().execTailEditFormulas();
           }
        
        if(e.getPos()==BODY && strKey.equals("vkjfs")){
        	String formulae=getBillCardPanel().getBodyValueAt(row, "formulae")==null?"":getBillCardPanel().getBodyValueAt(row, "formulae").toString();
        	String sql="select kzkjname , formulae , pk_kzkj from eh_kzkj where pk_kzkj='"+formulae+"'";
        	IUAPQueryBS  iUAPQueryBS =    (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        	try {
				ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
				for(int i=0;i<al.size();i++){
					HashMap hm=(HashMap) al.get(i);
					String kzkjname=hm.get("kzkjname")==null?"":hm.get("kzkjname").toString();
					String formula=hm.get("formulae")==null?"":hm.get("formulae").toString();
					getBillCardPanel().setBodyValueAt(kzkjname, row, "vkjfs");
					getBillCardPanel().setBodyValueAt(formula, row, "vkjgs");
				}
			} catch (BusinessException e1) {
				e1.printStackTrace();
			}
        	
        }
        
        
        
        
		super.afterEdit(e);
	}


}