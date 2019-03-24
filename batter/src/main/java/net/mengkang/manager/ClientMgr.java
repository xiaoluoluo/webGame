package net.mengkang.manager;

import io.netty.channel.Channel;
import net.mengkang.entity.Client;
import net.mengkang.entity.RoomInfo;
import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by luoxiaosong on 2018/2/22.
 */
public class ClientMgr {

    // wxid  roomId
    private static Map<String, Integer> allWxIdClient = new ConcurrentHashMap<String, Integer>();
    // cid  roomId
    private static Map<String, Integer> allCidClient = new ConcurrentHashMap<String, Integer>();


    public static Integer addClient(Client client){

        int isFriendRoom = 0;
        RoomInfo roomInfo = ClassRoomMgr.getAbleRoom();
        // 如果他的来源id 不是为0 那么就取他的来源ID所对应的房间号
        if (!client.getFromId().equals("0")){
            Integer rooId = allWxIdClient.get(client.getFromId());
            if (rooId != null){
                isFriendRoom = 1;
                roomInfo = ClassRoomMgr.getRoomInfo(rooId);
                if (roomInfo != null &&roomInfo.isNumMax() ){
                    return 0;
                }
            }else {
                roomInfo = null;
            }
        }
        if (roomInfo == null || roomInfo.isNumMax()){
            isFriendRoom = 0;
            roomInfo = ClassRoomMgr.createRoom();
            ClassRoomMgr.getAllRoom().put(roomInfo.getRoomId(),roomInfo);
        }
        client.IsFriendRoom(isFriendRoom);
        roomInfo.getClientMap().put(client.getClientId(),client);
        allWxIdClient.put(client.getWxId(),roomInfo.getRoomId());
        allCidClient.put(client.getClientId(),roomInfo.getRoomId());
        return 1;
    }

    public static Client createClient(Channel channel,String requestString){

        JSONObject json = new JSONObject(requestString);
        String userName= (String) json.get("user");
        Integer hp = (Integer) json.get("hp");
        String userInfo= (String) json.get("userInfo");

        String fromWxId= (String) json.get("fromWxId");
        String wxId= (String) json.get("wxId");

        Client newClient = new Client();
        newClient.setClientId(channel.id().asLongText());

        newClient.setWxId(wxId);
        newClient.setFromId(fromWxId);
        newClient.setUsername(userName);
        newClient.setUserInfo(userInfo);
        newClient.setHp(hp);
        newClient.setChannel(channel);
        return newClient;
    }

    public static void removeClient(String clientId){

        Integer roomId= allCidClient.get(clientId);
        if (roomId == null){
            return;
        }
        RoomInfo roomInfo = ClassRoomMgr.getRoomInfo(roomId);
        if (roomInfo == null) {
            return;
        }
        Client client = roomInfo.getClientMap().get(clientId);
        if (client == null){
            return;
        }
        roomInfo.getClientMap().remove(clientId);
        if (roomInfo.isEmpty()){
            ClassRoomMgr.getAllRoom().remove(roomInfo.getRoomId());
        }
        allWxIdClient.remove(client.getWxId());
        allCidClient.remove(clientId);
    }

    //--------------------------------------------------------------------------

    public static void sendMessageToAllClient(Channel channel,Client client ){

        Integer roomId = allCidClient.get(client.getClientId());
        if (roomId == null){
            return;
        }
        RoomInfo roomInfo = ClassRoomMgr.getRoomInfo(roomId);
        if (roomInfo == null){
            return;
        }

        for (String clientId :roomInfo.getClientMap().keySet()){
            if (clientId.equals(client.getClientId())){
                continue;
            }
            Client c = roomInfo.getClientMap().get(clientId);
            if (c != null){
                // 把别人信息发给我
                MessMgr.sendMessageToClient(channel,ClientCodeEnum.OtherEnterGame.getCode(),clientToString(c));
                //把我信息发给给别人
                MessMgr.sendMessageToClient(c.getChannel(),ClientCodeEnum.OtherEnterGame.getCode(),clientToString(client));
            }
        }
    }


    public static void sendMoveMessage(String  clientId,Double x,Double y,Double direction,String userInfo){


        Integer roomId = allCidClient.get(clientId);
        if (roomId == null){
            return;
        }
        RoomInfo roomInfo = ClassRoomMgr.getRoomInfo(roomId);
        if (roomInfo == null){
            return;
        }
        for (String cId :roomInfo.getClientMap().keySet()){
            if (cId.equals(clientId)){
                Client cc = roomInfo.getClientMap().get(clientId);
                if (cc != null){
                    cc.setUserInfo(userInfo);
                }
                continue;
            }
            Client c = roomInfo.getClientMap().get(cId);
            if (c != null){
                MessMgr.sendMessageToClient(c.getChannel(),ClientCodeEnum.Move.getCode(),moveToString(clientId,x,y,direction,userInfo));
            }
        }
    }

    public static void sendKillOther(String  clientId,String  targetClientId,Integer hp){

        Integer roomId = allCidClient.get(clientId);
        if (roomId == null){
            return;
        }
        RoomInfo roomInfo = ClassRoomMgr.getRoomInfo(roomId);
        if (roomInfo == null){
            return;
        }

        Client client = roomInfo.getClientMap().get(clientId);
        if (client == null){
            return;
        }
        Integer overHp = 0;
        if (hp > 0){
            Client targetClient = roomInfo.getClientMap().get(targetClientId);
            if (targetClient == null){
                return;
            }
            overHp = targetClient.getHp() - hp;
            if (overHp <= 0){
                // 广播 目标被砍死了
            }else {
                // 广播 目标掉血了
                targetClient.setHp(overHp);
            }
        }else {
            // 没有砍到人
            targetClientId = "";
        }
        for (String c :roomInfo.getClientMap().keySet()) {
            if (hp <= 0){
                // 如果是自己空晖一刀  消息不用发给自己
                if (c.equals(clientId)){
                    continue;
                }
            }
            Client cli = roomInfo.getClientMap().get(c);
            if (cli != null){
                MessMgr.sendMessageToClient(cli.getChannel(),ClientCodeEnum.Kill.getCode(),killOthersToString(clientId,targetClientId,overHp));
            }
        }
    }


    public static void sendUserSprint(String  clientId){

        Integer roomId = allCidClient.get(clientId);
        if (roomId == null){
            return;
        }
        RoomInfo roomInfo = ClassRoomMgr.getRoomInfo(roomId);
        if (roomInfo == null){
            return;
        }

        Client client = roomInfo.getClientMap().get(clientId);
        if (client == null){
            return;
        }
        for (String c :roomInfo.getClientMap().keySet()) {
            if (c.equals(clientId)){
                continue;
            }
            Client cli = roomInfo.getClientMap().get(c);
            if (cli != null){
                MessMgr.sendMessageToClient(cli.getChannel(),ClientCodeEnum.Sprint.getCode(),sprintToString(clientId));
            }
        }
    }



    //-----------------------------------------------------------------------------------


    public static String  sprintToString(String  clientId){
        JSONObject clientObject = new JSONObject();
        clientObject.put("spClientId",clientId);
        return clientObject.toString();
    }

    public static String  killOthersToString(String  clientId,String  targetClientId,Integer overHp){
        JSONObject clientObject = new JSONObject();
        clientObject.put("clientId",clientId);
        clientObject.put("targetClientId",targetClientId);
        clientObject.put("overHp",overHp);
        return clientObject.toString();
    }

    public static String  moveToString(String  clientId,Double x,Double y,Double direction,String userInfo){
        JSONObject clientObject = new JSONObject();
        clientObject.put("clientId",clientId);
        clientObject.put("x",x);
        clientObject.put("y",y);
        clientObject.put("direction",direction);
        clientObject.put("userInfo",userInfo);


        return clientObject.toString();
    }


    public static String  clientToString(Client client){
        JSONObject clientObject = new JSONObject();
        clientObject.put("userName",client.getUsername());
        clientObject.put("id",client.getClientId());
        clientObject.put("userInfo",client.getUserInfo());
        clientObject.put("IsFriendRoom",client.IsFriendRoom());

        return clientObject.toString();
    }




}
