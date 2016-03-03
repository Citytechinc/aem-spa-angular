package com.citytechinc.aem.spa.angular.core.adapters;

import com.citytechinc.aem.bedrock.api.page.PageDecorator;
import com.citytechinc.aem.bedrock.api.page.PageManagerDecorator;
import com.citytechinc.aem.spa.angular.core.models.application.impl.DefaultSinglePageApplication;
import com.citytechinc.aem.spa.angular.core.predicates.application.SinglePageApplicationPagePredicate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingConstants;
import org.apache.sling.api.adapter.AdapterFactory;
import org.apache.sling.api.resource.Resource;
import org.osgi.framework.Constants;

@Component
@Service(AdapterFactory.class)
@Properties({
        @Property(name = Constants.SERVICE_DESCRIPTION, value = "ApplicationRoot Adapter Factory"),
        @Property(name = SlingConstants.PROPERTY_ADAPTABLE_CLASSES, value = {
                "org.apache.sling.api.resource.Resource",
                "com.citytechinc.aem.bedrock.api.page.PageDecorator"
        }),
        @Property(name = SlingConstants.PROPERTY_ADAPTER_CLASSES, value = {
                "com.citytechinc.aem.spa.angular.api.models.application.SinglePageApplication"
        })
    })
public class SinglePageApplicationAdapterFactory implements AdapterFactory {

    private static final SinglePageApplicationPagePredicate applicationRootPagePredicate = new SinglePageApplicationPagePredicate();

    public <AdapterType> AdapterType getAdapter(Object adaptable, Class<AdapterType> type) {
        if (adaptable instanceof Resource) {
            Resource resource = (Resource) adaptable;

            return getPageDecoratorAdapter(resource.getResourceResolver().adaptTo(PageManagerDecorator.class).getContainingPage(resource), type);
        }
        if (adaptable instanceof PageDecorator) {
            return getPageDecoratorAdapter((PageDecorator) adaptable, type);
        }

        return null;
    }

    public <AdapterType> AdapterType getPageDecoratorAdapter(PageDecorator pageDecorator, Class<AdapterType> type) {
        if (applicationRootPagePredicate.apply(pageDecorator)) {
            return (AdapterType) new DefaultSinglePageApplication(pageDecorator);
        }

        return null;
    }

}
