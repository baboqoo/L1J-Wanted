CREATE TABLE `uml_conversion` (
  `oldname` varchar(45) NOT NULL,
  `newname` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `uml_conversion`
  ADD PRIMARY KEY (`oldname`) USING BTREE;
COMMIT;
