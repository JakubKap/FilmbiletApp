<?php
header('Content-Type: application/json; charset=utf-8');

	require_once '../includes/DbOperation.php';
	$db = new DbOperation();

	$response = array("error" => FALSE);
	if (isset($_GET['customerId']) && isset($_GET['hall']) && isset($_GET['seatNumber']) && isset($_GET['row']) && isset($_GET['seatTypeId']) && isset($_GET['repertoireId'])) {

		$customerId = $_GET['customerId'];
		$hall = $_GET['hall'];
		$seatNumber = $_GET['seatNumber'];
		$row = $_GET['row'];
		$seatTypeId = $_GET['seatTypeId'];
		$repertoireId = $_GET['repertoireId'];
		
		
		// check if reservation exist in database
		if ($db->isReservationExisted($hall, $seatNumber, $row, $seatTypeId, $repertoireId)) {
			$response["error"] = TRUE;
			$response["message"] = "Taka rezerwacja już istnieje";
			echo json_encode($response);
		} else {		
			$reservation = $db->storeReservation($customerId, $hall, $seatNumber, $row, $seatTypeId, $repertoireId);
			if ($reservation) {
				$response["error"] = FALSE;
				$response["reservation"]["customerId"] = $reservation["customerId"];
				$response["reservation"]["hall"] = $reservation["hall"];
				$response["reservation"]["seatNumber"] = $reservation["seatNumber"];
				$response["reservation"]["row"] = $reservation["row"];
				$response["reservation"]["date"] = $reservation["date"];
				$response["reservation"]["seatTypeId"] = $reservation["seatTypeId"];
				$response["reservation"]["repertoireId"] = $reservation["repertoireId"];
				echo json_encode($response);

			} else {
				$response["error"] = TRUE;
				$response["message"] = "Wystąpił nieznany błąd!";
				echo json_encode($response);
			}
		}
	} else {
		$response["error"] = TRUE;
		$response["message"] = "Brak wymaganych parametrów!";
		echo json_encode($response);
	}