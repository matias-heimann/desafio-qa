package com.meli.desafioqa.services.impl;

import com.meli.desafioqa.repositories.FlightRepository;
import com.meli.desafioqa.services.FlightService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FlightServiceImpl implements FlightService {

    private FlightRepository flightRepository;
}
