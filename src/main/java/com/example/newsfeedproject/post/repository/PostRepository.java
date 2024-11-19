package com.example.newsfeedproject.post.repository;

import com.example.newsfeedproject.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}
