<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1">
    <title>Contact Support</title>
    
	<script src="<c:url value='/webjars/jquery/3.7.1/jquery.min.js'/>"></script>
	<script src="<c:url value='/webjars/bootstrap/5.3.3/js/bootstrap.bundle.min.js'/>"></script>
	<script src="<c:url value='/webjars/select2/4.0.13/js/select2.min.js'/>"></script>
	<script src="<c:url value='/resources/javascript/index.js'/>"></script>

	<link rel="stylesheet" href="<c:url value='/webjars/bootstrap/5.3.3/css/bootstrap.min.css'/>"/>
	<link rel="stylesheet" href="<c:url value='/webjars/select2/4.0.13/css/select2.min.css'/>"/>
	<link rel="stylesheet" href="<c:url value='/webjars/font-awesome/6.5.1/css/all.min.css'/>"/>
    
    <style>
        /* General Styles */
        body {
            font-family: 'Poppins', sans-serif;
            background: #F3F4F6;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            color: #333;
            background: url('<c:url value="/resources/Images/img_Contect.jpg"/>') no-repeat center fixed;
            background-size: cover;
        }
    
        body::before {
            content: "";
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(255, 255, 255, 0.33);
            z-index: -1;
        }

        .container-box {
            background: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0px 4px 15px rgba(0, 0, 0, 0.1);
            width: auto;
            text-align: center;
            opacity: 0; /* Initially hidden */
            transform: scale(0.8); /* Initial scale */
            animation: fadeIn 1s forwards; /* Animation on load */
        }

        h2 {
            font-size: 34px;
            margin-bottom: 15px;
            color: #2563EB;
            font-weight: 600;
        }

        h3 {
            font-size: 20px;
            margin-bottom: 10px;
            color: #333;
        }

        p {
            font-size: 16px;
            line-height: 1.6;
            margin-bottom: 15px;
        }

        li {
            text-align: left;
            font-size: 24px;
            line-height: 1.5;
            margin-left: 20px;
            margin-bottom: 10px;
        }

        .btn-back {
            margin-top: 20px;
            display: inline-block;
            padding: 12px 20px;
            background: #2563EB;
            color: white;
            text-decoration: none;
            border-radius: 6px;
            font-size: 20px;
            transition: 0.3s;
        }

        .btn-back:hover {
            background: #1E40AF;
            transform: translateX(-5px); /* Smooth animation on hover */
        }

        /* Animation for container-box */
        @keyframes fadeIn {
            0% {
                opacity: 0;
                transform: scale(0.8);
            }
            100% {
                opacity: 1;
                transform: scale(1);
            }
        }

        /* Responsive Styles */
        @media (max-width: 768px) {
            .container-box {
                padding: 20px;
                max-width: 100%;
            }

            h2 {
                font-size: 24px;
            }

            h3 {
                font-size: 18px;
            }

            p {
                font-size: 14px;
            }

            li {
                font-size: 16px;
            }

            .btn-back {
                font-size: 14px;
                padding: 10px 15px;
            }
        }
    </style>
</head>
<body>

<div class="container-box">
    <h2>MANUAL PATHS</h2>
    <ul>
        <li><b>MANUAL FOLDER:</b> C:\Sports\Manual</li>
        <li><b>MANUAL SCENE PATH :</b> C:/DOAD_In_House_Everest/Manual/Scenes/</li>
    </ul>

    <a href="initialise" class="btn-back">Back to Initialise</a>
</div>

</body>
</html>