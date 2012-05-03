package nc.vo.hi.hi_301.validator;

/**
 * 表检验接口。
 * 创建日期：(2004-5-14 16:29:20)
 * @author：Administrator
 */
public interface IValidator {

	//有效性检验器注册表
	public static final Object[][] validators={
		{"bd_psndoc",new PsndocValidator()},
		{"bd_psnbasdoc",new AccpsndocValidator()},
		{"hi_psndoc_ctrt",new CtrtValidator()},
		{"hi_psndoc_deptchg", new DeptchgValidator()},
		{"hi_psndoc_enc",new EncValidator()},
		{"hi_psndoc_family",new FamilyValidator()},
		{"hi_psndoc_orgpsn",new OrgpsnValidator()},
		{"hi_psndoc_part",new PartValidator()},
		{"hi_psndoc_psnchg",new PsnchgValidator()},
		{"hi_psndoc_pun",new PunValidator()},
		{"hi_psndoc_req",new ReqValidator()},
		{"hi_psndoc_ass",new AssValidator()},
		{"hi_psndoc_edu",new EduValidator()},
		{"hi_psndoc_tryout",new TryoutValidator()},
		{"hi_psndoc_spetech",new SpetechValidator()},
		{"hi_psndoc_speitem",new SpeitemValidator()},
		{"hi_psndoc_abroad",new AbroadValidator()},
		{"hi_psndoc_keypsn",new KeyPsnValidator()}
	};
	public void validate(nc.vo.pub.CircularlyAccessibleValueObject[]records)throws Exception;
	public void validate(nc.vo.pub.CircularlyAccessibleValueObject record)throws Exception;
	public void validate(nc.vo.pub.ExtendedAggregatedValueObject person)throws Exception;
}
