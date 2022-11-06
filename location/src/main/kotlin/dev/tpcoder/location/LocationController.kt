package dev.tpcoder.location

import org.slf4j.LoggerFactory
import org.springframework.messaging.handler.annotation.*
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import java.time.Duration

@Controller
class LocationController {

    private val logger = LoggerFactory.getLogger(LocationController::class.java)

    @MessageMapping(value = ["locations.{driver}"])
    fun mockLocation(@DestinationVariable driver: String): Flux<Location> {
        logger.info("Reach RSocket! with driverId:$driver")
        return Flux.interval(Duration.ofSeconds(1)).take(5)
            .map { randomGeoPoint() }
    }

    @MessageMapping("collectLocation")
    fun collectLocation(@Payload location: Location) {
        logger.info("New location: $location")
    }

    data class Location(val latitude: Double, val longitude: Double)

    fun randomGeoPoint(): Location {
        val latitude = Math.random() * 180.0 - 90.0
        val longitude = Math.random() * 360.0 - 180.0
        return Location(longitude, latitude)
    }
}