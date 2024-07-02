package com.ssafy.gemini.model.mapper;

import com.ssafy.gemini.model.GeminiDto;

import java.util.List;

public interface GeminiMapper {
    List<GeminiDto> getPrompts();
}
