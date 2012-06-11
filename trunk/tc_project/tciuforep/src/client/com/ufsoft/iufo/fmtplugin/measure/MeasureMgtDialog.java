package com.ufsoft.iufo.fmtplugin.measure;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.table.TableCellEditor;

import nc.ui.pub.beans.UIPanel;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.resource.ResConst;

/**
@update
界面修改
@end
 * 此处插入类型描述。
 * 创建日期：(2002-5-27 10:00:52)
 * @author：王海涛
 */
public class MeasureMgtDialog extends UfoDialog implements ActionListener{
	private javax.swing.JButton ivjJBOK = null;
    private javax.swing.JButton ivjJBCancel = null;
	private MeasureMgtPane ivjJScrollPane = null;
	private javax.swing.JPanel ivjUfoDialogContentPane = null;
    private JPanel buttonPan = null;
	private MeasureMgtTableModel m_measureMgtTableModel = null;
/**
 * MeasureManagerDialog 构造子注解。
 */
public MeasureMgtDialog() {
	super();
}
/**
 * MeasureDialog 构造子注解。
 * @param parent java.awt.Container
 */
public MeasureMgtDialog(Container container ,MeasureMgtTableModel data) {
	super(container);
	this.m_measureMgtTableModel = data;
	initialize();
	data.addMouseListener(ivjJScrollPane.getTable());
}
/**
*
*/
public void actionPerformed(java.awt.event.ActionEvent e)
{
	if (e.getSource()==ivjJBOK)
	{
//		  如果表格中有编辑的项，应该先设值
	    TableCellEditor editor = getJScrollPane().getTable().getCellEditor();
		if (editor != null) {
			Object value = editor.getCellEditorValue();
			m_measureMgtTableModel.setValueAt(value, 
			        getJScrollPane().getTable().getEditingRow(), 
			        getJScrollPane().getTable().getEditingColumn());
		}
		setResult(UfoDialog.ID_OK);
//        this.data.setCellPropty();
		close();
	}else
    {
        close();
    }
}
/**
 * 此处插入方法说明。
 * 创建日期：(2001-4-12 11:04:54)
 */
private void addHelp()
{
	javax.help.HelpBroker hb=ResConst.getHelpBroker();
	if( hb == null )
		return;
	hb.enableHelpKey(getContentPane(), "TM_Data_Element_Manage", null);

}
/**
 * 返回 JBOK 特性值。
 * @return javax.swing.JButton
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JButton getJBOK() {
	if (ivjJBOK == null) {
		try {
			ivjJBOK = new nc.ui.pub.beans.UIButton();
			ivjJBOK.setName("JButtonOK");
			ivjJBOK.setText(StringResource.getStringResource("miufopublic246"));  //"确定"
			ivjJBOK.setPreferredSize(new Dimension( 75, 22));
            ivjJBOK.setSize(85, 27);
			ivjJBOK.addActionListener(this);
			ivjJBOK.registerKeyboardAction(this,KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,false),JComponent.WHEN_FOCUSED);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJBOK;
}
private javax.swing.JButton getJBCancel() {
    if (ivjJBCancel == null) {
        try {
            ivjJBCancel = new nc.ui.pub.beans.UIButton();
            ivjJBCancel.setName("JButtonCancel");
            ivjJBCancel.setText(StringResource.getStringResource("miufopublic247"));  //"取消"
            ivjJBCancel.setPreferredSize(new Dimension( 75, 22));
            ivjJBCancel.setSize(85, 27);
            ivjJBCancel.addActionListener(this);
            ivjJBCancel.registerKeyboardAction(this,KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,false),JComponent.WHEN_FOCUSED);
            // user code begin {1}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {2}
            // user code end
            handleException(ivjExc);
        }
    }
    return ivjJBCancel;
}

/**
 * 返回 JScrollPane 特性值。
 * @return javax.swing.JScrollPane
 */
/* 警告：此方法将重新生成。 */
private MeasureMgtPane getJScrollPane() {
	if (ivjJScrollPane == null) {
		try {

			ivjJScrollPane = new MeasureMgtPane(m_measureMgtTableModel);
			ivjJScrollPane.setName("JScrollPane");
			ivjJScrollPane.setMinimumSize ( new Dimension( 330, 230));

			ivjJScrollPane.setAutoscrolls(true);
			Border etched = BorderFactory.createEtchedBorder();
			Border title = BorderFactory.createTitledBorder(etched,StringResource.getStringResource("miufo1001538"));  //"指标列表"
			ivjJScrollPane.setBorder(title);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	//return scrollPane;
	return ivjJScrollPane;
}
	public MeasureMgtTableModel getMeasureMgtTableModel()
	{
		return this.m_measureMgtTableModel;
	}
    private JPanel getButtonPanel()
    {
        if(buttonPan == null)
        { 
                buttonPan = new UIPanel();
                buttonPan.setLayout(new GridBagLayout());
                buttonPan.add(getJBOK());
                buttonPan.add(Box.createHorizontalStrut(30));
                buttonPan.add(getJBCancel());
            
        }
        return buttonPan;
    }
/**
 * 返回 UfoDialogContentPane 特性值。
 * @return javax.swing.JPanel
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JPanel getUfoDialogContentPane() {
	if (ivjUfoDialogContentPane == null) {
		try {
			ivjUfoDialogContentPane = new UIPanel();
			ivjUfoDialogContentPane.setName("UfoDialogContentPane");
			ivjUfoDialogContentPane.setLayout(new BorderLayout());

			getUfoDialogContentPane().add(getJScrollPane(),BorderLayout.CENTER);
			
			getUfoDialogContentPane().add(getButtonPanel(),BorderLayout.SOUTH);

		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUfoDialogContentPane;
}
/**
 * 每当部件抛出异常时被调用
 * @param exception java.lang.Throwable
 */
private void handleException(java.lang.Throwable exception) {
	/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
	
	AppDebug.debug(exception);//@devTools  exception.printStackTrace(System.out);
}
/**
 * 初始化类。
 */
/* 警告：此方法将重新生成。 */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("MeasureManagerDialog");
		setTitle(StringResource.getStringResource("miufo1001539"));  //"指标管理"
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setSize(900, 600);
		setContentPane(getUfoDialogContentPane());//UIUtil.showPanelBorder(getUfoDialogContentPane()));//liuyy.

	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	addHelp();
	// user code begin {2}
	// user code end
}
 
}


