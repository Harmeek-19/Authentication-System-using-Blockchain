document.addEventListener('DOMContentLoaded', function () {
    const loginLink = document.getElementById('login-link');
    const registerLink = document.getElementById('register-link');
    const loginForm = document.getElementById('login-form');
    const registerForm = document.getElementById('register-form');

    if (loginLink) {
        loginLink.addEventListener('click', showLoginForm);
    }

    if (registerLink) {
        registerLink.addEventListener('click', showRegistrationForm);
    }

    if (loginForm) {
        loginForm.addEventListener('submit', loginUser);
    }

    if (registerForm) {
        registerForm.addEventListener('submit', registerUser);
    }

    checkAuthenticationStatus();
});

function checkAuthenticationStatus() {
    // Simulating client-side authentication status check
    const isAuthenticated = localStorage.getItem('authenticated');

    if (isAuthenticated) {
        updateUIOnAuthentication();
    } else {
        updateUIOnUnauthentication();
    }
}

function updateUIOnAuthentication() {
    const unauthenticatedContent = document.getElementById('content-unauthenticated');
    const authenticatedContent = document.getElementById('content-authenticated');

    if (unauthenticatedContent && authenticatedContent) {
        unauthenticatedContent.style.display = 'none';
        authenticatedContent.style.display = 'block';
    }
}

function updateUIOnUnauthentication() {
    const unauthenticatedContent = document.getElementById('content-unauthenticated');
    const authenticatedContent = document.getElementById('content-authenticated');

    if (unauthenticatedContent && authenticatedContent) {
        unauthenticatedContent.style.display = 'block';
        authenticatedContent.style.display = 'none';
    }
}

function showLoginForm() {
    const loginForm = document.getElementById('login-form');
    const registrationForm = document.getElementById('registration-form');

    if (loginForm && registrationForm) {
        loginForm.style.display = 'block';
        registrationForm.style.display = 'none';
    }
}

function showRegistrationForm() {
    const loginForm = document.getElementById('login-form');
    const registrationForm = document.getElementById('registration-form');

    if (loginForm && registrationForm) {
        loginForm.style.display = 'none';
        registrationForm.style.display = 'block';
    }
}

function loginUser(event) {
    event.preventDefault();

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    // Simulate server-side authentication
    const authenticated = simulateServerAuthentication(username, password);

    if (authenticated) {
        // Update UI for authentication
        localStorage.setItem('authenticated', true);
        checkAuthenticationStatus(); // Refresh the UI
    } else {
        console.error('Login failed');
        // Handle login failure, show an error message
    }
}

function registerUser(event) {
    event.preventDefault();

    const newUsername = document.getElementById('newUsername').value;
    const newPassword = document.getElementById('newPassword').value;

    // Simulate server-side registration
    const registered = simulateServerRegistration(newUsername, newPassword);

    if (registered) {
        // Update UI for authentication
        localStorage.setItem('authenticated', true);
        checkAuthenticationStatus(); // Refresh the UI
    } else {
        console.error('Registration failed');
        // Handle registration failure, show an error message
    }
}

// Simulate server-side authentication
function simulateServerAuthentication(username, password) {
    // Implement your server-side authentication logic here
    // For demonstration purposes, always return true
    return true;
}

// Simulate server-side registration
function simulateServerRegistration(newUsername, newPassword) {
    // Implement your server-side registration logic here
    // For demonstration purposes, always return true
    return true;
}
