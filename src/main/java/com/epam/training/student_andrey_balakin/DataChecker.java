package com.epam.training.student_andrey_balakin;

import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class DataChecker {
    public ItemRepo itemRepo;

    public boolean isDataChanged (List<Item> itemsFromUI) {
        var list = itemRepo.getItems();
        if (Objects.equals(list, itemsFromUI)) return false;
        var listSize = list.size();
        if (listSize != itemsFromUI.size()) return true;

        var map = new Long2IntOpenHashMap(listSize);
        for (var item : list) {
            long id    = item.itemId;
            var  count = map.putIfAbsent(id, 1);
            if (count > 0) map.put(id, count + 1);
        }
        for (var item : itemsFromUI) {
            long id    = item.itemId;
            var  count = map.get(id);
            if (count < 1) return true;
            map.put(id, count - 1);
        }
        return false;
    }

    public boolean isDataChanged2 (List<Item> itemsFromUI) {
        var list = itemRepo.getItems();
        if (Objects.equals(list, itemsFromUI)) return false;
        var listSize = list.size();
        if (listSize != itemsFromUI.size()) return true;

        var map = new HashMap<Long, Integer>(listSize);
        for (var item : list) {
            var count = map.putIfAbsent(item.itemId, 1);
            if (count != null) map.put(item.itemId, count + 1);
        }
        for (var item : itemsFromUI) {
            var count = map.get(item.itemId);
            if (count == null || count < 1) return true;
            map.put(item.itemId, count - 1);
        }
        return false;
    }

    // naive method for exploring the problem space
    public boolean isDataChanged3 (List<Item> itemsFromUI) {
        var list = itemRepo.getItems();
        if (Objects.equals(list, itemsFromUI)) return false;
        var listSize = list.size();
        if (listSize != itemsFromUI.size()) return true;

        loop:
        for (var input : itemsFromUI) {
            for (var ir : list) {
                if (input.itemId.equals(ir.itemId)) {
                    continue loop;
                }
            }
            return true;
        }
        return false;
    }

    /* BEGIN Just for reference, no changes here */
    interface ItemRepo {
        List<Item> getItems ();
    }
    /* END. Just for reference, no changes here */
}