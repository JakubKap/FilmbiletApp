<?php

	require_once '../includes/DbOperation.php';
	$db = new DbOperation();

	$response = array("error" => FALSE);

	if (isset($_POST['repertoireId'])) {

		$repertoireId = $_POST['repertoireId'];

		$reservation = $db->getReservationsFromRepertoire($repertoireId);
	
		$reservations=array();
		if ($reservation != false) {

			foreach($reservation as $arr){
			$response2 = array(); 
			$response2["reservation"]["customerId"] = $arr['customerId'];
			$response2["reservation"]["movieId"] = $arr['movieId']; 
			$response2["reservation"]["hall"] = $arr['hall'];
			$response2["reservation"]["seatNumber"] = $arr['seatNumber'];
			$response2["reservation"]["row"] = $arr['row'];
			$response2["reservation"]["date"] = $arr['date'];
			$response2["reservation"]["seatTypeId"] = $arr['seatTypeId'];
			array_push($response,$response2);
			}
			echo json_encode($response,JSON_FORCE_OBJECT);
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