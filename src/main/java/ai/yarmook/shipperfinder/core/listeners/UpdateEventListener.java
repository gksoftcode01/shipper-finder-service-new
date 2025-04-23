package ai.yarmook.shipperfinder.core.listeners;

import ai.yarmook.shipperfinder.core.service.YCargoRequestService;
import ai.yarmook.shipperfinder.core.service.YTripService;
import ai.yarmook.shipperfinder.domain.CargoRequest;
import ai.yarmook.shipperfinder.domain.CargoRequestItem;
import ai.yarmook.shipperfinder.domain.Trip;
import ai.yarmook.shipperfinder.domain.TripItem;
import java.io.IOException;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UpdateEventListener implements PostUpdateEventListener {

    private final Logger log = LoggerFactory.getLogger(UpdateEventListener.class);
    private final YTripService tripService;
    private final YCargoRequestService cargoRequestService;

    public UpdateEventListener(YTripService tripService, YCargoRequestService cargoRequestService) {
        this.tripService = tripService;
        this.cargoRequestService = cargoRequestService;
    }

    @Override
    public void onPostUpdate(PostUpdateEvent postUpdateEvent) {
        try {
            if (postUpdateEvent.getEntity() instanceof TripItem) {
                syncTrip(((TripItem) postUpdateEvent.getEntity()).getTrip().getId());
            } else if (postUpdateEvent.getEntity() instanceof Trip && postUpdateEvent.getDirtyProperties().length > 0) {
                syncTrip(((Trip) postUpdateEvent.getEntity()).getId());
            } else if (postUpdateEvent.getEntity() instanceof CargoRequestItem) {
                syncCargoRequest(((CargoRequestItem) postUpdateEvent.getEntity()).getCargoRequest().getId());
            } else if (postUpdateEvent.getEntity() instanceof CargoRequest && postUpdateEvent.getDirtyProperties().length > 0) {
                syncCargoRequest(((CargoRequest) postUpdateEvent.getEntity()).getId());
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    private void syncTrip(Long tripId) throws IOException {
        tripService.syncTrip(tripId);
    }

    private void syncCargoRequest(Long requestId) throws IOException {
        cargoRequestService.syncCargoRequest(requestId);
    }

    @Override
    public boolean requiresPostCommitHandling(EntityPersister entityPersister) {
        return false;
    }
}
