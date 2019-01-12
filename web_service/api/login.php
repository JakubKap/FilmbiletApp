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
			$response["customer"]["id"] = $customer["id"];
			$response["customer"]["name"] = $customer["name"];
			$response["customer"]["surname"] = $customer["surname"];
			$response["customer"]["email"] = $customer["email"];
			$response["customer"]["createdAt"] = $customer["createdAt"];
			$response["customer"]["updatedAt"] = $customer["updatedAt"];
			echo json_encode($response);
		} else {
			$response["error"] = TRUE;
			$response["message"] = "Dane do logowania są niepoprawne. Spróbuj ponownie.";
			echo json_encode($response);
		}
	} else {
		$response["error"] = TRUE;
		$response["message"] = "Pola e-mail lub hasło zostały pominięte!";
		echo json_encode($response);
	}