package com.ssafy.comments.model.service;

import com.ssafy.comments.model.CommentDto;
import com.ssafy.comments.model.CommentLikeDto;
import com.ssafy.comments.model.mapper.CommentMapper;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;

    public CommentServiceImpl(CommentMapper commentMapper){
        this.commentMapper = commentMapper;
    }

    @Override
    public List<CommentDto> getComments(CommentLikeDto commentLikeDto) throws SQLException {
        return commentMapper.getComments(commentLikeDto);
    }

    @Override
    public List<CommentDto> getMyComments(int userId) throws SQLException {
        return commentMapper.getMyComments(userId);
    }

    @Override
    public void registerComment(CommentDto commentDto) throws SQLException {
        commentMapper.registerComment(commentDto);
    }

    @Override
    public void modifyComment(CommentDto commentDto) throws SQLException {
        commentMapper.modifyComment(commentDto);
    }

    @Override
    public void deleteComment(int commentId) throws SQLException {
        commentMapper.deleteComment(commentId);
    }

    @Override
    public void updateLikeCount(int commentId) {
        commentMapper.updateLikeCount(commentId);
    }

    @Override
    public void addLike(CommentLikeDto commentLikeDto) {
        commentMapper.addLike(commentLikeDto);
    }

    @Override
    public void deleteLike(CommentLikeDto commentLikeDto) {
        commentMapper.deleteLike(commentLikeDto);
    }
}
