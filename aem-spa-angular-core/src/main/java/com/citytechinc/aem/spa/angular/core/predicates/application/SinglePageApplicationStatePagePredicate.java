package com.citytechinc.aem.spa.angular.core.predicates.application;

import com.citytechinc.aem.bedrock.api.page.PageDecorator;
import com.citytechinc.aem.spa.angular.api.models.application.SinglePageApplicationState;
import com.google.common.base.Predicate;

public class SinglePageApplicationStatePagePredicate implements Predicate<PageDecorator> {

    @Override
    public boolean apply(PageDecorator pageDecorator) {
        return pageDecorator.getContentResource().isResourceType(SinglePageApplicationState.RESOURCE_TYPE);
    }

}
