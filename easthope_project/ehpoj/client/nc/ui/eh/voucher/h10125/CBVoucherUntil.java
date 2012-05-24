package nc.ui.eh.voucher.h10125;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.bd.CorpVO;
import nc.vo.eh.cw.h11055.ArapCosthsVO;
import nc.vo.eh.ipub.Iinvtype;
import nc.vo.eh.ipub.InvtypeSQL;
import nc.vo.eh.voucher.h10115.EhVoucherBodyVO;
import nc.vo.eh.voucher.h10115.EhVoucherCashVO;
import nc.vo.eh.voucher.h10115.EhVoucherHeadVO;
import nc.vo.eh.voucher.h10115.EhVoucherItemVO;
import nc.vo.eh.voucher.h10115.EhVoucherVO;
import nc.vo.eh.voucher.h10115.PfxxVoucherVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class CBVoucherUntil {
	
	
	ArapCosthsVO costvo;
	String pk_corp;
	String unitcode;				//��˾����
	UFDate date;
	String pk_period;
	int nyear;
	int nmonth;
	String cusercode;				//�û�����
	IUAPQueryBS iUAPQueryBS = null;
	@SuppressWarnings("unchecked")
	HashMap hmKM = null;			//��Ŀ
	
	int maxvoucherid = 0;			//����ƾ֤��
	
	public CBVoucherUntil(ArapCosthsVO costVO){
		setCostvo(costVO);
		pk_corp = costVO.getPk_corp();
		date = costVO.getCalcdate();
		nyear = date.getYear();
		nmonth = date.getMonth();
		cusercode = costVO.getCoperatorid();
		unitcode = costVO.getMemo();
		pk_period=costVO.getPk_period();
		
		iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		maxvoucherid = Integer.parseInt(getMaxVoucherid(pk_corp,nyear,nmonth)); // ����ƾ֤��
		
		hmKM = getKMcode();
	}
	
	public ArapCosthsVO getCostvo() {
		return costvo;
	}


	public void setCostvo(ArapCosthsVO costvo) {
		this.costvo = costvo;
	}


	/**
	 * ǰ̨���ݿ�����ӿ�
	 * */
	public IUAPQueryBS getIUAPQueryBS(){
		if(iUAPQueryBS==null){
			iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		}
		return iUAPQueryBS;
	}
	
	
	
	
	/**
	 * �õ�����ƾ֤������ 
	 * hm<ƾ֤��,ƾ֤����>
	 * */
	public HashMap<String, EhVoucherVO> getALLVoucher(){
		HashMap<String, EhVoucherVO> hm = new HashMap<String, EhVoucherVO>();
		maxvoucherid = maxvoucherid+1;
		
		/**1.ԭ����ƾ֤**/
		EhVoucherVO yclVO = getVoucherVO(ItfVoucherType.YL_VOUCHER);
		if(yclVO!=null){
			hm.put(String.valueOf(maxvoucherid), yclVO);
		}
		
		/**2.��װ��ƾ֤**/
		EhVoucherVO bzVO = getVoucherVO(ItfVoucherType.BZ_VOUCHER);
		if(bzVO!=null){
			hm.put(String.valueOf(maxvoucherid), bzVO);
		}
		
		/**3.���ƾ֤**/
		EhVoucherVO dfVO = getVoucherVO(ItfVoucherType.DF_VOUCHER);
		if(dfVO!=null){
			hm.put(String.valueOf(maxvoucherid), dfVO);
		}
		
		/**4.ȼ��ƾ֤**/
		EhVoucherVO rlVO = getVoucherVO(ItfVoucherType.RLF_VOUCHER);
		if(rlVO!=null){
			hm.put(String.valueOf(maxvoucherid), rlVO);
		}
		
		/**5.����ƾ֤**/
		EhVoucherVO gzVO = getVoucherVO(ItfVoucherType.GZ_VOUCHER);
		if(gzVO!=null){
			hm.put(String.valueOf(maxvoucherid), gzVO);
		}
		
		/**6.�������ƾ֤**/
		EhVoucherVO zzfVO = getVoucherVO(ItfVoucherType.ZZF_VOUCHER);
		if(zzfVO!=null){
			hm.put(String.valueOf(maxvoucherid), zzfVO);
		}
		
		return hm;
	}
	
	/***
	 * �õ�����ƾ֤������
	 * @param vouchertype ԭ���ϡ���װ����ѡ�ȼ�Ϸѡ����ʡ��������
	 * @return
	 */
	public EhVoucherVO getVoucherVO(String vouchertype){
		EhVoucherVO voucherVO = new EhVoucherVO();
		
		/**ƾ֤ͷ*/
		EhVoucherHeadVO headVO = getVoucherHeadVO();
		voucherVO.setHeadvo(headVO);
		
		/**ƾ֤��¼**/
		ArrayList<EhVoucherBodyVO> bodyVOs= getBodyVO(vouchertype);
		voucherVO.setBodyvos(bodyVOs);
		
		if(bodyVOs==null||bodyVOs.size()==0){
			voucherVO = null;
		}
		
		return voucherVO;
	}
	
	/***
	 * ȡƾ֤��¼����
	 * ���ݲ�ͬ��ƾ֤��¼��ͬ 
	 * @return
	 */
	public ArrayList<EhVoucherBodyVO> getBodyVO(String vouchertype){
		ArrayList<EhVoucherBodyVO> arr = new ArrayList<EhVoucherBodyVO>();
		
		
		/**������¼*/
		EhVoucherBodyVO[] dfvos = getDFbodyVOs(vouchertype);
		/**�跽��¼*/
		EhVoucherBodyVO[] jfvos = getJFbodyVOs(vouchertype);
		
		if((jfvos!=null&&jfvos.length>0)&&(dfvos!=null&&dfvos.length>0)){			//�������跽��������ʱ����ƾ֤
			arr = dealBodyArr(arr, jfvos);
			arr = dealBodyArr(arr, dfvos);
		}
		return arr;
	}
	
	/***
	 * ������VO����arr��
	 * @param arr
	 * @param bodyVOs
	 * @return
	 */
	public ArrayList<EhVoucherBodyVO> dealBodyArr(ArrayList<EhVoucherBodyVO> arr,EhVoucherBodyVO[] bodyVOs){
		if(arr!=null){
			if(bodyVOs!=null&&bodyVOs.length>0){
				for(int i=0;i<bodyVOs.length;i++){
					arr.add(bodyVOs[i]);
				}
			}
		}
		return arr;
	}
	/***
	 * �跽��¼���� �跽һ��
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public EhVoucherBodyVO[] getJFbodyVOs(String vouchertype){
		String abstractinfo = null;
		if(vouchertype.equals(ItfVoucherType.YL_VOUCHER)){
			abstractinfo = "���º���ԭ����";
		}
		if(vouchertype.equals(ItfVoucherType.BZ_VOUCHER)){
			abstractinfo = "���º��ð�װ";
		}
		if(vouchertype.equals(ItfVoucherType.GZ_VOUCHER)){
			abstractinfo = "���·��乤��";
		}
		if(vouchertype.equals(ItfVoucherType.RLF_VOUCHER)){
			abstractinfo = "���º���ȼ��";
		}
		if(vouchertype.equals(ItfVoucherType.DF_VOUCHER)){
			abstractinfo = "���º��õ��";
		}
		if(vouchertype.equals(ItfVoucherType.ZZF_VOUCHER)){
			abstractinfo = "���·����������";
		} 
		EhVoucherBodyVO[] bodyVOs = null;
		StringBuffer sql = new StringBuffer()
		.append("  SELECT d.invcode,b.def_6 sl,b."+vouchertype+" je")
		.append("  FROM eh_arap_cosths a,eh_arap_cosths_b b,bd_invmandoc c,bd_invbasdoc d")
		.append("  WHERE a.pk_cosths = b.pk_cosths")
		.append("  AND b.pk_costobj_b = c.pk_invmandoc")
		.append("  AND c.pk_invbasdoc = d.pk_invbasdoc")
		.append("  AND a.pk_corp = '"+pk_corp+"' AND c.pk_corp = '"+pk_corp+"'")
		.append("  AND a.nyear = "+nyear+" AND a.nmonth = "+nmonth+" ")
		.append("  AND NVL(a.dr,0) = 0 AND NVL(b.dr,0) = 0 and b."+vouchertype+" <>0")
		.append("  ORDER BY d.invcode");
		try {
			ArrayList arr = (ArrayList)getIUAPQueryBS().executeQuery(sql.toString(), new MapListProcessor());
			if(arr!=null&&arr.size()>0){
				String invcode = null;					//�����������
				UFDouble sl = new UFDouble();			//����	
				UFDouble je = new UFDouble();			//���
				bodyVOs = new EhVoucherBodyVO[arr.size()];
				for(int i=0;i<arr.size();i++){
					HashMap hmA = (HashMap)arr.get(i);
					invcode = hmA.get("invcode")==null?"":hmA.get("invcode").toString();	
					sl = new UFDouble(hmA.get("sl")==null?"0":hmA.get("sl").toString());
					je = new UFDouble(hmA.get("je")==null?"0":hmA.get("je").toString());
					
					PfxxVoucherVO pfxxVO = new PfxxVoucherVO();
					pfxxVO.setAbstractinfo(abstractinfo);						//ժҪ
					pfxxVO.setAccount_code(hmKM.get(vouchertype)==null?"":hmKM.get(vouchertype).toString());      			//��Ŀ����
					pfxxVO.setNatural_debit_currency(je);			//�跽���
					if(vouchertype.equals(ItfVoucherType.YL_VOUCHER)){	//ԭ����ʱ��������,��Ĳ������� edit by wb 2010��1��21��15:08:34
						pfxxVO.setDebit_quantity(sl);					//�跽����
					}
					pfxxVO.setCurrency(hmKM.get("currencycode")==null?"":hmKM.get("currencycode").toString());				//����
					
					/**��������**/
					pfxxVO.setItemname1("�����������");
					pfxxVO.setItemcode1(invcode);
					
					pfxxVO.setItemname2("���ŵ���");
					pfxxVO.setItemcode2(hmKM.get("deptcode")==null?"":hmKM.get("deptcode").toString());
					
					bodyVOs[i] = dealBodyVO(pfxxVO);
				}
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return bodyVOs;
	}
	
	/***
	 * ������¼����
	 * 1 ԭ����ƾ֤
        ƾ֤�ӿڣ�
           �裺50010101�����ɱ�-���������ɱ�-ԭ����(��Ʒ������)
           ����1403ԭ���ϣ�ԭ�ϣ�
	   2 ��װ��ƾ֤
        ƾ֤�ӿڣ�
           �裺50010102�����ɱ�-���������ɱ�-��װ(��Ʒ������)
           ����1403ԭ���ϣ���װ��
	   3 ����ƾ֤
   		ƾ֤�ӿڣ�
           �裺50010103�����ɱ�-���������ɱ�-����(��Ʒ������)
           ����221101Ӧ������-����(������)
	   4 ���ƾ֤
       	ƾ֤�ӿڣ�
           �裺50010104020101�����ɱ�-���������ɱ�-���(��Ʒ������)
           ����1403ԭ���ϣ���ѣ�
	   5 ȼ��ƾ֤
       	ƾ֤�ӿڣ�
           �裺50010104010201 etc �����ɱ�-���������ɱ�-ȼ�Ϸ�(��Ʒ������)
           ����1403ԭ���ϣ�ȼ�ϣ�
	   6 �������ƾ֤
		ƾ֤�ӿڣ�
            �裺50010105�����ɱ�-���������ɱ�-�������(��Ʒ������)
            �����������  ��Ŀ������5101�����е���ϸ�������������㣩

	 * @param vouchertype
	 * @return
	 */
	public EhVoucherBodyVO[] getDFbodyVOs(String vouchertype){
		EhVoucherBodyVO[] bodyVOs = null;
		
		/**1��2 ԭ�ϻ��װ������¼**/
		if(vouchertype.equals(ItfVoucherType.YL_VOUCHER)||vouchertype.equals(ItfVoucherType.BZ_VOUCHER)){
			//bodyVOs = getYLBZdfVOS(vouchertype);	
			bodyVOs = getYLBZdfVOSNew(vouchertype);//modify by houcq 2011-07-15
		}
		
		/**3��4��5 ���� ��� ȼ�Ϸ� ������¼**/
		if(vouchertype.equals(ItfVoucherType.GZ_VOUCHER)||vouchertype.equals(ItfVoucherType.DF_VOUCHER)
				||vouchertype.equals(ItfVoucherType.RLF_VOUCHER)){
			bodyVOs = get345dfVOS(vouchertype);
		}
		
		/**6.������ô�����¼**/
		if(vouchertype.equals(ItfVoucherType.ZZF_VOUCHER)){
			bodyVOs = getZZFdfVOS(vouchertype);
		}
		
		return bodyVOs;
	}
	
	/***
	 * 6.������ô�����¼����
	 *	��Ŀ������5101�����е���ϸ�������������㣩
	 * @param vouchertype
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private EhVoucherBodyVO[] getZZFdfVOS(String vouchertype) {
		EhVoucherBodyVO[] bodyVOs = null;
		String zzkmbm = hmKM.get("dfpfcode")==null?"":hmKM.get("dfpfcode").toString();			//������ÿ�Ŀ����
		StringBuffer sql = new StringBuffer()
		.append("  SELECT b.subjcode,b.subjname,a.assid,a.localdebitamount je,a.debitquantity sl")
		.append("  FROM gl_balance a,bd_accsubj b")
		.append("  WHERE a.pk_accsubj = b.pk_accsubj")
		.append("  AND b.subjcode LIKE '"+zzkmbm+"%'")
		.append("  AND a.pk_corp = '"+pk_corp+"' AND YEAR = "+nyear+" AND period = "+nmonth+" ")
		.append("  AND NVL(a.dr,0) = 0 and nvl(a.localdebitamount,0)<>0")
		.append("  ORDER BY b.subjcode");
		try {
			ArrayList arr = (ArrayList)getIUAPQueryBS().executeQuery(sql.toString(), new MapListProcessor());
			if(arr!=null&&arr.size()>0){
				HashMap<String,ArrayList<EhVoucherItemVO>> hmfz = getFZhs();				//����������õĸ�������
				String subjcode = null;					//��Ŀ����
				String fzid = null;						//����ID
				UFDouble sl = new UFDouble();			//����	
				UFDouble je = new UFDouble();			//���
				bodyVOs = new EhVoucherBodyVO[arr.size()];
				for(int i=0;i<arr.size();i++){
					HashMap hmA = (HashMap)arr.get(i);
					subjcode = hmA.get("subjcode")==null?"":hmA.get("subjcode").toString();	
					fzid = hmA.get("assid")==null?"":hmA.get("assid").toString();	
					sl = new UFDouble(hmA.get("sl")==null?"0":hmA.get("sl").toString());
					je = new UFDouble(hmA.get("je")==null?"0":hmA.get("je").toString());
					
					PfxxVoucherVO pfxxVO = new PfxxVoucherVO();
					pfxxVO.setAbstractinfo("���·����������");						//ժҪ
					pfxxVO.setAccount_code(subjcode);      			//��Ŀ���� 1403	
					pfxxVO.setNatural_credit_currency(je);			//�������
					pfxxVO.setCredit_quantity(sl);					//��������
					pfxxVO.setCurrency(hmKM.get("currencycode")==null?"":hmKM.get("currencycode").toString());				//����
					
					/**��������**/
					ArrayList<EhVoucherItemVO> arrfz = hmfz.get(fzid);
					if(arrfz!=null&&arrfz.size()>0){
						for(int j=0;j<arrfz.size();j++){
							EhVoucherItemVO itemVO = arrfz.get(j);
							pfxxVO.setAttributeValue("itemname"+(j+1), itemVO.getItemname());
							pfxxVO.setAttributeValue("itemcode"+(j+1), itemVO.getItemvalue());
						}
					}
					
					bodyVOs[i] = dealBodyVO(pfxxVO);
				}
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return bodyVOs;
	}

	/***
	 * ������ô�����������
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String,ArrayList<EhVoucherItemVO>> getFZhs(){
		String zzkmbm = hmKM.get("dfpfcode")==null?"":hmKM.get("dfpfcode").toString();			//������ÿ�Ŀ����
		HashMap<String,ArrayList<EhVoucherItemVO>> hm = new HashMap<String, ArrayList<EhVoucherItemVO>>();
		StringBuffer sql = new StringBuffer()
		.append("  SELECT a.freevalueid,b.bdname,a.valuecode,a.valuename")
		.append("  FROM gl_freevalue a,bd_bdinfo b")
		.append("  WHERE a.checktype = b.pk_bdinfo")
		.append("  AND a.freevalueid IN (")
		.append("       SELECT a.assid")
		.append("       FROM gl_balance a,bd_accsubj b")
		.append("       WHERE a.pk_accsubj = b.pk_accsubj")
		.append("       AND b.subjcode LIKE '"+zzkmbm+"%'")
		.append("       AND a.pk_corp = '"+pk_corp+"' AND YEAR = "+nyear+" AND period = "+nmonth+" ")
		.append("        AND NVL(a.dr,0)=0")
		.append("  )")
		.append("  ORDER BY freevalueid ");
		try {
			ArrayList arr = (ArrayList)getIUAPQueryBS().executeQuery(sql.toString(), new MapListProcessor());
			if(arr!=null&&arr.size()>0){
				String fzid = null;						//����ID
				String fzname = null;					//��������
				String fzvlaue = null;					//����ֵ
				ArrayList<EhVoucherItemVO> arrFZ = null;
				for(int i=0;i<arr.size();i++){
					HashMap hmA = (HashMap)arr.get(i);
					fzid = hmA.get("freevalueid")==null?"":hmA.get("freevalueid").toString();	
					fzname = hmA.get("bdname")==null?"":hmA.get("bdname").toString();	
					fzvlaue = hmA.get("valuecode")==null?"":hmA.get("valuecode").toString();
					
					EhVoucherItemVO itemVO = new EhVoucherItemVO();
					itemVO.setItemname(fzname);
					itemVO.setItemvalue(fzvlaue);
					
					if(hm.containsKey(fzid)){
						hm.get(fzid).add(itemVO);
					}else{
						arrFZ = new ArrayList<EhVoucherItemVO>();
						arrFZ.add(itemVO);
						hm.put(fzid,arrFZ);
					}
				}
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return hm;
	}
	
	/***
	 * 3��4��5 ���� ��� ȼ�Ϸ� ������¼����
	 * 3 ����ƾ֤
   		ƾ֤�ӿڣ�
           �裺50010103�����ɱ�-���������ɱ�-����(��Ʒ������)
           ����221101Ӧ������-����(������)
	   4 ���ƾ֤
       	ƾ֤�ӿڣ�
           �裺50010104020101�����ɱ�-���������ɱ�-���(��Ʒ������)
           ����1403ԭ���ϣ���ѣ�
	   5 ȼ��ƾ֤
       	ƾ֤�ӿڣ�
           �裺50010104010201 etc �����ɱ�-���������ɱ�-ȼ�Ϸ�(��Ʒ������)
           ����1403ԭ���ϣ�ȼ�ϣ�
	 * @param vouchertype
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private EhVoucherBodyVO[] get345dfVOS(String vouchertype) {
		EhVoucherBodyVO[] bodyVOs = new EhVoucherBodyVO[1];
		String kmcode = null;			//��Ŀ
		String fzhsname = null;			//������������
		String fzhscode = null;			//�����������
		String abstractinfo = null;		//ժҪ
		HashMap hmSum = get345Sum();
		if(vouchertype.equals(ItfVoucherType.GZ_VOUCHER)){			//����
			kmcode = "dfgzcode";
			fzhsname = "���ŵ���";
			fzhscode = hmKM.get("deptcode")==null?"":hmKM.get("deptcode").toString();
			abstractinfo = "���·��乤��";
		}
		if(vouchertype.equals(ItfVoucherType.DF_VOUCHER)){			//���
			kmcode = "dfylcode";
			fzhsname = "�����������";
			fzhscode = hmKM.get("dfinvcode")==null?"":hmKM.get("dfinvcode").toString();
			abstractinfo = "���º��õ��";
		}
		if(vouchertype.equals(ItfVoucherType.RLF_VOUCHER)){			//ȼ��
			kmcode = "dfylcode";
			fzhsname = "�����������";
			fzhscode = hmKM.get("rlinvcode")==null?"":hmKM.get("rlinvcode").toString();
			abstractinfo = "���º���ȼ��";
		}
		
		UFDouble sl = new UFDouble(0);
		UFDouble je = new UFDouble(hmSum.get(vouchertype)==null?"0":hmSum.get(vouchertype).toString());
		
		PfxxVoucherVO pfxxVO = new PfxxVoucherVO();
		pfxxVO.setAbstractinfo(abstractinfo);						//ժҪ
		pfxxVO.setAccount_code(hmKM.get(kmcode)==null?"":hmKM.get(kmcode).toString());      			//��Ŀ���� 1403	
		pfxxVO.setNatural_credit_currency(je);			//�������
		pfxxVO.setCredit_quantity(sl);					//��������
		pfxxVO.setCurrency(hmKM.get("currencycode")==null?"":hmKM.get("currencycode").toString());				//����
		
		/**��������**/
		pfxxVO.setItemname1(fzhsname);
		pfxxVO.setItemcode1(fzhscode);
		bodyVOs[0] = dealBodyVO(pfxxVO);
		return bodyVOs;
	}

	/***
	 * �õ��ϼƹ��ʡ�ȼ�Ϸѡ���ѡ��������
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HashMap get345Sum(){
		HashMap hm = new HashMap();
		StringBuffer sql = new StringBuffer()
		.append("  SELECT SUM(NVL(b.gz,0)) gz,SUM(NVL(b.rlf,0)) rlf,SUM(NVL(b.df,0)) df,SUM(NVL(b.pf,0)) pf")
		.append("  FROM eh_arap_cosths a,eh_arap_cosths_b b,bd_invmandoc c,bd_invbasdoc d")
		.append("  WHERE a.pk_cosths = b.pk_cosths")
		.append("  AND b.pk_costobj_b = c.pk_invmandoc")
		.append("  AND c.pk_invbasdoc = d.pk_invbasdoc")
		.append("  AND a.pk_corp = '"+pk_corp+"' AND c.pk_corp = '"+pk_corp+"'")
		.append("  AND a.nyear = "+nyear+" AND a.nmonth = "+nmonth+" ")
		.append("  AND NVL(a.dr,0) = 0 AND NVL(b.dr,0) = 0 ");
		try {
			ArrayList arr = (ArrayList)getIUAPQueryBS().executeQuery(sql.toString(), new MapListProcessor());
			if(arr!=null&&arr.size()>0){
				hm = (HashMap)arr.get(0);
			}
		}catch (BusinessException e) {
			e.printStackTrace();
		}
		return hm;
	}
	
	/***
	 * ԭ�ϡ���װ�Ĵ�����¼����
	 * 1 ԭ����ƾ֤
        ƾ֤�ӿڣ�
           �裺50010101�����ɱ�-���������ɱ�-ԭ����(��Ʒ������)
           ����1403ԭ���ϣ�ԭ�ϣ�
	   2 ��װ��ƾ֤
        ƾ֤�ӿڣ�
           �裺50010102�����ɱ�-���������ɱ�-��װ(��Ʒ������)
           ����1403ԭ���ϣ���װ��
	 * @param vouchertype
	 * @return
	 */	
	@SuppressWarnings("unchecked")
	public EhVoucherBodyVO[] getYLBZdfVOS(String vouchertype){
		EhVoucherBodyVO[] bodyVOs = null;
		String[] invcodes = Iinvtype.yl;
		String abstractinfo = "���º���ԭ����";
		if(vouchertype.equals(ItfVoucherType.BZ_VOUCHER)){
			invcodes = Iinvtype.bz;
			abstractinfo = "���º��ð�װ";
		}
		StringBuffer sql =  new StringBuffer()							//ȡ��ԭ�ϻ��װ��������� ԭ�ϻ��װ�ĳ����������
		.append("  SELECT d.invcode,b.cksl sl,b.ckje je")
		.append("  FROM eh_calc_kcybb a,eh_calc_kcybb_b b,bd_invmandoc c,bd_invbasdoc d")
		.append("  WHERE a.pk_kcybb = b.pk_kcybb")
		.append("  AND b.pk_invbasdoc = c.pk_invmandoc")
		.append("  AND c.pk_invbasdoc = d.pk_invbasdoc")
		.append("  AND a.pk_corp = '"+pk_corp+"' AND c.pk_corp = '"+pk_corp+"'")
		.append("  AND a.nyear = "+nyear+" AND a.nmonth = "+nmonth+"")
		.append("  AND ("+InvtypeSQL.getInvBySecendCatalog(invcodes, "d")+") ")
		.append("  AND a.invtype = 'Y' and b.ckje<>0")
		.append("  AND NVL(a.dr,0) = 0 AND NVL(b.dr,0) = 0 ")
		.append("  ORDER BY d.invcode");
		try {
			ArrayList arr = (ArrayList)getIUAPQueryBS().executeQuery(sql.toString(), new MapListProcessor());
			if(arr!=null&&arr.size()>0){
				String invcode = null;					//�����������
				UFDouble sl = new UFDouble();			//����	
				UFDouble je = new UFDouble();			//���
				bodyVOs = new EhVoucherBodyVO[arr.size()];
				for(int i=0;i<arr.size();i++){
					HashMap hmA = (HashMap)arr.get(i);
					invcode = hmA.get("invcode")==null?"":hmA.get("invcode").toString();	
					sl = new UFDouble(hmA.get("sl")==null?"0":hmA.get("sl").toString());
					je = new UFDouble(hmA.get("je")==null?"0":hmA.get("je").toString());
					
					PfxxVoucherVO pfxxVO = new PfxxVoucherVO();
					pfxxVO.setAbstractinfo(abstractinfo);						//ժҪ
					pfxxVO.setAccount_code(hmKM.get("dfylcode")==null?"":hmKM.get("dfylcode").toString());      			//��Ŀ���� 1403	
					pfxxVO.setNatural_credit_currency(je);			//�������
					pfxxVO.setCredit_quantity(sl);					//��������
					pfxxVO.setCurrency(hmKM.get("currencycode")==null?"":hmKM.get("currencycode").toString());				//����
					
					/**��������**/
					pfxxVO.setItemname1("�����������");
					pfxxVO.setItemcode1(invcode);
					bodyVOs[i] = dealBodyVO(pfxxVO);
				}
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return bodyVOs;
	}
	/***
	 *modify by houcq 2011-07-15
	 *ԭ�ϰ�װȡ����Ϊ�Ӳ��ϳ��ⵥ��ȡ
	 * ԭ�ϡ���װ�Ĵ�����¼����
	 * 1 ԭ����ƾ֤
        ƾ֤�ӿڣ�
           �裺50010101�����ɱ�-���������ɱ�-ԭ����(��Ʒ������)
           ����1403ԭ���ϣ�ԭ�ϣ�
	   2 ��װ��ƾ֤
        ƾ֤�ӿڣ�
           �裺50010102�����ɱ�-���������ɱ�-��װ(��Ʒ������)
           ����1403ԭ���ϣ���װ��
	 * @param vouchertype
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public EhVoucherBodyVO[] getYLBZdfVOSNew(String vouchertype){
		EhVoucherBodyVO[] bodyVOs = null;
		String abstractinfo = "���º���ԭ����";
		String code ="'01%'";
		if(vouchertype.equals(ItfVoucherType.BZ_VOUCHER)){
			abstractinfo = "���º��ð�װ";
			code="'07%'";
		}
		StringBuffer sql =  new StringBuffer()	
		.append(" select * from (")
		//.append(" select c.invcode,sum(a.ylsl) ylsl,sum(a.je) je from eh_cb_detail a,bd_invmandoc b,bd_invbasdoc c")
		.append(" select c.invcode,sum(a.ylsl) sl,sum(a.je) je from eh_cb_detail a,bd_invmandoc b,bd_invbasdoc c")
		.append(" where a.yl_pk_invbasdoc=b.pk_invmandoc")
		.append(" and a.pk_corp='"+pk_corp+"'")
		.append(" and a.pk_period='"+pk_period+"'")
		.append(" and b.pk_invbasdoc=c.pk_invbasdoc")
		.append(" AND c.invcode like "+code)
		.append(" group by c.invcode")
		.append(" ) order by invcode");
		try {
			ArrayList arr = (ArrayList)getIUAPQueryBS().executeQuery(sql.toString(), new MapListProcessor());
			if(arr!=null&&arr.size()>0){
				String invcode = null;					//�����������
				UFDouble sl = new UFDouble();			//����	
				UFDouble je = new UFDouble();			//���
				bodyVOs = new EhVoucherBodyVO[arr.size()];
				for(int i=0;i<arr.size();i++){
					HashMap hmA = (HashMap)arr.get(i);
					invcode = hmA.get("invcode")==null?"":hmA.get("invcode").toString();	
					sl = new UFDouble(hmA.get("sl")==null?"0":hmA.get("sl").toString());
					je = new UFDouble(hmA.get("je")==null?"0":hmA.get("je").toString());
					
					PfxxVoucherVO pfxxVO = new PfxxVoucherVO();
					pfxxVO.setAbstractinfo(abstractinfo);						//ժҪ
					pfxxVO.setAccount_code(hmKM.get("dfylcode")==null?"":hmKM.get("dfylcode").toString());      			//��Ŀ���� 1403	
					pfxxVO.setNatural_credit_currency(je);			//�������
					pfxxVO.setCredit_quantity(sl);					//��������
					pfxxVO.setCurrency(hmKM.get("currencycode")==null?"":hmKM.get("currencycode").toString());				//����
					
					/**��������**/
					pfxxVO.setItemname1("�����������");
					pfxxVO.setItemcode1(invcode);
					bodyVOs[i] = dealBodyVO(pfxxVO);
				}
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return bodyVOs;
	}
	/***
	 * ƾ֤ͷ���ݴ���
	 * @return
	 */
	public EhVoucherHeadVO getVoucherHeadVO(){
		maxvoucherid = maxvoucherid+1;
		String voucher_id = String.valueOf(maxvoucherid);
		
		EhVoucherHeadVO headvo = new EhVoucherHeadVO();
		headvo.setUnitecode(unitcode);
		headvo.setNyear(String.valueOf(nyear));
		headvo.setNmonth(String.valueOf(nmonth));
		headvo.setVoucher_type("����ƾ֤");
		headvo.setBusinesscode(voucher_id);
		headvo.setVoucher_id(voucher_id);
		headvo.setPrepareddate(date.toString());
		headvo.setAttachment_number("0");
		headvo.setEnter(cusercode);
		headvo.setMemo(null);
		return headvo;
	}
	
	
	/***
	 * �����ƾ֤��¼����
	 * @param vo
	 * @return
	 */
	public EhVoucherBodyVO dealBodyVO(PfxxVoucherVO vo) {
		EhVoucherBodyVO bodyvo = new EhVoucherBodyVO();
		
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

		// ��Ŀת�����뼯����
		ArrayList<EhVoucherItemVO> arr = new ArrayList<EhVoucherItemVO>();
		if (vo.getItemcode1() != null && vo.getItemcode1().length() > 0) {
			EhVoucherItemVO itemvo1 = new EhVoucherItemVO();
			itemvo1.setItemname(vo.getItemname1());
			itemvo1.setItemvalue(vo.getItemcode1());
			arr.add(itemvo1);
		}
		if (vo.getItemcode2() != null && vo.getItemcode2().length() > 0) {
			EhVoucherItemVO itemvo2 = new EhVoucherItemVO();
			itemvo2.setItemname(vo.getItemname2());
			itemvo2.setItemvalue(vo.getItemcode2());
			arr.add(itemvo2);
		}
		if (vo.getItemcode3() != null && vo.getItemcode3().length() > 0) {
			EhVoucherItemVO itemvo3 = new EhVoucherItemVO();
			itemvo3.setItemname(vo.getItemname3());
			itemvo3.setItemvalue(vo.getItemcode3());
			arr.add(itemvo3);
		}
		if (vo.getItemcode4() != null && vo.getItemcode4().length() > 0) {
			EhVoucherItemVO itemvo4 = new EhVoucherItemVO();
			itemvo4.setItemname(vo.getItemname4());
			itemvo4.setItemvalue(vo.getItemcode4());
			arr.add(itemvo4);
		}
		if (vo.getItemcode5() != null && vo.getItemcode5().length() > 0) {
			EhVoucherItemVO itemvo5 = new EhVoucherItemVO();
			itemvo5.setItemname(vo.getItemname5());
			itemvo5.setItemvalue(vo.getItemcode5());
			arr.add(itemvo5);
		}
		if (vo.getItemcode6() != null && vo.getItemcode6().length() > 0) {
			EhVoucherItemVO itemvo6 = new EhVoucherItemVO();
			itemvo6.setItemname(vo.getItemname6());
			itemvo6.setItemvalue(vo.getItemcode6());
			arr.add(itemvo6);
		}
		if (vo.getItemcode7() != null && vo.getItemcode7().length() > 0) {
			EhVoucherItemVO itemvo7 = new EhVoucherItemVO();
			itemvo7.setItemname(vo.getItemname7());
			itemvo7.setItemvalue(vo.getItemcode7());
			arr.add(itemvo7);
		}
		if (vo.getItemcode8() != null && vo.getItemcode8().length() > 0) {
			EhVoucherItemVO itemvo8 = new EhVoucherItemVO();
			itemvo8.setItemname(vo.getItemname8());
			itemvo8.setItemvalue(vo.getItemcode8());
			arr.add(itemvo8);
		}
		if (vo.getItemcode9() != null && vo.getItemcode9().length() > 0) {
			EhVoucherItemVO itemvo9 = new EhVoucherItemVO();
			itemvo9.setItemname(vo.getItemname9());
			itemvo9.setItemvalue(vo.getItemcode9());
			arr.add(itemvo9);
		}
		if (vo.getItemcode10() != null && vo.getItemcode10().length() > 0) {
			EhVoucherItemVO itemvo10 = new EhVoucherItemVO();
			itemvo10.setItemname(vo.getItemname10());
			itemvo10.setItemvalue(vo.getItemcode10());
			arr.add(itemvo10);
		}
		if (vo.getItemcode11() != null && vo.getItemcode11().length() > 0) {
			EhVoucherItemVO itemvo11 = new EhVoucherItemVO();
			itemvo11.setItemname(vo.getItemname11());
			itemvo11.setItemvalue(vo.getItemcode11());
			arr.add(itemvo11);
		}
		if (vo.getItemcode12() != null && vo.getItemcode12().length() > 0) {
			EhVoucherItemVO itemvo12 = new EhVoucherItemVO();
			itemvo12.setItemname(vo.getItemname12());
			itemvo12.setItemvalue(vo.getItemcode12());
			arr.add(itemvo12);
		}
		if (vo.getItemcode13() != null && vo.getItemcode13().length() > 0) {
			EhVoucherItemVO itemvo13 = new EhVoucherItemVO();
			itemvo13.setItemname(vo.getItemname13());
			itemvo13.setItemvalue(vo.getItemcode13());
			arr.add(itemvo13);
		}
		if (vo.getItemcode14() != null && vo.getItemcode14().length() > 0) {
			EhVoucherItemVO itemvo14 = new EhVoucherItemVO();
			itemvo14.setItemname(vo.getItemname14());
			itemvo14.setItemvalue(vo.getItemcode14());
			arr.add(itemvo14);
		}
		if (vo.getItemcode15() != null && vo.getItemcode15().length() > 0) {
			EhVoucherItemVO itemvo15 = new EhVoucherItemVO();
			itemvo15.setItemname(vo.getItemname15());
			itemvo15.setItemvalue(vo.getItemcode15());
			arr.add(itemvo15);
		}
		if (vo.getItemcode16() != null && vo.getItemcode16().length() > 0) {
			EhVoucherItemVO itemvo16 = new EhVoucherItemVO();
			itemvo16.setItemname(vo.getItemname16());
			itemvo16.setItemvalue(vo.getItemcode16());
			arr.add(itemvo16);
		}
		if (vo.getItemcode17() != null && vo.getItemcode17().length() > 0) {
			EhVoucherItemVO itemvo17 = new EhVoucherItemVO();
			itemvo17.setItemname(vo.getItemname17());
			itemvo17.setItemvalue(vo.getItemcode17());
			arr.add(itemvo17);
		}
		if (vo.getItemcode18() != null && vo.getItemcode18().length() > 0) {
			EhVoucherItemVO itemvo18 = new EhVoucherItemVO();
			itemvo18.setItemname(vo.getItemname18());
			itemvo18.setItemvalue(vo.getItemcode18());
			arr.add(itemvo18);
		}
		if (vo.getItemcode19() != null && vo.getItemcode19().length() > 0) {
			EhVoucherItemVO itemvo19 = new EhVoucherItemVO();
			itemvo19.setItemname(vo.getItemname19());
			itemvo19.setItemvalue(vo.getItemcode19());
			arr.add(itemvo19);
		}
		if (vo.getItemcode20() != null && vo.getItemcode20().length() > 0) {
			EhVoucherItemVO itemvo20 = new EhVoucherItemVO();
			itemvo20.setItemname(vo.getItemname20());
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
	public String getMaxVoucherid(String pk_corp,int nyear,int nmonth) {
		String voucherid = "0";
		String sql = "select nvl(max(no),0) voucherid from gl_voucher a	where a.pk_corp = '"
				+ pk_corp
				+ "' and a.year = '"
				+ nyear
				+ "' and a.period = "
				+ nmonth + " and nvl(a.dr,0)=0";
		try {
			Object obj = getIUAPQueryBS().executeQuery(sql, new ColumnProcessor());
			voucherid = obj == null ? voucherid : obj.toString();
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return voucherid;
	}

	
	/***
	 * �õ���˾����
	 * @param pk_corp
	 * @return
	 */
	public String getUnitCode(String pk_corp){
		String unitcode = null;
		try {
			CorpVO corpvo = (CorpVO)getIUAPQueryBS().retrieveByPK(CorpVO.class, pk_corp);
			if(corpvo!=null){
				unitcode = corpvo.getUnitcode();
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return unitcode;
	}
	
	/***
	 * �ɱ�ƾ֤��Ŀ��Ӧ
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HashMap getKMcode(){
		HashMap hm = new HashMap();
		String sql = "select ylcode materialfy ,bzcode packagefy,dfcode df,rlcode rlf,gzcode gz,ofcode pf ,dfylcode,dfgzcode,dfpfcode,rlinvcode,dfinvcode,deptcode,currencycode "+ 
					 "from eh_arap_cbdata where pk_corp = '"+pk_corp+"' and nvl(dr,0)=0 ";
		try {
			ArrayList arr = (ArrayList)getIUAPQueryBS().executeQuery(sql, new MapListProcessor());
			if(arr!=null&&arr.size()>0){
				hm = (HashMap)arr.get(0);
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return hm;
	}
	/*
	 * �õ�ԭ�ϰ�װ�跽��������� =����-�跽
	 */
	public UFDouble getJdcy(String pk_corp,int nyear,int nmonth,String vouchertype,String begindate,String enddate,String code)
	{
		UFDouble uf = new UFDouble(0);
		StringBuffer sql = new StringBuffer()
		.append(" select sum(jfje)-sum(dfje) jdcy from (")
		.append(" select sum(je) jfje,0 dfje from (  ")
		.append(" SELECT d.invcode,b.def_6 sl,b."+vouchertype+" je")
		.append(" FROM eh_arap_cosths a,eh_arap_cosths_b b,bd_invmandoc c,bd_invbasdoc d")
		.append(" WHERE a.pk_cosths = b.pk_cosths")
		.append(" AND b.pk_costobj_b = c.pk_invmandoc")
		.append(" AND c.pk_invbasdoc = d.pk_invbasdoc")
		.append(" AND a.pk_corp = '"+pk_corp+"' AND c.pk_corp = '"+pk_corp+"'")
		.append(" AND a.nyear = "+nyear+" AND a.nmonth = "+nmonth+" ")
		.append(" AND NVL(a.dr,0) = 0 AND NVL(b.dr,0) = 0 and b."+vouchertype+" <>0)")
		.append(" union all")
		.append(" select 0 jfje,sum(je) dfje from (")
		.append(" select c.invcode, a.cksl sl,round(nvl(a.cksl, 0) * nvl(a.price, 0), 2) je from (")  	
    	.append(" select a.pk_invbasdoc,a.cksl,b.price from (")
    	.append(" select b.pk_invbasdoc, sum(nvl(b.blmount, 0)) cksl")
    	.append(" from eh_sc_ckd a, eh_sc_ckd_b b")
    	.append(" where a.pk_ckd = b.pk_ckd")
    	.append(" AND a.pk_corp = '"+pk_corp+"'")
    	.append(" and a.vbillstatus = 1   and nvl(a.dr, 0) = 0 and nvl(b.dr, 0) = 0 and a.vbilltype = 'ZA46'")
    	.append(" and a.dmakedate between '"+begindate+"' and '"+enddate+"'")
    	.append(" group by b.pk_invbasdoc) a,(")
    	.append(" SELECT b.pk_invbasdoc,(sum(b.qcje) + sum(b.rkje) + sum(b.drje)) /(sum(b.qcsl) + sum(b.rksl) + sum(b.drsl)) price")
    	.append(" FROM eh_calc_kcybb a, eh_calc_kcybb_b b")
    	.append(" WHERE a.pk_kcybb = b.pk_kcybb")
    	.append(" AND a.pk_corp = '"+pk_corp+"'")
    	.append(" AND a.nyear = "+nyear+" AND a.nmonth = "+nmonth+" ")
    	//.append(" AND nvl(a.dr, 0) = 0  AND nvl(b.dr, 0) = 0 and b.qcsl + b.rksl + b.drsl <> 0")
    	.append(" AND nvl(a.dr, 0) = 0  AND nvl(b.dr, 0) = 0")//modify by houcq 2011-07-28
    	//.append(" group by b.pk_invbasdoc) b")
    	.append(" group by b.pk_invbasdoc having sum(b.qcsl) + sum(b.rksl) + sum(b.drsl)<>0) b")//modify by houcq 2011-07-28
    	.append(" where a.pk_invbasdoc=b.pk_invbasdoc) a,bd_invmandoc b,bd_invbasdoc c")
    	.append(" where a.pk_invbasdoc=b.pk_invmandoc")
    	.append(" and b.pk_invbasdoc=c.pk_invbasdoc")
		.append(" AND c.invcode like "+code)
		.append(" ))");
		IUAPQueryBS  iUAPQueryBS =    (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
		try {
			Object o = iUAPQueryBS.executeQuery(sql.toString(), new ColumnProcessor());
			if (o!=null)
			{
				uf = new UFDouble(o.toString());
			}
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return uf;
	}	
	/*
	 *���½�����쵽�ɱ����������Ĳ�Ʒ��ȥ
	 */
	public void updateYlOrBzCy(String pk_corp,String field,int nyear,int nmonth,UFDouble value){
		 StringBuilder sql = new StringBuilder() 
    	.append(" update eh_arap_cosths_b set "+field+" = "+field+"-("+value+"),sumfy = sumfy-("+value+") where pk_cosths_b = (")
    	.append(" select a.pk_cosths_b from (")
    	.append(" select  b.pk_cosths_b from eh_arap_cosths a,eh_arap_cosths_b b")
    	.append(" where a.pk_cosths=b.pk_cosths")
    	.append(" and a.pk_corp='"+pk_corp+"'")
    	.append(" and nvl(a.dr,0)=0")
    	.append(" and nvl(b.dr,0)=0")
    	.append(" and a.nyear="+nyear)
    	.append(" and a.nmonth="+nmonth)
    	.append(" order by b."+field+" desc")
    	.append(" ) a where rownum=1)"); 
    	PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
    	try {
			pubItf.updateSQL(sql.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
