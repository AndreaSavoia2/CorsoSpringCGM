package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {

    //select u.id from user_ u where username ='username' or email="email"
    boolean existsByUsernameOrEmail(String usernamer, String email);

    Optional<User> findByUsername(String username);

    boolean existsByEmailAndIdNot(String email, int id);
}
