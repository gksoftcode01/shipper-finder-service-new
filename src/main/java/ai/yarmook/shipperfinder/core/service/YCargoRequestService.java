package ai.yarmook.shipperfinder.core.service;

import ai.yarmook.shipperfinder.core.dto.YCargoRequestDTO;
import java.io.IOException;
import java.time.Instant;
import java.util.UUID;
import org.springframework.scheduling.annotation.Async;

public interface YCargoRequestService {
    Long countRequestByUser(UUID userId, Instant fromDate, Instant toDate);

    Long canAddByUser(UUID userId, Instant fromDate, Instant toDate);

    YCargoRequestDTO save(YCargoRequestDTO cargoRequestDTO) throws IOException;

    UUID cancelRequest(UUID uuid, String appUserEncId) throws IOException;

    UUID completeRequest(UUID uuid, String appUserEncId) throws IOException;

    @Async
    void syncCargoRequest(Long requestId) throws IOException;

    @Async
    void deleteCargoRequest(Long requestId) throws IOException;
}
