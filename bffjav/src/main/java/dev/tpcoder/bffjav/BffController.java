package dev.tpcoder.bffjav;

import dev.tpcoder.bffjav.client.DriverClient;
import dev.tpcoder.bffjav.client.LocationClient;
import dev.tpcoder.bffjav.model.Driver;
import dev.tpcoder.bffjav.model.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/bff")
public class BffController {
    private final DriverClient driverClient;
    private final LocationClient locationClient;
    private final Logger logger;

    public BffController(DriverClient driverClient, LocationClient locationClient) {
        this.driverClient = driverClient;
        this.locationClient = locationClient;
        this.logger = LoggerFactory.getLogger(BffController.class);
    }

    /*
     * Below for RESTful Implementation
     * */

    @GetMapping("/drivers")
    public Flux<Driver> getDrivers() {
        return driverClient.getAll();
    }

    @GetMapping("/drivers/{id}")
    public Mono<Driver> getDrivers(@PathVariable String id) {
        return driverClient.getById(id);
    }

    @PostMapping("/drivers")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> createDriver(@RequestBody Driver body) {
        System.out.println(body.toString());
        return driverClient.create(body);
    }

    @PostMapping(value = "/drivers/form", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> exampleForm(@RequestPart("name") String name, @RequestPart("file") Mono<FilePart> filePart) {
        return filePart.flatMap(file -> {
            logger.info(name);
            logger.info(file.filename());
            return driverClient.form(name, file);
        });
    }

    /*
     * Below for RSocket Implementation
     * */

    @GetMapping("/locations")
    public Flux<Location> streamLocation(@RequestParam String driverId) {
        return locationClient.trackingLocation(driverId);
    }

    @PostMapping("/locations")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> submitLocation(@RequestBody Location location) {
        return locationClient.submitLocation(location);
    }
}
