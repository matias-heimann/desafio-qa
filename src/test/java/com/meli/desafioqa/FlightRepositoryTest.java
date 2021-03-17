package com.meli.desafioqa;

import com.meli.desafioqa.repositories.FlightRepository;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(FlightRepository.class)
public class FlightRepositoryTest {
}
