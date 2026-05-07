package com.eventostec.api.domain.evento;

import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.UUID;

public record EventResponseDTO(UUID id, String title, String description, Date date, String eventUrl, String city, String uf, Boolean remote, String image) {
}
