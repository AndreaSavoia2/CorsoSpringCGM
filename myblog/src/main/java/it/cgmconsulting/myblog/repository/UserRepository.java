package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User,Integer> {

    //select u.id from user_ u where username ='username' or email="email"
    boolean existsByUsernameOrEmail(String usernamer, String email);

    Optional<User> findByUsername(String username);

    boolean existsByEmailAndIdNot(String email, int id);

    @Query(value="SELECT u FROM User u " +
            "LEFT JOIN FETCH u.preferredPosts pp " + // FETCH forza il fetchType a eager
            "WHERE u.id = :userId")
    User getUserWithPreferredPost(int userId);

}
