package com.example.quran.services;

import com.example.quran.data.RoleData;
import com.example.quran.model.ERole;
import com.example.quran.model.Role;
import com.example.quran.model.Users;
import com.example.quran.repository.RoleRepository;
import com.example.quran.repository.UsersRepository;
import com.example.quran.response.DetailRoleResponse;
import com.example.quran.response.MessageResponse;
import com.example.quran.response.RoleResponse;
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

    public DetailRoleResponse getTeacherRoleDetail(Long userId){
        Users teacher = usersRepository.findById(userId).orElse(null);
        if (teacher == null) {
            return null;
        }
        DetailRoleResponse teacherDetail = new DetailRoleResponse();
        MessageResponse messageResponse = new MessageResponse(false, "Success");
        RoleData roleData = new RoleData();
        roleData.setId(teacher.getId().toString());
        roleData.setUsername(teacher.getUsername());
        roleData.setEmail(teacher.getEmail());
        roleData.setPhotoPath(teacher.getPhotoPath());
        roleData.setRoles(convertSetToList(teacher.getRoles()));
        teacherDetail.setMessageResponse(messageResponse);
        teacherDetail.setData(roleData);
        return teacherDetail;
    }

    public RoleResponse getTeacherRole() {
        ERole roleTeacher = ERole.ROLE_TEACHER;
        Optional<Role> roleOptional = roleRepository.findByName(roleTeacher);

        if (roleOptional.isPresent()) {
            Role role = roleOptional.get();
            List<Users> usersWithRole = usersRepository.findByRolesName(role.getName());
            List<RoleData> teacherList = new ArrayList<>();

            if (usersWithRole.isEmpty()) {
                RoleResponse emptyResponse = new RoleResponse();
                emptyResponse.setMessageResponse(new MessageResponse(true, "No teachers found"));
                return emptyResponse;
            }

            RoleResponse detailRoleResponse = new RoleResponse();
            MessageResponse messageResponse = new MessageResponse(false, "Success");

            for (Users user : usersWithRole) {
                RoleData roleData = new RoleData();
                roleData.setId(user.getId().toString());
                roleData.setUsername(user.getUsername());
                roleData.setEmail(user.getEmail());
                roleData.setPhotoPath(user.getPhotoPath());
                roleData.setRoles(convertSetToList(user.getRoles()));
                teacherList.add(roleData);
            }

            detailRoleResponse.setMessageResponse(messageResponse);
            detailRoleResponse.setData(teacherList);
            return detailRoleResponse;
        } else {
            // Lakukan penanganan jika role tidak ditemukan
            return null;
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
