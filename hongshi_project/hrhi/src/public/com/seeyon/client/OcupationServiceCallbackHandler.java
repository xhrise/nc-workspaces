
/**
 * OcupationServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 13, 2008 (05:03:35 LKT)
 */

    package com.seeyon.client;

    /**
     *  OcupationServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class OcupationServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public OcupationServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public OcupationServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for enableByName method
            * override this method for handling normal response from enableByName operation
            */
           public void receiveResultenableByName(
                    com.seeyon.client.OcupationServiceStub.EnableByNameResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from enableByName operation
           */
            public void receiveErrorenableByName(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for deleteOcupationByCode method
            * override this method for handling normal response from deleteOcupationByCode operation
            */
           public void receiveResultdeleteOcupationByCode(
                    com.seeyon.client.OcupationServiceStub.DeleteOcupationByCodeResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deleteOcupationByCode operation
           */
            public void receiveErrordeleteOcupationByCode(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for deleteByName method
            * override this method for handling normal response from deleteByName operation
            */
           public void receiveResultdeleteByName(
                    com.seeyon.client.OcupationServiceStub.DeleteByNameResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deleteByName operation
           */
            public void receiveErrordeleteByName(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for delete method
            * override this method for handling normal response from delete operation
            */
           public void receiveResultdelete(
                    com.seeyon.client.OcupationServiceStub.DeleteResponse result
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
                    com.seeyon.client.OcupationServiceStub.UpdateByNameResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from updateByName operation
           */
            public void receiveErrorupdateByName(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getOcupationByCode method
            * override this method for handling normal response from getOcupationByCode operation
            */
           public void receiveResultgetOcupationByCode(
                    com.seeyon.client.OcupationServiceStub.GetOcupationByCodeResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getOcupationByCode operation
           */
            public void receiveErrorgetOcupationByCode(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for enable method
            * override this method for handling normal response from enable operation
            */
           public void receiveResultenable(
                    com.seeyon.client.OcupationServiceStub.EnableResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from enable operation
           */
            public void receiveErrorenable(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for update method
            * override this method for handling normal response from update operation
            */
           public void receiveResultupdate(
                    com.seeyon.client.OcupationServiceStub.UpdateResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from update operation
           */
            public void receiveErrorupdate(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for create method
            * override this method for handling normal response from create operation
            */
           public void receiveResultcreate(
                    com.seeyon.client.OcupationServiceStub.CreateResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from create operation
           */
            public void receiveErrorcreate(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for updateOcupationByCode method
            * override this method for handling normal response from updateOcupationByCode operation
            */
           public void receiveResultupdateOcupationByCode(
                    com.seeyon.client.OcupationServiceStub.UpdateOcupationByCodeResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from updateOcupationByCode operation
           */
            public void receiveErrorupdateOcupationByCode(java.lang.Exception e) {
            }
                


    }
    