/*
 * 创建日期 2006-6-7
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.ui.eh.kc.h0257505;

import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.lang.UFDate;


/**
 * 功能:原料盘点单
 * ZB01
 * @author WB
 * 2008-10-15 16:30:09
 *
 */
@SuppressWarnings("serial")
public class ClientUI extends AbstractClientUI{
	
//	UIRefPane ref=null;
	public ClientUI(){
		super();
//		ref=(UIRefPane) getBillCardPanel().getHeadItem("pk_store").getComponent();
//		String ylflag = ((ClientEventHandler)getManageEventHandler()).getIntype();
//		String where = ref.getRefTempDataWherePart()+" and is_flag = "+ylflag+"";
//		ref.setWhereString(where);
	}

    /**
     * @param arg0
     */
    public ClientUI(Boolean arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @param arg3
     * @param arg4
     */
    public ClientUI(String pk_corp, String pk_billType, String pk_busitype, String operater, String billId)
    {
        super(pk_corp, pk_billType, pk_busitype, operater, billId);
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.manage.BillManageUI#createController()
     */
    @Override
	protected AbstractManageController createController() {
        // TODO 自动生成方法存根
        return new ClientCtrl();
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.trade.manage.BillManageUI#createEventHandler()
     */
    @Override
	public ManageEventHandler createEventHandler() {
        // TODO Auto-generated method stub
        return new ClientEventHandler(this,this.getUIControl());
    }
    
    @Override
	public void setDefaultData() throws Exception {
    	getBillCardPanel().setHeadItem("checkdate", _getDate());
    	IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        String sql = "select pk_period FROM eh_period WHERE nyear = "+_getDate().getYear()+" and nmonth = "+_getDate().getMonth()+" and pk_corp = '"+_getCorp().getPk_corp()+"'";
        Object pk_period = iUAPQueryBS.executeQuery(sql, new ColumnProcessor());
        getBillCardPanel().setHeadItem("pk_period", pk_period==null?null:pk_period.toString());
        super.setDefaultData();
    }
   
   
 
    /*
     * 注册自定义按钮
     * 2008-04-02
     */
    @Override
	protected void initPrivateButton() {
    	super.initPrivateButton();
    	nc.vo.trade.button.ButtonVO btnBus = ButtonFactory.createButtonVO(IEHButton.GENRENDETAIL,"生成明细","生成明细");
        btnBus.setOperateStatus(new int[]{IBillOperate.OP_ADD,IBillOperate.OP_EDIT});
        addPrivateButton(btnBus);
    }
 
    @Override
    public boolean beforeEdit(BillEditEvent e) {
    	String strKey=e.getKey();
    	if(strKey.equals("vinvcode")){
//    		String pk_period = getBillCardPanel().getHeadItem("pk_period").getValueObject()==null?null:
//    			 					getBillCardPanel().getHeadItem("pk_period").getValueObject().toString();			//期间
    		UFDate checkdate = new UFDate(getBillCardPanel().getHeadItem("checkdate").getValueObject()==null?null:
				getBillCardPanel().getHeadItem("checkdate").getValueObject().toString());
    		String pk_store = getBillCardPanel().getHeadItem("pk_store").getValueObject()==null?null:
					getBillCardPanel().getHeadItem("pk_store").getValueObject().toString();								//仓库
    		if(checkdate==null||pk_store==null){
    			showErrorMessage("请先选择盘点日期和盘点仓库!");
    		}
    	}
    	return super.beforeEdit(e);
    }
    @SuppressWarnings("unchecked")
	@Override
    public void afterEdit(BillEditEvent e) {
    	String strKey=e.getKey();
    	//盘点时根据物料取出 当月的库存
    	if(strKey.equals("vinvcode")){
    		int row=e.getRow();
    		String pk_store = getBillCardPanel().getHeadItem("pk_store").getValueObject()==null?"":getBillCardPanel().getHeadItem("pk_store").getValueObject().toString();								//仓库
    		String pk_invbasdoc = getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc")==null?"":
        							getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc").toString();					//物料
    		//UFDate checkdate = new UFDate(getBillCardPanel().getHeadItem("checkdate").getValueObject()==null?null:
			//						getBillCardPanel().getHeadItem("checkdate").getValueObject().toString());
    		try {
	    		//HashMap hm = getInvKc(pk_store, checkdate,pk_invbasdoc);
	    		//UFDouble kcamount = new UFDouble(hm.get(pk_invbasdoc)==null?"0":hm.get(pk_invbasdoc).toString());			//库存数量
	    		//modify by houcq 2011-06-20修改取库存方法
	    		//UFDouble kcamount = new PubTools().getInvKcAmount(_getCorp().getPk_corp(),_getDate(),pk_invbasdoc);
    			//modify by houcq 2011-08-17修改取库存方法
	    		HashMap hm = new PubTools().getInvKcAmountByStore( _getCorp().getPk_corp(),_getDate(), pk_store);
	    		getBillCardPanel().setBodyValueAt(hm.get(pk_invbasdoc), row, "totalnum");
	    		getBillCardPanel().setBodyValueAt(null, row, "inum");
	    		getBillCardPanel().setBodyValueAt(null, row, "difference");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
    		
    		
    	}
    	super.afterEdit(e);
    }
    
    /**
     * 得到库存数量
     * @param pk_store
     * @param pk_period
     * @return
     */
    @SuppressWarnings("unchecked")
	public HashMap getInvKc(String pk_store,UFDate checkdate,String pk_invbasdoc) throws Exception{
    	HashMap hm = new HashMap();
    	hm = new PubTools().getDateinvKC(pk_store, pk_invbasdoc, checkdate, ((ClientEventHandler)getManageEventHandler()).getIntype(), _getCorp().getPk_corp());
    	return hm;
    }
    
    @Override
    protected void initSelfData() {
    	super.initSelfData();
//      // 显示数据库中的0.
        getBillCardPanel().getBodyPanel().getRendererVO().setShowZeroLikeNull(false);
    }
}

   
    


