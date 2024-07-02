package com.ssafy.gemini.model.service;

import com.ssafy.gemini.model.GeminiDto;

import java.sql.SQLException;
import java.util.List;

public interface GeminiService {
    List<GeminiDto>getPrompts() throws SQLException;
}
