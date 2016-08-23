--
-- Table structure for table `contact_info`
--

CREATE TABLE `contact_info` (
  `id` bigint(20) NOT NULL,
  `address1` varchar(255) DEFAULT NULL,
  `address2` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `website` varchar(255) DEFAULT NULL,
  `zip` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `family_member`
--

CREATE TABLE `family_member` (
  `id` bigint(20) NOT NULL,
  `family_member_name` varchar(255) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `fsa`
--

CREATE TABLE `fsa` (
  `id` bigint(20) NOT NULL,
  `amount_in_fsa` double NOT NULL,
  `fsa_end_date` datetime DEFAULT NULL,
  `fsa_name` varchar(255) NOT NULL,
  `fsa_start_date` datetime DEFAULT NULL,
  `fsa_year` int(11) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `medical_expense`
--

CREATE TABLE `medical_expense` (
  `id` bigint(20) NOT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `copay_amount` double NOT NULL,
  `cost_according_to_provider` double NOT NULL,
  `date` datetime DEFAULT NULL,
  `deductible_amount` double NOT NULL,
  `maximum_amount` double NOT NULL,
  `medical_expense_in_plan` bit(1) NOT NULL,
  `medical_expense_type` int(11) DEFAULT NULL,
  `out_of_pocket_amount` double NOT NULL,
  `payment_amount` double NOT NULL,
  `prescription_tier_type` int(11) DEFAULT NULL,
  `family_member_id` bigint(20) DEFAULT NULL,
  `plan_id` bigint(20) DEFAULT NULL,
  `provider_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `plan`
--

CREATE TABLE `plan` (
  `id` bigint(20) NOT NULL,
  `active_plan` bit(1) NOT NULL,
  `emergency_room_copay` double NOT NULL,
  `family_in_network_deductible` double NOT NULL,
  `family_out_of_network_deductible` double NOT NULL,
  `family_out_of_pocket_limit` double NOT NULL,
  `individual_in_network_deductible` double NOT NULL,
  `individual_out_of_network_deductible` double NOT NULL,
  `individual_out_of_pocket_limit` double NOT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `plan_end_date` datetime DEFAULT NULL,
  `plan_name` varchar(50) NOT NULL,
  `plan_start_date` datetime DEFAULT NULL,
  `plan_type` int(11) NOT NULL,
  `plan_year` int(11) NOT NULL,
  `primary_care_copay` double NOT NULL,
  `specialist_copay` double NOT NULL,
  `tier1prescription_copay` double NOT NULL,
  `tier2prescription_copay` double NOT NULL,
  `tier3prescription_copay` double NOT NULL,
  `user_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `provider`
--

CREATE TABLE `provider` (
  `id` bigint(20) NOT NULL,
  `provider_id` varchar(255) DEFAULT NULL,
  `provider_in_plan` bit(1) NOT NULL,
  `provider_name` varchar(255) NOT NULL,
  `provider_type` int(11) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `provider_locations`
--

CREATE TABLE `provider_locations` (
  `provider_id` bigint(20) NOT NULL,
  `locations_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `account_non_expired` bit(1) NOT NULL,
  `account_non_locked` bit(1) NOT NULL,
  `credentials_non_expired` bit(1) NOT NULL,
  `enabled` bit(1) NOT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `user_user_roles`
--

CREATE TABLE `user_user_roles` (
  `user_id` bigint(20) NOT NULL,
  `user_roles` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `contact_info`
--
ALTER TABLE `contact_info`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `family_member`
--
ALTER TABLE `family_member`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKpfs5mceej35oh4n89cn4v1tmn` (`user_id`);

--
-- Indexes for table `fsa`
--
ALTER TABLE `fsa`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKfiwgkiycrpj0qxy043ijqsvjd` (`user_id`);

--
-- Indexes for table `medical_expense`
--
ALTER TABLE `medical_expense`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKl2374yi5k81i71pw7grvr5b5s` (`family_member_id`),
  ADD KEY `FKg8bemr2ruckhjppscmwgtabij` (`plan_id`),
  ADD KEY `FK4p12kmaj6nd217mcjvydm1m22` (`provider_id`),
  ADD KEY `FKn47oc8wnb442iu73s925rxgwp` (`user_id`);

--
-- Indexes for table `plan`
--
ALTER TABLE `plan`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK271ok4ss5pcte25w6o3hvv60x` (`user_id`);

--
-- Indexes for table `provider`
--
ALTER TABLE `provider`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKnfhwmlym56ylscdvkxofry87w` (`user_id`);

--
-- Indexes for table `provider_locations`
--
ALTER TABLE `provider_locations`
  ADD UNIQUE KEY `UK_59011c7q5x5rcn0ath0rb9s6c` (`locations_id`),
  ADD KEY `FK5kvarjvipj0k7mknh1b5o8gpc` (`provider_id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_sb8bbouer5wak8vyiiy4pf2bx` (`username`);

--
-- Indexes for table `user_user_roles`
--
ALTER TABLE `user_user_roles`
  ADD KEY `FKhxmdhadm01kwqo2lvyv8l8ho7` (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `contact_info`
--
ALTER TABLE `contact_info`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `family_member`
--
ALTER TABLE `family_member`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `fsa`
--
ALTER TABLE `fsa`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `medical_expense`
--
ALTER TABLE `medical_expense`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `plan`
--
ALTER TABLE `plan`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `provider`
--
ALTER TABLE `provider`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `family_member`
--
ALTER TABLE `family_member`
  ADD CONSTRAINT `FKpfs5mceej35oh4n89cn4v1tmn` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Constraints for table `fsa`
--
ALTER TABLE `fsa`
  ADD CONSTRAINT `FKfiwgkiycrpj0qxy043ijqsvjd` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Constraints for table `medical_expense`
--
ALTER TABLE `medical_expense`
  ADD CONSTRAINT `FK4p12kmaj6nd217mcjvydm1m22` FOREIGN KEY (`provider_id`) REFERENCES `provider` (`id`),
  ADD CONSTRAINT `FKg8bemr2ruckhjppscmwgtabij` FOREIGN KEY (`plan_id`) REFERENCES `plan` (`id`),
  ADD CONSTRAINT `FKl2374yi5k81i71pw7grvr5b5s` FOREIGN KEY (`family_member_id`) REFERENCES `family_member` (`id`),
  ADD CONSTRAINT `FKn47oc8wnb442iu73s925rxgwp` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Constraints for table `plan`
--
ALTER TABLE `plan`
  ADD CONSTRAINT `FK271ok4ss5pcte25w6o3hvv60x` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Constraints for table `provider`
--
ALTER TABLE `provider`
  ADD CONSTRAINT `FKnfhwmlym56ylscdvkxofry87w` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Constraints for table `provider_locations`
--
ALTER TABLE `provider_locations`
  ADD CONSTRAINT `FK1hsnaacxqtclokbktmc7m6vik` FOREIGN KEY (`locations_id`) REFERENCES `contact_info` (`id`),
  ADD CONSTRAINT `FK5kvarjvipj0k7mknh1b5o8gpc` FOREIGN KEY (`provider_id`) REFERENCES `provider` (`id`);

--
-- Constraints for table `user_user_roles`
--
ALTER TABLE `user_user_roles`
  ADD CONSTRAINT `FKhxmdhadm01kwqo2lvyv8l8ho7` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);
