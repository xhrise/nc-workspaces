package nc.vo.hi.hi_301;

/**
 * �˴���������˵����
 * �������ڣ�(2002-5-15 11:22:17)
 * @author����Ⱥ��
 */
public class HiCommonValue extends nc.vo.pub.ValueObject {

	public final static String ORG_GRAD_PK = "HI000000000000000019"; //��֯��������
	public final static String JOB_REQUIRE_TYPE_PK = "HI000000000000000018"; //��ְ�ʸ�����
	public final static String JOB_GRADE_PK = "HI000000000000000022"; //��λ�ȼ�����
	public final static String JOB_SERIES_PK = "HI000000000000000021"; //��λ��������
	public final static String DUTY_SERIES_PK =  "HI000000000000000020"; //ְ���������
	public final static String COUNTRY_PK = "HI000000000000000002"; //����/����
	public final static String NATION_PK = "HI000000000000000003"; //����
	public final static String SPECILITY_PK = "HI000000000000000005"; //רҵ
	public final static String KNOWLEDGE_PK = "HI000000000000000007"; //ѧ��
	public final static String DEGREE_PK =  "HI000000000000000008"; //ѧλ
	public final static String HEALTH_PK = "HI000000000000000010"; //����״��
	public final static String MARRIAGE_PK =  "HI000000000000000011"; //����״��
//����������ͨ���˲ſ���Ŀʱ������չ
	public final static String PSNBASETYPE_PK = "0001AA10000000001NRQ";//"HI000000000000000053"; //���
	public final static String POLITY_PK =  "0001AA10000000001NS2";//"HI000000000000000055"; //������ò
	public final static String TECHNICAL_TITLE_PK = "0001AA10000000001NSV";//"HI000000000000000056"; //ְ��
	public final static String DUTY_PK =  "0001AA10000000001NTB";//"HI000000000000000057"; //ְ��
	public final static String WORKCORP_PK =  "0001AA10000000001OJF";//"HI000000000000000058"; //������λ
//��ͨ����Ŀר�ó���
	public final static String TABLENAME_WORKEXP = "hi_psndoc_work";
	public final static String TABLENAME_ARTICLE = "hi_psndoc_grpdef1";
	public final static String TABLENAME_TASK = "hi_psndoc_grpdef10";
	public final static String FLDNAME_DATE = "groupdef5";
	public final static String FLDNAME_DUTY = "hi_psndoc_deptchg.groupdef4";
	public final static String FLDNAME_PSNBASETYPE = "hi_psndoc_deptchg.groupdef2";
	public final static String FLDNAME_TECHTITLE = "groupdef1";
	public final static String FLDNAME_POLICY = "groupdef2";
	public final static String FLDNAME_ARTICLE = "groupdef4";
	public final static String FLDNAME_CHECKSUG = "hi_psndoc_deptchg.groupdef6";
	public final static String FLDNAME_UNITSUG = "hi_psndoc_deptchg.groupdef5";
	public final static String FLDNAME_PSNCODE = "hi_psndoc_deptchg.groupdef1";
	public final static String FLDNAME_BENIF = "groupdef3";
	public final static String FLDNAME_WORKUNIT = "hi_psndoc_deptchg.groupdef3";
	public final static String FLDNAME_EDUCATION = "hi_psndoc_edu.education";
	public final static String FLDNAME_DEGREE = "hi_psndoc_edu.degree";
	public final static String FLDNAME_MAJOR = "hi_psndoc_edu.major";
	public final static String FLDNAME_SCHOOL = "hi_psndoc_edu.school";

	public final static String[] tables = {"hi_psndoc_deptchg","hi_psndoc_edu"};
	//�������״̬
	public final static int BROWSE = 1;
	public final static int INSERT = 2;
	public final static int UPDATE = 3;
	public final static int DELETE = 4;
	//4000AA10000000000000��4000AA10000000000001��40000000000000000010
/**
 * OmCommonValue ������ע�⡣
 */
public HiCommonValue() {
	super();
}
/**
 * ������ֵ�������ʾ���ơ�
 * 
 * �������ڣ�(2001-2-15 14:18:08)
 * @return java.lang.String ������ֵ�������ʾ���ơ�
 */
public String getEntityName() {
	return null;
}
/**
 * ��֤���������֮��������߼���ȷ�ԡ�
 * 
 * �������ڣ�(2001-2-15 11:47:35)
 * @exception nc.vo.pub.ValidationException �����֤ʧ�ܣ��׳�
 *     ValidationException���Դ�����н��͡�
 */
public void validate() throws nc.vo.pub.ValidationException {}
}
