package com.ufsoft.iufo.fmtplugin.measure;

import com.ufsoft.iufo.fmtplugin.formatcore.CacheProxy;

import nc.pub.iufo.cache.MeasureCache;

import nc.util.iufo.pub.IDMaker;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.iufo.measure.MeasureVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.ValueObject;

/**
 * 带位置信息的MeasureVO， 只允许在当前指标插件Package包内使用
 * 原类为MeasureTableVO，创建该类的目的是保留MeasureTableVO中的部分代码逻辑
 */

class MeasurePosVO extends ValueObject {

    /**
     * 指标主键
     */
    private String m_strMeasurePK;

    /**
     * 指标信息
     */
    private transient MeasureVO m_oMeasureVO;

    /**
     * 指标提取标志
     *  
     */
    private Boolean m_flag = Boolean.TRUE;

    /**
     * 指标引用标志
     *  
     */
    private Boolean m_isRefedMeasure = Boolean.FALSE;

    /**
     * 实际区域位置，对于主表，该值和getPos()的值相同
     */
    private transient String m_actPose;

    /**
     * 引用时，需要保存原有的指标，以便在这次提取过程中取消引用时，恢复原有的指标 其初始值是空，只有当引用的时候才会赋值 ***** 发版时转变为瞬时
     * *************************
     */
    private transient String m_originalMeasurePk = null;

    private transient String m_originalMeasureName = null;

//    private String pos; // 位置

    /**
     * 指标状态，表示指标是初始（或引用），新建，修改，删除，为在保存指标时按照新建，删除，修改分类提交做标记 ***** 发版时转变为瞬时
     * *************************
     */
    private transient int m_iState = 0;

    /**
     * 指标状态枚举 指标状态集： original <-->update, original <-->delete, original <-->
     * tokey, create <-->tokey. 为了区分tokey状态的原始状态，则对tokey
     * 状态不做特定的记录，即tokey状态有两个值，
     *  
     */
    public static final int MEASURE_TABLE_S_ORIGINAL = 0;

    public static final int MEASURE_TABLE_S_UPDATE = 1;

    public static final int MEASURE_TABLE_S_DELETE = 2;

    public static final int MEASURE_TABLE_S_CREATE = 3;

    public static final int MEAS_S_DELTA_TO_KEY = 4;

    private static final long serialVersionUID = 1421746759512280012L;

    /**
     * 此处插入方法描述。 创建日期：(2002-5-14 12:53:50)
     */
    public MeasurePosVO() {
    }

    //修改指标PK设置,, liuyy 2005-2-5

    //取消引用指标,恢复原有指标,并重新设置指标引用标志
    //如果是关键字的取消引用，则需要传入
    public void cancelRefMeasure(String strCurRepPk, String strMeasPackPK)
            throws Exception {
        //如果指标引用标志为false,则直接返回
        if (!m_isRefedMeasure.booleanValue()) {
            return;
        }

        MeasureCache measureCache = CacheProxy.getSingleton()
                .getMeasureCache();

        //如果m_originalMeasurePk==null,则表明原指标是新建的指标,使用IDMaker生成一个新的PK
        if (m_originalMeasurePk == null) {
            m_originalMeasurePk = measureCache.getNewMeasurePK(strMeasPackPK);
        }
        if (m_originalMeasureName == null) {
            m_originalMeasureName = IDMaker
                    .makeID(20);
        }

        //指标引用的取消
        if (m_flag.booleanValue()) {
            m_oMeasureVO = measureCache.getMeasure(m_originalMeasurePk);
            if (m_oMeasureVO == null) {
                m_oMeasureVO = new MeasureVO();
                m_oMeasureVO.setCode(m_originalMeasurePk);
                m_oMeasureVO.setType(MeasureVO.TYPE_NUMBER);
                m_oMeasureVO.setAttribute(MeasureVO.ATTR_COIN_YUAN);
                m_oMeasureVO.setName(m_originalMeasureName);
                m_oMeasureVO.setReportPK(strCurRepPk);
            }
            setMeasureVO(m_oMeasureVO);
            m_originalMeasurePk = null;
            m_originalMeasureName = null;
            m_isRefedMeasure = Boolean.FALSE;
            //          关键字取消引用
        } else {
            KeyVO orikey = CacheProxy.getSingleton().getKeywordCache()
                    .getByPK(m_originalMeasurePk);
            if (orikey == null) {
                m_oMeasureVO = new MeasureVO();
                m_oMeasureVO.setCode(m_originalMeasurePk);
                m_oMeasureVO.setType(MeasureVO.TYPE_NUMBER);
                m_oMeasureVO.setName(m_originalMeasureName);
                m_oMeasureVO.setReportPK(strCurRepPk);
            } else {
                MeasureVO orivo = measureCache.getMeasure(m_originalMeasurePk);
                if (orivo != null) {
                    m_oMeasureVO = orivo.copy();
                    m_oMeasureVO.setCode(measureCache
                            .getNewMeasurePK(strMeasPackPK));
                } else {
                    int type = orikey.getType();
                    if (type == KeyVO.TYPE_TIME)
                        type = MeasureVO.TYPE_CHAR;
                    m_oMeasureVO.setType(type);
                    m_oMeasureVO.setCode(measureCache
                            .getNewMeasurePK(strMeasPackPK));
                    m_oMeasureVO.setName(IDMaker.makeID(20));
                    m_oMeasureVO.setReportPK(strCurRepPk);
                }
            }
            m_originalMeasurePk = null;
            m_originalMeasureName = null;
            m_isRefedMeasure = Boolean.FALSE;
        }

    }

    /**
     * 对象复制
     */
    public Object clone() {
        MeasurePosVO vo = new MeasurePosVO();
        vo.m_actPose = this.m_actPose;
        vo.m_flag = this.m_flag;
        vo.m_isRefedMeasure = this.m_isRefedMeasure;
        vo.m_iState = this.m_iState;
        if (this.m_oMeasureVO != null)
            vo.setMeasureVO(this.m_oMeasureVO.copy());
        vo.m_originalMeasureName = this.m_originalMeasureName;
        vo.m_originalMeasurePk = this.m_originalMeasurePk;
        vo.m_strMeasurePK = this.m_strMeasurePK;
//        vo.setPos(this.getPos());
        vo.setDirty(this.isDirty());
        return vo;
    }

    ///**
    // * 根据当前的MeasureVO构造UfoKeyWord,指标提取中从指标提取成关键字用
    // * 创建日期：(2003-9-19 17:30:31)
    // * @return nc.vo.iufo.keydef.KeyVO
    // */
    //public UfoKeyWord generateKeyVO(String repId) {
    //
    //	//生成新的关键字
    //	KeyVO keyvo = new KeyVO();
    //	keyvo.setKeywordPK(com.ufsoft.iuforeport.reporttool.toolkit.IDMaker.makeID(20));
    //	keyvo.setName( m_oMeasureVO.getName() );
    //	keyvo.setNote( m_oMeasureVO.getNote() );
    //	//为关键字去掉数值类型作相应的修改
    //	int type = m_oMeasureVO.getType();
    //    int len = m_oMeasureVO.getLen();
    //	if( type == MeasureVO.TYPE_NUMBER )
    //    {
    //        type = MeasureVO.TYPE_CHAR;
    //        len = 64;
    //    }
    //	keyvo.setType( type );
    //    keyvo.setLen(len);
    //	keyvo.setIsPrivate( true );
    //	keyvo.setRepIdOfPrivate( repId );
    //	keyvo.setIsBuiltIn(new nc.vo.pub.lang.UFBoolean(false));
    //
    //	keyvo.setIsStop( new nc.vo.pub.lang.UFBoolean(false));
    //	UfoKeyWord ufokey = new UfoKeyWord();
    //	ufokey.setCell(new UfoCell(getPos()));
    //	ufokey.setKeyVO(keyvo);
    //	setFlag( Boolean.FALSE );
    //	setState(MeasureTableVO.MEAS_S_DELTA_TO_KEY);
    //	return ufokey;
    //}
    /**
     * 此处插入方法描述。 创建日期：(2003-9-16 18:08:44)
     * 
     * @return java.lang.String
     */
    public java.lang.String getActPos() {
        return m_actPose;
    }

    /**
     * 返回数值对象的显示名称。
     * 
     * 创建日期：(2001-5-14)
     * 
     * @return java.lang.String 返回数值对象的显示名称。
     */
    public String getEntityName() {
        return "User";
    }

    /**
     * 此处插入方法描述。 创建日期：(2003-8-13 11:07:11)
     * 
     * @return java.lang.Boolean
     */
    public java.lang.Boolean getFlag() {
        return m_flag == null ? Boolean.FALSE : m_flag;
    }

    /**
     * 此处插入方法描述。 创建日期：(2003-8-13 11:07:11)
     * 
     * @return java.lang.Boolean
     */
    public java.lang.Boolean getKeyFlag() {
        if (m_flag == null)
            return Boolean.FALSE;
        if (m_flag.booleanValue())
            return Boolean.FALSE;
        else
            return Boolean.TRUE;
    }

    /**
     * 此处插入方法描述。 创建日期：(2003-8-12 10:16:19)
     * 
     * @return java.lang.String
     */
    public java.lang.String getMeasurePK() {
        return m_strMeasurePK;
    }

    /**
     * 此处插入方法描述。 创建日期：(2003-7-4 11:23:05)
     * 
     * @return nc.vo.iufo.measure.MeasureVO
     */
    public nc.vo.iufo.measure.MeasureVO getMeasureVO() {
        return m_oMeasureVO;
    }

    /**
     * 此处插入方法描述。 创建日期：(2003-12-9 15:33:29)
     * 
     * @author：岳益民
     * @return java.lang.Boolean
     */
    public java.lang.Boolean getRefedMeasure() {
        return m_isRefedMeasure;
    }

    /**
     * 得到指标状态 创建日期：(2003-8-8 11:28:59)
     * 
     * @return int
     */
    public int getState() {
        return m_iState;
    }

    /**
     * 此处插入方法描述。 创建日期：(2003-8-13 11:37:38)
     * 
     * @return boolean
     */
    public boolean isRefMeasure() {
        return m_isRefedMeasure.booleanValue();
    }

    /**
     * 此处插入方法描述。 创建日期：(2003-9-16 18:08:44)
     * 
     * @param newM_virPose
     *            java.lang.String
     */
    public void setActPos(java.lang.String newActPose) {
        m_actPose = newActPose;
    }

    /**
     * 此处插入方法描述。 创建日期：(2003-8-13 11:07:11)
     * 
     * @param newM_flag
     *            java.lang.Boolean
     */
    public void setFlag(java.lang.Boolean newflag) {
        if (newflag == null)
            return;
        else {
            //如果当前不是关键字也不是指标,newflag==Boolean.TRUE时,才进行设置
            if (m_flag == null) {
                if (newflag.booleanValue())
                    m_flag = Boolean.TRUE;
            } else if (m_flag.booleanValue())//当前是指标
            {
                //取消指标设置
                if (!newflag.booleanValue())
                    m_flag = null;
            } else//当前是关键字
            {
                //设置为指标
                if (newflag.booleanValue()) {
                    m_flag = Boolean.TRUE;
                }
            }
        }
        if (!newflag.booleanValue()) {
            if (this.m_iState == MeasurePosVO.MEASURE_TABLE_S_ORIGINAL
                    || m_iState == MeasurePosVO.MEASURE_TABLE_S_UPDATE) {
                setState(MeasurePosVO.MEASURE_TABLE_S_DELETE);
            }
        } else {
            if (m_iState == MeasurePosVO.MEASURE_TABLE_S_DELETE) {
                setState(MeasurePosVO.MEASURE_TABLE_S_UPDATE);
            }
        }
    }

    /**
     * 此处插入方法描述。 创建日期：(2003-8-13 11:07:11)
     * 
     * @param newM_flag
     *            java.lang.Boolean
     */
    public void setKeyFlag(java.lang.Boolean newKeyflag) {

        if (newKeyflag == null)
            return;
        else {
            //如果当前不是关键字也不是指标,则只有当newKeyflag==Boolean.TRUE时,才进行设置
            if (m_flag == null) {
                if (newKeyflag.booleanValue())
                    m_flag = Boolean.FALSE;
            } else if (m_flag.booleanValue())//当前是指标
            {
                //设为关键字
                if (newKeyflag.booleanValue())
                    m_flag = Boolean.FALSE;
            } else//当前是关键字
            {
                if (!newKeyflag.booleanValue()) {
                    m_flag = null;
                }
            }
        }
    }

    /**
     * 此处插入方法描述。 创建日期：(2003-8-12 10:16:19)
     * 
     * @param newMeasurePK
     *            java.lang.String
     */
    public void setMeasurePK(java.lang.String newMeasurePK) {
        m_strMeasurePK = newMeasurePK;
    }

    /**
     * 此处插入方法描述。 创建日期：(2003-7-4 11:23:05)
     * 
     * @param newMeasureVO
     *            nc.vo.iufo.measure.MeasureVO
     */
    public void setMeasureVO(nc.vo.iufo.measure.MeasureVO newMeasureVO) {
        m_oMeasureVO = newMeasureVO;
        if (newMeasureVO != null)
            setMeasurePK(newMeasureVO.getCode());
    }

    /**
     * 对于动态区域提取关键字的参照时使用。按照关键字属性设置MeasureVO，并缓存MeasureVO中的pk,name
     * 创建日期：(2003-9-19 17:20:53)
     * 
     * @param key
     *            nc.vo.iufo.keydef.KeyVO
     */
    //public UfoKeyWord setMeasureVOAsRefKeyVO(DynamicAreaVO dynAreaVO, KeyVO
    // keyvo,String repId)
    //{
    //	if( keyvo == null || repId == null )
    //		return null;
    //	//新建MeasureVO,参照关键字的属性进行设置
    //	MeasureVO keyMeasure = new MeasureVO();
    //	keyMeasure.setName( keyvo.getName());
    //	keyMeasure.setNote( keyvo.getNote());
    //	keyMeasure.setReportPK(repId);
    //	//设置类型，如果是时间类型，则按字符处理
    //	int type = keyvo.getType();
    //	if(type == KeyVO.TYPE_TIME)
    //	{
    //		type = MeasureVO.TYPE_CHAR;
    //	}
    //	keyMeasure.setType(type);
    //	
    //// liuyy. 2005-1-13
    //// keyMeasure.setCode( keyvo.getKeywordPK());
    //	String strMeasurePK = null;
    //	 String strMeasPackPK = dynAreaVO.getMeasPackPK();
    //	 try {
    //	     MeasureCache mCache = null;
    //		 mCache = UICacheManager.getSingleton().getMeasureCache();
    //        strMeasurePK = mCache.getNewMeasurePK(strMeasPackPK);
    //    } catch (Exception e) {
    //        AppDebug.debug(e);
    //        strMeasurePK = strMeasPackPK +
    // IDMaker.makeID(MeasurePackVO.MEASURE_ID_LENGTH);
    //    }
    //    keyMeasure.setCode(strMeasurePK);
    //		
    //	keyMeasure.setLen( keyvo.getLen());
    //	//如果该关键字在缓存中不存在，说明是私有关键字，或者存在，但是本身就是私有关键字，则由于私有关键字只属于本报表，则不被设置为引用
    //	//只有公有关键字才被设为引用
    //	KeyVO cachekey =
    // UICacheManager.getSingleton().getKeywordCache().getByPK(keyvo.getKeywordPK());
    //	if(cachekey == null
    //		|| cachekey.isPrivate() )
    //	{
    //		m_isRefedMeasure = Boolean.FALSE;
    //	}else
    //	{
    //		m_isRefedMeasure = Boolean.TRUE;
    //	}
    //	setKeyFlag(Boolean.TRUE);
    //	if ( m_originalMeasurePk == null&&m_oMeasureVO!= null )
    //	{
    //		m_originalMeasurePk = m_oMeasureVO.getCode();
    //		m_originalMeasureName = m_oMeasureVO.getName();
    //	}
    //	this.m_oMeasureVO = keyMeasure;
    //	this.m_strMeasurePK = keyMeasure.getCode();
    //	//返回构造的UfoKeyWord
    //	UfoKeyWord ufokey = new UfoKeyWord();
    //	ufokey.setCell(new UfoCell(getPos()));
    //	ufokey.setKeyVO(keyvo);
    //	setState(MEASURE_TABLE_S_CREATE);
    //	setState(MEAS_S_DELTA_TO_KEY);
    //	return ufokey;
    //}
    /**
     * 此处插入方法描述。 创建日期：(2003-12-9 15:33:29)
     * 
     * @author：岳益民
     * @param newRefedMeasure
     *            java.lang.Boolean
     */
    public void setRefedMeasure(java.lang.Boolean newRefedMeasure) {
        m_isRefedMeasure = newRefedMeasure;
    }

    //设置引用的指标，并设置引用标志和缓存原指标pk
    public void setRefMeasure(MeasureVO refvo) {
        m_isRefedMeasure = Boolean.TRUE;
        m_flag = Boolean.TRUE;
        if (m_originalMeasurePk == null) {
            m_originalMeasurePk = m_oMeasureVO.getCode();
            m_originalMeasureName = m_oMeasureVO.getName();
        }
        setMeasureVO(refvo);
    }

    /**
     * 设置指标状态
     * 创建日期：(2003-8-8 11:28:59)
     * @param newM_iState int
     */
    public void setState(int newState) {
        //如果newState是基本状态，则可以直接设置
        if (newState == MEASURE_TABLE_S_ORIGINAL
                || newState == MEASURE_TABLE_S_UPDATE
                || newState == MEASURE_TABLE_S_DELETE
                || newState == MEASURE_TABLE_S_CREATE) {
            this.m_iState = newState;
        } else if (newState == this.MEAS_S_DELTA_TO_KEY) {
            //如果是要转到tokey状态，则只有m_iState状态为Original和Create状态时才可以
            if (m_iState == MEASURE_TABLE_S_CREATE
                    || m_iState == MEASURE_TABLE_S_ORIGINAL) {
                this.m_iState += newState;
            }
        }
        //如果newState值为其他数值，则不作处理
    }

    /**
     * 指标状态从tokey状态恢复到tokey前的状态
     * 创建日期：(2003-11-27 15:04:47)
     * @author：王海涛
     */
    public void stateRollBackFromToKey() {
        int newState = m_iState - MEAS_S_DELTA_TO_KEY;
        //只有tokey状态的值-MEAS_S_DELTA_TO_KEY后才会 >= 0
        if (newState >= 0)
            m_iState -= MEAS_S_DELTA_TO_KEY;
    }

    /**
     * 属性约束检测。
     * 创建日期：(2000-9-28 14:17:28)
     * @exception nc.ui.iufo.table.data.AttributeInvalidException 异常说明。
     */
    public void validate() throws ValidationException {
        /*if (keyName.length() > 32
         || columnName.length() > 16
         || keyLen <= 0
         || keyLen > 128
         || codeTableName.length() > 64
         || !(keyType >= 0 && keyType <= 7 && keyType != 4 && keyType != 6))
         {
         throw new ValidationException("属性设置错误");
         }*/
    }

	public String getOriginalMeasureName() {
		return m_originalMeasureName;
	}

	public void setOriginalMeasureName(String measureName) {
		m_originalMeasureName = measureName;
	}

    /**
     * @return Returns the pos.
     */
//    public String getPos() {
//        return pos;
//    }

    /**
     * @param pos The pos to set.
     */
//    public void setPos(String pos) {
//        this.pos = pos;
//    }
}
