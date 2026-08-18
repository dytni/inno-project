package by.dytni.orderservice.service;


import by.dytni.orderservice.dto.item.Item;
import by.dytni.orderservice.dto.item.ItemMaker;
import by.dytni.orderservice.dto.item.ItemUpdater;

public interface ItemService {
    Item createItem(ItemMaker itemMaker);

    Item updateItem(ItemUpdater itemUpdater, Long itemId);

    Item deleteItem(Long itemId);

    Item getItemById(Long itemId);
}
