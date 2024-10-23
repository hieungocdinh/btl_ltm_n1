/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  hieun
 * Created: Oct 5, 2024
 */

CREATE TABLE `btl_nhom1`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL,
  `password` VARCHAR(50) NOT NULL,
  `full_name` VARCHAR(50) NOT NULL,
  `total_score` FLOAT NOT NULL,
  PRIMARY KEY (`id`)
);
