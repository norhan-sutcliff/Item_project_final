<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Item Details</title>
  <link rel="stylesheet" href="css/show-items.css">
</head>
<body>
<div class="layer">
  <h1>Item Details</h1>

  <c:if test="${empty row}">
    <p><strong>Item not found.</strong></p>
    <p><a class="button" href="ItemController?action=LOAD_ITEMS">Back To Items</a></p>
  </c:if>

  <c:if test="${not empty row}">
    <table class="table">
      <thead>
        <tr>
          <th>ID</th><th>Name</th><th>Price</th><th>Total</th>
          <th>Model</th><th>Type</th><th>Description</th><th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>${row.id}</td>
          <td>${row.name}</td>
          <td>${row.price}</td>
          <td>${row.totalNumber}</td>

          <c:choose>
            <c:when test="${row.itemDetailsId gt 0}">
              <td>${row.model}</td>
              <td>${row.type}</td>
              <td>${row.description}</td>
            </c:when>
            <c:otherwise>
              <td colspan="3"><em>No details</em></td>
            </c:otherwise>
          </c:choose>
        </tr>
      </tbody>
    </table>

    <p style="margin-top:16px;">
      <a class="button" href="ItemController?action=LOAD_ITEMS">Back To Items</a>
    </p>
  </c:if>
</div>
</body>
</html>
