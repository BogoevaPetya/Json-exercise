package bg.softuni._17_jsonexercise.service.impl;

import bg.softuni._17_jsonexercise.data.entities.Category;
import bg.softuni._17_jsonexercise.data.entities.Product;
import bg.softuni._17_jsonexercise.data.entities.User;
import bg.softuni._17_jsonexercise.data.repositories.CategoryRepository;
import bg.softuni._17_jsonexercise.data.repositories.ProductRepository;
import bg.softuni._17_jsonexercise.data.repositories.UserRepository;
import bg.softuni._17_jsonexercise.service.ProductService;
import bg.softuni._17_jsonexercise.service.dtos.export.ProductInRangeDTO;
import bg.softuni._17_jsonexercise.service.dtos.imports.ProductSeedDTO;
import bg.softuni._17_jsonexercise.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private static final String FILE_PATH = "src/main/resources/json/products.json";
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;

    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void seedProducts() throws FileNotFoundException {
        if (this.productRepository.count() == 0){

            ProductSeedDTO[] productSeedDTOS = gson.fromJson(new FileReader(FILE_PATH), ProductSeedDTO[].class);
            for (ProductSeedDTO productSeedDTO : productSeedDTOS) {
                if (!this.validationUtil.isValid(productSeedDTO)){
                    this.validationUtil.getViolations(productSeedDTO).forEach(v -> System.out.println(v.getMessage()));
                } else {
                    Product product = modelMapper.map(productSeedDTO, Product.class);
                    product.setBuyer(getRandomUser(true));
                    product.setSeller(getRandomUser(false));
                    product.setCategories(getRandomCategories());

                    this.productRepository.saveAndFlush(product);
                }
            }
        }
    }

    @Override
    public List<ProductInRangeDTO> getAllProductsInRange(BigDecimal from, BigDecimal to) {
        return this.productRepository.findAllByPriceBetweenAndBuyerIsNull(from, to)
                .stream()
                .map(p -> {
                    ProductInRangeDTO dto = this.modelMapper.map(p, ProductInRangeDTO.class);
                    dto.setSeller(p.getSeller().getFirstName() + " " + p.getSeller().getLastName());
                    return dto;
                })
                .sorted((a, b) -> a.getPrice().compareTo(b.getPrice()))
                .collect(Collectors.toList());
    }

    @Override
    public void printAllProductsInRange(BigDecimal from, BigDecimal to) {
        System.out.println(this.gson.toJson(getAllProductsInRange(from, to)));
    }

    private Set<Category> getRandomCategories() {
        Set<Category> categories = new HashSet<>();
        long countCategories = categoryRepository.count();
        for (int i = 0; i < countCategories; i++) {
            long randomId = ThreadLocalRandom.current().nextLong(1, countCategories + 1);
            Category category = categoryRepository.findById(randomId).get();
            categories.add(category);
        }
        return categories;
    }

    private User getRandomUser(boolean isBuyer) {
        long count = this.userRepository.count();
        long randomId = ThreadLocalRandom.current().nextLong(1,  count + 1);
        return isBuyer && randomId % 4 == 0 ? null : this.userRepository.findById(randomId).get();
    }
}
