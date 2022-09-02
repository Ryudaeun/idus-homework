package com.idus.homework.member.domain;

import com.idus.homework.common.entity.BaseEntity;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "member")
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30, unique = true)
    private String username;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String password;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, length = 30)
    private String nickname;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(nullable = false, length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(length = 1)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 5)
    private Role role;

    public String getFormattedPhone() {
        if (phone.length() == 8) {
            return phone.replaceFirst("^([0-9]{4})([0-9]{4})$", "$1-$2");
        } else if (phone.length() == 12) {
            return phone.replaceFirst("(^[0-9]{4})([0-9]{4})([0-9]{4})$", "$1-$2-$3");
        }
        return phone.replaceFirst("(^02|[0-9]{3})([0-9]{3,4})([0-9]{4})$", "$1-$2-$3");
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        setPassword(passwordEncoder.encode(password));
    }

    public boolean matchPassword(PasswordEncoder passwordEncoder, String password) {
        return passwordEncoder.matches(password, getPassword());
    }

    public void setUserRole() {
        setRole(Role.USER);
    }
}
