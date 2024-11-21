package com.example.newsfeedproject.friend.service;

import com.example.newsfeedproject.friend.dto.accept.AcceptFriendResponseDto;
import com.example.newsfeedproject.friend.dto.accept.AcceptFriendServiceDto;
import com.example.newsfeedproject.friend.dto.reject.RejectFriendResponseDto;
import com.example.newsfeedproject.friend.dto.reject.RejectFriendServiceDto;
import com.example.newsfeedproject.friend.dto.request.RequestFriendRequestDto;
import com.example.newsfeedproject.friend.dto.request.RequestFriendResponseDto;
import com.example.newsfeedproject.friend.dto.request.RequestFriendServiceDto;
import com.example.newsfeedproject.friend.entity.Friend;
import com.example.newsfeedproject.friend.repository.FriendRepository;
import com.example.newsfeedproject.user.entity.User;
import com.example.newsfeedproject.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class FriendService {
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    public FriendService(FriendRepository friendRepository , UserRepository userRepository, EntityManager entityManager) {
        this.userRepository = userRepository;
        this.friendRepository = friendRepository;
    }

    public RequestFriendResponseDto RequestFriend(RequestFriendServiceDto dto) {
        User receiver = userRepository.findById(dto.getReceiverId())
                .orElseThrow(()->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다.")
                );
        User requester = userRepository.findById(dto.getReceiverId())
                .orElseThrow(()->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다.")
                );
        Friend friend = new Friend(receiver, requester);
        friendRepository.save(friend);
        return new RequestFriendResponseDto(true);
    }
    // TODO : Transaction -> repository 에서 join 하고 가져오는걸로 리팩토링
    @Transactional
    public AcceptFriendResponseDto AcceptFriend(AcceptFriendServiceDto dto) {
        User accepter = userRepository.findById(dto.getUserId())
                .orElseThrow(()->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다.")
                );
        // receiver의 유저 id로부터 friend를 찾고 status 변경
        Friend acceptableFriend = accepter.getReceivedFriendRequests().stream().filter(friend -> friend.getId().equals(dto.getFriendId())).findFirst()
                .orElseThrow(()->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "수락할 친구를 찾을 수 없습니다.")
                );
        acceptableFriend.accept();
        friendRepository.save(acceptableFriend);
        return new AcceptFriendResponseDto(true);
    }

    public RejectFriendResponseDto RejectFriend(RejectFriendServiceDto dto) {

        List<Friend> requestFriends = friendRepository.findAllFriendRequests(dto.getUserId());
        Friend requestFriend = requestFriends.stream().filter(friend -> friend.getId().equals(dto.getFriendId())).findFirst()
                .orElseThrow(()->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "수락할 친구를 찾을 수 없습니다.")
                );
        friendRepository.delete(requestFriend);

        friendRepository.deleteById(requestFriend.getId());



        System.out.println(friendRepository.count());
        return new RejectFriendResponseDto(true);
    }
}
