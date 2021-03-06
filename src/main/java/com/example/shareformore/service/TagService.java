package com.example.shareformore.service;

import com.example.shareformore.dto.TagDto;
import com.example.shareformore.entity.Tag;
import com.example.shareformore.exception.tag.TagNameHasBeenUsedException;
import com.example.shareformore.repository.TagRepository;
import com.example.shareformore.response.ResponseHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TagService {
    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    Logger logger = LoggerFactory.getLogger(TagService.class);

    public ResponseHolder listAllTags(){
        List<Tag> allTags = new LinkedList<>();
        tagRepository.findAll().forEach(allTags::add);
        List<TagDto> tagList = allTags.stream().map(TagDto::wrap).collect(Collectors.toList());

        return new ResponseHolder(HttpStatus.OK.value(), "success", null, tagList, null, null);
    }

    public ResponseHolder createTag(String tagName){
        Tag tag = tagRepository.findByTagName(tagName);
        if(tag != null){
            logger.debug("tag name used error");
            throw new TagNameHasBeenUsedException(tagName);
        }
        tag = new Tag(tagName);
        tagRepository.save(tag);

        return new ResponseHolder(HttpStatus.OK.value(), "success", null, null, TagDto.wrap(tag), null);
    }
}
