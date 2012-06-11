package com.ufsoft.iufo.fmtplugin.formula;



import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;

/**批量公式定义节点*/
public class BatchFmlExt extends AbsActionExt{

	/**
     * <code>descriptor</code> 的注释
     */
    private final FormulaDescriptor descriptor;

    /**
     * @param descriptor
     */
    BatchFmlExt(FormulaDescriptor descriptor) {
        this.descriptor = descriptor;
    }

	/* (non-Javadoc)
	 * @see com.ufsoft.report.menu.ICommandExt#getCommand()
	 */
	public UfoCommand getCommand() {
		return new BatchFmlCmd();
	}

	/* (non-Javadoc)
	 * @see com.ufsoft.report.menu.ICommandExt#getParams(com.ufsoft.report.UfoReport)
	 */
	public Object[] getParams(UfoReport container) {
	    return new Object[]{container};
	}

    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes = new ActionUIDes();
        uiDes.setName(StringResource.getStringResource("miufo1000966"));
        uiDes.setPaths(new String[]{StringResource.getStringResource("miufo1001692"),
//                StringResource.getStringResource("miufo1001615")
                });
        uiDes.setGroup("formulaDefExt");
        return new ActionUIDes[]{uiDes};
    }		
}