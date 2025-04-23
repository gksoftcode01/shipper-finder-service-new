package ai.yarmook.shipperfinder.service.impl;

import ai.yarmook.shipperfinder.domain.OtpLog;
import ai.yarmook.shipperfinder.repository.OtpLogRepository;
import ai.yarmook.shipperfinder.service.OtpLogService;
import ai.yarmook.shipperfinder.service.dto.OtpLogDTO;
import ai.yarmook.shipperfinder.service.mapper.OtpLogMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.yarmook.shipperfinder.domain.OtpLog}.
 */
@Service
@Transactional
public class OtpLogServiceImpl implements OtpLogService {

    private static final Logger LOG = LoggerFactory.getLogger(OtpLogServiceImpl.class);

    private final OtpLogRepository otpLogRepository;

    private final OtpLogMapper otpLogMapper;

    public OtpLogServiceImpl(OtpLogRepository otpLogRepository, OtpLogMapper otpLogMapper) {
        this.otpLogRepository = otpLogRepository;
        this.otpLogMapper = otpLogMapper;
    }

    @Override
    public OtpLogDTO save(OtpLogDTO otpLogDTO) {
        LOG.debug("Request to save OtpLog : {}", otpLogDTO);
        OtpLog otpLog = otpLogMapper.toEntity(otpLogDTO);
        otpLog = otpLogRepository.save(otpLog);
        return otpLogMapper.toDto(otpLog);
    }

    @Override
    public OtpLogDTO update(OtpLogDTO otpLogDTO) {
        LOG.debug("Request to update OtpLog : {}", otpLogDTO);
        OtpLog otpLog = otpLogMapper.toEntity(otpLogDTO);
        otpLog = otpLogRepository.save(otpLog);
        return otpLogMapper.toDto(otpLog);
    }

    @Override
    public Optional<OtpLogDTO> partialUpdate(OtpLogDTO otpLogDTO) {
        LOG.debug("Request to partially update OtpLog : {}", otpLogDTO);

        return otpLogRepository
            .findById(otpLogDTO.getId())
            .map(existingOtpLog -> {
                otpLogMapper.partialUpdate(existingOtpLog, otpLogDTO);

                return existingOtpLog;
            })
            .map(otpLogRepository::save)
            .map(otpLogMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OtpLogDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all OtpLogs");
        return otpLogRepository.findAll(pageable).map(otpLogMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OtpLogDTO> findOne(Long id) {
        LOG.debug("Request to get OtpLog : {}", id);
        return otpLogRepository.findById(id).map(otpLogMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete OtpLog : {}", id);
        otpLogRepository.deleteById(id);
    }
}
