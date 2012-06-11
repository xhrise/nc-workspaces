/*
 * 创建日期 2006-4-20
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.ui.bi.dataauth;

import com.ufida.web.comp.Area;
import com.ufida.web.comp.WebCheckBox;
import com.ufida.web.container.WebGridLayout;
import com.ufida.web.container.WebTitlePanel;
import com.ufsoft.iufo.resource.StringResource;

/**
 * @author zyjun
 *
 * 
 */
public class DataAuthRulePanel extends WebTitlePanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WebCheckBox 	checkBoxSelf = null;
    private WebCheckBox 	checkBoxSlibe = null;
    private WebCheckBox 	checkBoxParent = null;
    private WebCheckBox 	checkBoxChild = null;
    private WebCheckBox 	checkBoxAncestor = null;
    private WebCheckBox 	checkBoxOffSpring = null;
    

    public DataAuthRulePanel(){
        super();
        this.setLayout(new WebGridLayout(3, 2));
        Area area = new Area(1,1,1,1);
        add(getCheckBoxSelf(), area);
        area = new Area(1, 2, 1, 1);
        add(getCheckBoxSlibe(), area);
        area = new Area(2, 1, 1, 1);
        add(getCheckBoxParent(), area);
        area = new Area(2, 2, 1, 1);
        add(getCheckBoxChild(), area);
        area = new Area(3, 1, 1, 1);
        add(getCheckBoxAncestor(), area);
        area = new Area(3, 2, 1, 1);
        add(getCheckBoxOffSpring(), area);
        
    }
    
    
    protected WebCheckBox getCheckBoxSelf(){     
        if(checkBoxSelf == null){
            checkBoxSelf = new WebCheckBox();
            checkBoxSelf.setName("includeSelf");
            checkBoxSelf.setID("includeSelf");
 
        }
        return checkBoxSelf;
    }
    

    protected WebCheckBox getCheckBoxSlibe(){     
        if(checkBoxSlibe == null){
            checkBoxSlibe = new WebCheckBox();
            checkBoxSlibe.setName("includeSlibe");
            checkBoxSlibe.setID("includeSlibe");
 
        }
        return checkBoxSlibe;
    }
    

    protected WebCheckBox getCheckBoxParent(){     
        if(checkBoxParent == null){
            checkBoxParent = new WebCheckBox();
            checkBoxParent.setName("includeParent");
            checkBoxParent.setID("includeParent");
 
        }
        return checkBoxParent;
    }
    

    protected WebCheckBox getCheckBoxChild(){     
        if(checkBoxChild == null){
            checkBoxChild = new WebCheckBox();
            checkBoxChild.setName("includeChild");
            checkBoxChild.setID("includeChild");
 
        }
        return checkBoxChild;
    }
    

    protected WebCheckBox getCheckBoxAncestor(){     
        if(checkBoxAncestor == null){
            checkBoxAncestor = new WebCheckBox();
            checkBoxAncestor.setName("includeAncestor");
            checkBoxAncestor.setID("includeAncestor");
 
        }
        return checkBoxAncestor;
    }
    

    protected WebCheckBox getCheckBoxOffSpring(){     
        if(checkBoxOffSpring == null){
            checkBoxOffSpring = new WebCheckBox();
            checkBoxOffSpring.setName("includeOffSpring");
            checkBoxOffSpring.setID("includeOffSpring");
 
        }
        return checkBoxOffSpring;
    }
    

    
    protected void setData(DataAuthEditForm form){
    	setTitle(StringResource.getStringResource("ubiauth0001"));
    	
    	checkBoxSelf.setLabel(StringResource.getStringResource("ubiauth0002"));
    	checkBoxSlibe.setLabel(StringResource.getStringResource("ubiauth0003"));
    	checkBoxParent.setLabel(StringResource.getStringResource("ubiauth0004"));
    	checkBoxAncestor.setLabel(StringResource.getStringResource("ubiauth0005"));
    	checkBoxChild.setLabel(StringResource.getStringResource("ubiauth0006"));
    	checkBoxOffSpring.setLabel(StringResource.getStringResource("ubiauth0007"));
    	    	
        checkBoxSelf.setValue(form.isIncludeSelf());
        checkBoxSlibe.setValue(form.isIncludeSlibe());
        checkBoxParent.setValue(form.isIncludeParent());
        checkBoxChild.setValue(form.isIncludeChild());
        checkBoxAncestor.setValue(form.isIncludeAncestor());
        checkBoxOffSpring.setValue(form.isIncludeOffSpring());
        
    }
    
}
