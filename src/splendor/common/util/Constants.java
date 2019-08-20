package splendor.common.util;

public class Constants {
    public enum Color {
        White("w"), Blue("b"), Green("g"), Red("r"), Black("k"), Gold("o");

        private final String shortName;

        Color(String shortName) {
            this.shortName = shortName;
        }

        public static final Color[] colors = Color.values();

        public String getShortName() {
            return this.shortName;
        }

        public static Color fromShortName(String text) {
            for (Color c : Color.values()) {
                if (c.shortName.equalsIgnoreCase(text)) {
                    return c;
                }
            }
            return null;
        }
    }

    public enum ProtocolAction {
        WithdrawCoins, BuyCard, ReserveCard, DepositCoins, AcquireNoble;

        public static final ProtocolAction[] protocolActions = ProtocolAction.values();
    }

    public static final int MAX_GEMS_2PLAYER = 4;
    public static final int MAX_GEMS_3PLAYER = 5;
    public static final int MAX_GEMS_DEFAULT = 7;
    public static final int MAX_GOLD_COINS = 5;
}
