<%@page pageEncoding="utf-8"%>
<img src="images/logo.png" alt="logo" class="left"/>
<!-- EL默认从4个对象中取值，而不会从cookie中取值，要想从cookie中取值，必须按照如下的语法来书写EL：
     cookie.key.value -->
<%-- <span style="font-size:16px; color:red; font-weight:bold;"><span style="font-size:13px; color:#000;">您好:</span>&nbsp;&nbsp;${cookie.adminCode.value }&nbsp;&nbsp;</span> --%>
<span style="font-size:16px; color:red; font-weight:bold;"><span style="font-size:13px; color:#000;">您好:</span>&nbsp;&nbsp;${adminCode }&nbsp;&nbsp;</span>
<a href="/netctoss/logOut.do">[退出]</a> 