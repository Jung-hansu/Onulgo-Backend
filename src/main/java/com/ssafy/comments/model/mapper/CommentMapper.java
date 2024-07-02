package com.ssafy.comments.model.mapper;

import com.ssafy.comments.model.CommentDto;
import com.ssafy.comments.model.CommentLikeDto;
import com.ssafy.reviews.model.ReviewLikeDto;
import org.apache.ibatis.annotations.Mapper;

import java.sql.SQLException;
import java.util.List;

@Mapper
public interface CommentMapper {
    List<CommentDto> getComments(CommentLikeDto commentLikeDto) throws SQLException;
    List<CommentDto> getMyComments(int userId) throws SQLException;
    void registerComment(CommentDto commentDto) throws SQLException;
    void modifyComment(CommentDto commentDto) throws SQLException;
    void deleteComment(int commentId) throws SQLException;
    void updateLikeCount(int commentId);
    void addLike(CommentLikeDto commentLikeDto);
    void deleteLike(CommentLikeDto commentLikeDto);
}
