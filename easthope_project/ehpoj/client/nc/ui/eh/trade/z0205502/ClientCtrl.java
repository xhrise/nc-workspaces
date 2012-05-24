/*
 * 创建日期 2006-6-7
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.ui.eh.trade.z0205502;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.stock.z0150502.PerioddiscountHVO;
import nc.vo.eh.stock.z0150502.PerioddiscountVO;


/**
 * 说明：期间折扣计算
 * 类型：ZA66
 * 作者：wb
 * 时间：2008-6-10 16:01:45
 */
public class ClientCtrl extends AbstractManageController {

    /**
     * 
     */
    public ClientCtrl() {
        super();
        // TODO 自动生成构造函数存根
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.bill.ICardController#getCardBodyHideCol()
     */
    public String[] getCardBodyHideCol() {
        // TODO 自动生成方法存根
        return null;
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.bill.ICardController#getCardButtonAry()
     */
    public int[] getCardButtonAry() {
        // TODO 自动生成方法存根
        return new int[]{
                //IBillButton.Query,
                IEHButton.GenNextData,		//生成下月数据
                IEHButton.CALCKCYBB,		//试算
                IEHButton.ConfirmSC,		//启用
                IBillButton.Return,
                IBillButton.Refresh,
                IBillButton.Print,
                IEHButton.FirDayOfMonCut
                
               
        };
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.bill.ICardController#isShowCardRowNo()
     */
    public boolean isShowCardRowNo() {
        // TODO 自动生成方法存根
        return true;
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.bill.ICardController#isShowCardTotal()
     */
    public boolean isShowCardTotal() {
        // TODO 自动生成方法存根
        return true;
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.bill.IListController#getListBodyHideCol()
     */
    public String[] getListBodyHideCol() {
        // TODO 自动生成方法存根
        return null;
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.bill.IListController#getListButtonAry()
     */
    public int[] getListButtonAry() {
        // TODO 自动生成方法存根
        return new int[]{
                IBillButton.Query,
                IBillButton.Card,
                IBillButton.Refresh
        };
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.bill.IListController#getListHeadHideCol()
     */
    public String[] getListHeadHideCol() {
        // TODO 自动生成方法存根
        return null;
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.bill.IListController#isShowListRowNo()
     */
    public boolean isShowListRowNo() {
        // TODO 自动生成方法存根
        return true;
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.bill.IListController#isShowListTotal()
     */
    public boolean isShowListTotal() {
        // TODO 自动生成方法存根
        return true;
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getBillType()
     */
    public String getBillType() {
        // TODO 自动生成方法存根
        return IBillType.eh_z0205502;
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getBillVoName()
     */
    public String[] getBillVoName() {
        // TODO 自动生成方法存根
        return new String[]{
            PubBillVO.class.getName(),
            PerioddiscountHVO.class.getName(),
            PerioddiscountVO.class.getName()
        };
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getBodyCondition()
     */
    public String getBodyCondition() {
        // TODO 自动生成方法存根
        return null;
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getBodyZYXKey()
     */
    public String getBodyZYXKey() {
        // TODO 自动生成方法存根
        return null;
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getBusinessActionType()
     */
    public int getBusinessActionType() {
        // TODO 自动生成方法存根
        return IBusinessActionType.BD;
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getChildPkField()
     */
    public String getChildPkField() {
        // TODO 自动生成方法存根
        return "pk_perioddiscount";
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getHeadZYXKey()
     */
    public String getHeadZYXKey() {
        // TODO 自动生成方法存根
        return null;
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getPkField()
     */
    public String getPkField() {
        // TODO 自动生成方法存根
        return "pk_perioddiscount_h";
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#isEditInGoing()
     */
    public Boolean isEditInGoing() throws Exception {
        // TODO 自动生成方法存根
        return null;
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#isExistBillStatus()
     */
    public boolean isExistBillStatus() {
        // TODO 自动生成方法存根
        return false;
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#isLoadCardFormula()
     */
    public boolean isLoadCardFormula() {
        // TODO 自动生成方法存根
//      return true;
        return true;
    }

}
