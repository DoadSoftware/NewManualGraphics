<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Login Page</title>

	<script src="<c:url value='/webjars/jquery/3.7.1/jquery.min.js'/>"></script>
	<script src="<c:url value='/webjars/bootstrap/5.3.3/js/bootstrap.bundle.min.js'/>"></script>
	<script src="<c:url value='/webjars/select2/4.0.13/js/select2.min.js'/>"></script>
	<script src="<c:url value='/resources/javascript/index.js'/>"></script>

	<link rel="stylesheet" href="<c:url value='/webjars/bootstrap/5.3.3/css/bootstrap.min.css'/>"/>
	<link rel="stylesheet" href="<c:url value='/webjars/select2/4.0.13/css/select2.min.css'/>"/>
	<link rel="stylesheet" href="<c:url value='/webjars/font-awesome/6.5.1/css/all.min.css'/>"/>

 <style>
      body {
          font-family: 'Poppins', sans-serif;
          margin: 0;
          padding: 0;
          background: #f3f4f6;
          display: flex;
          justify-content: center;
          align-items: center;
          height: 100vh;
      }

      .container-box {
          display: flex;
          width: 100%;
          background: white;
          border-radius: 8px;
          box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
          overflow: hidden;
          flex-wrap: wrap;
      }

      .left-section {
          flex: 1;
          display: flex;
          flex-direction: column;
          justify-content: center;
          align-items: center;
          background: linear-gradient(135deg, #2563EB, #1E40AF);
          color: white;
          text-align: center;
          padding: 40px;
          background: url('<c:url value="/resources/Images/manual.png" />') no-repeat center center;
          background-size: cover;
      }

      .left-section h2 {
          font-size: 5vw;
          margin-top: 20px;
      }

      .right-section {
          flex: 1;
          padding: 40px;
          display: flex;
          justify-content: center;
          align-items: center;
          text-align: center;
      }

      .container-box_Right {
          background: white;
          padding: 30px;
          border-radius: 8px;
          box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
          width: 100%;
          text-align: center;
          max-width: 480px;
      }

      .form-group {
          text-align: left;
          margin-bottom: 18px;
      }

      .form-control {
          width: 100%;
          padding: 12px;
          border-radius: 6px;
          border: 1px solid #ddd;
          font-size: 16px;
      }

      .btn-submit {
          width: 100%;
          background-color: #2563EB; 
          color: white;
          font-size: 17px;
          font-weight: bold;
          padding: 12px;
          border-radius: 6px;
          border: none;
          cursor: pointer;
          transition: 0.3s;
          margin-top: 10px;
          position: relative;
          overflow: hidden;
      }

      .btn-submit:hover {
          background: #1E40AF; 
      }

      .btn-submit::after {
          content: '\f072'; 
          font-family: 'Font Awesome 5 Free';
          font-weight: 900;
          position: absolute;
          left: -20px;
          top: 50%;
          transform: translateY(-50%);
          opacity: 0;
          transition: left 5s ease-in-out, opacity 0.5s;
      }

      .btn-submit:hover::after {
          left: calc(100% + 10px);
          opacity: 1;
      }

      .footer {
          margin-top: 15px;
          font-size: 14px;
          color: #555;
      }

      .logo {
          max-width: 100%;
          height: auto;
      }

      /* Media Queries for smaller screens */
      @media (max-width: 768px) {
          .container-box {
              flex-direction: column;
              width: 100%;
              padding: 10px;
          }

          .left-section {
              width: 100%;
              padding: 20px;
          }

          .right-section {
              width: 100%;
              padding: 20px;
          }

          .left-section h2 {
              font-size: 6vw;
          }

          .container-box_Right {
              width: 100%;
              padding: 20px;
              max-width: 100%;
          }

          .btn-submit {
              font-size: 16px;
          }

          .footer {
              font-size: 12px;
          }
      }
	 h1, h2, h3, h4, h5, h6,a,label,input[type="text"],input[type="number"],p,select,option {
	    font-size: larger; 
	    font-weight: bold;
	  }
	  h2{
	  		font-size: 28px; 
	  }
      /* Media Queries for medium to large screens (>= 771px) */
      @media (min-width: 771px) {
          .container-box {
              flex-direction: row;
              width: 100%;
              padding: 20px;
          }

          .left-section {
              flex: 1;
              padding: 10px;
          }

          .right-section {
              flex: 1;
              padding: 40px;
          }

          .container-box_Right {
              width: 100%;
              padding: 30px;
              max-width: 480px;
          }

          .btn-submit {
              font-size: 18px;
          }
      }
     /* Media Queries for larger screens (>= 769px) */
      @media (min-width: 769px) {
          .container-box {
              flex-direction: row; 
          }

          .left-section, .right-section {
              width: 50%; 
              padding: 40px;
          }

          /* Heading */
          .left-section h2 {
              font-size: 4vw;
          }

          .btn-submit {
              font-size: 18px;
          }
      }

  </style>
</head>
<body>
<div class="container-box">
    <div class="left-section">
       <!--  <p class="font">Welcome Back</p>
        <p>Nice to see you again!</p> -->
    </div>
    <div class="right-section">
        <div class="container-box_Right">
            <img src="<c:url value='/resources/Images/Idents.jpg'/>" alt="DOAD Logo" class="logo">
            <h2>Initialise Settings</h2>
            <form:form name="initialise_form" autocomplete="off" action="manual" method="POST" enctype="multipart/form-data">
                <div class="form-group">
                    <label for="vizIPAddressEverest">IP Address Everest</label>
                    <input type="text" id="vizIPAddressEverest" name="vizIPAddressEverest" class="form-control" value="${session_Configurations.ipAddressEverest}" required placeholder="Enter Everest IP">
                </div>
                <div class="form-group">
                    <label for="vizIPAddressScenes">IP Address Scenes</label>
                    <input type="text" id="vizIPAddressScenes" name="vizIPAddressScenes" class="form-control" value="${session_Configurations.ipAddressScenes}" required placeholder="Enter Scenes IP">
                </div>
                <div class="form-group">
                    <label for="vizPortNumber">Port Number</label>
                    <input type="number" id="vizPortNumber" name="vizPortNumber" class="form-control" value="1980" required>
                </div>
                <div class="form-group">
                    <label for="select_sports">Select Sport</label>
                    <select id="select_sports" name="select_sports" class="form-control">
                        <option value="DOAD">DOAD</option>
                    </select>
                </div>
                <button type="submit" class="btn-submit">
                    <i class="fas fa-check"></i> SUBMIT
                </button>
            </form:form>
            <div class="footer">
                <p>Need help? <a href="<c:url value='/contact' />" style="color: #2563EB; text-decoration: underline; font-size: 24px;">Contact Support</a></p>
            </div>
        </div>
    </div>
</div>
</body>
</html>
