package net.mengkang.manager;

import io.netty.channel.Channel;
import net.mengkang.entity.Client;
import org.json.JSONObject;

/**
 * Created by luoxiaosong on 2018/2/22.
 */
public class MessMgr {

    public static void distribution(Channel channel, String request){

        if (!isJson(request)){
            System.out.println("收到" + request);
            return;
        }
        JSONObject json = new JSONObject(request);
        int codeId = (Integer) json.get("codeId");
        switch (codeId){
            case 10001:{
                // 广播 坐标
                String cid = (String) json.get("cid");
                Double x = (Double) json.get("x");
                Double y = (Double) json.get("y");
                Double direction = (Double) json.get("direction");
                ClientMgr.sendMoveMessage(cid,x,y,direction);
                return;
            }
            case 10002:{
                String targetClientId = (String) json.get("cid");
                Integer hp = (Integer) json.get("hp");
                ClientMgr.sendKillOther(channel.id().asLongText(),targetClientId,hp);
                return;
            }
            case 10102:{



                return;
            }
            case 10103:{




                return;
            }




        }
    }

    public static String createMessage(int errorCode,String errorMess,long messageId, String message) {
        JSONObject msg = new JSONObject();
        msg.put("errorCode",errorCode);
        msg.put("errorMess",errorMess);
        msg.put("messageId",messageId);
        msg.put("data",message);
        return msg.toString();
    }


    public static boolean isJson(String content) {
        try {
            new JSONObject(content);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
