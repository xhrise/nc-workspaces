/*
 * 创建日期 2006-6-20
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.ui.eh.trade.z0205510;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.trade.z0205510.SeconddiscountBillVO;
import nc.vo.eh.trade.z0205510.SeconddiscountCheckinvVO;
import nc.vo.eh.trade.z0205510.SeconddiscountPoliceVO;
import nc.vo.eh.trade.z0205510.SeconddiscountRangeVO;
import nc.vo.eh.trade.z0205510.SeconddiscountVO;



/**
 * 功能说明：二次折扣
 * 2008-4-8 16:43:39
 */
public class ClientCtrl extends AbstractManageController {

    public ClientCtrl() {
        super();
        // TODO 自动生成构造函数存根
    }


    public String[] getCardBodyHideCol() {
        // TODO 自动生成方法存根
        return null;
    }

 
    public int[] getCardButtonAry() {
        // TODO 自动生成方法存根
    	 int[] btns=nc.ui.eh.button.ButtonTool.insertButtons(
         		ISHSHConst.CARD_BUTTONS_M,
         		new int[] {IEHButton.GENRENDETAIL,IEHButton.STOCKCHANGE},
                 0);//modify by houcq 2010-11-30取消二次折扣终止单据按钮
    	 return btns;
//        return new int[]{
//                IBillButton.Query,
//                IBillButton.Add,
//                IBillButton.Line,
//                IBillButton.Edit,
//                IBillButton.Delete,
//                IBillButton.Save,
//                IBillButton.Action,
//                IBillButton.Return,
//                IBillButton.Cancel,
//                IBillButton.Refresh,
////                IBillButton.Copy,
//                IEHButton.GENRENDETAIL
//            };
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
    	return nc.ui.eh.button.ButtonTool.insertButtons(
                new int[] {},ISHSHConst.LIST_BUTTONS_M, 0);
        // TODO 自动生成方法存根
//        return new int[]{
//                IBillButton.Query,
//                IBillButton.Add,
//                IBillButton.Edit,
//                IBillButton.Delete,
//                IBillButton.Card
//
//            };
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
     * @see nc.ui.trade.controller.IControllerBase#getBillVoName()
     */
    public String[] getBillVoName() {
        // TODO 自动生成方法存根
        return new String[]{
            SeconddiscountBillVO.class.getName(),
            SeconddiscountVO.class.getName(),
            SeconddiscountRangeVO.class.getName(),
            SeconddiscountPoliceVO.class.getName(),
            SeconddiscountCheckinvVO.class.getName()
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
        return IBusinessActionType.PLATFORM;
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getChildPkField()
     */
    public String getChildPkField() {
        // TODO 自动生成方法存根
        return null;
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
        return "pk_seconddiscount";
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
        return true;
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#isLoadCardFormula()
     */
    public boolean isLoadCardFormula() {
        // TODO 自动生成方法存根
        return true;
    }

    public String getBillType() {
        // TODO Auto-generated method stub
        return IBillType.eh_z0205510;
    }


}