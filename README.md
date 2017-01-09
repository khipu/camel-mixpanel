Mixpanel component
=====================================

The Mixpanel component implement an events producer for Mixpanel service.

URI format:

    mixpanel:mixpanelProjectToken


 Where *mixpanelProjectToken* is the project associated token in your mixpanel user account.
 
###Message body

 

 The component take the JSON properties in the exchange body and send it to mixpanel service.
 
###Message headers
 
You should provide two headers.


MIXPANEL_DISCTINCT_ID : The distinct id for the event you are sending to mixpanel service

MIXPANEL_EVENT_NAME: The name for the event you are sending to mixpanel service



An example of use could be:



    from("direct:foo")
      .setHeader("MIXPANEL_DISCTINCT_ID","1234")
      .setHeader("MIXPANEL_EVENT_NAME","merchandise sent")
      .to("mixpanel:fdgsdxzdasdq23asa12");
      
Note that the component is designed to run over a standalone apache camel jvm, and has not osgi ready dependencies.
      