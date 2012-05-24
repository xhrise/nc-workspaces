/*
 * 创建日期 2006-6-7
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.ui.eh.button;

/**
 * 作者：牛冶
 * 功能：创建按钮
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
	 * 直接创建,省得兜一圈什么ButtonVO的
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
