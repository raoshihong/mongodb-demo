package com.rao.mongodb.demo.vo;

/**
 * @author raoshihong
 * @date 2021-01-24 10:11
 */
public class OrderCountVo {
    private String custId;
    private Long count;
    private String date;

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
