package br.com.mangarosa.producer;
//FEITO POR VICTOR
import br.com.mangarosa.config.RedisConfig;
import redis.clients.jedis.Jedis;

public class PyMarketPlaceProducer {

    private static final String TOPIC = "queue/long-distance-items"; // topico
    private static final int TTL_SECONDS = 300; // 5 minutos pra a msg expirar

    public static void main(String[] args) {
        RedisConfig.init();
        // isso aq é pra o jedis fechar
        try (Jedis jedis = RedisConfig.getJedis()) {
            // MENSAGEM
            String mensagemJson = "ID: 1237 PEDIDO: HAMBURGUER GTA VI VALOR: 53,90";

            // envia a mensagem para a fila
            jedis.lpush(TOPIC, mensagemJson);
            jedis.expire(TOPIC, TTL_SECONDS);

            System.out.println("Novo pedido recebido! ID:1237 NOME: FRAJOLA");
            Thread.sleep(2000); // aguarda 2 segundos antes de enviar o próximo
        } catch (Exception e) {
            System.err.println("Erro ao enviar mensagem para o Redis: " + e.getMessage()); //imprime msg de erro caso a msg n seja enviada pro redis
        }

    }
}

