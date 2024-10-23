/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  hieun
 * Created: Oct 5, 2024
 */
CREATE TABLE `btl_nhom1`.`questions` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `questionText` TEXT NOT NULL,
  `imageLink` TEXT NOT NULL,
  `answer` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`)
);

