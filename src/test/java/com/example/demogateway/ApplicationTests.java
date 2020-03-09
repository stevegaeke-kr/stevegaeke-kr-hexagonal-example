package com.example.demogateway;

import io.confluent.kafka.schemaregistry.client.rest.RestService;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.SocketException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import local.test.desp.PurchaseOrder;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

//import org.apache.kafka.clients.consumer.Consumer;

@SpringBootTest
class ApplicationTests {

  private static final int KAFKA_BROKER_PORT = 9092;

  @Autowired
  KafkaTemplate<String, PurchaseOrder> kafkaTemplate;

  @Autowired
  KafkaProperties kafkaProperties;

  //@Autowired
  //DefaultKafkaConsumerFactory<String, PurchaseOrder> consumerFactory;

  //@Autowired
  //org.apache.kafka.clients.consumer.Consumer<String, PurchaseOrder> consumer;

  @Test
  void contextLoads() throws InterruptedException {
//    launchDockerCompose();
//
//    val schemaRegistryClient = new CachedSchemaRegistryClient("http://localhost:8081", 2);
//
    kafkaTemplate.send("core.purchase-order", new PurchaseOrder("po-123", "PO description"));
    kafkaTemplate.flush();
    Thread.sleep(200);

//    val consumerProperties = kafkaProperties.buildConsumerProperties();
//    ConsumerFactory<String, PurchaseOrder> consumerFactory = new DefaultKafkaConsumerFactory<>(kafkaProperties.buildConsumerProperties());
    //,
        //new StringDeserializer(), new KafkaAvroDeserializer(schemaRegistryClient));

//    ConcurrentKafkaListenerContainerFactory<String, PurchaseOrder> factory = new ConcurrentKafkaListenerContainerFactory<>();
//    factory.getContainerProperties().setAckMode(MANUAL);
//    factory.setConsumerFactory(consumerFactory);
//
//    val consumer = factory.createContainer("core.puchase-order");
//    consumer.setConcurrency(1);
//    consumer.getContainers().get(0).setupMessageListener();


//    val consumer = consumerFactory.createConsumer();
//    consumer.subscribe(List.of("core.purchase-order"));
//
//    kafkaTemplate.send("core.purchase-order", new PurchaseOrder("po-123", "PO description"));
//    kafkaTemplate.flush();
//
//    ConsumerRecord<?, ?> singleRecord = KafkaTestUtils
//        .getOneRecord("localhost:9092", "foo", "core.purchase-order", 0, false, true, 10l);
//
//    var records = consumer.poll(Duration.ofMillis(10000));
//
//
//    for (ConsumerRecord<String, PurchaseOrder> record : records){
//      PurchaseOrder customer = record.value();
//      System.out.println(customer);
//    }

//    val r = record.records("core.purchase-order").iterator().next();
//
//    PurchaseOrder po = r.value();
    //val payload = consumer.poll(Duration.ofMillis(10));

    System.out.println("Done");
  }

  static {
    //launchDockerCompose();
  }

  @Test
  public void launchDockerCompose() {

    val waitForLogMessage = "broker entered RUNNING state";

    if (isLocalPortInUse(KAFKA_BROKER_PORT)) {
      return;
    }

    CompletableFuture<Boolean> started = new CompletableFuture<>();

    try {

      val process = Runtime.getRuntime()
          .exec("docker-compose -f ./src/test/docker/docker-compose.yml up");

      val endMillis = Duration.ofMillis(System.currentTimeMillis())
          .plus(Duration.ofSeconds(10))
          .toMillis();
      val stdout = new StringBuilder();

      // Write stdout and stderr to console.
      // Additionally, check for a specific log message to appear to determine when
      // services are likely available.
      val stderrGobbler = new StreamGobbler(process.getErrorStream(), System.err::println);
      val stdoutGobbler = new StreamGobbler(process.getInputStream(), line -> {

        if (started.isDone()) {
          return;
        }

        stdout.append(line);
        System.out.println(line);

        if (stdout.toString().contains(waitForLogMessage)) {
          started.complete(true);
        } else if (System.currentTimeMillis() > endMillis) {
          started.complete(false);
        }

      });

      Executors.newSingleThreadExecutor().submit(stderrGobbler);
      Executors.newSingleThreadExecutor().submit(stdoutGobbler);

      if (started.get()) {

        // The Confluent schema registry takes additional time to stabilize.
        // Attempt to register and then remove a dummy schema. Once successful,
        // everything can be considered initialized.
        val schemaRegistryClient = new RestService("http://localhost:8081");
        var attempts = 20;
        while (--attempts > 0) {
          try {
            schemaRegistryClient.registerSchema("{\n"
                + "  \"type\": \"record\",\n"
                + "  \"namespace\": \"bootstrap\",\n"
                + "  \"name\": \"dummy\",\n"
                + "  \"version\": \"1\",\n"
                + "  \"fields\": [\n"
                + "    { \"name\": \"flag\", \"type\": \"boolean\" }\n"
                + "  ]\n"
                + "}", "bootstrap-value");

            if (schemaRegistryClient.getAllSubjects().contains("bootstrap-value")) {
              schemaRegistryClient.deleteSubject(Map.of(), "bootstrap-value");
              break;
            }
          } catch (SocketException | RestClientException e) {
            // Nothing to do but wait and show progress
            System.out.print(".");
          }
          Thread.sleep(1000);
        }

        System.out.println("\n>>> docker-compose up");
        return;
      }

      System.out.println("\n>>> docker-compose aborting");
      process.destroy();

    } catch (IOException | InterruptedException | ExecutionException e) {
      started.complete(false);
    }
    System.out.println();
  }

  private static boolean isLocalPortInUse(int port) {
    try {
      // ServerSocket try to open a LOCAL port
      new ServerSocket(port).close();
      // local port can be opened, it's available
      return false;
    } catch (IOException e) {
      // local port cannot be opened, it's in use
      return true;
    }
  }

  private static class StreamGobbler implements Runnable {
    private InputStream inputStream;
    private Consumer<String> consumer;

    StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
      this.inputStream = inputStream;
      this.consumer = consumer;
    }

    @Override
    public void run() {
      new BufferedReader(new InputStreamReader(inputStream)).lines()
          .forEach(consumer);
    }
  }
}
