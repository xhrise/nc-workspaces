package nc.ui.iufo.dataexchange.base;

import com.ufida.web.WebException;
import com.ufida.web.comp.WebButton;
import com.ufida.web.comp.WebCloseButton;
import com.ufida.web.container.WebFlowLayout;
import com.ufida.web.container.WebPanel;
import com.ufida.web.html.A;
import com.ufida.web.window.WebDialog;

public class AbstractXiafaDlg extends WebDialog
{
  private static final long serialVersionUID = -2834384580190679881L;
  private WebButton btnClose = null;
  private WebPanel dlgPane = null;
  private WebPanel fieldPane = null;
  private WebPanel btnPane = null;
  private A APath = null;

  protected void setData()
    throws WebException
  {
    AbstractExportForm form = (AbstractExportForm)getActionForm(AbstractExportForm.class.getName());
    if (form == null) {
      return;
    }
    setTitle(form.getDlgTitle());
    getWebPathLabel().setID("lb_filename");
    getWebPathLabel().setValue("下发成功！");
    getWebPathLabel().setHref("#");

    getBtnClose().setValue("确定");
  }

  protected void initUI()
  {
    setWindowWidth(300);
    setWindowHeight(200);
    disableAutoResize();

    setContentPane(getDlgPane());
  }

  protected A getWebPathLabel()
  {
    if (this.APath == null) {
      this.APath = new A();
    }

    return this.APath;
  }

  protected WebPanel getFieldPane()
  {
    if (this.fieldPane == null) {
      this.fieldPane = new WebPanel();

      this.fieldPane.setLayout(new WebFlowLayout("center", "horizontal"));
      this.fieldPane.add(getWebPathLabel());
    }

    return this.fieldPane;
  }

  protected WebButton getBtnClose() {
    if (this.btnClose == null) {
      this.btnClose = new WebCloseButton();
    }
    return this.btnClose;
  }

  protected WebPanel getBtnPane() {
    if (this.btnPane == null) {
      this.btnPane = new WebPanel();
      this.btnPane.setLayout(new WebFlowLayout("center", "horizontal"));
      this.btnPane.add(getBtnClose());
    }

    return this.btnPane;
  }

  protected WebPanel getDlgPane()
  {
    if (this.dlgPane == null) {
      this.dlgPane = new WebPanel();
      this.dlgPane.setLayout(new WebFlowLayout("center", "vertical"));
      this.dlgPane.add(getFieldPane());
      this.dlgPane.add(getBtnPane());
    }
    return this.dlgPane;
  }
}