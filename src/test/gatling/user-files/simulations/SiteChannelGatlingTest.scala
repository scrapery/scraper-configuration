import _root_.io.gatling.core.scenario.Simulation
import ch.qos.logback.classic.{Level, LoggerContext}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.slf4j.LoggerFactory

import scala.concurrent.duration._

/**
 * Performance test for the SiteChannel entity.
 */
class SiteChannelGatlingTest extends Simulation {

    val context: LoggerContext = LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]
    // Log all HTTP requests
    //context.getLogger("io.gatling.http").setLevel(Level.valueOf("TRACE"))
    // Log failed HTTP requests
    //context.getLogger("io.gatling.http").setLevel(Level.valueOf("DEBUG"))

    val baseURL = Option(System.getProperty("baseURL")) getOrElse """http://localhost:8080"""

    val httpConf = http
        .baseURL(baseURL)
        .inferHtmlResources()
        .acceptHeader("*/*")
        .acceptEncodingHeader("gzip, deflate")
        .acceptLanguageHeader("fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3")
        .connectionHeader("keep-alive")
        .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:33.0) Gecko/20100101 Firefox/33.0")

    val headers_http = Map(
        "Accept" -> """application/json"""
    )

    val headers_http_authentication = Map(
        "Content-Type" -> """application/json""",
        "Accept" -> """application/json"""
    )

    val headers_http_authenticated = Map(
        "Accept" -> """application/json""",
        "Authorization" -> "${access_token}"
    )

    val scn = scenario("Test the SiteChannel entity")
        .exec(http("First unauthenticated request")
        .get("/api/account")
        .headers(headers_http)
        .check(status.is(401))).exitHereIfFailed
        .pause(10)
        .exec(http("Authentication")
        .post("/api/authenticate")
        .headers(headers_http_authentication)
        .body(StringBody("""{"username":"admin", "password":"admin"}""")).asJSON
        .check(header.get("Authorization").saveAs("access_token"))).exitHereIfFailed
        .pause(1)
        .exec(http("Authenticated request")
        .get("/api/account")
        .headers(headers_http_authenticated)
        .check(status.is(200)))
        .pause(10)
        .repeat(2) {
            exec(http("Get all siteChannels")
            .get("/scrapersetting/api/site-channels")
            .headers(headers_http_authenticated)
            .check(status.is(200)))
            .pause(10 seconds, 20 seconds)
            .exec(http("Create new siteChannel")
            .post("/scrapersetting/api/site-channels")
            .headers(headers_http_authenticated)
            .body(StringBody("""{"id":null, "url":"SAMPLE_TEXT", "contentType":null, "schedule":"SAMPLE_TEXT", "scheduleTimeZone":"SAMPLE_TEXT", "totalLevel":"0", "archiveLevel":"0", "unlimitedLevel":null, "fetchEngine":null, "category":"SAMPLE_TEXT", "tag":"SAMPLE_TEXT", "categorySlug":"SAMPLE_TEXT", "tagSlug":"SAMPLE_TEXT", "countryCode":"SAMPLE_TEXT", "languageCode":"SAMPLE_TEXT", "targetQueueChannel":"SAMPLE_TEXT", "topics":"SAMPLE_TEXT", "topicSlugs":"SAMPLE_TEXT", "postType":null, "rankingCountry":"0", "channelTotalLevel":"0", "channelArchiveLevel":"0", "channelFetchEngine":null, "channelRanking":"0", "channelTargetQueue":"SAMPLE_TEXT", "channelTargetPostType":null, "channelLevelOneFetchEngine":null, "channelLevelTwoFetchEngine":null, "channelLevelThreeFetchEngine":null, "channelLevelFourFetchEngine":null, "channelLevelOneContentType":null, "channelLevelTwoContentType":null, "channelLevelThreeContentType":null, "channelLevelFourContentType":null, "channelAllowExternalUrl":null, "channelLogo":"SAMPLE_TEXT", "channelSiteName":"SAMPLE_TEXT", "logo":"SAMPLE_TEXT", "siteName":"SAMPLE_TEXT", "channelType":null, "levelOneFetchEngine":null, "levelTwoFetchEngine":null, "levelThreeFetchEngine":null, "levelFourFetchEngine":null, "levelOneContentType":null, "levelTwoContentType":null, "levelThreeContentType":null, "levelFourContentType":null, "allowExternalUrl":null, "siteUrl":"SAMPLE_TEXT", "targetChannel":null, "target":null, "siteDomain":"SAMPLE_TEXT"}""")).asJSON
            .check(status.is(201))
            .check(headerRegex("Location", "(.*)").saveAs("new_siteChannel_url"))).exitHereIfFailed
            .pause(10)
            .repeat(5) {
                exec(http("Get created siteChannel")
                .get("/scrapersetting${new_siteChannel_url}")
                .headers(headers_http_authenticated))
                .pause(10)
            }
            .exec(http("Delete created siteChannel")
            .delete("/scrapersetting${new_siteChannel_url}")
            .headers(headers_http_authenticated))
            .pause(10)
        }

    val users = scenario("Users").exec(scn)

    setUp(
        users.inject(rampUsers(Integer.getInteger("users", 100)) over (Integer.getInteger("ramp", 1) minutes))
    ).protocols(httpConf)
}
