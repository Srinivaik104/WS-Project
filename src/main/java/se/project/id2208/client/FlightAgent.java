package se.project.id2208.client;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import se.project.id2208.server.Util;

import java.util.*;

public class FlightAgent extends AgentUtil {
  private Resource getFlights(String airportURI) {
    Model airportModel = getModel(airportURI);

    /**
     * SPARQL Query for selecting the flights.
     *
     * PREFIX  smp: <http://localhost:8080/id2208/project/ontology/flights#>
     *
     * SELECT  ?flightsUri
     * WHERE
     * {
     *    ?airport smp:hasFlights ?flightsURI .
     * }
     */
    String selectFlightsResource = "PREFIX smp: <" + Util.ABS_ONTOLOGY_NS + ">\n" +
        "SELECT ?flightsUri WHERE {\n" +
        "?airport smp:hasFlights ?flightsUri .\n" +
        "}";

    ResultSet results = runQuery(selectFlightsResource, airportModel);

    return results.nextSolution().getResource("flightsUri");
  }

  QuerySolution getCityInformation(String cityURI) {
    if (cityURI == null) return null;

    String selectCommentForCity = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
        "SELECT ?comment WHERE {\n" +
        "<" + cityURI + "> rdfs:comment ?comment\n" +
        "FILTER (lang(?comment) = 'en')" +
        "}";

    Model cityModel = getModel(cityURI);
    ResultSet cityResult = runQuery(selectCommentForCity, cityModel);

    // Assuming there's only a single existing solution
    return cityResult.nextSolution();
  }

  String getAirportCityURI(String airportURI) {
    Model airportModel = getModel(airportURI);

    /**
     * SPARQL Query for selecting the city if it exists.
     *
     * PREFIX dul: <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#>
     * PREFIX dbo: <http://dbpedia.org/ontology/>
     * PREFIX dbr: <http://dbpedia.org/resource/>
     *
     * SELECT  ?city
     * WHERE
     * {
     *    ?airport dul:hasLocation ?city .
     * }
     */
    String selectFlightsResource = "PREFIX dul: <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#>\n" +
        "SELECT ?city WHERE {\n" +
        "?airport dul:hasLocation ?city.\n" +
        "}";

    ResultSet results = runQuery(selectFlightsResource, airportModel);

    if (!results.hasNext()) return null;
    return results.nextSolution().getResource("city").getURI();
  }

  private ResultSet getDepartures(String flightsURI) {
    Model flightsModel = getModel(flightsURI);

    /**
     * SPARQL Query for selecting departures.
     *
     * PREFIX  smp: <http://localhost:8080/id2208/project/ontology/flights#>
     *
     * SELECT  ?departure
     *         ?airline
     * WHERE
     * {
     *    ?flight smp:hasDeparture ?departure .
     *    ?flight smp:hasAirline ?airline .
     * }
     */
    String selectFlightResource = "PREFIX smp: <" + Util.ABS_ONTOLOGY_NS + ">\n" +
        "SELECT ?departure ?airline WHERE {\n" +
        "?flight smp:hasDeparture ?departure .\n" +
        "?flight smp:hasAirline ?airline .\n" +
        "}";

    return runQuery(selectFlightResource, flightsModel);
  }

  /**
   * Adds the provided path to the provided list of itineraries.
   *
   * @param path The path to add to the itineraries.
   * @param itineraries The itineraries to add the path to.
   */
  private void addSolution(Stack<QuerySolution> path, List<List<QuerySolution>> itineraries) {
    Stack<QuerySolution> pathCpy = (Stack<QuerySolution>) path.clone();
    List<QuerySolution> itinerary = new ArrayList<>();

    while (!pathCpy.empty())
      itinerary.add(pathCpy.pop());

    itineraries.add(itinerary);
  }

  /**
   * Uses BFS search to find the closest itineraries.
   *
   * @param fromAirport The URI airport to start the search from.
   * @param toAirport The URI airport to end the search on.
   * @return All shortest itineraries.
   */
  private List<List<QuerySolution>> findItineraries(String fromAirport, String toAirport) {
    // A list where we can store our solutions.
    List<List<QuerySolution>> itineraries = new ArrayList<>();

    // FIFO set to keep track of the flights we have to take.
    Queue<QuerySolution> flightsToTake = new ArrayDeque<>();

    // Keep track of the airports we've already visited.
    Set<String> pathsTaken = new HashSet<>();

    // Keep track of the current path
    Stack<QuerySolution> path = new Stack<>();

    // Add initial flights
    ResultSet initialFlightsSol = getDepartures(getFlights(fromAirport).getURI());
    while (initialFlightsSol.hasNext()) {
      flightsToTake.add(initialFlightsSol.nextSolution());
    }

    // Start the search
    while (!flightsToTake.isEmpty()) {
      QuerySolution flight = flightsToTake.poll();
      String flightURI = flight.getResource("departure").getURI();
      // Add the current flight to the path
      path.add(flight);
      ResultSet departuresSol = getDepartures(getFlights(flightURI).getURI());

      while (departuresSol.hasNext()) {
        QuerySolution departureSol = departuresSol.nextSolution();

        String departure = departureSol.getResource("departure").getURI();
        // AIRPORT + DEPARTURE + AIRLINE = UNIQUE PATH
        String pathIdentifier = flightURI + departure + departureSol.getResource("airline").getURI();

        // This path has already been visited
        if (pathsTaken.contains(pathIdentifier)) continue;
        pathsTaken.add(pathIdentifier);

        // Check if this is a solution
        if (departure.equals(toAirport)) {
          path.push(departureSol);
          addSolution(path, itineraries);
          path.pop();
        } else {
          flightsToTake.add(departureSol);
        }
      }
    }

    return itineraries;
  }


  private FlightAgent() {
    String from = "http://localhost:8080/id2208/project/rdf/airport/jfk";
    String to = "http://localhost:8080/id2208/project/rdf/airport/blr";

    List<List<QuerySolution>> itineraries = findItineraries(from, to);

    new FlightPrinter(itineraries, from, to, this);
  }

  public static void main(String[] args) {
    new FlightAgent();
  }
}
