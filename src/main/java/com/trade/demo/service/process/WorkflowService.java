package com.trade.demo.service.process;

import com.trade.demo.bo.ApprovalBo;
import com.trade.demo.bo.ArticleBo;
import com.trade.demo.dao.PositionDao;
import com.trade.demo.po.PositionPo;
import liquibase.pro.packaged.S;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WorkflowService {
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    public void startTaskService(ArticleBo article) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("author", article.getAuthor());
        variables.put("url", article.getUrl());
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("articleReview", variables);
        log.info("instance id {}", instance.getId());
    }

    public List<ArticleBo> getTasks(String assignee) {
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup(assignee).list();
        return tasks.stream().map(task -> {
            Map<String, Object> variables = taskService.getVariables(task.getId());
            return new ArticleBo(task.getId(), (String) variables.get("author"), (String) variables.get("url"));
        }).collect(Collectors.toList());
    }

    public void submitReview(ApprovalBo approval) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("approved", approval.isStatus());
        taskService.complete(approval.getId(), variables);
    }
}
