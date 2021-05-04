package com.travel.booking.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.travel.booking.dao.entities.Hiker;
@Repository
public interface HikerRepository extends JpaRepository<Hiker, String> {
	
	

}
