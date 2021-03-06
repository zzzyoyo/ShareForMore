package com.example.shareformore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.persistence.Column;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Tag {
    private static final long serialVersionUID = -8546972979375001850L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long tagId;

    @Column(unique = true)
    private String tagName;

    @JsonIgnore
    @JoinTable(name = "work_tag",joinColumns = @JoinColumn(name="tagId"),inverseJoinColumns = @JoinColumn(name = "workId"))
    @ManyToMany
    Set<Work> workSet;

    public Tag() {
    }

    public Tag(String tagName) {
        this.tagName = tagName;
        this.workSet = new HashSet<>();
    }

    //重写equals方法, 最佳实践就是如下这种判断顺序:
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Tag))
            return false;
        if (obj == this)
            return true;
        return this.getTagId().equals(((Tag) obj).getTagId());
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tag_name) {
        this.tagName = tag_name;
    }

    public Set<Work> getWorkSet() {
        return workSet;
    }

    public void setWorkSet(Set<Work> workSet) {
        this.workSet = workSet;
    }
}
