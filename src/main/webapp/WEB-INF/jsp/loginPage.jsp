<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<html>
<head>

<title>Login OTMS</title>
<link href="./pub/app/css/login.css" rel="stylesheet"/>


</head>
<body>
	<!-- 
<div align="center" style="height:100%;background-color:#ffff00; vertical-align:middle ;padding-top: 24px;padding-bottom: 24px;">
 -->

	<table align="center" height="100%">
		<tr>
			<td valign="middle">

				<div class="loginPad" >
					<form action="doAuthentication.do" method="post" id="loginForm"
						autocomplete="off">
						<h2>OTMS System</h2>
						<table align="center" width="100%">
							<col width="40%"/>
							<col width="60%"/>
							<tr>
								<td align="right">
									<fmt:message key="sys.login.username" />
								</td>
								<td><input type="text" name="username" maxlength="30"/></td>
							</tr>
							<tr>
								<td align="right">
									<fmt:message key="sys.login.password" />
								</td>
								<td><input type="password" name="password" maxlength="30" /></td>
							</tr>
							<tr>
								<td></td>
								<td>
									<div onclick='document.getElementById("loginForm").submit()' class="appButton">
										<fmt:message key="sys.login.logon"/>
									</div>
								</td>
							</tr>
							<tr>
								<td colspan="2">
								<div width="100%" style="color:red; padding:10px">
									<c:choose>  
										<c:when test="${param.msg == 'BC'}">
											<fmt:message key="sys.error.logon.badcredential"/>
										</c:when>
										<c:when test="${param.msg == 'TO'}">
										<fmt:message key="sys.error.need_authentication"/>
									</c:when>
									</c:choose>
								</div>	
								</td>
							</tr>
						</table>
					</form>
				</div>

			</td>
		</tr>
		
		<tr>
			<td height="200px">
			
			</td>
		</tr>
	</table>

</body>

</html>