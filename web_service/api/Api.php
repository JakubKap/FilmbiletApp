<?php 

	require_once '../includes/DbOperation.php';
	$response = array();
	
	//api call
	if(isset($_GET['q'])){
		
		switch($_GET['q']){
			case 'getCustomers':
				$db = new DbOperation();
				$response['error'] = false; 
				$response['message'] = 'Request successfully completed';
				$response['customers'] = $db->getCustomers();
			break; 
			
			case 'getMovies':
				$db = new DbOperation();
				$response['error'] = false; 
				$response['message'] = 'Request successfully completed';
				$response['movies'] = $db->getMovies();
			break;
			
			case 'getSeatTypes':
				$db = new DbOperation();
				$response['error'] = false; 
				$response['message'] = 'Request successfully completed';
				$response['seatTypes'] = $db->getSeatTypes();
			break;
		}
		
	}else{
		$response['error'] = true; 
		$response['message'] = 'Invalid API Call';
	}

	echo json_encode($response);
	
	
