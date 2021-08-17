package com.jadenx.kxuserdetailsservice.repos;

import com.jadenx.kxuserdetailsservice.domain.Details;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DetailsRepository extends JpaRepository<Details, Long> {
}
