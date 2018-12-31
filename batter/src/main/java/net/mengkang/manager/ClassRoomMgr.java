package net.mengkang.manager;

import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import net.mengkang.entity.RoomInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by luoxiaosong on 2018/3/18.
 */
public class ClassRoomMgr {

    // 管理所有的房间
   private static Map<Long,RoomInfo> roomMap =  new ConcurrentHashMap();


   //获取房间信息
    public static  RoomInfo getRoomInfo(Long roomId){
        return roomMap.get(roomId);
    }

    //增加房间
    public static void addClassRoom(Long roomId, RoomInfo roomInfo){
        roomMap.put(roomId,roomInfo);
    }

    //发消息给房间其他成员
    public static void sendMessToRoomMember(boolean isTeacher, RoomInfo roomInfo, String message) {

        if (isTeacher && roomInfo.getStudentChannel() != null){
            // 如果是老师   就给学生发消息
            roomInfo.getStudentChannel().writeAndFlush(new TextWebSocketFrame(message));
        }
        if ((!isTeacher) && roomInfo.getTeacherChannel() != null){
            // 如果是学生   就给老师发消息
            roomInfo.getTeacherChannel().writeAndFlush(new TextWebSocketFrame(message));
        }
    }
}
