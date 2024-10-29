package bg.softuni._17_jsonexercise.service.dtos.export;

import com.google.gson.annotations.Expose;

import java.util.List;

public class UserSoldProductsDTO {
    @Expose
    private String firstName;
    @Expose
    private String lastName;
    @Expose
    private List<ProductSoldDTO> soldProducts;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<ProductSoldDTO> getSoldProducts() {
        return soldProducts;
    }

    public void setSoldProducts(List<ProductSoldDTO> soldProducts) {
        this.soldProducts = soldProducts;
    }
}
