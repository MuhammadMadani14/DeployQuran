package com.example.quran.repository;

import com.example.quran.model.ERole;
import com.example.quran.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Boolean existsByEmail(String email);

    Optional<Users> findByEmail(String email);

    @Query(value = "SELECT * FROM users WHERE email = :email", nativeQuery = true)
    Users findByEmail1(@Param("email") String email);

    @Transactional
    @Modifying
    @Query(value = "update users set password = :password", nativeQuery = true)
    void changeUserPassword(@Param("password") String password);

    // Query untuk memperbarui kata sandi pengguna berdasarkan alamat email
    @Modifying
    @Transactional
    @Query("UPDATE Users u SET u.password = :newPassword WHERE u.email = :email")
    void updatePasswordByEmail(String email, String newPassword);

    List<Users> findByRolesName(ERole role);

}
