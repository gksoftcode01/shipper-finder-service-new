package ai.yarmook.shipperfinder.service.impl;

import ai.yarmook.shipperfinder.domain.ItemType;
import ai.yarmook.shipperfinder.repository.ItemTypeRepository;
import ai.yarmook.shipperfinder.service.ItemTypeService;
import ai.yarmook.shipperfinder.service.dto.ItemTypeDTO;
import ai.yarmook.shipperfinder.service.mapper.ItemTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.yarmook.shipperfinder.domain.ItemType}.
 */
@Service
@Transactional
public class ItemTypeServiceImpl implements ItemTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(ItemTypeServiceImpl.class);

    private final ItemTypeRepository itemTypeRepository;

    private final ItemTypeMapper itemTypeMapper;

    public ItemTypeServiceImpl(ItemTypeRepository itemTypeRepository, ItemTypeMapper itemTypeMapper) {
        this.itemTypeRepository = itemTypeRepository;
        this.itemTypeMapper = itemTypeMapper;
    }

    @Override
    public ItemTypeDTO save(ItemTypeDTO itemTypeDTO) {
        LOG.debug("Request to save ItemType : {}", itemTypeDTO);
        ItemType itemType = itemTypeMapper.toEntity(itemTypeDTO);
        itemType = itemTypeRepository.save(itemType);
        return itemTypeMapper.toDto(itemType);
    }

    @Override
    public ItemTypeDTO update(ItemTypeDTO itemTypeDTO) {
        LOG.debug("Request to update ItemType : {}", itemTypeDTO);
        ItemType itemType = itemTypeMapper.toEntity(itemTypeDTO);
        itemType = itemTypeRepository.save(itemType);
        return itemTypeMapper.toDto(itemType);
    }

    @Override
    public Optional<ItemTypeDTO> partialUpdate(ItemTypeDTO itemTypeDTO) {
        LOG.debug("Request to partially update ItemType : {}", itemTypeDTO);

        return itemTypeRepository
            .findById(itemTypeDTO.getId())
            .map(existingItemType -> {
                itemTypeMapper.partialUpdate(existingItemType, itemTypeDTO);

                return existingItemType;
            })
            .map(itemTypeRepository::save)
            .map(itemTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ItemTypeDTO> findOne(Long id) {
        LOG.debug("Request to get ItemType : {}", id);
        return itemTypeRepository.findById(id).map(itemTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ItemType : {}", id);
        itemTypeRepository.deleteById(id);
    }
}
