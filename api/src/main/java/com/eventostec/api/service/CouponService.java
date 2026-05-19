package com.eventostec.api.service;

import com.eventostec.api.domain.coupon.Coupon;
import com.eventostec.api.domain.coupon.CouponResquestDTO;
import com.eventostec.api.domain.evento.Event;
import com.eventostec.api.repository.CouponRepository;
import com.eventostec.api.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private EventRepository eventRepository;

    public Coupon addCoupon(UUID eventId, CouponResquestDTO couponDate) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        Coupon coupon = new Coupon();
        coupon.setCode(couponDate.code());
        coupon.setDiscounat(couponDate.discount());
        coupon.setValidade(new Date(couponDate.validd()));
        coupon.setEvent(event);

        return couponRepository.save(coupon);
    }
    public List<Coupon> consultCoupons(UUID eventId, Date currentDate) {
        return couponRepository.findByEventIdAndValidadeAfter(eventId, currentDate);
    }
}
