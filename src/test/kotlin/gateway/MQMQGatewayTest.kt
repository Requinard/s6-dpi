package gateway

import com.flink.gateway.MQGateway
import junit.framework.Assert.assertTrue
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class MQMQGatewayTest {
    lateinit var MQGateway: MQGateway
    @Before
    fun setup() {
        MQGateway = MQGateway(
                "user",
                "pass"
        )
    }

    @Test
    fun testConnection() {
        Assert.assertNotNull(MQGateway.channel)
        assertTrue(MQGateway.channel.isOpen)
    }
}