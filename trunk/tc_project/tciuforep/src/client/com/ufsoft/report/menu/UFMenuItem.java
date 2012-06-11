package com.ufsoft.report.menu;

import java.awt.Component;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.IActionExt;
/**
 * name值用于比较路径,text值用于显示.
 * 
 * @author zzl 2005-6-30
 */
public class UFMenuItem extends nc.ui.pub.beans.UIMenuItem {
   
	private static final long serialVersionUID = 6490711196937957654L;
	protected IActionExt m_Extension = null;
    protected UfoReport aReport;
    private String group;
	/**
	 * @param extension
	 */
	public UFMenuItem(IActionExt extension, ActionUIDes uiDes, UfoReport report) {
		super(uiDes.getName());
		setName(uiDes.getName());
		m_Extension = extension;
		aReport = report;
		
		this.group = uiDes.getGroup();
		 
		 
	}
	
	public String getGroup(){
		return this.group;
	}
//	/**
//	 * @return 命令是否允许执行
//	 */
//	public boolean isCmdEnabled(){
//		UfoCommand cmd = m_Extension.getCommand();
//		return cmd==null?true :cmd.isPermit();
//	}
	
    /*
     * @see java.awt.Component#isEnabled()
     */
    public boolean isEnabled(Component focusComp) {
        return m_Extension.isEnabled(focusComp);
    }

    
    /*
     * @see java.lang.Object#toString()
     */
    public String toString() {
       return getText();
    }
    
//    public void updateUI() {
//        MenuItemUI ui = new BasicMenuItemUI(){
//            protected void paintText(Graphics g, JMenuItem menuItem, Rectangle textRect, String text) {
//                if(menuItem.getDisplayedMnemonicIndex() == -1 && getMnemonic() != 0){
//                    text = text + "("+getMnemonic()+")";
//                }
//                super.paintText(g,menuItem,textRect,text);
//            }
//        };
//        setUI(ui);
//    }
    /*
     * @see javax.swing.AbstractButton#setMnemonic(int)
     */
    public void setMnemonic(int mnemonic) {
        super.setMnemonic(mnemonic);
        dealMnemonicDisPlay();
    }
    /**
     * 处理记忆键的显示.
     * 菜单路径比较使用name值.
     *  void
     */
    private void dealMnemonicDisPlay(){
        if(this.getDisplayedMnemonicIndex() == -1 && getMnemonic() != 0){
            setText(getText() + "("+(char)getMnemonic()+")");
        }
    }
}
