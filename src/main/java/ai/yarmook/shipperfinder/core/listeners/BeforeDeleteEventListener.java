package ai.yarmook.shipperfinder.core.listeners;

import ai.yarmook.shipperfinder.core.repository.YCargoRequestItemRepository;
import ai.yarmook.shipperfinder.core.repository.YTripItemRepository;
import ai.yarmook.shipperfinder.domain.CargoRequest;
import ai.yarmook.shipperfinder.domain.Trip;
import org.hibernate.event.spi.PreDeleteEvent;
import org.hibernate.event.spi.PreDeleteEventListener;
import org.springframework.stereotype.Component;

@Component
public class BeforeDeleteEventListener implements PreDeleteEventListener {

    private final YTripItemRepository tripItemRepository;
    private final YCargoRequestItemRepository yCargoRequestItemRepository;

    public BeforeDeleteEventListener(YTripItemRepository tripItemRepository, YCargoRequestItemRepository yCargoRequestItemRepository) {
        this.tripItemRepository = tripItemRepository;
        this.yCargoRequestItemRepository = yCargoRequestItemRepository;
    }

    @Override
    public boolean onPreDelete(PreDeleteEvent preDeleteEvent) {
        if (preDeleteEvent.getEntity() instanceof Trip) {
            tripItemRepository.deleteByTripId(((Trip) preDeleteEvent.getEntity()).getId());
        }
        if (preDeleteEvent.getEntity() instanceof CargoRequest) {
            yCargoRequestItemRepository.deleteByRequestId(((CargoRequest) preDeleteEvent.getEntity()).getId());
        }
        return false;
    }
}
