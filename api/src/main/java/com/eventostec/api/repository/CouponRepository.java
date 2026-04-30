package com.eventostec.api.repository;

import com.eventostec.api.domain.coupon.Coupon;
import com.eventostec.api.domain.evento.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CouponRepository extends JpaRepository<Coupon, UUID> {
}
