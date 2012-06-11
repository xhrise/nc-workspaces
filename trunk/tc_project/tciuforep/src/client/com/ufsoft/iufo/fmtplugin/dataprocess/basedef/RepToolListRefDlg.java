package com.ufsoft.iufo.fmtplugin.dataprocess.basedef;
import java.util.Vector;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIPanel;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;

/**
 * ������ͨ��List���նԻ���
 *
 * �������ڣ�(2004-4-2 14:20:52)
 * @author������Ƽ
 */
public class RepToolListRefDlg extends com.ufsoft.report.dialog.UfoDialog implements java.awt.event.
    ActionListener{
    /**
     * ������Ŀʸ��
     */
    private Vector m_vecRefItem = null;
    /**
     * ���ձ���
     */
    private String m_strTitle = null;
    /**
     * ѡ��ֵ
     */
    private Object m_oSelItem = null;
    //�������
    private javax.swing.JButton ivjJBtnCancel = null;
    private javax.swing.JButton ivjJBtnOK = null;
    private javax.swing.JLabel ivjJLabelRef = null;
    private javax.swing.JPanel ivjJPanel1 = null;
    private javax.swing.JPanel ivjJPanel2 = null;
    private javax.swing.JPanel ivjUfoDialogContentPane = null;
    private javax.swing.JComboBox ivjJComboBoxRefItem = null;
    /**
     * RepToolListRefDlg ������ע�⡣
     */
    public RepToolListRefDlg(){
        super();
        initialize();
    }

    /**
     * RepToolListRefDlg ������ע�⡣
     * @param parent java.awt.Container
     * @param vecRefItem Vector
     * @param strTitle String
     */
    public RepToolListRefDlg(java.awt.Container parent, Vector vecRefItem, String strTitle){
        super(parent);
        this.m_vecRefItem = vecRefItem;
        this.m_strTitle = strTitle;

        initialize();

    }

    /**
     * Invoked when an action occurs.
     * @param event java.awt.event.ActionEvent
     */
    public void actionPerformed(java.awt.event.ActionEvent event){
        if(event.getSource() == getJBtnOK()){
            //����ѡ�е�ֵΪ����ֵ
            m_oSelItem = (Object)getJComboBoxRefItem().getSelectedItem();
            //�رմ���
            setResult(UfoDialog.ID_OK);
            close();
        } else if(event.getSource() == getJBtnCancel()){
            this.setResult(UfoDialog.ID_CANCEL);
            close();
        }
    }

    /**
     * ���� JBtnCancel ����ֵ��
     * @return javax.swing.JButton
     */
    /* ���棺�˷������������ɡ� */
    private javax.swing.JButton getJBtnCancel(){
        if(ivjJBtnCancel == null){
            try{
                ivjJBtnCancel = new UIButton();
                ivjJBtnCancel.setName("JBtnCancel");
                ivjJBtnCancel.setText("Cancel");
                ivjJBtnCancel.setBounds(169, 10, 85, 27);
                // user code begin {1}
                String strCancel = StringResource.getStringResource(StringResource.CANCEL);
                ivjJBtnCancel.setText(strCancel);
                ivjJBtnCancel.addActionListener(this);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJBtnCancel;
    }

    /**
     * ���� JBtnOK ����ֵ��
     * @return javax.swing.JButton
     */
    /* ���棺�˷������������ɡ� */
    private javax.swing.JButton getJBtnOK(){
        if(ivjJBtnOK == null){
            try{
                ivjJBtnOK = new UIButton();
                ivjJBtnOK.setName("JBtnOK");
                ivjJBtnOK.setText("OK");
                ivjJBtnOK.setBounds(47, 10, 85, 27);
                // user code begin {1}
                String strOK = StringResource.getStringResource(StringResource.OK);
                ivjJBtnOK.setText(strOK);                
                ivjJBtnOK.addActionListener(this);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJBtnOK;
    }

    /**
     * ���� JComboBoxRefItes ����ֵ��
     * @return javax.swing.JComboBox
     */
    /* ���棺�˷������������ɡ� */
    private javax.swing.JComboBox getJComboBoxRefItem(){
        if(ivjJComboBoxRefItem == null){
            try{
                ivjJComboBoxRefItem = new UIComboBox();
                ivjJComboBoxRefItem.setName("JComboBoxRefItem");
                ivjJComboBoxRefItem.setBounds(25, 46, 130, 25);
                // user code begin {1}
                initRefItem();
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJComboBoxRefItem;
    }

    /**
     * ���� JLabelRef ����ֵ��
     * @return javax.swing.JLabel
     */
    /* ���棺�˷������������ɡ� */
    private javax.swing.JLabel getJLabelRef(){
        if(ivjJLabelRef == null){
            try{
                ivjJLabelRef = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
                ivjJLabelRef.setName("JLabelRef");
                ivjJLabelRef.setText("UnitStrucRef");
                ivjJLabelRef.setBounds(28, 16, 208, 16);
                // user code begin {1}
                String strUnitStrucRef = StringResource.getStringResource("miufo1001307");  //"��λ�ṹ����"
                ivjJLabelRef.setText(strUnitStrucRef);
                if(m_strTitle != null){
                    ivjJLabelRef.setText(m_strTitle);
                }
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJLabelRef;
    }

    /**
     * ���� JPanel1 ����ֵ��
     * @return javax.swing.JPanel
     */
    /* ���棺�˷������������ɡ� */
    private javax.swing.JPanel getJPanel1(){
        if(ivjJPanel1 == null){
            try{
                ivjJPanel1 = new UIPanel();
                ivjJPanel1.setName("JPanel1");
                ivjJPanel1.setLayout(null);
                ivjJPanel1.setBounds(36, 14, 305, 237);
                getJPanel1().add(getJLabelRef(), getJLabelRef().getName());
                getJPanel1().add(getJComboBoxRefItem(), getJComboBoxRefItem().getName());
                // user code begin {1}
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJPanel1;
    }

    /**
     * ���� JPanel2 ����ֵ��
     * @return javax.swing.JPanel
     */
    /* ���棺�˷������������ɡ� */
    private javax.swing.JPanel getJPanel2(){
        if(ivjJPanel2 == null){
            try{
                ivjJPanel2 = new UIPanel();
                ivjJPanel2.setName("JPanel2");
                ivjJPanel2.setLayout(null);
                ivjJPanel2.setBounds(41, 266, 305, 50);
                getJPanel2().add(getJBtnOK(), getJBtnOK().getName());
                getJPanel2().add(getJBtnCancel(), getJBtnCancel().getName());
                // user code begin {1}
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJPanel2;
    }

    /**
     * ���� UfoDialogContentPane ����ֵ��
     * @return javax.swing.JPanel
     */
    /* ���棺�˷������������ɡ� */
    private javax.swing.JPanel getUfoDialogContentPane(){
        if(ivjUfoDialogContentPane == null){
            try{
                ivjUfoDialogContentPane = new UIPanel();
                ivjUfoDialogContentPane.setName("UfoDialogContentPane");
                ivjUfoDialogContentPane.setLayout(null);
                getUfoDialogContentPane().add(getJPanel1(), getJPanel1().getName());
                getUfoDialogContentPane().add(getJPanel2(), getJPanel2().getName());
                // user code begin {1}
                // user code end
            } catch(java.lang.Throwable ivjExc){
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
    private void handleException(java.lang.Throwable exception){

        /* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
        // System.out.println("--------- δ��׽�����쳣 ---------");
        // exception.printStackTrace(System.out);
    }

    /**
     * ��ʼ���ࡣ
     */
    /* ���棺�˷������������ɡ� */
    private void initialize(){
        try{
            // user code begin {1}
            // user code end
            setName("RepToolListRefDlg");
            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setSize(355, 346);
            setContentPane(getUfoDialogContentPane());
        } catch(java.lang.Throwable ivjExc){
            handleException(ivjExc);
        }
        // user code begin {2}
        String strTitle = ivjJLabelRef.getText();
        setTitle(strTitle);
        // user code end
    }

    /**
     * ����ڵ� - ��������ΪӦ�ó�������ʱ���������������
     * @param args java.lang.String[]
     */
    public static void main(java.lang.String[] args){
        try{
            RepToolListRefDlg aRepToolListRefDlg;
            aRepToolListRefDlg = new RepToolListRefDlg();
            aRepToolListRefDlg.setModal(true);
            aRepToolListRefDlg.addWindowListener(new java.awt.event.WindowAdapter(){
                public void windowClosing(java.awt.event.WindowEvent e){
                    System.exit(0);
                };
            });
            aRepToolListRefDlg.show();
            java.awt.Insets insets = aRepToolListRefDlg.getInsets();
            aRepToolListRefDlg.setSize(aRepToolListRefDlg.getWidth() + insets.left + insets.right,
                                       aRepToolListRefDlg.getHeight() + insets.top + insets.bottom);
            aRepToolListRefDlg.setVisible(true);
        } catch(Throwable exception){
            System.err.println("com.ufsoft.iuforeport.reporttool.pub.UfoDialog 's  main() :Exception");
AppDebug.debug(exception);//@devTools             exception.printStackTrace(System.out);
        }
    }
    /**
     * �õ�ѡ�е�ֵ
     * @return Object
     */
    public Object getReturnValue(){
        return m_oSelItem;
    }
    /**
     * �������б����ݼ��뵽�б�ؼ���
     */
    private void    initRefItem(){
        if(m_vecRefItem!=null){
            int iSize = m_vecRefItem.size();
            for (int i = 0; i < iSize; i++) {
                ivjJComboBoxRefItem.addItem(m_vecRefItem.get(i));
            }
        }
    }
}


