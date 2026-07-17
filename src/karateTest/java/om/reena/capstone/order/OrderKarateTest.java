package om.reena.capstone.order;

import io.karatelabs.junit6.Karate;

class OrderKarateTest {

    @Karate.Test
    Karate testOrderApi() {
        return Karate.run("classpath:karate/create-order.feature");
    }
}
