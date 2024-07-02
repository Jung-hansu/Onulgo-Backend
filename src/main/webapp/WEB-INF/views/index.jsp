<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="root" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>상품 관리 사이트</title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f4f4;
        }

        .navbar {
            background-color: #333;
            color: white;
            padding: 10px 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .navbar a {
            color: white;
            text-decoration: none;
            padding: 10px 15px;
        }

        .container {
            max-width: 1200px;
            margin: 20px auto;
            padding: 20px;
            background-color: white;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        h1 {
            color: #333;
        }
    </style>
</head>
<body>
<div class="navbar">
    <div>
        <a href="${root }">상품 관리</a>
    </div>
    <div>
        <c:if test="">
            <a href="">로그인</a>
        </c:if>
        <c:if test="">
            님, 로그인 하였습니다.
            <a href="">로그아웃</a>
        </c:if>
        <a href="/list">상품 목록</a> <a
            href="/regist">상품 정보 등록</a>
    </div>
</div>

<div class="container">
    <h1>안녕하세요, 상품 관리 사이트입니다.</h1>
    <p>이곳에서는 다양한 상품의 정보를 관리하고 업데이트할 수 있습니다. 편리한 관리를 위해 사용해 보세요.</p>
</div>
</body>
</html>
