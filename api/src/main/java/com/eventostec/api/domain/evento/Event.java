package com.eventostec.api.domain.evento;

import com.eventostec.api.domain.address.Address;
import com.eventostec.api.domain.coupon.Coupon;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Table(name = "event")
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue
    private UUID id;

    private String title;
    private String description;
    private String imgUrl;
    private String eventUrl;
    private Boolean remote;
    private Date date;
    private Double price;

    @Column(name = "speakers_json", columnDefinition = "TEXT")
    private String speakersJson;

    @Column(name = "agenda_json", columnDefinition = "TEXT")
    private String agendaJson;

    @OneToOne(mappedBy = "event", cascade = CascadeType.ALL)
    private Address address;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Coupon> coupons;
}

