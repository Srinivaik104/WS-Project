package se.project.id2208.server;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.StringWriter;

@RestController
public class AirportView {
  // Get the airportController instance -> not sure if rest controllers
  // are singleton by default in spring.
  private AirportController airportController = AirportController.getInstance();

  @RequestMapping(value = Util.RDF_URI + "airport/{airport}",
      produces = "application/rdf+xml",
      method = RequestMethod.GET)
  public String getAirport(@PathVariable(value = "airport") String airport) {
    StringWriter stringWriter = new StringWriter();

    airportController
        .getAirport(airport)
        .getModel()
        .write(stringWriter, "RDF/XML");

    return stringWriter.toString();
  }

  @RequestMapping(value = Util.RDF_URI + "airport/{airport}/flights",
      produces = "application/rdf+xml",
      method = RequestMethod.GET)
  public String getFlights(@PathVariable(value = "airport") String airport) {
    StringWriter stringWriter = new StringWriter();

    airportController
        .getAirport(airport)
        .getFlights()
        .getModel()
        .write(stringWriter, "RDF/XML");

    return stringWriter.toString();
  }
}
