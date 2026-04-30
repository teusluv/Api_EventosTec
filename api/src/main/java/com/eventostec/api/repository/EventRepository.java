package com.eventostec.api.repository;

import com.eventostec.api.domain.evento.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface EventRepository extends JpaRepository <Event, UUID> {

}
