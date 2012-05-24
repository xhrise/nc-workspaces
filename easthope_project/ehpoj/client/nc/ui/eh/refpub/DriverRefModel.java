/**
 * @(#)FyRefModel.java	V3.1 2008-5-29
 * 
 * Copyright 1988-2005 UFIDA, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of UFSoft, Inc.
 * Use is subject to license terms.
 *
 */
package nc.ui.eh.refpub;

/**
 * 功能：司机档案参照
 * @author blueskye
 * 2008年11月21日20:56:39
 */

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

    public class DriverRefModel extends AbstractRefModel {
    	ClientEnvironment ce = ClientEnvironment.getInstance();
    	
        public DriverRefModel(){
            super();
        }
        
        @Override
		public int getDefaultFieldCount() {
            return 3;
        }

        @Override
		public String[] getFieldCode() {
            // TODO Auto-generated method stub
            return new String[]{
                    "driverid","drivername","pk_driver"
            };
        }

        @Override
		public String[] getFieldName() {
            // TODO Auto-generated method stub
            return new String[]{
                    "身份证号","驾驶员姓名","主键"
            };
        }

        @Override
		public String getPkFieldCode() {
            // TODO Auto-generated method stub
            return "pk_driver";
        }

        @Override
		public String getRefTitle() {
            // TODO Auto-generated method stub
            return "驾驶员参照";
        }

        @Override
		public String getTableName() {
            // TODO Auto-generated method stub
            return "eh_trade_driver";
        }
        
        @Override
		public String getWherePart() {
            return "pk_corp = '"+ce.getCorporation().getPk_corp()+"' and isnull(dr,0)=0 ";
        }

}

