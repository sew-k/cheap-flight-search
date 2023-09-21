CREATE TABLE IF NOT EXISTS airports (
    airport_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    country VARCHAR(255),
    city VARCHAR(255),
    iata_code VARCHAR(255)
);

INSERT IGNORE INTO airports (airport_id, country, city, iata_code)
VALUES  (18, "Portugal", "Lisbon", "LIS"),
        (21, "Portugal", "Porto", "OPO"),
        (22, "Poland", "Wroclaw", "WRO"),
        (23, "Poland", "Warsaw", "WAW"),
        (24, "Poland", "Modlin", "WMI"),
        (25, "Poland", "Gdansk", "GDN"),
        (30, "Spain", "Palma de Mallorca", "PMI"),
        (31, "Spain", "Malaga", "AGP"),
        (40, "Greece", "Korfu", "CFU"),
        (41, "Greece", "Rhodes", "RHO"),
        (42, "Greece", "Chania", "CHQ"),
        (43, "Greece", "Heraklion", "HER"),
        (44, "Germany", "Cologne", "CGN");