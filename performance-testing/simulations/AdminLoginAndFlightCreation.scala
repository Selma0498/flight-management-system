
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class AdminLoginAndFlightCreation extends Simulation {

	val httpProtocol = http
		.baseUrl("http://localhost:8080")
		.inferHtmlResources()
		.acceptHeader("application/json, text/plain, */*")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-US,en;q=0.9,hr;q=0.8")
		.doNotTrackHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.102 Safari/537.36")

	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Sec-Fetch-Dest" -> "document",
		"Sec-Fetch-Mode" -> "navigate",
		"Sec-Fetch-Site" -> "none",
		"Sec-Fetch-User" -> "?1",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_1 = Map(
		"Accept" -> "text/css,*/*;q=0.1",
		"Sec-Fetch-Dest" -> "style",
		"Sec-Fetch-Mode" -> "no-cors",
		"Sec-Fetch-Site" -> "same-origin")

	val headers_2 = Map(
		"Accept" -> "*/*",
		"Sec-Fetch-Dest" -> "script",
		"Sec-Fetch-Mode" -> "no-cors",
		"Sec-Fetch-Site" -> "same-origin")

	val headers_3 = Map(
		"Accept" -> "image/avif,image/webp,image/apng,image/*,*/*;q=0.8",
		"Sec-Fetch-Dest" -> "image",
		"Sec-Fetch-Mode" -> "no-cors",
		"Sec-Fetch-Site" -> "same-origin")

	val headers_5 = Map(
		"Sec-Fetch-Dest" -> "empty",
		"Sec-Fetch-Mode" -> "cors",
		"Sec-Fetch-Site" -> "same-origin")

	val headers_9 = Map(
		"Accept" -> "*/*",
		"Sec-Fetch-Dest" -> "empty",
		"Sec-Fetch-Mode" -> "cors",
		"Sec-Fetch-Site" -> "same-origin")

	val headers_10 = Map(
		"Content-Type" -> "application/json",
		"Origin" -> "http://localhost:8080",
		"Sec-Fetch-Dest" -> "empty",
		"Sec-Fetch-Mode" -> "cors",
		"Sec-Fetch-Site" -> "same-origin")

	val headers_11 = Map(
		"Sec-Fetch-Dest" -> "empty",
		"Sec-Fetch-Mode" -> "cors",
		"Sec-Fetch-Site" -> "same-origin",
		"authorization" -> "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1dGgiOiJST0xFX0FETUlOLFJPTEVfVVNFUiIsImV4cCI6MTYwMDYxNDg3MH0.DL0DttuCrWS7-hjTjYuxyQAX67WebL4knvYPay3n4-f5N0RL7jRYLzfyDlhdL8jrKTI6QucOh3yuagkkqLRvvw")

	val headers_15 = Map(
		"Content-Type" -> "application/json",
		"Origin" -> "http://localhost:8080",
		"Sec-Fetch-Dest" -> "empty",
		"Sec-Fetch-Mode" -> "cors",
		"Sec-Fetch-Site" -> "same-origin",
		"authorization" -> "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1dGgiOiJST0xFX0FETUlOLFJPTEVfVVNFUiIsImV4cCI6MTYwMDYxNDg3MH0.DL0DttuCrWS7-hjTjYuxyQAX67WebL4knvYPay3n4-f5N0RL7jRYLzfyDlhdL8jrKTI6QucOh3yuagkkqLRvvw")



	val scn = scenario("AdminLoginAndFlightCreation")
		.exec(http("request_0")
			.get("/")
			.headers(headers_0)
			.resources(http("request_1")
			.get("/content/css/loading.css")
			.headers(headers_1),
            http("request_2")
			.get("/app/global.bundle.js")
			.headers(headers_2),
            http("request_3")
			.get("/content/images/logo-jhipster.png")
			.headers(headers_3),
            http("request_4")
			.get("/app/main.bundle.js")
			.headers(headers_2)))
		.pause(1)
		.exec(http("request_5")
			.get("/management/info")
			.headers(headers_5)
			.resources(http("request_6")
			.get("/api/account")
			.headers(headers_5)
			.check(status.is(401)),
            http("request_7")
			.get("/content/cfd81fbabebba3d187b7f0243b971186.png")
			.headers(headers_3),
            http("request_8")
			.get("/content/b28b345d7488ac0a22a5a9945a4eba45.png")
			.headers(headers_3),
            http("request_9")
			.get("/manifest.webapp")
			.headers(headers_9)))
		.pause(9)
		.exec(http("request_10")
			.post("/api/authenticate")
			.headers(headers_10)
			.body(RawFileBody("adminloginandflightcreation/0010_request.json"))
			.resources(http("request_11")
			.get("/api/account")
			.headers(headers_11)))
		.pause(4)
		.exec(http("request_12")
			.get("/app/1.chunk.js")
			.headers(headers_2)
			.resources(http("request_13")
			.get("/services/flights/api/flights")
			.headers(headers_11)))
		.pause(4)
		.exec(http("request_14")
			.get("/services/flights/api/airports")
			.headers(headers_11))
		.pause(50)
		.exec(http("request_15")
			.post("/services/flights/api/flights")
			.headers(headers_15)
			.body(RawFileBody("adminloginandflightcreation/0015_request.json"))
			.resources(http("request_16")
			.get("/services/flights/api/flights")
			.headers(headers_11),
            http("request_17")
			.get("/services/notifications/api/notify?notificationType=FLIGHT_UPDATED")
			.headers(headers_11)))

	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}