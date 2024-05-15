package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.entity.Rating;
import it.cgmconsulting.myblog.entity.RatingId;
import it.cgmconsulting.myblog.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RatingRepository extends JpaRepository<Rating, RatingId> {

    /*@Query("SELECT r from Rating r where r.ratingId.userId = :user and r.ratingId.postId = :post")
    Rating getRating(User user, Post post);*/

    @Query(value = "select rate from rating WHERE post_id = :postId AND user_id = :userId", nativeQuery = true)
    Byte getRating(int postId, int userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM rating WHERE post_id = :postId AND user_id = :userId", nativeQuery = true)
    void deleteRating(int postId, int userId);
}
