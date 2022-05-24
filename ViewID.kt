data class AccountSelect(var name:String,var issued:Boolean = false) : IViewId {
    override val ios
        get() = "account_$name"
    override val iosMatchType = IosMatchType.ID
    override val android
        get() = "account_$name"
    override val androidMatchType = AndroidMatchType.DESCRIPTION
}
object ViewId {

    enum class DefaultView(
        override val ios:String,
        override val iosMatchType: IosMatchType,
        override val android:String,
        override val androidMatchType: AndroidMatchType
    ) : IViewId
    {
        SCROLL(
            "Scroll",
            IosMatchType.ID,
            "recyclerView",
            AndroidMatchType.ID
        ),
        }
    enum class Option(
        override val ios:String,
        override val iosMatchType: IosMatchType,
        override val android:String,
        override val androidMatchType: AndroidMatchType
    ) : IViewId
    {
        SEND(
            "01",
            IosMatchType.ID,
            "Sent",
            AndroidMatchType.DESCRIPTION
        ),
        FAVOURITES(
            "FavAction",
            IosMatchType.ID,
            "AddFavorite",
            AndroidMatchType.DESCRIPTION
        ),
        }
    enum class SendFlow(
        override val ios:String,
        override val iosMatchType: IosMatchType,
        override val android:String,
        override val androidMatchType: AndroidMatchType
    ) : IViewId
    {
        SEND_PAYMENT(
            "CONTINUE",
            IosMatchType.ID,
            "ButtonPayment",
            AndroidMatchType.DESCRIPTION
        ),
        CONFIRM_TRANSACTION_BUTTON(
            "",
            IosMatchType.ID,
            "contiBUTTON",
            AndroidMatchType.ID
        ),
        INPUT_NAME(
            "favoriteName",
            IosMatchType.ID,
            "input_name",
            AndroidMatchType.DESCRIPTION
        ),    
    }        
}       