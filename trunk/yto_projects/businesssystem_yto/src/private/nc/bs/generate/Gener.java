package nc.bs.generate;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ufida.iufo.pub.tools.AppDebug;

import nc.bs.framework.common.NCLocator;
import nc.impl.yto.util.Readmsg;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.yto.util.IGener;
import nc.itf.yto.util.IReadmsg;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.bd.defref.DefdocVO;
import nc.vo.fa.logging.Debug;
import nc.vo.hi.hi_301.GeneralVO;
import nc.vo.yto.business.CorpVO;
import nc.vo.yto.business.DeptdocVO;
import nc.vo.yto.business.JobdocVO;
import nc.vo.yto.business.OperationMsg;
import nc.vo.yto.business.PsnbasdocVO;
import nc.vo.yto.business.PsndocVO;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Gener implements Serializable, IGener {

	private IReadmsg msg = null;

	private String psncode = null;
	private int num = 0;

	public String generateXml(GeneralVO vo, String roottag, String billtype,
			String proc) {
		if (proc.equals("update"))
			proc = "modify";
		else if (proc.equals("del"))
			proc = "delete";

		StringBuffer sb = new StringBuffer();
		try {
			sb.append("<?xml version=\"1.0\" encoding='UTF-8'?>\n");
			sb.append("<ufinterface roottag=\"" + roottag + "\" billtype=\""
					+ billtype + "\" proc=\"" + proc + "\" >\n");
			sb.append("<" + roottag + ">");
			String[] Attrs = vo.getAttributeNames();
			java.util.Arrays.sort(Attrs);
			for (String Attr : Attrs) {
				Object obj = vo.getAttributeValue(Attr);
				if (obj == null)
					obj = "";

				sb.append("<" + Attr + ">");
				sb.append(obj);
				sb.append("</" + Attr + ">");
			}
			sb.append("</" + roottag + ">\n");
			sb.append("</ufinterface>");
		} catch (Exception ex) {
			ex.printStackTrace();

		}

		return sb.toString(); // Gener.EncodingXML(sb.toString());

	}

	public String generateXml2(CorpVO vo, String roottag, String billtype,
			String proc) {
		if (proc.equals("update"))
			proc = "modify";
		else if (proc.equals("del"))
			proc = "delete";

		nc.itf.uap.IUAPQueryBS qryBs = (IUAPQueryBS) NCLocator.getInstance()
				.lookup(IUAPQueryBS.class.getName());

		// 修改中间表
		try {
			initReadmsg();
			String fathercorp = "";
			CorpVO[] fathercorpvos = (CorpVO[]) msg.getGeneralVOs(
					nc.vo.yto.business.CorpVO.class,
					" pk_corp = '" + vo.getAttributeValue("fathercorp") + "'");

			if (fathercorpvos.length > 0)
				fathercorp = fathercorpvos[0].getAttributeValue("unitcode")
						.toString();

			StringBuffer sb_s = new StringBuffer();
			List<String> sqls = new ArrayList<String>();
			if (proc.equals("modify")) {
				String[] procs = new String[] { "modify", "update", "merged",
						"changerelation" };

				for (String str : procs) {
					sb_s = new StringBuffer();
					sb_s.append(" select pk_organizational from BD_ORGANIZATIONAL where ");
					sb_s.append(" proc = '" + str + "' and ");
					sb_s.append(" unitcode = '" + vo.getUnitcode() + "'  ");
					// sb_s.append(" and unitname = '" + vo.getUnitname() +
					// "' and ");
					// sb_s.append(" parentcode = '" + fathercorp + "' and ");
					// sb_s.append(" type = '"+docpojo.getType()+"'  ");
					sb_s.append(" and dr = 0 ");

					sqls.add(sb_s.toString());
				}
			} else {
				sb_s.append(" select pk_organizational from BD_ORGANIZATIONAL where ");
				sb_s.append(" proc = '" + proc + "' and ");
				sb_s.append(" unitcode = '" + vo.getUnitcode() + "'  ");
				// sb_s.append(" and unitname = '" + vo.getUnitname() +
				// "' and ");
				// sb_s.append(" parentcode = '" + fathercorp + "' and ");
				// sb_s.append(" type = '"+docpojo.getType()+"' ");
				sb_s.append(" and  dr = 0 ");
			}

			List<String> pk_organizational = new ArrayList<String>();

			if (proc.equals("modify")) {
				for (String str : sqls) {
					String val = qryBs.executeQuery(str, new ColumnProcessor(
							"pk_organizational")) == null ? null : qryBs
							.executeQuery(str,
									new ColumnProcessor("pk_organizational"))
							.toString();

					if (val != null)
						pk_organizational.add(val);
				}

			} else
				pk_organizational
						.add(qryBs.executeQuery(sb_s.toString(),
								new ColumnProcessor("pk_organizational")) == null ? null
								: qryBs.executeQuery(
										sb_s.toString(),
										new ColumnProcessor("pk_organizational"))
										.toString());

			if (pk_organizational.size() > 0) {
				for (String str : pk_organizational) {
					if (str == null || "".equals(str) || "null".equals(str))
						continue;

					String sql = "update bd_organizational set dr = 1 where pk_organizational = '"
							+ str + "'";
					try {
						qryBs.executeQuery(sql, null);
					} catch (Exception ex) {
					}

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// try {
		// DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		//
		// Object ts =
		// qryBs.executeQuery("select max(ts) ts from bd_organizational where dr = 0",
		// new ColumnProcessor());
		// if(ts != null) {
		// Date nowTime = df.parse(String.valueOf(ts));
		// Date subTime = new Date(nowTime.getTime() - (5 * 1000 * 60 * 60 *
		// 24));
		//
		// try {
		// qryBs.executeQuery("update bd_organizational set dr = 1 where ts <= '"+subTime.toLocaleString()+"' and dr = 0 ",
		// null);
		//
		// } catch (Exception e) {}
		// }
		// } catch (Exception e) {
		// AppDebug.debug(e);
		// }

		StringBuffer sb = new StringBuffer();
		try {
			sb.append("<?xml version=\"1.0\" encoding='UTF-8'?>\n");
			sb.append("<ufinterface roottag=\"" + roottag + "\" billtype=\""
					+ billtype + "\" proc=\"" + proc + "\" >\n");
			sb.append("<" + roottag + ">");
			String[] Attrs = vo.getAttributeNames();
			java.util.Arrays.sort(Attrs);
			for (String Attr : Attrs) {
				Object obj = vo.getAttributeValue(Attr);
				if (obj == null)
					obj = "";

				if (Attr.equals("fathercorp")) {

					initReadmsg();

					CorpVO[] corps = (CorpVO[]) msg.getGeneralVOs(
							CorpVO.class,
							" 1=1 and pk_corp = '"
									+ vo.getAttributeValue("fathercorp") + "'");
					CorpVO corp = null;

					sb.append("<fathercorpcode>");
					try {
						if (corps.length > 0) {
							corp = corps[0];
							sb.append(corp.getAttributeValue("unitcode"));
						}
					} catch (Exception e) {
					}

					sb.append("</fathercorpcode>");

				}

				if (Attr.equals("unitdistinction")) {
					initReadmsg();
					DefdocVO[] defdocs = (DefdocVO[]) msg.getGeneralVOs(
							DefdocVO.class,
							" 1 = 1 and pk_defdoc = '"
									+ vo.getAttributeValue(Attr) + "' ");
					DefdocVO defdoc = null;

					if (defdocs.length > 0)
						defdoc = defdocs[0];
					else
						defdoc = new DefdocVO();

					sb.append("<distinctioncode>");
					try {
						sb.append(defdoc.getAttributeValue("doccode"));
					} catch (Exception e) {
					}
					sb.append("</distinctioncode>");

					sb.append("<distinctionname>");
					try {
						sb.append(defdoc.getAttributeValue("docname"));
					} catch (Exception e) {
					}
					sb.append("</distinctionname>");
				}

				sb.append("<" + Attr + ">");
				sb.append(obj);
				sb.append("</" + Attr + ">");
			}
			sb.append("</" + roottag + ">\n");
			sb.append("</ufinterface>");
		} catch (Exception ex) {
			ex.printStackTrace();

		}

		return sb.toString();// Gener.EncodingXML(sb.toString());
	}

	public String generateXml3(DeptdocVO vo, String roottag, String billtype,
			String proc) {
		if (proc.equals("update"))
			proc = "modify";
		else if (proc.equals("del"))
			proc = "delete";

		StringBuffer sb = new StringBuffer();

		nc.itf.uap.IUAPQueryBS qryBs = (IUAPQueryBS) NCLocator.getInstance()
				.lookup(IUAPQueryBS.class.getName());

		// 修改中间表
		try {
			String fathercorp = "";
			initReadmsg();
			CorpVO[] corpvos = (CorpVO[]) msg.getGeneralVOs(
					nc.vo.yto.business.CorpVO.class,
					" pk_corp = '" + vo.getAttributeValue("pk_corp") + "'");

			if (corpvos.length > 0)
				fathercorp = corpvos[0].getAttributeValue("unitcode")
						.toString();

			StringBuffer sb_s = new StringBuffer();
			List<String> sqls = new ArrayList<String>();
			if (proc.equals("modify")) {
				String[] procs = new String[] { "modify", "update", "merged",
						"changerelation" };

				for (String str : procs) {
					sb_s = new StringBuffer();
					sb_s.append(" select pk_organizational from BD_ORGANIZATIONAL where ");
					sb_s.append(" proc = '" + str + "' and ");
					sb_s.append(" unitcode = '" + vo.getDeptcode() + "'  ");
					// sb_s.append(" and unitname = '" + vo.getDeptname() +
					// "' and ");
					// sb_s.append(" parentcode = '" + fathercorp + "' and ");
					// sb_s.append(" type = '"+docpojo.getType()+"'  ");
					sb_s.append(" and dr = 0 ");

					sqls.add(sb_s.toString());
				}
			} else {
				sb_s.append(" select pk_organizational from BD_ORGANIZATIONAL where ");
				sb_s.append(" proc = '" + proc + "' and ");
				sb_s.append(" unitcode = '" + vo.getDeptcode() + "' ");
				// sb_s.append(" and unitname = '" + vo.getDeptname() +
				// "' and ");
				// sb_s.append(" parentcode = '" + fathercorp + "' and ");
				// sb_s.append(" type = '"+docpojo.getType()+"' ");
				sb_s.append("  and dr = 0 ");
			}

			List<String> pk_organizational = new ArrayList<String>();

			if (proc.equals("modify")) {
				for (String str : sqls) {
					String val = qryBs.executeQuery(str, new ColumnProcessor(
							"pk_organizational")) == null ? null : qryBs
							.executeQuery(str,
									new ColumnProcessor("pk_organizational"))
							.toString();

					if (val != null)
						pk_organizational.add(val);
				}

			} else
				pk_organizational
						.add(qryBs.executeQuery(sb_s.toString(),
								new ColumnProcessor("pk_organizational")) == null ? null
								: qryBs.executeQuery(
										sb_s.toString(),
										new ColumnProcessor("pk_organizational"))
										.toString());

			if (pk_organizational.size() > 0) {
				for (String str : pk_organizational) {
					if (str == null || "".equals(str) || "null".equals(str))
						continue;

					String sql = "update bd_organizational set dr = 1 where pk_organizational = '"
							+ str + "'";
					try {
						qryBs.executeQuery(sql, null);
					} catch (Exception ex) {
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// try {
		// DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		//
		// Object ts =
		// qryBs.executeQuery("select max(ts) ts from bd_organizational where dr = 0",
		// new ColumnProcessor());
		// if(ts != null) {
		// Date nowTime = df.parse(String.valueOf(ts));
		// Date subTime = new Date(nowTime.getTime() - (5 * 1000 * 60 * 60 *
		// 24));
		//
		// try {
		// qryBs.executeQuery("update bd_organizational set dr = 1 where ts <= '"+subTime.toLocaleString()+"' and dr = 0 ",
		// null);
		//
		// } catch (Exception e) {}
		// }
		// } catch (Exception e) {
		// AppDebug.debug(e);
		// }

		try {

			String deptcode = vo.getAttributeValue("deptcode").toString()
					.trim();
			String key = "pk_newcorp";
			String value = "";
			if (deptcode.length() == 6)
				value = vo.getAttributeValue("pk_deptdoc").toString().trim();
			else
				value = vo.getAttributeValue("pk_corp").toString().trim();

			sb.append("<?xml version=\"1.0\" encoding='UTF-8'?>\n");
			sb.append("<ufinterface roottag=\"" + roottag + "\" billtype=\""
					+ billtype + "\" proc=\"" + proc + "\" >\n");
			sb.append("<" + roottag + ">");
			String[] Attrs = vo.getAttributeNames();
			java.util.Arrays.sort(Attrs);
			for (String Attr : Attrs) {
				Object obj = vo.getAttributeValue(Attr);
				if (obj == null)
					obj = "";

				if (Attr.equals("pk_corp")) {
					initReadmsg();

					CorpVO[] corps = (CorpVO[]) msg.getGeneralVOs(
							CorpVO.class,
							" 1=1 and pk_corp = '"
									+ vo.getAttributeValue("pk_corp") + "'");
					CorpVO corp = null;
					if (corps.length > 0)
						corp = corps[0];
					else
						corp = new CorpVO();

					sb.append("<unitcode>");
					sb.append(corp.getAttributeValue("unitcode"));
					sb.append("</unitcode>");

					sb.append("<unitname>");
					sb.append(corp.getAttributeValue("unitname"));
					sb.append("</unitname>");

					// continue;
				}

				if (Attr.equals("pk_fathedept")) {
					DeptdocVO[] depts = (DeptdocVO[]) msg.getGeneralVOs(
							DeptdocVO.class,
							" 1=1 and pk_deptdoc = '"
									+ vo.getAttributeValue("pk_fathedept")
									+ "'");

					if (depts.length > 0) {

						sb.append("<fatherdeptcode>");
						sb.append(depts[0].getAttributeValue("deptcode"));
						sb.append("</fatherdeptcode>");

						sb.append("<fatherdeptname>");
						sb.append(depts[0].getAttributeValue("deptname"));
						sb.append("</fatherdeptname>");

					}

				}

				sb.append("<" + Attr + ">");
				sb.append(obj);
				sb.append("</" + Attr + ">");
			}

			sb.append("<" + key + ">");
			sb.append(value);
			sb.append("</" + key + ">");

			sb.append("</" + roottag + ">\n");
			sb.append("</ufinterface>");
		} catch (Exception ex) {
			ex.printStackTrace();

		}

		return sb.toString(); // Gener.EncodingXML(sb.toString());
	}

	public String generateXml4(PsndocVO vo, String roottag, String billtype,
			String proc, OperationMsg opmsg) {
		if (proc.equals("update"))
			proc = "modify";
		else if (proc.equals("del"))
			proc = "delete";

		StringBuffer sb = new StringBuffer();
		try {
			initReadmsg();
			DeptdocVO[] depts = (DeptdocVO[]) msg.getGeneralVOs(
					DeptdocVO.class,
					" 1=1 and pk_deptdoc = '"
							+ vo.getAttributeValue("pk_deptdoc") + "'");

			CorpVO[] corps = (CorpVO[]) msg.getGeneralVOs(CorpVO.class,
					" 1=1 and pk_corp = '" + vo.getAttributeValue("pk_corp")
							+ "'");

			DeptdocVO dept = null;
			if (depts.length > 0)
				dept = depts[0];

			String key = "pk_newcorp";
			String value = "";
			if (dept.getAttributeValue("deptcode").toString().trim().length() == 6)
				value = vo.getAttributeValue("pk_deptdoc").toString().trim();
			else
				value = vo.getAttributeValue("pk_corp").toString().trim();

			sb.append("<?xml version=\"1.0\" encoding='UTF-8'?>\n");
			sb.append("<ufinterface roottag=\"" + roottag + "\" billtype=\""
					+ billtype + "\" proc=\"" + proc + "\" >\n");
			sb.append("<" + roottag + ">");
			String[] Attrs = vo.getAttributeNames();
			java.util.Arrays.sort(Attrs);
			String pk_defdocs = "";
			for (String Attr : Attrs) {
				Object obj = vo.getAttributeValue(Attr);
				if (obj == null)
					obj = "";

				if (Attr.equals("psncode"))
					psncode = vo.getAttributeValue(Attr).toString();

				if (Attr.equals("pk_corp")) {

					CorpVO corp = null;
					if (corps.length > 0)
						corp = corps[0];
					else
						corp = new CorpVO();

					sb.append("<unitcode>");

					try {
						sb.append(corp.getAttributeValue("unitcode"));
					} catch (Exception e) {
					}

					sb.append("</unitcode>");

					sb.append("<unitname>");
					try {
						sb.append(corp.getAttributeValue("unitname"));
					} catch (Exception e) {
					}
					sb.append("</unitname>");

				}

				if (Attr.equals("pk_deptdoc")) {
					// superdeptcode
					String fatherdept = dept.getPk_fathedept();
					DeptdocVO superDeptdoc = null;
					try {
						initReadmsg();
						while (fatherdept != null) {
							DeptdocVO[] fatherdepts = (DeptdocVO[]) msg
									.getGeneralVOs(DeptdocVO.class,
											" 1=1 and pk_deptdoc = '"
													+ fatherdept + "'");

							if (fatherdepts.length > 0) {
								fatherdept = fatherdepts[0].getPk_fathedept();
								superDeptdoc = fatherdepts[0];
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

					if (superDeptdoc != null) {
						sb.append("<superdeptcode>");
						sb.append(superDeptdoc.getDeptcode());
						sb.append("</superdeptcode>");
					} else {

						sb.append("<superdeptcode>");
						sb.append(dept.getAttributeValue("deptcode"));
						sb.append("</superdeptcode>");
					}

					sb.append("<deptcode>");
					try {
						sb.append(dept.getAttributeValue("deptcode"));
					} catch (Exception e) {
					}
					sb.append("</deptcode>");

					sb.append("<deptname>");
					try {
						sb.append(dept.getAttributeValue("deptname"));
					} catch (Exception e) {
					}
					sb.append("</deptname>");

				}

				// 添加岗位信息
				if (Attr.equals("pk_om_job")) {

					JobdocVO[] jobs = (JobdocVO[]) msg.getGeneralVOs(
							JobdocVO.class,
							" 1=1 and pk_corp = '"
									+ vo.getAttributeValue("pk_corp")
									+ "' and pk_om_job = '"
									+ vo.getAttributeValue("pk_om_job") + "'");
					if (jobs.length > 0) {
						sb.append("<jobcode>");
						try {
							sb.append(jobs[0].getJobcode());
						} catch (Exception e) {
						}
						sb.append("</jobcode>");

						sb.append("<jobname>");
						try {
							sb.append(jobs[0].getJobname());
						} catch (Exception e) {
						}
						sb.append("</jobname>");

						// String[] jobnames = new String[] { "保安", "保洁员",
						// "操作管理专员", "操作员", "操作主管", "操作组长", "车辆调度专员",
						// "车辆管理组长", "大客户项目操作员", "大客户销售专员", "调度专员",
						// "航空提发货操作员", "后勤管理员", "生产设备维护专员", "食堂工作人员",
						// "网络车驾驶员", "网络车辆驾驶员", "维修工", "行政车辆驾驶员", "业务员"
						// };

						// 修改 非系统岗位
						// modify by river for 2012-02-24
						String[] jobnames = new String[] { "业务员", "操作员",
								"网络车辆驾驶员", "航空提发货操作员", "行政车辆驾驶员", "食堂工作人员",
								"保洁员", "保安", "维修工", "提发货操作员", "操作科主管", "操作组长",
								"操作主管", "电工", "航空提发货组长", "车辆管理组长", "车辆维修员",
								"保安员", "网络车驾驶员", "车辆维修组长", "修理工", "组长",
								"大客户项目操作员", "水电工" };

						boolean check = false;
						try {
							for (String jobname : jobnames) {
								if (jobs[0].getJobname().equals(jobname)) {
									check = true;
									break;
								}
							}
							
							String deptcode = dept.getDeptcode();
							if(deptcode.length() > 6)
								deptcode = deptcode.substring(0 , 6);
							
							IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class);
							Object count = iUAPQueryBS.executeQuery("select nvl(count(1) , 0) from bd_deptdoc where deptcode = '"+deptcode+"'", new ColumnProcessor());
							
							if(Integer.valueOf(String.valueOf(count)) > 0) {
								String jobcode = jobs[0].getJobcode().substring(jobs[0].getJobcode().length() - 4 , jobs[0].getJobcode().length());
								if(!"0001".equals(jobcode)) {
									check = true;
								}
							}
							
						} catch (Exception ex) {
							ex.printStackTrace();
						}

						if (check) {
							num = 1;
							sb.append("<sysflag>");
							sb.append(1);
							sb.append("</sysflag>");
						} else {
							num = 0;
							sb.append("<sysflag>");
							sb.append(0);
							sb.append("</sysflag>");
						}
					} else {
						sb.append("<jobcode></jobcode>");
						sb.append("<jobname></jobname>");

						num = 1;
						sb.append("<sysflag>");
						sb.append(1);
						sb.append("</sysflag>");
					}

				}

				sb.append("<" + Attr + ">");
				sb.append(obj);
				sb.append("</" + Attr + ">");

				if (Attr.equals("pk_psncl")) {
					nc.vo.bd.b05.PsnclVO[] psnclvo = (nc.vo.bd.b05.PsnclVO[]) msg
							.getGeneralVOs(nc.vo.bd.b05.PsnclVO.class,
									" pk_psncl = '" + obj + "'");
					if (psnclvo != null && psnclvo.length > 0) {
						sb.append("<psnclasscode>");
						sb.append(psnclvo[0].getPsnclasscode());
						sb.append("</psnclasscode>");

						sb.append("<psnclassname>");
						sb.append(psnclvo[0].getPsnclassname());
						sb.append("</psnclassname>");
					}
				}

				// add by river for 2011-12-13
				// 添加对应自定义档案里的字段名即可得到对应的编码和名称
				// start ..
				if (Attr.equals("jobrank") || Attr.equals("jobseries")
						|| Attr.equals("insource")
						|| Attr.equals("recruitresource"))
					pk_defdocs += "'" + obj + "',";

				// .. end
			}

			if (pk_defdocs.length() > 0)
				pk_defdocs = pk_defdocs.substring(0, pk_defdocs.length() - 1);

			DefdocVO[] defdocvos = (DefdocVO[]) msg.getGeneralVOs(
					DefdocVO.class, " pk_defdoc in (" + pk_defdocs + ")");
			if (defdocvos != null && defdocvos.length > 0) {
				for (DefdocVO defdoc : defdocvos) {
					for (String Attr : Attrs) {
						try {
							if (defdoc.getAttributeValue("pk_defdoc").equals(
									vo.getAttributeValue(Attr))) {

								sb.append("<" + Attr + "_code>");
								sb.append(defdoc.getDoccode());
								sb.append("</" + Attr + "_code>");

								sb.append("<" + Attr + "_name>");
								sb.append(defdoc.getDocname());
								sb.append("</" + Attr + "_name>");

								break;
							}
						} catch (Exception e) {
							Debug.debug(e);
							continue;
						}
					}
				}
			}

			sb.append("<" + key + ">");
			sb.append(value);
			sb.append("</" + key + ">");

			sb.append("<operateusercode>");
			sb.append(opmsg.getUsercode());
			sb.append("</operateusercode>");
			System.out.println("operateusercode : " + opmsg.getUsercode());

			sb.append("<operateusername>");
			sb.append(opmsg.getUsername());
			sb.append("</operateusername>");
			System.out.println("operateusername : " + opmsg.getUsername());

			sb.append("<operateorgcode>");
			sb.append(opmsg.getUnitcode());
			sb.append("</operateorgcode>");
			System.out.println("operateorgcode : " + opmsg.getUnitcode());

			sb.append("<operatetime>");
			sb.append(opmsg.optime);
			sb.append("</operatetime>");
			System.out.println("operateTime : " + opmsg.optime);

			sb.append("</" + roottag + ">\n");
			sb.append("</ufinterface>");
		} catch (Exception ex) {
			ex.printStackTrace();

		}

		return sb.toString(); // Gener.EncodingXML(sb.toString());
	}

	public String generateXml5(JobdocVO vo, String roottag, String billtype,
			String proc) {
		if (proc.equals("update"))
			proc = "modify";
		else if (proc.equals("del"))
			proc = "delete";

		StringBuffer sb = new StringBuffer();
		try {
			initReadmsg();

			sb.append("<?xml version=\"1.0\" encoding='UTF-8'?>\n");
			sb.append("<ufinterface roottag=\"" + roottag + "\" billtype=\""
					+ billtype + "\" proc=\"" + proc + "\" >\n");
			sb.append("<" + roottag + ">");
			String[] Attrs = vo.getAttributeNames();
			java.util.Arrays.sort(Attrs);
			for (String Attr : Attrs) {
				Object obj = vo.getAttributeValue(Attr);
				if (obj == null)
					obj = "";

				sb.append("<" + Attr + ">");
				sb.append(obj);
				sb.append("</" + Attr + ">");

				if (Attr.equals("pk_corp")) {
					initReadmsg();

					CorpVO[] corps = (CorpVO[]) msg.getGeneralVOs(
							CorpVO.class,
							" 1=1 and pk_corp = '"
									+ vo.getAttributeValue("pk_corp") + "'");
					CorpVO corp = null;
					if (corps.length > 0)
						corp = corps[0];
					else
						corp = new CorpVO();

					sb.append("<unitcode>");
					sb.append(corp.getAttributeValue("unitcode"));
					sb.append("</unitcode>");

					sb.append("<unitname>");
					sb.append(corp.getAttributeValue("unitname"));
					sb.append("</unitname>");

				}

				if (Attr.equals("pk_deptdoc")) {
					DeptdocVO[] depts = (DeptdocVO[]) msg.getGeneralVOs(
							DeptdocVO.class,
							" 1=1 and pk_deptdoc = '"
									+ vo.getAttributeValue("pk_deptdoc") + "'");

					if (depts.length > 0) {

						sb.append("<deptcode>");
						sb.append(depts[0].getAttributeValue("deptcode"));
						sb.append("</deptcode>");

						sb.append("<deptname>");
						sb.append(depts[0].getAttributeValue("deptname"));
						sb.append("</deptname>");

					}

				}

				// modify时同步影响岗位的人员编码
				if (proc.equals("modify")) {
					if (Attr.equals("pk_om_job")) {
						String psncodes = "";

						PsndocVO[] psndocs = (PsndocVO[]) msg
								.getGeneralVOs(
										PsndocVO.class,
										" 1=1 and pk_om_job = '"
												+ vo.getAttributeValue("pk_om_job")
												+ "' and outdutydate is null and pk_om_job not in (select pk_om_job from om_job where jobname = '操作员' or jobname = '业务员') and pk_corp = '"
												+ vo.getPk_corp() + "'");
						for (PsndocVO psn : psndocs) {
							String psncode = psn.getAttributeValue("psncode")
									.toString();
							psncodes += psncode + ",";
						}

						if (psncodes.length() > 0) {
							psncodes = psncodes.substring(0,
									psncodes.length() - 1);

							sb.append("<psncodes>");
							sb.append(psncodes);
							sb.append("</psncodes>");
						}
					}
				}
			}
			sb.append("</" + roottag + ">\n");
			sb.append("</ufinterface>");
		} catch (Exception ex) {
			ex.printStackTrace();

		}

		return sb.toString(); // Gener.EncodingXML(sb.toString());
	}

	public String generateXml6(PsnbasdocVO vo, String roottag, String billtype,
			String proc) {
		if (proc.equals("update"))
			proc = "modify";
		else if (proc.equals("del"))
			proc = "delete";

		StringBuffer sb = new StringBuffer();
		try {
			sb.append("<?xml version=\"1.0\" encoding='UTF-8'?>\n");
			sb.append("<ufinterface roottag=\"" + roottag + "\" billtype=\""
					+ billtype + "\" proc=\"" + proc + "\" >\n");
			sb.append("<" + roottag + ">");
			String[] Attrs = vo.getAttributeNames();
			java.util.Arrays.sort(Attrs);
			String pk_defdocs = "";
			for (String Attr : Attrs) {
				Object obj = vo.getAttributeValue(Attr);
				if (obj == null)
					obj = "";

				if (Attr.equals("photo")) {
					if (vo.getAttributeValue(Attr) == null) {
						sb.append("<" + Attr + ">");
						sb.append("");
						sb.append("</" + Attr + ">");

						continue;
					}

					byte[] bytes = (byte[]) obj;
					String photoStr = new BASE64Encoder().encode(bytes);

					// bytes = new BASE64Decoder().decodeBuffer(photoStr);
					//
					// InputStream inStream = new ByteArrayInputStream(bytes);
					// FileOutputStream fs = new FileOutputStream("D:\\1.jpg");
					//
					// int bytesum = 0;
					// int byteread = 0;
					//
					// byte[] buffer = new byte[100 * 1024];
					// while ((byteread = inStream.read(buffer)) != -1) {
					// bytesum += byteread; // 字节数 文件大小
					// // System.out.println(bytesum);
					// fs.write(buffer, 0, byteread);
					// }
					// fs.flush();
					// fs.close();
					// inStream.close();

					// String photoStr = new String(bytes);

					// for(byte b : bytes) {
					// photoStr += String.valueOf(b) + ",";
					// }

					// System.out.println(photoStr);

					sb.append("<" + Attr + ">");
					sb.append(photoStr);
					sb.append("</" + Attr + ">");

					continue;
				}

				sb.append("<" + Attr + ">");
				sb.append(obj);
				sb.append("</" + Attr + ">");

				// add by river for 2011-12-13
				// 添加对应自定义档案里的字段名即可得到对应的编码和名称
				// start ..
				if (Attr.equals("basgroupdef15")
						|| Attr.equals("basgroupdef16")
						|| Attr.equals("basgroupdef4")
						|| Attr.equals("basgroupdef5")
						|| Attr.equals("basgroupdef6")
						|| Attr.equals("basgroupdef7")
						|| Attr.equals("employform") || Attr.equals("health")
						|| Attr.equals("marital") || Attr.equals("nationality")
						|| Attr.equals("nativeplace")
						|| Attr.equals("permanreside") || Attr.equals("polity"))
					pk_defdocs += "'" + obj + "',";

				// .. end

			}

			if (pk_defdocs.length() > 0)
				pk_defdocs = pk_defdocs.substring(0, pk_defdocs.length() - 1);

			DefdocVO[] defdocvos = (DefdocVO[]) msg.getGeneralVOs(
					DefdocVO.class, " pk_defdoc in (" + pk_defdocs + ")");
			if (defdocvos != null && defdocvos.length > 0) {
				for (DefdocVO defdoc : defdocvos) {
					for (String Attr : Attrs) {
						try {
							if (defdoc.getAttributeValue("pk_defdoc").equals(
									vo.getAttributeValue(Attr))) {

								sb.append("<" + Attr + "_code>");
								sb.append(defdoc.getDoccode());
								sb.append("</" + Attr + "_code>");

								sb.append("<" + Attr + "_name>");
								sb.append(defdoc.getDocname());
								sb.append("</" + Attr + "_name>");

								break;
							}
						} catch (Exception e) {
							Debug.debug(e);
							continue;
						}
					}
				}
			}

			if (psncode != null) {
				sb.append("<psncode>");
				sb.append(psncode);
				sb.append("</psncode>");

				psncode = null;
			} else {
				String code = "";
				try {

					Readmsg msg = new Readmsg();
					PsndocVO[] psndoc = (PsndocVO[]) msg.getGeneralVOs(
							PsndocVO.class,
							" pk_psnbasdoc = '"
									+ vo.getAttributeValue("pk_psnbasdoc")
									+ "' and pk_corp = '"
									+ vo.getAttributeValue("pk_corp") + "'");

					if (psndoc.length > 0)
						code = psndoc[0].getPsncode();

				} catch (Exception e) {
				}

				sb.append("<psncode>");
				sb.append(code);
				sb.append("</psncode>");

			}

			sb.append("<sysflag>");
			sb.append(num);
			sb.append("</sysflag>");

			sb.append("</" + roottag + ">\n");
			sb.append("</ufinterface>");
		} catch (Exception ex) {
			ex.printStackTrace();

		}

		return sb.toString(); // Gener.EncodingXML(sb.toString());
	}

	public void initReadmsg() {
		if (msg == null)
			// msg = (IReadmsg) NCLocator.getInstance().lookup(
			// IReadmsg.class.getName());
			msg = new Readmsg();
	}

	private String EncodingXML(String xml) {
		try {
			byte[] xmlByte = xml.getBytes();
			String encodeStr = new BASE64Encoder().encode(xmlByte);

			// System.out.println(encodeStr);

			return encodeStr;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private byte[] DecodingXML(String xml) {
		try {
			return new BASE64Decoder().decodeBuffer(xml);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
