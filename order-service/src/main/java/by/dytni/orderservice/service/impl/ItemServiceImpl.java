package by.dytni.orderservice.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import by.dytni.orderservice.dto.item.Item;
import by.dytni.orderservice.dto.item.ItemMaker;
import by.dytni.orderservice.dto.item.ItemUpdater;
import by.dytni.orderservice.exceptions.ItemNotFoundException;
import by.dytni.orderservice.mapper.ItemMapper;
import by.dytni.orderservice.repository.ItemRepository;
import by.dytni.orderservice.repository.entity.ItemEntity;
import by.dytni.orderservice.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemMapper itemMapper;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public Item createItem(ItemMaker itemMaker) {
        ItemEntity itemEntity = itemMapper.dtoToEntity(itemMaker);
        return itemMapper.entityToDto(itemRepository.save(itemEntity));
    }

    @Override
    @Transactional
    public Item updateItem(ItemUpdater itemUpdater, Long itemId) {
        ItemEntity itemEntity = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException(itemId));
        ItemEntity updated = itemMapper.updateEntity(itemEntity, itemUpdater);
        return itemMapper.entityToDto(updated);
    }

    @Override
    @Transactional
    public Item deleteItem(Long itemId) {
        ItemEntity itemEntity = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException(itemId));
        itemRepository.delete(itemEntity);
        return itemMapper.entityToDto(itemEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Item getItemById(Long itemId) {
        return itemMapper.entityToDto(
                itemRepository.findById(itemId)
                        .orElseThrow(() -> new ItemNotFoundException(itemId))
        );
    }
}
