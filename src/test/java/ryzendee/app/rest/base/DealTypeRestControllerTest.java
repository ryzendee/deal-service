package ryzendee.app.rest.base;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ryzendee.app.dto.DealTypeSaveRequest;
import ryzendee.app.rest.impl.base.DealTypeRestController;
import ryzendee.app.service.DealTypeService;

import static org.mockito.Mockito.verify;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(DealTypeRestController.class)
public class DealTypeRestControllerTest {

    private static final String BASE_URI = "/deal-type";

    @MockitoBean
    private DealTypeService dealTypeService;

    @Autowired
    private MockMvc mockMvc;

    private MockMvcRequestSpecification request;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.basePath = BASE_URI;
        RestAssuredMockMvc.mockMvc(mockMvc);
        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();

        request = RestAssuredMockMvc.given()
                .contentType(ContentType.JSON);
    }

    @Test
    void saveOrUpdate_validRequest_statusOk() {
        DealTypeSaveRequest saveRequest = DealTypeSaveRequest.builder()
                .id("id")
                .name("name")
                .build();

        request.body(saveRequest)
                .put("/save")
                .then()
                .status(HttpStatus.OK);

        verify(dealTypeService).saveOrUpdate(saveRequest);
    }

    @Test
    void getAllDealTypes_statusOk() {
        request.get("/all")
                .then()
                .status(HttpStatus.OK);

        verify(dealTypeService).getAllTypes();
    }
}
