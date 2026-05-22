package com.eventostec.api.domain.evento;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public record EventDetailsDTO(
        UUID id,
        String title,
        String description,
        Date date,
        String city,
        String uf,
        String imgUrl,
        String eventUrl,
        Double price,
        List<CouponDTO> coupons,
        List<SpeakerDTO> speakers,
        List<AgendaDTO> agenda) {

    public record CouponDTO(
            UUID id,
            String code,
            Integer discount,
            Date valid) {
    }

    public record SpeakerDTO(
            String name,
            String role,
            String image,
            String profileUrl) {
    }

    public record AgendaDTO(
            String time,
            String title,
            String description) {
    }
}
