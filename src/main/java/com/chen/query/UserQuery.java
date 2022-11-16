package com.chen.query;

public class UserQuery {

    private String keyword;
    private Integer gender;
    private Integer max;
    private Integer min;

    public UserQuery() {
    }

    public UserQuery(String keyword, Integer gender, Integer max, Integer min) {
        this.keyword = keyword;
        this.gender = gender;
        this.max = max;
        this.min = min;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    @Override
    public String toString() {
        return "UserQuery{" +
                "keyword='" + keyword + '\'' +
                ", gender=" + gender +
                ", max=" + max +
                ", min=" + min +
                '}';
    }
}
