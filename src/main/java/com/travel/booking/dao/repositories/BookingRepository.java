package com.travel.booking.dao.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.travel.booking.dao.entities.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
	
	@Query(" select b from Booking b where date= :specifiedDate")
	public List<Booking> getBookingByDate(@Param("specifiedDate") Date specifiedDate);


}
