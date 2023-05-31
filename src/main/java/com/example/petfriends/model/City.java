package com.example.petfriends.model;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_city")
    private Long idCity;

    @Column(name = "city_name")
    private String cityName;

    @OneToMany(mappedBy = "city")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "city")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private List<Event> events = new ArrayList<>();

    @OneToMany(mappedBy = "city")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private List<Petshop> petshops = new ArrayList<>();
}
