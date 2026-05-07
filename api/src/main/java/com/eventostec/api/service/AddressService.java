package com.eventostec.api.service;

import com.eventostec.api.domain.address.Address;
import com.eventostec.api.domain.evento.Event;
import com.eventostec.api.domain.evento.EventRequestDTO;
import com.eventostec.api.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public Address createAddress(EventRequestDTO data, Event event) {
        Address address = new Address();
        address.setCity(data.city());
        address.setUf(data.uf());
        address.setEvent(event);
        return addressRepository.save(address);
    }
}
