<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Update Item Details</title>
  <link rel="stylesheet" href="css/add-item.css">
</head>
<body>
<div class="layer">
  <h2>Update Details for Item ID: ${itemId}</h2>

  <c:if test="${not empty errorMessage}">
    <div style="background-color:#ffe3e3; color:#a40000; border:1px solid #ffbdbd;
                padding:10px; margin-bottom:15px; border-radius:5px;">
      <strong>Error:</strong> ${errorMessage}
    </div>
  </c:if>

  <form action="ItemController" method="post">
    <input type="hidden" name="action" value="UPDATE_ITEM_DETAILS">
    <input type="hidden" name="id" value="${itemId}">

    <label>Model:</label><br>
    <div class="input-data">
    <input type="text" name="model" required><br><br>
	</div>
	
    <label>Type:</label><br>
    <div class="input-data">
    <input type="text" name="type"  required><br><br>
	</div>
	
    <label>Description:</label><br>
    <div class="input-data">
    <textarea name="description" rows="4" cols="50" required></textarea><br><br>
	</div>
	
    <input type="submit" value="Update Details" class="button">
    <a href="ItemController?action=SHOW_DETAILS&id=${itemId}" class="button">Cancel</a>
  </form>
</div>
</body>
</html>
