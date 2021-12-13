package com.example.shareformore.dto;

import com.example.shareformore.entity.Work;

import java.io.Serializable;


public class WorkDetailDto implements Serializable {
    private static final long serialVersionUID = -6998418963484017874L;

    public final long work_id;
    public final long author_id;
    public final long column_id;
    public final String title;
    public final long create_time;
    public final long update_time;
    public final String description;
    public final int price;
    public final String content;
    public final String image;

    private WorkDetailDto(Work work) {
        this.work_id = work.getWorkId();
        this.author_id = work.getAuthor().getUserId();
        this.column_id = work.getColumn().getColumnId();
        this.title = work.getTitle();
        this.create_time = work.getCreateTime().getTime() / 1000L;
        this.update_time = work.getUpdateTime().getTime() / 1000L;
        this.description = work.getDescription();
        this.price = work.getPrice();
        this.content = work.getContent();
        this.image = work.getImage();
    }

    public static WorkDetailDto wrap(Work work) {
        return new WorkDetailDto(work);
    }
}
