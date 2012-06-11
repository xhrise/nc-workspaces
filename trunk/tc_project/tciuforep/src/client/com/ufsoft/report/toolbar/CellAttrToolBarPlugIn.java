package com.ufsoft.report.toolbar;

import com.ufsoft.report.constant.DefaultSetting;
import com.ufsoft.report.constant.PropertyType;
import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.ICommandExt;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.report.toolbar.dropdown.ColorPanel;
import com.ufsoft.table.format.TableConstant;


/**
 * 格式设置工具栏插件
 * @author guogang
 *
 */
public class CellAttrToolBarPlugIn extends AbstractPlugIn {

	@Override
	protected IPluginDescriptor createDescriptor() {
		return new AbstractPlugDes(this) {
			protected IExtension[] createExtensions() {
				
				ICommandExt fontNameToolBar=new CellAttrComboBoxExt(PropertyType.FontIndex,DefaultSetting.fontNames,getReport());//字体工具栏
				
				ICommandExt fontSizeToolBar=new CellAttrComboBoxExt(PropertyType.FontSize,DefaultSetting.fontSizes,getReport());//字号工具栏
				
				ICommandExt forceColorToolBar=new CellAttrComboBoxExt(PropertyType.ForeColor,new ColorPanel(),"reportcore/forcecolor.gif",getReport());
				
				ICommandExt backColorToolBar=new CellAttrComboBoxExt(PropertyType.BackColor,new ColorPanel(),"reportcore/backcolor.gif",getReport());
				// add by 王宇光 2008-5-4 添加设置边框到工具栏
				
				ICommandExt cellBorderLineToolBar=new CellBorderLineToolBarExt(PropertyType.BorderLine,CellBorderLineToolBarExt.getSwatchPanel(),"reportcore/all_line.png",getReport());//设置单元边框线
				
				ICommandExt changeLineToolBar=new CellAttrButtonExt(PropertyType.ChangeLine,TableConstant.TRUE,getReport());//自动换行
				
				ICommandExt shrinkFitToolBar=new CellAttrButtonExt(PropertyType.ShrinkFit,TableConstant.TRUE,getReport());//缩小字体填充
				
				ICommandExt fontStyleToolBar=new CellAttrButtonExt(PropertyType.FontStyle,TableConstant.FS_BOLD,getReport());
			
				ICommandExt horAlignLeftToolBar=new CellAttrButtonExt(PropertyType.HorAlig,TableConstant.HOR_LEFT,getReport());
			
				ICommandExt horAlignCenterToolBar=new CellAttrButtonExt(PropertyType.HorAlig,TableConstant.HOR_CENTER,getReport());
			
				ICommandExt horAlignRightToolBar=new CellAttrButtonExt(PropertyType.HorAlig,TableConstant.HOR_RIGHT,getReport());
			
				ICommandExt combineCellToolBar=new CombineCellToolBarExt(getReport());
				
				return new IExtension[] { fontNameToolBar,fontSizeToolBar,forceColorToolBar,backColorToolBar,cellBorderLineToolBar, fontStyleToolBar,horAlignLeftToolBar,horAlignCenterToolBar,horAlignRightToolBar,changeLineToolBar,shrinkFitToolBar,combineCellToolBar};
			}
			
		};
	}

}
