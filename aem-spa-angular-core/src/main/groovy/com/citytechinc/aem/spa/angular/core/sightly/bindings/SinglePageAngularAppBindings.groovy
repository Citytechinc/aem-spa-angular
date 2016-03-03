package com.citytechinc.aem.spa.angular.core.sightly.bindings

import com.day.cq.wcm.api.WCMMode
import org.apache.sling.api.SlingHttpServletRequest

import javax.script.Bindings

class SinglePageAngularAppBindings implements Bindings {

    public static final String IS_APP_MODE = "isSPAAngularAemAppMode"

    @Delegate
    private final Map<String, Object> map = [:]

    SinglePageAngularAppBindings(SlingHttpServletRequest request) {
        def mode = WCMMode.fromRequest(request)

        map.put(IS_APP_MODE, mode == WCMMode.DISABLED || mode == WCMMode.PREVIEW)
    }

    @Override
    def put(String key, value) {
        map.put(key, value)
    }
}
