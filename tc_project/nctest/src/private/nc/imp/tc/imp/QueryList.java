package nc.imp.tc.imp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.naming.NamingException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.xml.sax.SAXException;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.web.WebException;
import com.ufida.web.action.ActionForm;
import com.ufida.web.action.ActionForward;
import com.ufida.web.action.CloseForward;
import com.ufida.web.action.ErrorForward;
import com.ufida.web.action.MessageForward;
import com.ufida.web.util.WebGlobalValue;

import com.ufsoft.iufo.check.vo.CheckResultVO;
import com.ufsoft.iufo.inputplugin.biz.data.AutoImportExcelDataBizUtil;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.web.IUFOAction;

import com.ufsoft.table.CellsModel;

import nc.pub.iufo.cache.ReportCache;
import nc.pub.iufo.exception.CommonException;
import nc.ui.iuforeport.rep.RepSaveImportAction;
import nc.ui.iuforeport.rep.ReportEditAction;

import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.iufo.constants.IIUFOConstants;
import nc.ui.iufo.dataexchange.ImportExcelCheckResultAction;
import nc.ui.iufo.dataexchange.AutoMultiSheetImportAction;
import nc.ui.iufo.dataexchange.MultiSheetImportForm;
import nc.ui.iufo.dataexchange.AutoMultiSheetImportUtil;
import nc.ui.iufo.input.CSomeParam;
import nc.ui.iufo.resmng.common.ResUIBizHelper;
import nc.ui.iufo.resmng.common.ResWebParam;
import nc.ui.iufo.resmng.uitemplate.IResTreeObjForm;

import nc.ui.iuforeport.rep.RepSaveImportForm;

import nc.imp.tc.imp.BasDMO;
import nc.itf.tc.imp.IQueryList;

import nc.util.iufo.resmng.IResMngConsants;
import nc.util.iufo.resmng.ResMngToolKit;

import nc.vo.iufo.data.MeasurePubDataVO;
import nc.vo.iufo.datasource.DataSourceVO;
import nc.vo.iufo.resmng.uitemplate.IResTreeObject;
import nc.vo.iufo.resmng.uitemplate.ResOperException;
import nc.vo.iufo.user.UserInfoVO;
import nc.vo.jcom.xml.XMLUtil;
import nc.vo.pub.ValueObject;

public class QueryList {
	// 导出成XML的数据源
	// private final String ORACLEDATASOURCE = "bym";
	// 服务器的
	private final String ORACLEDATASOURCE = "iufo";

	// 数据层
	private BasDMO dmo = null;

	public void getLikeFuncCount() {
		try {
			dmo = new BasDMO();
		
			int num = 0;
			num = dmo.getLikeFuncCount(ORACLEDATASOURCE);
			
			if(num == 0){
				dmo.insertFunc(ORACLEDATASOURCE);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getReportPk(String reportcode) throws NamingException, SQLException{
		dmo = new BasDMO();
		return dmo.getReportPk(reportcode, ORACLEDATASOURCE);
	}
	
//	public void insertReportCommit(String id , String aloneId , String unit_id) throws NamingException, SQLException{
//		dmo = new BasDMO();
//		dmo.insertReportCommit(id, aloneId , unit_id , ORACLEDATASOURCE);
//	}
	
	public void insertReportCommit(String id , String aloneId) throws NamingException, SQLException{
		dmo = new BasDMO();
		dmo.insertReportCommit(id, aloneId , ORACLEDATASOURCE);
	}
	
	public void updateReportCommit() throws NamingException, SQLException {
		dmo = new BasDMO();
		dmo.updateReportCommit(ORACLEDATASOURCE);
	}
	
//	public int alterReportCommit() throws Exception {
//		dmo = new BasDMO();
//		return dmo.alterReportCommit(ORACLEDATASOURCE);
//	}
	
	public String getUnitID(String unit_code) throws Exception {
		dmo = new BasDMO();
		return dmo.getUnitID(unit_code, ORACLEDATASOURCE);
	}
	
	public int updateCancleCommit(String unit_name,String reportcodes) throws Exception {
		dmo = new BasDMO();
		return dmo.updateCancleCommit(unit_name, reportcodes, ORACLEDATASOURCE);
	}
	
	public int insertReleaseinfo(String content, String title, String bbsid) throws Exception{
		dmo = new BasDMO();
		return dmo.insertReleaseinfo(content, title, bbsid, ORACLEDATASOURCE);
	}
	
	public int insertReleasetarget(String bbsid, String user_id) throws Exception{
		dmo = new BasDMO();
		return dmo.insertReleasetarget(bbsid, user_id, ORACLEDATASOURCE);
	}
	
	public List<String> getUserId() throws Exception{
		dmo = new BasDMO();
		return dmo.getUserId(ORACLEDATASOURCE);
	}
	
	public String GenPk() throws Exception{
		dmo = new BasDMO();
		return dmo.GenPk();
	}

}
