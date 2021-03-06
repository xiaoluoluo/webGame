package net.mengkang.entity;

import io.netty.channel.Channel;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by zhoumengkang on 16/7/2.
 */
public class Client {

    private String clientId;

    private String fromId;
    private String wxId;

    private int IsFriendRoom; // 是否在朋友房间

    private Channel channel;
    private String username;
    private Integer hp;
    private String userInfo;
    private String otherInfo;


    public String getClientId() {
        return clientId;
    }

    public void setClientId(String id) {
        this.clientId = id;
    }


    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getWxId() {
        return wxId;
    }

    public void setWxId(String wxId) {
        this.wxId = wxId;
    }

    public int IsFriendRoom() {
        return IsFriendRoom;
    }

    public void IsFriendRoom(int IsFriendRoom) {
        this.IsFriendRoom = IsFriendRoom;
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

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }


}
