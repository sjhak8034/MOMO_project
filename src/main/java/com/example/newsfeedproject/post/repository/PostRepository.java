package com.example.newsfeedproject.post.repository;

import com.example.newsfeedproject.post.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // ID로 Post 조회 시 존재하지 않으면 NOT_FOUND 예외 발생
    default Post findByIdOrElseThrow(long id) {
        return findById(id).
            orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Post not found")
            );
    }


}
