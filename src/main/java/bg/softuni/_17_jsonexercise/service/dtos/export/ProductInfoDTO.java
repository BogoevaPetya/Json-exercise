package bg.softuni._17_jsonexercise.service.dtos.export;

import com.google.gson.annotations.Expose;

import java.math.BigDecimal;

public class ProductInfoDTO {
    @Expose
    private String name;
    @Expose
    private BigDecimal price;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
