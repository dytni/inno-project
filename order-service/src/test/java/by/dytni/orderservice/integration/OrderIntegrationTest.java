package by.dytni.orderservice.integration;

import static by.dytni.orderservice.OrderServiceTestConstants.ITEM_PRICE;
import static by.dytni.orderservice.OrderServiceTestConstants.ITEM_QUANTITY;
import static by.dytni.orderservice.OrderServiceTestConstants.ORDER_PRICE;
import static by.dytni.orderservice.OrderServiceTestConstants.ORDER_STATUS;
import static by.dytni.orderservice.OrderServiceTestConstants.USER_ID;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import by.dytni.orderservice.config.SecurityTestConfig;
import by.dytni.orderservice.dto.item.Item;
import by.dytni.orderservice.dto.item.ItemMaker;
import by.dytni.orderservice.dto.order.Order;
import by.dytni.orderservice.dto.order.OrderMaker;
import by.dytni.orderservice.dto.order.OrderUpdater;
import by.dytni.orderservice.dto.orderItem.OrderItemMaker;
import by.dytni.orderservice.dto.orderItem.OrderItemUpdater;
import by.dytni.orderservice.repository.entity.OrderStatus;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@Import({
        SecurityTestConfig.class
})
public class OrderIntegrationTest {

    private static final String BASE_URL = "/api/order";

    private static final String POSTGRES_PASSWORD = UUID.randomUUID().toString();

    @Container
    public static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword(POSTGRES_PASSWORD);

    @Container
    static GenericContainer redis = new GenericContainer(DockerImageName.parse("redis:7-alpine"))
            .withExposedPorts(6379);


    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
    }

    private Item item;

    @BeforeEach
    void setUp() {
        item = createItem();
    }


    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void create_order() {
        OrderMaker request = createOrderRequest();

        ResponseEntity<Order> response =
                restTemplate.postForEntity(BASE_URL, request, Order.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Order body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.getId()).isNotNull();
        assertThat(body.getTotalPrice().compareTo(ORDER_PRICE)).isEqualTo(0);
    }

    @Test
    void get_order_by_id() {
        Order created = createOrder();

        assertThat(created).isNotNull();

        ResponseEntity<Order> response =
                restTemplate.getForEntity(BASE_URL + "/" + created.getId(), Order.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Order body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.getId()).isEqualTo(created.getId());
    }

    @Test
    void update_order() {
        Order created = createOrder();
        assertThat(created).isNotNull();
        OrderUpdater updater = OrderUpdater.builder()
                .status(OrderStatus.APPROVED)
                .orderItems(createOrderItemUpdaters())
                .build();

        HttpEntity<OrderUpdater> entity = new HttpEntity<>(updater);


        ResponseEntity<Order> response = restTemplate.exchange(
                BASE_URL + "/" + created.getId(),
                HttpMethod.PUT,
                entity,
                Order.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Order body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.getId()).isEqualTo(created.getId());
        assertThat(body.getStatus()).isEqualTo(OrderStatus.APPROVED);
    }

    @Test
    void get_all_orders() {
        createOrder();
        ResponseEntity<String> response =
                restTemplate.getForEntity(BASE_URL, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private OrderMaker createOrderRequest() {
        return OrderMaker.builder()
                .userId(USER_ID)
                .status(ORDER_STATUS)
                .orderItems(createOrderItems())
                .build();
    }

    private List<OrderItemMaker> createOrderItems() {
        return List.of(OrderItemMaker.builder()
                               .itemId(item.getId())
                               .quantity(ITEM_QUANTITY)
                               .build());
    }

    private List<OrderItemUpdater> createOrderItemUpdaters() {
        return List.of(OrderItemUpdater.builder()
                               .itemId(item.getId())
                               .quantity(ITEM_QUANTITY)
                               .build());
    }



    private Order createOrder() {
        return restTemplate
                .postForEntity(BASE_URL, createOrderRequest(), Order.class)
                .getBody();
    }



    private Item createItem() {
        return restTemplate
                .postForEntity(BASE_URL + "/item", createItemMaker(), Item.class)
                .getBody();
    }

    private ItemMaker createItemMaker() {
        return ItemMaker.builder()
                .name(UUID.randomUUID().toString())
                .price(ITEM_PRICE)
                .build();
    }
}
