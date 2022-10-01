package ru.yandex.practicum.catsgram.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PostFeedController {
    private final PostService postService;

    @Autowired
    public PostFeedController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping(value = "/feed/friends")
    public List<Post> getPostFriends(@RequestBody String data) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Post> friendsResults = new ArrayList<>();
        Friends friends;
        try{
            String parsedStringFromRequestBody = objectMapper.readValue(data, String.class);
            friends = objectMapper.readValue(parsedStringFromRequestBody, Friends.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Неправильный формат", e);
        }

        if (friends != null) {
            for (String friend:  friends.getFriends()) {
                friendsResults.addAll(postService.findUsersByEmail(friend, friends.sort, friends.size));
            }

            return friendsResults;
        }
        return null;
    }

    static class Friends{
        private String sort;
        private Integer size;
        private List<String> friends;

        public String getSort() {
            return sort;
        }

        public Integer getSize() {
            return size;
        }

        public List<String> getFriends() {
            return friends;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public void setSize(Integer size) {
            this.size = size;
        }

        public void setFriends(List<String> friends) {
            this.friends = friends;
        }

        @Override
        public String toString() {
            return "{sort="+ sort
                    +", size="+ size
                    +", friends="+ friends +"}";
        }
    }
}
