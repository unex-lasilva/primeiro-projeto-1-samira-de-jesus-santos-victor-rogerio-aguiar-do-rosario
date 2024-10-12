package br.com.mangarosa.config; //imports
// FEITO POR SAMIRA
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class RedisConfig {
    private static JedisPool jedisPool;

    // configura a conexao c o redis
    public static void init() {
        if (jedisPool == null) {
            try {
                jedisPool = new JedisPool("localhost", 6379);  // conecta ao Redis na porta padrão (q pode ser mudada caso esteja em uso)
                System.out.println("Conexão com o Redis estabelecida.");  //msg imprimida qnd a conexao da certo
            } catch (JedisConnectionException e) {
                System.err.println("Erro ao conectar ao Redis: " + e.getMessage()); //msg imprimida caso de erro de conexao
            }
        }
    }

    // obtem o jedis
    public static Jedis getJedis() {
        if (jedisPool == null) {
            init(); // inicializa o pool se ainda não tiver feito
        }
        return jedisPool.getResource();
    }

    // fecha a conexao
    public static void closePool() {
        if (jedisPool != null) {
            jedisPool.close();
            System.out.println("Conexão com o Redis fechada.");
        }
    }
}
