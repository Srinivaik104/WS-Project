package se.project.id2208.server;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;

/**
 * In-memory definition of an airport which can be converted to RDF.
 */
public class Airport extends RDFModel {
  private final String name;
  private final String location;
  private final Flights flights = new Flights(this);

  public Airport(String name, String location) {
    this.name = name;
    this.location = location;
  }

  public Airport(String name) {
    this.name = name;
    this.location = null;
  }

  @Override
  public String getResourceURI() {
    return NS + "airport/" + name;
  }

  /**
   * Getter for the name of this airport.
   *
   * @return The name of this airport.
   */
  public String getName() {
    return name;
  }

  /**
   * Adds a flight.
   *
   * @param destination The airport where the destination is going to.
   */
  public void addFlight(Airport destination, String airline) {
    flights.addFlight(new Flight(destination, airline));
  }

  /**
   * Returns the available flights from this airport.
   */
  public Flights getFlights() {
    return flights;
  }

  @Override
  protected Resource toRDF(OntModel ontModel, Model rdfModel) {
    Resource airport = rdfModel.createResource(getResourceURI());

    airport.addProperty(RDF.type, ontModel.getOntClass(FlightOntology.Airport));

    airport.addProperty(ontModel.getProperty(FlightOntology.hasName), name);

    // Add location if it exists
    if (location != null) {
      airport.addProperty(ontModel.getProperty(FlightOntology.hasLocation), ontModel.createResource(location));
    }

    airport.addProperty(ontModel.getProperty(FlightOntology.hasFlights), flights.toRDF());

    return airport;
  }
}
