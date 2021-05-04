package com.travel.booking.domain.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.travel.booking.dao.entities.Booking;
import com.travel.booking.dao.entities.Status;
import com.travel.booking.dao.entities.Trail;
import com.travel.booking.dao.repositories.BookingRepository;
import com.travel.booking.dao.repositories.HikerRepository;
import com.travel.booking.domain.exception.AgeException;
import com.travel.booking.domain.exception.AgeHikerInvalidException;
import com.travel.booking.domain.exception.InvalidDateException;
import com.travel.booking.domain.exception.InvalidTrailException;
import com.travel.booking.domain.exception.UnknownHikerException;
import com.travel.booking.domain.mapper.BookingMapper;

@Service
public class BookingService {

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	HikerRepository hikerRepository;

	public List<Booking> getAllBooking() {

		return bookingRepository.findAll();
	}

	@Transactional
	public Booking createBooking(Booking b,Date d) throws InvalidDateException, AgeException {
		
		
		if (b.getDate().before(d)) {
			throw new InvalidDateException("Invalid Date inferior to now!!");
		}

		AgeException exception = new AgeException(new ArrayList<>());
		b.getHikers().stream().forEach(hiker -> {
			if (hiker.getAgeHiker() < b.getTrail().getStartAge() || hiker.getAgeHiker() > b.getTrail().getMaxAge()) {
				exception.getExceptions().add(new AgeHikerInvalidException(hiker.getName()));
			}
		});
		if (exception.getExceptions().size() > 0)
			throw exception;
		hikerRepository.saveAll(b.getHikers());
		return bookingRepository.save(b);
	}

	public void cancelBooking(String date, String name, String trailName)
			throws UnknownHikerException, InvalidTrailException {
		List<Booking> bookings = bookingRepository.findAll();

		Trail trail = BookingMapper.stringToTrail(trailName);
		List<Booking> bookingByDate  = bookings.stream().filter(booking -> {
			try {
				return booking.getDate().getTime()==BookingMapper.stringdateToDate(date).getTime();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				return false;
			}
		}).collect(Collectors.toList());

		List<Booking> bookingByTrail = bookingByDate.stream().filter(booking -> booking.getTrail().equals(trail))

				.collect(Collectors.toList());
		List<Booking> bookingsTocancel = new ArrayList<>();
		for (Booking b : bookingByTrail) {
			if (b.getHikers().stream().map(hiker -> hiker.getName()).collect(Collectors.toList()).contains(name))
				bookingsTocancel.add(b);

		}
		bookingsTocancel.forEach(booking -> booking.setBookingStatus(Status.CANCELED));
		bookingRepository.saveAll(bookingsTocancel);
	}

	public List<Booking> getBookingByDate(Date date) {
		return bookingRepository.getBookingByDate(date);
	}

	public List<Booking> getBookingByHiker(String name) throws UnknownHikerException {
			return bookingRepository.findAll().stream().filter(booking -> booking.getHikers().stream().map(hiker -> hiker.getName()).collect(Collectors.toList()).contains(name))
					.collect(Collectors.toList());

		

	}

}
