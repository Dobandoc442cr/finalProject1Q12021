
package specifications;

import helpers.DataHelper;
import helpers.RequestHelper;
import io.restassured.authentication.AuthenticationScheme;
import io.restassured.authentication.BasicAuthScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class RequestSpecs {

    public static RequestSpecification generateToken(){

        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();

        String token = RequestHelper.getUserToken();

        requestSpecBuilder.addHeader("Authorization", "Bearer " + token);
        return requestSpecBuilder.build();
    };

    public static RequestSpecification generateFakeToken(){
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.addHeader("Authorization", "Beasadrer wrongtoken");
        return requestSpecBuilder.build();
    };

    //Basic Auth - Examen

    public static RequestSpecification generateBasicAuth(){

        BasicAuthScheme Auth = new BasicAuthScheme();

        Auth.setUserName(DataHelper.getCommentUser().getName());
        Auth.setPassword(DataHelper.getCommentUser().getPassword());

        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setAuth(Auth);

        return requestSpecBuilder.build();
    }

    public static RequestSpecification generateFakeBasicAuth(){

        BasicAuthScheme Auth = new BasicAuthScheme();

        Auth.setUserName(DataHelper.getFakeCommentUser().getName());
        Auth.setPassword(DataHelper.getFakeCommentUser().getPassword());

        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setAuth(Auth);

        return requestSpecBuilder.build();
    }
}
