package com.epam.training.student_andrey_balakin;

import java.util.Objects;

public class Item {
    public Long   itemId;
    public String name;
    public String desc;


    public Item (Long itemId, String name, String desc) {
        this.itemId = itemId;
        this.name   = name;
        this.desc   = desc;
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof Item item)) return false;
        return Objects.equals(itemId, item.itemId) && Objects.equals(name, item.name) && Objects.equals(desc, item.desc);
    }

    @Override
    public int hashCode () {
        return Objects.hash(itemId, name, desc);
    }
}