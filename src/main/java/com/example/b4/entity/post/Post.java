package com.example.b4.entity.post;

import com.example.b4.entity.BaseTimeEntity;
import com.example.b4.entity.User;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode(callSuper=false)
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

//    @Enumerated(EnumType.STRING)
    private String category;

    private String attachedFile;

    private Boolean bookmark;

    private PostCategory code;

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateCategory(String category) {
        this.category = category;
    }

    public void updateAttachedFile(String attachedFile) {
        this.attachedFile = attachedFile;
    }

    public void updateBookmark(Boolean bookmark) {
        this.bookmark = bookmark;
    }

    //    @Builder
    public Post(User user, String title, String category, String attachedFile, Boolean bookmark,
                PostCategory code) {
        this.user = user;
        this.title = title;
        this.category = category;
        this.attachedFile = attachedFile;
        this.bookmark = bookmark;
        this.code = code;
    }
}
