package ai.yarmook.shipperfinder.core.service.impl;

import ai.yarmook.shipperfinder.core.service.YAppUserService;
import ai.yarmook.shipperfinder.core.web.rest.errors.AppUserNotFound;
import ai.yarmook.shipperfinder.service.AppUserQueryService;
import ai.yarmook.shipperfinder.service.AppUserService;
import ai.yarmook.shipperfinder.service.criteria.AppUserCriteria;
import ai.yarmook.shipperfinder.service.dto.AppUserDTO;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tech.jhipster.service.filter.UUIDFilter;

@Service
public class YAppUserServiceImpl implements YAppUserService {

    private final AppUserQueryService appUserQueryService;
    private final AppUserService appUserService;

    public YAppUserServiceImpl(AppUserQueryService appUserQueryService, AppUserService appUserService) {
        this.appUserQueryService = appUserQueryService;
        this.appUserService = appUserService;
    }

    public AppUserDTO partialUpdate(AppUserDTO appUserDTO, String uuid) {
        AppUserCriteria criteria = new AppUserCriteria();
        UUIDFilter filter = new UUIDFilter();
        filter.setEquals(UUID.fromString(uuid));
        criteria.setEncId(filter);
        List<AppUserDTO> list = appUserQueryService.findByCriteria(criteria, Pageable.unpaged()).toList();
        if (!list.isEmpty()) {
            AppUserDTO dto = list.get(0);
            dto.setBirthDate(appUserDTO.getBirthDate());
            dto.setFirstName(appUserDTO.getFirstName());
            dto.setGender(appUserDTO.getGender());
            dto.setFullName(appUserDTO.getFullName());
            dto.setLastName(appUserDTO.getLastName());
            dto.setLocation(appUserDTO.getLocation());
            dto.setPhoneNumber(appUserDTO.getPhoneNumber());
            appUserService.partialUpdate(dto);
            return dto;
        } else {
            throw new AppUserNotFound("This user is not in our database information", "AppUser", "notFound");
        }
    }
}
