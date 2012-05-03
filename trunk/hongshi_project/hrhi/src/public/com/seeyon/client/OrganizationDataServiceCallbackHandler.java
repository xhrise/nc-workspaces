
/**
 * OrganizationDataServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 13, 2008 (05:03:35 LKT)
 */

    package com.seeyon.client;

    /**
     *  OrganizationDataServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class OrganizationDataServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public OrganizationDataServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public OrganizationDataServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for exportData method
            * override this method for handling normal response from exportData operation
            */
           public void receiveResultexportData(
                    com.seeyon.client.OrganizationDataServiceStub.ExportDataResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from exportData operation
           */
            public void receiveErrorexportData(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for importData method
            * override this method for handling normal response from importData operation
            */
           public void receiveResultimportData(
                    com.seeyon.client.OrganizationDataServiceStub.ImportDataResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from importData operation
           */
            public void receiveErrorimportData(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for exportOcupation method
            * override this method for handling normal response from exportOcupation operation
            */
           public void receiveResultexportOcupation(
                    com.seeyon.client.OrganizationDataServiceStub.ExportOcupationResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from exportOcupation operation
           */
            public void receiveErrorexportOcupation(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for exportDepartment method
            * override this method for handling normal response from exportDepartment operation
            */
           public void receiveResultexportDepartment(
                    com.seeyon.client.OrganizationDataServiceStub.ExportDepartmentResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from exportDepartment operation
           */
            public void receiveErrorexportDepartment(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for exportPerson method
            * override this method for handling normal response from exportPerson operation
            */
           public void receiveResultexportPerson(
                    com.seeyon.client.OrganizationDataServiceStub.ExportPersonResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from exportPerson operation
           */
            public void receiveErrorexportPerson(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for exportOType method
            * override this method for handling normal response from exportOType operation
            */
           public void receiveResultexportOType(
                    com.seeyon.client.OrganizationDataServiceStub.ExportOTypeResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from exportOType operation
           */
            public void receiveErrorexportOType(java.lang.Exception e) {
            }
                


    }
    