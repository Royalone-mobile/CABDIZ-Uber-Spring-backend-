package com.farebid.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.farebid.userservice.entity.UserFile;
import com.farebid.userservice.entity.UserFileType;

public interface UserFileRepository extends JpaRepository<UserFile, Long>{

  UserFile findByFileName(String fileName);
  UserFile findByEmailAndFileType(String email, UserFileType fileType);
  
  @Modifying
  @Transactional
  @Query("update UserFile f set f.userId = :userId, f.email = :email where f.fileName = :fileName")
  int update(@Param("email") String email, @Param("userId") Long userId, @Param("fileName") String fileName);
}
