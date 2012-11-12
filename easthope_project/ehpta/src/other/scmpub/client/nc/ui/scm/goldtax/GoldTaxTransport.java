package nc.ui.scm.goldtax;

import static nc.vo.jcom.lang.StringUtil.isEmpty;
import static nc.vo.jcom.lang.StringUtil.isEmptyWithTrim;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import nc.ui.ehpta.pub.convert.ConvertFunc;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.goldtax.Configuration;
import nc.vo.scm.goldtax.GoldTaxBodyVO;
import nc.vo.scm.goldtax.GoldTaxHeadVO;
import nc.vo.scm.goldtax.GoldTaxVO;
import nc.vo.scm.merge.DefaultVOMerger;
import nc.vo.scm.pub.SCMEnv;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * ��˰�����࣬�ṩ��˰�ϲ�����֡��������ļ��ȷ���
 * 
 * @author ��ǿ��
 * @since 2008-8-27
 */
public class GoldTaxTransport {
	/** ��˰�ļ�ע���е�ǰ׺ */
	private static final String COMMENT_PREFIX = "//";
	/** ��˰VO�пͻ�ID�������� */
	private static final String ATTR_CUSTOMER_ID = "customerId";
	/** ��˰VO�д���������� */
	private static final String ATTR_INVENTORY = "invBaseId";
	/** ��˰VO�д������������ */
	private static final String ATTR_INVCLASS = "invClassId";
	/** ��˰VO�м۸��������� */
	private static final String ATTR_PRICE = "price";

	/** ��˰VO�������������� */
	private static final String ATTR_QUANTITY = "number";
	/** ��˰VO�н���������� */
	private static final String ATTR_MONEY = "money";
	/** ��˰VO����˰����������� */
	private static final String ATTR_NOTAXMONEY = "noTaxMny";
	
	/** ��˾���� */
	private String corp = null;
	/** ����˰������Ϣ */
	private Configuration conf = null;
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public GoldTaxTransport(String corp) {
		this.corp = corp;
	}

	/**
	 * @return ����˰������Ϣ
	 */
	Configuration getConf() {
		if (null == conf) {
			// װ�ز���
			conf = Configuration.load(corp);
		}
		return conf;
	}

	/**
	 * @param conf ����˰������Ϣ
	 */
	void setConf(Configuration conf) {
		this.conf = conf;
	}

	/**
	 * �ϲ��ͷֵ������˰VO
	 * 
	 * @param goldTaxVOs ������Ľ�˰VO����
	 * @return �����Ľ�˰VO����
	 */
	public GoldTaxVO[] mergeAndSplit(GoldTaxVO[] goldTaxVOs) {
		// ���Ƚ��б�ͷ�ϲ�
		goldTaxVOs = mergeHead(goldTaxVOs);
		// �ٽ��б���ϲ�
		goldTaxVOs = mergeBody(goldTaxVOs);
		// �ٽ��зֵ�
		goldTaxVOs = split(goldTaxVOs);

		return goldTaxVOs;
	}

	/**
	 * ���տͻ��ϲ���ͷ��Ϣ��������Ժϲ���
	 * ��ϲ���ľۺ�VO������ֵȡ�ϲ��ı�ͷVO���е�һ����ͷ���ڵľۻ��ͷ�ϲ��Ľ�˰VO�ı�������һ�ϲ�Ϊһ������ŵ��ϲ���ľۺ�VO��ֵ��
	 * ��ͷVO�úϲ������ɵ�VO������VO����Ϊ�������кϲ������б�ͷVO��Ӧ�ı���VO������
	 * 
	 * @param goldTaxVOs ��˰VO����
	 * @return �ϲ���ͷ��Ľ�˰VO����
	 */
	private GoldTaxVO[] mergeHead(GoldTaxVO[] goldTaxVOs) {
		// ���û�ж൥�ϲ�������Ҫ�ϲ���ͷ��ֱ�ӷ���
		if (!getConf().isMergeMutiBill()) {
			return goldTaxVOs;
		}

		Map<String, List<GoldTaxVO>> mapOfCust = new HashMap<String, List<GoldTaxVO>>();
		GoldTaxHeadVO[] allHeadVOs = new GoldTaxHeadVO[goldTaxVOs.length];
		// ��ͷ���ͻ�����
		for (int i = 0; i < goldTaxVOs.length; i++) {
			GoldTaxVO taxVO = goldTaxVOs[i];
			allHeadVOs[i] = taxVO.getParentVO();
			List<GoldTaxVO> list = mapOfCust.get(taxVO.getParentVO().getCustomerId());
			if (null == list) {
				list = new ArrayList<GoldTaxVO>();
				mapOfCust.put(taxVO.getParentVO().getCustomerId(), list);
			}
			list.add(taxVO);
		}

		// �ϲ���ͷ
		GoldTaxHeadVO[] mergedHeadVO = null;
		DefaultVOMerger merger = new DefaultVOMerger();
		merger.setGroupingAttr(new String[]{ATTR_CUSTOMER_ID});
		try {
			mergedHeadVO = (GoldTaxHeadVO[]) merger.mergeByGroup(allHeadVOs);
		} catch (BusinessException e) {
			SCMEnv.error("�ϲ������쳣", e);
			throw new BusinessRuntimeException(e.getMessage(), e);
		}

		// �µĺϲ���ľۺ�VO����
		GoldTaxVO[] mergedTaxVO = new GoldTaxVO[mergedHeadVO.length];
		for (int i = 0; i < mergedHeadVO.length; i++) {
			GoldTaxHeadVO taxHeadVO = mergedHeadVO[i];

			// ���ܺϲ��˱�ͷ�ı���VO
			List<GoldTaxBodyVO> bodyVOsOfCust = new ArrayList<GoldTaxBodyVO>();
			// ���а����ĵ��ݺ�
			Set<String> codes = new HashSet<String>();
			List<GoldTaxVO> taxVoOfCust = mapOfCust.get(taxHeadVO.getCustomerId());
			for (GoldTaxVO taxVO : taxVoOfCust) {
				bodyVOsOfCust.addAll(Arrays.asList(taxVO.getChildrenVO()));
				codes.add(taxVO.getParentVO().getCode());
			}

			// �µĵ��ݺ�
			String newCode = "";
			for (String code : codes) {
				newCode += code;
			}

			// �µľۺ�VO
			GoldTaxVO taxVO = simplyCopy(taxVoOfCust.get(0));
			// ��ͷVOΪ�ϲ���ı�ͷ
			taxVO.setParentVO(taxHeadVO);
			// �����µ��ݺ�
			taxHeadVO.setCode(newCode);
			// ����VOΪ�����ϲ������б�ͷVO��Ӧ�ı���VO����
			taxVO.setChildrenVO(bodyVOsOfCust.toArray(new GoldTaxBodyVO[bodyVOsOfCust.size()]));
			mergedTaxVO[i] = taxVO;
		}

		return mergedTaxVO;
	}

	/**
	 * ��ÿ����˰VO�ı�����кϲ�
	 * 
	 * @param goldTaxVOs
	 * @return �ϲ������Ľ�˰VO����
	 */
	private GoldTaxVO[] mergeBody(GoldTaxVO[] goldTaxVOs) {
		List<String> groupAttrs = new ArrayList<String>();
		if (getConf().isMergeInventory()) {
			groupAttrs.add(ATTR_INVENTORY);
		}
		if (getConf().isMergeInvClass()) {
			groupAttrs.add(ATTR_INVCLASS);
		}
		if (getConf().isMergePrice()) {
			groupAttrs.add(ATTR_PRICE);
		}

		// ���û�кϲ��ֶΣ�����Ҫ�ϲ���ֱ�ӷ���
		if (groupAttrs.size() == 0) {
			return goldTaxVOs;
		}

		String[] groups = groupAttrs.toArray(new String[groupAttrs.size()]);
		String[] sums = new String[]{ATTR_QUANTITY, ATTR_MONEY, ATTR_NOTAXMONEY};

		DefaultVOMerger merger = new DefaultVOMerger();
		merger.setGroupingAttr(groups);
		merger.setSummingAttr(sums);
		for (GoldTaxVO taxVO : goldTaxVOs) {
			try {
				taxVO.setChildrenVO(merger.mergeByGroup(taxVO.getChildrenVO()));
			} catch (BusinessException e) {
				SCMEnv.error("�ϲ������쳣", e);
				throw new BusinessRuntimeException(e.getMessage(), e);
			}
			// ���¼��㵥��
			reCalPrice(taxVO.getChildrenVO());
		}
		return goldTaxVOs;
	}

	/**
	 * ���ݽ����������¼��㵥��
	 * 
	 * @param taxBodyVOs Ҫ���¼��㵥�۵Ľ�˰����VO����
	 */
	private void reCalPrice(GoldTaxBodyVO[] taxBodyVOs) {
		for (GoldTaxBodyVO bodyVO : taxBodyVOs) {
			// ��˰����
			bodyVO.setPrice(bodyVO.getMoney().div(bodyVO.getNumber()));
			// ��˰����
			bodyVO.setNoTaxPrice(bodyVO.getNoTaxMny().div(bodyVO.getNumber()));
		}
	}

	/**
	 * �Խ�˰������˰����޶���������зֵ�
	 * 
	 * @param taxVOs ���ֵ��Ľ�˰VO����
	 * @return �ֵ���Ľ�˰VO����
	 */
	GoldTaxVO[] split(GoldTaxVO[] taxVOs) {
		List<GoldTaxVO> orgTaxes = new ArrayList<GoldTaxVO>();
		orgTaxes.addAll(Arrays.asList(taxVOs));
		List<GoldTaxVO> splited = new ArrayList<GoldTaxVO>();

		while (!orgTaxes.isEmpty()) {
			boolean split = false;
			UFDouble sumMny = new UFDouble(0);
			int rownum = 0;
			GoldTaxVO curTax = orgTaxes.remove(0);
			for (GoldTaxBodyVO bodyVO : curTax.getChildrenVO()) {
				if (needSplitRow(rownum + 1)) {
					// ����
					GoldTaxVO[] splitedVO = splitRow(curTax, rownum);
					splited.add(splitedVO[0]);
					orgTaxes.add(0, splitedVO[1]);
					split = true;
					break;
				} else if (needSplitMny(sumMny.add(bodyVO.getNoTaxMny()))) {
					// ����
					GoldTaxVO[] splitedVO = splitMny(curTax);
					splited.add(splitedVO[0]);
					orgTaxes.add(0, splitedVO[1]);
					split = true;
					break;
				} else {
					sumMny = sumMny.add(bodyVO.getNoTaxMny());
					rownum++;
				}
			}
			if (!split) {
				// ����Ҫ�ֵ���ֱ�ӷ����ѷֵ��б�
				splited.add(curTax);
			}
		}
		return splited.toArray(new GoldTaxVO[splited.size()]);
	}

	/**
	 * ���ݸ����������ж��Ƿ���Ҫ�ֵ�
	 * 
	 * @param rownum ����
	 * @return �����Ҫ�ֵ�����true�����򷵻�false
	 */
	boolean needSplitRow(int rownum) {
		int maxRows = getConf().getMaxRows();
		return maxRows > 0 && rownum > maxRows;
	}

	/**
	 * ���ݸ����������ֵ�
	 * 
	 * @param rownum
	 *            ����
	 * @return ����Ϊ2�Ľ�˰VO���飬��һ��Ԫ��Ϊ�ֳ���VO���ڶ���Ԫ��Ϊʣ�µ�VO
	 */
	GoldTaxVO[] splitRow(GoldTaxVO taxVO, int rownum) {
		// �ֳ���VO
		GoldTaxBodyVO[] bodyVOs1 = new GoldTaxBodyVO[rownum];
		System.arraycopy(taxVO.getChildrenVO(), 0, bodyVOs1, 0, bodyVOs1.length);
		GoldTaxHeadVO headVO1 = simplyCopy(taxVO.getParentVO());
		GoldTaxVO taxVO1 = simplyCopy(taxVO);
		taxVO1.setParentVO(headVO1);
		taxVO1.setChildrenVO(bodyVOs1);
		// ʣ���VO
		GoldTaxBodyVO[] bodyVOs2 = new GoldTaxBodyVO[taxVO.getChildrenVO().length - rownum];
		System.arraycopy(taxVO.getChildrenVO(), rownum, bodyVOs2, 0, bodyVOs2.length);
		GoldTaxHeadVO headVO2 = simplyCopy(taxVO.getParentVO());
		GoldTaxVO taxVO2 = simplyCopy(taxVO);
		taxVO2.setParentVO(headVO2);
		taxVO2.setChildrenVO(bodyVOs2);
		// ���ص�VO����
		GoldTaxVO[] splited = new GoldTaxVO[2];
		splited[0] = taxVO1;
		splited[1] = taxVO2;
		return splited;
	}

	/**
	 * ���ݸ����Ľ���ж��Ƿ���Ҫ�ֵ�
	 * 
	 * @param money Ҫ�жϵ���˰���
	 * @return �����Ҫ�ֵ�����true�����򷵻�false
	 */
	boolean needSplitMny(UFDouble money) {
		UFDouble maxNoTaxMny = getConf().getMaxNoTaxMny();
		return (null != maxNoTaxMny) 
				&& maxNoTaxMny.compareTo(new UFDouble(0)) > 0
				&& money.compareTo(maxNoTaxMny) > 0;
	}

	/**
	 * ���ݸ����ķֵ�
	 * 
	 * @param rownum
	 *            ����
	 * @return ����Ϊ2�Ľ�˰VO���飬��һ��Ԫ��Ϊ�ֳ���VO���ڶ���Ԫ��Ϊʣ�µ�VO
	 */
	GoldTaxVO[] splitMny(GoldTaxVO taxVO) {
		UFDouble maxMny = getConf().getMaxNoTaxMny();
		UFDouble diffMny = maxMny;
		int splitRownum = 0;
		UFDouble diffNum = null;
		for (int i = 0; i < taxVO.getChildrenVO().length; i++ ) {
			GoldTaxBodyVO bodyVO = taxVO.getChildrenVO()[i];
			if (diffMny.sub(bodyVO.getNoTaxMny()).doubleValue() < 0) {
				diffNum = diffMny.div(bodyVO.getNoTaxPrice());
				// ���޶����ʱ�����Ƿ����Ϊ��������������������� 1 ʱ������ȥС��
				if (getConf().isIntegerQuantityOverMny() && diffNum.doubleValue() > 1) {
					diffNum = new UFDouble(diffNum.intValue());
					diffMny = bodyVO.getNoTaxPrice().multiply(diffNum);
				}
				splitRownum = i;
				break;
			}
			diffMny = diffMny.sub(bodyVO.getNoTaxMny());
		}
		if (diffNum.compareTo(new UFDouble(0)) == 0) {
			// ���������������㣬���ʾ����Ҫ��������ֻ��Ҫ���зֵ��Ϳ�����
			return splitRow(taxVO, splitRownum);
		}
		// �ֳ���VO
		GoldTaxBodyVO splitBody1 = simplyCopy(taxVO.getChildrenVO()[splitRownum]);
		splitBody1.setNumber(diffNum);
		splitBody1.setMoney(splitBody1.getPrice().multiply(splitBody1.getNumber()));
		splitBody1.setNoTaxMny(diffMny);
		GoldTaxBodyVO[] bodyVOs1 = new GoldTaxBodyVO[splitRownum + 1];
		System.arraycopy(taxVO.getChildrenVO(), 0, bodyVOs1, 0, bodyVOs1.length - 1);
		bodyVOs1[splitRownum] = splitBody1;
		GoldTaxHeadVO headVO1 = simplyCopy(taxVO.getParentVO());
		GoldTaxVO taxVO1 = simplyCopy(taxVO);
		taxVO1.setParentVO(headVO1);
		taxVO1.setChildrenVO(bodyVOs1);
		// ʣ���VO
		GoldTaxBodyVO splitBody2 = simplyCopy(taxVO.getChildrenVO()[splitRownum]);
		splitBody2.setNumber(taxVO.getChildrenVO()[splitRownum].getNumber().sub(diffNum));
		splitBody2.setMoney(taxVO.getChildrenVO()[splitRownum].getMoney().sub(splitBody1.getMoney()));
		splitBody2.setNoTaxMny(taxVO.getChildrenVO()[splitRownum].getNoTaxMny().sub(splitBody1.getNoTaxMny()));
		GoldTaxBodyVO[] bodyVOs2 = new GoldTaxBodyVO[taxVO.getChildrenVO().length - splitRownum];
		System.arraycopy(taxVO.getChildrenVO(), splitRownum + 1, bodyVOs2, 1, bodyVOs2.length - 1);
		bodyVOs2[0] = splitBody2;
		GoldTaxHeadVO headVO2 = simplyCopy(taxVO.getParentVO());
		GoldTaxVO taxVO2 = simplyCopy(taxVO);
		taxVO2.setParentVO(headVO2);
		taxVO2.setChildrenVO(bodyVOs2);
		// ���ص�VO����
		GoldTaxVO[] splited = new GoldTaxVO[2];
		splited[0] = taxVO1;
		splited[1] = taxVO2;
		return splited;
	}

	/**
	 * �򵥿���
	 * 
	 * @param <T> ����Ҫ���޲����Ĺ��췽��
	 * @param o �������Ķ���
	 * @return ���������Ե��¶���
	 */
	@SuppressWarnings("unchecked")
	private static <T> T simplyCopy(T o) {
		T newObj = null;
		try {
			newObj = (T) o.getClass().newInstance();
			PropertyUtils.copyProperties(newObj, o);
		} catch (Exception e) {
			// ��Ϊ��˽�з�����֪�����п����Ķ������޲����Ĺ��췽����
			// ��������ͬ���͵Ķ��󿽱����ԣ����ᷢ���쳣�����Բ��׳���
			SCMEnv.error("�򵥿��������쳣", e);
		}
		return newObj;
	}

	/**
	 * ����˰VOд��ָ���ļ�
	 * 
	 * @param goldTaxVOs ��˰VO����
	 * @param filename Ҫд����ļ���
	 */
	public void saveToFiles(GoldTaxVO[] goldTaxVOs, String filename) {
		try {
			FileOutputStream fos = new FileOutputStream(filename);
			BufferedOutputStream os = new BufferedOutputStream(fos);
			// �����һ��
			if (goldTaxVOs.length > 0) {
				os.write(joinString(getAggregatedString(goldTaxVOs[0])).getBytes());
				os.write("\r\n".getBytes());
			}
			
			List<GoldTaxVO> taxVOList = new ArrayList<GoldTaxVO>();
			for (GoldTaxVO taxVO : goldTaxVOs) {
				taxVOList.addAll(mergeAndSplitGoldTax(taxVO));
			}
			
			GoldTaxVO[] nowTaxVOs = taxVOList.toArray(new GoldTaxVO[0]);
			for (GoldTaxVO taxVO : nowTaxVOs) {
				
				// �����ͷ
				os.write(joinString(getHeadString(taxVO)).getBytes());
				os.write("\r\n".getBytes());
				// �������
				for (GoldTaxBodyVO bodyVO : taxVO.getChildrenVO()) {
					os.write(joinString(getBodyString(bodyVO)).getBytes());
					os.write("\r\n".getBytes());
				}
			}
			os.close();
		} catch (Exception e) {
			SCMEnv.error("д�뵽�ļ������쳣", e);
			throw new BusinessRuntimeException("д�뵽�ļ������쳣", e);
		}
	}
	
	/**
	 * 
	 * 1.�Ⱥϲ�ͬ���Ľ��������1170W����ִ�в�֡�����ʣ��Ľ��������´���
	 * 2.�����Ʊ��ֻ��һ�������ֱ�ӽ�ʣ������һ�ŷ�Ʊ��
	 * 3.�����Ʊ�д��ڲ�ͬ����� ���1 + ���2 + ... + ���n <= 1170W �� �򽫲�ͬ���ϲ���ͬһ�ŷ�Ʊ�С�
	 * 4.��� ���1 + ���2 + ... + ��� (n/2) > 1170W �� �� ���1 + ���2 + ... + ��� (n/2 - 1)�Ľ����ϲ���ͬһ�ŷ�Ʊ��
	 * 5.��ʣ��Ľ����ϲ���ͬһ��Ʊ�С�
	 * 
	 * @author river
	 * @param taxVO
	 * @return
	 * @throws Exception
	 */
	protected final List<GoldTaxVO> mergeAndSplitGoldTax(GoldTaxVO taxVO) throws Exception {
		
		List<GoldTaxVO> taxList = new ArrayList<GoldTaxVO>();
		List<GoldTaxBodyVO> lessTaxBodyList = new ArrayList<GoldTaxBodyVO>();
		
		if(taxVO == null )
			return taxList;
					
		GoldTaxBodyVO[] taxBodyVO = taxVO.getChildrenVO().clone();
		
		if(taxBodyVO == null || taxBodyVO.length == 0)
			return taxList;
		
		Map<String , GoldTaxBodyVO> bodyMap = new ConcurrentHashMap<String , GoldTaxBodyVO>();
		Set<String> invSet = new HashSet<String>();
		for(GoldTaxBodyVO bodyVO : taxBodyVO) {
			invSet.add(bodyVO.getInvName() + "_" + bodyVO.getInvSpec());
		}
		
		/** ���ֲ��ϲ�ͬ����ͬ��� Start */
		for(String inv : invSet) {
			
			for(GoldTaxBodyVO bodyVO : taxBodyVO) {
				
				if(inv.equals(bodyVO.getInvName() + "_" + bodyVO.getInvSpec())) {
					
					if(bodyMap.get(bodyVO.getInvName() + "_" + bodyVO.getInvSpec()) == null)
						bodyMap.put(bodyVO.getInvName() + "_" + bodyVO.getInvSpec() , bodyVO);
					
					else {
						
						GoldTaxBodyVO cubodyVO = bodyMap.get(bodyVO.getInvName() + "_" + bodyVO.getInvSpec());
						GoldTaxBodyVO cbodyVO = (GoldTaxBodyVO) bodyVO.clone();
						
						cubodyVO.setNumber(ConvertFunc.change(cubodyVO.getNumber()).add(ConvertFunc.change(cbodyVO.getNumber())));
						cubodyVO.setMoney(ConvertFunc.change(cubodyVO.getMoney()).add(ConvertFunc.change(cbodyVO.getMoney())));
						
						bodyMap.put(bodyVO.getInvName() + "_" + bodyVO.getInvSpec() , bodyVO);
						
					}
					
				}
				
			}
			
		} /** End ���ֲ��ϲ�ͬ����ͬ��� */
		
		/** �����Ʊ��ֻ��һ�����ϣ���ֱ�ӽ��в��  */
		if(bodyMap != null && bodyMap.size() == 1) 
			return splitGoldTax(bodyMap.get(0), taxVO.getParentVO());
		
		/** �����Ʊ�д��ڶ������ϣ�����VO���ϼ���� Start */
		splitMoreGoldTax(bodyMap, taxList, lessTaxBodyList, taxVO);
		
		/** ����ʣ��Ľ���� Start */
		if(lessTaxBodyList != null && lessTaxBodyList.size() > 0) {
			
			int page = taxList.size();
			
			UFDouble sumMoney = UFDouble.ZERO_DBL;
			List<GoldTaxBodyVO> mergeBodyList = new ArrayList<GoldTaxBodyVO>();
			
			int mergeNum = 0;
			for(GoldTaxBodyVO taxBody : lessTaxBodyList) {
				
				sumMoney = sumMoney.add(ConvertFunc.change(taxBody.getMoney()));
				if(sumMoney.doubleValue() > ConvertFunc.getMaxMoney().doubleValue()) {

					GoldTaxHeadVO taxHeadVO = (GoldTaxHeadVO) taxVO.getParentVO().clone();
					taxHeadVO.setCode(taxHeadVO.getCode() + page + "" + mergeNum);
					taxHeadVO.setRowNum(mergeBodyList.size());
					
					GoldTaxVO mergeTaxVO = new GoldTaxVO();
					mergeTaxVO.setParentVO(taxHeadVO);
					mergeTaxVO.setChildrenVO(mergeBodyList.toArray(new GoldTaxBodyVO[0]));
					taxList.add(mergeTaxVO);
					
					mergeBodyList = new ArrayList<GoldTaxBodyVO>();
					mergeBodyList.add(taxBody);
					mergeNum++ ;
					sumMoney = ConvertFunc.change(taxBody.getMoney());
					
				} else {
					
					mergeBodyList.add(taxBody);
					mergeNum++ ;
					
					if(mergeNum == lessTaxBodyList.size()) {
						
						GoldTaxHeadVO taxHeadVO = (GoldTaxHeadVO) taxVO.getParentVO().clone();
						taxHeadVO.setCode(taxHeadVO.getCode() + page + "" + mergeNum);
						taxHeadVO.setRowNum(mergeBodyList.size());
						
						GoldTaxVO mergeTaxVO = new GoldTaxVO();
						mergeTaxVO.setParentVO(taxHeadVO);
						mergeTaxVO.setChildrenVO(mergeBodyList.toArray(new GoldTaxBodyVO[0]));
						taxList.add(mergeTaxVO);
						
						mergeBodyList = new ArrayList<GoldTaxBodyVO>();
						mergeBodyList.add(taxBody);
						mergeNum++ ;
						sumMoney = ConvertFunc.change(taxBody.getMoney());
						
					}
					
				}
				
				
				
			}
			
		}
		
		return taxList;
	}
	
	/** �����Ʊ�д��ڶ������ϣ�����VO���ϼ���� Start */
	@SuppressWarnings("rawtypes")
	protected final void splitMoreGoldTax(Map<String , GoldTaxBodyVO> bodyMap , List<GoldTaxVO> taxList , List<GoldTaxBodyVO> lessTaxBodyList , GoldTaxVO taxVO) throws Exception {
		
		Iterator iter = bodyMap.entrySet().iterator();
		while(iter.hasNext()) {
			
			Entry entry = (Entry) iter.next();
			
			GoldTaxBodyVO bodyVO = (GoldTaxBodyVO) entry.getValue();
			
			UFDouble moeny = ConvertFunc.change(bodyVO.getMoney());
			UFDouble price = ConvertFunc.change(bodyVO.getPrice());
			UFDouble number = ConvertFunc.change(bodyVO.getNumber());
			UFDouble sumMny = UFDouble.ZERO_DBL;
			UFDouble sumNumber = UFDouble.ZERO_DBL;
			UFDouble maxMny = ConvertFunc.getMaxMoney();
			
			int num = 1;
			while(moeny.sub(sumMny).doubleValue() > maxMny.doubleValue()) {
				
				int minNumber = maxMny.div(price).intValue();
				sumNumber = sumNumber.add(minNumber);
				UFDouble minMny = price.multiply(minNumber);
				sumMny = sumMny.add(minMny);
				
				GoldTaxBodyVO calcBody = (GoldTaxBodyVO) bodyVO.clone();
				calcBody.setNumber(new UFDouble(minNumber , 2));
				calcBody.setMoney(minMny);
				
				String flag = "";
				if(calcBody.getInvBaseId() != null)
					flag = calcBody.getInvBaseId().substring(calcBody.getInvBaseId().length() - 2, calcBody.getInvBaseId().length());
				
				GoldTaxVO caclTaxVO = new GoldTaxVO();
				GoldTaxHeadVO headVO = (GoldTaxHeadVO) taxVO.getParentVO().clone();
				headVO.setCode(headVO.getCode() + flag + num);
				headVO.setRowNum(1);
				caclTaxVO.setParentVO(headVO);
				caclTaxVO.setChildrenVO(new GoldTaxBodyVO[]{calcBody} );
				
				taxList.add(caclTaxVO);
				
				num++;
			}
			
			if(moeny.sub(sumMny).doubleValue() > 0) {
				
				UFDouble resNumber = number.sub(sumNumber);
				UFDouble resMny = moeny.sub(sumMny);
				
				GoldTaxBodyVO calcBody = (GoldTaxBodyVO) bodyVO.clone();
				calcBody.setNumber(resNumber);
				calcBody.setMoney(resMny);
				
				lessTaxBodyList.add(calcBody);
				
			}
			
		}
		
	}
	
	/**
	 * �������1170W�ķ�Ʊ���в��
	 * ֻ�����ܽ��Ĳ�֣�����������������ʱ����������Ч��
	 * 
	 * @author river
	 */
	protected final List<GoldTaxVO> splitGoldTax(GoldTaxBodyVO bodyVO , GoldTaxHeadVO taxHeadVO ) throws Exception {
		
		List<GoldTaxVO> bodyList = new ArrayList<GoldTaxVO>();
		
		UFDouble moeny = bodyVO.getMoney() == null ? new UFDouble(0 , 2) : bodyVO.getMoney();
		UFDouble price = bodyVO.getPrice() == null ? new UFDouble(0 , 2) : bodyVO.getPrice();
		UFDouble number = bodyVO.getNumber() == null ? new UFDouble(0 , 2) : bodyVO.getNumber();
		UFDouble sumMny = new UFDouble(0, 2);
		UFDouble sumNumber = new UFDouble(0 , 2);
		UFDouble maxMny = ConvertFunc.getMaxMoney();
		
		int num = 1;
		while(moeny.sub(sumMny).doubleValue() > maxMny.doubleValue()) {
			
			int minNumber = maxMny.div(price).intValue();
			sumNumber = sumNumber.add(minNumber);
			UFDouble minMny = price.multiply(minNumber);
			sumMny = sumMny.add(minMny);
			
			GoldTaxBodyVO calcBody = (GoldTaxBodyVO) bodyVO.clone();
			calcBody.setNumber(new UFDouble(minNumber , 2));
			calcBody.setMoney(minMny);
			
			GoldTaxVO caclTaxVO = new GoldTaxVO();
			GoldTaxHeadVO headVO = (GoldTaxHeadVO) taxHeadVO.clone();
			headVO.setCode(headVO.getCode() + num);
			headVO.setRowNum(1);
			caclTaxVO.setParentVO(headVO);
			caclTaxVO.setChildrenVO(new GoldTaxBodyVO[]{calcBody} );
			
			bodyList.add(caclTaxVO);
			
			num++;
		}
		
		if(moeny.sub(sumMny).doubleValue() > 0) {
			
			UFDouble resNumber = number.sub(sumNumber);
			UFDouble resMny = moeny.sub(sumMny);
			
			GoldTaxBodyVO calcBody = (GoldTaxBodyVO) bodyVO.clone();
			calcBody.setNumber(resNumber);
			calcBody.setMoney(resMny);
			
			GoldTaxVO caclTaxVO = new GoldTaxVO();
			GoldTaxHeadVO headVO = (GoldTaxHeadVO) taxHeadVO.clone();
			headVO.setCode(headVO.getCode() + num);
			headVO.setRowNum(1);
			caclTaxVO.setParentVO(headVO);
			caclTaxVO.setChildrenVO(new GoldTaxBodyVO[]{calcBody} );
			
			bodyList.add(caclTaxVO);
			
		}
		
		return bodyList;
	}

	/**
	 * �Ӹ������ļ��ж�ȡ��˰VO
	 * 
	 * @param filename �ļ���
	 * @return ��ȡ�Ľ�˰VO����
	 * @throws Exception
	 */
	public GoldTaxVO[] loadFromFile(String filename) {
		List<GoldTaxVO> taxVOs = new ArrayList<GoldTaxVO>();
		GoldTaxVO curTaxVO = null;
		int lineNum = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			int rownum = -1;
			String[] firstLintContent = null;
			for (String line = reader.readLine(); null != line; line = reader.readLine()) {
				lineNum++;
				if (isComment(line) || isEmptyWithTrim(line)) {
					continue;
				}
				String[] content = splitString(line);

				// �ļ���һ�У��ۺ�VO��Ϣ
				if (null == firstLintContent) {
					firstLintContent = content;
					continue;
				}

				if (null == curTaxVO) {
					curTaxVO = new GoldTaxVO();
					taxVOs.add(curTaxVO);

					rownum = getRownum(content);

					curTaxVO.setParentVO(getHeadVO(content));
					curTaxVO.setChildrenVO(new GoldTaxBodyVO[rownum]);
				} else {
					curTaxVO.getChildrenVO()[--rownum] = getBodyVO(content);
					if (rownum <= 0) {
						curTaxVO = null;
					}
				}
			}
		} catch (Exception e) {
			SCMEnv.error("��ȡ�ļ������쳣���� " + lineNum + " �У�" + e.getMessage(), e);
			throw new BusinessRuntimeException("��ȡ�ļ������쳣���� " + lineNum + " �У�" + e.getMessage(), e);
		}
		return taxVOs.toArray(new GoldTaxVO[taxVOs.size()]);
	}

	/**
	 * ���������ַ����Ƿ�����ע����
	 * 
	 * @param line
	 * @return
	 */
	private boolean isComment(String line) {
		return line.startsWith(COMMENT_PREFIX);
	}

	/**
	 * �����ַ����������õķָ������ַ�����������Ϊһ���ַ���
	 * 
	 * @param strs �����ӵ��ַ�������
	 * @return ���Ӻ���ַ���
	 * @see #splitString(String)
	 */
	private String joinString(String[] strs) {
		StringBuffer buf = new StringBuffer();
		for (String str : strs) {
			if (buf.length() > 0) {
				buf.append(getConf().getSplit());
			}
			// ���Ϊ������ո�
			buf.append(isEmpty(str) ? "" : str);
		}
		return buf.toString();
	}

	/**
	 * �ָ��ַ����������õķָ��������ַ����ָ�Ϊ�ַ�������
	 * 
	 * @param line Ҫ�ָ���ַ���
	 * @return �ָ����ַ�������
	 * @see #joinString(String[])
	 */
	private String[] splitString(String line) {
		return line.split(getConf().getSplit());
	}

	/**
	 * ���ۺ�VO����ת��Ϊ�ַ�����ʾ��һ���ֶ�һ���ַ������������ļ�ʱ��������˳��
	 * 
	 * @param taxVO Ҫת���Ľ�˰�ۺ�VO
	 * @return ����������ļ����ַ�������
	 */
	private String[] getAggregatedString(GoldTaxVO taxVO) {
		return new String[]{
				taxVO.getBillIdentifier(),			// ����ʶ
				taxVO.getBillName(),				// ������
				//begin ncm linsf _����ϣ����ʽ����
				/*taxVO.getSellCorpName()*/
				taxVO.getParentVO().getBillDate()+taxVO.getSellCorpName(),			// ���۹�˾����
				//end ncm linsf _����ϣ����ʽ����
		};
	}

	/**
	 * ����ͷ����ת��Ϊ�ַ�����ʾ��һ���ֶ�һ���ַ������������ļ�ʱ����������˳��
	 * 
	 * @param taxVO Ҫת���Ľ�˰��ͷVO���ڵĽ�˰VO
	 * @return ����������ļ����ַ�������
	 */
	private String[] getHeadString(GoldTaxVO taxVO) {
		GoldTaxHeadVO headVO = taxVO.getParentVO();
		StringBuffer acc = new StringBuffer("");
		
		if(headVO.getAccName()!=null)
			acc.append(headVO.getAccName());
		if(headVO.getAccount()!=null)
			acc.append(" ").append(headVO.getAccount());
		return new String[]{
				headVO.getCode(),					// ���ݺ�
				"" + taxVO.getChildrenVO().length,	// ��Ʒ����
				headVO.getCustomerName(),			// ��������
				headVO.getTaxPayerId(),				// ����˰��
				headVO.getSaleAddrPhone(),			// ������ַ�绰
				acc.toString(),// ���������ʺ�
				headVO.getMemo(),					// ��ע
				headVO.getChecker(),				// ������
				headVO.getPayee()
				//begin linsf 201009151650445416����ϣ��_����˰��ʽ����
				/*,					// �տ���
				headVO.getRowInvName(),				// �嵥����Ʒ����
				toStr(headVO.getBillDate()),		// ��������
				headVO.getSellAccount(),			// ��������
				 */		
				//end linsf 201009151650445416����ϣ��_����˰��ʽ����
				};
	}

	private GoldTaxHeadVO getHeadVO(String[] content) {
		// ��ͷ��Ŀ�ĸ���
		final int HEAD_CONT_LEN = 13;
		if (content.length != HEAD_CONT_LEN) {
			SCMEnv.error("��ͷ��Ŀ������");
			throw new BusinessRuntimeException("��ͷ��Ŀ�����ԡ�" + content.length + "����Ӧ���� " + HEAD_CONT_LEN + " ��");
		}

		GoldTaxHeadVO headVO = new GoldTaxHeadVO();
		int pos = 0;
		headVO.setCode(content[pos++]);				// ���ݺ�
		pos++;										// ��Ʒ����
		headVO.setCustomerName(content[pos++]);		// ��������
		headVO.setTaxPayerId(content[pos++]);		// ����˰��
		headVO.setSaleAddrPhone(content[pos++]);	// ������ַ�绰
		headVO.setAccount(content[pos++]);			// ���������ʺ�
		headVO.setMemo(content[pos++]);				// ��ע
		headVO.setChecker(content[pos++]);			// ������
		headVO.setPayee(content[pos++]);			// �տ���
		headVO.setRowInvName(content[pos++]);		// �嵥����Ʒ����
		headVO.setBillDate(toUFDate(content[pos++]));// ��������
		headVO.setSellAccount(content[pos++]);		// ��������
		headVO.setTaxBillNo(content[pos++]);		// ��˰Ʊ��
		return headVO;
	}

	/**
	 * �ӱ�ͷ���ַ��������еõ���Ʒ����
	 * 
	 * @param headContent ��ͷ�����ַ�������
	 * @return ��Ʒ����
	 */
	private int getRownum(String[] headContent) {
		try {
			return Integer.parseInt(headContent[1]);
		} catch (NumberFormatException e) {
			throw new BusinessRuntimeException("��ͷ�� 2 ����Ŀ�Ǳ������������������֣������ǡ�" + headContent[1] + "��");
		}
	}

	private String toStr(UFDate date) {
		if (null == date) {
			return null;
		} else {
			return dateFormat.format(date.toDate());
		}
	}

	private UFDate toUFDate(String str) {
		if (isEmpty(str)) {
			return null;
		}
		try {
			Date date = dateFormat.parse(str);
			return new UFDate(date);
		} catch (ParseException e) {
			SCMEnv.error("���ڸ�ʽ���󣬲����ϸ�ʽ��yyyyMMdd", e);
			throw new BusinessRuntimeException("���ڸ�ʽ���󣬲����ϸ�ʽ��yyyyMMdd", e);
		}
	}

	/**
	 * ����������ת��Ϊ�ַ�����ʾ��һ���ֶ�һ���ַ������������ļ�ʱ����������˳��
	 * 
	 * @param bodyVO Ҫת���Ľ�˰����VO
	 * @return ����������ļ����ַ�������
	 */
	private String[] getBodyString(GoldTaxBodyVO bodyVO) {
		UFDouble taxrate = bodyVO.getTaxRate()==null?new UFDouble(0,2):
			new UFDouble(bodyVO.getTaxRate().multiply(0.01).doubleValue(),2);
		/*********** EastHope Editor shaoyt 2011-06-15 Start ***********/
		//��������Ʊ��˰�ϼ� ��  ��˰��˰�ϼ� ���0.01 
//		UFDouble noTaxMny = bodyVO.getNoTaxMny()==null?new UFDouble(0,2)
//				:bodyVO.getNoTaxMny();
		UFDouble noTaxMny = bodyVO.getMoney()==null?new UFDouble(0,2):bodyVO.getMoney();
		noTaxMny = noTaxMny.div(taxrate.add(1)).setScale(2, UFDouble.ROUND_HALF_UP);
		/*********** EastHope Editor shaoyt 2011-06-15 End ***********/
		
		return new String[]{
				// ��������
				getConf().isMergeInventory() ? bodyVO.getInvName() : bodyVO.getInvClassName(),
				bodyVO.getQuoteUnitName(),				// ������λ
				bodyVO.getInvSpec(),					// ���
				getString(bodyVO.getNumber()),			// ����
				/*getString(bodyVO.getMoney())*/
				getString(noTaxMny),			// ���
				getString(taxrate),			// ˰��
				"4001",	// ��Ʒ˰Ŀ���̶�Ϊ4001
				//begin ncm linsf_����ϣ��_����˰��ʽ����				
				"",		// �ۿ۽��				
				/*" ",							// ˰�Ϊһ���ո�
				" ",									// �ۿ�˰�Ϊһ���ո�
				" ",									// �ۿ��ʣ�Ϊһ���ո�
				getString(bodyVO.getPrice()),			// ����
				"1",									// �۸�ʽ���̶�Ϊ 1
				
*/				//begin ncm linsf 201011031105124159����ϣ��_˰��=��˰���*˰��
				/*********** EastHope Editor shaoyt 2011-03-31 Start ***********/
				 // �������뱣����λС��
				getString(noTaxMny.multiply(taxrate).setScale(2, UFDouble.ROUND_HALF_UP)),// ˰�Ϊһ���ո�
				/*********** EastHope Editor shaoyt 2011-03-31 End ***********/
				//end ncm linsf 201011031105124159����ϣ��_˰��=��˰���*˰��
				"",									// �ۿ�˰�Ϊһ���ո�
				"",									// �ۿ��ʣ�Ϊһ���ո�
				"",			// ����
				"1",									// �۸�ʽ���̶�Ϊ 1
				//end ncm linsf_����ϣ��_����˰��ʽ����
		};
	}	

	private GoldTaxBodyVO getBodyVO(String[] content) {
		// ������Ŀ�ĸ���
		final int BODY_CONT_LEN = 13;
		if (content.length != BODY_CONT_LEN) {
			SCMEnv.error("������Ŀ������");
			throw new BusinessRuntimeException("������Ŀ�����ԡ�" + content.length + "����Ӧ���� " + BODY_CONT_LEN + " ��");
		}

		GoldTaxBodyVO bodyVO = new GoldTaxBodyVO();
		int pos = 0;
		// ��������
		if (getConf().isMergeInventory()) {
			bodyVO.setInvName(content[pos++]);
		} else {
			 bodyVO.setInvClassName(content[pos++]);
		}
		bodyVO.setQuoteUnitId(content[pos++]);				// ������λ
		bodyVO.setInvSpec(content[pos++]);					// ���
		bodyVO.setNumber(toUFDouble(content[pos++]));		// ����
		bodyVO.setMoney(toUFDouble(content[pos++]));		// ���
		bodyVO.setTaxRate(toUFDouble(content[pos++]));		// ˰��
		bodyVO.setTaxItems(content[pos++]);					// ��Ʒ˰Ŀ
		bodyVO.setDiscountMny(toUFDouble(content[pos++]));	// �ۿ۽��
		bodyVO.setTaxMny(toUFDouble(content[pos++]));		// ˰��
		bodyVO.setDiscountTaxMny(toUFDouble(content[pos++]));// �ۿ�˰��
		bodyVO.setDiscountRate(toUFDouble(content[pos++]));	// �ۿ���
		bodyVO.setPrice(toUFDouble(content[pos++]));		// ����
		bodyVO.setPriceMode(toUFDouble(content[pos++]));	// �۸�ʽ
		return bodyVO;
	}

	private UFDouble toUFDouble(String str) {
		if (isEmpty(str)) {
			return null;
		} else {
			return new UFDouble(str);
		}
	}

	private static String getString(Object o) {
		return null == o ? null : o.toString();
	}
}
