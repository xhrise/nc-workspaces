package nc.itf.hskaoqin.all;

import java.util.List;

public interface IKaoQin {
	public String impkaoqindata(String starTime, String endTime , String sourceName);

	public List<String> getUnits(String dataSource) throws Exception;
}
