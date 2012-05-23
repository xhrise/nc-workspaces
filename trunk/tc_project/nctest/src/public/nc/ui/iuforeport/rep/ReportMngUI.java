package nc.ui.iuforeport.rep;

import com.ufida.web.WebException;
import com.ufida.web.action.ActionForward;
import com.ufida.web.comp.menu.WebMenu;
import com.ufida.web.comp.menu.WebMenuItem;
import com.ufida.web.comp.menu.WebMenubar;
import com.ufsoft.iufo.resource.StringResource;
import nc.ui.iufo.analysisrep.AnaExportAction;
import nc.ui.iufo.function.FunctionRightHandler;
import nc.ui.iufo.function.FunctionRightHelper;
import nc.ui.iufo.resmng.uitemplate.FuncToolKit;
import nc.ui.iufo.resmng.uitemplate.TreeTableMngUI;
import nc.ui.iufo.ressearch.ResSearchAction;

public class ReportMngUI extends TreeTableMngUI
{
  private static final long serialVersionUID = -4719064951570065023L;
  private WebMenu menuTool = null;
  private WebMenuItem menuItemDesignFormat = null;
  private WebMenuItem menuItemViewFormat = null;
  private WebMenuItem menuItemFromModel = null;
  private WebMenuItem menuItemToModel = null;
  private WebMenuItem menuItemCustom = null;
  private WebMenuItem menuItemImport = null;
  private WebMenuItem menuItemExport = null;
  private WebMenuItem menuItemToUFO = null;
  private WebMenuItem menuItemSearch = null;
  private WebMenuItem menuItemAddToTask = null;

  protected String getModuleTitle() {
    return StringResource.getStringResource("miufopublic131");
  }

  protected WebMenubar getMenubar1() {
    WebMenubar menubar = super.getMenubar1();
    if (this.menuTool == null) {
      menubar.add(getMenuTool());
    }
    return menubar;
  }

  protected void setData() throws WebException
  {
    setMyMenuLabels();

    WebMenu[] webMenus = { this.menuDir, this.menuFile, this.menuAuth, this.menuTool };
    String[][] funcOrders = { IFuncFlagReportMng.FUNCORDERS_DIR, 
      IFuncFlagReportMng.FUNCORDERS_FILE, 
      IFuncFlagReportMng.FUNCORDERS_AUTH, 
      IFuncFlagReportMng.FUNCORDERS_TOOL };
    FunctionRightHandler.setMenuItemFuncOrders(funcOrders, webMenus);

    super.setData(); }

  protected void clear() {
    super.clear();
    this.menuTool = null;
    this.menuItemDesignFormat = null;
    this.menuItemViewFormat = null;
    this.menuItemFromModel = null;
    this.menuItemToModel = null;
    this.menuItemCustom = null;
    this.menuItemImport = null;
    this.menuItemExport = null;
    this.menuItemToUFO = null;
    this.menuItemSearch = null;
    this.menuItemAddToTask = null;
  }

  protected void appendSelfFileMenuItems(WebMenu menuFile, boolean bSeperateMenu)
  {
    appendSepMenuItem(bSeperateMenu, menuFile);

    this.menuItemSearch = new WebMenuItem();
    String strSearchModuleID = "report_search";
    String strSearchActName = FunctionRightHelper.getSearchAction(ResSearchAction.class.getName(), strSearchModuleID);
    ActionForward fwd = new ActionForward(strSearchActName, "execute");
    fwd.addParameter("moduleId", "report_dir");
    fwd.addParameter("search_module_id", strSearchModuleID);
    this.menuItemSearch.setActionForward(fwd);

    this.menuItemImport = new WebMenuItem();
    String strImportActName = getImportOrExportAction("report_dir", "report_dir", 
      "[import]");
    fwd = new ActionForward(strImportActName, "execute");
    fwd.addParameter("moduleId", "report_dir");
    fwd.addParameter("operType", "importFile");
    FuncToolKit.addParamUniqueId(fwd, getModuleID(), getModuleID(), false);
    this.menuItemImport.setActionForward(fwd);

    this.menuItemImport.setSubmitType(2);

    this.menuItemExport = new WebMenuItem();
    String strExportActName = getImportOrExportAction("report_dir", "report_dir", 
      "[export]");
    fwd = new ActionForward(strExportActName, "execute");
    fwd.addParameter("moduleId", "report_dir");
    fwd.addParameter("operType", "exportFile");
    this.menuItemExport.setActionForward(fwd);
    this.menuItemExport.setSubmitType(1);

    WebMenuItem menuItemXiafa = new WebMenuItem();

    String strXiafaActName = "nc.ui.iufo.analysisrep.AnaExportAction";
    fwd = new ActionForward(strXiafaActName, "xiafa");

    fwd.addParameter("moduleId", "report_dir");

    menuItemXiafa.setActionForward(fwd);
    menuItemXiafa.setSubmitType(1);
    menuItemXiafa.setID("A10A10A20A90");
    menuItemXiafa.setMenuLabel("ÏÂ·¢");	

    menuFile.add(this.menuItemSearch);
    menuFile.add(this.menuItemImport);
    menuFile.add(this.menuItemExport);
    menuFile.add(menuItemXiafa);
  }

  public static String getImportOrExportAction(String strSrcModuleID, String strOperModuleID, String strUIRightType)
  {
    if ((strSrcModuleID == null) || (strOperModuleID == null) || (strUIRightType == null)) {
      return null;
    }
    StringBuffer sbActionName = new StringBuffer(AnaExportAction.class.getName());
    sbActionName.append("&uniqueId=" + strSrcModuleID);
    sbActionName.append("^" + strOperModuleID + "^");
    sbActionName.append(strUIRightType);
    return sbActionName.toString(); }

  private WebMenu getMenuTool() {
    if (this.menuTool == null) {
      this.menuTool = new WebMenu();
      this.menuItemDesignFormat = new WebMenuItem();
      ActionForward fwd = new ActionForward(getRepToolAction("report_dir"), "execute");
      this.menuItemDesignFormat.setActionForward(fwd);
      fwd.addParameter("moduleId", "report_dir");
      this.menuItemDesignFormat.setSubmitType(1);

      this.menuItemViewFormat = new WebMenuItem();
      fwd = new ActionForward(getRepLookAction("report_dir"), "execute");
      fwd.addParameter("moduleId", "report_dir");
      this.menuItemViewFormat.setActionForward(fwd);
      this.menuItemViewFormat.setSubmitType(1);

      this.menuItemCustom = new WebMenuItem();
      fwd = new ActionForward(RepPersonAction.class.getName(), "execute");
      fwd.addParameter("moduleId", "report_dir");
      fwd.addParameter("operType", "repPersonOper");
      this.menuItemCustom.setActionForward(fwd);
      this.menuItemCustom.setSubmitType(1);

      this.menuItemAddToTask = new WebMenuItem();
      fwd = new ActionForward(RepAddToTaskAction.class.getName(), "execute");
      this.menuItemAddToTask.setActionForward(fwd);
      this.menuItemAddToTask.setSubmitType(1);

      this.menuItemFromModel = new WebMenuItem();
      fwd = new ActionForward(ModelToRepAction.class.getName(), "execute");
      fwd.addParameter("moduleId", "report_dir");
      fwd.addParameter("operType", "new");
      this.menuItemFromModel.setActionForward(fwd);
      this.menuItemFromModel.setSubmitType(2);

      this.menuItemToModel = new WebMenuItem();
      fwd = new ActionForward(RepToModelAction.class.getName(), "execute");
      fwd.addParameter("moduleId", "report_dir");
      this.menuItemToModel.setActionForward(fwd);
      this.menuItemToModel.setSubmitType(1);

      this.menuItemToUFO = new WebMenuItem();
      fwd = new ActionForward(ToUfoFormatAction.class.getName(), "execute");
      fwd.addParameter("moduleId", "report_dir");
      this.menuItemToUFO.setActionForward(fwd);
      this.menuItemToUFO.setSubmitType(1);

      this.menuTool.add(this.menuItemDesignFormat);
      this.menuTool.add(this.menuItemViewFormat);
      this.menuTool.add(this.menuItemCustom);
      this.menuTool.add(this.menuItemAddToTask);
      this.menuTool.add(this.menuItemFromModel);
      this.menuTool.add(this.menuItemToModel);
      this.menuTool.add(this.menuItemToUFO);
    }
    return this.menuTool;
  }

  public static String getRepToolAction(String strModuleID)
  {
    if (strModuleID == null) {
      return null;
    }
    StringBuffer sbActionName = new StringBuffer(RepToolAction.class.getName());
    sbActionName.append("&uniqueId=" + strModuleID);
    return sbActionName.toString();
  }

  public static String getRepLookAction(String strModuleID) {
    if (strModuleID == null) {
      return null;
    }
    StringBuffer sbActionName = new StringBuffer(RepLookAction.class.getName());
    sbActionName.append("&uniqueId=" + strModuleID);
    return sbActionName.toString();
  }

  private void setMyMenuLabels()
  {
    this.menuTool.setMenuLabel(StringResource.getStringResource("miufopublic323"));
    this.menuItemDesignFormat.setMenuLabel(StringResource.getStringResource("miufopublic338"));
    this.menuItemViewFormat.setMenuLabel(StringResource.getStringResource("miufopublic339"));
    this.menuItemFromModel.setMenuLabel(StringResource.getStringResource("miufopublic354"));
    this.menuItemToModel.setMenuLabel(StringResource.getStringResource("miufopublic355"));
    this.menuItemCustom.setMenuLabel(StringResource.getStringResource("miufopublic353"));
    this.menuItemAddToTask.setMenuLabel(StringResource.getStringResource("miufoRepMng003"));

    this.menuItemImport.setMenuLabel(StringResource.getStringResource("miufopublic156"));
    this.menuItemExport.setMenuLabel(StringResource.getStringResource("miufopublic157"));
    this.menuItemToUFO.setMenuLabel(StringResource.getStringResource("miufo1003823"));

    this.menuItemSearch.setMenuLabel(StringResource.getStringResource("uiufo50resmng009"));
  }
}