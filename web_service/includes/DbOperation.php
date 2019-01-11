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
	
	public function storeCustomer($name, $surname, $email, $password) {
        $uuid = uniqid('', true);	//uniqueId
        $hash = $this->hashSSHA($password);
        $encryptedPassword = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt

        $results = $this->con->prepare("INSERT INTO customer(uniqueId, name, surname, email, encryptedPassword, salt, createdAt) VALUES(?, ?, ?, ?, ?, ?, NOW())");
        $results->bind_param("ssssss", $uuid, $name, $surname, $email, $encryptedPassword, $salt);
		
		if ($results !== false){
			$result = $results->execute();
			$results->close();

			// check for successful store
			if ($result) {
				$results = $this->con->prepare("SELECT * FROM customer WHERE email = ?");
				$results->bind_param("s", $email);
				$results->execute();
				$customer = $results->get_result()->fetch_assoc();
				$results->close();

				return $customer;
			} else {
				return false;
			}
		}
    }
	
	public function isCustomerExisted($email) {
		$results = $this->con->prepare("SELECT email from customer WHERE email = ?");

        $results->bind_param("s", $email);

        $results->execute();

        $results->store_result();

        if ($results->num_rows > 0) {
            $results->close();
            return true;
        } else {
            $results->close();
            return false;
        }
    }
	
	public function getCustomerByEmailAndPassword($email, $password) {

        $results = $this->con->prepare("SELECT * FROM customer WHERE email = ?");

        $results->bind_param("s", $email);

        if ($results->execute()) {
            $customer = $results->get_result()->fetch_assoc();
            $results->close();

            // verifying customer password
            $salt = $customer['salt'];
            $encryptedPassword = $customer['encryptedPassword'];
            $hash = $this->checkhashSSHA($salt, $password);
            if ($encryptedPassword == $hash) {
                return $customer;
            }
        } else {
            return NULL;
        }
    }

	public function getCustomers(){
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
	
	public function getMovies(){
		$results = $this->con->prepare("select id, title, runningTimeMin, age, languageVersion, releaseDate, description from movie");
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
	
	public function getSeatTypes(){
		$results = $this->con->prepare("select id, name, price from seatType");
		if ($results !== false){
			$results->execute();
			
			$results->bind_result($id, $name, $price);
			
			$seatTypes = array(); 
			
			while($results->fetch()){
				$seatType  = array();
				$seatType['id'] = $id;
				$seatType['name'] = $name;
				$seatType['price'] = $price;
				
				array_push($seatTypes, $seatType); 
			}
			
			return $seatTypes; 
		} else return false;
	}
	
	public function getGenres(){
		$results = $this->con->prepare("SELECT id, name FROM genre");
		if ($results !== false){
			$results->execute();
			
			$results->bind_result($id, $name);
			
			$genres = array(); 
			
			while($results->fetch()){
				$genre  = array();
				$genre['id'] = $id; 
				$genre['name'] = $name;
				
				array_push($genres, $genre); 
			}
			
			return $genres; 
		} else return false;
	}
	
	public function getReservations(){
		$results = $this->con->prepare("SELECT * FROM reservation");
		if ($results !== false){
			$results->execute();
			
			$results->bind_result($id, $customerId, $seatNumber, $row, $date, $seatTypeId, $repertoireId);
			
			$reservations = array(); 
			
			while($results->fetch()){
				$reservation  = array();
				$reservation['id'] = $id; 
				$reservation['customerId'] = $customerId; 
				$reservation['seatNumber'] = $seatNumber; 
				$reservation['row'] = $row; 
				$reservation['date'] = $date; 
				$reservation['seatTypeId'] = $seatTypeId; 
				$reservation['repertoireId'] = $seatNumber; 
				
				array_push($reservations, $reservation); 
			}
			
			return $reservations; 
		} else return false;
	}
	
	public function getReservationsFromRepertoire($repertoireId){
		
		 $results = $this->con->prepare("SELECT id, customerId, seatNumber, row, date, seatTypeId FROM reservation WHERE repertoireId = ?");
		
		 $results->bind_param("s", $repertoireId);
		 	
		if ($results !== false){
			$results->execute();
			
			$results->bind_result($id, $customerId, $seatNumber, $row, $date, $seatTypeId);
			
			$reservations = array(); 
			
			while($results->fetch()){
				$reservation  = array();
				$reservation['id'] = $id; 
				$reservation['customerId'] = $customerId; 
				$reservation['seatNumber'] = $seatNumber; 
				$reservation['row'] = $row; 
				$reservation['date'] = $date; 
				$reservation['seatTypeId'] = $seatTypeId; 
				
				array_push($reservations, $reservation); 
			}
			
			return $reservations; 
		} else return false;
	}
	
	public function storeReservation($customerId, $seatNumber, $row, $seatTypeId, $repertoireId) {
       
		$results = $this->con->prepare("INSERT INTO reservation(customerId, seatNumber, row, date, seatTypeId, repertoireId) VALUES(?, ?, ?, NOW(), ?, ?)");
		
		
        $results->bind_param("sssss", $customerId, $seatNumber, $row, $seatTypeId, $repertoireId);
		
		if ($results !== false){
			$result = $results->execute();
			$results->close();

			// check for successful store
			if ($result) {
				$results = $this->con->prepare("SELECT * FROM reservation WHERE seatNumber = ? AND repertoireId = ?");
				$results->bind_param("ss", $seatNumber, $repertoireId);
				$results->execute();
				$reservation = $results->get_result()->fetch_assoc();
				$results->close();

				return $reservation;
			} else {
				return false;
			}
		}
    }
	
	public function isReservationExisted($seatNumber, $repertoireId) {
		$results = $this->con->prepare("SELECT customerId from reservation WHERE seatNumber = ? AND repertoireId = ?");

        $results->bind_param("ss", $seatNumber, $repertoireId);

        $results->execute();

        $results->store_result();

        if ($results->num_rows > 0) {
            $results->close();
            return true;
        } else {
            $results->close();
            return false;
        }
    }
	
	public function getMoviesFromRepertoire(){
		$results = $this->con->prepare
		//("select id from genre");
		(
			"SELECT m.id, m.title, m.runningTimeMin, m.age, m.pictureUrl, GROUP_CONCAT(DISTINCT g.name SEPARATOR ', ') AS genres
			FROM movie AS m
			INNER JOIN repertoire AS rep ON rep.movieId = m.id
			LEFT JOIN 
			(
				gatunekHasMovie AS gm INNER JOIN genre AS g ON gm.genreId = g.id
			) ON m.id = gm.movieID
			GROUP BY m.id, m.title, m.runningTimeMin, m.age, m.pictureUrl"
		);
		if ($results !== false){
			$results->execute();
			
			$results->bind_result($id, $title, $runningTimeMin, $age, $pictureUrl, $genres);
			
			$movies = array(); 
			
			while($results->fetch()){
				$movie  = array();
				
				if (is_null($genres))
					$genres = "-";
				
				$movie['id'] = $id; 
				$movie['title'] = $title; 
				$movie['runningTimeMin'] = $runningTimeMin; 
				$movie['age'] = $age; 
				$movie['pictureUrl'] = $pictureUrl; 
				$movie['genres'] = $genres;
				
				array_push($movies, $movie); 
			}
			
			return $movies; 
		} else return false;
	}
	
	public function getMovieRepertoire($movieId){
			
			 $results = $this->con->prepare("SELECT id, date from repertoire WHERE date >= now() AND movieId  = ?");
			
			 $results->bind_param("s", $movieId);
				
			if ($results !== false){
				$results->execute();
				
				$results->bind_result($id, $date);
				
				$repertoires = array(); 
				
				while($results->fetch()){
					$repertoire  = array();
					$repertoire['id'] = $id; 
					$repertoire['date'] = $date; 
					
					array_push($repertoires, $repertoire); 
				}
				
				return $repertoires; 
			} else return false;
		}
	
	//Encrypting password
    private function hashSSHA($password) {

        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }

    //Decrypting password
    private function checkhashSSHA($salt, $password) {

        $hash = base64_encode(sha1($password . $salt, true) . $salt);

        return $hash;
    }
}