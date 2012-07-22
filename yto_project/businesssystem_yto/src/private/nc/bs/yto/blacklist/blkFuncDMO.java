package nc.bs.yto.blacklist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.NamingException;

import nc.bs.pub.DataManageObject;
import nc.jdbc.framework.ConnectionFactory;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

public class blkFuncDMO extends DataManageObject {

	public blkFuncDMO() throws NamingException {
		super();
	}

	private Connection conn = null;

	private PreparedStatement stmt = null;

	private ResultSet rs = null;
	
	public String getStr(String sql , String datasource) {
		try{
			if ("".equals(datasource))
				this.conn = ConnectionFactory.getConnection();
		    else
		    	this.conn = ConnectionFactory.getConnection(datasource);

			this.stmt = this.conn.prepareStatement(sql);
		    this.rs = this.stmt.executeQuery();
		    
		    if(rs.next()) {
		    	return rs.getString(1);
		    }
		    
		} catch(Exception ex){
			ex.printStackTrace();
		} finally {
			this.closeAll();
		}
		
		return null;
	}
	
	public boolean executePsndocBad(String sql , String datasource) {
		try{
			if ("".equals(datasource))
				this.conn = ConnectionFactory.getConnection();
		    else
		    	this.conn = ConnectionFactory.getConnection(datasource);

			this.stmt = this.conn.prepareStatement(sql);
		    return this.stmt.execute();
		    
		} catch(Exception ex){
			ex.printStackTrace();
		} finally {
			this.closeAll();
		}
		
		return false;
	}
	
	public nc.vo.yto.business.PsnbasdocVO getPsnbasdocbyPk(String sql , String datasource) {
		try{
			if ("".equals(datasource))
				this.conn = ConnectionFactory.getConnection();
		    else
		    	this.conn = ConnectionFactory.getConnection(datasource);

			this.stmt = this.conn.prepareStatement(sql);
		    this.rs = this.stmt.executeQuery();
		    
		    nc.vo.yto.business.PsnbasdocVO psnbas = new nc.vo.yto.business.PsnbasdocVO();
		    
		    if(rs.next()) {
		    	for(String attr : psnbas.getAttributeNames()) {
		    		try{
		    			psnbas.setAttributeValue(attr, rs.getString(attr));
		    		} catch(Exception e1) {
		    			try {
		    				psnbas.setAttributeValue(attr, new UFDate(rs.getString(attr)));
		    			} catch(Exception e2) {
		    				try {
			    				psnbas.setAttributeValue(attr, new UFBoolean(rs.getString(attr)));
			    			} catch(Exception e3) {
			    				try {
				    				psnbas.setAttributeValue(attr, new UFDateTime(rs.getString(attr)));
				    			} catch(Exception e4) {
				    				try{
				    					psnbas.setAttributeValue(attr, new UFDouble(rs.getString(attr)));
				    				} catch(Exception e5) {
				    					try{
					    					psnbas.setAttributeValue(attr, new Integer(rs.getString(attr)));
					    				} catch(Exception e6) {
					    					continue;
					    				}
				    				}
				    			}
			    			}
		    			}
		    		}
		    	}
		    }
		    
		    return psnbas;
		    
		} catch(Exception ex){
			ex.printStackTrace();
		} finally {
			this.closeAll();
		}
		
		return null;
	}
	
	
	public void closeAll() {
	    try {
	      if (this.rs != null)
	        this.rs.close();
	    }
	    catch (Exception localException) {
	    }
	    try {
	      if (this.stmt != null)
	        this.stmt.close();
	    }
	    catch (Exception localException1) {
	    }
	    try {
	      if (this.conn != null)
	        this.conn.close();
	    }
	    catch (Exception localException2) {
	    }
	  }
}
