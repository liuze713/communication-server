/**
 *
 */
package com.im.imTio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.intf.Packet;
import org.tio.core.stat.IpStat;
import org.tio.core.stat.IpStatListener;
import org.tio.utils.json.Json;

/**
 * @author 刘泽
 */
public class ShowcaseIpStatListener implements IpStatListener {
    private static Logger log = LoggerFactory.getLogger(ShowcaseIpStatListener.class);

    public static final ShowcaseIpStatListener me = new ShowcaseIpStatListener();

    /**
     *
     */
    private ShowcaseIpStatListener() {
    }

    @Override
    public void onExpired(GroupContext groupContext, IpStat ipStat) {
        //在这里把统计数据入库中或日志
        if (log.isInfoEnabled()) {
            log.info("可以把统计数据入库\r\n{}", Json.toFormatedJson(ipStat));
        }
    }

    /**
     * 连接之前
     *
     * @param channelContext
     * @param isConnected
     * @param isReconnect
     * @param ipStat
     * @throws Exception
     */
    @Override
    public void onAfterConnected(ChannelContext channelContext, boolean isConnected, boolean isReconnect, IpStat ipStat) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("onAfterConnected\r\n{}", Json.toFormatedJson(ipStat));
        }
    }

    /**
     * 解码错误
     *
     * @param channelContext
     * @param ipStat
     */
    @Override
    public void onDecodeError(ChannelContext channelContext, IpStat ipStat) {
        if (log.isInfoEnabled()) {
            log.info("onDecodeError\r\n{}", Json.toFormatedJson(ipStat));
        }
    }

    /**
     * 发送之后
     *
     * @param channelContext
     * @param packet
     * @param isSentSuccess
     * @param ipStat
     * @throws Exception
     */
    @Override
    public void onAfterSent(ChannelContext channelContext, Packet packet, boolean isSentSuccess, IpStat ipStat) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("onAfterSent\r\n{}\r\n{}", packet.logstr(), Json.toFormatedJson(ipStat));
        }
    }

    /**
     * 解码之后
     *
     * @param channelContext
     * @param packet
     * @param packetSize
     * @param ipStat
     * @throws Exception
     */
    @Override
    public void onAfterDecoded(ChannelContext channelContext, Packet packet, int packetSize, IpStat ipStat) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("onAfterDecoded\r\n{}\r\n{}", packet.logstr(), Json.toFormatedJson(ipStat));
        }
    }

    /**
     * 收到字节之后
     *
     * @param channelContext
     * @param receivedBytes
     * @param ipStat
     * @throws Exception
     */
    @Override
    public void onAfterReceivedBytes(ChannelContext channelContext, int receivedBytes, IpStat ipStat) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("onAfterReceivedBytes\r\n{}", Json.toFormatedJson(ipStat));
        }
    }

    /**
     * 握手之后
     *
     * @param channelContext
     * @param packet
     * @param ipStat
     * @param cost
     * @throws Exception
     */
    @Override
    public void onAfterHandled(ChannelContext channelContext, Packet packet, IpStat ipStat, long cost) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("onAfterHandled\r\n{}\r\n{}", packet.logstr(), Json.toFormatedJson(ipStat));
        }
    }

}
