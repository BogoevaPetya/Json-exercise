package bg.softuni._17_jsonexercise.service.dtos.export;

import bg.softuni._17_jsonexercise.data.entities.User;
import com.google.gson.annotations.Expose;

import java.util.List;

public class UserAndProductsDTO {
    @Expose
    private int usersCount;
    @Expose
    private List<UserSoldDTO> users;

    public int getUsersCount() {
        return usersCount;
    }

    public void setUsersCount(int usersCount) {
        this.usersCount = usersCount;
    }

    public List<UserSoldDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserSoldDTO> users) {
        this.users = users;
    }
}
