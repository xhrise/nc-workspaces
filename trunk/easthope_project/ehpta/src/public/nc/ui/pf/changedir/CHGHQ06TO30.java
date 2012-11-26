package nc.ui.pf.changedir;

import nc.ui.pf.afterclass.SaleContractAfterCHG;
import nc.ui.pf.change.VOConversionUI;
import nc.vo.pf.change.UserDefineFunction;

public class CHGHQ06TO30 extends VOConversionUI {
	
	public String getAfterClassName() {
		
	    return SaleContractAfterCHG.class.getName();
	  }

	  public String getOtherClassName() {
	    return SaleContractAfterCHG.class.getName();
	  }

	  //���ֶ�ת��
	  public String[] getField() {
		  // ǰ H�� = so_sale  B�� = so_saleorder_b
	    return new String[] { 
		      "H_cbiztype->H_pk_busitype",   // ҵ������
		      "H_ccurrencytypeid->00010000000000000001",  // ����   Ĭ�ϣ������
		      "H_ccustomerid->H_purchcode", // ��������
		      "H_creceiptcustomerid->H_purchcode", // ��������
		      "H_cdeptid->H_pk_deptdoc",  // ���۲���
		      "H_cemployeeid->H_pk_psndoc", // ҵ��Ա
	 	      "H_coperatorid->H_voperatorid",  //��������
		      "H_creceiptcorpid->H_purchcode",  // ��Ʊ��λ  �� �ͻ���Ʊ����
		      
		      "H_pk_corp->H_pk_corp",  // ��˾
		      "H_ts->H_ts",  //��ʱ���
		      "H_pk_contract->H_pk_contract",  // ��ͬ���� ��EH������ֶΣ�
		      "H_sendcompany->H_bargainor", // ���� ��EH��ӣ�
		      "H_concode->H_vbillno",  // ��ͬ���� ��EH��ӣ�
		      "B_cinvbasdocid->B_pk_invbasdoc",  //������������������� - so_saleorder_b��
		      "B_cinventoryid->B_pk_invbasdoc", //������������������� - so_saleorder_b��
		      "B_crowno->B_def1",   //  �кţ����� - so_saleorder_b�� PTA��ͬ���������  �� ����Ϊ 10 -> 20 -> 30
//		      "B_nnumber->B_num",  // ���� 
		      
		      
//		      "B_noriginalcurmny->B_notaxloan",  // ԭ����˰���
//		      "B_noriginalcursummny->B_sumpricetax",  // ԭ�Ҽ�˰�ϼ�
//		      "B_noriginalcurtaxmny->B_tax",  // ԭ��˰��
//		      "B_noriginalcurtaxnetprice->B_taxprice",  // ԭ�Һ�˰���� 
//		      "B_noriginalcurtaxprice->B_taxprice",  // ԭ�Һ�˰����
		      
		      "B_nquoteunitnum->B_num", // ���۵�λ����
		      "B_ntaxrate->B_taxrate",  // ˰��
		      "B_pk_corp->H_pk_corp",  // ��˾����
		      "B_ts->B_ts",  // ʱ���
		      "B_csourcebillbodyid->B_pk_contract_b", // ��Դ���ݸ���ID �� ��ͬ�ӱ�����
		      "B_csourcebillid->H_pk_contract" ,  // ��Դ��������ID , ��ͬ��������
		      "H_settletype->H_def3" , // ��������
	      };
	  }
	  
	  // ����ʱֱ�Ӹ�ֵ
	  public String[] getAssign() {
		// ��Դ�������� , PTA�ֻ���ͬ = HQ04
	    return new String[] { 
	    		"H_editionnum->1.0",
	    		"H_contracttype->20", // ��ͬ���� , ���ó�����ͬΪ 20
	    		"H_creceipttype->30", // ��������  ���۶��� = 30
	    		"H_ccurrencytypeid->00010000000000000001",
	    		"H_nexchangeotobrate->1",
	    		"H_ndiscountrate->100",
	    		"H_ccalbodyid->1120A8100000000YSLF2", // �����֯
	    		"H_csalecorpid->0001A8100000000044PP", // ������֯
	    		"B_creceipttype->\"HQ06\"" , 
	    		"B_ndiscountmny->0" , 
	    		"B_ndiscountrate->100" , 
	    		"B_nitemdiscountrate->100" , 
	    		"B_noriginalcurdiscountmny->0",
	    		"B_ccurrencytypeid->00010000000000000001",   //��ԭ��   ���� ������ - so_saleorder_b�� Ĭ�ϣ������
	    		"B_nexchangeotobrate->1",
	    		"B_cadvisecalbodyid->1120A8100000000YSLF2" , // �����֯
	    		
	    }; 
	  }

	 // ���������õĹ�ʽ
	  public String[] getFormulas() {
	    return new String[] { 
	    		"H_dbilldate->date();", 
	    		"H_dmakedate->date();" ,
//	    		"B_numof->int(B_num / B_invspec)", // ����
	    		"cadvisecalbody->getColValue(bd_calbody,bodyname,pk_calbody,cadvisecalbodyid)",
//	    		"B_noriginalcurnetprice->B_taxprice / (1 + B_taxrate / 100)",  // ԭ����˰���� ������ =�� ���ۣ�
//			    "B_noriginalcurprice->B_taxprice / (1 + B_taxrate / 100)"  // ԭ����˰����
	    		"B_creceipttype->\"HQ06\"" , 
	    }; 
	  }

	  public UserDefineFunction[] getUserDefineFunction() {
	    return null;
	  }
}
