import java.net.InetSocketAddress;
import java.io.OutputStream;
import java.nio.charset.*;
import com.sun.net.httpserver.HttpServer;

public class Servidor {

    public static void main(String[] args) throws  Exception {

        // Escuta em todas as interfaces de rede
        HttpServer servidor = HttpServer.create(new InetSocketAddress("0.0.0.0", 8080), 0);

        // rotas fixas
        servidor.createContext("/", troca -> enviarResposta(troca,"Olá Mundo"));
        servidor.createContext("/sobre", troca -> enviarResposta(troca,"<h1> Sobre <h1>"));
        servidor.createContext("/contato", troca -> enviarResposta(troca,"<h1> Contato <h1>"));
        servidor.createContext("/ajuda", troca -> enviarResposta(troca,"<h1> Ajuda <h1>"));
        servidor.createContext("/servicos", troca -> enviarResposta(troca,"<h1> Serviços <h1>"));
        servidor.createContext("/agradecimentos", troca -> enviarResposta(troca,"<h1> Agradecimentos <h1>"));

        //musica serie jogo
        servidor.createContext("/jogos", troca -> enviarResposta(troca, "<h1>Top 5 Jogos !</h1> <ul> <li> Marvel Rivals </li> <li> Detroit: Become Human </li> <li> Red Dead Redemption 2 </li> <li> God Of War 2 </li> <li> Resident Evil: Remake </li> <ul> <a href=\"/musicas\">Músicas</a>\""));
        servidor.createContext("/musicas", troca -> enviarResposta(troca, "<h1>Top 5 Músicas !</h1> <ul> <li> Anjos -Venere Vai Venus </li> <li> Sparks - Coldplay </li> <li> Lover You Should've Come Over - Jeff Buckley </li> <li> Back To Friends - Sombr </li> <li> Fake Plactic Trees - Radiohead </li> <ul> <a href=\"/filmes\">Filmes</a>\""));
        servidor.createContext("/filmes", troca -> enviarResposta(troca, "<h1>Top 5 Filmes !</h1> <ul> <li> Brilho Eterno de uma Mente sem Lembraças </li> <li> Clube da Luta </li> <li> IT: A Coisa </li> <li> 500 Dias Com Ela </li> <li> 10 Coisas que Eu Odeio Em Você </li> <ul> <a href=\"/index\">Página Inicial</a>\""));


        servidor.createContext("/index", troca -> {
            String html = """
                    <!DOCTYPE html>
                    <html lang="pt-br">
                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <title>ArielAula</title>
                    
                        <style>
                            h1 {
                                color: hotpink;
                                background-color: rebeccapurple;
                            }
                    
                            body{
                                background: linear-gradient(135deg, purple, rebeccapurple);
                                text-align: center;
                            }
                    
                            .botao{
                                display:inline-block;
                                margin: 15px;
                                padding: 20px;
                                background-color: rebeccapurple;
                                color: hotpink;
                                text-decoration: none;
                                border-radius: 30px;
                                box-shadow: 0 4px 10px black;
                            }
                    
                            .botao:hover{
                                background-color:darkorchid;
                                box-shadow: 0 4px 10px black;
                            }
                    
                    
                        </style>
                    
                    </head>
                    <body>
                        <h1> Bem-vindo ao portal da Mars <3 </h1>
                        <a href = "/jogos" class="botao"> Jogos </a>
                        <a href = "/filmes" class="botao"> Filmes </a>
                        <a href = "/musicas" class="botao"> Musicas </a>
                    
                    </body>
                    </html>
                    """;
                    enviarResposta(troca, html);
                });


        //rotas dinamicas (vamos enviar um valor para o servidor e ele retornará uma resposta)      /boasvindas?nome=MARS
        servidor.createContext("/boasvindas", troca -> {
            String consulta = troca.getRequestURI().getQuery();
            String nome = consulta.replace("nome=",""); //tiro a palavra nome da url
            String resposta = "Seja bem-vindo " + nome;
            enviarResposta(troca, resposta);
                });



        //iniciar o servidor
        servidor.start();

        //manter o serv ativo
        while(true){
            Thread.sleep(1000);
        }
    }

    public static void enviarResposta(com.sun.net.httpserver.HttpExchange troca, String resposta) throws java.io.IOException{
        byte[] bytes; //bytes com colchete aceita uma cadeia de caracteres
        bytes = resposta.getBytes(StandardCharsets.UTF_8); //para aceitar caracteres especiais

        // confimação de que deu certo o tamanho
        troca.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        troca.sendResponseHeaders(200,bytes.length);

        //envio para o cliente do resultado
        try(OutputStream os = troca.getResponseBody()){
            //escreve a mensagem para o cliente
            os.write(bytes);
        }
    }

