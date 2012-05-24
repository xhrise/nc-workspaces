
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
 * ˵�����ɱ�����
 * ���ͣ�ZA72
 * ���ߣ�wb
 * ʱ�䣺2008-8-11 15:34:32
 */
public class ClientEventHandler extends ManageEventHandler {
    nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();
    String rootdir = "C:/ƾ֤����/";		//��Ŀ¼
	String ydir = rootdir+"�����ļ�/";	//�����ļ�Ŀ¼
	String hzdir = rootdir+"��ִ��Ϣ/";	//��ִ��ϢĿ¼
    
    public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
        super(billUI, control);
        // TODO Auto-generated constructor stub
    }
    
    @Override
	protected void onBoElse(int intBtn) throws Exception {
        switch (intBtn)
        {
            case IEHButton.CALCKCYBB:    // ����
                onBoCalcKCYBB();
                break;
            case IEHButton.GENRENDETAIL: // ����ƾ֤
                onBoGenVoucher();
                break;
        }
    }
    
    /***
     * ����ƾ֤
     */
    @SuppressWarnings("unchecked")
	private void onBoGenVoucher() {
		try {
			//add by houcq  begin 2010-12-17
			//String[] tablenames= new String[]{"�ɹ���ⵥ�����","���ϳ��ⵥ�����","��Ʒ��ⵥ","��Ʒ���ⵥ","�̵��ԭ�ϡ���Ʒ��"};
			
			//modified by byb  2010-12-29:��������
			String[] tablenames= new String[]{"�ɹ���ⵥ�����","��Ʒ��ⵥ","�̵��ԭ�ϡ���Ʒ��","��Ʒ���ⵥ","���ϳ��ⵥ�����","�ɹ���Ʊ","�ػ��Ϻ���","�տ","���"};
			
			/*�㡮ƾ֤���ɡ���ϵͳ������ʾ��ϵͳ���ڼ�ⵥ��״̬�����Ժ򡭡�������̨��ʼ�ԣ�
			�ɹ���ⵥ����𣩱�
			���ϳ��ⵥ����𣩡�
			��Ʒ��ⵥ����Ʒ���ⵥ��
			�̵��ԭ�ϡ���Ʒ������е������ݼ�⣬����Ƿ��з�����ͨ���ĵ��ݣ����е��ݼ�����
			ϵͳ������ʾ�������е���ȫ������ͨ��������ʾ������ϲ����ϲ�����ݼ��ȫ��ͨ������������ƾ֤�ˣ�����
			��˵���ȷ����ϵͳ��ʼִ��ƾ֤���ɴ��롣���в��ֲ�ͨ����
			����ʾ����ע�⣺�ɹ���ⵥ����𣩼��ͨ������,���ϳ��ⵥ����𣩼��ͨ��������			
			��Ʒ��ⵥ���е���δ����ͨ����������Ʒ���ⵥ����ͨ���������̵��ԭ�ϡ���Ʒ�����ͨ������,���е���δ�������봦����ٲ�����������ȷ���󣬷��ء�
			*/
			
			String year=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vyear").getValueObject().toString();
			String month=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("nmonth").getValueObject().toString();
			StringBuilder message= new StringBuilder();
			message.append("ע�⣺");
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
					message.append("\n"+tablenames[i]).append("���е���δ����ͨ��......");
					flag=1;
				}
				else
				{
					message.append("\n"+tablenames[i]).append("���ͨ��......");
					flag+=2;
				}
			}
			//if (flag==10)
			if (flag==18)//modify by houcq 2011-07-13
			{
				getBillUI().showWarningMessage("'��ϲ����ϲ�����ݼ��ȫ��ͨ������������ƾ֤��!'");
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
        		getBillUI().showWarningMessage("�ڼ����,����!");
    			return;
        	}
	    	
	    	HashMap<String, EhVoucherVO> hm = new CBVoucherUntil(hvo).getALLVoucher();			//�õ�ƾ֤��������
	    	Document doc = new CBVXmlUntil().WriteToXML(_getCorp().getPk_corp(), hm);			//����XML
			doSend(doc);						//���͵��ⲿ����ƽ̨
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
	}

    /**
	 * ִ�������ļ����͡�
	 */
	public void doSend(Document xmldoc)
	{
		//ִ�����ݵ���
		getBillUI().showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("pfxx", "UPPpfxx-000088")/* @res "�ⲿ���ݵ�����..." */);
		String url = getPfxxUrl();
		if (url == null)
			return;
		final String sendURL = url + "&" + "langcode=" + ClientEnvironment.getInstance().getLanguage() +"&operator=" +ClientEnvironment.getInstance().getUser().getPrimaryKey();
		//�˴ζ�ÿ���ļ����е���
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
				String yfilename = ydir+"CBVoucher"+getPresentDate()+".xml";	//�����ļ�·��
				
				//�ȵ���XML
				Format formatt = Format.getPrettyFormat();
				formatt.setEncoding("gb2312");
				XMLOutputter outputterr = new XMLOutputter();
				outputterr.setFormat(formatt);
				FileWriter writer = new FileWriter(yfilename);
				outputterr.output(xmldoc, writer);
				writer.close();
				
				File filexml = new File(yfilename);				//XML�ļ�
			
				PostFile.sendFileWithResults(filexml, sendURL, hzdir, hzdir, true, fileQueue);
				String res = getResMsg();
				getBillUI().showWarningMessage(res+"\r\n������鿴<C:\\ƾ֤����\\��ִ��Ϣ\\>");
			}catch (Exception e){
				Debug.debug("���ͳ��ֲ���Ԥ�ϴ�����淶����:" + e.getMessage());
				getBillUI().showWarningMessage(e.getMessage());
			}
		}
	}
	
	/***
	 * ������Ϣ
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getResMsg(){
		StringBuffer res = new StringBuffer();
		ArrayList<String> arrRes = CBVXmlUntil.arr;				//����ƾ֤��ID����
		String[] voucheridss = arrRes.toArray(new String[arrRes.size()]); 
		String gbids = PubTools.combinArrayToString(voucheridss);	
		String querySQL = "select * from xx_xlog where doc_id in "+gbids;
		try {
			IUAPQueryBS  iUAPQueryBS =  (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			ArrayList<XlogVO> arr = (ArrayList<XlogVO>)iUAPQueryBS.executeQuery(querySQL, new BeanListProcessor(XlogVO.class));
			if(arr!=null&&arr.size()>0){
				String busibill = null;				//ƾ֤����
				String content = null;				//��ϸ����
				for(int i=0;i<arr.size();i++){
					XlogVO logVO = arr.get(i);
					busibill = logVO.getBusibill();
					content = logVO.getDoc_dscpt();
					res.append(content);
					if(busibill!=null&&busibill.length()>0){
						res.append("  ���ɵ�ƾ֤Ϊ��"+busibill+"\r\n");
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
		String countCode = cev.getAccount().getAccountCode();//���ױ���
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
       // �������ݵ�Buffer
       addDataToBuffer(queryVos);
       updateBuffer();
    }
    
	@SuppressWarnings({ "static-access", "unchecked" })
	private void onBoCalcKCYBB() {
		CalcDialog calcDialog = new CalcDialog();
		calcDialog.showModal();
		String pk_period = calcDialog.pk_period;
		String jsmethod = calcDialog.jsmethod==null?"":calcDialog.jsmethod.equals("����")?"0":"1";
		IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        if(pk_period!=null&&pk_period.length()>0){
			/*
        	 *����ʱ�����·���֤���ܡ�
			 *����ͷ���·����¼���ڵ��·ݲ�һ��ʱ����ʾ��"�����·�������·ݲ�ͳһ���޷����㣬��ȷ�Ϻ���������
			 *���·�һ��ʱ�����м��㡣
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
	        			 getBillUI().showErrorMessage("�����·�������·ݲ�ͳһ���޷����㣬��ȷ�Ϻ�������");
	        			 return;
	        		 }
	        	 }
	        	//end 
	        	//add by houcq 2011-10-24 begin
	        	 /*
	        	  * ������ƾ֤ʱ����������гɱ����㣬ֻ��ɾ��ƾ֤�󣬲�����������ƾ֤��
	        	  */
	        	 int year = _getDate().getYear();
	        	 int month = _getDate().getMonth();
	        	 StringBuffer yscpz = new StringBuffer()  
	           	.append(" select explanation,year,period from gl_voucher")
	           	.append(" where pk_corp = '"+ce.getCorporation().getPk_corp()+"' and year="+year+" and period= "+month)
	           	.append(" and explanation in ('���º���ԭ����','���º��ð�װ','���·��乤��','���º���ȼ��','���º��õ��','���·����������')")
	           	.append(" and nvl(dr,0)=0  and pk_system='XX'");
	        	 ArrayList yscpzarr = (ArrayList) iUAPQueryBS.executeQuery(yscpz.toString(), new MapListProcessor());
	        	 if (yscpzarr.size()>0)
	        	 {
	        		 getBillUI().showWarningMessage("������ƾ֤,��������гɱ�����,��ȷ�����¼���ɱ�,����ɾ��ƾ֤,Ȼ���ٽ��гɱ�����!");
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
				int ret = getBillUI().showYesNoMessage("��ȷ��Ҫ���¼���"+perVO.getNyear()+"��"+perVO.getNmonth()+"�µĳɱ���?");
				
		        if (ret ==UIDialog.ID_YES){
		        	String sql1 = check1(_getCorp().getPk_corp(), perVO.getBegindate(), perVO.getEnddate());
		        	String sql2 = check2(_getCorp().getPk_corp(), perVO.getBegindate(), perVO.getEnddate());
		        	ArrayList arr1 = (ArrayList) iUAPQueryBS.executeQuery(sql1,new MapListProcessor());
		        	ArrayList arr2 = (ArrayList) iUAPQueryBS.executeQuery(sql2,new MapListProcessor());
		        	if (arr1.size()>0||arr2.size()>0)
		        	{
		        		getBillUI().showWarningMessage("���������ڱ���BOM��δ���ֺ��ã���������!");
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
						getBillUI().showWarningMessage("�޷�ȡ���������ϣ�\n"+sb.toString()+"���õ���,���������!");
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
		        		getBillUI().showWarningMessage("�������޷�ȡ�ú��õ���,��Ӱ��ƾ֤ƾ֤����,���ѯ�ɱ�������ϸ����м��!");
						return;
		        	}
		        	//add by houcq 2011-10-13 end 
		        	//��������ʾ������
		        	String whereSql = " pk_period = '"+pk_period+"' and pk_corp = '"+_getCorp().getPk_corp()+"'";
		        	nc.ui.trade.bsdelegate.BusinessDelegator business = new nc.ui.trade.bsdelegate.BusinessDelegator();
	                SuperVO[] supervo = business.queryByCondition(ArapCosthsVO.class, whereSql);
	                getBufferData().clear();
	     	       // �������ݵ�Buffer
	     	       addDataToBuffer(supervo);
	     	       getBillUI().showHintMessage("����ɹ�!");
	     	       updateBuffer();
		        }
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
	}
	/*
     * ���Ϲ�˾ʹ��ERP���������ɱ�ʱ�п��ܻ���������쳣�����
     * �䷽�У����ϳ���û��
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
     * ���Ϲ�˾ʹ��ERP���������ɱ�ʱ�п��ܻ���������쳣�����
     * ���ϳ����У��䷽��
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
     * ȡ�к�������,��û�е��۵�����SQL
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

