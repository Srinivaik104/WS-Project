package se.project.id2208.server;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines the flights from an airport.
 */
public class Flights extends RDFModel {
  private final List<Flight> flights = new ArrayList<>();
  private final Airport airport;

  Flights(Airport airport) {
    this.airport = airport;
  }

  public void addFlight(Flight flight) {
    flights.add(flight);
  }

  @Override
  String getResourceURI() {
    return airport.getResourceURI() + "/flights";
  }

  @Override
  protected Resource toRDF(OntModel ontModel, Model rdfModel) {
    Resource flights = rdfModel.createResource(getResourceURI());

    flights.addProperty(RDF.type, ontModel.getOntClass(FlightOntology.Flights));
    flights.addProperty(RDFS.comment, "Flights from " + airport.getName());

    for (Flight flight : this.flights) {
      flight.toRDF(ontModel, rdfModel);
    }

    return flights;
  }
}
