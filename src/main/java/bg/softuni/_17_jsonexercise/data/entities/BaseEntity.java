package bg.softuni._17_jsonexercise.data.entities;

import jakarta.persistence.*;

@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    protected BaseEntity(){}

    public Long getId() {
        return id;
    }
}
