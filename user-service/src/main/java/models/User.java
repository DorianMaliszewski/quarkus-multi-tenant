package models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "test_user")
public class User extends BaseEntity {

    private String username;
    private String uuid;
}
