/**
 * 
 */
package nc.ui.eh.kc.h0250215;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.button.IEHButton;
import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.eh.kc.h0250210.PeriodVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;

/**
 * 说明: 会计期间
 * @author 牛冶
 * 2007-9-20 下午01:05:42
 */
public class ClientEventHandler extends CardEventHandler {


    public ClientEventHandler(BillCardUI arg0, ICardController arg1) {
        super(arg0, arg1);
    }

    @Override
	public void onBoElse(int btn) throws Exception{
        if(btn==IEHButton.PeriodClose)
            onBoClosePeriod();              //材料结帐
        if(btn==IEHButton.PERIODCANCEL)
        	onBoCancelPeriod();                   //反结账
        if(btn==IEHButton.DOCMANAGE)
        	onBoAddPeriod();                   //新增会计期间
        super.onBoElse(btn);
    }
    
    /**
     * 功能说明:关帐处理
     * @author newyear
     */
    protected void onBoClosePeriod() throws Exception {
       AggregatedValueObject aggVO = this.getBufferData().getCurrentVO();
       if (aggVO==null){
           this.getBillUI().showWarningMessage("缓存数据集为空!");
           return;
       }
       
       //判断以前的月份是否全部结帐
       int selectRow = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
       //判断当月是否已经结帐
       UFBoolean jz_flag = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(selectRow, "jz_flag")==null?new UFBoolean("N")
           :new UFBoolean(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(selectRow, "jz_flag").toString());
       if (jz_flag.equals(new UFBoolean("Y"))){
           this.getBillUI().showWarningMessage("该期间已结帐,不允许重复结帐!");
           return;
       }
       for(int i=0;i<selectRow;i++){
           //取以前的
           jz_flag = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "jz_flag")==null?new UFBoolean("N")
                   :new UFBoolean(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "jz_flag").toString());
           
           if (jz_flag.equals(new UFBoolean("N"))){
               String nyear = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "nyear").toString();
               String nmonth = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "nmonth").toString();
               this.getBillUI().showWarningMessage(nyear+"年"+nmonth+"月份的帐还没有关,请逐级关闭!");
               return;
           } 
       }
       
       //下面进行关帐操作
       String nyear = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(selectRow, "nyear").toString();
       String nmonth = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(selectRow, "nmonth").toString();
       
       if(getBillUI().showOkCancelMessage("你确认要关闭"+nyear+"年"+nmonth+"月的材料帐吗？")==1){
           PeriodVO currVO = (PeriodVO)aggVO.getChildrenVO()[selectRow];
           currVO.setJz_flag(new UFBoolean("Y"));
           
           IVOPersistence  iUAPQueryBS=(IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
           iUAPQueryBS.updateVO(currVO);
           //变昜界面上的值
           getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt(new UFBoolean("Y"), selectRow, "jz_flag");
       }
    }
    
    /**
     * 功能说明:反结账
     * @author newyear
     */
    protected void onBoCancelPeriod() throws Exception {
       AggregatedValueObject aggVO = this.getBufferData().getCurrentVO();
       if (aggVO==null){
           this.getBillUI().showWarningMessage("缓存数据集为空!");
           return;
       }
       
       //判断以前的月份是否全部结帐
       int selectRow = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
       //判断当月是否已经结帐
       UFBoolean jz_flag = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(selectRow, "jz_flag")==null?new UFBoolean("N")
           :new UFBoolean(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(selectRow, "jz_flag").toString());
       if (!jz_flag.equals(new UFBoolean("Y"))){
           this.getBillUI().showWarningMessage("该期间还未结帐!");
           return;
       }
       
       //下面进行开帐操作
       String nyear = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(selectRow, "nyear").toString();
       String nmonth = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(selectRow, "nmonth").toString();
       
       if(getBillUI().showOkCancelMessage("你确认要重新开启"+nyear+"年"+nmonth+"月的材料帐吗？")==1){
           PeriodVO currVO = (PeriodVO)aggVO.getChildrenVO()[selectRow];
           currVO.setJz_flag(new UFBoolean("N"));
           
           IVOPersistence  iUAPQueryBS=(IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
           iUAPQueryBS.updateVO(currVO);
           //变昜界面上的值
           getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt(new UFBoolean("N"), selectRow, "jz_flag");
       }
    }
    /**
     * 功能说明:
     * 新增会计期间，如果没有增加任何会计期间，则增加本年会计期间，如果本年已经有会计期间，
     * 则只增加本年度没有的月份
     * @author houcq 2011-04-27
     */
    @SuppressWarnings({ "unchecked", "static-access" })
	protected void onBoAddPeriod() throws Exception {
    	IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());    	
        StringBuffer sql = new StringBuffer()
        .append(" select nyear,nmonth from eh_period where pk_corp='"+_getCorp().getPk_corp())
        .append("' and nvl(dr,0)=0 order by nyear desc,nmonth desc");        
        ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
        int nmonth=1;
		int year=ClientEnvironment.getInstance().getServerTime().getYear();
        if (arr!=null&&arr.size()>0)
        {
        	HashMap hm =(HashMap) arr.get(0);
        	nmonth = Integer.parseInt(hm.get("nmonth").toString())+1;
        	year = Integer.parseInt(hm.get("nyear").toString());        	
        }
       
        if (nmonth==13 || arr==null)
        {
        	if (getBillUI().showOkCancelMessage("系统将一次性生成下一年期间数据，是否确认？")!=1)
        	{
        		return;
        	}
        	
        }
        else
        {
        	if (getBillUI().showOkCancelMessage("系统将一次性生成本年剩余期间数据，是否确认？")!=1)
        	{
        		return;
        	}
        }
        if (nmonth==13)
        {
        	year=year+1;
        	nmonth=1;
        }
        ArrayList list = new ArrayList();
        for ( int i=nmonth;i<13;i++)
        {
        	PeriodVO vo = new PeriodVO();
        	UFDate beginDate = new UFDate(toDateString(year,i,1));
        	UFDate endDate = new UFDate(toDateString(year,i,UFDate.getDaysMonth(year,i)));
        	vo.setNyear(year);
        	vo.setNmonth(i);
        	vo.setBegindate(beginDate);
        	vo.setEnddate(endDate);
        	vo.setPk_corp(_getCorp().getPk_corp());
        	vo.setPrimaryKey(_getCorp().getPk_corp());
        	vo.setTs(ClientEnvironment.getServerTime());
        	vo.setDr(0);
        	vo.setDmakedate(_getDate());
        	list.add(vo);
        }
        if (list.size()>0)
        {
        	PeriodVO[] pvos =(PeriodVO[]) list.toArray(new PeriodVO[list.size()]);
            IVOPersistence  ivo=(IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
            ivo.insertVOArray(pvos);
        }
        getBillUI().setDefaultData();
    }
    @Override
	protected void onBoBodyQuery() throws Exception {
        StringBuffer sbWhere = new StringBuffer();
        if(askForQueryCondition(sbWhere)==false) 
            return; 
        String pk_corp = _getCorp().getPrimaryKey();
        SuperVO[] queryVos = queryHeadVOs(sbWhere.toString()+" and (pk_corp = '"+pk_corp+"') ");

        getBufferData().clear();
        // 增加数据到Buffer
        addDataToBuffer(queryVos);
        updateBuffer(); 
    }
    private static String toDateString(int year, int month, int day) {
        String strYear = String.valueOf(year);
        for (int j = strYear.length(); j < 4; j++)
            strYear = "0" + strYear;
        String strMonth = String.valueOf(month);
        if (strMonth.length() < 2)
            strMonth = "0" + strMonth;
        String strDay = String.valueOf(day);
        if (strDay.length() < 2)
            strDay = "0" + strDay;
        return strYear + "-" + strMonth + "-" + strDay;

    }

}
