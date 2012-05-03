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
 * ������Ա
 * 
 * @author Administrator
 * 
 */
public class MbSyn implements IMbSys {
	// public static void main(String[] args) {
	// try {
	//
	// PersonServiceTest test = new PersonServiceTest();
	// // ����
	// // test.create("ry");
	//			
	// // ����¼������
	// test.updateByLoginName();
	//			
	// // ��ID����
	// // test.update(4647737107627917717l);
	//			
	// // ����¼��ɾ��
	// // test.create("testservice2");
	// // test.deleteByLoginName("testservice2");
	//			
	// // ��IDɾ��
	// // test.create("testservice2");
	// // test.delete(-1308372468846015786l);
	//			
	// // ����¼������/����
	// // test.enableByLoginName("testservice7", false);
	// test.enableByLoginName("ry_", true);
	//			
	// // ��ID����/����
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
	 * ͬ����Ա��OA
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

				System.out.println("�½���Ա��ID��" + r.getResult() + ",����ţ�"
						+ r.getErrorNumber() + ",������Ϣ��" + r.getErrorMessage());

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
			return "ͬ����Ա��Ϣ��\t\n\tû�п���ͬ����OA����Ա��Ϣ��";

		return "ͬ����Ա��Ϣ��\t\n\t�ɹ�������" + trueNum + "\t\n\tʧ��������" + falseNum;
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
			System.out.println("������Ա��ID��" + r.getResult() + ",����ţ�"
					+ r.getErrorNumber() + ",������Ϣ��" + r.getErrorMessage());
			
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
			System.out.println("������Ա��ID��" + r.getResult());
			System.out.println("����ţ�" + r.getErrorNumber());
			System.out.println("������Ϣ��" + r.getErrorMessage());

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
				System.out.println("��Ա�б�:NULL");
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
				System.out.println("���޸���Ա��ID��" + r.getResult());
				System.out.println("����ţ�" + r.getErrorNumber());
				System.out.println("������Ϣ��" + r.getErrorMessage());
				
				this.enable(id, arg4);
				
				if(r.getResult() == -1L) {
//					this.create(source, pk_psndoc, "Y"); // ��ֹOA�����ظ�
					throw new Exception ("���˺�ID�����쳣������ϵ�����Ա");
					
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
//						System.out.println("��Ա�б�:NULL");
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
//				System.out.println("���޸���Ա��ID��" + _r.getResult());
//				System.out.println("����ţ�" + _r.getErrorNumber());
//				System.out.println("������Ϣ��" + _r.getErrorMessage());
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
				System.out.println("��Ա�б�:NULL");
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
			System.out.println("���޸���Ա��ID��" + r.getResult());
			System.out.println("����ţ�" + r.getErrorNumber());
			System.out.println("������Ϣ��" + r.getErrorMessage());

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
				System.out.println("��Ա�б�:NULL");
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

				System.out.println("�½���Ա��ID��" + r.getResult() + ",����ţ�"
						+ r.getErrorNumber() + ",������Ϣ��" + r.getErrorMessage());

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

			System.out.println("ͬ����Ա��Ϣ��\t\n\t�ɹ�������" + trueNum + "\t\n\tʧ��������"
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

				// System.out.println("��ɾ����Ա��ID��" + r.getResult() + ",����ţ�" +
				// r.getErrorNumber() + ",������Ϣ��" + r.getErrorMessage());

				
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

		System.out.println("��ԱOAɾ��������\t\n\t�ɹ�ɾ��������" + trueNum + "\t\n\tɾ��ʧ��������"
				+ falseNum);
		return null;
	}

	/**
	 * ͬ�����ţ��ϳ��˹��ܣ� ��OA��ʹ��ͬ������ͬ������
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

					System.out.println("�½����ŵ�ID��" + r.getResult() + ",����ţ�"
							+ r.getErrorNumber() + ",������Ϣ��"
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
				retMsg += "�� " + units[0] + " �� �� ��" + deptList.size()
						+ "�� , �ɹ�" + trueNum + "�� , ʧ��" + falseNum + "���� \t\n";

		}

		if (retMsg == "" || "".equals(retMsg))
			retMsg = "û�п���ͬ���Ĳ�����Ϣ��";

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
			System.out.println("���ò��ŵ�ID��" + r.getResult() + ",����ţ�"
					+ r.getErrorNumber() + ",������Ϣ��" + r.getErrorMessage());

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
	 * ͬ����λ���ϳ��˹��ܣ� ��OA��ʹ��ͬ������ͬ����λ
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
					// ����
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

					System.out.println("�½���λ��ID��" + r.getResult() + ",����ţ�"
							+ r.getErrorNumber() + ",������Ϣ��"
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
				retMsg += "�� " + units[0] + " �� �� ��" + ocupList.size()
						+ "�� , �ɹ�" + trueNum + "�� , ʧ��" + falseNum + "���� \t\n";

		}

		if (retMsg == "" || "".equals(retMsg))
			retMsg = "û�п���ͬ���ĸ�λ��Ϣ��";

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
			System.out.println("���ø�λ��ID��" + r.getResult() + ",����ţ�"
					+ r.getErrorNumber() + ",������Ϣ��" + r.getErrorMessage());

			
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
	 * ������Աͬ��OAʾ��
	 */
	// public void create(String loginName) {
	// try {
	// PersonServiceStub stub = new PersonServiceStub();
	// PersonServiceStub.Create req = new PersonServiceStub.Create();
	// PersonServiceStub.PersonInfoParam_All person = new
	// PersonServiceStub.PersonInfoParam_All();
	// // ����
	// person.setAccountId(ServiceUtil.getAccountId("��ʨˮ��"));
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
	// output("�½���Ա��ID��" + r.getResult());
	// output("����ţ�" + r.getErrorNumber());
	// output("������Ϣ��" + r.getErrorMessage());
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
	// // ����
	//
	//
	// // Ҫ�޸ĵ���Ϣ
	// person.setTrueName("���԰�ID�޸�");
	// person.setPer_sort("6");
	// // [����������,��������]
	// String[] deptNames = new String[2];
	// deptNames[0] = "����1";
	// deptNames[1] = "����3";
	// person.setDepartmentName(deptNames);
	// person.setOcupationName("�ܾ���");
	// String[] secondOcupationNames = new String[2];
	// // ����_��λ
	// secondOcupationNames[0] = "����1_�ܾ���";
	// secondOcupationNames[1] = "����3_���ž���";
	// person.setSecondOcupationName(secondOcupationNames);
	// person.setOtypeName("�ܾ���");
	// person.setPassWord("123456");
	// person.setSex("1");
	//			
	// req.setToken(ServiceUtil.getToken());
	// req.setPersonId(id);
	// req.setPerson(person);
	//			
	// PersonServiceStub.UpdateResponse resp = stub.update(req);
	// ServiceResponse r = resp.get_return();
	// output("���޸���Ա��ID��" + r.getResult());
	// output("����ţ�" + r.getErrorNumber());
	// output("������Ϣ��" + r.getErrorMessage());
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
	// output("��ɾ����Ա��ID��" + r.getResult());
	// output("����ţ�" + r.getErrorNumber());
	// output("������Ϣ��" + r.getErrorMessage());
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
	// output("��ɾ����Ա��ID��" + r.getResult());
	// output("����ţ�" + r.getErrorNumber());
	// output("������Ϣ��" + r.getErrorMessage());
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
	// output("������Ա��ID��" + r.getResult());
	// output("����ţ�" + r.getErrorNumber());
	// output("������Ϣ��" + r.getErrorMessage());
	//
	// } catch (AxisFault e) {
	// e.printStackTrace();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//	
}
