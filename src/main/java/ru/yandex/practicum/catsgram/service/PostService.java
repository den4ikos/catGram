package ru.yandex.practicum.catsgram.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.catsgram.exception.PostNotFoundException;
import ru.yandex.practicum.catsgram.exception.UserNotFoundException;
import ru.yandex.practicum.catsgram.model.Post;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PostService {
    private final List<Post> posts = new ArrayList<>();
    private final UserService userService;

    @Autowired
    public PostService(UserService userService) {
        this.userService = userService;
    }

    public List<Post> findAll(Map<String, String> params) {
        Stream<Post> results = posts.stream()
                .sorted((post0, post1) -> {
                    int comparator = post0.getCreationDate().compareTo(post1.getCreationDate());
                    if (params.containsKey("sort") && params.get("sort").equals("desc")) {
                        comparator = comparator * -1;
                    }
                    return comparator;
                });
        if (params.containsKey("from")) {
            results = results.skip(Long.parseLong(params.get("from")));
        }

        if (params.containsKey("size")) {
            results = results.limit(Long.parseLong(params.get("size")));
        }

        return results.collect(Collectors.toList());
    }

    public Post create(Post post) {
        if (!userService.findUserByEmail(post.getAuthor())) {
            throw new UserNotFoundException("Пользователь " + post.getAuthor() + " не найден");
        }
        posts.add(post);
        return post;
    }

    public Post findById(Integer postId) {
        return posts.stream()
                .filter(p -> p.getId().equals(postId))
                .findFirst()
                .orElseThrow(() -> new PostNotFoundException(String.format("Пост #%d не найден", postId)));
    }
}