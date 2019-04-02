--
-- Table structure for organisations
--

DROP TABLE IF EXISTS `organisation`;

CREATE TABLE `organisation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `bic` varchar(9) DEFAULT NULL,
  `rn` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--
-- Table structure for forms101
--

DROP TABLE IF EXISTS `dates_of_forms101`;

CREATE TABLE `dates_of_forms101` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `organisation_id` int(11) DEFAULT NULL,
  `form_date` datetime DEFAULT NULL,
  `pln` varchar(1) DEFAULT NULL,
  `ap` varchar(2) DEFAULT NULL,
  `vr` varchar(20) DEFAULT NULL,
  `vv` varchar(20) DEFAULT NULL,
  `vitg` varchar(20) DEFAULT NULL,
  `ora` varchar(20) DEFAULT NULL,
  `ova` varchar(20) DEFAULT NULL,
  `oitga` varchar(20) DEFAULT NULL,
  `orp` varchar(20) DEFAULT NULL,
  `ovp` varchar(20) DEFAULT NULL,
  `oitgp` varchar(20) DEFAULT NULL,
  `ir` varchar(20) DEFAULT NULL,
  `iv` varchar(20) DEFAULT NULL,
  `iitg` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;



