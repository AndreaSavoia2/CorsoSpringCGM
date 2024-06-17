package it.cgmconsulting.msauth.repository;

import it.cgmconsulting.msauth.entity.Role;
import it.cgmconsulting.msauth.entity.User;
import it.cgmconsulting.msauth.payload.response.SimpleUserResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User,Integer> {

    boolean existsByUsernameOrEmail(String username, String email);
    Optional<User> findByUsername(String username);

    @Query(value = "SELECT u.username FROM User u WHERE u.id = :id")
    String getUsername(int id);

    @Query(value = "SELECT new it.cgmconsulting.msauth.payload.response.SimpleUserResponse(" +
            "u.id, " +
            "u.username" +
            ") FROM User u " +
            "WHERE u.id in (:ids)")
    Set<SimpleUserResponse> getSimpleUsers(Set<Integer> ids);

}
