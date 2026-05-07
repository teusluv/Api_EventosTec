package com.eventostec.api.domain.coupon;

import org.springframework.web.bind.annotation.RequestBody;

public record CouponResquestDTO(String code, Integer discount, Long validd) {
}
