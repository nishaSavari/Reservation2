package com.example.reservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.reservation.model.BookTicket;
import com.example.reservation.model.UpdateReservedSeat;
import com.example.reservation.model.User;
import com.example.reservation.service.ReservationService;

@RestController
@RequestMapping("/api/reserve")
@SuppressWarnings("rawtypes")
public class ReservationController {

	@Autowired
	ReservationService reservationService;

	@GetMapping("receipt/{id}")
	public ResponseEntity getTicket(@PathVariable(required = true) int ticketId) {
		ResponseEntity ticketDetails = reservationService.getTicketDetailsById(ticketId);
		return ticketDetails;
	}

	@PostMapping("/addUser")
	public ResponseEntity addUser(@RequestBody User user) {
		ResponseEntity reservedticket = reservationService.addUser(user);
		return reservedticket;
	}
	
	@PostMapping("/bookTicket")
	public ResponseEntity bookTicket(@RequestBody BookTicket bookTicket) {
		ResponseEntity reservedticket = reservationService.createTicket(bookTicket);
		return reservedticket;
	}
	
	@GetMapping("getAllReservedSeats")
	public ResponseEntity getAllReservedSeats() {
		ResponseEntity ticketDetails = reservationService.getAllReservedSeats();
		return ticketDetails;
	}
	
	@DeleteMapping("cancelTicket/{id}")
	public ResponseEntity cancelTicket(@PathVariable(required = true) int ticketId) {
		ResponseEntity ticketDetails = reservationService.cancelTicketByTicketId(ticketId);
		return ticketDetails;
	}
	
	@PutMapping("/updateSeat")
	public ResponseEntity updateSeat(@RequestBody UpdateReservedSeat newSeat) {
		ResponseEntity reservedticket = reservationService.updateReservedSeat(newSeat);
		return reservedticket;
	}
}
