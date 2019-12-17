package com.gjc.service;

import com.gjc.dao.UserDAO;
import com.gjc.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {
//    @Autowired
    @Resource
    private UserDAO userDAO;

    public User getUser(int id){
        return userDAO.selectById(id);
    }
}
