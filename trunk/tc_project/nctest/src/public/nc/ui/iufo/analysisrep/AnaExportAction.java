package nc.ui.iufo.analysisrep;

import com.ufida.web.action.ActionForm;
import com.ufida.web.action.ActionForward;
import com.ufsoft.iufo.fmtplugin.formatcore.UfoContextVO;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.web.DialogAction;
import java.io.PrintStream;
import nc.pub.iufo.exception.CommonException;
import nc.ui.iufo.dataexchange.RepFormatExport;
import nc.ui.iufo.input.InputUtil;
import nc.ui.iufo.resmng.common.AuthUIBizHelper;
import nc.ui.iufo.resmng.common.ResWebEnvKit;
import nc.util.iufo.resmng.ResMngBizHelper;
import nc.util.iufo.resmng.ResMngToolKit;
import nc.vo.iufo.resmng.uitemplate.ResOperException;
import nc.vo.iuforeport.rep.ReportVO;

public class AnaExportAction extends DialogAction
{
  public static final String FILETYPE = "filetype";
  public static final String ZIP = "ZIP";
  public static final String FLIE_NAME = "fileName";
  public static final String EXCEL = "Ana_excel";
  public static final String XML = "Ana_xml";
  public static final String EXPORT_FILENAME = "export_filename";
  public static final String ZIPFILE = "zipfile";
  public static final String XMLTYPE = "Ana_xmltype";
  public static final String XMLTYPE_REPORT = "report";
  public static final String XMLTYPE_TABLE = "table";

  public ActionForward xiafa(ActionForm actionForm)
  {
    ActionForward actionForward = null;
    System.out.print("这里是下发的方法");

    String operType = getRequestParameter("operType");
    if (operType == null)
      operType = "exportFile";
    String strModuleID = getRequestParameter("moduleId");
    String strHtmlType = getRequestParameter("Ana_xmltype");
    if (strHtmlType == null)
      strHtmlType = "table";
    String[] selItem = (String[])null;
    String sFilename = null;

    selItem = getTableSelectedIDs();

    if (ResWebEnvKit.isDir(this)) {
      throw new CommonException("miufo1002421");
    }
    if ((selItem == null) || (selItem.length == 0)) {
      throw new CommonException("miufo1002422");
    }
    sFilename = ResMngToolKit.getVOIDByTreeObjectID(selItem[0]);

    String userId = ResWebEnvKit.getLoginUserPK(this);
    String strResOwnerPK = null;
    try {
      strResOwnerPK = ResMngBizHelper.getResOwnerPK(strModuleID, userId);
    } catch (ResOperException e) {
      throw new CommonException(e.getMessage());
    }
    ReportVO repVO = nc.ui.iufo.cache.IUFOUICacheManager.getSingleton().getReportCache().getByPks(new String[] { sFilename })[0];

    UfoContextVO context = new UfoContextVO();
    context.setContextId(sFilename);
    context.setCurUnitId(strResOwnerPK);
    context.setName(StringResource.getStringResource("miufopublic178"));

    String strCreateUnitId = InputUtil.getCreateUnitID(repVO, strModuleID);
    context.setCreateUnitId(strCreateUnitId);
    context.setCurUserId(userId);
    context.setOrgPK(getCurOrgPK());

    RepFormatExport excelExport = new RepFormatExport();
    excelExport.setContext(context);
    addSessionObject(operType, excelExport);

    actionForward = new ActionForward(AnaDoExportAction.class.getName(), "execute");

    actionForward.addParameter("operType", operType);
    actionForward.addParameter("export_filename", sFilename);
    actionForward.addParameter("Ana_xmltype", strHtmlType);
    actionForward.setActionName("nc.ui.iufo.analysisrep.AnaDoXiafaAction");
    actionForward.addParameter("filetype", "Ana_xml");
    addSessionObject("AnaExportActionExecuteForward", actionForward);

    return actionForward; 
    }

  public ActionForward execute(ActionForm actionForm) {
    ActionForward actionForward = null;
    AnaExportForm form = (AnaExportForm)actionForm;

    actionForward = new ActionForward(AnaExportDlg.class.getName());

    String strModuleID = getRequestParameter("moduleId");
    boolean bAnaRep = true;
    if ((strModuleID != null) && (strModuleID.equals("report_dir"))) {
      bAnaRep = false;
    }
    String operType = getRequestParameter("operType");
    String titletext = StringResource.getStringResource("miufopublic157");
    boolean isExport = true;
    if ("importFile".equals(operType))
    {
      checkRight(getTreeSelectedID());

      titletext = StringResource.getStringResource("miufopublic156");
      isExport = false;
    }
    form.setExport(isExport);

    String[] selItem = (String[])null;
    String sFilename = null;
    if (isExport) {
      selItem = getTableSelectedIDs();

      if (ResWebEnvKit.isDir(this)) {
        throw new CommonException("miufo1002421");
      }
      if ((selItem == null) || (selItem.length == 0)) {
        throw new CommonException("miufo1002422");
      }
      sFilename = ResMngToolKit.getVOIDByTreeObjectID(selItem[0]);

      String userId = ResWebEnvKit.getLoginUserPK(this);
      String strResOwnerPK = null;
      try {
        strResOwnerPK = ResMngBizHelper.getResOwnerPK(strModuleID, userId);
      } catch (ResOperException e) {
        throw new CommonException(e.getMessage());
      }
      ReportVO repVO = nc.ui.iufo.cache.IUFOUICacheManager.getSingleton().getReportCache().getByPks(new String[] { sFilename })[0];

      UfoContextVO context = new UfoContextVO();
      context.setContextId(sFilename);
      context.setCurUnitId(strResOwnerPK);
      context.setName(StringResource.getStringResource("miufopublic178"));

      String strCreateUnitId = InputUtil.getCreateUnitID(repVO, strModuleID);
      context.setCreateUnitId(strCreateUnitId);
      context.setCurUserId(userId);
      context.setOrgPK(getCurOrgPK());

      RepFormatExport excelExport = new RepFormatExport();
      excelExport.setContext(context);
      addSessionObject(operType, excelExport);
    } else {
      selItem = new String[1];

      selItem[0] = getTreeSelectedID();
    }

    if (!(bAnaRep)) {
      form.setTitle(StringResource.getStringResource("miufopublic131") + 
        titletext + 
        StringResource.getStringResource("miufo1002423"));
    }
    else {
      form.setTitle(StringResource.getStringResource("miufopublic139") + 
        titletext + 
        StringResource.getStringResource("miufo1002423"));
    }

    String strHtmlType = getRequestParameter("Ana_xmltype");
    if (strHtmlType == null) {
      strHtmlType = "table";
    }
    ActionForward fwd = null;
    if (isExport) {
      fwd = new ActionForward(AnaDoExportAction.class.getName(), "execute");
      fwd.addParameter("operType", operType);
      fwd.addParameter("export_filename", sFilename);
      fwd.addParameter("Ana_xmltype", strHtmlType);
    }
    else
    {
      fwd = new ActionForward(AnaImportAction.class.getName(), "execute");
      fwd.addParameter("moduleId", strModuleID);
      fwd.addParameter("oper_obj_type", "obj_dir");
      fwd.addParameter(AnaRepMngUI.REP_DIR, selItem[0]);
    }

    addSessionObject("AnaExportActionExecuteForward", fwd);

    return actionForward;
  }

  protected void checkRight(String strSelectSrcID)
  {
    AuthUIBizHelper.checkRight(strSelectSrcID, this);
  }

  public ActionForward onSubmit(ActionForm actionForm) {
    ActionForward actionForward = (ActionForward)getSessionObject("AnaExportActionExecuteForward");
    AnaExportForm form = (AnaExportForm)actionForm;

    String export = getRequestParameter("export");
    if ((Boolean.valueOf(export).booleanValue()) && 
      (form.isZipFile())) {
      actionForward.addParameter("zipfile", "ZIP");
    }

    actionForward.addParameter("filetype", form.getFielType());
    return actionForward;
  }

  public String[] validate(ActionForm actionForm)
  {
    return null;
  }

  public String getFormName()
  {
    return AnaExportForm.class.getName();
  }
}