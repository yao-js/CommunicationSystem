package com.webank.weevent.core.fabric;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

import com.webank.weevent.core.IProducer;
import com.webank.weevent.core.dto.ContractContext;
import com.webank.weevent.core.fabric.sdk.FabricDelegate;
import com.webank.weevent.core.fisco.util.ParamCheckUtils;
import com.webank.weevent.client.BrokerException;
import com.webank.weevent.client.JsonHelper;
import com.webank.weevent.client.SendResult;
import com.webank.weevent.client.WeEvent;

import lombok.extern.slf4j.Slf4j;

/**
 * @author websterchen
 * @version v1.1
 * @since 2019/8/10
 */
@Slf4j
public class FabricBroker4Producer extends FabricTopicAdmin implements IProducer {
    public FabricBroker4Producer(FabricDelegate fabricDelegate) {
        super(fabricDelegate);
    }

    @Override
    public boolean startProducer() {
        return true;
    }

    @Override
    public boolean shutdownProducer() {
        return true;
    }

    @Override
    public CompletableFuture<SendResult> publish(WeEvent event, String channelName) throws BrokerException {
        log.debug("publish input param WeEvent: {}", event);

        ParamCheckUtils.validateEvent(event);
        this.validateChannelName(channelName);
        return fabricDelegate.publishEvent(event.getTopic(),
                channelName,
                new String(event.getContent(), StandardCharsets.UTF_8),
                JsonHelper.object2Json(event.getExtensions()));
    }

    @Override
    public ContractContext getContractContext(String groupId) throws BrokerException {
        return null;
    }
}
