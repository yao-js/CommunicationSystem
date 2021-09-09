package com.webank.weevent.client;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Topic data in page list.
 *
 * @author matthewliu
 * @since 2019/02/11
 */
@Getter
@Setter
public class TopicPage {
    private Integer total;
    private Integer pageIndex;
    private Integer pageSize;
    private List<TopicInfo> topicInfoList = new ArrayList<>();
}
