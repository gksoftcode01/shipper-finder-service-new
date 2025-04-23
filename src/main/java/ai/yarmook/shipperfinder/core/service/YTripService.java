package ai.yarmook.shipperfinder.core.service;

import ai.yarmook.shipperfinder.core.dto.YTripDTO;
import ai.yarmook.shipperfinder.core.web.rest.errors.TripNoFoundException;
import java.io.IOException;
import java.time.Instant;
import java.util.UUID;
import org.springframework.scheduling.annotation.Async;

public interface YTripService {
    Long countTripByUser(UUID userId, Instant fromDate, Instant toDate);

    Long canAddByUser(UUID userId, Instant fromDate, Instant toDate);

    YTripDTO save(YTripDTO tripDto) throws IOException;

    UUID cancelTrip(UUID uuid, String appUserEncId) throws TripNoFoundException, IOException;

    UUID completeTrip(UUID uuid, String appUserEncId) throws TripNoFoundException, IOException;

    @Async
    void syncTrip(Long tripId) throws IOException;

    @Async
    void deleteTrip(Long tripId) throws IOException;
}
