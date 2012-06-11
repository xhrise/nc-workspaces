package com.ufsoft.report;

import java.awt.BorderLayout;

import javax.swing.SwingUtilities;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.docking.view.actions.AbstractPageAction;
import com.ufida.zior.exception.MessageException;
import com.ufida.zior.view.Editor;
import com.ufida.zior.view.Viewer;
import com.ufida.zior.view.event.ViewerListener;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.ReportTable;
import com.ufsoft.table.SelectModel;
import com.ufsoft.table.SeperateLockSet;
import com.ufsoft.table.event.CellsModelChangeHandler;
import com.ufsoft.table.event.CellsModelSelectedHandler;
import com.ufsoft.table.event.CellsModelSelectedListener;
import com.ufsoft.table.event.HeaderModelHander;
import com.ufsoft.table.event.MouseListenerHandler;
import com.ufsoft.table.event.PropertyChangeHandler;
import com.ufsoft.table.event.SelectEvent;
import com.ufsoft.table.event.UserActionHandler;

/**
 * ��������������࣬�����ʼ�������༭���
 * 
 * @author liuyy
 * 
 */
public abstract class ReportDesigner extends Editor {

	private static final long serialVersionUID = 6262710974919678702L;

	private ReportTable m_table = null;

	{
		// ����������Ĳ�����ػ���Ϊ˲̬���ء�
		setPluginLoadStrategy(PLUGIN_LOAD_STRATEGY_INSTANT);
	}

	@Override
	public void startup() {
		//��ʼ��һЩ���õ�context����
		initContext();
		initEventHandler();
		setIcon("/images/reportcore/report.gif");
		m_table = ReportTable.createReportTable(true, true);
		removeAll();
		setLayout(new BorderLayout());
		add(m_table, BorderLayout.CENTER);
		getEventManager().addListener(createReportEventLinstener());
		addTitleAction(new AbstractPageAction.PreviousvViewAction(), 0);
		addTitleAction(new AbstractPageAction.NextViewAction(), 1);
	}

	/**
	 * ��ʼ������ģ��
	 * 
	 * @param model
	 */
	public void setCellsModel(CellsModel model) {
        if(model == null){
        	throw new IllegalArgumentException("model is null...");
        }

		getCellsPane().setDataModel(model);		
		// @edit by wangyga at 2009-3-5,����11:30:38 ����TableHeader
		getCellsPane().getTablePane().initTableHeader();
		
		//�����������
		SeperateLockSet set = model.getSeperateLockSet();
		if(set == null){
			getTable().setFreezing(false);
			getTable().cancelSeperate();			
		}else{
			int sptRow = set.getSeperateRow();
			int sptCol = set.getSeperateCol();
			boolean freezing = set.isFreezing();

			getTable().cancelSeperate();
			
			getTable().setSeperatePos(sptRow, sptCol);
			
			getTable().setFreezing(freezing);
		}
		
		getTable().initSeperator(model.getSeperateLockSet());
	}
		
	@Override
	public boolean store() {
		boolean isSucceedSave = false;
		try {
			isSucceedSave = save();
		} catch (MessageException e) {
			throw e;
		} catch (Throwable e) {
			AppDebug.debug(e);
			setHintMessage("����ʧ��");
			throw new RuntimeException(e.getMessage(), e);
		}
		getCellsModel().setDirty(!isSucceedSave);
		setHintMessage(StringResource.getStringResource("miufopublic391"));
		return isSucceedSave;
	}

	protected abstract boolean save();
	
	protected void initContext() {}
	
	@Override
	public String getIcon() {
		return "/images/reportcore/report.gif";
	}

	/**
	 * �Ƿ���Ҫ����
	 */
	public boolean isDirty() {
		if (getCellsModel() != null) {
			return getCellsModel().isDirty();
		}
		return false;
	}

	public ReportTable getTable() {
		return m_table;
	}

	public CellsPane getCellsPane() {
		if (m_table == null) {
			throw new IllegalArgumentException();
		}
		return m_table.getCells();
	}

	public CellsModel getCellsModel() {
		return getCellsPane().getDataModel();
	}

	protected  ReportEventLinstener createReportEventLinstener(){
		return new ReportEventLinstener();
	}
	
	/**
	 * ��ʼ��������༭�����¼�����handler
	 * @create by wangyga at 2009-7-20,����09:01:32
	 *
	 */
	protected void initEventHandler(){
		getEventManager().addHandler(new CellsModelSelectedHandler());
		getEventManager().addHandler(new UserActionHandler());
		getEventManager().addHandler(new HeaderModelHander());
		getEventManager().addHandler(new CellsModelChangeHandler());
		getEventManager().addHandler(new MouseListenerHandler());
		getEventManager().addHandler(new PropertyChangeHandler());	
	}
	
	private class ReportEventLinstener extends ViewerListener.Sub implements CellsModelSelectedListener{

		@Override
		public void onActive(Viewer newActiveView, Viewer oldActiveView) {
			SwingUtilities.invokeLater(new Runnable(){
				public void run() {
					if(getCellsPane() != null){
						getCellsPane().requestFocus();
					}
				}
			});
			
			if(newActiveView != ReportDesigner.this){
				return;
			}
			if(getCellsModel() == null){
				return;
			}
			SelectModel selectModel = getCellsModel().getSelectModel();
			final SelectEvent evt = new SelectEvent(selectModel, SelectEvent.ANCHOR_CHANGED, selectModel.getAnchorCell(),selectModel.getAnchorCell());

			getCellsPane().selectedChanged(evt);
		}
		
		//��֤�������б���ֻ��һ��
		public boolean equals(Object o){
			if(o instanceof ReportEventLinstener){
				return true;
			}
			return false;
		}

		public int hashCode(){
			return ReportEventLinstener.class.getName().hashCode();
		}
		
		/**
		 * ������༭������Ҫ��ê���л�ʱУ������Ŀ�����
		 */
		public void anchorChanged(CellsModel model, CellPosition oldAnchor,
				CellPosition newAnchor) {
			Viewer curViewer = getMainboard().getCurrentView();
			getMainboard().getPluginManager().validateAllCompsEnable(
					curViewer);
		}

		public void selectedChanged(CellsModel cellsModel,
				AreaPosition[] changedArea) {
			
		}
		
	}
	
}
