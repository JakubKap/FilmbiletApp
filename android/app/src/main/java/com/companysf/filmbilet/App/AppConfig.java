package com.companysf.filmbilet.App;

public class AppConfig {
    public static String LOGIN_URL = "http://35.204.119.131/api/login.php";
    public static String REGISTER_URL = "http://35.204.119.131/api/register.php";
    public static String GET_MOVIES_FROM_REPERTOIRE = "http://35.204.119.131/api/Api.php?q=getMovies&from=repertoire";
    public static String GET_RESERVATIONS = "http://35.204.119.131/api/reservations.php";
    public static String STORE_RESERVATION = "http://35.204.119.131/api/storeReservation.php";
    public static String GET_CUSTOMER_RESERVATIONS = "http://35.204.119.131/api/customerReservations.php";
    public static String GET_MOVIE_REPERTOIRE = "http://35.204.119.131/api/repertoires.php";
    public static String websocketURL = "ws://35.204.119.131:8080/";
}
