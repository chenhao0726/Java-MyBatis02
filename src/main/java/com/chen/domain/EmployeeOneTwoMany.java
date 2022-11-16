package com.chen.domain;

import java.io.Serializable;

public class EmployeeOneTwoMany implements Serializable {
    private Long id;
    private String name;
    private Integer age;

    public EmployeeOneTwoMany() {
    }

    public EmployeeOneTwoMany(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public EmployeeOneTwoMany(Long id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "EmployeeOneTwoMany{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
