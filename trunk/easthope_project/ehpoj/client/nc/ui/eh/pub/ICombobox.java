package nc.ui.eh.pub;


/*
 * ����˵��:�Գ��õ�һЩ�����˵����ж���
 */
public interface ICombobox {
	
	
	/*���ϵ���*/
	public static final String[] STR_BIGTYPE = new String[]{"ԭ����","���Ʒ","��Ʒ"};
    
    /*�������*/
    public static final String[] STR_custprop = new String[]{"�ͻ�","��Ӧ��","����"};
    
    /*��������*/
    public static final String[] STR_polictype = new String[]{"�¶�","���","����","ʱ��"};
    
    /*�ۿ�����*/
    public static final String[] STR_discounttype = new String[]{"�ۿ۶�","�ۿ���"};
    
    /*��Ʊ����*/
    public static final String[] STR_invice = new String[]{"��ͨ��Ʊ","��ͨ��ֵ˰��Ʊ","ר����ֵ˰��Ʊ"};
    
    /*����ʽ*/
    public static final String[] STR_dealtype = new String[]{"�ػ�","��������"};
    
    /*�ջ�ʽ(ԭ��������׼��)*/
    public static final String[] STR_treatype = new String[]{"�����ջ�","�ۼ��ջ�"};
    
    /*˾������ȡ������*/
    public static final String[] STR_SBGETDATETYPE = new String[]{"�ֶ�","�Զ�"};
    
    /*˾��������*/
    //public static final String[] STR_SBBILLTYPE = new String[]{"����","ԭ��˾��","��Ʒ˾��","����˾��"};
    public static final String[] STR_SBBILLTYPE = new String[]{"����","ԭ��˾��","��Ʒ˾��"};//modify by houcq 2011-06-22
    /*˾����״̬*/
    public static final String[] STR_SBSTATUS = new String[]{"����","����"};
    
    /*��ͬ״̬*/
    public static final String[] STR_CONTSTYPE = new String[]{"ȷ��","δȷ��"};
    
    /*���������ͣ���������*/
    public static final String[] STR_TYPEFLAG = new String[]{"�Զ�","�ֶ�"};
    
    /*���������ⱨ�棩*/
    public static final String[] STR_RESULE = new String[]{"�ϸ�","���ϸ�","�����ջ�","�ۼ��ջ�","���ؿۼ��ջ�"};
    
    /*�����ۿ��м��㷽ʽ*/
    public static final String[] STR_JSMETHOD = new String[]{"��������","�������"};
    
    /*�����ۿ��н��㷽ʽ*/
    public static final String[] STR_JSTYPE = new String[]{"�̶�ȫ�����","�̶��۽�����","����ȫ�����","�����۽�����"};
    
    /*�����ۿ��в�Ʒ���㷽ʽ*/
    public static final String[] STR_INVJSTYPE = new String[]{"����Ʒ��������","���Ʒ���ܼ���"};
    
    /*�ɱ������ж�������*/
    public static final String[] STR_OBJTYPE = new String[]{"��Ʒ","����","����"};
    
    /*��Ʊ���*/
    public static final String[] INVOICETYPE = new String[]{"��ͨ��Ʊ","��ͨ��ֵ˰��Ʊ","ר����ֵ˰��Ʊ"};
    
    //BUG��������
    public static final String[] STR_BUGTYPE = new String[]{"���ش���","һ�����","��ʾ����","�Ż�����","��������"};
    
    //�տ
    public static final String[] STR_pk_sfkfs = new String[]{"�ͻ�","Ա��","����"};
      
    //U8ƾ֤ģ�� �������
    public static final String[] STR_DIRECTION = new String[]{"��","��"};
    
    //���ϵ����� ��������
    public static final String[] STR_DBTYPE = new String[]{"ԭ��","��Ʒ"};
    
    /*�ջ�֪ͨ��˾�����Ͷ���*/
    public static final String[] STR_YJSB = new String[]{"ȫ��˾��","����˾��","����˾��"};
    
    /*�ջ�֪ͨ�����ֵ�Ķ���*/
    public static final String[] STR_ALLCHECK = new String[]{"ȫ�����","�������","ȫ�����"};
    
    /*��ⱨ�����ļ�������͵Ķ���*/
    public static final String[] STR_PASS_FLAG = new String[]{"�ϸ�","���ϸ�","�����ջ�","�ۼ��ջ�"};
    
    /*�ֿ�ά���еĲֿ����*/
    public static final String[] STR_IS_FLAG = new String[]{"ԭ�Ͽ�","��Ʒ��"};
    /*�ɱ����÷�̯�е�����*/
    public static final String[] STR_INVTYPE = new String[]{"����","����","����","",};//modify by houcq 2011-03-22    
    /*������ָ��*/
    public static final String[] CW_SXZB= new String[]{"����ָ��","����ָ��"};
    /*�Ƿ����*/
    public static final String[] CW_KZKJ= new String[]{"����","�ۼ�",""};
    /*�Ƿ��������*/
    public static final String[] CW_HIGH= new String[]{"���","���",""};
    /*����*/
    public static final String[] CW_GROUP= new String[]{"��һ��","�ڶ���","������","������","������","������","������","�ڰ���","�ھ���",""};
    /*ƾ֤�гɱ���������*/
    public static final String[] CW_CBHS= new String[]{"���","ȼ�Ϸ�","����","�����"};
    
    /*BOM�д��ϣ�С��**/
    public static final String[] BOM_INVPTYPE = new String[]{"����","С��",""};
    public static final String[] CW_CLCKPZ = new String[]{"��װ","�ǰ�װ"};
    /*ѯ�۷�ʽ**/
    public static final String[] CG_XJFS = new String[]{"�ɹ���","��Ӧ��"};
    
    /*����Ѯ�ƻ���Ѯ���*/
    public static final String[] Period_flag = new String[]{"��Ѯ","��Ѯ","��Ѯ"};
    
    /*����ɹ��ƻ�*/
    public static final String[] SpecCG_flag = new String[]{"�ڱ�׼��","���ڱ�׼��","ҩƷ/��Ӽ�"};
    
    /*�ɹ��������������*/
    public static final String[] CG_DECISION = new String[]{"ԭ��","��װ��ǩ"};
    
    /*�ɹ������к�ʮ�������ж�*/
    public static final String[] CG_HQ = new String[]{" ","����10Ԫ","����20Ԫ","���Ǹ���","�µ�10Ԫ","�µ�20Ԫ","�µ�����","�ǵ�10Ԫ����"};
    
    /*��Ӧ������*/
    public static final String[] Gys_xz = new String[]{" ","��������","���Ҵ���","�м���","����"};
    
}

