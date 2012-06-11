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
 * 公式追踪的插件 NOTICE: 该插件需要在InputDataPlugIn之前加载，才能保证“公式追踪”的菜单项显示在“数据”菜单组的第一项
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
		// addPlugin的时候会注册SelectModelListener
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
			//防止过多的线程
			String strMenuName = FormulaTraceBizUtil.doGetStrFormulaTrace();//"公式追踪"
			String[] strParantMenuNames = new String[]{FormulaTraceBizUtil.doGetStrData()};//"数据"
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
				AppDebug.debug("=====添加等待CmdThread" + cmdthrd.getId() + "====");
				
			} else {
				cmdWaittingThrd = null;
				cmdthrd.setCmdState(CmdThread.STAT_RUNNING);
				cmdRunningThrd = cmdthrd;
				cmdRunningThrd.start();
//				EventQueue.invokeLater(cmdRunningThrd);此处不得这样使用。invokeLater只适合于对UI组件的更新。
				
			}
			
//				 cmd.run(params, null);
		}
	}
	
	//允许线程,因为快速切换单元格时涉及代码下载等操作不能stop线程，因而保证前一个线程的运行，同时最后一个线程进入等待状态。
	CmdThread cmdRunningThrd = null;
	//等待线程
	CmdThread cmdWaittingThrd = null;

	private static Object lock = new Object();
	
	/**
	 * 使用线程方式实现公式追踪的计算和显示。
	 * 达到两个目的：1、单元格切换不能有明显迟钝；2、疯狂切换单元格时必须保证只有一个线程在处理公式追踪。
	 * @author liuyy
	 */
	class CmdThread extends Thread {
		UfoCommand cmd = null;
		Object[] params = null;
		private int cmdstat = STAT_COMPLETE;
		static final int STAT_RUNNING = 1;//运行
		static final int STAT_WAITING = 2;//等待
		static final int STAT_COMPLETE = 3;//完成
		

		CmdThread(UfoCommand cmd, Object[] params) {
			this.cmd = cmd;
			this.params = params;
			setName("CmdThread");
		}

		/**
		 * @i18n miufo00604======开始运行CmdThread
		 */
		public void run() {
			try {
				AppDebug.debug("=====开始运行CmdThread" + this.getId() + "====");
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
  