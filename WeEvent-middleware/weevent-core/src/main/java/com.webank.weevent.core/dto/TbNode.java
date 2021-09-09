package com.webank.weevent.core.dto;

import java.math.BigInteger;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

/**
 * Entity class of table tb_node.
 */
@Getter
@Setter
public class TbNode {

    private String nodeId;
    private String nodeName;
    private String nodeType;
    private BigInteger blockNumber;
    private BigInteger pbftView;
    private int nodeActive;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;

    public BigInteger getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(BigInteger blockNumber) {
        this.blockNumber = blockNumber;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }

    public int getNodeActive() {
        return nodeActive;
    }

    public void setNodeActive(int nodeActive) {
        this.nodeActive = nodeActive;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public BigInteger getPbftView() {
        return pbftView;
    }

    public void setPbftView(BigInteger pbftView) {
        this.pbftView = pbftView;
    }
}