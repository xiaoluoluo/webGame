package net.mengkang.manager;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import net.mengkang.entity.Client;
import net.mengkang.service.BaseService;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by luoxiaosong on 2018/2/22.
 */
public class ClientMgr {

    private static Map<String, Client> allClient = new ConcurrentHashMap<String, Client>();

    public static Client getClient(String clientId){
        return allClient.get(clientId);
    }

    public static void addClient(Client client){
        allClient.put(client.getClientId(),client);


    }

    public static Client createClient(Channel channel,String requestString){

        JSONObject json = new JSONObject(requestString);
        String userName= (String) json.get("user");
        Integer hp = (Integer) json.get("hp");
//        Float direction = (Float) json.get("direction");

        Client newClient = new Client();
        newClient.setClientId(channel.id().asLongText());
        newClient.setUsername(userName);
        newClient.setHp(hp);
        newClient.setChannel(channel);
        return newClient;
    }


    public static void removeClient(String clientId){
        allClient.remove(clientId);
    }


    public static void sendMessageToClient(Channel channel, Client client , boolean isFirstEnter){
        String clientString = clientToString(client);
        Long messageId = 1L;   // 别人进入游戏了
        if (isFirstEnter){
            messageId = 111L;  // 我进入游戏了
        }
        String message = MessMgr.createMessage(0,"",messageId, clientString);
        channel.writeAndFlush(new TextWebSocketFrame(message));
    }


    public static void sendMoveMessageToClient(Channel channel, String moveInfo ){
        //有人移动
        String message = MessMgr.createMessage(0,"",2, moveInfo);
        channel.writeAndFlush(new TextWebSocketFrame(message));
    }


    public static void sendkissOthersMessageToClient(Channel channel, String kissMessage ){
        //有人在杀人
        String message = MessMgr.createMessage(0,"",3, kissMessage);
        channel.writeAndFlush(new TextWebSocketFrame(message));
    }





    public static void sendMessageToAllClient(Channel channel,Client client ){

        for (String clientId :allClient.keySet()){
            if (clientId.equals(client.getClientId())){
                continue;
            }
            Client c = allClient.get(clientId);
            if (c != null){
                // 把别人信息发给我
                sendMessageToClient(channel,c,false);
                //把我信息发给给别人
                sendMessageToClient(c.getChannel(),client,false);
            }
        }
    }


    public static void sendMoveMessage(String  clientId,Double x,Double y,Double direction){

        for (String cId :allClient.keySet()){
            if (cId.equals(clientId)){
                continue;
            }
            Client c = allClient.get(cId);
            if (c != null){
                sendMoveMessageToClient(c.getChannel(),moveToString(clientId,x,y,direction));
            }
        }
    }

    public static void sendKillOther(String  clientId,String  targetClientId,Integer hp){

        Client client = allClient.get(clientId);
        if (client == null){
            return;
        }
        Integer overHp = 0;
        if (hp > 0){
            Client targetClient = allClient.get(targetClientId);
            if (targetClient == null){
                return;
            }
            overHp = targetClient.getHp() - hp;
            if (overHp <= 0){
                // 广播 目标被砍死了
            }else {
                // 广播 目标掉血了
                targetClient.setHp(overHp);
//                targetClient.setHp(overHp);
            }
        }else {
            // 没有砍到人
            targetClientId = "";
        }
        for (String c :allClient.keySet()) {
            if (hp <= 0){
                // 如果是自己空晖一刀  消息不用发给自己
                if (c.equals(clientId)){
                    continue;
                }
            }
            Client cli = allClient.get(c);
            if (cli != null){
                sendkissOthersMessageToClient(cli.getChannel(),killOthersToString(clientId,targetClientId,overHp));
            }
        }
    }


    public static String  killOthersToString(String  clientId,String  targetClientId,Integer overHp){
        JSONObject clientObject = new JSONObject();
        clientObject.put("clientId",clientId);
        clientObject.put("targetClientId",targetClientId);
        clientObject.put("overHp",overHp);
        return clientObject.toString();
    }





    public static String  moveToString(String  clientId,Double x,Double y,Double direction){
        JSONObject clientObject = new JSONObject();
        clientObject.put("clientId",clientId);
        clientObject.put("x",x);
        clientObject.put("y",y);
        clientObject.put("direction",direction);

        return clientObject.toString();
    }


    public static String  clientToString(Client client){
        JSONObject clientObject = new JSONObject();
        clientObject.put("userName",client.getUsername());
        clientObject.put("id",client.getClientId());
        return clientObject.toString();
    }




}
