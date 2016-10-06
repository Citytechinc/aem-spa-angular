package com.citytechinc.aem.spa.angular.core.models.application.impl;

import com.citytechinc.aem.bedrock.api.page.PageDecorator;
import com.citytechinc.aem.spa.angular.api.models.application.SinglePageApplication;
import com.citytechinc.aem.spa.angular.api.models.application.SinglePageApplicationState;
import com.citytechinc.aem.spa.angular.api.resource.TypedResource;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;

import java.util.List;

public class DefaultSinglePageApplicationState implements SinglePageApplicationState {

    private final PageDecorator statePage;
    private final PageDecorator rootPage;

    private String stateId;
    private String url;
    private Optional<String> angularController;

    public DefaultSinglePageApplicationState(PageDecorator statePage, PageDecorator rootPage) {
        this.statePage = statePage;
        this.rootPage = rootPage;
    }

    public String getUrl() {
        if (url != null) {
            return url;
        }

        PageDecorator currentPage = statePage;
        Optional<SinglePageApplicationState> parentState = getParentState();

        List<String> pathParts = Lists.newArrayList();

        while (currentPage != null &&
                currentPage.getContentResource().isResourceType(SinglePageApplicationState.RESOURCE_TYPE) &&
                (
                   currentPage.getPath().equals(statePage.getPath()) ||
                   currentPage.get("isStructuralState", false) ||
                   (parentState.isPresent() && !parentState.get().getPath().equals(currentPage.getPath())))) {
            if (currentPage.get("isSlugState", false)) {
                pathParts.add(":" + currentPage.getName());
            }
            else {
                pathParts.add(currentPage.getName());
            }

            currentPage = currentPage.getParent();
        }

        url = "/" + StringUtils.join(Lists.reverse(pathParts), "/");

        return url;
    }

    public String getTemplate() {
        return rootPage.getName() + statePage.getPath().substring(rootPage.getPath().length()) + ".template.html";
    }

    public String getId() {
        if (stateId != null) {
            return stateId;
        }

        Optional<SinglePageApplicationState> parentStateOptional = getParentState();

        if (parentStateOptional.isPresent()) {
            stateId = parentStateOptional.get().getId() + "." + statePage.getName();
            return stateId;
        }

        stateId = statePage.getName();
        return stateId;
    }

    public String getPath() {
        return statePage.getPath();
    }

    public Resource getContentResource() {
        return statePage.getContentResource();
    }

    public String getAngularController() {
        return getAngularControllerOptional().orNull();
    }

    @Override
    public String getTitle() {
        return statePage.getTitle();
    }

    public boolean isHasAngularController() {
        return getAngularControllerOptional().isPresent();
    }

    public boolean isAbstract() {
        return statePage.get("isAbstractState", false);
    }

    public boolean isStructuralState() {
        return statePage.get("isStructuralState", false);
    }

    @Override
    public Optional<SinglePageApplicationState> getDirectParentState() {
        return Optional.fromNullable(statePage.getParent().adaptTo(SinglePageApplicationState.class));
    }

    @Override
    public Optional<SinglePageApplicationState> getParentState() {
        Optional<String> parentStatePath = statePage.get("parentStatePath", String.class);

        if (parentStatePath.isPresent()) {
            return Optional.fromNullable(statePage.getPageManager().getPage(parentStatePath.get()).adaptTo(SinglePageApplicationState.class));
        }

        Optional<SinglePageApplicationState> directParentState = getDirectParentState();

        while (directParentState.isPresent() && directParentState.get().isStructuralState()) {
            directParentState = directParentState.get().getDirectParentState();
        }

        return directParentState;
    }

    public SinglePageApplication getApplicationRoot() {
        return rootPage.adaptTo(SinglePageApplication.class);
    }

    protected Optional<String> getAngularControllerOptional() {

        if (angularController == null) {
            TypedResource typedResource = getContentResource().adaptTo(TypedResource.class);

            if (typedResource != null) {
                angularController = typedResource.getResourceType().getInherited(ANGULAR_CONTROLLER_KEY, String.class);
            }
        }

        return angularController;

    }
}
