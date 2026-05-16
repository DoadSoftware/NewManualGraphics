<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1">
  <title>Manual</title>

	<script src="<c:url value='/webjars/jquery/3.7.1/jquery.min.js'/>"></script>
	<script src="<c:url value='/webjars/bootstrap/5.3.3/js/bootstrap.bundle.min.js'/>"></script>
	<script src="<c:url value='/webjars/select2/4.0.13/js/select2.min.js'/>"></script>
	<script src="<c:url value='/resources/javascript/index.js'/>"></script>

	<link rel="stylesheet" href="<c:url value='/webjars/bootstrap/5.3.3/css/bootstrap.min.css'/>"/>
	<link rel="stylesheet" href="<c:url value='/webjars/select2/4.0.13/css/select2.min.css'/>"/>
	<link rel="stylesheet" href="<c:url value='/webjars/font-awesome/6.5.1/css/all.min.css'/>"/>
      
  <script type="text/javascript">
    $(document).on("keydown", function(e){
      if($('#waiting_modal').hasClass('show')) {
        e.cancelBubble = true;
        e.stopImmediatePropagation();
        e.preventDefault();
        return false;
      }
      var evtobj = window.event? event : e;
      switch(e.target.tagName.toLowerCase()) {
        case "input": 
        case "textarea":
          // Possibly do something with form fields...
          break;
        default:
          e.preventDefault();
          var whichKey = '';
          var validKeyFound = false;
          if(evtobj.ctrlKey) {
            whichKey = 'Control';
          }
          if(evtobj.altKey) {
            if(whichKey) {
              whichKey = whichKey + '_Alt';
            } else {
              whichKey = 'Alt';
            }
          }
          if(evtobj.shiftKey) {
            if(whichKey) {
              whichKey = whichKey + '_Shift';
            } else {
              whichKey = 'Shift';
            }
          }
          if(evtobj.keyCode) {
            if(whichKey) {
              if(!whichKey.includes(evtobj.key)) {
                whichKey = whichKey + '_' + evtobj.key;
              }
            } else {
              whichKey = evtobj.key;
            }
          }
          validKeyFound = false;
          if(whichKey.includes('_')) {
            whichKey.split("_").forEach(function (this_key) {
              switch (this_key) {
                case 'Control': case 'Shift': case 'Alt':
                  break;
                default:
                  validKeyFound = true;
                  break;
              }
            });
          } else {
            if(whichKey != 'Control' && whichKey != 'Alt' && whichKey != 'Shift') {
              validKeyFound = true;
            }
          }
          if(validKeyFound == true) {
            console.log('whichKey = ' + whichKey);
            userSelectionData('LOGGER_FORM_KEYPRESS', whichKey);
          }
      }
    });
    $(document).ready(function() {
        // select2 fires 'select2:select' not native 'change'
        $('#previous_xml_data').on('select2:select', function() {
            processManualProcedures('READ-DATA-AND-PREVIEW', $(this).val());
        });

        $('#selectedScene').on('select2:select', function() {
            processManualProcedures('LOAD_SCENE', $(this).val());
        });

        setInterval(() => {
            processManualProcedures('CHECK_CONNECTION', '');
        }, 5000);

    });
  </script>
  <!-- Additional Responsive & Modern CSS -->
  <style type="text/css">
   body {
	  font-size: 14px;
	  font-family: "Segoe UI", Arial, sans-serif;
	  color: #1F2937;
	}
	
	h1, h2, h3, h4, h5, h6 {
	  margin: 0;
	}
	
	button,
	input,
	select,
	textarea {
	  font-size: 14px !important;
	}
	
	.form-group label,
	.configuration-row label {
	  font-size: 14px;
	  font-weight: 600;
	  color: #374151;
	}
	
	.panel-title {
	  font-size: 18px !important;
	  font-weight: 700;
	}
    /* Subtle gradient background for entire page */
    body {
    padding: 0 !important;
  	margin: 0 !important;
    color: #2E008B;
    font-family: Arial, sans-serif;
    background: url('<c:url value='/resources/Images/img_2.jpg'/>') no-repeat center center fixed;
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

    /* Container for the main content */
   #main_div.content.py-1 {
	  width: 100%;
	  min-height: 100vh;
	  overflow-x: hidden;
	  overflow-y: auto;
	  padding: 12px;
	}
    /* The outer .col-md-13 offset-md-0 container */
   .col-md-13.offset-md-0 {
	  width: 100%;
	  margin: 0;
	  padding: 0;
	}
    /* Card styling improvements */
   .card.card-outline-secondary {
	  background: rgba(255,255,255,0.96);
	  border-radius: 14px;
	  border: 1px solid rgba(255,255,255,0.4);
	  box-shadow:0 10px 30px rgba(0,0,0,0.12), 0 2px 8px rgba(0,0,0,0.08);
	  backdrop-filter: blur(8px);
	  overflow: hidden;
	}
    .card-header {
    	background: linear-gradient( 90deg, #0F172A, #1E293B);
	  padding: 14px 20px;
	  border-bottom: none;
      border-radius: 8px 8px 0 0;
      
    }
    .card-body {
      padding: 15px;
    }
    /* The 'Configuration' panel heading */
    .panel-heading {
      background: none;
      border: none;
    }
    .panel-title {
	  font-size: 18px !important;
	  font-weight: 700;
	  text-shadow: none;
	}
	
	.panel-title a {
	  color: black !important;
	  text-decoration: none;
	}

    /* Buttons with a slight box-shadow & hover effect */
    .btn-sm {
      padding: 8px 10px;
      border-radius: 5px;
      transition: background-color 0.3s ease, box-shadow 0.3s ease;
    }
    .btn-sm:hover {
      filter: brightness(0.9);
      box-shadow: 0 2px 4px rgba(0,0,0,0.15);
    }
    /* Previews section styling */
    #previews {
      display: flex;
      align-items: flex-start;
      justify-content: space-between;
      margin-left: 5%;
      width: 90%;
      margin-top: 20px;
      margin-bottom: 40px;
    }
    #event_stats_div, #preview_image_div {
      background-color: #fff;
      border-radius: 8px;
      box-shadow: 10px 5px 50px #9AA2A2;
      padding: 20px 8px;
      margin-right: 20px;
    }
    #event_stats_div {
	    display: block; /* Ensure it's visible */
	    overflow-y: auto; /* Enable vertical scrolling */
	    max-height: 65vh; /* Set a maximum height for scrolling */
	}
    
    /* Media query for smaller screens */
    @media (max-width: 768px) {
      /* Stack the Scenes/XML and Buttons vertically */
      .panel-body > div[style*="display: flex;"] {
        flex-direction: column !important;
        gap: 20px !important;
      }
      /* Previews also stack vertically */
      #previews {
        flex-direction: column;
        align-items: stretch;
      }
      #event_stats_div, #preview_image_div {
        margin-right: 0;
        margin-bottom: 20px;
        width: 100% !important;
      }
      /* Adjust .col-md-13 container for smaller screens if needed */
      .col-md-13.offset-md-0 {
        margin: 10px;
        border-radius: 10px;
      }
    }
    /* Make table cells wrap text and keep the table 100% width */
	.table {
	  width: 100%;
	  table-layout: fixed; /* Ensures columns share remaining width evenly */
	  border-collapse: collapse;
	}
	
	/* Let table cells wrap text instead of overflowing */
	.table th,
	.table td {
	  white-space: normal;        /* Allows line breaks */
	  word-wrap: break-word;      /* Breaks long words */
	  overflow-wrap: break-word;  /* CSS3 standard property */
	  vertical-align: top;        /* So labels & inputs align nicely at the top */
	  padding: 8px;
	  border: 1px solid #ddd;     /* Example border; remove if undesired */
	}
	
	/* Make inputs fill the entire table cell width */
	.table td input[type="text"],
	.table td input[type="file"] {
	  width: 100%;
	  box-sizing: border-box; /* Ensures padding/border are included in total width */
	}
	
	/* Ensure labels also wrap properly */
	.table td label {
	  display: block;
	  white-space: normal;
	  word-wrap: break-word;
	  overflow-wrap: break-word;
	}
	html, body {
	    overflow-x: hidden; /* Prevent horizontal scrolling */
	    overflow-y: auto; /* vertical scrolling on larger screens */
	    margin: 0;
	    padding: 0;
	}
    /* Responsive table container */
	.table-responsive {
	  width: 100%;
	  overflow-x: auto;         /* Enables horizontal scrolling if needed */
	  -webkit-overflow-scrolling: touch; /* Smooth scrolling on mobile devices */
	  border: 1px solid #ddd;   /* Optional border */
	  border-radius: 4px;       /* Rounded corners for the container */
	  margin-bottom: 20px;      /* Spacing at the bottom */
	}
	
	/* Make sure the table inside fills the container */
	.table-responsive table {
	  width: 100%;
	  max-width: 100%;
	  border-collapse: collapse;
	}
	#event_stats_div {
	  display: none;
	  background-color: white;
	  border-radius: 2px;
	  box-shadow: 10px 5px 50px #9AA2A2;
	  padding: 20px 8px;
	  width: 50%;
	  max-width: 100%;
	  height: 600px;        /* or 'auto' + a max-height, if you prefer */
	  margin-right: 20px;
	  overflow-x: hidden;   /* Hide horizontal scrolling */
	  overflow-y: auto;     /* Allow vertical scrolling only */
	}
	@media (min-width: 1024px) {
	  .col-md-13.offset-md-0 {
	    width: 100%; /* Adjust this value as needed */
	    margin: 0 auto; /* Center the container */
	  }
	}
	/* Container row that splits into left and right columns */
	.configuration-row {
	  display: flex;
	  align-items: flex-start;
	  justify-content: space-between;
	  gap: 1rem; /* spacing between left and right sections */
	  margin-bottom: 1rem;
	}
	
	/* Left and right columns share the space equally on large screens */
	.left-col {
	  flex: 0 0 38%;
	}
	
	.right-col {
	  flex: 0 0 60%;
	}
	
	/* Stack row-items vertically in the left column */
	.left-col {
	  display: flex;
	  flex-direction: column;
	  gap: 1rem;
	}
	
	/* Each label + select pair is one row-item */
	.row-item {
	  display: flex;
	  align-items: center;
	  gap: 12px;
	  width: 100%;
	}
	
	/* Right column: buttons, allow wrapping if needed */
	.right-col {
	  display: flex;
	  flex-wrap: wrap; /* buttons go to next line if there's no space */
	  gap: 0.625rem;
	  justify-content: flex-end;
	}
	
	/* Basic styling for labels */
	.configuration-row label {
	  font-weight: bold;
	  color: black;
	  white-space: nowrap; /* keep label on one line */
	}
	
	/* Basic styling for selects */
	.configuration-row select {
	  /* Let them expand on wide screens; override with media queries below if desired */
	  width: 100% !important;
	  max-width: 100% !important; /* 240px => 15rem, adjust as needed */
	  height: 42px;
	  border-radius: 10px;
	  border: 1px solid #D1D5DB;
	  background: #FFFFFF;
	  padding: 0 12px;
	  font-size: 14px !important;
	  transition: all 0.2s ease;
	}
	
	.configuration-row select:focus {
	  border-color: #2563EB;
	  box-shadow: 0 0 0 3px rgba(37,99,235,0.15);
	  outline: none;
	}
	
	/* Purple Buttons */
	.purple-btn {
	  background: linear-gradient(135deg, #2563EB, #1D4ED8);
	  border: none;
	  color: white;
	  border-radius: 10px;
	  padding: 10px 16px;
	  font-size: 13px !important;
	  font-weight: 600;
	  min-height: 42px;
	  text-shadow: none;
	  box-shadow: 0 4px 12px rgba(37,99,235,0.25);
	}
	.purple-btn:hover {
	  background-color: #1D0066; /* A darker shade of purple */
  	  color: #fff;  
	  box-shadow: 0 0.125rem 0.25rem rgba(0,0,0,0.15);
	}
	
	/* Red Buttons */
	.red-btn {
	  background: linear-gradient(135deg, #EF4444, #DC2626);
	  border: none;
	  color: white;
	  border-radius: 10px;
	  padding: 10px 16px;
	  font-size: 13px !important;
	  font-weight: 600;
	  min-height: 42px;
	  text-shadow: none;
	  box-shadow: 0 4px 12px rgba(239,68,68,0.25);
	}
	.red-btn:hover {
	  color: #fff; 
	  box-shadow: 0 0.125rem 0.25rem rgba(0,0,0,0.15);
	}
	.form-group.row {
	  margin-bottom: 5px !important;
	}
	
	/* For screens with width <= 769px (Mobile and Small Tablet) */
	@media (max-width: 769px) {
	
	    /* Ensure proper body font size for readability on mobile */
	    body {
	        font-size: 1.4rem; /* Adjust base font size */
	    }
	
	    /* .configuration-row adjustments */
	    .configuration-row {
	        display: flex;
	        flex-direction: column; /* Stack items vertically */
	        gap: 1rem; /* Space between items */
	    }
	
	    /* Left and right columns in configuration-row */
	    .left-col, .right-col {
	        width: 100%; /* Ensure full width */
	        min-width: 100%; /* Prevent shrinking */
	        margin-bottom: 1rem; /* Add space between the columns */
	        padding: 0; /* Remove unnecessary padding */
	    }
		.right-col {
		    justify-content: flex-start;
		    gap: 0.5rem;
		  }
	    /* Stack .row-item elements inside .left-col and .right-col */
	    .row-item {
	        display: flex;
	        flex-direction: column; /* Stack label and select vertically */
	        gap: 0.5rem; /* Adjust gap */
    		margin-bottom: 0.5rem;
	        align-items: flex-start; /* Align items to the start */
	    }
	
	    /* For the select elements, ensure they take full width */
	    .configuration-row select {
	        width: 100% !important; /* Ensure selects take up full width */
	        padding: 0.8rem; /* Add some padding for better readability */
	        font-size: 1.4rem; /* Adjust font size for mobile */
	    }
	
	    /* Ensure labels inside .configuration-row also fit */
	    .configuration-row label {
	        font-size: 1.4rem; /* Adjust label font size for mobile */
	        width: 100%; /* Ensure labels take full width */
	        margin-bottom: 0.5rem; /* Add spacing between label and select */
	    }
	
	    /* Adjust buttons inside .right-col */
	   .right-col {
		  display: flex;
		  flex-wrap: wrap;
		  gap: 10px;
		  justify-content: flex-start;
		  align-items: center;
		}
	
	    /* .btn-sm adjustments */
	    .btn-sm {
	        font-size: 1rem; /* Adjust font size for buttons */
	        padding: 10px 10px; /* Increase padding for better touch targets */
	    }
	
	    /* Adjust .purple-btn and .red-btn for better visibility and touch interaction */
	    .purple-btn, .red-btn {
	        font-size: 1rem; /* Adjust font size */
	        padding: 8px 12px; /* Increase button padding */
	    }
		h1, h2, h3, h4, h5, h6,a {
	    font-size: medium; /* This remains relative to the base font-size */
	   }
	    /* Adjust card layout for mobile */
	    .col-md-13.offset-md-0 {
	        width: 100%;
	        margin: 0 auto;
	        padding: 0 10px; /* Add padding for better spacing */
	        border-radius: 12px;
	    }
	
	    /* Previews section should stack vertically on mobile */
	    #previews {
	        flex-direction: column; /* Stack previews vertically */
	        width: 100%;
	        gap: 1rem; /* Add gap between preview items */
	    }
	
	    /* Event stats and preview section visibility adjustments */
	    #event_stats_div, #preview_image_div {
	        width: 100%; /* Ensure these sections take up full width */
	        margin-bottom: 1rem; /* Add spacing at the bottom */
	        padding: 1rem;
	    }
	
	    /* Adjust the card header and body padding */
	    .card-header {
	        padding: 12px; /* Adjust padding for better mobile fit */
	    }
	
	    .card-body {
	        padding: 15px; /* Reduce padding for better fit on mobile */
	    }
	
	    /* Ensure tables adjust properly on mobile */
	    .table-responsive {
	        width: 100%;
	        overflow-x: auto; /* Enable horizontal scrolling if needed */
	    }
	
	    /* Table cell adjustments */
	    .table th, .table td {
	        font-size: 1.4rem; /* Adjust font size for readability */
	        padding: 12px; /* Add more padding for touch devices */
	    }
	
	    /* Make sure all input fields are fully visible */
	    .table td input[type="text"],
	    .table td input[type="file"],
	    .table td select {
	        width: 100%; /* Full width for input fields */
	        padding: 0.8rem; /* Add padding for touch interaction */
	        font-size: 1.4rem; /* Adjust font size */
	    }
	
	    /* Ensure select dropdowns are fully visible */
	    .table td select {
	        font-size: 1.4rem; /* Adjust select dropdown font size */
	    }
	
	    /* Adjust the visibility of elements in the configuration row */
	    .configuration-row select {
	        display: block; /* Ensure select is displayed */
	    }
	
	    /* Ensure scene and file select dropdowns are visible */
	    .configuration-row .row-item select {
	        display: block; /* Make sure select elements are shown */
	        margin-top: 0.5rem; /* Adjust spacing */
	    }
	
	    /* Ensure panel title is visible and properly sized */
	    .panel-title {
	        font-size: 2rem; /* Adjust font size for readability */
	        text-shadow: none; /* Remove text-shadow */
	    }
	
	    /* Adjust event stats section for visibility */
	    #event_stats_div {
	        display: block;
	        overflow-y: auto; /* Enable vertical scrolling */
	        max-height: 60vh; /* Limit height */
	        height: auto;
	        margin-bottom: 1rem; /* Add space between sections */
	    }
	}
	table td button {
        font-size: 1rem; 
        border-radius: 5px; 
        cursor: pointer; 
        transition: background-color 0.3s ease, box-shadow 0.3s ease;  /* Smooth transition effect */
    }

    /* On hover, change button background color and add shadow */
    table td button:hover {
        background-color: #1D0066; 
        color:white;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.15); 
    }

    table td button:focus {
        outline: none; 
        box-shadow: 0 0 0 2px rgba(46, 0, 139, 0.5); 
    }

  </style>
</head>
<body onload="reloadPage('MANUAL');">
<form:form name="manual_form" autocomplete="off" action="manual" method="POST" 
	modelAttribute="session_Data" enctype="multipart/form-data">
<div id="main_div" class="content py-1" style="width: 100vw; height: 100vh;">
<div class="container-fluid px-3 py-3" style="width:100%;">
	<div class="row">
	 <div class="col-md-13 offset-md-0">
       <span class="anchor"></span>
         <div class="card card-outline-secondary">
           <div class="card-header">
			 <div class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
	         </div> 
           </div>
          <div class="card-body">
          <div id="CheckConnection_div"></div> 
          	<div id="logging_stats_div" style="display:none;">
			</div>
			  <div class="panel-group" id="match_configuration">
			    <div class="panel panel-default">
			      <div class="panel-heading d-flex align-items-center justify-content-between">
					    <h2 class="panel-title m-0">
					        <a data-bs-toggle="collapse" data-bs-parent="#match_configuration" href="#load_setup_match"
					           style="color:#FFFFFF; text-decoration:none; font-size:18px; font-weight:600; letter-spacing:0.5px;">
					           <i class="fas fa-sliders-h me-2"></i>
					           Configuration
					        </a>
					    </h2>
					
					</div>
			      <div id="load_setup_match" class="panel-collapse collapse">
					<div class="panel-body">
						<!-- Configuration Row -->
							<div class="configuration-row"style="width: 100%">
							  <!-- Left Section -->
							  <div class="left-col">
							    <div class="row-item">
							      <label for="select_cricket_scenes">Select Scenes</label>
							      <select id="selectedScene" name="selectedScene" class="browser-default custom-select custom-select-sm"style="width: 90%;">
							        <option value="BLANK">SELECT SCENE</option>
							        <c:forEach items="${session_viz_scenes}" var="scenes">
							          <option value="${scenes.name}">${scenes.name}</option>
							        </c:forEach>
							      </select>
							    </div>
							    <div class="row-item">
							      <label for="previous_xml_data">Select XML</label>
							      <select id="previous_xml_data" name="previous_xml_data" class="browser-default custom-select custom-select-sm" style="width: 90%;">
							        <option value="BLANK">SELECT FILE</option>
							        <c:forEach items="${scene_files}" var="files">
							          <option value="${files.name}">${files.name}</option>
							        </c:forEach>
							      </select>
							    </div>
							  </div>
							
							  <!-- Right Section -->
							  <div class="right-col">
							    <button class="btn btn-sm purple-btn" type="button" name="get_container_btn" id="get_container_btn" onclick="processUserSelection(this)">
							      <i class="fas fa-film"></i> Get Container
							    </button>
							    <button class="btn btn-sm purple-btn" type="button" name="load_container_btn" id="load_container_btn" onclick="processUserSelection(this)">
							      <i class="fas fa-film"></i> Load XML
							    </button>
							    <button class="btn btn-sm purple-btn" type="button" name="animatein_graphic_btn" id="animatein_graphic_btn" onclick="processUserSelection(this)">
							      AnimateIn
							    </button>
							    <button class="btn btn-sm red-btn" type="button" name="animateout_graphic_btn" id="animateout_graphic_btn" onclick="processUserSelection(this)">
							      AnimateOut
							    </button>
							    <button class="btn btn-sm red-btn" type="button" name="clear_all_btn" id="clear_all_btn" onclick="processUserSelection(this)">
							      Clear All
							    </button>
							    <button class="btn btn-sm red-btn" type="button" name="connection_btn" id="connection_btn" onclick="processUserSelection(this)">
							     RE CONNECT
							    </button>
							    <button class="btn btn-sm red-btn" type="button" name="preview_btn" id="preview_btn" onclick="processManualProcedures('PREVIEW_IMAGE_DATA')">
							     PREVIEW
							    </button>
							  </div>
							</div>
						 <div class="form-group row row-bottom-margin ml-2">
						   <div id="RowCol_stats_div" style="display:none;"></div> 
			             </div> 		
				    </div>
			      </div>
			    </div>
			  </div> 
		    <div class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
           </div>
          </div>
         </div>
       </div>
    </div>
 </div>
 <div id="previews" style="display: flex; align-items: flex-start; justify-content: space-between; margin-left: 5%; width: 90%;">
    <div id="event_stats_div" style="display:none; background-color: white; border-radius: 2px; box-shadow: 10px 5px 50px #9AA2A2;
         padding: 28px 8px; width: 50%; height: 50%; margin-right: 20px;">
    </div>
    <div id="preview_image_div" style="display:none; background-color: white; border-radius: 2px; box-shadow: 10px 5px 50px #9AA2A2; 
        padding: 8px; width: 50%; height: 65vh;">
        <img id="preview_img" style="width: 100%; height: 100%;" alt="Preview Image">
    </div>
</div>
</div> 
 <input type="hidden" name="select_sports" id="select_sports" value="${session_selected_sports}"/>
 <input type="hidden" id="manual_file_timestamp" name="manual_file_timestamp" value="${session_Data.manual_file_timestamp}"/>
 <input type="hidden" name="scenePath" id="scenePath" value=""/>
</form:form>
</body>
</html>
