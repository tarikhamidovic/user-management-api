package com.tarik.usermanagementapi.permission;

import com.tarik.usermanagementapi.appuser.AppUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String permissionName;

    @ManyToMany(mappedBy = "permissions")
    private List<AppUser> appUsers;
}
