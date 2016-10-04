package com.hiveTown.ws.server;


import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class Application extends ResourceConfig {

    /**
     * Register JAX-RS application components.
     */
    public Application () {
    	super(HTUploadFileServiceImpl.class, MultiPartFeature.class);
    	packages("com.hiveTown.ws.server");
        register(CommunityResource.class);
        register(HTUploadFileServiceImpl.class);
        register(HTWSServerImpl.class);
        register(NoticeBoardResource.class);
        register(AppResource.class);
    }
    
    
  
}
