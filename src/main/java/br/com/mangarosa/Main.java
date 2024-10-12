package br.com.mangarosa; // FEITO POR SAMIRA
import br.com.mangarosa.consumer.FoodDeliveryConsumer; //imports
import br.com.mangarosa.consumer.FastDeliveryConsumer;
import br.com.mangarosa.consumer.LongDistanceConsumer;
import br.com.mangarosa.consumer.PyMarketPlaceConsumer;
import br.com.mangarosa.producer.PhysicPersonDeliveryProducer;
import br.com.mangarosa.producer.PyMarketPlaceProducer;
import br.com.mangarosa.producer.FastDeliveryProducer;
import br.com.mangarosa.producer.FoodDeliveryProducer;
import br.com.mangarosa.config.RedisConfig;
import redis.clients.jedis.Jedis;

public class Main { //clase principal e de execução do projeto

    public static void main(String[] args) {
        System.out.println("Iniciando o projeto...");

        // inicializa a configuração do redis
        RedisConfig.init();

        // usa o Jedis para realizar alguma operação inicial, caso precise
        try (Jedis jedis = RedisConfig.getJedis()) {
            if (jedis != null) {

                jedis.set("status", "projeto iniciado");
                System.out.println("Status: " + jedis.get("status"));
            }
        } catch (Exception e) {
            System.err.println("Erro ao conectar com Redis: " + e.getMessage());
        }

        // inicia os produtores em threads

        new Thread(() -> FastDeliveryProducer.main(null)).start();
        new Thread(() -> FoodDeliveryProducer.main(null)).start();
        new Thread(() -> PhysicPersonDeliveryProducer.main(null)).start();
        new Thread(() -> PyMarketPlaceProducer.main(null)).start();

        // iniciar os consumidores em threads
        new Thread(() -> FastDeliveryConsumer.main(null)).start();
        new Thread(() -> FoodDeliveryConsumer.main(null)).start();
        new Thread(() -> LongDistanceConsumer.main(null)).start();
        new Thread(() -> PyMarketPlaceConsumer.main(null)).start();

        // fecha as conexões quando o programa terminar
        Runtime.getRuntime().addShutdownHook(new Thread(RedisConfig::closePool));
    }
}
