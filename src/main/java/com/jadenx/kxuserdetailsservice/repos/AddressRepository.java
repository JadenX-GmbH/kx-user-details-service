package com.jadenx.kxuserdetailsservice.repos;

import com.jadenx.kxuserdetailsservice.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AddressRepository extends JpaRepository<Address, Long> {
}
