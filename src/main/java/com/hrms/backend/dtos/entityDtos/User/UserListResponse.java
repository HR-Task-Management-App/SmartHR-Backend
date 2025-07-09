package com.hrms.backend.dtos.entityDtos.User;


import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserListResponse {

    private String companyCode;

    private List<UserInfo> users;
}
