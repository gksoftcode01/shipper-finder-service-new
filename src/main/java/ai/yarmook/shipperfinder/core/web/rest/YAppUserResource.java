package ai.yarmook.shipperfinder.core.web.rest;

import ai.yarmook.shipperfinder.core.dto.vm.UserDeviceInfo;
import ai.yarmook.shipperfinder.core.security.CurrentUser;
import ai.yarmook.shipperfinder.core.service.YAppUserDeviceService;
import ai.yarmook.shipperfinder.core.service.YAppUserService;
import ai.yarmook.shipperfinder.service.dto.AppUserDTO;
import ai.yarmook.shipperfinder.service.dto.AppUserDeviceDTO;
import ai.yarmook.shipperfinder.web.rest.errors.BadRequestAlertException;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api/y/app-users")
public class YAppUserResource {

    private final YAppUserDeviceService appUserDeviceService;

    private final Logger log = LoggerFactory.getLogger(YAppUserResource.class);

    private static final String ENTITY_NAME = "appUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final YAppUserService yAppUserService;

    public YAppUserResource(YAppUserDeviceService appUserDeviceService, YAppUserService yAppUserService) {
        this.appUserDeviceService = appUserDeviceService;
        this.yAppUserService = yAppUserService;
    }

    @PostMapping(value = "/{uuid}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AppUserDTO> partialUpdateAppUser(
        @PathVariable(value = "uuid", required = false) final String uuid,
        @RequestBody AppUserDTO appUserDTO,
        @CurrentUser Object currentUser
    ) throws URISyntaxException {
        log.debug("REST request to partial update AppUser partially : {}, {}", uuid, appUserDTO);
        log.info(currentUser.toString());
        if (!Objects.equals(uuid, appUserDTO.getEncId().toString())) {
            throw new BadRequestAlertException("Invalid Enc ID", ENTITY_NAME, "id invalid");
        }
        Optional<AppUserDTO> result = Optional.of(yAppUserService.partialUpdate(appUserDTO, uuid));
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appUserDTO.getId().toString())
        );
    }

    @PostMapping(value = "/logged", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AppUserDeviceDTO> userLogged(@RequestBody UserDeviceInfo userDeviceInfo, @CurrentUser Object currentUser)
        throws URISyntaxException {
        log.info(currentUser.toString());
        if (!Objects.equals(((Jwt) currentUser).getClaims().get("appUser").toString(), userDeviceInfo.getUserId())) {
            throw new BadRequestAlertException("Invalid Enc ID", ENTITY_NAME, "id invalid");
        }
        AppUserDeviceDTO appUserDeviceDTO = appUserDeviceService.userLogged(
            userDeviceInfo.getUserId(),
            userDeviceInfo.getDeviceCode(),
            userDeviceInfo.getNotificationToken()
        );
        return ResponseEntity.ok(appUserDeviceDTO);
    }
}
