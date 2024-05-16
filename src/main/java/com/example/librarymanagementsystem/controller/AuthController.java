package com.example.librarymanagementsystem.controller;


import com.example.librarymanagementsystem.dto.request.LoginRequestDto;
import com.example.librarymanagementsystem.dto.request.RegistrationRequestDto;
import com.example.librarymanagementsystem.dto.response.ApiResponse;
import com.example.librarymanagementsystem.dto.response.LoginResponseDto;
import com.example.librarymanagementsystem.dto.response.RegistrationResponseDto;
import com.example.librarymanagementsystem.exception.PatronNotFoundException;
import com.example.librarymanagementsystem.service.PatronService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final PatronService patronService;

  @PostMapping("/sign-up")
  public ResponseEntity<ApiResponse<RegistrationResponseDto>> signUp(@Valid @RequestBody RegistrationRequestDto registrationRequest){
      ApiResponse<RegistrationResponseDto> response = patronService.registerPatron(registrationRequest);
     return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PostMapping("/login")
  public ResponseEntity<ApiResponse<LoginResponseDto>> login(@RequestBody LoginRequestDto loginRequest){
      ApiResponse<LoginResponseDto> apiResponse = patronService.login(loginRequest);
      return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
  }
  @PutMapping("/update")
  public ResponseEntity<ApiResponse<String>> updatePatron(@RequestBody RegistrationRequestDto requestDto, @RequestParam Long id) throws PatronNotFoundException {
    ApiResponse<String> response = patronService.updatePatron(requestDto, id);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @DeleteMapping("/delete/{patronEmail}")
  public ResponseEntity<String> deletePatron(@PathVariable String patronEmail) {
    patronService.deletePatron(patronEmail);
    return ResponseEntity.ok("patron deleted successfully.");
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<RegistrationResponseDto>> getPatron(@PathVariable Long id) {
    ApiResponse<RegistrationResponseDto> response = patronService.getPatron(id);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
