<?php
header('Content-Type: application/json; charset=utf-8');

	require_once '../includes/DbOperation.php';
	$db = new DbOperation();

	$response = array("error" => FALSE);
	
	if (isset($_POST['name']) && isset($_POST['surname']) && isset($_POST['email']) && isset($_POST['password'])) {

		$name = $_POST['name'];
		$surname = $_POST['surname'];
		$email = $_POST['email'];
		$password = $_POST['password'];
		
		// check if email exist in database
		if ($db->isCustomerExisted($email)) {
			$response["error"] = TRUE;
			$response["message"] = "Użytkownik o adresie: " . $email . "już istnieje w systemie";
			echo json_encode($response);
		} else {		
			$customer = $db->storeCustomer($name, $surname, $email, $password);
			if ($customer) {
				$response["error"] = FALSE;
				$response["uid"] = $customer["uniqueId"];
				$response["customer"]["name"] = $customer["name"];
				$response["customer"]["surname"] = $customer["surname"];
				$response["customer"]["email"] = $customer["email"];
				$response["customer"]["createdAt"] = $customer["createdAt"];
				$response["customer"]["updatedAt"] = $customer["updatedAt"];
				echo json_encode($response);
			} else {
				$response["error"] = TRUE;
				$response["message"] = "Błąd serwera!";
				echo json_encode($response);
			}
		}
	} else {
		$response["error"] = TRUE;
		$response["message"] = "Required parameters (name, surname, email or password) are missing!";
		echo json_encode($response);
	}