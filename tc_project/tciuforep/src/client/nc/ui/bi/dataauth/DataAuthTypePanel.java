/*
 * �������� 2006-4-20
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.ui.bi.dataauth;


import com.ufida.web.comp.Area;
import com.ufida.web.comp.WebRadioGroup;
import com.ufida.web.container.WebGridLayout;
import com.ufida.web.container.WebTitlePanel;
import com.ufsoft.iufo.resource.StringResource;

/**
 * @author zyjun
 *
 * ����Ȩ���������
 */
public class DataAuthTypePanel extends WebTitlePanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WebRadioGroup btnRadioType = null;

    public DataAuthTypePanel(){
    	super();
    	setLayout(new WebGridLayout(1, 1));
    	Area	area = new Area(1,1,1,1);
    	add(getBtnRadioType(), area);
    }
    protected WebRadioGroup getBtnRadioType(){     
        if(btnRadioType == null){
            btnRadioType = new WebRadioGroup();
            btnRadioType.setName("type");
            btnRadioType.setID("type");
 
        }
        return btnRadioType;
    }
    protected void setData(DataAuthEditForm form){
    	setTitle(StringResource.getStringResource("ubiauth0008"));//"Ȩ������"));
       	String[][]	typeItems = {
        		{"0", StringResource.getStringResource("ubiauth0009")},//"��")},
    			{"1", StringResource.getStringResource("ubiauth0010")},//д
        	};
        	btnRadioType.setItems(typeItems);
        btnRadioType.setValue(Integer.toString(form.getType()) );
    }
}
