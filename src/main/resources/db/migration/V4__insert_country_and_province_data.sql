-- -----------------------------------------------------
-- V4: Insert data into country and province tables
-- -----------------------------------------------------
-- This migration inserts data for African countries and their provinces

USE `full-stack-ecommerce`;

-- Insert countries
INSERT INTO `country` (`code`, `name`) VALUES
('AO', 'Angola'),
('BW', 'Botswana'),
('KM', 'Comoros'),
('CD', 'Democratic Republic of the Congo'),
('SZ', 'Eswatini'),
('LS', 'Lesotho'),
('MG', 'Madagascar'),
('MW', 'Malawi'),
('MU', 'Mauritius'),
('MZ', 'Mozambique'),
('NA', 'Namibia'),
('SC', 'Seychelles'),
('ZA', 'South Africa'),
('TZ', 'Tanzania'),
('ZM', 'Zambia'),
('ZW', 'Zimbabwe');

-- Insert provinces for Angola
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Bengo', id FROM `country` WHERE `code` = 'AO';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Benguela', id FROM `country` WHERE `code` = 'AO';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Bié', id FROM `country` WHERE `code` = 'AO';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Cabinda', id FROM `country` WHERE `code` = 'AO';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Cuando Cubango', id FROM `country` WHERE `code` = 'AO';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Cuanza Norte', id FROM `country` WHERE `code` = 'AO';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Cuanza Sul', id FROM `country` WHERE `code` = 'AO';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Cunene', id FROM `country` WHERE `code` = 'AO';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Huambo', id FROM `country` WHERE `code` = 'AO';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Huíla', id FROM `country` WHERE `code` = 'AO';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Luanda', id FROM `country` WHERE `code` = 'AO';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Lunda Norte', id FROM `country` WHERE `code` = 'AO';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Lunda Sul', id FROM `country` WHERE `code` = 'AO';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Malanje', id FROM `country` WHERE `code` = 'AO';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Moxico', id FROM `country` WHERE `code` = 'AO';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Namibe', id FROM `country` WHERE `code` = 'AO';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Uíge', id FROM `country` WHERE `code` = 'AO';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Zaire', id FROM `country` WHERE `code` = 'AO';

-- Insert provinces for Botswana
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Central', id FROM `country` WHERE `code` = 'BW';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Chobe', id FROM `country` WHERE `code` = 'BW';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Ghanzi', id FROM `country` WHERE `code` = 'BW';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Kgalagadi', id FROM `country` WHERE `code` = 'BW';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Kgatleng', id FROM `country` WHERE `code` = 'BW';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Kweneng', id FROM `country` WHERE `code` = 'BW';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Ngamiland', id FROM `country` WHERE `code` = 'BW';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'North-East', id FROM `country` WHERE `code` = 'BW';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'South-East', id FROM `country` WHERE `code` = 'BW';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Southern', id FROM `country` WHERE `code` = 'BW';

-- Insert provinces for Comoros
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Grande Comore (Ngazidja)', id FROM `country` WHERE `code` = 'KM';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Anjouan (Ndzuwani)', id FROM `country` WHERE `code` = 'KM';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Mohéli (Mwali)', id FROM `country` WHERE `code` = 'KM';

-- Insert provinces for Democratic Republic of the Congo
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Bas-Uele', id FROM `country` WHERE `code` = 'CD';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Équateur', id FROM `country` WHERE `code` = 'CD';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Haut-Katanga', id FROM `country` WHERE `code` = 'CD';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Haut-Lomami', id FROM `country` WHERE `code` = 'CD';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Haut-Uele', id FROM `country` WHERE `code` = 'CD';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Ituri', id FROM `country` WHERE `code` = 'CD';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Kasaï', id FROM `country` WHERE `code` = 'CD';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Kasaï Central', id FROM `country` WHERE `code` = 'CD';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Kasaï Oriental', id FROM `country` WHERE `code` = 'CD';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Kinshasa', id FROM `country` WHERE `code` = 'CD';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Kongo Central', id FROM `country` WHERE `code` = 'CD';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Kwango', id FROM `country` WHERE `code` = 'CD';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Kwilu', id FROM `country` WHERE `code` = 'CD';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Lomami', id FROM `country` WHERE `code` = 'CD';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Lualaba', id FROM `country` WHERE `code` = 'CD';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Mai-Ndombe', id FROM `country` WHERE `code` = 'CD';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Maniema', id FROM `country` WHERE `code` = 'CD';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Mongala', id FROM `country` WHERE `code` = 'CD';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Nord-Kivu', id FROM `country` WHERE `code` = 'CD';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Nord-Ubangi', id FROM `country` WHERE `code` = 'CD';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Sankuru', id FROM `country` WHERE `code` = 'CD';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Sud-Kivu', id FROM `country` WHERE `code` = 'CD';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Sud-Ubangi', id FROM `country` WHERE `code` = 'CD';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Tanganyika', id FROM `country` WHERE `code` = 'CD';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Tshopo', id FROM `country` WHERE `code` = 'CD';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Tshuapa', id FROM `country` WHERE `code` = 'CD';

-- Insert provinces for Eswatini
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Hhohho', id FROM `country` WHERE `code` = 'SZ';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Lubombo', id FROM `country` WHERE `code` = 'SZ';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Manzini', id FROM `country` WHERE `code` = 'SZ';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Shiselweni', id FROM `country` WHERE `code` = 'SZ';

-- Insert provinces for Lesotho
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Berea', id FROM `country` WHERE `code` = 'LS';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Butha-Buthe', id FROM `country` WHERE `code` = 'LS';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Leribe', id FROM `country` WHERE `code` = 'LS';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Mafeteng', id FROM `country` WHERE `code` = 'LS';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Maseru', id FROM `country` WHERE `code` = 'LS';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Mohale''s Hoek', id FROM `country` WHERE `code` = 'LS';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Mokhotlong', id FROM `country` WHERE `code` = 'LS';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Qacha''s Nek', id FROM `country` WHERE `code` = 'LS';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Quthing', id FROM `country` WHERE `code` = 'LS';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Thaba-Tseka', id FROM `country` WHERE `code` = 'LS';

-- Insert provinces for Madagascar
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Alaotra-Mangoro', id FROM `country` WHERE `code` = 'MG';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Amoron''i Mania', id FROM `country` WHERE `code` = 'MG';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Analamanga', id FROM `country` WHERE `code` = 'MG';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Analanjirofo', id FROM `country` WHERE `code` = 'MG';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Androy', id FROM `country` WHERE `code` = 'MG';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Anosy', id FROM `country` WHERE `code` = 'MG';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Atsimo-Andrefana', id FROM `country` WHERE `code` = 'MG';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Atsimo-Atsinanana', id FROM `country` WHERE `code` = 'MG';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Atsinanana', id FROM `country` WHERE `code` = 'MG';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Betsiboka', id FROM `country` WHERE `code` = 'MG';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Boeny', id FROM `country` WHERE `code` = 'MG';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Bongolava', id FROM `country` WHERE `code` = 'MG';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Diana', id FROM `country` WHERE `code` = 'MG';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Haute Matsiatra', id FROM `country` WHERE `code` = 'MG';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Ihorombe', id FROM `country` WHERE `code` = 'MG';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Itasy', id FROM `country` WHERE `code` = 'MG';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Melaky', id FROM `country` WHERE `code` = 'MG';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Menabe', id FROM `country` WHERE `code` = 'MG';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Sava', id FROM `country` WHERE `code` = 'MG';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Sofia', id FROM `country` WHERE `code` = 'MG';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Vakinankaratra', id FROM `country` WHERE `code` = 'MG';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Vatovavy', id FROM `country` WHERE `code` = 'MG';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Fitovinany', id FROM `country` WHERE `code` = 'MG';

-- Insert provinces for Malawi
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Chitipa', id FROM `country` WHERE `code` = 'MW';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Karonga', id FROM `country` WHERE `code` = 'MW';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Mzimba', id FROM `country` WHERE `code` = 'MW';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Nkhata Bay', id FROM `country` WHERE `code` = 'MW';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Likoma', id FROM `country` WHERE `code` = 'MW';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Rumphi', id FROM `country` WHERE `code` = 'MW';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Kasungu', id FROM `country` WHERE `code` = 'MW';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Lilongwe', id FROM `country` WHERE `code` = 'MW';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Mchinji', id FROM `country` WHERE `code` = 'MW';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Nkhotakota', id FROM `country` WHERE `code` = 'MW';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Ntchisi', id FROM `country` WHERE `code` = 'MW';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Dowa', id FROM `country` WHERE `code` = 'MW';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Salima', id FROM `country` WHERE `code` = 'MW';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Dedza', id FROM `country` WHERE `code` = 'MW';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Blantyre', id FROM `country` WHERE `code` = 'MW';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Chikwawa', id FROM `country` WHERE `code` = 'MW';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Chiradzulu', id FROM `country` WHERE `code` = 'MW';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Machinga', id FROM `country` WHERE `code` = 'MW';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Mangochi', id FROM `country` WHERE `code` = 'MW';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Mulanje', id FROM `country` WHERE `code` = 'MW';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Mwanza', id FROM `country` WHERE `code` = 'MW';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Neno', id FROM `country` WHERE `code` = 'MW';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Nsanje', id FROM `country` WHERE `code` = 'MW';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Phalombe', id FROM `country` WHERE `code` = 'MW';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Thyolo', id FROM `country` WHERE `code` = 'MW';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Zomba', id FROM `country` WHERE `code` = 'MW';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Balaka', id FROM `country` WHERE `code` = 'MW';

-- Insert provinces for Mauritius
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Black River', id FROM `country` WHERE `code` = 'MU';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Flacq', id FROM `country` WHERE `code` = 'MU';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Grand Port', id FROM `country` WHERE `code` = 'MU';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Moka', id FROM `country` WHERE `code` = 'MU';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Pamplemousses', id FROM `country` WHERE `code` = 'MU';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Plaines Wilhems', id FROM `country` WHERE `code` = 'MU';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Port Louis (City)', id FROM `country` WHERE `code` = 'MU';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Rivière du Rempart', id FROM `country` WHERE `code` = 'MU';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Savanne', id FROM `country` WHERE `code` = 'MU';

-- Insert provinces for Mozambique
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Cabo Delgado', id FROM `country` WHERE `code` = 'MZ';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Gaza', id FROM `country` WHERE `code` = 'MZ';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Inhambane', id FROM `country` WHERE `code` = 'MZ';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Manica', id FROM `country` WHERE `code` = 'MZ';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Maputo Province', id FROM `country` WHERE `code` = 'MZ';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Nampula', id FROM `country` WHERE `code` = 'MZ';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Niassa', id FROM `country` WHERE `code` = 'MZ';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Sofala', id FROM `country` WHERE `code` = 'MZ';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Tete', id FROM `country` WHERE `code` = 'MZ';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Zambézia', id FROM `country` WHERE `code` = 'MZ';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Maputo City', id FROM `country` WHERE `code` = 'MZ';

-- Insert provinces for Namibia
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Erongo', id FROM `country` WHERE `code` = 'NA';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Hardap', id FROM `country` WHERE `code` = 'NA';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'ǁKaras', id FROM `country` WHERE `code` = 'NA';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Kavango East', id FROM `country` WHERE `code` = 'NA';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Kavango West', id FROM `country` WHERE `code` = 'NA';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Khomas', id FROM `country` WHERE `code` = 'NA';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Kunene', id FROM `country` WHERE `code` = 'NA';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Ohangwena', id FROM `country` WHERE `code` = 'NA';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Omaheke', id FROM `country` WHERE `code` = 'NA';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Omusati', id FROM `country` WHERE `code` = 'NA';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Oshana', id FROM `country` WHERE `code` = 'NA';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Oshikoto', id FROM `country` WHERE `code` = 'NA';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Otjozondjupa', id FROM `country` WHERE `code` = 'NA';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Zambezi', id FROM `country` WHERE `code` = 'NA';

-- Insert provinces for Seychelles
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Anse aux Pins', id FROM `country` WHERE `code` = 'SC';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Anse Boileau', id FROM `country` WHERE `code` = 'SC';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Anse Etoile', id FROM `country` WHERE `code` = 'SC';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Anse Royale', id FROM `country` WHERE `code` = 'SC';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Au Cap', id FROM `country` WHERE `code` = 'SC';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Baie Lazare', id FROM `country` WHERE `code` = 'SC';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Baie Sainte Anne', id FROM `country` WHERE `code` = 'SC';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Beau Vallon', id FROM `country` WHERE `code` = 'SC';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Bel Air', id FROM `country` WHERE `code` = 'SC';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Bel Ombre', id FROM `country` WHERE `code` = 'SC';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Cascade', id FROM `country` WHERE `code` = 'SC';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Glacis', id FROM `country` WHERE `code` = 'SC';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Grand Anse Mahé', id FROM `country` WHERE `code` = 'SC';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Grand Anse Praslin', id FROM `country` WHERE `code` = 'SC';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'La Digue and Inner Islands', id FROM `country` WHERE `code` = 'SC';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'La Rivière Anglaise', id FROM `country` WHERE `code` = 'SC';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Les Mamelles', id FROM `country` WHERE `code` = 'SC';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Mont Buxton', id FROM `country` WHERE `code` = 'SC';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Mont Fleuri', id FROM `country` WHERE `code` = 'SC';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Plaisance', id FROM `country` WHERE `code` = 'SC';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Pointe La Rue', id FROM `country` WHERE `code` = 'SC';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Port Glaud', id FROM `country` WHERE `code` = 'SC';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Roche Caïman', id FROM `country` WHERE `code` = 'SC';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Saint Louis', id FROM `country` WHERE `code` = 'SC';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Takamaka', id FROM `country` WHERE `code` = 'SC';

-- Insert provinces for South Africa
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Eastern Cape', id FROM `country` WHERE `code` = 'ZA';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Free State', id FROM `country` WHERE `code` = 'ZA';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Gauteng', id FROM `country` WHERE `code` = 'ZA';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'KwaZulu-Natal', id FROM `country` WHERE `code` = 'ZA';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Limpopo', id FROM `country` WHERE `code` = 'ZA';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Mpumalanga', id FROM `country` WHERE `code` = 'ZA';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Northern Cape', id FROM `country` WHERE `code` = 'ZA';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'North West', id FROM `country` WHERE `code` = 'ZA';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Western Cape', id FROM `country` WHERE `code` = 'ZA';

-- Insert provinces for Tanzania
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Arusha', id FROM `country` WHERE `code` = 'TZ';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Dar es Salaam', id FROM `country` WHERE `code` = 'TZ';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Dodoma', id FROM `country` WHERE `code` = 'TZ';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Geita', id FROM `country` WHERE `code` = 'TZ';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Iringa', id FROM `country` WHERE `code` = 'TZ';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Kagera', id FROM `country` WHERE `code` = 'TZ';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Katavi', id FROM `country` WHERE `code` = 'TZ';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Kigoma', id FROM `country` WHERE `code` = 'TZ';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Kilimanjaro', id FROM `country` WHERE `code` = 'TZ';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Lindi', id FROM `country` WHERE `code` = 'TZ';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Manyara', id FROM `country` WHERE `code` = 'TZ';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Mara', id FROM `country` WHERE `code` = 'TZ';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Mbeya', id FROM `country` WHERE `code` = 'TZ';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Morogoro', id FROM `country` WHERE `code` = 'TZ';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Mtwara', id FROM `country` WHERE `code` = 'TZ';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Mwanza', id FROM `country` WHERE `code` = 'TZ';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Njombe', id FROM `country` WHERE `code` = 'TZ';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Pemba North', id FROM `country` WHERE `code` = 'TZ';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Pemba South', id FROM `country` WHERE `code` = 'TZ';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Pwani', id FROM `country` WHERE `code` = 'TZ';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Rukwa', id FROM `country` WHERE `code` = 'TZ';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Ruvuma', id FROM `country` WHERE `code` = 'TZ';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Shinyanga', id FROM `country` WHERE `code` = 'TZ';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Simiyu', id FROM `country` WHERE `code` = 'TZ';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Singida', id FROM `country` WHERE `code` = 'TZ';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Songwe', id FROM `country` WHERE `code` = 'TZ';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Tabora', id FROM `country` WHERE `code` = 'TZ';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Tanga', id FROM `country` WHERE `code` = 'TZ';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Unguja North', id FROM `country` WHERE `code` = 'TZ';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Unguja South', id FROM `country` WHERE `code` = 'TZ';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Unguja West', id FROM `country` WHERE `code` = 'TZ';

-- Insert provinces for Zambia
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Central', id FROM `country` WHERE `code` = 'ZM';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Copperbelt', id FROM `country` WHERE `code` = 'ZM';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Eastern', id FROM `country` WHERE `code` = 'ZM';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Luapula', id FROM `country` WHERE `code` = 'ZM';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Lusaka', id FROM `country` WHERE `code` = 'ZM';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Muchinga', id FROM `country` WHERE `code` = 'ZM';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Northern', id FROM `country` WHERE `code` = 'ZM';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'North-Western', id FROM `country` WHERE `code` = 'ZM';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Southern', id FROM `country` WHERE `code` = 'ZM';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Western', id FROM `country` WHERE `code` = 'ZM';

-- Insert provinces for Zimbabwe
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Bulawayo', id FROM `country` WHERE `code` = 'ZW';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Harare', id FROM `country` WHERE `code` = 'ZW';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Manicaland', id FROM `country` WHERE `code` = 'ZW';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Mashonaland Central', id FROM `country` WHERE `code` = 'ZW';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Mashonaland East', id FROM `country` WHERE `code` = 'ZW';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Mashonaland West', id FROM `country` WHERE `code` = 'ZW';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Masvingo', id FROM `country` WHERE `code` = 'ZW';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Matabeleland North', id FROM `country` WHERE `code` = 'ZW';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Matabeleland South', id FROM `country` WHERE `code` = 'ZW';
INSERT INTO `province` (`name`, `country_id`) 
SELECT 'Midlands', id FROM `country` WHERE `code` = 'ZW';