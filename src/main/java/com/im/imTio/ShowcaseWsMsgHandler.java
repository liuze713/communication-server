package com.im.imTio;


import com.alibaba.fastjson.JSONObject;
import com.im.service.ImService;
import com.im.service.MsgsService;
import com.im.utils.SqlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.tio.client.ClientGroupContext;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.utils.lock.SetWithLock;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;
import org.tio.websocket.common.WsSessionContext;
import org.tio.websocket.server.handler.IWsMsgHandler;

import javax.annotation.Resource;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static javax.swing.text.html.HTML.Tag.OL;

/**
 * @author 刘泽
 * 2017年6月28日 下午5:32:38
 */
@Component
public class ShowcaseWsMsgHandler implements IWsMsgHandler {
    private static ImService service;

    @Resource
    public void setWuDPushService(ImService service) {
        ShowcaseWsMsgHandler.service = service;
    }

    private static MsgsService addMagsAsync;

    @Resource
    public void setMsgsService(MsgsService addMagsAsync) {
        ShowcaseWsMsgHandler.addMagsAsync = addMagsAsync;
    }

    private static SqlUtil sqlUtil;

    @Resource
    public void setSqlUtil(SqlUtil sqlUtil) {
        ShowcaseWsMsgHandler.sqlUtil = sqlUtil;
    }

    private static Logger log = LoggerFactory.getLogger(ShowcaseWsMsgHandler.class);

    public static final ShowcaseWsMsgHandler me = new ShowcaseWsMsgHandler();

    private ShowcaseWsMsgHandler() {
    }

    /**
     * 握手时走这个方法，业务可以在这里获取cookie，request参数等
     */
    @Override
    public HttpResponse handshake(HttpRequest request, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
        String clientip = request.getClientIp();
        log.info("收到来自{}的ws握手包\r\n{}", clientip, request.toString());
        return httpResponse;
    }

    /**
     * @param httpRequest
     * @param httpResponse
     * @param channelContext
     * @throws Exception
     * @author 刘泽
     */
    @Override
    public void onAfterHandshaked(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
        String toString = "";
        String username = null;
        String groupname = null;
        //绑定到群组，后面会有群发
        Map<String, Object[]> httpRequestParams = httpRequest.getParams();
        Set<String> stringSet = httpRequestParams.keySet();
        for (String s : stringSet) {
            Object[] objects = httpRequestParams.get(s);
            Object object = objects[0];
            if ("userid".equals(s)) {
                channelContext.setUserid(object.toString());
                Tio.bindUser(channelContext, object.toString());
                username = object.toString();
            } else if ("groupname".equals(s)) {
                toString = object.toString();
                Tio.bindGroup(channelContext, toString);
                groupname = toString;
            }
        }
        try {
            service.addLogin(username, groupname);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int count = Tio.getAllChannelContexts(channelContext.groupContext).getObj().size();
        String msg = channelContext.getClientNode().toString() + " 进来了，现在共有【" + count + "】人在线";
        //用tio-websocket，服务器发送到客户端的Packet都是WsResponse
        WsResponse wsResponse = WsResponse.fromText(msg, ShowcaseServerConfig.CHARSET);
        //群发
///        Tio.sendToGroup(channelContext.groupContext, Const.GROUP_ID, wsResponse);
///        Tio.sendToGroup(channelContext.groupContext, toString, wsResponse);
        Tio.sendToAll(channelContext.groupContext, wsResponse);
        try {
            Map<String, Object> stringObjectMap = sqlUtil.execSqlSingleResult("select  count(1) as num from msgs where gouserorgroup = 1 and msgtype = 1 and goname = ?", username);
            Object num = stringObjectMap.get("num");
            WsResponse wsResponse1 = WsResponse.fromText("【你有" + num + "条消息未读】", ShowcaseServerConfig.CHARSET);
            Tio.sendToUser(channelContext.groupContext, username, wsResponse1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 字节消息（binaryType = arraybuffer）过来后会走这个方法
     */
    @Override
    public Object onBytes(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
        return null;
    }

    /**
     * 当客户端发close flag时，会走这个方法
     */
    @Override
    public Object onClose(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
        String userid = channelContext.userid;
        String msg = "【用户" + userid + "永远的离开了我们】";
        WsResponse wsResponse = WsResponse.fromText(msg, ShowcaseServerConfig.CHARSET);
        Tio.sendToAll(channelContext.groupContext, wsResponse);
        Tio.remove(channelContext, "receive close flag");
        return null;
    }

    /*
     * 字符消息（binaryType = blob）过来后会走这个方法
     */
    @Override
    public Object onText(WsRequest wsRequest, String text, ChannelContext channelContext) throws Exception {
        WsSessionContext wsSessionContext = (WsSessionContext) channelContext.getAttribute();
        HttpRequest httpRequest = wsSessionContext.getHandshakeRequestPacket();//获取websocket握手包
        if (log.isDebugEnabled()) {
            log.debug("握手包:{}", httpRequest);
        }

        log.info("收到ws消息:{}", text);

        if (Objects.equals("心跳内容", text)) {
            return null;
        }
        JSONObject jsonObject = JSONObject.parseObject(text);
        Object textId = jsonObject.get("msg");
        Object fs = jsonObject.get("fs");
        Object fsz = jsonObject.get("fsz");
        String msg = channelContext.getClientNode().toString() + " 说：" + textId;
        //ip
        String ip = channelContext.getClientNode().toString();
        //发送人
        String userName = channelContext.userid;
        //用tio-websocket，服务器发送到客户端的Packet都是WsResponse
        WsResponse wsResponse = WsResponse.fromText(msg, ShowcaseServerConfig.CHARSET);
        //群发
        if (!StringUtils.isEmpty(String.valueOf(fsz))) {
            addMagsAsync.addMsgs(userName, textId.toString(), fsz.toString(), 2, ip, 0);
            Tio.sendToGroup(channelContext.groupContext, fsz.toString(), wsResponse);
        } else if (!StringUtils.isEmpty(String.valueOf(fs))) {
            SetWithLock<ChannelContext> contexts = Tio.getChannelContextsByUserid(channelContext.groupContext, fs.toString());
            if (StringUtils.isEmpty(contexts)) {
                //没登录过
                //加队列
                addMagsAsync.addMsgs(userName, textId.toString(), fs.toString(), 1, ip, 1);
            } else if (contexts.getObj().size() == 0) {
                //离线
                addMagsAsync.addMsgs(userName, textId.toString(), fs.toString(), 1, ip, 1);
            } else {
                //在线
                addMagsAsync.addMsgs(userName, textId.toString(), fs.toString(), 1, ip, 0);
                Tio.sendToUser(channelContext.groupContext, fs.toString(), wsResponse);
            }
        }
//        Tio.bindUser(channelContext, strings[1]);
        //返回值是要发送给客户端的内容，一般都是返回null
        return null;
    }
}
