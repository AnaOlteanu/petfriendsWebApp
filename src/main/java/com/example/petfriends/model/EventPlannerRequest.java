package com.example.petfriends.model;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class EventPlannerRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_event_planner_request")
    private Long idEventPlannerRequest;

    @Column(columnDefinition = "enum('PENDING', 'APPROVED', 'REJECTED')")
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user")
    @ToString.Exclude
    private User user;
}
