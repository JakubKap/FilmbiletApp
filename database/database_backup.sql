-- phpMyAdmin SQL Dump
-- version 4.6.6
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Czas generowania: 14 Gru 2018, 18:17
-- Wersja serwera: 10.1.37-MariaDB-2.cba
-- Wersja PHP: 7.1.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Baza danych: `seba974`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `customer`
--

CREATE TABLE `customer` (
  `id` int(11) NOT NULL,
  `name` varchar(45) COLLATE utf8_polish_ci NOT NULL,
  `surname` varchar(45) COLLATE utf8_polish_ci NOT NULL,
  `email` varchar(100) COLLATE utf8_polish_ci NOT NULL,
  `encryptedPassword` varchar(80) COLLATE utf8_polish_ci NOT NULL,
  `salt` varchar(10) COLLATE utf8_polish_ci NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `uniqueId` varchar(23) COLLATE utf8_polish_ci NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Zrzut danych tabeli `customer`
--

INSERT INTO `customer` (`id`, `name`, `surname`, `email`, `encryptedPassword`, `salt`, `createdAt`, `updatedAt`, `uniqueId`) VALUES
(1, 'Seba', 'Folusz', 'seba@mail.com', '/ud6N2EIan3rZBbKq1BQKTJXMhEwMjg1N2ZmOWE0', '02857ff9a4', '2018-11-19 23:52:53', NULL, '5bf33ec5d3dc25.73669269'),
(2, 'Seba', 'Folusz', 'seba2@mail.com', 'IgcBMhy3dA81JqkX477UDtcZgbE4NDYzZDVlYmQ4', '8463d5ebd8', '2018-11-20 00:50:47', NULL, '5bf34c574f87b0.21435215'),
(3, 'Jakub', 'Kapusta', 'jakub1996k@gmail.com', 'Z2DY/YEy1nKd17yFENuhxSc8zPViN2IxYWNhZjQ1', 'b7b1acaf45', '2018-12-02 10:58:13', NULL, '5c03acb592cd25.86037913'),
(4, 'jakub', 'Kapusta', 'jakub19896k@gmail.com', 'xUvg5BWIw5B7d39k0FMT458tPzE3NTY2OWY2NGIw', '75669f64b0', '2018-12-04 00:00:22', NULL, '5c05b5861b6a83.32272349'),
(5, 'Sebastian', 'nazwisko', 'kuba@mail.com', '+/+JV+lTnEem5ag45rVxRhvnWtJiMmQ5ZTE5NGYy', 'b2d9e194f2', '2018-12-07 20:04:10', NULL, '5c0ac42a7625c5.51833012'),
(6, 'hdjd', 'hxjd', 'nie_mail', 'kkJ7JflYn/4fOj0m8X1Kx8JxFSdkYzhlN2E2NzM3', 'dc8e7a6737', '2018-12-07 20:48:31', NULL, '5c0ace8f976b43.26374653');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `gatunekHasMovie`
--

CREATE TABLE `gatunekHasMovie` (
  `genreId` int(11) NOT NULL,
  `movieId` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Zrzut danych tabeli `gatunekHasMovie`
--

INSERT INTO `gatunekHasMovie` (`genreId`, `movieId`) VALUES
(2, 5),
(2, 7),
(5, 6),
(7, 3),
(7, 9),
(7, 10),
(20, 5),
(22, 2),
(23, 4);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `genre`
--

CREATE TABLE `genre` (
  `id` int(11) NOT NULL,
  `name` varchar(45) COLLATE utf8_polish_ci NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Zrzut danych tabeli `genre`
--

INSERT INTO `genre` (`id`, `name`) VALUES
(1, 'Komedia'),
(2, 'Dramat'),
(3, 'Melodramat'),
(4, 'Western'),
(5, 'Horror'),
(6, 'Musical'),
(7, 'Thriller'),
(8, 'Sensacyjny'),
(9, 'Gangsterski'),
(10, 'Kryminał'),
(11, 'Science fiction'),
(12, 'Fantasy'),
(13, 'Historyczny'),
(14, 'Komedia romantyczna'),
(15, 'Psychologiczny'),
(16, 'Wojenny'),
(17, 'Szpiegowski'),
(18, 'Familijny'),
(19, 'Sportowy'),
(20, 'Kostiumowy'),
(21, 'Animowany'),
(22, 'Obyczajowy'),
(23, 'Muzyczny');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `movie`
--

CREATE TABLE `movie` (
  `id` int(11) NOT NULL,
  `title` varchar(100) COLLATE utf8_polish_ci NOT NULL,
  `runningTimeMin` int(11) NOT NULL,
  `age` int(11) NOT NULL,
  `languageVersion` varchar(45) COLLATE utf8_polish_ci NOT NULL,
  `releaseDate` date NOT NULL,
  `description` varchar(600) COLLATE utf8_polish_ci NOT NULL,
  `pictureUrl` varchar(2083) COLLATE utf8_polish_ci NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Zrzut danych tabeli `movie`
--

INSERT INTO `movie` (`id`, `title`, `runningTimeMin`, `age`, `languageVersion`, `releaseDate`, `description`, `pictureUrl`) VALUES
(1, 'Planeta Singli 2', 159, 15, 'Polska', '2018-11-09', 'Związek Ani i Tomka przeżywa kryzys, bo oboje mają względem siebie inne zamiary. Tymczasem na horyzoncie pojawia się zakochany w dziewczynie milioner. ', 'http://filmbilet.cba.pl/images/planeta-singli-2.jpeg'),
(2, 'Kler', 133, 15, 'Polska', '2018-09-28', 'Życie trzech księży ulega zmianie, kiedy ich drogi krzyżują się ponownie.', 'http://filmbilet.cba.pl/images/kler.jpg'),
(4, 'Bohemian Rhapsody', 134, 15, 'Angielska (polskie napisy)', '2018-11-02', 'Dzięki oryginalnemu brzmieniu Queen staje się jednym z najpopularniejszych zespołów w historii muzyki.', 'http://filmbilet.cba.pl/images/bohemian.jpg'),
(5, 'Król wyjęty spod prawa', 121, 18, 'Angielska (polskie napisy)', '2018-09-06', 'Dotychczas nieopowiedziana prawdziwa historia Roberta I Bruce’a, który w ciągu jednego roku z upadłego szlachcica stał się królem, a następnie wyjętym spod prawa bohaterem. W zniewolonej przez Anglię średniowiecznej Szkocji walczył, by ocalić swoją rodzinę, lud i kraj. Po przejęciu szkockiej korony, Robert i jego garstka sprzymierzeńców muszą stawić czoła najsilniejszej armii świata, dowodzonej przez króla Edwarda I oraz jego nieprzewidywalnego syna, księcia Walii.', 'http://filmbilet.cba.pl/images/krol-wyjety-spod-prawa.jpg'),
(6, 'Suspiria', 152, 18, 'Angielska (polskie napisy)', '2018-11-02', 'Amerykańska baletnica rozpoczyna naukę na prestiżowej niemieckiej akademii tańca. Niebawem odkrywa mroczne sekrety szkoły.', 'http://filmbilet.cba.pl/images/suspiria.jpg'),
(7, 'Climax', 90, 12, 'Angielska (polski dubbing)', '2018-10-19', 'Climax to inspirowana prawdziwymi wydarzeniami opowieść o grupie tancerzy, którzy w pewną zimową noc spotykają się w opuszczonej szkole na odludziu. Taneczna próba w rytm przebojów takich grup jak Daft Punk, Apex Twin i Soft Cell szybko zamienia się w pełną seksualnego napięcia imprezę. Gdy wychodzi na jaw, że domowej roboty sangrię ktoś \"doprawił\" narkotykami, dzika zabawa w zamkniętym budynku przeradza się w ekstremalną, psychodeliczną podróż.', 'http://filmbilet.cba.pl/images/climax.jpg'),
(8, 'Pierwszy człowiek', 141, 15, 'Angielska (polskie napisy)', '2018-10-19', 'Fragment życia astronauty Neila Armstronga i jego legendarnej misji kosmicznej, dzięki której jako pierwszy człowiek stanął na Księżycu.', 'http://filmbilet.cba.pl/images/pierwszy-czlowiek.jpg'),
(9, 'Źle się dzieje w El Royale', 141, 18, 'Angielska (polskie napisy)', '2018-10-12', 'Siedmioro obcych sobie ludzi, z których każdy skrywa mroczną tajemnicę, spotyka się w El Royale.', 'http://filmbilet.cba.pl/images/zle-sie-dzieje-w-el-royale.jpg'),
(10, 'Ocean ognia', 121, 15, 'Angielska (polskie napisy)', '2018-10-26', 'Kapitan okrętu podwodnego współpracuje z drużyną Navy SEAL w celu uratowania prezydenta Rosji, który podczas zamachu wzięty został do niewoli. ', 'http://filmbilet.cba.pl/images/ocean-ognia.jpg'),
(3, 'Winni', 85, 15, 'Duński (polskie napisy)', '2018-11-09', 'Dyspozytor linii 112 odbiera alarmowy telefon od porwanej kobiety. Połączenie zostaje przerwane i zaczyna się walka z czasem, by odnaleźć dzwoniącą oraz jej porywaczy.', 'http://filmbilet.cba.pl/images/winni.jpg');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `repertoire`
--

CREATE TABLE `repertoire` (
  `id` int(11) NOT NULL,
  `date` datetime NOT NULL,
  `movieId` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Zrzut danych tabeli `repertoire`
--

INSERT INTO `repertoire` (`id`, `date`, `movieId`) VALUES
(1, '2018-11-28 14:20:00', 1),
(2, '2018-11-28 19:20:00', 7),
(3, '2018-11-28 20:00:00', 10),
(4, '2018-11-09 15:00:00', 5),
(5, '2018-11-27 13:00:00', 6),
(6, '2018-11-23 11:00:00', 5),
(7, '2018-11-30 12:00:00', 3),
(8, '2018-11-26 14:00:00', 4);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `reservation`
--

CREATE TABLE `reservation` (
  `id` int(11) NOT NULL,
  `customerId` int(11) NOT NULL,
  `seatNumber` int(11) NOT NULL,
  `row` int(11) NOT NULL,
  `date` datetime NOT NULL,
  `seatTypeId` int(11) NOT NULL,
  `repertoireId` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Zrzut danych tabeli `reservation`
--

INSERT INTO `reservation` (`id`, `customerId`, `seatNumber`, `row`, `date`, `seatTypeId`, `repertoireId`) VALUES
(1, 1, 1, 1, '2018-12-20 18:00:00', 1, 1),
(2, 1, 12, 2, '2018-12-01 17:58:11', 3, 1),
(3, 2, 13, 2, '2018-12-01 18:07:00', 3, 1),
(4, 25, 1, 1, '2018-12-12 20:59:45', 1, 1),
(5, 3, 1, 1, '2018-12-01 22:43:24', 4, 1),
(6, 4, 5, 1, '2018-12-02 09:23:28', 1, 1),
(7, 26, 1, 1, '2018-12-12 21:04:34', 2, 1),
(8, 2, 55, 11, '2018-12-02 11:05:22', 1, 2);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `seatType`
--

CREATE TABLE `seatType` (
  `id` int(11) NOT NULL,
  `name` varchar(50) COLLATE utf8_polish_ci NOT NULL,
  `price` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Zrzut danych tabeli `seatType`
--

INSERT INTO `seatType` (`id`, `name`, `price`) VALUES
(1, 'Miejsca na podwójnej kanapie', 30),
(2, 'Miejsce blisko ekranu', 10),
(3, 'Miejsce na środku sali', 15),
(4, 'Miejsce na końcu sali', 20);

--
-- Indeksy dla zrzutów tabel
--

--
-- Indexes for table `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `E-mail_UNIQUE` (`email`),
  ADD UNIQUE KEY `unique_id_UNIQUE` (`uniqueId`);

--
-- Indexes for table `gatunekHasMovie`
--
ALTER TABLE `gatunekHasMovie`
  ADD PRIMARY KEY (`genreId`,`movieId`),
  ADD KEY `fk_Gatunek_has_Film_Film1_idx` (`movieId`),
  ADD KEY `fk_Gatunek_has_Film_Gatunek1_idx` (`genreId`);

--
-- Indexes for table `genre`
--
ALTER TABLE `genre`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `movie`
--
ALTER TABLE `movie`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `repertoire`
--
ALTER TABLE `repertoire`
  ADD PRIMARY KEY (`id`,`movieId`),
  ADD KEY `fk_repertoire_movie1_idx` (`movieId`);

--
-- Indexes for table `reservation`
--
ALTER TABLE `reservation`
  ADD PRIMARY KEY (`id`,`repertoireId`,`seatNumber`),
  ADD KEY `fk_Klient_has_Film_Klient_idx` (`customerId`),
  ADD KEY `fk_Rezerwacja_TypMiejsca1_idx` (`seatTypeId`),
  ADD KEY `fk_reservation_repertoire1_idx` (`repertoireId`);

--
-- Indexes for table `seatType`
--
ALTER TABLE `seatType`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT dla tabeli `customer`
--
ALTER TABLE `customer`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
--
-- AUTO_INCREMENT dla tabeli `genre`
--
ALTER TABLE `genre`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;
--
-- AUTO_INCREMENT dla tabeli `movie`
--
ALTER TABLE `movie`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;
--
-- AUTO_INCREMENT dla tabeli `repertoire`
--
ALTER TABLE `repertoire`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
--
-- AUTO_INCREMENT dla tabeli `reservation`
--
ALTER TABLE `reservation`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
--
-- AUTO_INCREMENT dla tabeli `seatType`
--
ALTER TABLE `seatType`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
