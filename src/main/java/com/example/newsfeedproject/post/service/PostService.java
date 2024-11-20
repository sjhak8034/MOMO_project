package com.example.newsfeedproject.post.service;

import com.example.newsfeedproject.post.dto.PostResponseDto;
import com.example.newsfeedproject.post.entity.Post;
import com.example.newsfeedproject.post.repository.PostRepository;
import com.example.newsfeedproject.user.entity.User;
import com.example.newsfeedproject.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    //게시글을 생성하는 서비스 메서드
    public PostResponseDto createPost(HttpServletRequest request, String title, String content) {
        //requestSevlet을 통해 현재 로그인한 사용자의 세션을 확인한다.
        HttpSession session = request.getSession(false);

        //로그인할때 사용자의 세션을 userId로 삽입한다.
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다.");
        }

        User postUser = userRepository.findById(userId).orElseThrow(null);

        Post savedPost = postRepository.save(new Post(title, content));

        return new PostResponseDto(
            savedPost.getId(),
            postUser.getId(),
            savedPost.getTitle(),
            savedPost.getContent(),
            savedPost.getCreatedAt()
        );
    }
}
