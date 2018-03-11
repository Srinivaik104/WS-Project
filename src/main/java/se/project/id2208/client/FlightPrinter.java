package se.project.id2208.client;

import org.apache.jena.query.QuerySolution;

import java.util.Collections;
import java.util.List;

class FlightPrinter {
  private final FlightAgent flightAgent;

  private String newLineAtMaxLen(int maxLen, String input) {
    StringBuilder builder = new StringBuilder(input);
    int i = 0;
    while ((i = builder.indexOf(" ", i + maxLen)) != -1) {
      builder.replace(i, i + 1, "\n");
    }

    return builder.toString();
  }

  private void printFlight(String from, QuerySolution to) {
    System.out.println("Source:\t" + from);
    System.out.println("Destination:\t\t" + to.getResource("departure"));
    System.out.println("Airline:\t\t\t" + to.getResource("airline"));
    String cityURI = flightAgent.getAirportCityURI(to.getResource("departure").getURI());
    if (cityURI != null) {
      QuerySolution city = flightAgent.getCityInformation(cityURI);
      System.out.println("Some Information on the city:\n" + newLineAtMaxLen(50, city.getLiteral("comment").toString()));
      System.out.println("For more Information on the city, please follow the link :\t" + cityURI);
    }
  }

  private void printItinerary(List<QuerySolution> path, String from) {
    // Reverse the order before printing
    Collections.reverse(path);

    System.out.println("\n##Your Itinerary!##");

    printFlight(from, path.get(0));

    for (int i = 1; i < path.size(); i++) {
        System.out.println("--FLIGHT"+i+"--");
        printFlight(path.get(i - 1).getResource("departure").getURI(), path.get(i));
    }
  }

  FlightPrinter(List<List<QuerySolution>> itineraries, String from, String to, FlightAgent flightAgent) {
    this.flightAgent = flightAgent;

    System.out.println("Finding all possible flights to destination");
    System.out.println("From:\t" + from);
    System.out.println("To:\t\t" + to);

    for (List<QuerySolution> itinerary : itineraries) {
      printItinerary(itinerary, from);
    }
  }
}
