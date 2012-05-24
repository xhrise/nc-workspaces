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

/*
 * ���ܣ����ò���
 * ���ߣ�����Դ
 */

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

    public class FyRefModel extends AbstractRefModel {
    	ClientEnvironment ce = ClientEnvironment.getInstance();
    	
        public FyRefModel(){
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
                    "fylbcode","fylbname","pk_fylb"
            };
        }

        @Override
		public String[] getFieldName() {
            // TODO Auto-generated method stub
            return new String[]{
                    "���ñ���","��������","����"
            };
        }

        @Override
		public String getPkFieldCode() {
            // TODO Auto-generated method stub
            return "pk_fylb";
        }

        @Override
		public String getRefTitle() {
            // TODO Auto-generated method stub
            return "���ò���";
        }

        @Override
		public String getTableName() {
            // TODO Auto-generated method stub
            return "eh_arap_fylb";
        }
        
        @Override
		public String getWherePart() {
            return "pk_corp = '"+ce.getCorporation().getPk_corp()+"' and isnull(dr,0)=0 ";
        }

}

