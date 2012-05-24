
package nc.ui.eh.report.h0900405;

import java.awt.BorderLayout;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.pub.report.ReportBaseClass;
import nc.vo.eh.ipub.ISQLChange;
import nc.vo.eh.report.h0900405.ReportVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.report.SubtotalContext;

/*
 * 标签编织袋药品报表
 */
public class ReportUI extends ToftPanel {
	
	public ButtonObject m_boQuery;
    public ButtonObject m_boPrint;
    private ReportBaseClass m_report;
    private QueryConditionClient m_qryDlg;
    private ClientEnvironment ce=ClientEnvironment.getInstance();
    
    private UFDate ptoday = null;

	public ReportUI() {
		super();
		m_boQuery = new ButtonObject("查询", "查询报表", 0);
        m_boPrint = new ButtonObject("打印", "打印报表", 0);
        initialize(); 
	}
	
	private void initialize()
	{
	    setName("GeneralPane");
	    setLayout(new BorderLayout());
	    setSize(1024, 768);
	    add(getReportBase(), "Center");
	    setButtons(getBtnAry());
	    updateButtons();
	    getReportBase().setShowNO(true);
	    int colcount = getReportBase().getBillTable().getColumnCount();
	    String st[] = new String[colcount];
	    String skey = "";
	    for(int i=0;i<colcount;i++){
	    	 skey = getReportBase().getBodyShowItems()[i].getKey().trim();
	    	 st[i] = skey;
	    }
	}

	public ButtonObject[] getBtnAry()
	{
	    return (new ButtonObject[] {
	        m_boQuery, m_boPrint
	    }
     );
	   
	}
	public ReportBaseClass getReportBase()
	{
	    if(m_report == null)
	        try
	        {
	            m_report = new ReportBaseClass();
	            m_report.setName("ReportBase");
	            m_report.setTempletID(this.getCorpPrimaryKey(), "H0900405", null, null);
	      }
	        catch(Exception ex)
	        {
	            System.out.println("\u57FA\u7C7B:\u672A\u627E\u5230\u62A5\u8868\u6A21\u677F......");
	        }
	    return m_report;
	}

	public String getTitle() {
		return m_report.getReportTitle();
	}

	public void onButtonClicked(ButtonObject bo) {
		try
	    {
	        if(bo == m_boQuery){
	            onQuery();
	        }else if(bo == m_boPrint){
	            onPrint();
	        }
	    }
	    catch(BusinessException ex)
	    {
	        showErrorMessage(ex.getMessage());
	        ex.printStackTrace();
	    }
	    catch(Exception e)
	    {
	        showErrorMessage("\u672A\u77E5\u9519\u8BEF:" + e.getMessage());
	        e.printStackTrace();
	    }
	}
	
	public QueryConditionClient getQryDlg()
	{
	    if(m_qryDlg == null){
	        m_qryDlg = createQueryDLG();
	       
	    }
	    return m_qryDlg;
	}
	
	protected QueryConditionClient createQueryDLG()
	{
		QueryConditionClient dlg = new QueryConditionClient();
	    dlg.setTempletID(this.getCorpPrimaryKey(), "H0900405", null, null);
	    dlg.setNormalShow(false);
	    return dlg;
	}
	
	  //设置打印方法
	  public void onPrint() throws Exception{
	  		this.getReportBase().previewData(); 
	  }
      
      //查询方法
      public void onQuery() throws Exception{
          this.getReportBase().getBillModel().clearBodyData();
            String pk_corp = this.getCorpPrimaryKey();
           
            QueryConditionClient uidialog = getQryDlg();
			uidialog.setDefaultValue("today", ce.getDate().toString(), "");
			getReportBase().showZeroLikeNull(true);//add by houcq 2010-12-23设置0不显示
            getQryDlg().showModal();
            ArrayList list = new ArrayList();
            ReportVO[] rvos = null;  
            if(getQryDlg().getResult() == 1){
            	
                ConditionVO[] today  = getQryDlg().getConditionVOsByFieldCode("today"); //日期

                if(today==null||today.length==0){
					  showErrorMessage("请选择日期!");
					  return;
				  }
                
                UFDate dmakedate = new UFDate(today[0].getValue());	//选择日期
            	
                int year = dmakedate.getYear();
                int month = dmakedate.getMonth();

        		UFDate[] date6=getLastDate(dmakedate);//日期08.7.5
        		UFDate lastbegindate=date6[0]; 		//08.6.1
        		UFDate lastenddate=date6[1]; 		//08.6.5
        		UFDate calcbegindate=date6[2];		//08.7.1
        		UFDate lastyearbegindate=date6[3];	//07.7.1
        		UFDate lastyearenddate=date6[4];		//07.7.5
                
                   StringBuffer br = new StringBuffer();
                   br.append("  select  ");
                   br.append("  	t.pk_invbasdoc , z.invname , x.invclasscode , x.invclassname ,z.invcode , f.storname,z.invspec,z.colour,z.invtype,z.def_2,");
                   br.append("  	sum ( t.ycjc ) zrjc , sum ( t.rkbr ) drrk , sum ( t.rklj ) ljrk ,  ");
                   br.append("  	sum ( t.scbr ) drscck , sum ( t.lisc ) drljscck , ");
                   br.append("  sum(t.hb) drhbck, sum(t.ljhb) drhbljck,");
                   br.append("  	sum ( t.brqt ) drqtck ,  ");
                   br.append("  	sum ( t.ljqt ) drqtljck from (");
                   br.append("  select b.pk_invbasdoc pk_invbasdoc ,  "); //+lastenddate.getMonth()+期初数据
                   br.append("  sum ( isnull ( b.qmsl , 0 ) ) ycjc , 0 rkbr , 0 rklj , 0 scbr , 0 lisc ,0 hb, 0 ljhb, 0 brqt , 0 ljqt  ");
                   br.append("  from eh_calc_kcybb a , eh_calc_kcybb_b b  ");
                   br.append("  where a.pk_kcybb = b.pk_kcybb and a.invtype = 'Y' and a.nmonth = '"+lastenddate.getMonth()+"'  and isnull ( a.dr , 0 ) = 0 ");
                   br.append("  and isnull ( b.dr , 0 ) = 0  and a.pk_corp = '"+pk_corp+"'  group by b.pk_invbasdoc  ");
                   br.append("  union all ");//当日入库
                   br.append("  			select pk_invbasdoc ,0 ycjc,sum(a.rkbr+a.lsdbsl) rkbr, 0 rklj , 0 scbr , 0 lisc ,0 hb, 0 ljhb, 0 brqt , 0 ljqt   ");
                   br.append("  			from  (select b.pk_invbasdoc pk_invbasdoc , sum ( isnull ( inamount , 0 ) ) rkbr,");
                   br.append("  			0 lsdbsl  from eh_stock_in a , eh_stock_in_b b  where a.pk_in = b.pk_in ");
                   br.append("  			and a.dmakedate = '"+dmakedate.toString()+"' and isnull ( a.dr , 0 ) = 0  and isnull ( b.dr , 0 ) = 0");
                   br.append("  			 and a.vbillstatus = 1 and a.pk_corp = '"+pk_corp+"'  group by b.pk_invbasdoc  union all ");
                   br.append("  			 select b.pk_dr_inv pk_invbasdoc,0 rkbr, sum(isnull(b.drrmount,0)) lsdbsl ");
                   br.append("  			from eh_sc_dbd a,eh_sc_dbd_b b  where a.pk_dbd=b.pk_dbd  and  a.dmakedate='"+dmakedate.toString()+"' ");
                   br.append("  			and isnull(a.dr,0)=0 and isnull(b.dr,0)=0   and a.vbillstatus=1 and a.pk_corp='"+pk_corp+"' ");
                   br.append("  			and pk_intype='"+ISQLChange.INTYPE_DB+"' and dbtype=0 group by  b.pk_dr_inv) a ");
                   br.append("  			group by pk_invbasdoc  ");
                   br.append("  union all  ");//累计入库
                   br.append("  			 select pk_invbasdoc ,0 ycjc,0 rkbr,sum(a.rkbr+a.lsdbsl) rklj , 0 scbr , 0 lisc ,0 hb, 0 ljhb,  0 brqt ,");
                   br.append("  			 0 ljqt   from (select b.pk_invbasdoc pk_invbasdoc , sum ( isnull ( inamount , 0 ) ) rkbr,");
                   br.append("  			 0 lsdbsl  from eh_stock_in a , eh_stock_in_b b  where a.pk_in = b.pk_in ");
                   br.append("  			and a.dmakedate >= '"+calcbegindate.toString()+"' and  a.dmakedate< = '"+dmakedate.toString()+"' ");
                   br.append("  			and isnull ( a.dr , 0 ) = 0  and isnull ( b.dr , 0 ) = 0 and a.vbillstatus = 1 ");
                   br.append("  			and a.pk_corp = '"+pk_corp+"'  group by b.pk_invbasdoc  union all  ");
                   br.append("  			select b.pk_dr_inv pk_invbasdoc,0 rkbr, sum(isnull(b.drrmount,0)) lsdbsl ");
                   br.append("  			from eh_sc_dbd a,eh_sc_dbd_b b  where a.pk_dbd=b.pk_dbd  and  a.dmakedate>='"+calcbegindate.toString()+"' ");
                   br.append("  			and  a.dmakedate<='"+dmakedate.toString()+"' and isnull(a.dr,0)=0 and isnull(b.dr,0)=0   ");
                   br.append("  			and a.vbillstatus=1 and a.pk_corp='"+pk_corp+"' and pk_intype='"+ISQLChange.INTYPE_DB+"' ");
                   br.append("  			and dbtype=0 group by  b.pk_dr_inv) a group by pk_invbasdoc  ");
                   br.append("  union all ");//当日生产入库
                   br.append("  			select pk_invbasdoc, 0 ycjc,0 rkbr,0 rklj  ,sum(a.scbr+a.lsqt+a.lsdb) scbr , 0 lisc ,0 hb, 0 ljhb,  ");
                   br.append("  			0 brqt , 0 ljqt from  (select b.pk_invbasdoc pk_invbasdoc , ");
                   br.append("  			sum ( isnull ( blmount , 0 ) ) scbr ,0 lsqt,0 lsdb from eh_sc_ckd a , eh_sc_ckd_b b ");
                   br.append("  			where a.pk_ckd = b.pk_ckd  and a.dmakedate = '"+dmakedate.toString()+"' and isnull ( a.dr , 0 ) = 0  ");
                   br.append("  			and isnull ( b.dr , 0 ) = 0 and vbilltype = 'ZA46' and a.pk_corp = '"+pk_corp+"'  ");
                   br.append("  			and a.vbillstatus = 1 and  pk_outtype='"+ISQLChange.OUTTYPE_SC+"' group by b.pk_invbasdoc  ");
                   br.append("  			union all  select b.pk_invbasdoc pk_invbasdoc , 0 scbr ,sum ( isnull ( blmount , 0 ) ) lsqt,");
                   br.append("  			0 lsdb from eh_sc_ckd a , eh_sc_ckd_b b where a.pk_ckd = b.pk_ckd  ");
                   br.append("  			and a.dmakedate = '"+dmakedate.toString()+"' and isnull ( a.dr , 0 ) = 0  and isnull ( b.dr , 0 ) = 0 ");
                   br.append("  			and vbilltype = 'ZA49' and a.pk_corp = '"+pk_corp+"'  and a.vbillstatus = 1 ");
                   br.append("  			and  pk_outtype='"+ISQLChange.OUTTYPE_SC+"' group by b.pk_invbasdoc  ");
                   br.append("  			union all select b.pk_dr_inv pk_invbasdoc, 0 scbr ,0 lsqt,sum(isnull(b.drrmount,0)) lsdb ");
                   br.append("  			from eh_sc_dbd a,eh_sc_dbd_b b  where a.pk_dbd=b.pk_dbd  and  a.dmakedate='"+dmakedate.toString()+"' ");
                   br.append("  			and isnull(a.dr,0)=0 and isnull(b.dr,0)=0   and a.vbillstatus=1 and a.pk_corp='"+pk_corp+"' ");
                   br.append("  			and pk_outtype='"+ISQLChange.OUTTYPE_DB+"' and dbtype=0 group by  b.pk_dr_inv) a ");
                   br.append("  			group by pk_invbasdoc ");
                   br.append("  union all  ");//累计当日生产入库
                   br.append("  			select pk_invbasdoc, 0 ycjc,0 rkbr,0 rklj  ,0 scbr , sum ( a.scbr + a.lsqt + a.lsdb )  lisc ,0 hb, 0 ljhb, ");
                   br.append("  			0 brqt , 0 ljqt from  (select b.pk_invbasdoc pk_invbasdoc , ");
                   br.append("  			sum ( isnull ( blmount , 0 ) ) scbr ,0 lsqt,0 lsdb from eh_sc_ckd a , eh_sc_ckd_b b ");
                   br.append("  			where a.pk_ckd = b.pk_ckd  and a.dmakedate> = '"+calcbegindate.toString()+"'  ");
                   br.append("  			and a.dmakedate <= '"+dmakedate.toString()+"' and isnull ( a.dr , 0 ) = 0  and isnull ( b.dr , 0 ) = 0 ");
                   br.append("  			and vbilltype = 'ZA46' and a.pk_corp = '"+pk_corp+"'  and a.vbillstatus = 1 ");
                   br.append("  			and  pk_outtype='"+ISQLChange.OUTTYPE_SC+"' group by b.pk_invbasdoc  union all  ");
                   br.append("  			select b.pk_invbasdoc pk_invbasdoc , 0 scbr ,sum ( isnull ( blmount , 0 ) ) lsqt,0 lsdb ");
                   br.append("  			from eh_sc_ckd a , eh_sc_ckd_b b where a.pk_ckd = b.pk_ckd  and a.dmakedate >= '"+calcbegindate.toString()+"'  and a.dmakedate <= '"+dmakedate.toString()+"' ");
                   br.append("  			and isnull ( a.dr , 0 ) = 0  and isnull ( b.dr , 0 ) = 0 and vbilltype = 'ZA49' ");
                   br.append("  			and a.pk_corp = '"+pk_corp+"'  and a.vbillstatus = 1 and  pk_outtype='"+ISQLChange.OUTTYPE_SC+"' ");
                   br.append("  			group by b.pk_invbasdoc  union all select b.pk_dr_inv pk_invbasdoc, 0 scbr ,0 lsqt,");
                   br.append("  			sum(isnull(b.drrmount,0)) lsdb from eh_sc_dbd a,eh_sc_dbd_b b  where a.pk_dbd=b.pk_dbd  ");
                   br.append("  			and  a.dmakedate<='"+dmakedate.toString()+"' and  a.dmakedate>='"+calcbegindate.toString()+"' and isnull(a.dr,0)=0 ");
                   br.append("  			and isnull(b.dr,0)=0   and a.vbillstatus=1 and a.pk_corp='"+pk_corp+"' and ");
                   br.append("  			pk_outtype='"+ISQLChange.OUTTYPE_DB+"' and dbtype=0 group by  b.pk_dr_inv) a ");
                   br.append("  			group by pk_invbasdoc ");
                   br.append("  union all  ");//当日换包入库
                   br.append("  			select pk_invbasdoc, 0 ycjc,0 rkbr,0 rklj  ,0 scbr , 0 lisc ,sum(a.hbscbr+a.hblsqt+a.hblsdb) hb,0 ljhb, ");
                   br.append("  			0 brqt , 0 ljqt from  (select b.pk_invbasdoc pk_invbasdoc , ");
                   br.append("  			sum ( isnull ( blmount , 0 ) ) hbscbr ,0 hblsqt,0 hblsdb from eh_sc_ckd a , eh_sc_ckd_b b ");
                   br.append("  			where a.pk_ckd = b.pk_ckd  and a.dmakedate = '"+dmakedate.toString()+"' and isnull ( a.dr , 0 ) = 0  ");
                   br.append("  			and isnull ( b.dr , 0 ) = 0 and vbilltype = 'ZA46' and a.pk_corp = '"+pk_corp+"'  ");
                   br.append("  			and a.vbillstatus = 1 and  pk_outtype='"+ISQLChange.OUTTYPE_HB+"' group by b.pk_invbasdoc  ");
                   br.append("  			union all  select b.pk_invbasdoc pk_invbasdoc , 0 hbscbr ,sum ( isnull ( blmount , 0 ) ) hblsqt,");
                   br.append("  			0 hblsdb from eh_sc_ckd a , eh_sc_ckd_b b where a.pk_ckd = b.pk_ckd  ");
                   br.append("  			and a.dmakedate = '"+dmakedate.toString()+"' and isnull ( a.dr , 0 ) = 0  and isnull ( b.dr , 0 ) = 0 ");
                   br.append("  			and vbilltype = 'ZA49' and a.pk_corp = '"+pk_corp+"'  and a.vbillstatus = 1 ");
                   br.append("  			and  pk_outtype='"+ISQLChange.OUTTYPE_HB+"' group by b.pk_invbasdoc  ");
                   br.append("  			union all select b.pk_dr_inv pk_invbasdoc, 0 hbscbr ,0 hblsqt,sum(isnull(b.drrmount,0)) hblsdb ");
                   br.append("  			from eh_sc_dbd a,eh_sc_dbd_b b  where a.pk_dbd=b.pk_dbd  and  a.dmakedate='"+dmakedate.toString()+"' ");
                   br.append("  			and isnull(a.dr,0)=0 and isnull(b.dr,0)=0   and a.vbillstatus=1 and a.pk_corp='"+pk_corp+"' ");
                   br.append("  			and pk_outtype='"+ISQLChange.OUTTYPE_HB+"' and dbtype=0 group by  b.pk_dr_inv) a ");
                   br.append("  			group by pk_invbasdoc ");
                   br.append("  union all  ");//累计换包入库
                   br.append("  			select pk_invbasdoc, 0 ycjc,0 rkbr,0 rklj  ,0 scbr , 0 lisc ,0 hb,sum(a.hbscbr+a.hblsqt+a.hblsdb) ljhb, ");
                   br.append("  			0 brqt , 0 ljqt from  (select b.pk_invbasdoc pk_invbasdoc , ");
                   br.append("  			sum ( isnull ( blmount , 0 ) ) hbscbr ,0 hblsqt,0 hblsdb from eh_sc_ckd a , eh_sc_ckd_b b ");
                   br.append("  			where a.pk_ckd = b.pk_ckd  and a.dmakedate> = '"+calcbegindate.toString()+"'  ");
                   br.append("  			and a.dmakedate <= '"+dmakedate.toString()+"' and isnull ( a.dr , 0 ) = 0  and isnull ( b.dr , 0 ) = 0 ");
                   br.append("  			and vbilltype = 'ZA46' and a.pk_corp = '"+pk_corp+"'  and a.vbillstatus = 1 ");
                   br.append("  			and  pk_outtype='"+ISQLChange.OUTTYPE_HB+"' group by b.pk_invbasdoc  union all  ");
                   br.append("  			select b.pk_invbasdoc pk_invbasdoc , 0 hbscbr ,sum ( isnull ( blmount , 0 ) ) hblsqt,0 hblsdb ");
                   br.append("  			from eh_sc_ckd a , eh_sc_ckd_b b where a.pk_ckd = b.pk_ckd  and a.dmakedate >= '"+calcbegindate.toString()+"'and a.dmakedate <= '"+dmakedate.toString()+"'  ");
                   br.append("  			and isnull ( a.dr , 0 ) = 0  and isnull ( b.dr , 0 ) = 0 and vbilltype = 'ZA49' ");
                   br.append("  			and a.pk_corp = '"+pk_corp+"'  and a.vbillstatus = 1 and  pk_outtype='"+ISQLChange.OUTTYPE_HB+"' ");
                   br.append("  			group by b.pk_invbasdoc  union all select b.pk_dr_inv pk_invbasdoc, 0 hbscbr ,0 hblsqt,");
                   br.append("  			sum(isnull(b.drrmount,0)) hblsdb from eh_sc_dbd a,eh_sc_dbd_b b  where a.pk_dbd=b.pk_dbd  ");
                   br.append("  			and  a.dmakedate<='"+dmakedate.toString()+"' and  a.dmakedate>='"+calcbegindate.toString()+"' and isnull(a.dr,0)=0 ");
                   br.append("  			and isnull(b.dr,0)=0   and a.vbillstatus=1 and a.pk_corp='"+pk_corp+"' and ");
                   br.append("  			pk_outtype='"+ISQLChange.OUTTYPE_HB+"' and dbtype=0 group by  b.pk_dr_inv) a ");
                   br.append("  			group by pk_invbasdoc ");
                   br.append("  union all  ");//当日其他入库
                   br.append("  			select pk_invbasdoc , 0 ycjc,0 rkbr,0 rklj  ,0 scbr , 0 lisc  ,0 hb, 0 ljhb, ");
                   br.append("  			sum(a.scbr+a.lsqt+a.lsdb) brqt,0 ljqt from  (select b.pk_invbasdoc pk_invbasdoc , ");
                   br.append("  			sum ( isnull ( blmount , 0 ) ) scbr ,0 lsqt,0 lsdb from eh_sc_ckd a , eh_sc_ckd_b b ");
                   br.append("  			where a.pk_ckd = b.pk_ckd  and a.dmakedate = '"+dmakedate.toString()+"' and isnull ( a.dr , 0 ) = 0  ");
                   br.append("  			and isnull ( b.dr , 0 ) = 0 and vbilltype = 'ZA46' and a.pk_corp = '"+pk_corp+"'  ");
                   br.append("  			and a.vbillstatus = 1 and  (pk_outtype<>'"+ISQLChange.OUTTYPE_SC+"' and  pk_outtype<>'"+ISQLChange.OUTTYPE_HB+"' ) group by b.pk_invbasdoc  ");
                   br.append("  			union all  select b.pk_invbasdoc pk_invbasdoc , 0 scbr ,sum ( isnull ( blmount , 0 ) ) lsqt,");
                   br.append("  			0 lsdb from eh_sc_ckd a , eh_sc_ckd_b b where a.pk_ckd = b.pk_ckd  ");
                   br.append("  			and a.dmakedate = '"+dmakedate.toString()+"' and isnull ( a.dr , 0 ) = 0  and isnull ( b.dr , 0 ) = 0 ");
                   br.append("  			and vbilltype = 'ZA49' and a.pk_corp = '"+pk_corp+"'  and a.vbillstatus = 1 ");
                   br.append("  			and  (pk_outtype<>'"+ISQLChange.OUTTYPE_SC+"' and pk_outtype<>'"+ISQLChange.OUTTYPE_HB+"' ) group by b.pk_invbasdoc ");
                   br.append("  			 union all select 	b.pk_dr_inv pk_invbasdoc, 0 scbr ,0 lsqt,");
                   br.append("  			sum(isnull(b.drrmount,0)) lsdb from eh_sc_dbd a,eh_sc_dbd_b b  ");
                   br.append("  			where a.pk_dbd=b.pk_dbd  and  a.dmakedate='"+dmakedate.toString()+"' and isnull(a.dr,0)=0 ");
                   br.append("  			and isnull(b.dr,0)=0   and a.vbillstatus=1 and a.pk_corp='"+pk_corp+"' ");
                   br.append("  			and (pk_outtype<>'"+ISQLChange.OUTTYPE_DB+"' and pk_outtype<>'"+ISQLChange.OUTTYPE_HB+"' ) and dbtype=0 group by  b.pk_dr_inv) a ");
                   br.append("  			group by pk_invbasdoc ");
                   br.append("  union all  ");//累计其他入库
                   br.append("  			select pk_invbasdoc , 0 ycjc,0 rkbr,0 rklj  ,0 scbr , 0 lisc  ,0 hb, 0 ljhb,  0 brqt,");
                   br.append("  			sum(a.scbr+a.lsqt+a.lsdb) ljqt from (select b.pk_invbasdoc pk_invbasdoc , ");
                   br.append("  			sum ( isnull ( blmount , 0 ) ) scbr ,0 lsqt,0 lsdb from eh_sc_ckd a , eh_sc_ckd_b b ");
                   br.append("  			where a.pk_ckd = b.pk_ckd  and a.dmakedate <= '"+dmakedate.toString()+"' and a.dmakedate >= '"+calcbegindate.toString()+"' ");
                   br.append("  			and isnull ( a.dr , 0 ) = 0  and isnull ( b.dr , 0 ) = 0 and vbilltype = 'ZA46' ");
                   br.append("  			and a.pk_corp = '"+pk_corp+"' 	and a.vbillstatus = 1 and  (pk_outtype<>'"+ISQLChange.OUTTYPE_SC+"' and pk_outtype<>'"+ISQLChange.OUTTYPE_HB+"' )");
                   br.append("  			group by b.pk_invbasdoc 	union all 	select b.pk_invbasdoc pk_invbasdoc ,	0 scbr ,");
                   br.append("  			sum ( isnull ( blmount , 0 ) ) lsqt,0 lsdb	from eh_sc_ckd a , eh_sc_ckd_b b ");
                   br.append("  			where a.pk_ckd = b.pk_ckd 	and a.dmakedate <= '"+dmakedate.toString()+"'and a.dmakedate >= '"+calcbegindate.toString()+"'");
                   br.append("  			 and isnull ( a.dr , 0 ) = 0 	and isnull ( b.dr , 0 ) = 0 and vbilltype = 'ZA49' ");
                   br.append("  			and a.pk_corp = '"+pk_corp+"' 	and a.vbillstatus = 1 and  (pk_outtype<>'"+ISQLChange.OUTTYPE_SC+"' and pk_outtype<>'"+ISQLChange.OUTTYPE_HB+"' ) ");
                   br.append("  			group by b.pk_invbasdoc 	union all	select b.pk_dr_inv pk_invbasdoc,	0 scbr ,0 lsqt,");
                   br.append("  			sum(isnull(b.drrmount,0)) lsdb	from	eh_sc_dbd a,eh_sc_dbd_b b 	");
                   br.append("  			where	a.pk_dbd=b.pk_dbd  and  a.dmakedate<='"+dmakedate.toString()+"'and a.dmakedate >= '"+calcbegindate.toString()+"'");
                   br.append("  			and isnull(a.dr,0)=0 and isnull(b.dr,0)=0  	and a.vbillstatus=1 and a.pk_corp='"+pk_corp+"' and ");
                   br.append("  			(pk_outtype<>'"+ISQLChange.OUTTYPE_DB+"' and pk_outtype<>'"+ISQLChange.OUTTYPE_HB+"' ) and dbtype=0	group by 	b.pk_dr_inv) a ");
                   br.append("  			group by pk_invbasdoc");
                   br.append("  ) t, eh_invbasdoc z , bd_invcl x ,eh_stordoc f where isnull(z.lock_flag,'N')='N' ");
                   br.append("  and  t.pk_invbasdoc = z.pk_invbasdoc and z.pk_invcl = x.pk_invcl and f.pk_stordoc=z.warehouseid ");
                   br.append("  and substring ( z.invcode , 1 , 3 ) = '008' ");
                   br.append("  group by t.pk_invbasdoc , z.invname ,z.invspec,x.invclasscode , x.invclassname,z.invcode,f.storname,z.colour,z.invtype,z.def_2");
                   br.append("  order by z.invcode desc");

                   IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());                    
                   ArrayList all = (ArrayList) iUAPQueryBS.executeQuery(br.toString(), new MapListProcessor());
                   if(all!=null && all.size()>0){
                       for(int i=0;i<all.size();i++){
                           HashMap hm = (HashMap)all.get(i);
                           ReportVO rvo = new ReportVO();
                           String invname = hm.get("invname")==null?"":hm.get("invname").toString();
                           String invspec = hm.get("invspec")==null?"":hm.get("invspec").toString();
                           String invcode=hm.get("invcode")==null?"":hm.get("invcode").toString();
                           String colour=hm.get("colour")==null?"":hm.get("colour").toString();
                           String invtype=hm.get("invtype")==null?"":hm.get("invtype").toString();
                           String brandname=hm.get("def_2")==null?"":hm.get("def_2").toString();
                           UFDouble zrjc = new UFDouble(hm.get("zrjc")==null?"0":hm.get("zrjc").toString());
                           UFDouble drrk = new UFDouble(hm.get("drrk")==null?"0":hm.get("drrk").toString());
                           UFDouble ljrk = new UFDouble(hm.get("ljrk")==null?"0":hm.get("ljrk").toString());
                           UFDouble drscck = new UFDouble(hm.get("drscck")==null?"0":hm.get("drscck").toString());
                           UFDouble drljscck = new UFDouble(hm.get("drljscck")==null?"0":hm.get("drljscck").toString());
                           UFDouble drhbck = new UFDouble(hm.get("drhbck")==null?"0":hm.get("drhbck").toString());
                           UFDouble drhbljck = new UFDouble(hm.get("drhbljck")==null?"0":hm.get("drhbljck").toString());
                           UFDouble drqtck = new UFDouble(hm.get("drqtck")==null?"0":hm.get("drqtck").toString());
                           UFDouble drqtljck = new UFDouble(hm.get("drqtljck")==null?"0":hm.get("drqtljck").toString());
                           rvo.setDrhbck(new UFDouble(drhbck.toString(),0));
                           rvo.setDrhbljck(new UFDouble(drhbljck.toString(),0));
                           rvo.setDrljscck(new UFDouble(drljscck.toString(),0));
                           rvo.setColour(colour);
                           rvo.setBrand(brandname);
                           rvo.setInvtype(invtype);
                           rvo.setDrqtck(new UFDouble(drqtck.toString(),0));
                           rvo.setDrqtljck(new UFDouble(drqtljck.toString(),0));
                           rvo.setZrjc(new UFDouble(zrjc.toString(),0));
                           rvo.setInvname(invname);
                           rvo.setDrscck(new UFDouble(drscck.toString(),0));
                           rvo.setLjrk(new UFDouble(ljrk.toString(),0));
                           rvo.setDrrk(new UFDouble(drrk.toString(),0));
                           rvo.setInvspec(invspec);
                           rvo.setDrscs(new UFDouble((zrjc.add(ljrk).sub(drljscck).sub(drhbljck).sub(drqtljck)).toString(),0));
                           rvo.setInvcode(invcode);
                           list.add(rvo);
                       }
                   }
                   if(list.size()>0){
                       rvos = (ReportVO[]) list.toArray(new ReportVO[0]);
                   }
                   if(rvos!=null && rvos.length>0){
                       this.getReportBase().setBodyDataVO(rvos);
                       
                    /*显示合计项*/
                       String[] strValKeys = {"zrjc","drrk","ljrk","drscck","drljscck","drhbck","drhbljck","drqtck","drqtljck","drscs"};
                       SubtotalContext stctx = new SubtotalContext();
                       stctx.setSubtotalCols(strValKeys);  //配置要进行合计的字段
                       stctx.setTotalNameColKeys("invname");  //设置合计项显示列位置
                       stctx.setSumtotalName("合计");    //设置合计项显示名称
                       this.getReportBase().setSubtotalContext(stctx);
                       this.getReportBase().subtotal();
                        
                       this.getReportBase().execHeadLoadFormulas();
                       this.getReportBase().execTailLoadFormulas();
                    updateUI();
                   }
                   else{
                       this.showErrorMessage("无满足条件的报表数据!请重新查询!!");
                   }
            }
            

           }
      
      public static UFDate[] getLastDate(UFDate calcDate){
 		 UFDate[] date = new UFDate[5];
 		 UFDate lastbegindate = null;
 		 UFDate lastenddate = null;
 		 UFDate calcbegindate = null;
 		 DecimalFormat df = new DecimalFormat("00"); 
 		 int nyear = calcDate.getYear();
          int nmonth = calcDate.getMonth();
          String strmonth = df.format(nmonth);
          String day = df.format(calcDate.getDay());
          //1.取上月的期末数量
          int pyear = nyear,pmonth =0;
          if(nmonth==1){
              pmonth=12;
              pyear = nyear - 1;
          }else
              pmonth = nmonth - 1;
          String strpmonth = df.format(pmonth);
          calcbegindate = new UFDate(nyear+"-"+strmonth+"-"+"01");
          lastbegindate = new UFDate(pyear+"-"+strpmonth+"-"+"01");
          int days = lastbegindate.getDaysMonth();
          if(calcDate.getDaysMonth()!=Integer.valueOf(day)){
         	 days =  Integer.valueOf(day); 
         	 if(lastbegindate.getMonth()==2&&Integer.valueOf(day)>=lastbegindate.getDaysMonth()){  //与上个月是2月时特殊处理
         		 days = lastbegindate.getDaysMonth();
         	 }
          }
          lastenddate = new UFDate(pyear+"-"+strpmonth+"-"+days);
          UFDate lastyearbegindate=new UFDate((nyear-1)+"-"+strmonth+"-"+"01");
          UFDate lastyearenddate=new UFDate((nyear-1)+"-"+strmonth+"-"+day);
          date[0] = lastbegindate;
          date[1] = lastenddate;
          date[2] = calcbegindate;
          date[3]=lastyearbegindate;
          date[4]=lastyearenddate;
          
 		 return date;
 	 }

}
