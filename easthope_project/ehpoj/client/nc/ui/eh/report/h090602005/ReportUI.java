package nc.ui.eh.report.h090602005;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import nc.vo.eh.report.h090602005.ReportVO;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.pub.report.ReportBaseClass; 
import nc.ui.pub.report.ReportItem;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.eh.kc.h0250210.PeriodVO;

/**
 * 
 *���ܣ���˾��Ʒ�ṹ��
 *���ߣ���־Զ 
 *���ڣ�2009-9-30  ����11:06:53
 */

public class ReportUI extends ToftPanel {

	private static final long serialVersionUID = 1L;
	public ButtonObject m_boQuery;
	public ButtonObject m_boPrint;
	private ReportBaseClass m_report;
	private QueryConditionClient m_qryDlg;
	private ClientEnvironment ce = ClientEnvironment.getInstance();
	IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());

	@SuppressWarnings("deprecation")
	public ReportUI() {
		super();
		m_boQuery = new ButtonObject("��ѯ", "��ѯ����", 0);
		m_boPrint = new ButtonObject("��ӡ", "��ӡ����", 0);
		initialize();
	}

	private void initialize() {
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
		for (int i = 0; i < colcount; i++) {
			skey = getReportBase().getBodyShowItems()[i].getKey().trim();
			st[i] = skey;
		}
		getReportBase().setNotSortCols(st);
	}

	public ButtonObject[] getBtnAry() {
		return (new ButtonObject[] { m_boQuery, m_boPrint });
	}

	public ReportBaseClass getReportBase() {
		if (m_report == null)
			try {
				m_report = new ReportBaseClass();
				m_report.setName("ReportBase");
				m_report.setTempletID(this.getCorpPrimaryKey(), "H090602002",
						null, null);
			} catch (Exception ex) {
				System.out
						.println("\u57FA\u7C7B:\u672A\u627E\u5230\u62A5\u8868\u6A21\u677F......");
			}
		return m_report;
	}

	public String getTitle() {
		return m_report.getReportTitle();
	}

	public void onButtonClicked(ButtonObject bo) {
		try {
			if (bo == m_boQuery) {
				onQuery();
			} else if (bo == m_boPrint) {
				onPrint();
			}
		} catch (BusinessException ex) {
			showErrorMessage(ex.getMessage());
			ex.printStackTrace();
		} catch (Exception e) {
			showErrorMessage("\u672A\u77E5\u9519\u8BEF:" + e.getMessage());
			e.printStackTrace();
		}
	}

	public QueryConditionClient getQryDlg() {
		if (m_qryDlg == null) {
			m_qryDlg = createQueryDLG();
		}
		return m_qryDlg;
	}

	protected QueryConditionClient createQueryDLG() {
		QueryConditionClient dlg = new QueryConditionClient();
		dlg.setTempletID(this.getCorpPrimaryKey(), "H090602002", null, null);
		String today = ce.getDate().toString();
		String sql = "select pk_period FROM eh_period WHERE nyear = "
				+ today.substring(0, 4) + " and nmonth = "
				+ today.substring(5, 7) + " and pk_corp = '"
				+ ce.getCorporation().getPk_corp() + "'";
		try {
			String pk_period = iUAPQueryBS.executeQuery(sql,
					new ColumnProcessor()) == null ? null : iUAPQueryBS
					.executeQuery(sql, new ColumnProcessor()).toString();
			dlg.setDefaultValue("pk_period", pk_period, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		dlg.setNormalShow(false);
		return dlg;
	}

	// ���ô�ӡ����
	public void onPrint() throws Exception {
		this.getReportBase().previewData();
	}

	// ��ѯ����
	public void onQuery() throws Exception {
		this.getReportBase().getBillModel().clearBodyData();
		getQryDlg().showModal();
		getReportBase().showZeroLikeNull(true);//add by houcq 2010-12-23����0����ʾ
		ArrayList list = new ArrayList();
		ReportVO[] rvos = null;// �Զ���VO
		//�ϼ��ܶ���
		UFDouble hjzds = new UFDouble(0);
		//��ָ���ںϼ��ܶ���
		UFDouble lhnzds = new UFDouble(0);
		//��ָ���ںϼ��ܶ���ռ�ܶ�������
		UFDouble perslhnzdu = new UFDouble(0);
		//��ָ�������ջ���Χ�ںϼ��ܶ���
		UFDouble betweenlhsh = new UFDouble(0);
		//��ָ�������ջ���Χ�ںϼ��ܶ���ռ�ܶ�������
		UFDouble persbeweenlhsh = new UFDouble(0);
		//���ջ���Χ����׼�ϼ��ܶ���
		UFDouble cshzds = new UFDouble(0);
		//���ջ���Χ����׼�ϼ��ܶ���ռ�ܶ�������
		UFDouble perscshzds = new UFDouble(0);
		
		//���ںϼ��ܶ���
		UFDouble lasthjzds = new UFDouble(0);
		//������ָ���ںϼ��ܶ���
		UFDouble lastlhnzds = new UFDouble(0);
		//������ָ�������ջ���Χ�ںϼ��ܶ���
		UFDouble lastbetweenlhsh = new UFDouble(0);
		//���ڳ��ջ���Χ����׼�ϼ��ܶ���
		UFDouble lastcshzds = new UFDouble(0);
		
		//�ϼƹ����ܶ��������ڱȽ�
		UFDouble hjzdscomplast = new UFDouble(0);
		//�ϼ����������ڱȽ�
		UFDouble lhnzdscomplast = new UFDouble(0);
		//�ϼ������ջ��������ڱȽ�
		UFDouble betweenlhshcomplast = new UFDouble(0);
		//�ϼƳ��������ڱȽ�
		UFDouble cshzdscomplast = new UFDouble(0);

		
		if (getQryDlg().getResult() == 1) {
			ConditionVO[] period = getQryDlg().getConditionVOsByFieldCode("qudate"); // ��ѯ����PK
			String pk_period = null; 
		//��ѯ����
		String querydate = null;
		//��ѯ���ڵ��ϸ���
		String querylastdate = null;	
		if (period.length == 0) {
			this.showErrorMessage("��ѡ���ѯ���ڣ�");
			return;
		}else{
			pk_period = period[0].getValue();
			PeriodVO perVO = (PeriodVO) iUAPQueryBS.retrieveByPK(PeriodVO.class, pk_period);
			if(perVO.getNmonth() < 10){
				querydate = perVO.getNyear()+"-0"+perVO.getNmonth();
			}else{
				querydate = perVO.getNyear()+"-"+perVO.getNmonth();
			}

			if(perVO.getNmonth() == 1){
				querylastdate = (perVO.getNyear()-1)+"-12";//��һ���12�·�
			}else{
				if((perVO.getNmonth()-1) < 10){
					querylastdate = perVO.getNyear()+"-0"+(perVO.getNmonth()-1);
				}else{
					querylastdate = perVO.getNyear()+"-"+(perVO.getNmonth()-1);
				}
			}
		}
		
			//ȡ�òɹ����¼�������ж�Ӧ��ⱨ�浥����������
			StringBuffer sql = new StringBuffer()
			.append(" SELECT DISTINCT invbas.pk_invbasdoc pk_invbasdoc ,invbas.invname invname")
			.append(" FROM eh_stock_in skin ")
			.append(" , eh_stock_in_b skinb ")
			.append(" , eh_stock_checkreport skckrt ")
			.append(" ,bd_invmandoc invman, bd_invbasdoc invbas ")
			.append(" WHERE skin.pk_in = skinb.pk_in  ")
			.append(" AND SUBSTR(skin.vsourcebillid, 3, 20) = skckrt.pk_checkreport ")
			.append(" AND SUBSTR(skin.indate,1,7)='"+querydate+"' ")
			.append(" AND skin.vbillstatus = '1' ")
			.append(" AND NVL(skin.dr, '0') = '0' ")
			.append(" AND invman.pk_invmandoc = skinb.pk_invbasdoc ")
			.append(" AND invman.pk_invbasdoc = invbas.pk_invbasdoc ");
			IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			ArrayList all = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
			if (all != null && all.size() > 0) {
			for (int i = 0; i < all.size(); i++) {
				HashMap hm = (HashMap) all.get(i);
				//�ɹ�����з�����������������
				String pk_invbasdoc = hm.get("pk_invbasdoc")==null?"":hm.get("pk_invbasdoc").toString();
				//�ɹ�����з�����������������
				String invname = hm.get("invname")==null?"":hm.get("invname").toString();
			
			StringBuffer sqlamount = new StringBuffer()
			.append(" SELECT  ")
			.append("        skamone.inamount inneramount, ")
			.append("        skamsec.inamount betweamount, ")
			.append("        skamthi.inamount okeyouteramount, ")
			.append("        lastskamone.inamount lastinneramount, ")
			.append("        lastskamsec.inamount lastbetweamount, ")
			.append("        lastskamthi.inamount lastokeyouteramount ")
			.append("   FROM (SELECT ")
			.append("   SUM(skinb.inamount) inamount ")
			.append("           FROM eh_stock_in          skin, ")
			.append("                eh_stock_in_b        skinb, ")
			.append("                eh_stock_checkreport skckrt ")
			.append("                 ,bd_invmandoc invman ")
			.append("                ,bd_invbasdoc invbas ")
			.append("          WHERE skin.pk_in = skinb.pk_in ")
			.append("            AND SUBSTR(skin.vsourcebillid, 3, 20) = skckrt.pk_checkreport ")
			.append("            AND skckrt.resulst = '0' ")
			.append("            AND SUBSTR(skin.indate,1,7)='"+querydate+"' ")
			.append("            AND NVL(skckrt.tc_flag, 'N') = 'N' ")
			.append("            AND skin.vbillstatus = '1' ")
			.append("            AND NVL(skin.dr, '0') = '0' ")
			.append("            AND invman.pk_invmandoc = skinb.pk_invbasdoc ")
			.append("            AND invman.pk_invbasdoc = invbas.pk_invbasdoc ")
			.append("            AND invbas.pk_invbasdoc = '"+pk_invbasdoc+"' ")
			.append("            ) skamone, ")
			.append("        (SELECT  ")
			.append("        SUM(skinb.inamount) inamount ")
			.append("           FROM eh_stock_in          skin, ")
			.append("                eh_stock_in_b        skinb, ")
			.append("                eh_stock_checkreport skckrt ")
			.append("                ,bd_invmandoc invman ")
			.append("                ,bd_invbasdoc invbas ")
			.append("          WHERE skin.pk_in = skinb.pk_in ")
			.append("            AND SUBSTR(skin.vsourcebillid, 3, 20) = skckrt.pk_checkreport ")
			.append("            AND skckrt.resulst in ('2', '3', '4') ")
			.append("            AND SUBSTR(skin.indate,1,7)='"+querydate+"' ")
			.append("            AND NVL(skckrt.tc_flag, 'N') = 'N' ")
			.append("            AND skin.vbillstatus = '1' ")
			.append("            AND NVL(skin.dr, '0') = '0' ")
			.append("            AND invman.pk_invmandoc = skinb.pk_invbasdoc ")
			.append("            AND invman.pk_invbasdoc = invbas.pk_invbasdoc ")
			.append("            AND invbas.pk_invbasdoc = '"+pk_invbasdoc+"' ")
			.append("            ) skamsec, ")
			.append("        (SELECT  ")
			.append("        SUM(skinb.inamount) inamount ")
			.append("           FROM eh_stock_in          skin, ")
			.append("                eh_stock_in_b        skinb, ")
			.append("                eh_stock_checkreport skckrt ")
			.append("                ,bd_invmandoc invman ")
			.append("                ,bd_invbasdoc invbas ")
			.append("          WHERE skin.pk_in = skinb.pk_in ")
			.append("            AND SUBSTR(skin.vsourcebillid, 3, 20) = skckrt.pk_checkreport ")
			.append("            AND skckrt.resulst in ('0', '2', '3', '4') ")
			.append("            AND SUBSTR(skin.indate,1,7)='"+querydate+"' ")
			.append("            AND NVL(skckrt.tc_flag, 'N') = 'Y' ")
			.append("            AND skin.vbillstatus = '1' ")
			.append("            AND NVL(skin.dr, '0') = '0' ")
			.append("            AND invman.pk_invmandoc = skinb.pk_invbasdoc ")
			.append("            AND invman.pk_invbasdoc = invbas.pk_invbasdoc ")
			.append("            AND invbas.pk_invbasdoc = '"+pk_invbasdoc+"' ")
			.append("            ) skamthi, ")
			.append(" (SELECT ")
			.append("   SUM(skinb.inamount) inamount ")
			.append("           FROM eh_stock_in          skin, ")
			.append("                eh_stock_in_b        skinb, ")
			.append("                eh_stock_checkreport skckrt ")
			.append("                 ,bd_invmandoc invman ")
			.append("                ,bd_invbasdoc invbas ")
			.append("          WHERE skin.pk_in = skinb.pk_in ")
			.append("            AND SUBSTR(skin.vsourcebillid, 3, 20) = skckrt.pk_checkreport ")
			.append("            AND skckrt.resulst = '0' ")
			.append("            AND SUBSTR(skin.indate,1,7)='"+querylastdate+"' ")
			.append("            AND NVL(skckrt.tc_flag, 'N') = 'N' ")
			.append("            AND skin.vbillstatus = '1' ")
			.append("            AND NVL(skin.dr, '0') = '0' ")
			.append("            AND invman.pk_invmandoc = skinb.pk_invbasdoc ")
			.append("            AND invman.pk_invbasdoc = invbas.pk_invbasdoc ")
			.append("            AND invbas.pk_invbasdoc = '"+pk_invbasdoc+"' ")
			.append("            ) lastskamone, ")
			.append("        (SELECT  ")
			.append("        SUM(skinb.inamount) inamount ")
			.append("           FROM eh_stock_in          skin, ")
			.append("                eh_stock_in_b        skinb, ")
			.append("                eh_stock_checkreport skckrt ")
			.append("                ,bd_invmandoc invman ")
			.append("                ,bd_invbasdoc invbas ")
			.append("          WHERE skin.pk_in = skinb.pk_in ")
			.append("            AND SUBSTR(skin.vsourcebillid, 3, 20) = skckrt.pk_checkreport ")
			.append("            AND skckrt.resulst in ('2', '3', '4') ")
			.append("            AND SUBSTR(skin.indate,1,7)='"+querylastdate+"' ")
			.append("            AND NVL(skckrt.tc_flag, 'N') = 'N' ")
			.append("            AND skin.vbillstatus = '1' ")
			.append("            AND NVL(skin.dr, '0') = '0' ")
			.append("            AND invman.pk_invmandoc = skinb.pk_invbasdoc ")
			.append("            AND invman.pk_invbasdoc = invbas.pk_invbasdoc ")
			.append("            AND invbas.pk_invbasdoc = '"+pk_invbasdoc+"'           ")
			.append("            ) lastskamsec, ")
			.append("        (SELECT  ")
			.append("        SUM(skinb.inamount) inamount ")
			.append("           FROM eh_stock_in          skin, ")
			.append("                eh_stock_in_b        skinb, ")
			.append("                eh_stock_checkreport skckrt ")
			.append("                ,bd_invmandoc invman ")
			.append("                ,bd_invbasdoc invbas ")
			.append("          WHERE skin.pk_in = skinb.pk_in ")
			.append("            AND SUBSTR(skin.vsourcebillid, 3, 20) = skckrt.pk_checkreport ")
			.append("            AND skckrt.resulst in ('0', '2', '3', '4') ")
			.append("            AND SUBSTR(skin.indate,1,7)='"+querylastdate+"' ")
			.append("            AND NVL(skckrt.tc_flag, 'N') = 'Y' ")
			.append("            AND skin.vbillstatus = '1' ")
			.append("            AND NVL(skin.dr, '0') = '0' ")
			.append("            AND invman.pk_invmandoc = skinb.pk_invbasdoc ")
			.append("            AND invman.pk_invbasdoc = invbas.pk_invbasdoc ")
			.append("            AND invbas.pk_invbasdoc = '"+pk_invbasdoc+"' ")
			.append("            ) lastskamthi ");
			IUAPQueryBS iUAPQueryBSAmount = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			ArrayList allAmount = (ArrayList) iUAPQueryBSAmount.executeQuery(sqlamount.toString(), new MapListProcessor());

			UFDouble invamount = new UFDouble(0); //�����ܶ���
			UFDouble complastm = new UFDouble(0); //�����ܶ������������
			UFDouble inneramount = new UFDouble(0); //���ڶ���
			UFDouble persinner = new UFDouble(0); //����ռ����
			UFDouble innercomplastm = new UFDouble(0); //���ڶ������������
			UFDouble betweamount = new UFDouble(0); //�ջ���Χ�ڶ���
			UFDouble persbetwe = new UFDouble(0); //�ջ���Χռ����
			UFDouble betwecomplastm = new UFDouble(0); //�ջ���Χ�ڶ������������
			UFDouble outeramount = new UFDouble(0); //���վ���׼����
			UFDouble persouter = new UFDouble(0); //���վ���׼ռ����
			UFDouble outercomplastm = new UFDouble(0); //���վ���׼�������������
			
			UFDouble lastinvamount = new UFDouble(0); //���ڹ����ܶ���
			UFDouble lastinneramount = new UFDouble(0); //�������ڶ���
			UFDouble lastbetweamount = new UFDouble(0); //�����ջ���Χ�ڶ���
			UFDouble lastouteramount = new UFDouble(0); //���ڳ��վ���׼����
			
			if (allAmount != null && allAmount.size() > 0) {
			for (int iam = 0; iam < allAmount.size(); iam++) {
				HashMap hmam = (HashMap) allAmount.get(iam);
//				//Ƭ��
//				docname = hmam.get("docname")==null?"":hmam.get("docname").toString();
//				//��˾����
//				unitname = hmam.get("unitname")==null?"":hmam.get("unitname").toString();
				//���ڶ���
				inneramount = new UFDouble(hmam.get("inneramount")==null?"0":hmam.get("inneramount").toString());
				//�����ջ��ڶ���
				betweamount = new UFDouble(hmam.get("betweamount")==null?"0":hmam.get("betweamount").toString());
				//���վ�������
				outeramount = new UFDouble(hmam.get("okeyouteramount")==null?"0":hmam.get("okeyouteramount").toString());

				//�������ڶ���
				lastinneramount = new UFDouble(hmam.get("lastinneramount")==null?"0":hmam.get("lastinneramount").toString());
				//���������ջ��ڶ���
				lastbetweamount = new UFDouble(hmam.get("lastbetweamount")==null?"0":hmam.get("lastbetweamount").toString());
				//���ڳ��վ�������
				lastouteramount = new UFDouble(hmam.get("lastokeyouteramount")==null?"0":hmam.get("lastokeyouteramount").toString());

				
				//�ɹ��ܶ���
				invamount = inneramount.add(betweamount).add(outeramount);
				
				//���ڲɹ��ܶ���
				lastinvamount = lastinneramount.add(lastbetweamount).add(lastouteramount);
				
				//���ڶ���ռ�ܶ�������
				persinner = inneramount.div(invamount).multiply(100);
				//�����ջ���ռ�ܶ�������
				persbetwe = betweamount.div(invamount).multiply(100);
				//���վ�������ռ��������
				persouter = outeramount.div(invamount).multiply(100);
				
				//���¹����ܶ������������
				complastm = new UFDouble(100).sub(invamount.div(lastinvamount).multiply(100));
				//�������������
				innercomplastm = new UFDouble(100).sub(inneramount.div(lastinneramount).multiply(100));	
				//�����ջ������������
				betwecomplastm = new UFDouble(100).sub(betweamount.div(lastbetweamount).multiply(100));
				//���ջ����������
				outercomplastm = new UFDouble(100).sub(outeramount.div(lastouteramount).multiply(100));
				
				//���й�˾�ɹ������ϼ�
				hjzds = hjzds.add(invamount);
				//���й�˾���ڶ����ϼ�
				lhnzds = lhnzds.add(inneramount);
				//���й�˾�����ջ��ڶ����ϼ�
				betweenlhsh = betweenlhsh.add(betweamount);
				//���г��վ��������ϼ�
				cshzds = cshzds.add(outeramount);
				
				//���й�˾�ɹ������ϼ�
				lasthjzds = lasthjzds.add(lastinvamount);
				//���й�˾���ڶ����ϼ�
				lastlhnzds = lastlhnzds.add(lastinneramount);
				//���й�˾�����ջ��ڶ����ϼ�
				lastbetweenlhsh = lastbetweenlhsh.add(lastbetweamount);
				//���г��վ��������ϼ�
				lastcshzds = lastcshzds.add(lastouteramount);
				
				ReportVO rvo = new ReportVO();
//
//				rvo.setInvname(invname);
//				rvo.setInvamount(invamount);
//				rvo.setComplastm(complastm);
//
//				rvo.setInneramount(inneramount);
//				rvo.setPersinner(persinner);
//				rvo.setInnercomplastm(innercomplastm);
//				
//				rvo.setBetweamount(betweamount);
//				rvo.setPersbetwe(persbetwe);
//				rvo.setBetwecomplastm(betwecomplastm);
//				
//				rvo.setOuteramount(outeramount);
//				rvo.setPersouter(persouter);
//				rvo.setOutercomplastm(outercomplastm);
				list.add(rvo);
			}
			}
			}
			/**�ϼ�����ʾ������*/
			//�����ܺϼƱ���
			perslhnzdu = lhnzds.div(hjzds).multiply(100);
			//�����ջ����ܺϼƱ���
			persbeweenlhsh = betweenlhsh.div(hjzds).multiply(100);
			//���վ����ܺϼƱ���
			perscshzds = cshzds.div(hjzds).multiply(100);
			
			//�ϼƹ����ܶ��������ڱȽ�
			hjzdscomplast = new UFDouble(100).sub(hjzds.div(lasthjzds).multiply(100));
			//�ϼ����������ڱȽ�
			lhnzdscomplast = new UFDouble(100).sub(lhnzds.div(lastlhnzds).multiply(100));
			//�����ջ��������ڱȽ�
			betweenlhshcomplast = new UFDouble(100).sub(betweenlhsh.div(lastbetweenlhsh).multiply(100));
			//���������ڱȽ�
			cshzdscomplast = new UFDouble(100).sub(cshzds.div(lastcshzds).multiply(100));
			
			ReportVO rvo = new ReportVO();
//			rvo.setInvname("�ϼ�");
//			rvo.setInvamount(hjzds);
//			rvo.setComplastm(hjzdscomplast);
//
//			rvo.setInneramount(lhnzds);
//			rvo.setPersinner(perslhnzdu);
//			rvo.setInnercomplastm(lhnzdscomplast);
//			
//			rvo.setBetweamount(betweenlhsh);
//			rvo.setPersbetwe(persbeweenlhsh);
//			rvo.setBetwecomplastm(betweenlhshcomplast);
//			
//			rvo.setOuteramount(cshzds);
//			rvo.setPersouter(perscshzds);
//			rvo.setOutercomplastm(cshzdscomplast);
			list.add(rvo);
			}

			if(list.size()>0){
                rvos = (ReportVO[]) list.toArray(new ReportVO[0]);
            }          
			if(rvos!=null && rvos.length>0){
				ReportItem [] newItems = null;
				this.getReportBase().addBodyItem(newItems);				
                this.getReportBase().setBodyDataVO(rvos);
                this.getReportBase().execHeadLoadFormulas();
                this.getReportBase().execTailLoadFormulas();
                updateUI();
            }else{
                this.showErrorMessage("�����������ı�������!�����²�ѯ!!");
            }
		}
	}
}
