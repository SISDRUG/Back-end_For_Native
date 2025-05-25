package com.example.demo.Repositorys.Repository;

import com.example.demo.Repositorys.Entity.LoginDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface LoginDetailRepository extends JpaRepository<LoginDetail, Long> {
    @Modifying(clearAutomatically = true)
    @Transactional(readOnly = false)
    @Query("DELETE FROM LoginDetail l WHERE l.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    @Modifying(clearAutomatically = true)
    @Transactional(readOnly = false)
    @Query(value = "DELETE FROM login_details WHERE user_id = :userId", nativeQuery = true)
    void deleteByUserIdNative(@Param("userId") Long userId);
}