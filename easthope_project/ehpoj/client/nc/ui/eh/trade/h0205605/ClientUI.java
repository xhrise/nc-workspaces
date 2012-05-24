/*
 * 创建日期 2006-6-7
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.ui.eh.trade.h0205605;

import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.refpub.InvdocByCusdocRefModel;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;

/**
 * 功能:销售计划
 * ZA96
 * @author WB
 * 2008-10-10 14:35:32
 *
 */
public class ClientUI extends AbstractClientUI{
	
	String pk_cubasdoc = null;
	public ClientUI() {
        super();
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
        super.setDefaultData();
    }
   
    @Override
    public boolean beforeEdit(BillEditEvent e) {
    	String strKey=e.getKey();
        if (e.getPos()==BODY){
            if("vinvcode".equalsIgnoreCase(strKey)){
            	int row=getBillCardPanel().getBillTable().getSelectedRow();
            	pk_cubasdoc=getBillCardPanel().getBodyValueAt(row,"pk_cubasdoc")==null?"":
                    		getBillCardPanel().getBodyValueAt(row,"pk_cubasdoc").toString();
            	if(pk_cubasdoc==null||pk_cubasdoc.length()<=0){
            		showErrorMessage("客户不能为空,请先选择客户!");
            	}
            	InvdocByCusdocRefModel.pk_cubasdoc = pk_cubasdoc;  //将客商传到参照中
            }
        }
    	return super.beforeEdit(e);
    }
    
   
 
    /*
     * 注册自定义按钮
     * 2008-04-02
     */
    @Override
	protected void initPrivateButton() {
    	nc.vo.trade.button.ButtonVO btn1 = ButtonFactory.createButtonVO(IEHButton.GENRENDETAIL,"按可销料生成明细","按可销料生成明细");
    	btn1.setOperateStatus(new int[]{IBillOperate.OP_EDIT,IBillOperate.OP_ADD});
    	addPrivateButton(btn1);
    	nc.vo.trade.button.ButtonVO btn2 = ButtonFactory.createButtonVO(IEHButton.ExcelImport,"Excel导入","Excel导入");
    	btn2.setOperateStatus(new int[]{IBillOperate.OP_EDIT,IBillOperate.OP_ADD});
    	addPrivateButton(btn2);
    	super.initPrivateButton();
    	
    }
 
}

   
    

