package com.webank.weevent.broker.protocol.mqtt.command;

import com.webank.weevent.broker.protocol.mqtt.ProtocolProcess;
import com.webank.weevent.broker.protocol.mqtt.store.SessionStore;
import io.netty.handler.codec.mqtt.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * @author websterchen
 * @version v1.0
 * @since 2019/6/5
 */
@Slf4j
public class UnSubscribe implements MqttCommand {
    private final SessionStore sessionStore;

    public UnSubscribe(SessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }

    @Override
    public Optional<MqttMessage> process(MqttMessage req, String clientId, String remoteIp) {
        MqttUnsubscribeMessage msg = (MqttUnsubscribeMessage) req;
        log.info("UNSUBSCRIBE, {}", msg.payload().topics());

        if (msg.payload().topics().isEmpty()) {
            log.error("empty topic, skip it");
            return Optional.empty();
        }

        this.sessionStore.unSubscribe(clientId, msg.payload().topics());

        MqttMessage rsp = MqttMessageFactory.newMessage(new MqttFixedHeader(MqttMessageType.UNSUBACK, false, MqttQoS.AT_LEAST_ONCE, false, ProtocolProcess.fixLengthOfMessageId),
                MqttMessageIdVariableHeader.from(msg.variableHeader().messageId()), null);
        return Optional.of(rsp);
    }
}
