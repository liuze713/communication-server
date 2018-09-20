package com.im.service;

import com.im.repository.MsgsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 *
 * @author 刘泽 liuze713@gmail.com
 * @description TODO
 * @date 2018-9-20 11:22
 */
@Service
public class MsgsService {
    @Autowired
    MsgsRepository msgsRepository;

    /**
     * @param username
     * @param msg
     * @param goname
     * @param gouserorgroup 1 发送给人 2 发送组
     * @return void
     * @description TODO
     * @author 刘泽
     * @date 2018-9-20 11:31
     */
    @Async
    public void addMsgs(String username, String msg, String goname, Integer gouserorgroup, String ip, Integer msgtype) throws Exception {
        msgsRepository.addMsgs(username, msg, goname, gouserorgroup, ip, msgtype);
    }
}
