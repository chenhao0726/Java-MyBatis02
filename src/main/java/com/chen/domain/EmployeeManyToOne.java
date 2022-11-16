package com.chen.domain;

import java.io.Serializable;

public class EmployeeManyToOne implements Serializable {
    private Long id;
    private String name;
    private  Integer age;
    private DepartmentManyToOne dept;

    public EmployeeManyToOne() {
    }

    public EmployeeManyToOne(String name, Integer age, DepartmentManyToOne dept) {
        this.name = name;
        this.age = age;
        this.dept = dept;
    }

    public EmployeeManyToOne(Long id, String name, Integer age, DepartmentManyToOne dept) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.dept = dept;
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

    public DepartmentManyToOne getDept() {
        return dept;
    }

    public void setDept(DepartmentManyToOne dept) {
        this.dept = dept;
    }

    @Override
    public String toString() {
        return "EmployeeOneToMany{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
