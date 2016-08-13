(function ($, channel) {
    'use strict';
    $(function () {
        var loadedTime = new Date();
        channel.on('cq-layer-activated', function (event) {
            if (event.prevLayer && event.layer !== event.prevLayer) {
                var eventTime = new Date();
                if (event.prevLayer !== 'Annotate' && event.layer !== 'Annotate' && (eventTime - loadedTime) > 1500) {
                    location.reload();
                }
            }
        });
    });
})(Granite.$, jQuery(document));