package com.example.b4.dto.user;

import com.example.b4.entity.User;
import com.example.b4.entity.post.Mind;
import com.example.b4.entity.post.Post;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MindStatusDto {

    private String title;
    private String user;
    private String category;
    private Boolean status;
    private LocalDateTime createdAt;
    private String mindAttachedFile;
    private Boolean bookmark;

    public MindStatusDto (Post post) {
        this.title = post.getTitle();
        this.category = post.getCategory();
        this.createdAt = post.getCreatedDate();
        this.mindAttachedFile = post.getAttachedFile();
        this.bookmark = post.getBookmark();
    }

    public void updateUserStatus(User user, Mind mind) {
        this.user = user.getUsername();
        this.status = mind.getStatus();
    }
}
