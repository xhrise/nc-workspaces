/*
 * 创建日期 2006-6-7
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.ui.eh.trade.h0208005;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.lang.UFDate;

/**
 * 功能 原料价格 
 * ZA98
 * @author WB 2008-10-14 13:45:27
 */
@SuppressWarnings("serial")
public class ClientUI extends AbstractClientUI{
    HashMap<String,String> map = null;
	public ClientUI() {
        super();
        map = this.getAllInv();
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
        getBillCardPanel().setHeadItem("busidate", _getDate());
        super.setDefaultData();
    }
  
    @Override
	protected void initSelfData() {
    }
   
    /*
     * 注册自定义按钮
     * 2008-04-02
     */
    @Override
	protected void initPrivateButton() {
        nc.vo.trade.button.ButtonVO btn = ButtonFactory.createButtonVO(IEHButton.GENRENDETAIL,"生成原料明细","生成原料明细");
        btn.setOperateStatus(new int[]{IBillOperate.OP_EDIT,IBillOperate.OP_ADD});
        addPrivateButton(btn);
        super.initPrivateButton();
    }
    public void afterEdit(BillEditEvent e) {
    {

    	if("vinvcode".equals(e.getKey())){
            int row=getBillCardPanel().getBillTable().getSelectedRow();
            Object pk_invbasdoc=getBillCardPanel().getBodyValueAt(row,"pk_invbasdoc");
            if (!map.containsKey(pk_invbasdoc))
            {
            	showWarningMessage("你选择的物料必须满足库存月报表库存的且 编码以01或者 07开头且仓库类型不为成品库的,请检查!");
            	getBillCardPanel().getBillModel().delLine(new int[]{row});
            }
    	}
    }
    }
    @SuppressWarnings("unchecked")
	private HashMap<String,String> getAllInv() {
    	HashMap<String,String> hm = new HashMap<String,String>();
        UFDate	date = _getDate();
        int year = date.getYear();
        int month = date.getMonth();
        //新版SQL
        StringBuffer sql = new StringBuffer()
        /*
         * 原料价格录入： 库存月报表有库存的 且 编码=01或者 07 且 仓库类型不为成品库的 
         */
        .append(" select a.pk_invbasdoc,c.invcode,c.invname,a.qmsl from (")
        .append(" select b.pk_invbasdoc, sum(nvl(b.qmsl, 0)) qmsl")
        .append(" from eh_calc_kcybb a, eh_calc_kcybb_b b, eh_stordoc c")
        .append(" where a.pk_kcybb = b.pk_kcybb and nvl(a.dr, 0) = 0")
        .append(" and nvl(b.dr, 0) = 0 and b.pk_store = c.pk_bdstordoc")
        .append(" and c.is_flag <> 1 and a.nyear = "+year+"  and a.nmonth = "+month)
        .append(" and a.pk_corp='"+_getCorp().getPk_corp()+"'")
        .append(" group by b.pk_invbasdoc having  sum(nvl(b.qmsl, 0))<>0) a,bd_invmandoc b,bd_invbasdoc c")
        .append(" where a.pk_invbasdoc=b.pk_invmandoc and b.pk_invbasdoc=c.pk_invbasdoc")
        .append(" and b.pk_corp = '"+_getCorp().getPk_corp()+"'")
        .append(" and (c.invcode like '01%' or c.invcode like '07%')");
		try{
			IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
			ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
			if(arr!=null&&arr.size()>0){
				for(int i=0;i<arr.size();i++){
					HashMap tmphm = (HashMap)arr.get(i);
					String pk_invbasdoc = tmphm.get("pk_invbasdoc")==null?"":tmphm.get("pk_invbasdoc").toString();
					hm.put(pk_invbasdoc, pk_invbasdoc);
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return hm;
	}
}

   
    

