package com.chen.domain;

import java.io.Serializable;
import java.util.List;

public class DepartmentOneToMany implements Serializable {
    private Long id;
    private String name;
    private List<EmployeeOneTwoMany> emps;

    public DepartmentOneToMany() {
    }

    public DepartmentOneToMany(String name, List<EmployeeOneTwoMany> emps) {
        this.name = name;
        this.emps = emps;
    }

    public DepartmentOneToMany(Long id, String name, List<EmployeeOneTwoMany> emps) {
        this.id = id;
        this.name = name;
        this.emps = emps;
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

    public List<EmployeeOneTwoMany> getEmps() {
        return emps;
    }

    public void setEmps(List<EmployeeOneTwoMany> emps) {
        this.emps = emps;
    }

    @Override
    public String toString() {
        return "DepartmentOneToMany{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", emps=" + emps +
                '}';
    }
}
