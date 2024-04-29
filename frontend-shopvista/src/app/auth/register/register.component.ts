import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  firstname = '';
  lastname = '';
  email = '';
  mobilenumber = '';
  password = '';
  confirmPassword = '';

  constructor(private authService: AuthService, private router: Router) {}

  register() {
    if (this.password != this.confirmPassword) {
      alert("Passwords does not match");
    }
    this.authService.register(this.firstname, this.lastname, this.email, this.mobilenumber, this.password).subscribe({
      next: (response) => {
        alert("Registration successful!");
        console.log(response);
        this.router.navigate(['/login']);
      },
      error: (error) => {
        alert("Registration failed!");
        console.error(error);
      }
    });
  }
}


