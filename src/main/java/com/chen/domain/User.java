package com.chen.domain;

public class User {
    private Long id;
    private String name;
    private String password;
    private Integer gender;
    private Integer age;
    private Integer permissions_id;

    public User() {
    }

    public User(Long id, String name, String password, Integer gender, Integer age, Integer permissions_id) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.gender = gender;
        this.age = age;
        this.permissions_id = permissions_id;
    }

    public User(String name, String password, Integer gender, Integer age) {
        this.name = name;
        this.password = password;
        this.gender = gender;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getPermissions_id() {
        return permissions_id;
    }

    public void setPermissions_id(Integer permissions_id) {
        this.permissions_id = permissions_id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", gender=" + gender +
                ", age=" + age +
                ", permissions_id=" + permissions_id +
                '}';
    }
}
