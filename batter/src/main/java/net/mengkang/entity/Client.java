package net.mengkang.entity;

import io.netty.channel.Channel;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by zhoumengkang on 16/7/2.
 */
public class Client {

    private String clientId;
    private long roomId;

    private Channel channel;
    private String username;
    private Integer hp;
    private String userInfo;



    public String getClientId() {
        return clientId;
    }

    public void setClientId(String id) {
        this.clientId = id;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public Channel getChannel(){
        return channel;
    }

    public void setChannel(Channel channel){
        this.channel = channel;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public Integer getHp() {
        return hp;
    }

    public void setHp(Integer hp) {
        this.hp = hp;
    }


    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }
}
