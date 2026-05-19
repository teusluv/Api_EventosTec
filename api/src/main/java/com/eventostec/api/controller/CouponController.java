package com.eventostec.api.controller;

import com.eventostec.api.domain.coupon.Coupon;
import com.eventostec.api.domain.coupon.CouponResquestDTO;
import com.eventostec.api.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/coupon")
@CrossOrigin(origins = "*")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @PostMapping("/event/{eventId}")
    public ResponseEntity<Coupon> createCoupon(@PathVariable UUID eventId, @RequestBody CouponResquestDTO couponDate) {
        Coupon savedCoupon = couponService.addCoupon(eventId, couponDate);
        return ResponseEntity.ok(savedCoupon);
    }
}
