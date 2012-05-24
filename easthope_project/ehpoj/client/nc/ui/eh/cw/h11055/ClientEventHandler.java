
package nc.ui.eh.cw.h11055;


import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.voucher.h10125.CBVoucherUntil;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.eh.cw.h11055.ArapCosthsVO;
import nc.vo.eh.kc.h0250210.PeriodVO;
import nc.vo.eh.kc.h0257005.CalcKcybbVO;
import nc.vo.eh.voucher.h10115.EhVoucherVO;
import nc.vo.eh.voucher.h10125.CBVXmlUntil;
import nc.vo.logging.Debug;
import nc.vo.pfxx.pub.FileQueue;
import nc.vo.pfxx.pub.PostFile;
import nc.vo.pfxx.xlog.XlogVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * 说明：成本核算
 * 类型：ZA72
 * 作者：wb
 * 时间：2008-8-11 15:34:32
 */
public class ClientEventHandler extends ManageEventHandler {
    nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();
    String rootdir = "C:/凭证导入/";		//根目录
	String ydir = rootdir+"数据文件/";	//数据文件目录
	String hzdir = rootdir+"回执信息/";	//回执信息目录
    
    public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
        super(billUI, control);
        // TODO Auto-generated constructor stub
    }
    
    @Override
	protected void onBoElse(int intBtn) throws Exception {
        switch (intBtn)
        {
            case IEHButton.CALCKCYBB:    // 计算
                onBoCalcKCYBB();
                break;
            case IEHButton.GENRENDETAIL: // 生成凭证
                onBoGenVoucher();
                break;
        }
    }
    
    /***
     * 生成凭证
     */
    @SuppressWarnings("unchecked")
	private void onBoGenVoucher() {
		try {
			//add by houcq  begin 2010-12-17
			//String[] tablenames= new String[]{"采购入库单（五金）","材料出库单（五金）","产品入库单","产品出库单","盘点表（原料、成品）"};
			
			//modified by byb  2010-12-29:调整次序
			String[] tablenames= new String[]{"采购入库单（五金）","产品入库单","盘点表（原料、成品）","产品出库单","材料出库单（五金）","采购发票","回机料核算","收款单","付款单"};
			
			/*点‘凭证生成’后，系统弹出提示‘系统正在检测单据状态，请稍候……’，后台开始对：
			采购入库单（五金）表、
			材料出库单表（五金）、
			产品入库单表、产品出库单表、
			盘点表（原料、成品）表进行当月数据检测，检测是否有非审批通过的单据，所有单据检测完后，
			系统进行提示，当所有单据全部审批通过，则提示：‘恭喜，恭喜，单据检查全部通过，可以生成凭证了！’，
			点菜单中确定后，系统开始执行凭证生成代码。如有部分不通过，
			则提示：‘注意：采购入库单（五金）检查通过……,材料出库单（五金）检查通过……，			
			产品入库单尚有单据未审批通过……，产品出库单表检查通过……，盘点表（原料、成品）检查通过……,尚有单据未审批，请处理后再操作！’，点确定后，返回。
			*/
			
			String year=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vyear").getValueObject().toString();
			String month=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("nmonth").getValueObject().toString();
			StringBuilder message= new StringBuilder();
			message.append("注意：");
			int j= Integer.valueOf(month);
			if (j<10)
			{
				month="0"+month;
			}
			String startDate=year+"-"+month+"-01";
			String endDate=year+"-"+month+"-31";
			
			
			int flag=0;	
			//String[] sqls=new String[]{"eh_stock_in","eh_sc_cprkd","eh_store_check","eh_icout","eh_sc_ckd"};
			String[] sqls=new String[]{"eh_stock_in","eh_sc_cprkd","eh_store_check","eh_icout","eh_sc_ckd","eh_arap_stockinvoice","eh_hjlhs","eh_arap_sk","eh_arap_fk"};//modify by houcq 2011-07-13
			IUAPQueryBS  iUAPQueryBS =    (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
			for (int i=0;i<sqls.length;i++)
			{
				StringBuilder sql=new StringBuilder();
				sql.append("select * from ");
				sql.append(sqls[i]);
				sql.append(" where  vbillstatus<>1 and nvl(dr,0)=0 and pk_corp='"+ce.getCorporation().getPk_corp()+"' and dmakedate>='"+startDate+"' and dmakedate<='"+endDate+"'");
				
				ArrayList list=(ArrayList) iUAPQueryBS.executeQuery(sql.toString(),  new MapListProcessor());
				if (list.size()>0)
				{
					message.append("\n"+tablenames[i]).append("尚有单据未审批通过......");
					flag=1;
				}
				else
				{
					message.append("\n"+tablenames[i]).append("检查通过......");
					flag+=2;
				}
			}
			//if (flag==10)
			if (flag==18)//modify by houcq 2011-07-13
			{
				getBillUI().showWarningMessage("'恭喜，恭喜，单据检查全部通过，可以生成凭证了!'");
			}
			else
			{
				getBillUI().showWarningMessage(message.toString());
				return;
			}
			
			ArapCosthsVO hvo = (ArapCosthsVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
			hvo.setCoperatorid(ce.getUser().getUserCode());
	    	hvo.setMemo(ce.getCorporation().getUnitcode());
	    	hvo.setCalcdate(ce.getDate());
	    	String sql ="select pk_period from eh_period where pk_corp='"+ce.getCorporation().getPk_corp()+"' and nyear="+year+" and nmonth ="+month;
	    	Object o = iUAPQueryBS.executeQuery(sql, new ColumnProcessor());
        	if (o!=null)
        	{
        		hvo.setPk_period(o.toString());
        	}
        	else
        	{
        		getBillUI().showWarningMessage("期间错误,请检查!");
    			return;
        	}
	    	
	    	HashMap<String, EhVoucherVO> hm = new CBVoucherUntil(hvo).getALLVoucher();			//得到凭证所有数据
	    	Document doc = new CBVXmlUntil().WriteToXML(_getCorp().getPk_corp(), hm);			//生成XML
			doSend(doc);						//发送到外部交换平台
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
	}

    /**
	 * 执行数据文件发送。
	 */
	public void doSend(Document xmldoc)
	{
		//执行数据导入
		getBillUI().showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("pfxx", "UPPpfxx-000088")/* @res "外部数据导入中..." */);
		String url = getPfxxUrl();
		if (url == null)
			return;
		final String sendURL = url + "&" + "langcode=" + ClientEnvironment.getInstance().getLanguage() +"&operator=" +ClientEnvironment.getInstance().getUser().getPrimaryKey();
		//此次对每个文件进行导入
		if (xmldoc != null ){
			FileQueue fileQueue = null;
			try   
			{
				File rootfile = new File(rootdir);
				if (!rootfile.exists()) {
					rootfile.mkdirs();
				}
				File yfile = new File(ydir);
				if (!yfile.exists()) {
					yfile.mkdirs();
				}
				File hzfile = new File(hzdir);
				if (!hzfile.exists()) {
					hzfile.mkdirs();
				}
				String yfilename = ydir+"CBVoucher"+getPresentDate()+".xml";	//数据文件路径
				
				//先导出XML
				Format formatt = Format.getPrettyFormat();
				formatt.setEncoding("gb2312");
				XMLOutputter outputterr = new XMLOutputter();
				outputterr.setFormat(formatt);
				FileWriter writer = new FileWriter(yfilename);
				outputterr.output(xmldoc, writer);
				writer.close();
				
				File filexml = new File(yfilename);				//XML文件
			
				PostFile.sendFileWithResults(filexml, sendURL, hzdir, hzdir, true, fileQueue);
				String res = getResMsg();
				getBillUI().showWarningMessage(res+"\r\n具体请查看<C:\\凭证导入\\回执信息\\>");
			}catch (Exception e){
				Debug.debug("发送出现不可预料错误，请规范操作:" + e.getMessage());
				getBillUI().showWarningMessage(e.getMessage());
			}
		}
	}
	
	/***
	 * 返回信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getResMsg(){
		StringBuffer res = new StringBuffer();
		ArrayList<String> arrRes = CBVXmlUntil.arr;				//生成凭证的ID集合
		String[] voucheridss = arrRes.toArray(new String[arrRes.size()]); 
		String gbids = PubTools.combinArrayToString(voucheridss);	
		String querySQL = "select * from xx_xlog where doc_id in "+gbids;
		try {
			IUAPQueryBS  iUAPQueryBS =  (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			ArrayList<XlogVO> arr = (ArrayList<XlogVO>)iUAPQueryBS.executeQuery(querySQL, new BeanListProcessor(XlogVO.class));
			if(arr!=null&&arr.size()>0){
				String busibill = null;				//凭证单据
				String content = null;				//详细内容
				for(int i=0;i<arr.size();i++){
					XlogVO logVO = arr.get(i);
					busibill = logVO.getBusibill();
					content = logVO.getDoc_dscpt();
					res.append(content);
					if(busibill!=null&&busibill.length()>0){
						res.append("  生成的凭证为："+busibill+"\r\n");
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		if(CBVXmlUntil.arr!=null&&CBVXmlUntil.arr.size()>0){
			CBVXmlUntil.arr = null;
		}
		return res.toString();
	}
	
	public String getPresentDate(){
	 	String nowDate = "";
		try{
			Calendar theCa = new GregorianCalendar();
			theCa.setTime(new Date());
		   
			 nowDate = theCa.get(Calendar.YEAR)+""
			 +(theCa.get(Calendar.MONTH)+1)
		   +theCa.get(Calendar.DATE)
		   +(12*theCa.get(Calendar.AM_PM)
		   +theCa.get(Calendar.HOUR))
		   +theCa.get(Calendar.MINUTE)
		   +theCa.get(Calendar.SECOND);
	     }
		catch(Exception e){
			e.printStackTrace(System.out);
		}
		
		return nowDate;
	}
	
	public String getPfxxUrl(){
		ClientEnvironment cev = ClientEnvironment.getInstance();
		String countCode = cev.getAccount().getAccountCode();//帐套编码
		String corpCode = cev.getCorporation().getUnitcode();
		String langcode = cev.getLanguage();
		String URL = cev.getServerURL().toString();
		if(!URL.endsWith("/"))
			URL = URL + "/";
		URL = URL + "service/XChangeServlet?account=";
		URL = URL + countCode;
		URL = URL + "&receiver=";
		URL = URL + corpCode;
		URL = URL + "&" + "langcode=" + langcode;
		return URL;
	}
	
	@Override
    protected void onBoQuery() throws Exception {
    	// TODO Auto-generated method stub
    	StringBuffer sbWhere = new StringBuffer()
    	.append(" ");          
		if(askForQueryCondition(sbWhere)==false) 
			return;		
		SuperVO[] queryVos = queryHeadVOs(sbWhere.toString());
       getBufferData().clear();
       // 增加数据到Buffer
       addDataToBuffer(queryVos);
       updateBuffer();
    }
    
	@SuppressWarnings({ "static-access", "unchecked" })
	private void onBoCalcKCYBB() {
		CalcDialog calcDialog = new CalcDialog();
		calcDialog.showModal();
		String pk_period = calcDialog.pk_period;
		String jsmethod = calcDialog.jsmethod==null?"":calcDialog.jsmethod.equals("正常")?"0":"1";
		IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        if(pk_period!=null&&pk_period.length()>0){
			/*
        	 *计算时增加月份验证功能。
			 *当表头中月份与登录日期的月份不一致时，提示："计算月份与核算月份不统一，无法计算，请确认后再做！”
			 *当月份一致时，进行计算。
        	 */
        	//add by houcq 2011-07-29 begin
        	try {
	        	 String sql = "select nmonth FROM eh_period WHERE pk_period = '"+pk_period+"' and  pk_corp = '"+ce.getCorporation().getPk_corp()+"' and isnull(dr,0)=0";
	        	 Object ob  = iUAPQueryBS.executeQuery(sql, new ColumnProcessor());
	        	 if (ob!=null)
	        	 {
	        		 int month =Integer.parseInt(ob.toString());
	        		 if (month!=_getDate().getMonth())
	        		 {
	        			 getBillUI().showErrorMessage("计算月份与核算月份不统一，无法计算，请确认后再做！");
	        			 return;
	        		 }
	        	 }
	        	//end 
	        	//add by houcq 2011-10-24 begin
	        	 /*
	        	  * 已生成凭证时，不允许进行成本计算，只有删除凭证后，才能重新生成凭证。
	        	  */
	        	 int year = _getDate().getYear();
	        	 int month = _getDate().getMonth();
	        	 StringBuffer yscpz = new StringBuffer()  
	           	.append(" select explanation,year,period from gl_voucher")
	           	.append(" where pk_corp = '"+ce.getCorporation().getPk_corp()+"' and year="+year+" and period= "+month)
	           	.append(" and explanation in ('本月耗用原辅料','本月耗用包装','本月分配工资','本月耗用燃料','本月耗用电费','本月分配制造费用')")
	           	.append(" and nvl(dr,0)=0  and pk_system='XX'");
	        	 ArrayList yscpzarr = (ArrayList) iUAPQueryBS.executeQuery(yscpz.toString(), new MapListProcessor());
	        	 if (yscpzarr.size()>0)
	        	 {
	        		 getBillUI().showWarningMessage("已生成凭证,不允许进行成本计算,如确需重新计算成本,请先删除凭证,然后再进行成本计算!");
		        	 return;
	        	 }
	        	//add by houcq 2011-10-24 end	                   
	        	PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
	    		CalcKcybbVO kcVO = new CalcKcybbVO();
	        	kcVO.setPk_corp(_getCorp().getPk_corp());
	        	kcVO.setPk_period(pk_period);
	        	kcVO.setCoperatorid(_getOperator());
	        	kcVO.setCalcdate(_getDate());
	        	pubItf.calcCPLKCYBB(kcVO);
				PeriodVO perVO = (PeriodVO) iUAPQueryBS.retrieveByPK(PeriodVO.class, pk_period);
				int ret = getBillUI().showYesNoMessage("你确认要重新计算"+perVO.getNyear()+"年"+perVO.getNmonth()+"月的成本吗?");
				
		        if (ret ==UIDialog.ID_YES){
		        	String sql1 = check1(_getCorp().getPk_corp(), perVO.getBegindate(), perVO.getEnddate());
		        	String sql2 = check2(_getCorp().getPk_corp(), perVO.getBegindate(), perVO.getEnddate());
		        	ArrayList arr1 = (ArrayList) iUAPQueryBS.executeQuery(sql1,new MapListProcessor());
		        	ArrayList arr2 = (ArrayList) iUAPQueryBS.executeQuery(sql2,new MapListProcessor());
		        	if (arr1.size()>0||arr2.size()>0)
		        	{
		        		getBillUI().showWarningMessage("尚有物料在本期BOM中未发现耗用，请检查再试!");
		        		return;
		        	}
					String sql3 = getNoAvgPrice(_getCorp().getPk_corp(), perVO.getBegindate(), perVO.getEnddate());
		        	StringBuilder sb=new StringBuilder("");
		        	ArrayList arr = (ArrayList) iUAPQueryBS.executeQuery(sql3.toString(), new MapListProcessor());
					for(int i=0;i<arr.size();i++){
					   HashMap hmA = (HashMap)arr.get(i);
					   String invname = hmA.get("invname")==null?"":hmA.get("invname").toString();
					   sb.append(invname+"\n");
					}
					if(!"".equals(sb.toString()))
					{
						getBillUI().showWarningMessage("无法取得以下物料：\n"+sb.toString()+"耗用单价,请检查后再做!");
						return;
					}
		        	ArapCosthsVO vo  = new ArapCosthsVO();
		        	vo.setPk_corp(_getCorp().getPk_corp());
		        	vo.setPk_period(pk_period);
		        	vo.setCoperatorid(_getOperator());
		        	vo.setCalcdate(_getDate());
		        	vo.setMemo(jsmethod);	
		        	
		        	pubItf.calcCB(vo);
		        	//add by houcq 2011-10-13 begin
		        	String fsql="select * from eh_cb_detail where  pk_corp='"+_getCorp().getPk_corp()+"' and pk_period = '"+pk_period+"' and je=0";
		        	ArrayList arrs = (ArrayList) iUAPQueryBS.executeQuery(fsql.toString(), new MapListProcessor());
		        	if (arrs.size()>0)
		        	{
		        		getBillUI().showWarningMessage("有物料无法取得耗用单价,将影响凭证凭证生成,请查询成本核算明细表进行检查!");
						return;
		        	}
		        	//add by houcq 2011-10-13 end 
		        	//将数据显示到界面
		        	String whereSql = " pk_period = '"+pk_period+"' and pk_corp = '"+_getCorp().getPk_corp()+"'";
		        	nc.ui.trade.bsdelegate.BusinessDelegator business = new nc.ui.trade.bsdelegate.BusinessDelegator();
	                SuperVO[] supervo = business.queryByCondition(ArapCosthsVO.class, whereSql);
	                getBufferData().clear();
	     	       // 增加数据到Buffer
	     	       addDataToBuffer(supervo);
	     	       getBillUI().showHintMessage("计算成功!");
	     	       updateBuffer();
		        }
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
	}
	/*
     * 饲料公司使用ERP计算生产成本时有可能会出现如下异常情况：
     * 配方有，材料出库没有
     */
	private String  check1(String pk_corp,UFDate begindate,UFDate enddate)
	{
      	StringBuffer pfpdcysql = new StringBuffer()  
      	.append(" select b.yl_store,b.ylpk,a.pfsl,b.cksl from (")
      	.append(" select c.pk_altinvbasdoc yl_store, c.pk_invbasdoc ylpk,sum(round(nvl(a.rkamount * c.zamount,0),5)) pfsl")
      	.append(" from (select b.pk_invbasdoc, b.ver, sum(nvl(b.rkmount, 0)) rkamount")
      	.append(" from eh_sc_cprkd a, eh_sc_cprkd_b b")
      	.append(" where a.pk_rkd = b.pk_rkd")
      	.append(" and a.dmakedate between '"+begindate+"' and '"+enddate+"'")
      	.append(" and a.pk_corp = '"+pk_corp+"'")
      	.append(" and a.vbillstatus = 1 and nvl(a.dr, 0) = 0 and nvl(b.dr, 0) = 0")
      	.append(" group by b.pk_invbasdoc, b.ver) a, eh_bom b,eh_bom_b c")
      	.append(" where a.pk_invbasdoc = b.pk_invbasdoc")
      	.append(" and b.pk_bom = c.pk_bom")
      	.append(" and a.ver = b.ver AND a.rkamount <> 0")
      	.append(" and b.pk_corp = '"+pk_corp+"'")
      	.append(" and nvl(b.dr, 0) = 0 and nvl(c.dr, 0) = 0")
      	.append(" group by c.pk_altinvbasdoc, c.pk_invbasdoc having sum(round(nvl(a.rkamount * c.zamount,0),5)) <> 0) a,")
      	.append(" (select b.pk_store yl_store, b.pk_invbasdoc ylpk,sum(nvl(b.blmount, 0)) cksl")
      	.append(" from eh_sc_ckd a, eh_sc_ckd_b b, bd_invmandoc c, bd_invbasdoc d")
      	.append(" where a.pk_ckd = b.pk_ckd")
      	.append(" and b.pk_invbasdoc = c.pk_invmandoc")
      	.append(" and c.pk_invbasdoc = d.pk_invbasdoc")
      	.append(" and a.pk_corp = '"+pk_corp+"'")
      	.append(" and a.dmakedate between '"+begindate+"' and '"+enddate+"'")
      	.append(" and nvl(a.dr, 0) = 0 and nvl(b.dr, 0) = 0 and a.vbillstatus = 1")
      	.append(" and (a.vsourcebilltype = 'ZA47' or a.vsourcebilltype = 'ZB01')")
      	.append(" and (d.invcode like '01%' or d.invcode like '07%')")
      	.append(" group by b.pk_store, b.pk_invbasdoc having sum(nvl(blmount, 0)) <> 0) b")
      	//.append(" group by b.pk_store, b.pk_invbasdoc) b")//modify by houcq 2011-09-26
      	.append(" where a.yl_store=b.yl_store(+) and a.ylpk=b.ylpk(+) and b.ylpk is null");
      	 return pfpdcysql.toString();
	}
	/*
     * 饲料公司使用ERP计算生产成本时有可能会出现如下异常情况：
     * 材料出库有，配方无
     */
	private String  check2(String pk_corp,UFDate begindate,UFDate enddate)
	{
      	StringBuffer pfpdcysql = new StringBuffer()  
      	.append(" select b.yl_store,b.ylpk,a.pfsl,b.cksl from (")
      	.append(" select c.pk_altinvbasdoc yl_store, c.pk_invbasdoc ylpk,sum(round(nvl(a.rkamount * c.zamount,0),5)) pfsl")
      	.append(" from (select b.pk_invbasdoc, b.ver, sum(nvl(b.rkmount, 0)) rkamount")
      	.append(" from eh_sc_cprkd a, eh_sc_cprkd_b b")
      	.append(" where a.pk_rkd = b.pk_rkd")
      	.append(" and a.dmakedate between '"+begindate+"' and '"+enddate+"'")
      	.append(" and a.pk_corp = '"+pk_corp+"'")
      	.append(" and a.vbillstatus = 1 and nvl(a.dr, 0) = 0 and nvl(b.dr, 0) = 0")
      	.append(" group by b.pk_invbasdoc, b.ver) a, eh_bom b,eh_bom_b c")
      	.append(" where a.pk_invbasdoc = b.pk_invbasdoc")
      	.append(" and b.pk_bom = c.pk_bom")
      	.append(" and a.ver = b.ver AND a.rkamount <> 0")
      	.append(" and b.pk_corp = '"+pk_corp+"'")
      	.append(" and nvl(b.dr, 0) = 0 and nvl(c.dr, 0) = 0")
      	.append(" group by c.pk_altinvbasdoc, c.pk_invbasdoc having sum(round(nvl(a.rkamount * c.zamount,0),5)) <> 0) a,")
      	.append("  (select b.pk_store yl_store, b.pk_invbasdoc ylpk,sum(nvl(b.blmount, 0)) cksl")
      	.append(" from eh_sc_ckd a, eh_sc_ckd_b b, bd_invmandoc c, bd_invbasdoc d")
      	.append(" where a.pk_ckd = b.pk_ckd")
      	.append(" and b.pk_invbasdoc = c.pk_invmandoc")
      	.append(" and c.pk_invbasdoc = d.pk_invbasdoc")
      	.append(" and a.pk_corp = '"+pk_corp+"'")
      	.append(" and a.dmakedate between '"+begindate+"' and '"+enddate+"'")
      	.append(" and nvl(a.dr, 0) = 0 and nvl(b.dr, 0) = 0 and a.vbillstatus = 1")
      	.append(" and (a.vsourcebilltype = 'ZA47' or a.vsourcebilltype = 'ZB01')")
      	.append(" and (d.invcode like '01%' or d.invcode like '07%')")
      	.append(" group by b.pk_store, b.pk_invbasdoc having sum(nvl(blmount, 0)) <> 0) b")
      	//.append(" group by b.pk_store, b.pk_invbasdoc) b")//modify by houcq 2011-09-26
      	.append(" where a.yl_store(+)=b.yl_store and a.ylpk(+)=b.ylpk and a.ylpk is null");
      	 return pfpdcysql.toString();
	}
	/*
     * 取有耗用数量,但没有单价的物料SQL
     */
    public String getNoAvgPrice(String pk_corp,UFDate begindate,UFDate enddate){
    	StringBuffer sql = new StringBuffer()      			
    	.append(" select d.invname,b.avgprice from (")
    	.append(" select b.pk_store,b.pk_invbasdoc,sum(nvl(blmount, 0)) cksl")
    	.append(" from eh_sc_ckd a, eh_sc_ckd_b b,bd_invmandoc c,bd_invbasdoc d")
    	.append(" where a.pk_ckd = b.pk_ckd and b.pk_invbasdoc = c.pk_invmandoc and c.pk_invbasdoc = d.pk_invbasdoc")
    	.append(" and c.pk_corp = '"+pk_corp+"' and a.dmakedate between '"+begindate+"' and '"+enddate+"'")
    	.append(" and nvl(a.dr, 0) = 0 and nvl(b.dr, 0) = 0 and a.vbillstatus = 1   ")
    	.append(" and (a.vsourcebilltype = 'ZA47' or a.vsourcebilltype = 'ZB01')")
    	.append(" and (d.invcode like '01%' or d.invcode like '07%')")
    	.append(" group by b.pk_store,b.pk_invbasdoc having sum(nvl(blmount, 0))<>0)a,")
    	.append(" (select b.pk_store,b.pk_invbasdoc,(b.qcje+b.rkje+b.drje-b.hjje-b.dcje)/(b.qcsl+b.rksl+b.drsl-b.hjsl-b.dcsl) avgprice")
    	.append(" from eh_calc_kcybb a,eh_calc_kcybb_b b")
    	.append(" where a.pk_kcybb=b.pk_kcybb and nvl(a.dr,0)=0 and nvl(b.dr,0)=0")
    	.append(" and a.nyear="+begindate.getYear()+" and a.nmonth="+begindate.getMonth()+" and a.pk_corp='"+pk_corp+"'")
    	.append(" and (b.qcsl+b.rksl+b.drsl-b.hjsl-b.dcsl)<>0) b,bd_invmandoc c,bd_invbasdoc d")
    	.append(" where a.pk_store=b.pk_store(+) and a.pk_invbasdoc=b.pk_invbasdoc(+)")
    	.append(" and a.pk_invbasdoc=c.pk_invmandoc and c.pk_invbasdoc=d.pk_invbasdoc and nvl(b.avgprice,0)=0")
    	.append(" and a.pk_store||a.pk_invbasdoc not in (select  b.pk_store||b.pk_invbasdoc")
    	.append(" from eh_sc_cprkd a, eh_sc_cprkd_b b,eh_bom c")
    	.append(" where a.pk_rkd = b.pk_rkd and a.vbillstatus = 1 and nvl(a.dr, 0) = 0 and nvl(b.dr, 0) = 0")
    	.append(" and a.dmakedate between '"+begindate+"' and '"+enddate+"' and a.pk_corp='"+pk_corp+"'")
    	.append(" and b.pk_invbasdoc=c.pk_invbasdoc and b.ver=c.ver")
    	.append(" and c.vsourcebillid='Y')");      	
    	return sql.toString();
    }   
}

