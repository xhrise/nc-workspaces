package nc.vo.hskaoqin.all;

import nc.vo.pub.SuperVO;

public class ImpDataVO extends SuperVO {

	/**
	 * 主键
	 */
	private String pk_importdata;
	/**
	 * 公司主键
	 */
	private String pk_corp;
	/**
	 * timecardid
	 */
	private String timecardid;
	/**
	 * calendartime
	 */
	private String calendartime;
	/**
	 * datastatus
	 */
	private String datastatus;
	
	public String getCalendartime() {
		return calendartime;
	}

	public void setCalendartime(String calendartime) {
		this.calendartime = calendartime;
	}

	public String getDatastatus() {
		return datastatus;
	}

	public void setDatastatus(String datastatus) {
		this.datastatus = datastatus;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public String getPk_importdata() {
		return pk_importdata;
	}

	public void setPk_importdata(String pk_importdata) {
		this.pk_importdata = pk_importdata;
	}

	public String getTimecardid() {
		return timecardid;
	}

	public void setTimecardid(String timecardid) {
		this.timecardid = timecardid;
	}

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

}
