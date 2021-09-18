package com.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "t_social_net")
public class SocialNetwork {
    public static SocialNetwork GOOGLE = new SocialNetwork("google");
    public static SocialNetwork FACEBOOK = new SocialNetwork("facebook");
    public static SocialNetwork GITHUB = new SocialNetwork("github");

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    public SocialNetwork() {
    }

    public SocialNetwork(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SocialNetwork that = (SocialNetwork) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
