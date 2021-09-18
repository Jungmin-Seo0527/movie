package com.Jungmin.movie.domain.user;

import com.Jungmin.movie.domain.comment.Comment;
import com.Jungmin.movie.domain.post.Posts;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static lombok.AccessLevel.PRIVATE;

@Entity
@Getter @Setter(PRIVATE) @Builder
@NoArgsConstructor @AllArgsConstructor
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String publicId;

    private String username;

    @Column(nullable = false, unique = true, length = 20)
    private String userNickName;

    @Builder.Default
    @OneToMany(mappedBy = "author", cascade = ALL)
    private List<Posts> postsList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "author", cascade = ALL)
    private List<Comment> comments = new ArrayList<>();


    public void writeComment(Comment comment) {
        comments.add(comment);
    }
}
