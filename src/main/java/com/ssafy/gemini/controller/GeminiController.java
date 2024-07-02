package com.ssafy.gemini.controller;


import com.ssafy.comments.model.CommentDto;
import com.ssafy.gemini.model.GeminiDto;
import com.ssafy.gemini.model.service.GeminiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/ai")
public class GeminiController {
    private final GeminiService geminiService;

    public GeminiController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }


    @GetMapping("/prompts")
    public ResponseEntity<?> getPrompts(){

        ResponseEntity<?> response = null;

        try{
            List<GeminiDto>promptList = geminiService.getPrompts();
            response = ResponseEntity.ofNullable(promptList);
        }catch(Exception e){
            log.error("Exception caused by getPlans", e);
            response = ResponseEntity.badRequest().build();
        }

        return response;
    }
}
