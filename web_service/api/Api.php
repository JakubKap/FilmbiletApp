<?php 
	header('Content-Type: application/json; charset=utf-8');

	require_once '../includes/DbOperation.php';
	$response = array();
	
	//api call
	if(isset($_GET['q'])){
		$db = new DbOperation();
		switch($_GET['q']){
			case 'getCustomers':
				$response = getSuccessfulResponse('customers', $db->getCustomers());
			break; 
			
			case 'getMovies':
				$response = getSuccessfulResponse('movies', $db->getMovies());
			break;
			
			case 'getSeatTypes':
				$response = getSuccessfulResponse('seatTypes', $db->getSeatTypes());
			break;
			
			case 'getGenres':
				$response = getSuccessfulResponse('genres', $db->getGenres());
			break;
			
			case 'getReservations':
				$response = getSuccessfulResponse('reservations', $db->getReservations());
			break;
			
		}
		
	}else{
		$response['error'] = true; 
		$response['message'] = 'Invalid API Call';
	}
	
	function getSuccessfulResponse($responseName, $getFunction) {
        $response['error'] = false;
        $response['message'] = 'Request successfully completed';
        $response[$responseName] = $getFunction;
        return $response;
    }

	echo json_encode($response);
	
	
