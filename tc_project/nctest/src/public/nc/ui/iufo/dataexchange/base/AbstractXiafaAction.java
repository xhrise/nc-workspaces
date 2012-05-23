package nc.ui.iufo.dataexchange.base;

import com.ufida.web.WebException;
import com.ufida.web.action.Action;
import com.ufida.web.action.ActionForm;
import com.ufida.web.action.ActionForward;
import com.ufida.web.action.ErrorForward;
import com.ufida.web.util.WebGlobalValue;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.web.DialogAction;
import com.ufsoft.iufo.web.IUFOAction;
import java.io.File;
import java.util.Calendar;
import java.util.Hashtable;
import nc.util.iufo.pub.IDMaker;

public abstract class AbstractXiafaAction extends DialogAction
{
  public static Hashtable<String, String> m_hashSessionID = new Hashtable();
  public static Hashtable<String, Boolean> m_hashExportID = new Hashtable();

  public ActionForward execute(ActionForm actionForm)
    throws WebException
  {
    AbstractExportForm form = (AbstractExportForm)actionForm;
    try {
      form.setDlgTitle(getTitle(this));
      form.setLabelHint(getHint());
      String strWebPath = getFileUrlPath(this) + File.separator + innerCreateFile();
      form.setFileUrlPath(strWebPath);
      form.setHost(getRequestScheme() + "://" + getServerName() + ":" + getServerPort());
    }
    catch (Exception e)
    {
      return new ErrorForward(e.getMessage());
    }

    if (getExportUIClass().equals(UFOPrintUI.class.getName())) {
      return new ActionForward(AbstractXiafaDlg.class.getName());
    }
    return new ActionForward(AbstractXiafaDlg.class.getName());
  }

  protected String innerCreateFile() throws Exception
  {
    String strFileName = createFile(this);

    Calendar date = Calendar.getInstance();
    String strDate = "" + ((date.get(11) >= 10) ? Integer.valueOf(date.get(11)) : new StringBuilder("0").append(date.get(11)).toString()) + 
    ((date.get(12) >= 10) ? Integer.valueOf(date.get(12)) : new StringBuilder("0").append(date.get(12)).toString()) + 
    ((date.get(13) >= 10) ? Integer.valueOf(date.get(13)) : new StringBuilder("0").append(date.get(13)).toString());
    String strNewFileName = strDate + "_" + strFileName;

    File file = new File(getFileServPath(this) + File.separator + strFileName);
    file.renameTo(new File(getFileServPath(this) + File.separator + strNewFileName));

    return strNewFileName;
  }

  public String[] validate(ActionForm actionForm)
  {
    return null;
  }

  public String getFormName()
  {
    return AbstractExportForm.class.getName();
  }

  protected abstract String createFile(IUFOAction paramIUFOAction);

  public String getFileServPath(Action action)
  {
    StringBuffer sbuf = new StringBuffer();

    String sbufs = WebGlobalValue.WORK_DIR;
    sbufs = sbufs.substring(0, sbufs.length() - 1);
    sbuf.append(sbufs);

    sbuf.append(getSubPath());
    sbuf.append(File.separator);
    sbuf.append(getExportID(action.getSessionId()));
    String strPath = sbuf.toString();

    File file = new File(strPath);
    if (!(file.exists())) {
      file.mkdirs();
    }
    return strPath;
  }

  public String getFileUrlPath(Action action)
  {
    StringBuffer strPath = new StringBuffer();

    strPath.append(getSubPath());
    strPath.append(File.separator);
    strPath.append(getExportID(action.getSessionId()));
    return strPath.toString();
  }

  protected String getHint()
  {
    String strHint = StringResource.getStringResource("miufo1002419");
    return strHint + "...";
  }

  public String getSubPath()
  {
    return IDataExchange.PACKAGE_PATH;
  }

  public abstract String getTitle(Action paramAction);

  public static synchronized String getExportID(String strSessionID)
  {
    if (strSessionID == null) {
      return strSessionID;
    }
    String strExportID = (String)m_hashSessionID.get(strSessionID);
    if (strExportID != null) {
      return strExportID;
    }
    int iCount = 0;
    while (iCount < 40) {
      strExportID = IDMaker.makeID(30);
      if (m_hashExportID.get(strExportID) == null) {
        m_hashSessionID.put(strSessionID, strExportID);
        m_hashExportID.put(strExportID, new Boolean(true));
        return strExportID;
      }
      ++iCount;
    }
    return strExportID;
  }

  protected String getExportUIClass() {
    return AbstractExportDlg.class.getName();
  }
}