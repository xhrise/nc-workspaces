package com.ufsoft.iufo.fmtplugin.measure;

import com.ufsoft.iufo.fmtplugin.formatcore.CacheProxy;

import nc.pub.iufo.cache.MeasureCache;

import nc.util.iufo.pub.IDMaker;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.iufo.measure.MeasureVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.ValueObject;

/**
 * ��λ����Ϣ��MeasureVO�� ֻ�����ڵ�ǰָ����Package����ʹ��
 * ԭ��ΪMeasureTableVO�����������Ŀ���Ǳ���MeasureTableVO�еĲ��ִ����߼�
 */

class MeasurePosVO extends ValueObject {

    /**
     * ָ������
     */
    private String m_strMeasurePK;

    /**
     * ָ����Ϣ
     */
    private transient MeasureVO m_oMeasureVO;

    /**
     * ָ����ȡ��־
     *  
     */
    private Boolean m_flag = Boolean.TRUE;

    /**
     * ָ�����ñ�־
     *  
     */
    private Boolean m_isRefedMeasure = Boolean.FALSE;

    /**
     * ʵ������λ�ã�����������ֵ��getPos()��ֵ��ͬ
     */
    private transient String m_actPose;

    /**
     * ����ʱ����Ҫ����ԭ�е�ָ�꣬�Ա��������ȡ������ȡ������ʱ���ָ�ԭ�е�ָ�� ���ʼֵ�ǿգ�ֻ�е����õ�ʱ��Żḳֵ ***** ����ʱת��Ϊ˲ʱ
     * *************************
     */
    private transient String m_originalMeasurePk = null;

    private transient String m_originalMeasureName = null;

//    private String pos; // λ��

    /**
     * ָ��״̬����ʾָ���ǳ�ʼ�������ã����½����޸ģ�ɾ����Ϊ�ڱ���ָ��ʱ�����½���ɾ�����޸ķ����ύ����� ***** ����ʱת��Ϊ˲ʱ
     * *************************
     */
    private transient int m_iState = 0;

    /**
     * ָ��״̬ö�� ָ��״̬���� original <-->update, original <-->delete, original <-->
     * tokey, create <-->tokey. Ϊ������tokey״̬��ԭʼ״̬�����tokey
     * ״̬�����ض��ļ�¼����tokey״̬������ֵ��
     *  
     */
    public static final int MEASURE_TABLE_S_ORIGINAL = 0;

    public static final int MEASURE_TABLE_S_UPDATE = 1;

    public static final int MEASURE_TABLE_S_DELETE = 2;

    public static final int MEASURE_TABLE_S_CREATE = 3;

    public static final int MEAS_S_DELTA_TO_KEY = 4;

    private static final long serialVersionUID = 1421746759512280012L;

    /**
     * �˴����뷽�������� �������ڣ�(2002-5-14 12:53:50)
     */
    public MeasurePosVO() {
    }

    //�޸�ָ��PK����,, liuyy 2005-2-5

    //ȡ������ָ��,�ָ�ԭ��ָ��,����������ָ�����ñ�־
    //����ǹؼ��ֵ�ȡ�����ã�����Ҫ����
    public void cancelRefMeasure(String strCurRepPk, String strMeasPackPK)
            throws Exception {
        //���ָ�����ñ�־Ϊfalse,��ֱ�ӷ���
        if (!m_isRefedMeasure.booleanValue()) {
            return;
        }

        MeasureCache measureCache = CacheProxy.getSingleton()
                .getMeasureCache();

        //���m_originalMeasurePk==null,�����ԭָ�����½���ָ��,ʹ��IDMaker����һ���µ�PK
        if (m_originalMeasurePk == null) {
            m_originalMeasurePk = measureCache.getNewMeasurePK(strMeasPackPK);
        }
        if (m_originalMeasureName == null) {
            m_originalMeasureName = IDMaker
                    .makeID(20);
        }

        //ָ�����õ�ȡ��
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
            //          �ؼ���ȡ������
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
     * ������
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
    // * ���ݵ�ǰ��MeasureVO����UfoKeyWord,ָ����ȡ�д�ָ����ȡ�ɹؼ�����
    // * �������ڣ�(2003-9-19 17:30:31)
    // * @return nc.vo.iufo.keydef.KeyVO
    // */
    //public UfoKeyWord generateKeyVO(String repId) {
    //
    //	//�����µĹؼ���
    //	KeyVO keyvo = new KeyVO();
    //	keyvo.setKeywordPK(com.ufsoft.iuforeport.reporttool.toolkit.IDMaker.makeID(20));
    //	keyvo.setName( m_oMeasureVO.getName() );
    //	keyvo.setNote( m_oMeasureVO.getNote() );
    //	//Ϊ�ؼ���ȥ����ֵ��������Ӧ���޸�
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
     * �˴����뷽�������� �������ڣ�(2003-9-16 18:08:44)
     * 
     * @return java.lang.String
     */
    public java.lang.String getActPos() {
        return m_actPose;
    }

    /**
     * ������ֵ�������ʾ���ơ�
     * 
     * �������ڣ�(2001-5-14)
     * 
     * @return java.lang.String ������ֵ�������ʾ���ơ�
     */
    public String getEntityName() {
        return "User";
    }

    /**
     * �˴����뷽�������� �������ڣ�(2003-8-13 11:07:11)
     * 
     * @return java.lang.Boolean
     */
    public java.lang.Boolean getFlag() {
        return m_flag == null ? Boolean.FALSE : m_flag;
    }

    /**
     * �˴����뷽�������� �������ڣ�(2003-8-13 11:07:11)
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
     * �˴����뷽�������� �������ڣ�(2003-8-12 10:16:19)
     * 
     * @return java.lang.String
     */
    public java.lang.String getMeasurePK() {
        return m_strMeasurePK;
    }

    /**
     * �˴����뷽�������� �������ڣ�(2003-7-4 11:23:05)
     * 
     * @return nc.vo.iufo.measure.MeasureVO
     */
    public nc.vo.iufo.measure.MeasureVO getMeasureVO() {
        return m_oMeasureVO;
    }

    /**
     * �˴����뷽�������� �������ڣ�(2003-12-9 15:33:29)
     * 
     * @author��������
     * @return java.lang.Boolean
     */
    public java.lang.Boolean getRefedMeasure() {
        return m_isRefedMeasure;
    }

    /**
     * �õ�ָ��״̬ �������ڣ�(2003-8-8 11:28:59)
     * 
     * @return int
     */
    public int getState() {
        return m_iState;
    }

    /**
     * �˴����뷽�������� �������ڣ�(2003-8-13 11:37:38)
     * 
     * @return boolean
     */
    public boolean isRefMeasure() {
        return m_isRefedMeasure.booleanValue();
    }

    /**
     * �˴����뷽�������� �������ڣ�(2003-9-16 18:08:44)
     * 
     * @param newM_virPose
     *            java.lang.String
     */
    public void setActPos(java.lang.String newActPose) {
        m_actPose = newActPose;
    }

    /**
     * �˴����뷽�������� �������ڣ�(2003-8-13 11:07:11)
     * 
     * @param newM_flag
     *            java.lang.Boolean
     */
    public void setFlag(java.lang.Boolean newflag) {
        if (newflag == null)
            return;
        else {
            //�����ǰ���ǹؼ���Ҳ����ָ��,newflag==Boolean.TRUEʱ,�Ž�������
            if (m_flag == null) {
                if (newflag.booleanValue())
                    m_flag = Boolean.TRUE;
            } else if (m_flag.booleanValue())//��ǰ��ָ��
            {
                //ȡ��ָ������
                if (!newflag.booleanValue())
                    m_flag = null;
            } else//��ǰ�ǹؼ���
            {
                //����Ϊָ��
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
     * �˴����뷽�������� �������ڣ�(2003-8-13 11:07:11)
     * 
     * @param newM_flag
     *            java.lang.Boolean
     */
    public void setKeyFlag(java.lang.Boolean newKeyflag) {

        if (newKeyflag == null)
            return;
        else {
            //�����ǰ���ǹؼ���Ҳ����ָ��,��ֻ�е�newKeyflag==Boolean.TRUEʱ,�Ž�������
            if (m_flag == null) {
                if (newKeyflag.booleanValue())
                    m_flag = Boolean.FALSE;
            } else if (m_flag.booleanValue())//��ǰ��ָ��
            {
                //��Ϊ�ؼ���
                if (newKeyflag.booleanValue())
                    m_flag = Boolean.FALSE;
            } else//��ǰ�ǹؼ���
            {
                if (!newKeyflag.booleanValue()) {
                    m_flag = null;
                }
            }
        }
    }

    /**
     * �˴����뷽�������� �������ڣ�(2003-8-12 10:16:19)
     * 
     * @param newMeasurePK
     *            java.lang.String
     */
    public void setMeasurePK(java.lang.String newMeasurePK) {
        m_strMeasurePK = newMeasurePK;
    }

    /**
     * �˴����뷽�������� �������ڣ�(2003-7-4 11:23:05)
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
     * ���ڶ�̬������ȡ�ؼ��ֵĲ���ʱʹ�á����չؼ�����������MeasureVO��������MeasureVO�е�pk,name
     * �������ڣ�(2003-9-19 17:20:53)
     * 
     * @param key
     *            nc.vo.iufo.keydef.KeyVO
     */
    //public UfoKeyWord setMeasureVOAsRefKeyVO(DynamicAreaVO dynAreaVO, KeyVO
    // keyvo,String repId)
    //{
    //	if( keyvo == null || repId == null )
    //		return null;
    //	//�½�MeasureVO,���չؼ��ֵ����Խ�������
    //	MeasureVO keyMeasure = new MeasureVO();
    //	keyMeasure.setName( keyvo.getName());
    //	keyMeasure.setNote( keyvo.getNote());
    //	keyMeasure.setReportPK(repId);
    //	//�������ͣ������ʱ�����ͣ����ַ�����
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
    //	//����ùؼ����ڻ����в����ڣ�˵����˽�йؼ��֣����ߴ��ڣ����Ǳ������˽�йؼ��֣�������˽�йؼ���ֻ���ڱ������򲻱�����Ϊ����
    //	//ֻ�й��йؼ��ֲű���Ϊ����
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
    //	//���ع����UfoKeyWord
    //	UfoKeyWord ufokey = new UfoKeyWord();
    //	ufokey.setCell(new UfoCell(getPos()));
    //	ufokey.setKeyVO(keyvo);
    //	setState(MEASURE_TABLE_S_CREATE);
    //	setState(MEAS_S_DELTA_TO_KEY);
    //	return ufokey;
    //}
    /**
     * �˴����뷽�������� �������ڣ�(2003-12-9 15:33:29)
     * 
     * @author��������
     * @param newRefedMeasure
     *            java.lang.Boolean
     */
    public void setRefedMeasure(java.lang.Boolean newRefedMeasure) {
        m_isRefedMeasure = newRefedMeasure;
    }

    //�������õ�ָ�꣬���������ñ�־�ͻ���ԭָ��pk
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
     * ����ָ��״̬
     * �������ڣ�(2003-8-8 11:28:59)
     * @param newM_iState int
     */
    public void setState(int newState) {
        //���newState�ǻ���״̬�������ֱ������
        if (newState == MEASURE_TABLE_S_ORIGINAL
                || newState == MEASURE_TABLE_S_UPDATE
                || newState == MEASURE_TABLE_S_DELETE
                || newState == MEASURE_TABLE_S_CREATE) {
            this.m_iState = newState;
        } else if (newState == this.MEAS_S_DELTA_TO_KEY) {
            //�����Ҫת��tokey״̬����ֻ��m_iState״̬ΪOriginal��Create״̬ʱ�ſ���
            if (m_iState == MEASURE_TABLE_S_CREATE
                    || m_iState == MEASURE_TABLE_S_ORIGINAL) {
                this.m_iState += newState;
            }
        }
        //���newStateֵΪ������ֵ����������
    }

    /**
     * ָ��״̬��tokey״̬�ָ���tokeyǰ��״̬
     * �������ڣ�(2003-11-27 15:04:47)
     * @author��������
     */
    public void stateRollBackFromToKey() {
        int newState = m_iState - MEAS_S_DELTA_TO_KEY;
        //ֻ��tokey״̬��ֵ-MEAS_S_DELTA_TO_KEY��Ż� >= 0
        if (newState >= 0)
            m_iState -= MEAS_S_DELTA_TO_KEY;
    }

    /**
     * ����Լ����⡣
     * �������ڣ�(2000-9-28 14:17:28)
     * @exception nc.ui.iufo.table.data.AttributeInvalidException �쳣˵����
     */
    public void validate() throws ValidationException {
        /*if (keyName.length() > 32
         || columnName.length() > 16
         || keyLen <= 0
         || keyLen > 128
         || codeTableName.length() > 64
         || !(keyType >= 0 && keyType <= 7 && keyType != 4 && keyType != 6))
         {
         throw new ValidationException("�������ô���");
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
