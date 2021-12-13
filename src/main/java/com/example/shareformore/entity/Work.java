package com.example.shareformore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Work {
    private static final long serialVersionUID = -8546972979375001850L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long workId;

    String title;
    Timestamp createTime;
    Timestamp updateTime;
    String description;
    String content;

    String image;

    int price;
    @JsonIgnore
    @JoinColumn(name = "authorId")
    @ManyToOne(cascade = CascadeType.REFRESH)
    User author;

    @JsonIgnore
    @JoinColumn(name = "columnId")
    @ManyToOne(cascade = CascadeType.MERGE)
    SpecialColumn specialColumn;

    @JsonIgnore
    @JoinTable(name = "work_tag",joinColumns = @JoinColumn(name="workId"),inverseJoinColumns = @JoinColumn(name = "tagId"))
    @ManyToMany
    Set<Tag> tagSet;

    @JsonIgnore
    @JoinTable(name = "collection",joinColumns = @JoinColumn(name="workId"),inverseJoinColumns = @JoinColumn(name = "userId"))
    @ManyToMany
    Set<User> liked;

    @JsonIgnore
    @JoinTable(name = "payment",joinColumns = @JoinColumn(name="workId"),inverseJoinColumns = @JoinColumn(name = "userId"))
    @ManyToMany(cascade = {CascadeType.REMOVE, CascadeType.MERGE})
    Set<User> purchase;

    public Work() {
    }

    public Work(User author, SpecialColumn specialColumn, String title, String description, String content, String image, int price, Set<Tag> tagSet) {
        this.author = author;
        this.specialColumn = specialColumn;
        this.title = title;
        this.content = content;
        Timestamp timestamp = new Timestamp(new Date().getTime());
        this.createTime = timestamp;
        this.updateTime = timestamp;
        this.description = description;
        this.image = image;
        this.price = price;
        this.tagSet = tagSet;
        this.purchase = new HashSet<>();
    }

    //重写equals方法, 最佳实践就是如下这种判断顺序:
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Work))
            return false;
        if (obj == this)
            return true;
        return this.getWorkId().equals(((Work) obj).getWorkId());
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SpecialColumn getSpecialColumn() {
        return specialColumn;
    }

    public void setSpecialColumn(SpecialColumn specialColumn) {
        this.specialColumn = specialColumn;
    }

    public Set<User> getLiked() {
        return liked;
    }

    public void setLiked(Set<User> liked) {
        this.liked = liked;
    }

    public Long getWorkId() {
        return workId;
    }

    public void setWorkId(Long work_id) {
        this.workId = work_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getImage() {
        return image;
    }

    public void setImage(String content) {
        this.image = content;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @JsonIgnore
    public SpecialColumn getColumn() {
        return specialColumn;
    }

    public void setColumn(SpecialColumn specialColumn) {
        this.specialColumn = specialColumn;
    }

    public Set<Tag> getTagSet() {
        return tagSet;
    }

    public void setTagSet(Set<Tag> tagSet) {
        this.tagSet = tagSet;
    }

    public Set<User> getPurchase() {
        return purchase;
    }

    public void setPurchase(Set<User> purchase) {
        this.purchase = purchase;
    }
}
