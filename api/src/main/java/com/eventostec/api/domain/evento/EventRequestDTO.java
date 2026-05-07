package com.eventostec.api.domain.evento;

import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public record EventRequestDTO(String title, String description, Long date,String eventUrl, String city, String uf, Boolean remote, MultipartFile image) {

}
