<%@ page contentType="text/html;charset=UTF-8" %><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>
<head>
  <title>Departments | EPMS</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
  <link href="${pageContext.request.contextPath}/assets/app.css" rel="stylesheet">
</head>
<body>
<jsp:include page="/WEB-INF/views/header.jsp"/>
<main class="container page-shell">
  <div class="page-heading d-flex justify-content-between align-items-end flex-wrap gap-3">
    <div>
      <div class="text-primary fw-semibold small mb-2"><i class="bi bi-building me-1"></i>ORGANIZATION</div>
      <h1>Departments</h1>
      <p>Organize teams and see how employees are distributed.</p>
    </div>
    <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/dashboard">
      <i class="bi bi-arrow-left"></i>Back to dashboard
    </a>
  </div>

  <div class="row g-4">
    <div class="col-lg-8">
      <div class="card">
        <div class="card-header d-flex justify-content-between align-items-center">
          <h2 class="h5 mb-0"><i class="bi bi-diagram-3 text-primary me-2"></i>All departments</h2>
          <span class="badge rounded-pill text-bg-light">${departments.size()} departments</span>
        </div>
        <div class="card-body">
          <div class="row g-3">
            <c:forEach var="d" items="${departments}">
              <div class="col-md-6">
                <div class="border rounded-4 p-3 h-100 d-flex align-items-center justify-content-between">
                  <div class="d-flex align-items-center gap-3">
                    <span class="stat-icon"><i class="bi bi-building"></i></span>
                    <div>
                      <h3 class="h6 mb-1"><c:out value="${d.name}"/></h3>
                      <small class="text-secondary"><c:out value="${d.employee_count}"/> employees</small>
                    </div>
                  </div>
                  <i class="bi bi-chevron-right text-secondary"></i>
                </div>
              </div>
            </c:forEach>
            <c:if test="${empty departments}">
              <div class="empty-state"><i class="bi bi-building fs-2 d-block mb-2"></i>No departments found yet.</div>
            </c:if>
          </div>
        </div>
      </div>
    </div>
    <div class="col-lg-4">
      <div class="card">
        <div class="card-header"><h2 class="h5 mb-0"><i class="bi bi-plus-circle text-success me-2"></i>Add department</h2></div>
        <div class="card-body">
          <p class="text-secondary small">Create a department for a new team or business unit.</p>
          <form method="post">
            <label class="form-label" for="departmentName">Department name</label>
            <input id="departmentName" class="form-control mb-3" name="name" placeholder="e.g. Customer Success" required maxlength="100">
            <button class="btn btn-primary w-100"><i class="bi bi-plus-lg"></i>Add department</button>
          </form>
        </div>
      </div>
      <div class="card mt-4 bg-primary text-white">
        <div class="card-body">
          <i class="bi bi-lightbulb fs-4"></i>
          <h2 class="h6 mt-3">Tip</h2>
          <p class="small mb-0 text-white-50">Assign employees to departments from the employee management page to keep reports accurate.</p>
        </div>
      </div>
    </div>
  </div>
</main>
<jsp:include page="/WEB-INF/views/footer.jsp"/>
</body>
</html>
