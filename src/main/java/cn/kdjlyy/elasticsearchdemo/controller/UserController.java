package cn.kdjlyy.elasticsearchdemo.controller;

import cn.kdjlyy.elasticsearchdemo.document.UserDocument;
import cn.kdjlyy.elasticsearchdemo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    // 创建索引
    @PostMapping("/createUserIndex")
    public ResponseEntity<Boolean> createUserIndex(@RequestParam(value = "index") String index) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUserIndex(index));
    }

    @PostMapping("/bulkCreateUserDocument")
    public ResponseEntity<Boolean> bulkCreateUserDocument(@RequestBody List<UserDocument> document) throws Exception {
        log.info(document.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.bulkCreateUserDocument(document));
    }
}
