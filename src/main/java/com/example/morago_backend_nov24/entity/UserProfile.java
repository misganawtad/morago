package com.example.morago_backend_nov24.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "USER_PROFILE")
@PrimaryKeyJoinColumn(name = "id")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile extends User {

    private String name;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    private Long balance;

    @OneToMany(mappedBy = "userProfile", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Call> calls;

    @OneToMany(mappedBy = "userProfile", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Deposit> deposits;

    @OneToMany(mappedBy = "userProfile", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Charge> charges;

    @OneToMany(mappedBy = "ratedByUser", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Rating> ratingsGiven;
}
