package com.travel.booking.api.controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.travel.booking.api.dto.BookingDto;
import com.travel.booking.dao.entities.Booking;
import com.travel.booking.domain.exception.AgeException;
import com.travel.booking.domain.exception.InvalidDateException;
import com.travel.booking.domain.exception.InvalidTrailException;
import com.travel.booking.domain.exception.UnknownHikerException;
import com.travel.booking.domain.mapper.BookingMapper;
import com.travel.booking.domain.service.BookingService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "Swagger2BookingController", description = "REST APIs For George Booking Travel")
@Controller
@RequestMapping("/bookings")
public class BookingController {

	@Autowired
	private BookingService bookingService;


	 @ApiOperation(value = "Create Booking ", response = Booking.class, tags = "createBooking")
	    @ApiResponses(value = { 
	            @ApiResponse(code = 200, message = "Suceess|OK"),
	            @ApiResponse(code = 401, message = "not authorized!"), 
	            @ApiResponse(code = 403, message = "forbidden!!!"),
	            @ApiResponse(code = 404, message = "not found!!!") })
	@PostMapping("/create")
	public ResponseEntity<Booking> createBooking(@RequestBody @Valid BookingDto dto)
			throws InvalidDateException, InvalidTrailException, AgeException, ParseException {
		DateFormat sdf = new SimpleDateFormat("yyyy/mm/DD");

		return new ResponseEntity<Booking>(
				bookingService.createBooking(BookingMapper.bookingDtoToBooking(dto), sdf.parse(sdf.format(new Date()))),
				HttpStatus.OK);
	}
	 @ApiOperation(value = "Get All Booking ", response = Booking.class,responseContainer = "List", tags = "getBookings")
	    @ApiResponses(value = { 
	            @ApiResponse(code = 200, message = "Suceess|OK"),
	            @ApiResponse(code = 401, message = "not authorized!"), 
	            @ApiResponse(code = 403, message = "forbidden!!!"),
	            @ApiResponse(code = 404, message = "not found!!!") })
	@GetMapping("/view/all")
	public ResponseEntity<List<Booking>> viewAllBooking() {

		return new ResponseEntity<List<Booking>>(bookingService.getAllBooking(), HttpStatus.OK);
	}

	 @ApiOperation(value = "Get All Booking By Date ",  response = Booking.class,responseContainer = "List", tags = "getBookingsByDate")
	    @ApiResponses(value = { 
	            @ApiResponse(code = 200, message = "Suceess|OK"),
	            @ApiResponse(code = 401, message = "not authorized!"), 
	            @ApiResponse(code = 403, message = "forbidden!!!"),
	            @ApiResponse(code = 404, message = "not found!!!") })
	@GetMapping("/view")
	public ResponseEntity<List<Booking>> viewByDate(@RequestParam(name = "date") String date)
			throws InvalidDateException {
		DateFormat sdf = new SimpleDateFormat("yyyy/mm/DD");
		try {
			Date selectedDate = sdf.parse(date);

			return new ResponseEntity<List<Booking>>(bookingService.getBookingByDate(selectedDate), HttpStatus.OK);
		} catch (ParseException e) {
			throw new InvalidDateException(
					date + " can be parsed to date format . Your date should be like this YYYY/MM/DD");
		}
	}

	 @ApiOperation(value = "Cancel Booking  ", response = String.class, tags = "cancelBooking")
	    @ApiResponses(value = { 
	            @ApiResponse(code = 200, message = "Suceess|OK"),
	            @ApiResponse(code = 401, message = "not authorized!"), 
	            @ApiResponse(code = 403, message = "forbidden!!!"),
	            @ApiResponse(code = 404, message = "not found!!!") })
	@PutMapping("/cancel")
	public ResponseEntity<String> cancelBooking(@RequestParam(name = "date") String date,
			@RequestParam(name = "name") String name, @RequestParam(name = "trail") String trail)
			throws InvalidDateException, UnknownHikerException, InvalidTrailException {

		bookingService.cancelBooking(date, name, trail);

		return new ResponseEntity<String>("Booking Canceled", HttpStatus.OK);

	}


	 @ApiOperation(value = "List Bookings by Hiker Name  ", response = Booking.class,responseContainer = "List",  tags = "getBookingsByHiker")
	    @ApiResponses(value = { 
	            @ApiResponse(code = 200, message = "Suceess|OK"),
	            @ApiResponse(code = 401, message = "not authorized!"), 
	            @ApiResponse(code = 403, message = "forbidden!!!"),
	            @ApiResponse(code = 404, message = "not found!!!") })
	@GetMapping("/view/booking/{name}")
	public ResponseEntity<List<Booking>> hikerView(@PathVariable("name") String name) throws UnknownHikerException {

		return new ResponseEntity<List<Booking>>(bookingService.getBookingByHiker(name), HttpStatus.OK);
	}
}
