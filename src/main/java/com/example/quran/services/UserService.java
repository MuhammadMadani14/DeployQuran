package com.example.quran.services;

import com.example.quran.model.Users;
import com.example.quran.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Service
@Slf4j
public class UserService {
    @Autowired
    UsersRepository usersRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;


    public Optional<Users> getUserById(Long id){
        log.info("Get Data User By Id Succses!");
        return usersRepository.findById(id);
    }

    public Users getUserByEmail(String email){
        return usersRepository.findByEmail1(email);
    }

    public void uploadPhoto(Long id, MultipartFile file) throws Exception {
        Users users = usersRepository.findById(id)
                .orElseThrow();

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            String uploadDir = "user-photos/" + id;
            String filePath = uploadDir + "/" + fileName;
            Path storagePath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(storagePath);
            Files.copy(file.getInputStream(), storagePath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

            users.setPhotoPath(filePath);
            usersRepository.save(users);
        }catch (IOException e){
            throw new Exception("Could not store file " + fileName + ". Please try again!", e);
        }


    }
    public void changePhoto(Long id, MultipartFile file) throws Exception{
        uploadPhoto(id, file);
    }


    public boolean changeUserPassword(String email, String oldPassword, String newPassword) {
        Optional<Users> optionalUser = usersRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            if (passwordEncoder.matches(oldPassword, user.getPassword())) {
                user.setPassword(passwordEncoder.encode(newPassword));
                usersRepository.save(user);
                return true;
            }
        }
        return false;
    }

//    public boolean isTruePassword(String email, String password){
//        Users users = usersRepository.findByEmail1(email);
//
//        if(users != null){
//            boolean check = passwordEncoder.matches(password, users.getPassword());
//            return check;
//        }else {
//            return false;
//        }
//    }
//
//    public int changeUserPassword(UserDTO pDTO, String newPassword){
//        String email = pDTO.getEmail();
//        String passwordUser = pDTO.getPassword();
//
//        boolean checkPassword = isTruePassword(email, passwordUser);
//
//        if(checkPassword){
//            usersRepository.changeUserPassword(passwordEncoder.encode(newPassword));
//
//            return 1;
//        }
//        return 0;
//    }
}
