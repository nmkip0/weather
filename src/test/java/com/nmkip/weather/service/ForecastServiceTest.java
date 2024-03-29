package com.nmkip.weather.service;

import com.nmkip.weather.domain.Forecast;
import com.nmkip.weather.exception.NotFoundException;
import com.nmkip.weather.repository.ForecastRepository;
import name.falgout.jeffrey.testing.junit.mockito.MockitoExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;

import java.util.Optional;

import static com.nmkip.weather.domain.Weather.DRAUGHT;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ForecastServiceTest {

    @Mock
    ForecastRepository repository;

    private ForecastService service;

    @BeforeEach
    void setUp() {
        service = new ForecastService(repository);
    }

    @ParameterizedTest
    @CsvSource({
            "100, 100",
            "460, 100",
            "360, 360",
            "720, 360"

    })
    void search_for_forecast_by_day(Integer day, Integer equivalentDay) {
        given(repository.findById(equivalentDay)).willReturn(Optional.of(new Forecast(equivalentDay, DRAUGHT)));

        assertThat(service.forecastFor(day), is(new Forecast(day, DRAUGHT)));
    }

    @Test
    void throw_an_exception_when_day_is_not_found() {
        given(repository.findById(32)).willReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> service.forecastFor(32));
    }

}
