package se.project.id2208.server;

import org.apache.jena.ontology.OntModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.StringWriter;

/**
 * Defines the ontology.
 */
@RestController
public class FlightOntology {
  // TYPES
  public static final String Airport = Util.ABS_ONTOLOGY_NS + "Airport";
  public static final String Flights = Util.ABS_ONTOLOGY_NS + "Flights";
  public static final String Flight = Util.ABS_ONTOLOGY_NS + "Flight";

  // OBJECT PROPERTIES
  public static final String hasAirline = Util.ABS_ONTOLOGY_NS + "hasAirline";
  public static final String hasDestination = Util.ABS_ONTOLOGY_NS + "hasDeparture";
  public static final String hasFlight = Util.ABS_ONTOLOGY_NS + "hasFlight";
  public static final String hasFlights = Util.ABS_ONTOLOGY_NS + "hasFlights";
  public static final String hasLocation = "http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#hasLocation";

  // DATA PROPERTIES
  public static final String hasName = Util.ABS_ONTOLOGY_NS + "hasName";

  private static final OntModel ontModel = Util.readOntology(Util.FLIGHT_ONTOLOGY_PATH);

  /**
   * Getter for the ontology model from a file.
   *
   * @return The ontology model.
   */
  public static OntModel getOntModel() {
    return ontModel;
  }

  private String flightOntology() {
    StringWriter stringWriter = new StringWriter();
    ontModel.write(stringWriter, "RDF/XML");

    return stringWriter.toString();
  }

  /**
   * Returns the flight ontology in HTML format.
   *
   * @return HTML of the flight ontology.
   */
  @RequestMapping(
      value = Util.ONTOLOGY_URI,
      produces = MediaType.TEXT_HTML_VALUE,
      method = RequestMethod.GET)
  public String flightOntologyHTML() {
    return flightOntology();
  }

  /**
   * Returns the flight ontology in RDF/XML format.
   *
   * @return RDF/XML of the flight ontology.
   */
  @RequestMapping(value = Util.ONTOLOGY_URI,
      produces = "application/rdf+xml",
      method = RequestMethod.GET)
  public String flightOntologyRDF() {
    return flightOntology();
  }
}
