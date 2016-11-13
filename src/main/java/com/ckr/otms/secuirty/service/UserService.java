package com.ckr.otms.secuirty.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import com.ckr.otms.secuirty.dao.RoleDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ckr.otms.common.annotation.BaseTx;
import com.ckr.otms.common.annotation.ReadOnlyTx;
import com.ckr.otms.common.bo.HibernateRestPaginationService;
import com.ckr.otms.common.util.StringUtil;
import com.ckr.otms.common.web.util.RestPaginationTemplate.QueryResponse;
import com.ckr.otms.exception.ApplicationException;
import com.ckr.otms.secuirty.dao.UserDAO;
import com.ckr.otms.secuirty.valueobject.Role;

import com.ckr.otms.secuirty.valueobject.User;
import com.ckr.otms.secuirty.valueobject.UserDetailView;
import com.ckr.otms.secuirty.valueobject.UserDetailView.RoleDetailView;
import com.ckr.otms.secuirty.valueobject.UserQueryView;
import com.ckr.otms.secuirty.valueobject.UserServiceForm;
import com.ckr.otms.secuirty.valueobject.UserServiceForm.RoleServiceForm;


@Service
public class UserService {
	
	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private UserDAO userDAO;

	@Autowired
	private RoleDAO roleDAO;

	@Autowired
	@Qualifier("restPaginationService")
	private HibernateRestPaginationService restPaginationService;
	
	@ReadOnlyTx
	public Collection<User> getAllUsers(){
		return userDAO.getAllUsers();
	}
	
	
	@SuppressWarnings("unchecked")
	@ReadOnlyTx
	public QueryResponse<UserQueryView> queryUsers(String userName, String userDesc){
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		if(!StringUtil.isNull(userName)){
			params.put("userName", userName);
		}
		
		if(!StringUtil.isNull(userDesc) ){
			params.put("userDesc", "%" + userDesc +"%");
		}
		
		String queryStr = "select u.userName, u.userDescription, u.locked from User u where 1=1 " +
				          "/*userName| and u.userName = :userName */" + 
				          "/*userDesc| and u.userDescription like :userDesc */";
		
		
		Function<Object[], UserQueryView> mapper = new Function<Object[], UserQueryView>(){

			@Override
			public UserQueryView apply(Object[] row) {
				
				UserQueryView view = new UserQueryView();
				
				view.setUserName((String)row[0]);
				view.setUserDescription((String)row[1]);
				view.setLocked((String)row[2]);
				
				return view;
			}

			
		};
		
		return restPaginationService.query(queryStr, params, mapper);
	}
	
	@ReadOnlyTx
	public UserDetailView getUser(String userName){
		
		UserDetailView result = new UserDetailView();
		
		User user = this.userDAO.getUser(userName);
		
		result.setUserName(user.getUserName());
		result.setUserDescription(user.getUserDescription());
		result.setLocked(user.getLocked());
		
		List<RoleServiceForm> roleList = new ArrayList<RoleServiceForm>();
		
		for(Role role:user.getRoles()){
			
			RoleDetailView roleView = new RoleDetailView();
			
			roleView.setRoleCode(role.getRoleCode());
			roleView.setRoleDescription(role.getRoleDescription());
			
			roleList.add(roleView);
		}
		
		result.setRoles(roleList);
		
		return result;
	}
	
	@BaseTx
	public void createUser(UserServiceForm userForm){
		
		LOG.debug("create new user.");
		
		User user = new User();
		
		
		validateUserInfo(userForm);		
		
		if( this.userDAO.getUser(userForm.getUserName()) != null){
			throw new ApplicationException("security.maintain_user.duplicated_user");
		}
		
		
		user.setUserName(userForm.getUserName());
		user.setUserDescription(userForm.getUserDescription());
		user.setLocked("N");
		
		user.setPassword(encodePassword(userForm.getPassword()));
		
		this.userDAO.saveOrUpdateUser(user);

		this.saveRoles(user, userForm);

		return;
	}

	private void saveRoles(User user, UserServiceForm userForm){

		if(userForm.getRoles() == null){
			return;
		}

		List<Role> roleList = new ArrayList<Role>(userForm.getRoles().size());

		for(RoleServiceForm roleForm:userForm.getRoles()){
			String roleCode = roleForm.getRoleCode();

			Role role = this.roleDAO.getRole(roleCode);

			if(role == null){
				throw new ApplicationException("security.maintain_user.invalid_role");
			}

			roleList.add(role);
		}

		user.setRoles(roleList);
	}



	private void validateUserInfo(UserServiceForm user){
		if(StringUtil.isNull(user.getUserName())){
			throw new ApplicationException("security.maintain_user.user_name_empty");
		}
		
		if(StringUtil.isNull(user.getUserDescription())) {
			throw new ApplicationException("security.maintain_user.user_desc_empty");
		}
	}
	
	private String encodePassword(String pwd){
		return pwd;
	}
	
	@BaseTx
	public void updateUser(UserServiceForm userForm){
		
		LOG.debug("update an existing new user.");
		
		User curUser = this.userDAO.getUser(userForm.getUserName());
		
		if(curUser == null){
			throw new ApplicationException("security.maintain_user.not_existing_user");
		}
		
		validateUserInfo(userForm);
		
		curUser.setUserDescription(userForm.getUserDescription());
		curUser.setLocked(userForm.getLocked());
		
		if(!StringUtil.isNull(userForm.getPassword())){
			curUser.setPassword(encodePassword(userForm.getPassword()));
		}
		
		this.userDAO.updateUser(curUser);

		this.saveRoles(curUser, userForm);

		return;
	}
	
	@BaseTx
	public Collection<String> deleteUsers(Collection<String> userNames){
		Set<String> result = new HashSet<String>();
		
		if(userNames == null){
			return result;
		}
		
		for(String userName:userNames){
			User user = this.userDAO.getUser(userName);
			
			if(user == null){
				continue;
			}
			
			
			LOG.info("delete user: {}", user.getUserName() );
			//if a program for real production, should use delete logically but not physically.
			this.userDAO.deleteUser(user);
			
			result.add(user.getUserName());
		}
		
		
		return result;
	}
	
}
