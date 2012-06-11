package com.ufida.report.rep.applet;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.dnd.MouseDragGestureRecognizer;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.TooManyListenersException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.event.EventListenerList;

import sun.awt.dnd.SunDragSourceContextPeer;

import nc.ui.bi.query.manager.RptProvider;
import nc.vo.bi.query.manager.MetaDataVO;
import nc.vo.bi.query.manager.QueryModelVO;
import nc.vo.iufo.datasetmanager.DataSetDefVO;

import com.borland.dx.dataset.Variant;
import com.ufida.bi.base.BIException;
import com.ufida.dataset.descriptor.DescriptorType;
import com.ufida.dataset.metadata.Field;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.report.adhoc.model.AdhocPageDimField;
import com.ufida.report.adhoc.model.AdhocPublic;
import com.ufida.report.adhoc.model.PageDimField;
import com.ufida.report.anareport.applet.AnaRepAdapter.DragInfo;
import com.ufida.report.anareport.model.AnaRepField;
import com.ufida.report.anareport.model.AnaReportCondition;
import com.ufida.report.anareport.model.FieldCountDef;
import com.ufida.report.rep.model.DefaultReportField;
import com.ufida.report.rep.model.MetaDataSelection;
import com.ufida.report.rep.model.MetaDataVOSelection;
import com.ufsoft.iufo.data.IMetaData;
import com.ufsoft.report.sysplugin.dnd.DndObjectTransferable;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.iufo.resource.StringResource;

/**
 * 页纬度面板托拽目标处理
 * @author caijie
 */
public class PageDimNavigationDropTarget extends MouseDragGestureRecognizer implements DropTargetListener,DragSourceListener,DragGestureListener,ContainerListener{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static int motionThreshold;
	protected static final int ButtonMask=InputEvent.BUTTON2_DOWN_MASK|InputEvent.BUTTON2_DOWN_MASK|InputEvent.BUTTON3_DOWN_MASK;
	
	private transient EventListenerList listenerList  = new EventListenerList();
    private PageDimNavigationPanel m_panel = null;
    public PageDimNavigationDropTarget(PageDimNavigationPanel panel) {
    	super(DragSource.getDefaultDragSource(),panel,DnDConstants.ACTION_MOVE);
    	m_panel = panel;
        DropTarget dropTarget = new DropTarget(panel,
                java.awt.dnd.DnDConstants.
                ACTION_COPY_OR_MOVE, this);
        try {
			addDragGestureListener(this);
		} catch (TooManyListenersException e) {
			// TODO Auto-generated catch block
			AppDebug.debug(e);
		}
        panel.addContainerListener(this);
    }

    public PageDimNavigationPanel getDropPanel(){
    	return m_panel;
    }
    /* (non-Javadoc)
     * @see java.awt.dnd.DropTargetListener#dragEnter(java.awt.dnd.DropTargetDragEvent)
     */
    public void dragEnter(DropTargetDragEvent dtde) {
    	AppDebug.debug("--dragEnter--");//@devTools System.out.println("--dragEnter--");
        
    }

    /* (non-Javadoc)
     * @see java.awt.dnd.DropTargetListener#dragOver(java.awt.dnd.DropTargetDragEvent)
     */
    public void dragOver(DropTargetDragEvent dtde) {
//    	System.out.println("--dragOver--");
        
    }

    /* (non-Javadoc)
     * @see java.awt.dnd.DropTargetListener#dropActionChanged(java.awt.dnd.DropTargetDragEvent)
     */
    public void dropActionChanged(DropTargetDragEvent dtde) {
//    	System.out.println("--dropActionChanged--");
        
    }

    /* (non-Javadoc)
     * @see java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
     */
    /**
	 * @i18n miufo00054=页纬度设置错误,请重新设置
     * @i18n iufobi00097=不支持把该字段设置成页纬度
	 */
    public void drop(DropTargetDropEvent dtde) {
		try {
			Transferable tr = dtde.getTransferable();
			dtde.acceptDrop(java.awt.dnd.DnDConstants.ACTION_COPY);
			
			
			if (tr.isDataFlavorSupported(DndObjectTransferable.DND_OBJ_FLAVOR)) {
				AnaReportCondition anaField = null;
				DragInfo drag = (DragInfo) tr
						.getTransferData(DndObjectTransferable.DND_OBJ_FLAVOR);
				if (drag != null) {
					Field fld = null;
					String datasetPK = null;
					if (drag.getDragObj() instanceof Field) {// DatasetDef树的拖动
						fld = (Field) drag.getDragObj();
					} else if (drag.getDragObj() instanceof AnaRepField) {
						AnaRepField anaFld = (AnaRepField) drag.getDragObj();
						if (!(anaFld.getField() instanceof FieldCountDef)) {
							fld = anaFld.getField();
							datasetPK = anaFld.getDSPK();
						} else {
							UfoPublic.showErrorDialog(m_panel, StringResource.getStringResource("iufobi00097"), "");
							return;
						}
					}
					if (drag.getOtherInfo() instanceof DataSetDefVO) {
						DataSetDefVO dataset = (DataSetDefVO) drag
								.getOtherInfo();
						datasetPK = dataset.getPk_datasetdef();
					}
					if (fld != null && datasetPK != null
					// && dataset.getDataSetDef().supportDescriptorFunc(
					// DescriptorType.FilterDescriptor)
					) {
						if (fld.getDataType() == Variant.STRING
								|| fld.getExtType() == RptProvider.DIMENSION) {
							anaField = new AnaReportCondition(datasetPK, fld);
						} else {
							UfoPublic.showErrorDialog(m_panel, StringResource.getStringResource("iufobi00097"), "");
							return;
						}

					}
				}
				// 通知AnaReportModel
				if (anaField != null) {
					notifyListeners(new PropertyChangeEvent(this,
							AdhocPublic.PAGE_DIMENSION_ADD, null, anaField));
				} else {
					UfoPublic.showErrorDialog(m_panel, StringResource
							.getStringResource("miufo00054"), "");
				}

			} else {
				QueryModelVO qm = null;
				MetaDataVO md = null;
				IMetaData imd = null;

				if (tr
						.isDataFlavorSupported(MetaDataVOSelection.QueryModelVOFlavor)) {
					qm = (QueryModelVO) tr
							.getTransferData(MetaDataVOSelection.QueryModelVOFlavor);
				}
				if (tr
						.isDataFlavorSupported(MetaDataVOSelection.MetaDataVOFlavor)) {
					md = (MetaDataVO) tr
							.getTransferData(MetaDataVOSelection.MetaDataVOFlavor);
				}
				if (tr
						.isDataFlavorSupported(MetaDataVOSelection.ReportFieldFlavor)) {
					imd = (DefaultReportField) tr
							.getTransferData(MetaDataVOSelection.ReportFieldFlavor);
				} else if (tr
						.isDataFlavorSupported(MetaDataSelection.MetaDataFlavor)) {
					imd = (IMetaData) tr
							.getTransferData(MetaDataSelection.MetaDataFlavor);
				}

				if (md == null && imd == null) {
					return;
				}
				PageDimField field = (md != null) ? (new AdhocPageDimField(qm
						.getID(), md)) : (new AdhocPageDimField(null, imd));

				// 通知AdhocModel，准备添加页纬度
				notifyListeners(new PropertyChangeEvent(this,
						AdhocPublic.PAGE_DIMENSION_ADD, null, field));
			}

		} catch (BIException e1) {
			JOptionPane.showMessageDialog(m_panel, e1.getMessage(), "",
					JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			AppDebug.debug(e);// @devTools AppDebug.debug(e);
			// dtde.rejectDrop();
		}
	}

    /*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.dnd.DropTargetListener#dragExit(java.awt.dnd.DropTargetEvent)
	 */
    public void dragExit(DropTargetEvent dte) {
//    	System.out.println("--dragExit--");
        
    }
    
    /**
	 * Registers an object for notification of changes to the dataset.
	 * 
	 * @param listener
	 *            the object to register.
	 */
    public void addChangeListener(PropertyChangeListener listener) {
        this.listenerList.add(PropertyChangeListener.class, listener);
    }

    /**
     * Deregisters an object for notification of changes to the dataset.
     *
     * @param listener  the object to deregister.
     */
    public void removeChangeListener(PropertyChangeListener listener) {
        this.listenerList.remove(PropertyChangeListener.class, listener);
    }
    

    /**
     * Notifies all registered listeners that the dataset has changed.
     *
     * @param event  contains information about the event that triggered the notification.
     */
    private  void notifyListeners(final PropertyChangeEvent event) {
        final Object[] listeners = this.listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == PropertyChangeListener.class) {
                ((PropertyChangeListener) listeners[i + 1]).propertyChange(event);
            }
        }
    }

	public void dragDropEnd(DragSourceDropEvent dsde) {
		Component dragComp=dsde.getDragSourceContext().getComponent();
		if(dragComp instanceof PageDimItem){
			PageDimItem pageItem=(PageDimItem)dragComp;
			notifyListeners(new PropertyChangeEvent(this,
					AdhocPublic.PAGE_DIMENSION_REMOVE, pageItem.getPageDimField(), null));
		}
//		System.out.println("--dragDropEnd--");
	}

	public void dragEnter(DragSourceDragEvent dsde) {
//		System.out.println("--dragEnter--");
		
	}

	public void dragExit(DragSourceEvent dse) {
//		System.out.println("--dragExit--");
		
	}

	public void dragOver(DragSourceDragEvent dsde) {
//		System.out.println("--dragOver--");
		
	}

	public void dropActionChanged(DragSourceDragEvent dsde) {
//		System.out.println("--dropActionChanged--");
		
	}

	public void dragGestureRecognized(DragGestureEvent dge) {
//		System.out.println("--dragGestureRecognized--");
		Component dragComp=dge.getComponent();
		Transferable transferable = new DndObjectTransferable(dragComp);  
        Cursor cursor = DragSource.DefaultCopyDrop;
        int action = dge.getDragAction();                        
        if (action == DnDConstants.ACTION_MOVE)
            cursor = DragSource.DefaultMoveDrop;
        dge.startDrag(cursor,transferable,this); 
	}

	public void componentAdded(ContainerEvent e) {
//		System.out.println("--componentAdded--");
		PageDimItem c =null;
		if(e.getChild() instanceof PageDimItem){
			c=(PageDimItem)e.getChild();
			c.getLabel().addMouseListener(this);
			c.getLabel().addMouseMotionListener(this);
		}
		
	}

	public void componentRemoved(ContainerEvent e) {
//		System.out.println("--componentRemoved--");
		Component c = e.getChild();
		c.removeMouseListener(this);
		c.removeMouseMotionListener(this);
	}

	protected int mapDragOperationFromModifiers(MouseEvent e) {
        int mods = e.getModifiersEx();
        int btns = mods;
 
        // Prohibit multi-button drags.
        if (!(btns == InputEvent.BUTTON1_DOWN_MASK ||
              btns == InputEvent.BUTTON2_DOWN_MASK ||
              btns == InputEvent.BUTTON3_DOWN_MASK)) {
            return DnDConstants.ACTION_NONE;
        }
 
        return 
            SunDragSourceContextPeer.convertModifiersToDropAction(mods,
                                                                  getSourceActions()); 
    }
 
    /**
     * Invoked when the mouse has been clicked on a component.
     */
 
    public void mouseClicked(MouseEvent e) {
 // do nothing
    }
 
    /**
     * Invoked when a mouse button has been pressed on a component.
     */
 
    public void mousePressed(MouseEvent e) {
		events.clear();

		if (mapDragOperationFromModifiers(e) != DnDConstants.ACTION_NONE) {
			try {
				motionThreshold = DragSource.getDragThreshold();
			} catch (Exception exc) {
				motionThreshold = 5;
			}
			appendEvent(e);
		}
	}
 
    /**
	 * Invoked when a mouse button has been released on a component.
	 */
 
    public void mouseReleased(MouseEvent e) {
 events.clear();
    }
 
    /**
     * Invoked when the mouse enters a component.
     */
 
    public void mouseEntered(MouseEvent e) {
 events.clear();
    }
 
    /**
     * Invoked when the mouse exits a component.
     */
 
    public void mouseExited(MouseEvent e) {

		if (!events.isEmpty()) { // gesture pending
			int dragAction = mapDragOperationFromModifiers(e);

			if (dragAction == DnDConstants.ACTION_NONE) {
				events.clear();
			}
		}
	}
 
    /**
	 * Invoked when a mouse button is pressed on a component.
	 */
 
    public void mouseDragged(MouseEvent e) {
		if (!events.isEmpty()) { // gesture pending
			int dop = mapDragOperationFromModifiers(e);

			if (dop == DnDConstants.ACTION_NONE) {
				return;
			}

			MouseEvent trigger = (MouseEvent) events.get(0);

			Point origin = trigger.getPoint();
			Point current = e.getPoint();

			int dx = Math.abs(origin.x - current.x);
			int dy = Math.abs(origin.y - current.y);

			if (dx > motionThreshold || dy > motionThreshold) {
				if(e.getSource() instanceof JLabel){
					JLabel c=(JLabel)e.getSource();
					if(c.getParent() instanceof PageDimItem){
						this.setComponent(c.getParent());
					}
				}
				fireDragGestureRecognized(dop, ((MouseEvent) getTriggerEvent())
						.getPoint());
			} else
				appendEvent(e);
		}
	}
 
    /**
	 * Invoked when the mouse button has been moved on a component (with no
	 * buttons no down).
	 */
 
    public void mouseMoved(MouseEvent e) {
 // do nothing
    }
    
}
  