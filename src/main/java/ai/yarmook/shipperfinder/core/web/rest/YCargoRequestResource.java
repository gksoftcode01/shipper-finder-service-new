package ai.yarmook.shipperfinder.core.web.rest;

import ai.yarmook.shipperfinder.core.dto.YCargoRequestDTO;
import ai.yarmook.shipperfinder.core.security.CurrentUser;
import ai.yarmook.shipperfinder.core.service.YCargoRequestService;
import ai.yarmook.shipperfinder.core.util.DateUtil;
import ai.yarmook.shipperfinder.core.web.rest.errors.CargoRequestNoFoundException;
import ai.yarmook.shipperfinder.core.web.rest.errors.YErrorConstants;
import ai.yarmook.shipperfinder.web.rest.errors.BadRequestAlertException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api/y/cargo-requests")
public class YCargoRequestResource {

    private final Logger log = LoggerFactory.getLogger(YCargoRequestResource.class);

    private static final String ENTITY_NAME = "cargoRequest";

    private DateUtil dateUtil;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final YCargoRequestService yCargoRequestService;

    public YCargoRequestResource(YCargoRequestService yCargoRequestService) {
        this.yCargoRequestService = yCargoRequestService;
    }

    @PostMapping("")
    public ResponseEntity<YCargoRequestDTO> createCargoRequest(@RequestBody YCargoRequestDTO cargoRequestDTO)
        throws URISyntaxException, IOException {
        log.debug("REST request to save CargoRequest : {}", cargoRequestDTO);
        if (cargoRequestDTO.getId() != null) {
            throw new BadRequestAlertException("A new cargoRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        YCargoRequestDTO result = yCargoRequestService.save(cargoRequestDTO);
        return ResponseEntity.created(new URI("/api/y/cargo-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/cancel")
    public ResponseEntity<UUID> cancelCargoRequest(@RequestBody String cargoRequestId, @CurrentUser Object user) {
        UUID id = UUID.fromString(cargoRequestId);
        try {
            yCargoRequestService.cancelRequest(id, ((Jwt) user).getClaims().get("appUser").toString());
            return ResponseEntity.ok(id);
        } catch (IOException e) {
            throw new CargoRequestNoFoundException(YErrorConstants.CARGO_REQUEST_NOT_FOUND, cargoRequestId, "CargoRequest", "invalid Id");
        }
    }

    @PostMapping("/complete")
    public ResponseEntity<UUID> completeCargoRequest(@RequestBody String cargoRequestId, @CurrentUser Object user) {
        UUID id = UUID.fromString(cargoRequestId);
        try {
            yCargoRequestService.completeRequest(id, ((Jwt) user).getClaims().get("appUser").toString());
            return ResponseEntity.ok(id);
        } catch (IOException e) {
            throw new CargoRequestNoFoundException(YErrorConstants.CARGO_REQUEST_NOT_FOUND, cargoRequestId, "CargoRequest", "invalid Id");
        }
    }

    @PostMapping("/canAdd")
    public ResponseEntity<Long> canAdd(@CurrentUser Object user) {
        Instant fromDate = DateUtil.getFirstOfDay();
        Instant toDate = DateUtil.getEndOfDay();
        Long countRemain = yCargoRequestService.canAddByUser(
            UUID.fromString(((Jwt) user).getClaims().get("appUser").toString()),
            fromDate,
            toDate
        );
        return ResponseEntity.ok(countRemain);
    }
}
