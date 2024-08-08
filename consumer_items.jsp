<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>Consumer Food Items</title>
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
        .btn-purchase {
            background-color: orange;
            border-color: black;
            color: black;
        }
        .btn-purchase:hover {
            background-color: red;
            border-color: black;
        }
    </style>
</head>
<body>

    <header>
        <nav class="navbar navbar-expand-md navbar-dark" style="background-color: blue">
            <div>
                <a href="${pageContext.request.contextPath}/home.jsp" class="navbar-brand">Food Waste Reduction Platform</a>
            </div>
            <div class="collapse navbar-collapse">
                <ul class="navbar-nav mr-auto">
                    <c:if test="${sessionScope.userType == 'retailer'}">
                        <li class="nav-item"><a href="${pageContext.request.contextPath}/AddFoodItemServlet?action=list" class="nav-link">Food Items</a></li>
                        <li class="nav-item"><a href="${pageContext.request.contextPath}/SurplusItemsServlet?action=list" class="nav-link">Surplus Items</a></li>
                        <li class="nav-item"><a href="${pageContext.request.contextPath}/CharitableItemsServlet?action=listCharity" class="nav-link">Charitable Items</a></li>
                        <li class="nav-item"><a href="${pageContext.request.contextPath}/ConsumerItemsServlet?action=list" class="nav-link">Consumer Items</a></li>
                    </c:if>
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

    <div class="row">
        <div class="container">
            <h3 class="text-center">List of Consumer Food Items</h3>
            <hr>
            <table class="table table-bordered">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Retailer Name</th>
                        <th>Item Name</th>
                        <th>Price</th>
                        <th>Expiration Date</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="foodItem" items="${listConsumerItems}">
                        <tr>
                            <td><c:out value="${foodItem.id}" /></td>
                            <td><c:out value="${foodItem.retailerName}" /></td>
                            <td><c:out value="${foodItem.itemName}" /></td>
                            <td><c:out value="${foodItem.price}" /></td>
                            <td><c:out value="${foodItem.expirationDate}" /></td>
                            <td>
                                <c:if test="${sessionScope.userType == 'retailer'}">
                                    <a href="${pageContext.request.contextPath}/ConsumerItemsServlet?action=transferToCharity&id=${foodItem.id}" class="btn btn-success">Transfer to Charitable</a>
                                    <a href="${pageContext.request.contextPath}/ConsumerItemsServlet?action=delete&id=${foodItem.id}" class="btn btn-danger" onclick="return confirm('Are you sure you want to delete this item?');">Delete</a>
                                </c:if>
                                <c:if test="${sessionScope.userType != 'retailer'}">
                                    <a href="${pageContext.request.contextPath}/ConsumerItemsServlet?action=purchase&id=${foodItem.id}" class="btn btn-purchase" onclick="return confirm('Confirm purchase?');">Purchase</a>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>
