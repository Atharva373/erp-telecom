package com.atharva.erp_telecom.crm.enums;

public enum IndianState {

    // States
    ANDHRA_PRADESH("37", "Andhra Pradesh", "AP"),
    ARUNACHAL_PRADESH("12", "Arunachal Pradesh", "AR"),
    ASSAM("18", "Assam", "AS"),
    BIHAR("10", "Bihar", "BR"),
    CHHATTISGARH("22", "Chhattisgarh", "CG"),
    GOA("30", "Goa", "GA"),
    GUJARAT("24", "Gujarat", "GJ"),
    HARYANA("06", "Haryana", "HR"),
    HIMACHAL_PRADESH("02", "Himachal Pradesh", "HP"),
    JHARKHAND("20", "Jharkhand", "JH"),
    KARNATAKA("29", "Karnataka", "KA"),
    KERALA("32", "Kerala", "KL"),
    MADHYA_PRADESH("23", "Madhya Pradesh", "MP"),
    MAHARASHTRA("27", "Maharashtra", "MH"),
    MANIPUR("14", "Manipur", "MN"),
    MEGHALAYA("17", "Meghalaya", "ML"),
    MIZORAM("15", "Mizoram", "MZ"),
    NAGALAND("13", "Nagaland", "NL"),
    ODISHA("21", "Odisha", "OD"),
    PUNJAB("03", "Punjab", "PB"),
    RAJASTHAN("08", "Rajasthan", "RJ"),
    SIKKIM("11", "Sikkim", "SK"),
    TAMIL_NADU("33", "Tamil Nadu", "TN"),
    TELANGANA("36", "Telangana", "TG"),
    TRIPURA("16", "Tripura", "TR"),
    UTTAR_PRADESH("09", "Uttar Pradesh", "UP"),
    UTTARAKHAND("05", "Uttarakhand", "UK"),
    WEST_BENGAL("19", "West Bengal", "WB"),
    // Union Territories
    ANDAMAN_AND_NICOBAR_ISLANDS("35", "Andaman and Nicobar Islands", "AN"),
    CHANDIGARH("04", "Chandigarh", "CH"),
    DADRA_AND_NAGAR_HAVELI_AND_DAMAN_AND_DIU("26", "Dadra and Nagar Haveli and Daman and Diu", "DD"),
    DELHI("07", "Delhi", "DL"),
    JAMMU_AND_KASHMIR("01", "Jammu and Kashmir", "JK"),
    LADAKH("38", "Ladakh", "LA"),
    LAKSHADWEEP("31", "Lakshadweep", "LD"),
    PUDUCHERRY("34", "Puducherry", "PY");

    private final String code;
    private final String name;
    private final String abbreviation;

    IndianState(String code, String name, String abbreviation) {
        this.code = code;
        this.name = name;
        this.abbreviation = abbreviation;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getAbbreviation(){
        return abbreviation;
    }

    public static IndianState fromCode(String code) {
        for (IndianState state : values()) {
            if (state.getCode().equals(code)) {
                return state;
            }
        }
        return null;
    }
    public static IndianState fromAbbreviation(String abbreviation) {
        for (IndianState state : values()) {
            if (state.getAbbreviation().equals(abbreviation)) {
                return state;
            }
        }
        return null;
    }

}
