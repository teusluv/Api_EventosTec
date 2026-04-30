package com.eventostec.api.domain.coupon;
import com.eventostec.api.domain.evento.Event;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "coupon")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Coupon {
    @Id
    @GeneratedValue
    private UUID id;
    private String code;
    private Integer discounat;
    private Date validade;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;


}
