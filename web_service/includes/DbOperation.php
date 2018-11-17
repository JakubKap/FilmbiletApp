<?php
header('Content-Type: application/json; charset=utf-8');
 
class DbOperation
{
    //Database connection link
    private $con;
 
    //Class constructor
    function __construct()
    {
        require_once dirname(__FILE__) . '/DbConnect.php';
        $db = new DbConnect();
        $this->con = $db->connect();
    }

	function getCustomer(){
		//results: idKlient, Imię, Nazwisko, E-mail
		$results = $this->con->prepare("SELECT * FROM Klient");
		if ($results !== false){
			$results->execute();
			$results->bind_result($id, $name, $surname, $email);
			//$results->bind_result($id, $name);
			
			$customers = array(); 
			
			while($results->fetch()){
				$customer  = array();
				$customer['id'] = $id; 
				$customer['name'] = $name; 
				$customer['surname'] = $surname; 
				$customer['email'] = $email; 
				
				array_push($customers, $customer); 
			}
			
			return $customers; 
		}
		
	}
}