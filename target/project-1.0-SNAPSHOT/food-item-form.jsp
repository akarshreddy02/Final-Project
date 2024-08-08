<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>Food Item Management Application</title>
    <link rel="stylesheet"
        href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
        integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
        crossorigin="anonymous">
</head>
<body>

    <header>
        <nav class="navbar navbar-expand-md navbar-dark" style="background-color: blue">
            <div>
                <a href="https://www.xadmin.net" class="navbar-brand">Food Item Management Application</a>
            </div>

            <ul class="navbar-nav">
                <li><a href="<%=request.getContextPath()%>/AddFoodItemServlet?action=list" class="nav-link">Food Items</a></li>
            </ul>
        </nav>
    </header>
    <br>
    <div class="container col-md-6">
        <div class="card">
            <div class="card-body">
                <c:choose>
                    <c:when test="${foodItem != null}">
                        <form action="AddFoodItemServlet?action=update" method="post">
                    </c:when>
                    <c:otherwise>
                        <form action="AddFoodItemServlet?action=insert" method="post">
                    </c:otherwise>
                </c:choose>

                <h2>
                    <c:choose>
                        <c:when test="${foodItem != null}">
                            Edit Food Item
                        </c:when>
                        <c:otherwise>
                            Add New Food Item
                        </c:otherwise>
                    </c:choose>
                </h2>

                <c:if test="${foodItem != null}">
                    <input type="hidden" name="id" value="<c:out value='${foodItem.id}' />" />
                </c:if>

                <fieldset class="form-group">
                    <legend>Food Item Details</legend>
                    <label for="retailer_name">Retailer Name</label> 
                    <input type="text" id="retailer_name" value="<c:out value='${foodItem.retailerName}' />" class="form-control" name="retailer_name" required>
                </fieldset>

                <fieldset class="form-group">
                    <legend>Food Item Details</legend>
                    <label for="item_name">Item Name</label> 
                    <input type="text" id="item_name" value="<c:out value='${foodItem.itemName}' />" class="form-control" name="item_name" required>
                </fieldset>

                <fieldset class="form-group">
                    <legend>Quantity Details</legend>
                    <label for="quantity">Quantity</label> 
                    <input type="number" id="quantity" value="<c:out value='${foodItem.quantity}' />" class="form-control" name="quantity" required>
                </fieldset>

                <fieldset class="form-group">
                    <legend>Price Details</legend>
                    <label for="price">Price</label> 
                    <input type="number" id="price" step="0.01" value="<c:out value='${foodItem.price}' />" class="form-control" name="price" required>
                </fieldset>

                <fieldset class="form-group">
                    <legend>Expiration Details</legend>
                    <label for="expiration_date">Expiration Date</label> 
                    <input type="date" id="expiration_date" value="<c:out value='${foodItem.expirationDate}' />" class="form-control" name="expiration_date" required>
                </fieldset>

                <button type="submit" class="btn btn-success">Save</button>
                </form>
            </div>
        </div>
    </div>
</body>
</html>