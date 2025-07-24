// Basic client-side validation and alerts

document.addEventListener('DOMContentLoaded', () => {
    const transferForm = document.getElementById('transferForm');
    if (transferForm) {
        transferForm.onsubmit = (e) => {
            const toAccount = transferForm.toAccount.value.trim();
            const amount = parseFloat(transferForm.amount.value);
            if (!toAccount) {
                alert('Please enter recipient account number.');
                e.preventDefault();
                return false;
            }
            if (isNaN(amount) || amount <= 0) {
                alert('Please enter a valid positive amount.');
                e.preventDefault();
                return false;
            }
            return true;
        };
    }

    const registerForm = document.getElementById('registerForm');
    if (registerForm) {
        registerForm.onsubmit = (e) => {
            const password = registerForm.password.value;
            if(password.length < 6) {
                alert('Password must be at least 6 characters long.');
                e.preventDefault();
                return false;
            }
            return true;
        };
    }
});
