package com.example.reservation.repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.reservation.model.Reserved;
import com.example.reservation.model.TicketDetails;
import com.example.reservation.model.TrainDetails;
import com.example.reservation.model.Unreserved;
import com.example.reservation.model.UpdateReservedSeat;
import com.example.reservation.model.User;

@Repository
public class ReservationRepository {
	public List<User> userList = new ArrayList<>();
	public List<TicketDetails> ticketList = new ArrayList<TicketDetails>();
	TrainDetails trainInfo = new TrainDetails();
	List<Reserved> reservedList = new LinkedList<>();
	List<Unreserved> unreservedList = new ArrayList<>();

	public ReservationRepository() {
		User usr1=new User("u101", "Nisha", "savari", "nisha@yahoo.com");
		User usr2 = new User("u102", "Arun", "Kumar", "arun@gmail.com");
		User usr3 = new User("u103", "Blessy", "Ruth", "ruth@yahoo.com");
		userList.add(usr1);
		userList.add(usr2);
		userList.add(usr3);
		
		
		TicketDetails td1 = new TicketDetails(101, "London", "France", userList.get(0), 20.00);
		TicketDetails td2 = new TicketDetails(102, "London", "France", userList.get(1), 20.00);
		ticketList.add(td1);
		ticketList.add(td2);
		
		Unreserved unr1 = new Unreserved(3, "A");
		Unreserved unr2 = new Unreserved(1, "B");
		Unreserved unr3 = new Unreserved(2, "B");
		unreservedList.add(unr1);
		unreservedList.add(unr2);
		unreservedList.add(unr3);
		
		Reserved res = Reserved.builder().seatNo(1).section(Section.valueOf("secA").getSection()).user(userList.get(0)).build();
		Reserved res1 = Reserved.builder().seatNo(2).section(Section.valueOf("secA").getSection()).user(userList.get(1)).build();
		Reserved res2 = Reserved.builder().seatNo(3).section(Section.valueOf("secB").getSection()).user(userList.get(2)).build();
		reservedList.add(res);
		reservedList.add(res1);
		reservedList.add(res2);
	}

	public enum Section {
		secA("A"), secB("B");

		String sec;

		Section(String p) {
			sec = p;
		}

		String getSection() {
			return sec;
		}
	}
	
	public Optional<TicketDetails> getTicketDetailsById(int ticketId) {
		Optional<TicketDetails> ticket = ticketList.stream().filter(t -> t.getTicketId()==ticketId).findFirst();
		return ticket;
	}

	public User getUser(String userId) {
		User user = userList.stream().filter(u -> u.getUserId().equalsIgnoreCase(userId)).findFirst().get();
		return user;

	}

	public void createTicket(TicketDetails ticket, Reserved res4) {
		ticketList.add(ticket);
		reservedList.add(res4);

	}

	public int getTicketId() {
		int incTicketId = 0;
		Optional<TicketDetails> ticket = Optional.ofNullable(ticketList.stream().sorted()
				.max(Comparator.comparing(TicketDetails::getTicketId))
				.get());
		if (ticket.isEmpty()) {
			incTicketId = 1;
		} else {
			incTicketId = ticket.get().getTicketId()+1;
		}
		return incTicketId;
	}

	public Unreserved getSeatNo() {
		Optional<Unreserved> unr = unreservedList.stream().findFirst();
		if(unr.isPresent()) {
			 unreservedList.remove(unr.get());
			 return unr.get();
		}
		return null;
	}

	public List<Reserved> getAllReservedSeats() {
		return reservedList;
	}

	public Optional<TicketDetails> cancelTicketByTicketId(int ticketId) {
		Optional<TicketDetails> ticket = ticketList.stream().filter(t -> t.getTicketId()==ticketId).findFirst();
		if(ticket.isPresent()) {
			User user = ticket.get().getUser();
			Optional<Reserved> reserved = reservedList.stream().filter(i->i.getUser().equals(user)).findFirst();
			if(reserved.isPresent()) {
				int seatNo = reserved.get().getSeatNo();
				String section = reserved.get().getSection();
				unreservedList.add(new Unreserved(seatNo, section));
				reservedList.remove(reserved.get());
			}
			ticketList.remove(ticket.get());
			return ticket;
		}
		return ticket;
	}

	public boolean updateReservedSeat(UpdateReservedSeat newSeat) {
		Unreserved unr = Unreserved.builder().seatNo(newSeat.getNewSeatNo()).section(newSeat.getNewSection()).build();
		boolean available = unreservedList.contains(unr);
		if(available) {
			Optional<User> user = userList.stream().filter(i->i.getUserId().equalsIgnoreCase(newSeat.getUserId())).findFirst();
			if(user.isPresent()) {
				Reserved res = Reserved.builder().seatNo(unr.getSeatNo()).section(unr.getSection()).user(user.get()).build();
				reservedList.add(res);
			}
		}
		return available;
	}

	public User addUser(User user) {
		Optional<User> usr = userList.stream().filter(u->u.getUserId().equalsIgnoreCase(user.getUserId())).findFirst();
		if(usr.isEmpty()) {
			userList.add(user);
			return user;
		}
	return usr.get();
	}
	
	

}
