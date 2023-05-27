package com.example.petfriends.model;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_post")
    private Long idPost;

    @NotEmpty(message = "Post title cannot be empty!")
    @Length(min = 5, message = "Post title should be at least 5 characters!")
    private String title;

    @NotEmpty(message = "Post content cannot be empty!")
    @Length(min = 5, message = "Post content should be at least 5 characters!")
    private String content;

    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    User user;

    @OneToMany(mappedBy = "post")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private List<Image> images = new ArrayList<>();

    @ManyToMany(mappedBy = "likedPosts", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @ToString.Exclude
    private Set<User> usersLike = new LinkedHashSet<>();

    public String getFormattedDate() {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        return date.format(formatter);
    }

    public void removeUser(User user) {
        user.getLikedPosts().remove(this);
        usersLike.remove(user);
    }

}
