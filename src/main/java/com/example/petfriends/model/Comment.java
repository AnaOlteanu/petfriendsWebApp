package com.example.petfriends.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comment")
    private Long idComment;

    @NotEmpty(message = "Comment cannot be empty!")
    @NotNull
    private String content;

    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "id_post", nullable = false)
    Post post;

    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }
}
