package bg.softuni._17_jsonexercise.data.repositories;

import bg.softuni._17_jsonexercise.data.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Set<Product> findAllByPriceBetweenAndBuyerIsNull(BigDecimal from, BigDecimal to);
}
