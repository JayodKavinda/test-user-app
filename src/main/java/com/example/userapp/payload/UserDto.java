package com.example.userapp.payload;


import javax.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto{
    @NotEmpty
    @Size(min = 2, message = "First Name should have at least 2 characters" )
    private String firstName;

    @NotEmpty
    @Size(min = 2, message = "Last Name should have at least 2 characters")
    private String lastName;

    @NotEmpty
    @Email
    private String email;
}
