/**
 * Author:  vanh <your.name at your.org>
 * Created: Oct 23, 2024
 */

create database btl_nhom1;

CREATE TABLE `btl_nhom1`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL,
  `password` VARCHAR(50) NOT NULL,
  `full_name` VARCHAR(50) NOT NULL,
  `total_score` FLOAT NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `btl_nhom1`.`questions` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `questionText` TEXT NOT NULL,
  `imageLink` TEXT NOT NULL,
  `answer` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `btl_nhom1`.`results` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `userId1` VARCHAR(20) NOT NULL,
  `userId2` VARCHAR(20) NOT NULL,
  `UserWin` ENUM('USER1', 'USER2', 'HOA') NOT NULL,
  PRIMARY KEY (`id`)
);

INSERT INTO btl_nhom1.questions(questionText, imageLink, answer) VALUES
('Loài vật này có cái vòi dài và đôi tai lớn, sống ở châu Phi.', 'https://hoanghamobile.com/tin-tuc/wp-content/uploads/2024/06/hinh-anh-con-voi-6.jpg', 'VOI'),
('Trái cây này có màu vàng, hình tròn, thường ăn vào mùa hè.', 'https://suckhoedoisong.qltns.mediacdn.vn/324455921873985536/2021/10/14/chuoi1-16341869574602070184903.jpg', 'CHUOI'),
('Phương tiện giao thông này có nhiều bánh xe, thường chở hàng nặng.', 'https://thegioixetai.com/storage/anh-dai-dien/ISUZU/QKR230/xe-tai-Isuzu-QKR230-thung-bat-bung-nhom.jpg', 'XE TAI'),
('Môn thể thao này sử dụng quả bóng tròn, có 11 cầu thủ mỗi đội.', 'https://danviet.mediacdn.vn/upload/1-2015/images/2015-01-27/1434357667-hlyy15_ghbd.jpg', 'BONG DA'),
('Cây này có hoa màu hồng rất đẹp, thường trồng làm cảnh.', 'https://sasaki.com.vn/wp-content/uploads/2024/06/cach-trong-hoa-hong-tu-canh-01.jpg', 'HOA HONG'),
('Nhạc cụ này có 6 dây, thường được dùng để đệm hát.', 'https://vietthuong.edu.vn/wp-content/uploads/2017/05/Huong-dan-choi-dan-Guitar-Acoustic-1.jpg', 'GUITAR'),
('Loại rau này có thể ăn sống hoặc nấu chín.', 'https://suckhoedoisong.qltns.mediacdn.vn/2015/3-tac-dung-khong-phai-ai-cung-biet-cua-rau-xa-lach-1435115946775.jpg', 'XA LACH'),
('Món ăn này được làm từ gạo, thường ăn kèm với các loại thức ăn khác.', 'https://xabuon.com/uploads1/news/10-04-23/xabuon-girl-xinh-haivl-xemvn-sex-10-04-2023168109632752.jpg', 'COM'),
('Nước ngọt có ga này thường có màu nâu, có vị ngọt.', 'https://www.shutterstock.com/image-photo/tbilisi-georgia-may-29-2024-600nw-2468450965.jpg', 'COCA-COLA'),
('Cơ quan này có nhiệm vụ bảo vệ đất nước.', 'https://file1.dangcongsan.vn/data/0/images/2021/12/29/phuongdt/qdnd.jpg', 'QUAN DOI');

-- Thêm dòng này vào cuối file hoặc sau phần tạo bảng users

ALTER TABLE `btl_nhom1`.`users` 
ADD COLUMN `status` ENUM('Online', 'Offline', 'Ingame') NOT NULL DEFAULT 'Offline';
