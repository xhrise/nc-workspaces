package nc.ui.eh.report.h0900310;

import java.awt.BorderLayout;
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
import nc.vo.eh.report.h0900310.InvnamepriceVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.report.SubtotalContext;

/**
 * 
功能：产品牌价表
作者：zqy
日期：2008-12-16 下午07:15:56
 */

@SuppressWarnings("serial")
public class ReportUI extends ToftPanel {
	
	public ButtonObject m_boQuery;
    public ButtonObject m_boPrint;
    private ReportBaseClass m_report;
    private QueryConditionClient m_qryDlg;
    private ClientEnvironment ce=ClientEnvironment.getInstance();
    
	@SuppressWarnings("deprecation")
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
	    getReportBase().setNotSortCols(st);
	}

	public ButtonObject[] getBtnAry()
	{
	    return (new ButtonObject[] {
	        m_boQuery, m_boPrint
	    });
	   
	}
	public ReportBaseClass getReportBase()
	{
	    if(m_report == null)
	        try
	        {
	            m_report = new ReportBaseClass();
	            m_report.setName("ReportBase");
	            m_report.setTempletID(this.getCorpPrimaryKey(), "H0900310", null, null);
	      }
	        catch(Exception ex)
	        {
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
	    dlg.setTempletID(this.getCorpPrimaryKey(), "H0900310", null, null);
	    
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
			@SuppressWarnings("unused")
            QueryConditionClient uidialog = getQryDlg();
		    getQryDlg().showModal();
		    getReportBase().showZeroLikeNull(true);//add by houcq 2010-12-23设置0不显示
            String invtypy = null;          //物料分类PK
            String invclasscode = null;     //物料分类编码
            String pk_corp = ce.getCorporation().getPk_corp();
            HashMap hmbrand = Getbrand();
            HashMap hmxs = Getxs(pk_corp);
            HashMap hminvclasscode = Getinv();
            HashMap hminvname = new HashMap();//Getinvname();
            InvnamepriceVO[] VO = null;
            ArrayList list = new ArrayList();
            StringBuffer sql = new StringBuffer();
            IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		    if(getQryDlg().getResult() == 1){
                ConditionVO[] invtype  = getQryDlg().getConditionVOsByFieldCode("invtype");  //取得查询模板中的物料分类
                if(invtype.length>0){
                    invtypy = invtype[0].getValue()==null?"":invtype[0].getValue().toString();
                    invclasscode = hminvclasscode.get(invtypy)==null?"":hminvclasscode.get(invtypy).toString();
                    String subcode = invclasscode.substring(0, 2);
                    if(!"01".equals(subcode))                   
                    {
                    	this.showErrorMessage("请选择01开头的饲料产品查询");
                    	return;
                    }
                }else{
                	invclasscode = "01";//没有选择物料分类默认为01下的所有物料。时间：2010-01-20
                }
                hminvname = Getinvname(invclasscode);
                sql.append(" select cc.pk_invmandoc pk_invbasdoc, ");
                sql.append(" c.invcode, ");
                sql.append(" c.invname, ");
                sql.append(" c.def6 def_1, ");
                sql.append(" c.invpinpai brand, ");
                sql.append(" c.invspec, ");
                sql.append(" c.invtype, ");
                sql.append(" c.def1 colour, ");
                sql.append(" ccc.newprice price ");
                sql.append(" from bd_invbasdoc c, bd_invmandoc cc, ");
                sql.append(" (select * from (select rank() over(partition by pk_invbasdoc order by dapprovetime desc) rk,aaaa.* from");
                sql.append(" (select to_date(nvl(a.dapprovetime,a.dapprovedate),'yyyy-mm-dd:hh24:mi:ss') dapprovetime,b.pk_invbasdoc,b.newprice ");
                sql.append(" from eh_price a , eh_price_b b");
                sql.append(" where a.pk_price=b.pk_price");
                sql.append(" and a.vbillstatus=1");
                sql.append(" and nvl(a.dr,0)=0 ");
                sql.append(" and nvl(b.dr,0)=0");
                sql.append("  and a.pk_corp='"+pk_corp+"'");
                sql.append(" and '"+ce.getDate().toString()+"' between a.zxdate and a.yxdate)aaaa) t");
                sql.append(" where t.rk<2) ccc"); 
                sql.append(" where c.invcode like '"+invclasscode+"%' ");
                sql.append(" and c.pk_invbasdoc = cc.pk_invbasdoc ");
                sql.append(" and cc.pk_corp = '"+pk_corp+"'");
                sql.append(" and nvl(cc.dr, 0) = 0 ");
                sql.append(" and cc.pk_invmandoc=ccc.pk_invbasdoc(+) ");
                String pk_invbasdoc = null;     //物料PK
                String invtypename = null;      //物料分类名称
                String invcode = null;          //物料编码
                String invname = null;          //物料名称
                String dbhl = null;             //蛋白含量
                String brandname = null;        //品牌
                String invspec = null;          //规格
                String invtype2 = null;         //型号
                String colour = null;           //颜色
                UFDouble price = null;          //最新的牌价
                UFDouble packprice = null;      //袋价
                UFDouble changerate = null;     //换算率
                ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(),new MapListProcessor());
                if(arr!=null && arr.size()>0){
                    for(int i=0;i<arr.size();i++){
                        HashMap hm = (HashMap)arr.get(i);
                        pk_invbasdoc = hm.get("pk_invbasdoc")==null?"":hm.get("pk_invbasdoc").toString();
                        invtypename = hminvname.get(pk_invbasdoc)==null?"":hminvname.get(pk_invbasdoc).toString();
                        invcode = hm.get("invcode")==null?"":hm.get("invcode").toString();
                        invname = hm.get("invname")==null?"":hm.get("invname").toString();
                        dbhl = hm.get("def_1")==null?"":hm.get("def_1").toString();
                        String brand = hm.get("brand")==null?"":hm.get("brand").toString();
                        brandname = hmbrand.get(brand)==null?"":hmbrand.get(brand).toString();
                        invspec = hm.get("invspec")==null?"":hm.get("invspec").toString();
                        invtype2 = hm.get("invtype")==null?"":hm.get("invtype").toString();
                        colour = hm.get("colour")==null?"":hm.get("colour").toString(); 
                        price = new UFDouble(hm.get("price")==null?"0":hm.get("price").toString());
                        changerate = new UFDouble(hmxs.get(pk_invbasdoc)==null?"0":hmxs.get(pk_invbasdoc).toString());
                        packprice = price.multiply(changerate);//功能：与系数的涣散修改时间：2009-12-30
                        
                        InvnamepriceVO vo = new InvnamepriceVO();
                        vo.setInvtype(invtypename);
                        vo.setInvcode(invcode);
                        vo.setInvname(invname);
                        vo.setDbhl(dbhl);
                        vo.setInvspec(invspec);
                        vo.setInvtypy(invtype2);
                        vo.setBrandname(brandname);
                        vo.setColour(colour);
                        vo.setPrice(price);
                        vo.setPackprice(packprice);
                        
                        list.add(vo);
                    }
                }
                if(list!=null && list.size()>0){
                    VO = (InvnamepriceVO[]) list.toArray(new InvnamepriceVO[list.size()]);
                }
                if(VO!=null && VO.length>0){
                    this.getReportBase().setHeadItem("zdname", ce.getUser().getUserName());
                    this.getReportBase().setHeadItem("zddate", ce.getDate().toString());
                    this.getReportBase().setBodyDataVO(VO);
                    /*显示合计项*/
                    String[] strValKeys = {"price","packprice"};
                    SubtotalContext stctx = new SubtotalContext();
                    stctx.setSubtotalCols(strValKeys);  //配置要进行合计的字段
                    stctx.setTotalNameColKeys("invtype");  //设置合计项显示列位置
                    stctx.setSumtotalName("合计");    //设置合计项显示名称
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
      
   
    /**
     * 
    功能：取NC系统品牌的PK与品牌名称
    作者：zqy
    日期：2008-12-16 下午07:56:38
    @return
    @throws BusinessException
     */
      @SuppressWarnings("unchecked")
    public static HashMap Getbrand() throws BusinessException{
          HashMap hmbrand = new HashMap();
          IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
          StringBuffer sql = new StringBuffer()
          .append(" select pk_brand,brandname from eh_brand where isnull(dr,0)=0 ");
          ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(),new MapListProcessor());
          if(arr!=null && arr.size()>0){
              String pk_brand = null;          //品牌PK
              String brandname = null;         //品牌名称
              for(int i=0;i<arr.size();i++){
                  HashMap hm = (HashMap)arr.get(i);
                  pk_brand = hm.get("pk_brand")==null?"":hm.get("pk_brand").toString();
                  brandname = hm.get("brandname")==null?"":hm.get("brandname").toString();
                  hmbrand.put(pk_brand,brandname);
              }
          }
        return hmbrand;
      }
      
   /**
    * 
   功能：取得主辅计量单位直接的换算系数
   作者：zqy
   日期：2008-12-16 下午08:03:01
   @return
   @throws BusinessException
    */
      @SuppressWarnings("unchecked")
    public static HashMap Getxs(String pk_corp) throws BusinessException{
          HashMap hmxs = new HashMap();
          
          IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
          StringBuffer sql = new StringBuffer()
          .append(" select ii.pk_invmandoc,c.mainmeasrate from bd_invbasdoc i, ")
          .append(" bd_invmandoc ii, ")
          .append(" bd_convert c ")
          .append(" where nvl(ii.dr,0)=0 and nvl(ii.dr,0) = 0 ")
          .append(" and i.pk_invbasdoc=ii.pk_invbasdoc ")
          .append(" and c.pk_invbasdoc=i.pk_invbasdoc ")
          .append(" and ii.pk_corp = '"+pk_corp+"' ");
          ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(),new MapListProcessor());
          if(arr!=null && arr.size()>0){
              String pk_invbasdoc = null;          //品牌PK
              UFDouble changerate = null;         //品牌名称
              for(int i=0;i<arr.size();i++){
                  HashMap hm = (HashMap)arr.get(i);
                  pk_invbasdoc = hm.get("pk_invmandoc")==null?"":hm.get("pk_invmandoc").toString();
                  changerate = new UFDouble(hm.get("mainmeasrate")==null?"0":hm.get("mainmeasrate").toString());
                  hmxs.put(pk_invbasdoc,changerate);
              }
          }
        return hmxs;
      }
      
      /**
       * 功能：
       * <p>
       * 取得所有类别
       * </p>
       */
      @SuppressWarnings("unchecked")
    public static HashMap Getinv() throws BusinessException{
          HashMap Getinv = new HashMap();
          IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
          StringBuffer sql = new StringBuffer()
          .append(" select pk_invcl ,invclasscode from bd_invcl where isnull(dr,0)=0 ");
          ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(),new MapListProcessor());
          if(arr!=null && arr.size()>0){
              String pk_invcl = null;             //物料分类PK
              String invclasscode = null;         //物料分类编码
              for(int i=0;i<arr.size();i++){
                  HashMap hm = (HashMap)arr.get(i);
                  pk_invcl = hm.get("pk_invcl")==null?"":hm.get("pk_invcl").toString();
                  invclasscode = hm.get("invclasscode")==null?"0":hm.get("invclasscode").toString();
                  Getinv.put(pk_invcl,invclasscode);
              }
          }
        return Getinv;
      }
      
      /**
       * 根据物料PK取得对应的物料分类(一类)
       * @author 
       功能：
       作者：zqy
       日期：2009-2-20 下午04:58:22
       * @return
       * @throws BusinessException
       */
      @SuppressWarnings("unchecked")
     public HashMap Getinvname(String invtype) throws BusinessException{
          HashMap hminvname = new HashMap();
          IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
          StringBuffer invnamesql = new StringBuffer()
          .append(" select aa.pk_invmandoc, aa3.invclassname invname  ")
          .append("   from bd_invbasdoc  a, ")
          .append("        bd_invmandoc  aa, ")
          .append("        eh_view_invcl aa1, ")
          .append("        eh_view_invcl aa2, ")
          .append("        eh_view_invcl aa3 ")
          .append("  where a.pk_invcl = aa1.pk_invcl ")
          .append("    and a.pk_invbasdoc = aa.pk_invbasdoc ")
          .append("    and aa1.pk_father = aa2.pk_invcl ")
          .append("    and aa2.pk_father = aa3.pk_invcl ")
          .append("    and a.invcode like '"+invtype+"%' ")
          .append("    and nvl(aa.dr, 0) = 0 ")
          .append("    and aa.pk_corp='"+ce.getCorporation().getPk_corp()+"' ");
          ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(invnamesql.toString(), new MapListProcessor());
          if(arr!=null && arr.size()>0){
              String pk_invbasdoc = null;
              String invname = null;
              for(int i=0;i<arr.size();i++){
                  HashMap hm = (HashMap)arr.get(i);
                  pk_invbasdoc = hm.get("pk_invmandoc")==null?"":hm.get("pk_invmandoc").toString();
                  invname = hm.get("invname")==null?"":hm.get("invname").toString();
                  hminvname.put(pk_invbasdoc, invname);
              }
          }
        return hminvname;
      }
      
}
