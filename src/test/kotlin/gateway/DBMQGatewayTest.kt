package gateway

import com.flink.gateway.DBGateway
import com.flink.models.LogModel
import com.flink.models.UserModel
import com.flink.models.UserModelType.MANAGER
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.litote.kmongo.findOne

class DBMQGatewayTest {
    lateinit var gateway: DBGateway

    @Before
    fun setup() {
        gateway = DBGateway()
    }

    @Test
    fun testConnection() {
        assertNotNull(gateway.client.listDatabaseNames())
    }

    @Test
    fun testLogMessage() {
        val user = UserModel(
                username = "john",
                type = MANAGER
        )
        val message = LogModel(
                user = user,
                message = "hello world"
        )

        gateway.logDatabase.insertOne(message)

        val results = gateway.logDatabase.findOne()

        assertNotNull(results)
    }
}