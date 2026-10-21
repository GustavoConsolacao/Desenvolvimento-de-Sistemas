package Atividade9_FastSite.Application;

import Atividade9_FastSite.dao.UsuarioDAO;
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
                responder(exchange, 405, "Use o método POST para fazer login.");
                return;
            }

            try {
                String corpo = new String(
                        exchange.getRequestBody().readAllBytes(),
                        StandardCharsets.UTF_8
                );

                Map<String, String> dados = new HashMap<>();
                for (String campo : corpo.split("&")) {
                    String[] partes = campo.split("=", 2);
                    if (partes.length == 2) {
                        dados.put(
                                URLDecoder.decode(partes[0], StandardCharsets.UTF_8),
                                URLDecoder.decode(partes[1], StandardCharsets.UTF_8)
                        );
                    }
                }

                String login = dados.get("username");
                String senha = dados.get("password");

                if (login == null || login.isBlank() || senha == null || senha.isEmpty()) {
                    responder(exchange, 400, "Preencha o login e a senha.");
                    return;
                }

                boolean autenticado = new UsuarioDAO().autenticar(login.trim(), senha);
                if (autenticado) {
                    responder(exchange, 200, "LOGIN_SUCESSO");
                } else {
                    responder(exchange, 401, "Senha incorreta ou usuário inválido.");
                }

            } catch (SQLException e) {
                e.printStackTrace();
                responder(exchange, 500, "Não foi possível acessar o banco de dados.");
            } catch (Exception e) {
                e.printStackTrace();
                responder(exchange, 500, "Erro interno ao processar o login.");
            }
        });

        // Entrega a tela, o CSS e o JavaScript pelo mesmo endereço do backend.
        servidor.createContext("/", exchange -> servirFrontend(exchange));

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

        if ("/".equals(caminho) || "/index.html".equals(caminho)) {
            arquivo = "index.html";
        } else if ("/tela.css".equals(caminho)) {
            arquivo = "tela.css";
        } else if ("/tela.js".equals(caminho)) {
            arquivo = "tela.js";
        } else {
            responder(exchange, 404, "Página não encontrada.");
            return;
        }

        Path caminhoArquivo = FRONTEND.resolve(arquivo);
        if (!Files.isRegularFile(caminhoArquivo)) {
            responder(exchange, 404,
                    "Arquivo não encontrado: " + caminhoArquivo.toAbsolutePath());
            return;
        }

        String tipo = arquivo.endsWith(".css") ? "text/css; charset=UTF-8"
                : arquivo.endsWith(".js") ? "text/javascript; charset=UTF-8"
                : "text/html; charset=UTF-8";

        byte[] conteudo = Files.readAllBytes(caminhoArquivo);
        exchange.getResponseHeaders().set("Content-Type", tipo);
        exchange.sendResponseHeaders(200, conteudo.length);
        try (OutputStream saida = exchange.getResponseBody()) {
            saida.write(conteudo);
        }
    }

    private static void responder(HttpExchange exchange, int status, String mensagem)
            throws IOException {
        byte[] bytes = mensagem.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream saida = exchange.getResponseBody()) {
            saida.write(bytes);
        }
    }
}
