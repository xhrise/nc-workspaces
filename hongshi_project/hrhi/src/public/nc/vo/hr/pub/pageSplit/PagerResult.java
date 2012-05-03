package nc.vo.hr.pub.pageSplit;

import java.io.Serializable;

public class PagerResult implements Serializable {

	  private static final long serialVersionUID = 200L;

	  private int maxRecordCount;	//����¼��
	  private int avaCount;			//��ǰҳ�ܵļ�¼��
	  private Object result;		//��ҵ��ڵ��ѯ�ķ���ֵ

	  public PagerResult() {
	  }

	  public PagerResult(int maxRecordCount,int avaCount, Object result) {
		   this.maxRecordCount = maxRecordCount;
		   this.avaCount = avaCount;
		   this.result = result;
	  }

	  public PagerResult(PagerResult pagerResult) {
		  maxRecordCount = pagerResult.maxRecordCount;
		  avaCount = pagerResult.avaCount;
		  result = pagerResult.result;
	  }

	public int getMaxRecordCount() {
		return maxRecordCount;
	}

	public void setMaxRecordCount(int maxRecordCount) {
		this.maxRecordCount = maxRecordCount;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public int getAvaCount() {
		return avaCount;
	}

	public void setAvaCount(int avaCount) {
		this.avaCount = avaCount;
	}


}
