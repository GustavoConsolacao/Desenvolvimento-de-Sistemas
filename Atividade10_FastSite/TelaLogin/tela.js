document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('loginForm');
    const errorMessage = document.getElementById('errorMessage');
    const loginScreen = document.getElementById('loginScreen');
    const sessionScreen = document.getElementById('sessionScreen');
    const sessionUser = document.getElementById('sessionUser');
    const logoutBtn = document.getElementById('logoutBtn');
    const submitBtn = document.getElementById('submitBtn');

    loginForm.addEventListener('submit', async (event) => {
        event.preventDefault();

        const username = document.getElementById('username').value.trim();
        const password = document.getElementById('password').value;

        errorMessage.classList.add('hidden');
        submitBtn.disabled = true;
        submitBtn.textContent = 'Verificando...';

        try {
            const resposta = await fetch('/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
                },
                body: new URLSearchParams({
                    username: username,
                    password: password
                })
            });

            const mensagem = await resposta.text();

            if (resposta.ok && mensagem === 'LOGIN_SUCESSO') {
                loginScreen.classList.add('hidden');
                sessionUser.textContent = `Sessão ativa: ${username}`;
                sessionScreen.classList.remove('hidden');
                loginForm.reset();
            } else {
                errorMessage.textContent = mensagem || 'Senha incorreta ou usuário inválido.';
                errorMessage.classList.remove('hidden');
            }
        } catch (error) {
            errorMessage.textContent = 'Não foi possível conectar ao servidor. Confira se ele está iniciado.';
            errorMessage.classList.remove('hidden');
        } finally {
            submitBtn.disabled = false;
            submitBtn.textContent = 'Entrar no Sistema';
        }
    });

    logoutBtn.addEventListener('click', () => {
        sessionScreen.classList.add('hidden');
        loginScreen.classList.remove('hidden');
        errorMessage.classList.add('hidden');
        loginForm.reset();
    });
});
