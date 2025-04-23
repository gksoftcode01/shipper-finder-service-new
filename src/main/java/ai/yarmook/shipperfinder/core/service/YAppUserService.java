package ai.yarmook.shipperfinder.core.service;

import ai.yarmook.shipperfinder.service.dto.AppUserDTO;

public interface YAppUserService {
    AppUserDTO partialUpdate(AppUserDTO appUserDTO, String uuid);
}
