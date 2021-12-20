package com.epam.training.student_andrey_balakin;

public class Item {
    public Long   itemId;
    public String name;
    public String desc;

    public Item (Long itemId, String name, String desc) {
        this.itemId = itemId;
        this.name   = name;
        this.desc   = desc;
    }
}