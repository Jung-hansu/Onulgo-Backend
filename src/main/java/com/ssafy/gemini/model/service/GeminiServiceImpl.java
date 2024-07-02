package com.ssafy.gemini.model.service;

import com.ssafy.gemini.model.GeminiDto;
import com.ssafy.gemini.model.mapper.GeminiMapper;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class GeminiServiceImpl implements GeminiService{

    private final GeminiMapper geminiMapper;

    public GeminiServiceImpl(GeminiMapper geminiMapper) {
        this.geminiMapper = geminiMapper;
    }

    @Override
    public List<GeminiDto> getPrompts() throws SQLException {
        return geminiMapper.getPrompts();
    }
}
