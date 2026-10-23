document.addEventListener('DOMContentLoaded', () => {
    const welcomeScreen = document.getElementById('welcomeScreen');
    const loginScreen = document.getElementById('loginScreen');
    const registerScreen = document.getElementById('registerScreen');
    const sessionScreen = document.getElementById('sessionScreen');

    const showScreen = (screen) => {
        [welcomeScreen, loginScreen, registerScreen, sessionScreen]
            .forEach(item => item.classList.add('hidden'));
        screen.classList.remove('hidden');
    };

    document.getElementById('goLoginBtn').addEventListener('click', () => showScreen(loginScreen));
    document.getElementById('goRegisterBtn').addEventListener('click', () => showScreen(registerScreen));
    document.getElementById('backFromLoginBtn').addEventListener('click', () => showScreen(welcomeScreen));
    document.getElementById('backFromRegisterBtn').addEventListener('click', () => showScreen(welcomeScreen));

    const loginForm = document.getElementById('loginForm');
    const errorMessage = document.getElementById('errorMessage');
    const submitBtn = document.getElementById('submitBtn');
    const registerForm = document.getElementById('registerForm');
    const registerErrorMessage = document.getElementById('registerErrorMessage');
    const registerSuccessMessage = document.getElementById('registerSuccessMessage');
    const cadastroForm = document.getElementById('cadastroForm');
    const successMessage = document.getElementById('successMessage');
    const btnBuscar = document.getElementById('btnBuscar');
    const searchId = document.getElementById('searchId');
    const resultadoBusca = document.getElementById('resultadoBusca');
    registerForm.addEventListener('submit', async (event) => {
        event.preventDefault();
        registerErrorMessage.classList.add('hidden');
        registerSuccessMessage.classList.add('hidden');

        const nome = document.getElementById('registerName').value.trim();
        const login = document.getElementById('registerUsername').value.trim();
        const senha = document.getElementById('registerPassword').value;
        const confirmarSenha = document.getElementById('registerPasswordConfirm').value;

        if (senha !== confirmarSenha) {
            registerErrorMessage.textContent = 'As senhas não coincidem.';
            registerErrorMessage.classList.remove('hidden');
            return;
        }

        const registerSubmitBtn = document.getElementById('registerSubmitBtn');
        registerSubmitBtn.disabled = true;
        registerSubmitBtn.textContent = 'Criando conta...';
        try {
            const resposta = await fetch('/registrar-usuario', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' },
                body: new URLSearchParams({ nome, login, senha })
            });
            const mensagem = await resposta.text();
            if (resposta.ok) {
                document.getElementById('username').value = login;
                loginForm.reset();
                document.getElementById('username').value = login;
                registerForm.reset();
                showScreen(loginScreen);
                errorMessage.textContent = 'Conta criada com sucesso! Entre com seu login e senha.';
                errorMessage.classList.remove('hidden');
            } else {
                registerErrorMessage.textContent = mensagem || 'Não foi possível criar a conta.';
                registerErrorMessage.classList.remove('hidden');
            }
        } catch (error) {
            registerErrorMessage.textContent = 'Erro ao conectar ao servidor.';
            registerErrorMessage.classList.remove('hidden');
        } finally {
            registerSubmitBtn.disabled = false;
            registerSubmitBtn.textContent = 'Criar conta';
        }
    });
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
                headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' },
                body: new URLSearchParams({ username, password })
            });
            const resultado = await resposta.json();
            if (resposta.ok && resultado.status === 'LOGIN_SUCESSO') {
                document.getElementById('loggedUserName').textContent = resultado.nome;
                loginForm.reset();
                showScreen(sessionScreen);
            } else {
                errorMessage.textContent = resultado.mensagem || 'Senha incorreta ou usuário inválido.';
                errorMessage.classList.remove('hidden');
            }
        } catch (error) {
            errorMessage.textContent = 'Erro ao conectar ao servidor.';
            errorMessage.classList.remove('hidden');
        } finally {
            submitBtn.disabled = false;
            submitBtn.textContent = 'Entrar no Sistema';
        }
    });
    cadastroForm.addEventListener('submit', async (event) => {
        event.preventDefault();
        const dados = new URLSearchParams({
            nome: document.getElementById('cadNome').value.trim(),
            endereco: document.getElementById('cadEndereco').value.trim(),
            cep: document.getElementById('cadCep').value.trim(),
            numero: document.getElementById('cadNumero').value.trim(),
            telefone: document.getElementById('cadTelefone').value.trim(),
            observacao: document.getElementById('cadObservacao').value.trim()
        });
        try {
            const resposta = await fetch('/cadastrar', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' },
                body: dados
            });
            const mensagem = await resposta.text();
            if (resposta.ok) {
                successMessage.textContent = 'Cadastro realizado com sucesso!';
                successMessage.classList.remove('hidden');
                cadastroForm.reset();
                setTimeout(() => successMessage.classList.add('hidden'), 4000);
            } else {
                alert(mensagem || 'Erro ao realizar o cadastro.');
            }
        } catch (error) {
            alert('Erro de comunicação com o servidor ao cadastrar.');
        }
    });
    btnBuscar.addEventListener('click', async () => {
        const id = searchId.value.trim();
        if (!id || Number(id) < 1) {
            alert('Por favor, informe um ID válido para consultar.');
            return;
        }
        try {
            const resposta = await fetch(`/buscar-cliente?id=${encodeURIComponent(id)}`);
            if (resposta.ok) {
                const cliente = await resposta.json();
                document.getElementById('resId').textContent = cliente.id;
                document.getElementById('resNome').textContent = cliente.nome || '';
                document.getElementById('resEndereco').textContent = cliente.endereco || '';
                document.getElementById('resCep').textContent = cliente.cep || '';
                document.getElementById('resNumero').textContent = cliente.numero || '';
                document.getElementById('resTelefone').textContent = cliente.telefone || '';
                document.getElementById('resObservacao').textContent = cliente.observacao || 'Nenhuma';
                resultadoBusca.classList.remove('hidden');
            } else {
                alert('Cadastro não encontrado para o ID informado.');
                resultadoBusca.classList.add('hidden');
            }
        } catch (error) {
            alert('Erro ao consultar o servidor.');
        }
    });

    document.getElementById('logoutBtn').addEventListener('click', () => {
        resultadoBusca.classList.add('hidden');
        successMessage.classList.add('hidden');
        showScreen(welcomeScreen);
    });
});
