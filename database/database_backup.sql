-- phpMyAdmin SQL Dump
-- version 4.6.6
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Czas generowania: 16 Lis 2018, 17:48
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
-- Struktura tabeli dla tabeli `Film`
--

CREATE TABLE `Film` (
  `id` int(11) NOT NULL,
  `Tytul` varchar(100) CHARACTER SET utf8 COLLATE utf8_polish_ci NOT NULL,
  `CzasTrwaniaMin` int(11) NOT NULL,
  `OdLat` int(11) NOT NULL,
  `WersjaJezykowa` varchar(45) CHARACTER SET utf8 COLLATE utf8_polish_ci NOT NULL,
  `DataPremiery` date NOT NULL,
  `Opis` varchar(600) CHARACTER SET utf8 COLLATE utf8_polish_ci NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

--
-- Zrzut danych tabeli `Film`
--

INSERT INTO `Film` (`id`, `Tytul`, `CzasTrwaniaMin`, `OdLat`, `WersjaJezykowa`, `DataPremiery`, `Opis`) VALUES
(1, 'Planeta Singli 2', 159, 15, 'Polska', '2018-11-09', 'Związek Ani i Tomka przeżywa kryzys, bo oboje mają względem siebie inne zamiary. Tymczasem na horyzoncie pojawia się zakochany w dziewczynie milioner. '),
(2, 'Kler', 133, 15, 'Polska', '2018-09-28', 'Życie trzech księży ulega zmianie, kiedy ich drogi krzyżują się ponownie.'),
(4, 'Bohemian Rhapsody', 134, 15, 'Angielska (polskie napisy)', '2018-11-02', 'Dzięki oryginalnemu brzmieniu Queen staje się jednym z najpopularniejszych zespołów w historii muzyki.'),
(5, 'Król wyjęty spod prawa', 121, 18, 'Angielska (polskie napisy)', '2018-09-06', 'Dotychczas nieopowiedziana prawdziwa historia Roberta I Bruce’a, który w ciągu jednego roku z upadłego szlachcica stał się królem, a następnie wyjętym spod prawa bohaterem. W zniewolonej przez Anglię średniowiecznej Szkocji walczył, by ocalić swoją rodzinę, lud i kraj. Po przejęciu szkockiej korony, Robert i jego garstka sprzymierzeńców muszą stawić czoła najsilniejszej armii świata, dowodzonej przez króla Edwarda I oraz jego nieprzewidywalnego syna, księcia Walii.'),
(6, 'Suspiria', 152, 18, 'Angielska (polskie napisy)', '2018-11-02', 'Amerykańska baletnica rozpoczyna naukę na prestiżowej niemieckiej akademii tańca. Niebawem odkrywa mroczne sekrety szkoły.'),
(7, 'Climax', 90, 12, 'Angielska (polski dubbing)', '2018-10-19', 'Climax to inspirowana prawdziwymi wydarzeniami opowieść o grupie tancerzy, którzy w pewną zimową noc spotykają się w opuszczonej szkole na odludziu. Taneczna próba w rytm przebojów takich grup jak Daft Punk, Apex Twin i Soft Cell szybko zamienia się w pełną seksualnego napięcia imprezę. Gdy wychodzi na jaw, że domowej roboty sangrię ktoś \"doprawił\" narkotykami, dzika zabawa w zamkniętym budynku przeradza się w ekstremalną, psychodeliczną podróż.'),
(8, 'Pierwszy człowiek', 141, 15, 'Angielska (polskie napisy)', '2018-10-19', 'Fragment życia astronauty Neila Armstronga i jego legendarnej misji kosmicznej, dzięki której jako pierwszy człowiek stanął na Księżycu.'),
(9, 'Źle się dzieje w El Royale', 141, 18, 'Angielska (polskie napisy)', '2018-10-12', 'Siedmioro obcych sobie ludzi, z których każdy skrywa mroczną tajemnicę, spotyka się w El Royale.'),
(10, 'Ocean ognia', 121, 15, 'Angielska (polskie napisy)', '2018-10-26', 'Kapitan okrętu podwodnego współpracuje z drużyną Navy SEAL w celu uratowania prezydenta Rosji, który podczas zamachu wzięty został do niewoli. '),
(3, 'Winni', 85, 15, 'Duński (polskie napisy)', '2018-11-09', 'Dyspozytor linii 112 odbiera alarmowy telefon od porwanej kobiety. Połączenie zostaje przerwane i zaczyna się walka z czasem, by odnaleźć dzwoniącą oraz jej porywaczy.');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `Gatunek`
--

CREATE TABLE `Gatunek` (
  `idGatunek` int(11) NOT NULL,
  `NazwaGatunku` varchar(45) CHARACTER SET utf8 COLLATE utf8_polish_ci NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

--
-- Zrzut danych tabeli `Gatunek`
--

INSERT INTO `Gatunek` (`idGatunek`, `NazwaGatunku`) VALUES
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
-- Struktura tabeli dla tabeli `Gatunek_has_Film`
--

CREATE TABLE `Gatunek_has_Film` (
  `Gatunek_idGatunek` int(11) NOT NULL,
  `Film_id` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

--
-- Zrzut danych tabeli `Gatunek_has_Film`
--

INSERT INTO `Gatunek_has_Film` (`Gatunek_idGatunek`, `Film_id`) VALUES
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
-- Struktura tabeli dla tabeli `Klient`
--

CREATE TABLE `Klient` (
  `idKlient` int(11) NOT NULL,
  `Imię` varchar(45) CHARACTER SET utf8 COLLATE utf8_polish_ci NOT NULL,
  `Nazwisko` varchar(45) CHARACTER SET utf8 COLLATE utf8_polish_ci NOT NULL,
  `E-mail` varchar(45) CHARACTER SET utf8 COLLATE utf8_polish_ci NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

--
-- Zrzut danych tabeli `Klient`
--

INSERT INTO `Klient` (`idKlient`, `Imię`, `Nazwisko`, `E-mail`) VALUES
(1, 'Jan', 'Kowalski', 'jankowal@gmail.com'),
(2, 'Krzysztof', 'Marek', 'marek1996@outlook.com'),
(3, 'Krzysztof', 'Urbański', 'urbanskik@outlook.com'),
(4, 'Anna', 'Kamińska', 'annakam@op.pl'),
(5, 'Anna', 'Markiewicz', 'annam1990@onet.eu'),
(6, 'Robert', 'Kurzawa', 'kurzawarobert@wp.pl'),
(7, 'Mariola', 'Poniatowska', 'mariolap1970@gmail.com'),
(8, 'Agnieszka', 'Olecka', 'agnieszkaolecka12@yahoo.com'),
(9, 'Adrian', 'Małkiewicz', 'adi19658@gmail.com'),
(10, 'Tomasz', 'Szpak', 'szpak789@gmail.com');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `Rezerwacja`
--

CREATE TABLE `Rezerwacja` (
  `Klient_idKlient` int(11) NOT NULL,
  `Film_id` int(11) NOT NULL,
  `Sala` int(11) NOT NULL,
  `NumerMiejsca` int(11) NOT NULL,
  `Rząd` int(11) NOT NULL,
  `Czas` datetime NOT NULL,
  `TypMiejsca_id` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

--
-- Zrzut danych tabeli `Rezerwacja`
--

INSERT INTO `Rezerwacja` (`Klient_idKlient`, `Film_id`, `Sala`, `NumerMiejsca`, `Rząd`, `Czas`, `TypMiejsca_id`) VALUES
(1, 1, 1, 20, 16, '2018-11-28 14:20:00', 1),
(2, 1, 1, 10, 1, '2018-11-28 14:20:00', 2),
(7, 7, 3, 14, 20, '2018-11-28 19:20:00', 1),
(9, 7, 3, 15, 20, '2018-11-28 19:20:00', 1),
(10, 10, 2, 11, 1, '2018-11-28 20:00:00', 2),
(2, 5, 6, 10, 20, '2018-11-09 15:00:00', 4),
(6, 6, 1, 1, 1, '2018-11-27 13:00:00', 2),
(3, 2, 5, 5, 17, '2018-11-23 11:00:00', 4),
(4, 3, 5, 6, 17, '2018-11-30 12:00:00', 4),
(5, 4, 2, 12, 10, '2018-11-26 14:00:00', 3);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `TypMiejsca`
--

CREATE TABLE `TypMiejsca` (
  `id` int(11) NOT NULL,
  `Nazwa` varchar(50) CHARACTER SET utf8 COLLATE utf8_polish_ci NOT NULL,
  `Cena` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

--
-- Zrzut danych tabeli `TypMiejsca`
--

INSERT INTO `TypMiejsca` (`id`, `Nazwa`, `Cena`) VALUES
(1, 'Miejsca na podwójnej kanapie', 30),
(2, 'Miejsce blisko ekranu', 10),
(3, 'Miejsce na środku sali', 15),
(4, 'Miejsce na końcu sali', 20);

--
-- Indeksy dla zrzutów tabel
--

--
-- Indexes for table `Film`
--
ALTER TABLE `Film`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `Gatunek`
--
ALTER TABLE `Gatunek`
  ADD PRIMARY KEY (`idGatunek`);

--
-- Indexes for table `Gatunek_has_Film`
--
ALTER TABLE `Gatunek_has_Film`
  ADD PRIMARY KEY (`Gatunek_idGatunek`,`Film_id`),
  ADD KEY `fk_Gatunek_has_Film_Film1_idx` (`Film_id`),
  ADD KEY `fk_Gatunek_has_Film_Gatunek1_idx` (`Gatunek_idGatunek`);

--
-- Indexes for table `Klient`
--
ALTER TABLE `Klient`
  ADD PRIMARY KEY (`idKlient`);

--
-- Indexes for table `Rezerwacja`
--
ALTER TABLE `Rezerwacja`
  ADD PRIMARY KEY (`Klient_idKlient`,`Film_id`,`TypMiejsca_id`),
  ADD KEY `fk_Klient_has_Film_Film1_idx` (`Film_id`),
  ADD KEY `fk_Klient_has_Film_Klient_idx` (`Klient_idKlient`),
  ADD KEY `fk_Rezerwacja_TypMiejsca1_idx` (`TypMiejsca_id`);

--
-- Indexes for table `TypMiejsca`
--
ALTER TABLE `TypMiejsca`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT dla tabeli `Film`
--
ALTER TABLE `Film`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;
--
-- AUTO_INCREMENT dla tabeli `Gatunek`
--
ALTER TABLE `Gatunek`
  MODIFY `idGatunek` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;
--
-- AUTO_INCREMENT dla tabeli `Klient`
--
ALTER TABLE `Klient`
  MODIFY `idKlient` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;
--
-- AUTO_INCREMENT dla tabeli `TypMiejsca`
--
ALTER TABLE `TypMiejsca`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
