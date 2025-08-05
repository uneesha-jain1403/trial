package com.eazybank.accounts.controller;

import com.eazybank.accounts.dto.CustomerDto;
import com.eazybank.accounts.dto.ResponseDto;
import com.eazybank.accounts.service.AccountsService;
import com.eazybank.accounts.service.AuthService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Accounts Controller",
        description = "Controller for CRUD Operation on Accounts"
)
@RestController
@RequestMapping("/api")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AccountsController {

    private AccountsService accountsService;
    private AuthService authService;
    private AuthenticationManager authenticationManager;

    @ApiResponse(
            description = "Will greet to the Client"
    )
    @GetMapping("/greet")
    public ResponseEntity<ResponseDto> greet() {
        return ResponseEntity
                .status(201)
                .body(new ResponseDto("SUCCESS", HttpStatus.OK));
    }

    @ApiResponse(
            description = "Will create new record for the given customer details",
            responseCode = "201"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> create(@RequestBody CustomerDto customerDto){
        Long accountsId = accountsService.create(customerDto);
        return ResponseEntity.status(201)
                .body(new ResponseDto("CREATED - " + accountsId, HttpStatus.CREATED));

    }

    @ApiResponse(
            description = "Will fetch the record for the given customer mobile number",
            responseCode = "200"
    )
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/fetch")
    public ResponseEntity<CustomerDto> fetch(@RequestParam String mobileNumber){
        CustomerDto customerDto = accountsService.fetch(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(customerDto);
    }

    @ApiResponse(
            description = "Will delete the record for the given customer mobile number",
            responseCode = "200"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("delete")
    public ResponseEntity<ResponseDto> delete(@RequestParam String mobileNumber){
        Boolean isDeleted = accountsService.delete(mobileNumber);
        if(isDeleted){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDto("Deleted successfully", HttpStatus.OK));
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDto("Unable to delete for mobile number - " + mobileNumber, HttpStatus.NOT_FOUND));
        }
    }

    @ApiResponse(
            description = "Will update customer name and email based upon the mobile number",
            responseCode = "200"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/update")
    public ResponseEntity<ResponseDto> update(@RequestBody CustomerDto customerDto){
        Boolean isUpdated = accountsService.update(customerDto);
        if(isUpdated){
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(new ResponseDto("Updated successfully.",HttpStatus.ACCEPTED));
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDto("Unable to update record for mobile number - " + customerDto.getMobileNumber(), HttpStatus.NOT_FOUND));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Authentication> userLogin(@RequestBody LoginRequest loginRequest){
//        this.authService.userLogin(loginRequest);
        Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.username(), loginRequest.password());
        System.out.println(authenticationRequest);
        return ResponseEntity.status(HttpStatus.OK).body(this.authenticationManager.authenticate(authenticationRequest));
    }

    @GetMapping("/logout")
    public ResponseEntity<LogoutRequest> userLogout(){
        return ResponseEntity.status(HttpStatus.OK).body(new LogoutRequest("logout success", HttpStatus.ACCEPTED ));
    }

    public record LoginRequest (String username, String password){}
}