package com.example.shareformore.controller;

import com.example.shareformore.security.jwt.JwtUtils;
import com.example.shareformore.service.WorkManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/work")
public class WorkManagementController {

    WorkManagementService workManagementService;
    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public WorkManagementController(WorkManagementService workManagementService) {
        this.workManagementService = workManagementService;
    }

    @PostMapping("/new")
    public ResponseEntity<?> uploadWork(@RequestHeader String token,
                                        MultipartFile image,
                                        @RequestParam(value = "column_id", required = false) Long columnId,
                                        @RequestParam(value = "tag_list", required = false) List<Long> tagList,
                                        @NotBlank(message = "缺少标题") @RequestParam String title,
                                        @NotBlank(message = "缺少简介") @RequestParam String description,
                                        @RequestParam(required = false) String content,
                                        @Min(value = 0, message = "价格必须为自然数") @RequestParam int price) throws IOException {
        logger.debug("get an upload post from " + JwtUtils.getUsername(token));
        return workManagementService.uploadWork(JwtUtils.getUsername(token), columnId, tagList, title, description,content, price, image).getResponseEntity();
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateWork(@RequestHeader String token,
                              MultipartFile image,
                              @Min(value = 1, message = "不合法的ID") @RequestParam("work_id") Long workId,
                              @RequestParam(value = "column_id", required = false) Long columnId,
                              @RequestParam(value = "tag_list", required = false) List<Long> tagList,
                              @NotBlank(message = "缺少标题") @RequestParam String title,
                              @NotBlank(message = "缺少简介") @RequestParam String description,
                              @RequestParam(required = false) String content,
                              @Min(value = 1, message = "价格必须为正整数") @RequestParam int price) throws IOException {
        logger.debug("get an update post from " + JwtUtils.getUsername(token));
        return workManagementService.updateWork(JwtUtils.getUsername(token), workId, columnId, tagList, title, description, content, price, image).getResponseEntity();
    }

    @GetMapping("/list")
    public ResponseEntity<?> listWork(@RequestParam(required = false) String title,
                                      @RequestParam(required = false) String tag,
                                      @RequestParam(required = false) String name,
                                      @RequestParam(required = false, value = "column_id") Long columnId) {
        return workManagementService.listWork(title, tag, name, columnId).getResponseEntity();
    }

    @GetMapping("/detail")
    public ResponseEntity<?> showWorkDetail(@RequestParam("user_id")Long userId, @RequestParam("work_id") Long workId) {
        return workManagementService.showWorkDetail(userId, workId).getResponseEntity();
    }

    @GetMapping("/buy")
    public ResponseEntity<?> payWork(@RequestHeader String token, @RequestParam("work_id") Long workId) {
        logger.debug("get an pay request from " + JwtUtils.getUsername(token));
        return workManagementService.payWork(JwtUtils.getUsername(token), workId).getResponseEntity();
    }
}
