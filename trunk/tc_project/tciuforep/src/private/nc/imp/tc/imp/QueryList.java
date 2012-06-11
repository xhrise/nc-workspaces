package nc.imp.tc.imp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.NamingException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.SAXException;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.web.WebException;
import com.ufida.web.action.ActionForm;
import com.ufida.web.action.ActionForward;
import com.ufida.web.action.CloseForward;
import com.ufida.web.action.ErrorForward;
import com.ufida.web.action.MessageForward;
import com.ufida.web.util.WebGlobalValue;

import com.ufsoft.iufo.fmtplugin.formula.BatchFmlDlg;
import com.ufsoft.iufo.fmtplugin.formula.FormulaModel;
import com.ufsoft.iufo.fmtplugin.formula.UfoFmlExecutor;
import com.ufsoft.iufo.resource.StringResource;

import com.ufsoft.script.exception.ParseException;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.IArea;

import nc.pub.iufo.cache.ReportCache;
import nc.pub.iufo.cache.base.IUFOCacheException;
import nc.ui.iuforeport.rep.RepSaveImportAction;
import nc.ui.iuforeport.rep.ReportEditAction;
import nc.ui.iuforeport.rep.ReportForm;

import nc.ui.iufo.analysisrep.RepFiledImpl;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.iufo.constants.IIUFOConstants;
import nc.ui.iufo.resmng.common.ResUIBizHelper;
import nc.ui.iufo.resmng.common.ResWebParam;
import nc.ui.iufo.resmng.common.UISrvException;
import nc.ui.iufo.resmng.uitemplate.IResTreeObjForm;
import nc.ui.iufo.resmng.uitemplate.ResEditObjAction;
import nc.ui.iufo.resmng.uitemplate.ResTreeObjForm;

import nc.ui.iuforeport.rep.RepSaveImportForm;

import nc.imp.tc.imp.BasDMO;
import nc.itf.tc.imp.IQueryList;

import nc.util.iufo.iufo.resmng.IIUFOResMngConsants;
import nc.util.iufo.pub.CodeNameMaker;
import nc.util.iufo.pub.UfoException;
import nc.util.iufo.resmng.IResMngConsants;
import nc.util.iufo.resmng.ResMngToolKit;

import nc.vo.iufo.datasource.DataSourceVO;
import nc.vo.iufo.resmng.uitemplate.IResTreeObject;
import nc.vo.iufo.resmng.uitemplate.ResOperException;
import nc.vo.iuforeport.rep.ReportVO;
import nc.vo.jcom.xml.XMLUtil;
import nc.vo.pub.ValueObject;
import nc.vo.tc.imp.KeywordVO;

public class QueryList extends ReportEditAction implements IQueryList {
	// 导出成XML的数据源
	// private final String ORACLEDATASOURCE = "bym";
	// 服务器的
	// private final String ORACLEDATASOURCE = "tcg";
	private String ORACLEDATASOURCE = this.getDatasourse();

	// 数据层
	private BasDMO dmo = null;

	// 设置导入格式
	private final String selFileType = "Ana_xml";

	// 设置为报表表样
	private final String moduleId = "report_dir";

	//
	private RepSaveImportAction aciton1 = new RepSaveImportAction();

	private File xmlList = new File("Ufida_IUFO/jieshou/xmlList.xml");

	private String iufoName = "";

	private CellsModel cModel;

	public String testText(String string) {
		return string;
	}

	// 导入XML到数据库
	public String SaveXMLRep(String moduleName, String filePk, String fileName,
			String note, File file, String ID) {

		RepSaveImportForm form = new RepSaveImportForm();

		try {
			dmo = new BasDMO();
			// by fdh
			String DIR_ID = dmo.getDir_Id(moduleName, ORACLEDATASOURCE);
			form.setFiletype(selFileType);
			// 表
			form.setBizTableSelectedID("");
			// 目录
			form.setBizTreeSelectedID(DIR_ID);
			// 文件名
			form.setFilename(file.getName());
			String strOldRepPK = (file.getName().substring(0, file.getName()
					.length() - 4));
			form.setID(DIR_ID + "^0");
			form.setHBIntrade(false);
			form.setFormulaIsEdit(0);
			// 编码
			form.setReportCode(filePk);
			form.setName(fileName);
			form.setNote(note);
			// 修改原有表名，通过系统接口。
			// ReportCache reportCache =
			// IUFOUICacheManager.getSingleton().getReportCache();
			// 清除caches
			// IUFOUICacheManager.clearAllCaches();
			// IUFOUICacheManager.getSingleton().getReportCache().clear();
			// 保存文件进数据库

			ActionForward fwd = this.saveFile(form, file);
			// IUFOUICacheManager.clearAllCaches();
			// 更新文件状态--如果成功了则更新，不成功则不更新

			if (fwd instanceof CloseForward) {
				this.UpdateXmlListType(fileName);
				// 更新任务功能写入
				// 旧ID不能为0，也就是新更新的。
				if (ID != "0" || (!ID.equals("0"))) {
					// 获得新的文件的主键
					String[] report = dmo.getReportnamecode(filePk,
							ORACLEDATASOURCE);
					int restss = dmo.updateReporttaskset(ID, report[2],
							ORACLEDATASOURCE);
					// IUFOUICacheManager.getSingleton().getTaskCache().clear();
					IUFOUICacheManager.clearAllCaches();
					if (restss == 1) {
						return "更新失败";
					} else {
						return "更新成功";
					}
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "失败";
		}

		return "成功";
	}

	// 执行 导入的方法
	public synchronized void getAllxmlList() throws SAXException, IOException, NamingException, SQLException {

//		if(true) 
//			return ;
		
		String Id = null;
		org.w3c.dom.Document document = XMLUtil.getDocumentBuilder().parse(
				xmlList);
		org.w3c.dom.NodeList lists = document.getElementsByTagName("list");
		// 判断是否已经存在这个文件
		
		KeywordDMO keyDmo = new KeywordDMO();
		try{
			dmo = new BasDMO();
			List<KeywordVO> keyList = keyDmo.getKeywords();
			List<KeywordVO> basKeyList = dmo.getKeywords(ORACLEDATASOURCE);
			if(basKeyList.size() == 0){
				for(KeywordVO keyword : keyList){
					dmo.insertKeyword(keyword, ORACLEDATASOURCE);
				}
			}else{
				boolean check = true;
				for(KeywordVO key : keyList){
					for(KeywordVO basKey : basKeyList){
						if(basKey.getName().equals(key.getName()))
							check = false;
					}
					
					if(check)
						dmo.insertKeyword(key, ORACLEDATASOURCE);
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		for (int s = 0; s < lists.getLength(); s++) {
			try {
			
			String xmltype = document.getElementsByTagName("type").item(s)
					.getFirstChild().getNodeValue();
			String reyst = null;
			String reyst2 = null;
			String filePk = document.getElementsByTagName("ID").item(s)
					.getTextContent();
			// 0为未导入 1未已导入
			boolean checkRep = false;
			if (xmltype.equals("0") || xmltype == "0") {
				
				if (!filePk.equals("0") && filePk != "0") {
					String[] report = new String[3];
					try {
						String date = new SimpleDateFormat("MMddssSSS")
								.format(new Date());
						dmo = new BasDMO();
						report = dmo.getOldReportnamecode(filePk,
								ORACLEDATASOURCE);
						reyst = "备份表：名称：" + report[1] + "编号:" + report[0];
						String[] oldReport = report;
						int rest = 0;
						
						if (report[0] != null
								&& report[0].equals(document
										.getElementsByTagName("filePk").item(s)
										.getTextContent())) {
							report[0] += date;
							report[1] += date;
							rest = dmo.updateReportnamecode(report[0],
									report[1], report[2], ORACLEDATASOURCE);
						}
						
						if (rest > 0) {
							reyst2 = "原表改名为：名称：" + report[1] + "编号:"
									+ report[0];
							
							Id = report[2];
							
							
							String content = reyst + "\n" + reyst2;
							String title = "集团样表下发消息提示" ;
							String randomVal = dmo.GenPk();
							dmo.insertReleaseinfo(content, title, randomVal,ORACLEDATASOURCE);

							java.util.List<String> userList = dmo.getUserId(ORACLEDATASOURCE);
							for (String userId : userList) {
								dmo.insertReleasetarget(randomVal, userId,ORACLEDATASOURCE);
							}
							
							System.out.println(reyst);
							System.out.println(reyst2);
							checkRep = true;
							
						}else{
							checkRep = false;
						}
					} catch (Exception ex) {
						System.out.println("备份表出现未知错误");
						return;
					}
				}

				String xmlmoduleName = document.getElementsByTagName(
						"moduleName").item(s).getFirstChild().getNodeValue();
				String xmlfilePk = document.getElementsByTagName("filePk")
						.item(s).getFirstChild().getNodeValue();
				String xmlfileName = document.getElementsByTagName("fileName")
						.item(s).getFirstChild().getNodeValue();
				String xmlnote = document.getElementsByTagName("note").item(s)
						.getFirstChild().getNodeValue();
				String xmlfile = document.getElementsByTagName("file").item(s)
						.getFirstChild().getNodeValue();
				String xmlID = document.getElementsByTagName("ID").item(s)
						.getFirstChild().getNodeValue();

				// ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
				// 在保存报表表样前将502传来的XML文件中的公式替换成56识别的公式
				// modify by yh for 2011年3月10日9:55:40
				org.w3c.dom.Document newDoc = XMLUtil.getDocumentBuilder()
						.parse(xmlfile);
				org.w3c.dom.NodeList formulaContent = newDoc
						.getElementsByTagName("formulaContent");
				org.w3c.dom.NodeList keys = newDoc.getElementsByTagName("key");

				List<String> keysList = new ArrayList<String>();
				for (int i = 0; i < keys.getLength(); i++) {
					if (keys.item(i).getAttributes().item(0).getNodeValue() != null
							&& keys.item(i).getAttributes().item(0)
									.getNodeValue().equals(
											"com.ufsoft.table.AreaPosition"))
						keysList.add(keys.item(i).getTextContent());
				}

				String formulas = "";
				StringBuffer tableStr = new StringBuffer();
				for (int i = 0; i < formulaContent.getLength(); i++) {

					String key = keysList.get(i);
					String formula = formulaContent.item(i).getTextContent();

					String[] strs = formula.split("000000000000");
					String str = "";
					for (int j = 0; j < strs.length; j++) {
						if (strs.length == 1) {
							str = strs[j];
						} else {
							if (j < strs.length - 1) {
								str = str + strs[j] + "单位";
							} else {
								str = "" + str + strs[j];
							}
						}
					}

					strs = str.toString().split("000000000001");
					for (int j = 0; j < strs.length; j++) {
						if (strs.length == 1) {
							str = strs[j];
						} else {
							if (j < strs.length - 1)
								str += strs[j] + "年";
							else
								str += strs[j];
						}
					}

					strs = str.toString().split("000000000002");
					for (int j = 0; j < strs.length; j++) {
						if (strs.length == 1) {
							str = strs[j];
						} else {
							if (j < strs.length - 1)
								str += strs[j] + "半年";
							else
								str += strs[j];
						}
					}

					strs = str.toString().split("000000000003");
					for (int j = 0; j < strs.length; j++) {
						if (strs.length == 1) {
							str = strs[j];
						} else {
							if (j < strs.length - 1)
								str += strs[j] + "季度";
							else
								str += strs[j];
						}
					}

					strs = str.toString().split("000000000004");
					for (int j = 0; j < strs.length; j++) {
						if (strs.length == 1) {
							str = strs[j];
						} else {
							if (j < strs.length - 1)
								str += strs[j] + "月";
							else
								str += strs[j];
						}
					}

					strs = str.toString().split("000000000005");
					for (int j = 0; j < strs.length; j++) {
						if (strs.length == 1) {
							str = strs[j];
						} else {
							if (j < strs.length - 1)
								str += strs[j] + "旬";
							else
								str += strs[j];
						}
					}

					strs = str.toString().split("000000000006");
					for (int j = 0; j < strs.length; j++) {
						if (strs.length == 1) {
							str = strs[j];
						} else {
							if (j < strs.length - 1)
								str += strs[j] + "周";
							else
								str += strs[j];
						}
					}

					strs = str.toString().split("000000000007");
					for (int j = 0; j < strs.length; j++) {
						if (strs.length == 1) {
							str = strs[j];
						} else {
							if (j < strs.length - 1)
								str += strs[j] + "日";
							else
								str += strs[j];
						}
					}

					strs = str.toString().split("000000000008");
					for (int j = 0; j < strs.length; j++) {
						if (strs.length == 1) {
							str = strs[j];
						} else {
							if (j < strs.length - 1)
								str += strs[j] + "对方单位编码";
							else
								str += strs[j];
						}
					}

					strs = str.toString().split("000000000009");
					for (int j = 0; j < strs.length; j++) {
						if (strs.length == 1) {
							str = strs[j];
						} else {
							if (j < strs.length - 1)
								str += strs[j] + "币种";
							else
								str += strs[j];
						}
					}

					strs = str.toString().split("000000000010");
					for (int j = 0; j < strs.length; j++) {
						if (strs.length == 1) {
							str = strs[j];
						} else {
							if (j < strs.length - 1)
								str += strs[j] + "会计年";
							else
								str += strs[j];
						}
					}

					strs = str.toString().split("000000000011");
					for (int j = 0; j < strs.length; j++) {
						if (strs.length == 1) {
							str = strs[j];
						} else {
							if (j < strs.length - 1)
								str += strs[j] + "会计季度";
							else
								str += strs[j];
						}
					}

					strs = str.toString().split("000000000012");
					for (int j = 0; j < strs.length; j++) {
						if (strs.length == 1) {
							str = strs[j];
						} else {
							if (j < strs.length - 1)
								str += strs[j] + "会计月";
							else
								str += strs[j];
						}
					}

					newDoc.getElementsByTagName("formulaContent").item(i)
							.getFirstChild().setNodeValue(str);
					
					String[] formulaSlt = new String[]{};
					try {
						formulaSlt = str.toString().split("MSELECT\\(\'");

						if (formulaSlt.length > 1) {
							for (int j = 0; j < formulaSlt.length; j++) {
								try{
									String val = formulaSlt[j].substring(0,formulaSlt[j].indexOf("->"));
									String[] tableSpl = tableStr.toString().split(";");
									boolean check = true;
									for(int k = 0 ; k < tableSpl.length ; k++){
										if(val.equals(tableSpl[k])){
											check = false;
											break;
										}
									}
									if(check)
										tableStr.append(formulaSlt[j].substring(0,formulaSlt[j].indexOf("->")) + ";");

								}catch(Exception e){
									continue;
								}
							}
						}
					} catch (Exception ex) {
					}
					
					formula = key + "=" + str + ";\r\n";
					formulas += formula;
				}

				toSave(newDoc, xmlfile);

				String paths = "Ufida_IUFO/formula/";
				File path = new File(paths);
				if (!path.exists()) {
					path.mkdirs();
				}
				

				//String formulaFile = "Ufida_IUFO/formula/" + xmlfilePk + ".txt";
				if(!tableStr.toString().equals("")){
					String formulaFile = "Ufida_IUFO/formula/" + xmlfilePk + "  引用：" + tableStr + ".txt";
					File file = new File(formulaFile);
					if (file.exists()) {
						file.delete();
						file = new File(formulaFile);
					}
					OutputStream fout = new FileOutputStream(file);
					fout.write(formulas.getBytes());
					fout.close();
				}
				// ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

				try {
					IUFOUICacheManager.getSingleton().getReportCache().clear();
				} catch (IUFOCacheException e) {
					e.printStackTrace();
				}

				File xmlfiles = new File(xmlfile);
				this.SaveXMLRep(xmlmoduleName, xmlfilePk, xmlfileName, xmlnote,
						xmlfiles, xmlID);

				if(!checkRep){
					String DefTask = dmo.getTaskPk("太仓港任务" , ORACLEDATASOURCE);
					String RepId = dmo.getRepId(xmlfileName, xmlfilePk, ORACLEDATASOURCE);
					dmo.insertTaskSet("太仓港任务", DefTask, RepId, ORACLEDATASOURCE);
					AppDebug.debug("导入 [ 太仓港任务 ] ");
				}else{
					String DefTaskPk = dmo.getTaskPk("备份表任务" , ORACLEDATASOURCE);
					dmo.insertTaskSet("备份表任务" , DefTaskPk, Id, ORACLEDATASOURCE);
					AppDebug.debug("导入 [ 备份表任务 ] ");
				}
			}
	
			} catch (Exception e) {
				System.out.println(e.getMessage());
				continue;
			}
		}
	}

	// 更新type
	public void UpdateXmlListType(String fileName) throws SAXException,
			IOException {
		String path = "Ufida_IUFO/blobFile/";
		org.w3c.dom.Document document = XMLUtil.getDocumentBuilder().parse(
				xmlList);
		org.w3c.dom.NodeList lists = document.getElementsByTagName("list");
		// 判断是否已经存在这个文件
		for (int s = 0; s < lists.getLength(); s++) {
			String xmlfileName = document.getElementsByTagName("fileName")
					.item(s).getFirstChild().getNodeValue();
			if (xmlfileName.equals(fileName) || xmlfileName == fileName) {

				document.getElementsByTagName("type").item(s).getFirstChild()
						.setNodeValue("1");
				toSave(document, xmlList.getPath());

				// String xmlmoduleName = document.getElementsByTagName(
				// "moduleName").item(s).getFirstChild().getNodeValue();
				// String xmlfilePk = document.getElementsByTagName("filePk")
				// .item(s).getFirstChild().getNodeValue();
				// String xmlfileNames =
				// document.getElementsByTagName("fileName")
				// .item(s).getFirstChild().getNodeValue();
				// String xmlnote =
				// document.getElementsByTagName("note").item(s)
				// .getFirstChild().getNodeValue();
				// String xmlfile =
				// document.getElementsByTagName("file").item(s)
				// .getFirstChild().getNodeValue();
				// String xmlID = document.getElementsByTagName("ID").item(s)
				// .getFirstChild().getNodeValue();
				//
				//				
				// try {
				//
				// String[] strs = dmo.getReportnamecode(xmlfilePk,
				// ORACLEDATASOURCE);
				//						
				// //
				// document.getElementsByTagName("ID").item(s).setTextContent(strs[2]);
				// // this.toSave(document, xmlList.getPath());
				//						
				// String[] oldmsg = dmo.getOldReportnamecode(xmlID,
				// ORACLEDATASOURCE);
				// // int count = dmo.getReportcodeCount(strs[1],
				// ORACLEDATASOURCE);
				// if (oldmsg[2] != null && strs[2] != null) {
				// // Object[] measureOldObjs =
				// // dmo.getMeasurenamecode(xmlID,4
				// // ORACLEDATASOURCE);
				// // Object[] measureObjs = dmo.getMeasurenamecode(
				// // oldmsg[2], ORACLEDATASOURCE);
				// //
				// // dmo.getBlob(strs[2], ORACLEDATASOURCE);
				// // dmo.getBlob(oldmsg[2], ORACLEDATASOURCE);
				//
				// // 创建中间表
				// if (dmo.gettempMeasure(ORACLEDATASOURCE) == 0) {
				// dmo.createTempMeasure(ORACLEDATASOURCE);
				// dmo.insertTempMeasure(ORACLEDATASOURCE);
				// }
				//
				// String testID = "00000000000000000000";
				// String testID2 = "10000000000000000000";
				//
				// // 迁移measure表数据到中间表
				// int num = 0;
				// num += dmo.insertMeasurenamecode(oldmsg[2],
				// ORACLEDATASOURCE);
				// num += dmo.insertMeasurenamecode(strs[2],
				// ORACLEDATASOURCE);
				//
				// if (num == 2) {
				// // 删除measure表中对应的数据
				// dmo.delMeasurenamecode(xmlID, strs[2],
				// ORACLEDATASOURCE);
				//
				// if (strs[2] != xmlID) {
				//
				// // 更新report表数据
				// dmo.updateReportPKset(xmlfileName, testID,
				// ORACLEDATASOURCE);
				// dmo.updateReportPKset(oldmsg[1], testID2,
				// ORACLEDATASOURCE);
				//
				// dmo.updateReportPKset(xmlfileName, xmlID,
				// ORACLEDATASOURCE);
				// dmo.updateReportPKset(oldmsg[1], strs[2],
				// ORACLEDATASOURCE);
				//
				// // 更新中间表对应数据
				// num = 0;
				// num += dmo.updateMeasurePKset(oldmsg[2],
				// testID, ORACLEDATASOURCE);
				// num += dmo.updateMeasurePKset(strs[2],
				// testID2, ORACLEDATASOURCE);
				// num += dmo.updateMeasurePKset(testID,
				// strs[2], ORACLEDATASOURCE);
				// num += dmo.updateMeasurePKset(testID2,
				// oldmsg[2], ORACLEDATASOURCE);
				//
				// // 将中间表数据迁移回measure表
				// dmo.revertMeasure(oldmsg[2],ORACLEDATASOURCE);
				// dmo.revertMeasure(strs[2],ORACLEDATASOURCE);
				//
				// dmo.delTempMeasure(ORACLEDATASOURCE);
				//									
				// dmo.alterTs(ORACLEDATASOURCE);
				// if(dmo.getReportcodeCount(strs[1], ORACLEDATASOURCE) > 1){
				// dmo.updateCOPKset(oldmsg[2], testID, ORACLEDATASOURCE);
				// dmo.updateCOPKset(strs[2], oldmsg[2], ORACLEDATASOURCE);
				// dmo.updateCOPKset(testID, strs[2], ORACLEDATASOURCE);
				// }
				//									
				// if (num == 4) {
				//
				// System.out.println("更新新导入的样表的信息："
				// + xmlfileName + "_" + xmlID);
				// System.out.println("更新旧的样表信息："
				// + oldmsg[0] + "_" + strs[2]);
				// } else {
				// System.out.println("measure PK 替换失败");
				// }
				// }
				// }
				// }
				// } catch (Exception e) {
				// System.out.println(e.getMessage());
				// }
				//
				//				
			}
		}
	}

	// 更新type变0和路路径
	public void UpdateXmlListTypetozero(String fileName, String file, String ID)
			throws SAXException, IOException {
		System.out.println("Ufida_IUFO/jieshou/xmlList.xml" + " - " + fileName
				+ " - " + file + " - " + ID);
		org.w3c.dom.Document document = XMLUtil.getDocumentBuilder().parse(
				xmlList);
		org.w3c.dom.NodeList lists = document.getElementsByTagName("list");
		// 判断是否已经存在这个文件
		for (int s = 0; s < lists.getLength(); s++) {
			String xmlfileName = document.getElementsByTagName("fileName")
					.item(s).getFirstChild().getNodeValue();
			if (xmlfileName.equals(fileName) || xmlfileName == fileName) {
				document.getElementsByTagName("type").item(s).getFirstChild()
						.setNodeValue("0");
				document.getElementsByTagName("file").item(s).getFirstChild()
						.setNodeValue(file);
				document.getElementsByTagName("ID").item(s).getFirstChild()
						.setNodeValue(ID);
				toSave(document, xmlList.getPath());
			}
		}
	}

	// webservice接收XML，并保存到本地

	public String importXMLRep(String moduleName, String filePk,
			String fileName, String note, String xmlContent) {
		// 清除缓存
		IUFOUICacheManager.clearAllCaches();
		// 建立目录
		String paths = "Ufida_IUFO/jieshou/";
		File path = new File(paths);
		if (!path.exists()) {
			path.mkdirs();
		}
		if (note.equals("") || note == "") {
			note = "这是集团下发的报表";
		}
		String XMLName = SuiJiZiFuChuan(20, 1);
		// File newfile;
		String newFile;
		BufferedWriter writer = null;
		
		try {
			if(dmo == null)
				dmo = new BasDMO();
			
			newFile = "Ufida_IUFO/jieshou/" + XMLName + ".xml"; // 上传文件存放路径
			// out = new FileOutputStream(newFile);
			writer = new BufferedWriter(new FileWriter(newFile));
			writer.write(xmlContent);
			
			String content = "样表 【 "+filePk+" 】 从集团接收成功！";
			String title = "集团样表下发消息提示" ;
			String randomVal = dmo.GenPk();
			dmo.insertReleaseinfo(content, title, randomVal,ORACLEDATASOURCE);

			java.util.List<String> userList = dmo.getUserId(ORACLEDATASOURCE);
			for (String userId : userList) {
				dmo.insertReleasetarget(randomVal, userId,ORACLEDATASOURCE);
			}

		} catch (Exception e) {
			e.printStackTrace();
			
			return "写入XML错误";
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					return "关闭writer错误";
				}
			}
		}
		// newfile=new File(newFile);

		// System.out.println(file.getPath());
		// 保存到本地的文件路径
		// File newfile=new File(paths+file.getName());
		// 传递过来 暂不传
		// file.renameTo(newfile);
		// 将传来的文件信息保存到 jieshou目录下，并写如到xmlList.xml里
		File xmlList = new File("Ufida_IUFO/jieshou/xmlList.xml");
		// 如果xmlList.xml不存在则创建
		Boolean bl = xmlList.exists();
		if (!bl) {
			try {
				org.w3c.dom.Document document = XMLUtil.newDocument();
				org.w3c.dom.Element element_main = document
						.createElement("main");
				org.w3c.dom.Element element_all = document
						.createElement("list");
				org.w3c.dom.Element emoduleName = document
						.createElement("moduleName");
				emoduleName.appendChild(document.createTextNode(moduleName));
				element_all.appendChild(emoduleName);
				org.w3c.dom.Element efilePk = document.createElement("filePk");
				efilePk.appendChild(document.createTextNode(filePk));
				element_all.appendChild(efilePk);
				org.w3c.dom.Element efileName = document
						.createElement("fileName");
				efileName.appendChild(document.createTextNode(fileName));
				element_all.appendChild(efileName);

				org.w3c.dom.Element enote = document.createElement("note");
				enote.appendChild(document.createTextNode(note));
				element_all.appendChild(enote);

				org.w3c.dom.Element efile = document.createElement("file");
				// by fdh newfile.getPath()
				efile.appendChild(document.createTextNode(newFile));
				element_all.appendChild(efile);
				org.w3c.dom.Element etype = document.createElement("type");
				etype.appendChild(document.createTextNode("0"));
				element_all.appendChild(etype);
				org.w3c.dom.Element eID = document.createElement("ID");
				eID.appendChild(document.createTextNode("0"));
				element_all.appendChild(eID);
				
				
				
				
				element_main.appendChild(element_all);
				document.appendChild(element_main);
				toSave(document, xmlList.getPath());
			} catch (Exception e) {
				return "构建XmlList.xml错误";
			}
		} else // 如果存在则读取并写入
		{
			try {
				org.w3c.dom.Document document = XMLUtil.getDocumentBuilder()
						.parse(xmlList);
				org.w3c.dom.NodeList lists = document
						.getElementsByTagName("list");
				// 判断是否已经存在这个文件
				for (int s = 0; s < lists.getLength(); s++) {
					String xmlfileName = document.getElementsByTagName(
							"fileName").item(s).getTextContent();
					String xmltype = document.getElementsByTagName("type")
							.item(s).getTextContent();
					String xmlID = document.getElementsByTagName("ID").item(s)
							.getTextContent();
					if (xmlfileName.equals(fileName) || xmlfileName == fileName) {
						// 如果已经传过去，还没导入，又下发了一次的情况
						System.out.println("type:" + xmltype);
						if (xmltype.equals("0") || xmltype == "0") {
							document.getElementsByTagName("file").item(s)
									.getFirstChild().setNodeValue(newFile);
							toSave(document, xmlList.getPath());
							return "出现已经传过去，还没导入，又下发了一次的情况";
						}

						String[] report = new String[3];
						try {
							String date = new SimpleDateFormat("MMddssSSS")
									.format(new Date());
							dmo = new BasDMO();
							report = dmo.getReportnamecode(filePk,
									ORACLEDATASOURCE);
							// String reyst = "备份表：名称：" + report[1] + "编号:"
							// + report[0];
							// report[0] += date;
							// report[1] += date;
							// String reyst2 = "原表改名为：名称：" + report[1] + "编号:"
							// + report[0];

							// 如果已经有存在的表则写入OLDID。
							// document.getElementsByTagName("ID").item(s).getFirstChild().setNodeValue(report[2]);
							// 编码 名字ID

							if (report[2] != null) {
								// int rest =
								// dmo.updateReportnamecode(report[0],
								// report[1], report[2], ORACLEDATASOURCE);
								// 发现重复的，对原有重命名，并导入新的。
								int rest = dmo.getReportnamecodeCount(
										report[2], ORACLEDATASOURCE);

								this.UpdateXmlListTypetozero(fileName, newFile,
										report[2]);
								
								
								String formulaPath = "Ufida_IUFO/formula";
								File file = new File(formulaPath);
								String retStr = "您下发的样表被：";
								
									File[] files = file.listFiles();
									String[] names = new String[files.length];
									for (int i = 0; i < files.length; i++) {
										names[i] = files[i].getName();
									}
									List<String> strs = new ArrayList<String>();
									for (String name : names) {
										// System.out.println(name.substring(0 , name.length()-4));
										String refTbl = name.substring(0, name.indexOf("  "));
										String[] brefTbls = name.substring(name.indexOf("：") + 1,
												name.length()).split(";");

										for (String brefTbl : brefTbls) {
											if(filePk.equals(brefTbl) || fileName.equals(brefTbl)) {
												if(strs.size() == 0)
													strs.add(refTbl);
												boolean check = false;
												for(String str : strs){
													if(str.equals(refTbl))
														check = true;
												}
												
												if(!check)
													strs.add(refTbl);
											}
										}
									}
									
									for (String str : strs) {
										retStr += str + " ";
									}
									retStr += "引用，请重新下发这些样表!";
									
									if(strs.size() > 0)
										return retStr;
									
								

								// return reyst + "成功, " + reyst2;
								return "下发成功";
							} else {
								document.getElementsByTagName("type").item(s)
										.getFirstChild().setNodeValue("0");
								document.getElementsByTagName("file").item(s)
										.getFirstChild().setNodeValue(newFile);
								toSave(document, xmlList.getPath());
								return "过去存在的数据，TYPE=1，但是56上已经删除或重名了，找不到的情况下,做新的导入。";
							}
						} catch (Exception e) {
							return "-更新命令执行失败名称：" + report[1] + "编号:"
									+ report[0] + "ID：" + report[2];
						}
					}
				}
				org.w3c.dom.Element element_main = document
						.getDocumentElement();
				org.w3c.dom.Element element_all = document
						.createElement("list");
				org.w3c.dom.Element emoduleName = document
						.createElement("moduleName");
				emoduleName.appendChild(document.createTextNode(moduleName));
				element_all.appendChild(emoduleName);
				org.w3c.dom.Element efilePk = document.createElement("filePk");
				efilePk.appendChild(document.createTextNode(filePk));
				element_all.appendChild(efilePk);
				org.w3c.dom.Element efileName = document
						.createElement("fileName");
				efileName.appendChild(document.createTextNode(fileName));
				element_all.appendChild(efileName);

				org.w3c.dom.Element enote = document.createElement("note");
				enote.appendChild(document.createTextNode(note));
				element_all.appendChild(enote);

				org.w3c.dom.Element efile = document.createElement("file");
				// by fdh newfile.getPath()
				efile.appendChild(document.createTextNode(newFile));
				element_all.appendChild(efile);
				org.w3c.dom.Element etype = document.createElement("type");
				etype.appendChild(document.createTextNode("0"));
				element_all.appendChild(etype);
				org.w3c.dom.Element eID = document.createElement("ID");
				eID.appendChild(document.createTextNode("0"));
				element_all.appendChild(eID);
				element_main.appendChild(element_all);
				toSave(document, xmlList.getPath());
			} catch (Exception e) {
				e.printStackTrace();
				AppDebug.debug(e);
				return "重写XmlList.xml错误";
			}

		}
		
		
		
		
		
		return "写入成功";
	}

	public void toSave(org.w3c.dom.Document document, String filename) {
		PrintWriter pw = null;
		try {
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			DOMSource source = new DOMSource(document);
			transformer.setOutputProperty(OutputKeys.ENCODING, "GB2312");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			pw = new PrintWriter(new FileOutputStream(filename));
			StreamResult result = new StreamResult(pw);
			transformer.transform(source, result);
		} catch (TransformerException mye) {
			mye.printStackTrace();
		} catch (IOException exp) {
			exp.printStackTrace();
		} finally {
			pw.close();
		}
	}

	// 以下为系统方法
	public ActionForward saveFile(ActionForm actionForm, File file) {

		try {
			// 分类型得到报表格式模型
			RepSaveImportForm form = (RepSaveImportForm) actionForm;
			String[] fileNames = new String[] { file.getPath() };
			String[] strRepPKs = new String[1];
			cModel = this.aciton1.doImportCellsModel(form.isXMLType(),
					fileNames[0], strRepPKs);

			// 保存报表基本信息,调用ReportEditAction的功能
			ActionForward fwd = this.actSave(actionForm);
			if (fwd instanceof CloseForward) {
				this.aciton1.doSaveRepFormat(form.isXMLType(),ResMngToolKit.getVOIDByTreeObjectID(form.getID()),cModel,fileNames[0],strRepPKs[0],(DataSourceVO) getSessionObject(IIUFOConstants.DefaultDSInSession));
			}
			return fwd;
		} catch (Exception e) {
			// if( (e instanceof WebException)==false && (e instanceof
			// CommonException)==false){
			// AppDebug.debug(e);
			// }
			// e.printStackTrace();
			e.printStackTrace();
			AppDebug.debug(e);
			// if(e.getMessage()!=null){
			// return new MessageForward(e.getMessage());
			// }else{
			return new MessageForward(StringResource
					.getStringResource("miuforep015")); // "报表格式有误，无法导入和保存"
			// }
		}
	}

	public ActionForward actSave(ActionForm actionForm) throws WebException {
		ActionForward actionForward = null;
		// 特殊字符不能正确在validate里提交，需要在这里特别调用
		String[] strErrors = validate(actionForm);
		if (strErrors != null && strErrors.length > 0) {
			// StringBuffer sbError = new StringBuffer();
			// for(int i =0;i<strErrors.length;i++){
			// sbError.append(strErrors[i]);
			// }
			// throw new WebException("sdfsdaf");

			return new ErrorForward(strErrors);
		}
		IResTreeObjForm resTreeObjForm = changetoResTreeObjForm(actionForm);
		// USERCODE
		String strSelectedID = getSelectedObjID(resTreeObjForm);
		// 检查权限
		// checkRight(strSelectedID);
		// END
		IResTreeObject selResTreeObj = (IResTreeObject) getSelectedObj(
				strSelectedID, resTreeObjForm);

		try {
			if (isModify(actionForm)) {
				selResTreeObj = getPostVO(actionForm, selResTreeObj, true);
				doUpdateObj(selResTreeObj);
			} else {
				selResTreeObj = getPostVO(actionForm, selResTreeObj, false);
				selResTreeObj = doCreateObj(selResTreeObj);
				if (selResTreeObj != null) {
					String strNewTreeObjID = selResTreeObj.getID();
					if (strNewTreeObjID != null && strNewTreeObjID.length() > 0) {
						resTreeObjForm.setID(strNewTreeObjID);
						ResUIBizHelper.addSelTableID2Session(this,
								strNewTreeObjID);
					}
				}
			}
		} catch (ResOperException e1) {
			// throw new WebException(e1.getMessage());
			System.out.println("\n错误！\n");
		}

		actionForward = getSaveActForward(actionForm);

		return actionForward;
	}

	/**
	 * @param actionForm
	 * @return
	 */
	protected IResTreeObjForm changetoResTreeObjForm(ActionForm actionForm) {
		IResTreeObjForm resTreeObjForm = (ResTreeObjForm) actionForm;
		return resTreeObjForm;
	}

	/**
	 * 
	 * @param resTreeObjForm
	 * @return
	 */
	protected ReportForm changetoReportFileObjForm(
			IResTreeObjForm resTreeObjForm) {
		return (ReportForm) resTreeObjForm;
	}

	public String[] validate(ActionForm actionForm) {
		if (actionForm == null) {
			return null;
		}
		List<String> listStr = new ArrayList<String>();
		ReportForm reportFileObjForm = changetoReportFileObjForm(changetoResTreeObjForm(actionForm));
		// 检验输入的报表名称

		String strName = reportFileObjForm.getName();
		if (!checkName(strName)) {
			listStr.add(StringResource.getStringResource("miufopublic404")); // "名称包含非法字符"
		}
		if (strName != null) {
			byte[] bytersName = strName.getBytes();
			if (bytersName.length > CodeNameMaker.MAX_REPNAME_LENGTH) {
				listStr.add(StringResource.getStringResource("miufo50rep003"));// 报表名称长度超过60(汉字字符占2位)！
			}
		}
		String strRepCode = reportFileObjForm.getReportCode();
		if (strRepCode != null) {
			byte[] bytersCode = strRepCode.getBytes();
			if (bytersCode.length > CodeNameMaker.MAX_REPCODE_LENGTH) {
				listStr.add(StringResource.getStringResource("miufo50rep001"));// 报表编码长度超过30(汉字字符占2位)！
			}
		}
		// 检验输入的报表编码
		int nNotValidPos = checkRepCode(strRepCode);
		if (nNotValidPos >= 1) {
			listStr.add(StringResource.getStringResource("miufo50rep002",
					new String[] { Integer.valueOf(nNotValidPos).toString() }));// "报表编码的第{0}个字符是不支持的特殊字符"
		}

		// #业务规则检验
		// 1,报表名称在同一目录里不能重复;2,报表编码全局唯一
		// #web组建优化提交处理：不能保证在validate方法里的getTableSelectID和getTreeSelectID一直有值;改用业务Form里传递过来的值
		String strReportPK = null;
		if (isModify(actionForm)) {
			strReportPK = ResMngToolKit.getVOIDByTreeObjectID(reportFileObjForm
					.getBizTableSelectedID());// reportFileObjForm.getID());//getTableSelectedID());
		}

		String strRepDirPK = ResMngToolKit
				.getVOIDByTreeObjectID(reportFileObjForm.getBizTreeSelectedID());// repVO.getRepDir();//ResMngToolKit.getVOIDByTreeObjectID(getTreeSelectedID());
		try {
			this.chekRepBizRule(getModuleID(), strReportPK, strName,
					strRepCode, strRepDirPK);
		} catch (UISrvException e) {
			AppDebug.debug(e);// @devTools e.printStackTrace(System.out);
			listStr.add(e.getMessage());
		}

		if (listStr.size() > 0) {
			String[] strReturns = new String[listStr.size()];
			listStr.toArray(strReturns);
			return strReturns;
		}
		return null;
	}

	public static void chekRepBizRule(String strModuleID, String strReportPK,
			String strRepName, String strRepCode, String strRepDirPK)
			throws UISrvException {
		if (strModuleID == null) {
			return;
		}
		ReportCache reportCache = IUFOUICacheManager.getSingleton()
				.getReportCache();

		try {
			reportCache.clear();
		} catch (IUFOCacheException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		boolean bFind = false;
		boolean bSameDirName = false;
		boolean bSameDirCode = false;
		ReportVO[] oldRepVOs = reportCache
				.getByPks(new String[] { strReportPK });

		// 新建
		if (oldRepVOs == null || oldRepVOs.length <= 0 || oldRepVOs[0] == null) {
		}
		// 修改
		else {
			if (oldRepVOs[0].getName().equals(strRepName)) {
				bSameDirName = true;
			}
			if (oldRepVOs[0].getCode().equals(strRepCode)) {
				bSameDirCode = true;
			}
		}
		// 1,报表名称在同一目录里不能重复
		if (!bSameDirName) {
			bFind = reportCache.isExistRepNameByDir(strRepName, strRepDirPK);
			if (bFind) {
				// 同一目录下，报表名称重复！
				throw new UISrvException("miufo131");
			}
		}
		// 2.报表编码全局唯一
		if (!bSameDirCode) {
			// liuyy 2004-11-04
			bFind = reportCache
					.isExistRepByRepCode(strRepCode,
							IIUFOResMngConsants.MODULE_REPORT_MODEL
									.equals(strModuleID));
			if (bFind) {
				throw new UISrvException("uiuforesmng0054"); // "报表编码已存在！请重新输入！"
			}
		}
	}

	private ResWebParam getModuleInfoFromSession() {
		return ResUIBizHelper.getModuleInfoFromSession(this);
	}

	private IResTreeObject getPostVO(ActionForm actionForm,
			IResTreeObject selResTreeObj, boolean bModify)
			throws ResOperException {
		IResTreeObjForm resTreeObjForm = changetoResTreeObjForm(actionForm);
		ResWebParam resWebParam = getModuleInfoFromSession();
		ValueObject operateVO = null;
		if (bModify) {
			operateVO = doGetUpdateVO(resTreeObjForm, selResTreeObj.getSrcVO());
		} else {
			String strParentVOPK = null;
			if (selResTreeObj != null)
				strParentVOPK = ResMngToolKit
						.getVOIDByTreeObjectID(selResTreeObj.getID());
			else {
				strParentVOPK = IResMngConsants.VIRTUAL_ROOT_ID;
			}
			operateVO = doGetNewVO(strParentVOPK, resTreeObjForm, resWebParam);
		}
		// #得到IResTreeObj对象
		// ITreeObject treeObj =
		// ResourceMngUtil.getTreeObject((DirVO)dirVO,resWebParam.getModuleID(),ITreeObject.OBJECT_TYPE_FILE,null);
		String strResTreeObjID = selResTreeObj != null ? selResTreeObj.getID()
				: null;
		return doChangetoResTreeObj(operateVO, resWebParam.getModuleID(),
				strResTreeObjID);
	}

	private String SuiJiZiFuChuan(int x, int y) {
		String rest = "";
		for (int j = 0; j < y; j++) {
			for (int i = 0; i < x; i++) {
				int a = (int) (100 * Math.random() + 100 * Math.random());
				while (true) {
					if (a > 96 & a < 123)
						break;
					else
						a = (int) (100 * Math.random() + 100 * Math.random());
				}
				rest += (char) a;
			}
		}
		return rest;
	}

	public String getDatasourse() {
		String ncFileName = "Ufida_IUFO/iufo56_datasource";
		String xmlfile = "Ufida_IUFO/iufo56_datasource/datasource.xml";
		org.w3c.dom.NodeList ip = null;
		org.w3c.dom.Document document = null;

		File myFilePath = new File(ncFileName);
		if (!(myFilePath.exists())) {
			myFilePath.mkdirs();
		}
		java.io.File xmlList = new java.io.File(xmlfile);

		// 如果xmlList.xml不存在则创建
		Boolean bl = xmlList.exists();
		if (!bl) {

			try {
				document = XMLUtil.newDocument();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
			org.w3c.dom.Element element_ip = document.createElement("iufo");
			element_ip.setTextContent("tcg");
			document.appendChild(element_ip);
			this.toSave(document, xmlList.getPath());
		}

		try {
			document = XMLUtil.getDocumentBuilder().parse(xmlList);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ip = document.getElementsByTagName("iufo");

		return ip.item(0).getTextContent();
	}
	
	public String getReportcode(String repid) throws Exception{
		dmo = new BasDMO();
		return dmo.getReportcode(repid, ORACLEDATASOURCE);
	}
	
	public String[] getUnitId(String unit_id) throws Exception{
		dmo = new BasDMO();
		return dmo.getUnitId(unit_id, ORACLEDATASOURCE);
	}
	
	public String getUnit_code(String unitid) throws NamingException{
		dmo = new BasDMO();
		return dmo.getUnit_code(unitid, ORACLEDATASOURCE);
	}
	
	public List<String> getTimeList(String repid) throws NamingException{
		dmo = new BasDMO();
		return dmo.getTimeList(repid, ORACLEDATASOURCE);
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
	
	public boolean delRepCommit(String whereSql) throws NamingException{
		dmo = new BasDMO();
		return dmo.delRepCommit(whereSql, ORACLEDATASOURCE);
	}
	
}
