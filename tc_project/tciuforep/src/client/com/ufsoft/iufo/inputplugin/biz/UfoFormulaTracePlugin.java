package com.ufsoft.iufo.inputplugin.biz;

import java.util.EventObject;

import nc.ui.iufo.input.control.RepDataControler;
import nc.ui.iufo.input.edit.RepDataEditor;
import nc.ui.iufo.input.view.FormTraceResultViewer;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.plugin.AbstractPlugin;
import com.ufida.zior.plugin.IPluginAction;
import com.ufida.zior.view.Viewer;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.inputplugin.biz.formulatracenew.UfoFormulaTraceAction;
import com.ufsoft.iufo.inputplugin.biz.formulatracenew.UfoFormulaTraceRenderer;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.event.CellsModelSelectedListener;
import com.ufsoft.table.re.CellRenderAndEditor;

public class UfoFormulaTracePlugin extends AbstractPlugin implements CellsModelSelectedListener,IUfoContextKey{

	static{
		CellRenderAndEditor.getInstance().registExtSheetRenderer(new UfoFormulaTraceRenderer());
	}

	protected IPluginAction[] createActions() {
		return new IPluginAction[]{new UfoFormulaTraceAction()};
	}

	public void shutdown() {	
	}

	public void startup() {	
		getMainboard().getEventManager().addListener(this);
	}
	
	public String isSupport(int source, EventObject e) throws ForbidedOprException {
		return null;
	}


	public void anchorChanged(CellsModel model, CellPosition oldAnchor,
			CellPosition newAnchor) {
		RepDataControler controler=RepDataControler.getInstance(getMainboard());
		
//		controler.getLastActiveRepDataEditor().setTraceCells(null);
//		((RepDataEditor)getMainboard().getCurrentView()).setTraceCells(null);
		UfoFormulaTraceAction action = (UfoFormulaTraceAction)getPluginActions()[0];
		if(!controler.isCanFormulaTrace()){
			Viewer viewer = action.getMainboard().getView(RepDataControler.FORMULA_TRACERESULT_ID);
			if(viewer!=null){
				((FormTraceResultViewer)viewer).getTracePanel().setCurTracedPos(null);
			}
			return;
		}
		action.setSelPosition(newAnchor);
		CmdThread cmdthrd = new CmdThread(action);
		if(cmdRunningThrd != null && cmdRunningThrd.getCmdState()== CmdThread.STAT_RUNNING){
			cmdthrd.setCmdState(CmdThread.STAT_WAITING);
			cmdWaittingThrd = cmdthrd;
			AppDebug.debug("=====添加等待CmdThread" + cmdthrd.getId() + "====");
			
		} else {
			cmdWaittingThrd = null;
			cmdthrd.setCmdState(CmdThread.STAT_RUNNING);
			cmdRunningThrd = cmdthrd;
			cmdRunningThrd.start();
		}
	}

	// 允许线程,因为快速切换单元格时涉及代码下载等操作不能stop线程，因而保证前一个线程的运行，同时最后一个线程进入等待状态。

	CmdThread cmdRunningThrd = null;
	// 等待线程
	CmdThread cmdWaittingThrd = null;


	private static Object lock = new Object();


	/**
	 * 使用线程方式实现公式追踪的计算和显示。 达到两个目的：1、单元格切换不能有明显迟钝；2、疯狂切换单元格时必须保证只有一个线程在处理公式追踪。
	 * @author liuyy
	 */
	class CmdThread extends Thread {
		IPluginAction action = null;
		private int cmdstat = STAT_COMPLETE;
		static final int STAT_RUNNING = 1;// 运行
		static final int STAT_WAITING = 2;// 等待
		static final int STAT_COMPLETE = 3;// 完成

		CmdThread(IPluginAction action) {
			this.action = action;
			setName("CmdThread");
		}

		/**
		 * @i18n miufo00604======开始运行CmdThread
		 */
		public void run() {
			try {
				AppDebug.debug("=====开始运行CmdThread" + this.getId() + "====");
				// this.sleep(5000);
				action.execute(null);

			} catch (Throwable e) {
				AppDebug.debug(e);
			} finally {
				this.action = null;
				synchronized (lock) {
					this.setCmdState(CmdThread.STAT_COMPLETE);
					if (cmdWaittingThrd != null
							&& cmdWaittingThrd.getCmdState() == CmdThread.STAT_WAITING) {
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

	public void selectedChanged(CellsModel cellsModel,
			AreaPosition[] changedArea) {
		
	}
}
