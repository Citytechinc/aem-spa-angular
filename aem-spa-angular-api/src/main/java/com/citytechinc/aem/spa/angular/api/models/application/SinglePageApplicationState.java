package com.citytechinc.aem.spa.angular.api.models.application;

import org.apache.sling.api.resource.Resource;

public interface SinglePageApplicationState {

    String RESOURCE_TYPE = "aem-spa-angular/components/page/single-page-application-state";
    String ANGULAR_CONTROLLER_KEY = "angularController";

    String getUrl();

    String getTemplate();

    String getId();

    String getPath();

    Resource getContentResource();

    String getAngularController();

    boolean isAbstract();

    boolean isStructuralState();

    SinglePageApplication getApplicationRoot();

}
