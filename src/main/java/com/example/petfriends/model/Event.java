package com.example.petfriends.model;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_event")
    private Long idEvent;

    @NotEmpty(message = "Title of event cannot be empty!")
    @NotNull
    private String title;

    @FutureOrPresent(message = "Start date should be in the present or future!")
    @Column(name = "start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @FutureOrPresent(message = "End date should be in the present or future!")
    @Column(name = "end_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotEmpty(message = "Description of event cannot be empty!")
    @NotNull
    private String description;

    @NotEmpty(message = "Location of event cannot be empty!")
    @NotNull
    private String location;

    @Column(columnDefinition = "enum('PLAYDATE', 'MEETING_FOR_ALL', 'ADOPTION', 'FAMILY_MEETING', 'BREED_MEETING')")
    @Enumerated(EnumType.STRING)
    private CategoryEnum category;

    @ManyToOne
    @JoinColumn(name = "id_city", nullable = false)
    private City city;

    @ManyToOne
    @JoinColumn(name = "id_user_planner", nullable = true)
    private User userPlanner;

    @ManyToMany(mappedBy = "eventsJoined", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @ToString.Exclude
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "event")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private List<Image> images = new ArrayList<>();

    public String getFormattedStartDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return startDate.format(formatter);
    }

    public String getFormattedEndDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return endDate.format(formatter);
    }
}
