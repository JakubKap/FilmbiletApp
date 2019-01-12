<?php

	require_once '../includes/DbOperation.php';
	$db = new DbOperation();

	$response = array("error" => FALSE, "customerReservations" => array());

	if (isset($_POST['customerId'])) {

		$customerId = $_POST['customerId'];

		$customerReservations = $db->getCustomerReservations($customerId);
	
		
		if ($customerReservations != false) {
		
		
			foreach($customerReservations as $arr){
				$response2 = array(
				"seatNumbers" => $arr['seatNumbers'],
				"reservDate" => $arr['reservDate'],
				"repertId" => $arr['repertId'],
				"repertDate" => $arr['repertDate'],
				"movieTitle" => $arr['movieTitle']
				);
				array_push($response["customerReservations"],$response2);
			}
			
			echo json_encode($response);
			
		} else {
			$response["error"] = TRUE;
			$response["message"] = "Nie ma rezerwacji dla podanego numeru id klienta. Spróbuj ponownie.";
			echo json_encode($response);
		}
	} else {
		$response["error"] = TRUE;
		$response["message"] = "Pole id klienta zostało pominięte!";
		echo json_encode($response);
	}