package com.spring.todo.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "authorities")
public class Authorities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "authority")
    private String authority;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "authorities",fetch = FetchType.EAGER)
    private Set<User> users = new HashSet<>();

    public Authorities() {
    }

    public Authorities(String authority) {
        this.authority = authority;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public void addUser(User user) {
        users.add(user);
    }

    @Override
    public String toString() {
        return "Authorities{" +
                "id=" + id +
                ", authority='" + authority + '\'' +
                '}';
    }
}
