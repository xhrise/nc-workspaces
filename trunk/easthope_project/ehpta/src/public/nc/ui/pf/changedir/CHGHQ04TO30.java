package nc.ui.pf.changedir;

import nc.bs.pf.change.ConversionPolicyEnum;
import nc.bs.pf.change.VOConversion;
import nc.vo.pf.change.UserDefineFunction;

public class CHGHQ04TO30 extends VOConversion {
	public String getAfterClassName()
	  {
	    return null;
	  }

	  public String getOtherClassName() {
	    return null;
	  }

	  public ConversionPolicyEnum getConversionPolicy() {
	    return ConversionPolicyEnum.BILLITEM_BILLITEM;
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
		      "H_creceipttype->30", // ��������  ���۶��� = 30
		      "H_pk_corp->H_pk_corp",  // ��˾
		      "H_ts->H_ts",  //��ʱ���
		      
		      "H_contracttype->H_saletype", // ��ͬ��� Integer (EH���) �ô�δ֪��
		      "H_pk_contract->H_pk_contract",  // ��ͬ���� ��EH������ֶΣ�
		      "H_sendcompany->H_bargainor", // ���� ��EH��ӣ�
		      "H_concode->H_vbillno",  // ��ͬ���� ��EH��ӣ�
		      "H_version->H_version",  // �汾�� ��EH��ӣ�
		      
//		      "H_nheadsummny->H_contprice", // �������ϼ�
		      
		      "B_ccurrencytypeid->00010000000000000001",   //��ԭ��   ���� ������ - so_saleorder_b�� Ĭ�ϣ������
		      "B_cinvbasdocid->B_pk_invbasdoc",  //������������������� - so_saleorder_b��
		      "B_cinventoryid->B_pk_invbasdoc", //������������������� - so_saleorder_b��
		      "B_crowno->B_def1",   //  �кţ����� - so_saleorder_b�� PTA��ͬ���������  �� ����Ϊ 10 -> 20 -> 30
		      "B_nnumber->B_num",  // ���� 
//		      "B_npacknumber->B_numof", // ������
		      "B_noriginalcurmny->B_notaxloan",  // ԭ����˰���
		      "B_noriginalcurnetprice->(B_taxprice / (1 + (B_taxrate / 100)))",  // ԭ����˰���� ������ =�� ���ۣ�
		      "B_noriginalcurprice->(B_taxprice / (1 + (B_taxrate / 100)))",  // ԭ����˰����
		      "B_noriginalcursummny->B_sumpricetax",  // ԭ�Ҽ�˰�ϼ�
		      "B_noriginalcurtaxmny->B_tax",  // ԭ��˰��
		      "B_noriginalcurtaxnetprice->B_taxprice",  // ԭ�Һ�˰���� 
		      "B_noriginalcurtaxprice->B_taxprice",  // ԭ�Һ�˰����
		      
//		      "B_nmny->B_notaxloan",  
//		      "B_nnetprice->(B_taxprice / (1 + (B_taxrate / 100)))",  
//		      "B_nprice->(B_taxprice / (1 + (B_taxrate / 100)))",
//		      "B_nsummny->B_sumpricetax",  // ���Ҽ�˰�ϼ�
//		      "B_ntaxmny->B_tax",  
//		      "B_ntaxnetprice->B_taxprice",  
//		      "B_ntaxprice->B_taxprice",
		      
		      "B_nquoteunitnum->B_num", // ���۵�λ����
//		      "B_norgqtnetprc->(B_taxprice / (1 + (B_taxrate / 100)))", // ���۵�λ��˰����
//		      "B_norgqtprc->(B_taxprice / (1 + (B_taxrate / 100)))", // ���۵�λ��˰����
//		      "B_norgqttaxnetprc->B_taxprice", // ���۵�λ��˰����
//		      "B_norgqttaxprc->B_taxprice", // ���۵�λ��˰����
//		      "B_nqtnetprc->(B_taxprice / (1 + (B_taxrate / 100)))" ,
//		      "B_nqtprc->(B_taxprice / (1 + (B_taxrate / 100)))",
//		      "B_nqttaxnetprc->B_taxprice",
//		      "B_nqttaxprc->B_taxprice",
		      
		      "B_ntaxrate->B_taxrate",  // ˰��
		      "B_pk_corp->H_pk_corp",  // ��˾����
		      "B_ts->B_ts",  // ʱ���
		      "B_csourcebillbodyid->B_pk_contract_b", // ��Դ���ݸ���ID �� ��ͬ�ӱ�����
		      "B_csourcebillid->H_pk_contract"  // ��Դ��������ID , ��ͬ��������
	     
	      };
	  }

	  public String[] getAssign() {
		// ��Դ�������� , PTA�ֻ���ͬ = HQ04
	    return new String[] { 
	    		"H_editionnum->1.0",
	    		"B_creceipttype->HQ04" , 
	    		"B_ndiscountmny->0" , 
	    		"B_ndiscountrate->100" , 
	    		"B_nitemdiscountrate->100" , 
	    		"B_noriginalcurdiscountmny->0"
	    }; 
	  }

	  // ����ʱֱ�Ӹ�ֵ
	  public String[] getFormulas() {
	    return new String[] { "H_dbilldate->date();", 
	      "H_dmakedate->date();" /* , 
	      "H_ccalbodyid->getColValue(bd_stordoc,pk_calbody ,pk_stordoc ,H_pk_storedoc);" */ }; // �����֯ , �ֿ⵵���п��Ի�ȡ
	  }

	  public UserDefineFunction[] getUserDefineFunction() {
	    return null;
	  }
}
