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
 * ��ݼ������������
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
	 * ��ȡ������������
	 * @return
	 */
	public Container getContainer() {
		return this.getMainboard();
	}

	/**
	 * ��ȡ��ݼ���������
	 * 
	 * @return String[][]
	 */
	public abstract String[][] getKeyAssistContent();

	/**
	 * ��ݼ���������
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
			 * ���ڹرյĺ󣬽��м��̵�ģ��
			 */
			keyAssistDialog.addWindowListener(new WindowAdapter() {
				
				public void windowClosed(WindowEvent e) {
					// TODO Auto-generated method stub
					final int[] keycodes= keyAssistDialog.getKeycodes();
                    if (keycodes!=null && keycodes.length!=0)
                    {
                    	//�������ڵĽ���ӵ�������»�ȡ����
						SwingUtilities.invokeLater(new Runnable(){
							public void run() {
								focusOwner.dispatchEvent(new FocusEvent(focusOwner,FocusEvent.FOCUS_GAINED,true));
								focusOwner.requestFocusInWindow();					
						    }
						});
    					
						//����ʹ��Thread�߳�����ΪSwingUtilities.invokeLater�봰�ڹر��¼��������¼��ַ��߳���,���ӻ�keyAssistDialog�Ĺر�
    					SimualteKeyInputThread simualteKeyInputThread=new SimualteKeyInputThread(keycodes);
    					simualteKeyInputThread.start();
                    }
				}
				
				/**
				 * 
				 * ����ģ���߳�
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
		                //�ӳ�֮����ִ�м���ģ�⣬�����п������ģ��ʧЧ,Ϊʲô�����ӳ٣�����ģ��ʧЧ��ԭ��̫�����
						//ͨ��System.out.println(focusOwner.isFocusOwner());�ж����Ѿ���ȡ�˽����
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
				 * ģ����̰���
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

				//miufo1000550=��ݼ�����
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
