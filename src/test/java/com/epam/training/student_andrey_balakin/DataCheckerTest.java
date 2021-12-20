package com.epam.training.student_andrey_balakin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DataCheckerTest {
    long        seed = 123;
    DataChecker dc;

    @BeforeEach
    void setUp () {
        dc          = new DataChecker();
        dc.itemRepo = () -> {
            var list = new ArrayList<Item>();
            list.add(new Item(0L, "liaw", "miaw"));
            list.add(new Item(1L, "ni", "bu"));
            list.add(new Item(2L, "ni", "mu"));
            return list;
        };
    }

    @Test
    void shouldCheckForClone () {
        var list  = dc.itemRepo.getItems();
        var clone = new ArrayList<Item>();
        for (var el : list) {
            clone.add(new Item(el.itemId, el.name, el.desc));
        }
        assertFalse(dc.isDataChanged(clone));
    }

    @Test
    void shouldDetectRemovedElements () {
        var list = dc.itemRepo.getItems();
        list.remove(new Random(seed).nextInt(list.size()));
        assertTrue(dc.isDataChanged(list));
    }

    @Test
    void shouldDetectDifferentButSameSize () {
        var list = dc.itemRepo.getItems();
        list.remove(new Random(seed).nextInt(list.size()));
        list.add(new Item(123L, "kria", "kria"));
        assertTrue(dc.isDataChanged(list));
    }

    @Test
    void shouldDetectDuplicates () {
        var list = dc.itemRepo.getItems();
        list.remove(new Random(seed).nextInt(list.size() - 1));
        list.add(new Item(2L, "ni", "mu"));
        assertTrue(dc.isDataChanged(list));
    }
}