package com.riverlcn.cloud.user.dao;

import com.riverlcn.cloud.user.enity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author river
 */
public interface UserRepository extends JpaRepository<User, Long> {
}
