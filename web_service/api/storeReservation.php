<?php
header('Content-Type: application/json; charset=utf-8');

	require_once '../includes/DbOperation.php';
	$db = new DbOperation();

	$response = array("error" => FALSE);
	if (isset($_POST['customerId']) && isset($_POST['seatNumber']) && isset($_POST['row']) && isset($_POST['seatTypeId']) && isset($_POST['repertoireId'])) {

		$customerId = $_POST['customerId'];
		$seatNumber = $_POST['seatNumber'];
		$row = $_POST['row'];
		$seatTypeId = $_POST['seatTypeId'];
		$repertoireId = $_POST['repertoireId'];
		
		
		// check if reservation exist in database
		if ($db->isReservationExisted($seatNumber, $repertoireId)) {
			$response["error"] = TRUE;
			$response["message"] = "Taka rezerwacja już istnieje";
			echo json_encode($response);
		} else {		
			$reservation = $db->storeReservation($customerId, $seatNumber, $row, $seatTypeId, $repertoireId);
			if ($reservation) {
				$response["error"] = FALSE;
				$response["reservation"]["id"] = $reservation["id"];
				$response["reservation"]["customerId"] = $reservation["customerId"];
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