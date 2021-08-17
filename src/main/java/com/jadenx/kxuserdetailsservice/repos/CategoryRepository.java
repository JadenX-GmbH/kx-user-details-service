package com.jadenx.kxuserdetailsservice.repos;

import com.jadenx.kxuserdetailsservice.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository extends JpaRepository<Category, Long> {
}
