<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Food Waste Reduction Platform</title>
    <link rel="stylesheet"
        href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
        integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
        crossorigin="anonymous">
    <style>
        .intro {
            margin-top: 50px;
            text-align: center;
        }
        .btn-logout {
            background-color: red;
            border-color: red;
            color: black;
        }
        .btn-logout:hover {
            background-color: darkred;
            border-color: darkred;
        }
        .intro-image {
            width: 100%;
            max-width: 600px;
            height: auto;
            display: block;
            margin: 20px auto;
        }
    </style>
</head>
<body>
    <header>
        <nav class="navbar navbar-expand-md navbar-dark" style="background-color: blue">
            <a href="${pageContext.request.contextPath}/home.jsp" class="navbar-brand">Food Waste Reduction Platform</a>
            <div class="collapse navbar-collapse">
                <ul class="navbar-nav mr-auto">
                    <c:if test="${sessionScope.userType == 'retailer'}">
                        <li class="nav-item"><a href="${pageContext.request.contextPath}/AddFoodItemServlet?action=list" class="nav-link">Food Items</a></li>
                        <li class="nav-item"><a href="${pageContext.request.contextPath}/SurplusItemsServlet?action=list" class="nav-link">Surplus Items</a></li>
                        <li class="nav-item"><a href="${pageContext.request.contextPath}/CharitableItemsServlet?action=listCharity" class="nav-link">Charitable Items</a></li>
                        <li class="nav-item"><a href="${pageContext.request.contextPath}/ConsumerItemsServlet?action=list" class="nav-link">Consumer Items</a></li>
                    </c:if>
                    <c:if test="${sessionScope.userType == 'consumer'}">
                        <li class="nav-item"><a href="${pageContext.request.contextPath}/ConsumerItemsServlet?action=list" class="nav-link">Consumer Items</a></li>
                    </c:if>
                    <c:if test="${sessionScope.userType == 'charitable_organization'}">
                        <li class="nav-item"><a href="${pageContext.request.contextPath}/CharitableItemsServlet?action=listCharity" class="nav-link">Charitable Items</a></li>
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
    <div class="container intro">
        <h1>Welcome to the Food Waste Reduction Platform</h1>
        <p>Our platform aims to reduce food waste by connecting retailers with consumers and charitable organizations. Retailers can list surplus food items, which can then be distributed to consumers or donated to charitable organizations. This helps ensure that excess food is put to good use, reducing waste and supporting those in need.</p>
        <p>Use the navigation bar to explore the available features based on your needs.</p>
        <img src="${pageContext.request.contextPath}/images/home.jpg" alt="Food Waste Reduction" class="intro-image">
</body>
</html>
