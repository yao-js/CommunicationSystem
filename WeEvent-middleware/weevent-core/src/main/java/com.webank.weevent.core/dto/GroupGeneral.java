package com.webank.weevent.core.dto;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupGeneral {

    private int nodeCount;
    private BigInteger transactionCount = BigInteger.ZERO;
    private BigInteger latestBlock = BigInteger.ZERO;

    public BigInteger getLatestBlock() {
        return latestBlock;
    }

    public void setLatestBlock(BigInteger latestBlock) {
        this.latestBlock = latestBlock;
    }

    public int getNodeCount() {
        return nodeCount;
    }

    public void setNodeCount(int nodeCount) {
        this.nodeCount = nodeCount;
    }

    public BigInteger getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(BigInteger transactionCount) {
        this.transactionCount = transactionCount;
    }
}
