<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://java.sun.com/jsp/jstl/fmt" %>


<p>Please login</p>

<c:if test="${not empty failureReason}">
    <spring:message key="login.error.reason.${failureReason}"/>
</c:if>

<c:if test="${not empty newUserName and not empty newUserSurName}">
    Salut <strong>${newUserName} ${newUserSurName}</strong>, te rog asteapta ca un administrator sa te autorizeze.
</c:if>

<form action="/login_check" method='POST'>
    <table>
        <tr>
            <td>User:</td>
            <td><input type='text' name='util' value=''></td>
        </tr>
        <tr>
            <td>Password:</td>
            <td><input type='password' name='par' /></td>
        </tr>
        <tr>
            <td><input name="submit" type="submit" value="submit" /></td>
        </tr>
    </table>
</form>
