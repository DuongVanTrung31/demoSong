package cg.service;

import cg.model.Category;

import java.util.Optional;

public interface ICategoryService {
    Iterable<Category> findAll();
    Category save(Category category);
    void delete(Long id);
    Optional<Category> findOne(Long id);
}
