package at.ac.uibk;

import com.gc.android.market.api.MarketSession;
import com.gc.android.market.api.model.Market;

import static com.gc.android.market.api.MarketSession.Callback;
import static com.gc.android.market.api.model.Market.*;

public class Main {

    public static void main(String[] args) {
        MarketSession session = new MarketSession();
        session.login("styggje@gmail.com","hufflepuff");
        //session.getContext.setAndroidId("styggje");

        String query = "maps";
        AppsRequest appsRequest = AppsRequest.newBuilder()
                .setQuery(query)
                .setStartIndex(0).setEntriesCount(10)
                .setWithExtendedInfo(true)
                .build();

        session.append(appsRequest, new Callback<AppsResponse>() {
            @Override
            public void onResult(ResponseContext context, AppsResponse response) {
                System.out.println(response);
            }
        });
        session.flush();

        CommentsRequest commentsRequest = CommentsRequest.newBuilder()
                .setAppId("7065399193137006744")
                .setStartIndex(0)
       //         .setEntriesCount(10)
                .build();

        session.append(commentsRequest, new Callback<Market.CommentsResponse>() {
            @Override
            public void onResult(ResponseContext context, CommentsResponse response) {
                System.out.println("Response : " + response);
                // response.getComments(0).getAuthorName()
                // response.getComments(0).getCreationTime()
                // ...
            }
        });

        session.flush();
    }
}
