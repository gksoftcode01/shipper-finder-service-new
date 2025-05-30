package ai.yarmook.shipperfinder.service.impl;

import ai.yarmook.shipperfinder.domain.CargoRequestItem;
import ai.yarmook.shipperfinder.repository.CargoRequestItemRepository;
import ai.yarmook.shipperfinder.service.CargoRequestItemService;
import ai.yarmook.shipperfinder.service.dto.CargoRequestItemDTO;
import ai.yarmook.shipperfinder.service.mapper.CargoRequestItemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.yarmook.shipperfinder.domain.CargoRequestItem}.
 */
@Service
@Transactional
public class CargoRequestItemServiceImpl implements CargoRequestItemService {

    private static final Logger LOG = LoggerFactory.getLogger(CargoRequestItemServiceImpl.class);

    private final CargoRequestItemRepository cargoRequestItemRepository;

    private final CargoRequestItemMapper cargoRequestItemMapper;

    public CargoRequestItemServiceImpl(
        CargoRequestItemRepository cargoRequestItemRepository,
        CargoRequestItemMapper cargoRequestItemMapper
    ) {
        this.cargoRequestItemRepository = cargoRequestItemRepository;
        this.cargoRequestItemMapper = cargoRequestItemMapper;
    }

    @Override
    public CargoRequestItemDTO save(CargoRequestItemDTO cargoRequestItemDTO) {
        LOG.debug("Request to save CargoRequestItem : {}", cargoRequestItemDTO);
        CargoRequestItem cargoRequestItem = cargoRequestItemMapper.toEntity(cargoRequestItemDTO);
        cargoRequestItem = cargoRequestItemRepository.save(cargoRequestItem);
        return cargoRequestItemMapper.toDto(cargoRequestItem);
    }

    @Override
    public CargoRequestItemDTO update(CargoRequestItemDTO cargoRequestItemDTO) {
        LOG.debug("Request to update CargoRequestItem : {}", cargoRequestItemDTO);
        CargoRequestItem cargoRequestItem = cargoRequestItemMapper.toEntity(cargoRequestItemDTO);
        cargoRequestItem = cargoRequestItemRepository.save(cargoRequestItem);
        return cargoRequestItemMapper.toDto(cargoRequestItem);
    }

    @Override
    public Optional<CargoRequestItemDTO> partialUpdate(CargoRequestItemDTO cargoRequestItemDTO) {
        LOG.debug("Request to partially update CargoRequestItem : {}", cargoRequestItemDTO);

        return cargoRequestItemRepository
            .findById(cargoRequestItemDTO.getId())
            .map(existingCargoRequestItem -> {
                cargoRequestItemMapper.partialUpdate(existingCargoRequestItem, cargoRequestItemDTO);

                return existingCargoRequestItem;
            })
            .map(cargoRequestItemRepository::save)
            .map(cargoRequestItemMapper::toDto);
    }

    public Page<CargoRequestItemDTO> findAllWithEagerRelationships(Pageable pageable) {
        return cargoRequestItemRepository.findAllWithEagerRelationships(pageable).map(cargoRequestItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CargoRequestItemDTO> findOne(Long id) {
        LOG.debug("Request to get CargoRequestItem : {}", id);
        return cargoRequestItemRepository.findOneWithEagerRelationships(id).map(cargoRequestItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete CargoRequestItem : {}", id);
        cargoRequestItemRepository.deleteById(id);
    }
}
