package splendor.common.util;

public class Constants {
    public enum Colors
    {
    	White, Blue, Green, Red, Black, Gold
    }

    public static final Colors[] colors = Colors.values();
    public static final String[] shortColors = { "w", "b", "g", "r", "k", "o"};

    public enum ProtocolActions
    {
        WithdrawCoins, BuyCard, ReserveCard, DepositCoins, AcquireNoble
    }

    public static final ProtocolActions[] protocolActions = ProtocolActions.values();

    public static final int MAX_GEMS_2PLAYER = 4;
    public static final int MAX_GEMS_3PLAYER = 5;
    public static final int MAX_GEMS_DEFAULT = 7;
    public static final int MAX_GOLD_COINS = 5;
}
