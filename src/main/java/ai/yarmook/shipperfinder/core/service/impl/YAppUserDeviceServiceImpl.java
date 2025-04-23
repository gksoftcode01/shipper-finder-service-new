package ai.yarmook.shipperfinder.core.service.impl;

import ai.yarmook.shipperfinder.core.service.YAppUserDeviceService;
import ai.yarmook.shipperfinder.domain.AppUserDevice;
import ai.yarmook.shipperfinder.repository.AppUserDeviceRepository;
import ai.yarmook.shipperfinder.service.AppUserDeviceQueryService;
import ai.yarmook.shipperfinder.service.criteria.AppUserDeviceCriteria;
import ai.yarmook.shipperfinder.service.dto.AppUserDeviceDTO;
import ai.yarmook.shipperfinder.service.mapper.AppUserDeviceMapper;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.UUIDFilter;

@Service
public class YAppUserDeviceServiceImpl implements YAppUserDeviceService {

    private final AppUserDeviceQueryService appUserDeviceQueryService;
    private final AppUserDeviceRepository appUserDeviceRepository;
    private final AppUserDeviceMapper appUserDeviceMapper;

    public YAppUserDeviceServiceImpl(
        AppUserDeviceQueryService appUserDeviceQueryService,
        AppUserDeviceRepository appUserDeviceRepository,
        AppUserDeviceMapper appUserDeviceMapper
    ) {
        this.appUserDeviceQueryService = appUserDeviceQueryService;
        this.appUserDeviceRepository = appUserDeviceRepository;
        this.appUserDeviceMapper = appUserDeviceMapper;
    }

    @Override
    public AppUserDeviceDTO userLogged(String userId, String deviceCode, String notificationToken) {
        AppUserDeviceCriteria criteria = new AppUserDeviceCriteria();
        UUIDFilter uuidFilter = new UUIDFilter();
        uuidFilter.setEquals(UUID.fromString(userId));
        StringFilter deviceCodeFilter = new StringFilter();
        deviceCodeFilter.setEquals(deviceCode);
        StringFilter notificationTokenFilter = new StringFilter();
        notificationTokenFilter.setEquals(notificationToken);
        criteria.setDeviceCode(deviceCodeFilter);
        criteria.setUserEncId(uuidFilter);
        criteria.setNotificationToken(notificationTokenFilter);
        List<AppUserDeviceDTO> devices = appUserDeviceQueryService.findByCriteria(criteria, Pageable.unpaged()).toList();
        if (!devices.isEmpty()) {
            AppUserDeviceDTO userDeviceDTO = devices.get(0);
            userDeviceDTO.setActive(true);
            userDeviceDTO.setLastLogin(Instant.now());
            AppUserDevice userDevice = appUserDeviceRepository.findById(userDeviceDTO.getId()).orElse(null);
            if (userDevice != null) {
                appUserDeviceMapper.partialUpdate(userDevice, userDeviceDTO);
            }
            appUserDeviceRepository.saveAndFlush(userDevice);
            return appUserDeviceMapper.toDto(userDevice);
        } else {
            AppUserDevice userDevice = new AppUserDevice();
            userDevice.setActive(true);
            userDevice.setLastLogin(Instant.now());
            userDevice.setDeviceCode(deviceCode);
            userDevice.setNotificationToken(notificationToken);
            userDevice.setUserEncId(UUID.fromString(userId));
            appUserDeviceRepository.saveAndFlush(userDevice);
            return appUserDeviceMapper.toDto(userDevice);
        }
    }

    @Override
    public String getUserNotificationToken(String userId) {
        AppUserDeviceCriteria criteria = new AppUserDeviceCriteria();
        UUIDFilter uuidFilter = new UUIDFilter();
        uuidFilter.setEquals(UUID.fromString(userId));
        criteria.setUserEncId(uuidFilter);
        List<AppUserDeviceDTO> devices = appUserDeviceQueryService.findByCriteria(criteria, Pageable.unpaged()).toList();
        if (!devices.isEmpty()) {
            return devices.get(0).getNotificationToken();
        }
        return null;
    }
}
