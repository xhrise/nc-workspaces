package com.ufsoft.table.print.multidoc;

import java.awt.*;
import java.awt.event.*;
import java.awt.print.PageFormat;
import java.awt.print.Paper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.NumberFormatter;

import java.io.File;
import java.io.FilePermission;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.DecimalFormat;
import java.util.*;

import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.report.sysplugin.print.HeaderFooterDefDlg;
import com.ufsoft.report.sysplugin.print.HeaderFooterModel;
import com.ufsoft.report.sysplugin.print.HeaderFooterSegmentComp;
import com.ufsoft.report.sysplugin.print.HeaderFooterSegmentModel;
import com.ufsoft.report.util.ColDocument;
import com.ufsoft.report.util.DeepCopyUtil;
import com.ufsoft.report.util.IntegerDocument;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.print.PrintSet;
import com.ufsoft.table.print.multidoc.win32.IUFOPrintService;

import javax.print.DocFlavor;
import javax.print.ServiceUIFactory;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.Size2DSyntax;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.CopiesSupported;
import javax.print.attribute.standard.Destination;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.MediaTray;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PageRanges;
import javax.print.attribute.standard.PrinterInfo;
import javax.print.attribute.standard.PrinterIsAcceptingJobs;
import javax.print.attribute.standard.PrinterMakeAndModel;
import javax.print.attribute.standard.SheetCollate;

import nc.ui.pub.beans.UIRadioButton;

import sun.print.SunAlternateMedia;
import sun.print.SunPageSelection;
/**
 * 打印对话框
 * @author guogang
 *
 */
public class IUFOServiceDialog extends JDialog implements ActionListener {
	
	private boolean isAWT;
	/**isIndividual为true 页面设置的时候只设置PrintSet,否则只设置HashPrintRequestAttributeSet */
	private boolean isIndividual;
	public static final int WAITING = 0;
    public static final int APPROVE = 1;
    public static final int CANCEL = 2;
    private double d = 72D;
	private int units;
	private static final Insets panelInsets = new Insets(6, 6, 6, 6);
    private static final Insets compInsets = new Insets(3, 6, 3, 6);
	//打印相关
	private static ResourceBundle uimessage;
	/** 当前的打印服务*/
	private IUFOPrintService psCurrent;
	/** 当前的打印服务支持的数据类型*/
    private DocFlavor docFlavor;
    /** 获取的所有打印服务*/
    private IUFOPrintService[] services;
    /** 默认的打印服务序号*/
    private int defaultServiceIndex;
    /** 打印状态*/
    private int status;
    /** 进入打印设置对话框之前原始的打印请求设置*/
    private PrintRequestAttributeSet asOriginal;
    /** 设置后保存的当前的打印设置*/
    private HashPrintRequestAttributeSet asCurrent;
    /** 进入打印设置对话框之前原始的CellsModel个性化设置*/
    private PrintSet psetOriginal;
    /**设置后保存的当前的CellsModel个性化设置*/
    private PrintSet psetCurrent;
    /**IUFO个性化的打印设置面版*/
    private IndividualPanel pnlIndividual;
    /**常规设置面版*/
    private GeneralPanel pnlGeneral;
    /**页面设置面版*/
    private PageSetupPanel pnlPageSetup;
    /**外观设置面版*/
//    private AppearancePanel pnlAppearance;
    private JButton btnCancel;
    private JButton btnApprove;
	
	static 
    {
        initResource();
    }
	
	/**
	 * 构造器
	 * @param graphicsconfiguration 图形设备，null说明是首选设备或默认设备
	 * @param i 对话框父级坐标空间中新位置左上角的 x 坐标
	 * @param j 对话框父级坐标空间中新位置左上角的 y 坐标
	 * @param aprintservice 打印服务数组
	 * @param k 默认打印服务序号
	 * @param docflavor 打印服务支持的数据格式 
	 * @param printrequestattributeset 打印请求设置
	 * @param dialog 父对话框
	 * @param individuation 是否有个性化设置
	 */
	public IUFOServiceDialog(GraphicsConfiguration graphicsconfiguration, int i, int j, IUFOPrintService[] aprintservice, int k, DocFlavor docflavor, PrintRequestAttributeSet printrequestattributeset, 
            Dialog dialog,PrintSet printSet)
    {
        super(dialog, getMsg("dialog.printtitle"), true, graphicsconfiguration);
        isAWT = false;
        psetOriginal=printSet;
        initPrintDialog(i, j, aprintservice, k, docflavor, printrequestattributeset);
    }
    /**
     * 构造器
	 * @param graphicsconfiguration 图形设备，null说明是首选设备或默认设备
	 * @param i 对话框父级坐标空间中新位置左上角的 x 坐标
	 * @param j 对话框父级坐标空间中新位置左上角的 y 坐标
	 * @param aprintservice 打印服务数组
	 * @param k 默认打印服务序号
	 * @param docflavor 打印服务支持的数据格式 
	 * @param printrequestattributeset 打印请求设置
     * @param frame 父框架
     * @param individuation 是否有个性化设置
     */
    public IUFOServiceDialog(GraphicsConfiguration graphicsconfiguration, int i, int j, IUFOPrintService[] aprintservice, int k, DocFlavor docflavor, PrintRequestAttributeSet printrequestattributeset, 
            Frame frame,PrintSet printSet)
    {
        super(frame, getMsg("dialog.printtitle"), true, graphicsconfiguration);
        isAWT = false;
        psetOriginal=printSet;
        initPrintDialog(i, j, aprintservice, k, docflavor, printrequestattributeset);
    }
    /**
     * 初始化打印对话框
     * @param i 对话框父级坐标空间中新位置左上角的 x 坐标
     * @param j 对话框父级坐标空间中新位置左上角的 y 坐标
     * @param aprintservice 打印服务数组
     * @param k 默认打印服务序号
	 * @param docflavor 打印服务支持的数据格式 
	 * @param printrequestattributeset 打印请求设置
     */
    void initPrintDialog(int i, int j, IUFOPrintService[] aprintservice, int k, DocFlavor docflavor, PrintRequestAttributeSet printrequestattributeset)
    {
    	defaultServiceIndex=k;
    	services=aprintservice;
    	psCurrent=aprintservice[k];
    	asOriginal = printrequestattributeset;
        asCurrent = new HashPrintRequestAttributeSet(printrequestattributeset);
        
        units = Size2DSyntax.MM;
        String country = Locale.getDefault().getCountry();
		if (country != null
				&& (country.equals("") || country.equals(Locale.US.getCountry()) || country
						.equals(Locale.CANADA.getCountry()))) {
			units = Size2DSyntax.INCH;
		}
		
        SunPageSelection sunpageselection = (SunPageSelection)printrequestattributeset.get(SunPageSelection.class);
        if(sunpageselection != null)
            isAWT = true;
        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        
        
        if(psetOriginal==null){
             pnlGeneral = new GeneralPanel();
             container.add(pnlGeneral, "Center");
             
        }else{
        	//页面组件
            JTabbedPane tpTabs=new JTabbedPane();
            tpTabs.setBorder(new EmptyBorder(5, 5, 5, 5));
        	isIndividual=true;
        	psetCurrent=(PrintSet) psetOriginal.clone();//DeepCopyUtil.getDeepCopyBySerializable(psetOriginal);
        	
        	String s = getMsg("tab.general");
            pnlGeneral = new GeneralPanel();
            tpTabs.add(s, pnlGeneral);
            
        	String s1 = getMsg("tab.pagesetup");
            pnlPageSetup = new PageSetupPanel();
            tpTabs.add(s1, pnlPageSetup);
            
        	String sp=MultiLang.getString("PersonalPrintSet");
        	pnlIndividual=new IndividualPanel();
        	tpTabs.add(sp,pnlIndividual);
        	
        	container.add(tpTabs, "Center");
        }
       
        
        updatePanels();
        JPanel jpanel = new JPanel(new FlowLayout(4));
        btnApprove = createExitButton("button.print", this);
        jpanel.add(btnApprove);
        getRootPane().setDefaultButton(btnApprove);
        btnCancel = createExitButton("button.cancel", this);
        handleEscKey(btnCancel);
        jpanel.add(btnCancel);
        container.add(jpanel, "South");
    	
        setResizable(false);
        setLocation(i, j);
        pack();
    }
    /**
     * 加载和更新各panel的信息
     */
    private void updatePanels() {
		pnlGeneral.updateInfo();
		if(psetOriginal!=null){
			pnlPageSetup.updateInfo();
			pnlIndividual.updateInfo();
		}
		// pnlAppearance.updateInfo();
	}
    private void handleEscKey(JButton jbutton)
    {
        AbstractAction abstractaction = new AbstractAction() {

            public void actionPerformed(ActionEvent actionevent)
            {
                dispose(2);
            }
        };
        KeyStroke keystroke = KeyStroke.getKeyStroke('\033', false);
        InputMap inputmap = jbutton.getInputMap(2);
        ActionMap actionmap = jbutton.getActionMap();
        if(inputmap != null && actionmap != null)
        {
            inputmap.put(keystroke, "cancel");
            actionmap.put("cancel", abstractaction);
        }
    }
    public void dispose(int i)
    {
        status = i;
        super.dispose();
    }
    private boolean showFileChooser()
    {
        Destination destination = (Destination)asCurrent.get(Destination.class);
        if(destination == null)
        {
            destination = (Destination)asOriginal.get(Destination.class);
            if(destination == null)
            {
                destination = (Destination)psCurrent.getDefaultAttributeValue(Destination.class);
                if(destination == null)
                    destination = new Destination((new File("out.prn")).toURI());
            }
        }
        File file;
        if(destination != null)
            try
            {
                file = new File(destination.getURI());
            }
            catch(Exception exception)
            {
                file = new File("out.prn");
            }
        else
            file = new File("out.prn");
        ValidatingFileChooser validatingfilechooser = new ValidatingFileChooser();
        validatingfilechooser.setApproveButtonText(getMsg("button.ok"));
        validatingfilechooser.setDialogTitle(getMsg("dialog.printtofile"));
        validatingfilechooser.setSelectedFile(file);
        int i = validatingfilechooser.showDialog(this, null);
        if(i == 0)
        {
            File file1 = validatingfilechooser.getSelectedFile();
            try
            {
                asCurrent.add(new Destination(file1.toURI()));
            }
            catch(Exception exception1)
            {
                asCurrent.remove(Destination.class);
            }
        } else
        {
            asCurrent.remove(Destination.class);
        }
        return i == 0;
    }
	/**
	 * 加载资源文件
	 */
	private static void initResource() {
		AccessController.doPrivileged(new PrivilegedAction<Object>() {

			public Object run() {
				try {
					IUFOServiceDialog.uimessage = ResourceBundle
							.getBundle("sun.print.resources.serviceui");
					return null;
				} catch (MissingResourceException missingresourceexception) {
					throw new Error("Fatal: Resource for ServiceUI is missing");
				}
			}

		});
	}

	/**
	 * 从资源文件中获取对应的中文
	 * 
	 * @param s
	 * @return
	 */
	private static String getMsg(String s) {
		try {
			return uimessage.getString(s);
		} catch (MissingResourceException missingresourceexception) {
			throw new Error((new StringBuilder()).append(
					"Fatal: Resource for ServiceUI is broken; there is no ")
					.append(s).append(" key in resource").toString());
		}
	}
    /**
     * 获取打印服务状态
     * @return
     */
	public int getStatus() {
		return status;
	}
    /**
     * 获取打印请求设置
     * @return
     */
	public PrintRequestAttributeSet getAttributes() {
		if (status == 1)
			return asCurrent;
		else
			return asOriginal;
	}
	public PrintSet getPrintSet(){
		if(status==1)
			return psetCurrent;
		else
			return psetOriginal;
	}
    /**
     * 获取打印设置
     * @return
     */
	public IUFOPrintService getPrintService() {
		if (status == 1)
			return psCurrent;
		else
			return null;
	}
    /**
	 * 将组件按照指定的方式加到父组件
	 * 
	 * @param component
	 *            要添加到父组件的组件
	 * @param container
	 *            父组件容器
	 * @param gridbaglayout
	 *            组件的布局管理器
	 * @param gridbagconstraints
	 *            GridBagLayout管理的组件component对应的GridBagConstraints实例，指定组件在网格中的显示区域以及组件在其显示区域中的放置方式
	 * @see GridBagLayout
	 */
    private static void addToGB(Component component, Container container, GridBagLayout gridbaglayout, GridBagConstraints gridbagconstraints)
    {
        gridbaglayout.setConstraints(component, gridbagconstraints);
        container.add(component);
    }
    /**
     * 
     * @param abstractbutton 按钮组中要添加到父组件的按钮
     * @param container 父组件容器
     * @param buttongroup 排斥的按钮组
     */
    private static void addToBG(AbstractButton abstractbutton, Container container, ButtonGroup buttongroup)
    {
        buttongroup.add(abstractbutton);
        container.add(abstractbutton);
    }
    private Paper getPaperFromMedia(Media media){
    	Paper paper=new Paper();
    	MediaSize mediasize;
    	if(media instanceof MediaSizeName&& (mediasize = MediaSize.getMediaSizeForName((MediaSizeName)media)) != null){
    		double d1 = (double)mediasize.getX(Size2DSyntax.INCH) * d;
            double d3 = (double)mediasize.getY(Size2DSyntax.INCH) * d;
            paper.setSize(d1, d3);
            paper.setImageableArea(d, d, d1 - 2 * d, d3 - 2 * d);
    	}
    	return paper;
    }
    private void setPaperFromMedia(Paper orgPaper,MediaPrintableArea media){
    	    double d1 = media.getX(Size2DSyntax.INCH) * d;
            double d2 = media.getY(Size2DSyntax.INCH) * d;
            double d3=media.getWidth(Size2DSyntax.INCH)*d;
            double d4=media.getHeight(Size2DSyntax.INCH)*d;
            orgPaper.setImageableArea(d1, d2, d3, d4);
    }
    /**
     * 创建按钮组件
     * @param s
     * @param actionlistener
     * @return
     */
    private static JButton createButton(String s, ActionListener actionlistener)
    {
        JButton jbutton = new JButton(s);
        jbutton.addActionListener(actionlistener);
        return jbutton;
    }

    private static JButton createExitButton(String s, ActionListener actionlistener)
    {
        String s1 = getMsg(s);
        JButton jbutton = new JButton(s1);
        jbutton.addActionListener(actionlistener);
        jbutton.getAccessibleContext().setAccessibleDescription(s1);
        return jbutton;
    }
    private static JPanel createLabelText(JLabel jlabel,JTextField jtextfield){
    	JPanel jpanel=new JPanel();
    	GridBagLayout gridbaglayout = new GridBagLayout();
		GridBagConstraints gridbagconstraints = new GridBagConstraints();
		jpanel.setLayout(gridbaglayout);
		gridbagconstraints.fill = GridBagConstraints.HORIZONTAL;
		gridbagconstraints.weightx = 0.0D;
		gridbagconstraints.gridwidth=GridBagConstraints.RELATIVE;
		IUFOServiceDialog.addToGB(jlabel, jpanel, gridbaglayout, gridbagconstraints);
		gridbagconstraints.gridwidth=GridBagConstraints.REMAINDER;
		jlabel.setLabelFor(jtextfield);
		gridbagconstraints.weightx = 1.0D;
		IUFOServiceDialog.addToGB(jtextfield, jpanel, gridbaglayout, gridbagconstraints);
		return jpanel;
    }
    private static JPanel createFromToLabelText(JLabel jlabelFrom,JTextField jtextfieldFrom,JLabel jlabelTo,JTextField jtextfieldTo){
    	JPanel jpanel=new JPanel();
    	GridBagLayout gridbaglayout = new GridBagLayout();
		GridBagConstraints gridbagconstraints = new GridBagConstraints();
		jpanel.setLayout(gridbaglayout);
		gridbagconstraints.fill = GridBagConstraints.HORIZONTAL;
		gridbagconstraints.weightx = 1.0D;
		gridbagconstraints.gridwidth=GridBagConstraints.RELATIVE;
		
		IUFOServiceDialog.addToGB(createLabelText(jlabelFrom,jtextfieldFrom), jpanel, gridbaglayout, gridbagconstraints);
		gridbagconstraints.gridwidth=GridBagConstraints.REMAINDER;
		IUFOServiceDialog.addToGB(createLabelText(jlabelTo,jtextfieldTo), jpanel, gridbaglayout, gridbagconstraints);
		return jpanel;
    }
    /**
     * 创建单选按钮
     * @param s
     * @param actionlistener
     * @return
     */
    private static JCheckBox createCheckBox(String s, ActionListener actionlistener)
    {
        JCheckBox jcheckbox = new JCheckBox(getMsg(s));
        jcheckbox.addActionListener(actionlistener);
        return jcheckbox;
    }

    private String getIntString(int value) {
		if (value <= 0) {
			return "";
		}
		return Integer.toString(value);
	}
    private int getIntValue(String s) {
		if (s == null || s.equals("")) {
			return 0;
		}
		return Integer.parseInt(s);
	}
    private String colValue2ColLabel(int val){
		if(val == 0){
			return "";
		}
		String lbl = "";
		int a, b = 0;
		a = val;
		while(true){
			double a1 = a / 26;
			b = a % 26;
			lbl = String.valueOf((char)(b + 'A' - 1)) + lbl;
			if(a1 < 1){
				break;
			}
			a = (int) (a1);
		}
		return lbl;
		
	}
    private int colLabel2colValue(String lbl){
		if(lbl == null || lbl.trim().length() < 1){
			return 0;
		}
		if(!validColValue(lbl)){
			return 0;
		}
		
		char[] arr = lbl.toCharArray();
		if(arr.length > 2){
			return 0;
		}
		int val = 0;
		if(arr.length == 2){
			char c = arr[0];
			val = ((c - 'A') + 1) * 26;
			val += (arr[1] - 'A') + 1;
		} else {
			val += (arr[0] - 'A') + 1;
		}
		return val;
	}
  //校验输入列值的合法性
	private boolean validColValue(String v){
		if(v == null || v.length() < 1){
			return true;
		}
		if(v.length() > 2){
			return false;
		}
		
		char[] arr  = v.toCharArray();
		for (int i = 0; i < arr.length; i++) {
			char c = arr[i];
			if(c < 'A' || c > 'Z'){
				return false;
			}
		}
		
		return true;
	}
    /**
     * 创建单选按钮
     * @param s
     * @param actionlistener
     * @return
     */
    private static JRadioButton createRadioButton(String s, ActionListener actionlistener)
    {
        JRadioButton jradiobutton = new JRadioButton(s);
        jradiobutton.addActionListener(actionlistener);
        return jradiobutton;
    }
    /**
     * 创建面板
     * @return
     */
    private static JPanel createPanel(String strTitle){
    	JPanel jpanel=new JPanel();
    	jpanel.setBorder(BorderFactory.createTitledBorder(strTitle));
    	jpanel.setBackground(Color.WHITE);
    	jpanel.setLayout(null);
    	return jpanel;
    }
    /**
     * 个性化设置在提交前需要验证输入
     */
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
        boolean flag = false;
        if(obj == btnApprove)
        {
            flag = true;
            if(pnlGeneral != null)
                if(pnlGeneral.isPrintToFileSelected())
                    flag = showFileChooser();
                else
                    asCurrent.remove(Destination.class);
            if(pnlIndividual!=null){
            	if(!pnlIndividual.validateInput())
            		return;
            }
        }
        dispose(flag ? 1 : 2);

	}
	//******
	//对话框的内部类
	//******
	/**
	 * 带图表的单元按钮
	 */
	private class IconRadioButton extends JPanel
    {
		/** 单元按钮*/
		private JRadioButton rb;
		/** 单元按钮标签*/
        private JLabel lbl;
        
        public IconRadioButton(String s, String s1, boolean flag, ButtonGroup buttongroup, ActionListener actionlistener)
        {
            super(new FlowLayout(3));
            Icon icon =  ResConst.getImageIcon("reportcore/"+s1);
            lbl = new JLabel(icon);
            add(lbl);
            rb = IUFOServiceDialog.createRadioButton(s, actionlistener);
            rb.setSelected(flag);
            IUFOServiceDialog.addToBG(rb, this, buttongroup);
        }
        
        public void addActionListener(ActionListener actionlistener)
        {
            rb.addActionListener(actionlistener);
        }

        public boolean isSameAs(Object obj)
        {
            return rb == obj;
        }

        public void setEnabled(boolean flag)
        {
            rb.setEnabled(flag);
            lbl.setEnabled(flag);
        }

        public boolean isSelected()
        {
            return rb.isSelected();
        }

        public void setSelected(boolean flag)
        {
            rb.setSelected(flag);
        }

        
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
            gridbagconstraints.insets = IUFOServiceDialog.panelInsets;
            gridbagconstraints.weightx = 1.0D;
            gridbagconstraints.weighty = 1.0D;
            gridbagconstraints.gridwidth = 0;
            pnlPrintService = new PrintServicePanel();
            IUFOServiceDialog.addToGB(pnlPrintService, this, gridbaglayout, gridbagconstraints);
            gridbagconstraints.gridwidth = -1;
            pnlPrintRange = new PrintRangePanel();
            IUFOServiceDialog.addToGB(pnlPrintRange, this, gridbaglayout, gridbagconstraints);
            gridbagconstraints.gridwidth = 0;
            pnlCopies = new CopiesPanel();
            IUFOServiceDialog.addToGB(pnlCopies, this, gridbaglayout, gridbagconstraints);
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
		private final String strTitle = IUFOServiceDialog.getMsg("border.printservice");
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
	        gridbagconstraints.insets = IUFOServiceDialog.compInsets;
	        gridbagconstraints.weightx = 0.0D;
	        JLabel jlabel = new JLabel(IUFOServiceDialog.getMsg("label.psname"), 11);
	        jlabel.setLabelFor(cbName);
	        IUFOServiceDialog.addToGB(jlabel, this, gridbaglayout, gridbagconstraints);
	        gridbagconstraints.weightx = 1.0D;
	        gridbagconstraints.gridwidth = -1;
	        IUFOServiceDialog.addToGB(cbName, this, gridbaglayout, gridbagconstraints);
	        gridbagconstraints.weightx = 0.0D;
	        gridbagconstraints.gridwidth = 0;
	        btnProperties = IUFOServiceDialog.createButton(getMsg("button.properties"), this);
	        IUFOServiceDialog.addToGB(btnProperties, this, gridbaglayout, gridbagconstraints);
	        gridbagconstraints.weighty = 1.0D;
	        lblStatus = addLabel(IUFOServiceDialog.getMsg("label.status"), gridbaglayout, gridbagconstraints);
	        lblStatus.setLabelFor(null);
	        lblType = addLabel(IUFOServiceDialog.getMsg("label.pstype"), gridbaglayout, gridbagconstraints);
	        lblType.setLabelFor(null);
	        gridbagconstraints.gridwidth = 1;
	        IUFOServiceDialog.addToGB(new JLabel(IUFOServiceDialog.getMsg("label.info"), 11), this, gridbaglayout, gridbagconstraints);
	        gridbagconstraints.gridwidth = -1;
	        lblInfo = new JLabel();
	        lblInfo.setForeground(Color.black);
	        lblInfo.setLabelFor(null);
	        IUFOServiceDialog.addToGB(lblInfo, this, gridbaglayout, gridbagconstraints);
	        gridbagconstraints.gridwidth = 0;
	        cbPrintToFile = IUFOServiceDialog.createCheckBox("checkbox.printtofile", this);
	        IUFOServiceDialog.addToGB(cbPrintToFile, this, gridbaglayout, gridbagconstraints);
	    }
    public boolean isPrintToFileSelected()
    {
        return cbPrintToFile.isSelected();
    }

    private JLabel addLabel(String s, GridBagLayout gridbaglayout, GridBagConstraints gridbagconstraints)
    {
        gridbagconstraints.gridwidth = 1;
        IUFOServiceDialog.addToGB(new JLabel(s, 11), this, gridbaglayout, gridbagconstraints);
        gridbagconstraints.gridwidth = 0;
        JLabel jlabel = new JLabel();
        jlabel.setForeground(Color.black);
        IUFOServiceDialog.addToGB(jlabel, this, gridbaglayout, gridbagconstraints);
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
                    Destination destination = (Destination)asOriginal.get(Destination.class);
                    if(destination != null)
                        asCurrent.add(destination);
                    else
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
            lblStatus.setText(IUFOServiceDialog.getMsg(printserviceattribute1.toString()));
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
		private final String strTitle = IUFOServiceDialog.getMsg("border.printrange");
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
	        gridbagconstraints.insets = IUFOServiceDialog.compInsets;
	        gridbagconstraints.gridwidth = 0;
	        ButtonGroup buttongroup = new ButtonGroup();
	        JPanel jpanel = new JPanel(new FlowLayout(3));
	        rbAll = IUFOServiceDialog.createRadioButton(getMsg("radiobutton.rangeall"), this);
	        rbAll.setSelected(true);
	        buttongroup.add(rbAll);
	        jpanel.add(rbAll);
	        IUFOServiceDialog.addToGB(jpanel, this, gridbaglayout, gridbagconstraints);
	        JPanel jpanel1 = new JPanel(new FlowLayout(3));
	        rbPages = IUFOServiceDialog.createRadioButton(getMsg("radiobutton.rangepages"), this);
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
	        tfRangeFrom.getAccessibleContext().setAccessibleName(IUFOServiceDialog.getMsg("radiobutton.rangepages"));
	        jpanel1.add(tfRangeFrom);
	        lblRangeTo = new JLabel(IUFOServiceDialog.getMsg("label.rangeto"));
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
	        tfRangeTo.getAccessibleContext().setAccessibleName(IUFOServiceDialog.getMsg("label.rangeto"));
	        jpanel1.add(tfRangeTo);
	        IUFOServiceDialog.addToGB(jpanel1, this, gridbaglayout, gridbagconstraints);
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
		private final String strTitle = IUFOServiceDialog.getMsg("border.copies");
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
	        gridbagconstraints.insets = IUFOServiceDialog.compInsets;
	        lblCopies = new JLabel(IUFOServiceDialog.getMsg("label.numcopies"), 11);
	        lblCopies.getAccessibleContext().setAccessibleName(IUFOServiceDialog.getMsg("label.numcopies"));
	        IUFOServiceDialog.addToGB(lblCopies, this, gridbaglayout, gridbagconstraints);
	        snModel = new SpinnerNumberModel(1, 1, 999, 1);
	        spinCopies = new JSpinner(snModel);
	        lblCopies.setLabelFor(spinCopies);
	        ((javax.swing.JSpinner.NumberEditor)spinCopies.getEditor()).getTextField().setColumns(3);
	        spinCopies.addChangeListener(this);
	        gridbagconstraints.gridwidth = 0;
	        IUFOServiceDialog.addToGB(spinCopies, this, gridbaglayout, gridbagconstraints);
	        cbCollate = IUFOServiceDialog.createCheckBox("checkbox.collate", this);
	        cbCollate.setEnabled(false);
	        IUFOServiceDialog.addToGB(cbCollate, this, gridbaglayout, gridbagconstraints);
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

	/**
	 * 页面设置,与PrintSet中的PageFormat对应,PageFormat的Paper由MediaPanel和MarginsPanel对应设置
	 * @author guogang
	 *
	 */
	private class PageSetupPanel extends JPanel
    {
		private MediaPanel pnlMedia;
        private OrientationPanel pnlOrientation;
        private MarginsPanel pnlMargins;
        private PrintAlignPanel pnlPrintAlign;
        
        public PageSetupPanel()
        {
            super();
            GridBagLayout gridbaglayout = new GridBagLayout();
            GridBagConstraints gridbagconstraints = new GridBagConstraints();
            setLayout(gridbaglayout);
            gridbagconstraints.fill = 1;
            gridbagconstraints.insets = IUFOServiceDialog.panelInsets;
            gridbagconstraints.weightx = 1.0D;
            gridbagconstraints.weighty = 1.0D;
            gridbagconstraints.gridwidth = 0;
            pnlMedia = new MediaPanel();
            IUFOServiceDialog.addToGB(pnlMedia, this, gridbaglayout, gridbagconstraints);
            pnlOrientation = new OrientationPanel();
            gridbagconstraints.gridwidth = -1;
            gridbagconstraints.gridheight=2;
            IUFOServiceDialog.addToGB(pnlOrientation, this, gridbaglayout, gridbagconstraints);
            pnlMargins = new MarginsPanel();
            pnlOrientation.addOrientationListener(pnlMargins);
            pnlMedia.addMediaListener(pnlMargins);
            gridbagconstraints.gridwidth = 0;
            gridbagconstraints.gridheight=1;
            IUFOServiceDialog.addToGB(pnlMargins, this, gridbaglayout, gridbagconstraints);
            pnlPrintAlign=new PrintAlignPanel();
            IUFOServiceDialog.addToGB(pnlPrintAlign, this, gridbaglayout, gridbagconstraints);
        }
        public void updateInfo()
        {
            pnlMedia.updateInfo();
            pnlOrientation.updateInfo();
            pnlMargins.updateInfo();
            pnlPrintAlign.updateInfo();
        }

    }
	/**
	 * 页面设置的方向设置
	 * @author guogang
	 *
	 */
	private class OrientationPanel extends JPanel implements ActionListener
     {
		private final String strTitle = IUFOServiceDialog.getMsg("border.orientation");
	    /** 纵向*/
		private IconRadioButton rbPortrait;
		/** 横向*/
	    private IconRadioButton rbLandscape;
	    /** 纵向反面打印*/
	    private IconRadioButton rbRevPortrait;
	    /** 横向反面打印*/
	    private IconRadioButton rbRevLandscape;
	    private MarginsPanel pnlMargins;

	    public OrientationPanel()
	    {
	        super();
	        pnlMargins = null;
	        GridBagLayout gridbaglayout = new GridBagLayout();
	        GridBagConstraints gridbagconstraints = new GridBagConstraints();
	        setLayout(gridbaglayout);
	        setBorder(BorderFactory.createTitledBorder(strTitle));
	        gridbagconstraints.fill = 1;
	        gridbagconstraints.insets = IUFOServiceDialog.compInsets;
	        gridbagconstraints.weighty = 1.0D;
	        gridbagconstraints.gridwidth = 0;
	        ButtonGroup buttongroup = new ButtonGroup();
	        rbPortrait = new IconRadioButton(getMsg("radiobutton.portrait"), "orientPortrait.gif", true, buttongroup, this);
	        rbPortrait.addActionListener(this);
	        IUFOServiceDialog.addToGB(rbPortrait, this, gridbaglayout, gridbagconstraints);
	        rbLandscape = new IconRadioButton(getMsg("radiobutton.landscape"), "orientLandscape.gif", false, buttongroup, this);
	        rbLandscape.addActionListener(this);
	        IUFOServiceDialog.addToGB(rbLandscape, this, gridbaglayout, gridbagconstraints);
	        rbRevPortrait = new IconRadioButton(getMsg("radiobutton.revportrait"), "orientRevPortrait.gif", false, buttongroup, this);
	        rbRevPortrait.addActionListener(this);
	        IUFOServiceDialog.addToGB(rbRevPortrait, this, gridbaglayout, gridbagconstraints);
	        rbRevLandscape = new IconRadioButton(getMsg("radiobutton.revlandscape"), "orientRevLandscape.gif", false, buttongroup, this);
	        rbRevLandscape.addActionListener(this);
	        IUFOServiceDialog.addToGB(rbRevLandscape, this, gridbaglayout, gridbagconstraints);
	    }
    public void actionPerformed(ActionEvent actionevent)
    {
        Object obj = actionevent.getSource();
        if(rbPortrait.isSameAs(obj)){
        	if(isIndividual){
        		psetCurrent.getPageformat().setOrientation(PageFormat.PORTRAIT) ;  
        	}else{
        		asCurrent.add(OrientationRequested.PORTRAIT);
        	}
        
        }
        else
        if(rbLandscape.isSameAs(obj)){
        	if(isIndividual){
        		psetCurrent.getPageformat().setOrientation(PageFormat.LANDSCAPE) ;
        	}else{
        		asCurrent.add(OrientationRequested.LANDSCAPE);
        	}
                 
        }
        else
        if(rbRevPortrait.isSameAs(obj)){
        	if(isIndividual){
        		psetCurrent.getPageformat().setOrientation(PageFormat.PORTRAIT) ; 
        	}else{
        		 asCurrent.add(OrientationRequested.REVERSE_PORTRAIT);
        	}
             
        }
        else
        if(rbRevLandscape.isSameAs(obj)){
        	if(isIndividual){
        		psetCurrent.getPageformat().setOrientation(PageFormat.REVERSE_LANDSCAPE) ;
        	}else{
        		 asCurrent.add(OrientationRequested.REVERSE_LANDSCAPE);
        	}
                
        }
        if(pnlMargins != null)
            pnlMargins.updateInfo();
    }

    void addOrientationListener(MarginsPanel marginspanel)
    {
        pnlMargins = marginspanel;
    }

    public void updateInfo()
    {
        boolean flag = false;
        boolean flag1 = false;
        boolean flag2 = false;
        boolean flag3 = false;
        if(isAWT)
        {
            flag = true;
            flag1 = true;
        } else
        if(psCurrent.isAttributeCategorySupported(OrientationRequested.class))
        {
            Object obj = psCurrent.getSupportedAttributeValues(OrientationRequested.class, docFlavor, asCurrent);
            if(obj instanceof OrientationRequested[])
            {
                OrientationRequested aorientationrequested[] = (OrientationRequested[])(OrientationRequested[])obj;
                for(int i = 0; i < aorientationrequested.length; i++)
                {
                    OrientationRequested orientationrequested1 = aorientationrequested[i];
                    if(orientationrequested1 == OrientationRequested.PORTRAIT)
                    {
                        flag = true;
                        continue;
                    }
                    if(orientationrequested1 == OrientationRequested.LANDSCAPE)
                    {
                        flag1 = true;
                        continue;
                    }
                    if(orientationrequested1 == OrientationRequested.REVERSE_PORTRAIT)
                    {
                        flag2 = true;
                        continue;
                    }
                    if(orientationrequested1 == OrientationRequested.REVERSE_LANDSCAPE)
                        flag3 = true;
                }

            }
        }
        rbPortrait.setEnabled(flag);
        rbLandscape.setEnabled(flag1);
        rbRevPortrait.setEnabled(flag2);
        rbRevLandscape.setEnabled(flag3);
        OrientationRequested orientationrequested = (OrientationRequested)asCurrent.get(OrientationRequested.class);
        //从PrintSet中获取打印方向
        if(psetOriginal!=null){
        	PageFormat pgf=psetOriginal.getPageformat();
        	if(pgf.getOrientation()==PageFormat.PORTRAIT)
        		orientationrequested=OrientationRequested.PORTRAIT;
        	if(pgf.getOrientation()==PageFormat.LANDSCAPE)
        		orientationrequested=OrientationRequested.LANDSCAPE;
        	if(pgf.getOrientation()==PageFormat.REVERSE_LANDSCAPE)
        		orientationrequested=OrientationRequested.REVERSE_LANDSCAPE;
        }
        if(orientationrequested == null || !psCurrent.isAttributeValueSupported(orientationrequested, docFlavor, asCurrent))
        {
            orientationrequested = (OrientationRequested)psCurrent.getDefaultAttributeValue(OrientationRequested.class);
            if(!psCurrent.isAttributeValueSupported(orientationrequested, docFlavor, asCurrent))
            {
                orientationrequested = null;
                Object obj1 = psCurrent.getSupportedAttributeValues(OrientationRequested.class, docFlavor, asCurrent);
                if(obj1 instanceof OrientationRequested[])
                {
                    OrientationRequested aorientationrequested1[] = (OrientationRequested[])(OrientationRequested[])obj1;
                    if(aorientationrequested1.length > 1)
                        orientationrequested = aorientationrequested1[0];
                }
            }
            if(orientationrequested == null)
                orientationrequested = OrientationRequested.PORTRAIT;
            asCurrent.add(orientationrequested);
        }
        if(orientationrequested == OrientationRequested.PORTRAIT)
            rbPortrait.setSelected(true);
        else
        if(orientationrequested == OrientationRequested.LANDSCAPE)
            rbLandscape.setSelected(true);
        else
        if(orientationrequested == OrientationRequested.REVERSE_PORTRAIT)
            rbRevPortrait.setSelected(true);
        else
            rbRevLandscape.setSelected(true);
    }

    
}
	/**
	 * 页面设置的边距设置
	 * @author guogang
	 *
	 */
	private class MarginsPanel extends JPanel implements ActionListener,
			FocusListener {
		private final String strTitle = IUFOServiceDialog
				.getMsg("border.margins");
		/** 左边距输入框 */
		private JFormattedTextField leftMargin;
		/** 右边距输入框 */
		private JFormattedTextField rightMargin;
		/** 上边距输入框 */
		private JFormattedTextField topMargin;
		/** 下边距输入框 */
		private JFormattedTextField bottomMargin;
		private JLabel lblLeft;
		private JLabel lblRight;
		private JLabel lblTop;
		private JLabel lblBottom;
		private float lmVal;
		private float rmVal;
		private float tmVal;
		private float bmVal;
		private Float lmObj;
		private Float rmObj;
		private Float tmObj;
		private Float bmObj;

		public MarginsPanel() {
			super();
			
			lmVal = -1F;
			rmVal = -1F;
			tmVal = -1F;
			bmVal = -1F;
			GridBagLayout gridbaglayout = new GridBagLayout();
			GridBagConstraints gridbagconstraints = new GridBagConstraints();
			gridbagconstraints.fill = 2;
			gridbagconstraints.weightx = 1.0D;
			gridbagconstraints.weighty = 0.0D;
			gridbagconstraints.insets = IUFOServiceDialog.compInsets;
			setLayout(gridbaglayout);
			setBorder(BorderFactory.createTitledBorder(strTitle));
			String s = "label.millimetres";
			if(units == Size2DSyntax.INCH)
			   s = "label.inches";
			
			String s2 = IUFOServiceDialog.getMsg(s);
			DecimalFormat decimalformat;
			if (units == Size2DSyntax.MM) {
				decimalformat = new DecimalFormat("###.##");
				decimalformat.setMaximumIntegerDigits(3);
			} else {
				decimalformat = new DecimalFormat("##.##");
				decimalformat.setMaximumIntegerDigits(2);
			}
			decimalformat.setMinimumFractionDigits(1);
			decimalformat.setMaximumFractionDigits(2);
			decimalformat.setMinimumIntegerDigits(1);
			decimalformat.setParseIntegerOnly(false);
			decimalformat.setDecimalSeparatorAlwaysShown(true);
			NumberFormatter numberformatter = new NumberFormatter(decimalformat);
			numberformatter.setMinimum(new Float(0.0F));
			numberformatter.setMaximum(new Float(999F));
			numberformatter.setAllowsInvalid(true);
			numberformatter.setCommitsOnValidEdit(true);
			leftMargin = new JFormattedTextField(numberformatter);
			leftMargin.addFocusListener(this);
			leftMargin.addActionListener(this);
			leftMargin.getAccessibleContext().setAccessibleName(
					IUFOServiceDialog.getMsg("label.leftmargin"));
			rightMargin = new JFormattedTextField(numberformatter);
			rightMargin.addFocusListener(this);
			rightMargin.addActionListener(this);
			rightMargin.getAccessibleContext().setAccessibleName(
					IUFOServiceDialog.getMsg("label.rightmargin"));
			topMargin = new JFormattedTextField(numberformatter);
			topMargin.addFocusListener(this);
			topMargin.addActionListener(this);
			topMargin.getAccessibleContext().setAccessibleName(
					IUFOServiceDialog.getMsg("label.topmargin"));
			topMargin = new JFormattedTextField(numberformatter);
			bottomMargin = new JFormattedTextField(numberformatter);
			bottomMargin.addFocusListener(this);
			bottomMargin.addActionListener(this);
			bottomMargin.getAccessibleContext().setAccessibleName(
					IUFOServiceDialog.getMsg("label.bottommargin"));
			topMargin = new JFormattedTextField(numberformatter);
			gridbagconstraints.gridwidth = -1;
			lblLeft = new JLabel((new StringBuilder()).append(
					IUFOServiceDialog.getMsg("label.leftmargin")).append(" ")
					.append(s2).toString(), 10);
			// lblLeft.setDisplayedMnemonic(IUFOServiceDialog
			// .getMnemonic("label.leftmargin"));
			lblLeft.setLabelFor(leftMargin);
			IUFOServiceDialog.addToGB(lblLeft, this, gridbaglayout,
					gridbagconstraints);
			gridbagconstraints.gridwidth = 0;
			lblRight = new JLabel((new StringBuilder()).append(
					IUFOServiceDialog.getMsg("label.rightmargin")).append(" ")
					.append(s2).toString(), 10);
			lblRight.setLabelFor(rightMargin);
			IUFOServiceDialog.addToGB(lblRight, this, gridbaglayout,
					gridbagconstraints);
			gridbagconstraints.gridwidth = -1;
			IUFOServiceDialog.addToGB(leftMargin, this, gridbaglayout,
					gridbagconstraints);
			gridbagconstraints.gridwidth = 0;
			IUFOServiceDialog.addToGB(rightMargin, this, gridbaglayout,
					gridbagconstraints);
			IUFOServiceDialog.addToGB(new JPanel(), this, gridbaglayout,
					gridbagconstraints);
			gridbagconstraints.gridwidth = -1;
			lblTop = new JLabel((new StringBuilder()).append(
					IUFOServiceDialog.getMsg("label.topmargin")).append(" ")
					.append(s2).toString(), 10);
			lblTop.setLabelFor(topMargin);
			IUFOServiceDialog.addToGB(lblTop, this, gridbaglayout,
					gridbagconstraints);
			gridbagconstraints.gridwidth = 0;
			lblBottom = new JLabel((new StringBuilder()).append(
					IUFOServiceDialog.getMsg("label.bottommargin")).append(" ")
					.append(s2).toString(), 10);
			lblBottom.setLabelFor(bottomMargin);
			IUFOServiceDialog.addToGB(lblBottom, this, gridbaglayout,
					gridbagconstraints);
			gridbagconstraints.gridwidth = -1;
			IUFOServiceDialog.addToGB(topMargin, this, gridbaglayout,
					gridbagconstraints);
			gridbagconstraints.gridwidth = 0;
			IUFOServiceDialog.addToGB(bottomMargin, this, gridbaglayout,
					gridbagconstraints);
		}

		public void actionPerformed(ActionEvent actionevent) {
			Object obj = actionevent.getSource();
			updateMargins(obj);
		}

		public void focusLost(FocusEvent focusevent) {
			Object obj = focusevent.getSource();
			updateMargins(obj);
		}

		public void focusGained(FocusEvent focusevent) {
		}

		public void updateMargins(Object obj) {
			if (!(obj instanceof JFormattedTextField))
				return;
			Object obj1 = (JFormattedTextField) obj;
			Float float1 = (Float) ((JFormattedTextField) (obj1)).getValue();
			if (float1 == null)
				return;
			if (obj1 == leftMargin && float1.equals(lmObj))
				return;
			if (obj1 == rightMargin && float1.equals(rmObj))
				return;
			if (obj1 == topMargin && float1.equals(tmObj))
				return;
			if (obj1 == bottomMargin && float1.equals(bmObj))
				return;
			obj1 = (Float) leftMargin.getValue();
			float1 = (Float) rightMargin.getValue();
			Float float2 = (Float) topMargin.getValue();
			Float float3 = (Float) bottomMargin.getValue();
			float f = ((Float) (obj1)).floatValue();
			float f1 = float1.floatValue();
			float f2 = float2.floatValue();
			float f3 = float3.floatValue();
			OrientationRequested orientationrequested = (OrientationRequested) asCurrent
					.get(OrientationRequested.class);
			if (orientationrequested == null)
				orientationrequested = (OrientationRequested) psCurrent
						.getDefaultAttributeValue(OrientationRequested.class);
			if (orientationrequested == OrientationRequested.REVERSE_PORTRAIT) {
				float f4 = f;
				f = f1;
				f1 = f4;
				f4 = f2;
				f2 = f3;
				f3 = f4;
			} else if (orientationrequested == OrientationRequested.LANDSCAPE) {
				float f5 = f;
				f = f2;
				f2 = f1;
				f1 = f3;
				f3 = f5;
			} else if (orientationrequested == OrientationRequested.REVERSE_LANDSCAPE) {
				float f6 = f;
				f = f3;
				f3 = f1;
				f1 = f2;
				f2 = f6;
			}
			MediaPrintableArea mediaprintablearea;
			if ((mediaprintablearea = validateMargins(f, f1, f2, f3)) != null) {
				if(isIndividual){
					setPaperFromMedia(psetCurrent.getPageformat().getPaper(),mediaprintablearea);
				}else{
					asCurrent.add(mediaprintablearea);
				}
				lmVal = f;
				rmVal = f1;
				tmVal = f2;
				bmVal = f3;
				lmObj = ((Float) (obj1));
				rmObj = float1;
				tmObj = float2;
				bmObj = float3;
			} else {
				if (lmObj == null || rmObj == null || tmObj == null
						|| rmObj == null)
					return;
				leftMargin.setValue(lmObj);
				rightMargin.setValue(rmObj);
				topMargin.setValue(tmObj);
				bottomMargin.setValue(bmObj);
			}
		}

		private MediaPrintableArea validateMargins(float f, float f1, float f2,
				float f3) {
			MediaPrintableArea mediaprintablearea = null;
			MediaSize mediasize = null;
			Media media = (Media) asCurrent.get(Media.class);
			if (media == null || !(media instanceof MediaSizeName))
				media = (Media) psCurrent.getDefaultAttributeValue(Media.class);
			if (media != null && (media instanceof MediaSizeName)) {
				MediaSizeName mediasizename = (MediaSizeName) media;
				mediasize = MediaSize.getMediaSizeForName(mediasizename);
			}
			if (mediasize == null)
				mediasize = new MediaSize(8.5F, 11F, Size2DSyntax.INCH);
			if (media != null) {
				HashPrintRequestAttributeSet hashprintrequestattributeset = new HashPrintRequestAttributeSet(
						asCurrent);
				hashprintrequestattributeset.add(media);
				Object obj = psCurrent.getSupportedAttributeValues(
						MediaPrintableArea.class, docFlavor,
						hashprintrequestattributeset);
				if ((obj instanceof MediaPrintableArea[])
						&& ((MediaPrintableArea[]) (MediaPrintableArea[]) obj).length > 0)
					mediaprintablearea = ((MediaPrintableArea[]) (MediaPrintableArea[]) obj)[0];
			}
			if (mediaprintablearea == null)
				mediaprintablearea = new MediaPrintableArea(0.0F, 0.0F,
						mediasize.getX(units), mediasize.getY(units), units);
			float f4 = mediasize.getX(units);
			float f5 = mediasize.getY(units);
			float f6 = f;
			float f7 = f2;
			float f8 = f4 - f - f1;
			float f9 = f5 - f2 - f3;
			if (f8 <= 0.0F || f9 <= 0.0F || f6 < 0.0F || f7 < 0.0F
					|| f6 < mediaprintablearea.getX(units)
					|| f8 > mediaprintablearea.getWidth(units)
					|| f7 < mediaprintablearea.getY(units)
					|| f9 > mediaprintablearea.getHeight(units))
				return null;
			else
				return new MediaPrintableArea(f, f2, f8, f9, units);
		}

		public void updateInfo() {
			if (isAWT) {
				leftMargin.setEnabled(false);
				rightMargin.setEnabled(false);
				topMargin.setEnabled(false);
				bottomMargin.setEnabled(false);
				lblLeft.setEnabled(false);
				lblRight.setEnabled(false);
				lblTop.setEnabled(false);
				lblBottom.setEnabled(false);
				return;
			}
			
			MediaPrintableArea mediaprintablearea = (MediaPrintableArea) asCurrent
					.get(MediaPrintableArea.class);
			//打印服务获取的MediaPrintableArea
			MediaPrintableArea mediaprintablearea2 = null;
			MediaSize mediasize = null;
			
			Media media = (Media) asCurrent.get(Media.class);
			//从老版本的PrintSet中获取纸张属性
			if(psetOriginal!=null){
				Paper pp=psetOriginal.getPageformat().getPaper();
				media=(Media)MediaSize.findMedia((float)(pp.getWidth()/d),(float)(pp.getHeight()/d),Size2DSyntax.INCH);
				mediaprintablearea2 = new MediaPrintableArea((float)(pp.getImageableX()/d),(float)(pp.getImageableY()/d),(float)(pp.getImageableWidth()/d),(float)(pp.getImageableHeight()/d),Size2DSyntax.INCH);
			}
			
			if (media == null || !(media instanceof MediaSizeName))
				media = (Media) psCurrent.getDefaultAttributeValue(Media.class);
			if (media != null && (media instanceof MediaSizeName)) {
				MediaSizeName mediasizename = (MediaSizeName) media;
				mediasize = MediaSize.getMediaSizeForName(mediasizename);
			}
			if (mediasize == null)
				mediasize = new MediaSize(8.5F, 11F, Size2DSyntax.INCH);
			//从打印服务获取MediaPrintableArea
//			if (media != null) {
//				HashPrintRequestAttributeSet hashprintrequestattributeset = new HashPrintRequestAttributeSet(
//						asCurrent);
//				hashprintrequestattributeset.add(media);
//				Object obj = psCurrent.getSupportedAttributeValues(
//						MediaPrintableArea.class, docFlavor,
//						hashprintrequestattributeset);
//				if ((obj instanceof MediaPrintableArea[])
//						&& ((MediaPrintableArea[]) (MediaPrintableArea[]) obj).length > 0)
//					mediaprintablearea2 = ((MediaPrintableArea[]) (MediaPrintableArea[]) obj)[0];
//				else if (obj instanceof MediaPrintableArea)
//					mediaprintablearea2 = (MediaPrintableArea) obj;
//			}
			
			//从PrintSet中获取可打印
			if (mediaprintablearea2 == null)
				mediaprintablearea2 = new MediaPrintableArea(0.0F, 0.0F,
						mediasize.getX(units), mediasize.getY(units), units);
			
			float f = mediasize.getX(Size2DSyntax.INCH);
			float f1 = mediasize.getY(Size2DSyntax.INCH);
			float f2 = 5F;
			float f3;
			if (f > f2)
				f3 = 1.0F;
			else
				f3 = f / f2;
			float f4;
			if (f1 > f2)
				f4 = 1.0F;
			else
				f4 = f1 / f2;
			if (mediaprintablearea == null) {
				mediaprintablearea = new MediaPrintableArea(f3, f4, f - 2.0F
						* f3, f1 - 2.0F * f4, Size2DSyntax.INCH);
				asCurrent.add(mediaprintablearea);
			}
			float f5 = mediaprintablearea.getX(units);
			float f6 = mediaprintablearea.getY(units);
			float f7 = mediaprintablearea.getWidth(units);
			float f8 = mediaprintablearea.getHeight(units);
			float f9 = mediaprintablearea2.getX(units);
			float f10 = mediaprintablearea2.getY(units);
			float f11 = mediaprintablearea2.getWidth(units);
			float f12 = mediaprintablearea2.getHeight(units);
			boolean flag = false;
			f = mediasize.getX(units);
			f1 = mediasize.getY(units);
			if (lmVal >= 0.0F) {
				flag = true;
				if (lmVal + rmVal > f) {
					if (f7 > f11)
						f7 = f11;
					f5 = (f - f7) / 2.0F;
				} else {
					f5 = lmVal < f9 ? f9 : lmVal;
					f7 = f - f5 - rmVal;
				}
				if (tmVal + bmVal > f1) {
					if (f8 > f12)
						f8 = f12;
					f6 = (f1 - f8) / 2.0F;
				} else {
					f6 = tmVal < f10 ? f10 : tmVal;
					f8 = f1 - f6 - bmVal;
				}
			}
			if (f5 < f9) {
				flag = true;
				f5 = f9;
			}
			if (f6 < f10) {
				flag = true;
				f6 = f10;
			}
			if (f7 > f11) {
				flag = true;
				f7 = f11;
			}
			if (f8 > f12) {
				flag = true;
				f8 = f12;
			}
			if (f5 + f7 > f9 + f11 || f7 <= 0.0F) {
				flag = true;
				f5 = f9;
				f7 = f11;
			}
			if (f6 + f8 > f10 + f12 || f8 <= 0.0F) {
				flag = true;
				f6 = f10;
				f8 = f12;
			}
			if (flag) {
				MediaPrintableArea mediaprintablearea1 = new MediaPrintableArea(
						f5, f6, f7, f8, units);
				asCurrent.add(mediaprintablearea1);
			}
			lmVal = f5;
			tmVal = f6;
			rmVal = mediasize.getX(units) - f5 - f7;
			bmVal = mediasize.getY(units) - f6 - f8;
			lmObj = new Float(lmVal);
			rmObj = new Float(rmVal);
			tmObj = new Float(tmVal);
			bmObj = new Float(bmVal);
			OrientationRequested orientationrequested = (OrientationRequested) asCurrent
					.get(OrientationRequested.class);
			if (orientationrequested == null)
				orientationrequested = (OrientationRequested) psCurrent
						.getDefaultAttributeValue(OrientationRequested.class);
			if (orientationrequested == OrientationRequested.REVERSE_PORTRAIT) {
				Float float1 = lmObj;
				lmObj = rmObj;
				rmObj = float1;
				float1 = tmObj;
				tmObj = bmObj;
				bmObj = float1;
			} else if (orientationrequested == OrientationRequested.LANDSCAPE) {
				Float float2 = lmObj;
				lmObj = bmObj;
				bmObj = rmObj;
				rmObj = tmObj;
				tmObj = float2;
			} else if (orientationrequested == OrientationRequested.REVERSE_LANDSCAPE) {
				Float float3 = lmObj;
				lmObj = tmObj;
				tmObj = rmObj;
				rmObj = bmObj;
				bmObj = float3;
			}
			leftMargin.setValue(lmObj);
			rightMargin.setValue(rmObj);
			topMargin.setValue(tmObj);
			bottomMargin.setValue(bmObj);
		}

	}
	
	/**
	 * 页面设置的媒体设置
	 * 
	 * @author guogang
	 * 
	 */
	private class MediaPanel extends JPanel implements ItemListener
       {
		private final String strTitle = IUFOServiceDialog.getMsg("border.media");
		/** 媒体大小标签*/
		private JLabel lblSize;
		/** 媒体来源标签*/
		private JLabel lblSource;
		/** 媒体大小下拉框*/
		private JComboBox cbSize;
		/** 媒体来源下拉框*/
		private JComboBox cbSource;
		/** 媒体大小数组*/
		private Vector sizes;
		/** 媒体来源数组*/
		private Vector sources;
		private MarginsPanel pnlMargins;

		public MediaPanel() {
			super();
			sizes = new Vector();
			sources = new Vector();
			pnlMargins = null;
			GridBagLayout gridbaglayout = new GridBagLayout();
			GridBagConstraints gridbagconstraints = new GridBagConstraints();
			setLayout(gridbaglayout);
			setBorder(BorderFactory.createTitledBorder(strTitle));
			cbSize = new JComboBox();
			cbSource = new JComboBox();
			gridbagconstraints.fill = GridBagConstraints.HORIZONTAL;
			gridbagconstraints.insets = IUFOServiceDialog.compInsets;
			gridbagconstraints.weighty = 1.0D;
			gridbagconstraints.weightx = 0.0D;
			lblSize = new JLabel(IUFOServiceDialog.getMsg("label.size"), 11);
			lblSize.setLabelFor(cbSize);
			IUFOServiceDialog.addToGB(lblSize, this, gridbaglayout,
					gridbagconstraints);
			gridbagconstraints.weightx = 1.0D;
			gridbagconstraints.gridwidth = 0;
			IUFOServiceDialog.addToGB(cbSize, this, gridbaglayout,
					gridbagconstraints);
			gridbagconstraints.weightx = 0.0D;
			gridbagconstraints.gridwidth = 1;
			lblSource = new JLabel(IUFOServiceDialog.getMsg("label.source"), 11);
			lblSource.setLabelFor(cbSource);
			IUFOServiceDialog.addToGB(lblSource, this, gridbaglayout,
					gridbagconstraints);
			gridbagconstraints.gridwidth = 0;
			IUFOServiceDialog.addToGB(cbSource, this, gridbaglayout,
					gridbagconstraints);
		}

		private String getMediaName(String s) {
			try {
				String s1 = s.replace(' ', '-');
				s1 = s1.replace('#', 'n');
				return IUFOServiceDialog.uimessage.getString(s1);
			} catch (MissingResourceException missingresourceexception) {
				return s;
			}
		}
        //同时更改PrintReqeustAttribute和PrintSet
		public void itemStateChanged(ItemEvent itemevent) {
			Object obj = itemevent.getSource();
			if (itemevent.getStateChange() == 1) {
				if (obj == cbSize) {
					int i = cbSize.getSelectedIndex();
					if (i >= 0 && i < sizes.size()) {
						if (cbSource.getItemCount() > 1
								&& cbSource.getSelectedIndex() >= 1) {
							int k = cbSource.getSelectedIndex() - 1;
							MediaTray mediatray = (MediaTray) sources.get(k);
							asCurrent.add(new SunAlternateMedia(mediatray));
						}
						if (isIndividual) {
							psetCurrent.getPageformat().setPaper(
									getPaperFromMedia((MediaSizeName) sizes
											.get(i)));
						} else {
							asCurrent.add((MediaSizeName) sizes.get(i));
						}
					}
				} else if (obj == cbSource) {
					int j = cbSource.getSelectedIndex();
					if (j >= 1 && j < sources.size() + 1) {
						asCurrent.remove(SunAlternateMedia.class);
						asCurrent.add((MediaTray) sources.get(j - 1));
					} else if (j == 0) {
						asCurrent.remove(SunAlternateMedia.class);
						if (cbSize.getItemCount() > 0) {
							int l = cbSize.getSelectedIndex();
							if(isIndividual){
								psetCurrent.getPageformat().setPaper(getPaperFromMedia((MediaSizeName) sizes.get(l)));
							}else{
							asCurrent.add((MediaSizeName) sizes.get(l));
							}
						}
					}
				}
				if (pnlMargins != null)
					pnlMargins.updateInfo();
			}
		}

		public void addMediaListener(MarginsPanel marginspanel) {
			pnlMargins = marginspanel;
		}

		public void updateInfo() {
			boolean flag = false;
			cbSize.removeItemListener(this);
			cbSize.removeAllItems();
			cbSource.removeItemListener(this);
			cbSource.removeAllItems();
			cbSource.addItem(getMediaName("auto-select"));
			sizes.clear();
			sources.clear();
			if (psCurrent.isAttributeCategorySupported(Media.class)) {
				flag = true;
				Object obj = psCurrent.getSupportedAttributeValues(Media.class,
						docFlavor, asCurrent);
				if (obj instanceof Media[]) {
					Media amedia[] = (Media[]) (Media[]) obj;
					for (int i = 0; i < amedia.length; i++) {
						Media media1 = amedia[i];
						if (media1 instanceof MediaSizeName) {
							sizes.add(media1);
							cbSize.addItem(getMediaName(media1.toString()));
							continue;
						}
						if (media1 instanceof MediaTray) {
							sources.add(media1);
							cbSource.addItem(getMediaName(media1.toString()));
						}
					}

				}
			}
			boolean flag1 = flag && sizes.size() > 0;
			lblSize.setEnabled(flag1);
			cbSize.setEnabled(flag1);
			if (isAWT) {
				cbSource.setEnabled(false);
				lblSource.setEnabled(false);
			} else {
				cbSource.setEnabled(flag);
			}
			if (flag) {
				//从打印的公共请求中获取纸张属性
				Media media = (Media) asCurrent.get(Media.class);
				//从老版本的PrintSet中获取纸张属性
				if(media==null&&psetOriginal!=null){
					Paper pp=psetOriginal.getPageformat().getPaper();
					media=(Media)MediaSize.findMedia((float)(pp.getWidth()/d),(float)(pp.getHeight()/d),Size2DSyntax.INCH);
					
				}
				//如果还是无法获取纸张属性，则从当前的打印服务中获取默认的纸张属性
				if (media == null
						|| !psCurrent.isAttributeValueSupported(media,
								docFlavor, asCurrent)) {
					media = (Media) psCurrent
							.getDefaultAttributeValue(Media.class);
					if (media == null && sizes.size() > 0)
						media = (Media) sizes.get(0);
					if (media != null)
						asCurrent.add(media);
				}
				if (media != null) {
					if (media instanceof MediaSizeName) {
						MediaSizeName mediasizename = (MediaSizeName) media;
						cbSize.setSelectedIndex(sizes.indexOf(mediasizename));
					} else if (media instanceof MediaTray) {
						MediaTray mediatray = (MediaTray) media;
						cbSource
								.setSelectedIndex(sources.indexOf(mediatray) + 1);
					}
				} else {
					cbSize.setSelectedIndex(sizes.size() <= 0 ? -1 : 0);
					cbSource.setSelectedIndex(0);
				}
				SunAlternateMedia sunalternatemedia = (SunAlternateMedia) asCurrent
						.get(SunAlternateMedia.class);
				if (sunalternatemedia != null) {
					Media media2 = sunalternatemedia.getMedia();
					if (media2 instanceof MediaTray) {
						MediaTray mediatray1 = (MediaTray) media2;
						cbSource
								.setSelectedIndex(sources.indexOf(mediatray1) + 1);
					}
				}
				int j = cbSize.getSelectedIndex();
				if (j >= 0 && j < sizes.size())
					asCurrent.add((MediaSizeName) sizes.get(j));
				j = cbSource.getSelectedIndex();
				if (j >= 1 && j < sources.size() + 1)
					asCurrent.add((MediaTray) sources.get(j - 1));
			}
			cbSize.addItemListener(this);
			cbSource.addItemListener(this);
		}

	}
	/**
	 * IUFO个性化的打印设置,依赖构造方法中的PrintSet，只有其不为空的时候才会显示
	 * @author guogang
	 *
	 */
	private class IndividualPanel extends JPanel{
		private FixedAreaPanel pnlFixedArea;
		private PrintRankPanel pnlPrintRank;
		private PrintScalePanel pnlPrintScale;
		private PrintAreaPanel pnlPrintArea;
		
		public IndividualPanel(){
			super();
			GridBagLayout gridbaglayout = new GridBagLayout();
            GridBagConstraints gridbagconstraints = new GridBagConstraints();
            setLayout(gridbaglayout);
            //组件在x,y方向自动填充显示空间 
            gridbagconstraints.fill=GridBagConstraints.BOTH;
            gridbagconstraints.insets = IUFOServiceDialog.panelInsets;
            gridbagconstraints.weightx = 1.0D;
            gridbagconstraints.weighty = 1.0D;
            //组件自动添加到上个组件行的后面
            gridbagconstraints.gridwidth =GridBagConstraints.REMAINDER;
            pnlFixedArea=new FixedAreaPanel();
            IUFOServiceDialog.addToGB(pnlFixedArea, this, gridbaglayout, gridbagconstraints);
            pnlPrintRank=new PrintRankPanel();
            gridbagconstraints.gridwidth =GridBagConstraints.RELATIVE;
            gridbagconstraints.gridheight=2;
            IUFOServiceDialog.addToGB(pnlPrintRank, this, gridbaglayout, gridbagconstraints);
            pnlPrintScale=new PrintScalePanel();
            gridbagconstraints.gridwidth =GridBagConstraints.REMAINDER;
            gridbagconstraints.gridheight=1;
            IUFOServiceDialog.addToGB(pnlPrintScale, this, gridbaglayout, gridbagconstraints);
            pnlPrintArea=new PrintAreaPanel();
            IUFOServiceDialog.addToGB(pnlPrintArea, this, gridbaglayout, gridbagconstraints);
		}
		public void updateInfo(){
			pnlFixedArea.updateInfo();
			pnlPrintRank.updateInfo();
			pnlPrintScale.updateInfo();
			pnlPrintArea.updateInfo();
		}
		public boolean validateInput(){
			return pnlFixedArea.validateInput()&&pnlPrintArea.validateInput();
		}
	}
	/**
	 * IUFO个性化打印设置的固定区域设置，包括页眉/页脚设置、固定打印标题的设置
	 * @author guogang
	 *
	 */
	private class FixedAreaPanel extends JPanel implements ActionListener,FocusListener{
        private final String strTitle=MultiLang.getString("FixedArea");
        /** 页眉预览面板*/
        private JPanel headerPreviewPanel;
        /** 页脚预览面板*/
        private JPanel footerPreviewPanel;
        /** 页眉设置按钮*/
        private JButton headerSetButton;
        /** 页脚设置按钮*/
        private JButton footerSetButton;
        /** 页眉高度输入框 */
        private JFormattedTextField headerHeight;
        private JLabel lblHeader;
        /** 页脚高度输入框 */
        private JFormattedTextField footerHeight;
        private JLabel lblFooter;
        /**固定标题行起点*/
        private JTextField fixedRowHeaderFrom;
        /**固定标题行终点*/
        private JTextField fixedRowHeaderTo;
        /**固定标题列起点*/
        private JTextField fixedColHeaderFrom;
        /**固定标题列终点*/
        private JTextField fixedColHeaderTo;
        /**页眉/页脚模型*/
       private HeaderFooterModel _headFooterModel;  
        
        /**
		 * @i18n report00021=页眉预览
		 * @i18n report00022=页脚预览
		 */
        public FixedAreaPanel(){
        	super();       	
        	/** 长度单位*/
        	String s = "label.millimetres";
			if(units==Size2DSyntax.INCH)
				s = "label.inches";
			/** 长度单位名称*/
			String s2= IUFOServiceDialog.getMsg(s);
			DecimalFormat decimalformat;
			if (units ==Size2DSyntax.MM) {
				decimalformat = new DecimalFormat("###.##");
				decimalformat.setMaximumIntegerDigits(3);
			} else {
				decimalformat = new DecimalFormat("##.##");
				decimalformat.setMaximumIntegerDigits(2);
			}
			decimalformat.setMinimumFractionDigits(1);
			decimalformat.setMaximumFractionDigits(2);
			decimalformat.setMinimumIntegerDigits(1);
			decimalformat.setParseIntegerOnly(false);
			decimalformat.setDecimalSeparatorAlwaysShown(true);
			NumberFormatter numberformatter= new NumberFormatter(decimalformat);
			numberformatter.setMinimum(new Float(0.0F));
			numberformatter.setMaximum(new Float(99F));
			numberformatter.setAllowsInvalid(true);
			numberformatter.setCommitsOnValidEdit(true);
			
			
        	GridBagLayout gridbaglayout = new GridBagLayout();
			GridBagConstraints gridbagconstraints = new GridBagConstraints();
			setLayout(gridbaglayout);
			setBorder(BorderFactory.createTitledBorder(strTitle));
			gridbagconstraints.weighty = 1.0D;
			gridbagconstraints.weightx = 1.0D;
			gridbagconstraints.fill = GridBagConstraints.BOTH;
			gridbagconstraints.insets = IUFOServiceDialog.compInsets;
			gridbagconstraints.gridwidth=GridBagConstraints.REMAINDER;
			headerPreviewPanel=IUFOServiceDialog.createPanel(MultiLang.getString("report00021"));
			headerSetButton=IUFOServiceDialog.createButton(MultiLang.getString("report00027"), this);
			lblHeader= new JLabel((new StringBuilder()).append(
					MultiLang.getString("miufo00014")).append(" ")
					.append(s2).toString(), SwingConstants.RIGHT);
			headerHeight=new JFormattedTextField(numberformatter);
            JPanel headerPanel=createHeaderFooterPanel(headerPreviewPanel,headerSetButton,lblHeader,headerHeight);
            footerPreviewPanel=IUFOServiceDialog.createPanel(MultiLang.getString("report00022"));
            footerSetButton=IUFOServiceDialog.createButton(MultiLang.getString("report00028"), this);
            lblFooter= new JLabel((new StringBuilder()).append(
					MultiLang.getString("miufo00013")).append(" ")
					.append(s2).toString(), SwingConstants.RIGHT);
			footerHeight=new JFormattedTextField(numberformatter);
			JPanel footerPanel=createHeaderFooterPanel(footerPreviewPanel,footerSetButton,lblFooter,footerHeight);
			IUFOServiceDialog.addToGB(headerPanel, this, gridbaglayout, gridbagconstraints);
			IUFOServiceDialog.addToGB(footerPanel, this, gridbaglayout, gridbagconstraints);
			fixedRowHeaderFrom=new JTextField();
			fixedRowHeaderFrom.setDocument(new IntegerDocument());
			fixedRowHeaderFrom.setHorizontalAlignment(JTextField.CENTER);
			fixedRowHeaderFrom.addFocusListener(this);
			fixedRowHeaderTo=new JTextField();
			fixedRowHeaderTo.setDocument(new IntegerDocument());
			fixedRowHeaderTo.setHorizontalAlignment(JTextField.CENTER);
			fixedRowHeaderTo.addFocusListener(this);
			fixedColHeaderFrom=new JTextField();
			fixedColHeaderFrom.setDocument(new ColDocument());
			fixedColHeaderFrom.setHorizontalAlignment(JTextField.CENTER);
			fixedColHeaderFrom.addFocusListener(this);
			fixedColHeaderTo=new JTextField();
			fixedColHeaderTo.setDocument(new ColDocument());
			fixedColHeaderTo.setHorizontalAlignment(JTextField.CENTER);
			fixedColHeaderTo.addFocusListener(this);
			JPanel fixedAreaTitlePanel=createFixedAreaTitlePanel(fixedRowHeaderFrom,fixedRowHeaderTo,fixedColHeaderFrom,fixedColHeaderTo);
			IUFOServiceDialog.addToGB(fixedAreaTitlePanel, this, gridbaglayout, gridbagconstraints);
        }
        /**
         * 创建页眉/页脚设置面板
         * @param previewPanel 设置预览
         * @param setButton 设置按钮
         * @param setTitle 高度设置标题
         * @param jtextfield 高度设置输入
         * @return
         */
        private JPanel createHeaderFooterPanel(JPanel previewPanel,JButton setButton,JLabel setTitle,JTextField jtextfield){
        	GridBagLayout gridbaglayout = new GridBagLayout();
			GridBagConstraints gridbagconstraints = new GridBagConstraints();
			JPanel jpanel=new JPanel();
			jpanel.setLayout(gridbaglayout);
			gridbagconstraints.fill = GridBagConstraints.BOTH;
			gridbagconstraints.insets = IUFOServiceDialog.compInsets;
			//y方向自动拉伸,0为中间对齐
			gridbagconstraints.weighty = 1.0D;
			gridbagconstraints.weightx = 1.0D;
			gridbagconstraints.gridheight=2;
			gridbagconstraints.gridwidth=GridBagConstraints.RELATIVE;

			IUFOServiceDialog.addToGB(previewPanel, jpanel, gridbaglayout, gridbagconstraints);
			gridbagconstraints.fill = GridBagConstraints.VERTICAL;
			gridbagconstraints.gridwidth=GridBagConstraints.REMAINDER;
			gridbagconstraints.weightx = 0.0D;
			gridbagconstraints.weighty = 0.0D;
			gridbagconstraints.gridheight=1;
			IUFOServiceDialog.addToGB(setButton, jpanel, gridbaglayout, gridbagconstraints);
			gridbagconstraints.fill = GridBagConstraints.BOTH;
			gridbagconstraints.gridwidth=GridBagConstraints.REMAINDER;
			gridbagconstraints.weightx = 1.0D;
			JPanel headerLabelText=createLabelText(setTitle,jtextfield);
			IUFOServiceDialog.addToGB(headerLabelText, jpanel, gridbaglayout, gridbagconstraints);
			return jpanel;
        }
        /**
         * 页眉/页脚设置button的执行动作 
         */
    	private void defHeaderOrFooter(boolean isHeader){
    		int startPosition = isHeader ? 0 : 3;
    		HeaderFooterDefDlg defDlg = new HeaderFooterDefDlg(
    					null,
    					_headFooterModel.getHeaderFooterSegmentModel(startPosition),
    					_headFooterModel.getHeaderFooterSegmentModel(startPosition+1),
    					_headFooterModel.getHeaderFooterSegmentModel(startPosition+2)
    					);

    		defDlg.setTitle(isHeader?MultiLang.getString("report00027"):MultiLang.getString("report00028"));
    		defDlg.setVisible(true);
    		if(defDlg.getResult()==UfoDialog.ID_OK){
    			HeaderFooterSegmentModel[] segmentModels = defDlg.getSegmentModels();
    			if(segmentModels != null && segmentModels.length == 3 && 
    					(segmentModels[0]!=null || segmentModels[1]!=null || segmentModels[2]!=null)){

    				if(_headFooterModel == null){
    					_headFooterModel = new HeaderFooterModel();
    				}
    				for(int i=0;i<3;i++){
    					_headFooterModel.setHeaderFooterSegmentModel(i+startPosition,segmentModels[i]);
    				}
    			}
    			psetCurrent.setHeaderFooterModel(_headFooterModel);
    			updateDisplay();
    		}
    	}
    	/**
    	 * 更新页眉/页脚显示
    	 */
    	private void updateDisplay() {
			headerPreviewPanel.removeAll();
			footerPreviewPanel.removeAll();
			if (_headFooterModel != null) {
				Dimension preSize = headerPreviewPanel.getSize();
				for (int position = HeaderFooterModel.HEADERE_LEFT; position <= HeaderFooterModel.FOOTER_RIGHT; position++) {
					HeaderFooterSegmentModel segmentModel = _headFooterModel
							.getHeaderFooterSegmentModel(position);
					if (segmentModel != null && segmentModel.getValue() != null) {
						HeaderFooterSegmentComp comp = new HeaderFooterSegmentComp(
								segmentModel, 1, 1);
						comp.setBounds(new Rectangle(0, 0, preSize.width,
								preSize.height));
						if (segmentModel.isHeaderNotFooter()) {
							headerPreviewPanel.add(comp);
						} else {
							footerPreviewPanel.add(comp);
						}
					}
				}
			}
			this.repaint();
		}
    	/**
    	 * 创建固定打印标题面版
    	 * @param rowFrom
    	 * @param rowTo
    	 * @param colFrom
    	 * @param colTo
    	 * @return
    	 */
        private JPanel createFixedAreaTitlePanel(JTextField rowFrom,JTextField rowTo,JTextField colFrom,JTextField colTo){
        	GridBagLayout gridbaglayout = new GridBagLayout();
			GridBagConstraints gridbagconstraints = new GridBagConstraints();
			JPanel jpanel=new JPanel();
			jpanel.setLayout(gridbaglayout);
			jpanel.setBorder(BorderFactory.createTitledBorder(MultiLang.getString("miufo00019")));
			gridbagconstraints.fill = GridBagConstraints.HORIZONTAL;
			gridbagconstraints.insets = IUFOServiceDialog.compInsets;
			JLabel rowTitle=new JLabel();
			rowTitle.setHorizontalAlignment(SwingConstants.RIGHT);
			rowTitle.setText(MultiLang.getString("miufo00018"));
			JLabel colTitle=new JLabel();
			colTitle.setHorizontalAlignment(SwingConstants.RIGHT);
			colTitle.setText(MultiLang.getString("miufo00017"));
			JLabel rowfrom=new JLabel(MultiLang.getString("From"));
			JLabel rowto=new JLabel(MultiLang.getString("To"));
			JLabel colfrom=new JLabel(MultiLang.getString("From"));
			JLabel colto=new JLabel(MultiLang.getString("To"));
			JPanel fixedRowTitlePanel=createFromToLabelText(rowfrom,rowFrom,rowto,rowTo);
			JPanel fixedColTitlePanel=createFromToLabelText(colfrom,colFrom,colto,colTo);
			gridbagconstraints.gridwidth=GridBagConstraints.RELATIVE;
			gridbagconstraints.weightx = 0.0D;
			IUFOServiceDialog.addToGB(rowTitle, jpanel, gridbaglayout, gridbagconstraints);
			gridbagconstraints.gridwidth=GridBagConstraints.REMAINDER;
			gridbagconstraints.weightx = 1.0D;
			IUFOServiceDialog.addToGB(fixedRowTitlePanel, jpanel, gridbaglayout, gridbagconstraints);
			gridbagconstraints.gridwidth=GridBagConstraints.RELATIVE;
			gridbagconstraints.weightx = 0.0D;
			IUFOServiceDialog.addToGB(colTitle, jpanel, gridbaglayout, gridbagconstraints);
			gridbagconstraints.gridwidth=GridBagConstraints.REMAINDER;
			gridbagconstraints.weightx = 1.0D;
			IUFOServiceDialog.addToGB(fixedColTitlePanel, jpanel, gridbaglayout, gridbagconstraints);
			return jpanel;
        }
        /**
         * 更新固定表头显示
         */
        private void updateTitleNumber(){
        	int[] rowHeadRang = psetOriginal.getRowHeadRang();
        	if (rowHeadRang != null) {
        		fixedRowHeaderFrom.setText(getIntString(rowHeadRang[0] + 1));
        		fixedRowHeaderTo.setText(getIntString(rowHeadRang[1]));
    		}
    		int[] colHeadRang = psetOriginal.getColHeadRang();
    		if (colHeadRang != null) {
    			fixedColHeaderFrom.setText(colValue2ColLabel(colHeadRang[0] + 1));
    			fixedColHeaderTo.setText(colValue2ColLabel(colHeadRang[1]));
    		}
        }
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==headerSetButton){
				defHeaderOrFooter(true);
			}
			if(e.getSource()==footerSetButton){
				defHeaderOrFooter(false);
			}	
			
		}
		public void focusGained(FocusEvent e) {
			// TODO Auto-generated method stub
			
		}
		public void focusLost(FocusEvent e) {
			
		}
		public void updateInfo(){
			HeaderFooterModel hfModel = psetOriginal.getHeaderFooterModel();
    		if(hfModel != null){
    			_headFooterModel = (HeaderFooterModel) hfModel.clone();			
    		} else {
    			_headFooterModel = new HeaderFooterModel();
    		}
    		
			updateDisplay();
			updateTitleNumber();
		}
		public boolean validateInput(){
			int[] rowrang = null;
			int[] colrang=null;
			int start1 = getIntValue(fixedRowHeaderFrom.getText());
			int end1 = getIntValue(fixedRowHeaderTo.getText());
			//将客户计数习惯转为程序计数习惯：从1开始－》从0开始；包括最大值－》不包括最大值。
			int start2 = colLabel2colValue(fixedColHeaderFrom.getText());
			int end2 = colLabel2colValue(fixedColHeaderTo.getText());
            if (start1 > end1) {
					JOptionPane.showMessageDialog(null,MultiLang.getString("m2"));//("行头部信息设置错误,结束位置小于起始位置.");
				    return false;
				}
				if (start2 > end2) {
					JOptionPane.showMessageDialog(null,MultiLang.getString("m3"));//("列头部信息设置错误,结束位置小于起始位置.");
				    return false;
				}
			if (start1 != 0 || end1 != 0) {
				rowrang = new int[]{start1 - 1, end1};
			}
			if (start2 != 0 || end2 != 0) {
				colrang = new int[]{start2 - 1, end2};
			}
			psetCurrent.setRowHeadRang(rowrang);
			psetCurrent.setColHeadRang(colrang);
			return true;
		}
	}
	/**
	 * IUFO个性化打印设置的打印区域单元格的设置
	 * @author guogang
	 *
	 */
	private class PrintAreaPanel extends JPanel implements FocusListener{
		private final String strTitle =MultiLang.getString("PrintArea");
		/**打印区域行起点*/
        private JTextField fixedRowFrom;
        /**打印区域行终点*/
        private JTextField fixedRowTo;
        /**打印区域列起点*/
        private JTextField fixedColFrom;
        /**打印区域列终点*/
        private JTextField fixedColTo;
        
        public PrintAreaPanel(){
        	super();
        	GridBagLayout gridbaglayout = new GridBagLayout();
			GridBagConstraints gridbagconstraints = new GridBagConstraints();
			setLayout(gridbaglayout);
			setBorder(BorderFactory.createTitledBorder(strTitle));
			gridbagconstraints.fill = GridBagConstraints.HORIZONTAL;
			gridbagconstraints.insets = IUFOServiceDialog.compInsets;
			fixedRowFrom=new JTextField();
			fixedRowFrom.setDocument(new IntegerDocument());
			fixedRowFrom.setHorizontalAlignment(JTextField.CENTER);
			fixedRowFrom.addFocusListener(this);
			fixedRowTo=new JTextField();
			fixedRowTo.setDocument(new IntegerDocument());
			fixedRowTo.setHorizontalAlignment(JTextField.CENTER);
			fixedRowTo.addFocusListener(this);
			fixedColFrom=new JTextField();
			fixedColFrom.setDocument(new ColDocument());
			fixedColFrom.setHorizontalAlignment(JTextField.CENTER);
			fixedColFrom.addFocusListener(this);
			fixedColTo=new JTextField();
			fixedColTo.setDocument(new ColDocument());
			fixedColTo.setHorizontalAlignment(JTextField.CENTER);
			fixedColTo.addFocusListener(this);
			JLabel jblRow=new JLabel();
			jblRow.setText(MultiLang.getString("miufo00016"));
			jblRow.setHorizontalAlignment(SwingConstants.RIGHT);
			JLabel jblCol=new JLabel();
			jblCol.setText(MultiLang.getString("miufo00015"));
			jblCol.setHorizontalAlignment(SwingConstants.RIGHT);
			JLabel rowfrom=new JLabel(MultiLang.getString("From"));
			JLabel rowto=new JLabel(MultiLang.getString("To"));
			JLabel colfrom=new JLabel(MultiLang.getString("From"));
			JLabel colto=new JLabel(MultiLang.getString("To"));
			JPanel fixedRowPanel=createFromToLabelText(rowfrom,fixedRowFrom,rowto,fixedRowTo);
			JPanel fixedColPanel=createFromToLabelText(colfrom,fixedColFrom,colto,fixedColTo);
			gridbagconstraints.gridwidth=GridBagConstraints.RELATIVE;
			gridbagconstraints.weightx=0.0D;
			IUFOServiceDialog.addToGB(jblRow, this, gridbaglayout, gridbagconstraints);
			gridbagconstraints.gridwidth=GridBagConstraints.REMAINDER;
			gridbagconstraints.weightx=1.0D;
			IUFOServiceDialog.addToGB(fixedRowPanel, this, gridbaglayout, gridbagconstraints);
			gridbagconstraints.gridwidth=GridBagConstraints.RELATIVE;
			gridbagconstraints.weightx=0.0D;
			IUFOServiceDialog.addToGB(jblCol, this, gridbaglayout, gridbagconstraints);
			gridbagconstraints.gridwidth=GridBagConstraints.REMAINDER;
			gridbagconstraints.weightx=1.0D;
			IUFOServiceDialog.addToGB(fixedColPanel, this, gridbaglayout, gridbagconstraints);
			
        }
        
		public void focusGained(FocusEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void focusLost(FocusEvent e) {
			
		}
		public void updateInfo(){
			//设置打印区域.
			int[] printArea = psetOriginal.getPrintArea();
			if (printArea != null) {
				fixedRowFrom.setText(getIntString(printArea[0] + 1));
				fixedRowTo.setText(getIntString(printArea[2]));
				fixedColFrom.setText(colValue2ColLabel(printArea[1] + 1));
				fixedColTo.setText(colValue2ColLabel(printArea[3]));
			}
		}
		public boolean validateInput(){
			int startRow = getIntValue(fixedRowFrom.getText());
			int startCol = colLabel2colValue(fixedColFrom.getText());
			int endRow = getIntValue(fixedRowTo.getText());
			int endCol = colLabel2colValue(fixedColTo.getText());
			if (startRow > endRow) {
				JOptionPane.showMessageDialog(this, MultiLang.getString("m4"));
				return false;
			}
			if (startCol > endCol) {
				JOptionPane.showMessageDialog(this, MultiLang.getString("m5"));
				return false;
			}

			int[] area = null;
			if (startRow != 0 || startCol != 0 || endRow != 0 || endCol != 0) {
				// 设置为0或空，起始位置默认值为-1，结束位置默认值为0。目的是与正常设置情况下算法一致。
				area = new int[] { startRow - 1, startCol - 1, endRow, endCol };
			}
			psetCurrent.setPrintArea(area);
			return true;
		}
	}
	/**
	 * IUFO个性化打印设置的缩放比例设置、缩小到单页打印设置
	 * @author guogang
	 *
	 */
    private class PrintScalePanel extends JPanel implements ActionListener,FocusListener{
    	private final String strTitle =MultiLang.getString("miufo00012");
    	/** 缩放比例设置输入框*/
    	private JFormattedTextField printScale;
    	/** 缩放到单页打印按钮*/
    	private JRadioButton scaleToOne;
    	
    	public PrintScalePanel(){
    		super();
    		GridBagLayout gridbaglayout = new GridBagLayout();
	        GridBagConstraints gridbagconstraints = new GridBagConstraints();
	        setLayout(gridbaglayout);
	        setBorder(BorderFactory.createTitledBorder(strTitle));
	        gridbagconstraints.fill = GridBagConstraints.HORIZONTAL;
	        gridbagconstraints.insets = IUFOServiceDialog.compInsets;
	        gridbagconstraints.weighty = 1.0D;
	        
	        DecimalFormat decimalformat=new DecimalFormat("#.##");
	        decimalformat.setMaximumIntegerDigits(1);
	        decimalformat.setMinimumFractionDigits(1);
	        decimalformat.setMaximumFractionDigits(2);
	        decimalformat.setParseIntegerOnly(false);
			decimalformat.setDecimalSeparatorAlwaysShown(true);
			NumberFormatter numberformatter= new NumberFormatter(decimalformat);
			numberformatter.setMinimum(new Float(0.3F));
			numberformatter.setMaximum(new Float(3.0F));
			numberformatter.setAllowsInvalid(true);
			numberformatter.setCommitsOnValidEdit(true);
			
			printScale=new JFormattedTextField(numberformatter);
			JLabel scaleSetLabel=new JLabel(MultiLang.getString("miufo00011"));
			scaleSetLabel.setHorizontalAlignment(JLabel.RIGHT);
			scaleSetLabel.setLabelFor(printScale);
			JPanel scaleSetPanel=createLabelText(scaleSetLabel,printScale);
			JLabel afterScaleSet=new JLabel(MultiLang.getString("miufo00010"));
			afterScaleSet.setHorizontalAlignment(JLabel.LEFT);
			
			gridbagconstraints.gridwidth = GridBagConstraints.RELATIVE;
			gridbagconstraints.weightx=1.0D;
			IUFOServiceDialog.addToGB(scaleSetPanel, this, gridbaglayout, gridbagconstraints);
			gridbagconstraints.gridwidth = GridBagConstraints.REMAINDER;
			gridbagconstraints.weightx=0.0D;
			IUFOServiceDialog.addToGB(afterScaleSet, this, gridbaglayout, gridbagconstraints);
			scaleToOne=createRadioButton(MultiLang.getString("pageToOne"),this);
			IUFOServiceDialog.addToGB(scaleToOne, this, gridbaglayout, gridbagconstraints);
    	}
    	
		public void focusGained(FocusEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void focusLost(FocusEvent e) {
			if(e.getSource()==printScale){
				String strScale = printScale.getText().trim();
				if("".equals(strScale) || "0".equals(strScale)||scaleToOne.isSelected()){
					strScale = "1.0";
				}
				float scale = Float.parseFloat(strScale);
				psetCurrent.setViewScale(scale);
			}
			
		}

		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==scaleToOne){
				psetCurrent.setPageToOne(scaleToOne.isSelected());
			
			}
			
		}
		public void updateInfo(){
			//打印到单页
			if(psetOriginal.isPageToOne()){
				scaleToOne.setSelected(true);
			}
			//缩放比例
			String value = Float.toString(psetOriginal.getViewScale());
			printScale.setText(value);
		}
	}
	/**
	 * IUFO个性化打印设置的分页的打印顺序的设置
	 * @author guogang
	 *
	 */
	private class PrintRankPanel extends JPanel implements ActionListener{
		private final String strTitle =MultiLang.getString("PrintOrder");
		/** 先行后列*/
		private IconRadioButton rbRowToCol;
		/** 先列后行*/
	    private IconRadioButton rbColToRow;
	    
	    public PrintRankPanel(){
	    	super();
	    	GridBagLayout gridbaglayout = new GridBagLayout();
	        GridBagConstraints gridbagconstraints = new GridBagConstraints();
	        setLayout(gridbaglayout);
	        setBorder(BorderFactory.createTitledBorder(strTitle));
	        gridbagconstraints.fill = GridBagConstraints.BOTH;
	        gridbagconstraints.insets = IUFOServiceDialog.compInsets;
	        gridbagconstraints.weighty = 1.0D;
	        gridbagconstraints.gridwidth = GridBagConstraints.REMAINDER;
	        ButtonGroup buttongroup = new ButtonGroup();
	        rbRowToCol= new IconRadioButton(MultiLang
					.getString("RowThenColumn"), "printorder2.gif", true, buttongroup, this);
	        rbRowToCol.addActionListener(this);
	        rbColToRow=new IconRadioButton(MultiLang
					.getString("ColumnThenRow"), "printorder1.gif", true, buttongroup, this);
	        rbColToRow.addActionListener(this);
	        IUFOServiceDialog.addToGB(rbRowToCol, this, gridbaglayout, gridbagconstraints);
	        IUFOServiceDialog.addToGB(rbColToRow, this, gridbaglayout, gridbagconstraints);
	    }
		
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==rbRowToCol){
				psetCurrent.setColPriorityPrinted(false);
			}
			if(e.getSource()==rbColToRow){
				psetCurrent.setColPriorityPrinted(true);
			}
		}
		public void updateInfo(){
			if (psetOriginal.isColPriorityPrinted()) {
				rbColToRow.setSelected(true);
			} else {
				rbRowToCol.setSelected(true);
			}
		}
	}
	/**
	 * IUFO个性化打印设置的分页的打印居中方式的设置
	 * @author guogang
	 *
	 */
	private class PrintAlignPanel extends JPanel implements ActionListener{
		private final String strTitle =MultiLang.getString("PrintCenterModel");
		/** 水平居中*/
		private JRadioButton rbHCenter;
		/** 垂直居中*/
		private JRadioButton rbVCenter;
		
	    
	    public PrintAlignPanel(){
	    	super(new FlowLayout(FlowLayout.LEADING));
	    	rbHCenter = new UIRadioButton(MultiLang
					.getString("Horizontal"));
	    	rbHCenter.addActionListener(this);
			add(rbHCenter);
			rbVCenter = new UIRadioButton(MultiLang
					.getString("Vertical"));
			rbVCenter.addActionListener(this);
			add(rbVCenter);
			
	    }
		
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==rbHCenter){
				psetCurrent.setHCenter(true);
			}
			if(e.getSource()==rbVCenter){
				psetCurrent.setVCenter(true);
			}
		}
		public void updateInfo(){
			if (psetOriginal.isHCenter()) {
				rbHCenter.setSelected(true);
			} 
			if (psetOriginal.isVCenter()) {
				rbVCenter.setSelected(true);
			}
		}
	}
	private class ValidatingFileChooser extends JFileChooser
    {
        private ValidatingFileChooser()
        {
            super();
        }
        public void approveSelection()
        {
            File file = getSelectedFile();
            boolean flag;
            try
            {
                flag = file.exists();
            }
            catch(SecurityException securityexception)
            {
                flag = false;
            }
            if(flag)
            {
                int i = JOptionPane.showConfirmDialog(this, IUFOServiceDialog.getMsg("dialog.overwrite"), IUFOServiceDialog.getMsg("dialog.owtitle"), 0);
                if(i == 0)
                    super.approveSelection();
            } else
            {
                super.approveSelection();
            }
        }


    }
	
}
 