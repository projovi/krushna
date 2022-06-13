package com.krushna.veginew.retrofit;

import com.krushna.veginew.models.AeraRoot;
import com.krushna.veginew.models.ApplyCouponRoot;
import com.krushna.veginew.models.CategoryRoot;
import com.krushna.veginew.models.CityRoot;
import com.krushna.veginew.models.ComplainRoot;
import com.krushna.veginew.models.CouponRoot;
import com.krushna.veginew.models.DeliveryAddressListRoot;
import com.krushna.veginew.models.DeliveryAddressRoot;
import com.krushna.veginew.models.FaqRoot;
import com.krushna.veginew.models.HomePageRoot;
import com.krushna.veginew.models.NotificationRoot;
import com.krushna.veginew.models.OrderDetailRoot;
import com.krushna.veginew.models.OrderNotificationRoot;
import com.krushna.veginew.models.OrdersListRoot;
import com.krushna.veginew.models.ProductRoot;
import com.krushna.veginew.models.RatingReviewListRoot;
import com.krushna.veginew.models.RestResponse;
import com.krushna.veginew.models.SearchProductRoot;
import com.krushna.veginew.models.SettingRoot;
import com.krushna.veginew.models.UserRoot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface RetrofitService {
    @POST("Order/getSettingData")
    Single<SettingRoot> getSetting(@Header("apikey") String key);

    @FormUrlEncoded
    @POST("register")
    Single<UserRoot> registerUser1(@Header("apikey") String key,
                                   @Field("identity") String identity,
                                   @Field("email") String email,
                                   @Field("lastname") String lastName,
                                   @Field("firstname") String firstName,
                                   @Field("login_type") String loginType,
                                   @Field("device_type") String deviceType,
                                   @Field("device_token") String deviceToken);

    @Multipart
    @POST("updateProfile")
    Single<UserRoot> updateUser(@Header("apikey") String key,
                                @Header("token") String token,
                                @PartMap Map<String, RequestBody> partMap,
                                @Part MultipartBody.Part requestBody);

    @POST("logout")
    Single<RestResponse> logout(@Header("apikey") String key);

    @POST("Product/homePage")
    Single<HomePageRoot> getHomePageData(@Header("apikey") String key);


    @POST("Product/getCategoryList")
    Single<CategoryRoot> getCategories(@Header("apikey") String key);

    @POST("getFaqList")
    Single<FaqRoot> getFaqs(@Header("apikey") String key);


    @FormUrlEncoded
    @POST("Product/getProductById")
    Single<ProductRoot> getProductDetails(@Header("apikey") String key, @Field("product_id") String pid);

    @FormUrlEncoded
    @POST("Product/searchProduct")
    Single<SearchProductRoot> searchProduct(@Header("apikey") String key,
                                            @FieldMap HashMap<String, String> data,
                                            @Field("start") int start, @Field("count") int count);

    @FormUrlEncoded
    @POST("addAddress")
    Single<DeliveryAddressRoot> addAddress(@Header("apikey") String key,
                                           @Header("token") String token,
                                           @FieldMap HashMap<String, String> data);

    @FormUrlEncoded
    @POST("updateAddress")
    Single<DeliveryAddressRoot> updateAddress(@Header("apikey") String key,
                                              @Header("token") String token,
                                              @FieldMap HashMap<String, String> data);

    @POST("getCityList")
    Single<CityRoot> getCities(@Header("apikey") String key);

    @FormUrlEncoded
    @POST("getAreaByCity")
    Single<AeraRoot> getAreaByCity(@Header("apikey") String key, @Field("city_id") int cityId);

    @POST("getAllDeliveryAddress")
    Single<DeliveryAddressListRoot> getAllDeliveryAddress(@Header("apikey") String key, @Header("token") String token);

    @FormUrlEncoded
    @POST("deleteAddress")
    Single<RestResponse> deleteAddress(@Header("apikey") String key, @Header("token") String token, @Field("address_id") int addressId);


    @FormUrlEncoded
    @POST("Order/getAllOrderRating")
    Single<RatingReviewListRoot> getAllRating(@Header("apikey") String key, @Header("token") String token
            , @Field("start") int start, @Field("count") int count);

    @POST("Order/getCoupon")
    Single<CouponRoot> getCoupons(@Header("apikey") String key, @Header("token") String token);


    @FormUrlEncoded
    @POST("Order/placeOrder")
    Single<RestResponse> placeOrder(@Header("apikey") String key,
                                    @Header("token") String userToken,
                                    @Field("quantity[]") List<String> quantities,
                                    @Field("shipping_charge") String shippingCharge,
                                    @Field("total_amount") String totalAmount,
                                    @Field("payment_type") String paymentType,
                                    @Field("payment_id") String paymentId,
                                    @Field("payment_name") String paymentName,
                                    @Field("address_id") String addressId,
                                    @Field("coupon_discount") String couponDiscount,
                                    @Field("latitude") String lat,
                                    @Field("longitude") String lang,
                                    @Field("subtotal") String subtotal,
                                    @Field("price[]") List<String> prices,
                                    @Field("product_name[]") List<String> productNames,
                                    @Field("image[]") List<String> images,
                                    @Field("price_unit[]") List<String> price_units,
                                    @Field("product_id[]") List<String> product_id,
                                    @Field("price_unit_name[]") List<String> price_unit_names,
                                    @Field("total_price[]") List<String> total_prices);

    @FormUrlEncoded
    @POST("Order/checkProduct")
    Single<RestResponse> checkProduct(@Header("apikey") String key,
                                      @Header("token") String userToken,
                                      @Field("product_id[]") List<String> quantities
    );


    @FormUrlEncoded
    @POST("Order/raiseComplaint")
    Single<RestResponse> raiseComplaint(@Header("apikey") String key,
                                        @Header("token") String userToken,
                                        @Field("order_id") String orderId,
                                        @Field("mobile_no") String mobile,
                                        @Field("title") String title,
                                        @Field("description") String description);

    @FormUrlEncoded
    @POST("Order/getAllComplaint")
    Single<ComplainRoot> getMyComplains(@Header("apikey") String key, @Header("token") String token, @Field("type") int type,
                                        @Field("start") int start, @Field("count") int count);

    @FormUrlEncoded
    @POST("Product/getAllNotification")
    Single<NotificationRoot> getNotifications(@Header("apikey") String key, @Header("token") String token
            , @Field("start") int start, @Field("count") int count);

    @FormUrlEncoded
    @POST("Product/getAllUserNotification")
    Single<OrderNotificationRoot> getOrderNotification(@Header("apikey") String key, @Header("token") String token
            , @Field("start") int start, @Field("count") int count);

    @FormUrlEncoded
    @POST("Order/getMyOrderList")
    Single<OrdersListRoot> getMyOrders(@Header("apikey") String key, @Header("token") String token,
                                       @Field("start") int start, @Field("count") int count);


    @FormUrlEncoded
    @POST("Order/getOrderDetailsById")
    Single<OrderDetailRoot> getOrderDetail(@Header("apikey") String key, @Header("token") String token,
                                           @Field("order_id") String orderId);

    @FormUrlEncoded
    @POST("Order/cancelledOrder")
    Single<RestResponse> cancelOrder(@Header("apikey") String key,
                                     @Header("token") String userToken,
                                     @Field("order_id") String orderId);

    @FormUrlEncoded
    @POST("Order/productReviewRating")
    Single<RestResponse> addRating(@Header("apikey") String key,
                                   @Header("token") String userToken,
                                   @Field("order_id") String orderId,
                                   @Field("rating") String rating,
                                   @Field("review") String review);

    @FormUrlEncoded
    @POST("Order/applyCoupan")
    Single<ApplyCouponRoot> applyCoupon(@Header("apikey") String key,
                                        @Header("token") String userToken,
                                        @Field("coupon_id") String orderId,
                                        @Field("totalamount") String totalAmount);
}

