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
        Integer price,
        List<CouponDTO> coupons) {

    public record CouponDTO(
            UUID id,
            String code,
            Integer discount,
            Date valid) {
    }
}
