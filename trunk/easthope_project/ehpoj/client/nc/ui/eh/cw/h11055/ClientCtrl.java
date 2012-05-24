/*
 * 创建日期 2006-6-7
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.ui.eh.cw.h11055;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.cw.h11055.ArapCosthsBVO;
import nc.vo.eh.cw.h11055.ArapCosthsVO;
import nc.vo.eh.pub.PubBillVO;


/**
 * 说明：成本核算
 * 类型：ZA72
 * 作者：wb
 * 时间：2008-8-11 15:34:32
 */
public class ClientCtrl extends AbstractCtrl {

    /**
     * 
     */
    public ClientCtrl() {
        super();
        // TODO 自动生成构造函数存根
    }


    /* （非 Javadoc）
     * @see nc.ui.trade.bill.ICardController#getCardButtonAry()
     */
    @Override
	public int[] getCardButtonAry() {
        // TODO 自动生成方法存根
        return new int[]{
                IBillButton.Query,
                IEHButton.CALCKCYBB,
                IBillButton.Return,
                IBillButton.Refresh,
                IBillButton.Print,
                IEHButton.GENRENDETAIL
               
        };
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.bill.IListController#getListButtonAry()
     */
    @Override
	public int[] getListButtonAry() {
        // TODO 自动生成方法存根
        return new int[]{
                IBillButton.Query,
                IEHButton.CALCKCYBB,
                IBillButton.Card,
                IBillButton.Refresh
        };
    }

    

    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getBillType()
     */
    @Override
	public String getBillType() {
        // TODO 自动生成方法存根
        return IBillType.eh_h11055;
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getBillVoName()
     */
    @Override
	public String[] getBillVoName() {
        // TODO 自动生成方法存根
        return new String[]{
            PubBillVO.class.getName(),
            ArapCosthsVO.class.getName(),
            ArapCosthsBVO.class.getName()
        };
    }



    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getBusinessActionType()
     */
    @Override
	public int getBusinessActionType() {
        // TODO 自动生成方法存根
        return IBusinessActionType.BD;
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getChildPkField()
     */
    @Override
	public String getChildPkField() {
        // TODO 自动生成方法存根
        return "pk_cosths_b";
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getPkField()
     */
    @Override
	public String getPkField() {
        // TODO 自动生成方法存根
        return "pk_cosths";
    }


    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#isExistBillStatus()
     */
    @Override
	public boolean isExistBillStatus() {
        // TODO 自动生成方法存根
        return false;
    }


}
