package com.ufida.report.complexrep.applet;

import com.ufida.report.rep.applet.BIReportPreViewExt;
import com.ufida.report.rep.applet.BIReportSaveExt;
import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPlugIn;

/**
 * 
 * @author zzl 2005-5-9
 */
public class ComplexRepDesc extends AbstractPlugDes {

    public ComplexRepDesc(IPlugIn plugin) {
        super(plugin);
    }

    protected IExtension[] createExtensions() {
        ComplexRepPlugin pi = (ComplexRepPlugin)getPlugin();

        return new IExtension[]{
        		new BIReportSaveExt(pi),
//              new OperationStateExt(pi),
				new BIReportPreViewExt(pi),

				new ReportMenuExt(),
                new AddSubRepExt(pi),
                new EditSubRepExt(pi),
                new DelSubReportExt(pi),
				new SaveAsRepExt(pi),
                new SetPropertyExt(pi)
                };
    }

}
