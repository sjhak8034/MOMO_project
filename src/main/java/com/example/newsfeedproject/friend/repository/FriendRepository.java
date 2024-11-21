package com.example.newsfeedproject.friend.repository;

import com.example.newsfeedproject.friend.entity.Friend;
import com.example.newsfeedproject.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    @Query("select f from User u join u.receivedFriendRequests f where u.id = :id and f.status = com.example.newsfeedproject.friend.entity.Friend.FriendStatus.ACCEPTED")
    List<Friend> findAllFriendReceivedWAITING(@Param("id") Long id);

    @Query("select f from User u join u.receivedFriendRequests f where u.id = :id and f.status = com.example.newsfeedproject.friend.entity.Friend.FriendStatus.ACCEPTED")
    List<Friend> findAllFriendReceivedACCEPTED(@Param("id") Long id);

    @Query("select f from User u join u.sentFriendRequests f where u.id = :id and f.status = com.example.newsfeedproject.friend.entity.Friend.FriendStatus.ACCEPTED")
    List<Friend> findAllFriendSentWAITING(@Param("id") Long id);

    @Query("select f from User u join u.sentFriendRequests f where u.id = :id and f.status = com.example.newsfeedproject.friend.entity.Friend.FriendStatus.ACCEPTED")
    List<Friend> findAllFriendSentACCEPTED(@Param("id") Long id);

    @Query("select f from User u join u.receivedFriendRequests f where u.id = :id ")
    List<Friend> findAllFriendReceived(@Param("id") Long id);

    @Query("select f from User u join u.sentFriendRequests f where u.id = :id ")
    List<Friend> findAllFriendSent(@Param("id") Long id);

    @Query(value = "select p from User u join  u.sentFriendRequests sf " +
            "join sf.receiver ru join ru.posts p where u.id = :id " +
            "and ru.id != :id and sf.status = com.example.newsfeedproject.friend.entity.Friend.FriendStatus.ACCEPTED",
            countQuery ="select count(p)from User u join  u.sentFriendRequests sf join sf.receiver ru join ru.posts p" +
                    " where u.id = :id and ru.id != :id and sf.status = com.example.newsfeedproject.friend.entity.Friend.FriendStatus.ACCEPTED")
    Page<Post> findFriendPostsFromReceiver(@Param("id") Long id, Pageable pageable);
    @Query(value = "select p from User u join  u.receivedFriendRequests sf " +
            "join sf.requester ru join ru.posts p where u.id = :id " +
            "and ru.id != :id and sf.status = com.example.newsfeedproject.friend.entity.Friend.FriendStatus.ACCEPTED",
            countQuery ="select count(p)from User u join  u.receivedFriendRequests sf join sf.receiver ru join ru.posts p" +
                    " where u.id = :id and ru.id != :id and sf.status = com.example.newsfeedproject.friend.entity.Friend.FriendStatus.ACCEPTED")
    Page<Post> findFriendPostsFromRequester(@Param("id") Long id, Pageable pageable);

}
