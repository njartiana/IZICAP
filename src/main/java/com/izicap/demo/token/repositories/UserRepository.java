package com.izicap.demo.token.repositories;

import com.izicap.demo.token.entities.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long>, JpaSpecificationExecutor<User> {
    List<User> findUserByUserName(String username);
}
