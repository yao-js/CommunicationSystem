package com.webank.weevent.client;


import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Event entity, like a message in traditional MQ.
 * <p>
 *
 * @author matthewliu
 * @since 2018/11/02
 */
@Getter
@Setter
public class WeEvent {
    public static final String DEFAULT_GROUP_ID = "mychannel";
    public static final String OFFSET_FIRST = "OFFSET_FIRST";
    public static final String OFFSET_LAST = "OFFSET_LAST";

    /**
     * wildcard pattern support, see MQTT specification.
     * http://public.dhe.ibm.com/software/dw/webservices/ws-mqtt/mqtt-v3r1.html
     */
    // character to split layer.
    public static final String LAYER_SEPARATE = "/";
    // wildcard character for one layer.
    public static final String WILD_CARD_ONE_LAYER = "+";
    // wildcard for one layer.
    public static final String WILD_CARD_ALL_LAYER = "#";
    // character to split multiple topic
    public static final String MULTIPLE_TOPIC_SEPARATOR = ",";

    /**
     * custom header is prefixed with "weevent-", and the following key is reserved in WeEvent.
     */
    public static final String WeEvent_FORMAT = "weevent-format";
    public static final String WeEvent_SubscriptionId = "weevent-subscriptionId";
    public static final String WeEvent_TAG = "weevent-tag";
    public static final String WeEvent_SIGN = "weevent-sign";
    public static final String WeEvent_FILE = "weevent-file";
    public static final String WeEvent_EPHEMERAL = "weevent-ephemeral";

    /**
     * block chain information, see WeEventPlus
     * It is assigned by system default.
     */
    public static final String WeEvent_PLUS = "weevent-plus";

    /**
     * Binding topic, like "com.weevent.test".
     */
    private String topic;

    /**
     * Custom business data, everything as you like.
     * 10k bytes limit.
     */
    private byte[] content;

    /**
     * event's custom header.
     * all data length in total must be less then 1k.
     */
    private Map<String, String> extensions;

    /**
     * Event id in block chain, it's unique under one topic.
     * It is assigned by system default.
     */
    private String eventId = "";

    /**
     * Default construction needed by jackson marshall.
     */
    public WeEvent() {
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Map<String, String> getExtensions() {
        return extensions;
    }

    public void setExtensions(Map<String, String> extensions) {
        this.extensions = extensions;
    }

    /**
     * Construction.
     *
     * @param topic the topic
     * @param content the content, character utf8
     */
    public WeEvent(String topic, byte[] content) {
        this.topic = topic;
        this.content = content;
        this.extensions = new HashMap<>();
    }

    /**
     * Construction.
     *
     * @param topic the topic
     * @param content the content, character utf8
     * @param extensions event's custom header
     */
    public WeEvent(String topic, byte[] content, Map<String, String> extensions) {
        this.topic = topic;
        this.content = content;
        this.extensions = extensions;
    }


    @Override
    public String toString() {
        return "WeEvent{" +
                "topic='" + topic + '\'' +
                ", content.length=" + content.length +
                ", eventID='" + eventId + '\'' +
                ", extensions=" + extensions +
                '}';
    }
}
