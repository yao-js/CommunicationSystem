package com.webank.weevent.core.config;

import com.webank.weevent.core.fisco.util.WeEventUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author websterchen
 * @version v1.1
 * @since 2019/8/9
 */
@Slf4j
@Getter
@Setter
@ToString
@Component
@PropertySource(value = "classpath:fabric/fabric.properties", encoding = "UTF-8")
public class FabricConfig {
    @Value("${chain.channel.name:mychannel}")
    private String channelName;

    @Value("${chain.organizations.name:Org1}")
    private String orgName;

    @Value("${chain.organizations.mspid:Org1MSP}")
    private String mspId;

    @Value("${chain.organizations.username:Admin}")
    private String orgUserName;

    @Value("${chain.organizations.user.keyfile:}")
    private String orgUserKeyFile;

    @Value("${chain.organizations.user.certfile:}")
    private String orgUserCertFile;

    @Value("${chain.peer.address:}")
    private String peerAddress;

    @Value("${chain.peer.tls.cafile:}")
    private String peerTlsCaFile;

    @Value("${chain.orderer.address:}")
    private String ordererAddress;

    @Value("${chain.orderer.tls.cafile:}")
    private String ordererTlsCaFile;

    @Value("${chaincode.topic.version:v1.0}")
    private String topicVerison;

    @Value("${chaincode.topic.name:Topic}")
    private String topicName;

    private String topicSourceLoc;

    @Value("${chaincode.topic.path:contract/Topic}")
    private String topicPath;

    @Value("${chaincode.topic-controller.version:v1.0}")
    private String topicControllerVersion;

    @Value("${chaincode.topic-controller.name:TopicController}")
    private String topicControllerName;

    private String topicControllerSourceLoc;

    @Value("${chaincode.topic-controller.path:contract/TopicController}")
    private String topicControllerPath;

    @Value("${chaincode.proposal.timeout:12000}")
    private Long proposalTimeout;

    @Value("${chaincode.transaction.timeout:30000}")
    private Long transactionTimeout;

    @Value("${pool.core-pool-size:10}")
    private Integer corePoolSize;

    @Value("${pool.max-pool-size:200}")
    private Integer maxPoolSize;

    @Value("${pool.keep-alive-seconds:10}")
    private Integer keepAliveSeconds;

    @Value("${consumer.idle-time:1000}")
    private Integer consumerIdleTime;

    @Value("${consumer.history_merge_block:8}")
    private Integer consumerHistoryMergeBlock;

    public String getTopicControllerSourceLoc() {
        return topicControllerSourceLoc;
    }

    public String getTopicSourceLoc() {
        return topicSourceLoc;
    }

    public String getTopicControllerPath() {
        return topicControllerPath;
    }

    public String getTopicPath() {
        return topicPath;
    }

    public Long getProposalTimeout() {
        return proposalTimeout;
    }

    public Long getTransactionTimeout() {
        return transactionTimeout;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getOrgUserName() {
        return orgUserName;
    }

    public String getMspId() {
        return mspId;
    }

    public String getTopicName() {
        return topicName;
    }

    public String getTopicVerison() {
        return topicVerison;
    }

    public String getTopicControllerName() {
        return topicControllerName;
    }

    public String getTopicControllerVersion() {
        return topicControllerVersion;
    }

    public String getOrgUserKeyFile() {
        return orgUserKeyFile;
    }

    public void setOrgUserKeyFile(String orgUserKeyFile) {
        this.orgUserKeyFile = orgUserKeyFile;
    }

    public String getOrgUserCertFile() {
        return orgUserCertFile;
    }

    public void setOrgUserCertFile(String orgUserCertFile) {
        this.orgUserCertFile = orgUserCertFile;
    }

    public String getOrdererTlsCaFile() {
        return ordererTlsCaFile;
    }

    public void setOrdererTlsCaFile(String ordererTlsCaFile) {
        this.ordererTlsCaFile = ordererTlsCaFile;
    }

    public String getPeerTlsCaFile() {
        return peerTlsCaFile;
    }

    public void setPeerTlsCaFile(String peerTlsCaFile) {
        this.peerTlsCaFile = peerTlsCaFile;
    }

    public void setTopicSourceLoc(String topicSourceLoc) {
        this.topicSourceLoc = topicSourceLoc;
    }

    public void setTopicControllerSourceLoc(String topicControllerSourceLoc) {
        this.topicControllerSourceLoc = topicControllerSourceLoc;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Integer getConsumerHistoryMergeBlock() {
        return consumerHistoryMergeBlock;
    }

    public void setConsumerHistoryMergeBlock(Integer consumerHistoryMergeBlock) {
        this.consumerHistoryMergeBlock = consumerHistoryMergeBlock;
    }

    public Integer getConsumerIdleTime() {
        return consumerIdleTime;
    }

    public void setConsumerIdleTime(Integer consumerIdleTime) {
        this.consumerIdleTime = consumerIdleTime;
    }

    public Integer getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(Integer corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public Integer getKeepAliveSeconds() {
        return keepAliveSeconds;
    }

    public void setKeepAliveSeconds(Integer keepAliveSeconds) {
        this.keepAliveSeconds = keepAliveSeconds;
    }

    public Integer getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(Integer maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public void setMspId(String mspId) {
        this.mspId = mspId;
    }

    public String getOrdererAddress() {
        return ordererAddress;
    }

    public void setOrdererAddress(String ordererAddress) {
        this.ordererAddress = ordererAddress;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public void setOrgUserName(String orgUserName) {
        this.orgUserName = orgUserName;
    }

    public String getPeerAddress() {
        return peerAddress;
    }

    public void setPeerAddress(String peerAddress) {
        this.peerAddress = peerAddress;
    }

    public void setProposalTimeout(Long proposalTimeout) {
        this.proposalTimeout = proposalTimeout;
    }

    public void setTopicControllerName(String topicControllerName) {
        this.topicControllerName = topicControllerName;
    }

    public void setTopicControllerPath(String topicControllerPath) {
        this.topicControllerPath = topicControllerPath;
    }

    public void setTopicControllerVersion(String topicControllerVersion) {
        this.topicControllerVersion = topicControllerVersion;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public void setTopicPath(String topicPath) {
        this.topicPath = topicPath;
    }

    public void setTopicVerison(String topicVerison) {
        this.topicVerison = topicVerison;
    }

    public void setTransactionTimeout(Long transactionTimeout) {
        this.transactionTimeout = transactionTimeout;
    }

    /**
     * load configuration without spring
     *
     * @param configFile config file, if empty load from default location
     * @return true if success, else false
     */
    public boolean load(String configFile) {
        boolean loadResult = new SmartLoadConfig().load(this, configFile, "");
        this.setOrgUserKeyFile(WeEventUtils.getClassPath() + this.getOrgUserKeyFile());
        this.setOrgUserCertFile(WeEventUtils.getClassPath() + this.getOrgUserCertFile());
        this.setOrdererTlsCaFile(WeEventUtils.getClassPath() + this.getOrdererTlsCaFile());
        this.setPeerTlsCaFile(WeEventUtils.getClassPath() + this.getPeerTlsCaFile());

        this.setTopicSourceLoc(WeEventUtils.getClassPath() + "fabric");
        this.setTopicControllerSourceLoc(WeEventUtils.getClassPath() + "fabric");

        return loadResult;
    }


}
