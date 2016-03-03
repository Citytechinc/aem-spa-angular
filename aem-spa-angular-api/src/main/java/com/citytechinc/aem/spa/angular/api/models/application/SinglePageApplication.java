package com.citytechinc.aem.spa.angular.api.models.application;

import com.citytechinc.aem.spa.angular.api.errors.InvalidApplicationConfigurationException;

import java.util.List;
import java.util.Set;

public interface SinglePageApplication {

    String RESOURCE_TYPE = "aem-spa-angular/components/page/single-page-application";
    String ANGULAR_REQUIRED_MODULES_KEY = "requiredAngularModules";

    String getInitialStateUrl() throws InvalidApplicationConfigurationException;

    String getSanatizedControllerName();

    String getRelativePathToState(String absolutePathToState);

    List<SinglePageApplicationState> getApplicationStates();

    String getApplicationName();

    Set<String> getRequiredAngularModules();

    String getPath();

    String getApplicationDirectory();

}
