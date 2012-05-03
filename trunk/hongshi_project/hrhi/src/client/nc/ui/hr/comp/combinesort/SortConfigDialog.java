package nc.ui.hr.comp.combinesort;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.table.AbstractTableModel;

import nc.hr.utils.ResHelper;
import nc.ui.hr.comp.sort.AttributeDescription;
import nc.ui.hr.comp.sort.GeneralTypeCompare;
import nc.ui.hr.comp.sort.Pair;
import nc.ui.hr.frame.persistence.PersitenceDelegator;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UITable;
import nc.vo.hr.combinesort.SortVO;
import nc.vo.hr.combinesort.SortconVO;
import nc.vo.hr.tools.pub.HRAggVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;

/*************************************************************************************************************
 * <br>
 * Created on 2006-1-17 13:37:44<br>
 * @author myl
 ************************************************************************************************************/
public class SortConfigDialog extends UIDialog
{
    private static final long serialVersionUID = 1L;
    
    private Pair[] fields;
    
    private HRAggVO hrAggVO;
    
    private UIButton ivjBtnAdd = null;
    
    private UIButton ivjBtnCancel = null;
    private UIButton ivjBtnClearUp = null;
    private UIButton ivjBtnDel = null;
    private UIButton ivjBtnDown = null;
    private UIButton ivjBtnOk = null;
    private UIButton ivjBtnSave = null;
    
    private UIButton ivjBtnUp = null;
    private UIComboBox ivjCmbFields = null;
    
    private UIComboBox ivjCmbOrient = null;
    
    private final IvjEventHandler ivjEventHandler = new IvjEventHandler();
    
    private JPanel ivjJDialogContentPane = null;
    
    private JScrollPane ivjJScrollPane1 = null;
    
    private String strModuleCode = "";
    
    private String strTableCode = "TableCode";
    private UITable tblSortingFields = null;
    
    private Vector vectSortingAttribute = new Vector();
    private Vector vectSortingFields = new Vector();
    
    private String orderStr = " ";
    
    private boolean allowNullSort = false;// 是否允许空排序
    
    /*********************************************************************************************************
     * <br>
     * Created on 2006-1-23 14:01:44<br>
     * @author Rocex Wang
     * @author Rocex Wang
     ********************************************************************************************************/
    private class FieldTableModel extends AbstractTableModel
    {
        /*****************************************************************************************************
         * <code>serialVersionUID</code>:
         ****************************************************************************************************/
        private static final long serialVersionUID = -7364200641439027872L;
        
        private final String[] strColumnNames =
            {ResHelper.getString("nc_hr_pub", "UPPnc_hr_pub-000254")/* "字段名称" */, ResHelper.getString("nc_hr_pub", "UPPnc_hr_pub-000255")/* "升降" */};
        
        /*****************************************************************************************************
         * Created on 2006-2-10 13:16:53<br>
         * @author Rocex Wang
         * @see javax.swing.table.TableModel#getColumnCount()
         ****************************************************************************************************/
        public int getColumnCount()
        {
            return 2;
        }
        
        /*****************************************************************************************************
         * Created on 2006-2-10 13:16:56<br>
         * @author Rocex Wang
         * @see javax.swing.table.TableModel#getColumnName(int)
         ****************************************************************************************************/
        @Override
        public String getColumnName(int columnIndex)
        {
            return strColumnNames[columnIndex];
        }
        
        /*****************************************************************************************************
         * Created on 2006-2-10 13:16:59<br>
         * @author Rocex Wang
         * @see javax.swing.table.TableModel#getRowCount()
         ****************************************************************************************************/
        public int getRowCount()
        {
            return vectSortingFields.size();
        }
        
        /*****************************************************************************************************
         * Created on 2006-1-19 11:10:15<br>
         * @author Rocex Wang
         * @see javax.swing.table.TableModel#getValueAt(int, int)
         ****************************************************************************************************/
        public Object getValueAt(int row, int column)
        {
            Attribute attribute = (Attribute) vectSortingFields.elementAt(row);
            
            if (column == 0)
            {
                return attribute.getAttribute().getName();
            }
            
            return attribute.isAscend() ? ResHelper.getString("nc_hr_pub", "UPPnc_hr_pub-000256")/* "升序" */: ResHelper.getString(
                "nc_hr_pub", "UPPnc_hr_pub-000257")/* "降序" */;
        }
        
        /*****************************************************************************************************
         * Created on 2006-2-10 13:17:03<br>
         * @author Rocex Wang
         * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
         ****************************************************************************************************/
        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex)
        {
        }
    }
    
    /*********************************************************************************************************
     * <br>
     * Created on 2006-1-18 16:15:32<br>
     * @author Rocex Wang
     * @author Rocex Wang
     ********************************************************************************************************/
    private class IvjEventHandler implements ActionListener
    {
        /*****************************************************************************************************
         * Created on 2006-1-18 14:34:17<br>
         * @author Rocex Wang
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         ****************************************************************************************************/
        public void actionPerformed(ActionEvent evt)
        {
            if (evt.getSource() == SortConfigDialog.this.getBtnCancel())
            {
                connEtoC1(evt);
            }
            else if (evt.getSource() == SortConfigDialog.this.getBtnOk())
            {
                connEtoC2(evt);
            }
            else if (evt.getSource() == SortConfigDialog.this.getBtnAdd())
            {
                connEtoC3(evt);
            }
            else if (evt.getSource() == SortConfigDialog.this.getBtnDel())
            {
                connEtoC4(evt);
            }
            else if (evt.getSource() == SortConfigDialog.this.getBtnUp())
            {
                connEtoC5(evt);
            }
            else if (evt.getSource() == SortConfigDialog.this.getBtnDown())
            {
                connEtoC6(evt);
            }
            else if (evt.getSource() == SortConfigDialog.this.getBtnSave())
            {
                connEtoC7(evt);
            }
            else if (evt.getSource() == SortConfigDialog.this.getBtnClearUp())
            {
                connEtoC8(evt);
            }
        }
    }
    
    /*********************************************************************************************************
     * SortConfigDialog 构造子注解。
     ********************************************************************************************************/
    public SortConfigDialog()
    {
        super();
        
        initialize();
    }
    
    /*********************************************************************************************************
     * SortConfigDialog 构造子注解。
     * @param container java.awt.Dialog
     ********************************************************************************************************/
    public SortConfigDialog(Container container)
    {
        super(container);
        
        initialize();
    }
    
    /*********************************************************************************************************
     * SortConfigDialog 构造子注解。
     * @param container java.awt.Dialog
     * @param isModel
     ********************************************************************************************************/
    public SortConfigDialog(Container container, boolean isModel)
    {
        super(container);
        
        initialize();
        
        this.setModal(isModel);
    }
    
    /*********************************************************************************************************
     * SortConfigDialog 构造子注解。
     * @param container java.awt.Dialog
     * @param title java.lang.String
     ********************************************************************************************************/
    public SortConfigDialog(Container container, String title)
    {
        super(container, title);
        
        initialize();
    }
    
    /*********************************************************************************************************
     * SortConfigDialog 构造子注解。
     * @param owner java.awt.Frame
     ********************************************************************************************************/
    public SortConfigDialog(Frame owner)
    {
        super(owner);
        
        initialize();
    }
    
    /*********************************************************************************************************
     * SortConfigDialog 构造子注解。
     * @param owner java.awt.Frame
     * @param isModel
     ********************************************************************************************************/
    public SortConfigDialog(Frame owner, boolean isModel)
    {
        super(owner);
        
        initialize();
        
        this.setModal(isModel);
    }
    
    /*********************************************************************************************************
     * SortConfigDialog 构造子注解。
     * @param owner java.awt.Frame
     * @param title
     ********************************************************************************************************/
    public SortConfigDialog(Frame owner, String title)
    {
        super(owner, title);
        
        initialize();
    }
    
    /**
     * SortConfigDialog 构造子注解。
     * <br>
     * Created on 2012-02-02<br>
     * @author River
     * @param owner
     * @param ivjEventHandler
     */
    public SortConfigDialog(Frame owner , IvjEventHandler ivjEventHandler ) {
    	super(owner);
    	
    	initialize();
    }
    
    /*********************************************************************************************************
     * <br>
     * Created on 2006-1-18 13:51:16<br>
     * @author Rocex Wang
     * @param actionEvent
     ********************************************************************************************************/
    public void btnAdd_ActionPerformed(ActionEvent actionEvent)
    {
        Pair field = (Pair) getCmbFields().getSelectedItem();
        String ascend = (String) getCmbOrient().getSelectedItem();
        Attribute attribute = new Attribute(field, ascend.equals(ResHelper.getString("nc_hr_pub", "UPPnc_hr_pub-000256")/* "升序" */));
        
        if (vectSortingFields.contains(attribute))
        {
            int iRowIndex1 = vectSortingFields.indexOf(attribute);
            
            getTbSortingFields().scrollRectToVisible(getTbSortingFields().getCellRect(iRowIndex1, 0, false));
            getTbSortingFields().setRowSelectionInterval(iRowIndex1, iRowIndex1);
            
            return;
        }
        
        vectSortingFields.addElement(attribute);
        ((AbstractTableModel) getTbSortingFields().getModel()).fireTableDataChanged();
        
        getTbSortingFields().setRowSelectionInterval(vectSortingFields.size() - 1, vectSortingFields.size() - 1);
    }
    
    /*********************************************************************************************************
     * <br>
     * Created on 2006-1-18 13:51:28<br>
     * @author Rocex Wang
     * @param actionEvent
     ********************************************************************************************************/
    public void btnCancel_ActionPerformed(ActionEvent actionEvent)
    {
        setSortingAttribute(null);
        
        this.closeCancel();
    }
    
    /*********************************************************************************************************
     * <br>
     * Created on 2006-1-18 14:10:54<br>
     * @author Rocex Wang
     * @param arg1
     ********************************************************************************************************/
    public void btnClearUp_ActionPerformed(ActionEvent arg1)
    {
        if (UIDialog.ID_OK != MessageDialog
            .showOkCancelDlg(this, null, ResHelper.getString("nc_hr_pub", "UPPnc_hr_pub-000258")/* "是否清空数据库中保存的排序条件？" */))
        {
            return;
        }
        
        try
        {
            if (hrAggVO != null)
            {
                PersitenceDelegator.deleteByAggVO(hrAggVO);
                
                hrAggVO = null;
            }
        }
        catch (Exception ex)
        {
            MessageDialog.showHintDlg(this, null, ex.getMessage());
            
            return;
        }
        
        vectSortingFields.removeAllElements();
        
        ((AbstractTableModel) getTbSortingFields().getModel()).fireTableDataChanged();
    }
    
    /*********************************************************************************************************
     * <br>
     * Created on 2006-1-18 13:51:39<br>
     * @author Rocex Wang
     * @param actionEvent
     ********************************************************************************************************/
    public void btnDel_ActionPerformed(ActionEvent actionEvent)
    {
        int iRowCount = vectSortingFields.size();
        int iRowIndex = getTbSortingFields().getSelectedRow();
        
        if (iRowIndex != -1 && iRowIndex < iRowCount)
        {
            vectSortingFields.removeElementAt(iRowIndex);
            
            ((AbstractTableModel) getTbSortingFields().getModel()).fireTableDataChanged();
            
            if (iRowIndex >= vectSortingFields.size() && vectSortingFields.size() > 0)
            {
                getTbSortingFields().setRowSelectionInterval(vectSortingFields.size() - 1, vectSortingFields.size() - 1);
                getTbSortingFields().scrollRectToVisible(getTbSortingFields().getCellRect(vectSortingFields.size() - 1, 0, false));
            }
            else if (iRowIndex != -1 && iRowIndex < vectSortingFields.size())
            {
                getTbSortingFields().setRowSelectionInterval(iRowIndex, iRowIndex);
                getTbSortingFields().scrollRectToVisible(getTbSortingFields().getCellRect(iRowIndex, 0, false));
            }
        }
    }
    
    /*********************************************************************************************************
     * <br>
     * Created on 2006-1-18 13:51:48<br>
     * @author Rocex Wang
     * @param actionEvent
     ********************************************************************************************************/
    public void btnDown_ActionPerformed(ActionEvent actionEvent)
    {
        int iRowIndex = getTbSortingFields().getSelectedRow();
        int rowCount = vectSortingFields.size();
        
        if (iRowIndex != -1 && iRowIndex < rowCount - 1)
        {
            Object temp = vectSortingFields.elementAt(iRowIndex);
            
            vectSortingFields.setElementAt(vectSortingFields.elementAt(iRowIndex + 1), iRowIndex);
            vectSortingFields.setElementAt(temp, iRowIndex + 1);
            
            ((AbstractTableModel) getTbSortingFields().getModel()).fireTableDataChanged();
            
            getTbSortingFields().getSelectionModel().setSelectionInterval(iRowIndex + 1, iRowIndex + 1);
            getTbSortingFields().scrollRectToVisible(getTbSortingFields().getCellRect(iRowIndex + 1, 0, false));
        }
    }
    
    /*********************************************************************************************************
     * <br>
     * Created on 2006-1-18 14:10:43<br>
     * @author Rocex Wang
     * @param arg1
     ********************************************************************************************************/
    public void btnLoad_ActionPerformed(ActionEvent arg1)
    {
        HRAggVO hrAggVO2 = doLoad();
        
        if (hrAggVO2 == null || hrAggVO2.getChildrenVO() == null)
        {
            return;
        }
        
        SortconVO sortconVOs[] = (SortconVO[]) hrAggVO2.getChildrenVO();
        
        for (int i = 0; i < sortconVOs.length; i++)
        {
            Pair field = new Pair(sortconVOs[i].getField_code(), sortconVOs[i].getField_name());
            Attribute attribute = new Attribute(field, sortconVOs[i].getAscend_flag().booleanValue());
            
            if (vectSortingFields.contains(attribute))
            {
                continue;
            }
            
            vectSortingFields.addElement(attribute);
        }
        
        ((AbstractTableModel) getTbSortingFields().getModel()).fireTableDataChanged();
    }
    
    /*********************************************************************************************************
     * <br>
     * Created on 2006-1-18 13:51:59<br>
     * @author Rocex Wang
     * @param actionEvent
     ********************************************************************************************************/
    public void btnOk_ActionPerformed(ActionEvent actionEvent)
    {
        if (vectSortingFields.isEmpty() && !allowNullSort)
        {
            MessageDialog.showHintDlg(this, null, ResHelper.getString("nc_hr_pub", "UPPnc_hr_pub-000259")/* "请输入至少一个排序字段！" */);
            
            return;
        }
        
        vectSortingAttribute = new Vector();
        
        GeneralTypeCompare compare = new GeneralTypeCompare();
        
        for (int i = 0; i < vectSortingFields.size(); i++)
        {
            Attribute attribute = (Attribute) vectSortingFields.elementAt(i);
            AttributeDescription description = new AttributeDescription(attribute.getAttribute().getCode(), compare, attribute.isAscend());
            vectSortingAttribute.addElement(description);
        }
        
        // dispose();
        this.closeOK();
    }
    
    /*********************************************************************************************************
     * <br>
     * Created on 2006-1-18 14:08:51<br>
     * @author Rocex Wang
     * @param arg1
     ********************************************************************************************************/
    public void btnSave_ActionPerformed(ActionEvent arg1)
    {
        SortVO sortVO = new SortVO();
        SortconVO sortconVOs[] = new SortconVO[vectSortingFields.size()];
        
        sortVO.setStatus(VOStatus.NEW);
        sortVO.setFunc_code(getModuleCode());
        sortVO.setGroup_code(strTableCode);
        sortVO.setPk_corp(ClientEnvironment.getInstance().getCorporation().getPrimaryKey());
        sortVO.setPk_user(ClientEnvironment.getInstance().getUser().getPrimaryKey());
        
        for (int i = 0; i < vectSortingFields.size(); i++)
        {
            Attribute attribute = (Attribute) vectSortingFields.elementAt(i);
            
            sortconVOs[i] = new SortconVO();
            
            sortconVOs[i].setStatus(VOStatus.NEW);
            sortconVOs[i].setAscend_flag(new UFBoolean(attribute.isAscend()));
            sortconVOs[i].setField_code(attribute.getAttribute().getCode());
            sortconVOs[i].setField_name(attribute.getAttribute().getName());
            sortconVOs[i].setField_seq(new Integer(i));
        }
        
        HRAggVO hrAggVO2 = createHrAggVO(null);
        
        hrAggVO2.setParentVO(sortVO);
        hrAggVO2.setChildrenVO(sortconVOs);
        
        String strCondition = " func_code='" + getModuleCode() + "'";
        strCondition += " and group_code='" + strTableCode + "'";
        strCondition += " and pk_corp='" + ClientEnvironment.getInstance().getCorporation().getPrimaryKey() + "'";
        strCondition += " and pk_user='" + ClientEnvironment.getInstance().getUser().getPrimaryKey() + "'";
        
        try
        {
            HRAggVO hrAggDelVO = doLoad();
            
            if (hrAggDelVO != null)
            {
                PersitenceDelegator.deleteByAggVO(hrAggDelVO);
            }
            
            if (hrAggVO2.getChildrenVO() != null && hrAggVO2.getChildrenVO().length > 0)
            {
                PersitenceDelegator.saveAggVO(hrAggVO2);
            }
        }
        catch (Exception ex)
        {
            MessageDialog.showHintDlg(this, null, ex.getMessage());
            
            return;
        }
    }
    
    /*********************************************************************************************************
     * <br>
     * Created on 2006-1-18 13:52:06<br>
     * @author Rocex Wang
     * @param actionEvent
     ********************************************************************************************************/
    public void btnUp_ActionPerformed(ActionEvent actionEvent)
    {
        int iRowCount = vectSortingFields.size();
        int iSelectedRow = getTbSortingFields().getSelectedRow();
        
        if (iSelectedRow > 0 && iSelectedRow < iRowCount)
        {
            Object tempObj = vectSortingFields.elementAt(iSelectedRow);
            
            vectSortingFields.setElementAt(vectSortingFields.elementAt(iSelectedRow - 1), iSelectedRow);
            vectSortingFields.setElementAt(tempObj, iSelectedRow - 1);
            
            ((AbstractTableModel) getTbSortingFields().getModel()).fireTableDataChanged();
            getTbSortingFields().getSelectionModel().setSelectionInterval(iSelectedRow - 1, iSelectedRow - 1);
            getTbSortingFields().scrollRectToVisible(getTbSortingFields().getCellRect(iSelectedRow - 1, 0, false));
        }
    }
    
    /*********************************************************************************************************
     * connEtoC1: (BtnCancel.action.actionPerformed(java.awt.event.ActionEvent) -->
     * SortConfigDialog.btnCancel_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
     * @param arg1 java.awt.event.ActionEvent
     ********************************************************************************************************/
    private void connEtoC1(ActionEvent arg1)
    {
        try
        {
            this.btnCancel_ActionPerformed(arg1);
        }
        catch (Throwable ivjExc)
        {
            handleException(ivjExc);
        }
    }
    
    /*********************************************************************************************************
     * connEtoC2: (BtnOk.action.actionPerformed(java.awt.event.ActionEvent) -->
     * SortConfigDialog.btnOk_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
     * @param arg1 java.awt.event.ActionEvent
     ********************************************************************************************************/
    private void connEtoC2(ActionEvent arg1)
    {
        try
        {
            this.btnOk_ActionPerformed(arg1);
        }
        catch (Throwable ivjExc)
        {
            handleException(ivjExc);
        }
    }
    
    /*********************************************************************************************************
     * connEtoC3: (BtnAdd.action.actionPerformed(java.awt.event.ActionEvent) -->
     * SortConfigDialog.btnAdd_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
     * @param arg1 java.awt.event.ActionEvent
     ********************************************************************************************************/
    private void connEtoC3(ActionEvent arg1)
    {
        try
        {
            this.btnAdd_ActionPerformed(arg1);
        }
        catch (Throwable ivjExc)
        {
            handleException(ivjExc);
        }
    }
    
    /*********************************************************************************************************
     * connEtoC4: (BtnDel.action.actionPerformed(java.awt.event.ActionEvent) -->
     * SortConfigDialog.btnDel_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
     * @param arg1 java.awt.event.ActionEvent
     ********************************************************************************************************/
    private void connEtoC4(ActionEvent arg1)
    {
        try
        {
            this.btnDel_ActionPerformed(arg1);
        }
        catch (Throwable ivjExc)
        {
            handleException(ivjExc);
        }
    }
    
    /*********************************************************************************************************
     * connEtoC5: (BtnUp.action.actionPerformed(java.awt.event.ActionEvent) -->
     * SortConfigDialog.btnUp_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
     * @param arg1 java.awt.event.ActionEvent
     ********************************************************************************************************/
    private void connEtoC5(ActionEvent arg1)
    {
        try
        {
            this.btnUp_ActionPerformed(arg1);
        }
        catch (Throwable ivjExc)
        {
            handleException(ivjExc);
        }
    }
    
    /*********************************************************************************************************
     * connEtoC6: (BtnDown.action.actionPerformed(java.awt.event.ActionEvent) -->
     * SortConfigDialog.btnDown_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
     * @param arg1 java.awt.event.ActionEvent
     ********************************************************************************************************/
    private void connEtoC6(ActionEvent arg1)
    {
        try
        {
            this.btnDown_ActionPerformed(arg1);
        }
        catch (Throwable ivjExc)
        {
            handleException(ivjExc);
        }
    }
    
    /*********************************************************************************************************
     * <br>
     * Created on 2006-1-18 14:07:54<br>
     * @author Rocex Wang
     * @param arg1
     ********************************************************************************************************/
    private void connEtoC7(ActionEvent arg1)
    {
        try
        {
            this.btnSave_ActionPerformed(arg1);
        }
        catch (Throwable ivjExc)
        {
            handleException(ivjExc);
        }
    }
    
    /*********************************************************************************************************
     * <br>
     * Created on 2006-1-18 14:09:51<br>
     * @author Rocex Wang
     * @param arg1
     ********************************************************************************************************/
    private void connEtoC8(ActionEvent arg1)
    {
        try
        {
            this.btnClearUp_ActionPerformed(arg1);
        }
        catch (Throwable ivjExc)
        {
            handleException(ivjExc);
        }
    }
    
    /*********************************************************************************************************
     * <br>
     * Created on 2006-11-27 12:19:21<br>
     * @author Rocex Wang
     * @param hrAggVO2
     * @return HRAggVO
     ********************************************************************************************************/
    private HRAggVO createHrAggVO(HRAggVO hrAggVO2)
    {
        if (hrAggVO2 == null)
        {
            hrAggVO2 = new HRAggVO();
        }
        
        hrAggVO2.setTableCodes(new String[]{strTableCode});
        hrAggVO2.setTableNames(new String[]{new SortconVO().getTableName()});
        
        return hrAggVO2;
    }
    
    /*********************************************************************************************************
     * <br>
     * Created on 2006-1-25 15:30:25<br>
     * @author Rocex Wang
     * @return HRAggVO
     ********************************************************************************************************/
    private HRAggVO doLoad()
    {
        SortVO sortVOs[] = null;
        SortconVO sortconVOs[] = null;
        
        try
        {
            // 支持默认排序
            // String strCondition =
            // " func_code='" + getModuleCode() + "'" + " and group_code='" + strTableCode + "' and pk_corp='"
            // + ClientEnvironment.getInstance().getCorporation().getPrimaryKey() + "' and pk_user='"
            // + ClientEnvironment.getInstance().getUser().getPrimaryKey() + "'";
            String strCondition =
                " func_code='" + getModuleCode() + "'" + " and group_code='" + strTableCode + "' and ((pk_corp='"
                    + ClientEnvironment.getInstance().getCorporation().getPrimaryKey() + "' and pk_user='"
                    + ClientEnvironment.getInstance().getUser().getPrimaryKey() + "') or pk_corp ='@@@@') order by pk_corp";
            sortVOs = (SortVO[]) PersitenceDelegator.retrieveByClause(SortVO.class, strCondition);
            
            if (sortVOs == null || sortVOs.length == 0)
            {
                hrAggVO = null;
                return null;
            }
            
            strCondition = "pk_hr_sort='" + sortVOs[0].getPrimaryKey() + "'";
            
            sortconVOs = (SortconVO[]) PersitenceDelegator.retrieveByClause(SortconVO.class, strCondition);
        }
        catch (Exception ex)
        {
            MessageDialog.showHintDlg(this, null, ex.getMessage());
            return null;
        }
        
        hrAggVO = createHrAggVO(null);
        hrAggVO.setParentVO(sortVOs[0]);
        hrAggVO.setChildrenVO(sortconVOs);
        
        return hrAggVO;
    }
    
    /*********************************************************************************************************
     * 返回 BtnAdd 特性值。
     * @return nc.ui.pub.beans.UIButton
     ********************************************************************************************************/
    private UIButton getBtnAdd()
    {
        if (ivjBtnAdd == null)
        {
            try
            {
                ivjBtnAdd = new UIButton();
                ivjBtnAdd.setName("BtnAdd");
                ivjBtnAdd.setFont(new java.awt.Font("dialog", 0, 12));
                ivjBtnAdd.setText(ResHelper.getString("nc_hr_pub", "UPPnc_hr_pub-000260")/* "增加" */);
                ivjBtnAdd.setBounds(240, 45, 70, 20);
            }
            catch (Throwable ivjExc)
            {
                handleException(ivjExc);
            }
        }
        return ivjBtnAdd;
    }
    
    /*********************************************************************************************************
     * 返回 BtnCancel 特性值。
     * @return nc.ui.pub.beans.UIButton
     ********************************************************************************************************/
    private UIButton getBtnCancel()
    {
        if (ivjBtnCancel == null)
        {
            try
            {
                ivjBtnCancel = new UIButton();
                ivjBtnCancel.setName("BtnCancel");
                ivjBtnCancel.setFont(new java.awt.Font("dialog", 0, 12));
                ivjBtnCancel.setText(ResHelper.getString("nc_hr_pub", "UPPnc_hr_pub-000261")/* "取消" */);
                ivjBtnCancel.setBounds(137, 223, 83, 22);
            }
            catch (Throwable ivjExc)
            {
                handleException(ivjExc);
            }
        }
        return ivjBtnCancel;
    }
    
    /*********************************************************************************************************
     * <br>
     * Created on 2006-1-18 14:01:49<br>
     * @author Rocex Wang
     * @return Returns the UIButton ivjBtnClearUp.
     ********************************************************************************************************/
    private UIButton getBtnClearUp()
    {
        if (ivjBtnClearUp == null)
        {
            try
            {
                ivjBtnClearUp = new UIButton();
                ivjBtnClearUp.setName("BtnClearUp");
                ivjBtnClearUp.setFont(new Font("dialog", 0, 12));
                ivjBtnClearUp.setText(ResHelper.getString("nc_hr_pub", "UPPnc_hr_pub-000262")/* "清空" */);
                ivjBtnClearUp.setToolTipText(ResHelper.getString("nc_hr_pub", "UPPnc_hr_pub-000263")/* "清空数据库中保存的排序条件" */);
                ivjBtnClearUp.setBounds(240, 165, 70, 20);
            }
            catch (Throwable ivjExc)
            {
                handleException(ivjExc);
            }
        }
        
        return ivjBtnClearUp;
    }
    
    /*********************************************************************************************************
     * 返回 BtnDel 特性值。
     * @return nc.ui.pub.beans.UIButton
     ********************************************************************************************************/
    private UIButton getBtnDel()
    {
        if (ivjBtnDel == null)
        {
            try
            {
                ivjBtnDel = new UIButton();
                ivjBtnDel.setName("BtnDel");
                ivjBtnDel.setFont(new Font("dialog", 0, 12));
                ivjBtnDel.setText(ResHelper.getString("nc_hr_pub", "UPPnc_hr_pub-000264")/* "删除" */);
                ivjBtnDel.setBounds(240, 75, 70, 20);
            }
            catch (Throwable ivjExc)
            {
                handleException(ivjExc);
            }
        }
        return ivjBtnDel;
    }
    
    /*********************************************************************************************************
     * 返回 BtnDown 特性值。
     * @return nc.ui.pub.beans.UIButton
     ********************************************************************************************************/
    private UIButton getBtnDown()
    {
        if (ivjBtnDown == null)
        {
            try
            {
                ivjBtnDown = new UIButton();
                ivjBtnDown.setName("BtnDown");
                ivjBtnDown.setFont(new java.awt.Font("dialog", 0, 12));
                ivjBtnDown.setText(ResHelper.getString("nc_hr_pub", "UPPnc_hr_pub-000265")/* "下移" */);
                ivjBtnDown.setBounds(240, 135, 70, 20);
            }
            catch (Throwable ivjExc)
            {
                handleException(ivjExc);
            }
        }
        return ivjBtnDown;
    }
    
    /*********************************************************************************************************
     * 返回 BtnOk 特性值。
     * @return nc.ui.pub.beans.UIButton
     ********************************************************************************************************/
    private UIButton getBtnOk()
    {
        if (ivjBtnOk == null)
        {
            try
            {
                ivjBtnOk = new UIButton();
                ivjBtnOk.setName("BtnOk");
                ivjBtnOk.setFont(new java.awt.Font("dialog", 0, 12));
                ivjBtnOk.setText(ResHelper.getString("nc_hr_pub", "UPPnc_hr_pub-000266")/* "确定" */);
                ivjBtnOk.setBounds(32, 223, 83, 22);
            }
            catch (Throwable ivjExc)
            {
                handleException(ivjExc);
            }
        }
        return ivjBtnOk;
    }
    
    /*********************************************************************************************************
     * <br>
     * Created on 2006-1-18 14:01:49<br>
     * @author Rocex Wang
     * @return Returns the UIButton ivjBtnSave.
     ********************************************************************************************************/
    private UIButton getBtnSave()
    {
        if (ivjBtnSave == null)
        {
            try
            {
                ivjBtnSave = new UIButton();
                ivjBtnSave.setName("BtnSave");
                ivjBtnSave.setFont(new Font("dialog", 0, 12));
                ivjBtnSave.setText(ResHelper.getString("nc_hr_pub", "UPPnc_hr_pub-000267")/* "保存" */);
                ivjBtnSave.setBounds(240, 195, 70, 20);
            }
            catch (Throwable ivjExc)
            {
                handleException(ivjExc);
            }
        }
        
        return ivjBtnSave;
    }
    
    /*********************************************************************************************************
     * 返回 BtnUp 特性值。
     * @return nc.ui.pub.beans.UIButton
     ********************************************************************************************************/
    private UIButton getBtnUp()
    {
        if (ivjBtnUp == null)
        {
            try
            {
                ivjBtnUp = new UIButton();
                ivjBtnUp.setName("BtnUp");
                ivjBtnUp.setFont(new java.awt.Font("dialog", 0, 12));
                ivjBtnUp.setText(ResHelper.getString("nc_hr_pub", "UPPnc_hr_pub-000268")/* "上移" */);
                ivjBtnUp.setBounds(240, 105, 70, 20);
            }
            catch (Throwable ivjExc)
            {
                handleException(ivjExc);
            }
        }
        return ivjBtnUp;
    }
    
    /*********************************************************************************************************
     * 返回 CmbFields1 特性值。
     * @return nc.ui.pub.beans.UIComboBox
     ********************************************************************************************************/
    private UIComboBox getCmbFields()
    {
        if (ivjCmbFields == null)
        {
            try
            {
                ivjCmbFields = new UIComboBox();
                ivjCmbFields.setName("CmbFields");
                ivjCmbFields.setFont(new java.awt.Font("dialog", 0, 12));
                ivjCmbFields.setBounds(18, 14, 206, 22);
                ivjCmbFields.setTranslate(true);
            }
            catch (Throwable ivjExc)
            {
                handleException(ivjExc);
            }
        }
        return ivjCmbFields;
    }
    
    /*********************************************************************************************************
     * 返回 CmbOrient1 特性值。
     * @return nc.ui.pub.beans.UIComboBox
     ********************************************************************************************************/
    private UIComboBox getCmbOrient()
    {
        if (ivjCmbOrient == null)
        {
            try
            {
                ivjCmbOrient = new UIComboBox();
                ivjCmbOrient.setName("CmbOrient");
                ivjCmbOrient.setFont(new java.awt.Font("dialog", 0, 12));
                ivjCmbOrient.setBounds(240, 14, 67, 22);
                ivjCmbOrient.setTranslate(true);
            }
            catch (Throwable ivjExc)
            {
                handleException(ivjExc);
            }
        }
        return ivjCmbOrient;
    }
    
    /*********************************************************************************************************
     * 创建日期：(2004-5-18 11:23:12)
     * @return nc.ui.hi.hi_301.Pair
     ********************************************************************************************************/
    public Pair[] getFields()
    {
        return fields;
    }
    
    /*********************************************************************************************************
     * 返回 JDialogContentPane 特性值。
     * @return javax.swing.JPanel
     ********************************************************************************************************/
    private JPanel getJDialogContentPane()
    {
        if (ivjJDialogContentPane == null)
        {
            try
            {
                ivjJDialogContentPane = new JPanel();
                ivjJDialogContentPane.setName("JDialogContentPane");
                ivjJDialogContentPane.setLayout(null);
                getJDialogContentPane().add(getCmbFields(), getCmbFields().getName());
                getJDialogContentPane().add(getCmbOrient(), getCmbOrient().getName());
                getJDialogContentPane().add(getBtnOk(), getBtnOk().getName());
                getJDialogContentPane().add(getBtnCancel(), getBtnCancel().getName());
                getJDialogContentPane().add(getBtnAdd(), getBtnAdd().getName());
                getJDialogContentPane().add(getBtnDel(), getBtnDel().getName());
                getJDialogContentPane().add(getBtnUp(), getBtnUp().getName());
                getJDialogContentPane().add(getBtnDown(), getBtnDown().getName());
                getJDialogContentPane().add(getBtnSave(), getBtnSave().getName());
                getJDialogContentPane().add(getBtnClearUp(), getBtnClearUp().getName());
                getJDialogContentPane().add(getJScrollPane1(), getJScrollPane1().getName());
            }
            catch (Throwable ivjExc)
            {
                handleException(ivjExc);
            }
        }
        
        return ivjJDialogContentPane;
    }
    
    /*********************************************************************************************************
     * <br>
     * Created on 2006-1-18 13:52:20<br>
     * @author Rocex Wang
     * @return JScrollPane
     ********************************************************************************************************/
    private JScrollPane getJScrollPane1()
    {
        if (ivjJScrollPane1 == null)
        {
            try
            {
                ivjJScrollPane1 = new javax.swing.JScrollPane();
                ivjJScrollPane1.setName("JScrollPane1");
                ivjJScrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
                ivjJScrollPane1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                ivjJScrollPane1.setBounds(18, 45, 207, 170);
                getJScrollPane1().setViewportView(getTbSortingFields());
            }
            catch (Throwable ivjExc)
            {
                handleException(ivjExc);
            }
        }
        return ivjJScrollPane1;
    }
    
    /*********************************************************************************************************
     * <br>
     * Created on 2006-2-14 14:33:46<br>
     * @author Rocex Wang
     * @return Returns the String funcCode.
     ********************************************************************************************************/
    public String getModuleCode()
    {
        return strModuleCode;
    }
    
    /***************************************************************************
     * <br>
     * Created on 2009-7-27 14:36:40<br>
     * @return String
     * @author Rocex Wang
     ***************************************************************************/
    public String getOrderStr()
    {
        orderStr = "  ";
        if (vectSortingFields != null && vectSortingFields.size() > 0)
        {
            orderStr += "order by ";
        }
        int i = 0;
        for (i = 0; i < vectSortingFields.size(); i++)
        {
            Attribute attribute = (Attribute) vectSortingFields.elementAt(i);
            
            orderStr += attribute.getAttribute().getCode();
            orderStr += attribute.isAscend() ? " asc, " : " desc, ";
        }
        if (i > 0)
        {
            orderStr = orderStr.substring(0, orderStr.length() - 2);
        }
        
        return orderStr;
    }
    
    /*********************************************************************************************************
     * 创建日期：(2004-5-18 17:53:41)
     * @return java.util.Vector
     ********************************************************************************************************/
    public Vector getSortingAttribute()
    {
        return vectSortingAttribute;
    }
    
    /*********************************************************************************************************
     * 创建日期：(2004-5-18 11:24:25)
     * @return java.util.Vector
     ********************************************************************************************************/
    public Vector getSortingFields()
    {
        return vectSortingFields;
    }
    
    /*********************************************************************************************************
     * <br>
     * Created on 2006-1-23 16:04:28<br>
     * @author Rocex Wang
     * @return Returns the String tableCode.
     ********************************************************************************************************/
    public String getTableCode()
    {
        return strTableCode;
    }
    
    /*********************************************************************************************************
     * 返回 TbSortingFields 特性值。
     * @return nc.ui.pub.beans.UITable
     ********************************************************************************************************/
    private UITable getTbSortingFields()
    {
        if (tblSortingFields == null)
        {
            try
            {
                tblSortingFields = new UITable();
                tblSortingFields.setName("TbSortingFields");
                tblSortingFields.setPreferredSize(new Dimension(100, 170));
                tblSortingFields.setBounds(new Rectangle(0, 0, 204, 170));
                tblSortingFields.setShowGrid(true);
                getJScrollPane1().setColumnHeaderView(tblSortingFields.getTableHeader());
                getJScrollPane1().getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
                
                tblSortingFields.addMouseListener(new MouseAdapter()
                {
                    @Override
                    public void mouseClicked(MouseEvent evt)
                    {
                        if (MouseEvent.BUTTON1 != evt.getButton() || evt.getClickCount() != 2)
                        {
                            return;
                        }
                        
                        int iSelectedIndex = getTbSortingFields().getSelectedRow();
                        
                        Attribute attribute = (Attribute) vectSortingFields.get(iSelectedIndex);
                        
                        attribute.setAscend(!attribute.isAscend());
                        
                        vectSortingFields.set(iSelectedIndex, attribute);
                        ((AbstractTableModel) getTbSortingFields().getModel()).fireTableDataChanged();
                        
                        getTbSortingFields().setRowSelectionInterval(iSelectedIndex, iSelectedIndex);
                    }
                });
            }
            catch (Throwable ivjExc)
            {
                handleException(ivjExc);
            }
        }
        
        return tblSortingFields;
    }
    
    /*********************************************************************************************************
     * 每当部件抛出异常时被调用
     * @param exception java.lang.Throwable
     ********************************************************************************************************/
    private void handleException(Throwable exception)
    {
        System.out.println("--------- 未捕捉到的异常 ---------");
        exception.printStackTrace(System.out);
    }
    
    /*********************************************************************************************************
     * 初始化连接
     * @exception java.lang.Exception 异常说明。
     ********************************************************************************************************/
    private void initConnections() throws Exception
    {
        getBtnUp().addActionListener(ivjEventHandler);
        getBtnOk().addActionListener(ivjEventHandler);
        getBtnAdd().addActionListener(ivjEventHandler);
        getBtnDel().addActionListener(ivjEventHandler);
        getBtnDown().addActionListener(ivjEventHandler);
        getBtnSave().addActionListener(ivjEventHandler);
        getBtnCancel().addActionListener(ivjEventHandler);
        getBtnClearUp().addActionListener(ivjEventHandler);
    }
    
    /*********************************************************************************************************
     * 初始化类。
     ********************************************************************************************************/
    private void initialize()
    {
        try
        {
            setSize(323, 288);
            setTitle(ResHelper.getString("nc_hr_pub", "UPPnc_hr_pub-000269")/* "排序字段" */);
            setName("SortConfigDialog");
            setContentPane(getJDialogContentPane());
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            
            initConnections();
            
            setResizable(false);
        }
        catch (Throwable ivjExc)
        {
            handleException(ivjExc);
        }
        
        getTbSortingFields().setModel(new FieldTableModel());
        getTbSortingFields().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        getCmbOrient().addItem(ResHelper.getString("nc_hr_pub", "UPPnc_hr_pub-000256")/* "升序" */);
        getCmbOrient().addItem(ResHelper.getString("nc_hr_pub", "UPPnc_hr_pub-000257")/* "降序" */);
    }
    
    /**
     * @param allowNullSort the allowNullSort to set
     */
    public void setAllowNullSort(boolean allowNullSort)
    {
        this.allowNullSort = allowNullSort;
    }
    
    /*********************************************************************************************************
     * <br>
     * Created on 2006-1-18 13:56:00<br>
     * @author Rocex Wang
     * @param combo
     * @param items
     ********************************************************************************************************/
    private void setComboItems(UIComboBox combo, Pair[] items)
    {
        combo.removeAllItems();
        
        for (int i = 0; i < items.length; i++)
        {
            combo.addItem(items[i]);
        }
    }
    
    /*********************************************************************************************************
     * 创建日期：(2004-5-18 11:23:12)
     * @param newFields nc.ui.hi.hi_301.Pair
     ********************************************************************************************************/
    public void setFields(Pair[] newFields)
    {
        fields = newFields;
        
        setComboItems(getCmbFields(), newFields);
    }
    
    /*********************************************************************************************************
     * <br>
     * Created on 2006-2-14 14:33:46<br>
     * @author Rocex Wang
     * @param funcCode The String funcCode to set.
     ********************************************************************************************************/
    public void setModuleCode(String funcCode)
    {
        strModuleCode = funcCode;
    }
    
    /*********************************************************************************************************
     * 创建日期：(2004-5-18 17:53:41)
     * @param newSortingAttribute java.util.Vector
     ********************************************************************************************************/
    public void setSortingAttribute(Vector newSortingAttribute)
    {
        vectSortingAttribute = newSortingAttribute;
    }
    
    /*********************************************************************************************************
     * 创建日期：(2004-5-18 11:24:25)
     * @param newSortingFields java.util.Vector
     ********************************************************************************************************/
    public void setSortingFields(Vector newSortingFields)
    {
        vectSortingFields = newSortingFields;
    }
    
    /*********************************************************************************************************
     * <br>
     * Created on 2006-1-23 16:04:28<br>
     * @author Rocex Wang
     * @param tableCode The String tableCode to set.
     ********************************************************************************************************/
    public void setTableCode(String tableCode)
    {
        strTableCode = tableCode;
    }
}
