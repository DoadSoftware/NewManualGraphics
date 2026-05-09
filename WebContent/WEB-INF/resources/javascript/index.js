var previous_data = '';
var ipAddress='';
function processWaitingButtonSpinner(whatToProcess) 
{
	switch (whatToProcess) {
	case 'START_WAIT_TIMER': 
		$('.spinner-border').show();
		$(':button').prop('disabled', true);
		break;
	case 'END_WAIT_TIMER': 
		$('.spinner-border').hide();
		$(':button').prop('disabled', false);
		break;
	}
}


function reloadPage(whichPage)
{
	switch(whichPage){
	/*case 'INITIALISE':
		//processUserSelection(document.getElementById('select_broadcaster'));
		//processManualProcedures('LOAD_MATCHES');
		break;*/
	case 'MANUAL':
		$('#selectedScene').select2();
		$('#previous_xml_data').select2();
		break;
	}
}
function initialisePage(whichPage)
{
	switch(whichPage){
	case 'INITIALISE':
		processUserSelection($('#select_sports'));
		break;
	
	}
}
function processPreviewSelection(whichInput)
{
	if($(whichInput).attr('id').includes('Logo') ||$(whichInput).attr('id').includes('Sponsor')|| $(whichInput).attr('id').includes('Image')){
		uploadFormDataToSessionObjects('file',$(whichInput).attr('id'));
	}else{
		uploadFormDataToSessionObjects('MATCH_PREVIEW',null);
	}
	// Apply a delay of 200 milliseconds before executing processManualProcedures
	processManualProcedures('PREVIEW_IMAGE_DATA');
}
function processUserSelection(whichInput)
{		
	if($(whichInput).attr('id').includes('Logo')||$(whichInput).attr('id').includes('Sponsor') || $(whichInput).attr('id').includes('Image')){
		uploadFormDataToSessionObjects('file',$(whichInput).attr('id'));
	}else{
		switch ($(whichInput).attr('id')) {
		case 'finish_btn':
	      	document.initialise_form.submit();
			break;
		case 'load_scene_btn':
			processManualProcedures('LOAD_SCENE');
			break;
		case 'get_container_btn':
			processManualProcedures('LOAD_DATA');
			break;
		case 'save_button':
			$('#event_stats_div').hide();
			uploadFormDataToSessionObjects('SAVE_DATA',null);
			break;
		case 'load_previous_data_btn':
			processManualProcedures('LOAD_PREVIOUS_SCENE');
			break;
		case 'load_container_btn':
			processManualProcedures('LOAD_CONTAINER');
			break;
		case 'animateout_graphic_btn':
			if(confirm('Are You Sure To Animate Out? ') == true){
				processManualProcedures('ANIMATE-OUT');	
			}
			break;
		case 'animatein_graphic_btn':
			if(confirm('Animate In?') == true){
				processManualProcedures('ANIMATE-IN');
				//addItemsToList('LOAD_PREVIOUS_SCENE-OPTIONS',data);
			}else{
				document.initialise_form.submit();
			}
			break;
		case 'preview_btn':
			processManualProcedures('PREVIEW-IN');
			break;		
		case 'clear_all_btn':
			if(confirm('Are You Sure To Clear All Scenes? ') == true){
				//$('#logging_stats_div').hide();
				$('#logging_stats_div').hide();
				$('#event_stats_div').hide();
				processManualProcedures('CLEAR-ALL');	
			}
			break;
		case 'connection_btn':
			processManualProcedures('BUILD_CONNECTION');
		break;
		case 'cancel_graphics_btn':
			$('#event_stats_div').empty();
			document.getElementById('event_stats_div').style.display = 'none';
			$('#previews').empty();
			document.getElementById('main_div').style.height = '100vh';
			document.getElementById('previews').style.display = 'none';
			break;
		case 'select_sports':
			ipAddress = $('#vizIPAddressEverest').val();
			switch ($('#select_sports :selected').val()) {
				
				
			/*case 'BADMINTON':
				$('#vizPortNumber').attr('value','1980');
				//processManualProcedures('BADMINTON-OPTIONS')
				break;*/
			/*case 'CRICKET':
				processManualProcedures('CRICKET-OPTIONS')
				break;
			  case 'FOOTBALL':
				  processManualProcedures('FOOTBALL-OPTIONS')
				  break;*/
			}
			break;
		}
	}
}	
function userSelectionData(whatToProcess,dataToProcess){
	switch (whatToProcess) {
	case 'LOGGER_FORM_KEYPRESS':
		switch (dataToProcess) {
		case 'CLEAR':
		//case 32:
			processManualProcedures('CLEAR-ALL');
			break;	
		case '-':
			if(confirm('It will Also Delete Your Preview from Directory...\r\n \r\nAre You Sure To Animate Out? ') == true){
				processManualProcedures('ANIMATE-OUT');
			}
			break;
		case '/':
			if(confirm('Animate In?') == true){
				processManualProcedures('ANIMATE-IN');
				//addItemsToList('LOAD_PREVIOUS_SCENE-OPTIONS',data);
			}else{
				document.initialise_form.submit();
			}
			break;
		/*case 'Escape':
			processManualProcedures('CLEAR-ALL');
			break;*/
		}
		break;	
	}
}
function uploadFormDataToSessionObjects(whatToProcess,whichInput)
{
	var idOfImagebutton = whichInput;
	var formData = new FormData();
	var url_path;
	
	switch(whatToProcess.toUpperCase()) {
	case 'SAVE_DATA': case 'SAVE_FILE':
		$('input, select, textarea').each(
			function(index){
				formData.append($(this).attr('id'),document.getElementById($(this).attr('id')).value);
			}
		);
		url_path = 'save_data';
		formData.append('file_name',document.getElementById('file_name').value);
		break;
	case 'MATCH_PREVIEW':
		$('input, select, textarea').each(
			function(index){
				formData.append($(this).attr('id'),document.getElementById($(this).attr('id')).value);
			}
		);
		url_path = 'preview';
		formData.append('file_name',document.getElementById('file_name').value);
		break;
	case 'FILE':
		//alert(document.getElementById(whichInput).files[0])
		url_path = 'uploadFileToManual';
		formData.append(idOfImagebutton,document.getElementById(whichInput).files[0]);
		break;	
	}
	
	$.ajax({    
		headers: {'X-CSRF-TOKEN': $('meta[name="_csrf"]').attr('content')},
        url : url_path,     
        data : formData,
        cache: false,
        contentType: false,
        processData: false,
        type: 'POST',     
        success : function(data) {
				/*document.getElementById('preview_image').href = URL.createObjectURL(new File([data], {type: "image/png"}));
		        document.getElementById('preview_image').innerHTML = 'hi';*/
        },    
        error : function(e) {    
       	 	console.log('Error occured in uploadFormDataToSessionObjects with error description = ' + e);     
        }    
    });
    	
    switch(whatToProcess.toUpperCase()) {
	case 'SAVE_DATA':
		alert("File Successfully Saved");
		document.manual_form.method = 'post';
		document.manual_form.action = 'back_to_manual';
		document.manual_form.submit();
		break;
	case 'SAVE_FILE':
		previous_data = 'preview';
		break;
	case 'FILE':
		uploadFormDataToSessionObjects('MATCH_PREVIEW',null);
		break;				
	}	
	
}
function processManualProcedures(whatToProcess, valueToProcess) {

    // Determine valueToProcess from DOM if not passed in
    if (valueToProcess === undefined || valueToProcess === null) {
        switch (whatToProcess) {
        case 'LOAD_SCENE':
            valueToProcess = $('#selectedScene option:selected').val();
            break;
        case 'LOAD_PREVIOUS_SCENE':
            valueToProcess = $('#previous_xml_data option:selected').val();
            break;
        case 'LOAD_DATA':
            if ($('#selectedScene option:selected').val().includes('_Rows_')) {
                valueToProcess = $('#selectedScene option:selected').val() + "," + $('#rows').val() + "," + $('#column').val();
                $('#RowCol_stats_div').empty();
                $('#RowCol_stats_div').css('display', 'none');
            } else {
                valueToProcess = $('#selectedScene option:selected').val();
            }
            break;
        case 'BADMINTON-OPTIONS':
        case 'CRICKET-OPTIONS':
        case 'FOOTBALL-OPTIONS':
            valueToProcess = $('#select_sports option:selected').val();
            break;
        case 'READ-DATA-AND-PREVIEW':
        case 'LOAD_CONTAINER':
            valueToProcess = $('#previous_xml_data option:selected').val();
            break;
        case 'PREVIEW':
            valueToProcess = $('#selectedScene option:selected').val();
            break;
        case 'READ-MATCH-AND-POPULATE':
            if (previous_data == 'preview') {
                previous_data = '';
                processManualProcedures('PREVIEW');
            }
            return; // no AJAX needed
        default:
            valueToProcess = '';
        }
    }

    valueToProcess = valueToProcess || '';

    $.ajax({
        type: 'POST',
        url: 'processManualProcedures',
        data: {
            whatToProcess: whatToProcess,
            valueToProcess: valueToProcess
        },
        dataType: 'json',
        success: function (data) {
            switch (whatToProcess) {
            case 'LOAD_DATA':
                addItemsToList('LOAD_DATA-OPTIONS', data);
                break;
            case 'LOAD_SCENE':
                if ($('#selectedScene option:selected').val().includes('_Rows_') ||
                    $('#selectedScene option:selected').val().includes('_Row_')) {
                    addItemsToList('ROWS_COLUMN-OPTIONS', data);
                }
                break;
            case 'PREVIEW_IMAGE_DATA':
            case 'PREVIEW-IN':
                addItemsToList('PREVIEW_IMAGE_TO_DIV', data);
                break;
            case 'CHECK_CONNECTION':
                addItemsToList('CONNECTION_TO_DIV', data);
                break;
            case 'LOAD_PREVIOUS_SCENE':
                if (confirm('Animate In?') == true) {
                    processManualProcedures('ANIMATE-IN');
                    addItemsToList('LOAD_PREVIOUS_SCENE-OPTIONS', data);
                } else {
                    document.initialise_form.submit();
                }
                break;
            case 'LOAD_CONTAINER':
                addItemsToList('LOAD_PREVIOUS_SCENE-OPTIONS', data);
                break;
            }
            processWaitingButtonSpinner('END_WAIT_TIMER');
        },
        error: function (xhr, status, error) {
            console.error("Error occured in " + whatToProcess +
                " with error description = " + xhr.status + " " + xhr.statusText);
        }
    });
}
function addItemsToList(whatToProcess, dataToProcess){

	var option,i,label,select,div,col_num_per_row = 0, col_id = 0;
	
  switch (whatToProcess) {
	
	 case "CONNECTION_TO_DIV":
	    $('#CheckConnection_div').empty();
	    div = document.getElementById('CheckConnection_div');
	    div.style.display = 'flex';
	    div.style.alignItems = 'center'; 
	    div.style.justifyContent = 'flex-end'; 
	
	    // Create the circle in div
	    option = document.createElement('div');
	    option.style.width = '20px';
	    option.style.height = '20px';
	    option.style.borderRadius = '50%';
	    option.style.marginRight = '10px';
	    option.style.backgroundColor = dataToProcess.connection_type.toLowerCase()=== "connected"  ? 'green' : 'red';
	    option.style.boxShadow = '0 4px 8px rgba(0, 0, 0, 0.3)'; // 3D shadow
	    	
	    // Create the label for circle
	    label = document.createElement('span');
	    label.innerHTML = dataToProcess.connection_type.toLowerCase()=== "connected" ? '<b>CONNECTED</b>' : '<b>DISCONNECTED</b>';
	    label.style.marginLeft = '10px'; 
	    label.style.marginRight = '10px';
	    label.style.verticalAlign = 'middle';
		label.style.padding = '5px 10px'; 
	    label.style.fontSize = '15px'; 
	    label.style.fontWeight = '1000';
	    label.style.color = dataToProcess.connection_type.toLowerCase()=== "connected" ? 'green' : 'red'; 
	    label.style.textShadow = '2px 2px 3px rgba(0, 0, 0, 0.5)'; // 3D text shadow
	    label.style.textTransform = 'uppercase';
		
	    // Append elements to the div
	    div.appendChild(label);
	    div.appendChild(option);
	break;
	
   case "PREVIEW_IMAGE_TO_DIV":
    if (dataToProcess.file_data) {
    // Convert Base64 to Data URL and set it as the image source
	    document.getElementById('preview_img').src =
	        "data:" + dataToProcess.content_type + ";base64," + dataToProcess.file_data;
	
	    document.getElementById('preview_image_div').style.display = 'block';
	} else {
	    console.error("Error: File does not exist.");
	}

    break;
	case 'ROWS_COLUMN-OPTIONS':
		$('#RowCol_stats_div').empty();
	    table = document.createElement('table');
	    table.setAttribute('class', 'table table-bordered');
	    tbody = document.createElement('tbody');
	    
	    row = tbody.insertRow();
	
	    label = document.createElement('label');
	    label.setAttribute('for', 'rows');
	    label.textContent = 'Rows:';
	    row.insertCell().appendChild(label);
	
	    select = document.createElement('input');
	    select.type = 'number'; 
	    select.id = 'rows';
	    select.value = '1';  
	    select.min = '1'; 
	    select.style.width = '60px';  
	    select.setAttribute('onclick', 'processUserSelection(this);');
	    row.insertCell().appendChild(select);
	   
	
	    label = document.createElement('label');
	    label.setAttribute('for', 'column');
	    label.textContent = 'Columns:';
	    row.insertCell().appendChild(label);
	
	    select = document.createElement('input');
	    select.type = 'number'; 
	    select.id = 'column';
	    select.value = '1';  
	    select.min = '1';  
	    select.style.width = '60px';  
	    select.setAttribute('onclick', 'processUserSelection(this);');
	    row.insertCell().appendChild(select);
	
	    table.appendChild(tbody);
	    
	    div = document.getElementById('RowCol_stats_div');
	    div.appendChild(table);
	    div.style.display = '';
	    
	    break;


	case 'LOAD_DATA-OPTIONS':
	
		if(dataToProcess) {
			//$('#logging_stats_div').empty();
			$('#event_stats_div').empty();
			
			div = document.createElement('div');
			
			table = document.createElement('table');
			table.setAttribute('class', 'table table-bordered');
			
			tbody = document.createElement('tbody');
			
			for(var i = 0; i < dataToProcess.length ; i++) {
				if(i == 0) {
					document.getElementById('scenePath').value = dataToProcess[i];
				} else if(i == 1) {
				}else {
					if(col_num_per_row % 4 == 0) {
						row = tbody.insertRow(tbody.rows.length);
						col_num_per_row = col_num_per_row + 1; // 7
						col_id = 0; 
					} else {
						col_id = col_id + 1; // 2
						col_num_per_row = col_num_per_row + 1; // 6
					}
					if(dataToProcess[i].split(':')[1] == 'TAG_IMAGE1'){
						select = document.createElement('input')
						select.type = 'file';
						select.accept = 'image/*';
						select.id = (i - 1) + '_' + dataToProcess[i].split(':')[0];
						
						select.setAttribute('onchange','processUserSelection(this);');
						select.setAttribute('onblur', 'processPreviewSelection(this);');
						
						label = document.createElement('label');
						label.type = 'label';
						label.style = 'display:block';
						label.innerHTML =(i - 1) + '_' + dataToProcess[i].split(':')[0];
						label.for = select.id;
						div.appendChild(label).appendChild(select);
						select.setAttribute('onchange','processUserSelection(this);');
						select.setAttribute('onblur', 'processPreviewSelection(this);');
					}else{
						select = document.createElement('input')
						select.type = 'text';
						select.id = (i - 1) + '_' + dataToProcess[i].split(':')[0];
						
						label = document.createElement('label');
						label.type = 'label';
						label.style = 'display:block';
						label.innerHTML = (i - 1) + '_' +  dataToProcess[i].split(':')[0] + '<br>';
						label.for = select.id;
						div.appendChild(label).appendChild(select);
						select.setAttribute('onchange','processUserSelection(this);');
						select.setAttribute('onblur', 'processPreviewSelection(this);');

					}
					row.insertCell(col_id).appendChild(label).appendChild(select);
				}
				
			}
			row = tbody.insertRow(tbody.rows.length);
			
			select = document.createElement('input')
			select.type = 'text';
			select.id = 'file_name';
			select.value = (document.getElementById('scenePath').value).split('/')[
				(document.getElementById('scenePath').value).split('/').length - 1].replace('.sum','');
			label = document.createElement('label');
			label.type = 'label';
			label.innerHTML = 'Save XML File As';
			label.for = select.id;
			row.insertCell(0).appendChild(label).appendChild(select);
			
			select = document.createElement('button');
			select.innerHTML = '<b>Save XML</b>';
			select.id = 'save_button';
			select.className = 'btn action-btn save-btn';
			select.style.backgroundColor = 'blue'; // background color
			select.style.color = 'white'; 
	        select.style.padding = '4px 8px'; // Adjust padding
	        select.style.fontSize = window.innerWidth <= 480 ? '1.2rem' : window.innerWidth <= 768 ? '1.2rem' : '1.9rem';
	        select.style.borderRadius = '8px'; // Rounded corners
	        select.style.transition = 'all 0.3s ease'; // Smooth transition
	
	        // Add hover effect 
	        select.addEventListener('mouseover', function() {
	            select.style.boxShadow = '0 8px #666'; // Increase shadow on hover
	            select.style.transform = 'translateY(-4px)'; // Lift button on hover
	        });
	
	        select.addEventListener('mouseout', function() {
	            select.style.boxShadow = '0 4px #666'; // Restore shadow
	            select.style.transform = 'translateY(0)'; // Restore button position
	        });

			select.addEventListener('mouseout', function() {
			    select.style.boxShadow = '0 4px #666'; // Restore shadow
			    select.style.transform = 'translateY(0)'; // Restore button position
			});
			select.setAttribute('onclick','processUserSelection(this);');

			row.insertCell(1).appendChild(select);
			
			var cancelButton = document.createElement('button');
			cancelButton.innerHTML = '<b>Cancel</b>';
			cancelButton.id = 'cancel_graphics_btn';
		 	cancelButton.className = 'btn action-btn cancel-btn';
			cancelButton.style.color = 'white'; 
			cancelButton.style.backgroundColor = 'red'; // color
	        cancelButton.style.padding = '4px 8px'; // Adjust padding
	        cancelButton.style.fontSize =  window.innerWidth <= 480 ? '1.2rem' : window.innerWidth <= 768 ? '1.2rem' : '1.9rem';
	        cancelButton.style.borderRadius = '8px'; // Rounded corners
	        cancelButton.style.transition = 'all 0.3s ease'; // Smooth transition
	
	        // Add hover effect 
	        cancelButton.addEventListener('mouseover', function() {
	            cancelButton.style.boxShadow = '0 8px #666'; // Increase shadow on hover
	            cancelButton.style.transform = 'translateY(-4px)'; // Lift button on hover
	        });
	
	        cancelButton.addEventListener('mouseout', function() {
	            cancelButton.style.boxShadow = '0 4px #666'; // Restore shadow
	            cancelButton.style.transform = 'translateY(0)'; // Restore button position
	        });
			cancelButton.setAttribute('onclick', 'processUserSelection(this);');			
			row.insertCell(2).appendChild(cancelButton);
			
			table.appendChild(tbody);
			
			document.getElementById('event_stats_div').appendChild(table);
			document.getElementById('main_div').style.height = 'auto';
			document.getElementById('event_stats_div').style.display = 'block';
		}
		break;
		
		case 'LOAD_PREVIOUS_SCENE-OPTIONS':
		
				if(dataToProcess) {
				$('#event_stats_div').empty();
	
				div = document.createElement('div');
				
				table = document.createElement('table');
				table.setAttribute('class', 'table table-responsive');
				
				tbody = document.createElement('tbody');
				
				dataToProcess.containers.forEach(function(cont){
					if(cont.container_key == 'scenePath'){
					}else{
						if(col_num_per_row % 4 == 0) {
							row = tbody.insertRow(tbody.rows.length);
							col_num_per_row = col_num_per_row + 1; // 7
							col_id = 0; 
						} else {
							col_id = col_id + 1; // 2
							col_num_per_row = col_num_per_row + 1; // 6
						}
						if(cont.container_key.includes('Logo')||cont.container_key.includes('Sponsor') || cont.container_key.includes('Image')){
							select = document.createElement('input')
							select.type = 'file';
							select.accept = 'image/*';
							select.id = cont.container_key;
							
							label = document.createElement('label');
							label.type = 'label';
							label.style = 'display:block';
							if(cont.container_value == ''){
								label.innerHTML = cont.container_key;
							}else{
								label.innerHTML = cont.container_key + " (" + cont.container_value.split("Media/")[1].replace("DOAD_","") + ")";
							}
							label.for = select.id;
							row.insertCell(col_id).appendChild(label).appendChild(select);
							select.setAttribute('onchange','processUserSelection(this);');
							select.setAttribute('onblur','processPreviewSelection(this);');
						}else{
							select = document.createElement('input');
							select.type = 'text';
							select.id = cont.container_key;
							select.value = cont.container_value;
							
							label = document.createElement('label');
							label.type = 'label';
							label.style = 'display:block';
							label.innerHTML = cont.container_key + '<br>';
							label.for = select.id;
							row.insertCell(col_id).appendChild(label).appendChild(select);
							select.setAttribute('onchange','processUserSelection(this);');
							select.setAttribute('onblur','processPreviewSelection(this);');
	
						}
					}
					
				});
				row = tbody.insertRow(tbody.rows.length);
				
				select = document.createElement('input')
				select.type = 'text';
				select.id = 'file_name';
				//select.value = '';
				select.value = ($('#previous_xml_data option:selected').val()).replace('.xml','');
				label = document.createElement('label');
				label.type = 'label';
				label.innerHTML = 'Save XML Files As';
				label.for = select.id;
				row.insertCell(0).appendChild(label).appendChild(select);
				
				select = document.createElement('button');
				select.innerHTML = 'Save As';
				select.id = 'save_button';
				select.className = 'btn action-btn save-btn';
		        select.style.padding = '4px 8px'; // Adjust padding
				select.style.backgroundColor = 'blue'; // color
				select.style.color = 'white'; 
		        select.style.fontSize = '16px'; // Font size
		        select.style.borderRadius = '8px'; // Rounded corners
		        select.style.transition = 'all 0.3s ease'; // Smooth transition
		
		        // Add hover effect 
		        select.addEventListener('mouseover', function() {
		            select.style.boxShadow = '0 8px #666'; // Increase shadow on hover
		            select.style.transform = 'translateY(-4px)'; // Lift button on hover
		        });
		
		        select.addEventListener('mouseout', function() {
		            select.style.boxShadow = '0 4px #666'; // Restore shadow
		            select.style.transform = 'translateY(0)'; // Restore button position
		        });
	
				select.addEventListener('mouseout', function() {
				    select.style.boxShadow = '0 4px #666'; // Restore shadow
				    select.style.transform = 'translateY(0)'; // Restore button position
				});
				select.setAttribute('onclick','processUserSelection(this);');
				row.insertCell(1).appendChild(select);
	
	
				var cancelButton = document.createElement('button');
				cancelButton.innerHTML = '<b>Cancel</b>';
				cancelButton.id = 'cancel_graphics_btn';
			 	cancelButton.className = 'btn action-btn cancel-btn';
		        cancelButton.style.padding = '4px 8px'; // Adjust padding
				cancelButton.style.backgroundColor = 'red'; // color
				cancelButton.style.color = 'white'; 
		        cancelButton.style.fontSize = '16px'; // Font size
		        cancelButton.style.borderRadius = '8px'; // Rounded corners
		        cancelButton.style.transition = 'all 0.3s ease'; // Smooth transition
		
		        // Add hover effect using JavaScript
		        cancelButton.addEventListener('mouseover', function() {
		            cancelButton.style.boxShadow = '0 8px #666'; // Increase shadow on hover
		            cancelButton.style.transform = 'translateY(-4px)'; // Lift button on hover
		        });
		
		        cancelButton.addEventListener('mouseout', function() {
		            cancelButton.style.boxShadow = '0 4px #666'; // Restore shadow
		            cancelButton.style.transform = 'translateY(0)'; // Restore button position
		        });
				cancelButton.setAttribute('onclick', 'processUserSelection(this);');			
				row.insertCell(2).appendChild(cancelButton);
				table.appendChild(tbody);
				
				document.getElementById('event_stats_div').appendChild(table);
				//document.getElementById('event_stats_div').style.display = '';
				document.getElementById('main_div').style.height = 'auto';
				document.getElementById('event_stats_div').style.display = 'block';
			}
			
			break;
		
	}
}


function checkEmpty(inputBox,textToShow) {

	var name = $(inputBox).attr('id');
	
	document.getElementById(name + '-validation').innerHTML = '';
	document.getElementById(name + '-validation').style.display = 'none';
	$(inputBox).css('border','');
	if(document.getElementById(name).value.trim() == '') {
		$(inputBox).css('border','#E11E26 2px solid');
		document.getElementById(name + '-validation').innerHTML = textToShow + ' required';
		document.getElementById(name + '-validation').style.display = '';
		document.getElementById(name).focus({preventScroll:false});
		return false;
	}
	return true;	
}