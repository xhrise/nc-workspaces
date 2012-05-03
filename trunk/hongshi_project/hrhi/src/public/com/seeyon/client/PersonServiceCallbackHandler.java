
/**
 * PersonServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 13, 2008 (05:03:35 LKT)
 */

    package com.seeyon.client;

    /**
     *  PersonServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class PersonServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public PersonServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public PersonServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for updateByLoginName method
            * override this method for handling normal response from updateByLoginName operation
            */
           public void receiveResultupdateByLoginName(
                    com.seeyon.client.PersonServiceStub.UpdateByLoginNameResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from updateByLoginName operation
           */
            public void receiveErrorupdateByLoginName(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for createFromXml method
            * override this method for handling normal response from createFromXml operation
            */
           public void receiveResultcreateFromXml(
                    com.seeyon.client.PersonServiceStub.CreateFromXmlResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from createFromXml operation
           */
            public void receiveErrorcreateFromXml(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for updateFromXml method
            * override this method for handling normal response from updateFromXml operation
            */
           public void receiveResultupdateFromXml(
                    com.seeyon.client.PersonServiceStub.UpdateFromXmlResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from updateFromXml operation
           */
            public void receiveErrorupdateFromXml(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for delete method
            * override this method for handling normal response from delete operation
            */
           public void receiveResultdelete(
                    com.seeyon.client.PersonServiceStub.DeleteResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from delete operation
           */
            public void receiveErrordelete(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for enable method
            * override this method for handling normal response from enable operation
            */
           public void receiveResultenable(
                    com.seeyon.client.PersonServiceStub.EnableResponse result
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
                    com.seeyon.client.PersonServiceStub.UpdateResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from update operation
           */
            public void receiveErrorupdate(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for setPassword method
            * override this method for handling normal response from setPassword operation
            */
           public void receiveResultsetPassword(
                    com.seeyon.client.PersonServiceStub.SetPasswordResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from setPassword operation
           */
            public void receiveErrorsetPassword(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for create method
            * override this method for handling normal response from create operation
            */
           public void receiveResultcreate(
                    com.seeyon.client.PersonServiceStub.CreateResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from create operation
           */
            public void receiveErrorcreate(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for enableByLoginName method
            * override this method for handling normal response from enableByLoginName operation
            */
           public void receiveResultenableByLoginName(
                    com.seeyon.client.PersonServiceStub.EnableByLoginNameResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from enableByLoginName operation
           */
            public void receiveErrorenableByLoginName(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for setPasswordByLoginName method
            * override this method for handling normal response from setPasswordByLoginName operation
            */
           public void receiveResultsetPasswordByLoginName(
                    com.seeyon.client.PersonServiceStub.SetPasswordByLoginNameResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from setPasswordByLoginName operation
           */
            public void receiveErrorsetPasswordByLoginName(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for deleteByLoginName method
            * override this method for handling normal response from deleteByLoginName operation
            */
           public void receiveResultdeleteByLoginName(
                    com.seeyon.client.PersonServiceStub.DeleteByLoginNameResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deleteByLoginName operation
           */
            public void receiveErrordeleteByLoginName(java.lang.Exception e) {
            }
                


    }
    