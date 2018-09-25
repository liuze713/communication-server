package com.im.imTio;

import org.tio.server.ServerGroupContext;
import org.tio.websocket.server.WsServerStarter;

import java.io.IOException;

/**
 * @author 刘泽
 * 2017年6月28日 下午5:34:04
 */
public class ShowcaseWebsocketStarter {
    //服务启动器
    private WsServerStarter wsServerStarter;
    private ServerGroupContext serverGroupContext;

    /**
     * @author 刘泽
     */
    public ShowcaseWebsocketStarter(int port, ShowcaseWsMsgHandler wsMsgHandler) throws Exception {
        wsServerStarter = new WsServerStarter(port, wsMsgHandler);
        serverGroupContext = wsServerStarter.getServerGroupContext();
        serverGroupContext.setName(ShowcaseServerConfig.PROTOCOL_NAME);
        serverGroupContext.setServerAioListener(ShowcaseServerAioListener.me);

        //设置ip统计时间段
        serverGroupContext.ipStats.addDurations(ShowcaseServerConfig.IpStatDuration.IPSTAT_DURATIONS);
        //设置ip监控
        serverGroupContext.setIpStatListener(ShowcaseIpStatListener.me);
        //设置心跳超时时间
        serverGroupContext.setHeartbeatTimeout(ShowcaseServerConfig.HEARTBEAT_TIMEOUT);
        //如果你希望通过wss来访问，就加上下面的代码吧，不过首先你得有SSL证书（证书必须和域名相匹配，否则可能访问不了ssl）
//		String keyStoreFile = "classpath:config/ssl/keystore.jks";
//		String trustStoreFile = "classpath:config/ssl/keystore.jks";
//		String keyStorePwd = "214323428310224";
//		serverGroupContext.useSsl(keyStoreFile, trustStoreFile, keyStorePwd);

    }

    /**
     * @param args
     * @throws IOException
     * @author 刘泽
     */
    public static void start() throws Exception {
        ShowcaseWebsocketStarter appStarter = new ShowcaseWebsocketStarter(ShowcaseServerConfig.SERVER_PORT, ShowcaseWsMsgHandler.me);
        appStarter.wsServerStarter.start();
    }

    /**
     * @return the serverGroupContext
     */
    public ServerGroupContext getServerGroupContext() {
        return serverGroupContext;
    }

    public WsServerStarter getWsServerStarter() {
        return wsServerStarter;
    }

    /**
     * @return void
     * @description TODO 启动
     * @author 刘泽
     * @date 2018-9-25 11:52
     */
    public static void main(String[] args) throws Exception {
        start();
    }

}
