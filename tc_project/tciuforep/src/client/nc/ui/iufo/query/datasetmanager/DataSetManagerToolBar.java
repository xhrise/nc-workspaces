package nc.ui.iufo.query.datasetmanager;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JToolBar;

import com.ufsoft.report.menu.UFButton;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.IActionExt;


/**
 * 工具栏 创建时间 2004-8-4 16:34:26
 * 
 * @author caijie
 */
public class DataSetManagerToolBar extends JToolBar {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DataSetManagerToolBar() {
        super();
        setRollover(true);        
        setFloatable(true);
    }

    /**
     * 增加一个工具栏项目
     * 
     * @param groupId -
     *            工具栏分组的ID,起始组为0.
     * @param itemID -
     *            分组中的位置。起始位置为0。如果小于0认为放在最前头；如果大于已经存在的项目数量，放在 末尾
     * @param action -
     *            工具栏触发事件的描述。参考菜单触发的事件
     */
    public Component addToolItem(IActionExt ext,ActionUIDes uiDes, DataSetManager datasetManager) {
 
    	JComponent comp= DataSetManagerMenuUtil.createActionComp(ext,uiDes, datasetManager);
        this.add(comp);
        return comp;
        
    }

    public void adjustEnabled(Component focusComp){
        Component[] buttons = getComponents();
        for(int i=0;i<buttons.length;i++){
            if(buttons[i] instanceof UFButton){
                ((UFButton)buttons[i]).adjustEnabled(focusComp);
            }
        }
    }
  
}