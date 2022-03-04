package com.app.khavdawala.network

import app.app.patidarsaurabh.apputils.AppConstants
import com.app.khavdawala.pojo.response.*
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.POST

interface APIEndPointsInterface {

    //    @POST(AppConstants.APIEndPoints.NEWS_CATEGORY)
//    suspend fun newsCategory(
//
//    ): NewsCategoryResponse
//
//    @POST(AppConstants.APIEndPoints.GET_SHRADDHANJALI)
//    suspend fun getShraddhanjali(
//        @Body hashMap: MultipartBody
//    ): ShraddhanjaliResponse
//
    @POST(AppConstants.APIEndPoints.GET_PRODUCT)
    suspend fun getProductList(
        @Body hashMap: MultipartBody
    ): ProductListResponse

    @POST(AppConstants.APIEndPoints.GET_PRODUCT_DETAIL)
    suspend fun getProductDetail(
        @Body hashMap: MultipartBody
    ): ProductDetailResponse

    @POST(AppConstants.APIEndPoints.GET_FAV_PRODUCT)
    suspend fun getFavProductList(
        @Body hashMap: MultipartBody
    ): ProductListResponse

    @POST(AppConstants.APIEndPoints.ADD_FAV_PRODUCT)
    suspend fun addFavProduct(
        @Body hashMap: MultipartBody
    ): AddFavResponse

    @POST(AppConstants.APIEndPoints.REMOVE_FAV_PRODUCT)
    suspend fun removeFavProduct(
        @Body hashMap: MultipartBody
    ): AddFavResponse

    @POST(AppConstants.APIEndPoints.GET_CATEGORY)
    suspend fun getCategories(): CategoryResponse

    @POST(AppConstants.APIEndPoints.GET_STATIC_PAGE)
    suspend fun getStaticPage(): StaticPageResponse

    //
//    @POST(AppConstants.APIEndPoints.EMAGAZINE_AUTH)
//    suspend fun emagazineAuth(
//        @Body hashMap: MultipartBody
//    ): RegisterResponse
//
//    @POST(AppConstants.APIEndPoints.GET_VATAN_NU_GHAM)
//    suspend fun getVatanNuGham(
//        @Body hashMap: MultipartBody
//    ): VatanNuGhamResponse
//
//    @POST(AppConstants.APIEndPoints.GET_PARIVAR_DARSHAN)
//    suspend fun getParivarDarshan(
//        @Body hashMap: MultipartBody
//    ): ParivarDarshanResponse
//
//    @POST(AppConstants.APIEndPoints.GET_UMIYA_TEMPLE)
//    suspend fun getUmiyaTemple(
//        @Body hashMap: MultipartBody
//    ): UmiyaTempleResponse
//
//    @POST(AppConstants.APIEndPoints.GET_SAMAJIK_SANSTHA)
//    suspend fun getSamajikSanstha(
//        @Body hashMap: MultipartBody
//    ): SamajikSansthaResponse
//
//    @POST(AppConstants.APIEndPoints.GET_PATIDAR_SAMAJ)
//    suspend fun getPatidarSamaj(
//        @Body hashMap: MultipartBody
//    ): PatidarSamajResponse
//
//    @POST(AppConstants.APIEndPoints.GET_GUJARATI_SAMAJ)
//    suspend fun getGujaratiSamaj(
//        @Body hashMap: MultipartBody
//    ): GujratiSamajResponse
//
//    @POST(AppConstants.APIEndPoints.GET_GUJARATI_SAMAJ)
//    suspend fun getGujaratiSamaj(): GujratiSamajResponse
//
//    @POST(AppConstants.APIEndPoints.INQUIRY)
//    suspend fun inquiry(
//        @Body hashMap: MultipartBody
//    ): RegisterResponse
//
//    @POST(AppConstants.APIEndPoints.GET_VATAN_NU_GHAM_DETAIL)
//    suspend fun getVatanNuGhamDetail(
//        @Body hashMap: MultipartBody
//    ): VatanNuGhamDetailResponse
//
//    @POST(AppConstants.APIEndPoints.GET_PARIVAR_DARSHAN_DETAIL)
//    suspend fun getParivarDarshanDetail(
//        @Body hashMap: MultipartBody
//    ): ParivarDarshanDetailResponse
//
//    @POST(AppConstants.APIEndPoints.GET_UMIYA_TEMPLE_DETAIL)
//    suspend fun getUmiyaTempleDetail(
//        @Body hashMap: MultipartBody
//    ): UmiyaTempleDetailResponse
//
//    @POST(AppConstants.APIEndPoints.GET_SAMAJIK_SANSTHA_DETAIL)
//    suspend fun getSamajikSansthaDetail(
//        @Body hashMap: MultipartBody
//    ): SamajikSansthaDetailResponse
//
//    @POST(AppConstants.APIEndPoints.GET_PATIDAR_SAMAJ_DETAIL)
//    suspend fun getPatidarSamajDetail(
//        @Body hashMap: MultipartBody
//    ): PatidarSamajDetailResponse
//
//    @POST(AppConstants.APIEndPoints.GET_OPINION_POLL)
//    suspend fun getOpinionPoll(
//        @Body hashMap: MultipartBody
//    ): OpinionPollResponse
//
//    @POST(AppConstants.APIEndPoints.GET_LAVAJAM_PLAN)
//    suspend fun getLavajamPlan(
//
//    ): LavajamPlanListResponse
//
//    @POST(AppConstants.APIEndPoints.ADD_OPINION_POLL)
//    suspend fun addOpinionPoll(
//        @Body hashMap: MultipartBody
//    ): OpinionPollAnswerResponse
//
//    @POST(AppConstants.APIEndPoints.ADD_LAVAJAM)
//    suspend fun addLavajam(
//        @Body hashMap: MultipartBody
//    ): AddLavajamResponse
//
//    @POST(AppConstants.APIEndPoints.ADVERTISE_PAYMENT)
//    suspend fun addAdvertisement(
//        @Body hashMap: MultipartBody
//    ): AdvertisementResponse
//
//    //
////
////    @POST(AppConstants.APIEndPoints.ADD_ADDRESS)
////    suspend fun addAddress(
////        @Body addressRequestModel: AddressRequestModel
////    ): AddressResponseModel
////
////    @POST(AppConstants.APIEndPoints.UPDATE_ADDRESS)
////    suspend fun updateAddress(
////        @Body updatearessRequestModel: UpdatearessRequestModel
////    ): AddressResponseModel
////
////
    //multipart/form-data
    @POST(AppConstants.APIEndPoints.REGISTRATION)
    suspend fun registration(
        @Body hashMap: MultipartBody
    ): RegisterResponse
//
//    //multipart/form-data
//    @POST(AppConstants.APIEndPoints.GET_PRATINIDHI)
//    suspend fun getPratinidhi(
//        @Body hashMap: MultipartBody
//    ): PratinidhiResponse
//
//    //multipart/form-data
//    @POST(AppConstants.APIEndPoints.GET_NEWS_PORTAL)
//    suspend fun getNewsPortal(
//        @Body hashMap: MultipartBody
//    ): NewsPortalResponse
//
//    //multipart/form-data
//    @POST(AppConstants.APIEndPoints.GET_STATIC_PAGE)
//    suspend fun getStaticPage(
//
//    ): StaticPageResponse
//
//    //multipart/form-data
//    @POST(AppConstants.APIEndPoints.GET_CONTACT_US)
//    suspend fun getContactUs(
//
//    ): ContactUsResponse
//
//    //multipart/form-data
//    @POST(AppConstants.APIEndPoints.GET_NEWS)
//    suspend fun getNews(
//        @Body hashMap: MultipartBody
//    ): NewsResponse
//
//    //multipart/form-data
//    @POST(AppConstants.APIEndPoints.GET_NEWS_DETAIL)
//    suspend fun getNewsDetail(
//        @Body hashMap: MultipartBody
//    ): NewsDetailResponse
//
//    //multipart/form-data
//    @POST(AppConstants.APIEndPoints.ADD_NEWS_LIKE)
//    suspend fun addNewsLike(
//        @Body hashMap: MultipartBody
//    ): RegisterResponse
//
//    //multipart/form-data
//    @POST(AppConstants.APIEndPoints.REMOVE_NEWS_LIKE)
//    suspend fun removeNewsLike(
//        @Body hashMap: MultipartBody
//    ): RegisterResponse

//
//    //multipart/form-data
//    @POST(AppConstants.APIEndPoints.ADD_GUEST_USER)
//    suspend fun addGuestUser(
//        @Body hashMap: MultipartBody
//    ): GuestLoginResponseModel
//
//    //multipart/form-data
//    @POST(AppConstants.APIEndPoints.VERIFY_USER)
//    suspend fun verifyUser(
//        @Body hashMap: MultipartBody
//    ): SignupResponseModel
//
//    //multipart/form-data
//    @POST(AppConstants.APIEndPoints.IS_USER_EXIST)
//    suspend fun isUserExist(
//        @Body hashMap: MultipartBody
//    ): SignupResponseModel
//
//    //multipart/form-data
//    @POST(AppConstants.APIEndPoints.RESET_PASSWORD)
//    suspend fun resetPassword(
//        @Body hashMap: MultipartBody
//    ): CommonResponseModel
//
//    @GET(AppConstants.APIEndPoints.HOME_CONTENT)
//    suspend fun homeContent(): HomeContentModel
//
//    @GET(AppConstants.APIEndPoints.LOGOUT_USER)
//    suspend fun logoutUser(): CommonResponseModel
//
//    @POST(AppConstants.APIEndPoints.CAT_WISE_PRODUCTS)
//    suspend fun catWiseProducts(
//        @Body hashMap: MultipartBody
//    ): CatWiseProductListModel
//
//    @POST(AppConstants.APIEndPoints.CART_PRODUCT_LIST)
//    suspend fun cartProductList(
//        @Body cartProductListRequest: CartProductListRequest
//    ): CatWiseProductListModel
//
//    @POST(AppConstants.APIEndPoints.PLACE_ORDER)
//    suspend fun placeOrder(
//        @Body placeOrderRequestModel: PlaceOrderRequestModel
//    ): CommonResponseModel
//
//    @POST(AppConstants.APIEndPoints.SEARCH_PRODUCT_LIST)
//    suspend fun searchProductList(
//        @Body hashMap: MultipartBody
//    ): CatWiseProductListModel
//
//    @POST(AppConstants.APIEndPoints.ORDER_LIST)
//    suspend fun orderList(
//        @Body hashMap: MultipartBody
//    ): OrderListModel
//
//    @POST(AppConstants.APIEndPoints.SEARCH_PRODUCT)
//    suspend fun searchProduct(
//        @Body searchProductRequestModel: SearchProductRequestModel
//    ): CatWiseProductListModel
}