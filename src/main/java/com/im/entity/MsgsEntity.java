package com.im.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author 刘泽 liuze713@gmail.com
 * @description TODO
 * @date 2018-9-20 11:14
 */
@Entity
@Table(name = "login")
public class MsgsEntity {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;
    @Column(name = "username")
    private String username;
    @Column(name = "msg")
    private String msg;
    @Column(name = "goname")
    private String goname;
    @Column(name = "gouserorgroup")
    private Integer gouserorgroup;
    @Column(name = "creattime")
    private Date creattime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getGoname() {
        return goname;
    }

    public void setGoname(String goname) {
        this.goname = goname;
    }

    public Integer getGouserorgroup() {
        return gouserorgroup;
    }

    public void setGouserorgroup(Integer gouserorgroup) {
        this.gouserorgroup = gouserorgroup;
    }

    public Date getCreattime() {
        return creattime;
    }

    public void setCreattime(Date creattime) {
        this.creattime = creattime;
    }
}
