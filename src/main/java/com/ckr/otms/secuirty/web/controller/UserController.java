package com.ckr.otms.secuirty.web.controller;


import com.ckr.otms.common.web.annotation.RestCreateRequestMapping;
import com.ckr.otms.common.web.annotation.RestGetRequestMapping;
import com.ckr.otms.common.web.annotation.RestQueryRequestMapping;
import com.ckr.otms.common.web.annotation.RestUpdateRequestMapping;
import com.ckr.otms.common.web.constant.RequestPathConstant;
import com.ckr.otms.common.web.util.RestPaginationTemplate;
import com.ckr.otms.secuirty.constant.SecuriedAttribute;
import com.ckr.otms.secuirty.service.RoleService;
import com.ckr.otms.secuirty.service.UserService;
import com.ckr.otms.secuirty.valueobject.Role;
import com.ckr.otms.secuirty.valueobject.UserDetailView;
import com.ckr.otms.secuirty.valueobject.UserQueryView;
import com.ckr.otms.secuirty.valueobject.UserServiceForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    private static final String URL = RequestPathConstant.INTERNAL_WEB_DATA + "/maintainUsers";

    @RestQueryRequestMapping(value = URL)
    @Secured(SecuriedAttribute.ATT_AUTHENTICATED)
    public ResponseEntity<List<UserQueryView>> queryUsers(@RequestParam("userName") final String userName, @RequestParam("userDesc") final String userDesc) {

        return new RestPaginationTemplate<UserQueryView>() {

            @Override
            protected QueryResponse<UserQueryView> doQuery() {

                QueryResponse<UserQueryView> result = userService.queryUsers(userName, userDesc);

                return result;
            }

        }.query();

    }

    @RestGetRequestMapping(value = URL + "/{userId}")
    @Secured(SecuriedAttribute.ATT_AUTHENTICATED)
    public UserDetailView getUser(@PathVariable("userId") String userName) {

        return this.userService.getUser(userName);

    }


    @RestCreateRequestMapping(value = URL + "/*")
    @Secured(SecuriedAttribute.ATT_AUTHENTICATED)
    public Collection<UserQueryView> createUser(@RequestBody UserServiceForm user) {

        this.userService.createUser(user);


        return userService.queryUsers(user.getUserName(), null).getContent();
    }


    @RestUpdateRequestMapping(value = URL + "/*")
    @Secured(SecuriedAttribute.ATT_AUTHENTICATED)
    public Collection<UserQueryView> updateUser(@RequestBody UserServiceForm user) {

        this.userService.updateUser(user);

        return userService.queryUsers(user.getUserName(), null).getContent();
    }

    @RequestMapping(value = RequestPathConstant.INTERNAL_WEB_DATA + "/deleteUsers")
    @Secured(SecuriedAttribute.ATT_AUTHENTICATED)
    @ResponseBody
    public Collection<String> deleteUsers(@RequestBody Collection<String> userNames) {
        return this.userService.deleteUsers(userNames);
    }


    @RequestMapping(value = URL + "/getAllRoles")
    @Secured(SecuriedAttribute.ATT_AUTHENTICATED)
    @ResponseBody
    public Collection<Role> getAllRoles() {
        return this.roleService.getAllRoles();
    }

}
