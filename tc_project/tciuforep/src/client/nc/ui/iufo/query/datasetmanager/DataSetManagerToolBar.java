package nc.ui.iufo.query.datasetmanager;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JToolBar;

import com.ufsoft.report.menu.UFButton;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.IActionExt;


/**
 * ������ ����ʱ�� 2004-8-4 16:34:26
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
     * ����һ����������Ŀ
     * 
     * @param groupId -
     *            �����������ID,��ʼ��Ϊ0.
     * @param itemID -
     *            �����е�λ�á���ʼλ��Ϊ0�����С��0��Ϊ������ǰͷ����������Ѿ����ڵ���Ŀ���������� ĩβ
     * @param action -
     *            �����������¼����������ο��˵��������¼�
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