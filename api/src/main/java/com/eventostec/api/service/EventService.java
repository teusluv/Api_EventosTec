package com.eventostec.api.service;

import com.amazonaws.services.s3.AmazonS3;
import com.eventostec.api.domain.address.Address;
import com.eventostec.api.domain.coupon.Coupon;
import com.eventostec.api.domain.evento.Event;
import com.eventostec.api.domain.evento.EventDetailsDTO;
import com.eventostec.api.domain.evento.EventRequestDTO;
import com.eventostec.api.domain.evento.EventResponseDTO;
import com.eventostec.api.repository.AddressRepository;
import com.eventostec.api.repository.CouponRepository;
import com.eventostec.api.repository.EventRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;
import com.eventostec.api.service.CouponService;
import com.eventostec.api.domain.coupon.Coupon;
import com.eventostec.api.domain.evento.EventDetailsDTO;
import java.util.UUID;
import org.springframework.stereotype.Service;

import javax.annotation.processing.Generated;

import static java.util.Arrays.stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

@Service
@Setter
public class EventService {

    @Value("${aws.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 amazonS3Client;

    @Autowired
    private EventRepository repository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private AddressService addressService;

    @Autowired
    private CouponService couponService;


    public Event createEvent(EventRequestDTO data) {
        String imgUrl = null;

        if (data.image() != null && !data.image().isEmpty()) {
           imgUrl = this.uploadImg(data.image());
        }
        Event newEvent = new Event();
        newEvent.setTitle(data.title());
        newEvent.setDescription(data.description());
        newEvent.setEventUrl(data.eventUrl());
        if (data.date() != null) {
            long timestamp = data.date();
            if (timestamp < 10000000000L) { 
                timestamp *= 1000L;
            }
            newEvent.setDate(new Date(timestamp));
        }
        if (imgUrl == null || imgUrl.isEmpty()) {
            imgUrl = "https://images.unsplash.com/photo-1540575467063-178a50c2df87?w=1200&auto=format&fit=crop&q=80";
        }
        newEvent.setImgUrl(imgUrl);
        newEvent.setRemote(data.remote());
        newEvent.setPrice(data.price());
        newEvent.setSpeakersJson(data.speakers());
        newEvent.setAgendaJson(data.agenda());

        repository.save(newEvent);

        if (!data.remote()) {
            Address address = this.addressService.createAddress(data, newEvent);
            newEvent.setAddress(address);

        }
        return newEvent;
    }

    public List<EventResponseDTO> getUpcomingEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> eventsPage = this.repository.findUpcomingEvents(new Date(), pageable);

        return eventsPage.map(event -> {
            Address address = addressRepository.findByEventId(event.getId()).orElse(null);
            return new EventResponseDTO(
                    event.getId(),
                    event.getTitle(),
                    event.getDescription(),
                    event.getDate(),
                    event.getEventUrl(),
                    event.getAddress() != null ? event.getAddress().getCity(): "",
                    event.getAddress() != null ? event.getAddress().getUf(): "",
                    event.getRemote(),
                    event.getImgUrl(),
                    event.getPrice()
            );
        }).stream().toList();
    }
    public List<EventResponseDTO> getFilteredEvents(int page, int size, String title, String city, String uf, Date startDate, Date endDate) {
        title = (title != null) ? title : "";
        city = (city != null) ? city : "";
        uf = (uf != null) ? uf : "";
        
        if (startDate == null) startDate = new Date(0);
        if (endDate == null) endDate = new Date(9100000000000000L); 

        Pageable pageable = PageRequest.of(page, size);

        Page<Event> eventsPage = this.repository.findFilteredEvents(title, city, uf, startDate, endDate, pageable);

        return eventsPage.map(event -> {

            Address address = addressRepository.findByEventId(event.getId()).orElse(null);

            return new EventResponseDTO(
                    event.getId(),
                    event.getTitle(),
                    event.getDescription(),
                    event.getDate(),
                    event.getEventUrl(),
                    address != null ? address.getCity() : "",
                    address != null ? address.getUf() : "",
                    event.getRemote(),
                    event.getImgUrl(),
                    event.getPrice()
            );
        }).toList();
    }
    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<EventDetailsDTO.SpeakerDTO> parseSpeakers(String json) {
        if (json == null || json.isEmpty()) return Collections.emptyList();
        try {
            return objectMapper.readValue(json, new TypeReference<List<EventDetailsDTO.SpeakerDTO>>() {});
        } catch (Exception e) {
            System.err.println("Failed to parse speakers JSON: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private List<EventDetailsDTO.AgendaDTO> parseAgenda(String json) {
        if (json == null || json.isEmpty()) return Collections.emptyList();
        try {
            return objectMapper.readValue(json, new TypeReference<List<EventDetailsDTO.AgendaDTO>>() {});
        } catch (Exception e) {
            System.err.println("Failed to parse agenda JSON: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public EventDetailsDTO getEventDetails(UUID eventId) {
        Event event = repository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        List<Coupon> coupons = couponService.consultCoupons(eventId, new Date());

        List<EventDetailsDTO.CouponDTO> couponDTOs = coupons.stream()
                .map(coupon -> new EventDetailsDTO.CouponDTO(
                        coupon.getId(),
                        coupon.getCode(),
                        coupon.getDiscounat(),
                        coupon.getValidade()))
                .collect(Collectors.toList());

        return new EventDetailsDTO(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getDate(),
                event.getAddress() != null ? event.getAddress().getCity() : "",
                event.getAddress() != null ? event.getAddress().getUf() : "",
                event.getImgUrl(),
                event.getEventUrl(),
                event.getPrice(),
                couponDTOs,
                parseSpeakers(event.getSpeakersJson()),
                parseAgenda(event.getAgendaJson())
        );
    }

    @org.springframework.transaction.annotation.Transactional
    public void deleteEvent(UUID eventId) {
        Event event = repository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        repository.delete(event);
    }

    @org.springframework.transaction.annotation.Transactional
    public Event updateEvent(UUID eventId, EventRequestDTO data) {
        Event event = repository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        event.setTitle(data.title());
        event.setDescription(data.description());
        event.setEventUrl(data.eventUrl());
        
        if (data.date() != null) {
            long timestamp = data.date();
            if (timestamp < 10000000000L) {
                timestamp *= 1000L;
            }
            event.setDate(new Date(timestamp));
        }

        if (data.image() != null && !data.image().isEmpty()) {
            String imgUrl = this.uploadImg(data.image());
            if (imgUrl != null && !imgUrl.isEmpty()) {
                event.setImgUrl(imgUrl);
            }
        }
        
        event.setRemote(data.remote());
        event.setPrice(data.price());
        
        if (data.speakers() != null) {
            event.setSpeakersJson(data.speakers());
        }
        if (data.agenda() != null) {
            event.setAgendaJson(data.agenda());
        }

        // Handle Address
        if (data.remote()) {
            Address address = event.getAddress();
            if (address != null) {
                addressRepository.delete(address);
                event.setAddress(null);
            }
        } else {
            Address address = event.getAddress();
            if (address == null) {
                address = new Address();
                address.setEvent(event);
            }
            address.setCity(data.city());
            address.setUf(data.uf().toUpperCase());
            addressRepository.save(address);
            event.setAddress(address);
        }

        return repository.save(event);
    }

    private String uploadImg(MultipartFile image) {
        try {
            SupabaseStorageService storageService = new SupabaseStorageService();
            return storageService.fazerUpload(image);
        } catch (Exception e) {
            System.out.println("Supabase upload failed, falling back to local storage: " + e.getMessage());
            try {
                File uploadDir = new File("uploads");
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                
                String originalName = image.getOriginalFilename();
                String cleanName = originalName != null ? originalName.replaceAll("[\\s()]", "_") : "imagem.jpg";
                String fileName = System.currentTimeMillis() + "-" + cleanName;
                
                File destFile = new File(uploadDir, fileName);
                image.transferTo(destFile);
                
                System.out.println("File saved locally: " + destFile.getAbsolutePath());
                return "http://localhost:8080/uploads/" + fileName;
            } catch (Exception localEx) {
                System.out.println("Error while saving file locally: " + localEx.getMessage());
                localEx.printStackTrace();
                return null;
            }
        }
    }

    public class SupabaseStorageService {

        private final String SUPABASE_URL = "";
        private final String SUPABASE_KEY = "";
        private final String BUCKET_NAME = "Imagem_Url";

        public String fazerUpload(MultipartFile arquivo) throws Exception {
            String nomeOriginal = arquivo.getOriginalFilename();
            String nomeLimpo = nomeOriginal != null ? nomeOriginal.replaceAll("[\\s()]", "_") : "imagem.jpg";
            String nomeArquivo = System.currentTimeMillis() + "-" + nomeLimpo;

            String urlEndpoint = SUPABASE_URL + "/storage/v1/object/" + BUCKET_NAME + "/imagens/" + nomeArquivo;

            HttpClient client = HttpClient.newHttpClient();

            String contentType = arquivo.getContentType();
            if (contentType == null || contentType.isEmpty()) {
                contentType = "application/octet-stream";
            }

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlEndpoint))
                    .header("Authorization", "Bearer " + SUPABASE_KEY)
                    .header("Content-Type", contentType)
                    .POST(HttpRequest.BodyPublishers.ofByteArray(arquivo.getBytes()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Erro no Supabase: " + response.body());
            }
            
            return SUPABASE_URL + "/storage/v1/object/public/" + BUCKET_NAME + "/imagens/" + nomeArquivo;
        }
    }

    private File convertMultipartToFile(MultipartFile multipartFile) throws IOException {
        File convFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(multipartFile.getBytes());
        fos.close();
        return convFile;
    }
}
