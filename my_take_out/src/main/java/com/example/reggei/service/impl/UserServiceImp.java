package com.example.reggei.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggei.entity.User;
import com.example.reggei.mapper.UserMapper;
import com.example.reggei.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author Hao zeng
 * @create 2022-06-26 11:15
 */
@Service
public class UserServiceImp extends ServiceImpl<UserMapper, User> implements UserService {
}
