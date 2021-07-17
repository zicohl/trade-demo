package com.trade.demo.controller;

import com.trade.demo.api.model.ResponseResultVo;
import com.trade.demo.bo.ApprovalBo;
import com.trade.demo.bo.ArticleBo;
import com.trade.demo.po.PositionPo;
import com.trade.demo.service.process.WorkflowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/process")
@Slf4j
public class WorkflowController {
    @Autowired
    private WorkflowService workflowService;

    @PostMapping("/start")
    public void submit(@RequestBody ArticleBo article) {
        workflowService.startTaskService(article);
    }

    @GetMapping("/tasks")
    public List<ArticleBo> getTasks(@RequestParam String assignee) {
        return workflowService.getTasks(assignee);
    }

    @PostMapping("/review")
    public void review(@RequestBody ApprovalBo approval) {
        workflowService.submitReview(approval);
    }
}
