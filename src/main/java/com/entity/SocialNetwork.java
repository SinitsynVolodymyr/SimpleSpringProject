package com.entity;

import javax.persistence.*;

@Entity
@Table(name = "t_social_net")
public class SocialNetwork {

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

}
