package com.im.repository;

import com.im.entity.LoginEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author 刘泽 liuze713@gmail.com
 * @description TODO
 * @date 2018-9-19 16:46
 */
@Repository
public interface Imrepository extends JpaRepository<LoginEntity, String> {
    @Modifying
    @Query(value = "insert into login (username, groupname ,id, logintime) values (?1,?2,uuid(),sysdate())", nativeQuery = true)
    @Transactional(rollbackFor = Exception.class)
    void addLogin(String username, String groupname);
}