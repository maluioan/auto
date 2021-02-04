<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<h1>hello</h1>
<p>${name}</p>
<p>${host}</p>
<p>${port}</p>


<c:forEach items="${boards}" var="board">
    <p>Board: ${board.name} [${board.id}]</p>
</c:forEach>
