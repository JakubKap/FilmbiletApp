<?php

	require_once '../includes/DbOperation.php';
	$db = new DbOperation();

	$response = array("error" => FALSE);

	if (isset($_GET['repertoireId'])) {

		$repertoireId = $_GET['repertoireId'];

		$reservation = $db->getReservationsFromRepertoire($repertoireId);

		if ($reservation != false) {
			$response["error"] = FALSE;
			$response["reservation"]["customerId"] = $reservation['customerId'];
			$response["reservation"]["movieId"] = $reservation['movieId']; 
			$response["reservation"]["hall"] = $reservation['hall'];
			$response["reservation"]["seatNumber"] = $reservation['seatNumber'];
			$response["reservation"]["row"] = $reservation['row'];
			$response["reservation"]["date"] = $reservation['date'];
			$response["reservation"]["seatTypeId"] = $reservation['seatTypeId'];
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