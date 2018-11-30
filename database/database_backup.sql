-- phpMyAdmin SQL Dump
-- version 4.6.6
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Czas generowania: 19 Lis 2018, 23:28
-- Wersja serwera: 10.1.29-MariaDB-6
-- Wersja PHP: 7.1.24

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
(2, 8),
(5, 6),
(7, 3),
(7, 9),
(7, 10),
(14, 1),
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
(1, 'Planeta Singli 2', 159, 15, 'Polska', '2018-11-09', 'Związek Ani i Tomka przeżywa kryzys, bo oboje mają względem siebie inne zamiary. Tymczasem na horyzoncie pojawia się zakochany w dziewczynie milioner. ', 'https://m.media-amazon.com/images/M/MV5BY2RlMDhlY2MtMjQ1Zi00NzI5LTgxOTgtZjliNWMzYTY3NWZkL2ltYWdlL2ltYWdlXkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_.jpg'),
(2, 'Kler', 133, 15, 'Polska', '2018-09-28', 'Życie trzech księży ulega zmianie, kiedy ich drogi krzyżują się ponownie.', 'https://m.media-amazon.com/images/M/MV5BY2RlMDhlY2MtMjQ1Zi00NzI5LTgxOTgtZjliNWMzYTY3NWZkL2ltYWdlL2ltYWdlXkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_.jpg'),
(4, 'Bohemian Rhapsody', 134, 15, 'Angielska (polskie napisy)', '2018-11-02', 'Dzięki oryginalnemu brzmieniu Queen staje się jednym z najpopularniejszych zespołów w historii muzyki.', 'https://m.media-amazon.com/images/M/MV5BY2RlMDhlY2MtMjQ1Zi00NzI5LTgxOTgtZjliNWMzYTY3NWZkL2ltYWdlL2ltYWdlXkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_.jpg'),
(5, 'Król wyjęty spod prawa', 121, 18, 'Angielska (polskie napisy)', '2018-09-06', 'Dotychczas nieopowiedziana prawdziwa historia Roberta I Bruce’a, który w ciągu jednego roku z upadłego szlachcica stał się królem, a następnie wyjętym spod prawa bohaterem. W zniewolonej przez Anglię średniowiecznej Szkocji walczył, by ocalić swoją rodzinę, lud i kraj. Po przejęciu szkockiej korony, Robert i jego garstka sprzymierzeńców muszą stawić czoła najsilniejszej armii świata, dowodzonej przez króla Edwarda I oraz jego nieprzewidywalnego syna, księcia Walii.', 'https://m.media-amazon.com/images/M/MV5BY2RlMDhlY2MtMjQ1Zi00NzI5LTgxOTgtZjliNWMzYTY3NWZkL2ltYWdlL2ltYWdlXkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_.jpg'),
(6, 'Suspiria', 152, 18, 'Angielska (polskie napisy)', '2018-11-02', 'Amerykańska baletnica rozpoczyna naukę na prestiżowej niemieckiej akademii tańca. Niebawem odkrywa mroczne sekrety szkoły.', 'https://m.media-amazon.com/images/M/MV5BY2RlMDhlY2MtMjQ1Zi00NzI5LTgxOTgtZjliNWMzYTY3NWZkL2ltYWdlL2ltYWdlXkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_.jpg'),
(7, 'Climax', 90, 12, 'Angielska (polski dubbing)', '2018-10-19', 'Climax to inspirowana prawdziwymi wydarzeniami opowieść o grupie tancerzy, którzy w pewną zimową noc spotykają się w opuszczonej szkole na odludziu. Taneczna próba w rytm przebojów takich grup jak Daft Punk, Apex Twin i Soft Cell szybko zamienia się w pełną seksualnego napięcia imprezę. Gdy wychodzi na jaw, że domowej roboty sangrię ktoś \"doprawił\" narkotykami, dzika zabawa w zamkniętym budynku przeradza się w ekstremalną, psychodeliczną podróż.', 'https://m.media-amazon.com/images/M/MV5BY2RlMDhlY2MtMjQ1Zi00NzI5LTgxOTgtZjliNWMzYTY3NWZkL2ltYWdlL2ltYWdlXkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_.jpg'),
(8, 'Pierwszy człowiek', 141, 15, 'Angielska (polskie napisy)', '2018-10-19', 'Fragment życia astronauty Neila Armstronga i jego legendarnej misji kosmicznej, dzięki której jako pierwszy człowiek stanął na Księżycu.', 'https://m.media-amazon.com/images/M/MV5BY2RlMDhlY2MtMjQ1Zi00NzI5LTgxOTgtZjliNWMzYTY3NWZkL2ltYWdlL2ltYWdlXkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_.jpg'),
(9, 'Źle się dzieje w El Royale', 141, 18, 'Angielska (polskie napisy)', '2018-10-12', 'Siedmioro obcych sobie ludzi, z których każdy skrywa mroczną tajemnicę, spotyka się w El Royale.', 'https://m.media-amazon.com/images/M/MV5BY2RlMDhlY2MtMjQ1Zi00NzI5LTgxOTgtZjliNWMzYTY3NWZkL2ltYWdlL2ltYWdlXkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_.jpg'),
(10, 'Ocean ognia', 121, 15, 'Angielska (polskie napisy)', '2018-10-26', 'Kapitan okrętu podwodnego współpracuje z drużyną Navy SEAL w celu uratowania prezydenta Rosji, który podczas zamachu wzięty został do niewoli. ', 'https://m.media-amazon.com/images/M/MV5BY2RlMDhlY2MtMjQ1Zi00NzI5LTgxOTgtZjliNWMzYTY3NWZkL2ltYWdlL2ltYWdlXkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_.jpg'),
(3, 'Winni', 85, 15, 'Duński (polskie napisy)', '2018-11-09', 'Dyspozytor linii 112 odbiera alarmowy telefon od porwanej kobiety. Połączenie zostaje przerwane i zaczyna się walka z czasem, by odnaleźć dzwoniącą oraz jej porywaczy.', 'https://m.media-amazon.com/images/M/MV5BY2RlMDhlY2MtMjQ1Zi00NzI5LTgxOTgtZjliNWMzYTY3NWZkL2ltYWdlL2ltYWdlXkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_.jpg');

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
(6, '2018-11-23 11:00:00', 2),
(7, '2018-11-30 12:00:00', 3),
(8, '2018-11-26 14:00:00', 4);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `reservation`
--

CREATE TABLE `reservation` (
  `customerId` int(11) NOT NULL,
  `hall` int(11) NOT NULL,
  `seatNumber` int(11) NOT NULL,
  `row` int(11) NOT NULL,
  `date` datetime NOT NULL,
  `seatTypeId` int(11) NOT NULL,
  `repertoireId` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

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
  ADD PRIMARY KEY (`customerId`,`seatTypeId`,`repertoireId`),
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
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
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
-- AUTO_INCREMENT dla tabeli `seatType`
--
ALTER TABLE `seatType`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
