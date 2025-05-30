package ai.yarmook.shipperfinder.service.impl;

import ai.yarmook.shipperfinder.domain.ShowNumberHistory;
import ai.yarmook.shipperfinder.repository.ShowNumberHistoryRepository;
import ai.yarmook.shipperfinder.service.ShowNumberHistoryService;
import ai.yarmook.shipperfinder.service.dto.ShowNumberHistoryDTO;
import ai.yarmook.shipperfinder.service.mapper.ShowNumberHistoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.yarmook.shipperfinder.domain.ShowNumberHistory}.
 */
@Service
@Transactional
public class ShowNumberHistoryServiceImpl implements ShowNumberHistoryService {

    private static final Logger LOG = LoggerFactory.getLogger(ShowNumberHistoryServiceImpl.class);

    private final ShowNumberHistoryRepository showNumberHistoryRepository;

    private final ShowNumberHistoryMapper showNumberHistoryMapper;

    public ShowNumberHistoryServiceImpl(
        ShowNumberHistoryRepository showNumberHistoryRepository,
        ShowNumberHistoryMapper showNumberHistoryMapper
    ) {
        this.showNumberHistoryRepository = showNumberHistoryRepository;
        this.showNumberHistoryMapper = showNumberHistoryMapper;
    }

    @Override
    public ShowNumberHistoryDTO save(ShowNumberHistoryDTO showNumberHistoryDTO) {
        LOG.debug("Request to save ShowNumberHistory : {}", showNumberHistoryDTO);
        ShowNumberHistory showNumberHistory = showNumberHistoryMapper.toEntity(showNumberHistoryDTO);
        showNumberHistory = showNumberHistoryRepository.save(showNumberHistory);
        return showNumberHistoryMapper.toDto(showNumberHistory);
    }

    @Override
    public ShowNumberHistoryDTO update(ShowNumberHistoryDTO showNumberHistoryDTO) {
        LOG.debug("Request to update ShowNumberHistory : {}", showNumberHistoryDTO);
        ShowNumberHistory showNumberHistory = showNumberHistoryMapper.toEntity(showNumberHistoryDTO);
        showNumberHistory = showNumberHistoryRepository.save(showNumberHistory);
        return showNumberHistoryMapper.toDto(showNumberHistory);
    }

    @Override
    public Optional<ShowNumberHistoryDTO> partialUpdate(ShowNumberHistoryDTO showNumberHistoryDTO) {
        LOG.debug("Request to partially update ShowNumberHistory : {}", showNumberHistoryDTO);

        return showNumberHistoryRepository
            .findById(showNumberHistoryDTO.getId())
            .map(existingShowNumberHistory -> {
                showNumberHistoryMapper.partialUpdate(existingShowNumberHistory, showNumberHistoryDTO);

                return existingShowNumberHistory;
            })
            .map(showNumberHistoryRepository::save)
            .map(showNumberHistoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ShowNumberHistoryDTO> findOne(Long id) {
        LOG.debug("Request to get ShowNumberHistory : {}", id);
        return showNumberHistoryRepository.findById(id).map(showNumberHistoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ShowNumberHistory : {}", id);
        showNumberHistoryRepository.deleteById(id);
    }
}
