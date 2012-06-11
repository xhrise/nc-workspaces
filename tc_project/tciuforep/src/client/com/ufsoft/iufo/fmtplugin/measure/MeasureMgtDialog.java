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
�����޸�
@end
 * �˴���������������
 * �������ڣ�(2002-5-27 10:00:52)
 * @author��������
 */
public class MeasureMgtDialog extends UfoDialog implements ActionListener{
	private javax.swing.JButton ivjJBOK = null;
    private javax.swing.JButton ivjJBCancel = null;
	private MeasureMgtPane ivjJScrollPane = null;
	private javax.swing.JPanel ivjUfoDialogContentPane = null;
    private JPanel buttonPan = null;
	private MeasureMgtTableModel m_measureMgtTableModel = null;
/**
 * MeasureManagerDialog ������ע�⡣
 */
public MeasureMgtDialog() {
	super();
}
/**
 * MeasureDialog ������ע�⡣
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
//		  ���������б༭���Ӧ������ֵ
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
 * �˴����뷽��˵����
 * �������ڣ�(2001-4-12 11:04:54)
 */
private void addHelp()
{
	javax.help.HelpBroker hb=ResConst.getHelpBroker();
	if( hb == null )
		return;
	hb.enableHelpKey(getContentPane(), "TM_Data_Element_Manage", null);

}
/**
 * ���� JBOK ����ֵ��
 * @return javax.swing.JButton
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JButton getJBOK() {
	if (ivjJBOK == null) {
		try {
			ivjJBOK = new nc.ui.pub.beans.UIButton();
			ivjJBOK.setName("JButtonOK");
			ivjJBOK.setText(StringResource.getStringResource("miufopublic246"));  //"ȷ��"
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
            ivjJBCancel.setText(StringResource.getStringResource("miufopublic247"));  //"ȡ��"
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
 * ���� JScrollPane ����ֵ��
 * @return javax.swing.JScrollPane
 */
/* ���棺�˷������������ɡ� */
private MeasureMgtPane getJScrollPane() {
	if (ivjJScrollPane == null) {
		try {

			ivjJScrollPane = new MeasureMgtPane(m_measureMgtTableModel);
			ivjJScrollPane.setName("JScrollPane");
			ivjJScrollPane.setMinimumSize ( new Dimension( 330, 230));

			ivjJScrollPane.setAutoscrolls(true);
			Border etched = BorderFactory.createEtchedBorder();
			Border title = BorderFactory.createTitledBorder(etched,StringResource.getStringResource("miufo1001538"));  //"ָ���б�"
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
 * ���� UfoDialogContentPane ����ֵ��
 * @return javax.swing.JPanel
 */
/* ���棺�˷������������ɡ� */
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
 * ÿ�������׳��쳣ʱ������
 * @param exception java.lang.Throwable
 */
private void handleException(java.lang.Throwable exception) {
	/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
	
	AppDebug.debug(exception);//@devTools  exception.printStackTrace(System.out);
}
/**
 * ��ʼ���ࡣ
 */
/* ���棺�˷������������ɡ� */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("MeasureManagerDialog");
		setTitle(StringResource.getStringResource("miufo1001539"));  //"ָ�����"
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


