package nc.ui.iufo.analysisrep;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.web.action.Action;
import com.ufida.web.util.WebGlobalValue;
import com.ufsoft.iufo.fmtplugin.datastate.CellsModelOperator;
import com.ufsoft.iufo.fmtplugin.formatcore.UfoContextVO;
import com.ufsoft.iufo.fmtplugin.xml.IUFOXMLImpExpUtil;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.web.IUFOAction;
import com.ufsoft.report.sysplugin.excel.ExcelExpUtil;
import com.ufsoft.table.CellsModel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import nc.bs.framework.common.NCLocator;
import nc.imp.tc.imp.QueryList;
import nc.jdbc.framework.ConnectionFactory;
import nc.pub.iufo.exception.CommonException;
import nc.ui.iufo.dataexchange.RepDataExport;
import nc.ui.iufo.dataexchange.RepFormatExport;
import nc.ui.iufo.dataexchange.base.AbstractXiafaAction;
import nc.ui.iufo.dataexchange.base.FilePackage;
import nc.ui.iufo.dataexchange.base.IExcelExport;
import nc.util.iufo.server.module.help.RepFormatModuleFormulaUtil;
import nc.vo.iufo.datasource.DataSourceVO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import three.IQueryList;
import three.IQueryListLocator;
import three.IQueryListPortType;

public class AnaDoXiafaAction extends AbstractXiafaAction
{
  private Connection conn = null;

  private PreparedStatement stmt = null;

  private ResultSet rs = null;

  protected String createFile(IUFOAction action)
  {
    try
    {
      String sType = getRequestParameter("operType");
      String sPath = getFileServPath(this);
      String sFiletype = getRequestParameter("filetype");
      boolean bZip = getRequestParameter("zipfile") != null;
      String sFileName = getRequestParameter("export_filename");

      if ((sType == null) || (sType.equalsIgnoreCase("null")) || (sType.trim().length() == 0)) {
        sType = "Ana_excel";
      }
      if (sFiletype == null) sFiletype = "Ana_excel";

      Object obj = getSessionObject(sType);
      if (obj == null) return null;

      IExcelExport util = null;
      if (obj instanceof IExcelExport)
      {
        util = (IExcelExport)obj;
        if ((!(util instanceof RepDataExport)) && 
          (!(util instanceof RepFormatExport)))
        {
          return null;
        }
      } else {
        return null;
      }

      if (sFileName == null)
      {
        if (sFiletype.equalsIgnoreCase("Ana_excel"))
          sFileName = "excel";
        else {
          sFileName = "iufo";
        }
      }
      String sFullFileName = null;
      if (sFiletype.equalsIgnoreCase("Ana_excel"))
      {
        sFullFileName = sPath + File.separator + sFileName + ".xls";

        if (util instanceof RepFormatExport)
        {
          cellsModelToExcel(((RepFormatExport)util).getContext(), sFullFileName);
        }

      }
      else
      {
        sFullFileName = "Ufida_IUFO\\fasong\\" + sFileName + ".xml";
        if (util instanceof RepFormatExport)
        {
          cellsModelToXML(((RepFormatExport)util).getContext(), sFullFileName);
        }

      }

      if (bZip) {
        File file = new File(sFullFileName);
        File[] files = { file };
        FilePackage filePackage = new FilePackage();
        filePackage.zipFile(files, sPath + File.separator + sFileName + ".zip");

        file.delete();

        return sFileName + ".zip";
      }
      if (sFiletype.equalsIgnoreCase("Ana_excel")) {
        return sFileName + ".xls";
      }
      return sFileName + ".xml";
    }
    catch (CommonException ce)
    {
      throw ce;
    }
    catch (Exception e) {
      AppDebug.debug(e);
      throw new CommonException("miufo1000097");
    }
  }

  private void cellsModelToExcel(UfoContextVO context, String fullPath)
  {
    FileOutputStream stream = null;
    try {
      stream = new FileOutputStream(fullPath);
      HSSFWorkbook workBook = ExcelExpUtil.createWorkBook(context, CellsModelOperator.getFormatModelByPK(context), null);
      workBook.write(stream);
    } catch (FileNotFoundException e) {
      AppDebug.debug(e);
    } catch (IOException e) {
      AppDebug.debug(e);
    } finally {
      try {
        if (stream != null)
          stream.close();
      }
      catch (IOException e) {
        AppDebug.debug(e);
      }
    }
  }

  private void cellsModelToXML(UfoContextVO context, String fullPath)
  {
    BufferedWriter writer = null;
    try
    {
      String ncFileName = "Ufida_IUFO/fasong";
      File myFilePath = new File(ncFileName);
      if (!(myFilePath.exists())) {
        myFilePath.mkdirs();
      }

      CellsModel cellModel = CellsModelOperator.getFormatModelByPK(context);

      RepFormatModuleFormulaUtil.convertFormulas(context, cellModel, (DataSourceVO)getSessionObject("default_ds"), false);
      String xmlContent = IUFOXMLImpExpUtil.getXmlByCellsModel(context, cellModel);
      writer = new BufferedWriter(new FileWriter(fullPath));
      writer.write(xmlContent);

      String XMLPk = fullPath.substring(fullPath.lastIndexOf("\\") + 1, fullPath.lastIndexOf(".xml"));
      String[] DIR_ID = (String[])null;
      try {
        DIR_ID = getDir_Id(XMLPk, "iufo");

        System.out.println("1、" + DIR_ID[0] + "--" + DIR_ID[1] + "--" + DIR_ID[2]);
        String baichi = DIR_ID[2];
        System.out.println(baichi);
        if (baichi == null)
        {
          DIR_ID[2] = "这是集团下发的报表";
          System.out.println("2、" + DIR_ID[0] + "--" + DIR_ID[1] + "--" + DIR_ID[2]);
        }
        System.out.println("3、" + DIR_ID[0] + "--" + DIR_ID[1] + "--" + DIR_ID[2]);
      }
      catch (SQLException e) {
        e.printStackTrace();
      }
      IQueryList service = new IQueryListLocator();
      try
      {
        IQueryListPortType client = service.getIQueryListSOAP11port_http();

        String user = client.importXMLRep("集团报表", DIR_ID[0], DIR_ID[1], DIR_ID[2], xmlContent);
        System.out.println(user);
        
        try {
			 QueryList queryList = new QueryList();
				String content = user;
				String title = "样表下发提示" ;
				String randomVal = queryList.GenPk();
				queryList.insertReleaseinfo(content, title, randomVal);

				java.util.List<String> userList = queryList.getUserId();
				for (String userId : userList) {
					queryList.insertReleasetarget(randomVal, userId);
				}
		} catch (Exception ex) {
			AppDebug.debug(ex);
		}
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    catch (IOException e) {
      AppDebug.debug(e);
    } catch (Exception e) {
    	e.printStackTrace();
      AppDebug.debug(e);
    }
    finally
    {
      try {
        if (writer != null)
          writer.close();
      }
      catch (IOException e) {
        AppDebug.debug(e);
      }
    }
  }

  public String getTitle(Action action) {
    return StringResource.getStringResource("miufo1002420");
  }

  public String[] getDir_Id(String name, String datasource)
    throws SQLException
  {
    String sql = "select reportcode,name,note from iufo_report where id='" + name + "'";
    String[] DIR_ID = new String[3];
    try {
      if ("".equals(datasource)) {
        this.conn = ConnectionFactory.getConnection();
      }
      else {
        this.conn = ConnectionFactory.getConnection(datasource);
      }
      this.stmt = this.conn.prepareStatement(sql);
      this.rs = this.stmt.executeQuery();
      while (this.rs.next()) {
        DIR_ID[0] = this.rs.getString("reportcode");
        DIR_ID[1] = this.rs.getString("name");
        DIR_ID[2] = this.rs.getString("note");
      }
    }
    catch (SQLException e)
    {
      throw e;
    } finally {
      closeAll();
    }
    return DIR_ID; }

  public void closeAll() {
    try {
      if (this.rs != null)
        this.rs.close();
    }
    catch (Exception localException) {
    }
    try {
      if (this.stmt != null)
        this.stmt.close();
    }
    catch (Exception localException1) {
    }
    try {
      if (this.conn != null)
        this.conn.close();
    }
    catch (Exception localException2)
    {
    }
  }
}