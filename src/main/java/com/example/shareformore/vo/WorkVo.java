package com.example.shareformore.vo;

import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

public class WorkVo {
    private Long columnId;
    private List<Long> tag_list;
    private String title;
    private String description;
    private String image;
    private String content;
    private int price;

    public WorkVo() {
    }

    public WorkVo(Long columnId, List<Long> tag_list, String title, String description, String image, String content, int price) {
        this.columnId = columnId;
        this.tag_list = tag_list;
        this.title = title;
        this.description = description;
        this.image = image;
        this.content = content;
        this.price = price;
    }

    public Long getColumnId() {
        return columnId;
    }

    public void setColumnId(Long columnId) {
        this.columnId = columnId;
    }

    public List<Long> getTag_list() {
        return tag_list;
    }

    public void setTag_list(List<Long> tag_list) {
        this.tag_list = tag_list;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public void setImage(String image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
