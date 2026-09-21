package io.virinchi.hamrokitab2.Repository;

import io.virinchi.hamrokitab2.Model.UserTbl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<UserTbl, Integer> {

    Optional<UserTbl> findByEmail(String email);

    Optional<UserTbl> findByVerificationToken(String verificationToken);

    boolean existsByEmail(String email);


}
