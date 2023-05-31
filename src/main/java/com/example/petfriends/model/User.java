package com.example.petfriends.model;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long idUser;

    @NotEmpty(message = "Username cannot be empty!")
    @Length(min = 3, message = "Username should be at least 3 characters!")
    @Column(unique = true)
    private String username;

    @NotEmpty(message = "Password cannot be empty!")
    @Length(min = 5, message = "Password should be at least 5 characters!")
    private String password;

    @NotEmpty(message = "First name cannot be empty!")
    @Column(name = "first_name")
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty!")
    @Column(name = "last_name")
    private String lastName;

    @Email(message = "Email is not valid!")
    @NotEmpty(message = "Email cannot be empty!")
    @Column(unique = true)
    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "birth_date")
    @Past(message = "Birth date should be in the past!")
    private LocalDate birthDate;

    private String profileDescription;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
                joinColumns = @JoinColumn(name = "id_user"),
                inverseJoinColumns = @JoinColumn(name = "id_role"))
    private Set<Role> roles;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_pet")
    @ToString.Exclude
    private Pet pet;

    @OneToMany(mappedBy = "user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private List<Post> posts;

    @OneToMany(mappedBy = "user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private List<Image> images = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "id_city", nullable = false)
    private City city;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "user_like",
                joinColumns = @JoinColumn(name = "id_user"),
                inverseJoinColumns = @JoinColumn(name = "id_post"))
    @ToString.Exclude
    private Set<Post> likedPosts;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "user_follow",
            joinColumns = @JoinColumn(name = "id_user_source"),
            inverseJoinColumns = @JoinColumn(name = "id_user_followed"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"id_user_source", "id_user_followed"}))
    private List<User> followedUsers;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "user_event",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_event"))
    @ToString.Exclude
    private List<Event> eventsJoined;

    @OneToMany(mappedBy = "userPlanner")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private List<Event> eventsPlanned = new ArrayList<>();


    public User(String firstName, String lastName, String email, String username, String password, LocalDate birthDate, City city) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.birthDate = birthDate;
        this.city = city;
    }

    public void removeRole(Role role) {
        role.getUsers().remove(this);
        roles.remove(role);
    }

    public void removeLikedPost(Post post) {
        post.getUsersLike().remove(this);
        likedPosts.remove(post);
    }

}
