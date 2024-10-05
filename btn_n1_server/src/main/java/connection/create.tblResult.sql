/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  hieun
 * Created: Oct 5, 2024
 */
CREATE TABLE `btlltm`.`results` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `userId1` VARCHAR(20) NOT NULL,
  `userId2` VARCHAR(20) NOT NULL,
  `UserWin` ENUM('USER1', 'USER2', 'HOA') NOT NULL,
  PRIMARY KEY (`id`)
);

