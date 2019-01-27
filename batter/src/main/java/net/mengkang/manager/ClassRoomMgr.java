package net.mengkang.manager;

import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
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
