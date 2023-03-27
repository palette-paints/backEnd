package com.example.b4.dto.user;

import com.example.b4.entity.User;
import com.example.b4.entity.post.Play;
import com.example.b4.entity.post.Post;
import lombok.Data;

import java.security.Timestamp;
import java.time.LocalDateTime;

@Data
public class PlayStatusDto {
    private String title;
    private String user;
    private String category;
    private String status;
    private LocalDateTime createAt;

    public PlayStatusDto (Post post){
        this.title = post.getTitle();
        this.category = post.getCategory();
        this.createAt = post.getCreatedDate();
    }

    public void updateUserAndStatus(User user, String status) {
        this.user = user.getUsername();
        this.status = status;
    }
}

