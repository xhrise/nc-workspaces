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
 * ˵��: ����ڼ�
 * @author ţұ
 * 2007-9-20 ����01:05:42
 */
public class ClientEventHandler extends CardEventHandler {


    public ClientEventHandler(BillCardUI arg0, ICardController arg1) {
        super(arg0, arg1);
    }

    @Override
	public void onBoElse(int btn) throws Exception{
        if(btn==IEHButton.PeriodClose)
            onBoClosePeriod();              //���Ͻ���
        if(btn==IEHButton.PERIODCANCEL)
        	onBoCancelPeriod();                   //������
        if(btn==IEHButton.DOCMANAGE)
        	onBoAddPeriod();                   //��������ڼ�
        super.onBoElse(btn);
    }
    
    /**
     * ����˵��:���ʴ���
     * @author newyear
     */
    protected void onBoClosePeriod() throws Exception {
       AggregatedValueObject aggVO = this.getBufferData().getCurrentVO();
       if (aggVO==null){
           this.getBillUI().showWarningMessage("�������ݼ�Ϊ��!");
           return;
       }
       
       //�ж���ǰ���·��Ƿ�ȫ������
       int selectRow = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
       //�жϵ����Ƿ��Ѿ�����
       UFBoolean jz_flag = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(selectRow, "jz_flag")==null?new UFBoolean("N")
           :new UFBoolean(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(selectRow, "jz_flag").toString());
       if (jz_flag.equals(new UFBoolean("Y"))){
           this.getBillUI().showWarningMessage("���ڼ��ѽ���,�������ظ�����!");
           return;
       }
       for(int i=0;i<selectRow;i++){
           //ȡ��ǰ��
           jz_flag = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "jz_flag")==null?new UFBoolean("N")
                   :new UFBoolean(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "jz_flag").toString());
           
           if (jz_flag.equals(new UFBoolean("N"))){
               String nyear = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "nyear").toString();
               String nmonth = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "nmonth").toString();
               this.getBillUI().showWarningMessage(nyear+"��"+nmonth+"�·ݵ��ʻ�û�й�,���𼶹ر�!");
               return;
           } 
       }
       
       //������й��ʲ���
       String nyear = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(selectRow, "nyear").toString();
       String nmonth = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(selectRow, "nmonth").toString();
       
       if(getBillUI().showOkCancelMessage("��ȷ��Ҫ�ر�"+nyear+"��"+nmonth+"�µĲ�������")==1){
           PeriodVO currVO = (PeriodVO)aggVO.getChildrenVO()[selectRow];
           currVO.setJz_flag(new UFBoolean("Y"));
           
           IVOPersistence  iUAPQueryBS=(IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
           iUAPQueryBS.updateVO(currVO);
           //��[�����ϵ�ֵ
           getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt(new UFBoolean("Y"), selectRow, "jz_flag");
       }
    }
    
    /**
     * ����˵��:������
     * @author newyear
     */
    protected void onBoCancelPeriod() throws Exception {
       AggregatedValueObject aggVO = this.getBufferData().getCurrentVO();
       if (aggVO==null){
           this.getBillUI().showWarningMessage("�������ݼ�Ϊ��!");
           return;
       }
       
       //�ж���ǰ���·��Ƿ�ȫ������
       int selectRow = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
       //�жϵ����Ƿ��Ѿ�����
       UFBoolean jz_flag = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(selectRow, "jz_flag")==null?new UFBoolean("N")
           :new UFBoolean(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(selectRow, "jz_flag").toString());
       if (!jz_flag.equals(new UFBoolean("Y"))){
           this.getBillUI().showWarningMessage("���ڼ仹δ����!");
           return;
       }
       
       //������п��ʲ���
       String nyear = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(selectRow, "nyear").toString();
       String nmonth = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(selectRow, "nmonth").toString();
       
       if(getBillUI().showOkCancelMessage("��ȷ��Ҫ���¿���"+nyear+"��"+nmonth+"�µĲ�������")==1){
           PeriodVO currVO = (PeriodVO)aggVO.getChildrenVO()[selectRow];
           currVO.setJz_flag(new UFBoolean("N"));
           
           IVOPersistence  iUAPQueryBS=(IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
           iUAPQueryBS.updateVO(currVO);
           //��[�����ϵ�ֵ
           getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt(new UFBoolean("N"), selectRow, "jz_flag");
       }
    }
    /**
     * ����˵��:
     * ��������ڼ䣬���û�������κλ���ڼ䣬�����ӱ������ڼ䣬��������Ѿ��л���ڼ䣬
     * ��ֻ���ӱ����û�е��·�
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
        	if (getBillUI().showOkCancelMessage("ϵͳ��һ����������һ���ڼ����ݣ��Ƿ�ȷ�ϣ�")!=1)
        	{
        		return;
        	}
        	
        }
        else
        {
        	if (getBillUI().showOkCancelMessage("ϵͳ��һ�������ɱ���ʣ���ڼ����ݣ��Ƿ�ȷ�ϣ�")!=1)
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
        // �������ݵ�Buffer
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
