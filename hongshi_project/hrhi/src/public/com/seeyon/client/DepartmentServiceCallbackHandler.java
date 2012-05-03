
/**
 * DepartmentServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 13, 2008 (05:03:35 LKT)
 */

    package com.seeyon.client;

    /**
     *  DepartmentServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class DepartmentServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public DepartmentServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public DepartmentServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for enableByNameArray method
            * override this method for handling normal response from enableByNameArray operation
            */
           public void receiveResultenableByNameArray(
                    com.seeyon.client.DepartmentServiceStub.EnableByNameArrayResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from enableByNameArray operation
           */
            public void receiveErrorenableByNameArray(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for deleteDepartmentByCode method
            * override this method for handling normal response from deleteDepartmentByCode operation
            */
           public void receiveResultdeleteDepartmentByCode(
                    com.seeyon.client.DepartmentServiceStub.DeleteDepartmentByCodeResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deleteDepartmentByCode operation
           */
            public void receiveErrordeleteDepartmentByCode(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for enableByName method
            * override this method for handling normal response from enableByName operation
            */
           public void receiveResultenableByName(
                    com.seeyon.client.DepartmentServiceStub.EnableByNameResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from enableByName operation
           */
            public void receiveErrorenableByName(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for deleteByName method
            * override this method for handling normal response from deleteByName operation
            */
           public void receiveResultdeleteByName(
                    com.seeyon.client.DepartmentServiceStub.DeleteByNameResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deleteByName operation
           */
            public void receiveErrordeleteByName(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for enableByNames method
            * override this method for handling normal response from enableByNames operation
            */
           public void receiveResultenableByNames(
                    com.seeyon.client.DepartmentServiceStub.EnableByNamesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from enableByNames operation
           */
            public void receiveErrorenableByNames(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getDepartmentByCode method
            * override this method for handling normal response from getDepartmentByCode operation
            */
           public void receiveResultgetDepartmentByCode(
                    com.seeyon.client.DepartmentServiceStub.GetDepartmentByCodeResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getDepartmentByCode operation
           */
            public void receiveErrorgetDepartmentByCode(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for deleteByNameArray method
            * override this method for handling normal response from deleteByNameArray operation
            */
           public void receiveResultdeleteByNameArray(
                    com.seeyon.client.DepartmentServiceStub.DeleteByNameArrayResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deleteByNameArray operation
           */
            public void receiveErrordeleteByNameArray(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for delete method
            * override this method for handling normal response from delete operation
            */
           public void receiveResultdelete(
                    com.seeyon.client.DepartmentServiceStub.DeleteResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from delete operation
           */
            public void receiveErrordelete(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for updateByName method
            * override this method for handling normal response from updateByName operation
            */
           public void receiveResultupdateByName(
                    com.seeyon.client.DepartmentServiceStub.UpdateByNameResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from updateByName operation
           */
            public void receiveErrorupdateByName(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for enable method
            * override this method for handling normal response from enable operation
            */
           public void receiveResultenable(
                    com.seeyon.client.DepartmentServiceStub.EnableResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from enable operation
           */
            public void receiveErrorenable(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for move method
            * override this method for handling normal response from move operation
            */
           public void receiveResultmove(
                    com.seeyon.client.DepartmentServiceStub.MoveResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from move operation
           */
            public void receiveErrormove(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for deleteByNames method
            * override this method for handling normal response from deleteByNames operation
            */
           public void receiveResultdeleteByNames(
                    com.seeyon.client.DepartmentServiceStub.DeleteByNamesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deleteByNames operation
           */
            public void receiveErrordeleteByNames(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for update method
            * override this method for handling normal response from update operation
            */
           public void receiveResultupdate(
                    com.seeyon.client.DepartmentServiceStub.UpdateResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from update operation
           */
            public void receiveErrorupdate(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for moveByNameArray method
            * override this method for handling normal response from moveByNameArray operation
            */
           public void receiveResultmoveByNameArray(
                    com.seeyon.client.DepartmentServiceStub.MoveByNameArrayResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from moveByNameArray operation
           */
            public void receiveErrormoveByNameArray(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for create method
            * override this method for handling normal response from create operation
            */
           public void receiveResultcreate(
                    com.seeyon.client.DepartmentServiceStub.CreateResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from create operation
           */
            public void receiveErrorcreate(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for updateDepartmentByCode method
            * override this method for handling normal response from updateDepartmentByCode operation
            */
           public void receiveResultupdateDepartmentByCode(
                    com.seeyon.client.DepartmentServiceStub.UpdateDepartmentByCodeResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from updateDepartmentByCode operation
           */
            public void receiveErrorupdateDepartmentByCode(java.lang.Exception e) {
            }
                


    }
    