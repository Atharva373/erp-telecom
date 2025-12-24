package com.atharva.erp_telecom.repository.auth;

import com.atharva.erp_telecom.entity.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUserName(String userName);

    // Added new helper method for already exiting users.
    boolean existsByUserName(String userName);
}
