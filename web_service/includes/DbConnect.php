<?php 
	header('Content-Type: application/json; charset=utf-8');
	
	class DbConnect
	{
		private $con;
	 
		function __construct()
		{
		}
	 
		//connect to database
		function connect()
		{
			include_once dirname(__FILE__) . '/Constants.php';
	 
			$this->con = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);
	 
			if (mysqli_connect_errno()) {
				echo "Failed to connect to MySQL: " . mysqli_connect_error();
			}
			
			// Change character set to utf8
			mysqli_set_charset($this->con,"utf8");
	 
			//return the connection link 
			return $this->con;
		}
	}