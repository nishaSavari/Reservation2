package com.example.reservation.test;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.example.reservation.model.BookTicket;
import com.example.reservation.service.ReservationService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest
public class ReservationControllerTest {
	
	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService ReservationService;

    @Autowired
    private ObjectMapper objectMapper;
    
    // negative scenario - Invalid user id
    // JUnit test for GET employee by id REST API
    @Test
    public void givenBookTicketObject_whenCreateBookTicket_thenReturnTicketDetails() throws Exception{
    	BookTicket ticket = BookTicket.builder().userId("u102").from("London").to("France").trainSection("A").seatNo(3).build();
    	
    	given(ReservationService.createTicket(ticket))
        .willAnswer((invocation)-> invocation.getArgument(0));
    	
    	ResultActions response = mockMvc.perform(post("/api/bookTicket")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ResponseEntity.class)));
    	
    	// then - verify the result or output using assert statements
        response.andDo(print()).
                andExpect(status().isCreated());
                
    }

}
