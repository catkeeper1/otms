delete from `user`;
INSERT INTO `user` (`USER_NAME`,`PASSWORD`,`USER_DESCRIPTION`,`IS_LOCKED`,`LAST_MODIFIED_TIMESTAMP`) VALUES ('abc','abc','abc','N','2013-10-10 16:00:00');
INSERT INTO `user` (`USER_NAME`,`PASSWORD`,`USER_DESCRIPTION`,`IS_LOCKED`,`LAST_MODIFIED_TIMESTAMP`) VALUES ('ddd','dd','ddd','N','2013-10-10 16:00:00');
INSERT INTO `user` (`USER_NAME`,`PASSWORD`,`USER_DESCRIPTION`,`IS_LOCKED`,`LAST_MODIFIED_TIMESTAMP`) VALUES ('def','def','def','N','2013-10-10 16:00:00');



delete from `menu`;

INSERT INTO `menu`(`CODE`,`PARENT_CODE`,`DESCRIPTION`,`FUNCTION_POINT`,`MODULE`,`LAST_MODIFIED_TIME`) VALUES ('ROLE_MAIN', 'SECURITY', 'Role Maintenance', NULL, NULL, NULL);
INSERT INTO `menu`(`CODE`,`PARENT_CODE`,`DESCRIPTION`,`FUNCTION_POINT`,`MODULE`,`LAST_MODIFIED_TIME`) VALUES ('SECURITY', NULL, 'Security Setting', NULL, NULL, NULL);
INSERT INTO `menu`(`CODE`,`PARENT_CODE`,`DESCRIPTION`,`FUNCTION_POINT`,`MODULE`,`LAST_MODIFIED_TIME`) VALUES ('USER_MAIN', 'SECURITY', 'User Maintenance', NULL, 'app/security/user/UserMaintenance', NULL);


delete from `role`;

INSERT INTO `role` (`ROLE_CODE`,`PARENT_ROLE_CODE`,`ROLE_DESCRIPTION`,`LAST_MODIFIED_TIMESTAMP`) VALUES('SECURITY_ADMIN', NULL, 'Security Admin' , '2013-10-10 16:00:00');
INSERT INTO `role` (`ROLE_CODE`,`PARENT_ROLE_CODE`,`ROLE_DESCRIPTION`,`LAST_MODIFIED_TIMESTAMP`) VALUES('USER_ADMIN', 'SECURITY_ADMIN', 'User Admin' , '2013-10-10 16:00:00');
INSERT INTO `role` (`ROLE_CODE`,`PARENT_ROLE_CODE`,`ROLE_DESCRIPTION`,`LAST_MODIFIED_TIMESTAMP`) VALUES('ROLE_ADMIN', 'SECURITY_ADMIN', 'Role Admin' , '2013-10-10 16:00:00');
INSERT INTO `role` (`ROLE_CODE`,`PARENT_ROLE_CODE`,`ROLE_DESCRIPTION`,`LAST_MODIFIED_TIMESTAMP`) VALUES('GROUP_ADMIN', 'SECURITY_ADMIN', 'Group Admin' , '2013-10-10 16:00:00');
