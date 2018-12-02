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
			$response = array(); 
			$response["error"] = FALSE;
			$response["reservation"]["customerId"] = $arr['customerId'];
			$response["reservation"]["movieId"] = $arr['movieId']; 
			$response["reservation"]["hall"] = $arr['hall'];
			$response["reservation"]["seatNumber"] = $arr['seatNumber'];
			$response["reservation"]["row"] = $arr['row'];
			$response["reservation"]["date"] = $arr['date'];
			$response["reservation"]["seatTypeId"] = $arr['seatTypeId'];
			array_push($reservations,$response);
			}
			echo json_encode($reservations);
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