package Atividade11_FastSite2.Application;

import Atividade11_FastSite2.Entites.Cliente;
import Atividade11_FastSite2.Entites.Usuario;
import Atividade11_FastSite2.dao.ClienteDAO;
import Atividade11_FastSite2.dao.UsuarioDAO;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Servidor {
    private static final Path FRONTEND = Path.of("TelaLogin");

    public static void main(String[] args) throws IOException {
        HttpServer servidor = HttpServer.create(new InetSocketAddress(8080), 0);

        servidor.createContext("/login", exchange -> {
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                responderJson(exchange, 405, "{\"mensagem\":\"Use POST para fazer login.\"}");
                return;
            }
            try {
                Map<String, String> dados = parseFormData(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
                String login = dados.get("username");
                String senha = dados.get("password");
                if (login == null || login.isBlank() || senha == null || senha.isEmpty()) {
                    responderJson(exchange, 400, "{\"mensagem\":\"Preencha o login e a senha.\"}");
                    return;
                }
                UsuarioDAO dao = new UsuarioDAO();
                Usuario usuario = dao.buscarPorLogin(login.trim());
                if (usuario != null && dao.autenticar(login.trim(), senha)) {
                    responderJson(exchange, 200, "{\"status\":\"LOGIN_SUCESSO\",\"nome\":" + jsonString(usuario.getNome()) + "}");
                } else {
                    responderJson(exchange, 401, "{\"mensagem\":\"Senha incorreta ou usuário inválido.\"}");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                responderJson(exchange, 500, "{\"mensagem\":\"Não foi possível acessar o banco de dados. Confira a conexão.\"}");
            } catch (Exception e) {
                e.printStackTrace();
                responderJson(exchange, 500, "{\"mensagem\":\"Erro interno ao processar o login.\"}");
            }
        });

        servidor.createContext("/registrar-usuario", exchange -> {
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                responder(exchange, 405, "Método não permitido.");
                return;
            }
            try {
                Map<String, String> dados = parseFormData(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
                String nome = dados.get("nome");
                String login = dados.get("login");
                String senha = dados.get("senha");
                if (nome == null || nome.isBlank() || login == null || login.isBlank() || senha == null || senha.length() < 6) {
                    responder(exchange, 400, "Preencha nome e login e informe uma senha com pelo menos 6 caracteres.");
                    return;
                }
                UsuarioDAO dao = new UsuarioDAO();
                if (dao.buscarPorLogin(login.trim()) != null) {
                    responder(exchange, 409, "Esse login já está cadastrado. Escolha outro.");
                    return;
                }
                boolean criado = dao.cadastrar(new Usuario(nome.trim(), login.trim(), senha));
                if (criado) responder(exchange, 201, "CONTA_CRIADA");
                else responder(exchange, 500, "Não foi possível criar a conta.");
            } catch (SQLException e) {
                e.printStackTrace();
                responder(exchange, 500, "Erro no banco de dados ao criar a conta.");
            } catch (Exception e) {
                e.printStackTrace();
                responder(exchange, 500, "Erro interno ao criar a conta.");
            }
        });

        servidor.createContext("/cadastrar", exchange -> {
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                responder(exchange, 405, "Método não permitido.");
                return;
            }
            try {
                Map<String, String> dados = parseFormData(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
                Cliente c = new Cliente();
                c.setNome(dados.get("nome"));
                c.setEndereco(dados.get("endereco"));
                c.setCep(dados.get("cep"));
                c.setNumero(dados.get("numero"));
                c.setTelefone(dados.get("telefone"));
                c.setObservacao(dados.get("observacao"));
                boolean salvou = new ClienteDAO().salvar(c);
                if (salvou) responder(exchange, 200, "CADASTRO_SUCESSO");
                else responder(exchange, 400, "Erro ao salvar cadastro.");
            } catch (Exception e) {
                e.printStackTrace();
                responder(exchange, 500, "Erro interno no servidor ao salvar cadastro.");
            }
        });

        servidor.createContext("/buscar-cliente", exchange -> {
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                responder(exchange, 405, "Método não permitido.");
                return;
            }
            try {
                Map<String, String> parametros = parseFormData(exchange.getRequestURI().getRawQuery() == null ? "" : exchange.getRequestURI().getRawQuery());
                int id = Integer.parseInt(parametros.getOrDefault("id", "0"));
                Cliente c = new ClienteDAO().buscarPorId(id);
                if (c == null) {
                    responder(exchange, 404, "Cliente não encontrado.");
                    return;
                }
                String json = "{\"id\":" + c.getId()
                        + ",\"nome\":" + jsonString(c.getNome())
                        + ",\"endereco\":" + jsonString(c.getEndereco())
                        + ",\"cep\":" + jsonString(c.getCep())
                        + ",\"numero\":" + jsonString(c.getNumero())
                        + ",\"telefone\":" + jsonString(c.getTelefone())
                        + ",\"observacao\":" + jsonString(c.getObservacao()) + "}";
                responderJson(exchange, 200, json);
            } catch (NumberFormatException e) {
                responder(exchange, 400, "ID inválido.");
            } catch (Exception e) {
                e.printStackTrace();
                responder(exchange, 500, "Erro ao buscar cliente.");
            }
        });

        servidor.createContext("/", Servidor::servirFrontend);
        servidor.start();
        System.out.println("Sistema iniciado em http://localhost:8080");
    }

    private static void servirFrontend(HttpExchange exchange) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            responder(exchange, 405, "Método não permitido.");
            return;
        }
        String caminho = exchange.getRequestURI().getPath();
        String arquivo;
        if ("/".equals(caminho) || "/index.html".equals(caminho)) arquivo = "index.html";
        else if ("/tela.css".equals(caminho)) arquivo = "tela.css";
        else if ("/tela.js".equals(caminho)) arquivo = "tela.js";
        else { responder(exchange, 404, "Página não encontrada."); return; }

        Path caminhoArquivo = FRONTEND.resolve(arquivo);
        if (!Files.isRegularFile(caminhoArquivo)) {
            responder(exchange, 404, "Arquivo não encontrado: " + caminhoArquivo.toAbsolutePath());
            return;
        }
        String tipo = arquivo.endsWith(".css") ? "text/css; charset=UTF-8"
                : arquivo.endsWith(".js") ? "text/javascript; charset=UTF-8"
                : "text/html; charset=UTF-8";
        byte[] conteudo = Files.readAllBytes(caminhoArquivo);
        exchange.getResponseHeaders().set("Content-Type", tipo);
        exchange.sendResponseHeaders(200, conteudo.length);
        try (OutputStream saida = exchange.getResponseBody()) { saida.write(conteudo); }
    }

    private static Map<String, String> parseFormData(String corpo) {
        Map<String, String> dados = new HashMap<>();
        if (corpo == null || corpo.isEmpty()) return dados;
        for (String campo : corpo.split("&")) {
            String[] partes = campo.split("=", 2);
            String chave = URLDecoder.decode(partes[0], StandardCharsets.UTF_8);
            String valor = partes.length == 2 ? URLDecoder.decode(partes[1], StandardCharsets.UTF_8) : "";
            dados.put(chave, valor);
        }
        return dados;
    }

    private static String jsonString(String valor) {
        if (valor == null) return "null";
        StringBuilder sb = new StringBuilder("\"");
        for (char ch : valor.toCharArray()) {
            switch (ch) {
                case '\"' -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                default -> sb.append(ch);
            }
        }
        return sb.append('\"').toString();
    }

    private static void responderJson(HttpExchange exchange, int status, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream saida = exchange.getResponseBody()) { saida.write(bytes); }
    }

    private static void responder(HttpExchange exchange, int status, String mensagem) throws IOException {
        byte[] bytes = mensagem.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream saida = exchange.getResponseBody()) { saida.write(bytes); }
    }
}
