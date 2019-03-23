package net.mengkang.manager;

import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import net.mengkang.entity.Client;
import net.mengkang.entity.RoomInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by luoxiaosong on 2018/3/18.
 */
public class ClassRoomMgr {

    private static AtomicLong CONCURRENT_INTEGER = new AtomicLong(0);

    // 管理所有的房间
   private static Map<Integer,RoomInfo> roomMap =  new ConcurrentHashMap();

   // 获取一个可以进入的房间
   public static RoomInfo getAbleRoom(){
       if (roomMap.isEmpty()){
           return null;
       }
       for (RoomInfo room:roomMap.values()) {
           if (room.isNumMax()){
               continue;
           }
           // 如果这两个房间的人是好友邀请过来的 就没有可用的房间
           String fromWeiXinId = "";
           String WeiXinId = "";
           //房间内是否是好友关系
           boolean isFriend = false;
          for (Client c :room.getClientMap().values()){
              if (room.getClientMap().size()>2){
                  continue;
              }
              if (fromWeiXinId== "" && WeiXinId==""){
                  WeiXinId = c.getWxId();
                  fromWeiXinId =c.getFromId();
                  continue;
              }
              if (c.getWxId()== fromWeiXinId || c.getFromId()==WeiXinId){
                  isFriend = true;
              }
          }
          if (isFriend){
              continue;
          }

           return room;
       }
       return null;
   }


   //获取房间信息
    public static  RoomInfo getRoomInfo(Integer roomId){
        return roomMap.get(roomId);
    }


    public static Map<Integer,RoomInfo> getAllRoom(){
       return roomMap;
    }


    public static RoomInfo createRoom(){
        RoomInfo info = new RoomInfo();
        info.setRoomId(getRoomId());
        return info;
    }



    public static int getRoomId(){
        return (int)CONCURRENT_INTEGER.getAndIncrement();
    }




}
