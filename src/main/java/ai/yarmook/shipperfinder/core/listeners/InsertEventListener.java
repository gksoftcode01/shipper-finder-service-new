package ai.yarmook.shipperfinder.core.listeners;

import ai.yarmook.shipperfinder.core.service.YCargoRequestService;
import ai.yarmook.shipperfinder.core.service.YTripService;
import ai.yarmook.shipperfinder.domain.CargoRequest;
import ai.yarmook.shipperfinder.domain.CargoRequestItem;
import ai.yarmook.shipperfinder.domain.Trip;
import ai.yarmook.shipperfinder.domain.TripItem;
import java.io.IOException;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class InsertEventListener implements PostInsertEventListener {

    private final Logger log = LoggerFactory.getLogger(InsertEventListener.class);
    private final YTripService tripService;
    private final YCargoRequestService cargoRequestService;

    public InsertEventListener(YTripService tripService, YCargoRequestService cargoRequestService) {
        this.tripService = tripService;
        this.cargoRequestService = cargoRequestService;
    }

    @Override
    public void onPostInsert(PostInsertEvent postInsertEvent) {
        try {
            if (postInsertEvent.getEntity() instanceof TripItem) {
                syncTrip(((TripItem) postInsertEvent.getEntity()).getTrip().getId());
            } else if (postInsertEvent.getEntity() instanceof Trip) {
                syncTrip(((Trip) postInsertEvent.getEntity()).getId());
            } else if (postInsertEvent.getEntity() instanceof CargoRequestItem) {
                //                syncCargoRequest(((CargoRequestItem) postInsertEvent.getEntity()).getCargoRequest().getId());
            } else if (postInsertEvent.getEntity() instanceof CargoRequest) {
                //                syncCargoRequest(((CargoRequest) postInsertEvent.getEntity()).getId());
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
