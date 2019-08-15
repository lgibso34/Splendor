package splendor.common.util;

public class Constants {
    public enum Colors
    {
    	White, Blue, Green, Red, Black, Gold
    }

    public static Colors[] colors = Colors.values();

    public enum ProtocolActions
    {
        WithdrawCoins, BuyCard, ReserveCard, DepositCoins, AcquireNoble
    }

    public static ProtocolActions[] protocolActions = ProtocolActions.values();

    public static final int MAX_GEMS_2PLAYER = 4;
    public static final int MAX_GEMS_3PLAYER = 5;
    public static final int MAX_GEMS_DEFAULT = 7;
    public static final int MAX_GOLD_COINS = 5;
}
