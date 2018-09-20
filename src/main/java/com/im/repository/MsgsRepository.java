package com.im.repository;

import com.im.entity.MsgsEntity;
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
 * @date 2018-9-20 11:17
 */
@Repository
public interface MsgsRepository extends JpaRepository<MsgsEntity, String> {
    @Modifying
    @Query(value = "insert into msgs (id, username, msg, goname, gouserorgroup, creattime,ip,msgtype) values (uuid(),?1,?2,?3,?4,sysdate(),?5,?6)", nativeQuery = true)
    @Transactional(rollbackFor = Exception.class)
    void addMsgs(String username, String msg, String goname, Integer gouserorgroup, String ip, Integer msgtype);
}
