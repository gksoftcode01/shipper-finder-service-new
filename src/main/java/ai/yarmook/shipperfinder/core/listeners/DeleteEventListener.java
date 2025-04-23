package ai.yarmook.shipperfinder.core.listeners;

import ai.yarmook.shipperfinder.core.service.YCargoRequestService;
import ai.yarmook.shipperfinder.core.service.YTripService;
import ai.yarmook.shipperfinder.domain.CargoRequest;
import ai.yarmook.shipperfinder.domain.CargoRequestItem;
import ai.yarmook.shipperfinder.domain.Trip;
import ai.yarmook.shipperfinder.domain.TripItem;
import java.io.IOException;
import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostDeleteEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DeleteEventListener implements PostDeleteEventListener {

    private final Logger log = LoggerFactory.getLogger(DeleteEventListener.class);
    private final YTripService tripService;
    private final YCargoRequestService cargoRequestService;

    public DeleteEventListener(YTripService tripService, YCargoRequestService cargoRequestService) {
        this.tripService = tripService;
        this.cargoRequestService = cargoRequestService;
    }

    @Override
    public void onPostDelete(PostDeleteEvent postDeleteEvent) {
        try {
            if (postDeleteEvent.getEntity() instanceof TripItem) {
                syncTrip(((TripItem) postDeleteEvent.getEntity()).getTrip().getId());
            } else if (postDeleteEvent.getEntity() instanceof Trip) {
                syncDeleteTrip(((Trip) postDeleteEvent.getEntity()).getId());
            } else if (postDeleteEvent.getEntity() instanceof CargoRequestItem) {
                syncCargoRequest(((CargoRequestItem) postDeleteEvent.getEntity()).getCargoRequest().getId());
            } else if (postDeleteEvent.getEntity() instanceof CargoRequest) {
                syncDeleteCargoRequest(((CargoRequest) postDeleteEvent.getEntity()).getId());
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

    private void syncDeleteTrip(Long tripId) throws IOException {
        tripService.deleteTrip(tripId);
    }

    private void syncDeleteCargoRequest(Long requestId) throws IOException {
        cargoRequestService.deleteCargoRequest(requestId);
    }

    @Override
    public boolean requiresPostCommitHandling(EntityPersister entityPersister) {
        return false;
    }
}
