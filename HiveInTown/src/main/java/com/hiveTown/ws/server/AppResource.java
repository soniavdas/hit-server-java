package com.hiveTown.ws.server;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hiveTown.service.CommunityService;


@Component
@Path("/app")
public class AppResource {
	
	private static final Logger LOGGER = Logger.getLogger(AppResource.class);
 
	private org.springframework.beans.factory.config.PropertiesFactoryBean applicationProperties;
	
	@Autowired
    public void setApplicationProperties(org.springframework.beans.factory.config.PropertiesFactoryBean a){
        this.applicationProperties = a;
    }
	 
	@Value("${version}")
	public String version;
	 
	@GET
    @Path("/version")
    @Produces(MediaType.TEXT_PLAIN)
    public String getVersion() {
		String ver = "";
		
		try {
			ver = applicationProperties.getObject().getProperty("version");
			LOGGER.info("Get version:" + ver);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ver;
    }
	 
}
