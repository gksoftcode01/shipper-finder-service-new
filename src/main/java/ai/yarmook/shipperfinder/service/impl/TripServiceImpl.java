package ai.yarmook.shipperfinder.service.impl;

import ai.yarmook.shipperfinder.domain.Trip;
import ai.yarmook.shipperfinder.repository.TripRepository;
import ai.yarmook.shipperfinder.service.TripService;
import ai.yarmook.shipperfinder.service.dto.TripDTO;
import ai.yarmook.shipperfinder.service.mapper.TripMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.yarmook.shipperfinder.domain.Trip}.
 */
@Service
@Transactional
public class TripServiceImpl implements TripService {

    private static final Logger LOG = LoggerFactory.getLogger(TripServiceImpl.class);

    private final TripRepository tripRepository;

    private final TripMapper tripMapper;

    public TripServiceImpl(TripRepository tripRepository, TripMapper tripMapper) {
        this.tripRepository = tripRepository;
        this.tripMapper = tripMapper;
    }

    @Override
    public TripDTO save(TripDTO tripDTO) {
        LOG.debug("Request to save Trip : {}", tripDTO);
        Trip trip = tripMapper.toEntity(tripDTO);
        trip = tripRepository.save(trip);
        return tripMapper.toDto(trip);
    }

    @Override
    public TripDTO update(TripDTO tripDTO) {
        LOG.debug("Request to update Trip : {}", tripDTO);
        Trip trip = tripMapper.toEntity(tripDTO);
        trip = tripRepository.save(trip);
        return tripMapper.toDto(trip);
    }

    @Override
    public Optional<TripDTO> partialUpdate(TripDTO tripDTO) {
        LOG.debug("Request to partially update Trip : {}", tripDTO);

        return tripRepository
            .findById(tripDTO.getId())
            .map(existingTrip -> {
                tripMapper.partialUpdate(existingTrip, tripDTO);

                return existingTrip;
            })
            .map(tripRepository::save)
            .map(tripMapper::toDto);
    }

    public Page<TripDTO> findAllWithEagerRelationships(Pageable pageable) {
        return tripRepository.findAllWithEagerRelationships(pageable).map(tripMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TripDTO> findOne(Long id) {
        LOG.debug("Request to get Trip : {}", id);
        return tripRepository.findOneWithEagerRelationships(id).map(tripMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Trip : {}", id);
        tripRepository.deleteById(id);
    }
}
