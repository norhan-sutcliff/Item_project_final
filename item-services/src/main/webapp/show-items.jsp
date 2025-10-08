<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Show Items</title>
    <link rel="stylesheet" href="css/show-items.css">
</head>
<body>
<div class="layer">
    <table>
        <h1>Items</h1>
        <thead>
        <tr>
            <th>ID</th>
            <th>NAME</th>
            <th>PRICE</th>
            <th>TOTAL_NUMBER</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        
        <c:forEach var="item" items="${allItems}">
	        <tr>
	            <td>${item.id}</td>
	            <td>${item.name}</td>
	            <td>${item.price}</td>
	            <td>${item.totalNumber}</td>
	            <td>
  <!-- Always visible: core item buttons -->
  <a href="ItemController?action=UPDATE&id=${item.id}">Update</a>
  <a href="ItemController?action=DELETE&id=${item.id}">Delete</a>
  <a href="ItemController?action=LOAD_ITEM&id=${item.id}">Show</a>

  <!-- Conditionally visible: details buttons -->
  <c:choose>
    <c:when test="${item.itemDetailsId gt 0}">
      <!-- item has details -->
      <a href="ItemController?action=SHOW_DETAILS&id=${item.id}">Show Details</a>
      <a href="ItemController?action=UPDATE_DETAILS&id=${item.id}">Update Details</a>
      <a href="ItemController?action=DELETE_DETAILS&id=${item.id}">Delete Details</a>
    </c:when>
    <c:otherwise>
      <!-- item has NO details -->
      <a href="ItemController?action=ADD_DETAILS&id=${item.id}">Add Details</a>
    </c:otherwise>
  </c:choose>
</td>

	        </tr>
        </c:forEach>
        
        
        </tbody>
    </table>


    <button ><a href="add-item.html" >Add Item</a></button>
    <button><a href="LogoutController">Logout</a></button>
    <button onclick="return confirm('Are you sure?');"><a href="DeleteAccountController">Delete Account</a></button>

</div>

</body>
</html>