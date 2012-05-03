
/**
 * BPMServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 13, 2008 (05:03:35 LKT)
 */

    package com.seeyon.client;

    /**
     *  BPMServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class BPMServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public BPMServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public BPMServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for launchHtmlCollaboration method
            * override this method for handling normal response from launchHtmlCollaboration operation
            */
           public void receiveResultlaunchHtmlCollaboration(
                    com.seeyon.client.BPMServiceStub.LaunchHtmlCollaborationResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from launchHtmlCollaboration operation
           */
            public void receiveErrorlaunchHtmlCollaboration(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for sendFormCollaboration method
            * override this method for handling normal response from sendFormCollaboration operation
            */
           public void receiveResultsendFormCollaboration(
                    com.seeyon.client.BPMServiceStub.SendFormCollaborationResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from sendFormCollaboration operation
           */
            public void receiveErrorsendFormCollaboration(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getFormApprovalState method
            * override this method for handling normal response from getFormApprovalState operation
            */
           public void receiveResultgetFormApprovalState(
                    com.seeyon.client.BPMServiceStub.GetFormApprovalStateResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getFormApprovalState operation
           */
            public void receiveErrorgetFormApprovalState(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for launchFormCollaboration method
            * override this method for handling normal response from launchFormCollaboration operation
            */
           public void receiveResultlaunchFormCollaboration(
                    com.seeyon.client.BPMServiceStub.LaunchFormCollaborationResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from launchFormCollaboration operation
           */
            public void receiveErrorlaunchFormCollaboration(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getAllFormCollIdsByDateTimeAndState method
            * override this method for handling normal response from getAllFormCollIdsByDateTimeAndState operation
            */
           public void receiveResultgetAllFormCollIdsByDateTimeAndState(
                    com.seeyon.client.BPMServiceStub.GetAllFormCollIdsByDateTimeAndStateResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getAllFormCollIdsByDateTimeAndState operation
           */
            public void receiveErrorgetAllFormCollIdsByDateTimeAndState(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getTemplateDefinition method
            * override this method for handling normal response from getTemplateDefinition operation
            */
           public void receiveResultgetTemplateDefinition(
                    com.seeyon.client.BPMServiceStub.GetTemplateDefinitionResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getTemplateDefinition operation
           */
            public void receiveErrorgetTemplateDefinition(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getFlowState method
            * override this method for handling normal response from getFlowState operation
            */
           public void receiveResultgetFlowState(
                    com.seeyon.client.BPMServiceStub.GetFlowStateResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getFlowState operation
           */
            public void receiveErrorgetFlowState(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getAllFormCollIdsByDateTime method
            * override this method for handling normal response from getAllFormCollIdsByDateTime operation
            */
           public void receiveResultgetAllFormCollIdsByDateTime(
                    com.seeyon.client.BPMServiceStub.GetAllFormCollIdsByDateTimeResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getAllFormCollIdsByDateTime operation
           */
            public void receiveErrorgetAllFormCollIdsByDateTime(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getFormCollIdsByDateTime method
            * override this method for handling normal response from getFormCollIdsByDateTime operation
            */
           public void receiveResultgetFormCollIdsByDateTime(
                    com.seeyon.client.BPMServiceStub.GetFormCollIdsByDateTimeResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getFormCollIdsByDateTime operation
           */
            public void receiveErrorgetFormCollIdsByDateTime(java.lang.Exception e) {
            }
                


    }
    