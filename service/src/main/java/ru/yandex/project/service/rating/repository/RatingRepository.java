package ru.yandex.project.service.rating.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.yandex.project.service.rating.model.Rating;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface RatingRepository extends PagingAndSortingRepository<Rating, Long> {
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO USER_LIKE_EVENT_TABLE(EVENT_ID, USER_ID, IS_LIKE)" +
            " VALUES (:eventId, :userId, TRUE);" +
            "UPDATE LIKE_DISLIKE_TOTAL_TABLE " +
            "SET LIKE_TOTAL=LIKE_TOTAL+1 " +
            "WHERE EVENT_ID=:eventId", nativeQuery = true)
    void likeEvent(@Param("eventId") Long eventId, @Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO USER_LIKE_EVENT_TABLE(EVENT_ID, USER_ID, IS_LIKE)" +
            " VALUES (:eventId, :userId, FALSE);" +
            "UPDATE LIKE_DISLIKE_TOTAL_TABLE " +
            "SET DISLIKE_TOTAL=DISLIKE_TOTAL+1 " +
            "WHERE EVENT_ID=:eventId", nativeQuery = true)
    void dislikeEvent(@Param("eventId") Long eventId, @Param("userId") Long userId);

    @Query(value = "SELECT COUNT(EVENT_ID) FROM USER_LIKE_EVENT_TABLE" +
            " WHERE EVENT_ID=:eventId AND IS_LIKE=TRUE", nativeQuery = true)
    int getLikesOfEvent(@Param("eventId") Long eventId);

    @Query(value = "SELECT COUNT(EVENT_ID) FROM USER_LIKE_EVENT_TABLE" +
            " WHERE EVENT_ID=:eventId AND IS_LIKE=FALSE", nativeQuery = true)
    int getDislikesOfEvent(@Param("eventId") Long eventId);

    @Query(value = "SELECT COUNT(EVENT_ID) FROM USER_LIKE_EVENT_TABLE" +
            " WHERE USER_ID=:userId AND EVENT_ID=:eventId AND IS_LIKE=TRUE", nativeQuery = true)
    int checkLike(@Param("eventId") Long eventId, @Param("userId") Long userId);

    @Query(value = "SELECT COUNT(EVENT_ID) FROM USER_LIKE_EVENT_TABLE" +
            " WHERE USER_ID=:userId AND EVENT_ID=:eventId AND IS_LIKE=FALSE", nativeQuery = true)
    int checkDislike(@Param("eventId") Long eventId, @Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM USER_LIKE_EVENT_TABLE" +
            " WHERE USER_ID=:userId AND EVENT_ID=:eventId AND IS_LIKE=TRUE; " +
            "UPDATE LIKE_DISLIKE_TOTAL_TABLE " +
            "SET LIKE_TOTAL=LIKE_TOTAL-1 " +
            "WHERE EVENT_ID=:eventId", nativeQuery = true)
    void removeLike(@Param("eventId") Long eventId, @Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM USER_LIKE_EVENT_TABLE" +
            " WHERE USER_ID=:userId AND EVENT_ID=:eventId AND IS_LIKE=FALSE; " +
            "UPDATE LIKE_DISLIKE_TOTAL_TABLE " +
            "SET DISLIKE_TOTAL=DISLIKE_TOTAL-1 " +
            "WHERE EVENT_ID=:eventId", nativeQuery = true)
    void removeDislike(@Param("eventId") Long eventId, @Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE USER_LIKE_EVENT_TABLE SET IS_LIKE=TRUE" +
            " WHERE EVENT_ID=:eventId AND USER_ID=:userId; " +
            " UPDATE LIKE_DISLIKE_TOTAL_TABLE" +
            " SET LIKE_TOTAL = LIKE_TOTAL + 1," +
            " DISLIKE_TOTAL = DISLIKE_TOTAL - 1" +
            " WHERE EVENT_ID =:eventId", nativeQuery = true)
    void dislikeToLike(@Param("eventId") Long eventId, @Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE USER_LIKE_EVENT_TABLE SET IS_LIKE=FALSE" +
            " WHERE EVENT_ID=:eventId AND USER_ID=:userId;" +
            "UPDATE LIKE_DISLIKE_TOTAL_TABLE " +
            "SET LIKE_TOTAL=LIKE_TOTAL-1, " +
            "DISLIKE_TOTAL=DISLIKE_TOTAL+1" +
            " WHERE EVENT_ID=:eventId", nativeQuery = true)
    void likeToDislike(@Param("eventId") Long eventId, @Param("userId") Long userId);

    List<Rating> findAllByOrderByLikesDesc(Pageable pageable);

    List<Rating> findAllByOrderByDislikesDesc(Pageable pageable);

    List<Rating> findByEventId(Pageable pageable, Long eventId);

    boolean existsByEventId(Long eventId);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO LIKE_DISLIKE_TOTAL_TABLE(EVENT_ID) VALUES (:eventId)", nativeQuery = true)
    void createNewRating(@Param("eventId") Long eventId);

}
