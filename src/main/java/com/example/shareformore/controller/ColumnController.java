package com.example.shareformore.controller;

import com.example.shareformore.service.ColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/column")
public class ColumnController {
    private ColumnService columnService;

    @Autowired
    public ColumnController(ColumnService columnService) {
        this.columnService = columnService;
    }

    @PostMapping("/new")
    public ResponseEntity<?> newColumn(@RequestParam("author_id")Long authorId,
                                       @RequestParam("column_name")String columnName,
                                       @RequestParam String description) {
        return ResponseEntity.ok(columnService.newColumn(authorId, columnName, description));
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateColumn(@RequestParam("author_id")Long authorId,
                                          @RequestParam("column_id")Long columnId,
                                          @RequestParam("column_name") String columnName,
                                          @RequestParam String description) {
        return ResponseEntity.ok(columnService.updateColumn(authorId, columnId, columnName, description));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addColumn(@RequestParam("column_id")Long columnId,
                                       @RequestParam("work_id")Long workId) {
        return ResponseEntity.ok(columnService.addColumn(columnId, workId));
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteColumn(@RequestParam("column_id")Long columnId) {
        return ResponseEntity.ok(columnService.deleteColumn(columnId));
    }

    @GetMapping("/list")
    public ResponseEntity<?> listColumn(@RequestParam("author_id")Long authorId) {
        return ResponseEntity.ok(columnService.listColumn(authorId));
    }
}
