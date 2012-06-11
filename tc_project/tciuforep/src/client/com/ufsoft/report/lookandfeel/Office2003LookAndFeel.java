/**
 * 
 */
package com.ufsoft.report.lookandfeel;

import java.awt.Color;

import javax.swing.UIDefaults;
import javax.swing.UIManager;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import com.ufida.iufo.pub.tools.AppDebug;

import javax.swing.plaf.BorderUIResource;
/**
 * @author guogang
 *
 */
public class Office2003LookAndFeel extends WindowsLookAndFeel {
	private static final long serialVersionUID = 1L;
	protected Office2003ColorManager colorManager;
	public static final String THEME_PROPERTY	= "report.theme";

	/**
	 * Value for <code>THEME_PROPERTY</code> specifying the blue
	 * color scheme.
	 */
	public static final String THEME_BLUE		= "blue";

	/**
	 * Value for <code>THEME_PROPERTY</code> specifying the olive
	 * green color scheme.
	 */
	public static final String THEME_OLIVE		= "olive";

	/**
	 * Value for <code>THEME_PROPERTY</code> specifying the silver
	 * color scheme.
	 */
	public static final String THEME_SILVER		= "silver";

	/**
	 * Value for <code>THEME_PROPERTY</code> specifying the
	 * Windows Classic color scheme.
	 */
	public static final String THEME_CLASSIC	= "classic";
	
	public String getDescription() {
		return "Report's Microsoft Office 2003 Look And Feel";
	}

	@Override
	public String getID() {
		return "Office2003";
	}

	@Override
	public String getName() {
		return "Office 2003";
	}
	
	/**
	 * 初始化方法，调用setLookAndFeel()后UIManager会自动调用
	 */
	public void initialize() {
		colorManager = createColorManager();
		colorManager.install();
		super.initialize();
	}
	/**
	 * 此方法由 UIManager.setLookAndFeel后会自动调用一次以创建特定于外观的默认表,同时替换相关的用户默认属性
	 * BasicLookAndFeel会依次调用
	 * initClassDefaults()
	 * initSystemColorDefaults()
	 * initComponentDefaults()
	 * 来创建特定于外观的默认表
	 */
	public UIDefaults getDefaults() {
		UIDefaults defaults = super.getDefaults();
		initSystemColorDefaults2003(defaults);
		initComponentDefaults2003(defaults);
		
		return defaults;
	}
	/**
	 * 重载BasicLookAndFeel的initClassDefaults()
	 * 只加载需要的UI名和其实现的影射关系
	 */
	protected void initClassDefaults(UIDefaults table) {
		//装载Windows风格的UI影射
		super.initClassDefaults(table);
		//重载个性的UI影射
		String packageName = "com.ufsoft.report.lookandfeel.plaf.";
		Object[] uiDefaults = {
				"MenuBarUI",		"com.ufsoft.report.component.RepMenuBarUI",
//				"ButtonUI",			packageName + "Office2003ButtonUI",
				"ComboBoxUI",		packageName + "OfficeXPComboBoxUI",
				"ToolBarUI",		packageName + "Office2003ToolBarUI",
				"RootPaneUI",       "javax.swing.plaf.metal.MetalRootPaneUI"
		};
		//向外观默认添加属性
		table.putDefaults(uiDefaults);
		//向用户默认添加属性
		UIManager.getDefaults().putDefaults(uiDefaults);
	}
	protected void initComponentDefaults2003(UIDefaults table)  {
		Object border =new BorderUIResource.LineBorderUIResource(Color.BLUE);
		Object[] uiDefaults = {
//				"ComboBox.width", new Integer(50),
				"RootPane.frameBorder",border,
                "RootPane.plainDialogBorder", border,
                "RootPane.informationDialogBorder", border,
                "RootPane.errorDialogBorder", border,
                "RootPane.colorChooserDialogBorder", border,
                "RootPane.fileChooserDialogBorder", border,
                "RootPane.questionDialogBorder", border,
                "RootPane.warningDialogBorder", border,
                "RootPane.defaultButtonWindowKeyBindings", new Object[] {
			             "ENTER", "press",
			    "released ENTER", "release",
			        "ctrl ENTER", "press",
		       "ctrl released ENTER", "release"
		      }
		};
		table.putDefaults(uiDefaults);
		UIManager.getDefaults().putDefaults(uiDefaults);
		
	}
	protected void initSystemColorDefaults2003(UIDefaults table) {
		colorManager.updateDefaultColors(table);
	}
	/**
	 * 创建颜色管理器
	 * @return
	 */
	protected Office2003ColorManager createColorManager() {
		return new Office2003ColorManager();
	}

	public void uninitialize() {
		AppDebug.debug("--Office 2003 Look And Feel uninitialize--");
			super.uninitialize();
			colorManager.uninstall();
			colorManager = null;
	}


	

}
