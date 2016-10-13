package com.le.matrix.template.model;

import java.io.Serializable;

/**
 * Created by linzhanbo on 2016/10/10.
 */
public class Grage implements Serializable {

    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
