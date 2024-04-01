package com.example.quran.controller;

import com.example.quran.dto.ChangePasswordRequest;
import com.example.quran.dto.UserDTO;
import com.example.quran.model.Users;
import com.example.quran.repository.UsersRepository;
import com.example.quran.response.DetailRoleResponse;
import com.example.quran.services.UserService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    UsersRepository usersRepository;

    @GetMapping("/user/{userId}")
//    @PreAuthorize("hasAnyRole('BUYER', 'ADMIN')")
    public ResponseEntity<Users> getUserById(@PathVariable(value = "userId") Long id) {
        Optional<Users> userData = userService.getUserById(id);
        if (userData.isPresent()) {
            return new ResponseEntity<>(userData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
    @GetMapping("/guru")
    public ResponseEntity<?> getTeacherUsers(){
        return ResponseEntity.ok(userService.getTeacherRole());
    }
    @GetMapping("/guru/{userId}")
    public ResponseEntity<?> getTeacherById(@PathVariable Long userId) {
        DetailRoleResponse teacherDetail = userService.getTeacherRoleDetail(userId);
        if (teacherDetail == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(teacherDetail);
    }

    @GetMapping("/getEmailUser")
    public Users getUserEmail(String email){
        return userService.getUserByEmail(email);
    }

    @PostMapping("/{id}/uploadPhoto")
    public ResponseEntity<?> uploadPhoto(@PathVariable Long id, @RequestParam MultipartFile file) throws Exception{
        userService.uploadPhoto(id, file);
        return ResponseEntity.ok("Photo Upload Succesfully");
    }

    @PutMapping("/{id}/changePhoto")
    public ResponseEntity<?> changePhoto(@PathVariable Long id, @RequestParam MultipartFile file) throws Exception{
        userService.changePhoto(id, file);
        return ResponseEntity.ok("Photo Changed Successfully");
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestBody String email, @RequestBody String oldPassword, @RequestBody String newPassword , Model model) {
        boolean success = userService.changeUserPassword(email, oldPassword, newPassword);
        if (success) {
            model.addAttribute("success", "Kata sandi berhasil diubah");
        } else {
            model.addAttribute("error", "Kata sandi lama salah atau email tidak ditemukan");
        }
        return "result";
    }
//    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) throws Exception{
//        UserDTO pDTO = new UserDTO();
//        pDTO.setEmail(changePasswordRequest.getEmail());
//        pDTO.setPassword(changePasswordRequest.getOldPassword());
//
//        int check = userService.changeUserPassword(pDTO, changePasswordRequest.getNewPassword());
//
//        if(check == 1){
//            return ResponseEntity.status(HttpStatus.OK).body("Berhasil");
//
//        }
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("gagal");
//    }
}
