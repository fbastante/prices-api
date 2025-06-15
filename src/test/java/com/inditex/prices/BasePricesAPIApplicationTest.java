package com.inditex.prices;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inditex.prices.infrastructure.adapters.outbound.database.productprices.ProductPriceJPARepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@AutoConfigureWebMvc
@AutoConfigureMockMvc
@ExtendWith({ SpringExtension.class, MockitoExtension.class })
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = PricesAPIApplication.class)
public abstract class BasePricesAPIApplicationTest {

    @Resource
    protected MockMvc mockMvc;

    @Resource
    protected ModelMapper modelMapper;

    @Resource
    protected ObjectMapper objectMapper;

    @MockitoSpyBean
    protected ProductPriceJPARepository productPriceJPARepository;

    protected void assertEqualsRecursively(final Object actualObject, final Object expectedObject, final String... ignoredFields) {
        assertThat(actualObject)
                .usingRecursiveComparison()
                .ignoringFields(ignoredFields)
                .isEqualTo(expectedObject);
    }

}

