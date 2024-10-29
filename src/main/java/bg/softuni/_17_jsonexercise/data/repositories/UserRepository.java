package bg.softuni._17_jsonexercise.data.repositories;

import bg.softuni._17_jsonexercise.data.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllBySoldBuyerIsNotNull();
}
