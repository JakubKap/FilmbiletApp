<?php

	require_once '../includes/DbOperation.php';
	$db = new DbOperation();

	$response = array("error" => FALSE);

	if (isset($_POST['email']) && isset($_POST['password'])) {

		$email = $_POST['email'];
		$password = $_POST['password'];

		$customer = $db->getCustomerByEmailAndPassword($email, $password);

		if ($customer != false) {
			$response["error"] = FALSE;
			$response["uid"] = $customer["uniqueId"];
			$response["customer"]["name"] = $customer["name"];
			$response["customer"]["email"] = $customer["email"];
			$response["customer"]["createdAt"] = $customer["createdAt"];
			$response["customer"]["updatedAt"] = $customer["updatedAt"];
			echo json_encode($response);
		} else {
			$response["error"] = TRUE;
			$response["message"] = "Login credentials are wrong. Please try again!";
			echo json_encode($response);
		}
	} else {
		$response["error"] = TRUE;
		$response["message"] = "Required parameters email or password is missing!";
		echo json_encode($response);
	}