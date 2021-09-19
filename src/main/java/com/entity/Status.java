package com.entity;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "t_status")
public class Status implements GrantedAuthority {
    public static final Status NORMAL = new Status("normal");
    public static final Status BLOCK = new Status("block");

    @Id
    private Long id;
    private String name;

    public Status() {
    }

    public Status(Long id) {
        this.id = id;
    }

    public Status(String name) {
        this.name = name;
    }

    public Status(Long id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public String getAuthority() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Status status = (Status) o;
        return name.equals(status.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}