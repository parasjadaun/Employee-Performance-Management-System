<%@ page contentType="text/html;charset=UTF-8" %><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<nav class="navbar navbar-expand-lg navbar-dark epms-navbar">
  <div class="container">
    <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/dashboard"><i class="bi bi-speedometer2 me-2"></i>EPMS</a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#mainNav"><span class="navbar-toggler-icon"></span></button>
    <div class="collapse navbar-collapse" id="mainNav">
      <ul class="navbar-nav me-auto mb-2 mb-lg-0">
        <c:if test="${sessionScope.user.role == 'ADMIN'}">
          <li class="nav-item"><a class="nav-link ${pageContext.request.servletPath == '/admin/employees' ? 'active' : ''}" href="${pageContext.request.contextPath}/admin/employees"><i class="bi bi-people me-1"></i>Employees</a></li>
          <li class="nav-item"><a class="nav-link ${pageContext.request.servletPath == '/admin/departments' ? 'active' : ''}" href="${pageContext.request.contextPath}/admin/departments"><i class="bi bi-building me-1"></i>Departments</a></li>
          <li class="nav-item"><a class="nav-link ${pageContext.request.servletPath == '/admin/cycles' ? 'active' : ''}" href="${pageContext.request.contextPath}/admin/cycles"><i class="bi bi-calendar3 me-1"></i>Cycles</a></li>
        </c:if>
        <c:if test="${sessionScope.user.role == 'MANAGER'}"><li class="nav-item"><a class="nav-link ${pageContext.request.servletPath == '/team/goals' ? 'active' : ''}" href="${pageContext.request.contextPath}/team/goals"><i class="bi bi-bullseye me-1"></i>Goals</a></li><li class="nav-item"><a class="nav-link ${pageContext.request.servletPath == '/team/reviews' ? 'active' : ''}" href="${pageContext.request.contextPath}/team/reviews"><i class="bi bi-star me-1"></i>Reviews</a></li></c:if>
        <c:if test="${sessionScope.user.role == 'EMPLOYEE'}"><li class="nav-item"><a class="nav-link ${pageContext.request.servletPath == '/employee/goals' ? 'active' : ''}" href="${pageContext.request.contextPath}/employee/goals"><i class="bi bi-bullseye me-1"></i>My goals</a></li><li class="nav-item"><a class="nav-link ${pageContext.request.servletPath == '/employee/self-assessment' ? 'active' : ''}" href="${pageContext.request.contextPath}/employee/self-assessment"><i class="bi bi-journal-check me-1"></i>Reviews</a></li></c:if>
        <li class="nav-item"><a class="nav-link ${pageContext.request.servletPath == '/reports' ? 'active' : ''}" href="${pageContext.request.contextPath}/reports"><i class="bi bi-bar-chart me-1"></i>Reports</a></li>
        <li class="nav-item"><a class="nav-link ${pageContext.request.servletPath == '/notifications' ? 'active' : ''}" href="${pageContext.request.contextPath}/notifications"><i class="bi bi-bell me-1"></i>Notifications</a></li>
      </ul>
      <button class="btn btn-link nav-theme-toggle me-2" type="button" data-theme-toggle aria-label="Toggle dark mode"><i class="bi bi-moon-stars"></i></button>
      <span class="navbar-text me-3 user-chip"><i class="bi bi-person-circle me-1"></i><c:out value="${sessionScope.user.name}"/> <small><c:out value="${sessionScope.user.roleLabel}"/></small></span>
      <a class="btn btn-light btn-sm" href="${pageContext.request.contextPath}/logout"><i class="bi bi-box-arrow-right"></i>Logout</a>
    </div>
  </div>
</nav>
