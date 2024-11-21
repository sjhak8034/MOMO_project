package com.example.newsfeedproject.friend.dto.request;

import lombok.Getter;

@Getter
public class RequestFriendRequestDto {
    private final Long receiverId;
    private RequestFriendRequestDto(Long receiverId) {
        this.receiverId = receiverId;
    }
    private RequestFriendRequestDto() {
        this.receiverId = null;
    }

}
