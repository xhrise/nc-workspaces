package nc.ui.bi.query.freequery.measquery;

import java.awt.Container;

import nc.ui.bi.query.manager.RptTraceDataResult;

import com.ufida.dataset.tracedata.ITraceDataResult;
import com.ufida.dataset.tracedata.TraceDataOperator;
import com.ufsoft.iufo.inputplugin.MeasTraceInfo;
import com.ufsoft.iufo.inputplugin.biz.WindowNavUtil;

public class RptTraceDataOperator extends TraceDataOperator {

	@Override
	public void trace(Container container, ITraceDataResult result) {
	  
      if(result instanceof RptTraceDataResult){
    	  RptTraceDataResult traceResult=(RptTraceDataResult)result;
    	  MeasTraceInfo measTraceInfo=traceResult.getMeasTraceInfo();
          WindowNavUtil.traceZiorMeasure(container,measTraceInfo,true);
    	  
      }
	}

}
