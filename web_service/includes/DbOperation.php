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

	function getCustomers(){
		$results = $this->con->prepare("SELECT id, name, surname, email, createdAt, updatedAt FROM customer");
		if ($results !== false){
			$results->execute();
			$results->bind_result($id, $name, $surname, $email, $createdAt, $updatedAt);
			
			$customers = array(); 
			
			while($results->fetch()){
				$customer  = array();
				$customer['id'] = $id; 
				$customer['name'] = $name; 
				$customer['surname'] = $surname; 
				$customer['email'] = $email; 
				$customer['createdAt'] = $createdAt; 
				$customer['updatedAt'] = $updatedAt; 
				
				array_push($customers, $customer); 
			}
			
			return $customers; 
		} else return false;
	}
	
	function getMovies(){
		$results = $this->con->prepare("select * from movie");
		if ($results !== false){
			$results->execute();
			
			$results->bind_result($id, $title, $runningTimeMin, $age, $languageVersion, $releaseDate, $description);
			
			$movies = array(); 
			
			while($results->fetch()){
				$movie = array();
				$movie['id'] = $id;
				$movie['title'] = $title;
				$movie['runningTimeMin'] = $runningTimeMin;
				$movie['age'] = $age;
				$movie['languageVersion'] = $languageVersion;
				$movie['releaseDate'] = $releaseDate;
				$movie['description'] = $description;
				
				array_push($movies, $movie); 
			}
			
			return $movies; 
		} else return false;
	}
}