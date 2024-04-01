package com.example.quran.services;

import com.example.quran.model.ERole;
import com.example.quran.model.Role;
import com.example.quran.model.Users;
import com.example.quran.repository.RoleRepository;
import com.example.quran.repository.UsersRepository;
import com.example.quran.response.DetailRoleResponse;
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
import java.util.*;

@Service
@Slf4j
public class UserService {
    @Autowired
    UsersRepository usersRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;


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

    public List<DetailRoleResponse> getTeacherRole(){
        ERole roleTeacher = ERole.ROLE_TEACHER;
        Optional<Role> roleOptional = roleRepository.findByName(roleTeacher);

        if (roleOptional.isPresent()) { // Periksa apakah role ditemukan
            Role role = roleOptional.get(); // Dapatkan nilai dari Optional<Role>

            // Dapatkan daftar pengguna dengan peran yang sesuai
            List<Users> usersWithRole = usersRepository.findByRolesName(role.getName());

            // Buat daftar DetailRoleResponse untuk diisi
            List<DetailRoleResponse> detailRoles = new ArrayList<>();

            // Isi detailRoles dengan informasi pengguna
            for (Users user : usersWithRole) {
                DetailRoleResponse detailRoleResponse = new DetailRoleResponse();
                detailRoleResponse.setId(user.getId().toString());
                detailRoleResponse.setUsername(user.getUsername());
                detailRoleResponse.setEmail(user.getEmail());
                detailRoleResponse.setPhotoPath(user.getPhotoPath());
                detailRoleResponse.setRoles(convertSetToList(user.getRoles()));

                detailRoles.add(detailRoleResponse);
            }

            return detailRoles;
        } else {
            // Lakukan penanganan jika role tidak ditemukan, misalnya lempar pengecualian atau kembalikan daftar kosong
            return Collections.emptyList();
        }
    }

    private List<String> convertSetToList(Set<Role> rolesSet) {
        List<String> rolesList = new ArrayList<>();
        for (Role role : rolesSet) {
            rolesList.add(role.getName().name());
        }
        return rolesList;
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
