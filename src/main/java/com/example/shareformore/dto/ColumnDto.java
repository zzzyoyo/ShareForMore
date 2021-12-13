package com.example.shareformore.dto;

import com.example.shareformore.entity.SpecialColumn;
import com.example.shareformore.entity.User;

import java.io.Serializable;


public class ColumnDto implements Serializable {
    private static final long serialVersionUID = -7703031455627403152L;

    public final long column_id;
    public final long author_id;
    public final String column_name;
    public final String author_name;
    public final long create_time;
    public final long update_time;
    public final String description;

    private ColumnDto(SpecialColumn column) {
        this.column_id = column.getColumnId();
        this.author_id = column.getAuthor().getUserId();
        this.column_name = column.getColumnName();
        this.author_name = column.getAuthor().getUsername();
        this.create_time = column.getCreateTime().getTime() / 1000L;
        this.update_time = column.getUpdateTime().getTime() / 1000L;
        this.description = column.getDescription();
    }

    public static ColumnDto wrap(SpecialColumn column) {
        return new ColumnDto(column);
    }
}
