/*
 * 创建日期 2006-4-20
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
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
 * 数据权限类型面板
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
    	setTitle(StringResource.getStringResource("ubiauth0008"));//"权限类型"));
       	String[][]	typeItems = {
        		{"0", StringResource.getStringResource("ubiauth0009")},//"读")},
    			{"1", StringResource.getStringResource("ubiauth0010")},//写
        	};
        	btnRadioType.setItems(typeItems);
        btnRadioType.setValue(Integer.toString(form.getType()) );
    }
}
