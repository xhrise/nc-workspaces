/*
 * �������� 2006-6-7
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.ui.eh.button;

/**
 * ���ߣ�ţұ
 * ���ܣ�������ť
 */
public class ButtonFactory {

    /**
     * 
     */
    public ButtonFactory() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    /**
	 * ֱ�Ӵ���,ʡ�ö�һȦʲôButtonVO��
	 * @param id
	 * @param code
	 * @param name
	 * @return
	 */
	public static nc.vo.trade.button.ButtonVO createButtonVO(int id, String code, String name)
	{
		nc.vo.trade.button.ButtonVO btn = new nc.vo.trade.button.ButtonVO();
		btn.setBtnNo(id);
		btn.setBtnName(code);
		btn.setHintStr(name);
		btn.setBtnCode(name);
		return btn;
	}
}
