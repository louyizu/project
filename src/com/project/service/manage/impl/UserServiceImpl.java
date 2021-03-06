/**
 * 
 */
package com.project.service.manage.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.constant.Global;
import com.project.dao.UserDao;
import com.project.domain.PageView;
import com.project.domain.mapping.User;
import com.project.service.manage.UserService;
import com.project.util.MD5Util;
import com.project.util.NullUtil;

/**
 * @author dingjs
 *
 */
@Service(value="userService")
public class UserServiceImpl implements UserService {
	private UserDao userDao;
	
	@Autowired
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public List<User> findAll() {
		return userDao.findAll();
	}

	@Override
	public User findByUserName(String userName) {
		//System.out.println(UserServiceImpl.class.getResource("/"));
		if(NullUtil.isNullOrEmpty(userName)){
			//抛出异常
		}
		User user = userDao.findByUserName(userName);
		return user;
	}

	@Override
	public User getById(Long id) {
		return userDao.getById(id);
	}

	@Override
	public PageView<User> getPageViewList(Map<String, String> params, Integer pageSize, Integer currentPage) {
		if(pageSize == null) {
			pageSize = Global.INTEGER_DEFAULT_PAGE_SIZE;
		}
		if(currentPage == null) {
			currentPage = Global.INTEGER_DEFAULT_CURRENT_PAGE;
		}
		Integer recordCount = userDao.getCount(params);
		List<User> recordList = userDao.getPageList(params,PageView.calcFirstResult(currentPage, pageSize),pageSize);
		return new PageView<User>(pageSize, currentPage, recordCount, recordList);
	}

	@Override
	public void saveOrUpdate(User user) {
		if(user == null){
			//非法数据,抛出异常
			return ;
		}
		user.setPassword(MD5Util.encodeOneCount(user.getPassword()));
		if(user.getId() != null) {
			user.setUpdateTime(new Date());
			userDao.update(user);
		}else {
			user.setCreateTime(new Date());
			userDao.save(user);
		}
	}

	@Override
	public void saveUsers(List<User> list) {
		for (User user : list) {
			saveOrUpdate(user);
		}
	}
	
	
}
