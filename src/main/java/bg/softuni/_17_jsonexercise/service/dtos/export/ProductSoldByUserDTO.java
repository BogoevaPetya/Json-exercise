package bg.softuni._17_jsonexercise.service.dtos.export;

import bg.softuni._17_jsonexercise.data.entities.Product;
import com.google.gson.annotations.Expose;

import java.util.List;

public class ProductSoldByUserDTO {
    @Expose
    private int count;
    @Expose
    private List<ProductInfoDTO> products;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ProductInfoDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductInfoDTO> products) {
        this.products = products;
    }
}
