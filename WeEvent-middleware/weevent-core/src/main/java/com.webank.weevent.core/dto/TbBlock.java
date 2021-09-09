package com.webank.weevent.core.dto;

import java.math.BigInteger;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity class of table tb_block.
 */
@Getter
@Setter
public class TbBlock {
    private String pkHash;
    private BigInteger blockNumber = BigInteger.ZERO;
    private String blockTimestamp;
    private int transCount;
    private int sealerIndex;
    private String sealer;
    private String createTime;
    private String modifyTime;

    public TbBlock(String pkHash, BigInteger blockNumber,
                   String blockTimestamp, Integer transCount, int sealerIndex) {
        this.pkHash = pkHash;
        this.blockNumber = blockNumber;
        this.blockTimestamp = blockTimestamp;
        this.transCount = transCount;
        this.sealerIndex = sealerIndex;
    }

    public TbBlock() {

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

    public String getPkHash() {
        return pkHash;
    }

    public void setPkHash(String pkHash) {
        this.pkHash = pkHash;
    }

    public String getSealer() {
        return sealer;
    }

    public void setSealer(String sealer) {
        this.sealer = sealer;
    }

    public int getSealerIndex() {
        return sealerIndex;
    }

    public void setSealerIndex(int sealerIndex) {
        this.sealerIndex = sealerIndex;
    }

    public int getTransCount() {
        return transCount;
    }

    public void setTransCount(int transCount) {
        this.transCount = transCount;
    }
}
