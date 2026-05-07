package com.eventostec.api.service;

import com.amazonaws.services.s3.AmazonS3;
import com.eventostec.api.domain.address.Address;
import com.eventostec.api.domain.coupon.Coupon;
import com.eventostec.api.domain.evento.Event;
import com.eventostec.api.domain.evento.EventDetailsDTO;
import com.eventostec.api.domain.evento.EventRequestDTO;
import com.eventostec.api.domain.evento.EventResponseDTO;
import com.eventostec.api.repository.AddressRepository;
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
            if (timestamp < 10000000000L) { // Se for em segundos (10 dígitos), converte pra milissegundos
                timestamp *= 1000L;
            }
            newEvent.setDate(new Date(timestamp));
        }
        newEvent.setImgUrl(imgUrl);
        newEvent.setRemote(data.remote());

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
                    event.getImgUrl()
            );
        }).stream().toList();
    }
    public List<EventResponseDTO> getFilteredEvents(int page, int size, String title, String city, String uf, Date startDate, Date endDate) {
        title = (title != null) ? title : "";
        city = (city != null) ? city : "";
        uf = (uf != null) ? uf : "";
        
        if (startDate == null) startDate = new Date(0);
        if (endDate == null) endDate = new Date(9100000000000000L); // Data máxima segura para o PostgreSQL (aprox. ano 288.000)

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
                    event.getImgUrl()
            );
        }).toList();
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
                couponDTOs
        );
    }

    private String uploadImg(MultipartFile image) {
        try {
            SupabaseStorageService storageService = new SupabaseStorageService();
            return storageService.fazerUpload(image);
        } catch (Exception e) {
            System.out.println("Error while uploading file: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public class SupabaseStorageService {

        private final String SUPABASE_URL = "https://ybuydpfqjnckewumxjxz.supabase.co";
        private final String SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InlidXlkcGZxam5ja2V3dW14anh6Iiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc3Nzk5ODM3NSwiZXhwIjoyMDkzNTc0Mzc1fQ.mc6iFxkCquTvwjk_juVeYEg7sm-3catcQWwu9qCjTlI";
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
