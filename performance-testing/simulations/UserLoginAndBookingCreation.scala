
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class UserLoginAndBookingCreation extends Simulation {

	val httpProtocol = http
		.baseUrl("http://localhost:8080")
		.inferHtmlResources()
		.acceptHeader("application/json, text/plain, */*")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-US,en;q=0.9,hr;q=0.8")
		.doNotTrackHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.102 Safari/537.36")

	val headers_0 = Map(
		"Content-Type" -> "application/json",
		"Origin" -> "http://localhost:8080",
		"Sec-Fetch-Dest" -> "empty",
		"Sec-Fetch-Mode" -> "cors",
		"Sec-Fetch-Site" -> "same-origin")

	val headers_1 = Map(
		"Sec-Fetch-Dest" -> "empty",
		"Sec-Fetch-Mode" -> "cors",
		"Sec-Fetch-Site" -> "same-origin",
		"authorization" -> "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTYwMDYxNjU4MH0.5uce4IzNznwDYL8uIWD-1TSuRoosZNz4dBEh0UGULg4JkDtazv-DD5Ir34tI5PQB9HFJuXwR3vZzWr37AWNJFA")

	val headers_2 = Map(
		"Accept" -> "*/*",
		"Sec-Fetch-Dest" -> "script",
		"Sec-Fetch-Mode" -> "no-cors",
		"Sec-Fetch-Site" -> "same-origin")

	val headers_7 = Map(
		"Content-Type" -> "application/json",
		"Origin" -> "http://localhost:8080",
		"Sec-Fetch-Dest" -> "empty",
		"Sec-Fetch-Mode" -> "cors",
		"Sec-Fetch-Site" -> "same-origin",
		"authorization" -> "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTYwMDYxNjU4MH0.5uce4IzNznwDYL8uIWD-1TSuRoosZNz4dBEh0UGULg4JkDtazv-DD5Ir34tI5PQB9HFJuXwR3vZzWr37AWNJFA")



	val scn = scenario("UserLoginAndBookingCreation")
		.exec(http("request_0")
			.post("/api/authenticate")
			.headers(headers_0)
			.body(RawFileBody("userloginandbookingcreation/0000_request.json"))
			.resources(http("request_1")
			.get("/api/account")
			.headers(headers_1)))
		.pause(3)
		.exec(http("request_2")
			.get("/app/4.chunk.js")
			.headers(headers_2)
			.resources(http("request_3")
			.get("/services/bookings/api/bookings")
			.headers(headers_1)))
		.pause(6)
		.exec(http("request_4")
			.get("/app/1.chunk.js")
			.headers(headers_2)
			.resources(http("request_5")
			.get("/services/flights/api/flights")
			.headers(headers_1)))
		.pause(11)
		.exec(http("request_6")
			.get("/app/7.chunk.js")
			.headers(headers_2)
			.resources(http("request_7")
			.post("/services/bookings/api/bookings")
			.headers(headers_7)
			.body(RawFileBody("userloginandbookingcreation/0007_request.json"))))
		.pause(7)
		.exec(http("request_8")
			.get("/app/2.chunk.js")
			.headers(headers_2)
			.resources(http("request_9")
			.post("/services/luggage/api/luggages")
			.headers(headers_7)
			.body(RawFileBody("userloginandbookingcreation/0009_request.json")),
            http("request_10")
			.get("/services/payments/api/credit-cards?filter=payment-is-null")
			.headers(headers_1)))
		.pause(5)
		.exec(http("request_11")
			.post("/services/payments/api/payments")
			.headers(headers_7)
			.body(RawFileBody("userloginandbookingcreation/0011_request.json"))
			.resources(http("request_12")
			.get("/services/notifications/api/notify?notificationType=BOOKING_CONFIRMED")
			.headers(headers_1),
            http("request_13")
			.get("/services/flights/api/flights")
			.headers(headers_1)))

	setUp(scn.inject(atOnceUsers(10), constantUsersPerSec(10) during (10 seconds), rampUsers(1000) during (60 seconds))).protocols(httpProtocol)
}