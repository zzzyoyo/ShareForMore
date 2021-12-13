package com.example.shareformore.controller;

import com.example.shareformore.repository.TagRepository;
import com.example.shareformore.service.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/tag")
public class TagController {
    TagService tagService;

    Logger logger = LoggerFactory.getLogger(TagController.class);

    @Autowired
    public TagController(TagService tagService){
        this.tagService = tagService;
    }

    @GetMapping("/list")
    public ResponseEntity<?> listAllTags(){
        return tagService.listAllTags().getResponseEntity();
    }

    @GetMapping("/new")
    public ResponseEntity<?> createTag(@RequestParam("tag_name")String name){
        return tagService.createTag(name).getResponseEntity();
    }
}
