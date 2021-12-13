package com.example.shareformore.dto;

import com.example.shareformore.entity.User;
import com.example.shareformore.entity.Work;

import java.io.Serializable;


public class WorkDto implements Serializable {
    private static final long serialVersionUID = -4349063100879950043L;

    public final long work_id;
    public final long author_id;
    public final long column_id;
    public final String title;
    public final String author_name;
    public final String column_name;
    public final long create_time;
    public final long update_time;
    public final String description;
    public final int price;

    private WorkDto(Work work) {
        this.work_id = work.getWorkId();
        this.author_id = work.getAuthor().getUserId();
        this.column_id = work.getColumn().getColumnId();
        this.title = work.getTitle();
        this.author_name = work.getAuthor().getName();
        this.column_name = work.getColumn().getColumnName();
        this.create_time = work.getCreateTime().getTime() / 1000L;
        this.update_time = work.getUpdateTime().getTime() / 1000L;
        this.description = work.getDescription();
        this.price = work.getPrice();
    }

    public static WorkDto wrap(Work work) {
        return new WorkDto(work);
    }
}
