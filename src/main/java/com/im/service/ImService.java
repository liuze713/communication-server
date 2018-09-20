package com.im.service;

import com.im.repository.Imrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 *
 * @author 刘泽 liuze713@gmail.com
 * @description TODO
 * @date 2018-9-19 16:50
 */
@Service
public class ImService {

    @Autowired
    Imrepository imrepository;

    public void addLogin(String username, String groupname) {
        imrepository.addLogin(username, groupname);
    }
}
