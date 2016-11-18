package com.example.da.lab8;

/**
 * Created by Da on 2016/11/17.
 */
public class Member {
    private String name;
    private String birth;
    private String gift;
    //使用id来标识每一条记录,这样就算名字一样删除或者更新的时候就不会批量删除或被更新
    private int id;
    public Member(){};
    public Member(String name, String birth, String gift) {
        this.name = name;
        this.birth = birth;
        this.gift = gift;
    }
    public String getBirth() {
        return birth;
    }
    public String getName() {
        return name;
    }
    public String getGift() {
        return gift;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setBirth(String birth) {
        this.birth = birth;
    }
    public void setGift(String gift) {
        this.gift = gift;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
