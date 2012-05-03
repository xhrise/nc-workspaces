package nc.impl.mbSyn;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.mbSyn.IMbSys;
import nc.itf.mbSyn.IQueryList;
import nc.vo.hrsm.hrsm_301.StapplybHHeaderVO;
import nc.vo.mbSyn.DepartmentVO;

import org.apache.axis2.AxisFault;

import com.seeyon.client.AccountServiceStub;
import com.seeyon.client.DepartmentServiceStub;
import com.seeyon.client.OcupationServiceStub;
import com.seeyon.client.PersonServiceStub;
import com.seeyon.client.PersonServiceStub.PersonInfoParam_All;
import com.seeyon.client.PersonServiceStub.ServiceResponse;
import com.ufida.iufo.pub.tools.AppDebug;

/**
 * 导出人员
 * 
 * @author Administrator
 * 
 */
public class MbSyn implements IMbSys {
	// public static void main(String[] args) {
	// try {
	//
	// PersonServiceTest test = new PersonServiceTest();
	// // 创建
	// // test.create("ry");
	//			
	// // 按登录名更新
	// test.updateByLoginName();
	//			
	// // 按ID更新
	// // test.update(4647737107627917717l);
	//			
	// // 按登录名删除
	// // test.create("testservice2");
	// // test.deleteByLoginName("testservice2");
	//			
	// // 按ID删除
	// // test.create("testservice2");
	// // test.delete(-1308372468846015786l);
	//			
	// // 按登录名启用/禁用
	// // test.enableByLoginName("testservice7", false);
	// test.enableByLoginName("ry_", true);
	//			
	// // 按ID启用/禁用
	// // test.enable(4647737107627917717l, false);
	// // test.enable(4647737107627917717l, true);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	
	private Long id = 0L;

	public MbSyn() {

	}

	/**
	 * 同步人员到OA
	 */
	public String create(String datasource, String pk_psndoc, String reset)
			throws Exception {
		IQueryList queryList = (IQueryList) NCLocator.getInstance().lookup(
				IQueryList.class.getName());
		// QueryList queryList = new QueryList();
		List<PersonInfoParam_All> personList = queryList.getPerson(datasource, pk_psndoc);
		int falseNum = 0;
		int trueNum = 0;
		System.out.println(personList.size());
		for (PersonInfoParam_All person : personList) {
			try {
				PersonServiceStub stub = new PersonServiceStub();
				PersonServiceStub.Create req = new PersonServiceStub.Create();
				// PersonServiceStub.PersonInfoParam_All person = new
				// PersonServiceStub.PersonInfoParam_All();
				person.setPassWord("7200244ye"); // 7200244ye
				req.setToken(ServiceUtil.getToken());
				req.setPerson(person);

				PersonServiceStub.CreateResponse resp = stub.create(req);
				ServiceResponse r = resp.get_return();

				System.out.println("新建人员的ID：" + r.getResult() + ",错误号："
						+ r.getErrorNumber() + ",错误信息：" + r.getErrorMessage());

				if (reset.equals("Y"))
					this.enableByLoginName(person.getLoginName(), true);

				try {
					if(r.getResult() != -1L)
						queryList.updatePerson(person.getLoginName(), person.getTrueName(), String.valueOf(r.getResult()), datasource);
				} catch (Exception e) {
					falseNum++;
					continue;
				}
				
				if (r.getResult() == -1)
					falseNum++;
				else
					trueNum++;

				
				stub._getServiceClient().cleanupTransport();
				stub._getServiceClient().cleanup();
				stub.cleanup();
				stub = null;
				
			} catch (Exception ex) {
				ex.printStackTrace();
				falseNum++;
				continue;
			}
		}
		
		

		if ((trueNum + falseNum) == 0)
			return "同步人员信息：\t\n\t没有可以同步到OA的人员信息！";

		return "同步人员信息：\t\n\t成功人数：" + trueNum + "\t\n\t失败人数：" + falseNum;
	}

	public void enableByLoginName(String loginName , boolean enabled) {
		try {

			PersonServiceStub stub = new PersonServiceStub();
			PersonServiceStub.EnableByLoginName req = new PersonServiceStub.EnableByLoginName();

			req.setToken(ServiceUtil.getToken());
			req.setLoginName(loginName);

			req.setEnable(enabled);

			PersonServiceStub.EnableByLoginNameResponse resp = stub
					.enableByLoginName(req);
			ServiceResponse r = resp.get_return();
			System.out.println("启用人员的ID：" + r.getResult() + ",错误号："
					+ r.getErrorNumber() + ",错误信息：" + r.getErrorMessage());
			
			if(r.getResult() == -1)
				this.enable(id, enabled);

			
			stub._getServiceClient().cleanupTransport();
			stub._getServiceClient().cleanup();
			stub.cleanup();
			stub = null;
			
			
		} catch (AxisFault e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void enable(long id, boolean enabled) {
		try {

			PersonServiceStub stub = new PersonServiceStub();
			PersonServiceStub.Enable req = new PersonServiceStub.Enable();

			req.setToken(ServiceUtil.getToken());
			req.setPersonId(id);
			req.setEnable(enabled);

			PersonServiceStub.EnableResponse resp = stub.enable(req);
			ServiceResponse r = resp.get_return();
			System.out.println("启用人员的ID：" + r.getResult());
			System.out.println("错误号：" + r.getErrorNumber());
			System.out.println("错误信息：" + r.getErrorMessage());

			stub._getServiceClient().cleanupTransport();
			stub._getServiceClient().cleanup();
			stub.cleanup();
			stub = null;
			
			
		} catch (AxisFault e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void update(String pk_psndoc, String pk_aimcorp, Long id,
			String source, boolean arg4 , boolean resetPwd) throws Exception {
		try {
			
			this.enable(id, false);

			PersonServiceStub stub = new PersonServiceStub();
			PersonServiceStub.Update req = new PersonServiceStub.Update();
			PersonServiceStub.PersonInfoParam_All person = new PersonServiceStub.PersonInfoParam_All();
		
			IQueryList queryList = (IQueryList) NCLocator.getInstance().lookup(
					IQueryList.class.getName());
			List<String[]> StrsList = queryList
					.getUnitsbyPk(source, pk_aimcorp);
			long accountId = 0;
			if (StrsList.size() == 0)
				return;
			else
				accountId = ServiceUtil.getAccountId(StrsList.get(0)[0]);

			List<PersonInfoParam_All> list = queryList.getPerson(source,
					pk_psndoc);
			if (list.size() == 0) {
				System.out.println("人员列表:NULL");
				//return;
			} else
				person = list.get(0);

			
				this.id = id;
				//person = new PersonInfoParam_All();
				person.setAccountId(accountId);
				
				if(resetPwd)
					person.setPassWord("7200244ye");
				
				req.setToken(ServiceUtil.getToken());
				req.setPersonId(id);
				req.setPerson(person);
				

//				this.enable(id, false);
				
				PersonServiceStub.UpdateResponse resp = stub.update(req);
				ServiceResponse r = resp.get_return();
				System.out.println("被修改人员的ID：" + r.getResult());
				System.out.println("错误号：" + r.getErrorNumber());
				System.out.println("错误信息：" + r.getErrorMessage());
				
				this.enable(id, arg4);
				
				if(r.getResult() == -1L) {
//					this.create(source, pk_psndoc, "Y"); // 防止OA数据重复
					throw new Exception ("该账号ID存在异常，请联系相关人员");
					
				}
				
				
				stub._getServiceClient().cleanupTransport();
				stub._getServiceClient().cleanup();
				stub.cleanup();
				stub = null;
				
//			if (arg4 == false && resetPwd == false) {
//				
//				List<Long> ids = queryList.getDeletePerson(pk_psndoc, source);
//				String[] defs = queryList.getPersonDefVal(pk_psndoc, source);
//				queryList.updateDelPerson2(person.getLoginName(), source);
//				queryList.updateNewPerson(pk_psndoc, defs, source);
//				
//				
//				list = queryList.getPerson(source,
//						pk_psndoc);
//				if (list.size() == 0) {
//					
//					
//					String new_pk_psndoc = queryList.getPersonPKbyOldPK(pk_psndoc, pk_aimcorp , source);
//					list = queryList.getPerson3(source, new_pk_psndoc);
//					
//					if(list.size() == 0) {
//						System.out.println("人员列表:NULL");
//						return;
//					}
//					
//					person = list.get(0);
//				} else
//					person = list.get(0);
//				
//				person.setAccountId(accountId);
//				//person.setPassWord("7200244ye");
//				
//				req.setToken(ServiceUtil.getToken());
//				req.setPersonId(id);
//				req.setPerson(person);
//
////				this.enable(id, false);
//				
//				PersonServiceStub.UpdateResponse _resp = stub.update(req);
//				ServiceResponse _r = _resp.get_return();
//				System.out.println("被修改人员的ID：" + _r.getResult());
//				System.out.println("错误号：" + _r.getErrorNumber());
//				System.out.println("错误信息：" + _r.getErrorMessage());
//				
//				if(id != 0L && id != -1L)
//					this.enable(id, true);
//				else
//					this.enableByLoginName(person.getLoginName(), true);
//				
//				
//				
////				Map<String , Object> memberMap = queryList.getV3xMemberById_x(id, source);
////				
////				Iterator iter = memberMap.entrySet().iterator();
////				while(iter.hasNext()){
////					Map.Entry entry = (Map.Entry)iter.next();
////					
////					System.out.println(entry.getKey() + ":" + entry.getValue());
////				}
//			}
		} catch (AxisFault e) {
			e.printStackTrace();
			AppDebug.debug(e);
			throw new Exception(e);
		} catch (Exception e) {
			e.printStackTrace();
			AppDebug.debug(e);
			throw new Exception(e);
		}
	}

	public void update1(String def1, String pk_psndoc, String pk_aimcorp,
			Long id, String source, boolean arg4) {
		try {

			PersonServiceStub stub = new PersonServiceStub();
			PersonServiceStub.Update req = new PersonServiceStub.Update();
			PersonServiceStub.PersonInfoParam_All person = new PersonServiceStub.PersonInfoParam_All();

			IQueryList queryList = (IQueryList) NCLocator.getInstance().lookup(
					IQueryList.class.getName());
			List<String[]> StrsList = queryList
					.getUnitsbyPk(source, pk_aimcorp);
			long accountId = 0;
			if (StrsList.size() == 0)
				return;
			else
				accountId = ServiceUtil.getAccountId(StrsList.get(0)[0]);

			List<PersonInfoParam_All> list = queryList.getPerson1(source,
					pk_psndoc);
			if (list.size() == 0) {
				System.out.println("人员列表:NULL");
				return;
			} else
				person = list.get(0);

			person.setAccountId(accountId);
			person.setLoginName(def1);
			//person.setPassWord("123456");
			req.setToken(ServiceUtil.getToken());
			req.setPersonId(id);
			req.setPerson(person);

			PersonServiceStub.UpdateResponse resp = stub.update(req);
			ServiceResponse r = resp.get_return();
			System.out.println("被修改人员的ID：" + r.getResult());
			System.out.println("错误号：" + r.getErrorNumber());
			System.out.println("错误信息：" + r.getErrorMessage());

			this.enableByLoginName(person.getLoginName(), arg4);
			
			
			stub._getServiceClient().cleanupTransport();
			stub._getServiceClient().cleanup();
			stub.cleanup();
			stub = null;

		} catch (AxisFault e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void update2(String pk_psndoc, String pk_aimcorp, Long id,
			String source, boolean arg4) {
		try {

			this.delete(pk_psndoc, source);

			IQueryList queryList = (IQueryList) NCLocator.getInstance().lookup(
					IQueryList.class.getName());
			// QueryList queryList = new QueryList();
			PersonServiceStub.PersonInfoParam_All person = new PersonServiceStub.PersonInfoParam_All();
			int falseNum = 0;
			int trueNum = 0;

			List<String[]> StrsList = queryList
					.getUnitsbyPk(source, pk_aimcorp);
			long accountId = 0;
			if (StrsList.size() == 0)
				return;
			else
				accountId = ServiceUtil.getAccountId(StrsList.get(0)[0]);

			List<PersonInfoParam_All> list = queryList.getPerson2(source,
					pk_psndoc);
			if (list.size() == 0) {
				System.out.println("人员列表:NULL");
				return;
			} else
				person = list.get(0);

			try {
				PersonServiceStub stub = new PersonServiceStub();
				PersonServiceStub.Create req = new PersonServiceStub.Create();
				// PersonServiceStub.PersonInfoParam_All person = new
				// PersonServiceStub.PersonInfoParam_All();
				//person.setPassWord("123456");
				req.setToken(ServiceUtil.getToken());
				req.setPerson(person);

				PersonServiceStub.CreateResponse resp = stub.create(req);
				ServiceResponse r = resp.get_return();

				System.out.println("新建人员的ID：" + r.getResult() + ",错误号："
						+ r.getErrorNumber() + ",错误信息：" + r.getErrorMessage());

				this.enableByLoginName(person.getLoginName(), true);
				
				try {
					if(r.getResult() != -1L)
						queryList.updatePerson(person.getLoginName(), person
								.getTrueName(), String.valueOf(r.getResult()),
								source);
				} catch (Exception e) {
					falseNum++;
				}
				if (r.getResult() == -1)
					falseNum++;
				else
					trueNum++;
				
				stub._getServiceClient().cleanupTransport();
				stub._getServiceClient().cleanup();
				stub.cleanup();
				stub = null;

			} catch (Exception ex) {
				falseNum++;
			}

			System.out.println("同步人员信息：\t\n\t成功人数：" + trueNum + "\t\n\t失败人数："
					+ falseNum);

		} catch (AxisFault e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getPersonByCode(String personCode) throws Exception {

	}

	public String delete(String pk_psndoc, String arg0) throws Exception {

		// IQueryList queryList = (IQueryList)
		// NCLocator.getInstance().lookup(IQueryList.class.getName());
		QueryList queryList = new QueryList();
		List<Long> ids = queryList.getDeletePerson(pk_psndoc, arg0);
		String[] defs = queryList.getPersonDefVal(pk_psndoc, arg0);
		queryList.updateDelPerson2(pk_psndoc, arg0);
		queryList.updateNewPerson(pk_psndoc, defs, arg0);
		
		int falseNum = 0;
		int trueNum = 0;
		if (ids.size() == 0)
			return null;
		for (long id : ids) {
			if (id == 0)
				continue;

			if (id == -1) {
				falseNum++;
				continue;
			}
			try {
				PersonServiceStub stub = new PersonServiceStub();
				PersonServiceStub.Delete req = new PersonServiceStub.Delete();

				req.setToken(ServiceUtil.getToken());
				req.setPersonId(id);

				PersonServiceStub.DeleteResponse resp = stub.delete(req);
				ServiceResponse r = resp.get_return();

				// System.out.println("被删除人员的ID：" + r.getResult() + ",错误号：" +
				// r.getErrorNumber() + ",错误信息：" + r.getErrorMessage());

				
					queryList.updateDelPerson(String.valueOf(r.getResult()),
							arg0);
				

				trueNum++;
				
				stub._getServiceClient().cleanupTransport();
				stub._getServiceClient().cleanup();
				stub.cleanup();
				stub = null;
				
				
			} catch (Exception e) {
				falseNum++;
			}
		}

		System.out.println("人员OA删除操作：\t\n\t成功删除人数：" + trueNum + "\t\n\t删除失败人数："
				+ falseNum);
		return null;
	}

	/**
	 * 同步部门，废除此功能， 在OA中使用同步功能同步部门
	 */
	public String createDept(String arg0) throws Exception {
		IQueryList queryList = (IQueryList) NCLocator.getInstance().lookup(
				IQueryList.class.getName());
		// QueryList queryList = new QueryList();
		List<String[]> unitNames = queryList.getUnits(arg0);
		Map<String[], List<String[]>> deptMap = new HashMap<String[], List<String[]>>();
		for (String[] strs : unitNames) {
			deptMap.put(strs, queryList.getDepts(strs[1], arg0));
		}

		String retMsg = "";
		Iterator item = deptMap.entrySet().iterator();

		while (item.hasNext()) {
			Map.Entry entry = (Map.Entry) item.next();
			String[] units = (String[]) entry.getKey();
			List<String[]> deptList = (List<String[]>) entry.getValue();
			int falseNum = 0;
			int trueNum = 0;
			long accountId = ServiceUtil.getAccountId(units[0]);
			for (String[] deptVO2 : deptList) {
				try {
					DepartmentServiceStub stub = new DepartmentServiceStub();
					DepartmentServiceStub.Create req = new DepartmentServiceStub.Create();
					DepartmentServiceStub.DepartmentInfoParam_All dept = new DepartmentServiceStub.DepartmentInfoParam_All();

					if (accountId == -1)
						break;
					dept.setAccountId(accountId);
					dept.setDepartmentName(deptVO2);

					req.setToken(ServiceUtil.getToken());
					req.setDepartment(dept);

					DepartmentServiceStub.CreateResponse resp = stub
							.create(req);
					com.seeyon.client.DepartmentServiceStub.ServiceResponse r = resp
							.get_return();

					try {
						this.enableDeptByArray(deptVO2, units[0], true);
					} catch (Exception e) {
						falseNum++;
						continue;
					}

					System.out.println("新建部门的ID：" + r.getResult() + ",错误号："
							+ r.getErrorNumber() + ",错误信息："
							+ r.getErrorMessage());

					try {
						queryList.updateDepartment(units[1], deptVO2[1], arg0);
					} catch (Exception e) {
						falseNum++;
						continue;
					}

					trueNum++;

					stub._getServiceClient().cleanupTransport();
					stub._getServiceClient().cleanup();
					stub.cleanup();
					stub = null;
					
					
				} catch (Exception ex) {
					falseNum++;
					continue;
				}
			}
			if (deptList.size() > 0)
				retMsg += "【 " + units[0] + " 】 ： 共" + deptList.size()
						+ "条 , 成功" + trueNum + "条 , 失败" + falseNum + "条。 \t\n";

		}

		if (retMsg == "" || "".equals(retMsg))
			retMsg = "没有可以同步的部门信息！";

		return retMsg;
	}

	public void enableDeptByArray(String[] names, String unitname,
			boolean enabled) {
		try {

			DepartmentServiceStub stub = new DepartmentServiceStub();
			DepartmentServiceStub.EnableByNameArray req = new DepartmentServiceStub.EnableByNameArray();

			req.setToken(ServiceUtil.getToken());
			req.setAccountName(unitname);
			req.setDepartmentName(names);
			req.setEnable(enabled);

			DepartmentServiceStub.EnableByNameArrayResponse resp = stub
					.enableByNameArray(req);
			com.seeyon.client.DepartmentServiceStub.ServiceResponse r = resp
					.get_return();
			System.out.println("启用部门的ID：" + r.getResult() + ",错误号："
					+ r.getErrorNumber() + ",错误信息：" + r.getErrorMessage());

			stub._getServiceClient().cleanupTransport();
			stub._getServiceClient().cleanup();
			stub.cleanup();
			stub = null;
			
		} catch (AxisFault e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 同步岗位，废除此功能， 在OA中使用同步功能同步岗位
	 */
	public String createOcuption(String arg0) throws Exception {
		IQueryList queryList = (IQueryList) NCLocator.getInstance().lookup(
				IQueryList.class.getName());
		// QueryList queryList = new QueryList();
		List<String[]> unitNames = queryList.getUnits(arg0);
		Map<String[], List<String[]>> deptMap = new HashMap<String[], List<String[]>>();
		for (String[] strs : unitNames) {
			deptMap.put(strs, queryList.getOcup(strs[1], arg0));
		}

		String retMsg = "";
		Iterator item = deptMap.entrySet().iterator();

		while (item.hasNext()) {
			Map.Entry entry = (Map.Entry) item.next();
			String[] units = (String[]) entry.getKey();
			List<String[]> ocupList = (List<String[]>) entry.getValue();
			int falseNum = 0;
			int trueNum = 0;
			long accountId = ServiceUtil.getAccountId(units[0]);
			for (String[] ocupVO2 : ocupList) {
				try {
					OcupationServiceStub stub = new OcupationServiceStub();
					OcupationServiceStub.Create req = new OcupationServiceStub.Create();
					OcupationServiceStub.OcupationInfoParam_A8_All ocupation = new OcupationServiceStub.OcupationInfoParam_A8_All();
					// 必填
					if (accountId == -1)
						break;

					ocupation.setAccountId(accountId);
					ocupation.setOcupationName(ocupVO2[1]);

					//
					req.setToken(ServiceUtil.getToken());
					req.setOcupation(ocupation);

					OcupationServiceStub.CreateResponse resp = stub.create(req);
					com.seeyon.client.OcupationServiceStub.ServiceResponse r = resp
							.get_return();

					System.out.println("新建岗位的ID：" + r.getResult() + ",错误号："
							+ r.getErrorNumber() + ",错误信息："
							+ r.getErrorMessage());

					try {
						this.enableById(r.getResult(), true);
					} catch (Exception e) {
						falseNum++;
						continue;
					}

					try {
						queryList.updateOcupation(units[1], ocupVO2[1],
								ocupVO2[2], arg0);
					} catch (Exception e) {
						falseNum++;
						continue;
					}

					trueNum++;

					
					stub._getServiceClient().cleanupTransport();
					stub._getServiceClient().cleanup();
					stub.cleanup();
					stub = null;
					
				} catch (Exception e) {
					falseNum++;
					continue;
				}
			}
			if (ocupList.size() > 0)
				retMsg += "【 " + units[0] + " 】 ： 共" + ocupList.size()
						+ "条 , 成功" + trueNum + "条 , 失败" + falseNum + "条。 \t\n";

		}

		if (retMsg == "" || "".equals(retMsg))
			retMsg = "没有可以同步的岗位信息！";

		return retMsg;
	}

	public void enableById(long id, boolean enabled) {
		try {

			OcupationServiceStub stub = new OcupationServiceStub();
			OcupationServiceStub.Enable req = new OcupationServiceStub.Enable();

			req.setToken(ServiceUtil.getToken());
			req.setOcupationId(id);
			req.setEnable(enabled);

			OcupationServiceStub.EnableResponse resp = stub.enable(req);
			com.seeyon.client.OcupationServiceStub.ServiceResponse r = resp
					.get_return();
			System.out.println("启用岗位的ID：" + r.getResult() + ",错误号："
					+ r.getErrorNumber() + ",错误信息：" + r.getErrorMessage());

			
			stub._getServiceClient().cleanupTransport();
			stub._getServiceClient().cleanup();
			stub.cleanup();
			stub = null;
			
			
		} catch (AxisFault e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// public void output(String msg) {
	// System.out.println(msg);
	// }

	/**
	 * 测试人员同步OA示例
	 */
	// public void create(String loginName) {
	// try {
	// PersonServiceStub stub = new PersonServiceStub();
	// PersonServiceStub.Create req = new PersonServiceStub.Create();
	// PersonServiceStub.PersonInfoParam_All person = new
	// PersonServiceStub.PersonInfoParam_All();
	// // 必填
	// person.setAccountId(ServiceUtil.getAccountId("红狮水泥"));
	// person.setTrueName("River Yang");
	// person.setLoginName(loginName);
	// person.setPassWord("123456");
	//
	// //
	// req.setToken(ServiceUtil.getToken());
	// req.setPerson(person);
	//
	// PersonServiceStub.CreateResponse resp = stub.create(req);
	// ServiceResponse r = resp.get_return();
	// output("新建人员的ID：" + r.getResult());
	// output("错误号：" + r.getErrorNumber());
	// output("错误信息：" + r.getErrorMessage());
	//
	// } catch (AxisFault e) {
	// e.printStackTrace();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// }
	//
	//
	// public void update(long id) {
	// try {
	//
	// PersonServiceStub stub = new PersonServiceStub();
	// PersonServiceStub.Update req = new PersonServiceStub.Update();
	// PersonServiceStub.PersonInfoParam_All person = new
	// PersonServiceStub.PersonInfoParam_All();
	// // 必填
	//
	//
	// // 要修改的信息
	// person.setTrueName("测试按ID修改");
	// person.setPer_sort("6");
	// // [父部门名称,部门名称]
	// String[] deptNames = new String[2];
	// deptNames[0] = "部门1";
	// deptNames[1] = "部门3";
	// person.setDepartmentName(deptNames);
	// person.setOcupationName("总经理");
	// String[] secondOcupationNames = new String[2];
	// // 部门_岗位
	// secondOcupationNames[0] = "部门1_总经理";
	// secondOcupationNames[1] = "部门3_部门经理";
	// person.setSecondOcupationName(secondOcupationNames);
	// person.setOtypeName("总经理");
	// person.setPassWord("123456");
	// person.setSex("1");
	//			
	// req.setToken(ServiceUtil.getToken());
	// req.setPersonId(id);
	// req.setPerson(person);
	//			
	// PersonServiceStub.UpdateResponse resp = stub.update(req);
	// ServiceResponse r = resp.get_return();
	// output("被修改人员的ID：" + r.getResult());
	// output("错误号：" + r.getErrorNumber());
	// output("错误信息：" + r.getErrorMessage());
	//
	// } catch (AxisFault e) {
	// e.printStackTrace();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// public void deleteByLoginName(String loginName) {
	// try {
	//
	// PersonServiceStub stub = new PersonServiceStub();
	// PersonServiceStub.DeleteByLoginName req = new
	// PersonServiceStub.DeleteByLoginName();
	//
	// req.setToken(ServiceUtil.getToken());
	// req.setLoginName(loginName);
	//
	// PersonServiceStub.DeleteByLoginNameResponse resp = stub
	// .deleteByLoginName(req);
	// ServiceResponse r = resp.get_return();
	// output("被删除人员的ID：" + r.getResult());
	// output("错误号：" + r.getErrorNumber());
	// output("错误信息：" + r.getErrorMessage());
	//
	// } catch (AxisFault e) {
	// e.printStackTrace();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//	
	// public void delete(long id) {
	// try {
	//
	// PersonServiceStub stub = new PersonServiceStub();
	// PersonServiceStub.Delete req = new PersonServiceStub.Delete();
	//
	// req.setToken(ServiceUtil.getToken());
	// req.setPersonId(id);
	//
	// PersonServiceStub.DeleteResponse resp = stub
	// .delete(req);
	// ServiceResponse r = resp.get_return();
	// output("被删除人员的ID：" + r.getResult());
	// output("错误号：" + r.getErrorNumber());
	// output("错误信息：" + r.getErrorMessage());
	//
	// } catch (AxisFault e) {
	// e.printStackTrace();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//	
	// public void enable(long id,boolean enabled) {
	// try {
	//
	// PersonServiceStub stub = new PersonServiceStub();
	// PersonServiceStub.Enable req = new PersonServiceStub.Enable();
	//
	// req.setToken(ServiceUtil.getToken());
	// req.setPersonId(id);
	// req.setEnable(enabled);
	//
	// PersonServiceStub.EnableResponse resp = stub
	// .enable(req);
	// ServiceResponse r = resp.get_return();
	// output("启用人员的ID：" + r.getResult());
	// output("错误号：" + r.getErrorNumber());
	// output("错误信息：" + r.getErrorMessage());
	//
	// } catch (AxisFault e) {
	// e.printStackTrace();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//	
}
