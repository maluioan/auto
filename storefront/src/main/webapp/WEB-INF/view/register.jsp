<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<p>Please register</p>
<c:if test="${not empty failureReason}">
    <spring:message key="login.error.reason.${failureReason}"/>
</c:if>

<c:forEach var="failureError" items="${registerErrors}">
    <strong>${failureError}</strong>
</c:forEach>

<form:form method="POST" action="/register" modelAttribute="registerUserForm">
    <table>
        <tr>
            <td><form:label path="username">Username : </form:label></td>
            <td><form:input path="username"/></td>
        </tr>
        <tr>
            <td><form:label path="name">Name : </form:label></td>
            <td><form:input path="name"/></td>
        </tr>
        <tr>
            <td><form:label path="surname">Surname : </form:label></td>
            <td><form:input path="surname"/></td>
        </tr>
        <tr>
            <td><form:label path="telephone">
                Telephone Number: </form:label></td>
            <td><form:input path="telephone"/></td>
        </tr>

        <tr>
            <td><form:label path="email">Email : </form:label></td>
            <td><form:input path="email"/></td>
        </tr>
        <tr>
            <td><input type="submit" value="Submit"/></td>
        </tr>
    </table>
</form:form>