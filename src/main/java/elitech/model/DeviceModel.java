package elitech.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines Elitech Device Models and their corresponding VID, PID, Product IDs and naming attributes.
 */
public enum DeviceModel {
    RC_51(51, 0x04d8, 0x0033, "RC-51", "EL"),
    RC_51H(307, 0x04d8, 0x0133, "RC-51H", "EL"),
    RC_5_PLUS(12293, 0x04d8, 0x3005, "RC-5+", "EFI"),
    RC_55(55, 0x04d8, 0x0037, "RC-55", "EL"),
    TEMLOG_20(4116, 0x04d8, 0x1014, "TemLog 20", "TM1"),
    TEMLOG_20H(4372, 0x04d8, 0x1114, "TemLog 20H", "HM1"),
    RC_17(17, 0x04d8, -1, "RC-17", "EFL"),
    RC_17N(4113, 0x04d8, -1, "RC-17N", "EFM"),
    RC_18(18, 0x04d8, 0x0012, "RC-18", "EF1"),
    RC_19(19, 0x04d8, 0x0013, "RC-19", "EF2"),
    RC_19N(20, 0x04d8, -1, "RC-19N", "EFS"),
    TEMLOG_ST5(4101, 0x04d8, 0x1005, "TemLog ST5", "TM"),
    MSL_51(8243, 0x04d8, 0x2033, "MSL-51", "M03"),
    MSL_51H(8499, 0x04d8, 0x2133, "MSL-51H", "M04"),
    GSP_8A(53256, -1, -1, "GSP-8A", "CMA"),
    LOGET_1(1, 0x0416, 0x0001, "LogEt 1", "EML"),
    LOGET_1TH(257, 0x0416, 0x0101, "LogEt 1TH", "EMN"),
    LOGET_1BIO(513, 0x0416, 0x0201, "LogEt 1Bio", "EMM"),
    LOGET_6(12294, 0x0416, 0x3006, "LogEt 6", "EF3"),
    LOGET_6_PTE(13318, -1, -1, "LogEt 6 PTE", "EMX"),
    LOGET_8_LIFE_SCIENCE(17160, 0x0416, 0x4308, "LogEt 8 Life Science", "EM2"),
    LOGET_8(16392, 0x0416, 0x4008, "LogEt 8", "EM2"),
    LOGET_8_FOOD(12296, 0x0416, 0x3008, "LogEt 8 Food", "EF4"),
    LOGET_8_TH(16904, -1, -1, "LogEt 8 TH", "EM5"),
    LOGET_8_TE(16648, -1, -1, "LogEt 8 TE", "EM6"),
    LOGET_8_GLE(18184, -1, -1, "LogEt 8 GLE", "EMW"),
    LOGET_8_THE(17672, -1, -1, "LogEt 8 THE", "EMQ"),
    LOGET_8_BLE(13064, -1, -1, "LogEt 8 BLE", "EF8"),
    LOGET_8_PTE(17416, -1, -1, "LogEt 8 PTE", "EMO"),
    LOGET_8_UTE(17928, -1, -1, "LogEt 8 UTE", "EMR"),
    TLOG_100(45216, -1, -1, "Tlog 100", "CM1"),
    TLOG_100H(45472, -1, -1, "Tlog 100H", "CM2"),
    TLOG_100E(45984, -1, -1, "Tlog 100E", "CM3"),
    TLOG_100EH(46240, -1, -1, "Tlog 100EH", "CM4"),
    TLOG_100EC(46496, -1, -1, "Tlog 100EC", "CM5"),
    TLOG_100EL(46752, -1, -1, "Tlog 100EL", "CMB"),
    TLOG_100_GLE(47008, -1, -1, "Tlog 100 GLE", "CMK"),
    TLOG_B100(49313, -1, -1, "Tlog B100", "CM6"),
    TLOG_B100H(49569, -1, -1, "Tlog B100H", "CM7"),
    TLOG_B100E(50081, -1, -1, "Tlog B100E", "CM8"),
    TLOG_B100EH(50337, -1, -1, "Tlog B100EH", "CM9"),
    TLOG_B100EC(50593, -1, -1, "Tlog B100EC", "CME"),
    TLOG_B100EL(50849, -1, -1, "Tlog B100EL", "CMD"),
    RC_4(64, -1, -1, "RC-4", "EF5"),
    RC_4HC(65, -1, -1, "RC-4HC", "EF7"),
    RC_5(80, -1, -1, "RC-5", "EFE"),
    TLOG_10(45072, -1, -1, "Tlog 10", "CML"),
    TLOG_10H(45073, -1, -1, "Tlog 10H", "CMM"),
    TLOG_10E(45074, -1, -1, "Tlog 10E", "CMN"),
    TLOG_10EH(45075, -1, -1, "Tlog 10EH", "CMO"),
    ELOG_1(45088, -1, -1, "Elog 1", "EFN"),
    ELOG_1N(45089, -1, -1, "Elog 1N", "EFP"),
    LOGET_5_T(45090, -1, -1, "LogEt 5 T", "EB1"),
    LOGET_5_TH(45091, -1, -1, "LogEt 5 TH", "EB2"),
    LOGET_5_TE(45092, -1, -1, "LogEt 5 TE", "EB3"),
    LOGET_5_THE(45093, -1, -1, "LogEt 5 THE", "EB4"),
    LOGET_5_TLE(45094, -1, -1, "LogEt 5 TLE", "EB5");

    private final int modelValue;
    private final int vendorId;
    private final int productId;
    private final String modelDesc;
    private final String initial;

    DeviceModel(int modelValue, int vendorId, int productId, String modelDesc, String initial) {
        this.modelValue = modelValue;
        this.vendorId = vendorId;
        this.productId = productId;
        this.modelDesc = modelDesc;
        this.initial = initial;
    }

    public int getModelValue() {
        return modelValue;
    }

    public int getVendorId() {
        return vendorId;
    }

    public int getProductId() {
        return productId;
    }

    public String getModelDesc() {
        return modelDesc;
    }

    public String getInitial() {
        return initial;
    }

    private static final Map<Integer, DeviceModel> VALUE_MAP = new HashMap<>();
    static {
        for (DeviceModel m : values()) {
            VALUE_MAP.put(m.modelValue, m);
        }
    }

    public static DeviceModel fromValue(int value) {
        return VALUE_MAP.getOrDefault(value, null);
    }
}
