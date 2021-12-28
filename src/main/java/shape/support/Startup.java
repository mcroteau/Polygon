package shape.support;

import shape.service.StartupService;
import qio.Qio;
import qio.annotate.Events;
import qio.support.QioEvents;

@Events
public class Startup implements QioEvents {
    @Override
    public void setupComplete(Qio qio) {
        StartupService startupService = (StartupService) qio.getElement("startupservice");
        startupService.init();
    }
}
