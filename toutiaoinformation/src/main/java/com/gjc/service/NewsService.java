package com.gjc.service;

import com.gjc.dao.NewsDAO;
import com.gjc.model.News;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class NewsService {
    @Resource
    private NewsDAO newsDAO;

    public List<News> getLatestNews(int userId, int offset, int limit) {
        return newsDAO.selectByUserIdAndOffset(userId,offset,limit);
    }
}
