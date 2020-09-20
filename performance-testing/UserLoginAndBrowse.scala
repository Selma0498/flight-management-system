
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class UserLoginAndBrowse extends Simulation {

	val httpProtocol = http
		.baseUrl("http://localhost:8080")
		.inferHtmlResources(BlackList(), WhiteList())
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
		"authorization" -> "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTYwMDYxMDEwM30.kw8i87gCXskLsKC2xiS7Enons-tzlbCITmzCG5EaCAKAJWdjGU4jlKUNPKfCgYphP4n0ebv6NQYYdTxfixBTCQ")

	val headers_2 = Map(
		"Accept" -> "*/*",
		"Sec-Fetch-Dest" -> "script",
		"Sec-Fetch-Mode" -> "no-cors",
		"Sec-Fetch-Site" -> "same-origin")



	val scn = scenario("UserLoginAndBrowse")
		.exec(http("request_0")
			.post("/api/authenticate")
			.headers(headers_0)
			.body(RawFileBody("userloginandbrowse/0000_request.json"))
			.resources(http("request_1")
			.get("/api/account")
			.headers(headers_1)))
		.pause(4)
		.exec(http("request_2")
			.get("/app/1.chunk.js")
			.headers(headers_2)
			.resources(http("request_3")
			.get("/services/flights/api/flights")
			.headers(headers_1)))
		.pause(8)
		.exec(http("request_4")
			.get("/services/flights/api/flights/5")
			.headers(headers_1))
		.pause(8)
		.exec(http("request_5")
			.get("/services/flights/api/flights")
			.headers(headers_1))
		.pause(8)
		.exec(http("request_6")
			.get("/services/flights/api/flights/8")
			.headers(headers_1))
		.pause(5)
		.exec(http("request_7")
			.get("/services/flights/api/flights")
			.headers(headers_1))
		.pause(4)
		.exec(http("request_8")
			.get("/app/4.chunk.js")
			.headers(headers_2)
			.resources(http("request_9")
			.get("/services/bookings/api/bookings")
			.headers(headers_1)))
		.pause(4)
		.exec(http("request_10")
			.get("/services/flights/api/flights")
			.headers(headers_1))

	setUp(scn.inject(atOnceUsers(1000))).protocols(httpProtocol)
}