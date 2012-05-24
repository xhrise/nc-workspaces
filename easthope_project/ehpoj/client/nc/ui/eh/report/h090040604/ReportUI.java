package nc.ui.eh.report.h090040604;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.pub.report.ReportBaseClass;
import nc.vo.eh.report.h090040604.CheckylVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.report.SubtotalContext;

/**
 * 
 * @author 
 功能：原料质量报表
 作者：zqy
 日期：2009-1-20 下午02:32:00
 */
 
public class ReportUI extends ToftPanel {
	
	public ButtonObject m_boQuery;
    public ButtonObject m_boPrint;
    private ReportBaseClass m_report;
    private QueryConditionClient m_qryDlg;
    private ClientEnvironment ce=ClientEnvironment.getInstance();
    UIRefPane ref = null;
    
	public ReportUI() {
		super();
		m_boQuery = new ButtonObject("查询", "查询报表", 0);
        m_boPrint = new ButtonObject("打印", "打印报表", 0);
        initialize(); 
	}

	
	private void initialize(){
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
	    getReportBase().setNotSortCols(st);
	}

	public ButtonObject[] getBtnAry(){
	    return (new ButtonObject[] {
	        m_boQuery, m_boPrint
	    });
	}
    
	public ReportBaseClass getReportBase(){
	    if(m_report == null)
	        try{
	            m_report = new ReportBaseClass();
	            m_report.setName("ReportBase");
	            m_report.setTempletID(this.getCorpPrimaryKey(), "H090040604", null, null);
	        }
	        catch(Exception ex){
	            System.out.println("\u57FA\u7C7B:\u672A\u627E\u5230\u62A5\u8868\u6A21\u677F......");
	        }
	    return m_report;
	}
    
	@Override
	public String getTitle() {
		return m_report.getReportTitle();
	}

	@Override
	public void onButtonClicked(ButtonObject bo) {
		try{
	        if(bo == m_boQuery){
	            onQuery();
	        }else if(bo == m_boPrint){
	            onPrint();
	        }
	    }
	    catch(BusinessException ex){
	        showErrorMessage(ex.getMessage());
	        ex.printStackTrace();
	    }
	    catch(Exception e){
	        showErrorMessage("\u672A\u77E5\u9519\u8BEF:" + e.getMessage());
	        e.printStackTrace();
	    }
	}
	
	public QueryConditionClient getQryDlg(){
	    if(m_qryDlg == null){
	        m_qryDlg = createQueryDLG();
	    }
	    return m_qryDlg;
	}
	
	protected QueryConditionClient createQueryDLG(){
		QueryConditionClient dlg = new QueryConditionClient();
	    dlg.setTempletID(this.getCorpPrimaryKey(), "H090040604", null, null);
        String nowday = ce.getDate().toString();
        String nowdate = nowday.substring(0, 7);
        String endday = ""+nowdate+"-01";
        dlg.setDefaultValue("startdate", endday, "");
        dlg.setDefaultValue("enddate", nowday, "");
	    //结束开始日期
	    dlg.setNormalShow(false);
	    return dlg;
	}
	
	//设置打印方法
	public void onPrint() throws Exception{
	  	this.getReportBase().previewData(); 
	 }
      
	  //查询方法
	  @SuppressWarnings("unchecked")
    public void onQuery() throws Exception{
	  		this.getReportBase().getBillModel().clearBodyData();
		    getQryDlg().showModal();
		    getReportBase().showZeroLikeNull(true);//add by houcq 2010-12-23设置0不显示
		    if(getQryDlg().getResult() == 1){
                ConditionVO[] start_date  = getQryDlg().getConditionVOsByFieldCode("startdate");       //查询开始日期
                ConditionVO[] end_date  = getQryDlg().getConditionVOsByFieldCode("enddate");           //查询结束日期
                ConditionVO[] pkinvcl  = getQryDlg().getConditionVOsByFieldCode("pk_invcl");   //原料名称
                UFDate startdate = null;
                UFDate enddate = null;
                if(start_date!=null && start_date.length>0){
                    startdate = new UFDate(start_date[0].getValue()==null?"":start_date[0].getValue().toString());
                }
                if(end_date!=null && end_date.length>0){
                    enddate = new UFDate(end_date[0].getValue()==null?"":end_date[0].getValue().toString());
                }
                
                ArrayList pklist = new ArrayList();
                String invclasscode = null;
                String pk_invbasdoc = null;
                IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
                String pk_corp = ce.getCorporation().getPk_corp();
                if(pkinvcl!=null && pkinvcl.length>0){
                    for(int i=0;i<pkinvcl.length;i++){
                        String pk_inv = pkinvcl[i].getValue()==null?"":pkinvcl[i].getValue().toString();
                        StringBuffer sql = new StringBuffer()
                        .append(" select invclasscode from bd_invcl ")
                        .append(" where pk_invcl='"+pk_inv+"'  and isnull(dr,0)=0 ");
                        Vector arr = (Vector)iUAPQueryBS.executeQuery(sql.toString(),new VectorProcessor());
                        if(arr!=null && arr.size()>0){
                            Vector ve = (Vector)arr.get(0);
                            invclasscode = ve.get(0)==null?"":ve.get(0).toString();
                            ArrayList all = Getpkinvbasdoc(invclasscode,pk_corp);
                            if(all!=null && all.size()>0){
                                for(int j=0;j<all.size();j++){
                                    pk_invbasdoc = all.get(j)==null?"":all.get(j).toString();
                                    pklist.add(pk_invbasdoc);
                                }
                            }
                        }
                    }
                }else{
                    StringBuffer sql = new StringBuffer()
//                    .append(" SELECT a.pk_invbasdoc  ")
//                    .append(" FROM eh_invbasdoc a ,bd_invcl b ")
//                    .append(" WHERE a.pk_invcl = b.pk_invcl AND SUBSTRING(b.invclasscode,1,3) ")
//                    .append(" NOT IN ('008','009','010','011','012','013','014', ")
//                    .append(" '015','016','017','018','019','020','021','001') ")
//                    .append(" AND a.pk_corp='"+pk_corp+"' AND b.pk_corp='"+pk_corp+"' ")
//                    .append(" AND ISNULL(a.dr,0)=0 AND ISNULL(b.dr,0)=0 ");
                    
                    /**将
                     * 001-饲料类,008-包装材料类,009-标签合格证类,010-能源类,011-化验设备
                     * 012-化验试剂,013-电器配件,014-水暖备件,015-机械备件,016-机器备件,017-工具
                     * 018-办公用品,019-劳保用品类,020-家俱类,021-家用电器类
                     * 转换成
                     * 0101-产品、原料及燃料类中剔除010101-饲料类
                     */
                    .append(" SELECT aa.pk_invmandoc pk_invbasdoc ")
                    .append("   FROM bd_invbasdoc a, bd_invmandoc aa, bd_invcl b ")
                    .append("  WHERE a.pk_invcl = b.pk_invcl ")
                    .append("    and a.pk_invbasdoc = aa.pk_invbasdoc ")
                    //<修改>功能:存货分类用系统自带编码,时间:2009-09-21,作者:张志远
//                    .append("    AND SUBSTR(b.invclasscode, 1, 3) NOT IN ")
//                    .append("        ('008', '009', '010', '011', '012', '013', '014', '015', '016', '017', ")
//                    .append("         '018', '019', '020', '021', '001') ")
                    .append(" AND SUBSTR(b.invclasscode, 1, 2)='01' ")
                    .append("    AND aa.pk_corp = '"+pk_corp+"' ")
                    //.append("    AND b.pk_corp = '"+pk_corp+"' ")
                    .append("    AND nvl(aa.dr, 0) = 0 ")
                    .append("    AND nvl(b.dr, 0) = 0; ");

                    ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(),new MapListProcessor());
                    if(arr!=null && arr.size()>0){
                        for(int i=0;i<arr.size();i++){
                            HashMap hm = (HashMap)arr.get(i);
                            pk_invbasdoc = hm.get("pk_invbasdoc")==null?"":hm.get("pk_invbasdoc").toString();
                            pklist.add(pk_invbasdoc);
                        }
                    }else{
                        pklist.add("");
                    }
                }
                
                String allpkinvbasdoc = Getarraypk(pklist);
                String zdname = ce.getUser().getUserName();
                String zddate = ce.getDate().toString();
                CheckylVO cvo = new CheckylVO();
                cvo.setPk_corp(pk_corp);
                cvo.setStartdate(startdate);
                cvo.setEnddate(enddate);
                cvo.setPk_invcl(allpkinvbasdoc);
                
                PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
                CheckylVO[] CVO = pubitf.Checkyl(cvo);
                if(CVO!=null && CVO.length>0){
                    this.getReportBase().setHeadItem("zddate", zddate);
                    this.getReportBase().setHeadItem("zdname", zdname);
                    this.getReportBase().setBodyDataVO(CVO);
                    
                    /*显示合计项*/
                    String[] strValKeys = {"sumamount","lhamount","lhwamount","bpamount"};
                    SubtotalContext stctx = new SubtotalContext();
                    stctx.setSubtotalCols(strValKeys);          //配置要进行合计的字段
                    stctx.setTotalNameColKeys("invname");       //设置合计项显示列位置
                    stctx.setSumtotalName("合计");              //设置合计项显示名称
                    this.getReportBase().setSubtotalContext(stctx);
                    this.getReportBase().subtotal();
                     
                    this.getReportBase().execHeadLoadFormulas();
                    this.getReportBase().execTailLoadFormulas();
                    updateUI();
                }else{
                    this.showErrorMessage("无满足条件的报表数据!请重新查询!!");
                }   
            }
	  }
      
      public static String Getarraypk(ArrayList array){
          StringBuffer arraypk = new StringBuffer();                 
          for (int i = 0; i < array.size(); i++) {
              arraypk.append("'");
              arraypk.append(array.get(i));
              arraypk.append("'");
              if ((i + 1) < array.size()) {
                  arraypk.append(",");
              } else {
                  arraypk.append("");
              }
          }          
          return arraypk.toString();
      }
   
      @SuppressWarnings("unchecked")
    public ArrayList Getpkinvbasdoc(String invclasscode ,String pk_corp ) throws BusinessException{
          IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
          ArrayList arr = new ArrayList();
          StringBuffer sql = new StringBuffer()
//          .append(" select pk_invbasdoc from eh_invbasdoc where invcode like '"+invclasscode+"%' ")
//          .append(" and pk_corp='"+pk_corp+"' and isnull(lock_flag,'N')='N' and isnull(dr,0)=0 ");
          .append(" select aa.pk_invmandoc pk_invbasdoc ")
          .append("   from bd_invbasdoc a, bd_invmandoc aa ")
          .append("  where a.invcode like '"+invclasscode+"%' ")
          .append("    and aa.pk_invbasdoc = a.pk_invbasdoc ")
          .append("    and aa.pk_corp = '"+pk_corp+"' ")
          .append("    and nvl(aa.dr, 0) = 0 ");

          
          ArrayList all = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(),new MapListProcessor());
          if(all!=null && all.size()>0){
              String pk_invbasdoc = null;
              for(int i=0;i<all.size();i++){
                  HashMap hm = (HashMap)all.get(i);
                  pk_invbasdoc = hm.get("pk_invbasdoc")==null?"":hm.get("pk_invbasdoc").toString();
                  arr.add(pk_invbasdoc);
              }
          }
        return arr;
      }
      
}
