package com.example.shareformore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class SpecialColumn {
    private static final long serialVersionUID = -8546972979375001850L;
    private static final String Default_Column = "Default";
    private static final String Default_Column_Description = "Default Column";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long columnId;

    String columnName;
    Timestamp createTime;
    Timestamp updateTime;
    String description;

    @JsonIgnore
    @JoinColumn(name = "authorId")
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    User author;
    @JsonIgnore
    @OneToMany(mappedBy = "specialColumn",fetch = FetchType.EAGER)
    Set<Work> workSet;

    public SpecialColumn() {

    }

    public SpecialColumn(User author) {
        this.author = author;
        this.columnName = Default_Column;
        Timestamp timestamp = new Timestamp(new Date().getTime());
        this.createTime = timestamp;
        this.updateTime = timestamp;
        this.description = Default_Column_Description;
        this.workSet = new HashSet<>();
    }

    public SpecialColumn(User author, String columnName, String description) {
        this.author = author;
        this.columnName = columnName;
        Timestamp timestamp = new Timestamp(new Date().getTime());
        this.createTime = timestamp;
        this.updateTime = timestamp;
        this.description = description;
        this.workSet = new HashSet<>();
    }

    //重写equals方法, 最佳实践就是如下这种判断顺序:
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SpecialColumn))
            return false;
        if (obj == this)
            return true;
        return this.getColumnId().equals(((SpecialColumn) obj).getColumnId());
    }

    public Long getColumnId() {
        return columnId;
    }

    public void setColumnId(Long column_id) {
        this.columnId = column_id;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String column_name) {
        this.columnName = column_name;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp create_time) {
        this.createTime = create_time;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp update_time) {
        this.updateTime = update_time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Set<Work> getWorkSet() {
        return workSet;
    }

    public void setWorkSet(Set<Work> workSet) {
        this.workSet = workSet;
    }
}
