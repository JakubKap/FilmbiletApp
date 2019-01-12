<?php

	require_once '../includes/DbOperation.php';
	$db = new DbOperation();

	$response = array("error" => FALSE, "repertoire" => array());

	if (isset($_POST['movieId'])) {

		$movieId = $_POST['movieId'];

		$repertoire = $db->getMovieRepertoire($movieId);
	
		
		if ($repertoire != false) {
		
		
			foreach($repertoire as $arr){
				$response2 = array(
				"id" => $arr['id'],
				"date" => $arr['date'],
				);
				array_push($response["repertoire"],$response2);
			}
			
			echo json_encode($response);
			
		} else {
			$response["error"] = TRUE;
			$response["message"] = "Nie ma repertuaru dla podanego filmu. Spróbuj ponownie.";
			echo json_encode($response);
		}
	} else {
		$response["error"] = TRUE;
		$response["message"] = "Pole movieId zostało pominięte!";
		echo json_encode($response);
	}