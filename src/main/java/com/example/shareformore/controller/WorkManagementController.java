package com.example.shareformore.controller;

import com.example.shareformore.security.jwt.JwtUtils;
import com.example.shareformore.service.WorkManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

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
                                        @Min(value = 1, message = "不合法的ID") @RequestParam Long column_id,
                                        @Min(value = 1, message = "不合法的ID") @RequestParam List<Long> tag_list,
                                        @NotBlank(message = "缺少标题") @RequestParam String title,
                                        @NotBlank(message = "缺少简介") @RequestParam String description,
                                        @NotNull @RequestParam String content,
                                        @Min(value = 1, message = "价格必须为正整数") @RequestParam int price) throws IOException {
        logger.debug("get an upload post from " + JwtUtils.getUsername(token));
        return ResponseEntity.ok(workManagementService.uploadWork(JwtUtils.getUsername(token), column_id, tag_list, title, description,content, price, image));
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateWork(@RequestHeader String token,
                              @NotNull(message = "缺少内容") MultipartFile image,
                              @Min(value = 1, message = "不合法的ID") @RequestParam Long work_id,
                              @Min(value = 1, message = "不合法的ID") @RequestParam Long column_id,
                              @Min(value = 1, message = "不合法的ID") @RequestParam List<Long> tag_list,
                              @NotBlank(message = "缺少标题") @RequestParam String title,
                              @NotBlank(message = "缺少简介") @RequestParam String description,
                              @NotNull @RequestParam String content,
                              @Min(value = 1, message = "价格必须为正整数") @RequestParam int price) throws IOException {
        logger.debug("get an update post from " + JwtUtils.getUsername(token));
        return ResponseEntity.ok(workManagementService.updateWork(JwtUtils.getUsername(token), work_id, column_id, tag_list, title, description,content, price, image));
    }
}
