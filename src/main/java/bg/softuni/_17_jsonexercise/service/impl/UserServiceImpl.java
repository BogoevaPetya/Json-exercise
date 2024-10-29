package bg.softuni._17_jsonexercise.service.impl;

import bg.softuni._17_jsonexercise.data.entities.User;
import bg.softuni._17_jsonexercise.data.repositories.UserRepository;
import bg.softuni._17_jsonexercise.service.UserService;
import bg.softuni._17_jsonexercise.service.dtos.export.*;
import bg.softuni._17_jsonexercise.service.dtos.imports.UserSeedDTO;
import bg.softuni._17_jsonexercise.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private static final String FILE_PATH = "src/main/resources/json/users.json";
    private final UserRepository userRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final Gson gson;

    public UserServiceImpl(UserRepository userRepository, ValidationUtil validationUtil, ModelMapper modelMapper, Gson gson) {
        this.userRepository = userRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.gson = gson;
    }

    @Override
    public void seedUsers() throws FileNotFoundException {
        if (this.userRepository.count() == 0){

            UserSeedDTO[] userSeedDTOS = this.gson.fromJson(new FileReader(FILE_PATH), UserSeedDTO[].class);

            for (UserSeedDTO userSeedDTO : userSeedDTOS){
                if (!this.validationUtil.isValid(userSeedDTO)){
                    this.validationUtil.getViolations(userSeedDTO).stream().forEach(v -> System.out.println(v.getMessage()));
                } else {
                    User user = this.modelMapper.map(userSeedDTO, User.class);
                    this.userRepository.saveAndFlush(user);
                }
            }

        }
    }

    @Override
    public List<UserSoldProductsDTO> getAllUsersAndItems() {
        List<UserSoldProductsDTO> allUsers = this.userRepository.findAll()
                .stream()
                .filter(u -> u.getSold().stream().anyMatch(p -> p.getBuyer() != null))
                .map(u -> {
                    UserSoldProductsDTO userDTO = this.modelMapper.map(u, UserSoldProductsDTO.class);
                    List<ProductSoldDTO> soldProductsDTO = u.getSold()
                            .stream()
                            .filter(p -> p.getBuyer() != null)
                            .map(p -> this.modelMapper.map(p, ProductSoldDTO.class))
                            .collect(Collectors.toList());
                    userDTO.setSoldProducts(soldProductsDTO);
                    return userDTO;
                })
                .sorted(Comparator.comparing(UserSoldProductsDTO::getLastName).thenComparing(UserSoldProductsDTO::getFirstName))
                .toList();

        return allUsers;

    }

    @Override
    public void printAllUsersAndSoldItems() {

        String json = this.gson.toJson(getAllUsersAndItems());
        System.out.println(json);
    }

    @Override
    public UserAndProductsDTO getUserAndProductDTO() {
        UserAndProductsDTO userAndProductsDTO = new UserAndProductsDTO();
        List<UserSoldDTO> userSoldDTOS = this.userRepository.findAll()
                .stream()
                .filter(user -> !user.getSold().isEmpty())
                .map(user -> {
                    UserSoldDTO userSoldDTO = this.modelMapper.map(user, UserSoldDTO.class);
                    ProductSoldByUserDTO productSoldByUserDTO = this.modelMapper.map(user, ProductSoldByUserDTO.class);
                    List<ProductInfoDTO> productInfoDTOS = user.getSold().stream().map(product -> modelMapper.map(product, ProductInfoDTO.class)).collect(Collectors.toList());
                    productSoldByUserDTO.setProducts(productInfoDTOS);
                    productSoldByUserDTO.setCount(productInfoDTOS.size());
                    return userSoldDTO;
                })
                .collect(Collectors.toList());
        userAndProductsDTO.setUsers(userSoldDTOS);
        userAndProductsDTO.setUsersCount(userSoldDTOS.size());
        return userAndProductsDTO;
    }

    @Override
    public void printUserAndProductDTO() {
        System.out.println(this.gson.toJson(getUserAndProductDTO()));
    }
}
