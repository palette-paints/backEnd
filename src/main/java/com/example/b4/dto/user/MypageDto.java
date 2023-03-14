package com.example.b4.dto.user;


import com.example.b4.entity.Role;
import com.example.b4.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
public class MypageDto {

    private Role userRole;
    private String userName;
    private String userPhone;
    private String userBirth;
    private String userJob;
    private String email;

    public MypageDto (User user){
        this.userBirth = user.getUserBirth();
        this.userJob = user.getUserJob();
        this.userRole = user.getRole();
        this.userName = user.getUsername();
        this.userPhone = user.getUserPhone();
        this.email = user.getEmail();
    }
}
