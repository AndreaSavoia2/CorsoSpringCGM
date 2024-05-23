package it.cgmconsulting.msauth.repository;

import it.cgmconsulting.msauth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {

    boolean existsByUsernameOrEmail(String username, String email);

}
