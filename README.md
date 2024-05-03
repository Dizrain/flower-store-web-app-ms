Microservices architecture for a flower store web application named "Flower Store Web App MS".
This application is structured into several microservices, each responsible for handling different aspects of the
application,
such as customer management, product management, order processing, and inventory management.
The application is built using Spring Boot, leveraging RESTful APIs for communication between services.

In the context of the Flower Store application, a bouquet refers to a product that the flower store sells.
The Product class represents a bouquet in the application.
The attributes of the Product class, such as color, type, and inSeason, provide details about the bouquet.
For example, color could represent the dominant color of the flowers in the bouquet, type could represent the type of
flowers in the bouquet.  
When a customer browses the flower store, they would see a list of bouquets (products).
The type attribute in the Product class could represent the type of flower in the bouquet. For example, it could be "
Rose", "Tulip", "Orchid", etc. The inSeason attribute is a boolean that could represent whether the flowers in the
bouquet are currently in season or not. If a flower is in season, it means that it is currently the time of year when
the flower naturally blooms and is readily available. This could be useful information for customers, as flowers that
are in season could be fresher and potentially less expensive than those that are out of season.
For the sake of simplicity, in the current implementation, a bouquet (represented by the Product
class) does not support multiple types of flowers. The type attribute in the Product class is a single String, which
means it can only hold one type of flower.