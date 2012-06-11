package com.ufsoft.iufo.inputplugin.biz;

import java.awt.EventQueue;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.inputplugin.biz.formulatrace.FormulaTraceBizUtil;
import com.ufsoft.iufo.inputplugin.biz.formulatrace.FormulaTraceExt;
import com.ufsoft.iufo.inputplugin.biz.formulatrace.FormulaTraceNavExt;
import com.ufsoft.iufo.inputplugin.biz.formulatrace.FormulaTraceNavPanel;
import com.ufsoft.iufo.inputplugin.biz.formulatrace.FormulaTraceRenderer;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.table.SelectListener;
import com.ufsoft.table.event.SelectEvent;
import com.ufsoft.table.re.CellRenderAndEditor;
import com.ufsoft.iufo.resource.StringResource;

/**
 * ��ʽ׷�ٵĲ�� NOTICE: �ò����Ҫ��InputDataPlugIn֮ǰ���أ����ܱ�֤����ʽ׷�١��Ĳ˵�����ʾ�ڡ����ݡ��˵���ĵ�һ��
 * 
 * @author liulp
 * 
 */
public class FormulaTracePlugIn extends AbstractPlugIn implements
		SelectListener {

	@Override
	protected IPluginDescriptor createDescriptor() {
		return new FormulaTracePlugDes(this);
	}

	static{
		CellRenderAndEditor.getInstance().registExtSheetRenderer(new FormulaTraceRenderer(null));
	}
	public void startup() {
		if (getReport().getTable()!=null && getReport().getTable().getCells()!=null){
			IExtension[] exts = getDescriptor().getExtensions();
			FormulaTraceNavPanel panel = (FormulaTraceNavPanel) ((FormulaTraceNavExt) exts[1])
					.getFormulaTraceNavPanel();
			
			getReport().getTable().getCells().registExtSheetRenderer(
					new FormulaTraceRenderer(panel));
		}
		// addPlugin��ʱ���ע��SelectModelListener
		// getReport().getCellsModel().getSelectModel().addSelectModelListener(this);
	}

	
	/**
	 */
	public void selectedChanged(SelectEvent e) {
		if (e.getProperty() == SelectEvent.ANCHOR_CHANGED) {
			IExtension[] exts = getDescriptor().getExtensions();
			FormulaTraceExt ext = ((FormulaTraceExt) exts[0]);
			String strAlondID = InputBizOper.doGetTransObj(getReport())
					.getRepDataParam().getAloneID();
			if (strAlondID == null) {
				return;
			}
			//��ֹ������߳�
			String strMenuName = FormulaTraceBizUtil.doGetStrFormulaTrace();//"��ʽ׷��"
			String[] strParantMenuNames = new String[]{FormulaTraceBizUtil.doGetStrData()};//"����"
			boolean isTraceMenuSelected = FormulaTraceBizUtil.isMenuSelected(strMenuName,strParantMenuNames,getReport());
			
			if(!isTraceMenuSelected){
				FormulaTraceNavPanel panel = FormulaTraceBizUtil.getFormulaTraceNavPanel(getReport());
				if(panel!=null&&panel.getCurTracedPos()!=null){
					panel.setCurTracedPos(null);
				}
				return;
			}
			
			final Object[] params = ext.getParams(getReport());
			final UfoCommand cmd = ext.getCommand();
			if (cmd == null) {
				return;
			}
			CmdThread cmdthrd = new CmdThread(cmd, params);
			if(cmdRunningThrd != null && cmdRunningThrd.getCmdState()== CmdThread.STAT_RUNNING){
				cmdthrd.setCmdState(CmdThread.STAT_WAITING);
				cmdWaittingThrd = cmdthrd;
				AppDebug.debug("=====��ӵȴ�CmdThread" + cmdthrd.getId() + "====");
				
			} else {
				cmdWaittingThrd = null;
				cmdthrd.setCmdState(CmdThread.STAT_RUNNING);
				cmdRunningThrd = cmdthrd;
				cmdRunningThrd.start();
//				EventQueue.invokeLater(cmdRunningThrd);�˴���������ʹ�á�invokeLaterֻ�ʺ��ڶ�UI����ĸ��¡�
				
			}
			
//				 cmd.run(params, null);
		}
	}
	
	//�����߳�,��Ϊ�����л���Ԫ��ʱ�漰�������صȲ�������stop�̣߳������֤ǰһ���̵߳����У�ͬʱ���һ���߳̽���ȴ�״̬��
	CmdThread cmdRunningThrd = null;
	//�ȴ��߳�
	CmdThread cmdWaittingThrd = null;

	private static Object lock = new Object();
	
	/**
	 * ʹ���̷߳�ʽʵ�ֹ�ʽ׷�ٵļ������ʾ��
	 * �ﵽ����Ŀ�ģ�1����Ԫ���л����������Գٶۣ�2������л���Ԫ��ʱ���뱣ֻ֤��һ���߳��ڴ���ʽ׷�١�
	 * @author liuyy
	 */
	class CmdThread extends Thread {
		UfoCommand cmd = null;
		Object[] params = null;
		private int cmdstat = STAT_COMPLETE;
		static final int STAT_RUNNING = 1;//����
		static final int STAT_WAITING = 2;//�ȴ�
		static final int STAT_COMPLETE = 3;//���
		

		CmdThread(UfoCommand cmd, Object[] params) {
			this.cmd = cmd;
			this.params = params;
			setName("CmdThread");
		}

		/**
		 * @i18n miufo00604======��ʼ����CmdThread
		 */
		public void run() {
			try {
				AppDebug.debug("=====��ʼ����CmdThread" + this.getId() + "====");
//				this.sleep(5000);
				cmd.run(params, null);
				
			} catch(Throwable e){
				AppDebug.debug(e);
			} finally {
				this.cmd = null;
				this.params = null;
				synchronized (lock) {
					this.setCmdState(CmdThread.STAT_COMPLETE);
					if(cmdWaittingThrd != null && cmdWaittingThrd.getCmdState() == CmdThread.STAT_WAITING){
						cmdRunningThrd = cmdWaittingThrd;
						cmdWaittingThrd = null;
						cmdRunningThrd.setCmdState(CmdThread.STAT_RUNNING);
						cmdRunningThrd.start();
					} else {
						cmdRunningThrd = null;
						cmdWaittingThrd = null;
					}
				}
			}
		}

		public int getCmdState() {
			return cmdstat;
		}

		public void setCmdState(int state) {
			this.cmdstat = state;
		}
	}

}
  