package bg.softuni._17_jsonexercise.service;

import bg.softuni._17_jsonexercise.service.dtos.export.UserAndProductsDTO;
import bg.softuni._17_jsonexercise.service.dtos.export.UserSoldProductsDTO;

import java.io.FileNotFoundException;
import java.util.List;

public interface UserService {

    void seedUsers() throws FileNotFoundException;

    List<UserSoldProductsDTO> getAllUsersAndItems();

    void printAllUsersAndSoldItems();

    UserAndProductsDTO getUserAndProductDTO();

    void printUserAndProductDTO();
}
