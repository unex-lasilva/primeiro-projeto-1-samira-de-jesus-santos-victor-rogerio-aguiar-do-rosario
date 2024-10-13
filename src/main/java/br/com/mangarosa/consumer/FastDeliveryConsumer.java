

package br.com.mangarosa.consumer; //imports
//FEITO POR SAMIRA
import br.com.mangarosa.config.RedisConfig;
import redis.clients.jedis.Jedis;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
// por algum motivo chamado (nao sei como) ou so burrice eu n consegui deixar em ordem a consumação dos pedidos mas oq importa eh a tentativa!
public class FastDeliveryConsumer {
    private static final String TOPIC = "queue/long-distance-items"; //topico
    private static final int TTL_SECONDS = 300; // 5 minutos pra a msg expirar

    public static void main(String[] args) {
        RedisConfig.init(); //iniciar redis
        // fechar jedis
        try (Jedis jedis = RedisConfig.getJedis()) {
            while (true) {
                String mensagem = jedis.lpop(TOPIC);

                if (mensagem != null) {
                    // isso aq faz o horario em que a msg é consumida x 15min pra definir o horario de entrega
                    LocalDateTime agora = LocalDateTime.now();
                    LocalDateTime entregaPrevista = agora.plusMinutes(15);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                    String horarioPrevisto = entregaPrevista.format(formatter);
                    System.out.println("PEDIDO EM PREPARAÇÃO. ENTREGA PREVISTA: " + horarioPrevisto + ". Detalhes: " + mensagem);
                    Thread.sleep(2000); // aguarda 2 segundos para simular o processamento do pedido
                } else {
                    System.out.println("Fila vazia, aguardando novos pedidos...");
                    try {
                        Thread.sleep(50000); // aguarda 50 segundos antes de verificar novamente se há mensagens, se n vai imprimir a msg de fila vazia
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao conectar ao Redis: " + e.getMessage()); //msg de erro q vai ser imprimida caso n consiga conectar no redis
        }

    }
}
