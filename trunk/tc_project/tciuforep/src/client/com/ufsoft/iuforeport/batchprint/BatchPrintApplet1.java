package com.ufsoft.iuforeport.batchprint;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FilePermission;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import javax.print.DocFlavor;
import javax.print.PrintException;
import javax.print.ServiceUIFactory;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.CopiesSupported;
import javax.print.attribute.standard.Destination;
import javax.print.attribute.standard.PageRanges;
import javax.print.attribute.standard.PrinterInfo;
import javax.print.attribute.standard.PrinterIsAcceptingJobs;
import javax.print.attribute.standard.PrinterMakeAndModel;
import javax.print.attribute.standard.SheetCollate;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.NumberFormatter;

import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.iuforeport.rep.RepToolAction;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIRadioButton;
import nc.util.iufo.resmng.ResMngToolKit;
import nc.vo.iuforeport.rep.ReportDirVO;
import netscape.javascript.JSException;
import netscape.javascript.JSObject;
import sun.print.SunPageSelection;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.web.util.WebGlobalValue;
import com.ufsoft.iufo.check.ui.CheckResultBO_Client;
import com.ufsoft.iufo.check.vo.CheckResultVO;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.pluginregister.BatchPrintEditPluginRegister;
import com.ufsoft.iuforeport.tableinput.applet.IPrintParam;
import com.ufsoft.iuforeport.tableinput.applet.ITableInputAppletParam;
import com.ufsoft.iuforeport.tableinput.applet.PrintParam;
import com.ufsoft.iuforeport.tableinput.applet.RepDataParam;
import com.ufsoft.iuforeport.tableinput.applet.TableInputTransObj;
import com.ufsoft.report.ReportStyle;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.applet.UfoApplet;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.UFOTable;
import com.ufsoft.table.print.multidoc.win32.IUFOMultiDocPrintJob;
import com.ufsoft.table.print.multidoc.win32.IUFOPrintService;
import com.ufsoft.table.print.multidoc.win32.IUFOPrintServiceLookup;

/**
 * 新的打印
 * @author guogang
 */
public class BatchPrintApplet1 extends UfoApplet implements ActionListener {

	private Insets panelInsets = new Insets(6, 6, 6, 6);
	private Insets compInsets = new Insets(3, 6, 3, 6);
	private ResourceBundle uimessage;
	private boolean isAWT;
	
	/** IUFO报表的打印程序仅支持PAGEABLE*/
	private DocFlavor flavor=DocFlavor.SERVICE_FORMATTED.PAGEABLE;
	/** 获取的所有打印服务*/
    private IUFOPrintService[] services;
    /** 默认的打印服务序号*/
    private int defaultServiceIndex;
	/** 当前的打印服务*/
	private IUFOPrintService psCurrent;
	/** 设置后保存的当前的打印设置*/
    private HashPrintRequestAttributeSet asCurrent;
    
    private GeneralPanel pnlGeneral;
    /** 打印的多文档*/
	private IUFOMultiDoc printDoc;
	/** 当前打印的文档*/
	private IUFODoc doc;
	/** 当前文档对应的report*/
	private JFrame frame;
	
	boolean hasDataModel=false;
	private JButton btnCancel;
    private JButton btnApprove;
    private JButton btnReset;

    /**
     * 打印applet初始化，主要包括获取本地的打印服务,获取打印数据,初始化打印面板
     * @i18n uiuforep00017=获取本地打印服务出错，请安装打印服务！
     * @i18n uiuforep00018=本地没有打印服务，请安装打印服务！
     */
	public void init() {
		
		initResource();
		defaultServiceIndex = 0;
		
		IUFOPrintServiceLookup lookup=new IUFOPrintServiceLookup();
		try{
		  services=lookup.getMultiDocPrintServices(new DocFlavor[]{flavor}, null);
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, MultiLang.getString("uiuforep00017"));
			System.exit(0);
		}
		if(services==null){
			JOptionPane.showMessageDialog(null, MultiLang.getString("uiuforep00018"));
			return;
		}
		asCurrent=new HashPrintRequestAttributeSet();
		if(services!=null&&services.length>0){
			psCurrent=services[defaultServiceIndex];
		}
		
		initPrintData();
		
		initUI();
		
	}

	/**
	 * 获取所有的报表PK
	 * @return
	 */
	private String[] getReportPKs() {
		String select = getParameter("SELECTED_REPORTPKS");
		if (select == null || select.length() < 1) {
			return new String[0];
		}
		String[] ids = select.split(WebGlobalValue.FLAG_SPLIT);//关联脚本iufotable.js
		return ids;
	}

	/**
	 * 初始化applet的UI
	 * @i18n uiuforep00019=调整打印设置
	 */
	public void initUI() {
		getContentPane().setLayout(new BorderLayout());
		pnlGeneral=new GeneralPanel();
		getContentPane().add(pnlGeneral, BorderLayout.CENTER);
		updatePanels();
		JPanel jpanel = new JPanel(new FlowLayout(4));
		if(printDoc!=null&&!printDoc.isMulti()){
			btnReset= new UIButton(MultiLang.getString("uiuforep00019"));
			btnReset.setSize(new Dimension(100, 20));
			btnReset.setPreferredSize(new Dimension(100, 20));
			btnReset.addActionListener(this);
			jpanel.add(btnReset);
		}
        btnApprove = createExitButton("button.print", this);
        jpanel.add(btnApprove);
        getRootPane().setDefaultButton(btnApprove);
        btnCancel = createExitButton("button.cancel", this);
        jpanel.add(btnCancel);
        getContentPane().add(jpanel, "South");
	}
	
	private void initPrintData(){
		List<TableInputTransObj> vTransObj=new ArrayList<TableInputTransObj>();
		
		String[] repPKs = getReportPKs();
		String strAccSchemePK=getParameter(ITableInputAppletParam.PARAM_ACCSCHEME);
		if (repPKs==null || repPKs.length<=0){
			vTransObj.add(geneTransObj());
		}else{
			for (int i=0;i<repPKs.length;i++){
				String[] strDetailPKs=splitPrintDataPKs(repPKs[i]);
				if (strDetailPKs==null || strDetailPKs.length<=0)
					continue;
				
				for (int j=0;j<strDetailPKs.length;j++){
					TableInputTransObj transObj=geneTransObj();
					if(transObj.getRepDataParam()==null||!transObj.getRepDataParam().isAnaRep()){
						String[] strVals=strDetailPKs[j].split("@");
						transObj.getPrintParam().setAloneID(strVals[0]);
						transObj.getPrintParam().setRepID(strVals[1]);
						transObj.getPrintParam().setAccSchemePK(strAccSchemePK);
					}else{
						transObj.getPrintParam().setRepID(ResMngToolKit.getVOIDByTreeObjectID(strDetailPKs[j]));
					}				
					vTransObj.add(transObj);
				}
			}
		}
		printDoc=initDoc(vTransObj);
		
		if(!printDoc.isMulti()){
			try {
				doc= printDoc.getEditableDoc();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				AppDebug.debug(e);
				JOptionPane.showMessageDialog(this,e.getMessage());
				return;
			}
		}
	}
	
	
	private String[] splitPrintDataPKs(String strPK){
		String[] strVals=strPK.split("@");
		if (strVals==null || strVals.length<3 || "repid".equalsIgnoreCase(strVals[1])==false)
			return new String[]{strPK};
		
		String strTaskPK=getParameter(IUfoContextKey.TASK_PK);
		try{
			String[] strTaskRepPKs=IUFOUICacheManager.getSingleton().getTaskCache().getReportIdsByTaskId(strTaskPK);
			CheckResultVO[] results=CheckResultBO_Client.loadRepCheckResults(false,CheckResultVO.CHECK_ALLSTATE,new String[]{strVals[0]},strTaskRepPKs, null);
			Set<String> setRepPK=new HashSet<String>();
			if (results==null || results.length<=0)
				return null;
			
			for (int i=0;i<results.length;i++){
				if (results[i]!=null && results[i].getRepId()!=null)
					setRepPK.add(results[i].getRepId());
			}
			if (setRepPK.size()<=0)
				return null;
			
			List<String> vRetPK=new ArrayList<String>();
			for (int i=0;i<strTaskRepPKs.length;i++){
				if (setRepPK.contains(strTaskRepPKs[i]))
					vRetPK.add(strVals[0]+"@"+strTaskRepPKs[i]+"@0");
			}
			return vRetPK.toArray(new String[0]);
		}catch(Exception e){
			AppDebug.debug(e);
			return null;
		}
	}
	
	private TableInputTransObj geneTransObj(){
		TableInputTransObj transObj=new TableInputTransObj();
        IPrintParam oPrintParam = new PrintParam();
        String strNType = getParameter(ITableInputAppletParam.PARAM_PRINT_NTYPE);
        oPrintParam.setNType(strNType);
        String strSessionID = getParameter(ITableInputAppletParam.PARAM_PRINT_SESSIONID);
        oPrintParam.setSessionID(strSessionID);
        
        oPrintParam.setAloneID(getParameter(ITableInputAppletParam.PARAM_PRINT_ALONE_ID));
        oPrintParam.setRepID(getParameter(ITableInputAppletParam.PARAM_PRINT_REP_ID));
        oPrintParam.setTaskID(getParameter(ITableInputAppletParam.PARAM_PRINT_TASK_ID));
        oPrintParam.setTotalID(getParameter(ITableInputAppletParam.PARAM_PRINT_TOTAL_ID));
        oPrintParam.setUnitID(getParameter(ITableInputAppletParam.PARAM_PRINT_UNIT_ID));
        oPrintParam.setNeedCutZero("true".equalsIgnoreCase(getParameter(ITableInputAppletParam.PARAM_PRINT_NEEDCUTZERO)));
        oPrintParam.setFrom(getParameter(ITableInputAppletParam.PARAM_PRINT_FROM));
        oPrintParam.setOrgPK(getParameter(RepToolAction.PARAMETER_ORGPK));
        oPrintParam.setAccSchemePK(getParameter(ITableInputAppletParam.PARAM_ACCSCHEME));
        
        String strRepType = getParameter(ITableInputAppletParam.PARAM_REP_TYPE);
        if(strRepType != null && strRepType.trim().length() > 0 && strRepType.equals(String.valueOf(ReportDirVO.REPORT_DIR_TYPE_BI))){
        	RepDataParam dataParam = new RepDataParam();
        	dataParam.setAnaRep(true);
        	transObj.setRepDataParam(dataParam);
        }
        //＃设置
        transObj.setPrintParam(oPrintParam);
        transObj.setType(TableInputTransObj.TYPE_PRINT);

	    String strCurLoginDate = getParameter("CurrentDate");
	    transObj.setCurLoginDate(strCurLoginDate);
	    transObj.setLoginUnit(getParameter("OPER_UNIT_CODE"));
	    
		//用户选择的语种编码
		String strLangCode = getParameter(ITableInputAppletParam.PARAM_LANGCODE);
		transObj.setLangCode(strLangCode);
		
		return transObj;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnApprove) {
			print();
		}
		if (e.getSource() == btnCancel) {
			close();
			
		}
		if (e.getSource() == btnReset) {
			try {
					if (getUfoReport() == null&&!hasDataModel) {
						
						if (doc.getPrintData() != null
								&& doc.getPrintData() instanceof UFOTable) {
							setUfoReport(initReport(doc.getPrintData()));
							hasDataModel = true;
						}
						frame= new JFrame();
						frame.setSize(600, 500);
						frame.setContentPane(getUfoReport());
					}
					if(frame!=null){
					  frame.setVisible(true);
					  frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
					  frame.toFront();
					}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				AppDebug.debug(e1);
				JOptionPane.showMessageDialog(this,e1.getMessage());
			}
		}
		
	}
	
	public void close() {
		try {
			Container appletContainer = this.getParent().getParent()
					.getParent().getParent();
			if (appletContainer instanceof JApplet) {
				JSObject win = JSObject.getWindow((JApplet) appletContainer);
				if (win != null) {
					AppDebug.debug("close a IE window");
					win.eval("self.close()");
				}
			}
		} catch (JSException e1) {
			AppDebug.debug(e1);
			System.exit(0);
		}
	}
	public boolean print() {
		
		if (psCurrent != null && printDoc != null) {
			
			final IUFOMultiDocPrintJob printjob = psCurrent
					.createMultiDocPrintJob();
			String dialogInitialContent = UIManager
					.getString("PrintingDialog.contentInitialText");
			String progressTitle = UIManager
					.getString("PrintingDialog.titleProgressText");
			String abortText = UIManager
					.getString("PrintingDialog.abortButtonText");
			String abortTooltip = UIManager
					.getString("PrintingDialog.abortButtonToolTipText");
			final JButton abortButton = new JButton(abortText);
			abortButton.setToolTipText(abortTooltip);

			final JLabel statusLabel = new JLabel(dialogInitialContent);
			JOptionPane abortPane = new JOptionPane(statusLabel,
					JOptionPane.INFORMATION_MESSAGE,
					JOptionPane.DEFAULT_OPTION, null,
					new Object[] { abortButton }, abortButton);
			final JDialog abortDialog = abortPane.createDialog(this,
					progressTitle);
			abortDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			abortDialog.setSize(400, 100);
			printjob.setPrintStatus(statusLabel);
			final Action abortAction = new AbstractAction() {
				boolean isAborted = false;

				public void actionPerformed(ActionEvent ae) {
					if (!isAborted) {
						isAborted = true;
						abortButton.setEnabled(false);
						abortDialog.setTitle(UIManager
								.getString("PrintingDialog.titleAbortingText"));
						statusLabel
								.setText(UIManager
										.getString("PrintingDialog.contentAbortingText"));
						try {
							printjob.cancel();
						} catch (PrintException e) {
							// TODO Auto-generated catch block
							AppDebug.debug(e);
						}finally{
							abortButton.setEnabled(true);
						}
					}
				}
			};
			abortButton.addActionListener(abortAction);
			abortPane.getActionMap().put("close", abortAction);

			final WindowAdapter closeListener = new WindowAdapter() {
				public void windowClosing(WindowEvent we) {
					abortAction.actionPerformed(null);
				}
			};
			abortDialog.addWindowListener(closeListener);

			
			final PrintRequestAttributeSet copyAttr = asCurrent;
			Runnable runnable = new Runnable() {
				public void run() {
					
					
					
					try {
						printjob.print(printDoc, copyAttr);
					} catch (Throwable t) {
						AppDebug.debug(t);
						JOptionPane.showMessageDialog(null,t.getMessage());
					} finally {
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								// don't want to notify the abort action
								abortDialog.dispose();
								close();
							}
						});
						
					}
				}
			};
			Thread th = new Thread(runnable);
			th.start();
			abortDialog.setVisible(true);
		}
		return true;
	}
	
	private String getMsg(String s) {
		try {
			return uimessage.getString(s);
		} catch (MissingResourceException missingresourceexception) {
			throw new Error((new StringBuilder()).append(
					"Fatal: Resource for ServiceUI is broken; there is no ")
					.append(s).append(" key in resource").toString());
		}
	}
	
	private void initResource() {
		AccessController.doPrivileged(new PrivilegedAction<Object>() {

			public Object run() {
				try {
					String strShowRefID = getParameter(ITableInputAppletParam.PARAM_TI_SHOWREFID);
			        boolean bShowRefID = "true".equalsIgnoreCase(strShowRefID)?true:false;
			        ReportStyle.setShowRefID(bShowRefID);
			        
					uimessage = ResourceBundle
							.getBundle("sun.print.resources.serviceui");
					return null;
				} catch (MissingResourceException missingresourceexception) {
					throw new Error("Fatal: Resource for ServiceUI is missing");
				}
			}

		});
	}
	 private JButton createButton(String s, ActionListener actionlistener)
	    {
	        JButton jbutton = new UIButton(s);
	        jbutton.addActionListener(actionlistener);
	        return jbutton;
	    }
	 private JButton createExitButton(String s, ActionListener actionlistener)
	    {
	        String s1 = getMsg(s);
	        JButton jbutton = new UIButton(s1);
	        jbutton.addActionListener(actionlistener);
	        jbutton.getAccessibleContext().setAccessibleDescription(s1);
	        return jbutton;
	    }
	 private JCheckBox createCheckBox(String s, ActionListener actionlistener)
	    {
	        JCheckBox jcheckbox = new UICheckBox(getMsg(s));
	        jcheckbox.addActionListener(actionlistener);
	        return jcheckbox;
	    }
	 private JRadioButton createRadioButton(String s, ActionListener actionlistener)
	    {
	        JRadioButton jradiobutton = new UIRadioButton(s);
	        jradiobutton.addActionListener(actionlistener);
	        return jradiobutton;
	    }
	 private void updatePanels() {
			pnlGeneral.updateInfo();
		}
	/**
	 * 常规设置
	 * @author guogang
	 *
	 */
	private class GeneralPanel extends JPanel
    {
		private PrintServicePanel pnlPrintService;
        private PrintRangePanel pnlPrintRange;
        private CopiesPanel pnlCopies;

        public GeneralPanel()
        {
            super();
            GridBagLayout gridbaglayout = new GridBagLayout();
            GridBagConstraints gridbagconstraints = new GridBagConstraints();
            setLayout(gridbaglayout);
            gridbagconstraints.fill = 1;
            gridbagconstraints.insets =panelInsets;
            gridbagconstraints.weightx = 1.0D;
            gridbagconstraints.weighty = 1.0D;
            gridbagconstraints.gridwidth = 0;
            pnlPrintService = new PrintServicePanel();
            addToGB(pnlPrintService, this, gridbaglayout, gridbagconstraints);
            gridbagconstraints.gridwidth = -1;
            pnlPrintRange = new PrintRangePanel();
            addToGB(pnlPrintRange, this, gridbaglayout, gridbagconstraints);
            gridbagconstraints.gridwidth = 0;
            pnlCopies = new CopiesPanel();
            addToGB(pnlCopies, this, gridbaglayout, gridbagconstraints);
        }
        public boolean isPrintToFileSelected()
        {
            return pnlPrintService.isPrintToFileSelected();
        }

        public void updateInfo()
        {
            pnlPrintService.updateInfo();
            pnlPrintRange.updateInfo();
            pnlCopies.updateInfo();
        }        
    }
	/**
	 * 常规设置中的打印服务设置
	 * @author guogang
	 *
	 */
	private class PrintServicePanel extends JPanel implements ActionListener, ItemListener, PopupMenuListener
      {
		private final String strTitle = getMsg("border.printservice");
	    private FilePermission printToFilePermission;
	    /** 打印服务下拉框*/
	    private JComboBox cbName;
	    /** 打印服务属性按钮*/
	    private JButton btnProperties;
	    /** 类型：*/
	    private JLabel lblType;
	    /** 状态：*/
	    private JLabel lblStatus;
	    /** 信息：*/
	    private JLabel lblInfo;
	    /** 打印到文件单选按钮*/
	    private JCheckBox cbPrintToFile;
	    
	    private ServiceUIFactory uiFactory;
	    private boolean changedService;

	    public PrintServicePanel()
	    {
	        super();
	        changedService = false;
	        uiFactory = psCurrent.getServiceUIFactory();
	        GridBagLayout gridbaglayout = new GridBagLayout();
	        GridBagConstraints gridbagconstraints = new GridBagConstraints();
	        setLayout(gridbaglayout);
	        setBorder(BorderFactory.createTitledBorder(strTitle));
	        String as[] = new String[services.length];
	        for(int i = 0; i < as.length; i++)
	            as[i] = services[i].getName();

	        cbName = new JComboBox(as);
	        cbName.setSelectedIndex(defaultServiceIndex);
	        cbName.addItemListener(this);
	        cbName.addPopupMenuListener(this);
	        gridbagconstraints.fill = 1;
	        gridbagconstraints.insets = compInsets;
	        gridbagconstraints.weightx = 0.0D;
	        JLabel jlabel = new JLabel(getMsg("label.psname"), 11);
	        jlabel.setLabelFor(cbName);
	        addToGB(jlabel, this, gridbaglayout, gridbagconstraints);
	        gridbagconstraints.weightx = 1.0D;
	        gridbagconstraints.gridwidth = -1;
	        addToGB(cbName, this, gridbaglayout, gridbagconstraints);
	        gridbagconstraints.weightx = 0.0D;
	        gridbagconstraints.gridwidth = 0;
	        btnProperties =createButton(getMsg("button.properties"), this);
	        addToGB(btnProperties, this, gridbaglayout, gridbagconstraints);
	        gridbagconstraints.weighty = 1.0D;
	        lblStatus = addLabel(getMsg("label.status"), gridbaglayout, gridbagconstraints);
	        lblStatus.setLabelFor(null);
	        lblType = addLabel(getMsg("label.pstype"), gridbaglayout, gridbagconstraints);
	        lblType.setLabelFor(null);
	        gridbagconstraints.gridwidth = 1;
	        addToGB(new JLabel(getMsg("label.info"), 11), this, gridbaglayout, gridbagconstraints);
	        gridbagconstraints.gridwidth = -1;
	        lblInfo = new JLabel();
	        lblInfo.setForeground(Color.black);
	        lblInfo.setLabelFor(null);
	        addToGB(lblInfo, this, gridbaglayout, gridbagconstraints);
	        gridbagconstraints.gridwidth = 0;
	        cbPrintToFile =createCheckBox("checkbox.printtofile", this);
	        addToGB(cbPrintToFile, this, gridbaglayout, gridbagconstraints);
	    }
    public boolean isPrintToFileSelected()
    {
        return cbPrintToFile.isSelected();
    }

    private JLabel addLabel(String s, GridBagLayout gridbaglayout, GridBagConstraints gridbagconstraints)
    {
        gridbagconstraints.gridwidth = 1;
        addToGB(new JLabel(s, 11), this, gridbaglayout, gridbagconstraints);
        gridbagconstraints.gridwidth = 0;
        JLabel jlabel = new JLabel();
        jlabel.setForeground(Color.black);
        addToGB(jlabel, this, gridbaglayout, gridbagconstraints);
        return jlabel;
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        Object obj = actionevent.getSource();
        if(obj == btnProperties && uiFactory != null)
        {
            JDialog jdialog = (JDialog)uiFactory.getUI(3, "javax.swing.JDialog");
            if(jdialog != null)
                jdialog.show();
            else
                btnProperties.setEnabled(false);
        }
    }

    public void itemStateChanged(ItemEvent itemevent)
    {
        if(itemevent.getStateChange() == 1)
        {
            int i = cbName.getSelectedIndex();
            if(i >= 0 && i < services.length && !services[i].equals(psCurrent))
            {
                psCurrent = services[i];
                uiFactory = psCurrent.getServiceUIFactory();
                changedService = true;
                if(isPrintToFileSelected() && psCurrent.isAttributeCategorySupported(Destination.class))
                {
                    asCurrent.add(new Destination((new File("out.prn")).toURI()));
                } else
                {
                    asCurrent.remove(Destination.class);
                }
            }
        }
    }

    public void popupMenuWillBecomeVisible(PopupMenuEvent popupmenuevent)
    {
        changedService = false;
    }

    public void popupMenuWillBecomeInvisible(PopupMenuEvent popupmenuevent)
    {
        if(changedService)
        {
            changedService = false;
            updatePanels();
        }
    }

    public void popupMenuCanceled(PopupMenuEvent popupmenuevent)
    {
    }

    private boolean allowedToPrintToFile()
    {
        try
        {
            throwPrintToFile();
            return true;
        }
        catch(SecurityException securityexception)
        {
            return false;
        }
    }

    private void throwPrintToFile()
    {
        SecurityManager securitymanager = System.getSecurityManager();
        if(securitymanager != null)
        {
            if(printToFilePermission == null)
                printToFilePermission = new FilePermission("<<ALL FILES>>", "read,write");
            securitymanager.checkPermission(printToFilePermission);
        }
    }

    public void updateInfo()
    {
        boolean flag = false;
        boolean flag1 = false;
        boolean flag2 = allowedToPrintToFile();
        if(psCurrent.isAttributeCategorySupported(Destination.class))
            flag = true;
        Destination destination = (Destination)asCurrent.get(Destination.class);
        if(destination != null)
            flag1 = true;
        cbPrintToFile.setEnabled(flag && flag2);
        cbPrintToFile.setSelected(flag1 && flag2);
        //获取打印机的制造和型号
        javax.print.attribute.PrintServiceAttribute printserviceattribute = psCurrent.getAttribute(PrinterMakeAndModel.class);
        if(printserviceattribute != null)
            lblType.setText(printserviceattribute.toString());
        //获取打印机是否接受作业信息
        javax.print.attribute.PrintServiceAttribute printserviceattribute1 = psCurrent.getAttribute(PrinterIsAcceptingJobs.class);
        if(printserviceattribute1 != null)
            lblStatus.setText(getMsg(printserviceattribute1.toString()));
        //获取打印机的信息
        javax.print.attribute.PrintServiceAttribute printserviceattribute2 = psCurrent.getAttribute(PrinterInfo.class);
        if(printserviceattribute2 != null)
            lblInfo.setText(printserviceattribute2.toString());
        btnProperties.setEnabled(uiFactory != null);
    }

    
}
	/**
	 * 常规设置中的打印区域设置
	 * @author guogang
	 *
	 */
	private class PrintRangePanel extends JPanel implements ActionListener, FocusListener
      {
		private final String strTitle =getMsg("border.printrange");
	    /** 初始的打印页码范围*/
		private final PageRanges prAll = new PageRanges(1, 2147483647);
	    /** "全部"的单选按钮*/
		private JRadioButton rbAll;
		/** "页码范围"的单选按钮*/
	    private JRadioButton rbPages;
	    private JRadioButton rbSelect;
	    /** 打印起始页码*/
	    private JFormattedTextField tfRangeFrom;
	    /** 打印结束页码*/
	    private JFormattedTextField tfRangeTo;
	    /** "至"*/
	    private JLabel lblRangeTo;
	    private boolean prSupported;

	    public PrintRangePanel()
	    {
	        super();
	        GridBagLayout gridbaglayout = new GridBagLayout();
	        GridBagConstraints gridbagconstraints = new GridBagConstraints();
	        setLayout(gridbaglayout);
	        setBorder(BorderFactory.createTitledBorder(strTitle));
	        gridbagconstraints.fill = 1;
	        gridbagconstraints.insets = compInsets;
	        gridbagconstraints.gridwidth = 0;
	        ButtonGroup buttongroup = new ButtonGroup();
	        JPanel jpanel = new JPanel(new FlowLayout(3));
	        rbAll = createRadioButton(getMsg("radiobutton.rangeall"), this);
	        rbAll.setSelected(true);
	        buttongroup.add(rbAll);
	        jpanel.add(rbAll);
	        addToGB(jpanel, this, gridbaglayout, gridbagconstraints);
	        JPanel jpanel1 = new JPanel(new FlowLayout(3));
	        rbPages = createRadioButton(getMsg("radiobutton.rangepages"), this);
	        buttongroup.add(rbPages);
	        jpanel1.add(rbPages);
	        DecimalFormat decimalformat = new DecimalFormat("####0");
	        decimalformat.setMinimumFractionDigits(0);
	        decimalformat.setMaximumFractionDigits(0);
	        decimalformat.setMinimumIntegerDigits(0);
	        decimalformat.setMaximumIntegerDigits(5);
	        decimalformat.setParseIntegerOnly(true);
	        decimalformat.setDecimalSeparatorAlwaysShown(false);
	        NumberFormatter numberformatter = new NumberFormatter(decimalformat);
	        numberformatter.setMinimum(new Integer(1));
	        numberformatter.setMaximum(new Integer(2147483647));
	        numberformatter.setAllowsInvalid(true);
	        numberformatter.setCommitsOnValidEdit(true);
	        tfRangeFrom = new JFormattedTextField(numberformatter);
	        tfRangeFrom.setColumns(4);
	        tfRangeFrom.setEnabled(false);
	        tfRangeFrom.addActionListener(this);
	        tfRangeFrom.addFocusListener(this);
	        tfRangeFrom.setFocusLostBehavior(3);
	        tfRangeFrom.getAccessibleContext().setAccessibleName(getMsg("radiobutton.rangepages"));
	        jpanel1.add(tfRangeFrom);
	        lblRangeTo = new JLabel(getMsg("label.rangeto"));
	        lblRangeTo.setEnabled(false);
	        jpanel1.add(lblRangeTo);
	        NumberFormatter numberformatter1;
	        try
	        {
	            numberformatter1 = (NumberFormatter)numberformatter.clone();
	        }
	        catch(CloneNotSupportedException clonenotsupportedexception)
	        {
	            numberformatter1 = new NumberFormatter();
	        }
	        tfRangeTo = new JFormattedTextField(numberformatter1);
	        tfRangeTo.setColumns(4);
	        tfRangeTo.setEnabled(false);
	        tfRangeTo.addFocusListener(this);
	        tfRangeTo.getAccessibleContext().setAccessibleName(getMsg("label.rangeto"));
	        jpanel1.add(tfRangeTo);
	        addToGB(jpanel1, this, gridbaglayout, gridbagconstraints);
	    }
    public void actionPerformed(ActionEvent actionevent)
    {
        Object obj = actionevent.getSource();
        SunPageSelection sunpageselection = SunPageSelection.ALL;
        setupRangeWidgets();
        if(obj == rbAll)
            asCurrent.add(prAll);
        else
        if(obj == rbSelect)
            sunpageselection = SunPageSelection.SELECTION;
        else
        if(obj == rbPages || obj == tfRangeFrom || obj == tfRangeTo)
        {
            updateRangeAttribute();
            sunpageselection = SunPageSelection.RANGE;
        }
        if(isAWT)
            asCurrent.add(sunpageselection);
    }

    public void focusLost(FocusEvent focusevent)
    {
        Object obj = focusevent.getSource();
        if(obj == tfRangeFrom || obj == tfRangeTo)
            updateRangeAttribute();
    }

    public void focusGained(FocusEvent focusevent)
    {
    }

    private void setupRangeWidgets()
    {
        boolean flag = rbPages.isSelected() && prSupported;
        tfRangeFrom.setEnabled(flag);
        tfRangeTo.setEnabled(flag);
        lblRangeTo.setEnabled(flag);
    }

    private void updateRangeAttribute()
    {
        String s = tfRangeFrom.getText();
        String s1 = tfRangeTo.getText();
        int i;
        try
        {
            i = Integer.parseInt(s);
        }
        catch(NumberFormatException numberformatexception)
        {
            i = 1;
        }
        int j;
        try
        {
            j = Integer.parseInt(s1);
        }
        catch(NumberFormatException numberformatexception1)
        {
            j = i;
        }
        if(i < 1)
        {
            i = 1;
            tfRangeFrom.setValue(new Integer(1));
        }
        if(j < i)
        {
            j = i;
            tfRangeTo.setValue(new Integer(i));
        }
        PageRanges pageranges = new PageRanges(i, j);
        asCurrent.add(pageranges);
    }

    public void updateInfo()
    {
        prSupported = false;
        if(psCurrent.isAttributeCategorySupported(PageRanges.class) || isAWT)
            prSupported = true;
        SunPageSelection sunpageselection = SunPageSelection.ALL;
        int i = 1;
        int j = 1;
        PageRanges pageranges = (PageRanges)asCurrent.get(PageRanges.class);
        if(pageranges != null && !pageranges.equals(prAll))
        {
            sunpageselection = SunPageSelection.RANGE;
            int ai[][] = pageranges.getMembers();
            if(ai.length > 0 && ai[0].length > 1)
            {
                i = ai[0][0];
                j = ai[0][1];
            }
        }
        if(isAWT)
            sunpageselection = (SunPageSelection)asCurrent.get(SunPageSelection.class);
        if(sunpageselection == SunPageSelection.ALL)
            rbAll.setSelected(true);
        else
        if(sunpageselection != SunPageSelection.SELECTION)
            rbPages.setSelected(true);
        tfRangeFrom.setValue(new Integer(i));
        tfRangeTo.setValue(new Integer(j));
        rbAll.setEnabled(prSupported);
        rbPages.setEnabled(prSupported);
        setupRangeWidgets();
    }

}
	/**
	 * 常规设置中的打印份数的设置
	 * @author guogang
	 *
	 */
	private class CopiesPanel extends JPanel implements ActionListener, ChangeListener
      {
		private final String strTitle = getMsg("border.copies");
		/** 份数选择组件*/
	    private JSpinner spinCopies;
		/** 份数选择的数字序列*/
		private SpinnerNumberModel snModel;
		/** "份数"*/
	    private JLabel lblCopies;
	    /** "比较",对多页的文档：选择的时候，会每页都打印完成后在重复打印其他的份数；不选择的时候会先按份数打印完一页后再去重复打印其他的页*/
	    private JCheckBox cbCollate;
	    private boolean scSupported;

	    public CopiesPanel()
	    {
	        super();
	        GridBagLayout gridbaglayout = new GridBagLayout();
	        GridBagConstraints gridbagconstraints = new GridBagConstraints();
	        setLayout(gridbaglayout);
	        setBorder(BorderFactory.createTitledBorder(strTitle));
	        gridbagconstraints.fill = 2;
	        gridbagconstraints.insets =compInsets;
	        lblCopies = new JLabel(getMsg("label.numcopies"), 11);
	        lblCopies.getAccessibleContext().setAccessibleName(getMsg("label.numcopies"));
	        addToGB(lblCopies, this, gridbaglayout, gridbagconstraints);
	        snModel = new SpinnerNumberModel(1, 1, 999, 1);
	        spinCopies = new JSpinner(snModel);
	        lblCopies.setLabelFor(spinCopies);
	        ((javax.swing.JSpinner.NumberEditor)spinCopies.getEditor()).getTextField().setColumns(3);
	        spinCopies.addChangeListener(this);
	        gridbagconstraints.gridwidth = 0;
	        addToGB(spinCopies, this, gridbaglayout, gridbagconstraints);
	        cbCollate = createCheckBox("checkbox.collate", this);
	        cbCollate.setEnabled(false);
	        addToGB(cbCollate, this, gridbaglayout, gridbagconstraints);
	    }
    public void actionPerformed(ActionEvent actionevent)
    {
        if(cbCollate.isSelected())
            asCurrent.add(SheetCollate.COLLATED);
        else
            asCurrent.add(SheetCollate.UNCOLLATED);
    }

    public void stateChanged(ChangeEvent changeevent)
    {
        updateCollateCB();
        asCurrent.add(new Copies(snModel.getNumber().intValue()));
    }

    private void updateCollateCB()
    {
        int i = snModel.getNumber().intValue();
        if(isAWT)
            cbCollate.setEnabled(true);
        else
            cbCollate.setEnabled(i > 1 && scSupported);
    }

    public void updateInfo()
    {
        boolean flag = false;
        scSupported = false;
        if(psCurrent.isAttributeCategorySupported(Copies.class))
            flag = true;
        CopiesSupported copiessupported = (CopiesSupported)psCurrent.getSupportedAttributeValues(CopiesSupported.class, null, null);
        if(copiessupported == null)
            copiessupported = new CopiesSupported(1, 999);
        Copies copies = (Copies)asCurrent.get(Copies.class);
        if(copies == null)
        {
            copies = (Copies)psCurrent.getDefaultAttributeValue(Copies.class);
            if(copies == null)
                copies = new Copies(1);
        }
        spinCopies.setEnabled(flag);
        lblCopies.setEnabled(flag);
        int ai[][] = copiessupported.getMembers();
        int i;
        int j;
        if(ai.length > 0 && ai[0].length > 0)
        {
            i = ai[0][0];
            j = ai[0][1];
        } else
        {
            i = 1;
            j = 2147483647;
        }
        snModel.setMinimum(new Integer(i));
        snModel.setMaximum(new Integer(j));
        int k = copies.getValue();
        if(k < i || k > j)
            k = i;
        snModel.setValue(new Integer(k));
        if(psCurrent.isAttributeCategorySupported(SheetCollate.class))
            scSupported = true;
        SheetCollate sheetcollate = (SheetCollate)asCurrent.get(SheetCollate.class);
        if(sheetcollate == null)
        {
            sheetcollate = (SheetCollate)psCurrent.getDefaultAttributeValue(SheetCollate.class);
            if(sheetcollate == null)
                sheetcollate = SheetCollate.UNCOLLATED;
        }
        cbCollate.setSelected(sheetcollate == SheetCollate.COLLATED);
        updateCollateCB();
    }

    
}
	private void addToGB(Component component, Container container, GridBagLayout gridbaglayout, GridBagConstraints gridbagconstraints)
    {
        gridbaglayout.setConstraints(component, gridbagconstraints);
        container.add(component);
    }

	@Override
	public void setUfoReport(UfoReport report) {
		this.m_oUfoReport=report;
	}
	/**
	 * 主要用来实现加载不同的插件
	 * @create by guogang at Feb 11, 2009,3:20:02 PM
	 *
	 * @param table 当前打印的table
	 * @return
	 */
	protected UfoReport initReport(UFOTable table){
		return new UfoReport(table,new BatchPrintEditPluginRegister());
	}
	
	/**
	 * 主要用来加载基础的绘制器
	 * @create by guogang at Feb 16, 2009,2:10:54 PM
	 *
	 * @param datas
	 * @return
	 */
	protected IUFOMultiDoc initDoc(List<TableInputTransObj> datas){
		return new IUFOMultiDoc(datas);
	}
}
 