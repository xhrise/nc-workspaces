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
 * ���ܣ����Ϲ�˾����
 * @author blueskye
 * 2008��11��21��20:56:39
 */

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

    public class CorpRefModel extends AbstractRefModel {
    	ClientEnvironment ce = ClientEnvironment.getInstance();
    	
        public CorpRefModel(){
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
                    "unitname","unitcode","pk_corp"
            };
        }

        @Override
		public String[] getFieldName() {
            // TODO Auto-generated method stub
            return new String[]{
                    "��˾����","��˾����","����"
            };
        }

        @Override
		public String getPkFieldCode() {
            // TODO Auto-generated method stub
            return "pk_corp";
        }

        @Override
		public String getRefTitle() {
            // TODO Auto-generated method stub
            return "��˾����";
        }

        @Override
		public String getTableName() {
            // TODO Auto-generated method stub
            return "bd_corp";
        }
        
        @Override
		public String getWherePart() {
            return " unitcode like '001%' and length(unitcode)>5";
        }

}

