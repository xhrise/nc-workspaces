package nc.ui.eh.pub;

import nc.vo.trade.pub.IBillStatus;

public interface ISHSHBillStatus extends IBillStatus{
    //  ����״̬����
    public static String[]  strStateRemark = new String[] { "������ͨ��", "����ͨ��", "����������", "�ύ̬", "����̬",
            "����̬", "��ֹ(����)̬", "����̬", "����̬","�ѹر�","ִ����","�����","��ȷ��"  ,"�������","�������","������","�����","������","δ����"};
    //  �ѹر�
    public static int CLOSE=9;
    //  ִ����
    public static int EXECUTING=10;
    //  �����
    public static int COMPLETE=11;
    //  ��ȷ��
    public static int CONFIRM=12;
    //  �������
    public static int CHECKED=13;
    //  �������
    public static int HANDLED=14;
    //  ������
    public static int DELIVERING=15;
    //  �����
    public static int ENTERING=16;
    //  ����
    public static int LOCKED=17;
    //  δ����
    public static int UNLOCKED=18;
}
