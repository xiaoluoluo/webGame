package net.mengkang.entity;

import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by luoxiaosong on 2018/3/3.
 */
public class RoomInfo {

    private static final int roomUserNum = 2;
    private static final int roomUserInviteNum = 4;

    //<cid,Client>
    private Map<String,Client> clientMap = new ConcurrentHashMap<String, Client>(roomUserNum);


    private int roomId;



    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public  Map<String,Client > getClientMap() {
        return clientMap;
    }

    public boolean isNumMax (){
        return clientMap.size() >= roomUserNum;
    }

    public boolean isInviteNumMax (){
        return clientMap.size() >= roomUserInviteNum;
    }

    public boolean isEmpty (){
        return clientMap.isEmpty();
    }


}
