package com.example.shareformore.service;

import com.example.shareformore.entity.Tag;
import com.example.shareformore.exception.TagNameHasBeenUsedException;
import com.example.shareformore.repository.TagRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class TagService {
    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    Logger logger = LoggerFactory.getLogger(TagService.class);

    public Map<String,List> listAllTags(){
        Map<String, List> map = new HashMap<>();
        List<Tag> allTags = new LinkedList<>();
        tagRepository.findAll().forEach(allTags::add);
        map.put("list", allTags);
        return map;
    }

    public Map<String, Object> createTag(String tagName){
        Map<String, Object> map = new HashMap<>();
        Tag tag = tagRepository.findByTagName(tagName);
        if(tag != null){
            logger.debug("tag name used error");
            throw new TagNameHasBeenUsedException(tagName);
        }
        tag = new Tag(tagName);
        tagRepository.save(tag);
        map.put("data", "success");
        map.put("obj", tag);
        return map;
    }
}
