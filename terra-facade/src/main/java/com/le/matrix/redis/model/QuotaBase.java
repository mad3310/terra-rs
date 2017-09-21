package com.le.matrix.redis.model;

import com.letv.common.model.BaseModel;

public class QuotaBase extends BaseModel{

	private static final long serialVersionUID = 8524445473814088752L;

	private String name;//产品名称，例如：REDIS、GCE

    private String type;//产品类型，例如：num、cpu

    private Long value;//值大小

    private String descn;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public String getDescn() {
        return descn;
    }

    public void setDescn(String descn) {
        this.descn = descn == null ? null : descn.trim();
    }

}