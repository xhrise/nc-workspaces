package nc.ui.eh.voucher.h10115;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import jxl.Workbook;
import jxl.write.WritableWorkbook;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.PubTools;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.eh.voucher.h10112.PfxxItemVO;
import nc.vo.eh.voucher.h10115.EhVoucherBodyVO;
import nc.vo.eh.voucher.h10115.EhVoucherCashVO;
import nc.vo.eh.voucher.h10115.EhVoucherHeadVO;
import nc.vo.eh.voucher.h10115.EhVoucherItemVO;
import nc.vo.eh.voucher.h10115.EhVoucherVO;
import nc.vo.eh.voucher.h10115.PfxxVoucherVO;
import nc.vo.eh.voucher.h10115.VoucherUntil;
import nc.vo.logging.Debug;
import nc.vo.pfxx.exception.PfxxException;
import nc.vo.pfxx.pub.FileQueue;
import nc.vo.pfxx.pub.PostFile;
import nc.vo.pfxx.pub.SendResult;
import nc.vo.pfxx.xlog.XlogVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.trade.pub.HYBillVO;

import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * ˵��: ƾ֤����
 * 
 * @author wb 2009-9-27 13:49:49
 */

public class ClientEventHandler extends CardEventHandler {

	public static Workbook w = null;
	public static WritableWorkbook ww = null;
	private int res;
	private File txtFile = null;
	private nc.ui.pub.beans.UITextField txtfFileUrl = null; // �ı���,������ʾ�ļ�·��
	public static PfxxVoucherVO[] pzvo = null;
	
	//���ƪ�������ޣ�Ĭ��Ϊ1000K
    private int maxTransferSize = 1000;
    
	String rootdir = "C:/ƾ֤����/";		//��Ŀ¼
	String ydir = rootdir+"�����ļ�/";	//�����ļ�Ŀ¼
	String hzdir = rootdir+"��ִ��Ϣ/";	//��ִ��ϢĿ¼
	
	public ClientEventHandler(BillCardUI arg0, ICardController arg1) {
		super(arg0, arg1);
	}

	@Override
	protected void onBoElse(int intBtn) throws Exception {
		switch (intBtn) {
		case IEHButton.CONFIRMBUG: // ��ȡ
			onRead();
			break;
		case IEHButton.ExcelImport:// ����
			onExcelImport();
			break;
		case IEHButton.GENRENDETAIL:// ����XML
			onGenrenXML();
			break;
		}
	}

	private nc.ui.pub.beans.UITextField getTFLocalFile() {
		if (txtfFileUrl == null) {
			try {
				txtfFileUrl = new nc.ui.pub.beans.UITextField();
				txtfFileUrl.setName("txtfFileUrl");
				txtfFileUrl.setBounds(270, 160, 230, 26);
				txtfFileUrl.setMaxLength(2000);
				txtfFileUrl.setEditable(false);

			} catch (java.lang.Throwable e) {
				handleException(e);
			}
		}
		return txtfFileUrl;
	}

	private void handleException(java.lang.Throwable exception) {
		System.out.println("--------- δ��׽�����쳣 ---------");
		exception.printStackTrace(System.out);
	}

	// ��ȡExcel�е�����
	public void onRead() {
		try {
			nc.ui.pub.beans.UIFileChooser fileChooser = new nc.ui.pub.beans.UIFileChooser();
			fileChooser.setAcceptAllFileFilterUsed(true);
			res = fileChooser.showOpenDialog(getBillUI());
			if (res == 0) {
				getTFLocalFile().setText(
						fileChooser.getSelectedFile().getAbsolutePath());
				txtFile = fileChooser.getSelectedFile();
				String filepath = txtFile.getAbsolutePath();
				WriteToExcel.creatFile(filepath); // �����ļ���·��
				WriteToExcel.readData("", 0, 0, 0); // ��ȡ�ļ�������
			} else {
				return;
			}
			pzvo = WriteToExcel.vos;
			if (pzvo != null && pzvo.length > 0) {
				HYBillVO billVO = new HYBillVO();
				billVO.setChildrenVO(pzvo);
				if (getBufferData().isVOBufferEmpty()) {
					getBufferData().addVOToBuffer(billVO);
				} else {
					getBufferData().setCurrentVO(billVO);
				}
				getBufferData().setCurrentRow(0);
				HashMap<String, String> hmitem = getItems(); // ��Ŀ����
				/*** ת������ **/
				 int intitemcode = 19;
				 int intitemname = 20;
				 for(int i=1;i<=20;i++){
					 intitemcode = intitemcode+2;
					 intitemname = intitemname+2;
					 String itemname = 	 hmitem.get("Item"+i)==null?null:hmitem.get("Item"+i).toString();
					 if(itemname!=null){
						 getBillCardPanelWrapper().getBillCardPanel().getBillTable().getColumnModel().getColumn(intitemcode).setHeaderValue(itemname+"����"); 
						 getBillCardPanelWrapper().getBillCardPanel().getBillTable().getColumnModel().getColumn(intitemname).setHeaderValue(itemname+"����");
					 }
				 }
				getBillUI().showWarningMessage("��ȡ�ɹ���");
			}
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	/***
	 * ����ƾ֤
	 * @throws Exception
	 */
	private void onExcelImport() throws Exception {
		// �Էǿ���֤
		getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
		// ��Ŀ��֤
		String res = validateItem();
		if(res!=null&&res.length()>0){
			getBillUI().showErrorMessage(res);
			return;
		}
		PfxxVoucherVO[] bvos = (PfxxVoucherVO[])getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
		if (bvos != null && bvos.length > 0) {
			int iRet = getBillUI().showYesNoMessage("�Ƿ�ȷ������ƾ֤?");
			if (iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION) {
				HashMap<String, EhVoucherVO> hm = dealVOS(bvos);
				Document doc = new VoucherUntil().WriteToXML(_getCorp().getPk_corp(), hm);
				doSend(doc);
//				PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
//				pubItf.sendVoucher(getPfxxUrl(), _getCorp().getPk_corp(), hm);
				// IVOPersistence iVOPersistence =
				// (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
				// iVOPersistence.insertVOArray(pzvo);
				
			} else {
				return;
			}
		} else {
			getBillUI().showErrorMessage("���ȶ�ȡExcel��!");
			return;
		}

		// PubItf pubItf =
		// (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
		// pubItf.ReadDatewl(invo);

//		getBillUI().showWarningMessage("����ɹ���");
	}

	/**
	 * ����ļ����ȡ�
	 */
	private String getFileSize(long size)
	{
		long l = size / 1024 + 1;
		return String.valueOf(l);
	}
	
	public boolean checkContentLimit(File curFile)
	{
		int sizeByKB = Integer.parseInt(getFileSize(curFile.length()));
		Logger.debug("�����ļ�����: " + (new Integer(sizeByKB)).toString() + "<KB>");
		if (sizeByKB > maxTransferSize)
			return false;
		else
			return true;
	}
	
	/**
	 * ����ǰ���
	 * @param curFile
	 * @return
	 */
	private boolean checkBeforeSend(File curFile) throws PfxxException
	{
		//�������޼��
		if (!checkContentLimit(curFile))
		{
			throw new PfxxException(nc.ui.ml.NCLangRes.getInstance().getStrByID("pfxx", "UPPpfxx-000138"));/*
							                                              * @res
							                                              * "�����ļ�����,������������ٷ���,�����ļ�λ��:"
							                                              */
					
		}
		return true;
	}
	
	/**
	 * ִ�������ļ����͡�
	 */
	public void doSend(Document xmldoc)
	{
//		int postNum = -1;
//		int suspendNum = -1;
		//ִ�����ݵ���
		getBillUI().showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("pfxx", "UPPpfxx-000088")/* @res "�ⲿ���ݵ�����..." */);
		String url = getPfxxUrl();
		if (url == null)
			return;
		final String sendURL = url + "&" + "langcode=" + ClientEnvironment.getInstance().getLanguage() +"&operator=" +ClientEnvironment.getInstance().getUser().getPrimaryKey();
		//�˴ζ�ÿ���ļ����е���
		if (xmldoc != null ){
//			postNum = 0;
//	 		suspendNum = 0;
//			//ʱ����� 
//			long begintime = System.currentTimeMillis();
//			String msg = null;
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
				String yfilename = ydir+"NCVoucher"+getPresentDate()+".xml";	//�����ļ�·��
				
				//�ȵ���XML
				Format formatt = Format.getPrettyFormat();
				formatt.setEncoding("gb2312");
				XMLOutputter outputterr = new XMLOutputter();
				outputterr.setFormat(formatt);
				FileWriter writer = new FileWriter(yfilename);
				outputterr.output(xmldoc, writer);
				writer.close();
				
				File filexml = new File(yfilename);				//XML�ļ�
				//����ǰ���
				checkBeforeSend(filexml);
				SendResult results = PostFile.sendFileWithResults(filexml, sendURL, hzdir, hzdir, false, fileQueue);
//				//��ʾ���η���ȫ�����
//				msg = nc.ui.ml.NCLangRes.getInstance().getStrByID("pfxx", "UPPpfxx-000139")/* @res "�ѷ��������ļ�ƪ��:" */
//						+ (new Integer(postNum)).toString() + "," + nc.ui.ml.NCLangRes.getInstance().getStrByID("pfxx", "UPPpfxx-000140")/* @res "�ж�ƪ��:" */
//						+ (new Integer(suspendNum)).toString();
//				//ʱ�����
//				msg += nc.ui.ml.NCLangRes.getInstance().getStrByID("pfxx", "UPPpfxx-V50132")/*"�������̺�ʱ��"*/
//						+ String.valueOf(System.currentTimeMillis() - begintime) + "ms";
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
		ArrayList<String> arrRes = VoucherUntil.arr;				//����ƾ֤��ID����
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
		if(VoucherUntil.arr!=null&&VoucherUntil.arr.size()>0){
			VoucherUntil.arr = null;
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
	
	/***
	 * ����XML
	 * 
	 * @throws Exception
	 */
	private void onGenrenXML() throws Exception {
		String res = validateItem();
		if(res!=null&&res.length()>0){
			getBillUI().showErrorMessage(res);
			return;
		}
		PfxxVoucherVO[] bvos = (PfxxVoucherVO[])getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
		if(bvos!=null&&bvos.length>0){
			HashMap<String, EhVoucherVO> hm = dealVOS(bvos);
			Document doc = new VoucherUntil().WriteToXML(_getCorp().getPk_corp(), hm);
			try {
				File rootfile = new File(rootdir);
				if (!rootfile.exists()) {
					rootfile.mkdirs();
				}
				
				File yfile = new File(ydir);
				if (!yfile.exists()) {
					yfile.mkdirs();
				}
				
				String yfilename = ydir+"NCVoucher"+getPresentDate()+".xml";	//�����ļ�·��
				Format formatt = Format.getPrettyFormat();
				formatt.setEncoding("gb2312");
				XMLOutputter outputterr = new XMLOutputter();
				outputterr.setFormat(formatt);
				FileWriter writer = new FileWriter(yfilename);
				outputterr.output(doc, writer);
				writer.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			getBillUI().showWarningMessage("�����ɹ�!\r\n������鿴<C:\\ƾ֤����\\�����ļ�\\>");
		}else{
			getBillUI().showWarningMessage("û������!");
		}
	}

	/***
	 * �õ���Ŀ����
	 * 
	 * @return
	 */
	public HashMap<String, String> getItems() {
		HashMap<String, String> hm = new HashMap<String, String>();
		nc.ui.trade.bsdelegate.BusinessDelegator business = new nc.ui.trade.bsdelegate.BusinessDelegator();
		try {
			PfxxItemVO[] itemvos = (PfxxItemVO[]) business.queryByCondition(
					PfxxItemVO.class, "pk_corp='" + _getCorp().getPk_corp()
							+ "' and itemname is not null");
			if (itemvos != null && itemvos.length > 0) {
				for (int i = 0; i < itemvos.length; i++) {
					hm.put(itemvos[i].getItemcode(), itemvos[i].getItemname());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hm;
	}

	/***
	 * �������VO,���hm<ƾ֤��,vo(��ͷ,���弯��)>
	 * 
	 * @param vos
	 * @return hm wb 2009-9-28 10:31:54
	 */
	public HashMap<String, EhVoucherVO> dealVOS(PfxxVoucherVO[] vos) {
		HashMap<String, EhVoucherVO> hm = new HashMap<String, EhVoucherVO>();
		if (vos != null && vos.length > 0) {
			int maxvoucherid = Integer.parseInt(getMaxVoucherid()); // ����ƾ֤��
			for (int i = 0; i < vos.length; i++) {
				PfxxVoucherVO vo = vos[i];
				String businesscode = vo.getBusinesscode().toString(); // ҵ���
				// ƾ֤��
				EhVoucherBodyVO bodyvo = getBodyVO(vo);
				if (!hm.containsKey(businesscode)) { // hm�����ڴ�ƾ֤�Ž���ƾ֤��Ϣ����hm
					// ƾ֤ͷ
					maxvoucherid = maxvoucherid+1;
					String voucher_id = String.valueOf(maxvoucherid);
					EhVoucherHeadVO headvo = new EhVoucherHeadVO();
					headvo.setUnitecode(_getCorp().getUnitcode());
					headvo.setNyear(String.valueOf(new UFDate(vos[i]
							.getPrepareddate()).getYear()));
					headvo.setNmonth(vos[i].getNmonth().toString());
					headvo.setVoucher_type(vos[i].getVoucher_type());
					headvo.setBusinesscode(businesscode);
					headvo.setVoucher_id(voucher_id);
					headvo.setPrepareddate(vos[i].getPrepareddate());
					headvo.setAttachment_number(vos[i].getAttachment_number());
					headvo.setEnter(ClientEnvironment.getInstance().getUser().getUserCode());			//�Ƶ���Ϊ����Ա���� edit by wb 2010-1-25 15:05:17
					headvo.setMemo(vos[i].getMemo());

					EhVoucherVO ehvo = new EhVoucherVO();
					ehvo.setHeadvo(headvo);
					ArrayList<EhVoucherBodyVO> arr = new ArrayList<EhVoucherBodyVO>();
					arr.add(bodyvo);
					ehvo.setBodyvos(arr);
					hm.put(businesscode, ehvo);
				} else {
					hm.get(businesscode).getBodyvos().add(bodyvo); // ������vo�ӽ�hm�еı���vo��
				}
			}
		}
		return hm;
	}

	/***
	 * ���ݵ���VO�õ�ƾ֤����VO
	 * 
	 * @param vo
	 * @return
	 */
	public EhVoucherBodyVO getBodyVO(PfxxVoucherVO vo) {
		EhVoucherBodyVO bodyvo = new EhVoucherBodyVO();
		
		HashMap<String, String> hmitem = getItems(); // ��Ŀ����
		
		bodyvo.setAbstractinfo(vo.getAbstractinfo());
		bodyvo.setAccount_code(vo.getAccount_code());
		bodyvo.setAccount_name(vo.getAccount_name());
		bodyvo.setNatural_debit_currency(vo.getNatural_debit_currency());
		bodyvo.setSecondary_debit_amount(vo.getSecondary_debit_amount());
		bodyvo.setNatural_credit_currency(vo.getNatural_credit_currency());
		bodyvo.setSecondary_credit_amount(vo.getSecondary_credit_amount());
		bodyvo.setExchange_rate1(vo.getExchange_rate1());
		bodyvo.setDebit_quantity(vo.getDebit_quantity());
		bodyvo.setCredit_quantity(vo.getCredit_quantity());

		bodyvo.setSettlement(vo.getSettlement());
		bodyvo.setDocument_id(vo.getDocument_id());
		bodyvo.setDocument_date(vo.getDocument_date());
		bodyvo.setCurrency(vo.getCurrency());
		bodyvo.setUnit_price(vo.getUnit_price());				//���ϵ��� edit by wb 2010-2-2 12:57:57
		// ��Ŀת�����뼯����
		ArrayList<EhVoucherItemVO> arr = new ArrayList<EhVoucherItemVO>();
		if (vo.getItemcode1() != null && vo.getItemcode1().length() > 0) {
			EhVoucherItemVO itemvo1 = new EhVoucherItemVO();
			itemvo1.setItemname(hmitem.get("Item1"));
			itemvo1.setItemvalue(vo.getItemcode1());
			arr.add(itemvo1);
		}
		if (vo.getItemcode2() != null && vo.getItemcode2().length() > 0) {
			EhVoucherItemVO itemvo2 = new EhVoucherItemVO();
			itemvo2.setItemname(hmitem.get("Item2"));
			itemvo2.setItemvalue(vo.getItemcode2());
			arr.add(itemvo2);
		}
		if (vo.getItemcode3() != null && vo.getItemcode3().length() > 0) {
			EhVoucherItemVO itemvo3 = new EhVoucherItemVO();
			itemvo3.setItemname(hmitem.get("Item3"));
			itemvo3.setItemvalue(vo.getItemcode3());
			arr.add(itemvo3);
		}
		if (vo.getItemcode4() != null && vo.getItemcode4().length() > 0) {
			EhVoucherItemVO itemvo4 = new EhVoucherItemVO();
			itemvo4.setItemname(hmitem.get("Item4"));
			itemvo4.setItemvalue(vo.getItemcode4());
			arr.add(itemvo4);
		}
		if (vo.getItemcode5() != null && vo.getItemcode5().length() > 0) {
			EhVoucherItemVO itemvo5 = new EhVoucherItemVO();
			itemvo5.setItemname(hmitem.get("Item5"));
			itemvo5.setItemvalue(vo.getItemcode5());
			arr.add(itemvo5);
		}
		if (vo.getItemcode6() != null && vo.getItemcode6().length() > 0) {
			EhVoucherItemVO itemvo6 = new EhVoucherItemVO();
			itemvo6.setItemname(hmitem.get("Item6"));
			itemvo6.setItemvalue(vo.getItemcode6());
			arr.add(itemvo6);
		}
		if (vo.getItemcode7() != null && vo.getItemcode7().length() > 0) {
			EhVoucherItemVO itemvo7 = new EhVoucherItemVO();
			itemvo7.setItemname(hmitem.get("Item7"));
			itemvo7.setItemvalue(vo.getItemcode7());
			arr.add(itemvo7);
		}
		if (vo.getItemcode8() != null && vo.getItemcode8().length() > 0) {
			EhVoucherItemVO itemvo8 = new EhVoucherItemVO();
			itemvo8.setItemname(hmitem.get("Item8"));
			itemvo8.setItemvalue(vo.getItemcode8());
			arr.add(itemvo8);
		}
		if (vo.getItemcode9() != null && vo.getItemcode9().length() > 0) {
			EhVoucherItemVO itemvo9 = new EhVoucherItemVO();
			itemvo9.setItemname(hmitem.get("Item9"));
			itemvo9.setItemvalue(vo.getItemcode9());
			arr.add(itemvo9);
		}
		if (vo.getItemcode10() != null && vo.getItemcode10().length() > 0) {
			EhVoucherItemVO itemvo10 = new EhVoucherItemVO();
			itemvo10.setItemname(hmitem.get("Item10"));
			itemvo10.setItemvalue(vo.getItemcode10());
			arr.add(itemvo10);
		}
		if (vo.getItemcode11() != null && vo.getItemcode11().length() > 0) {
			EhVoucherItemVO itemvo11 = new EhVoucherItemVO();
			itemvo11.setItemname(hmitem.get("Item11"));
			itemvo11.setItemvalue(vo.getItemcode11());
			arr.add(itemvo11);
		}
		if (vo.getItemcode12() != null && vo.getItemcode12().length() > 0) {
			EhVoucherItemVO itemvo12 = new EhVoucherItemVO();
			itemvo12.setItemname(hmitem.get("Item12"));
			itemvo12.setItemvalue(vo.getItemcode12());
			arr.add(itemvo12);
		}
		if (vo.getItemcode13() != null && vo.getItemcode13().length() > 0) {
			EhVoucherItemVO itemvo13 = new EhVoucherItemVO();
			itemvo13.setItemname(hmitem.get("Item13"));
			itemvo13.setItemvalue(vo.getItemcode13());
			arr.add(itemvo13);
		}
		if (vo.getItemcode14() != null && vo.getItemcode14().length() > 0) {
			EhVoucherItemVO itemvo14 = new EhVoucherItemVO();
			itemvo14.setItemname(hmitem.get("Item14"));
			itemvo14.setItemvalue(vo.getItemcode14());
			arr.add(itemvo14);
		}
		if (vo.getItemcode15() != null && vo.getItemcode15().length() > 0) {
			EhVoucherItemVO itemvo15 = new EhVoucherItemVO();
			itemvo15.setItemname(hmitem.get("Item15"));
			itemvo15.setItemvalue(vo.getItemcode15());
			arr.add(itemvo15);
		}
		if (vo.getItemcode16() != null && vo.getItemcode16().length() > 0) {
			EhVoucherItemVO itemvo16 = new EhVoucherItemVO();
			itemvo16.setItemname(hmitem.get("Item16"));
			itemvo16.setItemvalue(vo.getItemcode16());
			arr.add(itemvo16);
		}
		if (vo.getItemcode17() != null && vo.getItemcode17().length() > 0) {
			EhVoucherItemVO itemvo17 = new EhVoucherItemVO();
			itemvo17.setItemname(hmitem.get("Item17"));
			itemvo17.setItemvalue(vo.getItemcode17());
			arr.add(itemvo17);
		}
		if (vo.getItemcode18() != null && vo.getItemcode18().length() > 0) {
			EhVoucherItemVO itemvo18 = new EhVoucherItemVO();
			itemvo18.setItemname(hmitem.get("Item18"));
			itemvo18.setItemvalue(vo.getItemcode18());
			arr.add(itemvo18);
		}
		if (vo.getItemcode19() != null && vo.getItemcode19().length() > 0) {
			EhVoucherItemVO itemvo19 = new EhVoucherItemVO();
			itemvo19.setItemname(hmitem.get("Item19"));
			itemvo19.setItemvalue(vo.getItemcode19());
			arr.add(itemvo19);
		}
		if (vo.getItemcode20() != null && vo.getItemcode20().length() > 0) {
			EhVoucherItemVO itemvo20 = new EhVoucherItemVO();
			itemvo20.setItemname(hmitem.get("Item20"));
			itemvo20.setItemvalue(vo.getItemcode20());
			arr.add(itemvo20);
		}
		bodyvo.setItems(arr);

		// �ֽ���ת�����뼯����
		ArrayList<EhVoucherCashVO> arrxj = new ArrayList<EhVoucherCashVO>();
		if (vo.getPk_cashflow1() != null && vo.getPk_cashflow1().length() > 0) {
			EhVoucherCashVO cashvo1 = new EhVoucherCashVO();
			cashvo1.setPk_cashflow(vo.getPk_cashflow1());
			cashvo1.setMoney(vo.getMoney1() == null ? "0.00" : vo.getMoney1().toString());
			arrxj.add(cashvo1);
		}
		if (vo.getPk_cashflow2() != null && vo.getPk_cashflow2().length() > 0) {
			EhVoucherCashVO cashvo2 = new EhVoucherCashVO();
			cashvo2.setPk_cashflow(vo.getPk_cashflow2());
			cashvo2.setMoney(vo.getMoney2() == null ? "0.00" : vo.getMoney2()
					.toString());
			arrxj.add(cashvo2);
		}
		if (vo.getPk_cashflow3() != null && vo.getPk_cashflow3().length() > 0) {
			EhVoucherCashVO cashvo3 = new EhVoucherCashVO();
			cashvo3.setPk_cashflow(vo.getPk_cashflow3());
			cashvo3.setMoney(vo.getMoney3() == null ? "0.00" : vo.getMoney3()
					.toString());
			arrxj.add(cashvo3);
		}
		bodyvo.setCashflows(arrxj);

		return bodyvo;
	}

	/***
	 * �õ���ǰ��˾��ǰ�·�����ƾ֤��
	 * 
	 * @return
	 */
	public String getMaxVoucherid() {
		String voucherid = "0";
		String sql = "select nvl(max(no),0) voucherid from gl_voucher a	where a.pk_corp = '"
				+ _getCorp().getPk_corp()
				+ "' and a.year = '"
				+ _getDate().getYear()
				+ "' and a.period = "
				+ _getDate().getMonth() + " and nvl(a.dr,0)=0";
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		try {
			Object obj = iUAPQueryBS.executeQuery(sql, new ColumnProcessor());
			voucherid = obj == null ? voucherid : obj.toString();
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return voucherid;
	}
	
	/***
	 *	��ĿУ��
	 * @return
	 * @throws Exception
	 */
	public String validateItem() throws Exception{
		StringBuffer res = new StringBuffer();
		PfxxVoucherVO[] bodys = (PfxxVoucherVO[])getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
		HashMap<String, String> items = getItems();	
		if(bodys!=null&&bodys.length>0){
			int re = 0;
			for (int i = 0; i <bodys.length; i++) {
				for (int j = 1;j <= 20; j++) {
					String itemcode = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"itemcode" + j) == null ? null:
								getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "itemcode" + j).toString();
					if (itemcode != null && itemcode.length() > 0&&!items.containsKey("Item"+j)) { // ��ֵ
						res.append("��Ŀ" + j + "û�ж��յ���,��������ֵ!�����ø�����Ŀ����!\r\n");
						re = 1;
						break;
					}
				}
				if(re==1){
					break;
				}
			}
		}
		return res.toString();
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
}
