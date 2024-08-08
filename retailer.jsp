<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>Food Waste Reduction Platform</title>
    <link rel="stylesheet"
        href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
        integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
        crossorigin="anonymous">
    <style>
        .btn-logout {
            background-color: red;
            border-color: red;
            color: black;
        }
        .btn-logout:hover {
            background-color: darkred;
            border-color: darkred;
        }
    </style>
</head>
<body>

    <header>
        <nav class="navbar navbar-expand-md navbar-dark" style="background-color: blue">
            <a href="${pageContext.request.contextPath}/home.jsp" class="navbar-brand">Food Waste Reduction Platform</a>
            <div class="collapse navbar-collapse">
                <ul class="navbar-nav mr-auto">
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/AddFoodItemServlet?action=list" class="nav-link">Food Items</a>
                    </li>
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/SurplusItemsServlet?action=list" class="nav-link">Surplus List</a>
                    </li>
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/CharitableItemsServlet?action=list" class="nav-link">Charity Items</a>
                    </li>
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/ConsumerItemsServlet?action=list" class="nav-link">Consumer Items</a>
                    </li>
                </ul>
                <ul class="navbar-nav ml-auto">
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/LogoutServlet" class="btn btn-logout nav-link" onclick="return confirm('Are you sure you want to log out?');">Logout</a>
                    </li>
                </ul>
            </div>
        </nav>
    </header>
    <br>

    <div class="container">
        <h3 class="text-center">List of Food Items</h3>
        <hr>
        <div class="text-left mb-3">
            <a href="${pageContext.request.contextPath}/AddFoodItemServlet?action=new" class="btn btn-success">Add New Food Item</a>
        </div>
        <table class="table table-bordered">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Retailer Name</th>
                    <th>Item Name</th>
                    <th>Quantity</th>
                    <th>Price</th>
                    <th>Expiration Date</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="foodItem" items="${listFoodItems}">
                    <tr>
                        <td><c:out value="${foodItem.id}" /></td>
                        <td><c:out value="${foodItem.retailerName}" /></td>
                        <td><c:out value="${foodItem.itemName}" /></td>
                        <td><c:out value="${foodItem.quantity}" /></td>
                        <td><c:out value="${foodItem.price}" /></td>
                        <td><c:out value="${foodItem.expirationDate}" /></td>
                        <td>
                            <a href="${pageContext.request.contextPath}/AddFoodItemServlet?action=edit&id=${foodItem.id}" class="btn btn-primary">Edit</a>
                            <a href="${pageContext.request.contextPath}/AddFoodItemServlet?action=delete&id=${foodItem.id}" class="btn btn-danger" onclick="return confirm('Are you sure you want to delete this item?');">Delete</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>
