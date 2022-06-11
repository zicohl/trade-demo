package com.trade.demo.controller;

import com.trade.demo.vo.DistrictVo;
import com.trade.demo.vo.ResponseResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Validated
@RestController
@RequestMapping("/upload")
@Slf4j
public class UploadController {

    @RequestMapping(value = "/single",
            produces = {"application/json;charset=UTF-8"},
            method = RequestMethod.PUT)
    public ResponseEntity<ResponseResultVo<Long>> upload(@RequestParam MultipartFile file) {
        log.info("upload {}", file.getOriginalFilename());

        ResponseResultVo<Long> result = new ResponseResultVo<>();
        result.setStatus("success");
        result.setData(1L);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
