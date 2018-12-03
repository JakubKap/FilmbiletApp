<?php

	require_once '../includes/DbOperation.php';
	$db = new DbOperation();

	$response = array("error" => FALSE, "reservation" => array());

	if (isset($_POST['repertoireId'])) {

		$repertoireId = $_POST['repertoireId'];

		$reservation = $db->getReservationsFromRepertoire($repertoireId);
	
		
		if ($reservation != false) {
		
		
			foreach($reservation as $arr){
				$response2 = array(
				"customerId" => $arr['customerId'],
				"movieId" => $arr['movieId'],
				"hall" => $arr['hall'],
				"seatNumber" => $arr['seatNumber'],
				"row" => $arr['row'],
				"date" => $arr['date'],
				"seatTypeId" => $arr['seatTypeId']
				);
				array_push($response["reservation"],$response2);
			}
			
			echo json_encode($response);
			
		} else {
			$response["error"] = TRUE;
			$response["message"] = "Nie ma rezerwacji dla podanego repertuaru. Spróbuj ponownie.";
			echo json_encode($response);
		}
	} else {
		$response["error"] = TRUE;
		$response["message"] = "Pole repertoireId zostało pominięte!";
		echo json_encode($response);
	}