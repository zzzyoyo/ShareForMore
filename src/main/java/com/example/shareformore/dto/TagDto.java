package com.example.shareformore.dto;

import com.example.shareformore.entity.Tag;
import com.example.shareformore.entity.User;

import java.io.Serializable;


public class TagDto implements Serializable {
    private static final long serialVersionUID = -9073114587273328598L;

    public final long tag_id;
    public final String tag_name;

    private TagDto(Tag tag) {
        this.tag_id = tag.getTagId();
        this.tag_name = tag.getTagName();
    }

    public static TagDto wrap(Tag tag) {
        return new TagDto(tag);
    }
}
