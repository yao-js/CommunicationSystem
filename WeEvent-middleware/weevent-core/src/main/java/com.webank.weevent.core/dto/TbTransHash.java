package com.webank.weevent.core.dto;

import java.math.BigInteger;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity class of table tb_trans_hash.
 */
@Getter
@Setter
public class TbTransHash {

    private String transHash;
    private String transFrom;
    private String transTo;
    private BigInteger blockNumber;
    private String blockTimestamp;
    private String createTime;
    private String modifyTime;

    public TbTransHash(String transHash, String transFrom, String transTo, BigInteger blockNumber, String blockTimestamp) {
        this.transHash = transHash;
        this.transFrom = transFrom;
        this.transTo = transTo;
        this.blockNumber = blockNumber;
        this.blockTimestamp = blockTimestamp;
    }

    public TbTransHash() {

    }

    public BigInteger getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(BigInteger blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getBlockTimestamp() {
        return blockTimestamp;
    }

    public void setBlockTimestamp(String blockTimestamp) {
        this.blockTimestamp = blockTimestamp;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getTransFrom() {
        return transFrom;
    }

    public void setTransFrom(String transFrom) {
        this.transFrom = transFrom;
    }

    public String getTransHash() {
        return transHash;
    }

    public void setTransHash(String transHash) {
        this.transHash = transHash;
    }

    public String getTransTo() {
        return transTo;
    }

    public void setTransTo(String transTo) {
        this.transTo = transTo;
    }
}
