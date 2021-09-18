package com.repo;

import com.entity.SocialNetwork;
import com.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialNetworkRepository extends JpaRepository<SocialNetwork,Long> {
    SocialNetwork findByName(String socialNetworkName);
}
