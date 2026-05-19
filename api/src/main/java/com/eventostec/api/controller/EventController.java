package com.eventostec.api.controller;

import com.eventostec.api.domain.evento.Event;
import com.eventostec.api.domain.evento.EventDetailsDTO;
import com.eventostec.api.domain.evento.EventRequestDTO;
import com.eventostec.api.domain.evento.EventResponseDTO;
import com.eventostec.api.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/event")
@CrossOrigin(origins = "*")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Event> create(@ModelAttribute EventRequestDTO data)  {
        Event newEvent = this.eventService.createEvent(data);
        return ResponseEntity.ok(newEvent);
    }

    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> getEventos(@RequestParam(defaultValue = "0" ) int page, @RequestParam(defaultValue = "10" ) int size) {
        List<EventResponseDTO> allEvents = this.eventService.getUpcomingEvents(page, size);
        return ResponseEntity.ok(allEvents);
    }
    @GetMapping("/filter")
    public ResponseEntity<List<EventResponseDTO>> getFilteredEvents(@RequestParam(defaultValue = "0" )int page,
                                                                    @RequestParam(defaultValue = "10")int size,
                                                                    @RequestParam(required = false) String title,
                                                                    @RequestParam(required = false) String city,
                                                                    @RequestParam(required = false) String uf,
                                                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
                                                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate){
        List<EventResponseDTO> events = eventService.getFilteredEvents(page,size,title,city,uf,startDate, endDate);
        return ResponseEntity.ok(events);


    }
    @GetMapping("/{eventId}")
    public ResponseEntity<EventDetailsDTO> getEventDetails(@PathVariable UUID eventId) {
        EventDetailsDTO eventDetailsDTO = eventService.getEventDetails(eventId);
        return ResponseEntity.ok(eventDetailsDTO);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID eventId) {
        this.eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }
}