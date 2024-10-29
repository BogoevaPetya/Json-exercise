package bg.softuni._17_jsonexercise.service;

import bg.softuni._17_jsonexercise.service.dtos.export.CategoryByProductsDTO;

import java.io.FileNotFoundException;
import java.util.List;

public interface CategoryService {

    void seedCategories() throws FileNotFoundException;

    List<CategoryByProductsDTO> getAllCategoriesByProducts();

    void printAllCategoriesByProducts();
}
