package com.example.shareformore.vo;

import java.util.List;

public class WorkUpdateVo extends WorkVo {
    private Long workId;

    public WorkUpdateVo() {
    }

    public WorkUpdateVo(Long workId, Long columnId, List<Long> tag_list, String title, String description, String image, String content, int price) {
        super(columnId, tag_list, title, description, image, content, price);
        this.workId = workId;
    }

    public Long getWorkId() {
        return workId;
    }

    public void setWorkId(Long workId) {
        this.workId = workId;
    }
}
