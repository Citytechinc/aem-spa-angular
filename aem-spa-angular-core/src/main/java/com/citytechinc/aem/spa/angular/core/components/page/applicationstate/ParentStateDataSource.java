package com.citytechinc.aem.spa.angular.core.components.page.applicationstate;

import com.citytechinc.aem.spa.angular.api.models.application.SinglePageApplicationState;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.citytechinc.aem.bedrock.api.page.PageDecorator;
import com.citytechinc.aem.bedrock.api.page.PageManagerDecorator;
import com.citytechinc.aem.bedrock.api.request.ComponentServletRequest;
import com.citytechinc.aem.bedrock.core.servlets.optionsprovider.AbstractOptionsProviderServlet;
import com.citytechinc.aem.bedrock.core.servlets.optionsprovider.Option;
import org.apache.felix.scr.annotations.sling.SlingServlet;

import java.util.List;


@SlingServlet(resourceTypes = ParentStateDataSource.RESOURCE_TYPE)
public class ParentStateDataSource extends AbstractOptionsProviderServlet {

    public static final String RESOURCE_TYPE = "aem-spa-angular/components/page/single-page-application-state/parentstatedatasource";

    @Override
    protected Optional<String> getOptionsRoot(ComponentServletRequest componentServletRequest){
        PageDecorator currentPage = componentServletRequest.getResourceResolver().adaptTo(PageManagerDecorator.class).getContainingPage(componentServletRequest.getSlingRequest().getRequestPathInfo().getSuffix());
        PageDecorator parent = currentPage.getParent();
        Option optionRoot = new Option(parent.getPath(),parent.getTitle());
//        return Optional.fromNullable(currentPage.getParent().getPath());
//        return Optional.fromNullable(optionRoot);
        return Optional.fromNullable(null);
    }

    @Override
    protected List<Option> getOptions(ComponentServletRequest componentServletRequest) {
        List<Option> options = Lists.newArrayList();

        PageDecorator currentPage = componentServletRequest.getResourceResolver().adaptTo(PageManagerDecorator.class).getContainingPage(componentServletRequest.getSlingRequest().getRequestPathInfo().getSuffix());

        SinglePageApplicationState currentState = currentPage.adaptTo(SinglePageApplicationState.class);

        if (currentState != null) {
            Optional<SinglePageApplicationState> parentStateOptional = currentState.getDirectParentState();

            while(parentStateOptional.isPresent()) {
                SinglePageApplicationState parentState = parentStateOptional.get();

                if (!parentState.isStructuralState()) {
                    options.add(new Option(parentStateOptional.get().getPath(), parentStateOptional.get().getTitle()));
                }

                parentStateOptional = parentStateOptional.get().getDirectParentState();

            }
        }

        return options;
    }

}