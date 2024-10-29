package bg.softuni._17_jsonexercise.service.impl;

import bg.softuni._17_jsonexercise.data.entities.Category;
import bg.softuni._17_jsonexercise.data.entities.Product;
import bg.softuni._17_jsonexercise.data.repositories.CategoryRepository;
import bg.softuni._17_jsonexercise.service.CategoryService;
import bg.softuni._17_jsonexercise.service.dtos.export.CategoryByProductsDTO;
import bg.softuni._17_jsonexercise.service.dtos.imports.CategorySeedDTO;
import bg.softuni._17_jsonexercise.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private static final String FILE_PATH = "src/main/resources/json/categories.json";
    private final CategoryRepository categoryRepository;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, Gson gson, ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }
    @Override
    public void seedCategories() throws FileNotFoundException {
        if (this.categoryRepository.count() == 0){
            CategorySeedDTO[] categorySeedDTOS = this.gson.fromJson(new FileReader(FILE_PATH), CategorySeedDTO[].class);

            for (CategorySeedDTO categorySeedDTO : categorySeedDTOS){
                if (!this.validationUtil.isValid(categorySeedDTO)){
                    this.validationUtil.getViolations(categorySeedDTO)
                            .forEach(v -> System.out.println(v.getMessage()));
                } else {
                    Category category = this.modelMapper.map(categorySeedDTO, Category.class);
                    this.categoryRepository.saveAndFlush(category);
                }
            }
        }
    }

    @Override
    public List<CategoryByProductsDTO> getAllCategoriesByProducts() {
        return this.categoryRepository.findAllByCategoriesByProducts()
                .stream()
                .map(c -> {
                    CategoryByProductsDTO dto = modelMapper.map(c, CategoryByProductsDTO.class);
                    dto.setProductsCount(c.getProducts().size());
                    BigDecimal sum = c.getProducts().stream().map(Product::getPrice).reduce(BigDecimal::add).get();
                    dto.setTotalRevenue(sum);
                    dto.setAveragePrice(sum.divide(BigDecimal.valueOf(c.getProducts().size()), MathContext.DECIMAL32));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void printAllCategoriesByProducts() {
        System.out.println(this.gson.toJson(getAllCategoriesByProducts()));
    }
}
