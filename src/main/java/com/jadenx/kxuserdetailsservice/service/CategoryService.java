package com.jadenx.kxuserdetailsservice.service;

import com.jadenx.kxuserdetailsservice.model.CategoryDTO;

import java.util.List;


public interface CategoryService {

    List<CategoryDTO> findAll();

    CategoryDTO get(final Long id);

    Long create(final CategoryDTO categoryDTO);

    void update(final Long id, final CategoryDTO categoryDTO);

    void delete(final Long id);

}
