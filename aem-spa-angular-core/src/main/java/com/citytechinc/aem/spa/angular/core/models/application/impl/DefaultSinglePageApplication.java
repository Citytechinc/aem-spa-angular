package com.citytechinc.aem.spa.angular.core.models.application.impl;

import com.citytechinc.aem.bedrock.api.node.ComponentNode;
import com.citytechinc.aem.bedrock.api.page.PageDecorator;
import com.citytechinc.aem.spa.angular.api.errors.InvalidApplicationConfigurationException;
import com.citytechinc.aem.spa.angular.api.models.application.SinglePageApplication;
import com.citytechinc.aem.spa.angular.api.models.application.SinglePageApplicationState;
import com.citytechinc.aem.spa.angular.api.resource.TypedResource;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

public class DefaultSinglePageApplication implements SinglePageApplication {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultSinglePageApplication.class);

    private static final Predicate<ComponentNode> COMPONENT_REQUIRES_ANGULAR_MODULES_PREDICATE = new Predicate<ComponentNode>() {
        public boolean apply(ComponentNode componentNode) {
            TypedResource typedResource = componentNode.getResource().adaptTo(TypedResource.class);

            return typedResource != null && !typedResource.getResourceType().getAsListInherited(ANGULAR_REQUIRED_MODULES_KEY, String.class).isEmpty();

        }
    };

    private final PageDecorator rootPage;

    private List<SinglePageApplicationState> applicationStates;
    private Set<String> applicationModules;
    private Set<String> applicationPlugins;

    public DefaultSinglePageApplication(PageDecorator rootPage) {
        this.rootPage = rootPage;
    }

    public String getInitialStateUrl() throws InvalidApplicationConfigurationException {
        Optional<String> initialStateOptional = rootPage.get("initialState", String.class);

        if (initialStateOptional.isPresent() && initialStateOptional.get().startsWith(rootPage.getPath())) {
            return initialStateOptional.get().substring(rootPage.getPath().length());
        }

        throw new InvalidApplicationConfigurationException("initialState is unconfigured for the Application Root");
    }

    public String getSanatizedControllerName() {
        //TODO: Tighten up
        return rootPage.getPath().replaceAll("[^A-Za-z0-9]", "");
    }

    public String getRelativePathToState(String absolutePathToState) {
        if (absolutePathToState != null && absolutePathToState.startsWith(getPath())) {
            return absolutePathToState.substring(getPath().length());
        }

        return absolutePathToState;
    }

    public List<SinglePageApplicationState> getApplicationStates() {
        if (applicationStates == null) {
            applicationStates = buildApplicationStates(rootPage.getChildren());
        }

        return applicationStates;
    }

    public String getApplicationName() {
        return rootPage.get("angularApplicationName", "AngularApplication");
    }

    public Set<String> getRequiredAngularModules() {

        if (applicationModules != null) {
            return applicationModules;
        }

        applicationModules = Sets.newHashSet();

        for (ComponentNode currentChildNode : rootPage.adaptTo(ComponentNode.class).getParent().get().findDescendants(COMPONENT_REQUIRES_ANGULAR_MODULES_PREDICATE)) {
            TypedResource typedResource = currentChildNode.getResource().adaptTo(TypedResource.class);

            if (typedResource != null) {
                applicationModules.addAll(typedResource.getResourceType().getAsListInherited(ANGULAR_REQUIRED_MODULES_KEY, String.class));
            }
        }

        return applicationModules;

    }

    public String getPath() {
        return rootPage.getPath();
    }

    public String getApplicationDirectory() {
        String path = rootPage.getParent().getPath();
        return path.substring(rootPage.getParent().getPath().lastIndexOf("/") + 1);
    }

    private List<SinglePageApplicationState> buildApplicationStates(List<PageDecorator> pages) {

        List<SinglePageApplicationState> states = Lists.newArrayList();

        for (PageDecorator childPage : pages) {
            states.add(new DefaultSinglePageApplicationState(childPage, rootPage));
            states.addAll(buildApplicationStates(childPage.getChildren()));
        }

        return states;

    }

}
