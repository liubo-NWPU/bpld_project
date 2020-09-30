package testutil;

public enum EnumUtil {
    //可以以逗号间隔
    FILE_OFFICE_EXCEL_NAME("XS_DHD.xlsx"),
    FILE_OFFICE_EXCEL_PATH("E:\\aa"),//office 的 excel文件
    COMMON_ENCODING("utf-8"),
    FILE_TXT_PATH("E:\\aa\\a.txt");


    private String temStr;
    private EnumUtil(String temStr){
        this.temStr=temStr;
    }
    @Override
    public String toString() {
        return String.valueOf (this.temStr);
    }
}
