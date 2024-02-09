package com.example.reservation.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.reservation.model.BookTicket;
import com.example.reservation.model.Reserved;
import com.example.reservation.model.TicketDetails;
import com.example.reservation.model.Unreserved;
import com.example.reservation.model.UpdateReservedSeat;
import com.example.reservation.model.User;
import com.example.reservation.repository.ReservationRepository;

import lombok.RequiredArgsConstructor;

@Service
@SuppressWarnings("rawtypes")
@RequiredArgsConstructor
public class ReservationService {

	@Autowired
	ReservationRepository reservationRepository;

	public ResponseEntity getTicketDetailsById(int ticketId) {
		Optional<TicketDetails> ticket = reservationRepository.getTicketDetailsById(ticketId);
		if (ticket.isPresent()) {
			return ResponseEntity.status(HttpStatus.CREATED).body(ticket.get());
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ticket is not available");
	}

	public ResponseEntity createTicket(BookTicket bookTicket) {
		String userId = bookTicket.getUserId();
		User user = reservationRepository.getUser(userId);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("User is not available. Please register the user details");
		}
		try {
			int getId = reservationRepository.getTicketId();
			String journey = bookTicket.getFrom().toUpperCase() + "_" + bookTicket.getTo().toUpperCase();
			double price = Journey.valueOf(journey).showPrice();
			TicketDetails ticket = TicketDetails.builder().ticketId(getId).from(bookTicket.getFrom())
					.to(bookTicket.getTo()).user(user).price(price).build();

			Unreserved seatNo = reservationRepository.getSeatNo();
			Reserved res4 = Reserved.builder().seatNo(seatNo.getSeatNo()).section(seatNo.getSection()).user(user)
					.build();
			reservationRepository.createTicket(ticket, res4);
			return ResponseEntity.status(HttpStatus.CREATED).body(ticket);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Check the input");
		}

	}

	public enum Journey {
		LONDON_FRANCE(20), FRANCE_LONDON(20), LONDON_SCOTLAND(30), SCOTLAND_LONDON(30);

		double price;

		Journey(double p) {
			price = p;
		}

		double showPrice() {
			return price;
		}
	}

	public ResponseEntity getAllReservedSeats() {
		List<Reserved> reservedList = reservationRepository.getAllReservedSeats();
		if (reservedList != null) {
			return ResponseEntity.status(HttpStatus.OK).body(reservedList);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No reserved seats");
		}
	}

	public ResponseEntity cancelTicketByTicketId(int ticketId) {
		Optional<TicketDetails> ticket = reservationRepository.cancelTicketByTicketId(ticketId);
		if (ticket.isPresent()) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("Ticket is cancelled successfully");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ticket is not available");
		}
	}

	public ResponseEntity updateReservedSeat(UpdateReservedSeat newSeat) {
		String userId = newSeat.getUserId();
		User user = reservationRepository.getUser(userId);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("User is not available. Please register the user details");
		}
		boolean available = reservationRepository.updateReservedSeat(newSeat);
		if (available) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("Seat is updated");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seat is not available. Already reserved");
		}
	}

	public ResponseEntity addUser(User user) {
		User usr = reservationRepository.addUser(user);
		if (usr != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body("User added successfully");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User already exists");
		}
	}
}
