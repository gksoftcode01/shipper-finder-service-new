package ai.yarmook.shipperfinder.service.impl;

import ai.yarmook.shipperfinder.domain.AppUserDevice;
import ai.yarmook.shipperfinder.repository.AppUserDeviceRepository;
import ai.yarmook.shipperfinder.service.AppUserDeviceService;
import ai.yarmook.shipperfinder.service.dto.AppUserDeviceDTO;
import ai.yarmook.shipperfinder.service.mapper.AppUserDeviceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.yarmook.shipperfinder.domain.AppUserDevice}.
 */
@Service
@Transactional
public class AppUserDeviceServiceImpl implements AppUserDeviceService {

    private static final Logger LOG = LoggerFactory.getLogger(AppUserDeviceServiceImpl.class);

    private final AppUserDeviceRepository appUserDeviceRepository;

    private final AppUserDeviceMapper appUserDeviceMapper;

    public AppUserDeviceServiceImpl(AppUserDeviceRepository appUserDeviceRepository, AppUserDeviceMapper appUserDeviceMapper) {
        this.appUserDeviceRepository = appUserDeviceRepository;
        this.appUserDeviceMapper = appUserDeviceMapper;
    }

    @Override
    public AppUserDeviceDTO save(AppUserDeviceDTO appUserDeviceDTO) {
        LOG.debug("Request to save AppUserDevice : {}", appUserDeviceDTO);
        AppUserDevice appUserDevice = appUserDeviceMapper.toEntity(appUserDeviceDTO);
        appUserDevice = appUserDeviceRepository.save(appUserDevice);
        return appUserDeviceMapper.toDto(appUserDevice);
    }

    @Override
    public AppUserDeviceDTO update(AppUserDeviceDTO appUserDeviceDTO) {
        LOG.debug("Request to update AppUserDevice : {}", appUserDeviceDTO);
        AppUserDevice appUserDevice = appUserDeviceMapper.toEntity(appUserDeviceDTO);
        appUserDevice = appUserDeviceRepository.save(appUserDevice);
        return appUserDeviceMapper.toDto(appUserDevice);
    }

    @Override
    public Optional<AppUserDeviceDTO> partialUpdate(AppUserDeviceDTO appUserDeviceDTO) {
        LOG.debug("Request to partially update AppUserDevice : {}", appUserDeviceDTO);

        return appUserDeviceRepository
            .findById(appUserDeviceDTO.getId())
            .map(existingAppUserDevice -> {
                appUserDeviceMapper.partialUpdate(existingAppUserDevice, appUserDeviceDTO);

                return existingAppUserDevice;
            })
            .map(appUserDeviceRepository::save)
            .map(appUserDeviceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AppUserDeviceDTO> findOne(Long id) {
        LOG.debug("Request to get AppUserDevice : {}", id);
        return appUserDeviceRepository.findById(id).map(appUserDeviceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete AppUserDevice : {}", id);
        appUserDeviceRepository.deleteById(id);
    }
}
