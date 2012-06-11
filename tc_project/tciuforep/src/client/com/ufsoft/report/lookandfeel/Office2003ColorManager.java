/**
 * 
 */
package com.ufsoft.report.lookandfeel;
import com.ufida.iufo.pub.tools.AppDebug;

import java.awt.Color;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

/**
 * @author guogang
 *
 */
public class Office2003ColorManager implements PropertyChangeListener {
	protected static final int COLOR_SCHEME_BLUE		= 0;
	protected static final int COLOR_SCHEME_GREEN	= 1;
	protected static final int COLOR_SCHEME_SILVER	= 2;
	
	protected static final String STYLE_DESKTOP_PROPERTY = "win.xpstyle.colorName";
	protected static final String THEMEACTIVE_DESKTOP_PROPERTY = "win.xpstyle.themeActive";
	
	private static final Toolkit toolkit	= Toolkit.getDefaultToolkit();
	private static final int DELTA		= 2;
	/**
	 * Constructor.
	 */
	public Office2003ColorManager() {
	}
	
	public void updateDefaultColors(UIDefaults table) {

		Object[] defaults = null;

		String theme = System.getProperty(Office2003LookAndFeel.THEME_PROPERTY);
        if(theme==null){
        	theme=Office2003LookAndFeel.THEME_BLUE;
        }
		if (Office2003LookAndFeel.THEME_BLUE.equals(theme)) {
			defaults = getColorSchemeBlueDefaults(table);
		}
		else if (Office2003LookAndFeel.THEME_OLIVE.equals(theme)) {
			defaults = getColorSchemeGreenDefaults(table);
		}
		else if (Office2003LookAndFeel.THEME_SILVER.equals(theme)) {
			defaults = getColorSchemeSilverDefaults(table);
		}
		else if (Office2003LookAndFeel.THEME_CLASSIC.equals(theme)) {
			defaults = getColorSchemeWindowsClassicDefaults(table);
		}
		else { // No override
			Boolean themeActive = (Boolean)toolkit.getDesktopProperty(
										THEMEACTIVE_DESKTOP_PROPERTY);
			boolean isThemeActive = themeActive!=null ?
								themeActive.booleanValue() : false;
			if (isThemeActive) {
				switch (getColorScheme(table)) {
					case COLOR_SCHEME_BLUE:
						defaults = getColorSchemeBlueDefaults(table);
						break;
					case COLOR_SCHEME_GREEN:
						defaults = getColorSchemeGreenDefaults(table);
						break;
					case COLOR_SCHEME_SILVER:
						defaults = getColorSchemeSilverDefaults(table);
						break;
				}
			}
			else { // "Classic" Windows look.
				defaults = getColorSchemeWindowsClassicDefaults(table);
			}
		}

		table.putDefaults(defaults);

	}
	protected static final int getColorScheme(final UIDefaults table) {

		Color c = table.getColor("Menu.selectionBackground");

		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();

		if (closeEnough(r,49) && closeEnough(g,106) && closeEnough(b,197)) {
			return COLOR_SCHEME_BLUE;
		}
		else if (closeEnough(r,147) && closeEnough(g,160) && closeEnough(b,112)) {
			return COLOR_SCHEME_GREEN;
		}
		else if (closeEnough(r,178) && closeEnough(g,180) && closeEnough(b,191)) {
			return COLOR_SCHEME_SILVER;
		}
		else {	// Unknown color scheme!
			return COLOR_SCHEME_BLUE; // ???
		}

	}
	private static final boolean closeEnough(int value, int target) {
		return (value>=(target-DELTA)) && (value<=(target+DELTA));
	}
	protected Object[] getColorSchemeBlueDefaults(UIDefaults table) {

		// Create some standard colors.
		Object standardOffice2003Blue			= new ColorUIResource(0xd2, 0xdb, 0xf1);
		Object office2003MenuBackground		= new ColorUIResource(0xf6, 0xf6, 0xf6);

		Object[] defaults = {
			
			"ScrollBar.background",					new ColorUIResource(0xe0, 0xe0, 0xe0),
			"ScrollBar.thumb",					new ColorUIResource(0xd2, 0xdb, 0xf1),
			"ScrollBar.thumbDarkShadow",					new ColorUIResource(0xd2, 0xdb, 0xf1),
			"ScrollBar.thumbHighlight",					new ColorUIResource(0xd2, 0xdb, 0xf1),
			"ScrollBar.thumbHighlight",					new ColorUIResource(0xd2, 0xdb, 0xf1),
				
			"report.ComboBox.Arrow.Armed.Gradient1",				new ColorUIResource(0xFF, 0xF4, 0xCC),
			"report.ComboBox.Arrow.Armed.Gradient2",				new ColorUIResource(0xFF, 0xD4, 0x97),
			"report.ComboBox.Arrow.Selected.Gradient1",			new ColorUIResource(0xFE, 0x91, 0x4E),
			"report.ComboBox.Arrow.Selected.Gradient2",			new ColorUIResource(0xFF, 0xCB, 0x87),
			"report.ComboBox.Arrow.Normal.Gradient1",				new ColorUIResource(0xc3, 0xda, 0xf9),
			"report.ComboBox.Arrow.Normal.Gradient2",				null,
				
			"report.HighlightBorderColor",						new ColorUIResource(0x00, 0x00, 0x80),
			"report.HighlightColor",							new ColorUIResource(0xFF, 0xEE, 0xC2),
			"OfficeXP.PressedHighlightColor",	new ColorUIResource(152,181,226),
			
			"report.ToolBarGripLightColor",					new ColorUIResource(0xFF, 0xFF, 0xFF),
			"report.ToolBarGripDarkColor",					new ColorUIResource(0x27, 0x41, 0x76),
			"report.ToolBarBeginGradientColor",				new ColorUIResource(0xF0, 0xFD, 0xFF),
			"report.ToolBarEndGradientColor",				new ColorUIResource(0xD2, 0xDB, 0xF1),
			"report.ToolBarBottomBorderColor",				new ColorUIResource(0x3B, 0x61, 0x9C),
			"report.ToolBarBackgroundColor",					standardOffice2003Blue,
			"report.ToolBarButtonArmedBeginGradientColor",		new ColorUIResource(0xFF, 0xF4, 0xCC),
			"report.ToolBarButtonArmedEndGradientColor",		new ColorUIResource(0xFF, 0xD0, 0x91),
			"report.ToolBarButtonSelectedBeginGradientColor",	new ColorUIResource(0xFE, 0x91, 0x4E),
			"report.ToolBarButtonSelectedEndGradientColor",		new ColorUIResource(0xFF, 0xD3, 0x8E),

			"report.MenuBarItemArmedBeginGradientColor",		new ColorUIResource(0xFF, 0xF4, 0xCC),
			"report.MenuBarItemArmedEndGradientColor",			new ColorUIResource(0xFF, 0xD6, 0x9A),
			"report.MenuBarItemSelectedBeginGradientColor",		new ColorUIResource(0xE3, 0xEF, 0xFF),
			"report.MenuBarItemSelectedEndGradientColor",		new ColorUIResource(0x93, 0xB5, 0xE7),
            "report.MenuBorderColor",							new ColorUIResource(0x00, 0x2d, 0x96),
            "checkBoxMenuItemBackground",							office2003MenuBackground,
            "menuBackground",									office2003MenuBackground,
			"menuBarBackground",								standardOffice2003Blue,
			"menuItemBackground",								office2003MenuBackground,
			"radioButtonMenuItemBackground",						office2003MenuBackground,

			"toolBarShadow",									new ColorUIResource(0x6A, 0x8C, 0xCB),
			"toolBarHighlight",									new ColorUIResource(0xF1, 0xF9, 0xFF),
			"separatorForeground",								new ColorUIResource(0x6A, 0x8C, 0xCB),
			"separatorBackground",								office2003MenuBackground,
			"report.TabBorderColor",						new ColorUIResource(0x7F, 0x9D, 0xB9),
			"report.TabSelectedColor",						new ColorUIResource(0xDD,0xE7,0xF0),
			"report.BackgroundTabBorderColor",				new ColorUIResource(0xAC, 0xA8, 0x99),
			"report.SelectedCBMenuItemBorderColor",			new ColorUIResource(75, 75, 111),
		};

		return defaults;

	}
	protected Object[] getColorSchemeGreenDefaults(UIDefaults table) {

		// Create some standard colors.
		Object standardOffice2003Blue			= new ColorUIResource(242, 241, 228);
		Object office2003MenuBackground		= new ColorUIResource(244, 244, 238);

		Object [] defaults = {

			"report.ComboBox.Arrow.Armed.Gradient1",				new ColorUIResource(0xFF, 0xF4, 0xCC),
			"report.ComboBox.Arrow.Armed.Gradient2",				new ColorUIResource(0xFF, 0xD4, 0x97),
			"report.ComboBox.Arrow.Selected.Gradient1",			new ColorUIResource(0xFE, 0x91, 0x4E),
			"report.ComboBox.Arrow.Selected.Gradient2",			new ColorUIResource(0xFF, 0xCB, 0x87),
			"report.ComboBox.Arrow.Normal.Gradient1",				table.getColor("Panel.background"),
			"report.ComboBox.Arrow.Normal.Gradient2",				null,

			"report.CBMenuItemCheckBGColor",					new ColorUIResource(255, 192, 111),
			"report.CBMenuItemCheckBGSelectedColor",			new ColorUIResource(254, 128, 62),

			"report.HighlightBorderColor",						new ColorUIResource(63, 93, 56),
			"report.HighlightColor",							new ColorUIResource(255, 238, 194),
			"OfficeXP.PressedHighlightColor",	new ColorUIResource(201,208,184),
			"report.ToolBarGripLightColor",					new ColorUIResource(255, 255, 255),
			"report.ToolBarGripDarkColor",					new ColorUIResource(81, 94, 51),

			"report.MenuItemBeginGradientColor",				new ColorUIResource(255, 255, 237),
			"report.MenuItemEndGradientColor",				new ColorUIResource(184, 199, 146),

			"report.PanelGradientColor1",					new ColorUIResource(217, 217, 167),
			"report.PanelGradientColor2",					new ColorUIResource(242, 240, 228),

			"report.ToolBarBeginGradientColor",				new ColorUIResource(244, 247, 222),
			"report.ToolBarEndGradientColor",				new ColorUIResource(177, 198, 140),
			"report.ToolBarBottomBorderColor",				new ColorUIResource(96, 128, 88),
			"report.ToolBarBackgroundColor",					standardOffice2003Blue,
			"report.ToolBarButtonArmedBeginGradientColor",		new ColorUIResource(255, 244, 204),
			"report.ToolBarButtonArmedEndGradientColor",		new ColorUIResource(255, 208, 145),
			"report.ToolBarButtonSelectedBeginGradientColor",	new ColorUIResource(254, 145, 78),
			"report.ToolBarButtonSelectedEndGradientColor",		new ColorUIResource(255, 211, 142),

			"report.MenuBarItemArmedBeginGradientColor",		new ColorUIResource(255, 244, 204),
			"report.MenuBarItemArmedEndGradientColor",			new ColorUIResource(255, 214, 154),
			"report.MenuBarItemSelectedBeginGradientColor",		new ColorUIResource(246, 240, 213),
			"report.MenuBarItemSelectedEndGradientColor",		new ColorUIResource(194, 206, 159),

			"report.MenuBorderColor",							new ColorUIResource(117, 141, 94),

			"checkBoxMenuItemBackground",							office2003MenuBackground,
			"menuBackground",									office2003MenuBackground,
			"menuBarBackground",								standardOffice2003Blue,
			"menuItemBackground",								office2003MenuBackground,
			"radioButtonMenuItemBackground",						office2003MenuBackground,
			//"toolBarBackground",								standardOffice2003Blue, // Not done because of toolbars in JFileChooser.
			"toolBarShadow",									new ColorUIResource(96, 128, 88),
			"toolBarHighlight",									new ColorUIResource(244, 247, 222),
			"separatorForeground",								new ColorUIResource(96, 128, 88),
			"separatorBackground",								office2003MenuBackground,
			"report.TabBorderColor",						new ColorUIResource(127, 157, 185),
			"report.TabSelectedColor",						new ColorUIResource(0xDD,0xE7,0xF0),
			"report.BackgroundTabBorderColor",				new ColorUIResource(172, 168, 153),
			"report.SelectedCBMenuItemBorderColor",			new ColorUIResource(147, 160, 112),
		};

		return defaults;

	}


	protected Object[] getColorSchemeSilverDefaults(UIDefaults table) {

		// Create some standard colors.
		Object standardOffice2003Blue			= new ColorUIResource(243, 243, 247);
		Object office2003MenuBackground		= new ColorUIResource(253, 250, 255);

		Object [] defaults = {

			"report.ComboBox.Arrow.Armed.Gradient1",				new ColorUIResource(255, 242, 200),
			"report.ComboBox.Arrow.Armed.Gradient2",				new ColorUIResource(255, 210, 148),
			"report.ComboBox.Arrow.Selected.Gradient1",			new ColorUIResource(254, 149, 82),
			"report.ComboBox.Arrow.Selected.Gradient2",			new ColorUIResource(255, 207, 139),
			"report.ComboBox.Arrow.Normal.Gradient1",				new ColorUIResource(237, 237, 245),
			"report.ComboBox.Arrow.Normal.Gradient2",				new ColorUIResource(179, 178, 201),

			"report.CBMenuItemCheckBGColor",					new ColorUIResource(255, 192, 111),
			"report.CBMenuItemCheckBGSelectedColor",			new ColorUIResource(254, 128, 62),

			"report.HighlightBorderColor",						new ColorUIResource(75, 75, 111),
			"report.HighlightColor",							new ColorUIResource(255, 238, 194),
			"OfficeXP.PressedHighlightColor",	new ColorUIResource(210,211,216),
			"report.ToolBarGripLightColor",					new ColorUIResource(255, 255, 255),
			"report.ToolBarGripDarkColor",					new ColorUIResource(84, 84, 117),

			"report.MenuItemBeginGradientColor",				new ColorUIResource(249, 249, 255),
			"report.MenuItemEndGradientColor",				new ColorUIResource(159, 157, 185),

			"report.PanelGradientColor1",					new ColorUIResource(215, 215, 229),
			"report.PanelGradientColor2",					new ColorUIResource(243, 243, 247),

			"report.ToolBarBeginGradientColor",				new ColorUIResource(243, 244, 250),
			"report.ToolBarEndGradientColor",				new ColorUIResource(152, 151, 181),
			"report.ToolBarBottomBorderColor",				new ColorUIResource(124, 124, 148),
			"report.ToolBarBackgroundColor",					standardOffice2003Blue,
			"report.ToolBarButtonArmedBeginGradientColor",		new ColorUIResource(255, 244, 204),
			"report.ToolBarButtonArmedEndGradientColor",		new ColorUIResource(255, 208, 145),
			"report.ToolBarButtonSelectedBeginGradientColor",	new ColorUIResource(254, 145, 78),
			"report.ToolBarButtonSelectedEndGradientColor",		new ColorUIResource(255, 211, 142),

			"report.MenuBarItemArmedBeginGradientColor",		new ColorUIResource(255, 244, 204),
			"report.MenuBarItemArmedEndGradientColor",			new ColorUIResource(255, 214, 154),
			"report.MenuBarItemSelectedBeginGradientColor",		new ColorUIResource(232, 233, 241),
			"report.MenuBarItemSelectedEndGradientColor",		new ColorUIResource(186, 185, 205),

			"report.MenuBorderColor",							new ColorUIResource(124, 124, 148),

			"checkBoxMenuItemBackground",							office2003MenuBackground,
			"menuBackground",									office2003MenuBackground,
			"menuBarBackground",								standardOffice2003Blue,
			"menuItemBackground",								office2003MenuBackground,
			"radioButtonMenuItemBackground",						office2003MenuBackground,
			//"toolBarBackground",								standardOffice2003Blue, // Not done because of toolbars in JFileChooser.
			"toolBarShadow",									new ColorUIResource(110, 109, 143),
			"toolBarHighlight",									new ColorUIResource(255, 255, 255),
			"separatorForeground",								new ColorUIResource(110, 109, 143),
			"separatorBackground",								office2003MenuBackground,
			"report.TabBorderColor",						new ColorUIResource(127, 157, 185),
			"report.TabSelectedColor",						new ColorUIResource(0xDD,0xE7,0xF0),
			"report.BackgroundTabBorderColor",				new ColorUIResource(157, 157, 161),
			"report.SelectedCBMenuItemBorderColor",			new ColorUIResource(75, 75, 111),
		};

		return defaults;

	}


	protected Object[] getColorSchemeWindowsClassicDefaults(UIDefaults table) {

		// Create some standard colors.
		Object standardOffice2003Blue			= new ColorUIResource(246, 245, 244);
		Object office2003MenuBackground		= new ColorUIResource(249, 248, 247);
		Object armedColor					= new ColorUIResource(182, 189, 210);
		Object selectedColor				= new ColorUIResource(133, 146, 181);

		Object [] defaults = {

			"report.ComboBox.Arrow.Armed.Gradient1",				armedColor,
			"report.ComboBox.Arrow.Armed.Gradient2",				null,
			"report.ComboBox.Arrow.Selected.Gradient1",			selectedColor,
			"report.ComboBox.Arrow.Selected.Gradient2",			null,
			"report.ComboBox.Arrow.Normal.Gradient1",				new ColorUIResource(241, 239, 237),
			"report.ComboBox.Arrow.Normal.Gradient2",				new ColorUIResource(221, 218, 211),

			"report.CBMenuItemCheckBGColor",					new ColorUIResource(212, 213, 216),
			"report.CBMenuItemCheckBGSelectedColor",			new ColorUIResource(133, 146, 181),

			"report.HighlightBorderColor",						new ColorUIResource(10, 36, 106),
			"report.HighlightColor",							new ColorUIResource(182, 189, 210),

			"report.ToolBarGripLightColor",					new ColorUIResource(255, 255, 255),
			"report.ToolBarGripDarkColor",					new ColorUIResource(160, 160, 160),

			"report.MenuItemBeginGradientColor",				new ColorUIResource(249, 248, 247),
			"report.MenuItemEndGradientColor",				new ColorUIResource(215, 211, 204),

			"report.PanelGradientColor1",					new ColorUIResource(212, 208, 200),
			"report.PanelGradientColor2",					new ColorUIResource(245, 245, 244),

			"report.ToolBarBeginGradientColor",				new ColorUIResource(245, 244, 242),
			"report.ToolBarEndGradientColor",				new ColorUIResource(213, 210, 202),
			"report.ToolBarBottomBorderColor",				new ColorUIResource(213, 210, 202),
			"report.ToolBarBackgroundColor",					standardOffice2003Blue,
			"report.ToolBarButtonArmedBeginGradientColor",		armedColor,
			"report.ToolBarButtonArmedEndGradientColor",		armedColor,
			"report.ToolBarButtonSelectedBeginGradientColor",	selectedColor,
			"report.ToolBarButtonSelectedEndGradientColor",		selectedColor,

			"report.MenuBarItemArmedBeginGradientColor",		armedColor,
			"report.MenuBarItemArmedEndGradientColor",			armedColor,
			"report.MenuBarItemSelectedBeginGradientColor",		new ColorUIResource(249, 247, 246),
			"report.MenuBarItemSelectedEndGradientColor",		new ColorUIResource(237, 235, 232),

			"report.MenuBorderColor",							new ColorUIResource(102, 102, 102),

			"checkBoxMenuItemBackground",							office2003MenuBackground,
			"menuBackground",									office2003MenuBackground,
			"menuBarBackground",								standardOffice2003Blue,
			"menuItemBackground",								office2003MenuBackground,
			"radioButtonMenuItemBackground",						office2003MenuBackground,
			//"toolBarBackground",								standardOffice2003Blue, // Not done because of toolbars in JFileChooser.
			"toolBarShadow",									new ColorUIResource(166, 166, 166),
			"toolBarHighlight",									new ColorUIResource(255, 255, 255),
			"separatorForeground",								new ColorUIResource(166, 166, 166),
			"separatorBackground",								office2003MenuBackground,
			"report.TabBorderColor",						new ColorUIResource(128, 128, 128),
			"report.TabSelectedColor",						new ColorUIResource(0xDD,0xE7,0xF0),
			"report.BackgroundTabBorderColor",				new ColorUIResource(128, 128, 128),
			"report.SelectedCBMenuItemBorderColor",			new ColorUIResource(10, 36, 106),
		};

		return defaults;

	}

	protected String getLookAndFeelClassName() {
		return "com.ufsoft.report.lookandfeel.Office2003LookAndFeel";
	}
	
	public void propertyChange(PropertyChangeEvent e) {
		String name = e.getPropertyName();

		if (STYLE_DESKTOP_PROPERTY.equals(name) ||
				THEMEACTIVE_DESKTOP_PROPERTY.equals(name)) {
			try {
				UIManager.setLookAndFeel(getLookAndFeelClassName());
			} catch (Exception ex) {
				AppDebug.debug(ex);
			}
		}

	}
	/**
	 * Ìí¼Ó¼àÌý
	 */
	public void install() {
		toolkit.addPropertyChangeListener(STYLE_DESKTOP_PROPERTY, this);
		toolkit.addPropertyChangeListener(THEMEACTIVE_DESKTOP_PROPERTY, this);
	}
	public void uninstall() {
		toolkit.removePropertyChangeListener(THEMEACTIVE_DESKTOP_PROPERTY,
										this);
		toolkit.removePropertyChangeListener(STYLE_DESKTOP_PROPERTY, this);
	}
}
