package com.webank.weevent.core.fabric.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author websterchen
 * @version v1.1
 * @since 2019/8/9
 */
@Getter
@Setter
public class TransactionInfo {
    /**
     * The payLoad
     */
    private String payLoad;

    /**
     * blockNumber
     */
    private Long blockNumber;

    /**
     * The error code.
     */
    private int code;

    /**
     * The error message.
     */
    private String message;

    public String getPayLoad() {
        return payLoad;
    }

    public void setPayLoad(String payLoad) {
        this.payLoad = payLoad;
    }

    public Long getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(Long blockNumber) {
        this.blockNumber = blockNumber;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
