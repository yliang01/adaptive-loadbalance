package com.aliware.tianchi;


import org.apache.dubbo.rpc.listener.CallbackListener;

/**
 * @author daofeng.xjf
 * <p>
 * 客户端监听器
 * 可选接口
 * 用户可以基于获取获取服务端的推送信息，与 CallbackService 搭配使用
 */
public class CallbackListenerImpl implements CallbackListener {

    @Override
    public void receiveServerMsg(String msg) {
        String[] split = msg.split("-");
        for (HiveInvokerInfo info : HiveCommon.infoMap.values()) {
            if (info.name.equals(split[0])) {
//                info.maxPendingRequest = split[1];
                System.out.println(Integer.valueOf(split[2])/Integer.valueOf(split[3]));
                break;
            }
        }

        HiveCommon.initCallBack();
        System.out.println("receive msg from server :" + msg);
    }
}