package Specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class RequestSpecOrders {

    public static RequestSpecification getSpec(){
//Declare Specs as a static method
        RequestSpecification requestSpec = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
        return requestSpec;

    }
}
