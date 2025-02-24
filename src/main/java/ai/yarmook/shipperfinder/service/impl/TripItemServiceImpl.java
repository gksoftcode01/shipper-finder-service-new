package ai.yarmook.shipperfinder.service.impl;

import ai.yarmook.shipperfinder.domain.TripItem;
import ai.yarmook.shipperfinder.repository.TripItemRepository;
import ai.yarmook.shipperfinder.service.TripItemService;
import ai.yarmook.shipperfinder.service.dto.TripItemDTO;
import ai.yarmook.shipperfinder.service.mapper.TripItemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.yarmook.shipperfinder.domain.TripItem}.
 */
@Service
@Transactional
public class TripItemServiceImpl implements TripItemService {

    private static final Logger LOG = LoggerFactory.getLogger(TripItemServiceImpl.class);

    private final TripItemRepository tripItemRepository;

    private final TripItemMapper tripItemMapper;

    public TripItemServiceImpl(TripItemRepository tripItemRepository, TripItemMapper tripItemMapper) {
        this.tripItemRepository = tripItemRepository;
        this.tripItemMapper = tripItemMapper;
    }

    @Override
    public TripItemDTO save(TripItemDTO tripItemDTO) {
        LOG.debug("Request to save TripItem : {}", tripItemDTO);
        TripItem tripItem = tripItemMapper.toEntity(tripItemDTO);
        tripItem = tripItemRepository.save(tripItem);
        return tripItemMapper.toDto(tripItem);
    }

    @Override
    public TripItemDTO update(TripItemDTO tripItemDTO) {
        LOG.debug("Request to update TripItem : {}", tripItemDTO);
        TripItem tripItem = tripItemMapper.toEntity(tripItemDTO);
        tripItem = tripItemRepository.save(tripItem);
        return tripItemMapper.toDto(tripItem);
    }

    @Override
    public Optional<TripItemDTO> partialUpdate(TripItemDTO tripItemDTO) {
        LOG.debug("Request to partially update TripItem : {}", tripItemDTO);

        return tripItemRepository
            .findById(tripItemDTO.getId())
            .map(existingTripItem -> {
                tripItemMapper.partialUpdate(existingTripItem, tripItemDTO);

                return existingTripItem;
            })
            .map(tripItemRepository::save)
            .map(tripItemMapper::toDto);
    }

    public Page<TripItemDTO> findAllWithEagerRelationships(Pageable pageable) {
        return tripItemRepository.findAllWithEagerRelationships(pageable).map(tripItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TripItemDTO> findOne(Long id) {
        LOG.debug("Request to get TripItem : {}", id);
        return tripItemRepository.findOneWithEagerRelationships(id).map(tripItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete TripItem : {}", id);
        tripItemRepository.deleteById(id);
    }
}
