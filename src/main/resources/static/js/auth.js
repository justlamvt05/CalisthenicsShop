/**
 * CALISTHENICS STORE - AUTH JAVASCRIPT
 * Enhanced authentication form functionality
 */

document.addEventListener('DOMContentLoaded', function() {
    // Initialize all auth functionality
    initPasswordToggle();
    initPasswordStrength();
    initFormValidation();
    initSocialLogin();
    initAnimations();
});

/**
 * Password visibility toggle functionality
 */
function initPasswordToggle() {
    const toggleButtons = document.querySelectorAll('.toggle-password');

    toggleButtons.forEach(button => {
        button.addEventListener('click', function() {
            const passwordInput = this.previousElementSibling ||
                this.parentElement.querySelector('input[type="password"], input[type="text"]');
            const icon = this.querySelector('i');

            if (passwordInput) {
                if (passwordInput.type === 'password') {
                    passwordInput.type = 'text';
                    icon.classList.remove('fa-eye');
                    icon.classList.add('fa-eye-slash');
                } else {
                    passwordInput.type = 'password';
                    icon.classList.remove('fa-eye-slash');
                    icon.classList.add('fa-eye');
                }
            }
        });
    });
}

/**
 * Password strength indicator
 */
function initPasswordStrength() {
    const passwordInput = document.querySelector('.password-input');
    const strengthBar = document.querySelector('.strength-bar');
    const strengthText = document.querySelector('.strength-level');

    if (!passwordInput || !strengthBar || !strengthText) return;

    passwordInput.addEventListener('input', function() {
        const password = this.value;
        const strength = calculatePasswordStrength(password);

        updatePasswordStrength(strength, strengthBar, strengthText);
    });
}

/**
 * Calculate password strength
 */
function calculatePasswordStrength(password) {
    let score = 0;
    let feedback = [];

    // Length check
    if (password.length >= 8) score += 1;
    else feedback.push('At least 8 characters');

    // Lowercase check
    if (/[a-z]/.test(password)) score += 1;
    else feedback.push('Lowercase letter');

    // Uppercase check
    if (/[A-Z]/.test(password)) score += 1;
    else feedback.push('Uppercase letter');

    // Number check
    if (/\d/.test(password)) score += 1;
    else feedback.push('Number');

    // Special character check
    if (/[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/.test(password)) score += 1;
    else feedback.push('Special character');

    return {
        score: score,
        feedback: feedback
    };
}

/**
 * Update password strength display
 */
function updatePasswordStrength(strength, strengthBar, strengthText) {
    const levels = ['weak', 'weak', 'fair', 'good', 'strong'];
    const labels = ['Weak', 'Weak', 'Fair', 'Good', 'Strong'];

    const level = levels[strength.score];
    const label = labels[strength.score];

    // Remove all strength classes
    strengthBar.className = 'strength-bar';

    // Add current strength class
    if (level) {
        strengthBar.classList.add(level);
    }

    // Update text
    strengthText.textContent = label;

    // Add color to text based on strength
    strengthText.style.color = getStrengthColor(level);
}

/**
 * Get color based on password strength
 */
function getStrengthColor(level) {
    const colors = {
        'weak': '#dc3545',
        'fair': '#ffc107',
        'good': '#28a745',
        'strong': '#20c997'
    };
    return colors[level] || '#666';
}

/**
 * Form validation enhancements
 */
function initFormValidation() {
    const forms = document.querySelectorAll('.auth-form');

    forms.forEach(form => {
        form.addEventListener('submit', function(e) {
            if (!validateForm(this)) {
                e.preventDefault();
                e.stopPropagation();
            } else {
                // Add loading state to submit button
                const submitBtn = this.querySelector('button[type="submit"]');
                if (submitBtn) {
                    submitBtn.classList.add('loading');
                    submitBtn.disabled = true;
                }
            }
        });

        // Real-time validation
        const inputs = form.querySelectorAll('input');
        inputs.forEach(input => {
            input.addEventListener('blur', function() {
                validateInput(this);
            });

            input.addEventListener('input', function() {
                // Clear invalid state when user starts typing
                if (this.classList.contains('is-invalid')) {
                    this.classList.remove('is-invalid');
                    const feedback = this.parentElement.querySelector('.invalid-feedback');
                    if (feedback) {
                        feedback.style.display = 'none';
                    }
                }
            });
        });
    });
}

/**
 * Validate entire form
 */
function validateForm(form) {
    const inputs = form.querySelectorAll('input[required]');
    let isValid = true;

    inputs.forEach(input => {
        if (!validateInput(input)) {
            isValid = false;
        }
    });

    // Check terms checkbox if present
    const termsCheckbox = form.querySelector('#terms');
    if (termsCheckbox && !termsCheckbox.checked) {
        showError(termsCheckbox, 'You must accept the terms and conditions');
        isValid = false;
    }

    return isValid;
}

/**
 * Validate individual input
 */
function validateInput(input) {
    const value = input.value.trim();
    const type = input.type;
    let isValid = true;
    let message = '';

    // Required field check
    if (input.required && !value) {
        message = 'This field is required';
        isValid = false;
    }

    // Email validation
    else if (type === 'email' && value) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(value)) {
            message = 'Please enter a valid email address';
            isValid = false;
        }
    }

    // Password validation
    else if (input.name === 'password' && value) {
        if (value.length < 8) {
            message = 'Password must be at least 8 characters long';
            isValid = false;
        }
    }

    // Phone validation
    else if (type === 'tel' && value) {
        const phoneRegex = /^[\+]?[1-9][\d]{0,15}$/;
        if (!phoneRegex.test(value.replace(/\s/g, ''))) {
            message = 'Please enter a valid phone number';
            isValid = false;
        }
    }

    // Update UI based on validation
    if (isValid) {
        input.classList.remove('is-invalid');
        input.classList.add('is-valid');
        hideError(input);
    } else {
        input.classList.remove('is-valid');
        input.classList.add('is-invalid');
        showError(input, message);
    }

    return isValid;
}

/**
 * Show error message
 */
function showError(input, message) {
    let feedback = input.parentElement.querySelector('.invalid-feedback');
    if (!feedback) {
        feedback = document.createElement('div');
        feedback.className = 'invalid-feedback';
        input.parentElement.appendChild(feedback);
    }
    feedback.textContent = message;
    feedback.style.display = 'block';
}

/**
 * Hide error message
 */
function hideError(input) {
    const feedback = input.parentElement.querySelector('.invalid-feedback');
    if (feedback) {
        feedback.style.display = 'none';
    }
}

/**
 * Social login functionality
 */
function initSocialLogin() {
    const socialButtons = document.querySelectorAll('.social-login .btn');

    socialButtons.forEach(button => {
        button.addEventListener('click', function() {
            const provider = this.textContent.includes('Google') ? 'google' : 'facebook';
            handleSocialLogin(provider);
        });
    });
}

/**
 * Handle social login
 */
function handleSocialLogin(provider) {
    // Add loading state
    const button = event.target.closest('.btn');
    button.classList.add('loading');
    button.disabled = true;

    // Simulate social login process
    setTimeout(() => {
        // Remove loading state
        button.classList.remove('loading');
        button.disabled = false;

        // In a real implementation, you would redirect to the social provider
        console.log(`Initiating ${provider} login...`);

        // For demo purposes, show an alert
        alert(`${provider.charAt(0).toUpperCase() + provider.slice(1)} login would be initiated here`);
    }, 2000);
}

/**
 * Initialize animations and interactions
 */
function initAnimations() {
    // Add hover effects to form inputs
    const inputs = document.querySelectorAll('.form-control');
    inputs.forEach(input => {
        input.addEventListener('focus', function() {
            this.parentElement.classList.add('focused');
        });

        input.addEventListener('blur', function() {
            this.parentElement.classList.remove('focused');
        });
    });

    // Add ripple effect to buttons
    const buttons = document.querySelectorAll('.btn');
    buttons.forEach(button => {
        button.addEventListener('click', function(e) {
            const ripple = document.createElement('span');
            const rect = this.getBoundingClientRect();
            const size = Math.max(rect.width, rect.height);
            const x = e.clientX - rect.left - size / 2;
            const y = e.clientY - rect.top - size / 2;

            ripple.style.cssText = `
                position: absolute;
                width: ${size}px;
                height: ${size}px;
                left: ${x}px;
                top: ${y}px;
                background: rgba(255, 255, 255, 0.3);
                border-radius: 50%;
                transform: scale(0);
                animation: ripple 0.6s ease-out;
                pointer-events: none;
            `;

            this.style.position = 'relative';
            this.style.overflow = 'hidden';
            this.appendChild(ripple);

            setTimeout(() => {
                ripple.remove();
            }, 600);
        });
    });
}

/**
 * Utility functions
 */

// Format phone number as user types
function formatPhoneNumber(input) {
    let value = input.value.replace(/\D/g, '');
    let formattedValue = '';

    if (value.length > 0) {
        if (value.length <= 3) {
            formattedValue = value;
        } else if (value.length <= 6) {
            formattedValue = `${value.slice(0, 3)}-${value.slice(3)}`;
        } else {
            formattedValue = `${value.slice(0, 3)}-${value.slice(3, 6)}-${value.slice(6, 10)}`;
        }
    }

    input.value = formattedValue;
}

// Auto-resize textarea (if needed)
function autoResize(textarea) {
    textarea.style.height = 'auto';
    textarea.style.height = textarea.scrollHeight + 'px';
}

// Add CSS for ripple animation
const style = document.createElement('style');
style.textContent = `
    @keyframes ripple {
        to {
            transform: scale(2);
            opacity: 0;
        }
    }
    
    .focused .input-group-text {
        border-color: #667eea;
        background: rgba(102, 126, 234, 0.15);
    }
`;
document.head.appendChild(style);