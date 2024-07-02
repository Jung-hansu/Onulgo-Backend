package com.ssafy.comments.model.service;

import com.ssafy.comments.model.CommentDto;
import com.ssafy.comments.model.CommentLikeDto;

import javax.xml.stream.events.Comment;
import java.sql.SQLException;
import java.util.List;

public interface CommentService {
    List<CommentDto> getComments(CommentLikeDto commentLikeDto) throws SQLException;
    List<CommentDto> getMyComments(int userId) throws SQLException;
    void registerComment(CommentDto commentDto) throws SQLException;
    void modifyComment(CommentDto commentDto) throws SQLException;
    void deleteComment(int commentId) throws SQLException;
    void updateLikeCount(int commentId);
    void addLike(CommentLikeDto commentLikeDto);
    void deleteLike(CommentLikeDto commentLikeDto);
}
