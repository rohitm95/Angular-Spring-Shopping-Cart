/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role`(id,name) VALUES (1,'Admin'),(2,'Customer'),(3,'Aggregator');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;

/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user`(id,first_name,last_name,email_id,mobile_number,gender,is_active,role_id,username,password) VALUES (1,'Inventory','','admin@admin.com','1234567890','Male',1,1,'admin','$2a$10$QdAMkO5/.sfyk7WURV5emeIwtF7OlPNXQDLJXgV4Qno0WfBp4K6rS'),(2,'mayank','gupta','mayank@email.com','1234567891','Male',1,2,'mayank','$2a$10$Mys6EKVRxcYuhz8/03eiWOvPgnyLMxVmJH4pVUFxfi/6S3DRNfY4i'),(3,'Inventory','','aggr@admin.com','1234567892','Male',1,3,'aggregator','$2a$10$QdAMkO5/.sfyk7WURV5emeIwtF7OlPNXQDLJXgV4Qno0WfBp4K6rS');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;

/*!40000 ALTER TABLE `store` DISABLE KEYS */;
INSERT INTO `store` (`id`,`delivery_in_slot` ,`is_active`, `slot_duration` ,`store_name`) VALUES ('1','2' ,'1','30' ,'Kamal');
/*!40000 ALTER TABLE `store` ENABLE KEYS */;

/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` (`order_no`, `amount_payable`, `delivery_date`, `is_active`, `slot_from`, `slot_to`, `status`, `customer_id`, `store_id`) VALUES ('25', '200', '2020-04-11 19:22:26', '1', '9:00', '9:30', 'COMPLETED', '2', '1');
INSERT INTO `orders` (`order_no`, `amount_payable`, `delivery_date`, `is_active`, `slot_from`, `slot_to`, `status`, `customer_id`, `store_id`) VALUES ('2501', '1550', '2020-04-11 19:22:26', '1', '9:00', '9:30', 'COMPLETED', '2', '1');
INSERT INTO `orders` (`order_no`, `amount_payable`, `delivery_date`, `is_active`, `slot_from`, `slot_to`, `status`, `customer_id`, `store_id`) VALUES ('2547', '550', '2020-04-11 19:22:26', '1', '10:00', '10:30', 'READY_TO_DELIVER', '2', '1');
INSERT INTO `orders` (`order_no`, `amount_payable`, `delivery_date`, `is_active`, `slot_from`, `slot_to`, `status`, `customer_id`, `store_id`) VALUES ('3578', '150', '2020-04-11 19:22:26', '1', '11:30', '12:00', 'LAPSED', '2', '1');
INSERT INTO `orders` (`order_no`, `amount_payable`, `delivery_date`, `is_active`, `slot_from`, `slot_to`, `status`, `customer_id`, `store_id`) VALUES ('5478', '1200', '2020-04-11 19:22:26', '1', '12:00', '12:30', 'INITIATED', '2', '1');
INSERT INTO `orders` (`order_no`, `amount_payable`, `delivery_date`, `is_active`, `slot_from`, `slot_to`, `status`, `customer_id`, `store_id`) VALUES ('5487', '111', '2020-04-11 19:22:26', '1', '1:30', '2:00', 'INITIATED', '2', '1');
INSERT INTO `orders` (`order_no`, `amount_payable`, `delivery_date`, `is_active`, `slot_from`, `slot_to`, `status`, `customer_id`, `store_id`) VALUES ('6578', '1140', '2020-04-11 19:22:26', '1', '2:30', '3:00', 'INITIATED', '2', '1');
INSERT INTO `orders` (`order_no`, `amount_payable`, `delivery_date`, `is_active`, `slot_from`, `slot_to`, `status`, `customer_id`, `store_id`) VALUES ('7587', '5999', '2020-04-11 19:22:26', '1', '3:00', '3:30', 'INITIATED', '2', '1');
INSERT INTO `orders` (`order_no`, `amount_payable`, `delivery_date`, `is_active`, `slot_from`, `slot_to`, `status`, `customer_id`, `store_id`) VALUES ('7748', '547', '2020-04-11 19:22:26', '1', '3:00', '3:30', 'INITIATED', '2', '1');
INSERT INTO `orders` (`order_no`, `amount_payable`, `delivery_date`, `is_active`, `slot_from`, `slot_to`, `status`, `customer_id`, `store_id`) VALUES ('8888', '1456', '2020-04-11 19:22:26', '1', '3:00', '3:30', 'INITIATED', '2', '1');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;


/*!40000 ALTER TABLE `order_details` DISABLE KEYS */;
INSERT INTO `order_details` (`id`, `amount`, `is_active`, `item_name`, `order_no`, `price`, `quantity`, `item_number`) VALUES ('1', '100', '1', 'ROOM FRESHNER 160 gm', '6578', '100', '1', '013029T');
INSERT INTO `order_details` (`id`, `amount`, `is_active`, `item_name`, `order_no`, `price`, `quantity`, `item_number`) VALUES ('2', '1000', '1', 'USHA 5LTR COOKER', '6578', '1000', '1', '014102K');
INSERT INTO `order_details` (`id`, `amount`, `is_active`, `item_name`, `order_no`, `price`, `quantity`, `item_number`) VALUES ('3', '100', '1', 'CHERRY POLISH BLACK', '6578', '50', '2', '028015T');
INSERT INTO `order_details` (`id`, `amount`, `is_active`, `item_name`, `order_no`, `price`, `quantity`, `item_number`) VALUES ('4', '1500', '1', 'PHILIPS MIXER GRINDER', '6578', '1500', '1', '021135L');

INSERT INTO `order_details` (`id`, `amount`, `is_active`, `item_name`, `order_no`, `price`, `quantity`, `item_number`) VALUES ('5', '1500', '1', 'PHILIPS MIXER GRINDER', '25', '1500', '1', '021135L');
INSERT INTO `order_details` (`id`, `amount`, `is_active`, `item_name`, `order_no`, `price`, `quantity`, `item_number`) VALUES ('6', '1500', '1', 'USHA 5LTR COOKER', '25', '1500', '1', '014102K');

INSERT INTO `order_details` (`id`, `amount`, `is_active`, `item_name`, `order_no`, `price`, `quantity`, `item_number`) VALUES ('7', '1500', '1', 'PHILIPS MIXER GRINDER', '2501', '1500', '1', '021135L');
INSERT INTO `order_details` (`id`, `amount`, `is_active`, `item_name`, `order_no`, `price`, `quantity`, `item_number`) VALUES ('8', '1500', '1', 'USHA 5LTR COOKER', '2501', '1500', '1', '014102K');

INSERT INTO `order_details` (`id`, `amount`, `is_active`, `item_name`, `order_no`, `price`, `quantity`, `item_number`) VALUES ('9', '1500', '1', 'PHILIPS MIXER GRINDER', '2547', '1500', '1', '021135L');
INSERT INTO `order_details` (`id`, `amount`, `is_active`, `item_name`, `order_no`, `price`, `quantity`, `item_number`) VALUES ('10', '1500', '1', 'USHA 5LTR COOKER', '2547', '1500', '1', '014102K');

INSERT INTO `order_details` (`id`, `amount`, `is_active`, `item_name`, `order_no`, `price`, `quantity`, `item_number`) VALUES ('11', '1500', '1', 'PHILIPS MIXER GRINDER', '3578', '1500', '1', '021135L');

INSERT INTO `order_details` (`id`, `amount`, `is_active`, `item_name`, `order_no`, `price`, `quantity`, `item_number`) VALUES ('12', '1500', '1', 'PHILIPS MIXER GRINDER', '5478', '1500', '1', '021135L');

INSERT INTO `order_details` (`id`, `amount`, `is_active`, `item_name`, `order_no`, `price`, `quantity`, `item_number`) VALUES ('13', '1500', '1', 'PHILIPS MIXER GRINDER', '5487', '1500', '1', '021135L');

INSERT INTO `order_details` (`id`, `amount`, `is_active`, `item_name`, `order_no`, `price`, `quantity`, `item_number`) VALUES ('14', '1500', '1', 'PHILIPS MIXER GRINDER', '7587', '1500', '1', '021135L');

INSERT INTO `order_details` (`id`, `amount`, `is_active`, `item_name`, `order_no`, `price`, `quantity`, `item_number`) VALUES ('15', '1500', '1', 'PHILIPS MIXER GRINDER', '7748', '1500', '1', '021135L');

INSERT INTO `order_details` (`id`, `amount`, `is_active`, `item_name`, `order_no`, `price`, `quantity`, `item_number`) VALUES ('16', '1500', '1', 'PHILIPS MIXER GRINDER', '8888', '1500', '1', '021135L');

/*!40000 ALTER TABLE `order_details` ENABLE KEYS */;