package owner.messedup.com.messedupowner.data.remote;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import owner.messedup.com.messedupowner.data.model.MessMenu;
import owner.messedup.com.messedupowner.data.model.Offer;
import owner.messedup.com.messedupowner.data.model.OwnerData;
import owner.messedup.com.messedupowner.data.model.Statistics;
import owner.messedup.com.messedupowner.data.model.Verification;
import owner.messedup.com.messedupowner.data.model.WeekMenu;
import owner.messedup.com.messedupowner.data.model.WeekStatus;
import owner.messedup.com.messedupowner.data.model.isSuccess;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIService {

    @GET("verifyowner.php")
    Single<Verification> getVerficationDetails(@Query("phonenumber") String phonenumber);

    @GET("getMenu.php")
    Single<List<WeekMenu>> getWeekMenu(@Query("messname") String messname);

    @POST("addEnquiry.php")
    Single<isSuccess> createOwner(@Body OwnerData owner);

    @POST("addMess.php")
    Completable saveOwner(@Body OwnerData owner);

    @POST("receivestatus.php")
    Single<WeekStatus> fetchWeekStatus(@Body HashMap<String,String> hashMap);

    @GET("try.php")
    Single<isSuccess> getServerDate();

    @POST("postinsertmenu.php")
    Completable postMenu(@Body MessMenu messMenu);

    @POST("postOffer.php")
    Completable postOffer(@Body Offer offer);

    @GET("getStats.php")
    Single<Statistics> getStatistics(@Query("messid") String messid);

}
