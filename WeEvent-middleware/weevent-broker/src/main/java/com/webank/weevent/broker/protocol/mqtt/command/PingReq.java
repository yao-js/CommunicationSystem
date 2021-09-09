package com.webank.weevent.broker.protocol.mqtt.command;

import io.netty.handler.codec.mqtt.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * @author websterchen
 * @version v1.0
 * @since 2019/6/5
 */
@Slf4j
public class PingReq implements MqttCommand {
    @Override
    public Optional<MqttMessage> process(MqttMessage req, String clientId, String remoteIp) {
        log.debug("heart beat from client: {}", clientId);
        
        MqttMessage rsp = MqttMessageFactory.newMessage(new MqttFixedHeader(MqttMessageType.PINGRESP, false, MqttQoS.AT_LEAST_ONCE, false, 0), null, null);
        return Optional.of(rsp);
    }
}
