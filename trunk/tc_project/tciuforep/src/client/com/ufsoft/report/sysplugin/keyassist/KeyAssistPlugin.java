package com.ufsoft.report.sysplugin.keyassist;
import java.awt.AWTException;
import java.awt.Component;
import java.awt.Container;
import java.awt.DefaultKeyboardFocusManager;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.Icon;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.plugin.AbstractPlugin;
import com.ufida.zior.plugin.AbstractPluginAction;
import com.ufida.zior.plugin.IPluginAction;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
/**
 * 快捷键帮助插件基类
 * @author xulm
 */
public abstract class KeyAssistPlugin extends AbstractPlugin {
    private Robot robot = null;
	@Override
	protected IPluginAction[] createActions() {
		return new IPluginAction[] { new KeyAssistAction() };
	}

	@Override
	public void shutdown() {

	}
	
	@Override
	public void startup() {
		
		try {
			robot = new Robot();
		} catch (AWTException ex) {
			AppDebug.debug(ex);
			UfoPublic.sendErrorMessage(MultiLang.getString("error"), getContainer(), ex);
		}
	}

	/**
	 * 获取父级顶层容器
	 * @return
	 */
	public Container getContainer() {
		return this.getMainboard();
	}

	/**
	 * 获取快捷键帮助内容
	 * 
	 * @return String[][]
	 */
	public abstract String[][] getKeyAssistContent();

	/**
	 * 快捷键帮助动作
	 * @author xulm
	 *
	 */
	private class KeyAssistAction extends AbstractPluginAction {

		public boolean isEnabled() {
			return true;
		}

		@Override
		public void execute(ActionEvent e) {
			final DefaultKeyboardFocusManager dkfm=new DefaultKeyboardFocusManager();
			final Component focusOwner=  dkfm.getFocusOwner();
			final KeyAssistDialog keyAssistDialog = new KeyAssistDialog(getContainer(),
					getKeyAssistContent());
			keyAssistDialog.setVisible(true);
			/**
			 * 窗口关闭的后，进行键盘的模拟
			 */
			keyAssistDialog.addWindowListener(new WindowAdapter() {
				
				public void windowClosed(WindowEvent e) {
					// TODO Auto-generated method stub
					final int[] keycodes= keyAssistDialog.getKeycodes();
                    if (keycodes!=null && keycodes.length!=0)
                    {
                    	//让主窗口的焦点拥有者重新获取焦点
						SwingUtilities.invokeLater(new Runnable(){
							public void run() {
								focusOwner.dispatchEvent(new FocusEvent(focusOwner,FocusEvent.FOCUS_GAINED,true));
								focusOwner.requestFocusInWindow();					
						    }
						});
    					
						//这里使用Thread线程是因为SwingUtilities.invokeLater与窗口关闭事件都处于事件分发线程中,会延缓keyAssistDialog的关闭
    					SimualteKeyInputThread simualteKeyInputThread=new SimualteKeyInputThread(keycodes);
    					simualteKeyInputThread.start();
                    }
				}
				
				/**
				 * 
				 * 键盘模拟线程
				 * @author xulm
				 */
				class SimualteKeyInputThread extends Thread{
					
					private int[] keycodes;
					
					public SimualteKeyInputThread(int[] keycodes)
					{
						this.keycodes=keycodes;
					}
					
					public void run()
					{
		                //延迟之后再执行键盘模拟，否则极有可能造成模拟失效,为什么必须延迟，否则模拟失效的原因不太清楚，
						//通过System.out.println(focusOwner.isFocusOwner());判断是已经获取了焦点的
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							AppDebug.debug(e);
						}
						SimualteKeyInput(keycodes) ;
					}
				}
				
				/**
				 * 模拟键盘按键
				 * @param keycodes
				 */
				private void SimualteKeyInput(int[] keycodes)
				{
					for (int i=0; i<keycodes.length;i++)
                    {
						robot.keyPress(keycodes[i]);
                    }
                    for (int i=0; i<keycodes.length;i++)
                    {
                    	robot.keyRelease(keycodes[i]);
                    }
				}
				
			});
		}

		@Override
		public IPluginActionDescriptor getPluginActionDescriptor() {
			return new IPluginActionDescriptor.Sub() {
				public XPOINT[] getExtensionPoints() {
					return new XPOINT[] { XPOINT.MENU };
				}

				public String[] getGroupPaths() {
					return new String[] { MultiLang.getString("help"),"help"};
				}

				public Icon getIcon() {
					return null;
				}

				//miufo1000550=快捷键帮助
				public String getName() {
					return MultiLang.getString("miufo1000550");
				}
				
				public KeyStroke getAccelerator() {
					return KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_MASK|KeyEvent.SHIFT_MASK );
				}
			};
		}
	}

}
