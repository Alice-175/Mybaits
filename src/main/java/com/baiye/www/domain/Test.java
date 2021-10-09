package com.baiye.www.domain;

/**
 * @author GUOZHIPENG
 * @version 1.0
 * @date 2021/9/2 15:20
 */
public class Test {
    private long id;
    private String name;

    public Test(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Test{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
