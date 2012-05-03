package nc.vo.hr.pub.carddef;

import java.util.HashMap;

import javax.swing.ImageIcon;

import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

/**
 * ��Ƭ����ʹ�õ�����س���������
 * @author wangxing
 *
 */
public class CommonValue extends nc.vo.hi.pub.CommonValue {
    	// �ڵ����
    	public static final String MOUDLE_CODE_CARDDEF = "60070104";
    	public static final String MOUDLE_CODE_CARDAUTH = "60070107";
    	public static final String MOUDLE_CODE_CARDRPT = "600715";
    	
    	// �����Թ����ļ�����
    	public static final String MLANG_RES_COMMON = "common";

	//��������������ʽ�����ַ������������洢��IUFO CELL�ĵ�Ԫ��չ��ʽ��
	public static final String CELL_VAR_FORMAT = "hr_card_variable_format";
	
	// ��Ҫ�س��������Ա��Ϣ�����ı���
	public static final String PSN_SPEC_MAIN_SET_CODE = "bd_psndoc";
	
	// �������������ֶε���ְ�Ӽ��ֶ��滻��Ӱ��
	public static HashMap<String, String> PSN_SET_FIELDS_MAP01 = new HashMap<String, String>();
	
	// ����������ְ�Ӽ��ֶε������ֶ��滻��Ӱ��
	public static HashMap<String, String> PSN_SET_FIELDS_MAP02 = new HashMap<String, String>();
	
	// ��ְ�����Ա��Ϣ�����е��ظ��ֶ�
	public static final String[] FIELDS_PSN_SUB = {
		"pk_psncl",
		"pk_deptdoc",
		"pk_postdoc",
		"pk_om_duty",
		"pk_jobserial",
		"pk_jobrank",
		"pk_detytype",
		"pk_dutyrank"
	};
	
	// ��Ա��Ϣ��������ְ�����ظ����ֶ�
	public static final String[] FIELDS_PSN_MAIN = {
		"pk_psncl",
		"pk_deptdoc",
		"pk_om_job",
		"dutyname",
		"jobseries",
		"jobrank",
		"series",
		"pk_dutyrank"
	};
	
	// ��ʼ��Ӱ��
	static{
		for(int i=0; i<FIELDS_PSN_SUB.length; i++){
			PSN_SET_FIELDS_MAP01.put(FIELDS_PSN_MAIN[i], FIELDS_PSN_SUB[i]);
			PSN_SET_FIELDS_MAP02.put(FIELDS_PSN_SUB[i], FIELDS_PSN_MAIN[i]);
		}//end for
	}
	
	public static final String REFTYPE_NULL = "::REFTYPE_NULL::";
	// �Ƚϲ�����
	// ����
	public static final int RELAOPER_EQUALS = 0;
	// ����
	public static final int RELAOPER_BIGGER = 1;
	// С��
	public static final int RELAOPER_SMALLER = 2;
	// ����
	public static final int RELAOPER_LIKE = 3;
	
	// ��Ա��Ϣҵ���Ӽ���������
	public static final String[] PSN_BUSINESS_SET_CODES = {
		"hi_psndoc_deptchg", /* ��ְ��� */
		"hi_psndoc_ctrt",/* �Ͷ���ͬ */
		"hi_psndoc_part",/* ��ְ��� */
		"hi_psndoc_training",/* ��ѵ��¼ */
		"hi_psndoc_ass",/* ���˼�¼ */
		"hi_psndoc_retire",/* ���˴��� */
		"hi_psndoc_orgpsn",/* ������֯ */
		"hi_psndoc_psnchg",/* Ա������ */
		"hi_psndoc_dimission"/* ��ְ��� */
	};
	
	
	// ��Ա����
	// ȫְ
	public static final int PSNTYPE_NORMAL = 1;
	// ��ְ
	public static final int PSNTYPE_PARTIME = 2;
	
	/**�������Ͷ�Ӧ��**/
	public final static Class DATATYPE_CLASS_STRING = String.class; //�ַ� 
	public final static Class DATATYPE_CLASS_INTEGER = Integer.class; //���� 
	public final static Class DATATYPE_CLASS_DECIMAL = UFDouble.class; //С�� 
	public final static Class DATATYPE_CLASS_DATE = UFDate.class; //���� 
	public final static Class DATATYPE_CLASS_BOOLEAN = UFBoolean.class; //�߼� 
	public final static Class DATATYPE_CLASS_UFREF = String.class; //����
	public final static Class DATATYPE_CLASS_PICTURE = ImageIcon.class; //ͼƬ 
	public final static Class DATATYPE_CLASS_TIME = UFDateTime.class; //ʱ�� 
	public final static Class DATATYPE_CLASS_MEMO = String.class; //��ע
	public final static Class DATATYPE_CLASS_FORMULADATE = String.class; //���ڹ�ʽ��
	
	public static final String[] CHINESE_NUMS_LOWWER = {
		"��",
		"һ",
		"��",
		"��",
		"��",
		"��",
		"��",
		"��",
		"��",
		"��",
		"ʮ",
		"ʮһ",
		"ʮ��",
		"ʮ��",
		"ʮ��",
		"ʮ��",
		"ʮ��",
		"ʮ��",
		"ʮ��",
		"ʮ��",
		"��ʮ",
		"��ʮһ",
		"��ʮ��",
		"��ʮ��",
		"��ʮ��",
		"��ʮ��",
		"��ʮ��",
		"��ʮ��",
		"��ʮ��",
		"��ʮ��",
		"��ʮ",
		"��ʮһ"
	};
	
	public static final String[] CHINESE_NUMS_UPPER = {
		"��",
		"Ҽ",
		"��",
		"��",
		"��",
		"��",
		"½",
		"��",
		"��",
		"��",
		"ʰ",
		"ʰҼ",
		"ʰ��",
		"ʰ��",
		"ʰ��",
		"ʰ��",
		"ʰ½",
		"ʰ��",
		"ʰ��",
		"ʰ��",
		"��ʰ",
		"��ʰҼ",
		"��ʰ��",
		"��ʰ��",
		"��ʰ��",
		"��ʰ��",
		"��ʰ½",
		"��ʰ��",
		"��ʰ��",
		"��ʰ��",
		"��ʰ",
		"��ʰҼ"
	};
	/**
	 * ���ڸ�ʽ������
	 */
	public static final String[] DATEFORMAT_NAMES = {
		"1977-08-28",
		"1977.8.28",
		"77.8.28",
		"1977.8",
		"1977.08",
		"77.8",
		"77.08",
		"һ����������¶�ʮ����",
		"һ�����������",
		"1977��8��28��",
		"1977��8��",
		"1977��08��28��",
		"1977��08��",
		"77��8��28��",
		"77��8��",
		"����",
		"���",
		"�·�",
		"�շ�"
	};
	

	// ��Ԫ����Ϣ���ǰ׺
	public static final String CELL_INFOITEM_PREFIX = "$";
	
	// ����ڵ�����ͣ���Ϊ������ļ�����������
	// �ļ���
	public static final int RPT_NODETYPE_FOLDER = 1;
	// ����
	public static final int RPT_NODETYPE_REPORT = 2;
	
	// �������ͣ�Ŀǰֻ�п�Ƭ����һ�����ͣ��������Ϳ����Ժ���չ
	// ��Ƭ����
	public static final int RPT_TYPE_CARD = 1;
	// �����ᱨ��, add by walkfire 2007-08-02 V5.02
	public static final int RPT_TYPE_LIST = 2;
	
	// ��������
	public static int[] RPT_TYPE_ARRAY = new int[]{
		RPT_TYPE_CARD,
		RPT_TYPE_LIST
	}; 
	
	// ������Ȩ��������
	public static final int RPT_AUTH_REF_TYPE_USER = 1;
	public static final int RPT_AUTH_REF_TYPE_ROLE = 2;
	
	// Ȩ������
	public static final int RPT_AUTH_NONE = 0;		// ��Ȩ��
	public static final int RPT_AUTH_VIEW = 1;		// ���Ȩ��
//	public static final int RPT_AUTH_FULL = 5;		// ����Ȩ��

}
